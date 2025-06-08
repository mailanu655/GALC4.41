package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ProductTypeDaoImpl Class description</h3>
 * <p> ProductTypeDaoImpl description </p>
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
 * Feb 16, 2012
 *
 *
 */
public class ProductTypeDaoImpl extends BaseDaoImpl<ProductTypeData,String> implements ProductTypeDao {

    private static final long serialVersionUID = 1L;
    private static String FIND_ALL_WILDCARD = "Select a from ProductTypeData a where a.productType like ";
    
    private final static String FIND_PRODUCT_TYPE  ="select e.productType from ProductTypeData e  order by e.productType";
    
	public List<ProductTypeData> findAllMatchProductType(String typeMask) {
		
		return findAllByQuery(FIND_ALL_WILDCARD + "'" + typeMask + "'");
	}
	
	public List<ProductTypeData> findAllByOwnerProduct(String ownerProduct) {
        return findAll(Parameters.with(("ownerProductType"),ownerProduct));
	    }

	@SuppressWarnings("unchecked")
	public List<String> findAllProductTypes() {
		return findResultListByQuery(FIND_PRODUCT_TYPE,null);
	}

}
