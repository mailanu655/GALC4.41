package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.AdminGroup;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class AdminGroupSearchForm extends ActionForm

{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String groupMask = "*";
    
    /**
     * A collection of AdministrativeGroupData objects
     */
    private List<AdminGroup> adminGroups = null;
    
    
    private boolean initializePage = false;
    
    private String find = null;
    
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
    /**
     * @return Returns the adminGroups.
     */
    public List<AdminGroup> getAdminGroups() {
        return adminGroups;
    }
    /**
     * @param adminGroups The adminGroups to set.
     */
    public void setAdminGroups(List<AdminGroup> adminGroups) {
        this.adminGroups = adminGroups;
    }
    /**
     * @return Returns the find.
     */
    public String getFind() {
        return find;
    }
    /**
     * @param find The find to set.
     */
    public void setFind(String find) {
        this.find = find;
    }
    /**
     * @return Returns the groupMask.
     */
    public String getGroupMask() {
    	if(groupMask == null) return "%";
    	String mask = groupMask.replace("*", "%");
    	if(!mask.contains("%")) return mask + "%";
    	else return mask;
    }
    
    /**
     * @param groupMask The groupMask to set.
     */
    public void setGroupMask(String groupMask) {
        this.groupMask = groupMask;
    }
    /**
     * @return Returns the initializePage.
     */
    public boolean isInitializePage() {
        return initializePage;
    }
    /**
     * @param initializePage The initializePage to set.
     */
    public void setInitializePage(boolean initializePage) {
        this.initializePage = initializePage;
    }
}
