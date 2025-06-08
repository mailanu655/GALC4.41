package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsDecisionPointId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsDecisionPointDao Class description</h3>
 * <p> GtsDecisionPointDao description </p>
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
 * Jun 25, 2015
 *
 *
 */
public interface GtsDecisionPointDao extends IDaoService<GtsDecisionPoint, GtsDecisionPointId>{
	
	public List<GtsDecisionPoint> findAll(String trackingArea);
	
	public Integer getNextId(String areaName);

}
