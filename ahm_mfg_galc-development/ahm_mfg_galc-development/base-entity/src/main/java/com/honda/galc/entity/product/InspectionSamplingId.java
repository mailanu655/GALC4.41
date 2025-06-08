package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/*
* 
* @author Gangadhararao Gadde 
* @since Feb 06, 2014
*/
@Embeddable
public class InspectionSamplingId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="MODEL_CODE")
	private String modelCode;
	
	@Column(name="MODEL_TYPE_CODE")
	private String modelTypeCode;

	public String getModelCode() {
		return StringUtils.trimToEmpty(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return  StringUtils.trimToEmpty(modelTypeCode);
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public InspectionSamplingId(String modelCode, String modelTypeCode) {
		super();
		this.modelCode = modelCode;
		this.modelTypeCode = modelTypeCode;
	}

	public InspectionSamplingId() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result
				+ ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
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
		InspectionSamplingId other = (InspectionSamplingId) obj;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		return true;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getModelCode()).append(",");
		builder.append(getModelTypeCode());
		return builder.toString();
	}


}
