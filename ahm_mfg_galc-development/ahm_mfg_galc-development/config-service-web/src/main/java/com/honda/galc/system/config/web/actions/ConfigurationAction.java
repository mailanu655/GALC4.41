/*
 * Created on Jan 31, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.honda.galc.system.config.web.actions;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.system.config.web.plugin.InitializationPugin;


/**
 * @author martinek
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ConfigurationAction extends Action {

    
	protected Logger logger= InitializationPugin.getLogger();
	
	/**
	 * 
	 */
	public ConfigurationAction() {
		super();
		
	}
	
	/**
	 * This utility method will log an ActionError object into
	 * the GALC logging framework.
	 * @param error
	 * @return
	 */
	protected ActionError logActionError(ActionError error)
	{
	    
	    Object[] additionalInfo = error.getValues();
	    if (additionalInfo != null && additionalInfo.length > 0)
	    {   
	        StringBuffer buffer = new StringBuffer();
	        for (int i=0; i <additionalInfo.length; i++)
	        {
	            Object o = additionalInfo[i];
	            if (o != null)
	            {
	               buffer.append(o.toString());
	               buffer.append(" ");
	            }
	        }
	        
	        logger.error(buffer.toString());
		        
	    }
	    
	    return error;
	}
	
	protected void logActionMessages(HttpServletRequest request,ActionMessages messages) {
		
		MessageResources messageResource = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
		
//		 Loop thru all the labels in the ActionMessage's  
	     for (Iterator i = messages.properties(); i.hasNext();) {
	      String property = (String)i.next();
	      
	      // Get all messages for this label
	       for (Iterator it = messages.get(property); it.hasNext();) {
	    	   ActionMessage a = (ActionMessage)it.next();
	    	   String key = a.getKey();
	    	   Object[] values = a.getValues();
	           logger.info(key + " [ " + messageResource.getMessage(key,values) + 
	          "]");
	      }
	    }
			
	}
	
	protected void logActionErrors(ActionErrors errors) {
		
		logger.info(errors.toString());
		
	}
	protected ActionForward forward(ActionMapping mapping, 
            HttpServletRequest request, ActionErrors errors) {
		ActionForward forward = null;
//		 into the request for use by the <struts:errors> tag.
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            
            // Forward control to the appropriate 'failure' URI (change name as desired)
            forward = mapping.findForward("failure");
        } else {
            // Forward control to the appropriate 'success' URI (change name as desired)
            forward = mapping.findForward("success");
        }
        return (forward);
		
	}
	
	protected ActionForward forward(ActionMapping mapping, 
            HttpServletRequest request, ActionErrors errors,ActionMessages messages) {
		//		 If a message is required, save the specified key(s)
        // into the request for use by the <struts:errors> tag.
        if (!messages.isEmpty()) {
            saveMessages(request, messages);
            
            logActionMessages(request,messages);
            
        }
        
        //      forward failure or success depending on errors
        return forward(mapping,request,errors);
	}
	
	protected String getRootCauseMessage(Throwable ex) {
		
		Throwable cause = ex;
		
		while(cause.getCause() != null) cause = cause.getCause();
		
		return cause.getClass().getName() + " : " + cause.getMessage();
	}	
}
