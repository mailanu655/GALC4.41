package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * AccessControlEntryId is ID of AccessControlEntry
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
 *
 * @see AccessControlEntry
 */
@Embeddable
public class AccessControlEntryId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "SCREEN_ID")
    private String screenId;

    @Column(name = "SECURITY_GRP")
    private String securityGroup;

    public AccessControlEntryId() {
        super();
    }
    
    public AccessControlEntryId(String screenId,String securityGroup) {
        super();
        this.screenId = screenId;
        this.securityGroup = securityGroup;
    }

    public String getScreenId() {
        return StringUtils.trim(this.screenId);
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getSecurityGroup() {
        return StringUtils.trim(this.securityGroup);
    }

    public void setSecurityGroup(String securityGrp) {
        this.securityGroup = securityGrp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccessControlEntryId)) {
            return false;
        }
        AccessControlEntryId other = (AccessControlEntryId) o;
        return this.screenId.equals(other.screenId)
                && this.securityGroup.equals(other.securityGroup);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.screenId.hashCode();
        hash = hash * prime + this.securityGroup.hashCode();
        return hash;
    }
    
}
