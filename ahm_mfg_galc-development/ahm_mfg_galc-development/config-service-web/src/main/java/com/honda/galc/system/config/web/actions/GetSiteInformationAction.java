package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.entity.conf.Site;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.data.SessionConstants;
import com.honda.galc.system.config.web.forms.SiteForm;

/**
 * <h3>GetSiteInformationAction</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This Struts Action form is used to obtain Site information for
 * the specified site.
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
 * <TD>Feb 2, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class GetSiteInformationAction extends ConfigurationAction
          implements SessionConstants
{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        SiteForm siteform = (SiteForm)form;
        ActionMessages messages = new ActionMessages();
        
        String applyCommand = request.getParameter("apply");
        
        boolean deleteCommand = false;
        
        if (siteform.getDelete() != null &&
            siteform.getDelete().equalsIgnoreCase("delete"))
        {
           deleteCommand = true;    
        }
        
        if (request.isUserInRole("EditProcess"))
        {
            siteform.setEditor(true);
            siteform.setPlantEditor(true);
        }
        else 
        {
            siteform.setEditor(false);
            siteform.setPlantEditor(false);
        }
        
        try {
        	  HttpSession session = request.getSession();
        	  Site site = null;
        	  
        	  String siteName = siteform.getName(); //request.getParameter("siteName");
        	   
        	  if (siteName == null || siteName.length() == 0)
        	  {
        	      
        	      if (siteform.isCreateFlag())
        	      {
        	          throw new ConfigurationServicesException("CFGW0045");
        	      }
        	      else
        	      {
        	         // just display the first site information
        	         List<Site> siteList = ServiceFactory.getDao(SiteDao.class).findAll();
        	      
        	         if (siteList.size() > 0)
        	         {
        	           site = (Site)siteList.get(0);
        	           siteName = site.getSiteName();
        	         }
        	      }
        	      
        	  }
        	  
        	  if (applyCommand != null && applyCommand.equalsIgnoreCase("apply"))
        	  {
        	      
        	    if (!siteform.isEditor())
        	    {
        	        throw new ConfigurationServicesException("CFGW1000");
        	    }
        	    
        	    site = siteform.getSite();
        	    if (siteform.isCreateFlag())
        	    {
        	       // Creating a new site
        	        
        	    	ServiceFactory.getDao(SiteDao.class).insert(site);
        	        
        	        messages.add("changesiteinfo",new ActionMessage("CFGW0046", site.getSiteName()));
        	        
        	        siteform.setCreateFlag(false);
        	        siteform.setRefreshTree(true);
        	    }
        	    else
        	    {
        	  	   // Updating the site information
        	    	ServiceFactory.getDao(SiteDao.class).save(site);
        	    	
        	  	   messages.add("changesiteinfo",new ActionMessage("CFGW0002", site.getSiteName()));
        	    }
        	  }
        	  else if (deleteCommand)
        	  {
        	      if (!siteform.isEditor())
        	      {
        	          throw new ConfigurationServicesException("CFGW1000");
        	      }
        	      
        	      if (!siteform.isConfirmDelete())
        	      {
        	          throw new ConfigurationServicesException("CFGW1001");
        	      }
        	      
        	      ServiceFactory.getDao(SiteDao.class).remove(siteform.getSite());
        	      
        	      messages.add("changesiteinfo",new ActionMessage("CFGW0034", siteform.getName()));
        	      
        	      // clear the fields
        	      siteform.setName("");
        	      siteform.setDescription("");
        	      siteform.setRefreshTree(true);
        	      
        	  }
        	  else
        	  {
        	    // Just fetching data	
        	
        	  	
        	  	
        	  	// First, see if site info is cached in navigation data
        	  	if (site == null && session != null)
        	  	{
        	  	   List<Site> sites = (List<Site>)session.getAttribute(SITE_NODES);
        	  	   
        	  	   if(sites != null) {
	        	  	   for(Site newSite : sites) {
	        	  		 if (newSite.getSiteName().equals(siteName))
	     	  	     	{
	     	  	   	  		site = newSite;
	     	  	   	  		break;
	     	  	   	  	}
	     	  	   	    
	        	  	   
	        	  	   }
        	  	   }
        	  	}
        	  
        	  	// Obtain site data from database if necessary
        	  	if (site == null)
        	  	{	
        	  	   site = ServiceFactory.getDao(SiteDao.class).findByKey(siteform.getName());	
        	  	}
        	  	
        	  	if (site == null)
        	  	{
        	  		// Setup error information
        	  		errors.add("getsiteinfo",new ActionError("CFGW0001",siteName));
        	  		
        	  	}
        	  	else
        	  	{
        	  		// Store site data in form
        	  		// Form will be propagated to JSP
        	  		siteform.setSite(site);
        	  		
        	  	}
        	  }

        }
 
        catch (SystemException e)
		{
        	errors.add("getsiteinfo",new ActionError("test",e.getMessage()));
        	
        	// Make sure deletion is not treated as confirmed
        	siteform.setConfirmDelete(false);
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0047",e.toString());
            logActionError(ae);
            errors.add("getsiteinfo", ae);
            
        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
}