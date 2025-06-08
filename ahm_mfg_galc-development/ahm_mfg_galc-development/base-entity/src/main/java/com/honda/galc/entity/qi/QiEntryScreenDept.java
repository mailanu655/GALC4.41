package com.honda.galc.entity.qi;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
@Entity
@Table(name = "QI_ENTRY_SCREEN_DEPT_TBX")
public class QiEntryScreenDept extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiEntryScreenDeptId id;

	public QiEntryScreenDept() {
		super();
	}

	public QiEntryScreenDept(String entryScreen, String divisionId ,String entryModel, Short isUsed) {
	    this.id = new QiEntryScreenDeptId(entryScreen,divisionId, entryModel, isUsed);
	}
	
	public QiEntryScreenDeptId getId() {
		return id;
	}

	public void setId(QiEntryScreenDeptId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		QiEntryScreenDept other = (QiEntryScreenDept) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getEntryScreen(),getId().getDivisionId(), getId().getEntryModel(), getId().IsUsed());
	}  
    
	
   
}
