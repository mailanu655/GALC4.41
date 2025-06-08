package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.ConditionType;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.notification.service.INotificationService;
import com.honda.galc.service.broadcast.IExternalService;
import com.honda.galc.service.broadcast.IMqAssembler;
import com.honda.galc.service.broadcast.servertask.IServerTask;
import com.honda.galc.system.config.web.forms.BroadcastDestinationForm;
import com.honda.galc.util.SortedArrayList;

/**
 * <H3>BroadcastDestinationSettingsAction</H3> 
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This Struts Action class controls the behavior of the Broadcast Destination Settings
 * "dialog" window that is launched by the Process Point Broadcast Tab.</P>
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
 * <TD>Mar 19, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Mar 28, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL014</TD>
 * <TD>Process Point Notification - extended broadcast to non-process point applications</TD>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 12, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL017</TD>
 * <TD>Printing enhancements: print forms - <br>
 * added impmentation for obtaining list of forms</TD>
 * </TR> 
 * </TABLE>
 */
public class BroadcastDestinationSettingsAction extends ConfigurationAction

{
	public BroadcastDestinationSettingsAction() {
		super();
	}
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

        ActionErrors errors = new ActionErrors();
        BroadcastDestinationForm bdForm = (BroadcastDestinationForm) form;
        try {      
        	
        	if(bdForm.getDestinationType() == null){
        		//Default value for destination type according to first element on select element in jsp view
        		bdForm.setDestinationTypeID(DestinationType.Equipment.getId());
        	}
        	
            Division division = null;
            if (bdForm.isFindInDivision())
            {
                ProcessPoint ppdata = getDao(ProcessPointDao.class).findByKey(bdForm.getProcessPointID());
                if(ppdata != null){
                	division = getDao(DivisionDao.class).findByKey(ppdata.getDivisionId());
                }else{
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("CFGW3600", bdForm.getProcessPointID()));
                }
                                    
            }
            
            List<LabelValueBean> destinations = getDestinationsChoice(bdForm.getDestinationType(), division);
            bdForm.setDestinations(destinations);              
            
            switch (bdForm.getDestinationType()) {	
            	case Printer:
            	case Notification:
            	case FTP:
            		//Requests List
            		bdForm.setRequests(getPrintForms());
            		break;	     		   	
     		   	case External:      			   
     			   //Requests List
     			   List<LabelValueBean> externalDevices = getDestinationsChoice(DestinationType.Printer, division);
     			   bdForm.setRequests(externalDevices);
     			   
     			   //Argument List	     			   
     			   List<LabelValueBean> methods = new ArrayList<LabelValueBean>(){{ add(getDefaultSelectOption()); }} ;
     			   String serviceName = bdForm.getDestinationID() != null ? bdForm.getDestinationID() : ""; 
     			   methods.addAll(getExternalMethods(serviceName));
     			   bdForm.setArguments(methods);
     		       break;
     		   case MQMANIFEST:
     		   case MQ: 
     			   //Argument List
     			   List<LabelValueBean> assemblers = getAssemblerChoice(bdForm.getDestinationType());
     			   bdForm.setArguments(assemblers);
     		       break;	
     		}
            
            bdForm.setCheckPoints(Arrays.asList(new CheckPoints[]{null, CheckPoints.BEFORE_PRODUCT_PROCESSED, CheckPoints.AFTER_PRODUCT_PROCESSED}));
            bdForm.setConditionTypes(Arrays.asList(new ConditionType[]{null, ConditionType.PRODUCT_CHECK, ConditionType.PRINT_ATTRIBUTE, ConditionType.DEVICE_FORMAT}));
            
        } catch (Exception e) {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("CFGW0005",e.toString()));
        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors);
    }

	private List<LabelValueBean> getPrintForms(){
    	List<LabelValueBean> forms = new ArrayList<LabelValueBean>();
 	   	forms.add(getDefaultSelectOption());
 	   	
 	   	// 	 Get all form IDs
 	   	List<PrintAttributeFormat> printAttributeFormats = getDao(PrintAttributeFormatDao.class).findAll();
 	   	Set<String> formIds = new HashSet<String>();
 	   	for(PrintAttributeFormat item : printAttributeFormats)
 	   		formIds.add(item.getId().getFormId());
 	   	SortedArrayList<String> items = new SortedArrayList<String>(formIds);
 	   	
 	   	// Create options list
 	   	for (String formId : items) {
			forms.add(new LabelValueBean(formId, formId));
		}
 	   	
		return forms;
	}

	private List<LabelValueBean> getDestinationsChoice(DestinationType destinationType, Division division) {
		
    	List<LabelValueBean> destinations = new ArrayList<LabelValueBean>();
		switch (destinationType) {		
		   case Equipment:
		   case Printer: {
		       getDeviceDestinations(destinationType, division, destinations);
		       break;
		   }

		   case Terminal: {
		       getTerminalDestinations(division, destinations);
		       break;
		   }
		   
		   case Application: {
		       getApplicationDestinations(division, destinations);
		       break;
		   }
		   case External:{
			   getExternalDestinations(destinations);
			   break;
		   }
		   case Notification:{
			   getNotificationDestinations(destinations);
			   break;
		   }
		   case MQ:{
			   getMqDestinations(destinationType, division, destinations);
			   break;
		   }		   
		   case MQMANIFEST:{ 
			   getMqManifestDestinations(destinationType, division, destinations);
			   break;
		   }
		   
		   case FTP: {
			   getFtpDestinations(division,destinations);
			   break;
		   }
		   
		   case SERVER_TASK:{
			   getServerTaskDestinations(destinations);
			   break;
			   
		   }
		   case DEVICE_WISE:
			   
		   default:{
		       getDeviceDestinations(destinationType, division, destinations);
		       break;
		   }	   
		   
		}
 	   	destinations.add(0, getDefaultSelectOption());
		return destinations;
	}

	private void getMqManifestDestinations(DestinationType destinationType, Division division,
			List<LabelValueBean> destinations) {

		List<Device> devices = division == null ?  getDao(DeviceDao.class).findAllOrderByClientId() :
			getDao(DeviceDao.class).findAllByDivisionId(division.getDivisionId()); 
		for (Device data : devices) {           
			// Only add the device to the list if it is of the specific type
			if (destinationType == DestinationType.MQMANIFEST &&
					data.getDeviceType() == DeviceType.MQ) 	                       
			{
				StringBuffer displayText = new StringBuffer(data.getClientId());
				if (data.getDeviceDescription() != null && data.getDeviceDescription().length() > 0){
					displayText.append ("  [");
					displayText.append(data.getDeviceDescription());
					displayText.append("]");
				}
				destinations.add(new LabelValueBean(displayText.toString(), data.getClientId()));

			}
		}


	}
	private void getMqDestinations(DestinationType destinationType, Division division, List<LabelValueBean> destinations) {
		List<Device> devices = division == null ?  getDao(DeviceDao.class).findAllOrderByClientId() :
			getDao(DeviceDao.class).findAllByDivisionId(division.getDivisionId()); 
		for (Device data : devices) {           
	           // Only add the device to the list if it is of the specific type
	           if (destinationType == DestinationType.MQ &&
		                 data.getDeviceType() == DeviceType.MQ) 	                       
	           {
	               StringBuffer displayText = new StringBuffer(data.getClientId());
	               if (data.getDeviceDescription() != null && data.getDeviceDescription().length() > 0){
	                   displayText.append ("  [");
	                   displayText.append(data.getDeviceDescription());
	                   displayText.append("]");
	               }
	               destinations.add(new LabelValueBean(displayText.toString(), data.getClientId()));
	                 
	           }
	   }
		
	}
	
	private void getFtpDestinations(Division division, List<LabelValueBean> destinations) {
		List<Device> devices = division == null ?  getDao(DeviceDao.class).findAllOrderByClientId() :
			getDao(DeviceDao.class).findAllByDivisionId(division.getDivisionId()); 
		for (Device data : devices) {           
	           // Only add the device to the list if it is of the specific type
	           if (data.getDeviceType() == DeviceType.FTP) 	                       
	           {
	               StringBuffer displayText = new StringBuffer(data.getClientId());
	               if (data.getDeviceDescription() != null && data.getDeviceDescription().length() > 0){
	                   displayText.append ("  [");
	                   displayText.append(data.getDeviceDescription());
	                   displayText.append("]");
	               }
	               destinations.add(new LabelValueBean(displayText.toString(), data.getClientId()));
	                 
	           }
	   }
		
	}

	private void getDeviceDestinations(DestinationType destinationType, Division division, List<LabelValueBean> destinations) {
		
		List<Device> devices = division == null ?  getDao(DeviceDao.class).findAllOrderByClientId() :
				getDao(DeviceDao.class).findAllByDivisionId(division.getDivisionId()); 
		   
		for (Device data : devices) {           
		           // Only add the device to the list if it is of the specific type
		           if ( (destinationType == DestinationType.Equipment &&
		                 (data.getDeviceType() == DeviceType.EQUIPMENT  ||  // @JM200705
		                  data.getDeviceType() == DeviceType.OPC)) ||
		                (destinationType == DestinationType.Printer &&
		                         data.getDeviceType() == DeviceType.PRINTER) ||
		                (destinationType == DestinationType.DEVICE_WISE &&
				                         data.getDeviceType() == DeviceType.DEVICE_WISE))
		                         
		                       
		           {
		               StringBuffer displayText = new StringBuffer(data.getClientId());
		               if (data.getDeviceDescription() != null && data.getDeviceDescription().length() > 0){
		                   displayText.append ("  [");
		                   displayText.append(data.getDeviceDescription());
		                   displayText.append("]");
		               }
		              
		        	   destinations.add(new LabelValueBean(displayText.toString(), data.getClientId()));
		                 
		           }
		       
		   }
	}

	private void getApplicationDestinations(Division division, List<LabelValueBean> destinations) {
		
		List<ProcessPoint> processPoints = (division == null) ? getDao(ProcessPointDao.class).findAllOrderByProcessPointId() :
			getDao(ProcessPointDao.class).findAllByDivision(division);
		   
		for(ProcessPoint data : processPoints) {
		           
           StringBuffer displayText = new StringBuffer(data.getProcessPointId());
           
           if (data.getProcessPointName() != null && data.getProcessPointName().length() > 0)
           {
               displayText.append ("  [");
               displayText.append(data.getProcessPointName());
               displayText.append("]");
           }
          
    	   destinations.add(new LabelValueBean(displayText.toString(), data.getProcessPointId()));
       }
       
	}

	private void getTerminalDestinations(Division division, List<LabelValueBean> destinations)  {
		List<Terminal> terminalList;
		if(division == null) terminalList= getDao(TerminalDao.class).findAllOrderByHostName();
		else terminalList  = getDao(TerminalDao.class).findAllByDivisionId(division.getDivisionId());
		   
		for(Terminal data : terminalList) {   
		           
           StringBuffer displayText = new StringBuffer(data.getHostName());
           
           if (data.getTerminalDescription() != null && data.getTerminalDescription().length() > 0)
           {
               displayText.append ("  [");
               displayText.append(data.getTerminalDescription());
               displayText.append("]");
           }
          
    	   destinations.add(new LabelValueBean(displayText.toString(), data.getHostName()));
       }
		       
	}

	private List<LabelValueBean> getAssemblerChoice(DestinationType destinationType) {
		
    	List<LabelValueBean> assemblers = new ArrayList<LabelValueBean>();
    	switch (destinationType) {
    	case MQMANIFEST:
    	case MQ:{
    		assemblers.add(getDefaultSelectOption());
    		getAssemblers(assemblers);
    		break;
    	}
    	default:{
    	}	   

    	}
		return assemblers;
	}
		
	private void getExternalDestinations(List<LabelValueBean> destinations){
		
		for(String serviceName : IExternalService.EXT_SERVICIES.keySet()){
			destinations.add(new LabelValueBean(serviceName, serviceName));
		}
		Collections.sort(destinations, new Comparator<LabelValueBean>() {
			@Override
			public int compare(LabelValueBean lvb1, LabelValueBean lvb2) {
				return lvb1.getLabel().compareTo(lvb2.getLabel());
			}
		});
	}
	
	private void getServerTaskDestinations(List<LabelValueBean> destinations){
		
		for(String serviceName : IServerTask.SERVER_TASKS.keySet()){
			destinations.add(new LabelValueBean(serviceName, serviceName));
		}
	}
	
	private void getAssemblers(List<LabelValueBean> assemblers){
		
		for(String serviceName : IMqAssembler.MQ_ASSEMBLERS.keySet()){
			assemblers.add(new LabelValueBean(serviceName, serviceName));
		}
	}
	
	private void getNotificationDestinations(List<LabelValueBean> destinations){
		Set<Entry<String,String>> entries = INotificationService.BROADCAST_SERVICIES.entrySet();
		for(Entry<String,String> entry : entries) {
			destinations.add(new LabelValueBean(entry.getKey() + "[" + entry.getValue() + "]", entry.getKey()));
		}
		Collections.sort(destinations, new Comparator<LabelValueBean>() {
			@Override
			public int compare(LabelValueBean lvb1, LabelValueBean lvb2) {
				return lvb1.getLabel().compareTo(lvb2.getLabel());
			}
		});
	}
	
	private List<LabelValueBean> getExternalMethods(String serviceName){
		List<LabelValueBean> externalMethods = new ArrayList<LabelValueBean>();
		if (IExternalService.EXT_SERVICIES.containsKey(serviceName.trim())){
			for(String[] methodName : IExternalService.EXT_SERVICE_METHODS){
				if(methodName[0].equals(serviceName.trim())){
					externalMethods.add(new LabelValueBean(methodName[1], methodName[1]));
				}
				continue;
			}
		}
		return externalMethods;
	}
	
	public LabelValueBean getDefaultSelectOption(){
		return new LabelValueBean("(none)", "");
	}
}
