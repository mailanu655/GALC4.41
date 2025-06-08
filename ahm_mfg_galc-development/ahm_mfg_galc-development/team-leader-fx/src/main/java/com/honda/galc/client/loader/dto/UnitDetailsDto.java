package com.honda.galc.client.loader.dto;

import java.util.ArrayList;
import java.util.List;


public class UnitDetailsDto {

	private String processPoint;
	private String asmProcNo;
	private int procSeqNo;
	private String opeartionName;
	private int opSeqNo;
	private String opType;
	private String processPointName;
	private List<PartDetailsDto> partDetailsList = new ArrayList<PartDetailsDto>();
	
	public UnitDetailsDto() {
		
	}
	
	public UnitDetailsDto(String processPoint, String processPointName, String asmProcNo, int procSeqNo, String opeartionName, int opSeqNo, String opType) {
		this.processPoint = processPoint;
		this.processPointName = processPointName;
		this.asmProcNo = asmProcNo;
		this.procSeqNo = procSeqNo;
		this.opeartionName = opeartionName;
		this.opSeqNo = opSeqNo;
		this.opType = opType;
	}

	public String getProcessPoint() {
		return processPoint;
	}
	public String getAsmProcNo() {
		return asmProcNo;
	}
	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}
	public String getOpeartionName() {
		return opeartionName;
	}
	public void setOpeartionName(String opeartionName) {
		this.opeartionName = opeartionName;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public List<PartDetailsDto> getPartDetailsList() {
		return partDetailsList;
	}
	public void setPartDetailsList(List<PartDetailsDto> partDetailsList) {
		this.partDetailsList = partDetailsList;
	}
	public void setProcessPoint(String processPoint) {
		this.processPoint = processPoint;
	}
	public int getProcSeqNo() {
		return procSeqNo;
	}
	public void setProcSeqNo(int procSeqNo) {
		this.procSeqNo = procSeqNo;
	}
	public int getOpSeqNo() {
		return opSeqNo;
	}
	public void setOpSeqNo(int opSeqNo) {
		this.opSeqNo = opSeqNo;
	}
	public String getProcessPointName() {
		return processPointName;
	}
	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
}
