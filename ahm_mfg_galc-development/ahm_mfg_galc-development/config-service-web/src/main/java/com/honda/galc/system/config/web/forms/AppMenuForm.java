package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.ApplicationMenuEntry;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This is the Struts form bean for the Application Menu Settings page.</p>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Feb 22, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class AppMenuForm extends ActionForm

{
    
    private static final long serialVersionUID = 1L;
	private String displayText = null;
	private String clientID = null;
	private String nodeName = null;
	private int nodeNumber = 0;
	private int parentNodeNumber = 0;
	
	// Form buttons
	private String apply = null;
	private String cancel = null;
	private String delete = null;
	
	// Create new child flag
	private boolean newChild = false;
	
	// Editor flag
	private boolean editor = false;
	
	// Flag that indicates if current "edit" node can be deleted
	private boolean deletable = false;
	
	// Flag that indicates if delete has been confirmed.
	private boolean confirmDelete = false;
	
	// This is the nested ApplicationMenuDataNode list
	private List<ApplicationMenuEntry> applicationMenuDataNodes =  null;
	
	// This is the current parent to display
	private int currentDisplayParent = 0;
	
	private int modifyNode = 0;
	
	private String createFlag = null;
	private boolean showSetting = false;
    
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
     * @return Returns the showSetting.
     */
    public boolean isShowSetting() {
        return showSetting;
    }
    /**
     * @param showSetting The showSetting to set.
     */
    public void setShowSetting(boolean showSetting) {
        this.showSetting = showSetting;
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
    /**
     * @return Returns the modifyNode.
     */
    public int getModifyNode() {
        return modifyNode;
    }
    /**
     * @param modifyNode The modifyNode to set.
     */
    public void setModifyNode(int modifyNode) {
        this.modifyNode = modifyNode;
    }
    /**
     * @return Returns the clientID.
     */
    public String getClientID() {
        return clientID;
    }
    /**
     * @param clientID The clientID to set.
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
    /**
     * @return Returns the displayText.
     */
    public String getDisplayText() {
        return displayText;
    }
    /**
     * @param displayText The displayText to set.
     */
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
    /**
     * @return Returns the nodeName.
     */
    public String getNodeName() {
        return nodeName;
    }
    /**
     * @param nodeName The nodeName to set.
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    /**
     * @return Returns the nodeNumber.
     */
    public int getNodeNumber() {
        return nodeNumber;
    }
    /**
     * @param nodeNumber The nodeNumber to set.
     */
    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }
    /**
     * @return Returns the parentNodeNumber.
     */
    public int getParentNodeNumber() {
        return parentNodeNumber;
    }
    /**
     * @param parentNodeNumber The parentNodeNumber to set.
     */
    public void setParentNodeNumber(int parentNodeNumber) {
        this.parentNodeNumber = parentNodeNumber;
    }
    /**
     * @return Returns the applicationMenuDataNodes.
     */
    public List<ApplicationMenuEntry> getApplicationMenuDataNodes() {
        
        if (applicationMenuDataNodes == null)
        {
            applicationMenuDataNodes = new ArrayList<ApplicationMenuEntry>();
        }
        return applicationMenuDataNodes;
    }
    
    /**
     * This utility method scans the nested applicationMenuDataNodes list for 
     * the current parent being edited and returns a list of the child nodes of that parent.
     * @return
     */
    public List<ApplicationMenuEntry> getCurrentChildList() {
        
    	List<ApplicationMenuEntry> childList = new ArrayList<ApplicationMenuEntry>();
    	for(ApplicationMenuEntry menuEntry : getApplicationMenuDataNodes()) {
            if(menuEntry.getParentNodeNumber() == currentDisplayParent)
            	childList.add(menuEntry);
    	}		
    	
        
        return childList;
        
    }
    
    public ApplicationMenuEntry getCurrentParent() {
        
        ApplicationMenuEntry currentParent = null;
        
        if (currentDisplayParent == 0)
        {
            currentParent = new ApplicationMenuEntry(getClientID(),0);
            
            currentParent.setNodeName("menu-root");
            return currentParent;
        }
        
        currentParent = scanForNode(getApplicationMenuDataNodes(),currentDisplayParent);
        
        
        return currentParent;
    }
    
    private ApplicationMenuEntry scanForNode(List<ApplicationMenuEntry> nodeList, int nodeID)
    {
        
        for(ApplicationMenuEntry menuEntry : nodeList)
            
            if (menuEntry.getId().getNodeNumber() == nodeID)
                return menuEntry;
        
        return null;
    }
    
    
    /**
     * @param applicationMenuDataNodes The applicationMenuDataNodes to set.
     */
    public void setApplicationMenuDataNodes(List<ApplicationMenuEntry> applicationMenuDataNodes) {
        this.applicationMenuDataNodes = applicationMenuDataNodes;
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
     * @return Returns the currentDisplayParent.
     */
    public int getCurrentDisplayParent() {
        return currentDisplayParent;
    }
    /**
     * @param currentDisplayParent The currentDisplayParent to set.
     */
    public void setCurrentDisplayParent(int currentDisplayParent) {
        this.currentDisplayParent = currentDisplayParent;
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
     * @return Returns the newChild.
     */
    public boolean isNewChild() {
        return newChild;
    }
    /**
     * @param newChild The newChild to set.
     */
    public void setNewChild(boolean newChild) {
        this.newChild = newChild;
    }
    /**
     * @return Returns the deletable.
     */
    public boolean isDeletable() {
        return deletable;
    }
    /**
     * @param deletable The deletable to set.
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
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
     * @return Returns the confirmDelte.
     */
    public boolean isConfirmDelete() {
        return confirmDelete;
    }
    /**
     * @param confirmDelte The confirmDelte to set.
     */
    public void setConfirmDelete(boolean confirmDelete) {
        this.confirmDelete = confirmDelete;
    }
}
