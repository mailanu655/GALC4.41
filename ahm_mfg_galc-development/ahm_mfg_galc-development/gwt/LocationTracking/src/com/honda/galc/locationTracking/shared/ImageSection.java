package com.honda.galc.locationTracking.shared;

import java.util.ArrayList;
import java.util.List;

public class ImageSection {
	
    private int imageSectionId;

    private String imageName;

    private short partKindFlag;
    
	private int descriptionId;

    private int overlayNo;

    private List<ImageSectionPoint> imageSectionPoints = new ArrayList<ImageSectionPoint>();

    public int getImageSectionId() {
		return imageSectionId;
	}

	public void setImageSectionId(int imageSectionId) {
		this.imageSectionId = imageSectionId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public short getPartKindFlag() {
		return partKindFlag;
	}

	public void setPartKindFlag(short partKindFlag) {
		this.partKindFlag = partKindFlag;
	}

	public int getDescriptionId() {
		return descriptionId;
	}

	public void setDescriptionId(int descriptionId) {
		this.descriptionId = descriptionId;
	}

	public int getOverlayNo() {
		return overlayNo;
	}

	public void setOverlayNo(int overlayNo) {
		this.overlayNo = overlayNo;
	}

	public List<ImageSectionPoint> getImageSectionPoints() {
		return imageSectionPoints;
	}

	public void setImageSectionPoints(List<ImageSectionPoint> imageSectionPoints) {
		this.imageSectionPoints = imageSectionPoints;
	}


}
