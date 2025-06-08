
package com.honda.galc.system.config.web.controller;

import java.security.Principal;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This class extends the Struts RequestProcessor in order to perform
 * some runtime initialization before the actions are called.
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
 * <TD>Jan 31, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class ConfigurationServicesRequestProcessor extends RequestProcessor {

	/**
	 * 
	 */
	public ConfigurationServicesRequestProcessor() {
		super();
		
	}

	protected ActionForward processActionPerform(javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response,
            Action action,
            ActionForm form,
            ActionMapping mapping)
     throws java.io.IOException,
            javax.servlet.ServletException
			{
		
		
		try {
					String user = "unknown";
					Principal userPrincipal = request.getUserPrincipal();
					
					if(userPrincipal != null) {
						user = userPrincipal.getName();
					}
					
					return super.processActionPerform(request, response,action,form,mapping);
				}
				finally
				{
				}
			}
}
