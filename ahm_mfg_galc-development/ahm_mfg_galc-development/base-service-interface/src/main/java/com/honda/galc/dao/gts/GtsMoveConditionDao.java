package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsMoveCondition;
import com.honda.galc.entity.gts.GtsMoveConditionId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsMoveConditionDao Class description</h3>
 * <p> GtsMoveConditionDao description </p>
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
public interface GtsMoveConditionDao extends IDaoService<GtsMoveCondition, GtsMoveConditionId>{
	
	public List<GtsMoveCondition> findAll(String trackingArea);
	
}
