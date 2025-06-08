package com.honda.galc.entity.qics;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>InspectionTwoPartDescrptionId Class description</h3>
 * <p> InspectionTwoPartDescrptionId description </p>
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
@Embeddable
public class InspectionTwoPartDescriptionId implements Serializable {
	@Column(name="INSPECTION_PART_NAME")
	private String inspectionPartName;

	@Column(name="INSPECTION_PART_LOCATION_NAME")
	private String inspectionPartLocationName;

	@Column(name="TWO_PART_PAIR_LOCATION")
	private String twoPartPairLocation;

	@Column(name="PART_GROUP_NAME")
	private String partGroupName;

	@Column(name="TWO_PART_PAIR_PART")
	private String twoPartPairPart;

	private static final long serialVersionUID = 1L;

	public InspectionTwoPartDescriptionId() {
		super();
	}
	
	

	public InspectionTwoPartDescriptionId(String inspectionPartName,
			String inspectionPartLocationName, String twoPartPairLocation,
			String partGroupName, String twoPartPairPart) {
		super();
		this.inspectionPartName = inspectionPartName;
		this.inspectionPartLocationName = inspectionPartLocationName;
		this.twoPartPairLocation = twoPartPairLocation;
		this.partGroupName = partGroupName;
		this.twoPartPairPart = twoPartPairPart;
	}



	public String getInspectionPartName() {
		return StringUtils.trim(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trim(this.inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getTwoPartPairLocation() {
		return StringUtils.trim(this.twoPartPairLocation);
	}

	public void setTwoPartPairLocation(String twoPartPairLocation) {
		this.twoPartPairLocation = twoPartPairLocation;
	}

	public String getPartGroupName() {
		return StringUtils.trim(this.partGroupName);
	}

	public void setPartGroupName(String partGroupName) {
		this.partGroupName = partGroupName;
	}

	public String getTwoPartPairPart() {
		return StringUtils.trim(this.twoPartPairPart);
	}

	public void setTwoPartPairPart(String twoPartPairPart) {
		this.twoPartPairPart = twoPartPairPart;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof InspectionTwoPartDescriptionId)) {
			return false;
		}
		InspectionTwoPartDescriptionId other = (InspectionTwoPartDescriptionId) o;
		return this.inspectionPartName.equals(other.inspectionPartName)
			&& this.inspectionPartLocationName.equals(other.inspectionPartLocationName)
			&& this.twoPartPairLocation.equals(other.twoPartPairLocation)
			&& this.partGroupName.equals(other.partGroupName)
			&& this.twoPartPairPart.equals(other.twoPartPairPart);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.inspectionPartName.hashCode();
		hash = hash * prime + this.inspectionPartLocationName.hashCode();
		hash = hash * prime + this.twoPartPairLocation.hashCode();
		hash = hash * prime + this.partGroupName.hashCode();
		hash = hash * prime + this.twoPartPairPart.hashCode();
		return hash;
	}

}
