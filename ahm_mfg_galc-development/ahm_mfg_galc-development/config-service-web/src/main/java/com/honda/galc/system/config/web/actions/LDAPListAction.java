package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.entity.conf.User;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;


/**
 * @version 	1.0
 * @author
 */
public class LDAPListAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "UserErrors";
    
    public LDAPListAction() {
        super();
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
 
        try {
            if (request.isUserInRole("EditUsers"))
            {
                request.setAttribute("isEditor", "true");
            }else{
                request.setAttribute("isEditor", "false");
            }
            
            List<User> users = ServiceFactory.getDao(UserDao.class).findAll();
            
            request.setAttribute("lDAPList", new SortedArrayList<User>(users,"getUserId"));
        }
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        
        catch (Exception e) {
            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
        }  

        return forward(mapping,request,errors);

    }
}
