package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockBuildResultId;

/**
 * 
 * <h3>BlockBuildResultDao Class description</h3>
 * <p> BlockBuildResultDao description </p>
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
 * Jun 19, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface BlockBuildResultDao extends ProductBuildResultDao<BlockBuildResult, BlockBuildResultId> {

	List<BlockBuildResult> findAllByProductId(String productId);
	
	public List<BlockBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber);

	List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList);
	
}
