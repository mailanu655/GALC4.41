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
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.system.config.web.forms.ComponentStatusSettingsForm;

public class ComponentStatusSettingsAction extends ConfigurationAction {

	public static final String ERRORS_GROUP = "componentStatusSettingsError";

	public ComponentStatusSettingsAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ComponentStatusSettingsForm componentStatusSettingsForm = (ComponentStatusSettingsForm) form;

		// Only allow admins to edit component status
		if (request.isUserInRole("EditAdmin")) {
			componentStatusSettingsForm.setEditor(true);
		}
		else {
			componentStatusSettingsForm.setEditor(false);
		}

		try {
			if (!componentStatusSettingsForm.isEditor() &&
					(componentStatusSettingsForm.getApply() != null ||
					componentStatusSettingsForm.getDelete() != null ||
					componentStatusSettingsForm.getDeleteAll() != null)) {
				// No permissions to perform operations
				throw new ConfigurationServicesException("CFGW3400");
			}

			if (componentStatusSettingsForm.getLoadComponent() != null) {
				String newID = componentStatusSettingsForm.getLoadComponentID();
				if (newID == null || newID.length() == 0 || newID.equals("[Create new component]")) {
					componentStatusSettingsForm.setComponentID("");
					componentStatusSettingsForm.setStaticComponentID(false);
				} else {
					componentStatusSettingsForm.setComponentID(newID);
					componentStatusSettingsForm.setStaticComponentID(true);
				}
			} else if (componentStatusSettingsForm.getApply() != null) {
				ComponentStatus data = componentStatusSettingsForm.getComponentStatusData();
				data.setChangeUserId(request.getRemoteUser());

				// Update handles both inserts and updates
				getDao(ComponentStatusDao.class).update(data);

				// Once we do an update, don't change component ID
				componentStatusSettingsForm.setStaticComponentID(true);
				messages.add(ERRORS_GROUP,new ActionMessage("CFGW3402",data.getId().getStatusKey()));

				// @RL005 - SOX compliancy - changed records
				logger.info(buildLogRecord(getMessage("CFGW3502",request), request.getRemoteUser(), data.getId().getComponentId(), data));
			} else if (componentStatusSettingsForm.getDelete() != null) {
				ComponentStatus data = componentStatusSettingsForm.getComponentStatusData();
				data.setChangeUserId(request.getRemoteUser());
				getDao(ComponentStatusDao.class).remove(data);
				messages.add(ERRORS_GROUP,new ActionMessage("CFGW3403",data.getId().getStatusKey()));

				componentStatusSettingsForm.setStatusKey("");
				componentStatusSettingsForm.setStatusValue("");
				componentStatusSettingsForm.setDescription("");

				// @RL005 - SOX compliency - changed records
				logger.info(buildLogRecord(getMessage("CFGW3503",request), request.getRemoteUser(), 
						data.getId().getComponentId(), data));
			} else if (componentStatusSettingsForm.getDeleteAll() != null) { 
				if (componentStatusSettingsForm.isConfirmDeleteAll()) {
					getDao(ComponentStatusDao.class).removeAllByComponentId(componentStatusSettingsForm.getComponentID());

					// @RL005 - SOX compliancy - changed records
					logger.info(buildLogRecord("CFGW3516", request.getRemoteUser(), componentStatusSettingsForm.getComponentID(), (ComponentStatus) null)); // FIXME: sdw change these messages to ComponentStatus messages

					// Allow the user to specify a new component ID
					componentStatusSettingsForm.setStaticComponentID(false);
				}
			}
		} catch (ConfigurationServicesException e) {
			ActionError actionError = null;
			if (e.getAdditionalInformation() != null && e.getAdditionalInformation().length() > 0) {
				actionError = new ActionError(e.getMessage(),e.getAdditionalInformation());
			} else {
				actionError = new ActionError(e.getMessage(),e.toString());
			}

			logActionError(actionError);
			errors.add(ERRORS_GROUP, actionError);
		} catch (Exception e) {
			// Report the error using the appropriate name and ID.
			ActionError error = new ActionError("CFGW3315",e);
			errors.add("name", new ActionError("CFGW3315"));
			logActionError(error);
		} finally {
			// Get the list of reference components
			try {
				componentStatusSettingsForm.setReferenceComponents(getReferenceComponentIds());
			} catch (Exception e) {
				// Report the error using the appropriate name and ID.
				e.printStackTrace();
				ActionError error = new ActionError("CFGW3315",e);
				errors.add("name", new ActionError("CFGW3315"));
				logActionError(error);			
			}
		}

		//	 forward failure or success depending on errors
		return forward(mapping,request,errors,messages);
	}

	/**
	 * Helper method to build LogRecord
	 * @param messageId message ID
	 * @param user User responsible for change
	 * @param componentId component ID
	 * @param data component status data
	 * 
	 * @return LogRecord
	 */
	private String buildLogRecord(String message, String user, String componentId, ComponentStatus data) {
		StringBuffer userMessage = new StringBuffer(message);
		userMessage.append("component: \"").append(componentId).append("\"");

		if (data != null) {
			userMessage.append(", status key: \"").append(data.getId().getStatusKey()).append("\"");
			if(data.getStatusValue() != null) {
				userMessage.append(", value: \"").append(data.getStatusValue()).append("\"");				
			}
		}
		userMessage.append(" User : ");
		userMessage.append(user);
		return userMessage.toString();
	}

	private String getMessage(String messageId, HttpServletRequest request) {
		MessageResources resources = this.getResources(request);
		return resources.getMessage(messageId);
	}

	private List<String> getReferenceComponentIds() {
		List<ComponentStatus> componentStatuses = getDao(ComponentStatusDao.class).findAll();
		Set<String> componentIds = new HashSet<String>();
		for (ComponentStatus componentStatus : componentStatuses) {
			componentIds.add(componentStatus.getId().getComponentId());
		}
		List<String> ids = new ArrayList<String>(componentIds);
		Collections.sort(ids);
		return ids;
	}
}