package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsZone;
import com.honda.galc.entity.gts.GtsZoneId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsZoneDao Class description</h3>
 * <p> GtsZoneDao description </p>
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
 * Jul 8, 2015
 *
 *
 */
public interface GtsZoneDao extends IDaoService<GtsZone, GtsZoneId>{
	
	public List<GtsZone> findAll(String trackingArea);
	
	
}
