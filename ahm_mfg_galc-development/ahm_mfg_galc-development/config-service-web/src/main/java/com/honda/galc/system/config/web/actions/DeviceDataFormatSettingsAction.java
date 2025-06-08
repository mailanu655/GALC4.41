package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.system.config.web.forms.DeviceDataFormatForm;

/**
 * @version 	1.0
 * @author
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class DeviceDataFormatSettingsAction extends ConfigurationAction
{
    protected final Map<String,String> tagTypes = new LinkedHashMap<String,String>();
    
    private static final String ERRORS_GROUP = "deviceDateFormatErrors";
    
    private static final String MESSAGES_GROUP = "updateDeviceDataFormatMessages";

    private String savedClientId = "";
    
    public DeviceDataFormatSettingsAction() {
        super();
        for(DeviceTagType type : DeviceTagType.values()) {
        	tagTypes.put(Integer.toString(type.getId()), type.toString());
    	}
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        
        DeviceDataFormatForm deviceDataFormatForm = (DeviceDataFormatForm) form;
        deviceDataFormatForm.setDeviceTagTypes(tagTypes);
        
        try {
            
            String operation = deviceDataFormatForm.getOperation();
            String clientID = deviceDataFormatForm.getClientID();

            if (operation != null && operation.equalsIgnoreCase("remove")
                    || (operation != null && operation.equalsIgnoreCase("apply"))) {

                //information message
                if (clientID == null || clientID.trim().length()==0) {
                    errors.add(ERRORS_GROUP, new ActionMessage("CFGW2014"));
                }
                String newClientID = deviceDataFormatForm.getNewClientID();
                String clone = deviceDataFormatForm.getClone();
                if ( (clone != null && clone.equalsIgnoreCase("clone"))
                        &&  ( newClientID == null || newClientID.trim().length()==0 )) {
                    errors.add(ERRORS_GROUP, new ActionMessage("CFGW2015"));
                }
                
                if (!request.isUserInRole("EditDevice"))
                {
                   deviceDataFormatForm.reset(mapping, request);
    	        	
    	    	    if (clientID != null && clientID.trim().length()>0) {
    	    	        List<DeviceFormat> list = getDao(DeviceFormatDao.class).findAllByDeviceId(clientID);
    	    	        deviceDataFormatForm.setClientID(clientID);
    	    	        deviceDataFormatForm.setData(list);
    	    	    }
    	
                }
            }

            if (operation != null && operation.equalsIgnoreCase("remove")) {
                boolean confirmed = deviceDataFormatForm.isDeleteConfirmed();
        	    if (clientID != null && confirmed) {
        	    	getDao(DeviceFormatDao.class).removeByClientId(clientID);
        	        messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2011", clientID));
        	    	clientID = null;
        	    }
        	}else if (operation != null && operation.equalsIgnoreCase("apply")) {
        	    List<DeviceFormat> list = deviceDataFormatForm.getData();
        	    String newClientID = deviceDataFormatForm.getNewClientID();
        	    
        	    List<DeviceFormat> originalList = getDao(DeviceFormatDao.class).findAllByDeviceId(clientID);
    	    	
        	    //copy the srouce client's device data formats to new client id
        	    if ( (newClientID != null && newClientID.trim().length()>0 ) 
        	            && ( clientID != null && clientID.trim().length()>0) ){
        	    	List<DeviceFormat> clonedList = new ArrayList<DeviceFormat>();
        	    	for(DeviceFormat deviceFormat : originalList) {
        	    		DeviceFormat newItem = deviceFormat.clone();
        	    		newItem.getId().setClientId(newClientID);
        	    		clonedList.add(newItem);
        	    	}
        	    	getDao(DeviceFormatDao.class).insertAll(clonedList);
        	    	clientID = newClientID;
            	    messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2012", clientID));
        	    }else if ( (clientID != null && clientID.trim().length()>0)
        	            && (newClientID == null || newClientID.trim().length()==0)) {
        	    	
        	    	List<DeviceFormat> removedList = new ArrayList<DeviceFormat>();
        	    	
        	    	for(DeviceFormat deviceFormat : originalList) {
        	    		if(!contains(deviceFormat,list)) removedList.add(deviceFormat);
        	    	}
        	    	
        	    	if(!removedList.isEmpty())getDao(DeviceFormatDao.class).removeAll(removedList);
        	    	String dcTag = findDuplicate(list);
        	    	if(dcTag == null) {
        	    		getDao(DeviceFormatDao.class).saveAll(list);
        	    		messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2010", clientID));
        	    	}else 
        	    		errors.add(ERRORS_GROUP,new ActionError("CFGW2016",clientID,dcTag));
        	    	
        	    }
        	}

            deviceDataFormatForm.setClientID(clientID);
            
            if (StringUtils.isEmpty(clientID)) {
            	deviceDataFormatForm.reset(mapping, request);
            	savedClientId = null;
            } else if (deviceDataFormatForm.getDataType() == null || !clientID.equals(savedClientId) || (operation != null && operation.equalsIgnoreCase("cancel"))) {
    	    	savedClientId = clientID;
    	    	List<DeviceFormat> list = getDao(DeviceFormatDao.class).findAllByDeviceId(clientID);
	    	    deviceDataFormatForm.setData(list);
    	    }
    	    
    	    if (request.isUserInRole("EditDevice"))
            {
                deviceDataFormatForm.setEditor(true);
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
            e.printStackTrace();
        	errors.add(ERRORS_GROUP, new ActionError("CFGW0005",getRootCauseMessage(e)));
        }
        
        List<Object[]> clientsList = getDao(DeviceFormatDao.class).findDistinctClientId();        
        request.setAttribute("clients",clientsList);

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
        
    }
    
    private boolean contains(DeviceFormat deviceFormat, List<DeviceFormat> deviceFormats) {
    	for(DeviceFormat item : deviceFormats) {
    		if(item.getId().getClientId().equals(deviceFormat.getId().getClientId()) &&
    				                             item.getTag().equals(deviceFormat.getTag()))
    				   return true;
    	}
    	return false;
    }
    
    private String findDuplicate(List<DeviceFormat> deviceFormats) {
    	Set<String> dcTags = new HashSet<String>();
    	for(DeviceFormat deviceFormat : deviceFormats) {
    		if(!dcTags.add(deviceFormat.getId().getTag())) return deviceFormat.getId().getTag();
    	}
    	return null;
    }
    
}
