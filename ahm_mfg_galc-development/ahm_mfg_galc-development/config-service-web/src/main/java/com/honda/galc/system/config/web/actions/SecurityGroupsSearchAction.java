package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.system.config.web.forms.SecurityGroupSearchForm;

import static com.honda.galc.service.ServiceFactory.getDao;




/**
 * @version 	1.0
 * @author
 */
public class SecurityGroupsSearchAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "securityGroupErrors";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        SecurityGroupSearchForm securityGroupSearchForm = (SecurityGroupSearchForm) form;

        try {
            if (securityGroupSearchForm.getFind() != null &&
                    securityGroupSearchForm.getFind().equalsIgnoreCase("find"))
            {
                List<SecurityGroup> securityGroups = getDao(SecurityGroupDao.class).findAllMatchGroupId(securityGroupSearchForm.getGroupMask());
                
                securityGroupSearchForm.setSecurityGroups(securityGroups);
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
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0005",e.toString());;
            logActionError(ae);
            
            errors.add(ERRORS_GROUP, ae);
        }        
        
        //      forward failure or success depending on errors
        return forward(mapping,request,errors);

    }
}
