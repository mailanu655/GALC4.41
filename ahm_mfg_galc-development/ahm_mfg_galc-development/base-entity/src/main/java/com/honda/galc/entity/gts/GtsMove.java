package com.honda.galc.entity.gts;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
/**
 * 
 * 
 * <h3>GtsMove Class description</h3>
 * <p> GtsMove description </p>
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
@Table(name="GTS_MOVE_TBX")
public class GtsMove extends AuditEntry {
	@EmbeddedId
	private GtsMoveId id;

	@Column(name="STATUS")
	private int statusId = GtsMoveStatus.FINISHED.getId();

	@Column(name="DP_ID")
	private int decisionPointId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;
    
	private static final int DEFAULT_TIME_INTERVAL = 30;  // seconds
	
	private static final long serialVersionUID = 1L;

	public GtsMove() {
		super();
	}
	
	public GtsMove(GtsIndicator indicator) {
		id = new GtsMoveId();
		id.setTrackingArea(indicator.getId().getTrackingArea());
		id.setSourceLaneId(indicator.getSourceLaneName());
		id.setDestinationLaneId(indicator.getDestLaneName());
		setMoveStatus(indicator.isStatusOn()? GtsMoveStatus.STARTED : GtsMoveStatus.FINISHED);
	}
	
	public GtsMove(String trackingArea,String sourceLaneId,String destnationLaneId) {
		id = new GtsMoveId(trackingArea,sourceLaneId,destnationLaneId);
	}

	public GtsMoveId getId() {
		return this.id;
	}

	public void setId(GtsMoveId id) {
		this.id = id;
	}

	public int getStatusId() {
		return this.statusId;
	}

	public void setStatus(int statusId) {
		this.statusId = statusId;
	}
	
	public GtsMoveStatus getMoveStatus() {
		return GtsMoveStatus.getType(getStatusId());
	}
	
	public void setMoveStatus(GtsMoveStatus moveStatus) {
		this.statusId = moveStatus.getId();
		this.actualTimestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public boolean isCreated() {
		return statusId == GtsMoveStatus.CREATED.getId();
	}
	
	public boolean isStarted() {
	    return statusId == GtsMoveStatus.STARTED.getId();
	}
	
	public boolean isFinished(){
		return statusId == GtsMoveStatus.FINISHED.getId() || statusId == GtsMoveStatus.EXPIRED.getId();
	}
	
	 /**
     * check if the move expires
     * when a move request is created, move status is changed to "Created" and the current timestamp is updated by 
     * database. 
     * @param seconds
     * @return
     */
    
    public boolean isMoveExpired(int seconds){
        if(statusId != GtsMoveStatus.CREATED.getId()) return false;
        if(actualTimestamp == null) return true;
		return System.currentTimeMillis() - actualTimestamp.getTime() > seconds * 1000;
    }
    
    public boolean isMoveExpired(){
        return isMoveExpired(DEFAULT_TIME_INTERVAL);
    }

	public int getDecisionPointId() {
		return this.decisionPointId;
	}

	public void setDecisionPointId(int dpId) {
		this.decisionPointId = dpId;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	public String getSource() {
		return id != null ? id.getSourceLaneId() : null;
	}
	
	public String getDestination() {
		return id != null ? id.getDestinationLaneId() : null;
	}
	
	public String getIoPointName(){
        return "MR-"+getSource()+"-"+this.getDestination();
    }

	public String toString() {
		return toString(getId().getTrackingArea(),getId().getSourceLaneId(),getId().getDestinationLaneId(),getStatusId());
	}

}
