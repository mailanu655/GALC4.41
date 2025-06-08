package com.honda.galc.oif.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for a Struts application.
 * Users may access 1 field on this form:
 * <ul>
 * <li>interfaceName - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class OifStartFormBean extends ActionForm

{
	private static final long serialVersionUID = 6215634802220698699L;
	public static final String DEF_START_TIME_STAMP = "yyyy-MM-dd HH:mm:ss";
    private String interfaceName = null;

    private String startTimeStamp = null;
    
    /**
     * Get interfaceName
     * @return String
     */
    public String getInterfaceName() {
    	return interfaceName;
    }

    /**
     * Set interfaceName
     * @param <code>String</code>
     */
    public void setInterfaceName(String n) {
    	this.interfaceName = n;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
		// Reset values are provided as samples only. Change as appropriate.
		interfaceName = null;
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

	public boolean isStartTimestampSet() {
		return this.startTimeStamp != null && !DEF_START_TIME_STAMP.equals(this.startTimeStamp);
	}
	public String getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(String startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
}
