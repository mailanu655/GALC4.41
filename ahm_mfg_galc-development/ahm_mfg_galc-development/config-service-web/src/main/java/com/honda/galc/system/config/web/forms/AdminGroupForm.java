package com.honda.galc.system.config.web.forms;

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
public class AdminGroupForm extends ActionForm

{
    
	private static final long serialVersionUID = 1L;

	private String groupID = null;
    
    private String description = null;
    
    private String displayName = null;
    
    private boolean existingGroup = false;
    
    private boolean deleteConfirm = false;
    
    private boolean initializePage = false;
    
    private boolean initializeFrame = false;
    
    private boolean editor = false;
    
    private String apply = null;
    private String cancel = null;
    private String delete = null;
    
    private boolean refreshList = false;
    
    
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
     * @return Returns the existingGroup.
     */
    public boolean isExistingGroup() {
        return existingGroup;
    }
    /**
     * @param existingGroup The existingGroup to set.
     */
    public void setExistingGroup(boolean existingGroup) {
        this.existingGroup = existingGroup;
    }
    /**
     * @return Returns the groupID.
     */
    public String getGroupID() {
        return groupID;
    }
    /**
     * @param groupID The groupID to set.
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
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
    
    /**
     * Utility method for packaging the group data in
     * value object.
     * @return
     */
    public AdminGroup getData()
    {
        AdminGroup data = new AdminGroup();
        
        data.setGroupId(getGroupID());
        data.setGroupDesc(getDescription());
        data.setDisplayName(getDisplayName());
        
        return data;
        
    }
}
