package com.honda.galc.dto.qi;


import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiEntryScreenType;
import com.honda.galc.entity.enumtype.QiEntryModelVersioningStatus;
import com.honda.galc.qi.constant.QiConstant;

public class QiEntryScreenDto implements IDto, Comparable<QiEntryScreenDto>{
	
	
	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "ENTRY_SCREEN")
	private String entryScreen;
	
	@DtoTag(outputName = "ENTRY_SCREEN_DESCRIPTION")
	private String entryScreenDescription;
	
	@DtoTag(outputName = "IS_IMAGE")
	private short isImage;
	
	@DtoTag(outputName = "PRODUCT_TYPE")
	private String productType;
	
	@DtoTag(outputName = "ACTIVE")
	private short active;
	
	@DtoTag(outputName = "ENTRY_MODEL")
	private String entryModel;
	
	@DtoTag(outputName = "DIVISION_ID")
	private String divisionId;
	
	@DtoTag(outputName = "DIVISION_NAME")
	private String divisionName;
	
	@DtoTag(outputName = "PLANT_NAME")
	private String plantName;
	
	@DtoTag(outputName = "CREATE_USER")
	private String createUser;
	
	@DtoTag(outputName = "UPDATE_USER")
	private String updateUser;
	
	@DtoTag(outputName = "CREATE_TIMESTAMP")
    private Timestamp createTimestamp;

	@DtoTag(outputName = "UPDATE_TIMESTAMP")
    private Timestamp updateTimestamp;
	
	private String isImageValue;
	
	@DtoTag(outputName = "IMAGE_NAME")
	private String imageName;
	private String used;
	
	@DtoTag(outputName = "IS_USED_VERSION")
	private short isUsedVersion;
	
	@DtoTag(outputName = "SCREEN_IS_USED")
	private short screenIsUsed;
	
	@DtoTag(name = "SEQ")
	private short seq;
	
	@DtoTag(name = "ORIENTATION_ANGLE")
	private short orientationAngle;
	
	@DtoTag(name = "ALLOW_SCAN")
	private short allowScan;
	
	public void setSeq(short seq) {
		this.seq = seq;
	}

	public short getSeq() {
		return seq;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getEntryScreen() {
		return entryScreen;
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public String getEntryScreenDescription() {
		return entryScreenDescription;
	}

	public void setEntryScreenDescription(String entryScreenDescription) {
		this.entryScreenDescription = entryScreenDescription;
	}

	public short getIsImage() {
		return isImage;
	}

	public void setIsImage(short isImage) {
		this.isImage = isImage;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public short getActive() {
		return active;
	}

	public void setActive(short active) {
		this.active = active;
	}
	
	public boolean isActive() {
        return this.active ==(short) 1;
    }

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}
	
	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}

	public boolean isImage() {
		return this.isImage>=1;
	}

	public void setImage(boolean image) {
		this.isImage =(short)( image ? 1 : 0);
	}
	
	public String getScreenType() {
		return QiEntryScreenType.getType(isImage).getName();
	}

	
	public String getEntryModel() {
		return entryModel;
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}
	
	public String getImageValue() {
		return isImageValue;
	}

	public void setImageValue(String isImage) {
		this.isImageValue = isImage;
		if(!StringUtils.isBlank(isImageValue) && isImageValue.trim().equalsIgnoreCase("Image"))  {
			setImage(true);
		}
		else  {
			setImage(false);
		}
	}
	
	public short getIsUsedVersion() {
		return isUsedVersion;
	}

	public void setIsUsedVersion(short isUsedVersion) {
		this.isUsedVersion = isUsedVersion;
	}
	
	public String getIsUsedVersionData() {
		return QiEntryModelVersioningStatus.getType(getIsUsedVersion()).getName();
	}
	
	public short getScreenIsUsed() {
		return screenIsUsed;
	}

	public void setScreenIsUsed(short screenIsUsed) {
		this.screenIsUsed = screenIsUsed;
	}

	public short getOrientationAngle() {
		return orientationAngle;
	}
	
	public String getOrientationAngleAsString() {
		return getImageName().equals(QiConstant.TEXT) ? QiConstant.NA : String.valueOf(orientationAngle);
	}

	public void setOrientationAngle(short orientationAngle) {
		this.orientationAngle = orientationAngle;
	}
	
	public String getAllowScanAsString() {
		return allowScan==0?"No":"Yes";
	}

	public short getAllowScan() {
		return allowScan;
	}

	public void setAllowScan(short allowScan) {
		this.allowScan = allowScan;
	}

	public boolean isSameEntryScreen(QiEntryScreenDto otherDto)  {
		if(entryScreen == null || entryModel == null || otherDto == null)  return false;
		if(entryScreen.equalsIgnoreCase(otherDto.getEntryScreen()) && entryModel.equalsIgnoreCase(otherDto.getEntryModel())
				&& isUsedVersion == otherDto.isUsedVersion)  return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((createTimestamp == null) ? 0 : createTimestamp.hashCode());
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result + ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + ((entryScreenDescription == null) ? 0 : entryScreenDescription.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + isImage;
		result = prime * result + ((isImageValue == null) ? 0 : isImageValue.hashCode());
		result = prime * result + ((plantName == null) ? 0 : plantName.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((updateTimestamp == null) ? 0 : updateTimestamp.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
		result = prime * result + ((used == null) ? 0 : used.hashCode());
		result = prime * result + isUsedVersion;
		result = prime * result + screenIsUsed;
		result = prime * result + allowScan;
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
		QiEntryScreenDto other = (QiEntryScreenDto) obj;
		if (active != other.active)
			return false;
		if (createTimestamp == null) {
			if (other.createTimestamp != null)
				return false;
		} else if (!createTimestamp.equals(other.createTimestamp))
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (entryScreenDescription == null) {
			if (other.entryScreenDescription != null)
				return false;
		} else if (!entryScreenDescription.equals(other.entryScreenDescription))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (isImage != other.isImage)
			return false;
		if (isImageValue == null) {
			if (other.isImageValue != null)
				return false;
		} else if (!isImageValue.equals(other.isImageValue))
			return false;
		if (plantName == null) {
			if (other.plantName != null)
				return false;
		} else if (!plantName.equals(other.plantName))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		if (used == null) {
			if (other.used != null)
				return false;
		} else if (!used.equals(other.used))
			return false;
		if (isUsedVersion != other.isUsedVersion)
			return false;
		if (screenIsUsed != other.screenIsUsed)
			return false;
		if (allowScan != other.allowScan)
			return false;
		return true;
	}

	public int compareTo(QiEntryScreenDto obj) {
		return this.entryScreen.compareTo(obj.getEntryScreen());
	}

}
