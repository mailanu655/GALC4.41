package com.honda.galc.client.teamleader.qics.image;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qics.model.PanelModel;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.entity.qics.InspectionPartDescription;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ImageModel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 15, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ImageModel extends PanelModel {

	private Image formImage;
	private List<ImageSection> imageSections;
	private List<ImageSection> createdImageSections;
	private List<ImageSection> updatedImageSections;
	private List<ImageSection> deletedImageSections;

	private List<InspectionPartDescription> InspectionPartDescriptions;

	public ImageModel() {
		reset();
	}

	public void reset() {
		setImageSections(new ArrayList<ImageSection>());
		setCreatedImageSections(new ArrayList<ImageSection>());
		setUpdatedImageSections(new ArrayList<ImageSection>());
		setDeletedImageSections(new ArrayList<ImageSection>());
	}

	public void removeImageSection(int imageSectionId) {
		ImageSection existingImageSection = findImageSection(imageSectionId, getImageSections());
		if (existingImageSection != null) {
			getImageSections().remove(existingImageSection);
			getDeletedImageSections().add(existingImageSection);
		}
		ImageSection createdImageSection = findImageSection(imageSectionId, getCreatedImageSections());
		if (createdImageSection != null) {
			getCreatedImageSections().remove(createdImageSection);
		}

		ImageSection updatedImageSection = findImageSection(imageSectionId, getUpdatedImageSections());
		if (updatedImageSection != null) {
			getUpdatedImageSections().remove(updatedImageSection);
		}
	}

	public void updateImageSection(int imageSectionId, int descriptionId) {
		ImageSection existingImageSection = findImageSection(imageSectionId, getImageSections());
		if (existingImageSection != null) {
			existingImageSection.setDescriptionId(descriptionId);
			if (!getUpdatedImageSections().contains(existingImageSection)) {
				getUpdatedImageSections().add(existingImageSection);
			}
		}
		ImageSection createdImageSection = findImageSection(imageSectionId, getCreatedImageSections());
		if (createdImageSection != null) {
			createdImageSection.setDescriptionId(descriptionId);
		}
	}

	public boolean isModelUpdated() {
		if (getCreatedImageSections() != null && getCreatedImageSections().size() > 0) {
			return true;
		}
		if (getUpdatedImageSections() != null && getUpdatedImageSections().size() > 0) {
			return true;
		}
		if (getDeletedImageSections() != null && getDeletedImageSections().size() > 0) {
			return true;
		}
		return false;
	}

	protected ImageSection findImageSection(int imageSectionId, List<ImageSection> list) {
		ImageSection imageSection = null;
		if (list == null || list.isEmpty()) {
			return imageSection;
		}
		for (ImageSection item : list) {
			if (item == null) {
				continue;
			}

			int id = item.getImageSectionId();
			if (imageSectionId == id) {
				return item;
			}
		}
		return imageSection;
	}

	public List<ImageSection> getImageSections() {
		return imageSections;
	}

	public void setImageSections(List<ImageSection> imageSections) {
		this.imageSections = imageSections;
	}

	public List<ImageSection> getCreatedImageSections() {
		return createdImageSections;
	}

	public void setCreatedImageSections(List<ImageSection> createdImageSections) {
		this.createdImageSections = createdImageSections;
	}

	public List<ImageSection> getDeletedImageSections() {
		return deletedImageSections;
	}

	public void setDeletedImageSections(List<ImageSection> deletedImageSections) {
		this.deletedImageSections = deletedImageSections;
	}

	public List<ImageSection> getUpdatedImageSections() {
		return updatedImageSections;
	}

	public void setUpdatedImageSections(List<ImageSection> updatedImageSections) {
		this.updatedImageSections = updatedImageSections;
	}

	public List<InspectionPartDescription> getInspectionPartDescriptions() {
		return InspectionPartDescriptions;
	}

	public void setInspectionPartDescriptions(List<InspectionPartDescription> inspectionPartDescriptions) {
		InspectionPartDescriptions = inspectionPartDescriptions;
	}

	public Image getFormImage() {
		return formImage;
	}

	public void setFormImage(Image formImage) {
		this.formImage = formImage;
	}
}
