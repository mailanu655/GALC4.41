package com.honda.galc.system.config.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.TaskSpecDao;
import com.honda.galc.entity.conf.TaskSpec;
import com.honda.galc.system.config.web.forms.ApplicationTaskForm;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This action sets up the form data to display in the ApplicationTaskSettings page.
 * Note that the page itself does not submit data; instead it enters the data in the master
 * configuration page.
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
 * <TD>Feb 12, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 
 * </TABLE>

 */
public class ApplicationTaskSettingsAction extends ConfigurationAction {

    public static final String ERRORS_GROUP = "applicationTaskErrors";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ApplicationTaskForm applicationTaskForm = (ApplicationTaskForm) form;

        try {
            
            // Get the list of task specifications
            applicationTaskForm.setTaskSpecList(getDao(TaskSpecDao.class).findAll());
            
            
            // If the task name is null set it to the first item in the list
            if (applicationTaskForm.getTaskName() == null || applicationTaskForm.getTaskName().length() == 0)
            {
                if (applicationTaskForm.getTaskSpecList().size() > 0)
                {
                    TaskSpec specdata = applicationTaskForm.getTaskSpecList().get(0);
                    applicationTaskForm.setTaskName(specdata.getTaskName());
                   
                }
                
            }
            

        } 
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005"));

        }
        
        //      forward failure or success depending on errors
        return forward(mapping,request,errors);

    }
}
