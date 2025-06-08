package com.honda.galc.oif.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.oif.sched.EventSchedulePropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>CheckAction Class description</h3>
 * <p> CheckAction description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 6, 2011
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class CheckAction extends AbstractAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		EventSchedulePropertyBean propertyBean = PropertyService.getRefreshedPropertyBean(EventSchedulePropertyBean.class);
		String result = propertyBean.isEventScheduleEnabled() ? "success" : "failure";
		return  mapping.findForward(result);
	}
}
