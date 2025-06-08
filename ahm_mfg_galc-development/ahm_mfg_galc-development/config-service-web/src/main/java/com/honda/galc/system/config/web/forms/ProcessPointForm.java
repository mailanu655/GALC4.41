package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.dto.ProcessPointGroupDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.ApplicationType;

/**
 * <H3>ProcessPointForm</H3> 
* <h3>Class description</h3>
* <h4>Description</h4>
* <P>This is the Struts form bean for the Process Point Configuration Page</P>
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
* <TD>Oct 3, 2007</TD>
* <TD></TD>
* <TD>@JM200731</TD>
* <TD>Support display of related device and opc information</TD>
* </TR>
* </TABLE>
*
 */
public class ProcessPointForm extends ActionForm

{
	
	private static final long serialVersionUID = 1L;

	/**
	 * NO_DEFINITION
	 */
	private String backfillProcessPointID;
	
	/**
	 * The name of the Division.
	 */
	private String divisionName1;
	
	/**
	 * The name of the Division.
	 */
	private String divisionName2;
	
	/**
	 * The ID of Line.
	 */
	private String lineID;
	
	/**
	 * The Line number.
	 */
	private String lineName;
	
	/**
	 * NO_DEFINITION
	 */
	private boolean passingCountFlag;
	
	/**
	 * A name of plant.
	 */
	private String plantName;
	
	
	/**
	 * This field is not set by the page, but is used to display 
	 * the current entry process point for the line.
	 */
	private String currentLineEntryPoint = "";
	
	/**
	 * The code of process point.(Key Field)
	 */
	private String processPointID;
	
	/**
	 * The name which shows a process point.definition of the process point is a datacollection point.
	 */
	private String processPointName;
	
	/**
	 * NO_DEFINITION
	 */
	private boolean recoveryPointFlag;
	
	/**
	 * Sequence Number.
	 */
	private int sequenceNo;
	
	/**
	 * The name of the Site
	 */
	private String siteName;
	
	/**
	 * A flag indicate this process point is tracking point or not.
	 */
	private boolean trackingPointFlag;
	
	/**
	 * The type of process point.
	 */
	private int processPointType;
	
	/**
	 * Type of the application associated with process point
	 */
	private int applicationType;
	
	/**
	 * The feature type. 
	 */
	private java.util.List featureTypeList = null;
	
	
	/** The feature id list. */
	private java.util.List featureIdList = null;
	
	
	/** The feature type. */
	private String featureType;
	
	
	/** The feature id. */
	private String featureId;
	
	/**
	 * The ID of division.
	 */
	private String divisionID;
	
	/**
	 * The name of the Division.
	 */
	private String divisionName;		
	
	/**
	 * The description of this process point.
	 */
	private String processPointDescription;
    
	
	/**
	 * The expected product flag.
	 */
	private boolean expectedProduct = false;

	// Application settings for the process point
	



    /**
     * The Name of Application.
     */
    private String applicationName;


    /**
     * @oim50
     * Implementation field for persistent attribute: preload
     */
    private int applicationPreload;

    /**
     * The class of screen.
     */
    private String applicationScreenClass;

    /**
     * The ID of Screen.
     */
    private String applicationScreenID;
    /**
     * @oim50
     * Implementation field for persistent attribute: sessionRequired
     */
    private boolean applicationSessionRequired;

    /**
     * The flag is indicated this application is terminal application or not .
     */
    private boolean applicationTerminalApplicationFlag;

    /**
     * The title of window.
     */
    private String applicationWindowTitle;

    private boolean applicationPersistentSession = false;
    
    /**
     * List of TaskByApplicationData objects
     */
    private List taskList  = null;
    
    /**
     * List of BroadcastDestinationData objects.
     */
    private List broadcastDestinationList = null;
    
    
    // @JM200731
    private List relatedDeviceList = null;
    
    // @JM200731
    private List relatedOPCList = null;
    
    /**
     * List of terminals that process point/application is associated with.
     */
    private List<Terminal> terminals;
    
    private List<ProcessPointGroupDto> processPointGroups;

    // Misc form fields
    private String apply = null;
    
    private String delete = null;
    
    private String deleteApplication = null;
    
    private boolean deleteConfirmed = false;
    
    private boolean deleteApplicationConfirmed = false;
    
    private String createFlag = null;
    
    private String activePage = null;
    
    private String missingGpcsDataMessage="Missing Division to GPCS relationship - needed to determine line, shift and Production Date.";
    
    /**
     * Flag that controls if user can edit
     */
    private boolean editor = false;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        // Reset field values here.
        
        processPointID = null;
        
        processPointName = null;
        
        processPointDescription = null;
        
        
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
     * @return Returns the applicationID.
     */
    public String getApplicationID() {
        return processPointID;
    }
    /**
     * @param applicationID The applicationID to set.
     */
    public void setApplicationID(String applicationID) {
        this.processPointID = applicationID;
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
     * @return Returns the applicationPreload.
     */
    public int getApplicationPreload() {
        return applicationPreload;
    }
    /**
     * @param applicationPreload The applicationPreload to set.
     */
    public void setApplicationPreload(int applicationPreload) {
        this.applicationPreload = applicationPreload;
    }
    /**
     * @return Returns the applicationScreenClass.
     */
    public String getApplicationScreenClass() {
        return applicationScreenClass;
    }
    /**
     * @param applicationScreenClass The applicationScreenClass to set.
     */
    public void setApplicationScreenClass(String applicationScreenClass) {
        this.applicationScreenClass = applicationScreenClass;
    }
    /**
     * @return Returns the applicationScreenID.
     */
    public String getApplicationScreenID() {
        return applicationScreenID;
    }
    /**
     * @param applicationScreenID The applicationScreenID to set.
     */
    public void setApplicationScreenID(String applicationScreenID) {
        this.applicationScreenID = applicationScreenID;
    }
    /**
     * @return Returns the applicationSessionRequired.
     */
    public boolean isApplicationSessionRequired() {
        return applicationSessionRequired;
    }
    /**
     * @param applicationSessionRequired The applicationSessionRequired to set.
     */
    public void setApplicationSessionRequired(boolean applicationSessionRequired) {
        this.applicationSessionRequired = applicationSessionRequired;
    }
    /**
     * @return Returns the applicationTerminalApplicationFlag.
     */
    public boolean isApplicationTerminalApplicationFlag() {
        return applicationTerminalApplicationFlag;
    }
    /**
     * @param applicationTerminalApplicationFlag The applicationTerminalApplicationFlag to set.
     */
    public void setApplicationTerminalApplicationFlag(boolean applicationTerminalApplicationFlag) {
        this.applicationTerminalApplicationFlag = applicationTerminalApplicationFlag;
    }
    /**
     * @return Returns the applicationWindowTitle.
     */
    public String getApplicationWindowTitle() {
        return applicationWindowTitle;
    }
    /**
     * @param applicationWindowTitle The applicationWindowTitle to set.
     */
    public void setApplicationWindowTitle(String applicationWindowTitle) {
        this.applicationWindowTitle = applicationWindowTitle;
    }
    /**
     * @return Returns the backfillProcessPointID.
     */
    public String getBackfillProcessPointID() {
        return backfillProcessPointID;
    }
    /**
     * @param backfillProcessPointID The backfillProcessPointID to set.
     */
    public void setBackfillProcessPointID(String backfillProcessPointID) {
        this.backfillProcessPointID = backfillProcessPointID;
    }
    /**
     * @return Returns the divisionID.
     */
    public String getDivisionID() {
        return divisionID;
    }
    /**
     * @param divisionID The divisionID to set.
     */
    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }
    /**
     * @return Returns the divisionName.
     */
    public String getDivisionName() {
        return divisionName;
    }
    /**
     * @param divisionName The divisionName to set.
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /**
     * @return Returns the divisionName1.
     */
    public String getDivisionName1() {
        return divisionName1;
    }
    /**
     * @param divisionName1 The divisionName1 to set.
     */
    public void setDivisionName1(String divisionName1) {
        this.divisionName1 = divisionName1;
    }
    /**
     * @return Returns the divisionName2.
     */
    public String getDivisionName2() {
        return divisionName2;
    }
    /**
     * @param divisionName2 The divisionName2 to set.
     */
    public void setDivisionName2(String divisionName2) {
        this.divisionName2 = divisionName2;
    }
    /**
     * @return Returns the lineID.
     */
    public String getLineID() {
        return lineID;
    }
    /**
     * @param lineID The lineID to set.
     */
    public void setLineID(String lineID) {
        this.lineID = lineID;
    }
    /**
     * @return Returns the lineName.
     */
    public String getLineName() {
        return lineName;
    }
    /**
     * @param lineName The lineName to set.
     */
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    /**
     * @return Returns the passingCountFlag.
     */
    public boolean isPassingCountFlag() {
        return passingCountFlag;
    }
    /**
     * @param passingCountFlag The passingCountFlag to set.
     */
    public void setPassingCountFlag(boolean passingCountFlag) {
        this.passingCountFlag = passingCountFlag;
    }
    /**
     * @return Returns the plantName.
     */
    public String getPlantName() {
        return plantName;
    }
    /**
     * @param plantName The plantName to set.
     */
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
    /**
     * @return Returns the processPointDescription.
     */
    public String getProcessPointDescription() {
        return processPointDescription;
    }
    /**
     * @param processPointDescription The processPointDescription to set.
     */
    public void setProcessPointDescription(String processPointDescription) {
        this.processPointDescription = processPointDescription;
    }
    /**
     * @return Returns the processPointID.
     */
    public String getProcessPointID() {
        if (processPointID == null)
        {
            processPointID = "";
        }
        return processPointID;
    }
    /**
     * @param processPointID The processPointID to set.
     */
    public void setProcessPointID(String processPointID) {
        this.processPointID = processPointID;
    }
    /**
     * @return Returns the processPointName.
     */
    public String getProcessPointName() {
        return processPointName;
    }
    /**
     * @param processPointName The processPointName to set.
     */
    public void setProcessPointName(String processPointName) {
        this.processPointName = processPointName;
    }
    /**
     * @return Returns the processPointType.
     */
    public int getProcessPointType() {
        return processPointType;
    }
    /**
     * @param processPointType The processPointType to set.
     */
    public void setProcessPointType(int processPointType) {
        this.processPointType = processPointType;
    }
    
    /**
     * Gets the feature type.
     *
     * @return the feature type
     */
    public java.util.List getFeatureTypeList() {
    	if (featureTypeList == null) {
    		featureTypeList = new java.util.ArrayList(0);
		}
		return featureTypeList;
	}

	/**
	 * Sets the feature type.
	 *
	 * @param featureType the new feature type
	 */
	public void setFeatureTypeList(java.util.List featureTypeList) {
		this.featureTypeList = featureTypeList;
	}
	
	/**
	 * Gets the feature id list.
	 *
	 * @return the feature id list
	 */
	public java.util.List getFeatureIdList() {
		return featureIdList;
	}

	/**
	 * Sets the feature id list.
	 *
	 * @param featureIdList the new feature id list
	 */
	public void setFeatureIdList(java.util.List featureIdList) {
		this.featureIdList = featureIdList;
	}

	/**
	 * Gets the selected feature type.
	 *
	 * @return the selected feature type
	 */
	public String getFeatureType() {
		return featureType;
	}

	/**
	 * Sets the selected feature type.
	 *
	 * @param selectedFeatureType the new selected feature type
	 */
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	/**
	 * Gets the select feature id.
	 *
	 * @return the select feature id
	 */
	public String getFeatureId() {
		return featureId;
	}

	/**
	 * Sets the select feature id.
	 *
	 * @param selectFeatureId the new select feature id
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	} 
    
    /**
     * @return Returns the recoveryPointFlag.
     */
    public boolean isRecoveryPointFlag() {
        return recoveryPointFlag;
    }
    /**
     * @param recoveryPointFlag The recoveryPointFlag to set.
     */
    public void setRecoveryPointFlag(boolean recoveryPointFlag) {
        this.recoveryPointFlag = recoveryPointFlag;
    }
    /**
     * @return Returns the sequenceNo.
     */
    public int getSequenceNo() {
        return sequenceNo;
    }
    /**
     * @param sequenceNo The sequenceNo to set.
     */
    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
    /**
     * @return Returns the siteName.
     */
    public String getSiteName() {
        return siteName;
    }
    /**
     * @param siteName The siteName to set.
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    /**
     * @return Returns the trackingPointFlag.
     */
    public boolean isTrackingPointFlag() {
        return trackingPointFlag;
    }
    /**
     * @param trackingPointFlag The trackingPointFlag to set.
     */
    public void setTrackingPointFlag(boolean trackingPointFlag) {
        this.trackingPointFlag = trackingPointFlag;
    }
    
    /**
     * Utility method for setting fieds from process point data flag.
     * @param data
     */
    public void setProcessPointData(ProcessPoint data)
    {
        setApplicationID(data.getProcessPointId());
        setApplicationName(data.getProcessPointName());
        setBackfillProcessPointID(data.getBackFillProcessPointId());
        setDivisionID(data.getDivisionId());
        setDivisionName(data.getDivisionName());
        setLineID(data.getLineId());
        setLineName(data.getLineName());
        setPassingCountFlag(data.getPassingCountFlag() == 1);
        setPlantName(data.getPlantName());
        setProcessPointDescription(data.getProcessPointDescription());
        setProcessPointID(data.getProcessPointId());
        setProcessPointType(data.getProcessPointTypeId());
        setProcessPointName(data.getProcessPointName());
        setRecoveryPointFlag(data.getRecoveryPointFlag() == 1);
        setSequenceNo(data.getSequenceNumber());
        setSiteName(data.getSiteName());
        setTrackingPointFlag(data.getTrackingPointFlag() == 1);
        setFeatureType(data.getFeatureType());
        setFeatureId(data.getFeatureId());
        
//        setExpectedProduct(data.isExpectedProduct());
    }
    
    public void setApplicationData(Application data)
    {
        setApplicationPreload(data.getPreload());
        setApplicationScreenClass(data.getScreenClass());
        setApplicationScreenID(data.getScreenId());
        setApplicationSessionRequired(data.getSessionRequired() == 1);
        setApplicationTerminalApplicationFlag(data.getTerminalApplicationFlag() == 1);
        setApplicationPersistentSession(data.getPersistentSession() == 1);
        setApplicationWindowTitle(data.getWindowTitle());
        setApplicationType(data.getApplicationTypeId());
        setTaskList(data.getApplicationTasks());
    }

    /**
     * Returns the ApplicationData from the form. The task will be empty when processing
     * form input.
     * @return
     */
    public Application getApplicationData()
    {
        Application data = new Application();

        data.setPreload(getApplicationPreload());
        data.setScreenClass(getApplicationScreenClass());
        data.setScreenId(getApplicationScreenID());
        data.setSessionRequired((short) (isApplicationSessionRequired() ? 1 : 0));
        data.setTerminalApplicationFlag((short)(isApplicationTerminalApplicationFlag()? 1:0));
        data.setPersistentSession((short)(isApplicationPersistentSession()? 1:0));
        data.setWindowTitle(getApplicationWindowTitle());
        
        data.setApplicationDescription(getProcessPointDescription());
        data.setApplicationName(getProcessPointName());
        data.setApplicationId(getProcessPointID());
        data.setApplicationTypeId(getApplicationType());

        data.setApplicationTasks(getTaskList());
        
        return data;
        
    }

    public ProcessPoint getProcessPointData()
    {
        ProcessPoint data = new ProcessPoint();

        data.setBackFillProcessPointId(getBackfillProcessPointID());
        data.setDivisionId(getDivisionID());
        data.setDivisionName(getDivisionName());
        data.setLineId(getLineID());
        data.setLineName(getLineName());
        data.setPassingCountFlag((short)(isPassingCountFlag() ? 1 : 0));
        data.setPlantName(getPlantName());
        data.setProcessPointDescription(getProcessPointDescription());
        data.setProcessPointId(getProcessPointID());
        data.setProcessPointName(getProcessPointName());
        data.setProcessPointTypeId(getProcessPointType());
        data.setRecoveryPointFlag((short)(isRecoveryPointFlag()? 1: 0));
        data.setSequenceNumber(getSequenceNo());
        data.setSiteName(getSiteName());
        data.setTrackingPointFlag((short)(isTrackingPointFlag()? 1:0));
        data.setFeatureType(getFeatureType().equals("NONE") ? null : getFeatureType());
        data.setFeatureId(getFeatureId().equals("NONE") ? null : getFeatureId());
        
//        data.setExpectedProduct(isExpectedProduct());
        
        return data;
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
    
    /**
     * @return Returns the applicationPersistentSession.
     */
    public boolean isApplicationPersistentSession() {
        return applicationPersistentSession;
    }
    
    /**
     * @param applicationPersistentSession The applicationPersistentSession to set.
     */
    public void setApplicationPersistentSession(boolean applicationPersistentSession) {
        this.applicationPersistentSession = applicationPersistentSession;
    }
    
    /**
     * @return Returns the expectedProduct.
     */
    public boolean isExpectedProduct() {
        return expectedProduct;
    }
    /**
     * @param expectedProduct The expectedProduct to set.
     */
    public void setExpectedProduct(boolean expectedProduct) {
        this.expectedProduct = expectedProduct;
    }
    /**
     * @return Returns the taskList.
     */
    public List getTaskList() {
        
        if (taskList == null)
        {
            taskList = new ArrayList(0);
        }
        return taskList;
    }
    
    /**
     * @param taskList The taskList to set.
     */
    public void setTaskList(List taskList) {
        this.taskList = taskList;
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
     * @return Returns the deleteApplication.
     */
    public String getDeleteApplication() {
        return deleteApplication;
    }
    /**
     * @param delete The delete to set.
     */
    public void setDeleteApplication(String deleteApplication) {
        this.deleteApplication = deleteApplication;
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
     * @return Returns the deleteApplicationConfirmed.
     */
    public boolean isDeleteApplicationConfirmed() {
        return deleteApplicationConfirmed;
    }
    /**
     * @param deleteConfirmed The deleteConfirmed to set.
     */
    public void setDeleteApplicationConfirmed(boolean deleteApplicationConfirmed) {
        this.deleteApplicationConfirmed = deleteApplicationConfirmed;
    }
    /**
     * @return Returns the currentLineEntryPoint.
     */
    public String getCurrentLineEntryPoint() {
        if (currentLineEntryPoint == null)
        {
            currentLineEntryPoint = "";
        }
        return currentLineEntryPoint;
    }
    /**
     * @param currentLineEntryPoint The currentLineEntryPoint to set.
     */
    public void setCurrentLineEntryPoint(String currentLineEntryPoint) {
        this.currentLineEntryPoint = currentLineEntryPoint;
    }

	public List getRelatedDeviceList() {
		return relatedDeviceList;
	}

	public void setRelatedDeviceList(List relatedDeviceList) {
		this.relatedDeviceList = relatedDeviceList;
	}

	public List getRelatedOPCList() {
		return relatedOPCList;
	}

	public void setRelatedOPCList(List relatedOPCList) {
		this.relatedOPCList = relatedOPCList;
	}
	
	public void setLineData(Line line) {
		this.setPlantName(line.getPlantName());
		this.setDivisionID(line.getDivisionId());
		this.setDivisionName(line.getDivisionName());
		this.setSiteName(line.getSiteName());
		this.setLineName(line.getLineName());
	}
	
	public int getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(int applicationType) {
		this.applicationType = applicationType;
	}
	
    /**
     * Returns application types 
     * @return
     */
	public List<ApplicationType> getApplicationTypes() {
		return ApplicationType.getProcessPointApplicationTypes();
	}

	public List<Terminal> getTerminals() {
		return terminals;
	}

	public void setTerminals(List<Terminal> terminals) {
		this.terminals = terminals;
	}

	public List<ProcessPointGroupDto> getProcessPointGroups() {
		return processPointGroups;
	}

	public void setProcessPointGroups(List<ProcessPointGroupDto> processPointGroups) {
		this.processPointGroups = processPointGroups;
	}
	public String getMissingGpcsDataMessage() {
		return missingGpcsDataMessage;
	}

	public void setMissingGpcsDataMessage(String missingGpcsDataMessage) {
		this.missingGpcsDataMessage = missingGpcsDataMessage;
	}
}
