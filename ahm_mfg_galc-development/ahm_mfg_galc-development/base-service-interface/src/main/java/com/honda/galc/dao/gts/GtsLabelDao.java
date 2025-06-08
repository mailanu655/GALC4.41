package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLabelId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsLabelDao Class description</h3>
 * <p> GtsLabelDao description </p>
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
public interface GtsLabelDao extends IDaoService<GtsLabel, GtsLabelId>{
	
	public List<GtsLabel> findAll(String trackingArea);

	public Integer getNextLabelId(String areaName);
}
