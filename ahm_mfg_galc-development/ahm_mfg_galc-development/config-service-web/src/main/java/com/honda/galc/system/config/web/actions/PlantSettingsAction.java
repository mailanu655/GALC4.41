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
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.forms.PlantForm;

/**
 * @version 	1.0
 * @author
 */
public class PlantSettingsAction extends ConfigurationAction

{

    /**
     * Constructor
     */
    public PlantSettingsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        PlantForm plantForm = (PlantForm) form;
        
        if (request.isUserInRole("EditProcess")) {
            
            plantForm.setEditor(true);
            plantForm.setDivisionEditor(true);
        }
        else 
        {
            plantForm.setEditor(false);
            plantForm.setDivisionEditor(false);
        }

        try {

        	if (plantForm.getCreateFlag() != null && plantForm.getCreateFlag().equals("true"))
        	{
        		// We are in the process of creating the site
        	    if (!plantForm.isEditor())
        	    {
        	        throw new ConfigurationServicesException("CFGW1000");
        	    }        	    
        		
        		// If name and description not supplied, forward to page
        		if (plantForm.getPlantName() != null && 
        			plantForm.getPlantDescription() != null &&
					plantForm.getPlantName().length() > 0 &&
					plantForm.getPlantDescription().length() > 0)
        		{
        			// Ok to create the plant
        			
        			Plant plant = plantForm.getPlant();
        			
        			ServiceFactory.getDao(PlantDao.class).save(plant);
        			
        			// Return to the page
        			plantForm.setCreateFlag("false");
        			request.setAttribute("existingflag","true");
        			messages.add("changeplantinfo",new ActionMessage("CFGW0003", plant.getId().getSiteName(),plant.getId().getPlantName()));
        			
        			plantForm.setRefreshTree(true);
        		}
        		else 
        		{
        			// Go back to page and get information
        			request.setAttribute("existingflag","false");
        			messages.add("changeplantinfo",new ActionMessage("CFGW0004"));
        			
        		}
        	}
            else if (plantForm.getApply() != null &&
            		 plantForm.getApply().equalsIgnoreCase("apply"))
            {
            	// We are updating plant information
        	    if (!plantForm.isEditor())
        	    {
        	        throw new ConfigurationServicesException("CFGW1000");
        	    }                
                
        	    ServiceFactory.getDao(PlantDao.class).update(plantForm.getPlant());
    			
            	// Return to the page
    			plantForm.setCreateFlag("false");
    			request.setAttribute("existingflag","true");
    			messages.add("changeplantinfo",new ActionMessage("CFGW0006", plantForm.getSiteName(),plantForm.getPlantName()));
            }
            else if (plantForm.getDelete() != null &&
                     plantForm.getDelete().equalsIgnoreCase("delete")) 
            {
        	    if (!plantForm.isEditor())
        	    {
        	        throw new ConfigurationServicesException("CFGW1000");
        	    }
        	    
        	    if (!plantForm.isDeleteConfirmed())
        	    {
        	        throw new ConfigurationServicesException("CFGW1001");
        	    }
        	    
        	    ServiceFactory.getDao(PlantDao.class).remove(plantForm.getPlant());
        	    
        	    messages.add("changeplantinfo",new ActionMessage("CFGW0033", plantForm.getPlantName()));
        	    
        	    // Clear the fields
        	    plantForm.setPlantName("");
        	    plantForm.setSiteName("");
        	    plantForm.setPlantDescription("");
        	    
        	    // Refresh the navigation tree
        	    plantForm.setRefreshTree(true);
        	    
            }
            else
            {
            	// We are displaying plant information
    			plantForm.setCreateFlag("false");
    			request.setAttribute("existingflag","true");
    			
    			Plant plantData = ServiceFactory.getDao(PlantDao.class).findById(plantForm.getSiteName(), plantForm.getPlantName());
    			
    			if (plantData == null)
    			{
    				errors.add("changeplanterror", new ActionError("CFGW0007",plantForm.getSiteName(),plantForm.getPlantName()));
    			}
    			else
    			{
    				plantForm.setPlant(plantData);

    			}
    			
            }

        }
        catch (SystemException e)
		{
        	errors.add("changeplanterror", new ActionError("test",e.toString()));
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add("changeplanterror", new ActionError("CFGW0005"));

        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
}
