package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ConditionType;
import com.honda.galc.entity.enumtype.DestinationType;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL111TBX")
public class BroadcastDestination extends AuditEntry {
	@EmbeddedId
	private BroadcastDestinationId id;

	@Column(name="DESTINATION_TYPE")
	private int destinationTypeId;

	@Column(name="DESTINATION_ID")
	private String destinationId;

	@Column(name="REQUEST_ID")
	private String requestId;

	private String argument;

	@Column(name="AUTO_ENABLED")
	private boolean autoEnabled;

	@Enumerated(EnumType.STRING)
	@Column(name="CHECK_POINT")
	private CheckPoints checkPoint;
	
	@Column(name="CONDITION")
	private String condition;
	
	@Enumerated(EnumType.STRING)
	@Column(name= "CONDITION_TYPE")
	private ConditionType conditionType;
	
	private static final long serialVersionUID = 1L;

	public BroadcastDestination() {
		super();
	}
	
	public BroadcastDestination(String processPointId, int seq) {
		this.id = new BroadcastDestinationId();
		id.setProcessPointId(processPointId);
		id.setSequenceNumber(seq);
	}

	public BroadcastDestinationId getId() {
		return this.id;
	}

	public void setId(BroadcastDestinationId id) {
		this.id = id;
	}

	public int getDestinationTypeId() {
		return this.destinationTypeId;
	}
	
	public String getDestinationTypeName() {
		return getDestinationType().toString();
	}

	public void setDestinationTypeId(int destinationTypeId) {
		this.destinationTypeId = destinationTypeId;
	}

	public DestinationType getDestinationType() {
		return DestinationType.getType(destinationTypeId);
	}

	public void setDestinationType(DestinationType destinationType) {
		this.destinationTypeId = destinationType.getId();
	}
	public String getDestinationId() {
		return StringUtils.trim(this.destinationId);
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public String getRequestId() {
		return StringUtils.trim(this.requestId);
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getArgument() {
		return StringUtils.trim(this.argument);
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	public String getProcessPointId() {
		return id.getProcessPointId();
	}
	
	public int getSequenceNumber() {
		return id.getSequenceNumber();
	}

	@Override
	public String toString() {
		return toString(id.getProcessPointId(),id.getSequenceNumber());
	}

	public boolean isAutoEnabled() {
		return autoEnabled;
	}

	public void setAutoEnabled(boolean autoEnabled) {
		this.autoEnabled = autoEnabled;
	}
	
	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public CheckPoints getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(CheckPoints checkPoint) {
		this.checkPoint = checkPoint;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
