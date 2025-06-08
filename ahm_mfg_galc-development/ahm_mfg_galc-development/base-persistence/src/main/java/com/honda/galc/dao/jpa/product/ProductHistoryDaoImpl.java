package com.honda.galc.dao.jpa.product;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>ProductHistoryDaoImpl Class description</h3>
 * <p> ProductHistoryDaoImpl description </p>
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
 * Apr 16, 2012
 *
 *
 */
public abstract class ProductHistoryDaoImpl<E extends ProductHistory,K> extends BaseDaoImpl<E,K> implements ProductHistoryDao<E,K> {

    public static final String DEFECT_TABLE_NAME = "qi_defect_result_tbx";
    public static final String REPAIR_TABLE_NAME = "qi_repair_result_tbx";
    public static final String PRODUCT_ID_HOLDER = "PRODUCT_ID_HOLDER";
    public static final String HISTORY_TBL_HOLDER = "HISTORY_TBL_HOLDER";
    public static final String PROCESS_POINT_IDS = "PROCESS_POINT_IDS";
    
    private static final String GET_LATEST_PRODUCT_ID = "SELECT " + PRODUCT_ID_HOLDER + " FROM " + HISTORY_TBL_HOLDER + " WHERE PROCESS_POINT_ID=?1 AND ACTUAL_TIMESTAMP=( SELECT MAX(ACTUAL_TIMESTAMP) FROM " + HISTORY_TBL_HOLDER + " WHERE PROCESS_POINT_ID=?1 )";
    
    public static final String SELECT_DEFECT_HISTORY_FOR_STATION_TMP_TBL = 
    		"with history as \n" 
    		+ "(select " + PRODUCT_ID_HOLDER + " as product_id, associate_no, actual_timestamp from  " + HISTORY_TBL_HOLDER + "  h where process_point_id = ?1 and actual_timestamp >= ?2 ) \n" 
    		+ ", connected_history as ( \n"
    		+ "    select product_id, associate_no, actual_timestamp, \n" 
    		+ "    (coalesce((select max(actual_timestamp) from history where product_id = h.product_id and actual_timestamp < h.actual_timestamp), \n" 
    		+ "    (select max(actual_timestamp) from  " + HISTORY_TBL_HOLDER + "  where " + PRODUCT_ID_HOLDER + " = h.product_id and actual_timestamp < h.actual_timestamp and process_point_id = ?1))) as previous_timestamp \n"
    		+ "    from history h \n"
    		+ ") \n"
    		+ ",defect as ( \n"
    		+ "    select product_id , create_user, actual_timestamp as create_time \n" 
    		+ "    from qi_defect_result_tbx where application_id = ?1 and actual_timestamp >= ?2 and deleted = 0 \n"  
    		+ ") \n"
    		+ ",defect_history as ( \n"
    		+ "    select ch.product_id, coalesce(d.create_user, ch.associate_no) as create_user, ch.actual_timestamp as create_time, \n"
    		+ "    case when d.product_id is not null then 1 else null end as defect \n"
    		+ "    from connected_history ch \n" 
    		+ "    left join defect d on d.product_id = ch.product_id and d.create_time <= ch.actual_timestamp and (d.create_time > ch.previous_timestamp or ch.previous_timestamp is null) \n"
    		+ ") \n";
    
    public static final String SELECT_DEFECT_HISTORY_FOR_MULTI_STATIONS_TMP_TBL = 
    		"with history as \n" 
    		+ "(select " + PRODUCT_ID_HOLDER + " as product_id, associate_no, actual_timestamp, process_point_id from  " + HISTORY_TBL_HOLDER + "  h where process_point_id in (" + PROCESS_POINT_IDS + ") and actual_timestamp >= ?1 ) \n" 
    		+ ", connected_history as ( \n"
    		+ "    select product_id, associate_no, actual_timestamp, process_point_id, \n" 
    		+ "    (coalesce((select max(actual_timestamp) from history where product_id = h.product_id and actual_timestamp < h.actual_timestamp), \n" 
    		+ "    (select max(actual_timestamp) from  " + HISTORY_TBL_HOLDER + "  where " + PRODUCT_ID_HOLDER + " = h.product_id and actual_timestamp < h.actual_timestamp and process_point_id = h.process_point_id))) as previous_timestamp \n"
    		+ "    from history h \n"
    		+ ") \n"
    		+ ",defect as ( \n"
    		+ "    select product_id , create_user, actual_timestamp as create_time, application_id as process_point_id \n" 
    		+ "    from qi_defect_result_tbx where application_id in (" + PROCESS_POINT_IDS+ ") and actual_timestamp >= ?1 and deleted = 0 \n"  
    		+ ") \n"
    		+ ",defect_history as ( \n"
    		+ "    select ch.product_id, coalesce(d.create_user, ch.associate_no) as create_user, ch.actual_timestamp as create_time, \n"
    		+ "    case when d.product_id is not null then 1 else null end as defect \n"
    		+ "    from connected_history ch \n" 
    		+ "    left join defect d on d.product_id = ch.product_id and d.create_time <= ch.actual_timestamp "
    		+ "    and (d.create_time > ch.previous_timestamp or ch.previous_timestamp is null) and ch.process_point_id = d.process_point_id \n"
    		+ ") \n";
    
    public static final String SELECT_DEFECT_HISTORY_FOR_STATION = 
    		"select product_id, create_user, case when count(defect) > 0 then to_char(count(defect)) else 'DP' end as count , create_time \n" 
    		+ "from defect_history group by product_id, create_user, create_time order by create_time desc \n";
    
    public static final String SELECT_REPAIR_HISTORY_FOR_STATION = 
    		"select product_id, create_user, to_char(count(defect)) as count, create_time \n" 
    		+ "from defect_history group by product_id, create_user, create_time order by create_time desc \n";
    
    private static final String UPDATE_ACTUAL_TIMESTAMP = "update %s e set e.id.actualTimestamp = :newActualTimestamp " +
			"where e.id.%s = :productId AND e.id.processPointId = :processPointId AND e.id.actualTimestamp = :oldActualTimestamp";
    
	private final String FIND_ALL_BY_PROCESS_POINT_IDS ="select e from %s e where e.id.processPointId in (:processPointIds) and e.id.%s in (:productIds)";
    
    public List<E> findAllByProductAndProcessPoint(
			String productId, String processPointId) {
		Parameters params = Parameters.with("id." + getProductIdName(), productId);
		params.put("id.processPointId", processPointId);
		return findAll(params,new String[]{"id.actualTimestamp"},false);
	} 
    
    public ProductHistory findMostRecentByProductAndProcessPointId(String productId, String processPointId) {
    	Parameters params = Parameters.with("id." + getProductIdName(), productId)
    			.put("id.processPointId", processPointId);
    	return findFirst(params, new String[]{"id.actualTimestamp"}, false);
    }
    
    @Override
    public List<E> findAllByProductIdAndSpecCodeAndProcessPoint(
			String productId, String specCode, String processPointId) {
		Parameters params = Parameters.with("id." + getProductIdName(), productId);
		params.put("id.processPointId", processPointId);
		params.put("productSpecCode",specCode);
		return findAll(params,new String[]{"id.actualTimestamp"},false);
	} 
    
    public List<E> findAllByProductId(String productId) {
		return findAll(Parameters.with("id." + getProductIdName(), productId),new String[]{"id.actualTimestamp"},true);
	}
    
	public List<E> findAllByProcessPoint(String processPointId, Timestamp startTime, Timestamp endTime) {
		Parameters params = Parameters.with("processPointId", processPointId).put("startTime", startTime).put("endTime", endTime);
		return findAllByQuery(getJpqlFindAllByProcessPointAndTime(), params);
	}
	
	public List<E> findAllByProductionDateAndProcessPoint(Date productionDate, String processPointId) {
		Parameters params = Parameters.with("productionDate", productionDate).put("id.processPointId", processPointId);
		return findAll(params,new String[]{"id.actualTimestamp"},false);
	}

	
	public List<E> findAllByProcessPointAndModel(String processPointId, Timestamp startTime, Timestamp endTime, String modelCode,ProductType productType) {
		Parameters params = Parameters.with("processPointId", processPointId).put("startTime", startTime).put("endTime", endTime);		
		StringBuilder sb = new StringBuilder(getJpqlFindAllByProcessPointAndTime().toString());
		if(!ProductTypeUtil.isDieCast(productType)){
		params.put("modelCode", modelCode);
		sb.append(" and TRIM(e.productSpecCode) like :modelCode ");
		}
		return findAllByQuery(sb.toString(), params);
	} 
    
	public List<E> findAllByProcessPointAndTime(String processPointId, Timestamp startTime, Timestamp endTime) {
		Parameters params = Parameters.with("processPointId", processPointId).put("startTime", startTime).put("endTime", endTime);		
		StringBuilder sb = new StringBuilder(getJpqlFindAllByProcessPointAndTime());
		return findAllByQuery(sb.toString(), params);
	}
	
	public List<E> findAllByProcessPointTimeAndDeviceId(String processPointId, String deviceId, Timestamp startTime, Timestamp endTime) {
		Parameters params = Parameters.with("processPointId", processPointId).put("deviceId", deviceId ).put("startTime", startTime).put("endTime", endTime);		
		StringBuilder sb = new StringBuilder(getJpqlFindAllByProcessPointTimeAndDeviceId());
		return findAllByQuery(sb.toString(), params);
	}
	
    public boolean hasProductHistory(String productId, String processPointId) {
		return !findAllByProductAndProcessPoint(productId,processPointId).isEmpty();
	}
    
    @Transactional
    public int updateActualTimestamp(Date actualTimestamp, K id) {
    	Parameters updateParams = Parameters.with("id.actualTimestamp", actualTimestamp);
    	Parameters whereParams = Parameters.with("id", id);
    	return update(updateParams, whereParams);
    }
    
    @Transactional
    public int updateActualTimestamp(Date newActualTimestamp, String productId, String processPointId, Timestamp oldActualTimestamp){
    	String sql = String.format(UPDATE_ACTUAL_TIMESTAMP, this.entityClass.getName(), getProductIdName());
    	
    	Parameters params = Parameters.with("newActualTimestamp", newActualTimestamp);
    	params.put("productId", productId);
    	params.put("processPointId", processPointId);
    	params.put("oldActualTimestamp", oldActualTimestamp);
    	
    	return executeUpdate(sql, params);
    }
    
	protected abstract String getProductIdName();
	protected abstract String getProductIdColumnName();
	
	protected String getJpqlFindAllByProcessPointAndTime() {
		return String.format("%s where e.id.processPointId = :processPointId and e.id.actualTimestamp >= :startTime and e.id.actualTimestamp <= :endTime", getFindAllSql());
	}
	
	protected String getJpqlFindAllByProcessPointTimeAndDeviceId() {
		return String.format("%s where e.id.processPointId = :processPointId and e.deviceId = :deviceId and e.id.actualTimestamp >= :startTime and e.id.actualTimestamp <= :endTime", getFindAllSql());
	}
	
	
	protected boolean isProductProcessed(String productId, String processPointId,
			String startTimestamp,String sql) {

		Parameters params = Parameters.with("1", productId);
		params.put("2", processPointId);
		params.put("3", startTimestamp);
		long counter = findFirstByNativeQuery(sql, params, Long.class);
		
		return counter > 0;
	}
	
    public boolean isProductProcessedOnOrAfter(String productId, String processPointId, Timestamp timestamp) {
		if (productId == null || processPointId == null || timestamp == null) {
			return false;
		}
		List<? extends ProductHistory> historyList = findAllByProductAndProcessPoint(productId, processPointId);
		if (historyList == null || historyList.isEmpty()) {
			return false;
		}
		ProductHistory latestHistory = historyList.get(0);
		if (latestHistory.getActualTimestamp() != null && !latestHistory.getActualTimestamp().before(timestamp)) {
			return true;
		}
		if (latestHistory.getUpdateTimestamp() != null && !latestHistory.getUpdateTimestamp().before(timestamp)) {
			return true;
		}
		return false;
	}

	/**
	 * Default behavior is to return true. 
	 * Override in derived class, if needed, to implement custom logic.   
	 */
	public boolean isMostRecent(String productId, Date actualTimestamp) {
		return true;
	}
	
	public String getLatestProductId(String ProcessPoint) {
		String sql = GET_LATEST_PRODUCT_ID;
		sql = sql.replaceAll(PRODUCT_ID_HOLDER, getProductIdColumnName());
		sql = sql.replaceAll(HISTORY_TBL_HOLDER, CommonUtil.getTableName(getEntityClass()));
		return findFirstByNativeQuery(sql, Parameters.with("1", ProcessPoint), String.class);
	}

	public List<QiDefectResultDto> findAllDefectHistoryByStationAndStartTime(String processPointId, Timestamp startTimestamp, int maxRecords, boolean repairStation) {
		Parameters params = Parameters.with("1", processPointId).put("2",startTimestamp);
		String tableName = CommonUtil.getTableName(getEntityClass());
		String sql = SELECT_DEFECT_HISTORY_FOR_STATION_TMP_TBL;
		if(repairStation) {
			sql = sql.replace(DEFECT_TABLE_NAME, REPAIR_TABLE_NAME);
			sql = sql + SELECT_REPAIR_HISTORY_FOR_STATION;
		} else {
			sql = sql + SELECT_DEFECT_HISTORY_FOR_STATION;
		}
		sql = sql.replaceAll(PRODUCT_ID_HOLDER, getProductIdColumnName());
		sql = sql.replaceAll(HISTORY_TBL_HOLDER, tableName);
		List<QiDefectResultDto> list = findAllByNativeQuery(sql, params, QiDefectResultDto.class,maxRecords);
		return list;
	}
	
	public List<QiDefectResultDto> findAllDefectHistoryByMultiStationsAndStartTime(String processPointIds, Timestamp startTimestamp, int maxRecords, boolean repairStation) {
		Parameters params = Parameters.with("1", startTimestamp);
		String tableName = CommonUtil.getTableName(getEntityClass());
		String sql = SELECT_DEFECT_HISTORY_FOR_MULTI_STATIONS_TMP_TBL;
		if(repairStation) {
			sql = sql.replace(DEFECT_TABLE_NAME, REPAIR_TABLE_NAME);
			sql = sql + SELECT_REPAIR_HISTORY_FOR_STATION;
		} else {
			sql = sql + SELECT_DEFECT_HISTORY_FOR_STATION;
		}
		sql = sql.replaceAll(PROCESS_POINT_IDS, processPointIds);
		sql = sql.replaceAll(PRODUCT_ID_HOLDER, getProductIdColumnName());
		sql = sql.replaceAll(HISTORY_TBL_HOLDER, tableName);
		List<QiDefectResultDto> list = findAllByNativeQuery(sql, params, QiDefectResultDto.class,maxRecords);
		return list;
	}
	
	public List<E> findAllByProcessPointIdsAndProductIds(List<String> processPoints,List<String> productIds) {
		
		String sql = String.format(FIND_ALL_BY_PROCESS_POINT_IDS, this.entityClass.getName(), getProductIdName());
		
		Parameters params = new Parameters(); 
	    params.put("processPointIds", processPoints);
	    params.put("productIds", productIds);
	    
		return findAllByQuery(sql, params);		
	}
}
