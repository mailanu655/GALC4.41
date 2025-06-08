package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.AdminUser;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class AdminUserSearchForm extends ActionForm

{
    private static final long serialVersionUID = 1L;

	private String userMask = "*";
    
    private List<AdminUser> adminUsers = null;
    
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
     * @return Returns the adminUsers.
     */
    public List<AdminUser> getAdminUsers() {
        
        if (adminUsers == null)
        {
            adminUsers = new ArrayList<AdminUser>(0);
        }
        return adminUsers;
    }
    
    
    /**
     * @param adminUsers A list of AdministrativeUserData objects.
     */
    public void setAdminUsers(List<AdminUser> adminUsers) {
        
        
        this.adminUsers = adminUsers;
    }
    
    /**
     * @return Returns the userMask.
     */
    public String getUserMask() {
    	if(userMask == null) return "%";
    	String mask = userMask.replace("*", "%");
    	return mask.contains("%") ? mask : mask + "%";
    }
    
    /**
     * @param userMask The userMask to set.
     */
    public void setUserMask(String userMask) {
        this.userMask = userMask;
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
}
