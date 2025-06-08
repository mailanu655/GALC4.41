package com.honda.galc.service.gts.wbs2;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsIndicator;
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
public class GtsWbs2StorageOutMoveDecisionRule extends GtsWbs2BaseMoveDecisionRule{

	private final String MC_StorageOut = "MC-StorageOut";

	
	public GtsWbs2StorageOutMoveDecisionRule(AbstractBodyTrackingService handler) {
		this.setHandler(handler);
	}

	@Override
	public void executeRule(GtsArea area) {
		
	}
	
	public void executeRule(GtsIndicator indicator) {
		boolean isMoveIn = processStorageOut(indicator);
		sendIndictorToPLC(MC_StorageOut, isMoveIn);
	}
	
	private boolean processStorageOut(GtsIndicator indicator) {
		
		final String LANE_Source = indicator.getSourceLaneName();
		final String LANE_Dest = indicator.getDestLaneName();

		GtsArea area = handler.fetchArea();

		final String dest_lot = getLastLotStartingLane(LANE_Dest,area);

		GtsLaneCarrier headCarrier_source = headOfLanesGroup(LANE_Source,area);

		if (headCarrier_source == null) {
			return false;
		} else if (headCarrier_source.isEmptyCarrier() || storageOutEmpty(area)
				|| headCarrier_source.getProductionLot().equalsIgnoreCase(dest_lot)) {
			return true;
		} else {
			String[] laneNames = { LANE_Lane1B, LANE_Lane2B, LANE_Lane3B, LANE_Short };
			if (!StringUtils.isEmpty(dest_lot)) {
				for (String laneId : laneNames) {
					GtsLaneCarrier headCarrier = headOfLanesGroup(laneId,area);
					if (headCarrier != null && headCarrier.getProductionLot().equalsIgnoreCase(dest_lot) ){
							return false;
						}
					}
				}
			String headLot_source = headCarrier_source.getProductionLot();
			String minLot = headLot_source;
			for (String laneId : laneNames) {
				GtsLaneCarrier headCarrier = headOfLanesGroup(laneId,area);
				if (headCarrier != null) {
					String headCarrieLott = headCarrier.getProductionLot();
					if (minLot.compareToIgnoreCase(headCarrieLott) > 0) {
						minLot = headCarrieLott;
						break;
					}
				}
			}
			
			return headLot_source.equalsIgnoreCase(minLot);
		}
	}

}
