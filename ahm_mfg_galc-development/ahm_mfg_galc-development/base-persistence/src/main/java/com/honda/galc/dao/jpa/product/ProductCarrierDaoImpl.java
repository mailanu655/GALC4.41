package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductCarrierId;
import com.honda.galc.service.Parameters;

public class ProductCarrierDaoImpl extends BaseDaoImpl<ProductCarrier,ProductCarrierId> implements ProductCarrierDao{

	
	private final String FIND_FIRST_EXPECTED_CARRIER_ID = " select car.* from galadm.PRODUCT_CARRIER_TBX car  " +
	" where car.product_id = ?1 " +
	" and car.carrier_id = ?2 " +
	" and  car.off_timestamp is null " +
	" order by car.create_timestamp desc ";		
	
	private final String FIND_PRODUCTS_IN_CARRIER_ID = " select car.* from galadm.PRODUCT_CARRIER_TBX car  " +
	" where car.carrier_id = ?1 " +
	" and  car.off_timestamp is null " +
	" order by car.create_timestamp desc ";		
	
	private static final String FIND_ALL_PROCESSED = " SELECT PCT.PRODUCT_ID, G143.PRODUCT_SPEC_CODE, PCT.CARRIER_ID AS SEQUENCE_NUMBER, G143.KD_LOT_NUMBER \n" + 
            " FROM PRODUCT_CARRIER_TBX AS PCT \n" + 
            " LEFT OUTER JOIN GAL143TBX G143 ON G143.PRODUCT_ID = PCT.PRODUCT_ID \n" + 
            " WHERE PCT.PROCESS_POINT_ID = ?1 \n" + 
            " ORDER BY PCT.ON_TIMESTAMP DESC \n";
	
	private static final String FIND_BY_SEQ_NUM = "select c.product_ID,b.PRODUCT_SPEC_CODE, c.carrier_ID as AF_ON_SEQUENCE_NUMBER, b.PRODUCTION_LOT, b.TRACKING_STATUS from \n" + 
			" GALADM.PRODUCT_CARRIER_TBX as c left outer join galadm.GAL143TBX as b on c.PRODUCT_ID =  b.PRODUCT_ID \n" + 
			" where c.carrier_ID = ?1 ";
	
	private static final String FIND_BY_SEQ_RANGE = "select c.product_ID,b.PRODUCT_SPEC_CODE, c.carrier_ID as AF_ON_SEQUENCE_NUMBER, b.PRODUCTION_LOT, b.TRACKING_STATUS from \n" + 
			" GALADM.PRODUCT_CARRIER_TBX as c left outer join galadm.GAL143TBX as b on c.PRODUCT_ID =  b.PRODUCT_ID \n" + 
			" where c.carrier_ID >= ?1 and c.carrier_ID <= ?2 and (b.TRACKING_STATUS = ?3 or b.TRACKING_STATUS is null) order by c.ON_TIMESTAMP desc ";
	
	private static final String FIND_PRODUCT_CARRIER_BY_PPID_AND_DATE_RANGE = "select e.* from galadm.PRODUCT_CARRIER_TBX  as e where e.PROCESS_POINT_ID = ?1 "+ 
			"and e.ON_TIMESTAMP>?2 AND e.ON_TIMESTAMP<=?3  order by e.ON_TIMESTAMP";
	
	private final String GET_EMPTY_CARRIER = " select p from ProductCarrier p  where p.processPointId = :processpoint AND p.id.carrierId = :seq  AND p.id.productId like 'EMPTY%' ";		

	private static final String FIND_BY_PROCESS_POINT_AND_SEQ_RANGE = "select  p from ProductCarrier p  where p.processPointId = :processPoint AND p.id.carrierId >= :startSeq  AND p.id.carrierId <= :endSeq order by p.id.onTimestamp desc"; 
			
	
	public List<ProductCarrier> findAll(String productId,String carrierId){
		Parameters params = Parameters.with("1", productId);
		params.put("2", carrierId);			
		
		return findAllByNativeQuery(FIND_FIRST_EXPECTED_CARRIER_ID, params);
	}
	
	public List findRackAssociatedEnginesInGivenStatus(String status, String[] validProcessPoints) {
		StringBuilder query = new StringBuilder("SELECT pc.CARRIER_ID RACK_ID, pc.PRODUCT_ID EIN, eng.PRODUCT_SPEC_CODE, pc.ON_TIMESTAMP " +
				" FROM product_carrier_tbx pc LEFT JOIN gal131tbx eng ON (pc.PRODUCT_ID = eng.PRODUCT_ID) WHERE eng.TRACKING_STATUS = ?1 ");
		
		if(validProcessPoints.length>0){
			query.append(" AND pc.PROCESS_POINT_ID IN ('");
			for (int i = 0; i < validProcessPoints.length; i++) {
			  if (i > 0) {
			    query.append("','");
			  }
			  query.append(validProcessPoints[i].trim());
			}
			query.append("')");
		}
		
		Parameters params = Parameters.with("1", status);
		return findResultListByNativeQuery(query.toString(), params);
	}
	
	public List<ProductCarrier> findEnginesInCarrier(String carrierId){
		Parameters params = Parameters.with("1", carrierId);
		
		return findAllByNativeQuery(FIND_PRODUCTS_IN_CARRIER_ID, params);
	}
	
	public ProductCarrier findfirstProduct(String productId){
		Parameters params = Parameters.with("id.productId", productId);
		return findFirst(params);
	}
	
	public List<ProcessProductDto> findAllProcessedProductsForProcessPoint(String ppId, int rowLimit) {
		Parameters params = Parameters.with("1", ppId);
		String sql = FIND_ALL_PROCESSED + "Fetch first "+ rowLimit +" rows Only";
		return findAllByNativeQuery(sql, params, ProcessProductDto.class);
	}
	
	public List<Frame> findByAfOnSeqNumber(String carrierId) {
		Parameters params = Parameters.with("1", carrierId);
		return findAllByNativeQuery(FIND_BY_SEQ_NUM, params, Frame.class);
	}

	public List<Frame> findByAfOnSeqRangeLineId(String startSeq, String endSeq, String lineId) {
		Parameters params = Parameters.with("1", startSeq);
		params.put("2", endSeq);
		params.put("3", lineId);
		return findAllByNativeQuery(FIND_BY_SEQ_RANGE, params, Frame.class);
	}
	
	public List<ProductCarrier> getProductCarriersByProcessPointAndDateRange(String ppId, String startTime, String endTime){	    
		Parameters params = Parameters.with("1", ppId);
	    params.put("2", startTime);
		params.put("3", endTime);
	    return	findAllByNativeQuery(FIND_PRODUCT_CARRIER_BY_PPID_AND_DATE_RANGE,params);		
	}

	@Override
	public boolean checkForEmptyCarrierByAfOnSeqAndProcessPointId(String seq, String processPointId) {
		Parameters params = Parameters.with("processpoint", processPointId);
	    params.put("seq", seq);
	    List<ProductCarrier> prodCarrierList = findAllByQuery(GET_EMPTY_CARRIER ,params);
		return prodCarrierList.isEmpty()?false:true;
	}

	@Override
	public ProductCarrier findByAfOnSeqAndProcessPointId(String seq, String processPointId) {
		Parameters params = Parameters.with("processPointId", processPointId);
	    params.put("id.carrierId", seq);
		return findFirst(params, new String[] { "id.onTimestamp" },false);
	}

	@Override
	public List<ProductCarrier> findAllByAfOnSeqRangeAndProcessPointId(String startSeq, String endSeq,
			String processPointId) {
		Parameters params = Parameters.with("processPoint", processPointId);
	    params.put("startSeq", startSeq);
	    params.put("endSeq", endSeq);
		return findAllByQuery(FIND_BY_PROCESS_POINT_AND_SEQ_RANGE,params);
	}
	
	public ProductCarrier findByCarrierId(String carrierId){
		Parameters params = Parameters.with("id.carrierId", carrierId);
		return findFirst(params, new String[] { "id.onTimestamp" },false);
	}
	
	public ProductCarrier findByProductId(String productId) {
		Parameters params = Parameters.with("1", productId);
		return findFirst(params);
	}
	
	public ProductCarrier findProductCarrierByProductIdandProcessPointId(String productId, String processPointId) {
		Parameters params = Parameters.with("id.productId", productId);
	    params.put("processPointId", processPointId);
	    return findFirst(params, new String[] { "id.onTimestamp" },false);
	}
	
	@Transactional
	public void removeProductCarrierByProductIdandProcessPointId(String productId, String processPointId) {
	    ProductCarrier carrier  = findProductCarrierByProductIdandProcessPointId(productId, processPointId);
	    remove(carrier);
   
	}
	
	public List<ProductCarrier> findAllByProductId(String productId){
		Parameters params = Parameters.with("id.productId", productId);
		return findAll(params);
	}
}
