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

import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.service.ServiceFactory;

/**
 * @version 	1.0
 * @author
 */
public class TerminalApplicationItemListAction extends ConfigurationAction

{
    public TerminalApplicationItemListAction() {
        super();
        
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();        
 
        try{
            String applicationType = request.getParameter("applicationType");
            String applicationFilter = request.getParameter("applicationFilter");            
            if (applicationType==null) {
                applicationType = (String)request.getAttribute("applicationType");
            }
            if (applicationType != null && !applicationType.equals("")) {
                int type = Integer.parseInt(applicationType);
                List<ApplicationType> types = ApplicationType.getByMainId(type);
                List<Application> applications = ServiceFactory.getDao(ApplicationDao.class).findAllByApplicationTypes(types, StringUtils.trimToEmpty(applicationFilter));
                request.setAttribute("applicationList", applications);
                request.setAttribute("applicationType", applicationType);
            }
        } catch (Exception e) {
            // Report the error using the appropriate name and ID.
            errors.add("name", new ActionError("id"));
        }

        //	 forward failure or success depending on errors
    	return forward(mapping,request,errors);

    }
}
