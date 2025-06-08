package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiExternalSystemDefectMap Class description</h3>
 * <p> QiExternalSystemDefectMap provides NAQ Defect Mapping between External System and QICS </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 * Feb 13, 2017      
 * 
 */
@Entity
@Table(name = "QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX")
public class QiExternalSystemDefectMap extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EXTERNAL_SYSTEM_DEFECT_MAP_ID")
	private Integer externalSystemDefectMapId;
	
	@Column(name = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;
	
	@Column(name = "EXTERNAL_PART_CODE")
	@Auditable
	private String externalPartCode;
	
	@Column(name = "EXTERNAL_DEFECT_CODE")
	@Auditable
	private String externalDefectCode;
	
	@Column(name = "ENTRY_MODEL")
	@Auditable
	private String entryModel;
	
	@Column(name = "LOCAL_DEFECT_COMBINATION_ID")
	@Auditable
	private Integer localDefectCombinationId;
		
	@Column(name = "IS_QICS_REPAIR_REQD")
	private short isQicsRepairReqd;

	@Column(name = "IS_EXT_SYS_REPAIR_REQD")
	private short isExtSysRepairReqd;

	/**
	 * @return the externalSystemDefectMapId
	 */
	public Integer getExternalSystemDefectMapId() {
		return externalSystemDefectMapId;
	}

	/**
	 * @param externalSystemDefectMapId the externalSystemDefectMapId to set
	 */
	public void setExternalSystemDefectMapId(Integer externalSystemDefectMapId) {
		this.externalSystemDefectMapId = externalSystemDefectMapId;
	}

	/**
	 * @return the externalSystemName
	 */
	public String getExternalSystemName() {
		return StringUtils.trimToEmpty(externalSystemName);
	}

	/**
	 * @param externalSystemName the externalSystemName to set
	 */
	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	/**
	 * @return the externalPartCode
	 */
	public String getExternalPartCode() {
		return StringUtils.trimToEmpty(externalPartCode);
	}

	/**
	 * @param externalPartCode the externalPartCode to set
	 */
	public void setExternalPartCode(String externalPartCode) {
		this.externalPartCode = externalPartCode;
	}

	/**
	 * @return the externalDefectCode
	 */
	public String getExternalDefectCode() {
		return StringUtils.trimToEmpty(externalDefectCode);
	}

	/**
	 * @param externalDefectCode the externalDefectCode to set
	 */
	public void setExternalDefectCode(String externalDefectCode) {
		this.externalDefectCode = externalDefectCode;
	}

	/**
	 * @return the localDefectCombinationId
	 */
	public Integer getLocalDefectCombinationId() {
		return localDefectCombinationId;
	}
	
	/**
	 * @param localDefectCombinationId the localDefectCombinationId to set
	 */
	public void setLocalDefectCombinationId(Integer localDefectCombinationId) {
		this.localDefectCombinationId = localDefectCombinationId;
	}

	public short getIsQicsRepairReqd() {
		return isQicsRepairReqd;
	}

	public void setIsQicsRepairReqd(short isQicsRepairReqd) {
		this.isQicsRepairReqd = isQicsRepairReqd;
	}

	public short getIsExtSysRepairReqd() {
		return isExtSysRepairReqd;
	}

	public void setIsExtSysRepairReqd(short isExtSysRepairReqd) {
		this.isExtSysRepairReqd = isExtSysRepairReqd;
	}

	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	public Object getId() {
		return externalSystemDefectMapId;
	}
	

	@Override
	public String toString() {
		return "QiExternalSystemDefectMap [externalSystemDefectMapId=" + externalSystemDefectMapId
				+ ", externalSystemName=" + externalSystemName + ", externalPartCode=" + externalPartCode
				+ ", externalDefectCode=" + externalDefectCode + ", entryModel=" + entryModel
				+ ", localDefectCombinationId=" + localDefectCombinationId + ", isQicsRepairReqd=" + isQicsRepairReqd
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result + ((externalDefectCode == null) ? 0 : externalDefectCode.hashCode());
		result = prime * result + ((externalPartCode == null) ? 0 : externalPartCode.hashCode());
		result = prime * result + ((externalSystemDefectMapId == null) ? 0 : externalSystemDefectMapId.hashCode());
		result = prime * result + ((externalSystemName == null) ? 0 : externalSystemName.hashCode());
		result = prime * result + ((localDefectCombinationId == null) ? 0 : localDefectCombinationId.hashCode());
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
		QiExternalSystemDefectMap other = (QiExternalSystemDefectMap) obj;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (externalDefectCode == null) {
			if (other.externalDefectCode != null)
				return false;
		} else if (!externalDefectCode.equals(other.externalDefectCode))
			return false;
		if (externalPartCode == null) {
			if (other.externalPartCode != null)
				return false;
		} else if (!externalPartCode.equals(other.externalPartCode))
			return false;
		if (externalSystemDefectMapId == null) {
			if (other.externalSystemDefectMapId != null)
				return false;
		} else if (!externalSystemDefectMapId.equals(other.externalSystemDefectMapId))
			return false;
		if (externalSystemName == null) {
			if (other.externalSystemName != null)
				return false;
		} else if (!externalSystemName.equals(other.externalSystemName))
			return false;
		if (localDefectCombinationId == null) {
			if (other.localDefectCombinationId != null)
				return false;
		} else if (!localDefectCombinationId.equals(other.localDefectCombinationId))
			return false;
		return true;
	}

	

}
