package com.honda.galc.client.product;

import java.util.List;

/**
 * 
 * <h3>IExpectedProductManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IExpectedProductManager description </p>
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
 * @author Paul Chou
 * Apr 9, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 * @version 0.3 
 * @author Jeffray Huang 
 * @since Feb 27,2014
 */ 
public interface IExpectedProductManager {
	public List<String> getIncomingProducts();
	public String getExpectedProductId();
	public void saveNextExpectedProduct();
	public void saveNextExpectedProduct(String nextProductId, String lastProcessedProduct);
	public void saveNextExpectedProduct(String nextProductId);
	public String getNextExpectedProductId(String productId);
	public void updatePreProductionLot();
	public void updateProductSequence();
	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId,String ProductId);
}
