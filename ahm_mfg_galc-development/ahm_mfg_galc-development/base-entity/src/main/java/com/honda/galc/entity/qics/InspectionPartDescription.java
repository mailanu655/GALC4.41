package com.honda.galc.entity.qics;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL180TBX")
public class InspectionPartDescription extends AuditEntry{
    @EmbeddedId
    private InspectionPartDescriptionId id;

    @Column(name = "DESCRIPTION_ID")
    private int descriptionId;


    private static final long serialVersionUID = 1L;

    public InspectionPartDescription() {
        super();
    }

    public InspectionPartDescriptionId getId() {
        return this.id;
    }

    public void setId(InspectionPartDescriptionId id) {
        this.id = id;
    }

    public int getDescriptionId() {
        return this.descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }
    
    public void setPartGroupName(String partGroupName) {
    	if(id == null) id = new InspectionPartDescriptionId();
    	id.setPartGroupName(partGroupName);
    }
    
    public String getPartGroupName() {
    	return id == null ? null : id.getPartGroupName();
    }
    
    public void setInspectionPartName(String inspectionPartName) {
    	if(id == null) id = new InspectionPartDescriptionId();
    	id.setInspectionPartName(inspectionPartName);
    }
    
    public String getInspectionPartName() {
    	return id == null ? null : id.getInspectionPartName();
    }
    
    public void setInspectionPartLocationName(String inspectionPartLocationName) {
    	if(id == null) id = new InspectionPartDescriptionId();
    	id.setInspectionPartLocationName(inspectionPartLocationName);
    }
    
    public String getInspectionPartLocationName() {
    	return id == null ? null : id.getInspectionPartLocationName();
    }

	@Override
	public String toString() {
		return toString(getInspectionPartName(),getInspectionPartLocationName(),
				getPartGroupName(),getDescriptionId());
	}
    
}
