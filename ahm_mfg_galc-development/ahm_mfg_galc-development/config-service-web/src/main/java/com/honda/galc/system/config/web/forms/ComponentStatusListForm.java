package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ComponentStatusListForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String componentID = null;
	private int componentType;
	@SuppressWarnings("rawtypes")
	private List componentStatusDataList = null;

	public String getComponentID() {
		return componentID;
	}

	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	public int getComponentType() {
		return componentType;
	}

	public void setComponentType(int componentType) {
		this.componentType = componentType;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// Reset values are provided as samples only. Change as appropriate.
		componentID = null;
		componentType = 0;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return new ActionErrors();
	}

	@SuppressWarnings("rawtypes")
	public List getComponentStatusDataList() {
		if (this.componentStatusDataList == null) {
			this.componentStatusDataList = new ArrayList(0);
		}
		return this.componentStatusDataList;
	}

	@SuppressWarnings("rawtypes")
	public void setComponentStatusDataList(List componentStatusDataList) {
		this.componentStatusDataList = componentStatusDataList;
	}
}