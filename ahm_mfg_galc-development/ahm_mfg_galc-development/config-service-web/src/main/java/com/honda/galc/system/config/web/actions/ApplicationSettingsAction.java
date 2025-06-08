package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.config.web.forms.ApplicationForm;
import com.honda.galc.system.config.web.utilities.TaskListParameterParser;

/**
 * @version 	1.0
 * @author
 */
public class ApplicationSettingsAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "applicationErrors";
    
    private static final String MESSAGES_GROUP = "applicationMessages";

	/**
     * Constructor
     */
    public ApplicationSettingsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ActionForward forward = new ActionForward(); // return value
        ApplicationForm applicationForm = (ApplicationForm) form;
        
        String showWarningColor = PropertyService.getProperty("System_Info", "DISPLAY_PRODUCTION_WARNING_COLOR", "background-color:transparent");	
        request.setAttribute("DISPLAY_PRODUCTION_WARNING_COLOR", showWarningColor);
        
        if (request.isUserInRole("EditApplication"))
        {
            applicationForm.setEditor(true);
        }
        
        boolean isApply = false;
        boolean isDelete = false;
        
        if (applicationForm.getApply() != null && 
            applicationForm.getApply().equalsIgnoreCase("apply"))
        {
            isApply = true;
        }
        else if (applicationForm.getDelete() != null && 
                 applicationForm.getDelete().equalsIgnoreCase("delete"))
        {
            isDelete = true;
        }
                 
        
        

        List<ApplicationTask> taskList = null;
        List<BroadcastDestination> inputBroadcastDestinationList = null;
        
        boolean goToMessages = false;
        
        try {

            
            if (applicationForm.isInitializePage())
            {
                // Do nothing
                return mapping.findForward("success");
            }
            
            if (isApply)
            {
                // confirm they have permissions for task
                if (!applicationForm.isEditor()) {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                
                
				if (applicationForm.getActivePage().equalsIgnoreCase("Application")) {
					TaskListParameterParser parser = new TaskListParameterParser();
					taskList = parser.buildTaskByApplicationList(applicationForm.getApplicationID(), request);
					Application data = applicationForm.getApplicationData();
					if (applicationForm.isCreateFlag()) {
						// We are creating a new application
					    getDao(ApplicationDao.class).insert(data);
					    getDao(ApplicationTaskDao.class).saveAll(taskList);
					    
						messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0027", applicationForm.getApplicationID()));
					} else {
						// We are updating a new application
						getDao(ApplicationDao.class).update(data);
						Application appData = getDao(ApplicationDao.class).findByKey(applicationForm.getApplicationID());
						List<ApplicationTask> removedList = new ArrayList<ApplicationTask>();
						
						for(ApplicationTask task : appData.getApplicationTasks()) {
							if(!contains(task,taskList)) removedList.add(task);
						}
						
						if(!removedList.isEmpty())getDao(ApplicationTaskDao.class).removeAll(removedList);
						
						if(taskList != null && !taskList.isEmpty()) getDao(ApplicationTaskDao.class).saveAll(taskList);
						
					
						messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0026", applicationForm.getApplicationID()));
					}
				} else if  (applicationForm.getActivePage().equalsIgnoreCase("Broadcast")) {
					
                    // Build a broadcast destination data list
                    String processPointID = applicationForm.getApplicationID();
                    
                    List<BroadcastDestination> broadcastDestinationList = new ArrayList<BroadcastDestination>();
                    
                    int idx = 1;
                    do {

                        
                        String destinationTypeArg = request.getParameter("bddestinationType"+idx);
                        String destinationID = request.getParameter("bddestinationID"+idx);
                        String bdrequestID = request.getParameter("bdrequestID"+idx);
                        String bdargument = request.getParameter("bdargument"+idx);
                        String bdAutoEnabled = request.getParameter("bdAutoEnabled"+idx);
                        
                        if (destinationTypeArg == null || destinationTypeArg.length() == 0)
                        {
                            break;
                        }
                        
                        int destinationType = Integer.parseInt(destinationTypeArg);
                        
                        BroadcastDestination destinationData = new BroadcastDestination(processPointID,idx);
                        destinationData.setArgument(bdargument);
                        destinationData.setDestinationId(destinationID);
                        destinationData.setRequestId(bdrequestID);
                        destinationData.setDestinationTypeId(destinationType);
                        destinationData.setAutoEnabled(Boolean.valueOf(bdAutoEnabled));
                        
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
                    refreshFormData(applicationForm);
                }
             
				applicationForm.setCreateFlag(false);
            }
            else if (isDelete)
            {
                if (!applicationForm.isDeleteConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                getDao(ApplicationDao.class).removeByKey(applicationForm.getApplicationID());
                
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0025",applicationForm.getApplicationID()));
                
                applicationForm.reset(mapping,request);
                
                goToMessages = true;
            }
  
            // Update the form with the application information
            if (applicationForm.getApplicationID() != null &&
                applicationForm.getApplicationID().length() > 0)
            {
                refreshFormData(applicationForm);
            }
            

        } 
       
        catch (ConfigurationServicesException e)
		{
            ActionError actionError = null;
            if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0)
            {
               actionError = new ActionError("Test",e.toString());
            } 
            else
            {
               actionError = new ActionError("Test",e.toString());
            }
            
            logActionError(actionError);
     	    errors.add(ERRORS_GROUP, actionError);

        	// Restore task list to form for error display
        	if (taskList != null)
        	{
        	    applicationForm.setTaskList(taskList);
        	}
        	
        	if (inputBroadcastDestinationList != null)
        	{
        		applicationForm.setBroadcastDestinationList(inputBroadcastDestinationList);
        	}
     	    
           
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
        	
        	// Restore task list to form for error display
        	if (taskList != null)
        	{
        	    applicationForm.setTaskList(taskList);
        	}
        	
        	if (inputBroadcastDestinationList != null)
        	{
        		applicationForm.setBroadcastDestinationList(inputBroadcastDestinationList);
        	}
        	
        	
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
            
        	// Restore task list to form for error display
        	if (taskList != null && applicationForm != null)
        	{
        	    applicationForm.setTaskList(taskList);
        	}
            

        }  

        if (!messages.isEmpty())
        {
        	saveMessages(request,messages);
        }
        
        
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
	    private void refreshFormData(ApplicationForm processPointForm)
	       throws SystemException, ConfigurationServicesException
	    {
	//      Obtain the process point information
	        Application appData = null;
        	appData = getDao(ApplicationDao.class).findByKey(processPointForm.getApplicationID());

        	if(appData == null){
        	// The application component may not be defined
	            appData = new Application();
	            appData.setApplicationId(processPointForm.getApplicationID());
	        }
	        
	        java.util.List broadcastDestinationList = null;
	            
            broadcastDestinationList = getDao(BroadcastDestinationDao.class).findAllByProcessPointId(appData.getApplicationId()); 
	        
	        processPointForm.setApplicationData(appData);
	        processPointForm.setBroadcastDestinationList(broadcastDestinationList);
	        
	    }
}
