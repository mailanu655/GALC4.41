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
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.config.web.forms.OpcListForm;
import com.honda.galc.util.SortedArrayList;

/**
 * <H3>OpcConfigurationListAction</H3> 
* <h3>Class description</h3>
* <h4>Description</h4>
* <P></P>
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
* <TD>Added support for device/process point queries</TD>
* </TR>
* </TABLE>
 */
public class OpcConfigurationListAction extends ConfigurationAction

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	OpcListForm opcListForm = (OpcListForm) form;
	
    if (request.isUserInRole("EditDevice"))
    {
        opcListForm.setEditor(true);
    }	

	try {

	    
	    	
	    	// Always load the instance name list
	    List<String> instances = ServiceFactory.getDao(OpcConfigEntryDao.class).findAllOpcInstanceNames();
			
	    opcListForm.setInstanceNames(instances);
	    	
	    
	    if (opcListForm.getFetchList() != null && 
	    		 opcListForm.getFetchList().equalsIgnoreCase("fetch")) {
	    	
	    	if (opcListForm.getInstanceName() != null && opcListForm.getInstanceName().length() >0 ) {
	    		
	    		List<OpcConfigEntry> opcDataList= ServiceFactory.getDao(OpcConfigEntryDao.class).findAllByOpcInstance(opcListForm.getInstanceName());
	    		opcListForm.setOpcConfigurationDataList(new SortedArrayList<OpcConfigEntry>(opcDataList,"getDataReadyTag"));
	    	}
	    	else if (opcListForm.getDeviceID() != null && opcListForm.getDeviceID().length() > 0) {
	    		List<OpcConfigEntry> opcDataList = ServiceFactory.getDao(OpcConfigEntryDao.class).findAllByDeviceId(opcListForm.getDeviceID());
	    		opcListForm.setOpcConfigurationDataList(opcDataList);	    		
	    	}
	    	else if (opcListForm.getProcessPointID() != null && opcListForm.getProcessPointID().length() > 0) {
//	    		List<OpcConfigEntry> opcDataList = ServiceFactory.getDao(OpcConfigEntryDao.class).findAllBy(opcListForm.getDeviceID());
//	    		java.util.List opcDataList = getDeviceConfiguration().getOPCConfigurationForProcessPoint(opcListForm.getProcessPointID());
//	    		opcListForm.setOpcConfigurationDataList(opcDataList);
	    	}
	    	else
	    	{
	    		messages.add("listopc",new ActionMessage("CFGW3000"));
	    	}
	    }

	}
	catch (ConfigurationServicesException e)
	{
    	errors.add("listopc", new ActionError("Test",e.toString()));
	}
	catch (SystemException e)
	{
    	errors.add("listopc", new ActionError("Test",e.toString()));
	}
    
	catch (Exception e) {

	    // Report the error using the appropriate name and ID.
	    errors.add("listopc", new ActionError("CFGW3001",e));

	}

    // forward failure or success depending on errors
    return forward(mapping,request,errors,messages);

    }
}
