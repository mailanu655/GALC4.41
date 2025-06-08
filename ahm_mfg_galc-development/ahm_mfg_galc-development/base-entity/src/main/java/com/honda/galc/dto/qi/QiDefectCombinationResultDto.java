package com.honda.galc.dto.qi;

import java.sql.Timestamp;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * 
 * <h3>QiDefectCombinationResultDto Class description</h3>
 * <p>QiDefectCombinationResultDto</p>
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
 *        May 14, 2017
 * 
 */
public class QiDefectCombinationResultDto implements IDto,Comparator<QiDefectCombinationResultDto> {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "COMBINATION")
	private String combination;

	@DtoTag(outputName = "MAIN_PART_NO")
	private String mainPartNo;
	
	@DtoTag(outputName ="REGIONAL_DEFECT_COMBINATION_ID")
	private Integer regionalDefectCombinationId;


	@DtoTag(outputName ="PART_LOCATION_ID")
	private Integer partLocationId;

	@DtoTag(outputName = "INSPECTION_PART_NAME")
	private String inspectionPartName;
	
	@DtoTag(outputName = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;
    
	@DtoTag(outputName = "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;
	
	@DtoTag(outputName = "INSPECTION_PART2_NAME")
	private String inspectionPart2Name;
	
	@DtoTag(outputName = "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;
	
	@DtoTag(outputName = "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;

	@DtoTag(outputName = "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;

	@DtoTag(outputName = "DEFECT_TYPE_NAME")
	private String defectTypeName;

	@DtoTag(outputName = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	

	@DtoTag(outputName = "OLD_INSPECTION_PART_NAME")
	private String oldInspectionPartName;
	
	@DtoTag(outputName = "OLD_INSPECTION_PART_LOCATION_NAME")
    private String oldInspectionPartLocationName;
    
	@DtoTag(outputName = "OLD_INSPECTION_PART_LOCATION2_NAME")
	private String oldInspectionPartLocation2Name;
	
	@DtoTag(outputName = "OLD_INSPECTION_PART2_NAME")
	private String oldInspectionPart2Name;
	
	@DtoTag(outputName = "OLD_INSPECTION_PART2_LOCATION_NAME")
	private String oldInspectionPart2LocationName;
	
	@DtoTag(outputName = "OLD_INSPECTION_PART2_LOCATION2_NAME")
	private String oldInspectionPart2Location2Name;

	@DtoTag(outputName = "OLD_INSPECTION_PART3_NAME")
	private String oldInspectionPart3Name;

	@DtoTag(outputName = "OLD_DEFECT_TYPE_NAME")
	private String oldDefectTypeName;

	@DtoTag(outputName = "OLD_DEFECT_TYPE_NAME2")
	private String oldDefectTypeName2;

	@DtoTag(outputName = "APPLICATION_ID")
	private String applicationId;
	
	@DtoTag(outputName = "COUNT")
	private String count;

	@DtoTag(outputName = "PRODUCT_KIND")
	private String productKind;
	
	@DtoTag(outputName = "IQS_ID")
	private String iqsId;
	
	@DtoTag(outputName = "THEME_NAME")
	private String themeName;
	
	@DtoTag(outputName = "REPORTABLE")
	private Short reportable;
	
	@DtoTag(outputName = "ACTIVE")
	private Short active;
	
	@DtoTag(outputName = "CREATE_USER")
	private String createUser;
	
	@DtoTag(outputName = "CREATE_TIME")
	private Timestamp createTime;

		
	public QiDefectCombinationResultDto() {
		super();
	}

	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCount() {
		return StringUtils.trimToEmpty(count);
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
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

	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(inspectionPartLocation2Name);
	}

	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}

	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Name);
	}

	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}

	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(inspectionPart2LocationName);
	}

	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}

	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Location2Name);
	}

	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}

	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(inspectionPart3Name);
	}

	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(defectTypeName2);
	}

	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public Short getReportable() {
		return reportable;
	}

	public void setReportable(Short reportable) {
		this.reportable = reportable;
	}

	public String getPartDefectDesc() {
		return StringUtils.trimToEmpty(inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name+" " +
				   defectTypeName+" " +
				   defectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	public String getNglcDefectCombination() {
		return StringUtils.trimToEmpty(oldInspectionPartName+" " +
				oldInspectionPartLocationName+" " +
				oldInspectionPartLocation2Name+" " +
				oldInspectionPart2Name+" " +
				oldInspectionPart2LocationName+" " +
				oldInspectionPart2Location2Name+" " +
				oldInspectionPart3Name+" " +
				oldDefectTypeName+" " +
				oldDefectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	public int compare(QiDefectCombinationResultDto object1, QiDefectCombinationResultDto object2) {
		return object1.getInspectionPartName().compareTo(object2.getInspectionPartName());
	}

	
	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getPartLocationId() {
		return partLocationId;
	}

	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}

	/**
	 * @return the combination
	 */
	public String getCombination() {
		return StringUtils.trimToEmpty(combination);
	}

	/**
	 * @param combination the combination to set
	 */
	public void setCombination(String combination) {
		this.combination = combination;
	}

	/**
	 * @return the mainPartNo
	 */
	public String getMainPartNo() {
		return StringUtils.trimToEmpty(mainPartNo);
	}

	/**
	 * @param mainPartNo the mainPartNo to set
	 */
	public void setMainPartNo(String mainPartNo) {
		this.mainPartNo = mainPartNo;
	}

	/**
	 * @return the oldInspectionPartName
	 */
	public String getOldInspectionPartName() {
		return StringUtils.trimToEmpty(oldInspectionPartName);
	}

	/**
	 * @param oldInspectionPartName the oldInspectionPartName to set
	 */
	public void setOldInspectionPartName(String oldInspectionPartName) {
		this.oldInspectionPartName = oldInspectionPartName;
	}

	/**
	 * @return the oldInspectionPartLocationName
	 */
	public String getOldInspectionPartLocationName() {
		return StringUtils.trimToEmpty(oldInspectionPartLocationName);
	}

	/**
	 * @param oldInspectionPartLocationName the oldInspectionPartLocationName to set
	 */
	public void setOldInspectionPartLocationName(String oldInspectionPartLocationName) {
		this.oldInspectionPartLocationName = oldInspectionPartLocationName;
	}

	/**
	 * @return the oldInspectionPartLocation2Name
	 */
	public String getOldInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(oldInspectionPartLocation2Name);
	}

	/**
	 * @param oldInspectionPartLocation2Name the oldInspectionPartLocation2Name to set
	 */
	public void setOldInspectionPartLocation2Name(String oldInspectionPartLocation2Name) {
		this.oldInspectionPartLocation2Name = oldInspectionPartLocation2Name;
	}

	/**
	 * @return the oldInspectionPart2Name
	 */
	public String getOldInspectionPart2Name() {
		return StringUtils.trimToEmpty(oldInspectionPart2Name);
	}

	/**
	 * @param oldInspectionPart2Name the oldInspectionPart2Name to set
	 */
	public void setOldInspectionPart2Name(String oldInspectionPart2Name) {
		this.oldInspectionPart2Name = oldInspectionPart2Name;
	}

	/**
	 * @return the oldInspectionPart2LocationName
	 */
	public String getOldInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(oldInspectionPart2LocationName);
	}

	/**
	 * @param oldInspectionPart2LocationName the oldInspectionPart2LocationName to set
	 */
	public void setOldInspectionPart2LocationName(String oldInspectionPart2LocationName) {
		this.oldInspectionPart2LocationName = oldInspectionPart2LocationName;
	}

	/**
	 * @return the oldInspectionPart2Location2Name
	 */
	public String getOldInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(oldInspectionPart2Location2Name);
	}

	/**
	 * @param oldInspectionPart2Location2Name the oldInspectionPart2Location2Name to set
	 */
	public void setOldInspectionPart2Location2Name(String oldInspectionPart2Location2Name) {
		this.oldInspectionPart2Location2Name = oldInspectionPart2Location2Name;
	}

	/**
	 * @return the oldInspectionPart3Name
	 */
	public String getOldInspectionPart3Name() {
		return StringUtils.trimToEmpty(oldInspectionPart3Name);
	}

	/**
	 * @param oldInspectionPart3Name the oldInspectionPart3Name to set
	 */
	public void setOldInspectionPart3Name(String oldInspectionPart3Name) {
		this.oldInspectionPart3Name = oldInspectionPart3Name;
	}

	/**
	 * @return the oldDefectTypeName
	 */
	public String getOldDefectTypeName() {
		return StringUtils.trimToEmpty(oldDefectTypeName);
	}

	/**
	 * @param oldDefectTypeName the oldDefectTypeName to set
	 */
	public void setOldDefectTypeName(String oldDefectTypeName) {
		this.oldDefectTypeName = oldDefectTypeName;
	}

	/**
	 * @return the oldDefectTypeName2
	 */
	public String getOldDefectTypeName2() {
		return StringUtils.trimToEmpty(oldDefectTypeName2);
	}

	/**
	 * @param oldDefectTypeName2 the oldDefectTypeName2 to set
	 */
	public void setOldDefectTypeName2(String oldDefectTypeName2) {
		this.oldDefectTypeName2 = oldDefectTypeName2;
	}

	/**
	 * @return the productKind
	 */
	public String getProductKind() {
		return StringUtils.trimToEmpty(productKind);
	}

	/**
	 * @param productKind the productKind to set
	 */
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	/**
	 * @return the iqsId
	 */
	public String getIqsId() {
		return StringUtils.trimToEmpty(iqsId);
	}

	/**
	 * @param iqsId the iqsId to set
	 */
	public void setIqsId(String iqsId) {
		this.iqsId = iqsId;
	}

	/**
	 * @return the active
	 */
	public Short getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Short active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((combination == null) ? 0 : combination.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((iqsId == null) ? 0 : iqsId.hashCode());
		result = prime * result + ((mainPartNo == null) ? 0 : mainPartNo.hashCode());
		result = prime * result + ((oldDefectTypeName == null) ? 0 : oldDefectTypeName.hashCode());
		result = prime * result + ((oldDefectTypeName2 == null) ? 0 : oldDefectTypeName2.hashCode());
		result = prime * result
				+ ((oldInspectionPart2Location2Name == null) ? 0 : oldInspectionPart2Location2Name.hashCode());
		result = prime * result
				+ ((oldInspectionPart2LocationName == null) ? 0 : oldInspectionPart2LocationName.hashCode());
		result = prime * result + ((oldInspectionPart2Name == null) ? 0 : oldInspectionPart2Name.hashCode());
		result = prime * result + ((oldInspectionPart3Name == null) ? 0 : oldInspectionPart3Name.hashCode());
		result = prime * result
				+ ((oldInspectionPartLocation2Name == null) ? 0 : oldInspectionPartLocation2Name.hashCode());
		result = prime * result
				+ ((oldInspectionPartLocationName == null) ? 0 : oldInspectionPartLocationName.hashCode());
		result = prime * result + ((oldInspectionPartName == null) ? 0 : oldInspectionPartName.hashCode());
		result = prime * result + partLocationId;
		result = prime * result + ((productKind == null) ? 0 : productKind.hashCode());
		result = prime * result + regionalDefectCombinationId;
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
		QiDefectCombinationResultDto other = (QiDefectCombinationResultDto) obj;
		if (active != other.active)
			return false;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (combination == null) {
			if (other.combination != null)
				return false;
		} else if (!combination.equals(other.combination))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (defectTypeName2 == null) {
			if (other.defectTypeName2 != null)
				return false;
		} else if (!defectTypeName2.equals(other.defectTypeName2))
			return false;
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null)
				return false;
		} else if (!inspectionPart2Location2Name.equals(other.inspectionPart2Location2Name))
			return false;
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null)
				return false;
		} else if (!inspectionPart2LocationName.equals(other.inspectionPart2LocationName))
			return false;
		if (inspectionPart2Name == null) {
			if (other.inspectionPart2Name != null)
				return false;
		} else if (!inspectionPart2Name.equals(other.inspectionPart2Name))
			return false;
		if (inspectionPart3Name == null) {
			if (other.inspectionPart3Name != null)
				return false;
		} else if (!inspectionPart3Name.equals(other.inspectionPart3Name))
			return false;
		if (inspectionPartLocation2Name == null) {
			if (other.inspectionPartLocation2Name != null)
				return false;
		} else if (!inspectionPartLocation2Name.equals(other.inspectionPartLocation2Name))
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
		if (iqsId == null) {
			if (other.iqsId != null)
				return false;
		} else if (!iqsId.equals(other.iqsId))
			return false;
		if (mainPartNo == null) {
			if (other.mainPartNo != null)
				return false;
		} else if (!mainPartNo.equals(other.mainPartNo))
			return false;
		if (oldDefectTypeName == null) {
			if (other.oldDefectTypeName != null)
				return false;
		} else if (!oldDefectTypeName.equals(other.oldDefectTypeName))
			return false;
		if (oldDefectTypeName2 == null) {
			if (other.oldDefectTypeName2 != null)
				return false;
		} else if (!oldDefectTypeName2.equals(other.oldDefectTypeName2))
			return false;
		if (oldInspectionPart2Location2Name == null) {
			if (other.oldInspectionPart2Location2Name != null)
				return false;
		} else if (!oldInspectionPart2Location2Name.equals(other.oldInspectionPart2Location2Name))
			return false;
		if (oldInspectionPart2LocationName == null) {
			if (other.oldInspectionPart2LocationName != null)
				return false;
		} else if (!oldInspectionPart2LocationName.equals(other.oldInspectionPart2LocationName))
			return false;
		if (oldInspectionPart2Name == null) {
			if (other.oldInspectionPart2Name != null)
				return false;
		} else if (!oldInspectionPart2Name.equals(other.oldInspectionPart2Name))
			return false;
		if (oldInspectionPart3Name == null) {
			if (other.oldInspectionPart3Name != null)
				return false;
		} else if (!oldInspectionPart3Name.equals(other.oldInspectionPart3Name))
			return false;
		if (oldInspectionPartLocation2Name == null) {
			if (other.oldInspectionPartLocation2Name != null)
				return false;
		} else if (!oldInspectionPartLocation2Name.equals(other.oldInspectionPartLocation2Name))
			return false;
		if (oldInspectionPartLocationName == null) {
			if (other.oldInspectionPartLocationName != null)
				return false;
		} else if (!oldInspectionPartLocationName.equals(other.oldInspectionPartLocationName))
			return false;
		if (oldInspectionPartName == null) {
			if (other.oldInspectionPartName != null)
				return false;
		} else if (!oldInspectionPartName.equals(other.oldInspectionPartName))
			return false;
		if (partLocationId != other.partLocationId)
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (regionalDefectCombinationId != other.regionalDefectCombinationId)
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

}
