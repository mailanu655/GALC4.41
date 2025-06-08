package com.honda.galc.entity.qics;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ImageSectionPointId implements Serializable {
	@Column(name="IMAGE_SECTION_ID")
	private int imageSectionId;

	@Column(name="POINT_SEQUENCE_NO")
	private int pointSequenceNo;

	private static final long serialVersionUID = 1L;

	public ImageSectionPointId() {
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
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ImageSectionPointId)) {
			return false;
		}
		ImageSectionPointId other = (ImageSectionPointId) o;
		return (this.imageSectionId == other.imageSectionId)
			&& (this.pointSequenceNo == other.pointSequenceNo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.imageSectionId;
		hash = hash * prime + this.pointSequenceNo;
		return hash;
	}

}
