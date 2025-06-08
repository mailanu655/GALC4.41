package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVDPT1", schema="VIOS")
public class DepartmentSettings extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DepartmentSettingsId id;

	@Column(name="GPACV_WCH", length=4)
	private String gpacvWch;

	@Column(name="MAINT_DATE", nullable=false)
	private Date maintDate;

	@Column(name="MAINT_LOGON_ID", nullable=false, length=8)
	private String maintLogonId;

	@Column(name="PQCT_YEAR", length=4)
	private String pqctYear;

	@Column(name="PRINT_MSDS_NO", length=1)
	private String printMsdsNo;

	@Column(name="SHOW_IMPACT_PT", nullable=false, length=1)
	private String showImpactPt;

	@Column(name="SHOW_JOINT_TYPE", nullable=false, length=1)
	private String showJointType;

	@Column(name="SHOW_QLTY_CTL_ITEM", length=1)
	private String showQltyCtlItem;

	@Column(name="SHOW_QUALITY_ISSUE", length=1)
	private String showQualityIssue;

	@Column(name="SHOW_SAFETY_PT", nullable=false, length=1)
	private String showSafetyPt;

	@Column(name="SHOW_TOOL", length=1)
	private String showTool;

	@Column(name="SHOW_TORQUE", nullable=false, length=1)
	private String showTorque;

	@Column(name="TORQUE_UOM", length=2)
	private String torqueUom;

	@Column(name="UNIT_SHEET_TOOL", length=254)
	private String unitSheetTool;

	@Column(name="USES_PQCT", length=1)
	private String usesPqct;

    public DepartmentSettings() {}

	public DepartmentSettingsId getId() {
		return this.id;
	}

	public void setId(DepartmentSettingsId id) {
		this.id = id;
	}
	
	public String getGpacvWch() {
		return StringUtils.trim(this.gpacvWch);
	}

	public void setGpacvWch(String gpacvWch) {
		this.gpacvWch = gpacvWch;
	}

	public Date getMaintDate() {
		return this.maintDate;
	}

	public void setMaintDate(Date maintDate) {
		this.maintDate = maintDate;
	}

	public String getMaintLogonId() {
		return StringUtils.trim(this.maintLogonId);
	}

	public void setMaintLogonId(String maintLogonId) {
		this.maintLogonId = maintLogonId;
	}

	public String getPqctYear() {
		return StringUtils.trim(this.pqctYear);
	}

	public void setPqctYear(String pqctYear) {
		this.pqctYear = pqctYear;
	}

	public String getPrintMsdsNo() {
		return StringUtils.trim(this.printMsdsNo);
	}

	public void setPrintMsdsNo(String printMsdsNo) {
		this.printMsdsNo = printMsdsNo;
	}

	public String getShowImpactPt() {
		return StringUtils.trim(this.showImpactPt);
	}

	public void setShowImpactPt(String showImpactPt) {
		this.showImpactPt = showImpactPt;
	}

	public String getShowJointType() {
		return StringUtils.trim(this.showJointType);
	}

	public void setShowJointType(String showJointType) {
		this.showJointType = showJointType;
	}

	public String getShowQltyCtlItem() {
		return StringUtils.trim(this.showQltyCtlItem);
	}

	public void setShowQltyCtlItem(String showQltyCtlItem) {
		this.showQltyCtlItem = showQltyCtlItem;
	}

	public String getShowQualityIssue() {
		return StringUtils.trim(this.showQualityIssue);
	}

	public void setShowQualityIssue(String showQualityIssue) {
		this.showQualityIssue = showQualityIssue;
	}

	public String getShowSafetyPt() {
		return StringUtils.trim(this.showSafetyPt);
	}

	public void setShowSafetyPt(String showSafetyPt) {
		this.showSafetyPt = showSafetyPt;
	}

	public String getShowTool() {
		return StringUtils.trim(this.showTool);
	}

	public void setShowTool(String showTool) {
		this.showTool = showTool;
	}

	public String getShowTorque() {
		return StringUtils.trim(this.showTorque);
	}

	public void setShowTorque(String showTorque) {
		this.showTorque = showTorque;
	}

	public String getTorqueUom() {
		return StringUtils.trim(this.torqueUom);
	}

	public void setTorqueUom(String torqueUom) {
		this.torqueUom = torqueUom;
	}

	public String getUnitSheetTool() {
		return StringUtils.trim(this.unitSheetTool);
	}

	public void setUnitSheetTool(String unitSheetTool) {
		this.unitSheetTool = unitSheetTool;
	}

	public String getUsesPqct() {
		return StringUtils.trim(this.usesPqct);
	}

	public void setUsesPqct(String usesPqct) {
		this.usesPqct = usesPqct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gpacvWch == null) ? 0 : gpacvWch.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((maintDate == null) ? 0 : maintDate.hashCode());
		result = prime * result
				+ ((maintLogonId == null) ? 0 : maintLogonId.hashCode());
		result = prime * result
				+ ((pqctYear == null) ? 0 : pqctYear.hashCode());
		result = prime * result
				+ ((printMsdsNo == null) ? 0 : printMsdsNo.hashCode());
		result = prime * result
				+ ((showImpactPt == null) ? 0 : showImpactPt.hashCode());
		result = prime * result
				+ ((showJointType == null) ? 0 : showJointType.hashCode());
		result = prime * result
				+ ((showQltyCtlItem == null) ? 0 : showQltyCtlItem.hashCode());
		result = prime
				* result
				+ ((showQualityIssue == null) ? 0 : showQualityIssue.hashCode());
		result = prime * result
				+ ((showSafetyPt == null) ? 0 : showSafetyPt.hashCode());
		result = prime * result
				+ ((showTool == null) ? 0 : showTool.hashCode());
		result = prime * result
				+ ((showTorque == null) ? 0 : showTorque.hashCode());
		result = prime * result
				+ ((torqueUom == null) ? 0 : torqueUom.hashCode());
		result = prime * result
				+ ((unitSheetTool == null) ? 0 : unitSheetTool.hashCode());
		result = prime * result
				+ ((usesPqct == null) ? 0 : usesPqct.hashCode());
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
		DepartmentSettings other = (DepartmentSettings) obj;
		if (gpacvWch == null) {
			if (other.gpacvWch != null)
				return false;
		} else if (!gpacvWch.equals(other.gpacvWch))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maintDate == null) {
			if (other.maintDate != null)
				return false;
		} else if (!maintDate.equals(other.maintDate))
			return false;
		if (maintLogonId == null) {
			if (other.maintLogonId != null)
				return false;
		} else if (!maintLogonId.equals(other.maintLogonId))
			return false;
		if (pqctYear == null) {
			if (other.pqctYear != null)
				return false;
		} else if (!pqctYear.equals(other.pqctYear))
			return false;
		if (printMsdsNo == null) {
			if (other.printMsdsNo != null)
				return false;
		} else if (!printMsdsNo.equals(other.printMsdsNo))
			return false;
		if (showImpactPt == null) {
			if (other.showImpactPt != null)
				return false;
		} else if (!showImpactPt.equals(other.showImpactPt))
			return false;
		if (showJointType == null) {
			if (other.showJointType != null)
				return false;
		} else if (!showJointType.equals(other.showJointType))
			return false;
		if (showQltyCtlItem == null) {
			if (other.showQltyCtlItem != null)
				return false;
		} else if (!showQltyCtlItem.equals(other.showQltyCtlItem))
			return false;
		if (showQualityIssue == null) {
			if (other.showQualityIssue != null)
				return false;
		} else if (!showQualityIssue.equals(other.showQualityIssue))
			return false;
		if (showSafetyPt == null) {
			if (other.showSafetyPt != null)
				return false;
		} else if (!showSafetyPt.equals(other.showSafetyPt))
			return false;
		if (showTool == null) {
			if (other.showTool != null)
				return false;
		} else if (!showTool.equals(other.showTool))
			return false;
		if (showTorque == null) {
			if (other.showTorque != null)
				return false;
		} else if (!showTorque.equals(other.showTorque))
			return false;
		if (torqueUom == null) {
			if (other.torqueUom != null)
				return false;
		} else if (!torqueUom.equals(other.torqueUom))
			return false;
		if (unitSheetTool == null) {
			if (other.unitSheetTool != null)
				return false;
		} else if (!unitSheetTool.equals(other.unitSheetTool))
			return false;
		if (usesPqct == null) {
			if (other.usesPqct != null)
				return false;
		} else if (!usesPqct.equals(other.usesPqct))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getDeptCode(), getId().getPlantLocCode(), getGpacvWch(), getMaintDate(), getMaintLogonId(), getPqctYear(), 
				getPrintMsdsNo(), getShowImpactPt(), getShowJointType(), getShowQltyCtlItem(), 
				getShowQualityIssue(), getShowSafetyPt(), getShowTool(), getShowTorque(), 
				getTorqueUom(), getUnitSheetTool(), getUsesPqct());
	}
}