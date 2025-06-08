package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.config.web.data.SessionConstants;

/**
 * @version 	1.0
 * @author
 */
public class CacheContentsAction extends ConfigurationAction
   implements SessionConstants

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        
        String showWarningColor = PropertyService.getProperty("System_Info", "DISPLAY_PRODUCTION_WARNING_COLOR", "background-color:transparent");	
        request.setAttribute("DISPLAY_PRODUCTION_WARNING_COLOR", showWarningColor);
     
        try {

            List<String> componentIds = PropertyService.getComponentIds();
            
            List<String> processPoints = LotControlRuleCache.getProcessPoints();
            
            request.setAttribute("COMPONENT_IDS", componentIds);
            request.setAttribute("PROCESS_POINT_IDS", processPoints);
        } catch (SystemException e)
		{
            e.printStackTrace(System.out);
        	errors.add("refreshcacheerror", new ActionError("CFGW0009",e.toString()));
		}
        catch (Exception e) {

            e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            errors.add("refreshcacheerror", new ActionError("CFGW0009",e.toString()));

        }

        //      forward failure or success depending on errors
        return forward(mapping,request,errors);

    }
    

}
