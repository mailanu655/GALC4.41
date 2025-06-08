package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@Embeddable
public class LetProgramCategoryId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="INSPECTION_DEVICE_TYPE")
	private String inspectionDeviceType;

	@Column(name="CRITERIA_PGM_ATTRIBUTE")
	private String criteriaPgmAttr;

	public LetProgramCategoryId() {
		super();
	}

	public LetProgramCategoryId(String inspectionDeviceType,String criteriaPgmAttr) {
		super();
		this.inspectionDeviceType = inspectionDeviceType;
		this.criteriaPgmAttr = criteriaPgmAttr;
	}

	public String getInspectionDeviceType() {
		return StringUtils.trim(inspectionDeviceType);
	}

	public void setInspectionDeviceType(String inspectionDeviceType) {
		this.inspectionDeviceType = inspectionDeviceType;
	}

	public String getCriteriaPgmAttr() {
		return StringUtils.trim(criteriaPgmAttr);
	}

	public void setCriteriaPgmAttr(String criteriaPgmAttr) {
		this.criteriaPgmAttr = criteriaPgmAttr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((criteriaPgmAttr == null) ? 0 : criteriaPgmAttr.hashCode());
		result = prime
				* result
				+ ((inspectionDeviceType == null) ? 0 : inspectionDeviceType
						.hashCode());
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
		LetProgramCategoryId other = (LetProgramCategoryId) obj;
		if (criteriaPgmAttr == null) {
			if (other.criteriaPgmAttr != null)
				return false;
		} else if (!criteriaPgmAttr.equals(other.criteriaPgmAttr))
			return false;
		if (inspectionDeviceType == null) {
			if (other.inspectionDeviceType != null)
				return false;
		} else if (!inspectionDeviceType.equals(other.inspectionDeviceType))
			return false;
		return true;
	}
	
	
}
