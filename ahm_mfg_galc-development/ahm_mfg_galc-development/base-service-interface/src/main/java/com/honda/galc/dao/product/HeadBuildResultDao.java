package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadBuildResultId;

/**
 * 
 * <h3>HeadBuildResultDao Class description</h3>
 * <p> HeadBuildResultDao description </p>
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
 * Mar 22, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface HeadBuildResultDao extends ProductBuildResultDao<HeadBuildResult, HeadBuildResultId> {

	List<HeadBuildResult> findAllByProductId(String productId);
	
	public List<HeadBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber);

	List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList);

}
