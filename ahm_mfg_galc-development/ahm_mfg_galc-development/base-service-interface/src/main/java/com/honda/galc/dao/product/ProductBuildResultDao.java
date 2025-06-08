package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ProductDao Class description</h3>
 * <p> ProductDao description </p>
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
 * Feb 17, 2012
 *
 *
 */
public interface ProductBuildResultDao<E extends ProductBuildResult,K> extends IDaoService<E, K> {
	
	public E findById(String productId, String partName);
		
	public List<E> findAllByProductId(String productId);
	
    public List<E> findAllByProductIdAndPartNames(String productId, List<String> partNames);
	
	public boolean isPartInstalled(String productId,String partName);

	public List<E> findAllByPartNameAndSerialNumber(String partName, String partNumber);
	
	public ProductBuildResult saveResult(ProductBuildResult result);
	
	public ProductBuildResult saveResult(ProductBuildResult result, Boolean saveHistory);
	
	public List<ProductBuildResult> saveAllResults(List<ProductBuildResult> result);
	
	public List<ProductBuildResult> saveAllResults(List<ProductBuildResult> result, Boolean saveHistory);
	
	public void remove(ProductBuildResult entity);
	
	public List<E> findAllByPartNameAndSerialNumber(List<String> partName, String partNumber);

	ProductBuildResult findByRefId(Long refId);

	public List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList);

	List<? extends ProductBuildResult> findAllPartsWithDefect(String productId);

}
