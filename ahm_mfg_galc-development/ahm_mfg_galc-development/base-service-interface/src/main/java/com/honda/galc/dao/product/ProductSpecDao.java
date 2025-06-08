package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.ProductSpec;

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
public interface ProductSpecDao<E extends ProductSpec,K> extends BaseProductSpecDao<E,K>  {
	
	public List<String> findAllModelYearCodes(String productType);
	
	public List<String> findAllModelYearCodesAfterModelYearDescription(String modelYearDescription);
	
	public List<E> findAllByModelYearCode(String yearCode,String productType);

	public List<E> findAllByProcessPointId(String processPointId);

	public List<E> findAllByPrefix(String prefix);
	
	public List<String> findAllModelTypeCodes(String productType);
	
    

}
