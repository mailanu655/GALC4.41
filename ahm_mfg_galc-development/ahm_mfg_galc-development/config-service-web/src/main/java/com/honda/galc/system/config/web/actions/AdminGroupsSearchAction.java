package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.AdminGroupDao;
import com.honda.galc.entity.conf.AdminGroup;

import static com.honda.galc.service.ServiceFactory.getDao;
import com.honda.galc.system.config.web.forms.AdminGroupSearchForm;

/**
 * <H3>AdminGroupsSearchAction</H3> 
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This Struts Action class controls the portion of the Administrative Group settings page
 * that loads a list of defined groups.</p>
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
 * <TD>Mar 9, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class AdminGroupsSearchAction extends ConfigurationAction
{

    private static final String ERRORS_GROUP = "adminGroupErrors";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        AdminGroupSearchForm adminGroupSearchForm = (AdminGroupSearchForm) form;

        try {

            if (adminGroupSearchForm.getFind() != null &&
                adminGroupSearchForm.getFind().equalsIgnoreCase("find"))
            {
               
            	
            	List<AdminGroup> result = getDao(AdminGroupDao.class).findAllMatchGroupId(adminGroupSearchForm.getGroupMask());
            	
                adminGroupSearchForm.setAdminGroups(result);
            }

        } 
         catch (ConfigurationServicesException e)
		{
            ActionError actionError = null;
            if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0)
            {
               actionError = new ActionError(e.getMessage(),e.getAdditionalInformation());
            } 
            else
            {
               actionError = new ActionError(e.getMessage(),e.toString());
            }
            
            logActionError(actionError);
     	    errors.add(ERRORS_GROUP, actionError);
		}
         catch (SystemException e)
 		{
             errors.add(ERRORS_GROUP, new ActionError(e.getMessage(),e.toString()));
         }
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0005",e.toString());;
            logActionError(ae);
            
            errors.add(ERRORS_GROUP, ae);
        }

        //      forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
    }
}
