package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.entity.product.ProductTypeToNumberDef;
import com.honda.galc.entity.product.ProductTypeToNumberDefId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ProductTypeToNumberDefDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTypeToNumberDefDao description </p>
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
 * <TD>Feb 21, 2017</TD>
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
 * @since Feb 21, 2017
 */
public interface ProductTypeToNumberDefDao extends IDaoService<ProductTypeToNumberDef, ProductTypeToNumberDefId> {

	/**
	 *  Find all product number defs assigned to the main no.
	 */
	List<ProductIdNumberDef> findByMainNo(String mainNo);

	/**
	 * Remove all product number defs assigned to the main no
	 * @param mainNo
	 */
	void removeByMainNo(String mainNo);
	
	/**
	 * Remove all product number defs associated with the product ID def
	 * @param mainNo
	 */
	void removeByProductIdDef(String productIdDef);

}
