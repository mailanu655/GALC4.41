package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
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
 */ 
public interface IExpectedProductManager {
	public List<String> getIncomingProducts(DataCollectionState state);
	public void getExpectedProductId(ProcessProduct state);
	public void saveNextExpectedProduct(ProcessProduct state);
	public void saveNextExpectedProduct(String nextProductId, String lastProcessedProduct);
	public void saveNextExpectedProduct(String nextProductId);
	public String getNextExpectedProductId(String productId);
	public void updatePreProductionLot();
	public void updateProductSequence(ProcessProduct state);
	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId,String ProductId);
	public String findPreviousProductId(String productId);
	public boolean isInSequenceProduct(String productId);
}
