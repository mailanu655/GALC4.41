/**
 * 
 */
package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.notification.service.IProductHoldResultNotification;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Jan 5, 2012
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class HoldResultDaoImpl extends BaseDaoImpl<HoldResult,HoldResultId> implements HoldResultDao {
    private static final long serialVersionUID = 1L;
	   
	private static final String FIND_BY_PRODUCTID = "select a from HoldResult a where a.id.productId = :productId";
    private static final String RELEASE_PRODUCT_HOLDS = "update galadm.gal147tbx hr set hr.RELEASE_ASSOCIATE_NO = ?1, hr.RELEASE_ASSOCIATE_NAME = ?2, hr.RELEASE_ASSOCIATE_PAGER = ?3, hr.RELEASE_ASSOCIATE_PHONE = ?4, hr.RELEASE_REASON = ?5, hr.RELEASE_TIMESTAMP = ?6, hr.UPDATE_TIMESTAMP = ?6, hr.RELEASE_FLAG = 1 where hr.PRODUCT_ID = ?7 and (hr.RELEASE_FLAG is null or hr.RELEASE_FLAG = 0)";    	
	private static final String FIND_DISTINCT_HOLD_REASONS = "select distinct hold_reason  from  gal147tbx order by hold_reason";
	private static final String FIND_DISTINCT_RELEASE_REASONS = "select distinct release_reason  from  gal147tbx order by release_reason";
	private static final String FIND_BY_PRODUCT_SERIAL_RANGE = "select h.* from gal147tbx h,gal143tbx p,qsr_tbx q where p.product_id = h.product_id and q.qsr_id = h.qsr_id "
			+" and q.hold_access_type_id = ?3 "
			+ "AND SUBSTR(p.Product_ID, 12,6) >=  ?1 AND SUBSTR(p.Product_ID, 12,6) <= ?2 ORDER BY SUBSTR(p.Product_ID, 12, 6)";
	private static final String FIND_BY_HOLD_DATE_RANGE = "select h.* from gal147tbx h,gal143tbx p,qsr_tbx q where p.product_id = h.product_id and q.qsr_id = h.qsr_id "
			+" and q.hold_access_type_id = ?3 "
			+ "AND h.ACTUAL_TIMESTAMP >=  ?1 AND h.ACTUAL_TIMESTAMP <= ?2 ORDER BY h.ACTUAL_TIMESTAMP";
	
	private static final String FIND_BY_RANGE = "select h.* from gal147tbx h join gal143tbx p on p.product_id = h.product_id join qsr_tbx q on q.qsr_id = h.qsr_id  ";
	private static final String FIND_COUNT_BY_RANGE = "select count(h.product_id) from gal147tbx h join gal143tbx p on p.product_id = h.product_id join qsr_tbx q on q.qsr_id = h.qsr_id   ";
	private static final String FIND_BY_PRODUCTID_AND_QSR = "select a from HoldResult a where a.id.productId = :productId and a.qsrId = :qsrId";	
	private static final String FIND_BY_PRODUCTION_LOT_RANGE = "select h.* from gal147tbx h join gal143tbx p on p.product_id = h.product_id where p.PRODUCTION_LOT BETWEEN ?1 AND ?2";
	private static final String FIND_QSR_BY_PRODUCTION_LOT_RANGE = "select distinct h.qsr_id from gal147tbx h join gal143tbx p on p.product_id = h.product_id where p.PRODUCTION_LOT BETWEEN ?1 AND ?2";
   
	public List<HoldResult> findAllByProductId(String productId) {
    	return findAllByQuery(FIND_BY_PRODUCTID, Parameters.with("productId", productId));
    }
	
	public List<HoldResult> findAllByProductAndReleaseFlag(String productId,
			boolean releaseFlag, HoldResultType holdType) {
		Parameters param = Parameters.with("id.productId", productId).put("releaseFlag", releaseFlag ? 1 : 0);
		if (!holdType.equals(HoldResultType.GENERIC_HOLD)) {
			param.put("id.holdType", holdType.getId());
		}
		return findAll(param);
		
	}
	
	public List<HoldResult> findAllByProductAndReleaseFlag(String productId,boolean releaseFlag) {
		Parameters param = Parameters.with("id.productId", productId).put("releaseFlag", releaseFlag ? 1 : 0);
		return findAll(param);
	}

	public List<HoldResult> findAllByQsrId(int qsrId) {
		return findAll(Parameters.with("qsrId", qsrId));
	}
	
	public int releaseProductHolds(String associateId, String associateName, String pager, String phone, String reason, String productId) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Parameters params = Parameters.with("1", associateId);
		params.put("2", associateName);
		params.put("3", pager);
	    params.put("4", phone);
	    params.put("5", reason);
	    params.put("6", ts);
	    params.put("7", productId);
		return executeNative(RELEASE_PRODUCT_HOLDS, params);
	}
	public List<Object[]> getHoldReasons() {
		return findAllByNativeQuery(FIND_DISTINCT_HOLD_REASONS, null, Object[].class);
	}
	
	public List<Object[]> getReleaseReasons() {
		return findAllByNativeQuery(FIND_DISTINCT_RELEASE_REASONS, null, Object[].class);
	}
	
	public List<HoldResult>findAllByHoldReason(String holdReason)
	{
		return findAll(Parameters.with("holdReason", holdReason));
	}
	
	public List<HoldResult> findAllHoldByProductIdAndHoldReason(String productId, String holdReason) {
		
		return findAll(Parameters.with("id.productId", productId).put("releaseFlag", 0).put("holdReason",holdReason));
	}
	
	@Override
	@Transactional
	public HoldResult insert(HoldResult holdResult) {
		HoldResult savedHoldResult = super.insert(holdResult);
		notifyHoldResult(holdResult);
		return savedHoldResult;
	}
	
	@Override
	@Transactional
	public void insertAll(List<HoldResult> holdResults) {
		super.insertAll(holdResults);
		notifyHoldResults(holdResults);
	}
	
	@Override
	@Transactional
	public HoldResult update(HoldResult holdResult) {
		HoldResult savedHoldResult = super.update(holdResult);
		notifyHoldResult(holdResult);
		return savedHoldResult;
	}
	
	@Override
	@Transactional
	public void updateAll(List<HoldResult> holdResults) {
		super.updateAll(holdResults);
		notifyHoldResults(holdResults);
	}
	
	@Override
	@Transactional
	public HoldResult save(HoldResult holdResult) {
		HoldResult savedHoldResult = super.save(holdResult);
		notifyHoldResult(holdResult);
		return savedHoldResult;
	}
	
	@Override
	@Transactional
	public List<HoldResult> saveAll(List<HoldResult> holdResults) {
		List<HoldResult> savedHoldResults = super.saveAll(holdResults);
		notifyHoldResults(holdResults);
		return savedHoldResults;
	}
	
	private void notifyHoldResult(HoldResult holdResult) {
		List<HoldResult> results = new ArrayList<HoldResult>();
		results.add(holdResult);
		notifyHoldResults(results);
	}
	
	private void notifyHoldResults(List<HoldResult> holdResults) {
		ServiceFactory.getNotificationService(IProductHoldResultNotification.class).holdResultChanged(holdResults);
	}
	
	@Override
	public List<HoldResult> findAllByProductHoldTypeAndHoldReason(String productId, HoldResultType holdType, String holdReason) {
		
		Parameters params = Parameters.with("id.productId", productId).put("releaseFlag", 0).put("holdReason",holdReason);
		if (!holdType.equals(HoldResultType.GENERIC_HOLD)) {
			params.put("id.holdType", holdType.getId());
		}
		return findAll(params);
	}
	
	public List<HoldResult> findAllBySequenceRange(String startSeq,String endSeq, String holdAccessType){
		Parameters params = Parameters.with("1", startSeq);
		params.put("2", endSeq);
		params.put("3", holdAccessType);
		
		return findAllByNativeQuery(FIND_BY_PRODUCT_SERIAL_RANGE, params);
		
	}
	
	public List<HoldResult> findAllByDateRange(Timestamp startTime,Timestamp endTime, String holdAccessType){
		Parameters params = Parameters.with("1", startTime);
		params.put("2", endTime);
		params.put("3", holdAccessType);
		
		return findAllByNativeQuery(FIND_BY_HOLD_DATE_RANGE, params);
		
	}
	
	public List<HoldResult> findAllByRange(String productId,Timestamp startTime,Timestamp endTime,String startSeq,String endSeq, String holdAccessType,int qsrId){
		String whereClause = prepareWhereClause(productId,startTime,endTime,startSeq,endSeq,holdAccessType,qsrId);
		return findAllByNativeQuery(FIND_BY_RANGE+ " where " + whereClause,null);
	}
	
	public long findCountByRange(String productId,Timestamp startTime,Timestamp endTime,String startSeq,String endSeq, String holdAccessType,int qsrId){
		String whereClause = prepareWhereClause(productId,startTime,endTime,startSeq,endSeq,holdAccessType,qsrId);
		return countByNativeSql(FIND_COUNT_BY_RANGE + " where " + whereClause,null);
	}
	
	
	private String prepareWhereClause(String productId,Timestamp startTime,Timestamp endTime,String startSeq,String endSeq, String holdAccessType,int qsrId) {
		StringBuilder whereBuilder = new StringBuilder();
		
		if(!StringUtils.isEmpty(productId)) {
			if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
			 whereBuilder.append(" p.PRODUCT_ID = '"+productId+"'");
		}
		
		if (!StringUtils.isEmpty(startSeq) && !StringUtils.isEmpty(endSeq)) {
			if (startSeq.length() == 6 && endSeq.length() == 6) {
				if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
				 whereBuilder.append(" SUBSTR(p.PRODUCT_ID,12,6) BETWEEN '"+startSeq+"' AND '"+endSeq+"'");
			}
		}
		if (startTime!=null && endTime!=null) {
			if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
			 whereBuilder.append(" h.ACTUAL_TIMESTAMP  BETWEEN '"+startTime+"' AND '"+endTime+"'");
		}
		if(!StringUtils.isEmpty(holdAccessType)) {
			if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
			 whereBuilder.append(" q.hold_access_type_id = '"+holdAccessType+"'");
		}
		if(qsrId > 0) {
			if (whereBuilder.length() > 0) whereBuilder.append(" AND ");
			 whereBuilder.append(" h.qsr_id = "+qsrId);
		}
		
		return whereBuilder.toString();
	}
	
	
	public long unreleasedCountByQsr(int qsrId) {
		StringBuilder sb = new StringBuilder();
		Parameters params = Parameters.with("qsrId", qsrId).put("releaseFlag", 0);
		
		long count =  count(params);
		return count;
	}
	
	public List<HoldResult> findAllByProductIdAndQsr(String productId, int qsrId) {
		return findAll(Parameters.with("id.productId", productId).put("qsrId", qsrId));
    }

	@Override
	public long countByQsr(int qsrId) {
		StringBuilder sb = new StringBuilder();
		Parameters params = Parameters.with("qsrId", qsrId);
		
		long count =  count(params);
		return count;
	}
	
	@Override
	public List<HoldResult> findAllByProductionLotRange(String start, String end) {
		Parameters params = Parameters.with("1", start);
		params.put("2", end);
			
		return findAllByNativeQuery(FIND_BY_PRODUCTION_LOT_RANGE, params);
	}

	@Override
	public List<Integer> findQsrByProductionLotRange(String start, String end) {
		Parameters params = Parameters.with("1", start);
		params.put("2", end);
			
		return findAllByNativeQuery(FIND_QSR_BY_PRODUCTION_LOT_RANGE, params,Integer.class);
	}
	
}

