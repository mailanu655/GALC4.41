package com.honda.galc.dto;

import java.io.Serializable;

public class TrainingDataDto  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int ProcessId;
	private String  ProcessNum;
	private int TrainingStatus;
	private String ModelYear;
	private String LineNum;
	private String TeamId;
	private String ProcessDescription;
	private String PlantLocationCode;
	private String DeptCode;
	private Integer ProdRate;
	private Integer Inactive_days;
	public int getProcessId() {
		return ProcessId;
	}
	public void setProcessId(int processId) {
		ProcessId = processId;
	}
	public String getProcessNum() {
		return ProcessNum;
	}
	public void setProcessNum(String processNum) {
		ProcessNum = processNum;
	}
	
	public int getTrainingStatus() {
		return TrainingStatus;
	}
	public void setTrainingStatus(int trainingStatus) {
		TrainingStatus = trainingStatus;
	}
	public String getModelYear() {
		return ModelYear;
	}
	public void setModelYear(String modelYear) {
		ModelYear = modelYear;
	}
	public String getLineNum() {
		return LineNum;
	}
	public void setLineNum(String lineNum) {
		LineNum = lineNum;
	}
	public String getTeamId() {
		return TeamId;
	}
	public void setTeamId(String teamId) {
		TeamId = teamId;
	}
	public String getProcessDescription() {
		return ProcessDescription;
	}
	public void setProcessDescription(String processDescription) {
		ProcessDescription = processDescription;
	}
	public String getPlantLocationCode() {
		return PlantLocationCode;
	}
	public void setPlantLocationCode(String plantLocationCode) {
		PlantLocationCode = plantLocationCode;
	}
	public String getDeptCode() {
		return DeptCode;
	}
	public void setDeptCode(String deptCode) {
		DeptCode = deptCode;
	}
	
	public Integer getProdRate() {
		return ProdRate;
	}
	public void setProdRate(Integer prodRate) {
		ProdRate = prodRate;
	}
	public Integer getInactive_days() {
		return Inactive_days;
	}
	public void setInactive_days(Integer inactive_days) {
		Inactive_days = inactive_days;
	}
	
	
	
}
