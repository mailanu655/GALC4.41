package com.honda.galc.system.config.web.actions;

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
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.entity.enumtype.DeviceType;

import static com.honda.galc.service.ServiceFactory.getDao;
import com.honda.galc.system.config.web.forms.OpcSettingsForm;
import com.honda.galc.util.SortedArrayList;

/**
 * @version 	1.0
 * @author
 */
public class OpcSettingsAction extends ConfigurationAction

{

    /**
     * Constructor
     */
    public OpcSettingsAction() {
	super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	OpcSettingsForm opcSettingsForm = (OpcSettingsForm) form;
	
    if (request.isUserInRole("EditDevice"))
    {
        opcSettingsForm.setEditor(true);
    }	


	try {

	    // do something here
		if (!opcSettingsForm.isNewInstance() &&
		    opcSettingsForm.getLoad() != null && 
			opcSettingsForm.getLoad().equalsIgnoreCase("load")) {
			
			// Load the data
			OpcConfigEntry data = getDao(OpcConfigEntryDao.class).findByKey(opcSettingsForm.getId());
			Device device = getDao(DeviceDao.class).findByKey(data.getDeviceId());
			if (data != null) {
			
				opcSettingsForm.setOpcConfigurationData(data,device);
			}
		}
		else if (opcSettingsForm.getSelectDevice() != null) {
			
			List<Device> devices = getDao(DeviceDao.class).findAllByDeviceType(DeviceType.OPC);
			opcSettingsForm.setDeviceList(new SortedArrayList<Device>(devices,"getClientId"));
		}
		else if (opcSettingsForm.getApply() != null &&
				 opcSettingsForm.getApply().equalsIgnoreCase("apply")) {
			OpcConfigEntry data = null;
			
			if (opcSettingsForm.getOpcInstanceName()==null || "".equals(opcSettingsForm.getOpcInstanceName().trim())){
				messages.add("opcsettings",new ActionMessage("CFGW3003"));
			}
			else if (opcSettingsForm.isNewInstance()) {
				
				data = getDao(OpcConfigEntryDao.class).insert(opcSettingsForm.getOpcConfigurationData());
				
				opcSettingsForm.setNewInstance(false);
				
				messages.add("opcsettings",new ActionMessage("CFGW3004"));
			}
			else
			{
				data = getDao(OpcConfigEntryDao.class).update(opcSettingsForm.getOpcConfigurationData());
				
				messages.add("opcsettings",new ActionMessage("CFGW3005"));
			}
			
			Device device = getDao(DeviceDao.class).findByKey(data.getDeviceId());
			if (data != null) {
				opcSettingsForm.setOpcConfigurationData(data,device);
			}
		}
		else if (opcSettingsForm.getDelete() != null && 
				 opcSettingsForm.getDelete().equalsIgnoreCase("Delete")) {
			
			getDao(OpcConfigEntryDao.class).removeByKey(opcSettingsForm.getId());
			messages.add("opcsettings",new ActionMessage("CFGW3006"));
			
		}

	} 
	catch (ConfigurationServicesException e)
	{
    	errors.add("opcsettings", new ActionError("Test",e.toString()));
	}
	catch (SystemException e)
	{
    	errors.add("opcsettings", new ActionError("Test",e.toString()));
	}
    
	catch (Exception e) {

	    // Report the error using the appropriate name and ID.
	    errors.add("opcsettings", new ActionError("CFGW3002",e));

	}

    // forward failure or success depending on errors
    return forward(mapping,request,errors,messages);

    }
}
