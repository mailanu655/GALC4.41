package com.honda.galc.service;

import com.honda.galc.dto.BadgeInfoDto;

/**
 * 
 * <h3>BadgeService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BadgeService description </p>
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
 * <TR>
 * <TD>K. Maharjan</TD>
 * <TD>March 12,2024</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * @since March 12,2024
 */

public interface BadgeService extends IService {
	BadgeInfoDto getUserByBadge(int badgeid); 

}
