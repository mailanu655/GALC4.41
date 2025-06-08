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
@Table(name="BLOCK_HISTORY_TBX")
public class BlockHistory extends ProductHistory {
	
	@EmbeddedId
	private BlockHistoryId id;

	private static final long serialVersionUID = 1L;

	public BlockHistory() {
		super();
	}
	
    public BlockHistory(Block block, String processPointId) {
    	this(block.getProductId(), processPointId);
    }
    
	public BlockHistory(String productId,String processPointId) {
		id = new BlockHistoryId(productId, processPointId);
	}

	public BlockHistoryId getId() {
		return this.id;
	}

	public void setId(BlockHistoryId id) {
		this.id = id;
	}

	@Override
	public String getProcessPointId() {
		return id.getProcessPointId();
	}
	
	@Override
	public void setProcessPointId(String processPointId) {
		if(id == null) id = new BlockHistoryId();
		id.setProcessPointId(processPointId);
	}

	@Override
	public String getProductId() {
		return id.getBlockId();
	}
	
	@Override
	public void setProductId(String productId) {
		if(id == null) id = new BlockHistoryId();
		id.setBlockId(productId);
	}

	@Override
	public Timestamp getActualTimestamp() {
		return id.getActualTimestamp();
	}

	@Override
	public void setActualTimestamp(Timestamp timestamp) {
		if(id == null) id = new BlockHistoryId();
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
		return toString(getProductId(), getProcessPointId(), 
				getActualTimestamp(), getAssociateNo(), getApproverNo());
	}
}
