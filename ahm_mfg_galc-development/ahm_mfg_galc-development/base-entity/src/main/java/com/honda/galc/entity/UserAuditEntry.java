package com.honda.galc.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * Added UPDATE_USER for entities that need it.
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Feb. 21, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180221</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */

@MappedSuperclass()
public abstract class UserAuditEntry extends AuditEntry {
	private static final long serialVersionUID = -4780236670153805083L;

    @Column(name = "UPDATE_USER")
    private String updateUser;
	
	public UserAuditEntry() {
        super();
    }
  
	public String getUpdateUser() {
		return StringUtils.trimToEmpty(updateUser);
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}    
}
