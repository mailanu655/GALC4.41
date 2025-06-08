package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author vfc91343
 * @date May 15, 2018 
 * 
 */
@Entity
@Table(name="ADVANCE_PROCESS_CYCLE_TIMER_TBX")
public class AdvanceProcessCycleTimer extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Integer id;
	
	@Column(name="LINE_ID")
	private String lineId;
	
	@Column(name="CYCLE_TIMER")
	private String cycleTimer;

	@Column(name="WARNING_VALUE")
	private Integer warningValue;
	
	@Column(name="ERROR_VALUE")
	private Integer errorValue;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
		

	public AdvanceProcessCycleTimer() {
		super();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getLineId() {
		return lineId;
	}


	public void setLineId(String lineId) {
		this.lineId = lineId;
	}


	public String getCycleTimer() {
		return cycleTimer;
	}


	public void setCycleTimer(String cycleTimer) {
		this.cycleTimer = cycleTimer;
	}


	public Integer getWarningValue() {
		return warningValue;
	}


	public void setWarningValue(Integer warningValue) {
		this.warningValue = warningValue;
	}


	public Integer getErrorValue() {
		return errorValue;
	}


	public void setErrorValue(Integer errorValue) {
		this.errorValue = errorValue;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	
	
}