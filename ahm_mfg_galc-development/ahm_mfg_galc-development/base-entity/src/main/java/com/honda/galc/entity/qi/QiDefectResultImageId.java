package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class QiDefectResultImageId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "DEFECTRESULTID", nullable=false)
	private long defectResultId;
	
	@Column(name = "IMAGE_URL")
	private String imageUrl;

	public QiDefectResultImageId() {
		super();
	}
	
	public QiDefectResultImageId(long defectResultId, String imageUrl) {
		this.defectResultId = defectResultId;
		this.imageUrl = imageUrl;
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
	}

	public String getImageUrl() {
		return StringUtils.trimToEmpty(imageUrl);
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)defectResultId;
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		QiDefectResultImageId other = (QiDefectResultImageId) obj;
		return defectResultId == other.defectResultId && StringUtils.equals(imageUrl, other.getImageUrl());
	}

	@Override
	public String toString() {
		return "QiDefectResultImageId [defectResultId=" + defectResultId + ", imageUrl=" + imageUrl + "]";
	}
}
