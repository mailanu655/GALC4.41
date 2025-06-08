package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.system.config.web.data.AttributeConstants;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class ApplicationSearchForm extends ActionForm

{

    private static final long serialVersionUID = 1L;
    
    
	private int applicationType = AttributeConstants.COMBOBOX_UNSELECTED_OPTION_VALUE;
    
    /**
     * This is a list of ApplicationData objects
     */
    private List<Application> applicationList = null;
    
    private boolean editor = false;
        
    private String searchText = null; 
    
    /**
     * This flag indicates application page is being accessed for first time.
     */
    private boolean initializePage = false;
    
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
     * @return Returns the applicationList.
     */
    public List<Application> getApplicationList() {
        return applicationList;
    }
    /**
     * @param applicationList The applicationList to set.
     */
    public void setApplicationList(List<Application> applicationList) {
        this.applicationList = applicationList;
    }
    /**
     * @return Returns the applicationType.
     */
    public int getApplicationType() {
        return applicationType;
    }
    /**
     * @param applicationType The applicationType to set.
     */
    public void setApplicationType(int applicationType) {
        this.applicationType = applicationType;
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
    
    public String getSearchText() {
        return searchText;
    }
    
    public void setSearchText(String searchText) {
        this.searchText = searchText;
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
     * Returns application types 
     * @return
     */
	public List<ApplicationType> getApplicationTypes() {
		return ApplicationType.getNonProcessPointApplicationTypes();
	}    
}
