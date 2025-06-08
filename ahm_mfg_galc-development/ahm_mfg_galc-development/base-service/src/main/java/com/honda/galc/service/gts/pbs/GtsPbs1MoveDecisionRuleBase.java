package com.honda.galc.service.gts.pbs;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsArea;
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
public abstract class GtsPbs1MoveDecisionRuleBase extends AbstractDecisionRule{

	protected final String LANE_pA = "pA";
	protected final String LANE_pB = "pB";
	protected final String LANE_pC = "pC";
	protected final String LANE_pD = "pD";
	protected final String LANE_pV = "pV";
	protected final String LANE_pE = "pE";
	protected final String LANE_pF = "pF";
	protected final String LANE_pG = "pG";
	protected final String LANE_pH = "pH";
	protected final String LANE_pI = "pI";
	protected final String LANE_pK = "pK";
	protected final String LANE_pL = "pL";
	protected final String LANE_pM = "pM";
	protected final String LANE_pN = "pN";
	protected final String LANE_pQ = "pQ";
	protected final String LANE_pP = "pP";
	protected final String LANE_pO = "pO";
	protected final String LANE_pJ = "pJ";
	protected final String LANE_pS = "pS";
	
	
	protected final String LANE_pR = "pR";
	protected final String LANE_pW = "pW";
	protected final String LANE_pT = "pT";
	
	protected final String LANE_qE = "qE";
	protected final String LANE_qH = "qH";
	protected final String LANE_wA = "wA";
	
	protected final String LANE_tX = "tX";
	protected final String LANE_tF = "tF";
	protected final String LANE_wQ = "wQ";

	protected final String LANE_tG = "tG";
	protected final String LANE_tE = "tE";
	
	protected final String LANE_tA = "tA";
	protected final String LANE_tD = "tD";
	protected final String LANE_tY = "tY";
	
	protected final String LANE_tC = "tC";
	protected final String LANE_tH = "tH";
	
	protected final String LANE_tZ = "tZ";
	protected final String LANE_tS = "tS";
	protected final String LANE_tW = "tW";
	protected final String LANE_tQ = "tQ";
	protected final String LANE_tR = "tR";
	
	public GtsPbs1MoveDecisionRuleBase() {
		super();
	}
	
	public GtsPbs1MoveDecisionRuleBase(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	protected String getCurrentLot(String laneName) {
		String[] laneNames;
    	
		if(LANE_pG.equals(laneName) || LANE_pI.equals(laneName)) {
    		laneNames = new String[] {laneName,LANE_pO,LANE_pJ};
    	}else if(LANE_pO.equals(laneName)) {
    		laneNames = new String[] {LANE_pO,LANE_pJ};
    	}else return "";
    	
    	for(String laneId : laneNames) {
    		String currentLot = area.findLane(laneId).getProdLotOfLastReleasedProduct();
    		if(!StringUtils.isEmpty(currentLot)) return currentLot; 
    	}
    	
    	return "";
    	
    }
	
	   protected boolean isLogicallyFull(String laneName){       
		   if (laneName.equalsIgnoreCase(LANE_pT)) return false;
		   return super.isLogicallyFull(laneName);
	      
	    }
	   
	    protected boolean isMoveInPossible(String fromLane, String toLane){
	        
	    	if (fromLane.equalsIgnoreCase(LANE_pR)) 
	    		return super.isMoveInPossibleIgnoreEntryGate(fromLane, toLane);
	    	else 
	    		return super.isMoveInPossible(fromLane, toLane);
	    	
	    }
	   
	    protected Message checkMoveInPossible(String fromLane,String toLane) {
	        
	        // clear previous message
	        message = null;
	        
	        boolean flag1 = false;
	        if(!fromLane.equals(toLane)) 
	            flag1 = this.isPhysicallyFull(toLane) ||
	                    this.isLogicallyFull(toLane);
	        
	        if((hasEntryGate(toLane) && (isEntryGateOpen(toLane) && !toLane.equalsIgnoreCase("pT"))) ||
	           this.isMoveInRequestCreated(toLane) ||
	           !this.isMoveInFinished(toLane) || flag1)
	              return message; 
	        else return null;
	        
	    }
	    
	    protected boolean isMoveConditionOk(String fromLane,String toLane) {
	  	  
	    	if (fromLane.equalsIgnoreCase(LANE_pP) &&
	    			(toLane.equalsIgnoreCase(LANE_pM) || toLane.equalsIgnoreCase(LANE_pN))) {
	    		if(this.isPhysicallyFull(LANE_pQ) ||this.isLogicallyFull(LANE_pQ)) {
	    			String msg = "There is a car at lane pQ, move request from lane pP can not be issued";              
	    			handler.getLogger().error("Decision point condition does not meet " + msg);                 
	    			Message.logError(msg);
	    			return false;
	     		}   
	    	}
	    	
	    	if ((fromLane.equalsIgnoreCase(LANE_pM) || fromLane.equalsIgnoreCase(LANE_pN)) &&
	    			toLane.equalsIgnoreCase(LANE_pT) ) {
	    		if(this.isPhysicallyFull(LANE_pW) ||this.isLogicallyFull(LANE_pW)) {
	    			String msg = "There is a car at lane pW, move request to lane pT can not be issued";              
	    			handler.getLogger().error("Decision point condition does not meet " + msg);                 
	    			Message.logError(msg);
	    			return false;
	     		}   
	    	}
	    	return super.isMoveConditionOk(fromLane, toLane);
	    }


	    

}
