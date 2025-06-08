package com.honda.galc.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.product.ProductionLot;

/**
 * 
 * <h3>GenericDaoService Class description</h3>
 * <p> GenericDaoService description </p>
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
 * Jun 4, 2012
 *
 *
 */
  /*   
 * @author Gangadhararao Gadde
 * ver 2
 *
 *
 */
public interface GenericDaoService extends IService{
	
	public int deleteAllFinishedHeads(String tableName, String productIdName, Date date);
	
	public int deleteAllFinishedBlocks(String tableName, String productIdName, Date date);
	
	public int deleteAllFinishedProducts(String tableName, String productTableName, String productIdName, String productionLot);

	public int deleteAllFinishedSubProducts(String tableName, String productType, String productionLot);
	
	public int deleteProduct(String tableName, String productIdName, String productId);

	public int deleteProductionLot(String tableName,String productionLot);
	
	public int deleteAllByProductionDate(String tableName,Date date);
	
	public int count(String tableName);
	
	public List<ProductionLot> findFinishedEngineProdLots(String processLocation, String shippingPPID, Date date);
	public List<ProductionLot> findFinishedFrameProdLots(String processLocation, String shippingPPID, Date date);
	public List<ProductionLot> findFinishedSubProductProdLots(String processLocation, String shippingPPID, Date date);
	
	public List<String> findFinishedHeads(Date date);
	public List<String> findFinishedBlocks(Date date);
	/**
	 * get current server date
	 * @return
	 */
	public Date getDate();
	
	/**
	 * get current server time in MS
	 * @return
	 */
	public long getCurrentTime();
	
	/**
	 * get db2 current time in ms
	 * @return
	 */
	public Timestamp getCurrentDBTime();
	
	public List executeGenericSqlSelectQuery(String sql);

	public List<ProductionLot> findIntactSubProductProdLotsForGivenDate(String trackingProcessPointId, Date date, String productType);

	public int deleteAllByProductionDate(String tableName, String productIdName, Date date);
	public int deleteMbpnProductsAndHistory(String processPointId, Date date);
	
	public Long getRowCountGenericSql(String entityName, String whereStr);
	public boolean isUpdated(AuditEntry entity);
	public boolean isUpdated(List<? extends AuditEntry> entities);
	public <T> T find(Class<T> entityClass, Object id);
	
	public List executeGenericSelectQuery(String query,Parameters params, Integer count);
	
	public <T> T find(String queryString, Class<T> entityClass);
	public <T> List<T> findAll( String sql, Parameters parm, Class<T> entityClass);
	public void saveEntity ( AuditEntry entity);
	public void saveAllEntity (List<? extends AuditEntry> entities);
}
