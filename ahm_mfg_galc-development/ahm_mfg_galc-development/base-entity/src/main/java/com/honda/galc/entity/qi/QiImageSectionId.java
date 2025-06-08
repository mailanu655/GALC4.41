package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.honda.galc.util.ToStringUtil;

/** * *
* @author Abhishek Garg
* @since May 18, 2016
*/

@Embeddable
public class QiImageSectionId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="IMAGE_SECTION_ID")
	private int imageSectionId;

	@Column(name="PART_LOCATION_ID")
	private int partLocationId;
	
	public QiImageSectionId() {
		super();
	}

	public int getImageSectionId() {
		return imageSectionId;
	}

	public int getPartLocationId() {
		return partLocationId;
	}

	public void setImageSectionId(int imageSectionId) {
		this.imageSectionId = imageSectionId;
	}

	public void setPartLocationId(int partLocationId) {
		this.partLocationId = partLocationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + imageSectionId;
		result = prime * result + partLocationId;
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
		QiImageSectionId other = (QiImageSectionId) obj;
		if (imageSectionId != other.imageSectionId)
			return false;
		if (partLocationId != other.partLocationId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
