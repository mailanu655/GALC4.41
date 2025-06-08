package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.Head;

/**
 * 
 * <h3>HeadDao Class description</h3>
 * <p> HeadDao description </p>
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
 * Jun 3, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface HeadDao extends DiecastDao<Head> {

	public Head findByDCSerialNumber(String dcNumber);
	
	public Head findByMCSerialNumber(String mcNumber);
	
	public List<Head> findAllByEngineSerialNumber(String engineId);
	
	
	/**
	 * find all production dates 
	 * @return
	 */
	public List<Date> findAllProductionDates();
	
	public void updateEngineFiringFlag(String productId, short flag);

}
