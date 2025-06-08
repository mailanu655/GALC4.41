package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;

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
public interface ProductSpecCodeDao extends ProductSpecDao<ProductSpecCode,ProductSpecCodeId>  {

	void updateProductSpecCode(ProductSpecCode prodSpec, String productSpecCode);

	public List<ProductSpecCode> findAllByProductTypeAndPrefix(String productType, String prefix);

	List<QiMtcToEntryModelDto> findAllMtcModelYearCodesForDieCastByFilter(String filter, String productType);
	
	
}
