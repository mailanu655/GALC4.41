package com.honda.galc.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */

public class ProcessPpeImageDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int maintenanceId;
	
	private short imageSeqNo;

	private String deptCode;

	private byte[] image;

	private Timestamp imageTimestamp;

	private String plantLocCode;

	private String potentialHazard;

	private int ppeId;

	private String ppeRequired;

	private String ppeUsage;


    public ProcessPpeImageDto() {}


	public int getMaintenanceId() {
		return maintenanceId;
	}


	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}


	public short getImageSeqNo() {
		return imageSeqNo;
	}


	public void setImageSeqNo(short imageSeqNo) {
		this.imageSeqNo = imageSeqNo;
	}


	public String getDeptCode() {
		return deptCode;
	}


	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}


	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}


	public Timestamp getImageTimestamp() {
		return imageTimestamp;
	}


	public void setImageTimestamp(Timestamp imageTimestamp) {
		this.imageTimestamp = imageTimestamp;
	}


	public String getPlantLocCode() {
		return plantLocCode;
	}


	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}


	public String getPotentialHazard() {
		return potentialHazard;
	}


	public void setPotentialHazard(String potentialHazard) {
		this.potentialHazard = potentialHazard;
	}


	public int getPpeId() {
		return ppeId;
	}


	public void setPpeId(int ppeId) {
		this.ppeId = ppeId;
	}


	public String getPpeRequired() {
		return ppeRequired;
	}


	public void setPpeRequired(String ppeRequired) {
		this.ppeRequired = ppeRequired;
	}


	public String getPpeUsage() {
		return ppeUsage;
	}


	public void setPpeUsage(String ppeUsage) {
		this.ppeUsage = ppeUsage;
	}


}