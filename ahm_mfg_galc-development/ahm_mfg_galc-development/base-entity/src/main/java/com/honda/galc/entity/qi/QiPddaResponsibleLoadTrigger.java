package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiPddaResponsibleLoadTrigger Class </h3>
 * <p> QiPddaResponsibleLoadTrigger contains the getter and setter of the PddaResponsibleLodaTrigger properties and maps this class with database table and properties with its columns . </p>
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
 *        May 12, 2017
 * 
 */
@Entity
@Table(name = "QI_PDDA_RESP_LOAD_TRIGGER_TBX")
public class QiPddaResponsibleLoadTrigger extends CreateUserAuditEntry{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	QiPddaResponsibleLoadTriggerId id;
	
	@Column(name = "PREV_RESP_LEVEL1")
	@Auditable(isPartOfPrimaryKey=false,sequence=2)
	private String prevRespLevel1;
	
	@Column(name = "PREV_BASE_PART_NO")
	@Auditable(isPartOfPrimaryKey=false,sequence=3)
	private String prevBasePartNo;
	
	@Column(name = "PREV_RESP_LEVEL1_DESC")
	@Auditable(isPartOfPrimaryKey=false,sequence=4)
	private String prevRespLevel1Desc;
	
	@Column(name = "PREV_PROCESS_NUMBER")
	@Auditable(isPartOfPrimaryKey=false,sequence=5)
	private String prevProcessNumber;
	
	@Column(name = "PREV_PROCESS_NAME")
	@Auditable(isPartOfPrimaryKey=false,sequence=6)
	private String prevProcessName;
	
	@Column(name = "PREV_UNIT_DESC")
	@Auditable(isPartOfPrimaryKey=false,sequence=7)
	private String prevUnitDesc;
	
	@Column(name = "PREV_PDDA_LINE")
	@Auditable(isPartOfPrimaryKey=false,sequence=8)
	private String prevPddaLine;
	
	@Column(name = "ADMIN_CONFIRMED_FIX")
	@Auditable(isPartOfPrimaryKey=false,sequence=8)
	private short adminConfirmedFix;
	
	public String getPrevRespLevel1() {
		return StringUtils.trimToEmpty(prevRespLevel1);
	}



	public void setPrevRespLevel1(String prevRespLevel1) {
		this.prevRespLevel1 = prevRespLevel1;
	}



	public String getPrevBasePartNo() {
		return StringUtils.trimToEmpty(prevBasePartNo);
	}



	public void setPrevBasePartNo(String prevBasePartNo) {
		this.prevBasePartNo = prevBasePartNo;
	}



	public String getPrevRespLevel1Desc() {
		return StringUtils.trimToEmpty(prevRespLevel1Desc);
	}



	public void setPrevRespLevel1Desc(String prevRespLevel1Desc) {
		this.prevRespLevel1Desc = prevRespLevel1Desc;
	}



	public String getPrevProcessNumber() {
		return StringUtils.trimToEmpty(prevProcessNumber);
	}



	public void setPrevProcessNumber(String prevProcessNumber) {
		this.prevProcessNumber = prevProcessNumber;
	}



	public String getPrevProcessName() {
		return StringUtils.trimToEmpty(prevProcessName);
	}



	public void setPrevProcessName(String prevProcessName) {
		this.prevProcessName = prevProcessName;
	}



	public String getPrevUnitDesc() {
		return StringUtils.trimToEmpty(prevUnitDesc);
	}



	public void setPrevUnitDesc(String prevUnitDesc) {
		this.prevUnitDesc = prevUnitDesc;
	}



	public short getAdminConfirmedFix() {
		return adminConfirmedFix;
	}



	public void setAdminConfirmedFix(short adminConfirmedFix) {
		this.adminConfirmedFix = adminConfirmedFix;
	}

	public void setId(QiPddaResponsibleLoadTriggerId id) {
		this.id = id;
	}

	public QiPddaResponsibleLoadTriggerId getId() {
		return this.id;
	}
	
	public String getPrevPdaaLine() {
		 return StringUtils.trimToEmpty(prevPddaLine);
	}

	public void setPrevPdaaLine(String prevPdaaLine) {
		this.prevPddaLine = prevPdaaLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adminConfirmedFix;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((prevBasePartNo == null) ? 0 : prevBasePartNo.hashCode());
		result = prime * result + ((prevPddaLine == null) ? 0 : prevPddaLine.hashCode());
		result = prime * result + ((prevProcessName == null) ? 0 : prevProcessName.hashCode());
		result = prime * result + ((prevProcessNumber == null) ? 0 : prevProcessNumber.hashCode());
		result = prime * result + ((prevRespLevel1 == null) ? 0 : prevRespLevel1.hashCode());
		result = prime * result + ((prevRespLevel1Desc == null) ? 0 : prevRespLevel1Desc.hashCode());
		result = prime * result + ((prevUnitDesc == null) ? 0 : prevUnitDesc.hashCode());
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
		QiPddaResponsibleLoadTrigger other = (QiPddaResponsibleLoadTrigger) obj;
		if (adminConfirmedFix != other.adminConfirmedFix)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (prevBasePartNo == null) {
			if (other.prevBasePartNo != null)
				return false;
		} else if (!prevBasePartNo.equals(other.prevBasePartNo))
			return false;
		if (prevPddaLine == null) {
			if (other.prevPddaLine != null)
				return false;
		} else if (!prevPddaLine.equals(other.prevPddaLine))
			return false;
		if (prevProcessName == null) {
			if (other.prevProcessName != null)
				return false;
		} else if (!prevProcessName.equals(other.prevProcessName))
			return false;
		if (prevProcessNumber == null) {
			if (other.prevProcessNumber != null)
				return false;
		} else if (!prevProcessNumber.equals(other.prevProcessNumber))
			return false;
		if (prevRespLevel1 == null) {
			if (other.prevRespLevel1 != null)
				return false;
		} else if (!prevRespLevel1.equals(other.prevRespLevel1))
			return false;
		if (prevRespLevel1Desc == null) {
			if (other.prevRespLevel1Desc != null)
				return false;
		} else if (!prevRespLevel1Desc.equals(other.prevRespLevel1Desc))
			return false;
		if (prevUnitDesc == null) {
			if (other.prevUnitDesc != null)
				return false;
		} else if (!prevUnitDesc.equals(other.prevUnitDesc))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
