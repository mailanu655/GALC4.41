package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;


/**
 * @version 	1.0
 * @author
 */
public class ProcessPointListAction extends ConfigurationAction

{
    private static final String ERRORS_GROUP = "processPointListErrors";
    /**
     * Constructor
     */
    public ProcessPointListAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
         
        String lineID = request.getParameter("lineID");
        
        if (lineID == null)
        {
            lineID = "*";
        }
        
        request.setAttribute("lineID",lineID);

        try {
        	List<ProcessPoint> processPointList;
            if(lineID.equals("*")) processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAll();
            else {
            	Line line = ServiceFactory.getDao(LineDao.class).findByKey(lineID);
        	    processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllByLine(line);
            }
            
            request.setAttribute("processPointList",processPointList);

        }
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005"));

        }        

        // forward failure or success depending on errors
        return forward(mapping,request,errors);

    }
}
