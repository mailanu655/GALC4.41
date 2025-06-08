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
@Table(name="PVLTM1", schema="VIOS")
public class MdrsIndependentTeam extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MdrsIndependentTeamId id;
	
	@Column(name="EFF_BEG_DATE", nullable=false)
	private Date effBegDate;

	@Column(name="EFF_END_DATE", nullable=false)
	private Date effEndDate;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="PROD_RATE_ID", nullable=false)
	private int prodRateId;

	@Column(name="TEAM_NO", nullable=false, length=4)
	private String teamNo;

	@Column(name="TEAM_SEQ_NO", nullable=false)
	private short teamSeqNo;

    public MdrsIndependentTeam() {}
    
	public MdrsIndependentTeamId getId() {
		return this.id;
	}

	public void setId(MdrsIndependentTeamId id) {
		this.id = id;
	}

	public Date getEffBegDate() {
		return this.effBegDate;
	}

	public void setEffBegDate(Date effBegDate) {
		this.effBegDate = effBegDate;
	}

	public Date getEffEndDate() {
		return this.effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	public Date getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}

	public int getProdRateId() {
		return this.prodRateId;
	}

	public void setProdRateId(int prodRateId) {
		this.prodRateId = prodRateId;
	}

	public String getTeamNo() {
		return StringUtils.trim(this.teamNo);
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public short getTeamSeqNo() {
		return this.teamSeqNo;
	}

	public void setTeamSeqNo(short teamSeqNo) {
		this.teamSeqNo = teamSeqNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((effBegDate == null) ? 0 : effBegDate.hashCode());
		result = prime * result
				+ ((effEndDate == null) ? 0 : effEndDate.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + prodRateId;
		result = prime * result + ((teamNo == null) ? 0 : teamNo.hashCode());
		result = prime * result + teamSeqNo;
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
		MdrsIndependentTeam other = (MdrsIndependentTeam) obj;
		if (effBegDate == null) {
			if (other.effBegDate != null)
				return false;
		} else if (!effBegDate.equals(other.effBegDate))
			return false;
		if (effEndDate == null) {
			if (other.effEndDate != null)
				return false;
		} else if (!effEndDate.equals(other.effEndDate))
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
		if (prodRateId != other.prodRateId)
			return false;
		if (teamNo == null) {
			if (other.teamNo != null)
				return false;
		} else if (!teamNo.equals(other.teamNo))
			return false;
		if (teamSeqNo != other.teamSeqNo)
			return false;
		return true;
	}	

	@Override
	public String toString() {
		return toString(getId().getTeamId(), getId().getPlantLocCode(), getId().getDeptCode(),
				getId().getModelYearDate(), getId().getProdSchQty(), getId().getVehicleModelCode(),
				getId().getProdAsmLineNo(), getEffBegDate(), getEffEndDate(), getExtractDate(), 
				getProdRateId(), getTeamNo(), getTeamSeqNo());
	}
}