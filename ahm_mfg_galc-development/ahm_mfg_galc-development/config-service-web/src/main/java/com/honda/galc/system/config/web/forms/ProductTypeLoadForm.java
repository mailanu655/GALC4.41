package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.product.ProductTypeData;

public class ProductTypeLoadForm extends ActionForm{
    private static final long serialVersionUID = 1L;
    private List<ProductTypeData> productTypes = null;
    private boolean initializePage = false;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    /**
     * @return Returns the adminUsers.
     */
    public List<ProductTypeData> getProductTypes() {
        
        if (productTypes == null)
        {
        	productTypes = new ArrayList<ProductTypeData>(0);
        }
        return productTypes;
    }
    
    
  
    public void setProductTypes(List<ProductTypeData> producTypes) {
        this.productTypes = producTypes;
    }
    
    public boolean isInitializePage() {
        return initializePage;
    }
   
    public void setInitializePage(boolean initializePage) {
        this.initializePage = initializePage;
    }
}
