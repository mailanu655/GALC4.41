package com.honda.galc.dto.qi;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>QiDefectResultDto Class description</h3>
 * <p>
 * QiDefectResultDto
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
public class QiDefectEntryDto implements IDto{

	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName ="ENTRY_SCREEN")
	private String entryScreen;
	
	@DtoTag(outputName ="IMAGE_NAME")
	private String imageName;
	
	@DtoTag(outputName ="IMAGE_DATA")
	private byte[] imageData;
	
	@DtoTag(outputName ="IS_IMAGE")
	private short isImage;
	
	@DtoTag(name = "ORIENTATION_ANGLE")
	private short orientationAngle;

	public QiDefectEntryDto() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + isImage;
		result = prime * result + orientationAngle;
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
		QiDefectEntryDto other = (QiDefectEntryDto) obj;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (!Arrays.equals(imageData, other.imageData))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (isImage != other.isImage)
			return false;
		if (orientationAngle != other.orientationAngle)
			return false;
		return true;
	}

	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public short getIsImage() {
		return isImage;
	}

	public void setIsImage(short isImage) {
		this.isImage = isImage;
	}
	
	public boolean isImage() {
		return this.isImage == 1;
	}

	public short getOrientationAngle() {
		return orientationAngle;
	}

	public void setOrientationAngle(short orientationAngle) {
		this.orientationAngle = orientationAngle;
	}
	
}
