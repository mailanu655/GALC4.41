package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationMeasurementId;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCOperationMeasurementDao 
	extends IDaoService<MCOperationMeasurement, MCOperationMeasurementId>{
	
	public List<MCOperationMeasurement> findAllMeasurementForOperationPartAndPartRevision(String operationName, String partId, int partRevision);
	
	public List<Object[]> findDistinctDeviceId();

	public List<MCOperationMeasurement> findAllByOperationNamePartIdAndRevision(String operationName, String partId, int partRevision);
	
	public void deleteBy(String operationName, String partId, int partRevision, int measSeq);
	
	public List<String> findAllByOperationName(String operationName);
	
	public void saveEntity(MCViosMasterOperationMeasurement mcViosOpMeasObj);
	
	public void deleteMeasurementRevision(int measSeq, String unitNo, String viosPlatformId);
	
}
