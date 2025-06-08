package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class FeaturePointId implements Serializable {
	@Column(name="FEATURE_ID")
	private String featureId;

	@Column(name="FEATURE_SEQ")
	private int featureSeq;


	private static final long serialVersionUID = 1L;

	public FeaturePointId() {
		super();
	}
	
	public FeaturePointId(String featureId,int featureSeq) {
		super();
		this.featureId = featureId;
		this.featureSeq = featureSeq;
	}

	public String getFeatureId() {
		return StringUtils.trim(this.featureId);
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public int getFeatureSeq() {
		return this.featureSeq;
	}

	public void setFeatureSeq(int featureSeq) {
		this.featureSeq = featureSeq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + featureSeq;
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
		FeaturePointId other = (FeaturePointId) obj;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (featureSeq != other.featureSeq)
			return false;
		return true;
	}





}