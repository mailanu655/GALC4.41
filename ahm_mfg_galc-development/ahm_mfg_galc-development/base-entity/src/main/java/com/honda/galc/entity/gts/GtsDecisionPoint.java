package com.honda.galc.entity.gts;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsDecisionPoint Class description</h3>
 * <p> GtsDecisionPoint description </p>
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
@Table(name="GTS_DECISION_POINT_TBX")
public class GtsDecisionPoint extends AuditEntry {
	@EmbeddedId
	private GtsDecisionPointId id;

	@Column(name="DECISION_POINT_NAME")
	private String decisionPointName;

	@Column(name="DECISION_POINT_DESCRIPTION")
	private String decisionPointDescription;

	@Column(name="RULE_CLASS")
	private String ruleClass;

	private int enabled;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="TIME_INTERVAL")
	private int timeInterval;

	private static final long serialVersionUID = 1L;

	public GtsDecisionPoint() {
		super();
	}

	public GtsDecisionPointId getId() {
		return this.id;
	}

	public void setId(GtsDecisionPointId id) {
		this.id = id;
	}
	
	public int getDecisionPointId() {
		 return id.getDecisionPointId();
	}

	public String getDecisionPointName() {
		return this.decisionPointName;
	}

	public void setDecisionPointName(String decisionPointName) {
		this.decisionPointName = decisionPointName;
	}

	public String getDecisionPointDescription() {
		return StringUtils.trim(this.decisionPointDescription);
	}

	public void setDecisionPointDescription(String decisionPointDescription) {
		this.decisionPointDescription = decisionPointDescription;
	}

	public String getRuleClass() {
		return StringUtils.trim(this.ruleClass);
	}

	public void setRuleClass(String ruleClass) {
		this.ruleClass = ruleClass;
	}

	public int getEnabled() {
		return this.enabled;
	}
	
	public boolean isEnabled() {
		return getEnabled() > 0;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled ? 1 : 0;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	 public void setCurrentTimestamp() {
	    this.actualTimestamp = new Timestamp(new Date().getTime());
	 }

	public int getTimeInterval() {
		return this.timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public boolean toBeExecuted(){
        if(!this.isEnabled()) return false;
        if(this.timeInterval <= 0 || actualTimestamp == null) return true;
        return System.currentTimeMillis() - actualTimestamp.getTime() >= timeInterval ;
    }

	public String toString() {
		return toString(getId().getTrackingArea(),getId().getDecisionPointId(),getDecisionPointName(),getEnabled());
	}

}
