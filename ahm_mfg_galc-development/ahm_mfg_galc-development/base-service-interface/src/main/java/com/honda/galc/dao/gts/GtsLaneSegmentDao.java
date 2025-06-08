package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsLaneSegmentId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsLaneSegmentDao Class description</h3>
 * <p> GtsLaneSegmentDao description </p>
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
 * May 27, 2015
 *
 *
 */
public interface GtsLaneSegmentDao extends IDaoService<GtsLaneSegment, GtsLaneSegmentId>{
	
	public List<GtsLaneSegment> findAll(String trackingArea);
	
	public GtsLaneSegment createLaneSegment(GtsLaneSegment segment);
	
	public Integer getNextLaneSegmentId(String areaName);
	
	public GtsLaneSegment replaceNode(GtsLaneSegment segment, int fromId, int toId);
	
}
