package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class BlockHistoryId implements Serializable {
	@Column(name="BLOCK_ID")
	private String blockId;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	private static final long serialVersionUID = 1L;

	public BlockHistoryId() {
		super();
	}
	
	public BlockHistoryId(String blockId, String processPointId) {
		this.blockId = blockId;
		this.processPointId = processPointId;
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
	}

	public String getBlockId() {
		return StringUtils.trim(this.blockId);
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof BlockHistoryId)) {
			return false;
		}
		BlockHistoryId other = (BlockHistoryId) o;
		return this.blockId.equals(other.blockId)
			&& this.processPointId.equals(other.processPointId)
			&& this.actualTimestamp.equals(other.actualTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.blockId.hashCode();
		hash = hash * prime + this.processPointId.hashCode();
		hash = hash * prime + this.actualTimestamp.hashCode();
		return hash;
	}

}
