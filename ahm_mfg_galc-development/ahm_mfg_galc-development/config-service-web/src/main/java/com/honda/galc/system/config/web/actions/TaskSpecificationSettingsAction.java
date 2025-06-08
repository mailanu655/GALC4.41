package com.honda.galc.system.config.web.actions;

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
import com.honda.galc.dao.conf.TaskSpecDao;
import com.honda.galc.entity.conf.TaskSpec;

import static com.honda.galc.service.ServiceFactory.getDao;
import com.honda.galc.system.config.web.forms.TaskSpecificationForm;

/**
 * @version 	1.0
 * @author
 */
public class TaskSpecificationSettingsAction extends ConfigurationAction {

	private static final String ERRORS_GROUP = "taskSpecErrors";
    
    private static final String MESSAGES_GROUP = "updateTaskSpecMessages";


    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        TaskSpecificationForm taskSpecificationForm = (TaskSpecificationForm) form;

        try {

            if (taskSpecificationForm.getFindTask() != null) {
            	// Load the task list
                taskSpecificationForm.setTaskSpecList(getDao(TaskSpecDao.class).findAll());
            }
            else if (taskSpecificationForm.getApply() != null &&
                     taskSpecificationForm.getApply().equalsIgnoreCase("apply")) {
                
               TaskSpec data = taskSpecificationForm.getTaskSpecData();
               
               getDao(TaskSpecDao.class).insert(data);
               
               messages.add(MESSAGES_GROUP , new ActionMessage("CFGW0023",data.getTaskName()));
                
            }
            else if (taskSpecificationForm.getDelete() != null &&
                     taskSpecificationForm.getDelete().equalsIgnoreCase("delete")) {
                
            	getDao(TaskSpecDao.class).removeByKey(taskSpecificationForm.getTaskName());
 
            	taskSpecificationForm.setTaskName("");
                taskSpecificationForm.setJndiName("");
                taskSpecificationForm.setTaskDescription("");
                taskSpecificationForm.setStatefulSessionBean(false);
                messages.add(MESSAGES_GROUP , new ActionMessage("CFGW0022",taskSpecificationForm.getTaskName()));
            }
                    
        }    
        catch (ConfigurationServicesException e)
   		{
           	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));

        } catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));

        }  

		//	 forward failure or success depending on errors
    	return forward(mapping,request,errors,messages);

    }
}
