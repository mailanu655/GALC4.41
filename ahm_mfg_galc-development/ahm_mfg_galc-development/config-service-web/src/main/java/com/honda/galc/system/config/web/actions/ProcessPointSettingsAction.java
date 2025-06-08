package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ProcessPointGroupDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.FeatureDao;
import com.honda.galc.dao.product.FeatureTypeDao;
import com.honda.galc.dto.ProcessPointGroupDto;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.BroadcastDestinationId;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.ConditionType;
import com.honda.galc.entity.product.Feature;
import com.honda.galc.entity.product.FeatureType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.forms.ProcessPointForm;
import com.honda.galc.system.config.web.utilities.TaskListParameterParser;

/**
 * <h3>ProcessPointSettingsAction</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This Struts action class handles the process of input from the CompleteProcessPoint jsp
 * page.
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
 * <TD>Feb 13, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Oct 3, 2007</TD>
 * <TD></TD>
 * <TD>@JM200731</TD>
 * <TD>Support device and opc related info</TD>
 * </TR>
 * </TABLE>
 *
 */
public class ProcessPointSettingsAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "processPointErrors";
    
    private static final String MESSAGES_GROUP = "updateProcessPointMessages";
    

    
    /**
     * Constructor
     */
    public ProcessPointSettingsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ActionForward forward = new ActionForward(); // return value
        ProcessPointForm processPointForm = (ProcessPointForm) form;
        
        boolean create = false;
        boolean apply = false;
        boolean delete = false;
        boolean deleteApplication = false;
        boolean refreshForm = true;
        
        List<ApplicationTask> inputTaskList = null;
        List<BroadcastDestination> inputBroadcastDestinationList = null;
        
        boolean goToMessages = false;

        try {
            
            if (request.isUserInRole("EditProcessPoint"))
            {
                processPointForm.setEditor(true);
            }

            if (processPointForm.getCreateFlag() != null &&
                processPointForm.getCreateFlag().equalsIgnoreCase("true"))
            {
                create = true;
            }
            
            if (processPointForm.getApply() != null &&
                processPointForm.getApply().equalsIgnoreCase("apply"))
            {
                apply = true;        
                // Apply can only be pressed if editor
                if (!processPointForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
            }
            
            if (processPointForm.getDelete() != null &&
                processPointForm.getDelete().equalsIgnoreCase("delete"))
            {
                delete = true;
                // Delete can only be pressed if editor
                if (!processPointForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
            }
            
            if (processPointForm.getDeleteApplication() != null &&
                processPointForm.getDeleteApplication().equalsIgnoreCase("delete"))
            {
                deleteApplication = true;
                // Delete can only be pressed if editor
                if (!processPointForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
            }
            
            {
            	String applicationId = processPointForm.getApplicationID();
            	if (applicationId == null) {
            		request.setAttribute("existingapplicationflag", "false");
            	} else {
            		Application existingApplication = getDao(ApplicationDao.class).findByKey(applicationId);
            		if (existingApplication == null) {
            			request.setAttribute("existingapplicationflag", "false");
            		}
            	}
            }
            
            if (create)
            {
                // We are creating a new process point but still require
                // additional input
            	processPointForm.setFeatureTypeList(getFeatureTypes());
                processPointForm.setCreateFlag("true");
                request.setAttribute("existingflag", "false");
                // Create the process point
                if (apply) {
                    if (processPointForm.getProcessPointID() == null ||
                        processPointForm.getProcessPointID().length() == 0)
                    {
                    	processPointForm.setMissingGpcsDataMessage("");
                        throw new ConfigurationServicesException("CFGW0062");
                    } else if (isProcessPointExists(processPointForm.getProcessPointID())) {
                    	refreshForm = false;
                    	throw new ConfigurationServicesException("CFGW0063"); 
                    } else if (processPointForm.getProcessPointName() == null ||
                            processPointForm.getProcessPointName().length() == 0)
                    {
                    	refreshForm = false;
                		throw new ConfigurationServicesException("CFGW0017");
                    } else {
                    	Line lineObj = ServiceFactory.getDao(LineDao.class).findByKey(processPointForm.getLineID());
                    	processPointForm.setLineData(lineObj);
                    	processPointForm.setFeatureTypeList(getFeatureTypes());
                    	//getFeatureIds
                    	processPointForm.setFeatureIdList(getFeatureIds(processPointForm.getFeatureType()));
                    	
                    	// Pull the process point 
                        ProcessPoint data = processPointForm.getProcessPointData();
                        
                        ServiceFactory.getDao(ProcessPointDao.class).insert(data);
                        
                        List<Object[]> gpcsRecords = getDao(GpcsDivisionDao.class).findProcessPointInfoAndGpcsData(processPointForm.getProcessPointID());
                        
                        if(gpcsRecords!=null && gpcsRecords.size()>0) {
                        	processPointForm.setMissingGpcsDataMessage("");
                        }
                        else {
                        	processPointForm.setMissingGpcsDataMessage("Missing Division to GPCS relationship - needed to determine line, shift and Production Date.");
                        }
                        
                        // We have successfully created a new process point
                        messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0020", data.getLineId(), data.getProcessPointId()));
                        processPointForm.setCreateFlag("false");
                        request.setAttribute("existingflag", "true");
                    }
                }
            }
            else if (apply)
            {
                // We are showing an existing process point and processing changes
                processPointForm.setCreateFlag("false");
                request.setAttribute("existingflag", "true");
                
                // Determine which "tab" is active
                if (processPointForm.getActivePage().equalsIgnoreCase("ProcessPoint"))
                {
                	if (processPointForm.getProcessPointName() == null ||
                        processPointForm.getProcessPointName().length() == 0)
                    {
                        throw new ConfigurationServicesException("CFGW0017");
                    } else {
	                    ProcessPoint data = processPointForm.getProcessPointData();
	                    ServiceFactory.getDao(ProcessPointDao.class).update(data);
	                    Application app = ServiceFactory.getDao(ApplicationDao.class).findByKey(data.getProcessPointId());
	                    if (app != null) {
	                    	app.setApplicationName(data.getProcessPointName());
	                    	ServiceFactory.getDao(ApplicationDao.class).update(app);
	                    }
	                    
                        List<Object[]> gpcsRecords = getDao(GpcsDivisionDao.class).findProcessPointInfoAndGpcsData(processPointForm.getProcessPointID());
                        
                        if(gpcsRecords!=null && gpcsRecords.size()>0) {
                       	 processPointForm.setMissingGpcsDataMessage("");
                        }
	                    
	                    // Obtain the complete process point information
	                    // and load it into the form
	                    refreshFormData(processPointForm);
	                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0018"));
                    }
                }
                else  if (processPointForm.getActivePage().equalsIgnoreCase("Application"))
                {
                    
                	// We are updating the application definition
                    Application data = processPointForm.getApplicationData();
                    
                    TaskListParameterParser taskListParser = new TaskListParameterParser();
                    
                    List<ApplicationTask> taskList = taskListParser.buildTaskByApplicationList(processPointForm.getProcessPointID(), request);
                    
                    
                    // Store the task list for error processing
                    inputTaskList = data.getApplicationTasks();
                    
					// Determine if we are creating the application or updating it
                    // A process point can be defined without corresponding application def
 
					getDao(ApplicationDao.class).update(data);
					Application appData = getDao(ApplicationDao.class).findByKey(processPointForm.getApplicationID());
					List<ApplicationTask> removedList = new ArrayList<ApplicationTask>();
					
					for(ApplicationTask task : appData.getApplicationTasks()) {
						if(!contains(task,taskList)) removedList.add(task);
					}
					
					if(!removedList.isEmpty())getDao(ApplicationTaskDao.class).removeAll(removedList);
					
					if(taskList != null && !taskList.isEmpty()) getDao(ApplicationTaskDao.class).saveAll(taskList);
					// Determine if we are creating the application or updating it
                    // A process point can be defined without corresponding application def
                    
                    refreshFormData(processPointForm);
                    
                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0019"));
                    request.setAttribute("existingapplicationflag", "true");
                }
                else if (processPointForm.getActivePage().equalsIgnoreCase("Broadcast"))
                {
                    // Build a broadcast destination data list
                    String processPointID = processPointForm.getProcessPointID();
                    
                    List<BroadcastDestination> broadcastDestinationList = new ArrayList<BroadcastDestination>(10);
                    
                    int idx = 1;
                    do {

                        
                        String destinationTypeArg = request.getParameter("bddestinationType"+idx);
                        String destinationID = request.getParameter("bddestinationID"+idx);
                        String bdrequestID = request.getParameter("bdrequestID"+idx);
                        String bdargument = request.getParameter("bdargument"+idx);
                        String bdAutoEnabled = request.getParameter("bdAutoEnabled"+idx);
                        String checkPoint = request.getParameter("bdCheckPoint"+idx);
                        String condition = request.getParameter("bdCondition"+idx);
                        String conditionType = request.getParameter("bdConditionType"+idx);
                        
                        if (destinationTypeArg == null || destinationTypeArg.length() == 0)
                        {
                            break;
                        }
                        
                        int destinationType = Integer.parseInt(destinationTypeArg);
                        
                        BroadcastDestination destinationData = new BroadcastDestination();
                        destinationData.setId(new BroadcastDestinationId());
                        
                        destinationData.getId().setProcessPointId(processPointID);
                        destinationData.getId().setSequenceNumber(idx);
                        destinationData.setArgument(bdargument);
                        destinationData.setDestinationId(destinationID);
                        destinationData.setRequestId(bdrequestID);
                        destinationData.setDestinationTypeId(destinationType);
                        destinationData.setAutoEnabled(Boolean.valueOf(bdAutoEnabled));
                        if (StringUtils.isNotBlank(checkPoint)) {
                        	destinationData.setCheckPoint(CheckPoints.valueOf(checkPoint));
                        }
                        if (StringUtils.isNotBlank(condition)) {
                        	destinationData.setCondition(condition);
                            if (StringUtils.isNotBlank(conditionType)) {
                           	 destinationData.setConditionType(ConditionType.valueOf(conditionType)); 	
                           } 
                        }

                        broadcastDestinationList.add(destinationData);
                        
                        idx++;
                    } while (true);
                    
                    // Store the destination list for error processing
                    inputBroadcastDestinationList = broadcastDestinationList;
                    
                    List<BroadcastDestination> originalList = getDao(BroadcastDestinationDao.class).findAllByProcessPointId(processPointID);
                    
                    List<BroadcastDestination> removedList = new ArrayList<BroadcastDestination>();
                    
                    for(BroadcastDestination destination : originalList) {
                    	if(!contains(destination,broadcastDestinationList)) removedList.add(destination);
                    }
                    if(!removedList.isEmpty()) getDao(BroadcastDestinationDao.class).removeAll(removedList);
                    getDao(BroadcastDestinationDao.class).saveAll(broadcastDestinationList);
                    
                    // Add result message
                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0024",processPointID));
                    
                    
                    // Obtain the process point information
                    // and load it into the form
                    refreshFormData(processPointForm);
                }
                
            }
            else if (delete)
            {
                String processPointID = processPointForm.getProcessPointID();
                int applicationCount; {
                	Application application = ServiceFactory.getDao(ApplicationDao.class).findByKey(processPointID);
                	applicationCount = application == null ? 0 : 1;
                }
                int broadcastDestinationCount; {
                	List<BroadcastDestination> broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(processPointID);
                	broadcastDestinationCount = broadcastDestinations == null ? 0 : broadcastDestinations.size();
                }
                int componentPropertyCount; {
                	List<ComponentProperty> componentProperties = ServiceFactory.getDao(ComponentPropertyDao.class).findAllByComponentId(processPointID);
                	componentPropertyCount = componentProperties == null ? 0 : componentProperties.size();
                }
                int lotControlRuleCount; {
                	List<LotControlRule> lotControlRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(processPointID);
                	lotControlRuleCount = lotControlRules == null ? 0 : lotControlRules.size();
                }
                if (applicationCount > 0 || broadcastDestinationCount > 0 || componentPropertyCount > 0 || lotControlRuleCount > 0) {
                	refreshForm = false;
                	StringBuilder errorBuilder = new StringBuilder();
                	errorBuilder.append("Cannot delete process point ");
                	errorBuilder.append(processPointID);
                	errorBuilder.append(" due to existing records: ");

                	StringBuilder countBuilder = new StringBuilder();
                	appendCount(countBuilder, "Application", applicationCount);
                	appendCount(countBuilder, "BroadcastDestination", broadcastDestinationCount);
                	appendCount(countBuilder, "ComponentProperty", componentPropertyCount);
                	appendCount(countBuilder, "LotControlRule", lotControlRuleCount);
                	errorBuilder.append(countBuilder.toString());
                	throw new SystemException(errorBuilder.toString());
                }
                ServiceFactory.getDao(ProcessPointDao.class).removeByKey(processPointID);
                // Add result message
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0028",processPointID));
                // Clear the form data
                processPointForm.reset(mapping,request);
                request.setAttribute("existingflag", "false");
                goToMessages = true;
            }
            else if (deleteApplication)
            {
                if (!processPointForm.isDeleteApplicationConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                getDao(ApplicationDao.class).removeByKey(processPointForm.getApplicationID());
                
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0025",processPointForm.getApplicationID()));
                request.setAttribute("existingapplicationflag", "false");
                
                refreshFormData(processPointForm);
            }
            else
            {
                // Obtain the process point information
                // and load it into the form
            	
                List<Object[]> gpcsRecords = getDao(GpcsDivisionDao.class).findProcessPointInfoAndGpcsData(processPointForm.getProcessPointID());
                
                if(gpcsRecords!=null && gpcsRecords.size()>0) {
                	processPointForm.setMissingGpcsDataMessage("");
                } 
                refreshFormData(processPointForm);
                
                // We are showing an exisiting process point
                processPointForm.setCreateFlag("false");
                request.setAttribute("existingflag", "true");
            }

        } 
         catch (ConfigurationServicesException e)
		{
            ActionError actionError = null;
            if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0)
            {
               actionError = new ActionError(e.getAdditionalInformation(), e.toString());
            } 
            else
            {
               actionError = new ActionError("CFGW0005", e.toString());
            }
            
            logActionError(actionError);
     	    errors.add(ERRORS_GROUP, actionError);
            
     	    if(!create || refreshForm) {
         	    try { refreshFormData(processPointForm); } catch (Exception e2) {}
     	    }
        	
        	if (inputBroadcastDestinationList != null)
        	{
        	    processPointForm.setBroadcastDestinationList(inputBroadcastDestinationList);
        	}
        	
        	if (inputTaskList != null)
        	{
        	    processPointForm.setTaskList(inputTaskList);
        	}            
		}
        catch (SystemException e)
 		{
         	errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
         	
         	try { refreshFormData(processPointForm); } catch (Exception e2) {}
         	
         	if (inputBroadcastDestinationList != null)
         	{
         	    processPointForm.setBroadcastDestinationList(inputBroadcastDestinationList);
         	}
         	
         	if (inputTaskList != null)
         	{
         	    processPointForm.setTaskList(inputTaskList);
         	}
 		}
       catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));

        }  

        if (!messages.isEmpty())
        {
        	saveMessages(request,messages);
        }
        
        // If a message is required, save the specified key(s)
        // into the request for use by the <struts:errors> tag.
        
        if (goToMessages) {
            
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            }
            
            forward = mapping.findForward("messages");
        }
        else if (!errors.isEmpty()) {
            saveErrors(request, errors);

            // Forward control to the appropriate 'failure' URI (change name as desired)
            forward = mapping.findForward("failure");

        } else {

            // Forward control to the appropriate 'success' URI (change name as desired)
            forward = mapping.findForward("success");

        }

        // Finish with
        return (forward);

    }
    
    private boolean contains(ApplicationTask item, List<ApplicationTask> items) {
    	for(ApplicationTask eachItem: items) {
    		if(eachItem.getId().equals(item.getId())) return true;
    	}
    	return false;
    }
    
    private boolean contains(BroadcastDestination item, List<BroadcastDestination> items) {
    	
    	for(BroadcastDestination eachItem : items) {
    		if(eachItem.getId().equals(item.getId())) return true;
    	}
    	return false;
    }

    /**
     * This utility method is used to update the form with the persistent
     * data.
     * @param processPointForm
     * @throws SystemException
     * @throws ConfigurationServicesException
     */
    private void refreshFormData(ProcessPointForm processPointForm)
       throws SystemException, ConfigurationServicesException
    {
//      Obtain the process point information
        ProcessPoint data = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointForm.getProcessPointID());
        
        Application appData = ServiceFactory.getDao(ApplicationDao.class).findByKey(processPointForm.getProcessPointID());
        if(appData == null) appData = new Application();
        appData.setApplicationId(processPointForm.getProcessPointID());

        List<BroadcastDestination> broadcastDestinationList = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(data.getProcessPointId());
        processPointForm.setFeatureTypeList(getFeatureTypes());
        processPointForm.setFeatureIdList(getFeatureIds(data.getFeatureType()));
        processPointForm.setProcessPointData(data);
        processPointForm.setApplicationData(appData);
        processPointForm.setBroadcastDestinationList(broadcastDestinationList);
        
//      Obtain the line data so we can have a check on the current entry point if tracking is enabled
        Line line = ServiceFactory.getDao(LineDao.class).findByKey(processPointForm.getLineID());
        
        // Update the form with the entry process point
        processPointForm.setCurrentLineEntryPoint(line.getEntryProcessPointId());        
        
        // @JM200731
        // Get the related device information
        try {
        	List<Device> deviceList = ServiceFactory.getDao(DeviceDao.class).findAllByProcessPointId(processPointForm.getProcessPointID());
        	List<OpcConfigEntry> opcList = ServiceFactory.getDao(OpcConfigEntryDao.class).findAllByProcessPointId(processPointForm.getProcessPointID());
        	List<Terminal> terminals = ServiceFactory.getDao(TerminalDao.class).findAllByApplicationId(processPointForm.getProcessPointID());
        	List<ProcessPointGroupDto> groups = ServiceFactory.getDao(ProcessPointGroupDao.class)
        										.findAllGroupsByProcessPointId(processPointForm.getProcessPointID());

        	processPointForm.setRelatedDeviceList(deviceList);
        	processPointForm.setRelatedOPCList(opcList);
        	processPointForm.setTerminals(terminals);
        	processPointForm.setProcessPointGroups(groups);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
    }
    
    private List<String> getFeatureTypes() {
    	List<FeatureType> featureTypes = getDao(FeatureTypeDao.class).findAll();
    	List<String> featureTypes1 = new ArrayList<String>();
    	for(FeatureType feature : featureTypes) {
    		featureTypes1.add(feature.getFeatureType());
    	}
    	return featureTypes1;
    }
    
    private List<String> getFeatureIds(String featureType){
    	List<Feature> featureIds= getDao(FeatureDao.class).findAllFeatures(featureType);
    	List<String> featureIdList = new ArrayList<String>();
    	for(Feature feature : featureIds) {
    		featureIdList.add(feature.getFeatureId());
    	}
    	return featureIdList;
    }
    
    private boolean isProcessPointExists(String processPointId) {
    	if(StringUtils.isEmpty(processPointId)) {
    		return false;
    	}
    	ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
    	return processPoint != null;
    }

    private void appendCount(StringBuilder countBuilder, String entityName, int count) {
    	if (count <= 0) {
    		return;
    	}
    	if (countBuilder.length() > 0) {
			countBuilder.append(", ");
		}
    	countBuilder.append(entityName);
		countBuilder.append(" (");
		countBuilder.append(count);
		countBuilder.append(")");
    }
     
}
