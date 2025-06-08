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
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.config.web.forms.SecurityGroupForm;
import com.honda.galc.util.CommonUtil;

/**
 * @version 	1.0
 * @author
 */
public class SecurityGroupSettingsAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "securityGroupErrors";
    
    private static final String MESSAGES_GROUP = "securityGroupMessages";  

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();

        SecurityGroupForm securityGroupForm = (SecurityGroupForm) form;
        
        String showWarningColor = PropertyService.getProperty("System_Info", "DISPLAY_PRODUCTION_WARNING_COLOR", "background-color:transparent");	
        request.setAttribute("DISPLAY_PRODUCTION_WARNING_COLOR", showWarningColor);

        if (request.isUserInRole("EditAdmin"))
        {
            securityGroupForm.setEditor(true);    
        }
        
        boolean isApply = false;
        boolean isDelete = false;
        boolean fetchUserData = false;
        
        if (securityGroupForm.getApply() != null && 
            securityGroupForm.getApply().equalsIgnoreCase("apply"))
        {
            isApply = true;
        }
        else if (securityGroupForm.getDelete() != null && 
                 securityGroupForm.getDelete().equalsIgnoreCase("delete"))
        {
            isDelete = true;
        }

        try {
            if (securityGroupForm.isInitializeFrame())
            {
                securityGroupForm.setInitializeFrame(false);
                return mapping.findForward("initialize");
            }

            if (isApply) {
                
                if (!securityGroupForm.isExistingGroup()) {
                    
                    // We are creating a new group
                    SecurityGroup data =  securityGroupForm.getData();
                    data.setCreateTimestamp(CommonUtil.getTimestampNow());
                    
                    ServiceFactory.getDao(SecurityGroupDao.class).insert(data);
                    
                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0048",data.getSecurityGroup()));
                    
                    securityGroupForm.setExistingGroup(true);
                    
                    securityGroupForm.setRefreshList(true);
                    
                }
                else
                { 
                	SecurityGroup data =  securityGroupForm.getData();
                    ServiceFactory.getDao(SecurityGroupDao.class).update(data);
                    
                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0049",data.getSecurityGroup()));
                }
                
            } else if (isDelete) {
                
                ServiceFactory.getDao(SecurityGroupDao.class).removeByKey(securityGroupForm.getGroupID());
                
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0051",securityGroupForm.getGroupID()));
                securityGroupForm.setGroupID("");
                securityGroupForm.setGroupName("");
                securityGroupForm.setDisplayText("");
                
            }
            else if (!securityGroupForm.isExistingGroup())
            {
                // We are prompting for new dat
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0050"));
            }
            else         
            {
                // Just obtain group information
                SecurityGroup data = ServiceFactory.getDao(SecurityGroupDao.class).findByKey(securityGroupForm.getGroupID());
                
                securityGroupForm.setData(data);
                securityGroupForm.setExistingGroup(true);
                
            }
        } 
       catch (ConfigurationServicesException e)
		{
            ActionError actionError = null;
            if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0)
            {
               actionError = new ActionError("Test",e.getAdditionalInformation());
            } 
            else
            {
               actionError = new ActionError("Test",e.toString());
            }
            
            logActionError(actionError);
     	    errors.add(ERRORS_GROUP, actionError);
		}
       catch (SystemException e)
		{
           errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
       }
       catch (Exception e) {

            // Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0005",getRootCauseMessage(e));;
            logActionError(ae);
            
            errors.add(ERRORS_GROUP, ae);
        }
        
       //     forward failure or success depending on errors
       return forward(mapping,request,errors,messages);

    }
}
