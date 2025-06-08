package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.data.ProductNumberDef.ProductNumberType;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.system.config.web.forms.ProductTypeForm;

/**
 * 
 * <h3>ProductTypeConfigAction</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTypeConfigAction description </p>
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
public class ProductTypeConfigAction extends ConfigurationAction{
    private static final String ERRORS_GROUP = "productTypeErrors";
    private boolean isValid = true;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean isApply = false;
        boolean isDelete = false;
        boolean fetchProductTypeData = false;
        
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        ProductTypeForm productTypeForm = (ProductTypeForm) form;
        
        if (request.isUserInRole("EditAdmin"))
        {
            productTypeForm.setEditor(true);    
        }
        
        if (productTypeForm.getApply() != null && 
            productTypeForm.getApply().equalsIgnoreCase("apply"))
        {
            isApply = true;
        }
        else if (productTypeForm.getDelete() != null && 
                 productTypeForm.getDelete().equalsIgnoreCase("delete"))
        {
            isDelete = true;
        }

        try {

            if (productTypeForm.isInitializeFrame())
            {
                productTypeForm.setInitializeFrame(false);
                return mapping.findForward("initialize");
            }
            
            if (isApply || isDelete)
            {
                if (!productTypeForm.isEditor())
                {
                    throw new ConfigurationServicesException("CFGW1000");
                }
            }
            
			if (isApply) {
				isValid = true;
				validateProductType(productTypeForm, messages, errors);
				if (isValid) {
					ProductTypeData productTypeData = getDao(ProductTypeDao.class)
							.findByKey(productTypeForm.getProductType());
					if (productTypeData != null) {
						productTypeForm.setExistingProductType(true);
					} else
						productTypeForm.setExistingProductType(false);
					
					if (!productTypeForm.isExistingProductType()) {
						getDao(ProductTypeDao.class).insert(productTypeForm.getProductTypeData());
						fetchProductTypeData = true;
						messages.add("changeproducttypeinfo",
								new ActionMessage("CFGW0069", productTypeForm.getProductType()));
						productTypeForm.setRefreshList(true);
					} else {
						getDao(ProductTypeDao.class).update(productTypeForm.getProductTypeData());
						fetchProductTypeData = true;
						messages.add("changeproducttypeinfo",
								new ActionMessage("CFGW0070", productTypeForm.getProductType()));
					}
				} else {
					List<String> mbpnTypes = getDao(MbpnProductTypeDao.class).findAllProductTypes();
					productTypeForm.populateMbpnTypes(mbpnTypes);
					productTypeForm.populate(productTypeForm.getProductTypeData());
					messages.add("producttypecannotbesaved",
							new ActionMessage("CFGW0075", productTypeForm.getProductType()));
				}

			}
            else if (isDelete)
            {
                if (!productTypeForm.isDeleteConfirm())
                {
                    throw new ConfigurationServicesException("CFGW1001");
                }
                
                getDao(ProductTypeDao.class).removeByKey(productTypeForm.getProductType());
                messages.add("changeproducttypeinfo", new ActionMessage( "CFGW0071", productTypeForm.getProductType()));
                Map<String,String> productTypesParameter = new LinkedHashMap<String,String>();
                productTypeForm.setProductTypes(productTypesParameter); 
                productTypeForm.reset();
                productTypeForm.setRefreshList(true);
            }
            else
            {
                fetchProductTypeData = true;
            }
            
            if (fetchProductTypeData) {
            	
            	List<String> mbpnTypes = getDao(MbpnProductTypeDao.class).findAllProductTypes();
                productTypeForm.populateMbpnTypes(mbpnTypes);
            	
                if (productTypeForm.getProductType() != null &&
                    productTypeForm.getProductType().length() > 0)
                {
                    ProductTypeData productTypeData = getDao(ProductTypeDao.class).findByKey(productTypeForm.getProductType());
                    
                    productTypeForm.setExistingProductType(true);
                    productTypeForm.populate(productTypeData);
                }
            }

        } catch (ConfigurationServicesException e) {
        	errors.add(ERRORS_GROUP, new ActionError(e.getMessage(),e.toString()));
		} catch (Exception e) {
            e.printStackTrace();
            ActionError ae = new ActionError("CFGW0005",e.toString());;
            logActionError(ae);
            errors.add(ERRORS_GROUP, ae);
        }
        
        // forward failure or success depending on errors
        return forward(mapping,request,errors,messages);
    
    }
    
    private void validateProductType(ProductTypeForm form, ActionMessages messages, ActionErrors errors) throws ConfigurationServicesException
    {
    	validateProductSpecCodeFormat(form,messages);
    	validateProductIdFormat(form, errors);
    }

	private void validateProductIdFormat(ProductTypeForm form, ActionErrors errors) {
		if(StringUtils.isEmpty(form.getProductIdFormat())) return;
		for (String token : form.getProductIdFormat().split(",")){
			try {
				ProductNumberType.valueOf(token.trim());
			} catch (Exception e) {
				errors.add("error product id format", new ActionError("CFGW0074"));
				isValid = false;	
			}
		}		
	}

	private void validateProductSpecCodeFormat(ProductTypeForm form, ActionMessages messages) {
		ProductSpecCodeDef current = null;
    	for(char c : form.getProductSpecCodeFormat().toCharArray()){
			ProductSpecCodeDef specCode = ProductSpecCodeDef.valueOfCode(c);
			if(specCode == null) {
				messages.add("Invalid product spec code format", new ActionMessage( "CFGW0072", c + " ."));
				isValid = false; 
				return;
			}
			if(current == null) {
				current = specCode;
				isValid = true;
			}
			else{
				if(specCode.getSequenceNumber() <= current.getSequenceNumber()) {
					messages.add("Invalid product spec code format", new ActionMessage( "CFGW0073"));	
				    isValid = false;
				    return;
				}					
				else {
					current = specCode;
					isValid = true;
				}	
			}
		}
	}

}
