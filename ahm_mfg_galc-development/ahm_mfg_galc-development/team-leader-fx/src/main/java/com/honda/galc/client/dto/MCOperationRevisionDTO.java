package com.honda.galc.client.dto;

public class MCOperationRevisionDTO implements Cloneable{
	private String operationName;
	private String opRevision;
	private String revId;
	private String view;
	private String processor;
	private String status;
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getOpRevision() {
		return opRevision;
	}
	public void setOpRevision(String opRevision) {
		this.opRevision = opRevision;
	}
	public String getRevId() {
		return revId;
	}
	public void setRevId(String revId) {
		this.revId = revId;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	 public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Object clone(){  
		    try{  
		        return super.clone();  
		    }catch(Exception e){ 
		        return null; 
		    }
		}
	

}
