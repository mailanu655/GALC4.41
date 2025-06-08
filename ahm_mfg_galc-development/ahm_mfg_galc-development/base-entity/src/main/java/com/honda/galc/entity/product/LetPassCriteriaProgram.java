package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 25, 2013
 */
@Entity
@Table(name="GAL718TBX")
public class LetPassCriteriaProgram extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CRITERIA_PGM_ID")
	private Integer criteriaPgmId;

	@Column(name="CRITERIA_PGM_NAME")
	private String criteriaPgmName;

	@Column(name="INSPECTION_DEVICE_TYPE")
	private String inspectionDeviceType;

	@Column(name="CRITERIA_PGM_ATTRIBUTE")
	private String criteriaPgmAttr;

    
    public LetPassCriteriaProgram() {
		super();
	}
       
	public LetPassCriteriaProgram(Integer criteriaPgmId,
			String criteriaPgmName, String inspectionDeviceType,
			String criteriaPgmAttr) {
		super();
		this.criteriaPgmId = criteriaPgmId;
		this.criteriaPgmName = criteriaPgmName;
		this.inspectionDeviceType = inspectionDeviceType;
		this.criteriaPgmAttr = criteriaPgmAttr;
	}	
    	
	public Integer getCriteriaPgmId() {
		return criteriaPgmId;
	}

	public void setCriteriaPgmId(Integer criteriaPgmId) {
		this.criteriaPgmId = criteriaPgmId;
	}

	public String getCriteriaPgmName() {
		return StringUtils.trim(criteriaPgmName);
	}

	public void setCriteriaPgmName(String criteriaPgmName) {
		this.criteriaPgmName = criteriaPgmName;
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

	public String toString() {
		return toString(getCriteriaPgmId(),getCriteriaPgmAttr(),getCriteriaPgmName(),getInspectionDeviceType());
	}

	public Integer getId() {
		
		return getCriteriaPgmId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((criteriaPgmAttr == null) ? 0 : criteriaPgmAttr.hashCode());
		result = prime * result
				+ ((criteriaPgmId == null) ? 0 : criteriaPgmId.hashCode());
		result = prime * result
				+ ((criteriaPgmName == null) ? 0 : criteriaPgmName.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetPassCriteriaProgram other = (LetPassCriteriaProgram) obj;
		if (criteriaPgmAttr == null) {
			if (other.criteriaPgmAttr != null)
				return false;
		} else if (!criteriaPgmAttr.equals(other.criteriaPgmAttr))
			return false;
		if (criteriaPgmId == null) {
			if (other.criteriaPgmId != null)
				return false;
		} else if (!criteriaPgmId.equals(other.criteriaPgmId))
			return false;
		if (criteriaPgmName == null) {
			if (other.criteriaPgmName != null)
				return false;
		} else if (!criteriaPgmName.equals(other.criteriaPgmName))
			return false;
		if (inspectionDeviceType == null) {
			if (other.inspectionDeviceType != null)
				return false;
		} else if (!inspectionDeviceType.equals(other.inspectionDeviceType))
			return false;
		return true;
	}	

}
