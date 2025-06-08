package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVCFU1", schema="VIOS")
public class ChangeFormUnit extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ChangeFormUnitId id;

	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;

	@Column(name="CHANGE_DESC", nullable=false, length=254)
	private String changeDesc;

	@Column(name="CU_IN", nullable=false, length=1)
	private String cuIn;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="ISSR_LOGON_ID", length=8)
	private String issrLogonId;

	@Column(name="ISSUED_DATE")
	private Date issuedDate;

	@Column(name="PCF_IN", nullable=false, length=1)
	private String pcfIn;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PPS_IN", nullable=false, length=1)
	private String ppsIn;

	@Column(name="PS_IN", nullable=false, length=1)
	private String psIn;

	@Column(name="SS_IN", nullable=false, length=1)
	private String ssIn;

	@Column(name="UNIT_NO", length=6)
	private String unitNo;

	@Column(name="USR_IN", nullable=false, length=1)
	private String usrIn;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CHANGE_FORM_ID", nullable=false, insertable=false, updatable=false)
	private ChangeForm changeForm;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="APVD_PROC_MAINT_ID", nullable=false, insertable=false, updatable=false)
	private Process process;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="APVD_UNIT_MAINT_ID", nullable=false, insertable=false, updatable=false)
	private Unit unit;

    public ChangeFormUnit() {}

	public ChangeFormUnitId getId() {
		return this.id;
	}

	public void setId(ChangeFormUnitId id) {
		this.id = id;
	}
	
	public String getAsmProcNo() {
		return StringUtils.trim(this.asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public String getChangeDesc() {
		return StringUtils.trim(this.changeDesc);
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}

	public String getCuIn() {
		return StringUtils.trim(this.cuIn);
	}

	public void setCuIn(String cuIn) {
		this.cuIn = cuIn;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getIssrLogonId() {
		return StringUtils.trim(this.issrLogonId);
	}

	public void setIssrLogonId(String issrLogonId) {
		this.issrLogonId = issrLogonId;
	}

	public Date getIssuedDate() {
		return this.issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getPcfIn() {
		return StringUtils.trim(this.pcfIn);
	}

	public void setPcfIn(String pcfIn) {
		this.pcfIn = pcfIn;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getPpsIn() {
		return StringUtils.trim(this.ppsIn);
	}

	public void setPpsIn(String ppsIn) {
		this.ppsIn = ppsIn;
	}

	public String getPsIn() {
		return StringUtils.trim(this.psIn);
	}

	public void setPsIn(String psIn) {
		this.psIn = psIn;
	}

	public String getSsIn() {
		return StringUtils.trim(this.ssIn);
	}

	public void setSsIn(String ssIn) {
		this.ssIn = ssIn;
	}

	public String getUnitNo() {
		return StringUtils.trim(this.unitNo);
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUsrIn() {
		return StringUtils.trim(this.usrIn);
	}

	public void setUsrIn(String usrIn) {
		this.usrIn = usrIn;
	}

	public ChangeForm getChangeForm() {
		return this.changeForm;
	}

	public void setChangeForm(ChangeForm changeForm) {
		this.changeForm = changeForm;
	}
	
	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
	
	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result
				+ ((changeDesc == null) ? 0 : changeDesc.hashCode());
		result = prime * result
				+ ((changeForm == null) ? 0 : changeForm.hashCode());
		result = prime * result + ((cuIn == null) ? 0 : cuIn.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((issrLogonId == null) ? 0 : issrLogonId.hashCode());
		result = prime * result
				+ ((issuedDate == null) ? 0 : issuedDate.hashCode());
		result = prime * result + ((pcfIn == null) ? 0 : pcfIn.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + ((ppsIn == null) ? 0 : ppsIn.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result + ((psIn == null) ? 0 : psIn.hashCode());
		result = prime * result + ((ssIn == null) ? 0 : ssIn.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result + ((usrIn == null) ? 0 : usrIn.hashCode());
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
		ChangeFormUnit other = (ChangeFormUnit) obj;
		if (asmProcNo == null) {
			if (other.asmProcNo != null)
				return false;
		} else if (!asmProcNo.equals(other.asmProcNo))
			return false;
		if (changeDesc == null) {
			if (other.changeDesc != null)
				return false;
		} else if (!changeDesc.equals(other.changeDesc))
			return false;
		if (changeForm == null) {
			if (other.changeForm != null)
				return false;
		} else if (!changeForm.equals(other.changeForm))
			return false;
		if (cuIn == null) {
			if (other.cuIn != null)
				return false;
		} else if (!cuIn.equals(other.cuIn))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (issrLogonId == null) {
			if (other.issrLogonId != null)
				return false;
		} else if (!issrLogonId.equals(other.issrLogonId))
			return false;
		if (issuedDate == null) {
			if (other.issuedDate != null)
				return false;
		} else if (!issuedDate.equals(other.issuedDate))
			return false;
		if (pcfIn == null) {
			if (other.pcfIn != null)
				return false;
		} else if (!pcfIn.equals(other.pcfIn))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (ppsIn == null) {
			if (other.ppsIn != null)
				return false;
		} else if (!ppsIn.equals(other.ppsIn))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (psIn == null) {
			if (other.psIn != null)
				return false;
		} else if (!psIn.equals(other.psIn))
			return false;
		if (ssIn == null) {
			if (other.ssIn != null)
				return false;
		} else if (!ssIn.equals(other.ssIn))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		if (usrIn == null) {
			if (other.usrIn != null)
				return false;
		} else if (!usrIn.equals(other.usrIn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getApprovedProcMaintId(), getId().getApprovedUnitMaintId(), getId().getChangeFormId(), 
				getAsmProcNo(), getChangeDesc(), getCuIn(), getDeptCode(), getIssrLogonId(), getIssuedDate(), 
				getPcfIn(), getPlantLocCode(), getPpsIn(), getPsIn(), getSsIn(), getUnitNo(), getUsrIn());
	}
}