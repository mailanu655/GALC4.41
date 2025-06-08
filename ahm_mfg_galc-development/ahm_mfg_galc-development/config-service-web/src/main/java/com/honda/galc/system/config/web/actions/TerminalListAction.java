package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;


/**
 * @version 	1.0
 * @author
 */
public class TerminalListAction extends ConfigurationAction

{

    /**
     * Constructor
     */
    public TerminalListAction() {
        super();
    }

    private static final String ERRORS_GROUP = "terminalListErrors";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        
        String divID = request.getParameter("divisionID");
        String appId = request.getParameter("appId");
        TerminalDao dao = ServiceFactory.getDao(TerminalDao.class);
        List<Terminal> terminals = null;

        try {
			if (StringUtils.isNotBlank(appId)) {
				request.setAttribute("appId", appId);
				terminals = dao.findAllByApplicationId(appId);
			} else if (StringUtils.isNotBlank(divID)) {
				request.setAttribute("divID", divID);
				terminals = dao.findAllByDivisionId(divID);
			} else {
				request.setAttribute("divID", "*");
				terminals = dao.findAll();
			}
			request.setAttribute("terminalList", new SortedArrayList<Terminal>(terminals, "getHostName"));
        }
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.getMessage()));
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005"));

        }        

		//	 forward failure or success depending on errors
    	return forward(mapping,request,errors,messages);

    }
}
