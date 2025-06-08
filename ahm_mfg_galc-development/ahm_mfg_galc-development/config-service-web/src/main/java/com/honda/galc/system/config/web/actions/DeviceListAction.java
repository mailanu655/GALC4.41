package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;


/**
 * @version 	1.0
 * @author
 */
public class DeviceListAction extends ConfigurationAction

{


    private static final String ERRORS_GROUP = "deviceListErrors";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        
        String divID = request.getParameter("divisionID");
        if (divID == null)
        {
            divID = "*";
        }
        
        request.setAttribute("divID",divID);

        String processPointID = request.getParameter("processPointID");
        if (processPointID != null) {
        	request.setAttribute("processPointID",processPointID);
        }
        
        try {
        	List<Device> deviceList = null;
        	if (processPointID == null) 
                deviceList = divID.equals("*")?  ServiceFactory.getDao(DeviceDao.class).findAll() :
                		ServiceFactory.getDao(DeviceDao.class).findAllByDivisionId(divID);
        	else
        		deviceList = ServiceFactory.getDao(DeviceDao.class).findAllByProcessPointId(processPointID); 
            request.setAttribute("deviceList",new SortedArrayList<Device>(deviceList,"getClientId"));

        }
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test", e.toString()));
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
        return forward(mapping,request,errors,messages);

    }
}
