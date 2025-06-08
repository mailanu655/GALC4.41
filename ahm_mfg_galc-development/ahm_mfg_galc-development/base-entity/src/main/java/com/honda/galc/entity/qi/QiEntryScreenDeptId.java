package com.honda.galc.entity.qi;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiEntryScreenDeptId Class description</h3>
 * <p>
 * QiEntryScreenDeptId contains the getter and setter of the QiEntryScreenDept mapping composite key and maps this class with database and these columns.
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
 *        Jul 26, 2016
 * 
 */
@Embeddable
public class QiEntryScreenDeptId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ENTRY_SCREEN", nullable=false)
	private String entryScreen;
	
	@Column(name = "DIVISION_ID")
	private String divisionId;
	
	@Column(name = "ENTRY_MODEL" , nullable=false)
	private String entryModel;
	
	@Column(name = "IS_USED" , nullable=false)
	private short isUsed;
	
	
	public QiEntryScreenDeptId() {
		super();
	}

	public QiEntryScreenDeptId(String entryScreen, String divisionId,
			String entryModel, short isUsed) {
		super();
		this.entryScreen = entryScreen;
		this.divisionId = divisionId;
		this.entryModel = entryModel;
		this.isUsed = isUsed;
	}


	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}


	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}



	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}



	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}
		
	public short getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(short isUsed) {
		this.isUsed = isUsed;
	}

	public boolean IsUsed() {
		return this.isUsed ==(short) 1;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result
				+ ((entryModel == null) ? 0 : entryModel.hashCode());
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
		QiEntryScreenDeptId other = (QiEntryScreenDeptId) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
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
