package com.honda.galc.system.config.web.actions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.system.config.web.data.SessionConstants;
import com.honda.galc.util.ServerInfoUtil;

/**
 * 
 * <h3>CacheDisplayAction Class description</h3>
 * <p> CacheDisplayAction description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 13, 2012
 *
 *
 */
public class CacheDisplayPropertyAction extends ConfigurationAction
   implements SessionConstants

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
     
        try {

            String item = request.getParameter("ITEM");
            
            Map<String,List<ComponentProperty>> properties = ServerInfoUtil.getPropertyFromActiveServerCache(item);
        	
            request.setAttribute("COMPONENT_PROPERTY", properties);
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
