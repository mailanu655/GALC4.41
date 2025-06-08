package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.dao.conf.AdminGroupDao;
import com.honda.galc.dao.conf.AdminUserDao;
import com.honda.galc.dao.conf.AdminUserGroupDao;
import com.honda.galc.entity.conf.AdminGroup;
import com.honda.galc.entity.conf.AdminUser;
import com.honda.galc.entity.conf.AdminUserGroup;
import com.honda.galc.system.config.web.forms.AdminUserForm;

/**
 * @version 	1.0
 * @author
 */
public class AdminUserSettingsAction extends ConfigurationAction

{

    private static final String ERRORS_GROUP = "adminUserErrors";
    
    private static final String MESSAGES_GROUP = "adminUserMessages";  
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
         AdminUserForm adminUserForm = (AdminUserForm) form;
        
        
        if (request.isUserInRole("EditAdmin"))
        {
            adminUserForm.setEditor(true);    
        }
        
        boolean isApply = false;
        boolean isDelete = false;
        boolean fetchUserData = false;
        
        if (adminUserForm.getApply() != null && 
            adminUserForm.getApply().equalsIgnoreCase("apply"))
        {
            isApply = true;
        }
        else if (adminUserForm.getDelete() != null && 
                 adminUserForm.getDelete().equalsIgnoreCase("delete"))
        {
            isDelete = true;
        }

        List<AdminGroup> groupList = null;
        try {

            if (adminUserForm.isInitializeFrame())
            {
                adminUserForm.setInitializeFrame(false);
                return mapping.findForward("initialize");
            }
            
            if (isApply || isDelete)
            {
                if (!adminUserForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
            }
            
            if (isApply)
            {
               if (!adminUserForm.isExistingUser())
               {
                   if (adminUserForm.getUserID() == null || 
                       adminUserForm.getUserID().length() == 0)
                   {
                       throw new ConfigurationServicesException("CFGW0041");
                   }
                   
                   // Create time
                   validatePassword(adminUserForm);
                   
                   getDao(AdminUserDao.class).insert(adminUserForm.getAdminUser());
                   
                   getDao(AdminUserGroupDao.class).saveAll(adminUserForm.getUserGroups());
                   
                   // Add success message
                   messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0042",adminUserForm.getUserID()));
                   
                   // Set the flag so we refresh user data
                   fetchUserData = true;
                   
                   // Trigger the refresh of the list
                   adminUserForm.setRefreshList(true);
                   
               }
               else
               {
                   // Update time
                   if (adminUserForm.isChangePassword())
                   {
                       validatePassword(adminUserForm);
                   }
                   
                   getDao(AdminUserDao.class).update(adminUserForm.getAdminUser(), adminUserForm.isChangePassword());
                   
                   AdminUserGroupDao dao = getDao(AdminUserGroupDao.class);
                		   
                   dao.deleteAllByUserId(adminUserForm.getUserID());
                   
                   dao.saveAll(adminUserForm.getAssignedUserGroups());
                   
                   // Add success message
                   messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0042",adminUserForm.getUserID()));
                   
                   
                   // Set the flag to reload user data
                   fetchUserData = true;
                   
               }
               
               
            }
            else if (isDelete)
            {
                if (!adminUserForm.isDeleteConfirm())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                // Remove the user ID
                
                getDao(AdminUserGroupDao.class).deleteAllByUserId(adminUserForm.getUserID());
                getDao(AdminUserDao.class).removeByKey(adminUserForm.getUserID());
                
                adminUserForm.setAssignedGroups(null);
                adminUserForm.setAvailableGroups(null);
                adminUserForm.setDescription("");
                adminUserForm.setDisplayName("");
                adminUserForm.setChangePassword(false);
                
                
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0044",adminUserForm.getUserID()));
                
                // Trigger the refresh of the list
                adminUserForm.setRefreshList(true);

               
                
            }
            else if (!adminUserForm.isExistingUser())
            {
                // Just go to the page to get user settings
                ;
            }
            else
            {
                // Just fetch the data
                fetchUserData = true;
            }
            
            if (fetchUserData) {
                // Just get the user information
                if (adminUserForm.getUserID() != null &&
                    adminUserForm.getUserID().length() > 0)
                {
                    AdminUser userData = getDao(AdminUserDao.class).findByKey(adminUserForm.getUserID());
                    
                    adminUserForm.setExistingUser(true);
                    adminUserForm.setDescription(userData.getUserDesc());
                    adminUserForm.setDisplayName(userData.getDisplayName());
                    
                    List<AdminUserGroup> usersGroups = getDao(AdminUserGroupDao.class).findAllByUserId(adminUserForm.getUserID());
                    
                    adminUserForm.setUserGroups(usersGroups);
                    
                }
            }
            
            // Get the group list
            
            groupList = getDao(AdminGroupDao.class).findAll();
            adminUserForm.setAllGroups(groupList);
            

        }
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError(e.getMessage(),e.toString()));
        	
        	// Restore group list to form for error display
        	if (groupList != null)
        	{
        	    adminUserForm.setAllGroups(groupList);
        	}

        	// Rebuild list of existing groups
        	try {
               List<AdminUserGroup> usersGroups = getDao(AdminUserGroupDao.class).findAllByUserId(adminUserForm.getUserID());
               adminUserForm.setUserGroups(usersGroups);
        	}
        	catch (Exception e2)
        	{
        	    
        	}        	
        	
		}
        
                    	
           
		catch (Exception e) {

            e.printStackTrace();
			// Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0005",e.toString());;
            logActionError(ae);
            
            errors.add(ERRORS_GROUP, ae);
            
        	// Restore group list to form for error display
        	if (groupList != null)
        	{
        	    adminUserForm.setAllGroups(groupList);
        	}
            

        }
        finally 
        {
        	// Restore group list to form for error display
        	if (groupList != null)
        	{
        	    adminUserForm.setAllGroups(groupList);
        	}
        	else
        	{
        	    try {
        	        // Get the group list
                    groupList = getDao(AdminGroupDao.class).findAll();
                    adminUserForm.setAllGroups(groupList);   
        	    }
        	    catch (Exception e)
        	    {
        	        
        	    }
        	}
        }
        
        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
    
    }
    
    private void validatePassword(AdminUserForm form)
       throws ConfigurationServicesException
    {
        String pw1 = form.getTextPassword();
        String pw2 = form.getConfirmPassword();
        
        if (pw1 == null || pw2 == null  ||
            pw1.length() == 0 || pw2.length() == 0)
        {
            throw new ConfigurationServicesException("CFGW0038");
        }
        
        pw1 = pw1.trim();
        pw2 = pw2.trim();
        
        form.setTextPassword(pw1);
        form.setConfirmPassword(pw2);
        
        if (pw1.length() == 0)
        {
            throw new ConfigurationServicesException("CFGW0038");
        }
        
        if (!pw1.equals(pw2)) {
            throw new ConfigurationServicesException("CFGW0039");
        }
        
        // Perform remaining tests in lower case
        pw1 = pw1.toLowerCase();
        
        if (pw1.indexOf(form.getUserID().toLowerCase()) >= 0)
        {
            throw new ConfigurationServicesException("CFGW0040");
        }
            
            
    }
}
