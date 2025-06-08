package com.honda.galc.client.qics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.*;
import com.honda.galc.entity.qics.*;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ProductModel</code> is ...
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
 * <TD>Apr 5, 2008</TD>
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
public class ProductModel {

	private String inputNumber;
	private BaseProduct product;
	private BaseProduct ownerProduct;
	
	private boolean productProcessable;
	
	private boolean productScrapped = false;

	private List<DefectResult> defects = new ArrayList<DefectResult>();

	private List<DefectResult> existingDefects = new ArrayList<DefectResult>();
	private List<DefectResult> updatedDefects = new ArrayList<DefectResult>();
	private List<DefectResult> newDefects = new ArrayList<DefectResult>();

	private List<InspectionModel> inspectionModels;

	private List<ImageSection> imageSections;
	private List<DefectTypeDescription> defectTypes;

	private List<InspectionPartDescription> parts;
	private Map<String, List<DefectDescription>> defectDescriptions;

	private Map<String, Object> productPreCheckResults;
	private Map<String, Object> productWarnCheckResults;
	private Map<String, Object> productItemCheckResults;
	private List<ProductHistory> productHistoryList;
	private List<IPPTag> ippHistory;
	
	 public boolean isDuplicatedPictureDefect(DefectResult newData) {
			if (getDefects() != null) {
				for (DefectResult data : getDefects()) {
						if( equals(data.getId().getInspectionPartName(), newData.getId().getInspectionPartName())
							&& equals(data.getId().getInspectionPartLocationName(), newData.getId().getInspectionPartLocationName())
							&& equals(data.getId().getDefectTypeName(), newData.getId().getDefectTypeName())
							&& equals(data.getId().getDefectTypeName(), newData.getId().getDefectTypeName())
							&& equals(data.getId().getSecondaryPartName(), newData.getId().getSecondaryPartName()) 
							&& data.getDefectStatus() == newData.getDefectStatus()
							&& 0 != newData.getPointX() && 0 != newData.getPointY())
							return true;
				}
			}
			return false;
	    }
	 
	 public boolean isDuplicatedTextDefect(DefectResult newData) {
		 if (getDefects() != null) {
				for (DefectResult data : getDefects()) {
					if( equals(data.getId().getInspectionPartName(), newData.getId().getInspectionPartName())
						&& equals(data.getId().getInspectionPartLocationName(), newData.getId().getInspectionPartLocationName())
						&& equals(data.getId().getDefectTypeName(), newData.getId().getDefectTypeName())
						&& equals(data.getId().getDefectTypeName(), newData.getId().getDefectTypeName())
						&& equals(data.getId().getSecondaryPartName(), newData.getId().getSecondaryPartName()) 
						&& data.getDefectStatus() == newData.getDefectStatus())						
						return true;
				}
			}
			return false;
	    }

	
	

	protected boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	public BaseProduct getProduct() {
		return product;
	}
	
	public String getProductId() {
		return product == null ? null : product.getProductId();
	}
	
	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public String getInputNumber() {
		return inputNumber;
	}

	public void setInputNumber(String inputNumber) {
		this.inputNumber = inputNumber;
	}

	public List<DefectResult> getExistingDefects() {
		return existingDefects;
	}

	protected void setExistingDefects(List<DefectResult> existingDefects) {
		this.existingDefects = existingDefects;
	}

	public List<DefectResult> getNewDefects() {
		return newDefects;
	}

	protected void setNewDefects(List<DefectResult> newDefects) {
		this.newDefects = newDefects;
	}

	public List<DefectResult> getOutstandingDefects() {
		List<DefectResult> outstandingDefects = new ArrayList<DefectResult>();
		for (DefectResult defect : getDefects()) {
			if (!defect.isRepairedStatus()) {
				outstandingDefects.add(defect);
			}
		}
		return outstandingDefects;
	}

	public Map<String, Object> getProductPreCheckResults() {
		return productPreCheckResults;
	}

	public void setProductPreCheckResults(Map<String, Object> productPreCheckResults) {
		this.productPreCheckResults = productPreCheckResults;
	}

	public Map<String, Object> getProductWarnCheckResults() {
		return productWarnCheckResults;
	}

	public void setProductWarnCheckResults(Map<String, Object> productWarnChecks) {
		this.productWarnCheckResults = productWarnChecks;
	}

	public Map<String, Object> getProductItemCheckResults() {
		return productItemCheckResults;
	}

	public void setProductItemCheckResults(Map<String, Object> productItemChecks) {
		this.productItemCheckResults = productItemChecks;
	}

	public List<InspectionPartDescription> getParts() {
		return parts;
	}

	public void setParts(List<InspectionPartDescription> parts) {
		this.parts = parts;
	}

	public List<String> getPartNames() {
		Set<String> partNames= new HashSet<String>();
		
		for(InspectionPartDescription item : getParts()) {
			partNames.add(item.getInspectionPartName());
		}
		return new SortedArrayList<String>(partNames);
	}

	public List<String> getLocationNames(String partName) {
		List<String> locationNames= new ArrayList<String>();
		
		for(InspectionPartDescription item : getParts()) {
			if(StringUtils.equals(partName, item.getInspectionPartName()))
				locationNames.add(item.getInspectionPartLocationName());
		}
		return locationNames;

	}

	public Set<String> getDefectNames() {
		return getDefectDescriptions().keySet();
	}

	public List<String>  getSecondaryPartNames(String defectName) {
		
		List<String> otherParts = new ArrayList<String>();
		
		for(DefectDescription item : getDefectDescriptions().get(defectName)) {
			otherParts.add(item.getId().getSecondaryPartName());
		}
		
		return otherParts;
		
	}

	public Map<String, List<DefectDescription>> getDefectDescriptions() {
		return defectDescriptions;
	}
	
	public List<DefectDescription> getDefectDescriptions(String defectName) {
		return getDefectDescriptions().get(defectName);
	}

	public void setDefectDescriptions(Map<String, List<DefectDescription>> defectDescriptions) {
		this.defectDescriptions = defectDescriptions;
	}

	public List<DefectResult> getUpdatedDefects() {
		return updatedDefects;
	}

	protected void setUpdatedDefects(List<DefectResult> updatedDefects) {
		this.updatedDefects = updatedDefects;
	}

	public List<DefectTypeDescription> getDefectTypes() {
		return defectTypes;
	}
	
	public List<DefectTypeDescription> getDistinctDefectTypes() {
		Set<String> defectTypeNames = new HashSet<String>();
		List<DefectTypeDescription> selectedDefectTypes = new SortedArrayList<DefectTypeDescription>("getDefectTypeName");
		if(defectTypes == null) return selectedDefectTypes;
		for(DefectTypeDescription defectType : defectTypes) {
			if(!defectTypeNames.contains(defectType.getDefectTypeName())) {
				selectedDefectTypes.add(defectType);
				defectTypeNames.add(defectType.getDefectTypeName());
			}
		}
		return selectedDefectTypes;
	}

	public void setDefectTypes(List<DefectTypeDescription> defectTypes) {
		this.defectTypes = defectTypes;
	}

	public List<ImageSection> getImageSections() {
		return imageSections;
	}

	public void setImageSections(List<ImageSection> imageSections) {
		this.imageSections = imageSections;
	}

	public List<DefectResult> getDefects() {
		return defects;
	}

	protected void setDefects(List<DefectResult> defects) {
		this.defects = defects;
	}

	public void removeDefect(DefectResult defect) {
		getDefects().remove(defect);
		getNewDefects().remove(defect);
		getUpdatedDefects().remove(defect);
	}

	public void addNewDefect(DefectResult defect) {
		getDefects().add(defect);
		getNewDefects().add(defect);
	}

	public void addExistingDefects(List<DefectResult> defects) {
		if (defects == null) {
			return;
		}
		getExistingDefects().addAll(defects);
		getDefects().addAll(defects);
	}

	public void addUpdatedDefect(DefectResult defect) {
		if(!defect.isNewDefect())
		   getUpdatedDefects().add(defect);
	}

	public void clearNewDefects() {
		getDefects().removeAll(getNewDefects());
		getNewDefects().clear();
	}
	
	public List<DefectResult> getChangedDefects() {
		List<DefectResult> defectResults = new ArrayList<DefectResult>();
		defectResults.addAll(getUpdatedDefects());
		defectResults.addAll(getNewDefects());
		return defectResults;
	}

	public boolean isDefectsUpdated() {
		boolean updated = !getNewDefects().isEmpty() || !getUpdatedDefects().isEmpty();
		return updated;
	}

	public List<InspectionModel> getInspectionModels() {
		return inspectionModels;
	}

	public void setInspectionModels(List<InspectionModel> inspectionModels) {
		this.inspectionModels = inspectionModels;
	}

	public List<DefectGroup> getDefectGroups() {
		List<DefectGroup> defectGroups = new SortedArrayList<DefectGroup>("getDefectGroupName");
		for (InspectionModel model : getInspectionModels()) {
			
			DefectGroup defectGroup = model.getDefectGroup();
			if(!defectGroups.contains(defectGroup))
				defectGroups.add(defectGroup);
		}	
		return defectGroups;
	}

	public List<String> getPartGroupNames() {
		List<String> groupNames = new ArrayList<String>();
		for (InspectionModel model : getInspectionModels()) {
			if (model == null || model.getId().getPartGroupName() == null) {
				continue;
			}
			String groupName = model.getId().getPartGroupName().trim();
			if (!groupNames.contains(groupName)) {
				groupNames.add(groupName);
			}
		}
		Collections.sort(groupNames);
		return groupNames;
	}

	public void setScrap(boolean scrap) {
		this.productScrapped = scrap;
	}
	
	public boolean isProductScrapped() {
		return this.productScrapped;
	}
	
	public void setProductProcessable(boolean productProcessable) {
		this.productProcessable = productProcessable;
	}

	public boolean isProductProcessable() {
		return productProcessable;
	}

	public BaseProduct getOwnerProduct() {
		return ownerProduct;
	}

	public void setOwnerProduct(BaseProduct ownerProduct) {
		this.ownerProduct = ownerProduct;
	}

	public List<ProductHistory> getProductHistory() {
		return productHistoryList;
	}

	public void setProductHistory(List<ProductHistory> productHistoryList) {
		this.productHistoryList = productHistoryList;
	}
	
	public List<IPPTag> getIppHistory() {
		return ippHistory;
	}

	public void setIppHistory(List<IPPTag> ippHistory) {
		this.ippHistory = ippHistory;
	}
}
