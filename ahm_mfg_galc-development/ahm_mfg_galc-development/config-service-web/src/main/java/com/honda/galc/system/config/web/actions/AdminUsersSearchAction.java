package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.dao.conf.AdminUserDao;
import com.honda.galc.entity.conf.AdminUser;
import static com.honda.galc.service.ServiceFactory.getDao;
import com.honda.galc.system.config.web.forms.AdminUserSearchForm;

/**
 * <H3>AdminUsersSearchAction</H3> 
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This Struts Action class controls the behavior of the "find" portion of the 
 * Administrative Users Settings page.</P>
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
 * <TD>Mar 19, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class AdminUsersSearchAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "adminUserErrors";
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        AdminUserSearchForm adminUserSearchForm = (AdminUserSearchForm) form;

        try {

            if (adminUserSearchForm.isInitializePage())
            {
                adminUserSearchForm.setInitializePage(false);
                return mapping.findForward("initialize");
            }
            else if (adminUserSearchForm.getFind() != null &&
                     adminUserSearchForm.getFind().equalsIgnoreCase("find"))
            {
                
            	List<AdminUser> adminUsers = getDao(AdminUserDao.class).findAllMatchUserId(adminUserSearchForm.getUserMask());
                adminUserSearchForm.setAdminUsers(adminUsers);
            }

        } 
        catch (Exception e) {

            e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
        }          
        
        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
}
