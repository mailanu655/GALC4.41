package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.entity.product.ProductionLot;
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
public class PruneKnuckleTask extends PruneByProdutionLotTask{

	
	public PruneKnuckleTask(String name) {
		super(name,"SUB_PRODUCT_TBX","Knuckle");
	}

	@Override
	public List<ProductionLot> getFinishedProductionLots() {
		return getService(GenericDaoService.class).
		findFinishedSubProductProdLots(getProcessLocation(), getShippingPPID(), getStartProductionDate());
	}

	@Override
	public void pruneProductionResults(ProductionLot productionLot) {
		
		for(TableDefinitions.TableDefinition table : TableDefinitions.knuckleTables.getTables()) {
			int count = getService(GenericDaoService.class).deleteAllFinishedSubProducts(
					table.getTableName(), "KNUCKLE", productionLot.getProductionLot());
			logger.info("deleted " + count + " " + table.getTableName() + " " + 
					 table.getTableDescription() +  " data of production lot " + productionLot.getProductionLot());
		}

	}

	@Override
	public void pruneProductSpecificResults(ProductionLot productionLot) {
		
	}
	
	@Override
	protected void pruneByProductionDate() {
		int count;
		count = getDao(PartLotDao.class).deleteAllByProductionDate(startProductionDate);
		putCount("PART_LOT_TBX",count);
		logger.info("deleted " + count + " PART_LOT_TBX "+" data before production date " + startProductionDate);
		
		super.pruneByProductionDate();
	}

	@Override
	public void pruneProductionLot(ProductionLot productionLot) {
		int count = getService(GenericDaoService.class).deleteProductionLot("SUB_PRODUCT_SHIPPING_TBX",productionLot.getProductionLot());
		putCount("SUB_PRODUCT_SHIPPING_TBX",count);
		logger.info("deleted " + count + " SUB_PRODUCT_SHIPPING_TBX Vanning Schedule of production lot - " + productionLot.getProductionLot());
	    
		super.pruneProductionLot(productionLot);
	}

	
	@Override
	public void postPruneByProductionLot() {
		
	}

	@Override
	protected Set<String> getAllPrunningTables() {
		Set<String> tableNames = new HashSet<String>();
		for(TableDefinitions.TableDefinition table : TableDefinitions.knuckleTables.getTables()) 
			tableNames.add(table.getTableName());
		tableNames.add("PART_LOT_TBX");
		
		return tableNames;
	}


}
