package com.honda.galc.entity.fif;

import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>FifCodeGroups.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FifCodeGroups.java description </p>
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
 * The persistent class for the FIF_CODE_GROUPS_TBX database table.
 * 
 */
@Entity
@Table(name = "FIF_CODE_GROUPS_TBX")
public class FifCodeGroups extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FifCodeGroupsId id;

	@Column(name = "GROUP_DESC")
	private String groupDesc;

	@Column(name = "EFCT_END_DT")
	private Date efctEndDt;

	@Column(name = "REQUIRED")
	private String required;

	@Column(name = "FIF_LENGTH")
	private int fifLength;

	@Column(name = "FIF_OFFSET")
	private int fifOffset;

	public FifCodeGroups() {
		super();
	}

	public FifCodeGroups(FifCodeGroupsId id, Date efecEndDt, String required,
			int fifLength, int fifOffset) {
		super();
		this.id = id;
		this.efctEndDt = efecEndDt;
		this.required = required;
		this.fifLength = fifLength;
		this.fifOffset = fifOffset;
	}

	public FifCodeGroupsId getId() {
		return id;
	}

	public void setId(FifCodeGroupsId id) {
		this.id = id;
	}

	public String getGroupDesc() {
		return StringUtils.trim(groupDesc);
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public Date getEfctEndDt() {
		return efctEndDt;
	}

	public void setEfctEndDt(Date efctEndDt) {
		this.efctEndDt = efctEndDt;
	}

	public String getRequired() {
		return StringUtils.trim(required);
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public int getFifLength() {
		return fifLength;
	}

	public void setFifLength(int fifLength) {
		this.fifLength = fifLength;
	}

	public int getFifOffset() {
		return fifOffset;
	}

	public void setFifOffset(int fifOffset) {
		this.fifOffset = fifOffset;
	}

	@Override
	public String toString() {
		return toString(id.getPlantCd(), id.getModelYear(), id.getModelCd(),
				id.getDevSeqCd(), id.getBaseType(), id.getFifType(),
				id.getGroupCd(), id.getEfctBegDt(), getGroupDesc(),
				getEfctEndDt(), getRequired(), getFifLength(), getFifOffset());

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((efctEndDt == null) ? 0 : efctEndDt.hashCode());
		result = prime * result + fifLength;
		result = prime * result + fifOffset;
		result = prime * result
				+ ((groupDesc == null) ? 0 : groupDesc.hashCode());

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
		FifCodeGroups other = (FifCodeGroups) obj;
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
		if (fifLength != other.fifLength) {
			return false;
		}
		if (fifOffset != other.fifOffset) {
			return false;
		}
		if (groupDesc == null) {
			if (other.groupDesc != null)
				return false;
		} else if (!groupDesc.equals(other.groupDesc))
			return false;

		return true;
	}
}
