package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.device.Tag;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class BlockBuildResultId implements Serializable {
	@Column(name="BLOCK_ID")
	@Tag(name="PRODUCT_ID", alt="BLOCK_ID")
	private String blockId;

	@Column(name="PART_NAME")
	private String partName;

	private static final long serialVersionUID = 1L;

	public BlockBuildResultId() {
		super();
	}
	
	public BlockBuildResultId(String blockId,String partName) {
		this.blockId = blockId;
		this.partName = partName;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getPartName() {
		return this.partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof BlockBuildResultId)) {
			return false;
		}
		BlockBuildResultId other = (BlockBuildResultId) o;
		return this.blockId.equals(other.blockId)
			&& this.partName.equals(other.partName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.blockId.hashCode();
		hash = hash * prime + this.partName.hashCode();
		return hash;
	}
	
	public String toString() {
		return getClass().getSimpleName() + "(" + getBlockId() + "," + getPartName() + ")";
	}


}
