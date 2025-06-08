package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationMeasurementId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCOperationMeasurementDaoImpl 
	extends BaseDaoImpl<MCOperationMeasurement, MCOperationMeasurementId> 
	implements MCOperationMeasurementDao {
	
	public static String GET_DISTINCT_DEVICE_ID = "SELECT DISTINCT m.DEVICE_ID from galadm.MC_OP_MEAS_TBX m";
	
	public static String GET_BY_OPERATION = "SELECT DISTINCT TRIM(OPERATION_NAME) FROM GALADM.MC_OP_MEAS_TBX WHERE OPERATION_NAME LIKE ?1 FETCH FIRST 50 ROWS ONLY";
	
	public List<MCOperationMeasurement> findAllMeasurementForOperationPartAndPartRevision(
			String operationName, String partId, int partRevision) {
		
		Parameters param = Parameters.with("id.operationName", operationName);
		param.put("id.partId", partId);
		param.put("id.partRevision", partRevision);
		return findAll(param);
	}

	public List<Object[]> findDistinctDeviceId() {
		return executeNative(GET_DISTINCT_DEVICE_ID);
	}
	
	public List<MCOperationMeasurement> findAllByOperationNamePartIdAndRevision(String operationName, String partId, int partRevision) {
		Parameters param = Parameters.with("id.operationName", operationName);
		param.put("id.partId", partId);
		param.put("id.partRevision", partRevision);
		return findAll(param);
	}
	
	@Transactional
	public void deleteBy(String operationName, String partId, int partRevision, int measSeq) {
		Parameters param = Parameters.with("id.operationName", operationName);
		param.put("id.partId", partId);
		param.put("id.partRevision", partRevision);
		if(measSeq > 0)
		param.put("id.measurementSeqNum", measSeq);
		delete(param);
	}
	
	public List<String> findAllByOperationName(String operationName) {
		String likeOpName = "%" + operationName + "%";
		return findAllByNativeQuery(GET_BY_OPERATION, Parameters.with("1", likeOpName), String.class);
	}
	
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationMeasurement mcViosOpMeasObj) {
		
		MCOperationPartRevision opPartRev = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.findApprovedByPartNoAndType(mcViosOpMeasObj.getOperationName(), "", "MFG");
		if (opPartRev != null) {
			ServiceFactory.getDao(MCOperationMeasurementDao.class).deleteBy(opPartRev.getId().getOperationName(),
					opPartRev.getId().getPartId(), opPartRev.getId().getPartRevision(),-1);
			
				MCOperationMeasurementId id = new MCOperationMeasurementId();
				id.setOperationName(opPartRev.getId().getOperationName());
				id.setPartId(opPartRev.getId().getPartId());
				id.setPartRevision(opPartRev.getId().getPartRevision());
				id.setOpMeasSeqNum(mcViosOpMeasObj.getId().getMeasurementSeqNum());
				MCOperationMeasurement meansObj = new MCOperationMeasurement();
				meansObj.setId(id);
				meansObj.setDeviceId(mcViosOpMeasObj.getDeviceId());
				meansObj.setDeviceMsg(mcViosOpMeasObj.getDeviceMsg());
				meansObj.setMaxLimit(mcViosOpMeasObj.getMaxLimit());
				meansObj.setMinLimit(mcViosOpMeasObj.getMinLimit());
				meansObj.setMaxAttempts(mcViosOpMeasObj.getMaxAttempts());
				save(meansObj);
				
		}
	}
	
	@Override
	public void deleteMeasurementRevision(int measSeq, String unitNo, String viosPlatformId) {
		Parameters params =  Parameters.with("id.operationName", unitNo+"_"+viosPlatformId).put("id.measurementSeqNum", measSeq);
		delete(params);
		
	}

}
