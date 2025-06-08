package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.MassMessageSeverity;


/**
 * The persistent class for the MASS_MESSAGE_TBX database table.
 * 
 */
@Entity
@Table(name="MASS_MESSAGE_TBX")
public class MassMessage extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MassMessageId id;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="MASS_MESSAGE")
	private String massMessage;

	@Column(name="MASS_MESSAGE_SEVERITY")
	private String massMessageSeverity;
	

    public MassMessage() {
    }
    
    public MassMessage(MassMessageId massMessageId, String massMessageStr, String massMessageSeverity, String associateNo){
    	this.setId(massMessageId);
    	this.setMassMessage(massMessageStr);
    	this.setAssociateNo(associateNo);
    	this.setMassMessageSeverity(massMessageSeverity);
    }

	public MassMessageId getId() {
		return this.id;
	}
	
	public void setId(MassMessageId id) {
		this.id = id;
	}
	
	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getMassMessage() {
		return StringUtils.trim(this.massMessage);
	}

	public void setMassMessage(String massMessage) {
		this.massMessage = massMessage;
	}

	public String getMassMessageSeverity() {
		return StringUtils.trim(this.massMessageSeverity);
	}

	public void setMassMessageSeverity(String massMessageSeverity) {
		this.massMessageSeverity = massMessageSeverity;
	}
	
	public MassMessageSeverity getMessageSeverity(){
		return MassMessageSeverity.valueOf(getMassMessageSeverity());
	}

}