package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.service.Parameters;

public class ProductSequenceDaoImpl extends BaseDaoImpl<ProductSequence,ProductSequenceId> implements ProductSequenceDao{

	private final String SELECT_NEXT_PRODUCT_ID = "select p from ProductSequence p where p.id.processPointId = :processpoint and p.referenceTimestamp > :refTime order by p.referenceTimestamp asc";
	private final String FIND_FIRST_EXPECTED_PRODUCT_ID = "SELECT H.* FROM galadm.PRODUCT_SEQUENCE_TBX H WHERE H.PROCESS_POINT_ID =?1 AND NOT EXISTS (SELECT PRODUCT_ID FROM galadm.GAL185TBX WHERE PRODUCT_ID = H.PRODUCT_ID AND PART_NAME IN (SELECT DISTINCT PART_NAME FROM galadm.GAL246TBX WHERE PROCESS_POINT_ID =?2)) ORDER BY H.REFERENCE_TIMESTAMP FOR READ ONLY";
	private final String FIND_ALL_EXPECTED_PRODUCT_ID = "select p from ProductSequence p where p.id.processPointId = :processpoint order by p.referenceTimestamp asc";
	private final String FIND_ALL_PROCESS_POINT_ID = "select distinct(trim(process_point_id)) from galadm.PRODUCT_SEQUENCE_TBX";
	private final String SELECT_PREVIOUS_PRODUCT_ID = "select p from ProductSequence p where p.id.processPointId = :processpoint and p.referenceTimestamp < :refTime order by p.referenceTimestamp desc";


	private static final String FIND_ALL_UPCOMING = "SELECT p.SEQUENCE_NUMBER,f.PRODUCT_SPEC_CODE, f.KD_LOT_NUMBER, p.PRODUCT_ID "+
			" FROM galadm.PRODUCT_SEQUENCE_TBX p inner join galadm.gal143tbx f on p.product_Id = f.product_Id "+
			" where p.PROCESS_POINT_ID = ?2 AND p.REFERENCE_TIMESTAMP >(SELECT max(r.REFERENCE_TIMESTAMP) from galadm.PRODUCT_SEQUENCE_TBX r where r.sequence_Number = ?1 and  r.PROCESS_POINT_ID = ?2)"+
			" order by p.REFERENCE_TIMESTAMP asc ";
	
	private static final String FIND_ALL_PROCESSED = "SELECT p.SEQUENCE_NUMBER,f.PRODUCT_SPEC_CODE, f.KD_LOT_NUMBER, p.PRODUCT_ID "+
			" FROM galadm.PRODUCT_SEQUENCE_TBX p inner join galadm.gal143tbx f on p.product_Id = f.product_Id "+
			" where p.PROCESS_POINT_ID = ?1 order by p.REFERENCE_TIMESTAMP desc ";

	private static final String DELETE_FOR_STATIONID_BEFORE_TIMESTAMP = "delete from ProductSequence p where p.referenceTimestamp <= :refTs and p.id.processPointId = :ppId";
	private static final String FIND_ALL_FOR_STATIONID_BEFORE_TIMESTAMP = "select p from ProductSequence p where p.referenceTimestamp <= :refTs and p.id.processPointId = :ppId";
	public ProductSequence findNextExpectedProductId(ProductSequence productSequence) {
		Parameters params = Parameters.with("processpoint", productSequence.getId().getProcessPointId());
		params.put("refTime", productSequence.getReferenceTimestamp());

		return findFirstByQuery(SELECT_NEXT_PRODUCT_ID, params);
	}

	public ProductSequence findFirstExpectedProductId(String processPointId, String InSequenceProcessPointId){
		Parameters params = Parameters.with("1", InSequenceProcessPointId);
		params.put("2", processPointId);

		return findFirstByNativeQuery(FIND_FIRST_EXPECTED_PRODUCT_ID, params);
	}

	public List<ProductSequence> findAll(String InSequenceProcessPointId){
		Parameters params = Parameters.with("processpoint", InSequenceProcessPointId);

		return findAllByQuery(FIND_ALL_EXPECTED_PRODUCT_ID, params);
	}



	public List<String> findIncomingExpectedProductIds(ProductSequence productSequence) {
		Parameters params = Parameters.with("processpoint", productSequence.getId().getProcessPointId());
		params.put("refTime", productSequence.getReferenceTimestamp());

		List<ProductSequence> list = findAllByQuery(SELECT_NEXT_PRODUCT_ID, params);
		List<String> incomingProductList = new ArrayList<String>();

		if(list == null) return incomingProductList;

		for(ProductSequence seq : list){
			incomingProductList.add(seq.getId().getProductId());
		}

		return incomingProductList;
	}

	public ProductSequence findByProductId(String productId) {
		Parameters params = Parameters.with("id.productId", productId);
		return findFirst(params);
	}

	@Override
	@Transactional
	public int deleteAllBeforeTimestamp(Timestamp refTs, String stationId)  {
		
		int i = executeUpdate(DELETE_FOR_STATIONID_BEFORE_TIMESTAMP, Parameters.with("refTs", refTs).put("ppId", stationId));
		return i;
	}
	
	@Override
	@Transactional
	public List<ProductSequence> findAllForStationIdBeforeTimestamp(Timestamp refTs, String stationId)  {
		
		Parameters params = Parameters.with("refTs", refTs).put("ppId", stationId);
		List<ProductSequence> pList = findAllByQuery(FIND_ALL_FOR_STATIONID_BEFORE_TIMESTAMP, params);
		return pList;
	}
	
	@Transactional 
	public int deleteProdIds(List <String> prodIds)  
	{
		int count = 0;
		for( String prodId : prodIds )
		{
			count = count + delete(Parameters.with("id.productId", prodId));
		}
		return count;
	}    

	public List<String> getAllProcessPointIds() {
		return findAllByNativeQuery(FIND_ALL_PROCESS_POINT_ID, null, String.class);
	}    

	@Transactional 
	public List<ProductSequence> removeProductSequence(ProductSequence sequence) {
		List<ProductSequence> removed = new ArrayList<ProductSequence>();
		if(!StringUtils.isEmpty(sequence.getId().getProcessPointId())){
			super.removeByKey(sequence.getId());
			removed.add(sequence);
		} else {
			Parameters params = Parameters.with("id.productId", sequence.getId().getProductId());
			removed.addAll(findAll(params));
			delete(params);
		}

		return removed;
	}

	public ProductSequence findPrevProductId(
			ProductSequence productSequence) {
		Parameters params = Parameters.with("processpoint", productSequence.getId().getProcessPointId());
		params.put("refTime", productSequence.getReferenceTimestamp());

		return findFirstByQuery(SELECT_PREVIOUS_PRODUCT_ID, params);
	}

	public List<ProcessProductDto> findAllUpcoming(String processPointId,Integer sequenceNumber, int rowLimit){
		
		Parameters params = Parameters.with("1", sequenceNumber);
		params.put("2", processPointId);
		String sql = FIND_ALL_UPCOMING + "Fetch first "+ rowLimit +" rows Only"; 
		return findAllByNativeQuery(sql, params, ProcessProductDto.class);
		
	}

	public List<ProductSequence> findAllByProcessPointIdAndSequenceNumber(String processPointId, Integer sequenceNumber) {
		Parameters params = Parameters.with("id.processPointId", processPointId);
		params.put("sequenceNumber", sequenceNumber);
 
		List<ProductSequence> productSequenceList = findAll(params, new String[] { "referenceTimestamp" }, true);
		if (productSequenceList == null || productSequenceList.isEmpty()) {
			return null;
		}
		return productSequenceList;
	}

	@Override
	public List<ProcessProductDto> findAllAlreadyProcessed(String processPointId, int rowLimit) {
		Parameters params = Parameters.with("1", processPointId);
		String sql = FIND_ALL_PROCESSED + "Fetch first "+ rowLimit +" rows Only"; 
		return findAllByNativeQuery(sql, params, ProcessProductDto.class);
	}

}
