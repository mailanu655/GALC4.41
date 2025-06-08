package com.honda.galc.entity.qics;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Feb 19, 2015
 */
@Embeddable
public class DefectResultHistoryId implements Serializable {
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
	
	@Column(name="CHANGE_TIMESTAMP")
	private Timestamp changeTimestamp;

	private static final long serialVersionUID = 1L;

	public DefectResultHistoryId() {
		super();
	}
 
	
	public DefectResultHistoryId(String inspectionPartName,
			String inspectionPartLocationName, String defectTypeName,
			String secondaryPartName, int defectResultId, String applicationId,
			String productId, String twoPartPairPart, String twoPartPairLocation,Timestamp changeTimestamp) {
		super();
		this.inspectionPartName = inspectionPartName;
		this.inspectionPartLocationName = inspectionPartLocationName;
		this.defectTypeName = defectTypeName;
		this.secondaryPartName = secondaryPartName;
		this.defectResultId = defectResultId;
		this.applicationId = applicationId;
		this.productId = productId;
		this.twoPartPairPart = twoPartPairPart;
		this.twoPartPairLocation = twoPartPairLocation;
		this.changeTimestamp=changeTimestamp;
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
		
	public Timestamp getChangeTimestamp() {
		return changeTimestamp;
	}

	public void setChangeTimestamp(Timestamp changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
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
            sb.append(changeTimestamp);
            sb.append(")");
            return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result
				+ ((changeTimestamp == null) ? 0 : changeTimestamp.hashCode());
		result = prime * result + defectResultId;
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocationName == null) ? 0
						: inspectionPartLocationName.hashCode());
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime
				* result
				+ ((secondaryPartName == null) ? 0 : secondaryPartName
						.hashCode());
		result = prime
				* result
				+ ((twoPartPairLocation == null) ? 0 : twoPartPairLocation
						.hashCode());
		result = prime * result
				+ ((twoPartPairPart == null) ? 0 : twoPartPairPart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefectResultHistoryId other = (DefectResultHistoryId) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (changeTimestamp == null) {
			if (other.changeTimestamp != null)
				return false;
		} else if (!changeTimestamp.equals(other.changeTimestamp))
			return false;
		if (defectResultId != other.defectResultId)
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName
				.equals(other.inspectionPartLocationName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (secondaryPartName == null) {
			if (other.secondaryPartName != null)
				return false;
		} else if (!secondaryPartName.equals(other.secondaryPartName))
			return false;
		if (twoPartPairLocation == null) {
			if (other.twoPartPairLocation != null)
				return false;
		} else if (!twoPartPairLocation.equals(other.twoPartPairLocation))
			return false;
		if (twoPartPairPart == null) {
			if (other.twoPartPairPart != null)
				return false;
		} else if (!twoPartPairPart.equals(other.twoPartPairPart))
			return false;
		return true;
	}


}
