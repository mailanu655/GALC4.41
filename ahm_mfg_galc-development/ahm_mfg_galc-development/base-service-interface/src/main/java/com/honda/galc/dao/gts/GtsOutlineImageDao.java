package com.honda.galc.dao.gts;

import com.honda.galc.entity.gts.GtsOutlineImage;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsOutlineImageDao Class description</h3>
 * <p> GtsOutlineImageDao description </p>
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
 * Jun 26, 2015
 *
 *
 */
public interface GtsOutlineImageDao extends IDaoService<GtsOutlineImage, Integer>{
	
	public Integer getNextId();
	
}
