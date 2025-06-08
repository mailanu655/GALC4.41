package com.honda.galc.system.config.web.actions;

import java.util.List;
import java.util.TreeMap;

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
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.forms.DeviceForm;

/**
 * <H3>DeviceSettingsAction</H3> 
* <h3>Class description</h3>
* <h4>Description</h4>
* <P>This is the Struts action form for the device settings page.</P>
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
* <TD>Oct 2, 2007</TD>
* <TD></TD>
* <TD>@JM200731</TD>
* <TD>Added support for opc linkage</TD>
* </TR>
* </TABLE>
 */
public class DeviceSettingsAction extends ConfigurationAction

{
    private final TreeMap<String,String> deviceTypeMap = new TreeMap<String,String>();
    
    private static final String ERRORS_GROUP = "deviceErrors";
    
    private static final String MESSAGES_GROUP = "updateDeviceMessages";
    
    public DeviceSettingsAction() {
        super();
        for(DeviceType type : DeviceType.values()) {
        	deviceTypeMap.put(Integer.toString(type.getId()), type.getName());
        }
    }
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        DeviceForm deviceForm = (DeviceForm) form;
        
        boolean loadopc = false;

        try {
            
            deviceForm.setDeviceTypeMap(deviceTypeMap);
            
            if (request.isUserInRole("EditDevice"))
            {
                deviceForm.setEditor(true);
            }
            
            if (deviceForm.isCreateFlag()) {

                // option is selected.
                if (deviceForm.getClientID() != null && deviceForm.getClientID().length() > 0) {
                    
                    // Ok to create division
                    Device data = deviceForm.getDeviceData();

                    ServiceFactory.getDao(DeviceDao.class).insert(data);
                    
                    // We are creating a new device
                    deviceForm.setCreateFlag(false);
                    request.setAttribute("existingflag", "true");
                } else {
                    // Go back to page and get required fields.
                    request.setAttribute("existingflag", "false");
                    messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2001"));
                }

 
            } else if (deviceForm.getApply() != null && deviceForm.getApply().equalsIgnoreCase("apply")) {
                // We are updating an existing division
                if (!request.isUserInRole("EditDevice"))
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                
                Device data = deviceForm.getDeviceData();
                ServiceFactory.getDao(DeviceDao.class).update(data);
                
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2005" ));
                
                deviceForm.setCreateFlag(false);
                request.setAttribute("existingflag", "true");
                
                loadopc = true;

            } else if (deviceForm.getApply() != null && deviceForm.getApply().equalsIgnoreCase("delete")) {
                //deleting the device 
                Device device = deviceForm.getDeviceData();
                ServiceFactory.getDao(DeviceDao.class).remove(device);
                
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0057", deviceForm.getClientID()));
                deviceForm.reset(mapping, request);
                deviceForm.setDeleteConfirmed(true);
                
            } else if (deviceForm.getClientID() != null && deviceForm.getClientID().length()>0){
                // We are obtaining device data
                String clientID = deviceForm.getClientID();
                Device data = ServiceFactory.getDao(DeviceDao.class).findByKey(clientID);

                deviceForm.setDeviceData(data);

                deviceForm.setCreateFlag(false);
                request.setAttribute("existingflag", "true");
                
                loadopc = true;

            }

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
            errors.add(ERRORS_GROUP, new ActionError("CFGW2009",e.toString()));
        }
        
        // @JM200731
        if (loadopc) {
        	
        	try {
        		List<OpcConfigEntry> opcList= ServiceFactory.getDao(OpcConfigEntryDao.class).findAllByDeviceId(deviceForm.getClientID());
        		
        		deviceForm.setOpcConfigurationDataList(opcList);
        	}
        	catch (Exception e) {
        		
        	}
        	
        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
}
