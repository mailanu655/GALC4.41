package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>InspectionTwoPartDescrption Class description</h3>
 * <p> InspectionTwoPartDescrption description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 30, 2011
 *
 *
 */
/** * *
* @version 
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
@Entity
@Table(name="GAL364TBX")
public class InspectionTwoPartDescription extends AuditEntry {
	@EmbeddedId
	private InspectionTwoPartDescriptionId id;

	@Column(name="DESCRIPTION_ID")
	private int descriptionId;

	private static final long serialVersionUID = 1L;

	public InspectionTwoPartDescription() {
		super();
	}

	public InspectionTwoPartDescriptionId getId() {
		return this.id;
	}

	public void setId(InspectionTwoPartDescriptionId id) {
		this.id = id;
	}

	public int getDescriptionId() {
		return this.descriptionId;
	}

	public void setDescriptionId(int descriptionId) {
		this.descriptionId = descriptionId;
	}
	
	public void setPartGroupName(String partGroupName) {
    	if(id == null) id = new InspectionTwoPartDescriptionId();
    	id.setPartGroupName(partGroupName);
    }
    
    public String getPartGroupName() {
    	return id == null ? null : id.getPartGroupName();
    }
    
    public void setInspectionPartName(String inspectionPartName) {
    	if(id == null) id = new InspectionTwoPartDescriptionId();
    	id.setInspectionPartName(inspectionPartName);
    }
    
    public String getInspectionPartName() {
    	return id == null ? null : id.getInspectionPartName();
    }
    
    public void setInspectionPartLocationName(String inspectionPartLocationName) {
    	if(id == null) id = new InspectionTwoPartDescriptionId();
    	id.setInspectionPartLocationName(inspectionPartLocationName);
    }
    
    public String getInspectionPartLocationName() {
    	return id == null ? null : id.getInspectionPartLocationName();
    }
    
    public String getTwoPartPairPart() {
		return id == null ? null : id.getTwoPartPairPart();
	}

	public void setTwoPartPairPart(String twoPartPairPart) {
		if(id == null) id = new InspectionTwoPartDescriptionId();
    	id.setTwoPartPairPart(twoPartPairPart);
	}
	
	public String getTwoPartPairLocation() {
		return id == null ? null : id.getTwoPartPairLocation();
	}

	public void setTwoPartPairLocation(String twoPartPairLocation) {
		if(id == null) id = new InspectionTwoPartDescriptionId();
    	id.setTwoPartPairLocation(twoPartPairLocation);
	}

	@Override
	public String toString() {
		return toString(id.getInspectionPartName(),id.getInspectionPartLocationName(),
				id.getPartGroupName(),id.getTwoPartPairLocation(),id.getTwoPartPairPart(),
				getDescriptionId());
	}

}
