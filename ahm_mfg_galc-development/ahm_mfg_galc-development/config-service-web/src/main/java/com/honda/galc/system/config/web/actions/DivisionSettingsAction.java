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
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.entity.conf.Division;
import static com.honda.galc.service.ServiceFactory.getDao;
import com.honda.galc.system.config.web.forms.DivisionForm;

/**
 * 
 * <h3>DivisionAction</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
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
 * <TD>Feb 2, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * <TR>
 * <TD>rlasenko</TD>
 * <TD>Oct 19, 2007</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL301</TD>
 * <TD>Moved DivisionData value objects to GALC_Core</TD>
 * </TR>
 * </TABLE>
 */
public class DivisionSettingsAction extends ConfigurationAction

{

    /**
     * Constructor
     */
    public DivisionSettingsAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        DivisionForm divisionForm = (DivisionForm) form;
        
        
        divisionForm.setEditor(request.isUserInRole("EditProcess"));
        divisionForm.setTerminalEditor(request.isUserInRole("EditTerminal"));
        divisionForm.setDeviceEditor(request.isUserInRole("EditDevice"));

        try {

            if (divisionForm.getCreateFlag() != null && divisionForm.getCreateFlag().equals("true")) {
                
            	divisionForm.setMissingGpcsDataMessage("");

                if (!divisionForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }

                // Make sure we have the required fields. This will work to
                // redirect to the page after the "Create new division"
                // navigation
                // option is selected.
                if (divisionForm.getPlantName() != null && divisionForm.getPlantName().length() > 0
                        && divisionForm.getSiteName() != null && divisionForm.getSiteName().length() > 0
                        && divisionForm.getDivisionName() != null && divisionForm.getDivisionName().length() > 0) {
                    
                    // Ok to create division
                    Division data = divisionForm.getDivisionData();
                    
                    List<Division> divisionList = getDao(DivisionDao.class).findById(divisionForm.getSiteName(),divisionForm.getPlantName());

                    //default value of the input will be 0, if user don't set that value then it will auto assign 
                    //the value to list size (as it was doing previously) but if they set some user defined value 
                    //then it will save that value
                    if (divisionList != null) {
                       if(divisionForm.getSequenceNumber()==0){
                    	   data.setSequenceNumber(divisionList.size());
                       }else{
                    	   data.setSequenceNumber(divisionForm.getSequenceNumber());
                       }
                    }

                    //Check if a division already exists with the form's DIVISION_ID and
                    //if it does, update the SITE_NAME and PLANT_NAME for it, its Lines and its ProcessPoints;
                    //else save the new division
                    List<Object[]> gpcsRecords = getDao(GpcsDivisionDao.class).findDivisionInfoAndGpcs(divisionForm.getDivisionID());
                    
                    if(gpcsRecords!=null && gpcsRecords.size()>0) {
                    	Object[] gpcsRecord = gpcsRecords.get(0);
                    	divisionForm.setProcessLocation((String) gpcsRecord[0]);
                    	divisionForm.setLineNo((String) gpcsRecord[1]);
                    	divisionForm.setMissingGpcsDataMessage("");
                    } else{
                    	divisionForm.setProcessLocation("??");
                    	divisionForm.setLineNo("??");
                        divisionForm.setMissingGpcsDataMessage("Missing Division to GPCS relationship - needed to determine line, shift and Production Date.");
                    }
                    
                    Division dataExisting = getDao(DivisionDao.class).findByKey(divisionForm.getDivisionID());
                    if (dataExisting == null) {
                    	getDao(DivisionDao.class).save(data);
                    } else {
                    	getDao(DivisionDao.class).updateId(divisionForm.getSiteName(), divisionForm.getPlantName(), divisionForm.getDivisionID());
                    }
                    
                    divisionForm.setDivisionData(data);
                    
                    messages.add("changedivinfo", new ActionMessage(dataExisting == null ? "CFGW0035" : "CFGW0064", data.getDivisionId()));                    

                    // We are creating a new division
                    divisionForm.setCreateFlag("false");
                    request.setAttribute("existingflag", "true");
                    
                    // Refresh the navigation tree
                    divisionForm.setRefreshTree(true);
                } else {
                    // Go back to page and get required fields.
                    request.setAttribute("existingflag", "false");
                    messages.add("changedivinfo", new ActionMessage("CFGW0008"));
                }

 
            } else if (divisionForm.getApply() != null && divisionForm.getApply().equalsIgnoreCase("apply")) {
                
                
                // We are updating an existing division                
                if (!divisionForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
                
                List<Object[]> gpcsRecords = getDao(GpcsDivisionDao.class).findDivisionInfoAndGpcs(divisionForm.getDivisionID());
                
                if(gpcsRecords!=null && gpcsRecords.size()>0) {
                	Object[] gpcsRecord = gpcsRecords.get(0);
                	divisionForm.setProcessLocation((String) gpcsRecord[0]);
                	divisionForm.setLineNo((String) gpcsRecord[1]);
                	divisionForm.setMissingGpcsDataMessage("");
                } else{
                	divisionForm.setProcessLocation("??");
                	divisionForm.setLineNo("??");
                    divisionForm.setMissingGpcsDataMessage("Missing Division to GPCS relationship - needed to determine line, shift and Production Date.");
                }
                
                String divisionDescription = divisionForm.getDivisionDescription();
                if (divisionDescription != null)
                {
                   getDao(DivisionDao.class).update(divisionForm.getDivisionData());
                    
                    messages.add("changedivinfo", new ActionMessage("CFGW0036", divisionForm.getDivisionID())); 
                }
                
                divisionForm.setCreateFlag("false");
                request.setAttribute("existingflag", "true");

            } else if (divisionForm.getDelete() != null && 
                       divisionForm.getDelete().equalsIgnoreCase("delete"))
            {
                if (!divisionForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }

                if (!divisionForm.isDeleteConfirmed())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                getDao(DivisionDao.class).removeByKey(divisionForm.getDivisionID());
                
                messages.add("changedivinfo", new ActionMessage("CFGW0037", divisionForm.getDivisionID()));
                
                divisionForm.setDivisionDescription("");
                divisionForm.setDivisionName("");
                
                // Refresh the navigation tree
                divisionForm.setRefreshTree(true);
                
            }
            else {
                // We are obtaining division data
                Division data = getDao(DivisionDao.class).findByKey(divisionForm.getDivisionID());

                divisionForm.setDivisionData(data);

                List<Object[]> gpcsRecords = getDao(GpcsDivisionDao.class).findDivisionInfoAndGpcs(divisionForm.getDivisionID());
                
                if(gpcsRecords!=null && gpcsRecords.size()>0) {
                	Object[] gpcsRecord = gpcsRecords.get(0);
                	divisionForm.setProcessLocation((String) gpcsRecord[0]);
                	divisionForm.setLineNo((String) gpcsRecord[1]);
                	divisionForm.setMissingGpcsDataMessage("");
                } else{
                	divisionForm.setProcessLocation("??");
                	divisionForm.setLineNo("??");
                }


                divisionForm.setCreateFlag("false");
                request.setAttribute("existingflag", "true");

            }

        }
        catch (ConfigurationServicesException e)
		{
        	errors.add("changedivisionerror", new ActionError("Test"));
		}
        catch (SystemException e)
		{
        	errors.add("changedivisionerror", new ActionError("Test",e.toString()));
		}
        
        catch (Exception e) {

            // Report the error using the appropriate name and ID.
            ActionError ae = new ActionError("CFGW0009",e.toString());
            logActionError(ae);
            errors.add("changedivisionerror", ae);

        }

        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);

    }
}
