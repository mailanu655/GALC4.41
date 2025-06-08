package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;
import java.util.Set;

import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingTrailerStatusDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.service.GenericDaoService;

/**
 * 
 * <h3>PruneProductTask Class description</h3>
 * <p> PruneProductTask description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 30, 2012
 *
 *
 */
public class PruneEngineTask extends PruneByProdutionLotTask{

	
	public PruneEngineTask(String name) {
		super(name,"GAL131TBX","Engine");
	}

	@Override
	public List<ProductionLot> getFinishedProductionLots() {
		return getService(GenericDaoService.class).
			findFinishedEngineProdLots(getProcessLocation(), getShippingPPID(), getStartProductionDate());
	}

	@Override
	public void pruneProductSpecificResults(ProductionLot productionLot) {
		for(TableDefinitions.TableDefinition table : TableDefinitions.engineTables.getTables()) 
			deleteProductionResultByLot(productionLot, table.getTableName(), table.getProductIdName(), table.getTableDescription());
	}
	
	@Override
	public void pruneProductionLot(ProductionLot productionLot) {
		
		deleteProductionResultByProductionDate("VANNING_SCHEDULE_TBX", "Vanning Schedule");
		deleteProductionResultByProductionDate("GAL260TBX", "QICS Station Result");

		super.pruneProductionLot(productionLot);
	}
	
	@Override
	protected void pruneByProductionDate() {
		int count; 
		count = getDao(BlockLoadDao.class).deleteAllByProductionDate(startProductionDate);
		putCount("BLOCK_LOAD_TBX",count);
		logger.info("deleted " + count + " BLOCK_LOAD_TBX "+" data before production date " + startProductionDate);
		
		count = getDao(PartLotDao.class).deleteAllByProductionDate(startProductionDate);
		putCount("PART_LOT_TBX",count);
		logger.info("deleted " + count + " PART_LOT_TBX "+" data before production date " + startProductionDate);

		deleteProductionResultByProductionDate("GAL260TBX", "QICS Station Result");

		super.pruneByProductionDate();
	}

	@Override
	public void postPruneByProductionLot() {
		
		int count;
		
		List<ShippingTrailerInfo> trailers = getDao(ShippingTrailerInfoDao.class).findAllFinishedTrailers();
		
		for(ShippingTrailerInfo trailer : trailers) {
			
			count = getDao(ShippingQuorumDetailDao.class).deleteAllByTrailerId(trailer.getTrailerId());
			putCount("QUORUM_DETAIL_TBX",count);
			logger.info("deleted " + count + " Shipping Quorum Detail data of trailer " + trailer.getTrailerId());
			
			count = getDao(ShippingQuorumDao.class).deleteAllByTrailerId(trailer.getTrailerId());
			putCount("QUORUM_TBX",count);
			logger.info("deleted " + count + " Shipping Quorum data of trailer " + trailer.getTrailerId());
			
			getDao(ShippingTrailerStatusDao.class).removeByKey(trailer.getTrailerId());
			putCount("TRAILER_STATUS",count);
			logger.info("deleted " + count + " Shipping Trailer status data of trailer " + trailer.getTrailerId());
			
			getDao(ShippingTrailerInfoDao.class).removeByKey(trailer.getTrailerId());
			putCount("TRAILER_INFO_TBX",count);
			logger.info("deleted " + count + " Shipping Trailer Info data of trailer " + trailer.getTrailerId());

		}
	}

	@Override
	protected Set<String> getAllPrunningTables() {
		Set<String> tableNames = super.getAllPrunningTables();
		for(TableDefinitions.TableDefinition table : TableDefinitions.engineTables.getTables()) 
			tableNames.add(table.getTableName());
		tableNames.add("VANNING_SCHEDULE_TBX");
		tableNames.add("BLOCK_LOAD_TBX");
		tableNames.add("PART_LOT_TBX");
		tableNames.add("QUORUM_DETAIL_TBX");
		tableNames.add("QUORUM_TBX");
		tableNames.add("TRAILER_STATUS");
		tableNames.add("TRAILER_INFO_TBX");
		
		return tableNames;
	}


}
