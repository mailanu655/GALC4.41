package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsLaneSegmentMapDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsLaneSegmentMap;
import com.honda.galc.entity.gts.GtsLaneSegmentMapId;
import com.honda.galc.notification.GtsNotificationSender;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsLaneSegmentMapDaoImpl Class description</h3>
 * <p> GtsLaneSegmentMapDaoImpl description </p>
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
public class GtsLaneSegmentMapDaoImpl extends BaseDaoImpl<GtsLaneSegmentMap,GtsLaneSegmentMapId> implements GtsLaneSegmentMapDao{

	private static String UPDATE_SEQUENCES = 
		"update GtsLaneSegmentMap set id.laneSeq = id.laneSeq -1 where id.trackingArea = :trackingArea and id.laneId = :laneId and id.laneSeq > :laneSeq";
	
	public List<GtsLaneSegmentMap> findAll(String trackingArea) {
		return findAll(Parameters.with("id.trackingArea",trackingArea),new String[] {"id.laneId","id.laneSeq"});
	}
	
	@Transactional
	public void remove(GtsLaneSegmentMap laneSegmentMap) {
	//	updateLaneSequences(laneSegmentMap);
		super.remove(laneSegmentMap);
	}
	
	private void updateLaneSequences(GtsLaneSegmentMap laneSegmentMap) {
		Parameters params = Parameters.with("trackingArea", laneSegmentMap.getId().getTrackingArea())
			.put("laneId",laneSegmentMap.getId().getLaneId()).put("laneSeq", laneSegmentMap.getId().getLaneSeq());
		executeUpdate(UPDATE_SEQUENCES, params);
	}

	@Transactional
	public void removeAll(String trackingArea, String laneId) {
		delete(Parameters.with("id.trackingArea",trackingArea).put("id.laneId",laneId));
	}



}
