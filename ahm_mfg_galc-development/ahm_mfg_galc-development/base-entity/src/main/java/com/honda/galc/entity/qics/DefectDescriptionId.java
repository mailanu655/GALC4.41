package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 
 * <h3>DefectDescriptionId Class description</h3>
 * <p> DefectDescriptionId description </p>
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
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class DefectDescriptionId implements Serializable {
    @Column(name = "INSPECTION_PART_NAME")
    private String inspectionPartName;

    @Column(name = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;

    @Column(name = "DEFECT_TYPE_NAME")
    private String defectTypeName;

    @Column(name = "TWO_PART_PAIR_PART")
    private String twoPartPairPart;

    @Column(name = "TWO_PART_PAIR_LOCATION")
    private String twoPartPairLocation;

    @Column(name = "SECONDARY_PART_NAME")
    private String secondaryPartName;

    @Column(name = "PART_GROUP_NAME")
    private String partGroupName;

    private static final long serialVersionUID = 1L;

    public DefectDescriptionId() {
        super();
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

    public String getDefectTypeName() {
        return StringUtils.trim(this.defectTypeName);
    }

    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }

    public String getTwoPartPairPart() {
        return StringUtils.trim(this.twoPartPairPart);
    }

    public void setTwoPartPairPart(String twoPartPairPart) {
        this.twoPartPairPart = twoPartPairPart;
    }

    public String getTwoPartPairLocation() {
        return StringUtils.trim(this.twoPartPairLocation);
    }

    public void setTwoPartPairLocation(String twoPartPairLocation) {
        this.twoPartPairLocation = twoPartPairLocation;
    }

    public String getSecondaryPartName() {
        return StringUtils.trim(this.secondaryPartName);
    }

    public void setSecondaryPartName(String secondaryPartName) {
        this.secondaryPartName = secondaryPartName;
    }

    public String getPartGroupName() {
        return StringUtils.trim(this.partGroupName);
    }

    public void setPartGroupName(String partGroupName) {
        this.partGroupName = partGroupName;
    }

    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DefectDescriptionId)) {
            return false;
        }
        DefectDescriptionId other = (DefectDescriptionId) o;
        return this.inspectionPartName.equals(other.inspectionPartName)
                && this.inspectionPartLocationName.equals(other.inspectionPartLocationName)
                && this.defectTypeName.equals(other.defectTypeName)
                && this.twoPartPairPart.equals(other.twoPartPairPart)
                && this.twoPartPairLocation.equals(other.twoPartPairLocation)
                && this.secondaryPartName.equals(other.secondaryPartName)
                && this.partGroupName.equals(other.partGroupName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.inspectionPartName.hashCode();
        hash = hash * prime + this.inspectionPartLocationName.hashCode();
        hash = hash * prime + this.defectTypeName.hashCode();
        hash = hash * prime + this.twoPartPairPart.hashCode();
        hash = hash * prime + this.twoPartPairLocation.hashCode();
        hash = hash * prime + this.secondaryPartName.hashCode();
        hash = hash * prime + this.partGroupName.hashCode();
        return hash;
    }

}
