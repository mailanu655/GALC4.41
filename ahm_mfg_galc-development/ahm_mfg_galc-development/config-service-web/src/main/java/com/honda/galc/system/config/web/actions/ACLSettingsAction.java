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
import com.honda.galc.dao.conf.AccessControlEntryDao;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.entity.conf.AccessControlEntry;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.system.config.web.forms.ACLForm;

/**
 * @version 	1.0
 * @author
 */
public class ACLSettingsAction extends ConfigurationAction

{

    private static final String ERRORS_GROUP = "applicationErrors";
    
    private static final String MESSAGES_GROUP = "applicationMessages";    

 
    /**
     * Constructor
     */
    public ACLSettingsAction() {
        super();
        
 }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ACLForm aclForm = (ACLForm) form;

        
        
        boolean isApply = false;
        boolean isRemove = false;
        
        List<AccessControlEntry> aclList = null;
        List<SecurityGroup>groupList = null;
        
        if (aclForm.getApply() != null && 
            aclForm.getApply().equalsIgnoreCase("apply") )
        {
            isApply = true;
        }
        else if (aclForm.getRemove() != null && 
                 aclForm.getRemove().equalsIgnoreCase("remove"))
        {
            isRemove = true;
        }
        
        if (request.isUserInRole("EditACL"))
        {
            aclForm.setEditor(true);
        }
        else
        {
            aclForm.setEditor(false);
        }
        
        try {
            
            if ((isApply || isRemove) && !aclForm.isEditor())   
            {
                throw new ConfigurationServicesException("CFGW1000");
            }

            if (aclForm.isInit())
            {
                // Look up the screen ID for the application
                if (aclForm.getApplicationID() != null && 
                    aclForm.getApplicationID().length() > 0)
                {
                   Application appData = getDao(ApplicationDao.class).findByKey(aclForm.getApplicationID());
                	
                    aclForm.setScreenID(appData.getScreenId());
                    aclForm.setApplicationName(appData.getApplicationName());
                    
                }
                
                
                // Screen ID was either supplied or obtained above
                if (aclForm.getScreenID() != null && 
                         aclForm.getScreenID().length() > 0)
                {
                   
                	aclList = getDao(AccessControlEntryDao.class).findAllByScreenId(aclForm.getScreenID());
                    
                    aclForm.setAclData(aclList);
                    
                }
            }
            else if (isApply)
            {
                // Store the new ACL data
            	AccessControlEntry aclData = new AccessControlEntry(aclForm.getScreenID(),aclForm.getSecurityGrp());
                
                aclData.setOperation(aclForm.getOperation());
                
                getDao(AccessControlEntryDao.class).insert(aclData);
                
                // Rebuild list
 
                aclList = getDao(AccessControlEntryDao.class).findAllByScreenId(aclForm.getScreenID());
                
                aclForm.setAclData(aclList);        
                
                messages.add(MESSAGES_GROUP,new ActionMessage("CFGW0032",aclData.getId().getScreenId(),aclData.getId().getSecurityGroup()));
            }
            else if (isRemove)
            {
                
            	AccessControlEntry aclData = new AccessControlEntry(aclForm.getScreenID(),aclForm.getRemoveSecurityGroup());
                
                aclData.setOperation(1);
                
                getDao(AccessControlEntryDao.class).remove(aclData);
                // Rebuild list
                
                aclList = getDao(AccessControlEntryDao.class).findAllByScreenId(aclForm.getScreenID());
                               
                aclForm.setAclData(aclList);        
                               
            }
            
            
            
            
            // Make sure the form is up to date with all security groups
            groupList = getDao(SecurityGroupDao.class).findAllSecurityGroup();
            aclForm.setGroups(groupList);
            

        } 
        catch (Exception e) {

        	e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));

        }  
        finally 
        {
            try {
                if (aclList == null)
                {
                    // Now obtain list of ACL Data associated with screen 
                	aclList = getDao(AccessControlEntryDao.class).findAllByScreenId(aclForm.getScreenID());
                    aclForm.setAclData(aclList);
                }
                
                if (groupList == null)
                {
                    // Make sure the form is up to date with all security groups
                	groupList = getDao(SecurityGroupDao.class).findAll();
                    aclForm.setGroups(groupList);                    
                }
            }
            catch (Exception e)
            {
                
            }
        }

        //forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
    }
    
    
}
