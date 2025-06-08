package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * @author Subu Kathiresan
 * @date Mar 25, 2013 
 * 
 */
@Embeddable
public class MapFeatureLocationId implements Serializable {

	private static final long serialVersionUID = -6539948644170030513L;

	@Column(name="FEATURE_ID")
	private String featureId;

	@Column(name="FEATURE_TYPE")
	private String featureType;
	
	@Column(name="FEATURE_LAYER")
	private String featureLayer;

	public MapFeatureLocationId() {
		super();
	}
	
	public MapFeatureLocationId(String featureId, String featureType, String featureLayer) {
		super();
		this.featureId = featureId;
		this.featureType = featureType;
		this.featureLayer = featureLayer;
	}

	public String getFeatureId() {
		return StringUtils.trim(this.featureId);
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getFeatureType() {
		return StringUtils.trim(this.featureType);
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureLayer() {
		return StringUtils.trim(this.featureLayer);
	}

	public void setFeatureLayer(String featureLayer) {
		this.featureLayer = featureLayer;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof MapFeatureLocationId)) {
			return false;
		}
		MapFeatureLocationId other = (MapFeatureLocationId) o;
		return this.featureId.equals(other.featureId)
			&& this.featureType.equals(other.featureType)
			&& this.featureLayer.equals(other.featureLayer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.featureId.hashCode();
		hash = hash * prime + this.featureType.hashCode();
		hash = hash * prime + this.featureLayer.hashCode();
		return hash;
	}

}
