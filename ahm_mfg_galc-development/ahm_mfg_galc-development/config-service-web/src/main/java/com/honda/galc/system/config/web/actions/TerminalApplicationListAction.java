package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.util.SortedArrayList;

import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * @version 	1.0
 * @author
 */
public class TerminalApplicationListAction extends ConfigurationAction

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
 
        try {
            String terminalHostName = request.getParameter("terminalHostName");
            if (terminalHostName==null || terminalHostName.length() ==0) {
                terminalHostName = (String)request.getAttribute("terminalHostName");
            }
            
            List<ApplicationByTerminal> results = getDao(ApplicationByTerminalDao.class).findAllByTerminal(terminalHostName);
            request.setAttribute("terminalApplicationList", new SortedArrayList<ApplicationByTerminal>(results,"getApplicationId"));
            // do something here

        } catch (Exception e) {
            // Report the error using the appropriate name and ID.
            errors.add("name", new ActionError("id"));
        }

        //	 forward failure or success depending on errors
    	return forward(mapping,request,errors);

    }
}
