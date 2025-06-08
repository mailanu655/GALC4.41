package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.forms.LineForm;


/**
 * @version 	1.0
 * @author
 */
public class LineSettingsAction extends ConfigurationAction

{

    /**
     * Constructor
     */
    public LineSettingsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        LineForm lineForm = (LineForm) form;
        
        lineForm.setEditor(request.isUserInRole("EditProcess"));

        try {

            if (lineForm.isCreateFlag() &&
                lineForm.getApply() != null && lineForm.getApply().equalsIgnoreCase("apply"))
            {
            	lineForm.setMissingGpcsDataMessage("");

                // We are creating a new line
                
                // Verify editor
                if (!lineForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                
    			// Set existing flag to false before verifying - it may end up in exception
                request.setAttribute("existingflag","false");
 
                validateCreate(lineForm);
                
                Line line = lineForm.getLineData();
                
                ServiceFactory.getDao(LineDao.class).insert(line);

                GpcsDivisionDao dao = ServiceFactory.getDao(GpcsDivisionDao.class);

                List<Object[]> gpcsRecords = dao.findLineInfoAndGpcsData(lineForm.getLineID());

                if(gpcsRecords!=null && gpcsRecords.size()>0) {
                	lineForm.setMissingGpcsDataMessage("");
                }else{
                	lineForm.setMissingGpcsDataMessage("Missing Division to GPCS relationship - needed to determine line, shift and Production Date.");
                }
                // Return to the page
    			lineForm.setCreateFlag(false);
    			request.setAttribute("existingflag","true");
    			lineForm.setRefreshTree(true);
    			messages.add("changelineinfo",new ActionMessage("CFGW0012", lineForm.getLineID(), lineForm.getDivisionID()));
            }
            else if (lineForm.getApply() != null && lineForm.getApply().equalsIgnoreCase("apply"))
            {
                
                // Verify editor
                if (!lineForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                
                validateUpdate(lineForm);
                
                GpcsDivisionDao dao = ServiceFactory.getDao(GpcsDivisionDao.class);

                List<Object[]> gpcsRecords = dao.findLineInfoAndGpcsData(lineForm.getLineID());
                
                if(gpcsRecords!=null && gpcsRecords.size()>0) {
                	lineForm.setMissingGpcsDataMessage("");
                }else{
                	lineForm.setMissingGpcsDataMessage("Missing Division to GPCS relationship - needed to determine line, shift and Production Date.");
                }
                
                Line line = lineForm.getLineData();
                
                ServiceFactory.getDao(LineDao.class).save(line);
                
                lineForm.setCreateFlag(false);
    			request.setAttribute("existingflag","true");
    			messages.add("changelineinfo",new ActionMessage("CFGW0015", lineForm.getLineID()));
            }
            else if (lineForm.getDelete() != null && lineForm.getDelete().equalsIgnoreCase("delete"))
            {
                // Verify editor
                if (!lineForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");      
                }
                
                if (!lineForm.isDeleteConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                
                ServiceFactory.getDao(LineDao.class).removeByKey(lineForm.getLineID());
                
                messages.add("changelineinfo",new ActionMessage("CFGW0056", lineForm.getLineID()));
    			lineForm.setDivisionName("");
    			lineForm.setLineDescription("");
    			lineForm.setLineName("");
    			lineForm.setMaxInventory(0);
    			lineForm.setMinInventory(0);
    			lineForm.setStdInventory(0);
    			
    			lineForm.setRefreshTree(true);
            }
         
            else
            {
                
            	setLine(lineForm);
                
                lineForm.setCreateFlag(false);
                request.setAttribute("existingflag", "true");
            }

        }
        catch (ConfigurationServicesException e)
		{
            if (e.getAdditionalInformation() != null)
            {
            	errors.add("changelineerror", new ActionError(e.getMessage() ,e.getAdditionalInformation()));
            }
            else
            {
            	errors.add("changelineerror", new ActionError(e.getMessage()));
            }   
		}catch (SystemException e)
		{
            if (e.getAdditionalInformation() != null)
            {
                errors.add("changelineerror", new ActionError(e.getMessage(), e.getAdditionalInformation()));
            }
            else
            {
        	   errors.add("changelineerror", new ActionError(e.getMessage()));
            }   
		}
        
         
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add("changelineerror", logActionError(new ActionError("CFGW0016",e.toString())));

        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
    
    private void verifyLineInformation(LineForm lineForm)
       throws ConfigurationServicesException
       {
        // Verify basic information 
        if (lineForm.getLineName() == null || lineForm.getLineName().length() == 0)
        {
            throw new ConfigurationServicesException("CFGW0013");
        }
        
        if (lineForm.getStdInventory() <= lineForm.getMinInventory() ||
            lineForm.getMaxInventory() <= lineForm.getStdInventory())
        {
            throw new ConfigurationServicesException("CFGW0014");
        }
        
        
    }
    
    protected void validateCreate(LineForm lineForm) {
    	if (StringUtils.isBlank(lineForm.getLineID())) {
    		throw new ConfigurationServicesException("CFGW0058");
    	}
    	
    	verifyLineInformation(lineForm);
    	
        LineDao dao = ServiceFactory.getDao(LineDao.class);
		Line existingLine = dao.findByKey(lineForm.getLineID());
		if (existingLine != null) {
			throw new ConfigurationServicesException("CFGW0059");
		}

		existingLine = dao.findByLineName(lineForm.getLineName());
		if (existingLine != null) {
			if (StringUtils.trim(existingLine.getDivisionId()).equals(StringUtils.trim(lineForm.getDivisionID()))) {
				throw new ConfigurationServicesException("CFGW0060");
			}
		}
		validatePreviousLine(lineForm);
    }
    
    protected void validateUpdate(LineForm lineForm) {
    	
    	verifyLineInformation(lineForm);
    	
    	LineDao dao = ServiceFactory.getDao(LineDao.class);
    	Line existingLine = dao. findByLineName(lineForm.getLineName());
		if (existingLine != null && !existingLine.getLineId().equals(StringUtils.trim(lineForm.getLineID()))) {
			if (StringUtils.trim(existingLine.getDivisionId()).equals(StringUtils.trim(lineForm.getDivisionID()))) {
				throw new ConfigurationServicesException("CFGW0060");
			}
		}
		validatePreviousLine(lineForm);
    }
    
    private void setLine(LineForm lineForm) {
    	Line line = ServiceFactory.getDao(LineDao.class).findByKey(lineForm.getLineID());
    	// We are simply obtaing line data
		line.setProcessPointListCount(ServiceFactory.getDao(ProcessPointDao.class).countByLine(line));
        
        lineForm.setLineData(line);
    	
        GpcsDivisionDao dao = ServiceFactory.getDao(GpcsDivisionDao.class);

        List<Object[]> gpcsRecords = dao.findLineInfoAndGpcsData(lineForm.getLineID());
        
        if(gpcsRecords!=null && gpcsRecords.size()>0) {
        	lineForm.setMissingGpcsDataMessage("");
        }
        
        
    }

//    Validation of previous lines 
//    throws ConfigurationServicesException when invalid
    private void validatePreviousLine(LineForm lineForm) {
        LineDao dao = ServiceFactory.getDao(LineDao.class);
		String previousLines = lineForm.getPreviousLinesText();
		if (!StringUtils.trimToEmpty(previousLines).equals("")) {
			String[] lines = previousLines.split(",");
			for(String line : lines) {
				if(line.length() > 32) {	// not longer than 32 characters
					throw new ConfigurationServicesException("CFGW0061");
				}
				Line chkLine = dao.findByKey(line);
				if(chkLine == null) {	// check if exists
					throw new ConfigurationServicesException("CFGW0061");
				}
			}
		}
    }
}
