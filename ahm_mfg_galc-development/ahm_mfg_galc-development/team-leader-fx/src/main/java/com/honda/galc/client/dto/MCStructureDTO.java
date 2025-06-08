package com.honda.galc.client.dto;

import java.io.Serializable;

public class MCStructureDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String opName;
	private String opRev;
	private String processPoint;
	private String partRev;
	private String partId;
	private String revision;
	private String productSpecCode;
	
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getOpRev() {
		return opRev;
	}
	public void setOpRev(String opRev) {
		this.opRev = opRev;
	}
	public String getProcessPoint() {
		return processPoint;
	}
	public void setProcessPoint(String processPoint) {
		this.processPoint = processPoint;
	}
	public String getPartRev() {
		return partRev;
	}
	public void setPartRev(String partRev) {
		this.partRev = partRev;
	}
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getProductSpecCode() {
		return productSpecCode;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
}