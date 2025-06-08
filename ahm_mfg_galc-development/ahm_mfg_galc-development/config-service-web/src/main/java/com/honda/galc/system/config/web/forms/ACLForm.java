package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.SecurityGroup;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class ACLForm extends ActionForm

{
    
    private static final long serialVersionUID = 1L;
	private int operation;
    private String screenID;
    private String securityGrp;
    
    /**
     * ID of application with the specified screen ID
     */
    private String applicationID;
    
    /**
     * Name of application with the specified screen ID.
     */
    private String applicationName;
    
    private String removeSecurityGroup = null;
    
    private boolean editor = false;
    
    private String apply = null;
    private String remove = null;
    private String cancel = null;
    private boolean init=false;

    /**
     * List of security group objects
     */
    private List<SecurityGroup> groups = null;
    
    /**
     * List of ACL data objects for the specified screen ID
     */
    private List<AccessControlEntry> aclData = null;
    
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
     * @return Returns the operation.
     */
    public int getOperation() {
        return operation;
    }
    /**
     * @param operation The operation to set.
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }
    /**
     * @return Returns the screenID.
     */
    public String getScreenID() {
        return screenID;
    }
    /**
     * @param screenID The screenID to set.
     */
    public void setScreenID(String screenID) {
        this.screenID = screenID;
    }
    /**
     * @return Returns the securityGrp.
     */
    public String getSecurityGrp() {
        return securityGrp;
    }
    /**
     * @param securityGrp The securityGrp to set.
     */
    public void setSecurityGrp(String securityGrp) {
        this.securityGrp = securityGrp;
    }
    /**
     * @return Returns the aclData.
     */
    public List<AccessControlEntry> getAclData() {
        if (aclData == null)
        {
            aclData = new java.util.ArrayList<AccessControlEntry>(0);
        }
        return aclData;
    }
    /**
     * @param aclData The aclData to set.
     */
    public void setAclData(List<AccessControlEntry> aclData) {
        this.aclData = aclData;
    }
    /**
     * @return Returns the apply.
     */
    public String getApply() {
        return apply;
    }
    /**
     * @param apply The apply to set.
     */
    public void setApply(String apply) {
        this.apply = apply;
    }
    /**
     * @return Returns the cancel.
     */
    public String getCancel() {
        return cancel;
    }
    /**
     * @param cancel The cancel to set.
     */
    public void setCancel(String cancel) {
        this.cancel = cancel;
    }
    /**
     * @return Returns the groups.
     */
    public List<SecurityGroup> getGroups() {
        if (groups == null)
        {
            groups = new ArrayList<SecurityGroup>(0);
        }
        return groups;
    }
    /**
     * @param groups The groups to set.
     */
    public void setGroups(List<SecurityGroup> groups) {
        this.groups = groups;
    }
    /**
     * @return Returns the remove.
     */
    public String getRemove() {
        return remove;
    }
    /**
     * @param remove The remove to set.
     */
    public void setRemove(String remove) {
        this.remove = remove;
    }
    /**
     * @return Returns the applicationID.
     */
    public String getApplicationID() {
        return applicationID;
    }
    /**
     * @param applicationID The applicationID to set.
     */
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }
    /**
     * @return Returns the applicationName.
     */
    public String getApplicationName() {
        return applicationName;
    }
    /**
     * @param applicationName The applicationName to set.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    

    /**
     * @return Returns the init.
     */
    public boolean isInit() {
        return init;
    }
    /**
     * @param init The init to set.
     */
    public void setInit(boolean init) {
        this.init = init;
    }
    /**
     * @return Returns the removeSecurityGroup.
     */
    public String getRemoveSecurityGroup() {
        return removeSecurityGroup;
    }
    /**
     * @param removeSecurityGroup The removeSecurityGroup to set.
     */
    public void setRemoveSecurityGroup(String removeSecurityGroup) {
        this.removeSecurityGroup = removeSecurityGroup;
    }
    /**
     * @return Returns the editor.
     */
    public boolean isEditor() {
        return editor;
    }
    /**
     * @param editor The editor to set.
     */
    public void setEditor(boolean editor) {
        this.editor = editor;
    }
}
