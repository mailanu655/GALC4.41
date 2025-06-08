package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * From LANE_2 to LANE_3, LANE_4, LANE_5A, LANE_6, LANE_7
 * non-released vin go to lane with biggest non-released vins, emptiest lane, highest lane number
 * Vin with Inspect FAIL_L,FAIL_M,FAIL_S go to emptiest lane with highest lane number
 * released vin go to same lot lane with most empty spaces, lowerest lane number
 * if no same lot lane, got to lane with most empty spaces, lowerest lane number
 * 
 * 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleB extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleB() {
		super();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(!isMoveOutPossible(LANE_2)) return;
        
		String[] laneIds = new String[] {LANE_3,LANE_4,LANE_5A,LANE_6,LANE_7};

        GtsProduct product = getHeadProduct(LANE_2);
        
        if(product == null || !product.isReleased()) {
        	// 1. Lane with most non-release vehicles.
            // 2. Emptiest.
            // 3. Highest numbered.
        	
        	String laneName = getMostNonReleasedLane(laneIds);
        	if(laneName != null) {
        		issueMoveRequest(LANE_2, laneName);
        		return;
        	}
 
        }else if(product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_L) ||
        		 product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_M) ||
        		 product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_S)) {
            // 1. Emptiest Lane
            // 2. Highest numbered lane.
       	
           	String laneName = getEmptiestLane(laneIds);
        	if(laneName != null) {
        		issueMoveRequest(LANE_2, laneName);
        		return;
        	}
        	
        }else {
        	// Pass WELD, Released vehicles
            // 1. Most with the same lot.
            // 2. Emptiest.
            // 3. Lowest numbered lane.
	      	String laneName = getSameLotLane(product.getLotNumber(),laneIds);
           	if(laneName != null) {
        		issueMoveRequest(LANE_2, laneName);
        		return;
        	}else {
        		// no same lot lane
        		// got to lane with biggest number of empty bodies and lowerest lane id
            	laneName = getEmptiestLaneWithLoweresLaneId(laneIds);
               	if(laneName != null) {
            		issueMoveRequest(LANE_2, laneName);
            		return;
               	}
        	}

        }
         
    }
	
	// get lane with biggest number of non-released products , biggest number of empty bodies and highest lane name
	
	private String getMostNonReleasedLane(String[] laneIds) {
		
		int count = 0;
		String laneName = null;
		int availabeSpace = 0;
		
		for(String laneId : laneIds) {
			if(!isMoveInPossible(laneId)) continue;
			GtsLane lane = area.findLane(laneId);
			int lane_count = 0;
			for(GtsLaneCarrier lc : lane.getLaneCarriers()) {
				if(lc.getProduct() == null || 
						(lc.getProduct() != null && !lc.getProduct().isReleased()) ) lane_count++; 
			}
			
			if(lane_count > count || (lane_count == count && lane.getAvailableSpaces() >= availabeSpace)) {
				count = lane_count;
				laneName = laneId;
				availabeSpace = lane.getAvailableSpaces();
			}
		}
		
		return laneName;
	}
	
	private String getEmptiestLane(String[] laneIds) {

		String laneName = null;
		int availabeSpace = 0;
		
		for(String laneId : laneIds) {
			if(!isMoveInPossible(laneId)) continue;
			GtsLane lane = area.findLane(laneId);
			
			if(lane.getAvailableSpaces() >= availabeSpace) {
				laneName = laneId;
				availabeSpace = lane.getAvailableSpaces();
			}
		}
		
		return laneName;
	}
	
	private String getEmptiestLaneWithLoweresLaneId(String[] laneIds) {

		String laneName = null;
		int availabeSpace = 0;
		
		for(String laneId : laneIds) {
			if(!isMoveInPossible(laneId)) continue;
			GtsLane lane = area.findLane(laneId);
			
			if(lane.getAvailableSpaces() > availabeSpace) {
				laneName = laneId;
				availabeSpace = lane.getAvailableSpaces();
			}
		}
		
		return laneName;
	}
	
	private String getSameLotLane(String lot,String[] laneIds) {
		
		int count = 0;
		String laneName = null;
		int availabeSpace = 0;
		
		for(String laneId : laneIds) {
			if(!isMoveInPossible(laneId)) continue;
			GtsLane lane = area.findLane(laneId);
			int lot_count = 0;
			for(int i=lane.getLaneCarriers().size() - 1;i>=0;i--) {
				GtsLaneCarrier lc = lane.getLaneCarriers().get(i);
				if(lc.getProductionLot().equals(lot)) lot_count++;
				else break;
			}			
			if(lot_count > count || (lot_count == count && lane.getAvailableSpaces() > availabeSpace)) {
				count = lot_count;
				laneName = laneId;
				availabeSpace = lane.getAvailableSpaces();
			}
		}
		
		return laneName;
	}
	

}
