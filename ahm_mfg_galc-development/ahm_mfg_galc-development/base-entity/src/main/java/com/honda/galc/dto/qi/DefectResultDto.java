package com.honda.galc.dto.qi;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * 
 * <h3>DefectResultDto Class description</h3>
 * <p>
 * DefectResultDto
 * </p>
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
 * @author LnTInfotech<br>
 *        Oct 03, 2016
 * 
 */

public class DefectResultDto implements IDto,Comparator<DefectResultDto> {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "APPLICATION_ID")
	private String applicationId;
	
	@DtoTag(outputName = "INSPECTION_PART_NAME")
	private String inspectionPartName;
	
	@DtoTag(outputName = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;
	
	@DtoTag(outputName = "TWO_PART_PAIR_PART")
	private String twoPartPairPart;
	
	@DtoTag(outputName = "TWO_PART_PAIR_LOCATION")
	private String twoPartPairLocation;

	@DtoTag(outputName = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@DtoTag(outputName = "SECONDARY_PART_NAME")
	private String secondaryPartName;	

	@DtoTag(outputName ="IMAGE_NAME")
	private String imageName;

	@DtoTag(outputName = "THEME_NAME")
	private String themeName;
	
	@DtoTag(outputName = "REPORTABLE")
	private short reportable;
	
	@DtoTag(outputName = "REPAIR_AREA_NAME")
	private String repairAreaName;
	
	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod;
	
	@DtoTag(outputName = "REPAIR_METHOD_TIME")
	private short repairMethodTime;
	
	public DefectResultDto() {
		super();
	}

	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public short getReportable() {
		return reportable;
	}

	public void setReportable(short reportable) {
		this.reportable = reportable;
	}

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public String getRepairMethod() {
		return StringUtils.trimToEmpty(repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public short getRepairMethodTime() {
		return repairMethodTime;
	}

	public void setRepairMethodTime(short repairMethodTime) {
		this.repairMethodTime = repairMethodTime;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTwoPartPairPart() {
		return twoPartPairPart;
	}

	public void setTwoPartPairPart(String twoPartPairPart) {
		this.twoPartPairPart = twoPartPairPart;
	}

	public String getTwoPartPairLocation() {
		return twoPartPairLocation;
	}

	public void setTwoPartPairLocation(String twoPartPairLocation) {
		this.twoPartPairLocation = twoPartPairLocation;
	}

	public String getSecondaryPartName() {
		return secondaryPartName;
	}

	public void setSecondaryPartName(String secondaryPartName) {
		this.secondaryPartName = secondaryPartName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((twoPartPairPart == null) ? 0 : twoPartPairPart.hashCode());
		result = prime * result + ((twoPartPairLocation == null) ? 0 : twoPartPairLocation.hashCode());
		result = prime * result + ((secondaryPartName == null) ? 0 : secondaryPartName.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result + ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + repairMethodTime;
		result = prime * result + reportable;
		result = prime * result + ((themeName == null) ? 0 : themeName.hashCode());
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
		DefectResultDto other = (DefectResultDto) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName.equals(other.inspectionPartLocationName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (twoPartPairPart == null) {
			if (other.twoPartPairPart != null)
				return false;
		} else if (!twoPartPairPart.equals(other.twoPartPairPart))
			return false;
		if (twoPartPairLocation == null) {
			if (other.twoPartPairLocation != null)
				return false;
		} else if (!twoPartPairLocation.equals(other.twoPartPairLocation))
			return false;
		if (secondaryPartName == null) {
			if (other.secondaryPartName != null)
				return false;
		} else if (!secondaryPartName.equals(other.secondaryPartName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (repairMethod == null) {
			if (other.repairMethod != null)
				return false;
		} else if (!repairMethod.equals(other.repairMethod))
			return false;
		if (repairMethodTime != other.repairMethodTime)
			return false;
		if (reportable != other.reportable)
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		return true;
	}
	
	public int compare(DefectResultDto object1, DefectResultDto object2) {
		return object1.getInspectionPartName().compareTo(object2.getInspectionPartName());
	}
}
