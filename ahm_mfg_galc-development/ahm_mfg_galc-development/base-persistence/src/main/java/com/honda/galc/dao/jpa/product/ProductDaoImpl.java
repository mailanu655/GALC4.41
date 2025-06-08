package com.honda.galc.dao.jpa.product;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.StringUtil;


/**
 * 
 * <h3>ProductDaoImpl Class description</h3>
 * <p> ProductDaoImpl description </p>
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
 * Feb 17, 2012
 *
 *
 */
public abstract class ProductDaoImpl<E extends BaseProduct> extends BaseDaoImpl<E, String> implements ProductDao<E>{

	private static final String FIND_ALL_BY_QSR_ID = "select p from %s p, HoldResult h where h.id.productId =  p.productId and h.qsrId = :qsrId";
	private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME = "select p from %s p, ProductResult h where h.id.processPointId = :processPointId and h.id.productId = p.productId and h.id.actualTimestamp >= :startTime and h.id.actualTimestamp <= :endTime";
	private static final String RELEASE_PRODUCT_HOLD_WITH_CHECK = "update %s e set e.autoHoldStatus = 0 where e.productId = :productId and (select count(hr) from HoldResult hr where hr.id.productId = :productId and (hr.releaseFlag <> 1 or hr.releaseFlag is null)) = 0";	
	private static final String COUNT_BY_MATCHING_SN = "select count(p) from %s p where trim(p.productId) like :sn";
	private static final String FIND_ALL_BY_MATCHING_SN = "select p from %s p where trim(p.productId) like :sn order by p.productId";
	private static final String FIND_ALL_LAST_PROCESSED = "select a from %s a,ProductResult b " + 
			"where b.id.processPointId = :processPointId and a.productId = b.id.productId " +
			"order by b.id.actualTimestamp desc";

	private static final String GET_SCRAPED_EXCEPTIONAL_COUNT = "select count(p) from %s p, ProductionLot h where h.productionLot = :productionLot "+
			"and (trim(p.trackingStatus) = :scrapLineID or trim(p.trackingStatus) = :exceptionalLineID) and p.productionLot = h.productionLot";

	private static final String FIND_ALL_TRACKING_STATUS = "select distinct p.trackingStatus from %s p";
	
	private static final String FIND_ALL_BY_MATCHING_SN_NATIVE = "SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER(ORDER BY E.PRODUCT_ID) AS rn FROM %s E where TRIM(E.PRODUCT_ID) LIKE ?1) AS temp WHERE rn BETWEEN %d AND %d";
	
	private static final String FIND_ALL_BY_TRACKING_STATUS = "select e from %s e where e.trackingStatus = :trackingStatus";
	private static final String FIND_ALL_BY_TRACKING_STATUS_LIST = "SELECT e FROM %s e WHERE e.trackingStatus IN (%s)";
	private static final String FIND_ALL_BY_TRACKING_STATUS_NATIVE = "SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER(ORDER BY %s) AS rn FROM %s E where E.TRACKING_STATUS = ?1) AS temp WHERE rn BETWEEN %d AND %d";
	
	private static final String FIND_ALL_BY_PRODUCTION_LOT_NATIVE ="SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER() AS rn FROM %s E where E.PRODUCTION_LOT = ?1) AS temp WHERE rn BETWEEN %d AND %d";

	private static final String FIND_NEXT_INPROCESS_PRODUCT = "select p from %s p, InProcessProduct i where i.productId = :productId and i.nextProductId = p.productId";
	private static final String FIND_PREVIOUS_INPROCESS_PRODUCT = "select p from %s p, InProcessProduct i where i.nextProductId = :productId and i.productId = p.productId";

	public static final String DUNNAGE_FIELD_NAME = "dunnage";
	
	private static final String FIND_ALL_PROCESSED_FOR_TIME_RANGE = "SELECT a.* From %s AS a inner join galadm.GAL215TBX x on a.PRODUCT_ID = x.PRODUCT_ID where x.PROCESS_POINT_ID = ?1 and x.ACTUAL_TIMESTAMP >= ?2 and  x.ACTUAL_TIMESTAMP <= ?3";
	
	private static final String FIND_ALL_PROCESSED_BEFORE_TIME = "SELECT a.* From %s AS a inner join galadm.GAL215TBX x on a.PRODUCT_ID = x.PRODUCT_ID where x.PROCESS_POINT_ID = ?1 and x.ACTUAL_TIMESTAMP < ?2";
	
	private static final String FIND_PRODUCTION_LOT_NUMBERS_BY_SUBSTRING = "SELECT DISTINCT e.PRODUCTION_LOT FROM %s e WHERE e.PRODUCTION_LOT LIKE ?1";
	
	public E findBySn(String sn) {
		return findByKey(sn);
	}

	public E findBySn(String sn, ProductType productType) {
		E product = findBySn(sn);
		if (productType == null || product == null) {
			return product;
		}

		if(productType == ProductType.MBPN_PART) 
			return product;

		else if (productType.equals(product.getProductType())) {
			return product;
		}
		return null;
	}	

	@Transactional
	public void updateTrackingAttributes(E product){
		update(Parameters.with("lastPassingProcessPointId", product.getLastPassingProcessPointId())
				.put("trackingStatus", product.getTrackingStatus()),
				Parameters.with("productId", product.getProductId()));
	}

	/**
	 * Updates products with actual off date in addition to tracking status
	 * and last passing process point id.
	 */
	@Transactional
	public void updateProductOffPPTrackingAttributes(E product) {
		Parameters parameters = Parameters.with("lastPassingProcessPointId", product.getLastPassingProcessPointId())
				.put("trackingStatus", product.getTrackingStatus());
		if (Product.class.isAssignableFrom(product.getClass())) {
			parameters.put("actualOffDate", ((Product)product).getActualOffDate());
		}
		update(parameters, Parameters.with("productId", product.getProductId()));
	}

	@Transactional
	public void updateDefectStatus(String productId, DefectStatus status) {
		update(Parameters.with("defectStatus", status.getId()), Parameters.with("productId", productId));

	}

	@Transactional
	public void updateHoldStatus(String productId, int status) {
		update(Parameters.with("autoHoldStatus", status), Parameters.with("productId", productId));
	}


	public List<E> findAllBySN(String SN) {
		String queryString = String.format(FIND_ALL_BY_MATCHING_SN, entityClass.getName());
		Parameters params = Parameters.with("sn", "%" + SN);
		return findAllByQuery(queryString, params);
	}

	public long countByMatchingSN(String SN) {
		String queryString = String.format(COUNT_BY_MATCHING_SN, entityClass.getName());
		Parameters params = Parameters.with("sn", "%" + SN);
		return count(queryString, params);
	}

	public List<E> findPageBySN(String SN, int pageNumber, int pageSize) {
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		if (tableAnnotation == null) {
			String queryString = String.format(FIND_ALL_BY_MATCHING_SN, entityClass.getName());
			Parameters params = Parameters.with("sn", "%" + SN);
			return findAllByQuery(queryString, params, pageNumber * pageSize, pageSize);
		}
		Parameters params = Parameters.with("1", "%" + SN);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_MATCHING_SN_NATIVE, tableAnnotation.name(), (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);

	}

	public List<E> findAllBySNPP(String SN, String PP) {
		throw new UnsupportedOperationException("findAllBySNPP is not implemented for " + entityClass.getName());
	}
	
	public List<E> findByTrackingStatus(String trackingStatus) {
		return findAll(Parameters.with("trackingStatus", trackingStatus));
	}

	public List<E> findByTrackingStatus(String trackingStatus, int count) {
		return findAllByQuery(FIND_ALL_BY_TRACKING_STATUS, Parameters.with("trackingStatus", trackingStatus), count);
	}
	
	public List<E> findByTrackingStatus(List<String> trackingStatusList) {
		if (trackingStatusList == null || trackingStatusList.isEmpty()) return null;
		StringBuilder sb = new StringBuilder();
		for (String trackingStatus : trackingStatusList) {
			if (trackingStatus == null) continue;
			sb.append("\'" + trackingStatus.trim() + "\',");
		}
		return findAllByQuery(String.format(FIND_ALL_BY_TRACKING_STATUS_LIST, entityClass.getName(), sb.toString().replaceAll(",$","")));
	}

	public List<E> findPageByTrackingStatus(String trackingStatus, int pageNumber, int pageSize) {
		boolean isProduct = Product.class.isAssignableFrom(entityClass);
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		if (tableAnnotation == null) {
			String[] orderBy = isProduct ? new String[] {"productionDate"} : new String[] {"updateTimestamp","createTimestamp"};
			return findAll(Parameters.with("trackingStatus", trackingStatus), orderBy, false, pageNumber * pageSize, pageSize);
		}
		String orderBy = isProduct ? "PRODUCTION_DATE DESC" : "UPDATE_TIMESTAMP DESC, CREATE_TIMESTAMP DESC";
		Parameters params = Parameters.with("1", trackingStatus);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_TRACKING_STATUS_NATIVE, orderBy, tableAnnotation.name(), (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);
		
	}

	public long countByTrackingStatus(String trackingStatus) {
		return count(Parameters.with("trackingStatus", trackingStatus));
	}
	
	public List<E> findAllByProcessPoint(String processPointId, Timestamp startTime, Timestamp endTime) {
		Parameters params = Parameters.with("processPointId", processPointId).put("startTime", startTime).put("endTime", endTime);
		return findAllByQuery(getJpqlFindAllByProcessPointIdAndTime(), params);
	}

	public List<E> findAllByProcessPoint(String processPointId) {
		Parameters params = Parameters.with("lastPassingProcessPointId", processPointId);
		return findAll(params,new String[] {"updateTimestamp"},false);
	}



	public List<E> findAllByProcessPointAndModel(String processPointId, Timestamp startTime, Timestamp endTime, String modelCode, ProductType productType) {
		Parameters params = Parameters.with("processPointId", processPointId).put("startTime", startTime).put("endTime", endTime).put("modelCode", modelCode);
		StringBuilder sb = new StringBuilder(getJpqlFindAllByProcessPointIdAndTime().toString());
		if(ProductTypeUtil.isMbpnProduct(productType)){
			sb.append(" and TRIM(p.currentProductSpecCode) like :modelCode ");
		}
		else if(ProductTypeUtil.isDieCast(productType)){
			sb.append(" and TRIM(p.modelCode) like :modelCode ");
		}
		else{
			sb.append(" and TRIM(p.productSpecCode) like :modelCode");
		}
		return findAllByQuery(sb.toString(), params);
	}

	public List<E> findAllByQsrId(int qsrId) {
		return findAllByQuery(getJpqlFindAllByQsrId(), Parameters.with("qsrId", qsrId));
	}

	protected List<InventoryCount> toInventoryCounts(List<?> resultList) {
		List<InventoryCount> counts = new ArrayList<InventoryCount>();
		for(Object entry : resultList) {
			Object[] items = (Object[]) entry;
			InventoryCount count = new InventoryCount();
			count.count = (Integer)items[0];
			count.processPointId = (String)items[1];
			count.plant = (String) items[2];
			counts.add(count);
		}
		return counts;
	}

	protected List<InventoryCount> toInventoryCounts(List<?> resultList,List<?> holdResultList) {
		List<InventoryCount> counts = new ArrayList<InventoryCount>();
		for(Object entry : resultList) {
			Object[] items = (Object[]) entry;
			InventoryCount count = new InventoryCount();
			count.count = (Integer)items[0];
			count.processPointId = (String)items[1];
			count.plant = (String) items[2];
			for(Object holdEntry : holdResultList) {
				Object[] holdItems = (Object[]) holdEntry;
				if ((count.processPointId.equals((String)holdItems[1])) && (count.plant.equals((String)holdItems[2])))
					count.holdCount = (Integer)holdItems[0];
			}
			counts.add(count);
		}
		return counts;
	}

	/**
	 * save or update Production Lot. If the corresponding pre production lot is not created , create and update the link 
	 * @param prodLot
	 */
	protected void saveProductionLot(ProductionLot prodLot) {
		getDao(ProductionLotDao.class).save(prodLot);
		PreProductionLotDao preProductionLotDao = getDao(PreProductionLotDao.class);

		PreProductionLot preProdLot = preProductionLotDao.findByKey(prodLot.getProductionLot());

		if(preProdLot == null) {
			preProdLot = prodLot.derivePreProductionLot();
			PreProductionLot lastProductionLot = 
					preProductionLotDao.findLastLot(prodLot.getProcessLocation());
			if(lastProductionLot != null) {
				lastProductionLot.setNextProductionLot(preProdLot.getProductionLot());
				preProductionLotDao.save(lastProductionLot);
			}
		}else {
			preProdLot.setStartProductId(prodLot.getStartProductId());
		}
		preProductionLotDao.save(preProdLot);
	}

	public List<E> findAllByProductionLot(String productionLot) {
		return new ArrayList<E>();
	}

	public List<E> findAllByProductionLot(String productionLot, int count) {
		return new ArrayList<E>();
	}

	public List<E> findPageByProductionLot(String productionLot, int pageNumber, int pageSize) {
		if (!Product.class.isAssignableFrom(entityClass))
			throw new UnsupportedOperationException("findPageByProductionLot is not implemented for " + entityClass.getName());
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		if (tableAnnotation == null)
			return findAll(Parameters.with("productionLot", productionLot), pageNumber * pageSize, pageSize);
		Parameters params = Parameters.with("1", productionLot);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_PRODUCTION_LOT_NATIVE, tableAnnotation.name(), (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);
	}

	public List<E> findAllByLikeProductionLot(String productionLot) {
		return new ArrayList<E>();
	}

	public long countByProductionLot(String productionLot) {
		return count(Parameters.with("productionLot", productionLot));
	}
	
	public List<String> findProductionLotNumbersBySubstring(String prodLotSubstring){
		if (StringUtils.isBlank(prodLotSubstring)) return null;
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		Parameters params = Parameters.with("1", "%" + prodLotSubstring + "%");
		return findAllByNativeQuery(String.format(FIND_PRODUCTION_LOT_NUMBERS_BY_SUBSTRING, tableAnnotation.name()), params, String.class);
	}
	

	/**
	 * find last "count" products at the processpoint processPointId
	 * order by actualtimestamp desc
	 * @param processPointId
	 * @param count
	 * @return
	 */
	public List<E> findAllLastProcessed(String processPointId, int count){
		String jpql = String.format(FIND_ALL_LAST_PROCESSED, entityClass.getName());
		return findAllByQuery(jpql, Parameters.with("processPointId", processPointId),count);
	}

	public int createProducts(ProductionLot prodLot,String productType,String lineId,String ppId) {
		return 0;
	}


	public List<Object[]> getProductsWithinRange(String startProductId,String stopProductId)
	{
		return null;
	}

	public List<E> findAllByDunnage(String dunnageNumber) {
		// TODO Auto-generated method stub
		return new ArrayList<E>();
	}

	@Transactional
	public int updateDunnage(String productId, String dunnageNumber, int dunnageCapacity) {
		if (!ProductTypeUtil.isDunnagable(getEntityClass())) {
			return 0;
		}
		int dunnageSize = dunnageCapacity - 1;
		String jpql = getUpdateDunnageJpql();
		Parameters params = Parameters.with("productId", productId);
		params.put("dunnage", dunnageNumber);
		params.put("dunnageSize", dunnageSize);
		return executeUpdate(jpql, params);		
	}

	@Transactional
	public int updateDunnage(List<String> productId, String dunnageNumber, int dunnageCapacity) {
		if (!ProductTypeUtil.isDunnagable(getEntityClass())) {
			return 0;
		}
		int dunnageSize = dunnageCapacity - productId.size();
		String jpql = getUpdateDunnageJpql();
		Parameters params = Parameters.with("productId", productId);
		params.put("dunnage", dunnageNumber);
		params.put("dunnageSize", dunnageSize);
		return executeUpdate(jpql, params);
	}

	public int removeDunnage(String productId) {
		// TODO Auto-generated method stub
		return 0;
	}
	public long countByDunnage(String dunnage) {
		//TODO implement in subclass
		return 0;
	}

	public List<Map<String, Object>> selectDunnageInfo(String criteria, int resultsetSize) {
		//TODO implement in subclass
		return new ArrayList<Map<String, Object>>();
	}

	protected String getJpqlFindAllByProcessPointIdAndTime() {
		return String.format(FIND_ALL_BY_PROCESS_POINT_ID_AND_TIME, entityClass.getName());
	}


	@Transactional
	public int releaseHoldWithCheck(String productId) {
		String jpql = String.format(RELEASE_PRODUCT_HOLD_WITH_CHECK, entityClass.getName());
		return executeUpdate(jpql, Parameters.with("productId", productId));
	}

	protected String getJpqlFindAllByQsrId() {
		return String.format(FIND_ALL_BY_QSR_ID, entityClass.getName());
	}

	@Transactional
	protected int saveProductionAndPreProductionLot(ProductionLot prodLot) {
		getDao(ProductionLotDao.class).save(prodLot);
		PreProductionLotDao preProductionLotDao = getDao(PreProductionLotDao.class);

		PreProductionLot preProdLot = preProductionLotDao.findByKey(prodLot.getProductionLot());

		if(preProdLot == null) {
			preProdLot = prodLot.derivePreProductionLot();
			double maxSeq = preProductionLotDao.findMaxSequence(preProdLot.getPlanCode());
			preProdLot.setSequence(maxSeq + 1000);

		}else {
			preProdLot.setStartProductId(prodLot.getStartProductId());
		}
		preProductionLotDao.save(preProdLot);
		return 0;
	}

	public Integer getScrapedExceptionalCount(String productionLot, String scrapLineID, String exceptionalLineID) {
		String queryString = String.format(GET_SCRAPED_EXCEPTIONAL_COUNT, entityClass.getName());
		Parameters params = Parameters.with("productionLot", productionLot);
		params.put("scrapLineID", scrapLineID);
		params.put("exceptionalLineID", exceptionalLineID);
		Number seq = findFirstByQuery(queryString, Number.class,params);
		if (seq != null) {
			return Integer.valueOf(seq.intValue());
		}
		return null;

	}

	public boolean isProductActiveForProductionDate(String lotPrefix, String productionDate, List<String> initialProcessPointIds) {
		return false;
	}

	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		throw new UnsupportedOperationException("isProductTrackingStatusValidForProcessPoint is not implemented for " + entityClass.getName());
	}

	public List<String> findAllTrackingStatus() {
		String queryString = String.format(FIND_ALL_TRACKING_STATUS, entityClass.getName());
		return findByQuery(queryString, String.class);
	}

	private static final String FIND_ALL_TRACKING_STATUS_BY_PLANT = "select distinct p.trackingStatus from %s p,Line l where p.trackingStatus=l.lineId and l.plantName='%s'";
	public List<String> findAllTrackingStatusByPlant(String plantName) {
		String queryString = String.format(FIND_ALL_TRACKING_STATUS_BY_PLANT, entityClass.getName(), plantName);
		return findByQuery(queryString, String.class);
	}

	private static final String FIND_BY_PRODUCTID_RANGE = "select distinct p from %s p where p.productId >= :start and p.productId <= :end order by p.productId asc" ;
	public List<E> findAllByProductIdRange(String start, String end, int count) {
		Parameters params = Parameters.with("start", start).put("end", end);
		return findAllByQuery(String.format(FIND_BY_PRODUCTID_RANGE, entityClass.getName()), params, count);
	}

	private static final String FIND_BY_PLANT_PRODUCTID_RANGE =
			"select distinct p from %s p,ProcessPoint pp where p.lastPassingProcessPointId=pp.processPointId "
			+ " and p.productId >= :start and p.productId <= :end and pp.plantName = :plantName order by p.productId asc" ;
	public List<E> findAllByPlantProductIdRange(String plantName, String start, String end, int count) {
		Parameters params = Parameters.with("plantName", plantName).
				put("start", start).
				put("end", end);
		return findAllByQuery(String.format(FIND_BY_PLANT_PRODUCTID_RANGE, entityClass.getName()), params, count);
	}

	public E findNextInprocessProduct(String productId) {
		Parameters params = Parameters.with("productId", productId);

		return findFirstByQuery(String.format(FIND_NEXT_INPROCESS_PRODUCT, entityClass.getName()), params);
	}

	public E findPreviousInprocessProduct(String productId) {
		Parameters params = Parameters.with("productId", productId);

		return findFirstByQuery(String.format(FIND_PREVIOUS_INPROCESS_PRODUCT, entityClass.getName()), params);
	}

	public List<E> findProducts(List<String> productIds, int startPos, int pageSize) {
		return findAll(Parameters.with("productId", productIds), startPos, pageSize);
	}

	// === get/set === //
	protected String getIdFieldName() {
		String fieldName = ReflectionUtils.getAnnotatedFieldName(getEntityClass(), Id.class);
		if (StringUtils.isNotBlank(fieldName)) {
			return fieldName;
		}
		return ReflectionUtils.getAnnotatedFieldName(getEntityClass(), EmbeddedId.class);
	}

	protected String getDunnageFieldName() {
		return DUNNAGE_FIELD_NAME;
	}

	protected String getUpdateDunnageJpql() {
		String entityName = getEntityClass().getSimpleName();
		String idFieldName = getIdFieldName();
		String dunnageFieldName = getDunnageFieldName();
		String jpql = "update " + entityName + " e set e." + dunnageFieldName + " = :dunnage where e." + idFieldName + " in (:productId) " ;
		jpql = jpql + " \n and (select count(p." +  idFieldName + ") from "+ entityName + " p where  p." + dunnageFieldName + " = :dunnage and p." + idFieldName + " not in (:productId)) <= :dunnageSize ";		
		return jpql;
	}	


	/**
	 * Method for updating tracking status to next Line if property is set
	 * @param productId
	 * @param nextTrackingStatus
	 * @return
	 */
	@Transactional
	public void updateNextTracking(String productId, String nextTrackingStatus){
		update(Parameters.with("trackingStatus", nextTrackingStatus),
				Parameters.with("productId", productId));
	}
	public List<E> findAllProcessedProductsForProcessPointForTimeRange(String ppId,String startTime,String endTime, List<String> trackingStatuses) {
		if (!BaseProduct.class.isAssignableFrom(entityClass))
			throw new UnsupportedOperationException("findAllProcessedProductsForProcessPointForTimeRange is not implemented for " + entityClass.getName());
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		
		
		Parameters params = Parameters.with("1", ppId);
		params.put("2", startTime);
		params.put("3", endTime);
		
		String addtionalSql = "";		
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			addtionalSql = " and  a.TRACKING_STATUS NOT IN ("+ StringUtil.toSqlInString(trackingStatuses)+")";
		}
		String sql = FIND_ALL_PROCESSED_FOR_TIME_RANGE+addtionalSql+" order by x.ACTUAL_TIMESTAMP  desc " ;
		
		return findAllByNativeQuery(String.format(sql, tableAnnotation.name()), params);
	}
	
	public List<E> findAllProcessedProductsForProcessPointBeforeTime(String onPP, String startTimeStamp, List<String>trackingStatuses){
		if (!BaseProduct.class.isAssignableFrom(entityClass))
			throw new UnsupportedOperationException("findAllProcessedProductsForProcessPointBeforeTime is not implemented for " + entityClass.getName());
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		
		
		Parameters params = Parameters.with("1", onPP);
		params.put("2", startTimeStamp);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = " and  a.TRACKING_STATUS NOT IN ("+ StringUtil.toSqlInString(trackingStatuses)+")";
		}
		String sql = FIND_ALL_PROCESSED_BEFORE_TIME + trackingStatusSql +" order by x.ACTUAL_TIMESTAMP  desc ";
		
		return findAllByNativeQuery(String.format(sql, tableAnnotation.name()), params);
	}
}