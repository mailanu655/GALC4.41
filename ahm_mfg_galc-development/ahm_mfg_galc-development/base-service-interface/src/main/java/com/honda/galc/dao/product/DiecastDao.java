package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.entity.product.BaseProduct;

/**
 * 
 * <h3>DiecastDao Class description</h3>
 * <p> DiecastDao description </p>
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
public interface DiecastDao<T extends BaseProduct> extends  ProductDao<T> {

	public T findByDCSerialNumber(String dcNumber);
	
	public T findByMCSerialNumber(String mcNumber);
	
	/**
	 * productId is either MC or DC number
	 * @param productId
	 * @return
	 */
	public T findByMCDCNumber(String productId);
	public T findBySn(String sn, NumberType numberType);
	
	public List<T> findAllByEngineSerialNumber(String engineId);
	public List<T> findAllByMCDCNumber(List<String> numbers);
	public List<T> findAllByMCDCNumber(String serialNumber);
	
	public int updateDunnage(String productId,String dunnage);
	public void updateEngineSerialNumber(String productId, String esn);
}
