package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVCFR1", schema="VIOS")
public class ChangeForm extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CHANGE_FORM_ID", nullable=false)
	private int id;

	@Column(name="APV_PART_EFF_DATE")
	private Date apvPartEffDate;

	@Column(name="APVD_DATE")
	private Date approvedDate;

	@Column(name="APVD_USER", length=8)
	private String approvedUser;

	@Column(name="ASHIFT_REMARKS", length=10)
	private String ashiftRemarks;

	@Column(name="ASHIFT_TRAINING_DT")
	private Date ashiftTrainingDt;

	@Column(name="BSHIFT_REMARKS", length=10)
	private String bshiftRemarks;

	@Column(name="BSHIFT_TRAINING_DT")
	private Date bshiftTrainingDt;

	@Column(name="CHANGE_DATE", nullable=false)
	private Date changeDate;

	@Column(name="CHANGE_FORM_TYPE", nullable=false, length=1)
	private String changeFormType;

	@Column(name="CONTROL_NO", nullable=false)
	private int controlNo;

	@Column(name="CU_DOCUMENT_NO", nullable=false, length=20)
	private String cuDocumentNo;

	@Column(name="CU_REVISION_NO", nullable=false, length=10)
	private String cuRevisionNo;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="DOCUMENT_NO", nullable=false, length=20)
	private String documentNo;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="MAINT_DATE", nullable=false)
	private Date maintDate;

	@Column(name="MAINT_LOGON_ID", nullable=false, length=8)
	private String maintLogonId;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PPS_DOCUMENT_NO", nullable=false, length=20)
	private String ppsDocumentNo;

	@Column(name="PPS_REVISION_NO", nullable=false, length=10)
	private String ppsRevisionNo;

	@Column(name="PRINTED_LOGON_ID", length=8)
	private String printedLogonId;

	@Column(name="PROD_ASM_LINE_NO", nullable=false, length=1)
	private String prodAsmLineNo;

	@Column(name="PROD_SCH_QTY", nullable=false, precision=5, scale=1)
	private BigDecimal prodSchQty;

	@Column(name="PS_DOCUMENT_NO", nullable=false, length=20)
	private String psDocumentNo;

	@Column(name="PS_REVISION_NO", nullable=false, length=10)
	private String psRevisionNo;

	@Column(name="REPORT_DATE", nullable=false)
	private Date reportDate;

	@Column(name="REVISION_NO", nullable=false, length=5)
	private String revisionNo;

	@Column(name="SS_DOCUMENT_NO", nullable=false, length=20)
	private String ssDocumentNo;

	@Column(name="SS_REVISION_NO", nullable=false, length=10)
	private String ssRevisionNo;

	@Column(name="TEAM_NO", nullable=false, length=4)
	private String teamNo;

	@Column(name="USR_DOCUMENT_NO", nullable=false, length=20)
	private String usrDocumentNo;

	@Column(name="USR_REVISION_NO", nullable=false, length=10)
	private String usrRevisionNo;

	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

    public ChangeForm() {}

	public Integer getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Date getApvPartEffDate() {
		return this.apvPartEffDate;
	}

	public void setApvPartEffDate(Date apvPartEffDate) {
		this.apvPartEffDate = apvPartEffDate;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApprovedUser() {
		return StringUtils.trim(this.approvedUser);
	}

	public void setApprovedUser(String approvedUser) {
		this.approvedUser = approvedUser;
	}

	public String getAshiftRemarks() {
		return StringUtils.trim(this.ashiftRemarks);
	}

	public void setAshiftRemarks(String ashiftRemarks) {
		this.ashiftRemarks = ashiftRemarks;
	}

	public Date getAshiftTrainingDt() {
		return this.ashiftTrainingDt;
	}

	public void setAshiftTrainingDt(Date ashiftTrainingDt) {
		this.ashiftTrainingDt = ashiftTrainingDt;
	}

	public String getBshiftRemarks() {
		return StringUtils.trim(this.bshiftRemarks);
	}

	public void setBshiftRemarks(String bshiftRemarks) {
		this.bshiftRemarks = bshiftRemarks;
	}

	public Date getBshiftTrainingDt() {
		return this.bshiftTrainingDt;
	}

	public void setBshiftTrainingDt(Date bshiftTrainingDt) {
		this.bshiftTrainingDt = bshiftTrainingDt;
	}

	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeFormType() {
		return StringUtils.trim(this.changeFormType);
	}

	public void setChangeFormType(String changeFormType) {
		this.changeFormType = changeFormType;
	}

	public int getControlNo() {
		return this.controlNo;
	}

	public void setControlNo(int controlNo) {
		this.controlNo = controlNo;
	}

	public String getCuDocumentNo() {
		return StringUtils.trim(this.cuDocumentNo);
	}

	public void setCuDocumentNo(String cuDocumentNo) {
		this.cuDocumentNo = cuDocumentNo;
	}

	public String getCuRevisionNo() {
		return StringUtils.trim(this.cuRevisionNo);
	}

	public void setCuRevisionNo(String cuRevisionNo) {
		this.cuRevisionNo = cuRevisionNo;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDocumentNo() {
		return StringUtils.trim(this.documentNo);
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public Date getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
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

	public BigDecimal getModelYearDate() {
		return this.modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getPpsDocumentNo() {
		return StringUtils.trim(this.ppsDocumentNo);
	}

	public void setPpsDocumentNo(String ppsDocumentNo) {
		this.ppsDocumentNo = ppsDocumentNo;
	}

	public String getPpsRevisionNo() {
		return StringUtils.trim(this.ppsRevisionNo);
	}

	public void setPpsRevisionNo(String ppsRevisionNo) {
		this.ppsRevisionNo = ppsRevisionNo;
	}

	public String getPrintedLogonId() {
		return StringUtils.trim(this.printedLogonId);
	}

	public void setPrintedLogonId(String printedLogonId) {
		this.printedLogonId = printedLogonId;
	}

	public String getProdAsmLineNo() {
		return StringUtils.trim(this.prodAsmLineNo);
	}

	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}

	public BigDecimal getProdSchQty() {
		return this.prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getPsDocumentNo() {
		return StringUtils.trim(this.psDocumentNo);
	}

	public void setPsDocumentNo(String psDocumentNo) {
		this.psDocumentNo = psDocumentNo;
	}

	public String getPsRevisionNo() {
		return StringUtils.trim(this.psRevisionNo);
	}

	public void setPsRevisionNo(String psRevisionNo) {
		this.psRevisionNo = psRevisionNo;
	}

	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getRevisionNo() {
		return StringUtils.trim(this.revisionNo);
	}

	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}

	public String getSsDocumentNo() {
		return StringUtils.trim(this.ssDocumentNo);
	}

	public void setSsDocumentNo(String ssDocumentNo) {
		this.ssDocumentNo = ssDocumentNo;
	}

	public String getSsRevisionNo() {
		return StringUtils.trim(this.ssRevisionNo);
	}

	public void setSsRevisionNo(String ssRevisionNo) {
		this.ssRevisionNo = ssRevisionNo;
	}

	public String getTeamNo() {
		return StringUtils.trim(this.teamNo);
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public String getUsrDocumentNo() {
		return StringUtils.trim(this.usrDocumentNo);
	}

	public void setUsrDocumentNo(String usrDocumentNo) {
		this.usrDocumentNo = usrDocumentNo;
	}

	public String getUsrRevisionNo() {
		return StringUtils.trim(this.usrRevisionNo);
	}

	public void setUsrRevisionNo(String usrRevisionNo) {
		this.usrRevisionNo = usrRevisionNo;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apvPartEffDate == null) ? 0 : apvPartEffDate.hashCode());
		result = prime * result
				+ ((approvedDate == null) ? 0 : approvedDate.hashCode());
		result = prime * result
				+ ((approvedUser == null) ? 0 : approvedUser.hashCode());
		result = prime * result
				+ ((ashiftRemarks == null) ? 0 : ashiftRemarks.hashCode());
		result = prime
				* result
				+ ((ashiftTrainingDt == null) ? 0 : ashiftTrainingDt.hashCode());
		result = prime * result
				+ ((bshiftRemarks == null) ? 0 : bshiftRemarks.hashCode());
		result = prime
				* result
				+ ((bshiftTrainingDt == null) ? 0 : bshiftTrainingDt.hashCode());
		result = prime * result
				+ ((changeDate == null) ? 0 : changeDate.hashCode());
		result = prime * result
				+ ((changeFormType == null) ? 0 : changeFormType.hashCode());
		result = prime * result + controlNo;
		result = prime * result
				+ ((cuDocumentNo == null) ? 0 : cuDocumentNo.hashCode());
		result = prime * result
				+ ((cuRevisionNo == null) ? 0 : cuRevisionNo.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((documentNo == null) ? 0 : documentNo.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((maintDate == null) ? 0 : maintDate.hashCode());
		result = prime * result
				+ ((maintLogonId == null) ? 0 : maintLogonId.hashCode());
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((ppsDocumentNo == null) ? 0 : ppsDocumentNo.hashCode());
		result = prime * result
				+ ((ppsRevisionNo == null) ? 0 : ppsRevisionNo.hashCode());
		result = prime * result
				+ ((printedLogonId == null) ? 0 : printedLogonId.hashCode());
		result = prime * result
				+ ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result
				+ ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result
				+ ((psDocumentNo == null) ? 0 : psDocumentNo.hashCode());
		result = prime * result
				+ ((psRevisionNo == null) ? 0 : psRevisionNo.hashCode());
		result = prime * result
				+ ((reportDate == null) ? 0 : reportDate.hashCode());
		result = prime * result
				+ ((revisionNo == null) ? 0 : revisionNo.hashCode());
		result = prime * result
				+ ((ssDocumentNo == null) ? 0 : ssDocumentNo.hashCode());
		result = prime * result
				+ ((ssRevisionNo == null) ? 0 : ssRevisionNo.hashCode());
		result = prime * result + ((teamNo == null) ? 0 : teamNo.hashCode());
		result = prime * result
				+ ((usrDocumentNo == null) ? 0 : usrDocumentNo.hashCode());
		result = prime * result
				+ ((usrRevisionNo == null) ? 0 : usrRevisionNo.hashCode());
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
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
		ChangeForm other = (ChangeForm) obj;
		if (apvPartEffDate == null) {
			if (other.apvPartEffDate != null)
				return false;
		} else if (!apvPartEffDate.equals(other.apvPartEffDate))
			return false;
		if (approvedDate == null) {
			if (other.approvedDate != null)
				return false;
		} else if (!approvedDate.equals(other.approvedDate))
			return false;
		if (approvedUser == null) {
			if (other.approvedUser != null)
				return false;
		} else if (!approvedUser.equals(other.approvedUser))
			return false;
		if (ashiftRemarks == null) {
			if (other.ashiftRemarks != null)
				return false;
		} else if (!ashiftRemarks.equals(other.ashiftRemarks))
			return false;
		if (ashiftTrainingDt == null) {
			if (other.ashiftTrainingDt != null)
				return false;
		} else if (!ashiftTrainingDt.equals(other.ashiftTrainingDt))
			return false;
		if (bshiftRemarks == null) {
			if (other.bshiftRemarks != null)
				return false;
		} else if (!bshiftRemarks.equals(other.bshiftRemarks))
			return false;
		if (bshiftTrainingDt == null) {
			if (other.bshiftTrainingDt != null)
				return false;
		} else if (!bshiftTrainingDt.equals(other.bshiftTrainingDt))
			return false;
		if (changeDate == null) {
			if (other.changeDate != null)
				return false;
		} else if (!changeDate.equals(other.changeDate))
			return false;
		if (changeFormType == null) {
			if (other.changeFormType != null)
				return false;
		} else if (!changeFormType.equals(other.changeFormType))
			return false;
		if (controlNo != other.controlNo)
			return false;
		if (cuDocumentNo == null) {
			if (other.cuDocumentNo != null)
				return false;
		} else if (!cuDocumentNo.equals(other.cuDocumentNo))
			return false;
		if (cuRevisionNo == null) {
			if (other.cuRevisionNo != null)
				return false;
		} else if (!cuRevisionNo.equals(other.cuRevisionNo))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (documentNo == null) {
			if (other.documentNo != null)
				return false;
		} else if (!documentNo.equals(other.documentNo))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (id != other.id)
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
		if (modelYearDate == null) {
			if (other.modelYearDate != null)
				return false;
		} else if (!modelYearDate.equals(other.modelYearDate))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (ppsDocumentNo == null) {
			if (other.ppsDocumentNo != null)
				return false;
		} else if (!ppsDocumentNo.equals(other.ppsDocumentNo))
			return false;
		if (ppsRevisionNo == null) {
			if (other.ppsRevisionNo != null)
				return false;
		} else if (!ppsRevisionNo.equals(other.ppsRevisionNo))
			return false;
		if (printedLogonId == null) {
			if (other.printedLogonId != null)
				return false;
		} else if (!printedLogonId.equals(other.printedLogonId))
			return false;
		if (prodAsmLineNo == null) {
			if (other.prodAsmLineNo != null)
				return false;
		} else if (!prodAsmLineNo.equals(other.prodAsmLineNo))
			return false;
		if (prodSchQty == null) {
			if (other.prodSchQty != null)
				return false;
		} else if (!prodSchQty.equals(other.prodSchQty))
			return false;
		if (psDocumentNo == null) {
			if (other.psDocumentNo != null)
				return false;
		} else if (!psDocumentNo.equals(other.psDocumentNo))
			return false;
		if (psRevisionNo == null) {
			if (other.psRevisionNo != null)
				return false;
		} else if (!psRevisionNo.equals(other.psRevisionNo))
			return false;
		if (reportDate == null) {
			if (other.reportDate != null)
				return false;
		} else if (!reportDate.equals(other.reportDate))
			return false;
		if (revisionNo == null) {
			if (other.revisionNo != null)
				return false;
		} else if (!revisionNo.equals(other.revisionNo))
			return false;
		if (ssDocumentNo == null) {
			if (other.ssDocumentNo != null)
				return false;
		} else if (!ssDocumentNo.equals(other.ssDocumentNo))
			return false;
		if (ssRevisionNo == null) {
			if (other.ssRevisionNo != null)
				return false;
		} else if (!ssRevisionNo.equals(other.ssRevisionNo))
			return false;
		if (teamNo == null) {
			if (other.teamNo != null)
				return false;
		} else if (!teamNo.equals(other.teamNo))
			return false;
		if (usrDocumentNo == null) {
			if (other.usrDocumentNo != null)
				return false;
		} else if (!usrDocumentNo.equals(other.usrDocumentNo))
			return false;
		if (usrRevisionNo == null) {
			if (other.usrRevisionNo != null)
				return false;
		} else if (!usrRevisionNo.equals(other.usrRevisionNo))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId(), getApvPartEffDate(), getApprovedDate(), getApprovedUser(), getAshiftRemarks(),
				getAshiftTrainingDt(), getBshiftRemarks(), getBshiftTrainingDt(), getChangeDate(), 
				getChangeFormType(), getControlNo(), getCuDocumentNo(), getCuRevisionNo(), getDeptCode(),
				getDocumentNo(), getExtractDate(), getMaintDate(), getMaintLogonId(), getModelYearDate(),
				getPlantLocCode(), getPpsDocumentNo(), getPpsRevisionNo(), getPrintedLogonId(), 
				getProdAsmLineNo(), getProdSchQty(), getPsDocumentNo(), getPsRevisionNo(), getReportDate(),
				getRevisionNo(), getSsDocumentNo(), getSsRevisionNo(), getTeamNo(), getUsrDocumentNo(),
				getUsrRevisionNo(),getVehicleModelCode());
	}
}