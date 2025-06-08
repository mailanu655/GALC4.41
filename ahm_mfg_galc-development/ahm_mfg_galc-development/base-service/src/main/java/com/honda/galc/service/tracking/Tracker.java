package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.IService;

/**
 * 
 * <h3>Tracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Tracker description </p>
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
 * Aug 24, 2010
 *
 * @param <T>
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface Tracker<T extends BaseProduct> extends IService{
	
	void track(T product, String processPoint);

	void track(String productId, String processPointId);
	
	void track(ProductHistory productHistory);

	void track(T product, String processPointId, String deviceId);
	
	void track(String productId, String processPointId, String deviceId);

			
}
