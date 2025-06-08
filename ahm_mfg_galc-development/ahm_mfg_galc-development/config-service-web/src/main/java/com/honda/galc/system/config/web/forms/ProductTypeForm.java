package com.honda.galc.system.config.web.forms;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.ProductTypeData;

public class ProductTypeForm extends ActionForm{
	private static final long serialVersionUID = 1L;

	private String productType;
	private String productTypeLabel;
	private String productSpecCodeFormat;
	private String productSpecCodeLabel;
	private String ownerProductType;
	private String productIdLabel;
	private String productIdFormat;

	private boolean deleteConfirm = false;
	private boolean initializePage = false;
	private boolean initializeFrame = false;
	private boolean existingProductType = false;
	private boolean editor = false; 
	private String apply = null;
	private String cancel = null;
	private String delete = null;
	private boolean refreshList = false;
	private Map<String, String> productTypes;
	
	public ProductTypeData getProductTypeData() {
		ProductTypeData productTypeData = new ProductTypeData();
		productTypeData.setProductTypeName(this.getProductType()); 
		productTypeData.setProductTypeLabel(this.getProductTypeLabel());
		
		productTypeData.setProductSpecCodeFormat(this.getProductSpecCodeFormat());
		productTypeData.setProductSpecCodeLabel(this.getProductSpecCodeLabel());
		productTypeData.setOwnerProductTypeName(this.getOwnerProductType());
		productTypeData.setProductIdLabel(this.getProductIdLabel());
		productTypeData.setProductIdFormatType(this.getProductIdFormat());
		
		return productTypeData;
	}
	
	public void populate(ProductTypeData type) {
		this.setProductType(type.getProductTypeName());		
		this.setOwnerProductType(type.getOwnerProductTypeName());
		this.setProductSpecCodeFormat(type.getProductSpecCodeFormat());
		this.setProductTypeLabel(type.getProductTypeLabel());
		this.setProductSpecCodeLabel(type.getProductSpecCodeLabel());
		this.setProductIdLabel(type.getProductIdLabel());
		this.setProductIdFormat(type.getProductIdFormatType());
		
	}	
	
	//------------getters & setters ---------------
	public String getProductType() {
		return StringUtils.trim(productType);
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductTypeLabel() {
		return productTypeLabel;
	}
	public void setProductTypeLabel(String productTypeLabel) {
		this.productTypeLabel = productTypeLabel;
	}
	public String getProductSpecCodeFormat() {
		return productSpecCodeFormat;
	}
	public void setProductSpecCodeFormat(String productSpecCodeFormat) {
		this.productSpecCodeFormat = productSpecCodeFormat;
	}
	public String getProductSpecCodeLabel() {
		return productSpecCodeLabel;
	}
	public void setProductSpecCodeLabel(String productSpecCodeLabel) {
		this.productSpecCodeLabel = productSpecCodeLabel;
	}
	public String getOwnerProductType() {
		return ownerProductType;
	}
	public void setOwnerProductType(String ownerProductType) {
		this.ownerProductType = ownerProductType;
	}
	public String getProductIdLabel() {
		return productIdLabel;
	}
	public void setProductIdLabel(String productIdLabel) {
		this.productIdLabel = productIdLabel;
	}
	public String getProductIdFormat() {
		return productIdFormat;
	}
	public void setProductIdFormat(String productIdFormat) {
		this.productIdFormat = productIdFormat;
	}
	public boolean isEditor() {
		return editor;
	}
	public void setEditor(boolean editor) {
		this.editor = editor;
	}
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}
	public boolean isDeleteConfirm() {
		return deleteConfirm;
	}
	public void setDeleteConfirm(boolean deleteConfirm) {
		this.deleteConfirm = deleteConfirm;
	}
	public boolean isInitializePage() {
		return initializePage;
	}
	public void setInitializePage(boolean initializePage) {
		this.initializePage = initializePage;
	}
	public boolean isInitializeFrame() {
		return initializeFrame;
	}
	public void setInitializeFrame(boolean initializeFrame) {
		this.initializeFrame = initializeFrame;
	}
	public boolean isExistingProductType() {
		return existingProductType;
	}
	public void setExistingProductType(boolean existingProductType) {
		this.existingProductType = existingProductType;
	}
	public boolean isRefreshList() {
		return refreshList;
	}
	public void setRefreshList(boolean refreshList) {
		this.refreshList = refreshList;
	}
	
	public Map<String, String> getProductTypes() {
	    return productTypes;
	}

	public void setProductTypes(Map<String, String> productTypes) {
		this.productTypes = productTypes;
	}
	

	public void reset() {
		this.setProductType("");		
		this.setOwnerProductType("");
		this.setProductIdLabel("");
		this.setProductTypeLabel("");
		this.setProductSpecCodeLabel("");
		this.setProductIdLabel("");
		this.setProductIdFormat("");
	}

	public void populateMbpnTypes(List<String> mbpnTypes) {
		if(productTypes == null){
	    	productTypes = new LinkedHashMap<String, String>();
	    	productTypes.put("None", "");
	    	productTypes.putAll(ProductType.getProductTypeNames());
	    	productTypes.remove(ProductType.MBPN_PART.name());
	    }
		
		for(String mbpnType : mbpnTypes)
			productTypes.put(StringUtils.trim(mbpnType), StringUtils.trim(mbpnType));
		
	}


}
