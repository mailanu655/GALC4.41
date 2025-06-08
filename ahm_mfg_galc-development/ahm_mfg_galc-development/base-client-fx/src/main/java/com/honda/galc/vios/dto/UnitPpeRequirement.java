package com.honda.galc.vios.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class UnitPpeRequirement {
	
	private SimpleStringProperty unitNo = null;
	
	private SimpleStringProperty unitOfOperationName = null;
	
	private SimpleStringProperty ppeRequired = null;
	
	private SimpleStringProperty potentialHazard = null;
	
	private SimpleStringProperty howToUse = null;
	
	private ImageView ppeImages = null;
	
	public UnitPpeRequirement() {
		unitNo = new SimpleStringProperty();
		unitOfOperationName = new SimpleStringProperty();
		ppeRequired = new SimpleStringProperty();
		potentialHazard = new SimpleStringProperty();
		howToUse = new SimpleStringProperty();
		ppeImages = new ImageView();
	}

	public String getUnitNo() {
		return unitNo.get();
	}

	public void setUnitNo(String unitNo) {
		this.unitNo.set(unitNo);
	}

	public String getUnitOfOperationName() {
		return unitOfOperationName.get();
	}

	public void setUnitOfOperationName(String unitOfOperationName) {
		this.unitOfOperationName.set(unitOfOperationName);
	}
	
	public String getPpeRequired() {
		return ppeRequired.get();
	}

	public void setPpeRequired(String ppeRequired) {
		this.ppeRequired.set(ppeRequired);
	}

	public String getPotentialHazard() {
		return potentialHazard.get();
	}

	public void setPotentialHazard(String potentialHazard) {
		this.potentialHazard.set(potentialHazard);
	}

	public String getHowToUse() {
		return howToUse.get();
	}

	public void setHowToUse(String howToUse) {
		this.howToUse.set(howToUse);
	}

	public ImageView getPpeImages() {
		return ppeImages;
	}

	public void setPpeImages(ImageView ppeImages) {
		this.ppeImages = ppeImages;
	}

}
