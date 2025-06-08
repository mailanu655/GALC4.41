package com.honda.galc.system.config.web.actions;

import java.util.ArrayList;
import java.util.List;
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
import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.conf.TerminalTypeDao;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.conf.TerminalType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.forms.TerminalForm;

/**
 * @version 1.0
 * @author
 */
public class TerminalSettingsAction extends ConfigurationAction
{
	private final TreeMap<Integer, String> terminalTypeMap = new TreeMap<Integer, String>();
	
	public TerminalSettingsAction() {
		super();
        for(TerminalType tType : ServiceFactory.getDao(TerminalTypeDao.class).findAll()) {
        	terminalTypeMap.put(tType.getTerminalFlag(), tType.getName());
        }
	}
	
	private static final String ERRORS_GROUP = "terminalErrors";

	private static final String MESSAGES_GROUP = "updateTerminalMessages";

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        TerminalForm terminalForm = (TerminalForm) form;
        ActionMessages messages = new ActionMessages();

        try {
            String operation = terminalForm.getOperation();
            
            if (request.isUserInRole("EditTerminal"))
            {
                terminalForm.setEditor(true);
            }else{
                terminalForm.setEditor(false);
            }
            
            if(operation != null && operation.equalsIgnoreCase("apply")) {
                
                if(!request.isUserInRole("EditTerminal")) {
                    throw new ConfigurationServicesException("CFGW1000");
                }
				
                terminalForm.setTerminalTypeMap(terminalTypeMap);
                terminalForm.setHostName(StringUtils.trim(terminalForm.getHostName()));
                
                Terminal terminal = new Terminal();
                terminal.setHostName(terminalForm.getHostName());
                terminal.setDivisionId(terminalForm.getDivisionID());
                terminal.setIpAddress(terminalForm.getIpAddress());
                terminal.setPort(terminalForm.getPort());
                terminal.setLocatedProcessPointId(terminalForm.getLocatedProcessPointID());
                terminal.setRouterPort(terminalForm.getRouterPort());
                terminal.setTerminalDescription(terminalForm.getTerminalDescription());
                terminal.setAfTerminalFlag(terminalForm.getAfTerminalFlag());
                terminal.setAutoUpdateIpAddress(terminalForm.isAutoUpdateIpFlag()?(short)1:(short)0);
                
                //initialize related applications  
                String[] appIDs = terminalForm.getApplicationID();
                String defaultApp = terminalForm.getDefaultApplication();
                List<ApplicationByTerminal> apps = null;
                if (appIDs != null) {
                    apps = new ArrayList<ApplicationByTerminal>();
                    for(int i=0;i<appIDs.length;i++) {
                        ApplicationByTerminal applicationByTerminal = new ApplicationByTerminal(appIDs[i],terminalForm.getHostName());
                        if (appIDs[i].equalsIgnoreCase(defaultApp)) {
                        	applicationByTerminal.setDefaultApplicationFlag((short) 1);
                        }
                        apps.add(applicationByTerminal);
                    }
                }
               
                if (terminalForm.getCreateFlag() != null && terminalForm.getCreateFlag().equalsIgnoreCase("true")) {
                    //create a terminal
                	if(terminal.getHostName().length()>0){
                    	ServiceFactory.getDao(TerminalDao.class).save(terminal);
                        if(apps != null && !apps.isEmpty()) ServiceFactory.getDao(ApplicationByTerminalDao.class).saveAll(apps);
                        messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0065", terminalForm.getHostName()));
                        terminalForm.setCreateFlag("false");
                	}
                	else{
                		messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0068"));
                	}
                }else{
                    //update terminal
                	ServiceFactory.getDao(TerminalDao.class).save(terminal);
                	List<ApplicationByTerminal> originalApps = ServiceFactory.getDao(ApplicationByTerminalDao.class).findAllByTerminal(terminalForm.getHostName());
                	List<ApplicationByTerminal> removeApps = new ArrayList<ApplicationByTerminal>();
                	for(ApplicationByTerminal app : originalApps) {
                		if(!contains(app,apps))removeApps.add(app);
                	}
                	if(!removeApps.isEmpty()) ServiceFactory.getDao(ApplicationByTerminalDao.class).removeAll(removeApps);
                	if(apps != null && !apps.isEmpty()) ServiceFactory.getDao(ApplicationByTerminalDao.class).saveAll(apps);
                    messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0066", terminalForm.getHostName()));
                }
            }else if (operation != null && 
                    operation.equalsIgnoreCase("delete"))
            {
                if (!terminalForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }

                if (!terminalForm.isDeleteConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                ServiceFactory.getDao(ApplicationByTerminalDao.class).removeById(terminalForm.getHostName());
                
                ServiceFactory.getDao(TerminalDao.class).removeByKey(terminalForm.getHostName());
                
                messages.add(MESSAGES_GROUP, new ActionMessage("CFGW0067", terminalForm.getHostName()));
                
                terminalForm.reset(mapping, request);
            }

        } 
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test"));
		}
        catch (SystemException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError("Test",e.toString()));
		}
        
        catch (Exception e) {
        	e.printStackTrace();
            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
        }  
        
		try {
			// we should initialize terminal settings in spite of update problems
			initialTerminalInformation(request, terminalForm);
		} catch (Exception e) {
			e.printStackTrace();
            // Report the error using the appropriate name and ID.
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005", e.toString()));
		}        

		//	 forward failure or success depending on errors
    	return forward(mapping,request,errors,messages);

    }

	private boolean contains(ApplicationByTerminal app,
			List<ApplicationByTerminal> apps) {
		if (apps == null)
			return false;
		for (ApplicationByTerminal appByTerminal : apps) {
			if (appByTerminal.getId().equals(app.getId()))
				return true;
		}
		return false;
	}

	private void initialTerminalInformation(HttpServletRequest request, TerminalForm terminalForm) 
			throws SystemException, ConfigurationServicesException {

		String hostName = StringUtils.trim(terminalForm.getHostName());
		// get the terminal information with specified terminal name
		if (hostName != null && hostName.length() > 0) {

			request.setAttribute("existingflag", "true");

			Terminal terminal = ServiceFactory.getDao(TerminalDao.class).findByKey(hostName);
			if (terminal != null) {
				terminalForm.setIpAddress(terminal.getIpAddress());
				terminalForm.setPort(terminal.getPort());
				terminalForm.setLocatedProcessPointID(terminal.getLocatedProcessPointId());
				terminalForm.setRouterPort(terminal.getRouterPort());
				terminalForm.setTerminalDescription(terminal.getTerminalDescription());
				terminalForm.setDivisionID(terminal.getDivisionId());
				terminalForm.setAfTerminalFlag(terminal.getAfTerminalFlag());
				terminalForm.setAutoUpdateIpFlag(terminal.isAutoUpdateIpAddress());
				terminalForm.setTerminalTypeMap(terminalTypeMap);
				
				// get application by specifed terminal
				List<ApplicationByTerminal> appList = ServiceFactory.getDao(ApplicationByTerminalDao.class).findAllByTerminal(hostName);
				terminalForm.setApplicationByTerminalList(appList);
			}
		}
	}
}