package com.honda.galc.entity.qi;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiEntryModelGroupingId Class description</h3>
 * <p>
 * QiEntryModel contains the getter and setter of the entry model properties and
 * maps this class with database table and properties with the database its
 * columns .
 * </p>
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
 *         August 29, 2016
 * 
 */
@Embeddable
public class QiEntryModelGroupingId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ENTRY_MODEL", nullable=false)
	private String entryModel;
	
	@Column(name = "MTC_MODEL", nullable=false)
	private String mtcModel;
	@Column(name = "IS_USED" , nullable=false)
	private short isUsed;
	public QiEntryModelGroupingId() {
		super();
	}

	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	public String getMtcModel() {
		return StringUtils.trimToEmpty(mtcModel);
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}
	public short getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(short isUsed) {
		this.isUsed = isUsed;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result + ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result + isUsed;
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
		QiEntryModelGroupingId other = (QiEntryModelGroupingId) obj;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (isUsed != other.isUsed)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

}
