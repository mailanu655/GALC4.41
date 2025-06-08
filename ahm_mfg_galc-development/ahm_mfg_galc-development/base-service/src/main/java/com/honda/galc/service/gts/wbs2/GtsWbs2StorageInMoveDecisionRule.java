package com.honda.galc.service.gts.wbs2;

import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.service.gts.AbstractBodyTrackingService;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule Class description</h3>
 * <p> GtsPbs1MoveDecisionRule description </p>
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
 * @author is08925<br>
 * Nov 30, 2017
 *
 *
 */
public class GtsWbs2StorageInMoveDecisionRule extends GtsWbs2BaseMoveDecisionRule{
	
	private final String LANE_MERGE = "Merge";
	private final String LANE_RepairIn = "RepairIn";
	private final String MC_StorageIn = "MC-StorageIn";


	public GtsWbs2StorageInMoveDecisionRule(AbstractBodyTrackingService handler) {
		this.setHandler(handler);
	}

	@Override
	public void executeRule(GtsArea area) {

	}
	
	public void executeRule(GtsIndicator indicator) {
		
		boolean isMoveIn = processStorgeIn(indicator);
		sendIndictorToPLC(MC_StorageIn, isMoveIn);
		
	}
	
	private boolean processStorgeIn(GtsIndicator indicator) {
		final String LANE_Dest = indicator.getDestLaneName();
		GtsArea area = handler.fetchArea();
		GtsLane lane_source = area.findLane(LANE_MERGE);
		GtsLaneCarrier headCarrier_source = lane_source.getHeadCarrier();

		if (headCarrier_source == null || headCarrier_source.isEmptyCarrier()) {
			return false;
		} else if (LANE_Dest.equalsIgnoreCase(LANE_RepairIn)) {
			return true;
		} else if (headCarrier_source.getDefectStatusString().equalsIgnoreCase(DefectStatus.OUTSTANDING.getName())) {
			return LANE_Dest.equalsIgnoreCase(LANE_RepairIn);
		} else if (lanesGroupFull(LANE_Dest,area)) {
			return false;
		} else {
			String headLot_Source = headCarrier_source.getProductionLot();
			if (getLastLotStartingLane(LANE_Dest,area).equalsIgnoreCase(headLot_Source)) {
				return true;
			} else if (getLastLotStartingLane(LANE_Transit1In,area).equalsIgnoreCase(headLot_Source)
					|| getLastLotStartingLane(LANE_Transit2In,area).equalsIgnoreCase(headLot_Source)
					|| getLastLotStartingLane(LANE_Transit3In,area).equalsIgnoreCase(headLot_Source)) {
				return false;
			} else {
				return true;
			}
		}

	}
	
	private boolean lanesGroupFull(String laneName, GtsArea area) {
		String[] laneNames;

		if (LANE_Transit1In.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane1A, LANE_Lane1B };
		} else if (LANE_Transit2In.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane2A, LANE_Lane2B };
		} else if (LANE_Transit3In.equals(laneName)) {
			laneNames = new String[] { laneName, LANE_Lane3A, LANE_Lane3B };
		} else
			return false;

		int capacity = 0;
		int carrierCount = 0;
		for (String laneId : laneNames) {
			GtsLane lane = area.findLane(laneId);
			if(lane !=null) {
			capacity = capacity + lane.getLaneCapacity();
			carrierCount = carrierCount + lane.getLaneCarriers().size();
			}
		}
		return ((capacity - 1) <= carrierCount ? true : false);
	}


}
