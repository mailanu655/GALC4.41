package com.honda.galc.entity.gts;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsDpCondition Class description</h3>
 * <p> GtsDpCondition description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_DP_CONDITION_TBX")
public class GtsDecisionPointCondition extends AuditEntry {
	@EmbeddedId
	private GtsDecisionPointConditionId id;

	@Column(name="REQUIRED_VALUE")
	private int requiredValue;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="TRACKING_AREA", referencedColumnName="TRACKING_AREA"),
		@JoinColumn(name="INDICATOR_ID", referencedColumnName="INDICATOR_NAME")
	})
	private GtsIndicator indicator;

	private static final long serialVersionUID = 1L;

	public GtsDecisionPointCondition() {
		super();
	}
	
	public GtsDecisionPointCondition(String trackingArea,int decisionPointId,String indicatorId){
		id = new GtsDecisionPointConditionId(trackingArea,decisionPointId,indicatorId);
	}
	
	public GtsDecisionPointConditionId getId() {
		return this.id;
	}

	public void setId(GtsDecisionPointConditionId id) {
		this.id = id;
	}
	
	public int getDecisionPointId() {
		return id.getDecisionPointId();
	}

	public int getRequiredValue() {
		return this.requiredValue;
	}
	
	public boolean isRequired() {
		return getRequiredValue() > 0;
	}

	public void setRequiredValue(int requiredValue) {
		this.requiredValue = requiredValue;
	}
	
	public void setRequiredValue(boolean value) {
		this.requiredValue = value ? 1: 0;
	}

	public GtsIndicator getIndicator() {
		return this.indicator;
	}

	public void setIndicator(GtsIndicator indicator) {
		this.indicator = indicator;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getDecisionPointId(),getId().getIndicatorId(),getRequiredValue());
	}

}
