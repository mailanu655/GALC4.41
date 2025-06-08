package com.honda.galc.system.config.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Site;

/**
 * Form bean for a Struts application.
 * Users may access 2 fields on this form:
 * <ul>
 * <li>description - [your comment here]
 * <li>name - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class SiteForm extends ActionForm

{
	private static final long serialVersionUID = 1L;
	
	private String description = null;

    private String name = null;
    
    private boolean editor = false;
    
    private boolean plantEditor = false;
 

    private String delete = null;
    
    private boolean confirmDelete = false;
    
    private boolean createFlag = false;
    
    private boolean refreshTree = false;
    
    /**
     * Get description
     * @return String
     */
    public String getDescription() {
    	
    	if (description == null)
    	{
    		description = "";
    	}
        return description;
    }

    /**
     * Set description
     * @param <code>String</code>
     */
    public void setDescription(String d) {
    	
    	if (d != null)
    	{
    		d = d.trim();
    	}
    	
        this.description = d;
    }

    /**
     * Get name
     * @return String
     */
    public String getName() {
    	if (name == null)
    	{
    		name = "";
    	}
        return name;
    }

    /**
     * Set name
     * @param <code>String</code>
     */
    public void setName(String n) {
    	
    	if (n != null)
    	{
    		n = n.trim();
    	}
    	
        this.name = n;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        // Reset values are provided as samples only. Change as appropriate.

        description = null;
        name = null;

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
     * Utility method for obtaining a SiteData value object
     * from the data in the form.
     * @return
     */
    public Site getSite()
    {
    	Site siteData = new Site();
    	
    	siteData.setSiteDescription(getDescription());
    	siteData.setSiteName(getName());
    	
    	return siteData;
    }
    
    /**
     * Utility method for setting form data from a 
     * SiteData value object.
     * @param siteData
     */
    public void setSite(Site site)
    {
        setDescription(site.getSiteDescription());
        setName(site.getSiteName());
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
     * @return Returns the plantEditor.
     */
    public boolean isPlantEditor() {
        return plantEditor;
    }
    /**
     * @param plantEditor The plantEditor to set.
     */
    public void setPlantEditor(boolean plantEditor) {
        this.plantEditor = plantEditor;
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
     * @return Returns the confirmDelete.
     */
    public boolean isConfirmDelete() {
        return confirmDelete;
    }
    /**
     * @param confirmDelete The confirmDelete to set.
     */
    public void setConfirmDelete(boolean confirmDelete) {
        this.confirmDelete = confirmDelete;
    }
    /**
     * @return Returns the createFlag.
     */
    public boolean isCreateFlag() {
        return createFlag;
    }
    /**
     * @param createFlag The createFlag to set.
     */
    public void setCreateFlag(boolean createFlag) {
        this.createFlag = createFlag;
    }
    /**
     * @return Returns the refreshTree.
     */
    public boolean isRefreshTree() {
        return refreshTree;
    }
    /**
     * @param refreshTree The refreshTree to set.
     */
    public void setRefreshTree(boolean refreshTree) {
        this.refreshTree = refreshTree;
    }
}
