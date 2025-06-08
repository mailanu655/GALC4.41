package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.HeadHistory;
import com.honda.galc.entity.product.HeadHistoryId;
import com.honda.galc.service.IDaoService;


/**
 * 
 * <h3>HeadHistoryDao Class description</h3>
 * <p> HeadHistoryDao description </p>
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
 * Jul 19, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface HeadHistoryDao extends ProductHistoryDao<HeadHistory,HeadHistoryId>,IDaoService<HeadHistory, HeadHistoryId> {


	public List<HeadHistory> findAllByProductId(String productId);
	
}
