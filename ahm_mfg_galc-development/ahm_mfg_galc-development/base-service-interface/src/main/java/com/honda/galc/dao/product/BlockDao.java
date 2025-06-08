package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.InstalledPart;

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
 * Jun 19, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface BlockDao extends DiecastDao<Block> {

	public Block findByDCSerialNumber(String dcNumber);
	
	public Block findByMCSerialNumber(String mcNumber);
	
	public List<Block> findAllByEngineSerialNumber(String engineId);
	
	public List<Block> findAllByInProcessProduct(String currentProductId, int processedSize, int upcomingSize);
	
	public List<Block> findAllByProductSequence(String processPointId,String currentProductId,int processedSize,int upcomingSize);

	/**
	 * find all production dates 
	 * @return
	 */
	public List<Date> findAllProductionDates();
	
	public Block save(Block block, InstalledPart installedPart);
}
