package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.enumtype.ApplicationType;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class ApplicationForm extends ActionForm

{

    private static final long serialVersionUID = 1L;

	/**
     * The Description of Application.
     */
    public String applicationDescription;

    /**
     * The ID of Application. (Key Field)
     */
    public String applicationID;

    /**
     * The Name of Application.
     */
    public String applicationName;

    /**
     * The Type of Application.
     */
    public int applicationType;

    /**
     * @oim50
     * Implementation field for persistent attribute: preload
     */
    public int preload;

    /**
     * The class of screen.
     */
    public String screenClass;

    /**
     * The ID of Screen.
     */
    public String screenID;

    /**
     * @oim50
     * Implementation field for persistent attribute: sessionRequired
     */
    public boolean sessionRequired;
    
    private boolean persistentSession = false;

    /**
     * The flag is indicated this application is terminal application or not .
     */
    public boolean terminalApplicationFlag = true;

    /**
     * The title of window.
     */
    public String windowTitle;

    /**
     * List of TaskByApplicationData objects
     */
    private List<ApplicationTask> taskList  = null;
    
    
    private boolean editor = false;
    
    private boolean initializePage = false;
    
    private boolean createFlag = false;
    
    private String apply = null;
    private String cancel = null;
    private String delete = null;
    
    private boolean deleteConfirmed = false;

	/**
	 * List of BroadcastDestinationData objects.
	 */
	private List broadcastDestinationList = null;

	private String activePage = null;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        applicationID = null;
        applicationDescription = null;
        applicationName = null;
        applicationType = ApplicationType.TEAM_LEAD.getId();
        screenClass = "";
        screenID = "";
        preload = 0;
        taskList = null;

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
     * @return Returns the applicationDescription.
     */
    public String getApplicationDescription() {
        return applicationDescription;
    }
    /**
     * @param applicationDescription The applicationDescription to set.
     */
    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
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
     * @return Returns the preload.
     */
    public int getPreload() {
        return preload;
    }
    /**
     * @param preload The preload to set.
     */
    public void setPreload(int preload) {
        this.preload = preload;
    }
    /**
     * @return Returns the screenClass.
     */
    public String getScreenClass() {
        return screenClass;
    }
    /**
     * @param screenClass The screenClass to set.
     */
    public void setScreenClass(String screenClass) {
        this.screenClass = screenClass;
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
     * @return Returns the sessionRequired.
     */
    public boolean isSessionRequired() {
        return sessionRequired;
    }
    /**
     * @param sessionRequired The sessionRequired to set.
     */
    public void setSessionRequired(boolean sessionRequired) {
        this.sessionRequired = sessionRequired;
    }
    /**
     * @return Returns the terminalApplicationFlag.
     */
    public boolean isTerminalApplicationFlag() {
        return terminalApplicationFlag;
    }
    /**
     * @param terminalApplicationFlag The terminalApplicationFlag to set.
     */
    public void setTerminalApplicationFlag(boolean terminalApplicationFlag) {
        this.terminalApplicationFlag = terminalApplicationFlag;
    }
    /**
     * @return Returns the windowTitle.
     */
    public String getWindowTitle() {
        return windowTitle;
    }
    /**
     * @param windowTitle The windowTitle to set.
     */
    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
    
    /**
     * @return Returns the taskList.
     */
    public List<ApplicationTask> getTaskList() {
        if (taskList == null)
        {
            taskList = new ArrayList<ApplicationTask>(0);
        }
        return taskList;
    }
    /**
     * @param taskList The taskList to set.
     */
    public void setTaskList(List<ApplicationTask> taskList) {
        this.taskList = taskList;
    }
    
    public void setApplicationData(Application application)
    {
        setApplicationDescription(application.getApplicationDescription());
        setApplicationID(application.getApplicationId());
        setApplicationName(application.getApplicationName());
        setApplicationType(application.getApplicationTypeId());
        setInitializePage(false);
        setPreload(application.getPreload());
        setScreenClass(application.getScreenClass());
        setScreenID(application.getScreenId());
        setSessionRequired(application.getSessionRequired()== 1);
        setTerminalApplicationFlag(application.getTerminalApplicationFlag() == 1);
        setPersistentSession(application.getPersistentSession() == 1);
        setWindowTitle(application.getWindowTitle());
        
        setTaskList(application.getApplicationTasks());
    }
    
    /**
     * Returns the ApplicationData from the form. The task will be empty when processing
     * form input.
     * @return
     */
    public Application getApplicationData()
    {
        Application data = new Application();

        data.setPreload(getPreload());
        data.setScreenClass(getScreenClass());
        data.setScreenId(getScreenID());
        data.setSessionRequired((short)(isSessionRequired() ? 1:0));
        data.setTerminalApplicationFlag((short)(isTerminalApplicationFlag() ? 1:0));
        data.setPersistentSession((short)(isPersistentSession()? 1 : 0));
        data.setWindowTitle(getWindowTitle());
        
        data.setApplicationDescription(getApplicationDescription());
        data.setApplicationName(getApplicationName());
        data.setApplicationId(getApplicationID());
        
        data.setApplicationTypeId(getApplicationType());
        

        data.setApplicationTasks(getTaskList());
        
        return data;
        
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
     * @return Returns the persistentSession.
     */
    public boolean isPersistentSession() {
        return persistentSession;
    }
    /**
     * @param persistentSession The persistentSession to set.
     */
    public void setPersistentSession(boolean persistentSession) {
        this.persistentSession = persistentSession;
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

	/**
	 * @return Returns the broadcastDestinationList.
	 */
	public List getBroadcastDestinationList() {
	    if (broadcastDestinationList == null)
	    {
	        broadcastDestinationList = new ArrayList(0);
	    }
	            
	    return broadcastDestinationList;
	}

	/**
	 * @param broadcastDestinationList The broadcastDestinationList to set.
	 */
	public void setBroadcastDestinationList(List broadcastDestinationList) {
	    this.broadcastDestinationList = broadcastDestinationList;
	}

	/**
	 * @return Returns the activePage.
	 */
	public String getActivePage() {
	    if (activePage == null)
	    {
	        activePage = "";
	    }
	    return activePage;
	}

	/**
	 * @param activePage The activePage to set.
	 */
	public void setActivePage(String activePage) {
	    this.activePage = activePage;
	}
	
	public List<ApplicationType> getApplicationTypes() {
		return ApplicationType.getNonProcessPointApplicationTypes();
	}
}
