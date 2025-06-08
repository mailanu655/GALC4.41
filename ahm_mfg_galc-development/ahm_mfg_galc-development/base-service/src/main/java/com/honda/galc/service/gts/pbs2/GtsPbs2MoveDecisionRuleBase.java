package com.honda.galc.service.gts.pbs2;

import java.util.List;

import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.service.gts.AbstractDecisionRule;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRuleBase Class description</h3>
 * <p> GtsPbs1MoveDecisionRuleBase description </p>
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
 * @author hcm_adm_008925<br>
 * Sep 24, 2018
 *
 *
 */
public abstract class GtsPbs2MoveDecisionRuleBase extends AbstractDecisionRule{

	protected final String LANE_0   =  "0";   
	protected final String LANE_0A  =  "0A";  
	protected final String LANE_1   =  "1";   
	protected final String LANE_10  =  "10";  
	protected final String LANE_11A =  "11A"; 
	protected final String LANE_11B =  "11B"; 
	protected final String LANE_11C =  "11C"; 
	protected final String LANE_11D =  "11D"; 
	protected final String LANE_12A =  "12A"; 
	protected final String LANE_12B =  "12B"; 
	protected final String LANE_12D =  "12D"; 
	protected final String LANE_12E =  "12E"; 
	protected final String LANE_12F =  "12F"; 
	protected final String LANE_13  =  "13";  
	protected final String LANE_14  =  "14";  
	protected final String LANE_14A =  "14A"; 
	protected final String LANE_15A =  "15A"; 
	protected final String LANE_15B =  "15B"; 
	protected final String LANE_15C =  "15C"; 
	protected final String LANE_17  =  "17";  
	protected final String LANE_18  =  "18";  
	protected final String LANE_19  =  "19";  
	protected final String LANE_2   =  "2";   
	protected final String LANE_20A =  "20A"; 
	protected final String LANE_20C =  "20C"; 
	protected final String LANE_20D =  "20D"; 
	protected final String LANE_20S =  "20S"; 
	protected final String LANE_21B =  "21B"; 
	protected final String LANE_21C =  "21C"; 
	protected final String LANE_21E =  "21E"; 
	protected final String LANE_21G =  "21G"; 
	protected final String LANE_22  =  "22";  
	protected final String LANE_22A =  "22A"; 
	protected final String LANE_23  =  "23"; 
	protected final String LANE_23A =  "23A"; 
	protected final String LANE_23B =  "23B"; 
	protected final String LANE_23C =  "23C"; 
	protected final String LANE_24  =  "24";  
	protected final String LANE_3   =  "3";   
	protected final String LANE_30A =  "30A"; 
	protected final String LANE_30B =  "30B"; 
	protected final String LANE_30C =  "30C"; 
	protected final String LANE_4   =  "4";   
	protected final String LANE_5A  =  "5A";  
	protected final String LANE_5B  =  "5B";  
	protected final String LANE_6   =  "6";   
	protected final String LANE_7   =  "7";   
	protected final String LANE_8   =  "8";   
	protected final String LANE_9   =  "9";   
	protected final String LANE_Exit = "Exit";
	protected final String LANE_I1  =  "I1";  
	protected final String LANE_T1  =  "T1";  
	protected final String LANE_T2  =  "T2";  
	protected final String LANE_T3  =  "T3";  
	protected final String LANE_T5  =  "T5";  
	protected final String DUMMYLOT = "                    ";

	
	public GtsPbs2MoveDecisionRuleBase() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl) super.getHandler();
	}
	
	public GtsLaneCarrier getHeadOfLaneCarrier(String laneId) {
		List<GtsLaneCarrier> laneCarriers = area.findLane(laneId).getLaneCarriers();
		return laneCarriers.isEmpty() ?  null : laneCarriers.get(0);		
	}
	
	public GtsLaneCarrier getTailOfLaneCarrier(String laneId) {
		List<GtsLaneCarrier> laneCarriers = area.findLane(laneId).getLaneCarriers();
		return laneCarriers.isEmpty() ?  null : laneCarriers.get(laneCarriers.size() -1);
		
	}
	
	public GtsProduct getHeadProduct(String laneId) {
		List<GtsLaneCarrier> laneCarriers = area.findLane(laneId).getLaneCarriers();
		return laneCarriers.isEmpty() ?  null : laneCarriers.get(0).getProduct();
		
	}
	
	public GtsProduct getTailProduct(String laneId) {
		List<GtsLaneCarrier> laneCarriers = area.findLane(laneId).getLaneCarriers();
		return laneCarriers.isEmpty() ?  null : laneCarriers.get(laneCarriers.size() -1).getProduct();
		
	}
	
	public GtsProduct getTailNonNullProduct(String laneId) {
		List<GtsLaneCarrier> laneCarriers = area.findLane(laneId).getLaneCarriers();
		
		for (int i = laneCarriers.size()-1; i>=0; i--) {
			GtsLaneCarrier lc= laneCarriers.get(i);			
			if (lc !=null && lc.getProduct() !=null) return lc.getProduct();
		}
		
		return null;	
	}

	protected boolean isMoveInPossible(String toLane){
        
        boolean flag1 = !this.isPhysicallyFull(toLane) && !this.isLogicallyFull(toLane);
        boolean flag = (!hasEntryGate(toLane) || isEntryGateOpen(toLane)) &&
                       !this.isMoveInRequestCreated(toLane) &&
                        this.isMoveInFinished(toLane) && flag1;
        
        //      log debug - is move in possible
        getLogger().debug("Move into lane " + toLane + " is " + (flag?"" :"not") + " possible" );
        
        return flag;
        
    }
    
    protected boolean isProductReleased(GtsProduct product) {
		return product != null && product.isReleased() && 
				(product.getInspectionStatus().equals(GtsInspectionStatus.PASS) || 
				 product.getInspectionStatus().equals(GtsInspectionStatus.WELD));
	}
    
    protected boolean isGoodProduct(GtsLaneCarrier lc) {
    	if (lc == null || lc.isEmptyCarrier() || lc.isUnknownCarrier() || lc.getProduct() == null) return false;
    	
    	GtsProduct product = lc.getProduct();
    	
    	return !product.getInspectionStatus().equals(GtsInspectionStatus.WELD) && !product.getInspectionStatus().equals(GtsInspectionStatus.UNKNOWN);
    }
    
    protected boolean isGoodProductNoPause(GtsLaneCarrier lc) {
    	if (lc == null || lc.isEmptyCarrier() || lc.isUnknownCarrier() || lc.getProduct() == null) return false;
    	
    	GtsProduct product = lc.getProduct();
    	
    	return !product.getInspectionStatus().equals(GtsInspectionStatus.WELD) && 
    		   !product.getInspectionStatus().equals(GtsInspectionStatus.UNKNOWN) &&
    		   !product.getInspectionStatus().equals(GtsInspectionStatus.STOP);
    }
    
    protected boolean isGoodProductNoPauseNoWeld(GtsLaneCarrier lc) {
    	if (lc == null || lc.isEmptyCarrier() || lc.isUnknownCarrier() || lc.getProduct() == null) return false;
    	
    	GtsProduct product = lc.getProduct();
    	
    	return !product.getInspectionStatus().equals(GtsInspectionStatus.WELD) && 
    		   !product.getInspectionStatus().equals(GtsInspectionStatus.UNKNOWN) &&
    		   !product.getInspectionStatus().equals(GtsInspectionStatus.PAUSE);
    }
    
	protected String getBiggestLot(String laneName) {
		GtsLane lane = area.findLane(laneName);
		String lot = "";
		for(GtsLaneCarrier lc : lane.getLaneCarriers()) {
			if(lc.getProduct() != null && lc.getProduct().getLotNumber().compareTo(lot) > 0) {
				lot = lc.getProduct().getLotNumber();
			}
		}		
		return lot;
	}
	
	protected boolean isHeadOfLaneBadProduct(GtsLaneCarrier lc) {
		return lc != null && (lc.isEmptyCarrier() || lc.isUnknownCarrier() || (lc.getProduct().getInspectionStatus().equals(GtsInspectionStatus.UNKNOWN)));
	}
	
	protected String checkLowestLot(boolean movePossible, String laneName, String currentLot, String currentLowestLot) {
    	if(movePossible && !isLaneEmpty(laneName)) {
    		GtsLaneCarrier lc = getHeadOfLaneCarrier(laneName);
    		String lot = lc.getProductionLot();
    		if(!isGoodProduct(lc)) return null;
    		if(currentLot.equals(lot)) return currentLot;
    		else if(lot.compareTo(currentLowestLot) < 0) {
    			return lot;
    		}
    	}
    	
    	return null;
	}
	
	protected String checkLowestLotNoPauseNoWeld(boolean movePossible, String laneName, String currentLot, String currentLowestLot) {
    	if(movePossible && !isLaneEmpty(laneName)) {
    		GtsLaneCarrier lc = getHeadOfLaneCarrier(laneName);
    		String lot = lc.getProductionLot();
    		if(!isGoodProductNoPauseNoWeld(lc)) return null;
    		if(currentLot.equals(lot)) return currentLot;
    		else if(lot.compareTo(currentLowestLot) < 0) {
    			return lot;
    		}
    	}
    	
    	return null;
	}
	
	protected boolean isSameLotGoodHeadProduct(String laneName, String lot) {
		GtsLaneCarrier lc = getHeadOfLaneCarrier(laneName);

		return lc != null && lot.equals(lc.getProductionLot()) && isGoodProduct(lc);
		
	}
	
	// move possible without considering move request, move in progress and carrier present(physical and logical)
	protected boolean isMoveToBeReady(String fromLane, String toLane) {
		return isDecisionConditionOk(fromLane, toLane) && 
        		isMoveConditionOk(fromLane, toLane) && 
        		 !isPhysicallyFull(toLane) && !isLogicallyFull(toLane) &&
                 (!hasEntryGate(toLane) || isEntryGateOpen(toLane)) &&
                 (!hasExitGate(fromLane) || isExitGateOpen(fromLane));
	} 

}
