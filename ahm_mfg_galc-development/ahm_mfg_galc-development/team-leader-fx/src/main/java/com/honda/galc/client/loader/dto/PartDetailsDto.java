package com.honda.galc.client.loader.dto;

import java.util.ArrayList;
import java.util.List;

public class PartDetailsDto {

	private String partNumber;
	private String partItemNumber;
	private String partSecCode;
	private String partType;
	private String partMask;
	private String partId;
	private int partRev;
	private List<MeasurementDetailsDto> measurementDetailsList = new ArrayList<MeasurementDetailsDto>();
	
	public PartDetailsDto(){
		
	}
	
	public PartDetailsDto(String partNumber, String partSecCode, String partItemNumber, String partType, String partMask, String partId, int partRev) {
		this.partNumber = partNumber;
		this.partSecCode = partSecCode;
		this.partItemNumber = partItemNumber;
		this.partType = partType;
		this.partMask = partMask;
		this.partId = partId;
		this.partRev = partRev;
	}
	
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartSecCode() {
		return partSecCode;
	}

	public void setPartSecCode(String partSecCode) {
		this.partSecCode = partSecCode;
	}

	public String getPartItemNumber() {
		return partItemNumber;
	}

	public void setPartItemNumber(String partItemNumber) {
		this.partItemNumber = partItemNumber;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getPartMask() {
		return partMask;
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public List<MeasurementDetailsDto> getMeasurementDetailsList() {
		return measurementDetailsList;
	}

	public void setMeasurementDetailsList(
			List<MeasurementDetailsDto> measurementDetailsList) {
		this.measurementDetailsList = measurementDetailsList;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public int getPartRev() {
		return partRev;
	}

	public void setPartRev(int partRev) {
		this.partRev = partRev;
	}
}
