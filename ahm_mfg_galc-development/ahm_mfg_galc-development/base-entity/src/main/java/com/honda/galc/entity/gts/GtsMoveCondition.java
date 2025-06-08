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
 * <h3>GtsMoveCondition Class description</h3>
 * <p> GtsMoveCondition description </p>
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
@Table(name="GTS_MOVE_CONDITION_TBX")
public class GtsMoveCondition extends AuditEntry {
	@EmbeddedId
	private GtsMoveConditionId id;

	@Column(name="REQUIRED_VALUE")
	private int requiredValue;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="TRACKING_AREA", referencedColumnName="TRACKING_AREA"),
		@JoinColumn(name="INDICATOR_ID", referencedColumnName="INDICATOR_NAME")
	})
	private GtsIndicator indicator;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="TRACKING_AREA", referencedColumnName="TRACKING_AREA"),
		@JoinColumn(name="SOURCE_LANE_ID", referencedColumnName="SOURCE_LANE_ID"),
		@JoinColumn(name="DESTINATION_LANE_ID", referencedColumnName="DESTINATION_LANE_ID")
	})
	private GtsMove move;

	private static final long serialVersionUID = 1L;

	public GtsMoveCondition() {
		super();
	}
	
	public GtsMoveCondition(String trackingArea,String soureLaneId,String destinationLaneId,String indicatorId) {
		id = new GtsMoveConditionId(trackingArea,soureLaneId,destinationLaneId,indicatorId);
	}
		
	public GtsMoveConditionId getId() {
		return this.id;
	}

	public void setId(GtsMoveConditionId id) {
		this.id = id;
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
		this.requiredValue = value ? 1 : 0;
	}

	public GtsIndicator getIndicator() {
		return this.indicator;
	}

	public void setIndicator(GtsIndicator indicator) {
		this.indicator = indicator;
	}

	public GtsMove getMove() {
		return this.move;
	}

	public void setMove(GtsMove move) {
		this.move = move;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getSourceLaneId(),getId().getDestinationLaneId(),
				getId().getIndicatorId(),getRequiredValue());
	}

}
