package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 
 * <h3>DefectTypeDescriptionId Class description</h3>
 * <p> DefectTypeDescriptionId description </p>
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
@Embeddable
public class DefectTypeDescriptionId implements Serializable {
    @Column(name = "DEFECT_TYPE_NAME")
    private String defectTypeName;

    @Column(name = "SECONDARY_PART_NAME")
    private String secondaryPartName;

    @Column(name = "DEFECT_GROUP_NAME")
    private String defectGroupName;

    private static final long serialVersionUID = 1L;

    public DefectTypeDescriptionId() {
        super();
    }

    public String getDefectTypeName() {
        return StringUtils.trim(this.defectTypeName);
    }

    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }

    public String getSecondaryPartName() {
        return StringUtils.trim(this.secondaryPartName);
    }

    public void setSecondaryPartName(String secondaryPartName) {
        this.secondaryPartName = secondaryPartName;
    }

    public String getDefectGroupName() {
        return StringUtils.trim(this.defectGroupName);
    }

    public void setDefectGroupName(String defectGroupName) {
        this.defectGroupName = defectGroupName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DefectTypeDescriptionId)) {
            return false;
        }
        DefectTypeDescriptionId other = (DefectTypeDescriptionId) o;
        return this.defectTypeName.equals(other.defectTypeName)
                && this.secondaryPartName.equals(other.secondaryPartName)
                && this.defectGroupName.equals(other.defectGroupName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.defectTypeName.hashCode();
        hash = hash * prime + this.secondaryPartName.hashCode();
        hash = hash * prime + this.defectGroupName.hashCode();
        return hash;
    }

}
