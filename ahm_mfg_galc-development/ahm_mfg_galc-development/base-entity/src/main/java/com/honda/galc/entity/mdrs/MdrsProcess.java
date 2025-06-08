package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the PCLPR1 database table.
 * 
 */
@Entity
@Table(name="PCLPR1", schema="VIOS")
public class MdrsProcess extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MdrsProcessId id;

	@Column(name="ASM_PROC_NAME", nullable=false, length=35)
	private String asmProcName;

	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;

	@Column(name="ASM_PROC_SEQ_NO", nullable=false)
	private short asmProcSeqNo;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Timestamp extractDate;

	@Column(name="MPTAG_ID", nullable=false)
	private int mptagId;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(nullable=false, length=1)
	private String rotation;

	public MdrsProcess() {
	}

	public MdrsProcessId getId() {
		return this.id;
	}

	public void setId(MdrsProcessId id) {
		this.id = id;
	}

	public String getAsmProcName() {
		return StringUtils.trim(this.asmProcName);
	}

	public void setAsmProcName(String asmProcName) {
		this.asmProcName = asmProcName;
	}

	public String getAsmProcNo() {
		return StringUtils.trim(this.asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public short getAsmProcSeqNo() {
		return this.asmProcSeqNo;
	}

	public void setAsmProcSeqNo(short asmProcSeqNo) {
		this.asmProcSeqNo = asmProcSeqNo;
	}

	public String getBodyLocNo() {
		return StringUtils.trim(this.bodyLocNo);
	}

	public void setBodyLocNo(String bodyLocNo) {
		this.bodyLocNo = bodyLocNo;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Timestamp getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Timestamp extractDate) {
		this.extractDate = extractDate;
	}

	public int getMptagId() {
		return this.mptagId;
	}

	public void setMptagId(int mptagId) {
		this.mptagId = mptagId;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getRotation() {
		return StringUtils.trim(this.rotation);
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((asmProcName == null) ? 0 : asmProcName.hashCode());
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((int)this.asmProcSeqNo);
		result = prime * result
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result + mptagId;
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((rotation == null) ? 0 : rotation.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MdrsProcess other = (MdrsProcess)obj;
		if (asmProcName == null) {
			if (other.asmProcName != null)
				return false;
		} else if (!asmProcName.equals(other.asmProcName))
			return false;
		if (asmProcNo == null) {
			if (other.asmProcNo != null)
				return false;
		} else if (!asmProcNo.equals(other.asmProcNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (asmProcSeqNo != other.asmProcSeqNo)
			return false;
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null)
				return false;
		} else if (!bodyLocNo.equals(other.bodyLocNo))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (mptagId != other.mptagId)
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (rotation == null) {
			if (other.rotation != null)
				return false;
		} else if (!rotation.equals(other.rotation))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getApvdProcMaintId(), getId().getTeamId(), getAsmProcName(), getAsmProcNo()
				, getAsmProcSeqNo(), getBodyLocNo(), getDeptCode(), getExtractDate(), getMptagId()
				, getPlantLocCode(), getRotation());
	}
}