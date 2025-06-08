package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

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
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.system.config.web.data.AttributeConstants;
import com.honda.galc.system.config.web.forms.ApplicationSearchForm;

/**
 * @version 	1.0
 * @author
 */
public class ApplicationSearchAction extends ConfigurationAction

{

    private static final String ERRORS_GROUP = "applicationErrors";
    
    /**
     * Constructor
     */
    public ApplicationSearchAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ApplicationSearchForm applicationSearchForm = (ApplicationSearchForm) form;

        try {
            
            if (applicationSearchForm.isInitializePage())
            {
                return mapping.findForward("initialize");
            }else if(applicationSearchForm.getApplicationType() != AttributeConstants.COMBOBOX_UNSELECTED_OPTION_VALUE){
            	List<Application> appList;
            	if(applicationSearchForm.getSearchText() != null){
            		appList = getDao(ApplicationDao.class).findAllByApplicationTypeId(applicationSearchForm.getApplicationType(), applicationSearchForm.getSearchText());
            	}else{
            		appList = getDao(ApplicationDao.class).findAllByApplicationTypeId(applicationSearchForm.getApplicationType());
            	}
                applicationSearchForm.setApplicationList(appList);
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
            
           
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
        	
        	
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));

        }  

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
    }
}
