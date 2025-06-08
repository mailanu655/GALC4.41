package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsLaneDao;
import com.honda.galc.dao.gts.GtsLaneSegmentMapDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsLaneDaoImpl Class description</h3>
 * <p> GtsLaneDaoImpl description </p>
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
public class GtsLaneDaoImpl extends BaseDaoImpl<GtsLane,GtsLaneId> implements GtsLaneDao{
	
	private static final String UPDATE_LANE_CAPACITY =
		"UPDATE GALADM.GTS_LANE_TBX SET ";
	
	@Autowired
	GtsLaneSegmentMapDao laneSegmentMapDao;
	
	public List<GtsLane> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea", trackingArea));
	}

	public void updateLaneCapacity(String trackingArea, String laneId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@Transactional
	public void remove(GtsLane lane) {
		laneSegmentMapDao.removeAll(lane.getId().getTrackingArea(), lane.getId().getLaneId());
		super.remove(lane);
	}

}
