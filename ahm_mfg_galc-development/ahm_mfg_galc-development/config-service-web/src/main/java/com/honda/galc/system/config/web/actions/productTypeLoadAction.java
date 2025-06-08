package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.dao.conf.AdminUserDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.AdminUser;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.system.config.web.forms.AdminUserSearchForm;
import com.honda.galc.system.config.web.forms.ProductTypeLoadForm;

/**
 * 
 * <h3>productTypeSearchAction</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> productTypeSearchAction description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Nov 27, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov 27, 2013
 */
public class productTypeLoadAction extends ConfigurationAction{

	private static final String ERRORS_GROUP = "productTypeErrors";
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ProductTypeLoadForm productTypeSearchForm = (ProductTypeLoadForm) form;

		try {
				List<ProductTypeData> productTypes = getDao(ProductTypeDao.class).findAll();
				productTypeSearchForm.setProductTypes(productTypes);
		} 
		catch (Exception e) {

			e.printStackTrace();
			errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
		}          
		return forward(mapping,request,errors,messages);

	}

}
