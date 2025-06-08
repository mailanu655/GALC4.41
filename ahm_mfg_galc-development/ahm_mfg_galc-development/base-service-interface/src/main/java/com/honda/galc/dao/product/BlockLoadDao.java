package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>BlockLoadDao Class description</h3>
 * <p> BlockLoadDao description </p>
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
 * Jun 5, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface BlockLoadDao extends IDaoService<BlockLoad, String> {

	public int deleteAllByProductionDate(Date productionDate);
	
	public List<BlockLoad> findAllNonStampedBlocks();
	
	public int updateStatus(String mcNumber,BlockLoadStatus status);
	
	public BlockLoad findLastBlockLoad();
	
	public long countByProductionLot(String productionLot);
	
}
