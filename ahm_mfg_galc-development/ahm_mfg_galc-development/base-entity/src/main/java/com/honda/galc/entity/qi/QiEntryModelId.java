package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;

/**
 * 
 * <h3>QiEntryModelId Class description</h3>
 * <p>
 * QiEntryModelId contains the getter and setter of the entry model properties and maps this class with database table and properties with the database its columns .
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
 *         May 4, 2017
 * 
 */
@Embeddable
public class QiEntryModelId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "ENTRY_MODEL")
	@Auditable(isPartOfPrimaryKey=true,sequence=1)
	private String entryModel;
	
	@Column(name = "IS_USED" , nullable=false)
	private short isUsed;
	
	public QiEntryModelId() {
	}
	
	public QiEntryModelId(String entryModel, short isUsed) {
		this.entryModel = entryModel;
		this.isUsed = isUsed;
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
	public boolean isUsed() {
		return this.isUsed ==(short) 1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		QiEntryModelId other = (QiEntryModelId) obj;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (isUsed != other.isUsed)
			return false;
		return true;
	}

}
