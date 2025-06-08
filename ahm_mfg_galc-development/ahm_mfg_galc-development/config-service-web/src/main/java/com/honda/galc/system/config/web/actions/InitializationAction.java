package com.honda.galc.system.config.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.service.property.PropertyService;


/**
 * 
 * <h3>InitializationAction</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
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
 * <TD>Feb 6, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class InitializationAction extends ConfigurationAction

{
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();

        try {

            // Create a new session
            HttpSession session = request.getSession(true);
            
            
            String lineID = PropertyService.getProperty("System_Info", "ASSEMBLY_LINE_ID","Unknown");
            
            session.setAttribute("ASSEMBLY_LINE_ID",lineID);
                
            // @NALC-28 - For production systems, we want to display a warning on login
            String showWarning = PropertyService.getProperty("System_Info", "DISPLAY_PRODUCTION_WARNING", "FALSE");
            String showWarningColor = PropertyService.getProperty("System_Info", "DISPLAY_PRODUCTION_WARNING_COLOR", "background-color:transparent");	
            	
            session.setAttribute("DISPLAY_PRODUCTION_WARNING", showWarning);
            session.setAttribute("DISPLAY_PRODUCTION_WARNING_COLOR", showWarningColor);
            	
        } catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add("name", new ActionError("id"));

        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors);


    }


}
