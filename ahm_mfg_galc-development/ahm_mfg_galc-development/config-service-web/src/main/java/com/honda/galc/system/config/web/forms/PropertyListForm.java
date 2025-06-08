package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for a Struts application.
 * Users may access 2 fields on this form:
 * <ul>
 * <li>componentID - [your comment here]
 * <li>componentType - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class PropertyListForm extends ActionForm

{

   private static final long serialVersionUID = 1L;

	private String componentID = null;

    private int componentType;
    
    private java.util.List propertyDataList = null;

    /**
     * Get componentID
     * @return String
     */
    public String getComponentID() {
	return componentID;
    }

    /**
     * Set componentID
     * @param <code>String</code>
     */
    public void setComponentID(String c) {
	this.componentID = c;
    }

    /**
     * Get componentType
     * @return int
     */
    public int getComponentType() {
	return componentType;
    }

    /**
     * Set componentType
     * @param <code>int</code>
     */
    public void setComponentType(int c) {
	this.componentType = c;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

	// Reset values are provided as samples only. Change as appropriate.

	componentID = null;
	componentType = 0;

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

	public java.util.List getPropertyDataList() {
		if (propertyDataList == null) {
			propertyDataList = new ArrayList(0);
		}
		return propertyDataList;
	}

	public void setPropertyDataList(java.util.List propertyDataList) {
		this.propertyDataList = propertyDataList;
	}
    
    
}
