package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.apache.struts.util.LabelValueBean;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.system.config.web.forms.PrintAttributeFormatForm;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.entity.enumtype.PrintAttributeFormatRequiredType;

/**
 * @version 	1.0
 * @author
 */
/**
 * @version 2.0
 * @author Gangadhararao Gadde
 * @date Sept 20, 2016
 */
public class PrintAttributeFormatSettingsAction extends ConfigurationAction {

    protected final Map<String,String> attributeTypes = new TreeMap<String,String>();
    
    protected final Map<String,String> requiredTypes = new TreeMap<String,String>();
    
    private String savedFormId = "";
    
    private static final String ERRORS_GROUP = "printAttributeFormatErrors";
    
    private static final String MESSAGES_GROUP = "updatePrintAttributeFormatMessages";

    public PrintAttributeFormatSettingsAction() {
        super();
    	for(PrintAttributeType type : PrintAttributeType.values()) {
    		attributeTypes.put(Integer.toString(type.getId()), type.toString());
    	}
    	for(PrintAttributeFormatRequiredType type : PrintAttributeFormatRequiredType.values()) {
    		requiredTypes.put(Integer.toString(type.getId()), type.toString());
    	}
    	
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
         ActionMessages messages = new ActionMessages();
        
        PrintAttributeFormatForm printAttributeFormatForm = (PrintAttributeFormatForm) form;
        
        try {
            
            String operation = printAttributeFormatForm.getOperation();
            String formID = printAttributeFormatForm.getFormID();
            printAttributeFormatForm.setAttributeTypes(attributeTypes);
            printAttributeFormatForm.setRequiredTypes(requiredTypes);
            
            
            //authorization and validation check
            if (operation != null && operation.equalsIgnoreCase("remove")
                    || (operation != null && operation.equalsIgnoreCase("apply"))) {
                
                //information message
                if (formID == null || formID.trim().length()==0) {
                    errors.add(ERRORS_GROUP, new ActionError("CFGW2024"));
                }
                String newFormID = printAttributeFormatForm.getNewFormID();
                String clone = printAttributeFormatForm.getClone();
                if ( (clone != null && clone.equalsIgnoreCase("clone"))
                        &&  ( newFormID == null || newFormID.trim().length()==0 )) {
                    errors.add(ERRORS_GROUP, new ActionError("CFGW2025"));
                }

                if (!request.isUserInRole("EditDevice"))
                {
               	    if (formID != null && formID.trim().length()>0) {
        	   	        List<PrintAttributeFormat> list = getDao(PrintAttributeFormatDao.class).findAllByFormId(formID);
        	   	        SortedArrayList<PrintAttributeFormat> sortedList = new SortedArrayList<PrintAttributeFormat>(list,"getSequenceNumber");
     	   	        	printAttributeFormatForm.setFormID(formID);
        	   	        printAttributeFormatForm.setData(sortedList);
        	   	    }
               
                }
                
            }

        	if (operation != null && operation.equalsIgnoreCase("remove")) {
        	    boolean confirmed = printAttributeFormatForm.isDeleteConfirmed();
        	    if (formID != null && formID.trim().length()>0 && confirmed) {
        	        
        	    	getDao(PrintAttributeFormatDao.class).removeAllByFormId(formID);
        	    	messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2021", formID));
        	    	formID = null;
        	    }
        	}else if (operation != null && operation.equalsIgnoreCase("apply")) {
        		List<PrintAttributeFormat> list = printAttributeFormatForm.getData();
        	    String newFormID = printAttributeFormatForm.getNewFormID();
        	    List<PrintAttributeFormat> originalList = getDao(PrintAttributeFormatDao.class).findAllByFormId(formID);
	   	        
        	    //copy the srouce client's device data formats to new client id
        	    if ((newFormID != null && newFormID.trim().length() > 0 ) 
        	            && ( formID != null && formID.trim().length() > 0) ){
        	      
        	    	List<PrintAttributeFormat> newList = new ArrayList<PrintAttributeFormat>();
        	    	for(PrintAttributeFormat item : originalList) {
        	    		PrintAttributeFormat clone = item.clone();
        	    		clone.getId().setFormId(newFormID);
        	    		newList.add(clone);
    	   	        }
    	   	        getDao(PrintAttributeFormatDao.class).saveAll(newList);
        	    	
    	   	        formID = newFormID;
            	    messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2022", formID));
        	    }else if ((formID != null && formID.trim().length() > 0)  && 
        	            (newFormID == null || newFormID.trim().length() == 0)) {
        	    	List<PrintAttributeFormat> removedList = new ArrayList<PrintAttributeFormat>();
        	    	for(PrintAttributeFormat item : originalList) {
        	    		if(!contains(item,list)) removedList.add(item); 
        	    	}
        	    	if(!removedList.isEmpty()) getDao(PrintAttributeFormatDao.class).removeAll(removedList);
        	    	if(list != null && !list.isEmpty()){
        	    		String duplicate = findDuplicate(list);
        	    		if(duplicate == null) {
        	    			getDao(PrintAttributeFormatDao.class).saveAll(list);
        	    			messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2020", formID));
        	    		}else
        	    			errors.add(ERRORS_GROUP,new ActionError("CFGW2026",formID,duplicate));
        	    	}else 
        	    		messages.add(MESSAGES_GROUP, new ActionMessage("CFGW2020", formID));
        	    }
        	}

        	if(StringUtils.isEmpty(formID)) {
        		printAttributeFormatForm.reset(mapping, request);
        		savedFormId = null;
        	}	
        	
            if (request.isUserInRole("EditDevice"))
            {
                printAttributeFormatForm.setEditor(true);
            }
            
            // Doing find
    		if (!StringUtils.isEmpty(operation) && operation.equalsIgnoreCase("findall")) {
				
				List<PrintAttributeFormat> list = getDao(PrintAttributeFormatDao.class).findAll();
	   	        List<LabelValueBean> forms = new ArrayList<LabelValueBean>();
				forms.add(new LabelValueBean("(none)", ""));
				Set<String> formIdList = new HashSet<String>();
				for (PrintAttributeFormat item : list) {
					formIdList.add(item.getId().getFormId());
				}
				SortedArrayList<String> idList = new SortedArrayList<String>(formIdList);
				
				for(String formId : idList) {
					forms.add(new LabelValueBean(formId, formId));
					
				}
				
				
				printAttributeFormatForm.setForms(forms);
				
			} 
			
    		if(!StringUtils.isEmpty(formID)&& (!formID.equals(savedFormId) || (operation != null && operation.equalsIgnoreCase("cancel")))) {
    			
				savedFormId = formID;
				List<PrintAttributeFormat> list = getDao(PrintAttributeFormatDao.class).findAllByFormId(formID);
				SortedArrayList<PrintAttributeFormat> sortedList = new SortedArrayList<PrintAttributeFormat>(list,"getSequenceNumber");
				if (sortedList == null || sortedList.size() <= 0) {
					printAttributeFormatForm.reset(mapping, request);
					printAttributeFormatForm.setFormID(formID);
				} else {
					printAttributeFormatForm.setFormID(formID);
					printAttributeFormatForm.setData(sortedList);
				}
			}
				    	    
        	
        } 
        
        catch (ConfigurationServicesException e)
		{
        	errors.add(ERRORS_GROUP, new ActionError(e.getMessage()));
		}
        catch (Exception e) {
        	e.printStackTrace();
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
        }  
        
        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
    
    private boolean contains(PrintAttributeFormat item, List<PrintAttributeFormat> items) {
    	for(PrintAttributeFormat eachItem: items) {
    		if(eachItem.getId().equals(item.getId())) return true;
    	}
    	return false;
    }
    
    private String findDuplicate(List<PrintAttributeFormat> items) {
    	Set<String> attributes = new HashSet<String>();
    	for(PrintAttributeFormat item : items) {
    		if(!attributes.add(item.getId().getAttribute())) return item.getId().getAttribute();
    	}
    	return null;
    }
}
