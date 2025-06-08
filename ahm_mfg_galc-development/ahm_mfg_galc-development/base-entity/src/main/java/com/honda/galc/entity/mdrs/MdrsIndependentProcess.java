package com.honda.galc.entity.mdrs;

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
@Table(name="PVLPR1", schema="VIOS")
public class MdrsIndependentProcess extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MdrsIndependentProcessId id;

	@Column(name="ASM_PROC_NAME", nullable=false, length=35)
	private String asmProcName;

	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;

	@Column(name="ASM_PROC_SEQ_NO", nullable=false)
	private short asmProcSeqNo;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="MPTAG_ID", nullable=false)
	private int mptagId;

    public MdrsIndependentProcess() {}

	public MdrsIndependentProcessId getId() {
		return this.id;
	}

	public void setId(MdrsIndependentProcessId id) {
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

	public Date getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}

	public int getMptagId() {
		return this.mptagId;
	}

	public void setMptagId(int mptagId) {
		this.mptagId = mptagId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((asmProcName == null) ? 0 : asmProcName.hashCode());
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result + asmProcSeqNo;
		result = prime * result
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + mptagId;
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
		MdrsIndependentProcess other = (MdrsIndependentProcess) obj;
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
		if (asmProcSeqNo != other.asmProcSeqNo)
			return false;
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null)
				return false;
		} else if (!bodyLocNo.equals(other.bodyLocNo))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mptagId != other.mptagId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProcessId(), getId().getTeamId(), getAsmProcName(), getAsmProcNo(), 
				getAsmProcSeqNo(), getBodyLocNo(), getExtractDate(), getMptagId());
	}
}