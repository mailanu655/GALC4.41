package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * From LANE_12B or LANE_1 to LANE_2 AND LANE_12B to LANE_13
 * 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleA extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleA() {
		super();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        // Try 12B-2 first.  In this case, there can be only a single move for the decision point so
        // we return if the move is issued.

        boolean movePossible_12B_2 = isMovePossible(LANE_12B, LANE_2);
       
      	if(movePossible_12B_2) {
           	GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_12B);
           	GtsProduct product = lc.getProduct();
           	
        	if(product == null || (product != null && !product.isReleased()) || 
              	   (product != null && product.isReleased() && (
      
         		!product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_L) &&
         		!product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_M) &&
         		!product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_S))) ){
         			issueMoveRequest(LANE_12B, LANE_2);
         			return;
         	}
        }
        
        // try lane 1 - 2

        boolean movePossible_1_2 = isMovePossible(LANE_1, LANE_2);
        
        if(movePossible_1_2) {
        	GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_1);
           	GtsProduct product = lc.getProduct();
           	if(product == null ||
           			(product != null && !product.getInspectionStatus().equals(GtsInspectionStatus.STOP)))
           			issueMoveRequest(LANE_1, LANE_2);
        }
        
        // lane 12B-13 could be same as lane 1 -2 
        
       
        boolean movePossible_12B_13 = isMovePossible(LANE_12B, LANE_13);
        
        if(movePossible_12B_13) {
        	GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_12B);
           	GtsProduct product = lc.getProduct();
         	if(product != null && product.isReleased() &&
         		(product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_L) ||
         		 product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_M) ||
         		 product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_S))){
         		issueMoveRequest(LANE_12B, LANE_13);
         	}
        }

    }   	

}
