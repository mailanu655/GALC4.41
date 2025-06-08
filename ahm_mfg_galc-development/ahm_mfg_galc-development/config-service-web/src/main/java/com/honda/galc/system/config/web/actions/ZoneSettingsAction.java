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

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.entity.conf.Zone;
import static com.honda.galc.service.ServiceFactory.getDao;
import com.honda.galc.system.config.web.forms.ZoneForm;

/**
 * @version 	1.0
 * @author
 */
public class ZoneSettingsAction extends ConfigurationAction

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ZoneForm zoneForm = (ZoneForm) form;
        
        boolean isApply = false;
        boolean isDelete = false;

        if (zoneForm.getApply() != null && 
                zoneForm.getApply().equalsIgnoreCase("apply"))
        {
           isApply = true;
        }
        else if (zoneForm.getDelete() != null && 
                 zoneForm.getDelete().equalsIgnoreCase("delete"))
        {
           isDelete = true;
        }

        // Determine if the caller can edit Zones
        if (request.isUserInRole("EditProcess"))
        {
            zoneForm.setEditor(true);
        }
        
        

        try {
            
            // Determine if the user has correct permissions for
            // create, update, or delete
            if (isApply || isDelete || !zoneForm.isExistingZone())
            {
                if (!zoneForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
            }
            
            if (isApply)
            {
                if (!zoneForm.isExistingZone())
                {
                	Zone newZone  = getDao(ZoneDao.class).insert(zoneForm.getZoneData());
                	
                    zoneForm.setZoneID(newZone.getZoneId());
                    
                    messages.add("changezoneinfo",new ActionMessage("CFGW0052", zoneForm.getZoneID(), zoneForm.getDivisionID()));
                    
                    zoneForm.setExistingZone(true);
                    
                    zoneForm.setRefreshTree(true);
                }
                else
                {
                	getDao(ZoneDao.class).update(zoneForm.getZoneData());

                    messages.add("changezoneinfo",new ActionMessage("CFGW0053", zoneForm.getZoneID()));
                    
                    zoneForm.setExistingZone(true);
                    
                    
                }
            }
            else if (isDelete)
            {
                
                if (!zoneForm.isDeleteConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                getDao(ZoneDao.class).removeByKey(zoneForm.getZoneID());
                
                zoneForm.setRefreshTree(true);
                
                messages.add("changezoneinfo",new ActionMessage("CFGW0055", zoneForm.getZoneID()));
                
            }
            else if (!zoneForm.isExistingZone())
            {
                // Prompt the user to enter zone data
                messages.add("changezoneinfo",new ActionMessage("CFGW0054"));
                
            }
            else
            {
                // Just get the zone information
                Zone zoneData = getDao(ZoneDao.class).findByKey(zoneForm.getZoneID());
                
                zoneForm.setZoneData(zoneData);
                zoneForm.setExistingZone(true);
            }

            

        } 
        catch (ConfigurationServicesException e)
		{
        	errors.add("changezoneerror", new ActionError("Test"));
		}
        catch (SystemException e)
		{
        	errors.add("changezoneerror", new ActionError("Test",e.toString()));
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0009",e.toString());
            logActionError(ae);
            errors.add("changezoneerror", ae);

        }
        
		//	 forward failure or success depending on errors
    	return forward(mapping,request,errors,messages);

    }
}
