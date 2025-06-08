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
public class ComponentStatusId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "COMPONENT_ID")
    private String componentId;

    @Column(name = "STATUS_KEY")
    private String statusKey;
    
    public ComponentStatusId() {
        super();
    }

	public ComponentStatusId(String componentId, String statusKey) {
    	this.componentId = componentId;
    	this.statusKey = statusKey;
    }
    
    public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

    public String getComponentId() {
        return StringUtils.trim(componentId);
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ComponentStatusId)) {
            return false;
        }
        ComponentStatusId other = (ComponentStatusId) o;
        return this.getComponentId().equals(other.getComponentId())
                && this.getStatusKey().equals(other.getStatusKey());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.componentId.hashCode();
        hash = hash * prime + this.statusKey.hashCode();
        return hash;
    }
    
    public String toString() {
        return componentId + "," + statusKey;
    }

}
