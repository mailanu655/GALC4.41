package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * ComponentPropertyId is ID of ComponentProperty object identify property<br>
 * by its key and component ID
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
@Embeddable
public class ComponentPropertyId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "COMPONENT_ID")
    private String componentId;

    @Column(name = "PROPERTY_KEY")
    private String propertyKey;
    
    public ComponentPropertyId(String componentId, String propertyKey) {
    	this.componentId = StringUtils.trim(componentId);
    	this.propertyKey = StringUtils.trim(propertyKey);
    }
    
    public ComponentPropertyId() {
        super();
    }

    public String getComponentId() {
        return StringUtils.trim(componentId);
    }

    public void setComponentId(String componentId) {
        this.componentId = StringUtils.trim(componentId);
    }

    public String getPropertyKey() {
        return StringUtils.trim(propertyKey);
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = StringUtils.trim(propertyKey);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ComponentPropertyId)) {
            return false;
        }
        ComponentPropertyId other = (ComponentPropertyId) o;
        return this.getComponentId().equals(other.getComponentId())
                && this.getPropertyKey().equals(other.getPropertyKey());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.componentId.hashCode();
        hash = hash * prime + this.propertyKey.hashCode();
        return hash;
    }
    
    public String toString() {
        return componentId + "," + propertyKey;
    }

}
