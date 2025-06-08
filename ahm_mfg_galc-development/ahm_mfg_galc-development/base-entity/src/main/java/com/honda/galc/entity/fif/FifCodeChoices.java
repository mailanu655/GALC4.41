package com.honda.galc.entity.fif;

import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>FifCodeChoices.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FifCodeChoices.java description </p>
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
 * <TR>
 * <TD>KM</TD>
 * <TD>Feb 17, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Xiaomei Ma
 * @created Feb 17, 2015
 */

/**
 * The persistent class for the FIF_CODE_CHOICES_TBX database table.
 * 
 */
@Entity
@Table(name = "FIF_CODE_CHOICES_TBX")
public class FifCodeChoices extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FifCodeChoicesId id;

	@Column(name = "FIF_DESC")
	private String fifDesc;

	@Column(name = "EFCT_END_DT")
	private Date efctEndDt;

	public FifCodeChoices() {
		super();
	}

	public FifCodeChoices(FifCodeChoicesId id, Date efctEndDt, String fifDesc) {
		super();
		this.id = id;
		this.efctEndDt = efctEndDt;
		this.fifDesc = fifDesc;
	}

	public FifCodeChoicesId getId() {
		return id;
	}

	public void setId(FifCodeChoicesId id) {
		this.id = id;
	}

	public String getFifDesc() {
		return StringUtils.trim(fifDesc);
	}

	public void setFifDesc(String fifDesc) {
		this.fifDesc = fifDesc;
	}

	public Date getEfctEndDt() {
		return efctEndDt;
	}

	public void setEfctEndDt(Date efctEndDt) {
		this.efctEndDt = efctEndDt;
	}

	@Override
	public String toString() {
		return toString(id.getModelYear(), id.getModelCd(), id.getFifCode(),
				id.getFifType(), id.getDevSeqCd(), id.getEfctBegDt(),
				getFifDesc(), getEfctEndDt());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((efctEndDt == null) ? 0 : efctEndDt.hashCode());
		result = prime * result + ((fifDesc == null) ? 0 : fifDesc.hashCode());

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
		FifCodeChoices other = (FifCodeChoices) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (efctEndDt == null) {
			if (other.efctEndDt != null)
				return false;
		} else if (!efctEndDt.equals(other.efctEndDt))
			return false;
		if (fifDesc == null) {
			if (other.fifDesc != null)
				
				return false;
		} else if (!fifDesc.equals(other.fifDesc))
			return false;

		return true;
	}
}
