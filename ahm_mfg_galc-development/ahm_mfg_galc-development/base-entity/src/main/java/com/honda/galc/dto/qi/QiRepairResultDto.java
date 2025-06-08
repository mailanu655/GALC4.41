package com.honda.galc.dto.qi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiRepairResultImage;

/**
 * 
 * <h3>QiRepairResultDto Class description</h3>
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
 *         Nov 16, 2016
 * 
 */
public class QiRepairResultDto implements IDto {

	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "APPLICATION_ID")
	private String applicationId;

	@DtoTag(outputName = "REPAIR_ID")
	private long repairId;

	@DtoTag(outputName = "DEFECTRESULTID")
	private long defectresultid;

	@DtoTag(outputName = "ACTUAL_PROBLEM_SEQ")
	private short actualProblemSeq;

	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;

	@DtoTag(outputName = "POINT_X")
	private int pointX;

	@DtoTag(outputName = "POINT_Y")
	private int pointY;

	@DtoTag(outputName = "IMAGE_NAME")
	private String imageName;
	
	@DtoTag(outputName = "ENTRY_PLANT")
	private String entryPlant;

	@DtoTag(outputName = "ENTRY_DEPT")
	private String entryDept;

	@DtoTag(outputName = "DIVISION_NAME")
	private String divisionName;

	@DtoTag(outputName = "ORIGINAL_DEFECT_STATUS")
	private short originalDefectStatus;

	@DtoTag(outputName = "CURRENT_DEFECT_STATUS")
	private short currentDefectStatus;

	@DtoTag(outputName = "ENTRY_SCREEN")
	private String entryScreen;

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
	
	@DtoTag(outputName = "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;
	
	@DtoTag(outputName = "REPAIR_TIMESTAMP")
	private Date repairTimestamp;
	
	@DtoTag(outputName = "IS_REPAIR_RELATED")
	private short isRepairRelated;
	
	@DtoTag(outputName = "IS_COMPLETELY_FIXED")
	private short isCompletelyFixed;

	private List<QiRepairResultDto> childRepairResultList;
	
	@DtoTag(outputName ="IMAGE_DATA")
	private byte[] imageData;
	
	@DtoTag(outputName ="WRITE_UP_DEPARTMENT")
	private String writeUpDepartment;
	
	@DtoTag(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	@DtoTag(name = "RESPONSIBLE_SITE")
	private String responsibleSite;
	
	@DtoTag(name = "RESPONSIBLE_PLANT")
	private String responsiblePlant;
	
	@DtoTag(name = "RESPONSIBLE_DEPT")
	private String responsibleDept;

	@DtoTag(name = "RESPONSIBLE_LEVEL1")
	private String responsibleLevel1;

	@DtoTag(name = "RESPONSIBLE_LEVEL2")
	private String responsibleLevel2;

	@DtoTag(name = "RESPONSIBLE_LEVEL3")
	private String responsibleLevel3;

	@DtoTag(outputName ="REPAIR_AREA")
	private String repairArea;
	
	@DtoTag(name ="DEFECT_TRANSACTION_GROUP_ID")
	private long defectTransactionGroupId;
	
	@DtoTag(name = "KICKOUT_ID")
	private long kickoutId;
	
	@DtoTag(name = "KICKOUT_STATUS")
	private int kickoutStatus;
	
	@DtoTag(name = "DIVISION_ID")
	private String kickoutDivisionId;
	
	@DtoTag(name = "LINE_ID")
	private String kickoutLineId;
	
	@DtoTag(name = "PROCESS_POINT_ID")
	private String kickoutProcessPointId;
	
	@DtoTag(name = "PROCESS_POINT_NAME")
	private String kickoutProcessPointName;
	
	@DtoTag(name ="CREATE_USER")
	private String createUser;

	@DtoTag(name ="UPDATE_USER")
    private String updateUser;
	
	private String partDefectDesc;	
	
	private List<QiDefectResultImage> defectResultImages = new ArrayList<QiDefectResultImage>();

	private List<QiRepairResultImage> repairResultImages = new ArrayList<QiRepairResultImage>();
	
	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
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

	public String getDefectDesc() {
		return StringUtils.trimToEmpty(inspectionPartName + " " +
					inspectionPartLocationName + " " +
					inspectionPartLocation2Name + " " +
					inspectionPart2Name + " " +
					inspectionPart2LocationName + " " +
					inspectionPart2Location2Name + " " +
					inspectionPart3Name + " " +
					defectTypeName + " " +
					defectTypeName2).replaceAll("null", " ").replaceAll("\\s+", " ");
	}

	public long getRepairId() {
		return repairId;
	}

	public void setRepairId(long repairId) {
		this.repairId = repairId;
	}

	public long getDefectResultId() {
		return defectresultid;
	}

	public void setDefectResultId(long defectresultid) {
		this.defectresultid = defectresultid;
	}

	public short getActualProblemSeq() {
		return actualProblemSeq;
	}

	public void setActualProblemSeq(short actualProblemSeq) {
		this.actualProblemSeq = actualProblemSeq;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getPointX() {
		return pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getEntryDept() {
		return StringUtils.trimToEmpty(entryDept);
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public short getOriginalDefectStatus() {
		return originalDefectStatus;
	}

	public void setOriginalDefectStatus(short originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}

	public short getCurrentDefectStatus() {
		return currentDefectStatus;
	}

	public void setCurrentDefectStatus(short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}

	public List<QiRepairResultDto> getChildRepairResultList() {
		return childRepairResultList;
	}

	public void setChildRepairResultList(List<QiRepairResultDto> childRepairResultList) {
		this.childRepairResultList = childRepairResultList;
	}
	
	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(defectCategoryName);
	}

	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}
	
	public Date getRepairTimestamp() {
		return repairTimestamp;
	}

	public void setRepairTimestamp(Date repairTimestamp) {
		this.repairTimestamp = repairTimestamp;
	}

	public short getIsRepairRelated() {
		return isRepairRelated;
	}

	public void setIsRepairRelated(short isRepairRelated) {
		this.isRepairRelated = isRepairRelated;
	}

	public short getIsCompletelyFixed() {
		return isCompletelyFixed;
	}

	public void setIsCompletelyFixed(short isCompletelyFixed) {
		this.isCompletelyFixed = isCompletelyFixed;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getWriteUpDept() {
		return StringUtils.trimToEmpty(writeUpDepartment);
	}

	public void setWriteUpDept(String writeUpDept) {
		this.writeUpDepartment = writeUpDept;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getResponsibleDept() {
		return StringUtils.trimToEmpty(responsibleDept);
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}
	
	public String getRepairArea() {
		return StringUtils.trimToEmpty(repairArea);
	}

	public void setRepairArea(String repairArea) {
		this.repairArea = repairArea;
	}
	
	public String getPartDefectDesc() {
		return partDefectDesc;
	}

	public void setPartDefectDesc(String partDefectDesc) {
		this.partDefectDesc = partDefectDesc;
	}
	
	public String getEntryPlant() {
		return entryPlant;
	}

	public void setEntryPlant(String entryPlant) {
		this.entryPlant = entryPlant;
	}

	public String getWriteUpDepartment() {
		return writeUpDepartment;
	}

	public void setWriteUpDepartment(String writeUpDepartment) {
		this.writeUpDepartment = writeUpDepartment;
	}

	public String getResponsibleSite() {
		return responsibleSite;
	}

	public void setResponsibleSite(String responsibleSite) {
		this.responsibleSite = responsibleSite;
	}

	public String getResponsiblePlant() {
		return responsiblePlant;
	}

	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}

	/**
	 * @return the responsibleLevel1
	 */
	public String getResponsibleLevel1() {
		return responsibleLevel1;
	}

	/**
	 * @param responsibleLevel1 the responsibleLevel1 to set
	 */
	public void setResponsibleLevel1(String responsibleLevel1) {
		this.responsibleLevel1 = responsibleLevel1;
	}

	/**
	 * @return the responsibleLevel2
	 */
	public String getResponsibleLevel2() {
		return responsibleLevel2;
	}

	/**
	 * @param responsibleLevel2 the responsibleLevel2 to set
	 */
	public void setResponsibleLevel2(String responsibleLevel2) {
		this.responsibleLevel2 = responsibleLevel2;
	}

	/**
	 * @return the responsibleLevel3
	 */
	public String getResponsibleLevel3() {
		return responsibleLevel3;
	}

	/**
	 * @param responsibleLevel3 the responsibleLevel3 to set
	 */
	public void setResponsibleLevel3(String responsibleLevel3) {
		this.responsibleLevel3 = responsibleLevel3;
	}

	public long getDefectTransactionGroupId() {
		return this.defectTransactionGroupId;
	}

	public void setDefectTransactionGroupId(long defectTransactionGroupId) {
		this.defectTransactionGroupId = defectTransactionGroupId;
	}
	
	public long getKickoutId() {
		return this.kickoutId;
	}
	
	public void setKickoutId(long kickoutId) {
		this.kickoutId = kickoutId;
	}
	
	public int getKickoutStatus() {
		return kickoutStatus;
	}

	public void setKickoutStatus(int kickoutStatus) {
		this.kickoutStatus = kickoutStatus;
	}

	public String getKickoutDivisionId() {
		return StringUtils.trim(this.kickoutDivisionId);
	}

	public void setKickoutDivisionId(String kickoutDivisionId) {
		this.kickoutDivisionId = kickoutDivisionId;
	}

	public String getKickoutLineId() {
		return StringUtils.trim(this.kickoutLineId);
	}

	public void setKickoutLineId(String kickoutLineId) {
		this.kickoutLineId = kickoutLineId;
	}

	public String getKickoutProcessPointId() {
		return StringUtils.trim(this.kickoutProcessPointId);
	}

	public void setKickoutProcessPointId(String kickoutProcessPointId) {
		this.kickoutProcessPointId = kickoutProcessPointId;
	}
	
	public String getKickoutProcessPointName() {
		return this.kickoutProcessPointName;
	}

	public void setKickoutProcessPointName(String kickoutProcessPointName) {
		this.kickoutProcessPointName = kickoutProcessPointName;
	}

	public List<QiDefectResultImage> getDefectResultImages() {
		return defectResultImages;
	}

	public void setDefectResultImages(List<QiDefectResultImage> defectResultImages) {
		this.defectResultImages = defectResultImages;
	}

	public List<QiRepairResultImage> getRepairResultImages() {
		return repairResultImages;
	}
	
	public void setRepairResultImages(List<QiRepairResultImage> repairResultImages) {
		this.repairResultImages = repairResultImages;
	}
	
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}	

	public String getUpdateUser() {
		return StringUtils.trimToEmpty(updateUser);
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
    }

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + actualProblemSeq;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + currentDefectStatus;
		result = prime * result + ((defectCategoryName == null) ? 0 : defectCategoryName.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + (int)defectresultid;
		result = prime * result + (int)defectTransactionGroupId;
		result = prime * result + ((entryDept == null) ? 0 : entryDept.hashCode());
		result = prime * result + ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + originalDefectStatus;
		result = prime * result + pointX;
		result = prime * result + pointY;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((repairArea == null) ? 0 : repairArea.hashCode());
		result = prime * result + (int)repairId;
		result = prime * result + ((entryPlant == null) ? 0 : entryPlant.hashCode());
		result = prime * result + ((responsibleSite == null) ? 0 : responsibleSite.hashCode());
		result = prime * result + ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result + ((writeUpDepartment == null) ? 0 : writeUpDepartment.hashCode());
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
		QiRepairResultDto other = (QiRepairResultDto) obj;
		if (actualProblemSeq != other.actualProblemSeq)
			return false;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (currentDefectStatus != other.currentDefectStatus)
			return false;
		if (defectCategoryName == null) {
			if (other.defectCategoryName != null)
				return false;
		} else if (!defectCategoryName.equals(other.defectCategoryName))
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
		if (defectresultid != other.defectresultid)
			return false;
		if (entryDept == null) {
			if (other.entryDept != null)
				return false;
		} else if (!entryDept.equals(other.entryDept))
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (updateUser == null) {
		if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
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
		if (originalDefectStatus != other.originalDefectStatus)
			return false;
		if (pointX != other.pointX)
			return false;
		if (pointY != other.pointY)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (repairArea == null) {
			if (other.repairArea != null)
				return false;
		} else if (!repairArea.equals(other.repairArea))
			return false;
		if (repairId != other.repairId)
			return false;
		if (writeUpDepartment == null) {
			if (other.writeUpDepartment != null)
				return false;
		} else if (!writeUpDepartment.equals(other.writeUpDepartment))
			return false;
		if (entryPlant == null) {
			if (other.entryPlant != null)
				return false;
		} else if (!entryPlant.equals(other.entryPlant))
			return false;
		if (responsibleSite == null) {
			if (other.responsibleSite != null)
				return false;
		} else if (!responsibleSite.equals(other.responsibleSite))
			return false;
		if (responsiblePlant == null) {
			if (other.responsiblePlant != null)
				return false;
		} else if (!responsiblePlant.equals(other.responsiblePlant))
			return false;
		if (defectTransactionGroupId != other.defectTransactionGroupId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QiRepairResultDto [applicationId=");
		builder.append(applicationId);
		builder.append(", repairId=");
		builder.append(repairId);
		builder.append(", defectresultid=");
		builder.append(defectresultid);
		builder.append(", actualProblemSeq=");
		builder.append(actualProblemSeq);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", pointX=");
		builder.append(pointX);
		builder.append(", pointY=");
		builder.append(pointY);
		builder.append(", imageName=");
		builder.append(imageName);
		builder.append(", imageDate=");
		builder.append(imageData);
		builder.append(", createUser=");
		builder.append(createUser);
		builder.append(", updateUser=");
	    builder.append(updateUser);
		builder.append(", writeUpDept=");
		builder.append(writeUpDepartment);
		builder.append(", entryDept=");
		builder.append(entryDept);
		builder.append(", originalDefectStatus=");
		builder.append(originalDefectStatus);
		builder.append(", currentDefectStatus=");
		builder.append(currentDefectStatus);
		builder.append(", entryScreen=");
		builder.append(entryScreen);
		builder.append(", inspectionPartName=");
		builder.append(inspectionPartName);
		builder.append(", inspectionPartLocationName=");
		builder.append(inspectionPartLocationName);
		builder.append(", inspectionPartLocation2Name=");
		builder.append(inspectionPartLocation2Name);
		builder.append(", inspectionPart2Name=");
		builder.append(inspectionPart2Name);
		builder.append(", inspectionPart2LocationName=");
		builder.append(inspectionPart2LocationName);
		builder.append(", inspectionPart2Location2Name=");
		builder.append(inspectionPart2Location2Name);
		builder.append(", inspectionPart3Name=");
		builder.append(inspectionPart3Name);
		builder.append(", defectTypeName=");
		builder.append(defectTypeName);
		builder.append(", defectTypeName2=");
		builder.append(defectTypeName2);
		builder.append(", defectCategoryName=");
		builder.append(defectCategoryName);
		builder.append(", childRepairResultList=");
		builder.append(childRepairResultList);
		builder.append(", entryPlant=");
		builder.append(entryPlant);
		builder.append(", responsibleSite=");
		builder.append(responsibleSite);
		builder.append(", responsiblePlant=");
		builder.append(responsiblePlant);
		builder.append(defectTransactionGroupId);
		builder.append(" , kickoutId=");
		builder.append(kickoutId);
		builder.append("]");
		return builder.toString();
	}
	

	public QiRepairResultDto() {
		super();
	}

	public QiRepairResultDto(QiDefectResult defectResult, long cachedRepairId) {
		this.repairId = cachedRepairId;
		this.applicationId = defectResult.getApplicationId();
		this.defectresultid = defectResult.getDefectResultId();
		this.productId = defectResult.getProductId();
		this.pointX = defectResult.getPointX();
		this.pointY = defectResult.getPointY();
		this.imageName = defectResult.getImageName();
		this.createUser = defectResult.getCreateUser();
		this.updateUser = defectResult.getUpdateUser();
		this.entryDept = defectResult.getEntryDept();
		this.originalDefectStatus = defectResult.getOriginalDefectStatus();
		this.currentDefectStatus = defectResult.getCurrentDefectStatus();
		this.inspectionPartName = defectResult.getInspectionPartName();
		this.inspectionPartLocationName = defectResult.getInspectionPartLocationName();
		this.inspectionPartLocation2Name = defectResult.getInspectionPartLocation2Name();
		this.inspectionPart2Name = defectResult.getInspectionPart2Name();
		this.inspectionPart2LocationName = defectResult.getInspectionPart2LocationName();
		this.inspectionPart2Location2Name = defectResult.getInspectionPart2Location2Name();
		this.inspectionPart3Name = defectResult.getInspectionPart3Name();
		this.defectTypeName = defectResult.getDefectTypeName();
		this.defectTypeName2 =  defectResult.getDefectTypeName2();
		this.defectCategoryName =  defectResult.getDefectCategoryName();
		this.writeUpDepartment = defectResult.getWriteUpDept();
		this.responsibleDept = defectResult.getResponsibleDept();
		this.partDefectDesc = defectResult.getPartDefectDesc();
		this.entryPlant = defectResult.getEntryPlantName();
		this.responsibleSite = defectResult.getResponsibleSite();
		this.responsiblePlant = defectResult.getResponsiblePlant();
		this.responsibleLevel1 = defectResult.getResponsibleLevel1();
		this.responsibleLevel2 = defectResult.getResponsibleLevel2();
		this.responsibleLevel3 = defectResult.getResponsibleLevel3();
		this.defectTransactionGroupId = defectResult.getDefectTransactionGroupId();
		this.isRepairRelated = defectResult.getIsRepairRelated();
		this.kickoutId = defectResult.getKickoutId();
		this.kickoutProcessPointId = defectResult.getKickoutProcessPointId();
		this.kickoutProcessPointName = defectResult.getKickoutProcessPointName();
		this.defectResultImages = defectResult.getDefectResultImages();
	}
}
