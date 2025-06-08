package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>BlockDao Class description</h3>
 * <p> BlockDao description </p>
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
 * Jun 28, 2011
 *
 *
 */
public interface ProductTypeDao extends IDaoService<ProductTypeData,String>  {

	List<ProductTypeData> findAllMatchProductType(String typeMask);
	
	List<ProductTypeData> findAllByOwnerProduct(String ownerProduct);

	public List<String> findAllProductTypes();
	
}
