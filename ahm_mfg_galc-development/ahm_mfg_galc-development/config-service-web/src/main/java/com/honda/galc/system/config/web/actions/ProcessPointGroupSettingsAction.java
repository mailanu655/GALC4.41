package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ProcessPointGroupDao;
import com.honda.galc.entity.conf.ProcessPointGroup;
import com.honda.galc.entity.conf.ProcessPointGroupId;
import com.honda.galc.system.config.web.forms.ProcessPointGroupSettingsForm;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 *
 */

public class ProcessPointGroupSettingsAction extends ConfigurationAction {
	
	private final static String ERRORS_GROUP = "processPointGroup";
	private final static String ADD = "ADD";
	private final static String DELETE = "DELETE";
    public ProcessPointGroupSettingsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            																		throws Exception {
       ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ProcessPointGroupSettingsForm settingsForm = (ProcessPointGroupSettingsForm) form;

        try {
        	String operation = settingsForm.getOperation();
        	if(ADD.equalsIgnoreCase(operation)) {
        		ProcessPointGroup ppGroup = new ProcessPointGroup(createProcessPointGroupId(settingsForm));
        		getDao(ProcessPointGroupDao.class).insert(ppGroup);
        	} else if(DELETE.equalsIgnoreCase(operation)) {
        		getDao(ProcessPointGroupDao.class).removeByKey(createProcessPointGroupId(settingsForm));
        	}
            
        } catch (ConfigurationServicesException e) {
            ActionError actionError = null;
            if (StringUtils.isEmpty(e.getAdditionalInformation())) {
            	actionError = new ActionError("", e.toString());
            } else {
            	actionError = new ActionError("", e.getAdditionalInformation());
            }
            
            logActionError(actionError);
     	    errors.add(ERRORS_GROUP, actionError);
 		} catch (SystemException e) {
        	errors.add(ERRORS_GROUP, new ActionError("", e.toString()));
		} catch (Exception e) {
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005", e.toString()));
        }  
        
        return forward(mapping, request, errors, messages);

    }
    
    private ProcessPointGroupId createProcessPointGroupId(ProcessPointGroupSettingsForm settingsForm) {
        short categoryCode = (short) settingsForm.getCategoryCode();
        String site = settingsForm.getSite() == null ? "" : settingsForm.getSite();
        String ppGroupName = settingsForm.getProcessPointGroupName() == null ? "" : settingsForm.getProcessPointGroupName();
        String processPointId = settingsForm.getProcessPointId() == null ? "" : settingsForm.getProcessPointId();
        
        ProcessPointGroupId groupId = new ProcessPointGroupId(categoryCode, site, ppGroupName, processPointId);
        return groupId;
    }
}
