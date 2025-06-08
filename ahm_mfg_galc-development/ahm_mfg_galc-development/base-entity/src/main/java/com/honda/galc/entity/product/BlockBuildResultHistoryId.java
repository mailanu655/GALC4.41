package com.honda.galc.entity.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/** * * 
* @author Xiaomei Ma
* @since Feb 27, 2020
*/
@Embeddable
public class BlockBuildResultHistoryId implements Serializable {
	@Column(name="BLOCK_ID")
	private String blockId;

	@Column(name="PART_NAME")
	private String partName;
	
	@Column(name="ACTUAL_TIMESTAMP")
	private Date actualTimestamp;

	private static final long serialVersionUID = 1L;

	public BlockBuildResultHistoryId() {
		super();
	}

	public BlockBuildResultHistoryId(String blockId, String partName, Date actualTimestamp) {
		super();
		this.blockId = blockId;
		this.partName = partName;
		this.actualTimestamp = actualTimestamp;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((blockId == null) ? 0 : blockId.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockBuildResultHistoryId other = (BlockBuildResultHistoryId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (blockId == null) {
			if (other.blockId != null)
				return false;
		} else if (!blockId.equals(other.blockId))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BlockBuildResultHistoryId [blockId=" + blockId + ", partName=" + partName + ", actualTimestamp="
				+ actualTimestamp + "]";
	}
	
}
