package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.HeadDao;
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
public class PruneHeadBlockTask extends PrunningAbstractTask{

	
	public PruneHeadBlockTask(String name) {
		super(name);
	}

	@Override
	public void performPrunning() {
		
		logger.info("start to prune head data");
		pruneHeadData();
		logger.info("finished to prune head data");
		
		logger.info("start to prune block data");
		pruneBlockData();
		logger.info("finished to prune block data");
		
	}
	
	private void pruneHeadData(){
		
		List<Date> dates = findHeadProductionDates();
		
		int index = 0;
		for(Date date : dates) {
			logger.info("start to prune head data of production date " + date);
			pruneHeadByProductionDate(date);
			logger.info("finish to prune head data of production date " + date);
			if(++index >= getBatchSize()) break;
		}
	}
	
	private void pruneBlockData(){
		
		List<Date> dates = findBlockProductionDates();
		
		int index = 0;
		for(Date date : dates) {
			logger.info("start to prune block data of production date " + date);
			pruneBlockByProductionDate(date);
			logger.info("finish to prune block data of production date " + date);
			if(++index >= getBatchSize()) break;
		}
	}
	
	private void pruneHeadByProductionDate(Date date) {

		List<String> heads = getService(GenericDaoService.class).findFinishedHeads(date);
		
		for(String headId: heads) {
			
			for(TableDefinitions.TableDefinition table : TableDefinitions.headTables.getTables()) {
			
				int count = getService(GenericDaoService.class).deleteProduct(
					table.getTableName(),table.getProductIdName(), headId);
				putCount(table.getTableName(), count);
				logger.info("deleted " + count + " " + table.getTableName() + " " + 
					 table.getTableDescription() + " head id - " + headId + "  production date - " + date);
			}
		}	
	}
	
	private void pruneBlockByProductionDate(Date date) {

List<String> blocks = getService(GenericDaoService.class).findFinishedBlocks(date);
		
		for(String blockId: blocks) {
			
			for(TableDefinitions.TableDefinition table : TableDefinitions.blockTables.getTables()) {
			
				int count = getService(GenericDaoService.class).deleteProduct(
					table.getTableName(),table.getProductIdName(), blockId);
				putCount(table.getTableName(), count);
				logger.info("deleted " + count + " " + table.getTableName() + " " + 
					 table.getTableDescription() + " block id - " + blockId + "  production date - " + date);
			}
		}	
	}
	
	private List<Date> findHeadProductionDates() {
		
		List<Date> allDates = getDao(HeadDao.class).findAllProductionDates();
		
		List<Date> dates = new ArrayList<Date>();
		
		for(Date date :allDates){
			if(date != null && date.before(startProductionDate)) 
				dates.add(date);
		}
		return dates;
	}
	
	private List<Date> findBlockProductionDates() {
		
		List<Date> allDates = getDao(BlockDao.class).findAllProductionDates();
		
		List<Date> dates = new ArrayList<Date>();
		
		for(Date date :allDates){
			if(date != null && date.before(startProductionDate)) 
				dates.add(date);
		}
		return dates;
	}

	@Override
	protected Set<String> getAllPrunningTables() {
		
		Set<String> tableNames = new HashSet<String>();
		
		for(TableDefinitions.TableDefinition table : TableDefinitions.headTables.getTables()) 
			tableNames.add(table.getTableName());
		
		for(TableDefinitions.TableDefinition table : TableDefinitions.blockTables.getTables()) 
			tableNames.add(table.getTableName());
			
		return tableNames;
	}

	@Override
	protected void checkConfiguration() {
		
	}

}
