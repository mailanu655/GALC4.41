package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.OifServiceException;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.GenericDaoService;

/**
 * 
 * <h3>PrunningAbstractTask Class description</h3>
 * <p> PrunningAbstractTask description </p>
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
 * May 31, 2012
 *
 *
 */
public abstract class PruneByProdutionLotTask extends PrunningAbstractTask {
	
	private String productTableName;
	
	private String productTableDescription;
	
	private String subProductType;
	
	public PruneByProdutionLotTask(String name, String productTableName,String productTableDescription) {
		super(name);
		this.productTableName = productTableName;
		this.productTableDescription = productTableDescription;
	}
	
	public PruneByProdutionLotTask(String name, String productTableName,String productTableDescription, String subProductType) {
		super(name);
		this.productTableName = productTableName;
		this.productTableDescription = productTableDescription;
		this.subProductType = subProductType;
	}
	
	
	
	public String getProductTableName() {
		return productTableName;
	}

	public void setProductTableName(String productTableName) {
		this.productTableName = productTableName;
	}

	public String getSubProductType() {
		return subProductType;
	}

	public void setSubProductType(String subProductType) {
		this.subProductType = subProductType;
	}

	@Override
	public void performPrunning() {
		
		logger.info("start prunning products by production lots");
		pruneProductByProductionLot();
		logger.info("finished prunning products by production lots");
		
		logger.info("start prunning data by production date");
		pruneByProductionDate();
		logger.info("finished prunning data by production date");
		
	}
	
	@Override
	protected Set<String> getAllPrunningTables() {
		Set<String> tableNames = new HashSet<String>();
		for(TableDefinitions.TableDefinition table : TableDefinitions.productTables.getTables()) 
			tableNames.add(table.getTableName());
		
		for(TableDefinitions.TableDefinition table : TableDefinitions.productionDateTables.getTables()) 
			tableNames.add(table.getTableName());

		tableNames.add(productTableName);
		tableNames.add("GAL212TBX");
		tableNames.add("GAL217TBX");

		return tableNames;
	}
	
	@Override
	protected void checkConfiguration() {
		if(StringUtils.isEmpty(getShippingPPID()))
			throw new OifServiceException("SHIPPING_PROCESS_POINT_ID Property is not configured");
		
		if(StringUtils.isEmpty(getShippingPPID()))
			throw new OifServiceException("PROCESS_LOCATION Property is not configured");
	}
	
	public void pruneProductByProductionLot() {
		List<ProductionLot> productionLots = getFinishedProductionLots();
		logger.info("There are : "+ productionLots.size() + " finished production lots");
		
		int index = 0;
		for(ProductionLot productionLot : productionLots) {
			logger.info("Prunning production lot - " + productionLot); 
			pruneProductionResults(productionLot);
			
			pruneProductSpecificResults(productionLot);

			pruneProductionLot(productionLot);
			
			if(++index >= getLotBatchSize()) break;
		}
		logger.info("finished prunning " + index + " production lots");
		
		logger.info("starting post pruning production lots");
		postPruneByProductionLot();
		logger.info("finished post pruning production lots");
		
	}
	
	public void pruneProductionLot(ProductionLot productionLot) {
		
		deleteByProductionLot(productionLot, productTableName, productTableDescription);

		int count = getDao(PreProductionLotDao.class).delete(productionLot.getProductionLot());
		logger.info("deleted " + count + " GAL212TBX Pre Production Lot - " + productionLot.getProductionLot());
		putCount("GAL212TBX",count);
		
		deleteByProductionLot(productionLot, "GAL217TBX", "Production Lot");

	}
	
	
	protected void pruneProductionResults(ProductionLot productionLot) {
		logger.info("starting to delete the product result data of production lot " + productionLot);
		
		for(TableDefinitions.TableDefinition table : TableDefinitions.productTables.getTables())
			deleteProductionResultByLot(productionLot,table.getTableName(),table.getProductIdName(),table.getTableDescription());
	}
	
	protected void pruneByProductionDate() {

		for(TableDefinitions.TableDefinition table : TableDefinitions.productionDateTables.getTables()) 
			deleteProductionResultByProductionDate(table.getTableName(),table.getTableDescription());
		
	}
	
	protected void deleteProductionResultByLot(ProductionLot productionLot,String tableName, String productIdName,String tableDescription) {
		int count = getService(GenericDaoService.class).deleteAllFinishedProducts(
				tableName, productTableName, productIdName, productionLot.getProductionLot());
		putCount(tableName,count);
		logger.info("deleted " + count + " " + tableName + " " + 
				tableDescription +  " data of production lot " + productionLot.getProductionLot());
	}
	
	protected void deleteProductionResultByProductionDate(String tableName,String tableDescription) {
		int count = getService(GenericDaoService.class).deleteAllByProductionDate(
				tableName, startProductionDate);
		putCount(tableName,count);
		logger.info("deleted " + count + " " + tableName + " " + 
				tableDescription +  " data before production date " + startProductionDate);
	}
	
	protected void deleteByProductionLot(ProductionLot productionLot,String tableName,String tableDescription) {
		int count = getService(GenericDaoService.class).deleteProductionLot(tableName,productionLot.getProductionLot());
		putCount(tableName,count);
		logger.info("deleted " + count + " " + tableName + " " + tableDescription + "-"  + productionLot.getProductionLot());

	}
	
	/**
	 * get list of the finished production lot by BUSINESS_DAYS_TO_KEEP parameters
	 * @return
	 */
	public abstract List<ProductionLot> getFinishedProductionLots();
	
	/**
	 * prune all the child tables' record of the production Lot
	 * @param productionLot
	 */
	public abstract void pruneProductSpecificResults(ProductionLot productionLot);
	
	public abstract void postPruneByProductionLot();
	
}
