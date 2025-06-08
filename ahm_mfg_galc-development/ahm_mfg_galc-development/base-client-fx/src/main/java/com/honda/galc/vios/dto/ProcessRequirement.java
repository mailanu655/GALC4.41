package com.honda.galc.vios.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class ProcessRequirement {
	
	private SimpleStringProperty ppeRequired = null;
	
	private SimpleStringProperty potentialHazard = null;
	
	private SimpleStringProperty howToUse = null;
	
	private ImageView ppeImages = null;

	public ProcessRequirement() {
		ppeRequired = new SimpleStringProperty();
		potentialHazard = new SimpleStringProperty();
		howToUse = new SimpleStringProperty();
		setPpeImages(new ImageView());
	}
	
	public void setPpeRequired(String ppeRequired) {
		this.ppeRequired.set(ppeRequired);
	}

	public String getPpeRequired() {
		return ppeRequired.get();
	}

	public void setPotentialHazard(String potentialHazard) {
		this.potentialHazard.set(potentialHazard);
	}

	public String getPotentialHazard() {
		return potentialHazard.get();
	}

	public void setHowToUse(String howToUse) {
		this.howToUse.set(howToUse);
	}

	public String getHowToUse() {
		return howToUse.get();
	}

	public void setPpeImages(ImageView ppeImages) {
		this.ppeImages = ppeImages;
	}

	public ImageView getPpeImages() {
		return ppeImages;
	}

}
