package com.honda.galc.service;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dto.PartDto;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationMeasurementId;
import com.honda.galc.entity.conf.MCOperationPart;
import com.honda.galc.entity.conf.MCOperationPartId;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrixId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.entity.enumtype.PartCheck;

public class MfgDataLoaderServiceImpl implements MfgDataLoaderService{

	@Transactional
	public void saveMatrix(Set<MCOperationMatrix> opMatrixSet, Set<MCOperationPartMatrix> partMatrixSet) {
		getDao(MCOperationMatrixDao.class).saveAll(new ArrayList<MCOperationMatrix>(opMatrixSet));
		getDao(MCOperationPartMatrixDao.class).saveAll(new ArrayList<MCOperationPartMatrix>(partMatrixSet));
	}
	
	@Transactional
	public void createMfgPart(String operationName, PartDto p, MCOperationPartRevision defaultMFgPart, 
			MCOperationPartRevision refPart){
		 int maxPartId = Integer.valueOf(getDao(MCOperationPartRevisionDao.class).getMaxPartId(operationName));
		 if(maxPartId > 0) {
			 List<MCOperationPartMatrix> insertPartMatrixList = new ArrayList<MCOperationPartMatrix>();
			 List<MCOperationPartMatrix> deletePartMatrixList = new ArrayList<MCOperationPartMatrix>();
			 List<MCOperationMeasurement> insertOperationMeasurements = new ArrayList<MCOperationMeasurement>();
			 String partId = StringUtils.substring(defaultMFgPart.getId().getPartId(), 0,1) + String.format("%04d", (maxPartId + 1));
			 
			//Create MC_OP_PART
			 getDao(MCOperationPartDao.class).insert(convertToOpPartEntity(operationName, partId, refPart));
			 
			//Create MC_OP_PART_REV
			 MCOperationPartRevision newMfgPart = convertToOpPartRevEntity(operationName, partId, p, refPart);
			 getDao(MCOperationPartRevisionDao.class).insert(newMfgPart);
			 
			//CREATE MC_OP_PART_MATRIX
			 List<MCOperationPartMatrix> partMatrixList = getDao(MCOperationPartMatrixDao.class).findAllSpecCodeForOperationPartIdAndPartRev(operationName, refPart.getId().getPartId(), refPart.getId().getPartRevision());
			 for (MCOperationPartMatrix mcOperationPartMatrix : partMatrixList) {
				 insertPartMatrixList.add(generateOpPartMatrix(newMfgPart, mcOperationPartMatrix));
				 deletePartMatrixList.add(generateOpPartMatrix(defaultMFgPart, mcOperationPartMatrix));
			 }
			 getDao(MCOperationPartMatrixDao.class).insertAll(insertPartMatrixList);
			
			//DELETE MC_OP_PART_MATRIX from default Mfg
			 getDao(MCOperationPartMatrixDao.class).removeAll(deletePartMatrixList);
			 
			//Create MC_OP_MEAS_TBX
			 List<MCOperationMeasurement> measurements = getDao(MCOperationMeasurementDao.class).findAllByOperationNamePartIdAndRevision(operationName, defaultMFgPart.getId().getPartId(), refPart.getId().getPartRevision());
			 for (MCOperationMeasurement mcOperationMeasurement : measurements) {
				 insertOperationMeasurements.add(generateMeasurements(newMfgPart, mcOperationMeasurement));
			 }
			 getDao(MCOperationMeasurementDao.class).insertAll(insertOperationMeasurements);
		 }
	}
	
	@Transactional
	public void addMeasurement(String opName, String partId, int partRev, int qty,
			String minLimit, String maxLimit, int maxAttempts, String pset, String tool) {
		getDao(MCOperationMeasurementDao.class).deleteBy(opName, partId, partRev, -1);
		for(int i = 1; i <= qty; i++) {
			getDao(MCOperationMeasurementDao.class).insert(convertToMeasEntity(opName, partId, partRev, i, minLimit, maxLimit, maxAttempts, pset, tool));
		}
	}
	
	private MCOperationMeasurement convertToMeasEntity(String opName, String partId, int partRev, int measSeq, String minLimit, String maxLimit, int maxAtt, String pset, String tool){
		MCOperationMeasurement meas = new MCOperationMeasurement();
		MCOperationMeasurementId id = new MCOperationMeasurementId();
		id.setOperationName(opName);
		id.setPartId(partId);
		id.setPartRevision(partRev);
		id.setOpMeasSeqNum(measSeq);
		meas.setId(id);
		meas.setDeviceId(tool);
		meas.setDeviceMsg(pset);
		meas.setMinLimit(Double.parseDouble(minLimit));
		meas.setMaxLimit(Double.parseDouble(maxLimit));
		meas.setMaxAttempts(maxAtt);
		return meas;
	}
	
	private MCOperationMeasurement generateMeasurements(MCOperationPartRevision mfgPart,
			MCOperationMeasurement mcOperationMeasurement) {
		 MCOperationMeasurement measurement = new MCOperationMeasurement();
		 MCOperationMeasurementId measurementId = new MCOperationMeasurementId();
		 measurementId.setOperationName(mfgPart.getId().getOperationName());
		 measurementId.setPartId(mfgPart.getId().getPartId());
		 measurementId.setPartRevision(mfgPart.getId().getPartRevision());
		 measurementId.setOpMeasSeqNum(mcOperationMeasurement.getId().getMeasurementSeqNum());
		 measurement.setId(measurementId);
		 measurement.setType(mcOperationMeasurement.getType());
		 measurement.setView(mcOperationMeasurement.getView());
		 measurement.setProcessor(mcOperationMeasurement.getProcessor());
		 measurement.setDeviceId(mcOperationMeasurement.getDeviceId());
		 measurement.setDeviceMsg(mcOperationMeasurement.getDeviceMsg());
		 measurement.setMinLimit(mcOperationMeasurement.getMinLimit());
		 measurement.setMaxLimit(mcOperationMeasurement.getMaxLimit());
		 measurement.setMaxAttempts(mcOperationMeasurement.getMaxAttempts());
		 measurement.setTime(mcOperationMeasurement.getTime());
		 measurement.setCheck(mcOperationMeasurement.getCheck());
		 return measurement;
	}

	private MCOperationPartMatrix generateOpPartMatrix(MCOperationPartRevision mfgPart, MCOperationPartMatrix mcOperationPartMatrix) {
		 MCOperationPartMatrix partMatrix = new MCOperationPartMatrix();
		 MCOperationPartMatrixId partMatrixId = new MCOperationPartMatrixId();
		 partMatrixId.setOperationName(mfgPart.getId().getOperationName());
		 partMatrixId.setPartId(mfgPart.getId().getPartId());
		 partMatrixId.setPartRevision(mfgPart.getId().getPartRevision());
		 partMatrixId.setSpecCodeMask(mcOperationPartMatrix.getId().getSpecCodeMask());
		 partMatrixId.setSpecCodeType(mcOperationPartMatrix.getId().getSpecCodeType());
		 partMatrix.setId(partMatrixId);
		 return partMatrix;
	}

	private MCOperationPart convertToOpPartEntity(String operationName, String partId, MCOperationPartRevision refPart) {
		MCOperationPart operationPart = new MCOperationPart();
		MCOperationPartId id = new MCOperationPartId();
		id.setOperationName(operationName);
		id.setPartId(partId);
		operationPart.setId(id);
		operationPart.setPartRev(refPart.getId().getPartRevision());
		return operationPart; 
	}
	
	private MCOperationPartRevision convertToOpPartRevEntity(String operationName, String partId, PartDto p, MCOperationPartRevision refPart) {
				
		MCOperationPartRevision operationPartRevision = new MCOperationPartRevision();
		MCOperationPartRevisionId id = new MCOperationPartRevisionId();
		id.setOperationName(operationName);
		id.setPartId(partId);
		id.setPartRevision(refPart.getId().getPartRevision());
		operationPartRevision.setId(id);
		operationPartRevision.setRevisionId(refPart.getRevisionId());
		operationPartRevision.setPartDesc(refPart.getPartDesc());
		operationPartRevision.setPartType(PartType.MFG);
		operationPartRevision.setDeviceId(refPart.getDeviceId());
		operationPartRevision.setDeviceMsg(refPart.getDeviceMsg());
		operationPartRevision.setPartNo(refPart.getPartNo());
		operationPartRevision.setPartSectionCode(refPart.getPartSectionCode());
		operationPartRevision.setPartItemNo(refPart.getPartItemNo());
		operationPartRevision.setPartMask(p.getPartMask());
		operationPartRevision.setPartCheck(PartCheck.DEFAULT);
		operationPartRevision.setApproved(new Timestamp(System.currentTimeMillis()));
		operationPartRevision.setDeprecatedRevisionId(0);
		return operationPartRevision;
	}
	
	@Transactional
	public void addMeasurementByQty(String opName, String partId, int partRev, int qty, String minLimit, String maxLimit,
			int maxAttempts, String pset, String tool, int measSeq) {
		getDao(MCOperationMeasurementDao.class).deleteBy(opName, partId, partRev, measSeq);
		if(measSeq < 0) {
			for (int i = 1; i <= qty; i++) {
				getDao(MCOperationMeasurementDao.class).insert(
						convertToMeasEntity(opName, partId, partRev, i, minLimit, maxLimit, maxAttempts, pset, tool));
				
			}
		} else
			getDao(MCOperationMeasurementDao.class).insert(
					convertToMeasEntity(opName, partId, partRev, measSeq, minLimit, maxLimit, maxAttempts, pset, tool));
	}
	
}
