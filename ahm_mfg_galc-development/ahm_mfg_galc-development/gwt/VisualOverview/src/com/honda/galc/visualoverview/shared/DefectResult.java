package com.honda.galc.visualoverview.shared;

import java.util.ArrayList;

import com.honda.galc.visualoverview.shared.feature.DefectFeature;

public class DefectResult {
	private DefectResultId id;

	private String associateNo;

	private String shift;

	private int defectStatus;

	private String imageName;

	private String responsibleDept;

	private String responsibleZone;

	private String repairMethodNamePlan;

	private int repairTimePlan;

	private String repairAssociateNoPlan;

	private String repairAssociateNo;

	private String writeUpDepartment;

	private int twoPartDefectFlag;

	private String iqsCategoryName;

	private String iqsItemName;

	private String regressionCode;

	private int pointX;

	private int pointY;

	private String entryDept;

	private int outstandingFlag;

	private String responsibleLine;

	boolean isNewDefect = false;

	boolean isChangeAtRepair = false;

	boolean engineFiring = false;

	private String entryStation;

	public DefectResultId getId() {
		return id;
	}

	public void setId(DefectResultId id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return associateNo;
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public int getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(int defectStatus) {
		this.defectStatus = defectStatus;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getResponsibleDept() {
		return responsibleDept;
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public String getResponsibleZone() {
		return responsibleZone;
	}

	public void setResponsibleZone(String responsibleZone) {
		this.responsibleZone = responsibleZone;
	}

	public String getRepairMethodNamePlan() {
		return repairMethodNamePlan;
	}

	public void setRepairMethodNamePlan(String repairMethodNamePlan) {
		this.repairMethodNamePlan = repairMethodNamePlan;
	}

	public int getRepairTimePlan() {
		return repairTimePlan;
	}

	public void setRepairTimePlan(int repairTimePlan) {
		this.repairTimePlan = repairTimePlan;
	}

	public String getRepairAssociateNoPlan() {
		return repairAssociateNoPlan;
	}

	public void setRepairAssociateNoPlan(String repairAssociateNoPlan) {
		this.repairAssociateNoPlan = repairAssociateNoPlan;
	}

	public String getRepairAssociateNo() {
		return repairAssociateNo;
	}

	public void setRepairAssociateNo(String repairAssociateNo) {
		this.repairAssociateNo = repairAssociateNo;
	}

	public String getWriteUpDepartment() {
		return writeUpDepartment;
	}

	public void setWriteUpDepartment(String writeUpDepartment) {
		this.writeUpDepartment = writeUpDepartment;
	}

	public int getTwoPartDefectFlag() {
		return twoPartDefectFlag;
	}

	public void setTwoPartDefectFlag(int twoPartDefectFlag) {
		this.twoPartDefectFlag = twoPartDefectFlag;
	}

	public String getIqsCategoryName() {
		return iqsCategoryName;
	}

	public void setIqsCategoryName(String iqsCategoryName) {
		this.iqsCategoryName = iqsCategoryName;
	}

	public String getIqsItemName() {
		return iqsItemName;
	}

	public void setIqsItemName(String iqsItemName) {
		this.iqsItemName = iqsItemName;
	}

	public String getRegressionCode() {
		return regressionCode;
	}

	public void setRegressionCode(String regressionCode) {
		this.regressionCode = regressionCode;
	}

	public int getPointX() {
		return pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public String getEntryDept() {
		return entryDept;
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	public int getOutstandingFlag() {
		return outstandingFlag;
	}

	public void setOutstandingFlag(int outstandingFlag) {
		this.outstandingFlag = outstandingFlag;
	}

	public String getResponsibleLine() {
		return responsibleLine;
	}

	public void setResponsibleLine(String responsibleLine) {
		this.responsibleLine = responsibleLine;
	}

	public boolean isNewDefect() {
		return isNewDefect;
	}

	public void setNewDefect(boolean isNewDefect) {
		this.isNewDefect = isNewDefect;
	}

	public boolean isChangeAtRepair() {
		return isChangeAtRepair;
	}

	public void setChangeAtRepair(boolean isChangeAtRepair) {
		this.isChangeAtRepair = isChangeAtRepair;
	}

	public boolean isEngineFiring() {
		return engineFiring;
	}

	public void setEngineFiring(boolean engineFiring) {
		this.engineFiring = engineFiring;
	}

	public String getEntryStation() {
		return entryStation;
	}

	public void setEntryStation(String entryStation) {
		this.entryStation = entryStation;
	}
	
	public DefectFeature toFeature()
	{
		DefectFeature feature = new DefectFeature();
		
		feature.setFeatureId(Integer.toString(id.getDefectResultId()));
		feature.setFeatureType("DEFECT");
		feature.setEnableHistory(0);
		feature.setReferenceId("");
		feature.setReferenceType(id.getDefectTypeName().trim());
		feature.setFeaturePoints(new ArrayList<FeaturePoints>());
		FeaturePoints featurePoint = new FeaturePoints();
		FeaturePointsId fpId = new FeaturePointsId();
		featurePoint.setId(fpId);
		featurePoint.getId().setFeatureId(Integer.toString(id.getDefectResultId()));
		featurePoint.getId().setFeatureSeq(0);
		featurePoint.setXCoordinate(pointX);
		featurePoint.setYCoordinate(pointY);
		featurePoint.setZCoordinate(0);
		featurePoint.setSpatialReferenceSystem("CUSTOM_SRS");
		featurePoint.setChildFeatureId("");
		feature.getFeaturePoints().add(featurePoint);
		
		
		
		return feature;
		
	}
	
	

}
