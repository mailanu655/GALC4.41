package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * UserSecurityGroupId is ID for UserSecurityGroup
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
 * @see UserSecurityGroup
 */
@Embeddable
public class UserSecurityGroupId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "USER_ID")
    private String userId;
    
    @Column(name = "SECURITY_GRP")
    private String securityGroup;

    public UserSecurityGroupId() {
        super();
    }

    public UserSecurityGroupId(String userId, String securityGroup) {
        this.userId = userId;
        this.securityGroup = securityGroup;
    }


    public String getUserId() {
        return StringUtils.trimToEmpty(this.userId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecurityGroup() {
        return StringUtils.trimToEmpty(this.securityGroup);
    }

    public void setSecurityGroup(String securityGrp) {
        this.securityGroup = securityGrp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserSecurityGroupId)) {
            return false;
        }
        UserSecurityGroupId other = (UserSecurityGroupId) o;
        return this.getUserId().equals(other.getUserId())
                && this.getSecurityGroup().equals(other.getSecurityGroup());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.userId.hashCode();
        hash = hash * prime + this.securityGroup.hashCode();
        return hash;
    }

    @Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
