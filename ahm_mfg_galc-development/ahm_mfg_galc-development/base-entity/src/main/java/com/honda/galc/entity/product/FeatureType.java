package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * @author Sachin Kudikala
 * @date March 16, 2015
 * 
 */
@Entity
@Table(name="FEATURE_TYPE_TBX")
public class FeatureType extends AuditEntry{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FEATURE_TYPE")
	private String featureType;
	
	@Column(name="FEATURE_SHAPE")
	private String featureshape;

	public FeatureType() {
		super();
	}
	
	public String getFeatureType()
	{
		return StringUtils.trim(featureType);
	}
	
	public void setFeatureType(String featureType)
	{
		this.featureType = featureType;
	}

	public String getFeatureshape() {
		return StringUtils.trim(featureshape);
	}

	public void setFeatureshape(String featureshape) {
		this.featureshape = featureshape;
	}

	public String getId() {
		return StringUtils.trim(featureType);
	}
	
	@Override
	public String toString() {
		return toString(getFeatureType());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result
				+ ((featureshape == null) ? 0 : featureshape.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureType other = (FeatureType) obj;
		if (featureType == null) {
			if (other.featureType != null)
				return false;
		} else if (!featureType.equals(other.featureType))
			return false;
		if (featureshape == null) {
			if (other.featureshape != null)
				return false;
		} else if (!featureshape.equals(other.featureshape))
			return false;
		return true;
	}
	
	
}
