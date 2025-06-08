package com.honda.galc.entity.conf;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * DynamicComponentProperty is a property entry of some component
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
 * <TD>Larry Karpov</TD>
 * <TD>May 05, 2014</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
@Entity
@Table(name = "COMPONENT_STATUS_TBX ")
public class ComponentStatus extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ComponentStatusId id;

	@Column(name = "STATUS_VALUE")
    private String statusValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CHANGE_USER_ID")
    private String changeUserId;

    public ComponentStatus() {
        super();
    }

    public ComponentStatus(String componentId, String statusKey, String statusValue) {
    	this.id = new ComponentStatusId(componentId, statusKey);
    	this.statusValue = statusValue;
    }
    
    public ComponentStatusId getId() {
        return this.id;
    }

    public void setId(ComponentStatusId id) {
		this.id = id;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

    public String getDescription() {
        return StringUtils.trim(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChangeUserId() {
        return StringUtils.trim(changeUserId);
    }

    public void setChangeUserId(String changeUserId) {
        this.changeUserId = changeUserId;
    }

    public ComponentStatus clone() {
    	ComponentStatus clone = new ComponentStatus(getId().getComponentId(), getId().getStatusKey(), getStatusValue());
    	clone.setDescription(getDescription());
    	clone.setChangeUserId(getChangeUserId());
     	return clone;
    }

}
