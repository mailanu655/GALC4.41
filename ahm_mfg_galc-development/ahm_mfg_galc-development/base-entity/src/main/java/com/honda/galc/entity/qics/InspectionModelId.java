package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Embeddable
public class InspectionModelId implements Serializable {
    @Column(name = "MODEL_CODE")
    private String modelCode;

    @Column(name = "PART_GROUP_NAME")
    private String partGroupName;

    @Column(name = "DEFECT_GROUP_NAME")
    private String defectGroupName;

    @Column(name = "APPLICATION_ID")
    private String applicationId;

    private static final long serialVersionUID = 1L;

    public InspectionModelId() {
        super();
    }

    public String getModelCode() {
        return StringUtils.trim(this.modelCode);
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getPartGroupName() {
        return StringUtils.trim(this.partGroupName);
    }

    public void setPartGroupName(String partGroupName) {
        this.partGroupName = partGroupName;
    }

    public String getDefectGroupName() {
        return StringUtils.trim(this.defectGroupName);
    }

    public void setDefectGroupName(String defectGroupName) {
        this.defectGroupName = defectGroupName;
    }

    public String getApplicationId() {
        return StringUtils.trim(this.applicationId);
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InspectionModelId)) {
            return false;
        }
        InspectionModelId other = (InspectionModelId) o;
        return this.modelCode.equals(other.modelCode)
                && this.partGroupName.equals(other.partGroupName)
                && this.defectGroupName.equals(other.defectGroupName)
                && this.applicationId.equals(other.applicationId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.modelCode.hashCode();
        hash = hash * prime + this.partGroupName.hashCode();
        hash = hash * prime + this.defectGroupName.hashCode();
        hash = hash * prime + this.applicationId.hashCode();
        return hash;
    }

}
