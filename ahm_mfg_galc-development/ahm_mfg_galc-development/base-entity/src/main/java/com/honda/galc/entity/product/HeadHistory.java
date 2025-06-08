package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="HEAD_HISTORY_TBX")
public class HeadHistory extends ProductHistory {
	
	@EmbeddedId
	private HeadHistoryId id;


	private static final long serialVersionUID = 1L;

	public HeadHistory() {
		super();
	}

    public HeadHistory(Head head, String processPointId) {
    	this(head.getProductId(), processPointId);
    }
    
	public HeadHistory(String productId,String processPointId) {
		id = new HeadHistoryId(productId,processPointId);
	}
	
	public HeadHistoryId getId() {
		return this.id;
	}

	public void setId(HeadHistoryId id) {
		this.id = id;
	}

	@Override
	public String getProcessPointId() {
		return id.getProcessPointId();
	}
	
	@Override
	public void setProcessPointId(String processPointId) {
		if(id == null) id = new HeadHistoryId();
		id.setProcessPointId(processPointId);
	}

	@Override
	public String getProductId() {
		return id.getHeadId();
	}
	
	@Override
	public void setProductId(String productId) {
		if(id == null) id = new HeadHistoryId();
		id.setHeadId(productId);
	}

	@Override
	public Timestamp getActualTimestamp() {
		return id.getActualTimestamp();
	}

	@Override
	public void setActualTimestamp(Timestamp timestamp) {
		if(id == null) id = new HeadHistoryId();
		id.setActualTimestamp(timestamp);
	}
	

	@Override
	public String getApproverNo() {
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public void setApproverNo(String approverNo) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return toString(getProductId(),getProcessPointId(),getActualTimestamp(), getAssociateNo(), getApproverNo());
	}

	
}
