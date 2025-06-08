package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.ProductIdMaskId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3> ProductIdMaskDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Interface class for the ProductIdMaskDao</p>
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
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * March 05, 2016
 * 
 */

public interface ProductIdMaskDao extends IDaoService<ProductIdMask, ProductIdMaskId>{
	
	public List<String> findAllByProductType(String productType);
	public List<ProductIdMask> findAllByProcessPointId(String processPointId);
	public List<ProductIdMask> findAllByProcessPointAndProductType(String processPointId, String productType);
	public List<ProductIdMask> findAllByProcessPointAndProductSpec(String processPointId, String ProductSpecCode);
	public ProductIdMask findByMaskAndProcessPointId(String productIdMask, String processPointId);
	public List<ProductIdMask> findAllByProcessPointIds(List<String> onProcessPoints);
	public List<ProductIdMask> findAllByProductSpecCode(String productSpecCode);
	
}
