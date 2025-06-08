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
public class QiImageSectionPointId implements Serializable {
	@Column(name="IMAGE_SECTION_ID")
	private int imageSectionId;

	@Column(name="POINT_SEQUENCE_NO")
	private int pointSequenceNo;

	private static final long serialVersionUID = 1L;

	public QiImageSectionPointId() {
		super();
	}

	public int getImageSectionId() {
		return this.imageSectionId;
	}

	public void setImageSectionId(int imageSectionId) {
		this.imageSectionId = imageSectionId;
	}

	public int getPointSequenceNo() {
		return this.pointSequenceNo;
	}

	public void setPointSequenceNo(int pointSequenceNo) {
		this.pointSequenceNo = pointSequenceNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + imageSectionId;
		result = prime * result + pointSequenceNo;
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
		QiImageSectionPointId other = (QiImageSectionPointId) obj;
		if (imageSectionId != other.imageSectionId)
			return false;
		if (pointSequenceNo != other.pointSequenceNo)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
