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
public class HeadBuildResultId implements Serializable {
	@Column(name="HEAD_ID")
	@Tag(name="PRODUCT_ID", alt="HEAD_ID")
	private String headId;

	@Column(name="PART_NAME")
	private String partName;

	private static final long serialVersionUID = 1L;

	public HeadBuildResultId() {
		super();
	}
	
	public HeadBuildResultId(String headId,String partName) {
		this.headId = headId;
		this.partName = partName;
	}

	public String getHeadId() {
		return this.headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
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
		if ( ! (o instanceof HeadBuildResultId)) {
			return false;
		}
		HeadBuildResultId other = (HeadBuildResultId) o;
		return this.headId.equals(other.headId)
			&& this.partName.equals(other.partName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.headId.hashCode();
		hash = hash * prime + this.partName.hashCode();
		return hash;
	}
	
	public String toString() {
		return getClass().getSimpleName() + "(" + getHeadId() + "," + getPartName() + ")";
	}

}
