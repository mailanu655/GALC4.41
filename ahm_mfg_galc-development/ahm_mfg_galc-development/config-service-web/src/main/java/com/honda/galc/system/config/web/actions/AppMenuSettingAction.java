package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ApplicationMenuDao;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.data.SessionConstants;
import com.honda.galc.system.config.web.forms.AppMenuForm;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This is the action class for obtaining and setting of application menu data
 * for a terminal.</p>
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
 * <TD>Feb 22, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 
 * </TABLE>

 */
public class AppMenuSettingAction extends ConfigurationAction
   implements SessionConstants

{

    private static final String ERRORS_GROUP = "Menu Settings Errors";
    
    private static final String MESSAGES_GROUP = "Menu Settings Messages";
    
    /**
     * Constructor
     */
    public AppMenuSettingAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        AppMenuForm appMenuForm = (AppMenuForm) form;
        
        List<ApplicationMenuEntry> menuList = null;
        
        boolean isApply = false;
        boolean isCreate = false;
        boolean isDelete = false;
        
        if (appMenuForm.getApply() != null && appMenuForm.getApply().equalsIgnoreCase("apply")) {
            
            isApply = true;
        }
        else if (appMenuForm.getDelete() != null && appMenuForm.getDelete().equalsIgnoreCase("delete"))
        {
            isDelete = true;
        }
        
        if (appMenuForm.getCreateFlag() != null && appMenuForm.getCreateFlag().equalsIgnoreCase("true")) {
            isCreate = true;
        }
        
        
        if (request.isUserInRole("EditTerminal"))
        {
            appMenuForm.setEditor(true);
        }
        
        try {
            
            // See if menu is stored in session
            HttpSession session = request.getSession(true);
            
            menuList = (List)session.getAttribute(SESSION_MENU_DATA);
            
            if (menuList != null)
            {
                // Confirm that this is the list for the terminal
                // we are working with
                
                if (menuList.size() > 0)
                {
                    ApplicationMenuEntry node = menuList.get(0);
                    
                    if (!node.getId().getClientId().equals(appMenuForm.getClientID()))
                    {
                       menuList = null;
                       session.removeAttribute(SESSION_MENU_DATA);    
                    }
                        
                }
            }

            // Make sure user has change authorization
            if ( (isApply || isCreate || isDelete || appMenuForm.isNewChild()) 
                  && !appMenuForm.isEditor())
            {
        	    throw new ConfigurationServicesException("CFGW1000");
            }
            
            if (isDelete)
            {
                if (!appMenuForm.isConfirmDelete())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                ServiceFactory.getDao(ApplicationMenuDao.class).removeById(appMenuForm.getClientID(), appMenuForm.getNodeNumber());
                
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0031",new Integer(appMenuForm.getNodeNumber())));
                
                // rebuild list
                menuList = null;
                
                // Dont show settings again
                appMenuForm.setShowSetting(false);                
            }
            else if (isApply)
            {
                
                
                ApplicationMenuEntry data = null;
                //update the node
                if (appMenuForm.getModifyNode() != 0 && !isCreate) {
                    data = new ApplicationMenuEntry(appMenuForm.getClientID(),appMenuForm.getNodeNumber());
	                data.setParentNodeNumber(appMenuForm.getParentNodeNumber());
	                data.setNodeName(appMenuForm.getNodeName());
	                
	                ServiceFactory.getDao(ApplicationMenuDao.class).update(data);
	                
	                ActionMessage message = new ActionMessage("CFGW2030", appMenuForm.getClientID(), String.valueOf(data.getId().getNodeNumber()));
	                messages.add(MESSAGES_GROUP, message);
	                
	                // Rebuild the menu tree
	                menuList = null;
	                
	                // Dont show settings again
	                appMenuForm.setShowSetting(false);
                }
                //create new node
                else if (isCreate){
                    
                    // Validate node number
                    if (appMenuForm.getNodeNumber() <= 0)
                    {
                        throw new ConfigurationServicesException("CFGW0029");
                    }
                    
                    if (appMenuForm.getNodeName() == null || appMenuForm.getNodeName().length() == 0)
                    {
                        throw new ConfigurationServicesException("CFGW0030");
                    }
                        
                    
	                data = new ApplicationMenuEntry(appMenuForm.getClientID(),appMenuForm.getNodeNumber());
	                data.setParentNodeNumber(appMenuForm.getParentNodeNumber());
	                data.setNodeName(appMenuForm.getNodeName());
	                ServiceFactory.getDao(ApplicationMenuDao.class).insert(data);
	                
	                ActionMessage message = new ActionMessage("CFGW2032", appMenuForm.getClientID(), String.valueOf(data.getId().getNodeNumber()));
	                messages.add(MESSAGES_GROUP, message);
	                
	                appMenuForm.setCreateFlag(null);
	                isCreate = false;
	                
	                // rebuild the menu list
	                menuList = null;
	                
	                appMenuForm.setShowSetting(false);
                }
            }
            else if (appMenuForm.isNewChild())
            {
                // Prepare settings to display new child
                appMenuForm.setParentNodeNumber(appMenuForm.getCurrentDisplayParent());
                appMenuForm.setCreateFlag("true");
                
                appMenuForm.setShowSetting(true);
                
                appMenuForm.setNodeName(null);
                appMenuForm.setDisplayText(null);
                appMenuForm.setNodeNumber(0);
                appMenuForm.setParentNodeNumber(appMenuForm.getCurrentDisplayParent());

            }
            else if (appMenuForm.getModifyNode() > 0)
            {
                // Show settings for this node
                appMenuForm.setShowSetting(true);
                
                
                
            }

            // Load the menu information
            if (menuList == null)
            {
               
               menuList = ServiceFactory.getDao(ApplicationMenuDao.class).findAllByClientId(appMenuForm.getClientID());
               appMenuForm.setApplicationMenuDataNodes(menuList);
               
               session = request.getSession();
               
               session.setAttribute(SESSION_MENU_DATA,menuList);
               
            }
            
            
            if (menuList != null) {
                
               
               
               // Load the form fields with either current parent or first item in list
               if (appMenuForm.getModifyNode() != 0) {
   
            	   
            	   // get root menu
            	   ApplicationMenuEntry node = findNode(appMenuForm.getModifyNode(),menuList);
                   if (node != null) {

                     appMenuForm.setDisplayText(node.getApplicationName());
                     appMenuForm.setNodeName(node.getNodeName());
                     appMenuForm.setNodeNumber(node.getId().getNodeNumber());
                     appMenuForm.setParentNodeNumber(node.getParentNodeNumber());
                   
                     appMenuForm.setDeletable(hasChildren(node.getId().getNodeNumber(), menuList));
                   }
               }

                
            }


        } 
        catch (ConfigurationServicesException e)
		{
            ActionError actionError = null;
            if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0)
            {
               actionError = new ActionError("Test",e.getAdditionalInformation());
            } 
            else
            {
               actionError = new ActionError("Test",e.toString());
            }
            
            logActionError(actionError);
     	    errors.add(ERRORS_GROUP, actionError);
            
            
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        
        catch (Exception e) {
            e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",ExceptionUtils.getRootCauseMessage(e)));
        } 
        
        if (menuList != null)
        {
            appMenuForm.setApplicationMenuDataNodes(menuList);
        }
        
        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
    
    private ApplicationMenuEntry findNode(int nodeNumber, List<ApplicationMenuEntry> menuList) {
    	
    	for(ApplicationMenuEntry menu : menuList) {
    		if(menu.getId().getNodeNumber() == nodeNumber) return menu;
    	}
    	return null;
    	
    }
    
    private boolean hasChildren(int nodeNumber, List<ApplicationMenuEntry> menuList) {
    	for(ApplicationMenuEntry menu : menuList) {
    		if(menu.getParentNodeNumber() == nodeNumber) return true;
    	}
    	return false;
    }
    
 }
