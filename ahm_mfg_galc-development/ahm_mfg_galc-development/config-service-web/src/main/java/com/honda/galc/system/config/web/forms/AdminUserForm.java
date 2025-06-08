package com.honda.galc.system.config.web.forms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.entity.conf.AdminGroup;
import com.honda.galc.entity.conf.AdminUser;
import com.honda.galc.entity.conf.AdminUserGroup;
import com.honda.galc.util.CommonUtil;


/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class AdminUserForm extends ActionForm

{

   private static final long serialVersionUID = 1L;

	private byte[] encryptedPassword = null;
    
    private String textPassword = null;
    
    private String confirmPassword = null;
    
    private String userID = null;
    
    private String displayName = null;
    
    private String description = null;
    
    private boolean changePassword = false;
    
    private boolean existingUser = false;
    
    private boolean deleteConfirm = false;
    
    private boolean initializePage = false;
    
    private boolean initializeFrame = false;
    
    private boolean editor = false;
    
    private String apply = null;
    private String cancel = null;
    private String delete = null;
    
    private boolean refreshList = false;
    
    private List<AdminGroup> allGroups = null;
    
    private List<AdminUserGroup> userGroups = new ArrayList<AdminUserGroup>(0);
    
    
    private String[] assignedGroups = null;
    private String availableGroups = null;
    
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
     * @return Returns the allGroups.
     */
    public List<AdminGroup> getAllGroups() {
        if (allGroups == null)
        {
            allGroups = new ArrayList<AdminGroup>(0);
        }
        return allGroups;
    }
    /**
     * @param allGroups The allGroups to set.
     */
    public void setAllGroups(List<AdminGroup> allGroups) {
        this.allGroups = allGroups;
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
     * @return Returns the changePassword.
     */
    public boolean isChangePassword() {
        return changePassword;
    }
    /**
     * @param changePassword The changePassword to set.
     */
    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }
    /**
     * @return Returns the confirmPassword.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }
    /**
     * @param confirmPassword The confirmPassword to set.
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    /**
     * @return Returns the delete.
     */
    public String getDelete() {
        return delete;
    }
    /**
     * @param delete The delete to set.
     */
    public void setDelete(String delete) {
        this.delete = delete;
    }
    /**
     * @return Returns the deleteConfirm.
     */
    public boolean isDeleteConfirm() {
        return deleteConfirm;
    }
    /**
     * @param deleteConfirm The deleteConfirm to set.
     */
    public void setDeleteConfirm(boolean deleteConfirm) {
        this.deleteConfirm = deleteConfirm;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the displayName.
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * @param displayName The displayName to set.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    /**
     * @return Returns the encryptedPassword.
     */
    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }
    /**
     * @param encryptedPassword The encryptedPassword to set.
     */
    public void setEncryptedPassword(byte[] encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
    /**
     * @return Returns the existingUser.
     */
    public boolean isExistingUser() {
        return existingUser;
    }
    /**
     * @param existingUser The existingUser to set.
     */
    public void setExistingUser(boolean existingUser) {
        this.existingUser = existingUser;
    }
    /**
     * @return Returns the textPassword.
     */
    public String getTextPassword() {
        return textPassword;
    }
    /**
     * @param textPassword The textPassword to set.
     */
    public void setTextPassword(String textPassword) {
        this.textPassword = textPassword;
    }
    /**
     * @return Returns the userGroups.
     */
    public List<AdminUserGroup> getUserGroups() {
        
        if (userGroups == null)
        {
            userGroups = new ArrayList<AdminUserGroup>(0);
        }
        
        return userGroups;
    }
    
    public List<AdminUserGroup> getAssignedUserGroups() {
    	List<AdminUserGroup> assignedUserGroups = new ArrayList<AdminUserGroup>();
    	String[] assignedGroups = getAssignedGroups();
    	for(String groupName: assignedGroups) {
    		AdminUserGroup item = new AdminUserGroup(getUserID(),groupName);
    		item.setCreateTimestamp(CommonUtil.getTimestampNow());
    		assignedUserGroups.add(item);
    	}
        return assignedUserGroups;
    }
    
    /**
     * @param userGroups The userGroups to set.
     */
    public void setUserGroups(List<AdminUserGroup> groups) {
    	userGroups = new ArrayList<AdminUserGroup>();
        for(AdminUserGroup item : groups)
        	this.userGroups.add(item);
    }
    
    public void setUserGroups(String[] assignedGroups) {
    	
    	userGroups = new ArrayList<AdminUserGroup>();
    	if(assignedGroups == null || assignedGroups.length == 0) return;
    	
    	for(String groupName: assignedGroups) {
    		AdminUserGroup item = new AdminUserGroup(getUserID(),groupName);
    		item.setCreateTimestamp(CommonUtil.getTimestampNow());
    		userGroups.add(item);
    	}
    }
    /**
     * @return Returns the userID.
     */
    public String getUserID() {
        return userID;
    }
    /**
     * @param userID The userID to set.
     */
    public void setUserID(String userID) {
        this.userID = userID;
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
     * @return Returns the initializeFrame.
     */
    public boolean isInitializeFrame() {
        return initializeFrame;
    }
    /**
     * @param initializeFrame The initializeFrame to set.
     */
    public void setInitializeFrame(boolean initializeFrame) {
        this.initializeFrame = initializeFrame;
    }
    
    public AdminUser getAdminUser() {
    	AdminUser user = new AdminUser();
    	user.setUserId(this.getUserID());
    	user.setUserDesc(this.getDescription());
    	user.setDisplayName(this.getDisplayName());
    	user.setPassword(getTextPassword());
    	return user;
    }
    
    private String encryptPassword(String password) throws ConfigurationServicesException{
    	
    	MessageDigest md = null;
    	try {
			md = MessageDigest.getInstance("MD5", "IBMJCE");
			md.reset();
			md.update(password.getBytes());
			return new String(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new ConfigurationServicesException("CFGW0080");
		} catch (NoSuchProviderException e) {
			throw new ConfigurationServicesException("CFGW0080");
		}
    }
    
    /**
     * @return Returns the assignedGroups.
     */
    public String[] getAssignedGroups() {
        
        if (assignedGroups == null)
        {
            List<AdminUserGroup> groupList = getUserGroups();
            assignedGroups = new String[groupList.size()];
            
            int idx = 0;
            
            for(AdminUserGroup data : groupList) {
            
                assignedGroups[idx] = data.getId().getGroupId();
                idx++;
            }
        }
        
        return assignedGroups;
    }
    
    /**
     * @param assignedGroups The assignedGroups to set.
     */
    public void setAssignedGroups(String[] assignedGroups) {
        this.assignedGroups = assignedGroups;
    }
    
    /**
     * @return Returns the availableGroups.
     */
    public String getAvailableGroups() {
        if (availableGroups == null)
        {
            availableGroups =  "";
        }
        return availableGroups;
    }
    /**
     * @param availableGroups The availableGroups to set.
     */
    public void setAvailableGroups(String availableGroups) {
        this.availableGroups = availableGroups;
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
    /**
     * @return Returns the refreshList.
     */
    public boolean isRefreshList() {
        return refreshList;
    }
    /**
     * @param refreshList The refreshList to set.
     */
    public void setRefreshList(boolean refreshList) {
        this.refreshList = refreshList;
    }
}
