package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.BlockHistory;
import com.honda.galc.entity.product.BlockHistoryId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>BlockHistoryDao Class description</h3>
 * <p> BlockHistoryDao description </p>
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
public interface BlockHistoryDao extends ProductHistoryDao<BlockHistory,BlockHistoryId>,IDaoService<BlockHistory, BlockHistoryId> {

	public List<BlockHistory> findAllByProductId(String productId);
	
}
