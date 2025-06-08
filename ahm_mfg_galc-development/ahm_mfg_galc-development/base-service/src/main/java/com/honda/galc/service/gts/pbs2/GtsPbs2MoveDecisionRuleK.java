package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * INPUT LANEs - LANE_14
 * OUTPUT LANEs - LANE_19
 * allow released vehicle to move
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleK extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleK() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(isMovePossible(LANE_14, LANE_19)) {
        	GtsProduct product = getHeadProduct(LANE_14);
        	
        	if(product != null && product.getInspectionStatus() != GtsInspectionStatus.PASS && product.getInspectionStatus() != GtsInspectionStatus.WELD ) 
        		return;
        	if(isProductReleased(product)) issueMoveRequest(LANE_14, LANE_19);
        }
         
    }  	

}
