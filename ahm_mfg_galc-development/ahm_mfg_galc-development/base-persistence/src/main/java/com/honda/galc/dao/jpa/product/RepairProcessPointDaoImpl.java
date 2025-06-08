package com.honda.galc.dao.jpa.product;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;

import com.honda.galc.dao.product.RepairProcessPointDao;
import com.honda.galc.entity.product.RepairProcessPoint;
import com.honda.galc.entity.product.RepairProcessPointId;
import com.honda.galc.service.Parameters;



/*
* 
* 
* 
*/
public class RepairProcessPointDaoImpl extends BaseDaoImpl<RepairProcessPoint, RepairProcessPointId> implements RepairProcessPointDao {

	private static final String UPDATE_SEQUENCE_FOR_PART_NAME = "update  RepairProcessPoint r set r.sequenceNo =: seqNo where r.id.processPointId = :processPointId and r.id.partName = :partName ";	
	private static final String FIND_REPAIR_PROCESSPOINT_FOR_PART_NAME = "select  distinct r.id.processPointId from RepairProcessPoint r  where r.id.partName = :partName ";
	private static final String FIND_REPAIR_PART_DATA = "SELECT A.SEQUENCE_NUMBER, B.PART_NAME, B.PRODUCT_TYPE, B.PART_CONFIRM_CHECK, B.WINDOW_LABEL, B.SUB_PRODUCT_TYPE, B.PART_VISIBLE, B.REPAIR_CHECK, B.EXTERNAL_REQUIRED, B.LET_CHECK_REQUIRED FROM REPAIR_PROCESS_POINT_TBX A JOIN GAL261TBX B ON A.PART_NAME = B.PART_NAME WHERE A.PROCESS_POINT_ID = ?1 AND NOT EXISTS (SELECT 1 FROM REPAIR_PROCESS_POINT_TBX C WHERE A.PROCESS_POINT_ID = C.PROCESS_POINT_ID AND A.PART_NAME = C.PART_NAME AND (A.SEQUENCE_NUMBER > C.SEQUENCE_NUMBER OR (A.SEQUENCE_NUMBER = C.SEQUENCE_NUMBER AND A.PART_ID > C.PART_ID)) FETCH FIRST ROW ONLY) ORDER BY A.SEQUENCE_NUMBER, B.PART_NAME";
	
	public List<RepairProcessPoint> findAllRepairPartsForProcessPoint(String processPointId) {
		Parameters params = new Parameters();
		params.put("id.processPointId", processPointId);
		return findAll(params, new String[]{"sequenceNo"}, true);
	}

	public List<RepairProcessPoint> findAllRepairPartsByProcessPointAndPartName(String processPointId,
			String partName) {
		Parameters params = new Parameters();
		params.put("id.processPointId", processPointId);
		params.put("id.partName", partName);
		return findAll(params);
	}
	
	@Transactional
	public int updateSequenceNo(String processPointId,	String partName, Integer seqNo){
		Parameters params = new Parameters();
		params.put("seqNo", seqNo);
		params.put("processPointId", processPointId);
		params.put("partName", partName);
		return executeUpdate(UPDATE_SEQUENCE_FOR_PART_NAME, params);
	}

	public List<String> findRepairProcessPointForPartName(String partName) {
		Parameters params = new Parameters();
		params.put("partName", partName);
		
		return (List<String>)findResultListByQuery(FIND_REPAIR_PROCESSPOINT_FOR_PART_NAME,params);
	}
	
	public List<RepairProcessPoint> findAllRepairPartsByPartNameAndPartId(String partName,String partId) {
		Parameters params = new Parameters();
		params.put("id.partName", partName);
		params.put("id.partId", partId);
		
		return findAll(params);
	}
	
	public List<Object[]> findRepairPartData(String processPointId) {
		return findAllByNativeQuery(FIND_REPAIR_PART_DATA, Parameters.with("1", processPointId), Object[].class);
	}
}
