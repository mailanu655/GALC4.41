package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="CONROD_HISTORY_TBX")
public class ConrodHistory extends ProductHistory {
	@EmbeddedId
	private ConrodHistoryId id;


	private static final long serialVersionUID = 1L;

	public ConrodHistory() {
		super();
	}
	
    public ConrodHistory(Conrod conrod, String processPointId) {
    	this(conrod.getProductId(), processPointId);
    }
    
	public ConrodHistory(String productId,String processPointId) {
		id = new ConrodHistoryId(productId, processPointId);
	}

	public ConrodHistoryId getId() {
		return this.id;
	}

	public void setId(ConrodHistoryId id) {
		this.id = id;
	}

	@Override
	public String getProcessPointId() {
		return id.getProcessPointId();
	}
	
	@Override
	public void setProcessPointId(String processPointId) {
		if(id == null) id = new ConrodHistoryId();
		id.setProcessPointId(processPointId);
	}

	@Override
	public String getProductId() {
		return id.getConrodId();
	}
	
	@Override
	public void setProductId(String productId) {
		if(id == null) id = new ConrodHistoryId();
		id.setConrodId(productId);
	}

	@Override
	public Timestamp getActualTimestamp() {
		return id.getActualTimestamp();
	}

	@Override
	public void setActualTimestamp(Timestamp timestamp) {
		if(id == null) id = new ConrodHistoryId();
		id.setActualTimestamp(timestamp);
	}

	
	@Override
	public String getApproverNo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApproverNo(String approverNo) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return toString(getProductId(), getProcessPointId(), getActualTimestamp(), 
				getAssociateNo(), getApproverNo());
	}
}
