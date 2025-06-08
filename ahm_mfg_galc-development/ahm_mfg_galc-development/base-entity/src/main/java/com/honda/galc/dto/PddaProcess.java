package com.honda.galc.dto;

import java.io.Serializable;

public class PddaProcess implements Serializable{
	private static final long serialVersionUID = 1L;
	private String asmProcNumber;
	private String asmProcName;
	
	public PddaProcess() {
		super();
	}

	public PddaProcess(String asmProcNumber, String asmProcName) {
		super();
		this.asmProcNumber = asmProcNumber;
		this.asmProcName = asmProcName;
	}

	public String getAsmProcNumber() {
		return asmProcNumber;
	}

	public void setAsmProcNumber(String asmProcNumber) {
		this.asmProcNumber = asmProcNumber;
	}

	public String getAsmProcName() {
		return asmProcName;
	}

	public void setAsmProcName(String asmProcName) {
		this.asmProcName = asmProcName;
	}
}