package com.honda.galc.client.teamlead.checker;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.MCAppCheckerDao;
import com.honda.galc.dao.conf.MCMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCOperationCheckerDao;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.conf.MCPddaUnitRevisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.dto.CopyCheckerDetailsDto;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCAppCheckerId;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationCheckerId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
import com.honda.galc.entity.conf.MCPddaUnitRevision;
import com.honda.galc.entity.conf.ProcessPoint;

public class CheckerConfigModel extends AbstractModel {
	
	private String divisionId;
	
	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public List<ApplicationCheckerDto> loadAppliationChecker(String applicationId, String checker, String divisionId){
		return getDao(MCAppCheckerDao.class).findAllByApplicationAndChecker(applicationId, checker, divisionId);
	}
	
	public List<ApplicationCheckerDto> loadAppliationChecker(String applicationId, String checker){
		return getDao(MCAppCheckerDao.class).findAllByApplicationAndChecker(applicationId, checker, getDivisionId());
	}
	
	public List<OperationCheckerDto> loadOperationChecker(String opName, String checker, String divisionId){
		return getDao(MCOperationCheckerDao.class).findAllByOperationNameAndChecker(opName, checker, divisionId);
	}
	
	public List<OperationCheckerDto> loadOperationChecker(String opName, String checker){
		return getDao(MCOperationCheckerDao.class).findAllByOperationNameAndChecker(opName, checker, getDivisionId());
	}
	
	public List<PartCheckerDto> loadOperationPartChecker(String operationName, String basePartNo, String checker, String divisionId){
		return getDao(MCPartCheckerDao.class).findAllByOperationNamePartNoAndChecker(operationName, basePartNo, checker, divisionId);
	}
	
	public List<PartCheckerDto> loadOperationPartChecker(String operationName, String basePartNo, String checker){
		return getDao(MCPartCheckerDao.class).findAllByOperationNamePartNoAndChecker(operationName, basePartNo, checker, getDivisionId());
	}
	
	public List<MeasurementCheckerDto> loadMeasurementChecker(String operationName, String basePartNo, String checker, String divisionId){
		return getDao(MCMeasurementCheckerDao.class).findAllByOperationNamePartNoAndChecker(operationName, basePartNo, checker, divisionId);
	}
	
	public List<MeasurementCheckerDto> loadMeasurementChecker(String operationName, String basePartNo, String checker){
		return getDao(MCMeasurementCheckerDao.class).findAllByOperationNamePartNoAndChecker(operationName, basePartNo, checker, getDivisionId());
	}
	
	public List<PartDetailsDto> loadPartByOperationPartAndPartType(String operationName, String PartNo, String partType){
		return getDao(MCOperationPartRevisionDao.class).findAllByOperationPartAndPartType(operationName, PartNo, partType);
	}
	
	public List<PartDetailsDto> loadOperationRevisionAndPartIdBy(String operationName, String PartNo, String partSectionCode, 
			String partItemNo, String partType, String revisions, boolean active) {
		return getDao(MCOperationRevisionDao.class).loadOperationRevisionAndPartIdBy(operationName, PartNo, partSectionCode, 
				partItemNo, partType, revisions, active);
	}
	
	public List<PartDetailsDto> loadPartByOpPartTypeAndMeasurement(String operationName, String PartNo, String partType){
		return getDao(MCOperationPartRevisionDao.class).findAllByPartByOpPartTypeAndMeasurement(operationName, PartNo, partType);
	}
	
	public List<ProcessPoint> findAll() {
		return getDao(ProcessPointDao.class).findAll();
	}
	
	public List<MCAppChecker> loadAllAppliationChecker(){
		return getDao(MCAppCheckerDao.class).findAll();
	}
	
	public ProcessPoint findAllProcessPointById(String processPointId) {
		return getDao(ProcessPointDao.class).findById(processPointId);
	}
	
	public List<String> findAllOperation(String opName) {
		return getDao(MCOperationRevisionDao.class).findByOperationName(opName);
	}
	
	public List<String> findAllOperationChecker(String opName) {
		return getDao(MCOperationCheckerDao.class).findByOperationName(opName);
	}
	
	public List<String> findAllParts(String partNo, String opName) {
		return getDao(MCOperationPartRevisionDao.class).findAllPartNoByOperationName(partNo, opName);
	}
	
	public List<String> findAllPartOperation(String opName) {
		return getDao(MCOperationPartRevisionDao.class).findAllByOperationName(opName);
	}
	
	public List<String> findAllMeasurementOperation(String opName) {
		return getDao(MCOperationMeasurementDao.class).findAllByOperationName(opName);
	}
	
	public List<MCOperationRevision> findAllByOperationName(String operationName) {
		return getDao(MCOperationRevisionDao.class).findAllByOperationName(operationName);
	}
	
	public void updateApplicationChecker(MCAppChecker appChecker, MCAppChecker updateAppChecker) {
		getDao(MCAppCheckerDao.class).removeAndInsert(updateAppChecker, appChecker);
	}
	
	public void updateOperationChecker(List<MCOperationChecker> updateOpCheckersList, List<MCOperationChecker> operationCheckers) {
		getDao(MCOperationCheckerDao.class).removeAndInsertAll(updateOpCheckersList, operationCheckers);
	}
	
	public void updatePartChecker(List<MCPartChecker> updatePartCheckersList, List<MCPartChecker> removePartCheckers) {
		getDao(MCPartCheckerDao.class).removeAndInsertAll(updatePartCheckersList, removePartCheckers);
	}
	
	public void updateMeasurementChecker(List<MCMeasurementChecker> updateMeasCheckersList, List<MCMeasurementChecker> measurementCheckers) {
		getDao(MCMeasurementCheckerDao.class).removeAndInsertAll(updateMeasCheckersList, measurementCheckers);
	}
	
	public List<Integer> loadActiveOperationRevision(String operationName) {
		return getDao(MCOperationRevisionDao.class).findAllActiveByOperationName(operationName);
	}
	
	public List<MCOperationRevision> findAllByOperationAndRevisions(String operationName, List<Integer> revisionIds, boolean active) {
		return getDao(MCOperationRevisionDao.class).findAllByOperationAndRevisions(operationName, revisionIds, active);
	}
	
	public MCOperationPartRevision findAllByPartNoSecCodeItemNoAndType(String opName, String partNo, String partItemNo, String partSectionCode, PartType partType) {
		return getDao(MCOperationPartRevisionDao.class).findAllByPartNoSecCodeItemNoAndType(opName, partNo, partItemNo, partSectionCode, partType);
	}
	
	public List<MCOperationRevision> loadAllOperationRevisions(String operationName) {
		return getDao(MCOperationRevisionDao.class).findAllByOperationName(operationName);
	}
	
	public List<MCOperationChecker> findAllOpCheckerOperationRevision(String operationName, String checkPoint, int checkSeq, List<Integer> opRevList) {
		return getDao(MCOperationCheckerDao.class).findAllBy(operationName, checkPoint, checkSeq, opRevList);
	}
	
	public List<MCPartChecker> loadAllPartCheckerOpRev(String operationName, String partId, String checkPoint, int checkSeq, List<Integer> opRevList) {
		return getDao(MCPartCheckerDao.class).findAllOpRevBy(operationName, partId, checkPoint, checkSeq, opRevList);
	}
	
	public List<MCMeasurementChecker> loadAllMeasurementCheckerOpRev(String operationName, String partId, String checkPoint, 
			int checkSeq, int measurementSeqNumber, String checkName) {
		return getDao(MCMeasurementCheckerDao.class).findAllOpRevBy(operationName, partId, checkPoint, checkSeq, measurementSeqNumber, checkName);
	}
	
	public List<MCPartChecker> findAllPartCheckerBy(PartCheckerDto partCheckerDto) {
		return getDao(MCPartCheckerDao.class).findAllPartCheckerBy(partCheckerDto);
	}
	
	public List<MCMeasurementChecker> findAllMeasurementCheckerBy(MeasurementCheckerDto measCheckerDto) {
		return getDao(MCMeasurementCheckerDao.class).findAllCheckerBy(measCheckerDto);
	}
	
	public List<String> loadAllPartCheckerOpName(String operationName) {
		return getDao(MCPartCheckerDao.class).loadAllPartCheckerOpName(operationName);
	}
	
	public List<String> loadAllMeasurementCheckerOpName(String operationName) {
		return getDao(MCMeasurementCheckerDao.class).loadAllMeasurementCheckerOpName(operationName);
	}
	
	public List<MCMeasurementChecker> loadAllMeasCheckerBy(MeasurementCheckerDto measCheckerDto, List<Integer> opRevList) {
		return getDao(MCMeasurementCheckerDao.class).findAllBy(measCheckerDto, opRevList);
	}
	
	public void addOperationChecker(MCOperationChecker opCheckers) {
		getDao(MCOperationCheckerDao.class).save(opCheckers);
	}
	
	public int getMaxCheckSequence(String applicationId, String checkPoint) {
		return getDao(MCAppCheckerDao.class).getMaxCheckSeqBy(applicationId, checkPoint);
	}
	
	public void addAppChecker(MCAppChecker appCheckers) {
		getDao(MCAppCheckerDao.class).insert(appCheckers);
	}
	
	public MCAppChecker findAppCheckerById(MCAppCheckerId id) {
		return getDao(MCAppCheckerDao.class).findByKey(id);
	}
	
	public MCOperationChecker findOperationCheckerById(MCOperationCheckerId id) {
		return getDao(MCOperationCheckerDao.class).findByKey(id);
	}
	
	public void addPartChecker(MCPartChecker partCheckers) {
		getDao(MCPartCheckerDao.class).insert(partCheckers);
	}
	
	public MCPartChecker findPartCheckerById(MCPartCheckerId id) {
		return getDao(MCPartCheckerDao.class).findByKey(id);
	}
	
	public void addMeasurementChecker(MCMeasurementChecker measCheckers) {
		getDao(MCMeasurementCheckerDao.class).insert(measCheckers);
	}
	
	public MCMeasurementChecker findMeasurementCheckerById(MCMeasurementCheckerId id) {
		return getDao(MCMeasurementCheckerDao.class).findByKey(id);
	}
	
	public void deleteApplicationChecker(List<MCAppChecker> appCheckers) {
		getDao(MCAppCheckerDao.class).removeAll(appCheckers);
	}
	
	public void deleteOpCheckerByOperationNameCheckpointAndSeq(String operationName, String checkPoint, int checkSeq) {
		getDao(MCOperationCheckerDao.class).deleteCheckerByOpNameCheckPtAndSeq(operationName, checkPoint, checkSeq);
	}
	
	public void deletePartChecker(List<MCPartChecker> partCheckers) {
		getDao(MCPartCheckerDao.class).removeAll(partCheckers);
	}
	
	public void deleteMeasurementChecker(List<MCMeasurementChecker> measCheckers) {
		getDao(MCMeasurementCheckerDao.class).removeAll(measCheckers);
	}
	
	public void saveCopyApplicationChecker(MCAppChecker appChecker) {
		getDao(MCAppCheckerDao.class).insert(appChecker);
	}
	
	public void saveCopyOperationChecker(MCOperationChecker opChecker) {
		getDao(MCOperationCheckerDao.class).insert(opChecker);
	}

	public void copyPartChecker(MCPartChecker partChecker) {
		getDao(MCPartCheckerDao.class).insert(partChecker);
	}
	
	public void copyMeasurementChecker(MCMeasurementChecker measChecker) {
		getDao(MCMeasurementCheckerDao.class).insert(measChecker);
	}
	
	public List<String> loadFromPlant() {
		return getDao(MCPddaPlatformDao.class).findAllPlants();
	}
	
	public List<String> loadFromDepartment(String plant) {
		return getDao(MCPddaPlatformDao.class).findAllDeptBy(plant);
	}

	public List<Long> loadFromModelYear(String plant, String dept) {
		return getDao(MCPddaPlatformDao.class).findAllModelYearBy(plant, dept);
	}
	
	public List<Long> loadFromProductionRate(String plantCode, String dept, BigDecimal modelYearDate) {
		return getDao(MCPddaPlatformDao.class).findAllProdQtyBy(plantCode, dept, modelYearDate);
	}

	public List<String> loadFromLineNo(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty) {
		return getDao(MCPddaPlatformDao.class).findAllLineNo(plantCode, dept, modelYearDate, prodSchQty);
	}
	
	public List<String> loadFromVMC(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty, String lineNo) {
		return getDao(MCPddaPlatformDao.class).findAllVMC(plantCode, dept, modelYearDate, prodSchQty, lineNo);
	}
	
	public List<MCOperationChecker> loadOperationCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		return getDao(MCOperationCheckerDao.class).loadOperationCheckerByPddaPlatform(plantLocCode, deptCode, modelYearDate, prodSchQty, prodAsmLineNo, vehicleModelCode);
	}
	
	public MCPddaUnitRevision findByOpNameAndOpRev(String operationName, int opRevId){
		return getDao(MCPddaUnitRevisionDao.class).findBy(operationName, opRevId);
	}
	
	public List<CopyCheckerDetailsDto> findAllByUnitNoAndPddaDetails(String unitNo, String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode, String checker) {
		return getDao(MCPddaUnitRevisionDao.class).findAllByUnitNoAndPddaDetails(unitNo, plantLocCode, deptCode, modelYearDate, 
				prodSchQty, prodAsmLineNo, vehicleModelCode, checker);
	}
	
	public List<PartCheckerDto> loadPartCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		return getDao(MCPartCheckerDao.class).loadPartCheckerByPddaPlatform(plantLocCode, deptCode, modelYearDate, prodSchQty, prodAsmLineNo, vehicleModelCode);
	}
	
	public List<MeasurementCheckerDto> loadMeasurementCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		return getDao(MCMeasurementCheckerDao.class).loadMeasurementCheckerByPddaPlatform(plantLocCode, 
				deptCode, modelYearDate, prodSchQty, prodAsmLineNo, vehicleModelCode);
	}
	
	public List<String> findAllDivision() {
		return getDao(DivisionDao.class).findAllDivisionId();
	}
	
	@Override
	public void reset() {
		
	}

}
