package com.honda.galc.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * AuditEntry is generic entry supporting create and update time stamps
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
@MappedSuperclass()
public abstract class AuditEntry extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    @Column(name = "CREATE_TIMESTAMP", insertable = false, updatable = false)
    private Timestamp createTimestamp;

    @Column(name = "UPDATE_TIMESTAMP")
    private Timestamp updateTimestamp;

    public AuditEntry() {
        super();
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
  
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuditEntry)) {
			return false;
		}
		if (!(getClass().isAssignableFrom(obj.getClass()))) {
			return false;
		}
		AuditEntry e = (AuditEntry) obj;
		return getId() == null ? e.getId() == null : getId().equals(e.getId());
	}    
}
