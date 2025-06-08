package com.honda.galc.system.config.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.Site;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.data.SessionConstants;

/**
 * @version 	1.0
 * @author
 */
public class BuildProcessTreeAction extends ConfigurationAction
   implements SessionConstants

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors errors = new ActionErrors();
     
        try {

            List<Line> lines = ServiceFactory.getDao(LineDao.class).findAlLinesParents();
            
            List<Site> sites = ServiceFactory.getDao(SiteDao.class).findAllWithChildren();
            for(Site  site: sites) {
            	for(Plant plant: site.getPlants()) {
            		List<Division> divisions = new ArrayList<Division>();		
            		for(Division division: plant.getDivisions()) {
                    	Division div = ServiceFactory.getDao(DivisionDao.class).findWithChildren(division.getDivisionId());
            			List<Line> divlines = new ArrayList<Line>();
            			for(Line line: lines) {
            				if ((line.getDivisionId().trim().equals(division.getDivisionId().trim()))&&
            						(line.getPlantName().trim().equals(plant.getPlantName()))&&
            						(line.getSiteName().trim().equals(site.getSiteName())))  {
            					divlines.add(line);
            				}
            			}
            			div.setLines(divlines);
            			divisions.add(div);
            		}
            		plant.setDivisions(divisions);
            	}
            }
            
          	HttpSession session = request.getSession(false);
          	if (session == null)
          	{
          	    // Only create session if new session flag was passed in
          	    if (request.getParameter("newSession") != null)
          	    {
          	        session = request.getSession(true);
          	    }
          	    else
          	    {
//          	        throw new ConfigurationServicesException("CFGW0011");
          	    }
          	        
          	    
          	}
          	// Add the list to the session
          	session.setAttribute(SITE_NODES, sites);
          	

        }         
        catch (SystemException e)
		{
            e.printStackTrace(System.out);
        	errors.add("buildtreeerror", new ActionError("CFGW0009",e.toString()));
		}
        catch (Exception e) {

            e.printStackTrace();
        	// Report the error using the appropriate name and ID.
            errors.add("buildtreeerror", new ActionError("CFGW0009",e.toString()));

        }

        //      forward failure or success depending on errors
        return forward(mapping,request,errors);

    }
    

}
