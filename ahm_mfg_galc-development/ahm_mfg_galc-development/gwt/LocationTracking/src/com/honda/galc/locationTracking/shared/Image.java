package com.honda.galc.locationTracking.shared;

import java.util.ArrayList;
import java.util.List;

public class Image {

	private String bitmapFileName;

    private String imageName;

    private String imageDescriptionShort;

    private String imageDescriptionLong;

    private byte[] imageData;
   
    private List<ImageSection> sections = new ArrayList<ImageSection>();

	public String getBitmapFileName() {
		return bitmapFileName;
	}

	public void setBitmapFileName(String bitmapFileName) {
		this.bitmapFileName = bitmapFileName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageDescriptionShort() {
		return imageDescriptionShort;
	}

	public void setImageDescriptionShort(String imageDescriptionShort) {
		this.imageDescriptionShort = imageDescriptionShort;
	}

	public String getImageDescriptionLong() {
		return imageDescriptionLong;
	}

	public void setImageDescriptionLong(String imageDescriptionLong) {
		this.imageDescriptionLong = imageDescriptionLong;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public List<ImageSection> getSections() {
		return sections;
	}

	public void setSections(List<ImageSection> sections) {
		this.sections = sections;
	}
    
}
