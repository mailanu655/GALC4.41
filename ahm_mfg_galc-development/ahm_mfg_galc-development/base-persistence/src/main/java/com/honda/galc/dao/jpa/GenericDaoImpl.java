package com.honda.galc.dao.jpa;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>GenericDaoImpl Class description</h3>
 * <p> GenericDaoImpl description </p>
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
 *
 */
public class GenericDaoImpl extends JpaEntityManager implements GenericDaoService{
	private static final String LOG_NAME = "JPA Server"; 
	
	private static final String GET_DB2_CURRENT_TIMESTAMP = "SELECT TIMESTAMP(CURRENT_TIMESTAMP) FROM SYSIBM.SYSDUMMY1";
	
	private static final String DELETE_ALL_FINISHED_HEADS = "DELETE FROM GALADM.$TableName a " +
	    "WHERE EXISTS(SELECT * FROM GALADM.HEAD_TBX b " + 
	    			  "WHERE a.$ProductId = b.HEAD_ID AND " + 
	    			  "(b.LAST_PASSING_PROCESS_POINT_ID = 'MEAEP16601' OR b.LAST_PASSING_PROCESS_POINT_ID LIKE 'AE%' OR b.DEFECT_STATUS IN(3,4)) " + 
	    			  "AND DATE(b.UPDATE_TIMESTAMP) = ?)"; 

	private static final String SELECT_FINISHED_HEADS_BY_DATE = "SELECT HEAD_ID FROM GALADM.HEAD_TBX b " + 
		     "WHERE (b.LAST_PASSING_PROCESS_POINT_ID = 'MEAEP16601' OR b.LAST_PASSING_PROCESS_POINT_ID LIKE 'AE%' OR b.DEFECT_STATUS IN(3,4)) " + 
		      "AND DATE(b.UPDATE_TIMESTAMP) = ?1"; 

	private static final String SELECT_FINISHED_BLOCKS_BY_DATE = "SELECT BLOCK_ID FROM GALADM.BLOCK_TBX b " + 
			  "WHERE (b.LAST_PASSING_PROCESS_POINT_ID = 'MEAEP16601' OR b.LAST_PASSING_PROCESS_POINT_ID LIKE 'AE%' OR b.DEFECT_STATUS IN(3,4)) " + 
			  "AND DATE(b.UPDATE_TIMESTAMP) = ?1"; 

	private static final String DELETE_ALL_FINISHED_BLOCKS = "DELETE FROM GALADM.$TableName a " +
		"WHERE EXISTS(SELECT * FROM GALADM.BLOCK_TBX b " + 
			  		  "WHERE a.$ProductId = b.BLOCK_ID AND " +
			  		  "(b.LAST_PASSING_PROCESS_POINT_ID = 'MEAEP16601' OR b.LAST_PASSING_PROCESS_POINT_ID LIKE 'AE%' OR b.DEFECT_STATUS IN(3,4)) " + 
	    			  "AND DATE(b.UPDATE_TIMESTAMP) = ?)"; 

	private static final String DELETE_BY_PRODUCTION_LOT_PRODUCT = 
		"DELETE FROM GALADM.$TableName a " + 
		"WHERE EXISTS(SELECT * FROM GALADM.$ProductTableName b " + 
					 "WHERE a.$ProductId = b.PRODUCT_ID AND b.PRODUCTION_LOT = ?)";
	
	private static final String DELETE_PRODUCT = 
			"DELETE FROM GALADM.$TableName a WHERE a.$ProductId =?1";

	private static final String DELETE_BY_PRODUCTION_LOT_SUB_PRODUCT = 
		"DELETE FROM GALADM.$TableName a " + 
		"WHERE EXISTS(SELECT * FROM GALADM.SUB_PRODUCT_TBX b " + 
					 "WHERE a.PRODUCT_ID = b.PRODUCT_ID AND b.PRODUCTION_LOT = ?1 AND b.PRODUCT_TYPE = ?2)";

	private static final String DELETE_PRODUCTION_LOT = "DELETE FROM GALADM.$TableName a WHERE a.PRODUCTION_LOT = ?"; 

	private static final String DELETE_BY_PRODUCTION_DATE = "DELETE FROM GALADM.$TableName a WHERE a.PRODUCTION_DATE <= ?"; 

	private static final String COUNT = "SELECT COUNT(*) FROM GALADM.$TableName FOR READ ONLY";
	
	private static final String FIND_FINISHED_ENGINE_PRODUCTION_LOTS =
		"WITH FINISHED_LOT (PRODUCTION_LOT,COUNT,UPDATE_TIMESTAMP) AS (" +
			"SELECT a.PRODUCTION_LOT,COUNT(*),MAX(a.UPDATE_TIMESTAMP) AS UPDATE_TIMESTAMP FROM GALADM.GAL131TBX a, GALADM.GAL217TBX b " +  
			"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT AND b.PROCESS_LOCATION = ?1 AND " + 
			       "(a.LAST_PASSING_PROCESS_POINT_ID = ?2 OR EXISTS(SELECT * FROM GALADM.GAL136TBX WHERE PRODUCT_ID = a.PRODUCT_ID)) " +  
			"GROUP BY a.PRODUCTION_LOT) "+
		"SELECT a.* FROM GALADM.GAL217TBX a,FINISHED_LOT f " + 
		"WHERE a.PRODUCTION_LOT = f.PRODUCTION_LOT AND f.COUNT = (SELECT COUNT(*) from GALADM.GAL131TBX c " +
						  "WHERE f.PRODUCTION_LOT = c.PRODUCTION_LOT) AND DATE(f.UPDATE_TIMESTAMP) <?3 "+
		"ORDER BY f.UPDATE_TIMESTAMP WITH UR";
		
	private static final String FIND_FINISHED_FRAME_PRODUCTION_LOTS =
		"WITH FINISHED_LOT (PRODUCTION_LOT,COUNT,UPDATE_TIMESTAMP) AS (" +
			"SELECT a.PRODUCTION_LOT,COUNT(*),MAX(a.UPDATE_TIMESTAMP) AS UPDATE_TIMESTAMP FROM GALADM.GAL141TBX a, GALADM.GAL217TBX b " +  
			"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT AND b.PROCESS_LOCATION = ?1 AND " + 
			       "(a.LAST_PASSING_PROCESS_POINT_ID = ?2 OR EXISTS(SELECT * FROM GALADM.GAL136TBX WHERE PRODUCT_ID = a.PRODUCT_ID)) " +  
			"GROUP BY a.PRODUCTION_LOT) "+
		"SELECT a.* FROM GALADM.GAL217TBX a,FINISHED_LOT f " + 
		"WHERE a.PRODUCTION_LOT = f.PRODUCTION_LOT AND f.COUNT = (SELECT COUNT(*) from GALADM.GAL141TBX c " +
						  "WHERE f.PRODUCTION_LOT = c.PRODUCTION_LOT) AND DATE(f.UPDATE_TIMESTAMP) <?3 "+
		"ORDER BY f.UPDATE_TIMESTAMP FOR READ ONLY";
	
	private static final String FIND_FINISHED_SUB_PRODUCT_PRODUCTION_LOTS =
		"WITH FINISHED_LOT (PRODUCTION_LOT,COUNT,UPDATE_TIMESTAMP) AS (" +
			"SELECT a.PRODUCTION_LOT,COUNT(*),MAX(a.UPDATE_TIMESTAMP) AS UPDATE_TIMESTAMP FROM GALADM.SUB_PRODUCT_TBX a, GALADM.GAL217TBX b " +  
			"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT AND b.PROCESS_LOCATION = ?1 AND " + 
			       "(a.LAST_PASSING_PROCESS_POINT_ID IN ( $ppIDs ) OR EXISTS(SELECT * FROM GALADM.GAL136TBX WHERE PRODUCT_ID = a.PRODUCT_ID)) " +  
			"GROUP BY a.PRODUCTION_LOT) "+
		"SELECT a.* FROM GALADM.GAL217TBX a,FINISHED_LOT f " + 
		"WHERE a.PRODUCTION_LOT = f.PRODUCTION_LOT AND f.COUNT = (SELECT COUNT(*) from GALADM.SUB_PRODUCT_TBX c " +
						  "WHERE f.PRODUCTION_LOT = c.PRODUCTION_LOT) AND DATE(f.UPDATE_TIMESTAMP) <?2 "+
		"ORDER BY f.UPDATE_TIMESTAMP FOR READ ONLY";

	private static final String FIND_INTACT_SUB_PRODUCT_PRODUCTION_LOTS = 
		"SELECT DISTINCT (S.PRODUCTION_LOT) FROM GALADM.SUB_PRODUCT_TBX S, GALADM.GAL215TBX H " +
		"WHERE S.PRODUCT_TYPE=?1 and S.PRODUCT_ID = H.PRODUCT_ID and H.PROCESS_POINT_ID=?2 and DATE(H.CREATE_TIMESTAMP) < ?3 WITH CS FOR READ ONLY";


	private static final String DELETE_ALL_FINISHED_MBPN_PRODUCT = "DELETE FROM GALADM.$TableName A " +
			"WHERE EXISTS(SELECT * FROM GALADM.MBPN_PRODUCT_TBX M, GALADM.GAL215TBX H " + 
		  		  "WHERE A.PRODUCT_ID = M.PRODUCT_ID AND M.PRODUCT_ID = H.PRODUCT_ID " +
		  		  "AND H.PROCESS_POINT_ID in (@ProcessPointIds@) AND DATE(H.CREATE_TIMESTAMP) = ?1)"; 
	
	private static final String FIND_ALL_PRUNING_PRODUCT_IDS = 
		"SELECT B.PRODUCT_ID FROM GALADM.GAL215TBX A, GALADM.$TableName B " +
		"WHERE A.PRODUCT_ID = B.PRODUCT_ID AND A.PROCESS_POINT_ID in (@ProcessPointIds@) AND DATE(A.CREATE_TIMESTAMP) = ?1 WITH CS FOR READ ONLY";

	
    private static final String DELETE_ALL_MBPN_PRODUCTS="delete from MbpnProduct m where m.productId in (:pruningList)";

	private static final String DELETE_ALL_HISTORY = "delete from ProductResult h where h.id.productId in (:pruningList)";
	
	public GenericDaoImpl() {
	}	
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteAllFinishedHeads(String tableName, String productIdName,Date date) {
		return executeNativeUpdate(
				processSqlString(DELETE_ALL_FINISHED_HEADS, tableName, productIdName), 
				Parameters.with("1", date));
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteAllFinishedBlocks(String tableName, String productIdName,Date date) {
		return executeNativeUpdate(
				processSqlString(DELETE_ALL_FINISHED_BLOCKS, tableName, productIdName), 
				Parameters.with("1", date));
	}
	

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteAllFinishedProducts(String tableName, String productTableName, String productIdName,
			String productionLot) {
		return executeNativeUpdate(
				processSqlString(DELETE_BY_PRODUCTION_LOT_PRODUCT, tableName, productTableName,productIdName), 
				Parameters.with("1", productionLot));
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteProduct(String tableName, String productIdName, String productId) {
		return executeNativeUpdate(
				processSqlString(DELETE_PRODUCT, tableName, productIdName), 
				Parameters.with("1", productId));
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteAllFinishedSubProducts(String tableName, String productType,
			String productionLot) {
		return executeNativeUpdate(
				processSqlString(DELETE_BY_PRODUCTION_LOT_SUB_PRODUCT, tableName), 
				Parameters.with("1", productionLot).put("2", productType));
	}

	private String processSqlString(String sql,String tableName) {
		return sql.replace("$TableName",tableName);
	}
	
	private String processSqlString(String sql,String tableName, String productIdName) {
		return sql.replace("$TableName",tableName).replace("$ProductId", productIdName);
	}
	
	private String processSqlString(String sql,String tableName, String productTableName, String productIdName) {
		return sql.replace("$TableName",tableName).
				   replace("$ProductTableName",productTableName).
				   replace("$ProductId", productIdName);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteProductionLot(String tableName,String productionLot) {
		return executeNativeUpdate(
				processSqlString(DELETE_PRODUCTION_LOT, tableName),
				Parameters.with("1", productionLot));
	}

	/**
	 * delete table data which is earlier than "date"
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteAllByProductionDate(String tableName, Date date) {
		return executeNativeUpdate(
				processSqlString(DELETE_BY_PRODUCTION_DATE, tableName),
				Parameters.with("1", date));
	}

	public int count(String tableName) {
		return findFirstByNativeQuery(
				processSqlString(COUNT, tableName),
				null,
				Integer.class);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<ProductionLot> findFinishedEngineProdLots(String processLocation,
			String shippingPPID, Date date) {
		Parameters params = Parameters.with("1", processLocation).put("2",shippingPPID).put("3", date);
		return findAllByNativeQuery(FIND_FINISHED_ENGINE_PRODUCTION_LOTS, params, ProductionLot.class);
	}


	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<ProductionLot> findFinishedFrameProdLots(String processLocation,
			String shippingPPID, Date date) {
		Parameters params = Parameters.with("1", processLocation).put("2",shippingPPID).put("3", date);
		return findAllByNativeQuery(FIND_FINISHED_FRAME_PRODUCTION_LOTS, params, ProductionLot.class);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<ProductionLot> findFinishedSubProductProdLots(String processLocation,
			String shippingPPID, Date date) {
		Parameters params = Parameters.with("1", processLocation).put("2", date);
		return findAllByNativeQuery(
				FIND_FINISHED_SUB_PRODUCT_PRODUCTION_LOTS.replace("$ppIDs", shippingPPID),
				params, ProductionLot.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<ProductionLot> findIntactProductProdLots(String processLocation, String shippingPPID, Date date) {
		Parameters params = Parameters.with("1", processLocation).put("2", date);
		return findAllByNativeQuery(
				FIND_FINISHED_SUB_PRODUCT_PRODUCTION_LOTS.replace("$ppIDs", shippingPPID),
				params, ProductionLot.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<String> findFinishedHeads(Date date) {
		Parameters params = Parameters.with("1", date);
		return findAllByNativeQuery(SELECT_FINISHED_HEADS_BY_DATE,params, String.class); 
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<String> findFinishedBlocks(Date date) {
		Parameters params = Parameters.with("1", date);
		return findAllByNativeQuery(SELECT_FINISHED_BLOCKS_BY_DATE,params, String.class); 
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public List<ProductionLot> findIntactSubProductProdLotsForGivenDate(String trackingProcessPointId, Date date, String productType){
		Parameters params = Parameters.with("1", productType).put("2", trackingProcessPointId).put("3", date);
		return findAllByNativeQuery(FIND_INTACT_SUB_PRODUCT_PRODUCTION_LOTS, params, ProductionLot.class);
	}

	public Timestamp getCurrentDBTime() {
		return findFirstByNativeQuery(GET_DB2_CURRENT_TIMESTAMP, null,Timestamp.class);
	}

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public Date getDate() {
		return new Date(getCurrentTime()); 
	}
	
	public List executeGenericSqlSelectQuery(String sql){
		return findAllByQuery(sql);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteAllByProductionDate(String tableName, String processPointId, Date date) {
		
		return executeNativeUpdate(processSqlString(DELETE_ALL_FINISHED_MBPN_PRODUCT.replace("@ProcessPointIds@", CommonUtil.toInString(processPointId)), tableName),
				Parameters.with("1",date));
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRES_NEW)
	public int deleteMbpnProductsAndHistory(String processPointId, Date date) {

		List<String> pruningList = findAllByNativeQuery(processSqlString(FIND_ALL_PRUNING_PRODUCT_IDS.replace("@ProcessPointIds@", CommonUtil.toInString(processPointId)), "MBPN_PRODUCT_TBX"),
				Parameters.with("1", date), String.class);
		
		Parameters parms = Parameters.with("pruningList", pruningList);
		executeUpdate(DELETE_ALL_HISTORY,parms);
		executeUpdate(DELETE_ALL_MBPN_PRODUCTS, parms);
					
		return pruningList.size();
	}
	
	public Long getRowCountGenericSql(String entityName, String whereStr)
	{
		return getRowCountByQuery( entityName,  whereStr);
	}
	
	public boolean isUpdated(AuditEntry entity) {
		if (entity == null || entity.getId() == null) {
			return false;
		}
		AuditEntry found = entityManager.find(entity.getClass(), entity.getId());
		if (found == null) {
			return true;
		}
		java.sql.Timestamp dbUpdateTs = found.getUpdateTimestamp();
		if (dbUpdateTs == null) {
			return false;
		}
		if (convert(dbUpdateTs).equals(convert(entity.getUpdateTimestamp()))) {
			return false;
		} else {
			return true;
		}
	}
	
	public Date convert(Timestamp ts) {           
        long secs = ts.getTime() / 1000;
        double milsDouble = ts.getNanos() / 1000000d;
        long mils = Math.round(milsDouble);
        long timeInMils = secs * 1000 + mils;
        return new Date(timeInMils);
	  }
	
	public boolean isUpdated(List<? extends AuditEntry> entities) {
		if (entities == null || entities.isEmpty()) {
			return false;
		}
		for (AuditEntry entity : entities) {
			if (isUpdated(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public <T> T find(Class<T> entityClass, Object id) {
		if (entityClass == null || id == null) {
			return null;
		}
		T found = entityManager.find(entityClass, id);
		return found;
	}
	
	public List executeGenericSelectQuery(String query,Parameters params, Integer count)
	{
		return findAllByQuery(query,params, count);
	}
	
	public <T> T find(String queryString, Class<T> entityClass) {
		if (entityClass == null || queryString == null) {
			return null;
		}
		return findFirstByNativeQuery(queryString, null, entityClass);
	}
	
	public <T> List<T> findAll( String sql, Parameters parm, Class<T> entityClass) {
		return findAllByNativeQuery(sql, parm, entityClass);
	}
	
	@Transactional
	public void saveEntity (AuditEntry entity) {
		if(entity == null) return;
		entityManager.merge(entity);
	}


	@Transactional
	public void saveAllEntity (List<? extends AuditEntry> entities) {

		if(entities == null || entities.size() ==0) return;
		
		for(AuditEntry entity: entities) {
			entityManager.merge(entity);
			getLogger().check("Merged entity : " + entity.toString());
		}
	}

	private Logger getLogger(){
		return Logger.getLogger(LOG_NAME);
	}
}
