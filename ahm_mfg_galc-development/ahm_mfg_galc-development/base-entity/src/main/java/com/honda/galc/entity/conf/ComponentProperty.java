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
 * ComponentProperty is a property entry of some component
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
@Entity
@Table(name = "GAL489TBX")
public class ComponentProperty extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ComponentPropertyId id;

    @Column(name = "PROPERTY_VALUE")
    private String propertyValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CHANGE_USER_ID")
    private String changeUserId;

    public ComponentProperty(String componentId, String propertyKey, String propertyValue) {
    	this.id = new ComponentPropertyId(componentId,propertyKey);
    	this.propertyValue = StringUtils.trim(propertyValue);
    }
    
    public ComponentProperty() {
        super();
    }

    public ComponentPropertyId getId() {
        return this.id;
    }

    public void setId(ComponentPropertyId id) {
        this.id = id;
    }
    
    public String getPropertyKey() {
    	return getId().getPropertyKey();
    }
    
    public String getPropertyValue() {
        return StringUtils.trim(propertyValue);
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = StringUtils.trim(propertyValue);
    }

    public String getDescription() {
        return StringUtils.trim(description);
    }

    public void setDescription(String description) {
        this.description = StringUtils.trim(description);
    }

    public String getChangeUserId() {
        return StringUtils.trim(changeUserId);
    }

    public void setChangeUserId(String changeUserId) {
        this.changeUserId = StringUtils.trim(changeUserId);
    }
    
    public String toString() {
        return "ComponentProperty(" + id + "," + propertyValue + ")";
    }
    
    public ComponentProperty clone() {
    	ComponentProperty clone = new ComponentProperty(getId().getComponentId(),getId().getPropertyKey(),getPropertyValue());
    	clone.setDescription(getDescription());
    	clone.setChangeUserId(getChangeUserId());
     	return clone;
    }

}
