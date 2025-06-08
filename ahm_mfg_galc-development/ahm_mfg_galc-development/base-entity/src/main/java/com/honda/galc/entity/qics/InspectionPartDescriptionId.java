package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Embeddable
public class InspectionPartDescriptionId implements Serializable {
    @Column(name = "INSPECTION_PART_NAME")
    private String inspectionPartName;

    @Column(name = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;

    @Column(name = "PART_GROUP_NAME")
    private String partGroupName;

    private static final long serialVersionUID = 1L;

    public InspectionPartDescriptionId() {
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
        if (!(o instanceof InspectionPartDescriptionId)) {
            return false;
        }
        InspectionPartDescriptionId other = (InspectionPartDescriptionId) o;
        return this.inspectionPartName.equals(other.inspectionPartName)
                && this.inspectionPartLocationName.equals(other.inspectionPartLocationName)
                && this.partGroupName.equals(other.partGroupName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.inspectionPartName.hashCode();
        hash = hash * prime + this.inspectionPartLocationName.hashCode();
        hash = hash * prime + this.partGroupName.hashCode();
        return hash;
    }

}
