package com.honda.galc.entity.conf;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * UserSecurityGroup is a user security group
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
@Table(name = "GAL282TBX")
public class UserSecurityGroup extends AuditEntry{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @Auditable(isPartOfPrimaryKey = true, sequence = 1)
    private UserSecurityGroupId id;

    @Column(name = "ROW_INSERT_TSTMP")
    private Timestamp entryInsertTimestamp;

    @Column(name = "ROW_UPDATE_TSTMP")
    private Timestamp entryUpdateTimetamp;

    @Column(name = "ROW_DISUSE_TSTMP")
    private Timestamp entryDisuseTimestamp;

    @Column(name = "ROW_HISTRC_CNTER")
    private int entryHisotricCounter;

    @Column(name = "ROW_UPDATE_USER")
    private String entryUpdateUser;

    @OneToOne(targetEntity = SecurityGroup.class,fetch = FetchType.EAGER)
    @JoinColumn(name="SECURITY_GRP",referencedColumnName="SECURITY_GRP")
    private SecurityGroup securityGroup;

    public UserSecurityGroup() {
        super();
    }

    public UserSecurityGroup(String userId,String groupId) {
    	this.id = new UserSecurityGroupId();
    	id.setSecurityGroup(groupId);
    	id.setUserId(userId);
    }
    
    public UserSecurityGroupId getId() {
        return this.id;
    }

    public void setId(UserSecurityGroupId pk) {
        this.id = pk;
    }

    public Timestamp getEntryInsertTimestamp() {
        return this.entryInsertTimestamp;
    }

    public void setEntryInsertTimestamp(Timestamp rowInsertTstmp) {
        this.entryInsertTimestamp = rowInsertTstmp;
    }

    public Timestamp getEntryUpdateTimetamp() {
        return this.entryUpdateTimetamp;
    }

    public void setEntryUpdateTimetamp(Timestamp rowUpdateTstmp) {
        this.entryUpdateTimetamp = rowUpdateTstmp;
    }

    public Timestamp getEntryDisuseTimestamp() {
        return this.entryDisuseTimestamp;
    }

    public void setEntryDisuseTimestamp(Timestamp rowDisuseTstmp) {
        this.entryDisuseTimestamp = rowDisuseTstmp;
    }

    public int getEntryHisotricCounter() {
        return this.entryHisotricCounter;
    }

    public void setEntryHisotricCounter(int rowHistrcCnter) {
        this.entryHisotricCounter = rowHistrcCnter;
    }

    public String getEntryUpdateUser() {
        return StringUtils.trimToEmpty(this.entryUpdateUser);
    }

    public void setEntryUpdateUser(String rowUpdateUser) {
        this.entryUpdateUser = rowUpdateUser;
    }
    
    public String getSecurityGroupId() {
        return StringUtils.trimToEmpty(id.getSecurityGroup());
    }
    
    public void setSecurityGroup(SecurityGroup securityGroup) {
    	this.securityGroup = securityGroup;
    }
    
    public SecurityGroup getSecurityGroup() {
    	return this.securityGroup;
    }
    
    public String getGroupName() {
    	return  StringUtils.trimToEmpty(securityGroup.getGroupName());
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryDisuseTimestamp == null) ? 0 : entryDisuseTimestamp.hashCode());
		result = prime * result + entryHisotricCounter;
		result = prime * result + ((entryInsertTimestamp == null) ? 0 : entryInsertTimestamp.hashCode());
		result = prime * result + ((entryUpdateTimetamp == null) ? 0 : entryUpdateTimetamp.hashCode());
		result = prime * result + ((entryUpdateUser == null) ? 0 : entryUpdateUser.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		UserSecurityGroup other = (UserSecurityGroup) obj;
		if (entryDisuseTimestamp == null) {
			if (other.entryDisuseTimestamp != null)
				return false;
		} else if (!entryDisuseTimestamp.equals(other.entryDisuseTimestamp))
			return false;
		if (entryHisotricCounter != other.entryHisotricCounter)
			return false;
		if (entryInsertTimestamp == null) {
			if (other.entryInsertTimestamp != null)
				return false;
		} else if (!entryInsertTimestamp.equals(other.entryInsertTimestamp))
			return false;
		if (entryUpdateTimetamp == null) {
			if (other.entryUpdateTimetamp != null)
				return false;
		} else if (!entryUpdateTimetamp.equals(other.entryUpdateTimetamp))
			return false;
		if (entryUpdateUser == null) {
			if (other.entryUpdateUser != null)
				return false;
		} else if (!entryUpdateUser.equals(other.entryUpdateUser))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (securityGroup == null) {
			if (other.securityGroup != null)
				return false;
		} else if (!securityGroup.equals(other.securityGroup))
			return false;
		return true;
	}

	
    
   
}
