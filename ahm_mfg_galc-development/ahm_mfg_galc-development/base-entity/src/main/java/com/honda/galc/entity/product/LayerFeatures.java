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
@Table(name = "LAYER_FEATURES_TBX")
public class LayerFeatures extends AuditEntry {
    
    @Column(name="VISIBLE")
	private int visible;

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private LayerFeaturesId id;

    public LayerFeatures() {
        super();
    }
    
    public LayerFeatures(String layerId, String featureId) {
    	this.id = new LayerFeaturesId(layerId, featureId);
    }

    public LayerFeaturesId getId() {
        return this.id;
    }

    public void setId(LayerFeaturesId id) {
        this.id = id;
    }
	
	public String getFeatureId() {
		return getId().getFeatureId();
	}
	
	public String getLayerId() {
		return getId().getLayerId();
	}
	
	public int getVisible()
	{
		return this.visible;
	}
	
	public void setVisible(int visible)
	{
		this.visible = visible;
	}

	@Override
	public String toString() {
		return toString(getLayerId(),getFeatureId());
	}

}
