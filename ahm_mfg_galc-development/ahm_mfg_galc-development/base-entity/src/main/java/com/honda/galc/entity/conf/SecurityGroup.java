package com.honda.galc.entity.conf;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * SecurityGroup represents plant floor security group
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
@Table(name = "GAL324TBX")
public class SecurityGroup extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SECURITY_GRP")
    private String securityGroup;

    @Column(name = "GROUP_NAME")
    private String groupName;

    public SecurityGroup() {
        super();
    }

    public String getSecurityGroup() {
        return StringUtils.trimToEmpty(this.securityGroup);
    }
    
    public String getId() {
    	return StringUtils.trimToEmpty(getSecurityGroup());
    }

    public void setSecurityGroup(String securityGrp) {
        this.securityGroup = securityGrp;
    }

    public String getGroupName() {
        return StringUtils.trimToEmpty(this.groupName);
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getDisplayText() {
    	String result = "";
    	if(groupName != null) result = groupName + " ";
    	if(securityGroup != null) result += "[" + securityGroup + "]";
    	return result;
    }
    
    public Timestamp getCreateTimestamp() {
       if(super.getCreateTimestamp() == null) {
           this.setCreateTimestamp( new Timestamp(System.currentTimeMillis()));
       }
       return super.getCreateTimestamp();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((securityGroup == null) ? 0 : securityGroup.hashCode());
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
		SecurityGroup other = (SecurityGroup) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (securityGroup == null) {
			if (other.securityGroup != null)
				return false;
		} else if (!securityGroup.equals(other.securityGroup))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SecurityGroup [securityGroup=" + securityGroup + ", groupName=" + groupName + "]";
	}

    
}
