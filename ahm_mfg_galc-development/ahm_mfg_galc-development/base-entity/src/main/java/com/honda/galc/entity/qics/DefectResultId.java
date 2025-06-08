package com.honda.galc.entity.qics;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>DefectResultId Class description</h3>
 * <p> DefectResultId description </p>
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
public class DefectResultId implements Serializable {
	@Column(name="INSPECTION_PART_NAME")
	private String inspectionPartName;

	@Column(name="INSPECTION_PART_LOCATION_NAME")
	private String inspectionPartLocationName;

	@Column(name="DEFECT_TYPE_NAME")
	private String defectTypeName;

	@Column(name="SECONDARY_PART_NAME")
	private String secondaryPartName;

	@Column(name="DEFECTRESULTID")
	private int defectResultId;

	@Column(name="APPLICATION_ID")
	private String applicationId;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TWO_PART_PAIR_PART")
	private String twoPartPairPart;

	@Column(name="TWO_PART_PAIR_LOCATION")
	private String twoPartPairLocation;

	private static final long serialVersionUID = 1L;

	public DefectResultId() {
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

	public String getSecondaryPartName() {
		return StringUtils.trim(this.secondaryPartName);
	}

	public void setSecondaryPartName(String secondaryPartName) {
		this.secondaryPartName = secondaryPartName;
	}

	public int getDefectResultId() {
		return this.defectResultId;
	}

	public void setDefectResultId(int defectResultId) {
		this.defectResultId = defectResultId;
	}

	public String getApplicationId() {
		return StringUtils.trim(this.applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getProductId() {
		return StringUtils.trim( this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefectResultId other = (DefectResultId) obj;
		if (getApplicationId() == null) {
			if (other.getApplicationId() != null)
				return false;
		} else if (!getApplicationId().equals(other.getApplicationId()))
			return false;
		if (getDefectResultId() != other.getDefectResultId())
			return false;
		if (getDefectTypeName() == null) {
			if (other.getDefectTypeName() != null)
				return false;
		} else if (!getDefectTypeName().equals(other.getDefectTypeName()))
			return false;
		if (getInspectionPartLocationName() == null) {
			if (other.getInspectionPartLocationName() != null)
				return false;
		} else if (!getInspectionPartLocationName()
				.equals(other.getInspectionPartLocationName()))
			return false;
		if (getInspectionPartName() == null) {
			if (other.getInspectionPartName() != null)
				return false;
		} else if (!getInspectionPartName().equals(other.getInspectionPartName()))
			return false;
		if (getProductId() == null) {
			if (other.getProductId() != null)
				return false;
		} else if (!getProductId().equals(other.getProductId()))
			return false;
		if (getSecondaryPartName() == null) {
			if (other.getSecondaryPartName() != null)
				return false;
		} else if (!getSecondaryPartName().equals(other.getSecondaryPartName()))
			return false;
		if (getTwoPartPairLocation() == null) {
			if (other.getTwoPartPairLocation() != null)
				return false;
		} else if (!getTwoPartPairLocation().equals(other.getTwoPartPairLocation()))
			return false;
		if (getTwoPartPairPart() == null) {
			if (other.getTwoPartPairPart() != null)
				return false;
		} else if (!getTwoPartPairPart().equals(other.getTwoPartPairPart()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.inspectionPartName.hashCode();
		hash = hash * prime + this.inspectionPartLocationName.hashCode();
		hash = hash * prime + this.defectTypeName.hashCode();
		hash = hash * prime + this.secondaryPartName.hashCode();
		hash = hash * prime + this.defectResultId;
		hash = hash * prime + this.applicationId.hashCode();
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.twoPartPairPart.hashCode();
		hash = hash * prime + this.twoPartPairLocation.hashCode();
		return hash;
	}
	
	@Override
    public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getSimpleName()).append("(");
            sb.append(productId).append(",");
            sb.append(inspectionPartName).append(",");
            sb.append(inspectionPartLocationName).append(",");
            sb.append(defectTypeName).append(",");
            sb.append(applicationId).append(",");
            sb.append(defectResultId);
            sb.append(")");

            return sb.toString();
    }


}
