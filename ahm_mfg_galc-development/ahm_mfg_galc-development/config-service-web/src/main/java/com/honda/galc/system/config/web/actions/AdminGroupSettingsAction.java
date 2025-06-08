package com.honda.galc.system.config.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.dao.conf.AdminGroupDao;
import com.honda.galc.entity.conf.AdminGroup;
import com.honda.galc.system.config.web.forms.AdminGroupForm;

/**
 * <H3>AdminGroupSettingsAction</H3> 
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This struts Action class controls the Administrative Group Settings Page.</P>
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
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class AdminGroupSettingsAction extends ConfigurationAction

{


    private static final String ERRORS_GROUP = "adminGroupErrors";
    
    private static final String MESSAGES_GROUP = "adminGroupMessages";  
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ActionForward forward = new ActionForward(); // return value
        AdminGroupForm adminGroupForm = (AdminGroupForm) form;

        if (request.isUserInRole("EditAdmin"))
        {
            adminGroupForm.setEditor(true);    
        }
        
        boolean isApply = false;
        boolean isDelete = false;
        
        if (adminGroupForm.getApply() != null && 
            adminGroupForm.getApply().equalsIgnoreCase("apply"))
        {
            isApply = true;
        }
        else if (adminGroupForm.getDelete() != null && 
                 adminGroupForm.getDelete().equalsIgnoreCase("delete"))
        {
            isDelete = true;
        }

        try {
            
            if (adminGroupForm.isInitializeFrame())
            {
                adminGroupForm.setInitializeFrame(false);
                return mapping.findForward("initialize");
            }

            if (isApply) {
                
                if (!adminGroupForm.isExistingGroup()) {
                    
                    // We are creating a new group
                    AdminGroup data =  adminGroupForm.getData();
                    getDao(AdminGroupDao.class).insert(data);
                    
                    
                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0048",data.getGroupId()));
                    
                    adminGroupForm.setExistingGroup(true);
                    
                    adminGroupForm.setRefreshList(true);
                    
                }
                else
                { 
                	AdminGroup data =  adminGroupForm.getData();
                    getDao(AdminGroupDao.class).update(data);
                    
                    messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0049",data.getGroupId()));
                    
                }
                
                
            } else if (isDelete) {
                
                getDao(AdminGroupDao.class).removeByKey(adminGroupForm.getGroupID());
            	
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0051",adminGroupForm.getGroupID()));
                adminGroupForm.setDescription("");
                adminGroupForm.setDisplayName("");
                adminGroupForm.setGroupID("");
                
            }
            else if (!adminGroupForm.isExistingGroup())
            {
                // We are prompting for new dat
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0050"));
            }
            else         
            {
                // Just obtain group information
                AdminGroup data = getDao(AdminGroupDao.class).findByKey(adminGroupForm.getGroupID());
                
                adminGroupForm.setGroupID(data.getGroupId());
                adminGroupForm.setDescription(data.getGroupDesc());
                adminGroupForm.setDisplayName(data.getDisplayName());
                adminGroupForm.setExistingGroup(true);
                
            }
            

        } 
        catch (Exception e) {

            e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0005",e.toString());;
            logActionError(ae);
            
            errors.add(ERRORS_GROUP, ae);
        }
        
        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
    }

}
