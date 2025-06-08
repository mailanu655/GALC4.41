package com.honda.galc.client.dto;

import com.honda.galc.dto.MCRevisionDto;
import com.honda.galc.entity.conf.MCRevision;

import javafx.beans.property.SimpleStringProperty;

public class MCRevisionDTO {
	private long id;
	private SimpleStringProperty associateNo;
	private SimpleStringProperty description;
	private SimpleStringProperty status;
	private SimpleStringProperty type;
	private String controlNo;
	private MCRevision entity;
	
	public MCRevisionDTO(MCRevision rev) {
		this.id = rev.getId();
		this.associateNo = new SimpleStringProperty(rev.getAssociateNo()!=null ? rev.getAssociateNo() : "");
		this.description = new SimpleStringProperty(rev.getDescription()!=null ? rev.getDescription() : "");
		this.status = new SimpleStringProperty(rev.getStatus()!=null ? rev.getStatus() : "");
		this.type = new SimpleStringProperty(rev.getType()!=null ? rev.getType() : "");
		this.entity = rev;
	}
	
	public MCRevisionDTO(MCRevisionDto rev) {
		this.id = rev.getRevId();
		this.associateNo = new SimpleStringProperty(rev.getAssociateNo()!=null ? rev.getAssociateNo() : "");
		this.description = new SimpleStringProperty(rev.getRevDesc()!=null ? rev.getRevDesc() : "");
		this.status = new SimpleStringProperty(rev.getRevStatus()!=null ? rev.getRevStatus() : "");
		this.type = new SimpleStringProperty(rev.getRevType()!=null ? rev.getRevType() : "");
		this.controlNo = String.valueOf(rev.getControlNo());
	}
	
	public MCRevisionDTO(String associateNo, String description, String status,
			String type) {
		this.associateNo = new SimpleStringProperty(associateNo);
		this.description = new SimpleStringProperty(description);
		this.status = new SimpleStringProperty(status);
		this.type = new SimpleStringProperty(type);
		
		this.entity = new MCRevision();
		entity.setAssociateNo(associateNo);
		entity.setDescription(description);
		entity.setStatus(status);
		entity.setType(type);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getAssociateNo() {
        return associateNo.get();
    }

    public void setAssociateNo(String associateNo) {
    	this.associateNo.set(associateNo);
    }

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public String getType() {
		return type.get();
	}

	public void setType(String type) {
		this.type.set(type);
	}
	
	public MCRevision getEntity() {
		return entity;
	}

	public void setEntity(MCRevision entity) {
		this.entity = entity;
	}

	public SimpleStringProperty statusProperty() {
        return status;
    }

	public String getControlNo() {
		return controlNo;
	}

	public void setControlNo(String controlNo) {
		this.controlNo = controlNo;
	}


	
}