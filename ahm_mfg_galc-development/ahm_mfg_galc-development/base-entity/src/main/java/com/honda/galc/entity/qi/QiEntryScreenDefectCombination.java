package com.honda.galc.entity.qi;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
@Entity
@Table(name = "QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX")
public class QiEntryScreenDefectCombination extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@Auditable
	@EmbeddedId
	private QiEntryScreenDefectCombinationId id;
	
	@Auditable
	@Column(name = "TEXT_ENTRY_MENU")
	private String textEntryMenu;
	
	public QiEntryScreenDefectCombination() {
		super();
	}
	

	public QiEntryScreenDefectCombination(String entryScreen, int regionalDefectCombinationId , String entryModel, short isUsed) {
	    this.id = new QiEntryScreenDefectCombinationId(entryScreen,regionalDefectCombinationId,entryModel,isUsed);
	}


	public void setId(QiEntryScreenDefectCombinationId id) {
		this.id = id;
	}
	
	public QiEntryScreenDefectCombinationId getId() {
		return this.id;
	}


	public String getTextEntryMenu() {
		return StringUtils.trimToEmpty(textEntryMenu);
	}


	public void setTextEntryMenu(String textEntryMenu) {
		this.textEntryMenu = textEntryMenu;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((textEntryMenu == null) ? 0 : textEntryMenu.hashCode());
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
		QiEntryScreenDefectCombination other = (QiEntryScreenDefectCombination) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (textEntryMenu == null) {
			if (other.textEntryMenu != null)
				return false;
		} else if (!textEntryMenu.equals(other.textEntryMenu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getEntryScreen(), getId().getRegionalDefectCombinationId(), getId().getEntryModel(), getId().isUsed(), getTextEntryMenu());
	}	
   
}
