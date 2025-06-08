package com.honda.galc.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PddaUnitImage implements Serializable {
	private static final long serialVersionUID = 1L;

	private int maintenanceId;
	
	private String plantLocCode;
	
	private String deptCode;
	
	private short imageSeqNo;
	
	private Timestamp imageTimestamp;

	private byte[] image;

	private String unitImage;

    public PddaUnitImage() {
    }

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public short getImageSeqNo() {
		return this.imageSeqNo;
	}

	public void setImageSeqNo(short imageSeqNo) {
		this.imageSeqNo = imageSeqNo;
	}

	public Timestamp getImageTimestamp() {
		return this.imageTimestamp;
	}

	public void setImageTimestamp(Timestamp imageTimestamp) {
		this.imageTimestamp = imageTimestamp;
	}

	public int getMaintenanceId() {
		return this.maintenanceId;
	}

	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public String getPlantLocCode() {
		return this.plantLocCode;
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getUnitImage() {
		return this.unitImage;
	}

	public void setUnitImage(String unitImage) {
		this.unitImage = unitImage;
	}

}