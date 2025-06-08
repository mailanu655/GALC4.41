package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;

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
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.RegionalProcessPointGroupDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.conf.RegionalProcessPointGroupId;
import com.honda.galc.system.config.web.forms.AssignedProcessPointListForm;
/**
 * <h3>Class description</h3>
 * Action for assigned process point list
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

public class AssignedProcessPointListAction extends ConfigurationAction {

    private static final String ERRORS_GROUP = "processPointListErrors";
    
    public AssignedProcessPointListAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        AssignedProcessPointListForm ppForm = (AssignedProcessPointListForm) form;
        
        try {
            short categoryCode = (short) ppForm.getCategoryCode();
            String site = ppForm.getSite() == null ? "" : ppForm.getSite();
            String ppGroupName = ppForm.getProcessPointGroupName() == null ? "" : ppForm.getProcessPointGroupName();
            String searchText = ppForm.getSearchText() == null ? "" : ppForm.getSearchText();
            RegionalProcessPointGroupId groupId = new RegionalProcessPointGroupId(categoryCode, site, ppGroupName);
            RegionalProcessPointGroup regionalGroup = getDao(RegionalProcessPointGroupDao.class).findByKey(groupId);

            if(regionalGroup == null) {
        		ppForm.setProcessPointList(new ArrayList<ProcessPoint> ());
        	} else if(StringUtils.isEmpty(searchText)) {
           		ppForm.setProcessPointList(getDao(ProcessPointDao.class).findAllByGroup(regionalGroup));
        	} else {
           		ppForm.setProcessPointList(getDao(ProcessPointDao.class).findAllByGroupAndMatchingText(regionalGroup, searchText));
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
        	errors.add(ERRORS_GROUP, new ActionError("",e.toString()));
		} catch (Exception e) {
            errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
        }  

        return forward(mapping, request, errors, messages);
    }
}
