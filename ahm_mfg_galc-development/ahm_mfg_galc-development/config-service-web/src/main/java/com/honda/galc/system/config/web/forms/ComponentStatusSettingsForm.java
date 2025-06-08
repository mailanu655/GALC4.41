package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.ComponentStatus;

public class ComponentStatusSettingsForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String componentID = null;
	private String statusKey = null;
	private String statusValue = null;
	private String description = null;
	private String changeUserID = null;

	private String referenceComponentID = null;
	@SuppressWarnings("rawtypes")
	private List referenceComponents = null;
	private String loadComponentID = null;
	private boolean staticComponentID = false;
	private String apply = null;
	private String delete = null;
	private String deleteAll = null;
	private String loadComponent = null;
	private boolean editor = false;
	private boolean confirmDelete = false;
	private boolean confirmDeleteAll = false;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return new ActionErrors();
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

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public boolean isStaticComponentID() {
		return staticComponentID;
	}

	public void setStaticComponentID(boolean staticComponentID) {
		this.staticComponentID = staticComponentID;
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
	public ComponentStatus getComponentStatusData() {
		ComponentStatus data = new ComponentStatus(getComponentID(), getStatusKey(), getStatusValue());
		data.setChangeUserId(getChangeUserID());
		data.setDescription(getDescription());
		return data;
	}

	/**
	 * Utility method for initializing form data with value object.
	 * @param data
	 */
	public void setComponentStatusData(ComponentStatus data) {
		setChangeUserID(data.getChangeUserId());
		setComponentID(data.getId().getComponentId());
		setDescription(data.getDescription());
		setStatusKey(data.getId().getStatusKey());
		setStatusValue(data.getStatusValue());
	}

	public String getReferenceComponentID() {
		return referenceComponentID;
	}

	public void setReferenceComponentID(String referenceComponentID) {
		this.referenceComponentID = referenceComponentID;
	}

	@SuppressWarnings("rawtypes")
	public List getReferenceComponents() {
		if (referenceComponents == null) {
			referenceComponents = new ArrayList(0);
		}
		return referenceComponents;
	}

	@SuppressWarnings("rawtypes")
	public void setReferenceComponents(List referenceComponents) {
		this.referenceComponents = referenceComponents;
	}

	public boolean isConfirmDelete() {
		return confirmDelete;
	}

	public void setConfirmDelete(boolean confirmDelete) {
		this.confirmDelete = confirmDelete;
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
}
