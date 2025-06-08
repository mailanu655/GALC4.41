package com.honda.galc.system.config.web.actions;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.qics.QicsDefectInfoManager;
import com.honda.galc.system.config.web.enumtype.UserRoles;
import com.honda.galc.system.config.web.forms.PropertySettingsForm;
import com.honda.galc.system.config.web.utilities.MultiServerPropertyRefresher;
import com.honda.galc.util.CommonUtil;

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
public class PropertySettingsAction extends ConfigurationAction

{

	public static final String ERRORS_GROUP = "propertySettingsError";

	
    /**
     * Constructor
     */
    public PropertySettingsAction() {
	super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	ActionForward forward = new ActionForward(); // return value
	PropertySettingsForm propertySettingsForm = (com.honda.galc.system.config.web.forms.PropertySettingsForm) form;
	
	if (request.isUserInRole(UserRoles.EDIT_ADMIN.getName())
		|| request.isUserInRole(UserRoles.EDIT_PROCESS_POINT.getName())
		|| request.isUserInRole(UserRoles.EDIT_USERS.getName())
		|| request.isUserInRole(UserRoles.EDIT_TERMINAL.getName())
		|| request.isUserInRole(UserRoles.EDIT_DEVICE.getName())
		|| request.isUserInRole(UserRoles.EDIT_APPLICATION.getName())
		|| request.isUserInRole(UserRoles.EDIT_PROCESS.getName())
		|| request.isUserInRole(UserRoles.EDIT_APPLICATION.getName())
		|| request.isUserInRole(UserRoles.EDIT_ACL.getName())
		) {
		propertySettingsForm.setEditor(true);
	}else {
		propertySettingsForm.setEditor(false);
	}
	
	try {
		
		if (!propertySettingsForm.isEditor() &&
			 (propertySettingsForm.getApply() != null ||
			  propertySettingsForm.getDelete() != null ||
			  propertySettingsForm.getDeleteAll() != null ||
			  propertySettingsForm.getLoadFromTemplate() != null ||
			  propertySettingsForm.getRefresh() != null)) {
			
			// No permissions to perform operations
			throw new ConfigurationServicesException("CFGW3100");
		}
		
		//SUBMIT FORM
		if (propertySettingsForm.getLoadComponent() != null) {
			//load properties action
			
			String newID = propertySettingsForm.getLoadComponentID();
			if (newID == null || newID.length() == 0 || newID.equals("[Create new component]")) {
				//new component
				propertySettingsForm.setComponentID("");
				propertySettingsForm.setStaticComponentID(false);
			}
			else
			{
				propertySettingsForm.setComponentID(newID);
				propertySettingsForm.setStaticComponentID(true);
			}
			
			propertySettingsForm.setComponentType(PropertySettingsForm.COMPONENT_TYPE_MISC_COMPONENT);
			
		}
		else if (propertySettingsForm.getApply() != null) {
			//apply property changes action
			
			ComponentProperty data = propertySettingsForm.getPropertyData();
			
			data.setChangeUserId(request.getRemoteUser());
			
			// Update handles both inserts and updates
			getDao(ComponentPropertyDao.class).update(data);
			
			// Once we do an update, don't change component ID
			propertySettingsForm.setStaticComponentID(true);
			
			messages.add(ERRORS_GROUP,new ActionMessage("CFGW3102",data.getId().getPropertyKey()));

			// @RL005 - SOX compliency - changed records
			logger.info(buildLogRecord(getMessage("CFGW3202",request), request.getRemoteUser(), 
					data.getId().getComponentId(), data));
		}
		else if (propertySettingsForm.getDelete() != null) {
			//delete property action
			
			ComponentProperty data = propertySettingsForm.getPropertyData();
			data.setChangeUserId(request.getRemoteUser());
			getDao(ComponentPropertyDao.class).remove(data);
			
			messages.add(ERRORS_GROUP,new ActionMessage("CFGW3103",data.getId().getPropertyKey()));
			
			propertySettingsForm.setPropertyKey("");
			propertySettingsForm.setPropertyValue("");
			propertySettingsForm.setDescription("");
			
			// @RL005 - SOX compliency - changed records
			logger.info(buildLogRecord(getMessage("CFGW3203",request), request.getRemoteUser(), 
					data.getId().getComponentId(), data));
		}
		else if (propertySettingsForm.getRename() != null) {
			//rename property action
			
			final String componentId = propertySettingsForm.getComponentID();
			final String oldPropertyKey = propertySettingsForm.getPropertyKey();
			ComponentProperty oldData = getDao(ComponentPropertyDao.class).findByKey(new ComponentPropertyId(componentId, oldPropertyKey));
			if (oldData == null) {
				// property does not exist for the component id
				messages.add(ERRORS_GROUP,new ActionMessage("CFGW3017", oldPropertyKey, componentId));
			} else {
				final String newPropertyKey = propertySettingsForm.getRenamePropertyKey();
				ComponentProperty newData = oldData.clone();
				newData.setChangeUserId(request.getRemoteUser());
				newData.getId().setPropertyKey(newPropertyKey);
				getDao(ComponentPropertyDao.class).save(newData);
				getDao(ComponentPropertyDao.class).remove(oldData);
				messages.add(ERRORS_GROUP,new ActionMessage("CFGW3105", oldPropertyKey, newPropertyKey));
				propertySettingsForm.setPropertyKey(newPropertyKey);
			
				// @RL005 - SOX compliance - changed records
				logger.info(buildLogRecord(getMessage("CFGW3205",request), request.getRemoteUser(), 
						newData.getId().getComponentId(), newData));
			}
		}
		else if (propertySettingsForm.getDeleteAll() != null) { 
			//delete all property action
			
			if (propertySettingsForm.isConfirmDeleteAll()) {
				
				getDao(ComponentPropertyDao.class).removeAllByComponentId(propertySettingsForm.getComponentID());
				
				// @RL005 - SOX compliency - changed records
				logger.info(buildLogRecord("CFGW3216", request.getRemoteUser(), 
						propertySettingsForm.getComponentID(), (ComponentProperty)null));
				if (propertySettingsForm.getComponentType() == PropertySettingsForm.COMPONENT_TYPE_MISC_COMPONENT) {
					
					// Allow the user to specify a new component ID
					propertySettingsForm.setStaticComponentID(false);
				}
				
			}
			
		}
		else if (propertySettingsForm.getLoadFromTemplate() != null) {
			List<ComponentProperty> originalProperties = getDao(ComponentPropertyDao.class).findAllByComponentId(propertySettingsForm.getComponentID());
			List<ComponentProperty> otherProperties = getDao(ComponentPropertyDao.class).findAllByComponentId(propertySettingsForm.getReferenceComponentID());
			
			List<ComponentProperty> mergedProperties = new ArrayList<ComponentProperty>();
			
			for(ComponentProperty propertyData : otherProperties) {
				ComponentProperty clone = propertyData.clone();
				clone.getId().setComponentId(propertySettingsForm.getComponentID());
				clone.setCreateTimestamp(CommonUtil.getTimestampNow());
				if(!contains(propertyData,originalProperties)) mergedProperties.add(clone);
			}
			
			getDao(ComponentPropertyDao.class).insertAll(mergedProperties);
			
			// @RL005 - SOX compliency - changed records
			logger.info(buildLogRecord(getMessage("CFGW3216",request), request.getRemoteUser(), 
					propertySettingsForm.getComponentID(), propertySettingsForm.getReferenceComponentID()));
			
			messages.add(ERRORS_GROUP,new ActionMessage("CFGW3016", propertySettingsForm.getComponentID(), propertySettingsForm.getReferenceComponentID())); 
		}
		else if (propertySettingsForm.getRefresh() != null) { 
			// Refresh properties
			PropertyService.refreshComponentProperties(propertySettingsForm.getComponentID());
			if(isHeadlessProcessPoint(propertySettingsForm.getComponentID()))
				QicsDefectInfoManager.getInstance().refresh(propertySettingsForm.getComponentID());
			
			messages.add(ERRORS_GROUP,new ActionMessage("CFGW3110", propertySettingsForm.getComponentID())); 
			
			// attempt to make refresh in cluster
			boolean isOk = MultiServerPropertyRefresher.refreshOtherNodeProperties(propertySettingsForm.getComponentID());
			if (isOk) {
				messages.add(ERRORS_GROUP, new ActionMessage("CFGW3302"));
			}else {
				messages.add(ERRORS_GROUP, new ActionMessage("CFGW3301"));
			}
		}
		
	} 

	catch (ConfigurationServicesException e)
	{
        ActionError actionError = null;
        if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0)
        {
           actionError = new ActionError(e.getMessage(),e.getAdditionalInformation());
        } 
        else
        {
           actionError = new ActionError(e.getMessage(),e.toString());
        }
        
        logActionError(actionError);
 	    errors.add(ERRORS_GROUP, actionError);	    
       
	}
	catch (Exception e) {

	    // Report the error using the appropriate name and ID.
		ActionError error = new ActionError("CFGW3015",e);
	    errors.add("name", new ActionError("CFGW3015"));
	    
	    logActionError(error);

	}
	finally {
		
		
		// Get the list of reference components
		try {
			
			
			propertySettingsForm.setReferenceComponents(getReferenceComponentIds());
		}
		catch (Exception e) {
		    // Report the error using the appropriate name and ID.
			e.printStackTrace();
			ActionError error = new ActionError("CFGW3015",e);
		    errors.add("name", new ActionError("CFGW3015"));
		    
		    logActionError(error);			
		}
	}

		//	 forward failure or success depending on errors
    	return forward(mapping,request,errors,messages);


    }
    
    private boolean contains(ComponentProperty property, List<ComponentProperty> properties) {
    	for(ComponentProperty item : properties) {
    		if(item.getId().getPropertyKey().equals(property.getId().getPropertyKey())) return true;
    	}
    	return false;
    }
    
	private boolean isHeadlessProcessPoint(String componentID) {
		try {
			ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(componentID);
			if (processPoint != null && 
				ServiceFactory.getDao(TerminalDao.class).findFirstByProcessPointId(componentID) == null)

				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
    
	/**
     * Helper method to build LogRecord
     * @param messageId message ID
     * @param user User responsible for change
     * @param componentId component ID
     * @param data property data
     * 
     * @return LogRecord
     */
    private String buildLogRecord(String message, String user, String componentId, ComponentProperty data)
    {
		
		StringBuffer userMessage = new StringBuffer(message);
		userMessage.append("component: \"").append(componentId).append("\"");
		
		if(data != null)
		{
			userMessage.append(", property key: \"").append(data.getId().getPropertyKey()).append("\"");
			if(data.getPropertyValue() != null) {
				userMessage.append(", value: \"").append(data.getPropertyValue()).append("\"");				
			}
		}
		
		userMessage.append(" User : ");
		userMessage.append(user);
		return userMessage.toString();
    }
    
    /**
     * Helper method to build LogRecord
     * 
     * @param messageId
     * @param user
     * @param destComponentId
     * @param srcComponentID
     * 
     * @return LogRecord
     */
    private String buildLogRecord(String message, String user, String destComponentId, 
    		String srcComponentID) {
    	
		StringBuffer userMessage = new StringBuffer(message);
		userMessage.append("\"").append(srcComponentID);
		userMessage.append("\" -> \"").append(destComponentId).append("\"");
		userMessage.append(" User : ");
		userMessage.append(user);
		return userMessage.toString();
		
	}
    
    private String getMessage(String messageId, HttpServletRequest request) {
    	MessageResources resources = this.getResources(request);
    	return resources.getMessage(messageId);
    }
    
    private List<String> getReferenceComponentIds() {
    	List<ComponentProperty> properties = getDao(ComponentPropertyDao.class).findAll();
    	Set<String> componentIds = new HashSet<String>();
    	for(ComponentProperty property : properties) {
    		componentIds.add(property.getId().getComponentId());
    	}
    	List<String> ids = new ArrayList<String>(componentIds);
    	Collections.sort(ids);
    	return ids;
    }
    
    

}
