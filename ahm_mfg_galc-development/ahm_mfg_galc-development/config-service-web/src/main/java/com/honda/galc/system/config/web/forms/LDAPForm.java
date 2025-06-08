package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.User;
/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class LDAPForm extends ActionForm

{
	private static final long serialVersionUID = 1L;
	
	private String createFlag;
    private List usersList;
    private String operation;

    private int expireDays;
    private String passwd;
    private String userID;
    private String userName;
    private String pwdUpdatedDate;
    private String cnfPwd;
    private String oldPassword;
    
    private boolean editor;
    private boolean freshUserList;
    private boolean deleteConfirmed = false;
    
    /**
     * @return Returns the freshUserList.
     */
    public boolean isFreshUserList() {
        return freshUserList;
    }
    /**
     * @param freshUserList The freshUserList to set.
     */
    public void setFreshUserList(boolean freshUserList) {
        this.freshUserList = freshUserList;
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
     * @return Returns the oldPassword.
     */
    public String getOldPassword() {
        return oldPassword;
    }
    /**
     * @param oldPassword The oldPassword to set.
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    private List securityGroups;
    private List userSecurityGroups;
    private String selectedSecurityGroup;
    private String assignedSecurityGroup[];
     
    /**
     * @return Returns the assignedSecurityGroup.
     */
    public String[] getAssignedSecurityGroup() {
        if(assignedSecurityGroup == null) assignedSecurityGroup = new String[0];
    	return assignedSecurityGroup;
    }
    /**
     * @param assignedSecurityGroup The assignedSecurityGroup to set.
     */
    public void setAssignedSecurityGroup(String[] assignedSecurityGroup) {
        this.assignedSecurityGroup = assignedSecurityGroup;
    }
    /**
     * @return Returns the selectedSecurityGroup.
     */
    public String getSelectedSecurityGroup() {
        return selectedSecurityGroup;
    }
    /**
     * @param selectedSecurityGroup The selectedSecurityGroup to set.
     */
    public void setSelectedSecurityGroup(String selectedSecurityGroup) {
        this.selectedSecurityGroup = selectedSecurityGroup;
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
    public void setSecurityGroups(List securityGroups) {
        this.securityGroups = securityGroups;
    }
    /**
     * @return Returns the userSecurityGroups.
     */
    public List getUserSecurityGroups() {
        return userSecurityGroups;
    }
    /**
     * @param userSecurityGroups The userSecurityGroups to set.
     */
    public void setUserSecurityGroups(List userSecurityGroups) {
        this.userSecurityGroups = userSecurityGroups;
    }
    /**
     * @return Returns the cnfPwd.
     */
    public String getCnfPwd() {
        return cnfPwd;
    }
    /**
     * @param cnfPwd The cnfPwd to set.
     */
    public void setCnfPwd(String cnfPwd) {
        this.cnfPwd = cnfPwd;
    }
    /**
     * @param pwdUpdatedDate The pwdUpdatedDate to set.
     */
    public void setPwdUpdatedDate(String pwdUpdatedDate) {
        this.pwdUpdatedDate = pwdUpdatedDate;
    }
    /**
     * @return Returns the expireDays.
     */
    public int getExpireDays() {
        return expireDays;
    }
    /**
     * @param expireDays The expireDays to set.
     */
    public void setExpireDays(int expireDays) {
        this.expireDays = expireDays;
    }
    /**
     * @return Returns the passwd.
     */
    public String getPasswd() {
        return passwd;
    }
    /**
     * @param passwd The passwd to set.
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
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
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }
    /**
     * @param operation The operation to set.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    /**
     * @return Returns the createFlag.
     */
    public String getCreateFlag() {
        return createFlag;
    }
    /**
     * @param createFlag The createFlag to set.
     */
    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        createFlag = null;
        usersList = null;
        expireDays = 0;
        passwd = null;
        cnfPwd = null;
        userID = null;
        userName = null;
        pwdUpdatedDate = null;
        securityGroups = null;
        userSecurityGroups = null;
        selectedSecurityGroup = null;
        assignedSecurityGroup = null;
        oldPassword = null;
        editor = false;
        freshUserList = false;
        boolean deleteConfirmed = false;
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
     * @return Returns the usersList.
     */
    public List getUsersList() {
        return usersList;
    }
    /**
     * @param usersList The usersList to set.
     */
    public void setUsersList(List usersList) {
        this.usersList = usersList;
    }

    /**
     * @return Returns the pwdUpdatedDate.
     */
    public String getPwdUpdatedDate() {
        return pwdUpdatedDate;
    }
    
    
    /**
     * @return Returns the deleteConfirmed.
     */
    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }
    /**
     * @param deleteConfirmed The deleteConfirmed to set.
     */
    public void setDeleteConfirmed(boolean deleteConfirmed) {
        this.deleteConfirmed = deleteConfirmed;
    }
    public void setUser(User aUser) 
    {
        expireDays = aUser.getExpireDays();
        passwd = aUser.getPasswd();
        userID = aUser.getUserId();
        userName = aUser.getUserName();
        pwdUpdatedDate = aUser.getPasswordUpdateDateString();
        cnfPwd = aUser.getPasswd(); 
        oldPassword = aUser.getPasswd();
    }

    public User getUser() 
    {
        User aUser = new User();
        aUser.setExpireDays(expireDays);
        aUser.setPasswd(passwd);
        aUser.setUserId(userID);
        aUser.setUserName(userName);
        aUser.setPasswd1(passwd);
        aUser.setPasswd2(passwd);
        aUser.setPasswd3(passwd);
        aUser.setPasswd4(passwd);
        aUser.setPasswd5(passwd);
        aUser.setPasswordUpdateDate(pwdUpdatedDate);
        return aUser;
    }

}
