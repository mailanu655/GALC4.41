package com.honda.galc.system.config.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.ComponentProperty;


/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class PropertySettingsForm extends ActionForm

{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Component ID that identifies current properties being edited.
	 */
	private String componentID = null;
	private String propertyKey = null;
	private String propertyValue = null;
	private String description = null;
	
	private String changeUserID = null;
	
	private String referenceComponentID = null;
	private java.util.List referenceComponents = null;
	
	/**
	 * New ID to load properties for.
	 */
	private String loadComponentID = null;
	
	/**
	 * If true, the user cannot change componetID value.
	 */
	private boolean staticComponentID = false;
	

	private String apply = null;
	
	private String delete = null;
	
	private String rename = null;
	
	private String refresh = null;
	
	private String deleteAll = null;
	
	private String loadFromTemplate = null;
	
	private String loadComponent = null;
	
	private boolean editor = false;
	
	private boolean confirmDelete = false;
	
	private boolean confirmDeleteAll = false;
	
	private String renamePropertyKey = null;
	
	/**
	 * 0 = application property
	 * 1 = terminal property
	 * 2 = component property
	 */
	private int componentType = 2;
	
	public static final int COMPONENT_TYPE_APPLICATION = 0;
	public static final int COMPONENT_TYPE_TERMINAL = 1;
	public static final int COMPONENT_TYPE_MISC_COMPONENT = 2;
	
	

    public void reset(ActionMapping mapping, HttpServletRequest request) {

	// Reset field values here.

    }

    public ActionErrors validate(ActionMapping mapping,
	    HttpServletRequest request) {

	ActionErrors errors = new ActionErrors();
	// Validate the fields in your form, adding
	// adding each error to this.errors as found, e.g.

	// if ((field == null) || (field.length() == 0)) {
	//   errors.add("field", new org.apache.struts.action.ActionError("error.field.required"));
	// }
	return errors;

    }

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String getChangeUserID() {
		return changeUserID;
	}

	public void setChangeUserID(String changeUserID) {
		this.changeUserID = changeUserID;
	}

	public String getComponentID() {
		return componentID;
	}

	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	public String getDeleteAll() {
		return deleteAll;
	}

	public void setDeleteAll(String deleteAll) {
		this.deleteAll = deleteAll;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEditor() {
		return editor;
	}

	public void setEditor(boolean editor) {
		this.editor = editor;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public boolean isStaticComponentID() {
		return staticComponentID;
	}

	public void setStaticComponentID(boolean staticComponentID) {
		this.staticComponentID = staticComponentID;
	}

	public int getComponentType() {
		return componentType;
	}

	public void setComponentType(int componentType) {
		this.componentType = componentType;
	}

	public String getRenamePropertyKey() {
		return renamePropertyKey;
	}

	public void setRenamePropertyKey(String renamePropertyKey) {
		this.renamePropertyKey = renamePropertyKey;
	}

	public boolean isConfirmDeleteAll() {
		return confirmDeleteAll;
	}

	public void setConfirmDeleteAll(boolean confirmDeleteAll) {
		this.confirmDeleteAll = confirmDeleteAll;
	}
	
	/**
	 * Utility method for getting value object from form data.
	 * @return
	 */
	public ComponentProperty getPropertyData() {
		ComponentProperty data = new ComponentProperty(getComponentID(),getPropertyKey(),getPropertyValue());
		data.setChangeUserId(getChangeUserID());
		data.setDescription(getDescription());
		return data;
	}
	
	/**
	 * Utility method for initializing form data with value object.
	 * @param data
	 */
	public void setPropertyData(ComponentProperty data) {
		setChangeUserID(data.getChangeUserId());
		setComponentID(data.getId().getComponentId());
		setDescription(data.getDescription());
		setPropertyKey(data.getId().getPropertyKey());
		setPropertyValue(data.getPropertyValue());
	}

	public String getReferenceComponentID() {
		return referenceComponentID;
	}

	public void setReferenceComponentID(String referenceComponentID) {
		this.referenceComponentID = referenceComponentID;
	}

	public java.util.List getReferenceComponents() {
		if (referenceComponents == null) {
			referenceComponents = new java.util.ArrayList(0);
		}
		return referenceComponents;
	}

	public void setReferenceComponents(java.util.List referenceComponents) {
		this.referenceComponents = referenceComponents;
	}

	public boolean isConfirmDelete() {
		return confirmDelete;
	}

	public void setConfirmDelete(boolean confirmDelete) {
		this.confirmDelete = confirmDelete;
	}

	public String getLoadFromTemplate() {
		return loadFromTemplate;
	}

	public void setLoadFromTemplate(String loadFromTemplate) {
		this.loadFromTemplate = loadFromTemplate;
	}

	public String getLoadComponent() {
		return loadComponent;
	}

	public void setLoadComponent(String loadComponent) {
		this.loadComponent = loadComponent;
	}

	public String getLoadComponentID() {
		return loadComponentID;
	}

	public void setLoadComponentID(String loadComponentID) {
		this.loadComponentID = loadComponentID;
	}

	/**
	 * @return the refresh
	 */
	public String getRefresh() {
		return refresh;
	}

	/**
	 * @param refresh the refresh to set
	 */
	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}
}
