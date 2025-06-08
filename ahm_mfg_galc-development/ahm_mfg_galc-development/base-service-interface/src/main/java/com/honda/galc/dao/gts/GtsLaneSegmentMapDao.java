package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsLaneSegmentMap;
import com.honda.galc.entity.gts.GtsLaneSegmentMapId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsLaneSegmentMapDao Class description</h3>
 * <p> GtsLaneSegmentMapDao description </p>
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
public interface GtsLaneSegmentMapDao extends IDaoService<GtsLaneSegmentMap, GtsLaneSegmentMapId>{
	
	/**
	 * sort by lane_id and lane position seq
	 * @param trackingArea
	 * @return
	 */
	public List<GtsLaneSegmentMap> findAll(String trackingArea);
	
	public void removeAll(String trackingArea,String laneId);

}
