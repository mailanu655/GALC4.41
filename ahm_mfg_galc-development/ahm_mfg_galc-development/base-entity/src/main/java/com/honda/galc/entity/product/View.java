package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.0 
* @author Cody Getz 
* @since Jul 15, 2013
*/
@Entity
@Table(name = "VIEW_TBX")
public class View extends AuditEntry {


    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private ViewId id;

    public View() {
        super();
    }
    
    public View(String layerId, String featureId) {
    	this.id = new ViewId(layerId, featureId);
    }

    public ViewId getId() {
        return this.id;
    }

    public void setId(ViewId id) {
        this.id = id;
    }
	
	public String getViewId() {
		return getId().getViewId();
	}
	
	public String getLayerId() {
		return getId().getLayerId();
	}

	@Override
	public String toString() {
		return toString(getViewId(), getLayerId());
	}

}
