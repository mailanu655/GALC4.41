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
import com.honda.galc.dao.conf.RegionalCodeDao;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.system.config.web.forms.RegionalCodeListForm;

/**
 * <h3>Class description</h3>
 * Action for RegionalCode list
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

public class RegionalCodeListAction extends ConfigurationAction {

    private static final String ERRORS_GROUP = "regionalCodeErrors";
    
    public RegionalCodeListAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        RegionalCodeListForm codeForm = (RegionalCodeListForm) form;

        try {
            if(StringUtils.isEmpty(codeForm.getSearchText())) {
            	codeForm.setRegionalCodes(getDao(RegionalCodeDao.class).findAllByRegionalCodeName(RegionalCodeName.CATEGORY_CODE.getName()));
            } else {
            	codeForm.setRegionalCodes(getDao(RegionalCodeDao.class).
            			findAllByNameAndMatchingText(RegionalCodeName.CATEGORY_CODE.getName(), codeForm.getSearchText()));
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
}
