package com.honda.galc.system.config.web.actions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.SystemException;
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
public class CacheRefreshAction extends ConfigurationAction
   implements SessionConstants

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
     
        try {

        	String cacheName= request.getParameter("CACHE_NAME");
            String item = request.getParameter("ITEM");
            Map<String,Exception> messages = null;
            if("PROPERTY".equalsIgnoreCase(cacheName))
            	messages = ServerInfoUtil.refreshProperty(item);
            else if("LOT_CONTROL_RULE".equalsIgnoreCase(cacheName)) {
            	messages = ServerInfoUtil.refreshLotControlRule(item);
            }
            request.setAttribute("CACHE_NAME", cacheName);
            request.setAttribute("ITEM", item);
            request.setAttribute("MESSAGES", convertMessages(messages, item));
            
        } catch (SystemException e){
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
    
    private Map<String,String> convertMessages(Map<String,Exception> messages, String item) {
    	Map<String,String> msgs = new LinkedHashMap<String, String>();
    	for(Map.Entry<String, Exception> entry: messages.entrySet()) {
    		Exception ex = entry.getValue();
    		if(ex == null) msgs.put(entry.getKey(), "Item " + item + " is refreshed succesfully.");
    		else msgs.put(entry.getKey(), "Item " + item + " is not refreshed successfully due to exception " + getStackTrace(ex));
    	}
    	return msgs;
    }
    
    protected String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}
    

}
