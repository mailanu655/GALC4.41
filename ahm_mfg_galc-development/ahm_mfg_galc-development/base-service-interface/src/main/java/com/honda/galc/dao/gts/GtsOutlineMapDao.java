package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsOutlineMap;
import com.honda.galc.entity.gts.GtsOutlineMapId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsColorMapDao Class description</h3>
 * <p> GtsColorMapDao description </p>
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
 * May 21, 2015
 *
 *
 */
public interface GtsOutlineMapDao extends IDaoService<GtsOutlineMap, GtsOutlineMapId>{
	
	public List<GtsOutlineMap> findAll(String trackingArea);
	
	public Integer getNextId(String areaName);
	
}
