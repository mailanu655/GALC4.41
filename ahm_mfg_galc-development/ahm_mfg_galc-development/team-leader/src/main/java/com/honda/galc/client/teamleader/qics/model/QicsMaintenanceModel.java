package com.honda.galc.client.teamleader.qics.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.entity.qics.SecondaryPart;


/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>QicsMaintenanceModel</code> is ...
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
public class QicsMaintenanceModel {

	private String siteName;
	private List<Division> departments;
	private List<String> models;

	private List<PartGroup> partGroups;
	private List<InspectionPart> parts;
	private List<InspectionPartLocation> partLocations;

	private Map<String,Image> images;
	private List<DefectGroup> defectGroups;
	private List<DefectType> defectTypes;
	private List<SecondaryPart> secondaryParts;

	private long partGroupsUpdateTime;
	private long defectGroupsUpdateTime;
	private long imagesUpdateTime;

	public QicsMaintenanceModel() {

		departments = new ArrayList<Division>();
		models = new ArrayList<String>();

		partGroups = new ArrayList<PartGroup>();
		parts = new ArrayList<InspectionPart>();
		partLocations = new ArrayList<InspectionPartLocation>();

		images = new LinkedHashMap<String,Image>();
		setDefectGroups(new ArrayList<DefectGroup>());
		defectTypes = new ArrayList<DefectType>();
		secondaryParts = new ArrayList<SecondaryPart>();
	}

	public List<DefectGroup> getDefectGroups() {
		return defectGroups;
	}
	
	public List<DefectGroup> getDefectGroups(List<String> modelCodes) {
		if(modelCodes == null || modelCodes.isEmpty()) return getDefectGroups();
		List<DefectGroup> groups = new ArrayList<DefectGroup>();
		for(DefectGroup defectGroup : getDefectGroups()) {
			if(modelCodes.contains(defectGroup.getModelCode()) || StringUtils.isEmpty(defectGroup.getModelCode()))
				groups.add(defectGroup);
		}
		
		return groups;
	}

	// === get/set === //
	public void setDefectGroups(List<DefectGroup> defectGroups) {
		setDefectGroupsUpdateTime();
		this.defectGroups = defectGroups;

	}

	public List<DefectType> getDefectTypes() {
		return defectTypes;
	}

	public void setDefectTypes(List<DefectType> defectTypes) {
		this.defectTypes = defectTypes;
	}

	public List<Image> getImages() {
		return new ArrayList<Image>(images.values());
	}
	
	public void setImages(List<Image> images) {
		setImagesUpdateTime();
		this.images = new LinkedHashMap<String, Image>();
		for(Image image : images) {
			this.images.put(image.getImageName(), image);
		}
	}
	
	public Image getImage(String imageName) {
		return images.get(imageName);
	}
	
	public void putImage(Image image) {
		images.put(image.getImageName(), image);
	}

	public List<PartGroup> getPartGroups() {
		return partGroups;
	}
	
	public List<PartGroup> getPartGroups(List<String> modelCodes) {
		if(modelCodes == null || modelCodes.isEmpty()) return getPartGroups();
		List<PartGroup> groups = new ArrayList<PartGroup>();
		for(PartGroup partGroup : getPartGroups()) {
			if(modelCodes.contains(partGroup.getModelCode()) || StringUtils.isEmpty(partGroup.getModelCode()))
				groups.add(partGroup);
		}
		
		return groups;
	}

	public void setPartGroups(List<PartGroup> partGroups) {
		setPartGroupsUpdateTime();
		this.partGroups = partGroups;
	}

	public List<InspectionPartLocation> getPartLocations() {
		return partLocations;
	}

	public void setPartLocations(List<InspectionPartLocation> partLocations) {
		this.partLocations = partLocations;
	}

	public List<InspectionPart> getParts() {
		return parts;
	}

	public void setParts(List<InspectionPart> parts) {
		this.parts = parts;
	}

	public List<SecondaryPart> getSecondaryParts() {
		return secondaryParts;
	}

	public void setSecondaryParts(List<SecondaryPart> secondaryParts) {
		this.secondaryParts = secondaryParts;
	}

	public List<Division> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Division> departments) {
		this.departments = departments;
	}

	public List<String> getModels() {
		return models;
	}

	public void setModels(List<String> models) {
		this.models = models;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	// === update time === //
	public long getDefectGroupsUpdateTime() {
		return defectGroupsUpdateTime;
	}

	private void setDefectGroupsUpdateTime() {
		this.defectGroupsUpdateTime = System.currentTimeMillis();
	}

	public boolean isDefectGroupsUpdated(long time) {
		return getDefectGroupsUpdateTime() > time;
	}

	public long getPartGroupsUpdateTime() {
		return partGroupsUpdateTime;
	}

	public void setPartGroupsUpdateTime() {
		this.partGroupsUpdateTime = System.currentTimeMillis();
	}

	public boolean isPartGroupsUpdated(long time) {
		return getPartGroupsUpdateTime() > time;
	}

	public boolean isImagesUpdated(long time) {
		return getImagesUpdateTime() > time;
	}

	public long getImagesUpdateTime() {
		return imagesUpdateTime;
	}

	public void setImagesUpdateTime() {
		this.imagesUpdateTime = System.currentTimeMillis();
	}
}
