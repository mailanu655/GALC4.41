package com.honda.galc.entity.conf;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * AccessControlEntry defines operations allowed to be done by users in given security<br>
 * group for a given screen
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Aug 31, 2009</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/** * *
* @version 0.2
* @author Gangadhararao Gadde
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL283TBX")
public class AccessControlEntry extends AuditEntry{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AccessControlEntryId id;

    private int operation;

    public AccessControlEntry() {
        super();
    }

    public AccessControlEntry(String screenId,String securityGroup){
    	this.id = new AccessControlEntryId(screenId,securityGroup);
    }

    public AccessControlEntryId getId() {
        return this.id;
    }

    public void setId(AccessControlEntryId id) {
        this.id = id;
    }

    public int getOperation() {
        return this.operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }
	@Override
	public String toString() {
		return toString(id.getScreenId(),id.getSecurityGroup(),getOperation());
	}

}
