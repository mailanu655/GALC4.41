package com.honda.galc.service.gts.wbs2;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.service.gts.AbstractDecisionRule;

public abstract class GtsWbs2BaseMoveDecisionRule extends AbstractDecisionRule{

	protected final String LANE_Lane1B = "Lane1B";
	protected final String LANE_Lane2B = "Lane2B";
	protected final String LANE_Lane3B = "Lane3B";
	
	protected final String LANE_Lane1A = "Lane1A";
	protected final String LANE_Lane2A = "Lane2A";
	protected final String LANE_Lane3A = "Lane3A";

	protected final String LANE_Transit1In = "Transit1In";
	protected final String LANE_Transit2In = "Transit2In";
	protected final String LANE_Transit3In = "Transit3In";
	protected final String LANE_Short = "Short";

	private final String LANE_Lane1Out = "Lane1Out";
	private final String LANE_Lane2Out = "Lane2Out";
	private final String LANE_Lane3Out = "Lane3Out";
	private final String LANE_RepairOut = "RepairOut";

	private final String LANE_PaintOn2 = "PaintOn2";
	private final String LANE_PaintOn1 = "PaintOn1";
	private final String LANE_JigDeck3 = "JigDeck3";
	private final String LANE_JigDeck2 = "JigDeck2";
	private final String LANE_JigDeck1 = "JigDeck1";
	private final String LANE_LanesExit = "LanesExit";



	protected String getLastLotStartingLane(String laneName, GtsArea area) {
		String[] laneNames;

		if (LANE_Transit1In.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane1A, LANE_Lane1B };
		} else if (LANE_Transit2In.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane2A, LANE_Lane2B };
		} else if (LANE_Transit3In.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane3A, LANE_Lane3B };
		} else if (LANE_Lane1Out.equals(laneName) || LANE_Lane2Out.equals(laneName) || LANE_Lane3Out.equals(laneName)
				|| LANE_RepairOut.equals(laneName)) {
			laneNames = new String[] { LANE_LanesExit, LANE_JigDeck1, LANE_JigDeck2, LANE_JigDeck3, LANE_PaintOn1,
					LANE_PaintOn2 };
		} else
			return "";

		for (String laneId : laneNames) {
			GtsLane lane = area.findLane(laneId);
			if (lane != null) {
			String lastLot = lane.getProdLotOfLastReleasedProduct();
			if (!StringUtils.isEmpty(lastLot))
				return lastLot;
		}
		}
		return "";
	}
	
	protected GtsLaneCarrier headOfLanesGroup(String laneName,GtsArea area) {
		String[] laneNames ;
		if (LANE_Lane1B.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane1A, LANE_Transit1In };
		} else if (LANE_Lane2B.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane2A, LANE_Transit2In };
		} else if (LANE_Lane3B.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane3A, LANE_Transit3In };
		} else if (LANE_Short.equals(laneName)) {
			laneNames = new String[] { laneName };
		} else {
			laneNames = new String[] {};
		}

		GtsLaneCarrier headCarrier = null;
		for (String laneId : laneNames) {
			GtsLane lane = area.findLane(laneId);
			headCarrier = lane.getHeadCarrier();
			if (lane != null && (headCarrier = lane.getHeadCarrier()) != null ) {
				return headCarrier;			
			}
		}
		return headCarrier;
	}
	
	protected boolean storageOutEmpty(GtsArea area) {
		String[] laneNames = new String[] { LANE_PaintOn2, LANE_PaintOn1, LANE_JigDeck3, LANE_JigDeck2, LANE_JigDeck1,
				LANE_LanesExit };

		for (String laneId : laneNames) {
			GtsLane lane = area.findLane(laneId);
			if (lane != null  && !lane.getLaneCarriers().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	protected boolean sendIndictorToPLC(String ioPointName, boolean value) {
		return handler.sendIndicator(ioPointName, value? 1:0);
	}


}
