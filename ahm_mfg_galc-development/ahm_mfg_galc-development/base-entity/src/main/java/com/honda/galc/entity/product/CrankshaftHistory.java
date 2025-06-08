package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="CRANKSHAFT_HISTORY_TBX")
public class CrankshaftHistory extends ProductHistory {
	@EmbeddedId
	private CrankshaftHistoryId id;


	private static final long serialVersionUID = 1L;

	public CrankshaftHistory() {
		super();
	}
	
    public CrankshaftHistory(Crankshaft crankshaft, String processPointId) {
    	this(crankshaft.getProductId(), processPointId);
    }
    
	public CrankshaftHistory(String productId,String processPointId) {
		id = new CrankshaftHistoryId(productId, processPointId);
	}

	public CrankshaftHistoryId getId() {
		return this.id;
	}

	public void setId(CrankshaftHistoryId id) {
		this.id = id;
	}

	@Override
	public String getProcessPointId() {
		return id.getProcessPointId();
	}
	
	@Override
	public void setProcessPointId(String processPointId) {
		if(id == null) id = new CrankshaftHistoryId();
		id.setProcessPointId(processPointId);
	}

	@Override
	public String getProductId() {
		return id.getCrankshaftId();
	}
	
	@Override
	public void setProductId(String productId) {
		if(id == null) id = new CrankshaftHistoryId();
		id.setCrankshaftId(productId);
	}

	@Override
	public Timestamp getActualTimestamp() {
		return id.getActualTimestamp();
	}

	@Override
	public void setActualTimestamp(Timestamp timestamp) {
		if(id == null) id = new CrankshaftHistoryId();
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
