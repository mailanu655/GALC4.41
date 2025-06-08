package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.SecurityGroup;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class SecurityGroupSearchForm extends ActionForm

{
    
	private static final long serialVersionUID = 1L;
	
	private String groupMask = "*";
    
    private List<SecurityGroup> securityGroups = null;
    
    private boolean initializePage = false;
    
    private String find = null;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        groupMask = "*";
        securityGroups = null;
        initializePage = false;
        find = null;

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
    /**
     * @return Returns the securityGroups.
     */
    public List getSecurityGroups() {
        return securityGroups;
    }
    /**
     * @param securityGroups The securityGroups to set.
     */
    public void setSecurityGroups(List<SecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
    }
}
