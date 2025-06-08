package com.honda.galc.entity.qi;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiEntryModelVersioningStatus;

/**
 * 
 * <h3>QIBOMQICSPartMappingId Class description</h3>
 * <p>
 * QIBOMQICSPartMappingId contains the getter and setter of the BOM Part mapping composite key and maps this class with database and these columns .
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
@Entity
@Table(name = "QI_TEXT_ENTRY_MENU_TBX")
public class QiTextEntryMenu  extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey=true,sequence=1)
	private QiTextEntryMenuId id;
	
	@Auditable(isPartOfPrimaryKey=true,sequence=2)
	@Column(name = "TEXT_ENTRY_MENU_DESC")
	private String textEntryMenuDesc;
	
	public QiTextEntryMenu() {
		super();
	}
	
	public QiTextEntryMenuId getId() {
		return this.id;
	}

	public void setId(QiTextEntryMenuId id) {
		this.id = id;
	}

	public QiTextEntryMenu(String entryScreen,String textEntryMenu, String entryModel, short isUsed) {
	    this.id = new QiTextEntryMenuId(entryScreen,textEntryMenu, entryModel, isUsed);
	}
	

	public String getTextEntryMenuDesc() {
		return StringUtils.trimToEmpty(textEntryMenuDesc);
	}

	public void setTextEntryMenuDesc(String textEntryMenuDesc) {
		this.textEntryMenuDesc = textEntryMenuDesc;
	}
	
	public String getIsUsedVersion(){
		return QiEntryModelVersioningStatus.getType(getId().getIsUsed()).getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((textEntryMenuDesc == null) ? 0 : textEntryMenuDesc
						.hashCode());
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
		QiTextEntryMenu other = (QiTextEntryMenu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (textEntryMenuDesc == null) {
			if (other.textEntryMenuDesc != null)
				return false;
		} else if (!textEntryMenuDesc.equals(other.textEntryMenuDesc))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return toString(id.getEntryScreen(),id.getTextEntryMenu());
	}
    
	
   
}
