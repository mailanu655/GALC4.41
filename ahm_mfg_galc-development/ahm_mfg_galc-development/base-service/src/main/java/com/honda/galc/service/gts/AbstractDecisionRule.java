package com.honda.galc.service.gts;

import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsMoveCondition;
import com.honda.galc.entity.enumtype.GtsIndicatorType;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.util.KeyValue;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>AbstractDecisionRule</code> is an abstract decision rule class<br>
 * it contains some basic common methods which can be used by different<br>
 * decision point. Each decision point has to subclass this class<br>
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Feb 24, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public abstract class AbstractDecisionRule {
    protected AbstractBodyTrackingService handler;
    protected GtsArea area;
    protected GtsDecisionPoint decisionPoint;
    
    protected Message message = null;
    
    public AbstractDecisionRule(){
        
    }
    
    public AbstractDecisionRule(AbstractBodyTrackingService handler) {
        this.handler = handler;
    }

    public AbstractBodyTrackingService getHandler() {
        return handler;
    }
    
    
    public Logger getLogger() {
        
        return handler.getLogger();
        
    }
    
    public void setHandler(AbstractBodyTrackingService handler) {
        this.handler = handler;
    }
    
    
    public GtsDecisionPoint getDecisionPoint() {
        return decisionPoint;
    }

    public void setDecisionPoint(GtsDecisionPoint decisionPoint) {
        this.decisionPoint = decisionPoint;
    }

    
    /**
     * issue move request
     * @param source - source lane
     * @param dest - destination lane
     * @return
     */
    
    protected boolean issueMoveRequest(String source,String dest){
        
        getLogger().info("issue move request from lane: " + source + " to: " + dest);
        
        if(!isMovePossible(source, dest)) return false;
        GtsMove move = getMove(source,dest);
        if(move == null){
            // log emergency move request does not exist
            getLogger().emergency("Invalid source or destination lane " + "Source Lane: " + source + " Destination Lane: " + dest);
        }
        if(move == null || !move.isFinished()) return false;
        
        move.setMoveStatus(GtsMoveStatus.CREATED);
        
        if(handler.getPropertyBean().isIssueMoveRequestEnabled()) {
        	if(!handler.sendMoveRequestToPLC(move)) return false; 
        };
        
        // log issued move request
        getLogger().info("Issued move request - ", move.getIoPointName());
        
        // when we successfully sent the move request to PLC, we want to update move created status to the data base
        
        try {
            handler.updateMove(move);
            return true;
        } catch (Exception e) {
            
            // failed to update data base move created status
            getLogger().error("Could not update Move Status to the database - Move Created Status for " + move.getIoPointName());

        }
        
        return false;
    }
    
    /**
     * get move object 
     * @param source - source lane name
     * @param dest - destination lane name
     * @return
     */
    
    protected GtsMove getMove(String source,String dest){
        
        for(GtsMove move:area.getMoves()){
            if(move.getSource().equals(source) && move.getDestination().equals(dest)) return move;
        }
        
        return null;
    }
    
    /**
     * Check if a move from "fromLane" to "toLane" is possible
     * @param fromLane - from lane
     * @param toLane - to lane
     * @return
     */
    
    protected boolean isMovePossible(String fromLane,String toLane){
        
        boolean flag = isMoveConditionOk(fromLane,toLane) && 
                       isMoveInPossible(fromLane,toLane) && 
                       isMoveOutPossible(fromLane);
        
        //      log debug - is move out possible
        getLogger().debug("GTSD004", "Move from lane " + fromLane + " to lane " + toLane + 
                        " is " + (flag?"" :"not") + " possible" );
        
        return flag;
    }
    
    
    protected Message checkMovePossible(String fromLane, String toLane,GtsArea area) {
        
        this.area = area;
        
        if(!isMoveConditionOk(fromLane,toLane)) 
            return message;
            
        // clear previous message
        message = this.checkMoveInPossible(fromLane,toLane);
        
        if(message != null) return message;
        
        return this.checkMoveOutPossible(fromLane);
        
    }
    
    protected boolean isMoveConditionOk(String fromLane,String toLane) {
        
        message = null;
        
        getLogger().debug("Start checking move request condition. Move Request is from "+ fromLane + " to " + toLane);
        
        for(GtsMoveCondition mc : area.getMoveConditions()) {
            
            if(mc.getId().getSourceLaneId().equals(fromLane) && mc.getId().getDestinationLaneId().equals(toLane)) {
                
                GtsIndicator indicator = area.findIndicator(mc.getId().getIndicatorId());
                
                if(indicator == null) {
                    // log error
                    getLogger().error("Invalid indicator name " + mc.getId().getIndicatorId() + " in the move_condition table");
                    
                    return false;
                    
                }else {
                    
                    if(indicator.getStatus() != mc.getRequiredValue()) {
                        message = Message.logError("Indicator " + indicator.getIndicatorName() + "'s status is " + 
                                                    indicator.getStatus() + ". But " + mc.getRequiredValue() + " is required!");
                        return false;
                    }
                    
                    getLogger().debug("Check indicator status : Indicator " + indicator.getIndicatorName() + "'s current status is " + 
                                    indicator.getStatus() + ", required status is " + mc.getRequiredValue());
                    
                    // when indicator is a MIP, check its corresponding move request
                    if(indicator.isMoveInProgress()){
                        if(this.isMoveRequestCreated(indicator.getSourceLaneName(), indicator.getDestLaneName()))
                            return false;
                    }
                }
            }
        }
        
        return true;
        
    }
    
    
    protected Message checkMoveInPossible(String fromLane,String toLane) {
        
        // clear previous message
        message = null;
        
        boolean flag1 = false;
        if(!fromLane.equals(toLane)) 
            flag1 = this.isPhysicallyFull(toLane) ||
                    this.isLogicallyFull(toLane);
        
        if((hasEntryGate(toLane) && isEntryGateOpen(toLane)) ||
           this.isMoveInRequestCreated(toLane) ||
           !this.isMoveInFinished(toLane) || flag1)
              return message; 
        else return null;
        
    }
    
    protected boolean isDecisionConditionOk(String fromLane,String toLane) {
   	 
	 if (handler.decisionController.checkDecisionPointConditions( area, fromLane, toLane) == null) 
		 return true;
	 
	 return false;
        
    }
       
    protected Message checkMoveOutPossible(String laneName) {
        
        message = null;
        
        if((hasExitGate(laneName) && isExitGateOpen(laneName))||
           !isCarrierPresent(laneName) || 
           this.isMoveOutRequestCreated(laneName)||
           !this.isMoveOutFinished(laneName)) return message;
        else return null;
        
    }
    
    protected boolean hasExitGate(String laneName) {
    	return area.getExitNode(laneName) != null;
    }
    
    protected boolean hasEntryGate(String laneName) {
    	return area.getEntryNode(laneName) != null;
    }
    
    /**
     * Check if it is possible to move into a lane. This includes
     * if the logical entry gate is open (if there is a logical entry gate
     * if no move in request was created previously
     * if no other other is moving into the lane
     * if this lane is physically full
     * and if the lane is logically full
     * If fromLane == toLane we don't check physically full and logically full
     * @param laneName - lane name
     * @return
     */
    
    protected boolean isMoveInPossible(String fromLane, String toLane){
        
        boolean flag1 = true;
        if(!fromLane.equals(toLane)) 
            flag1 = !this.isPhysicallyFull(toLane)&&
                    !this.isLogicallyFull(toLane);
        boolean flag = 
               (!hasEntryGate(toLane) || isEntryGateOpen(toLane))&&
               !this.isMoveInRequestCreated(toLane)&&
               this.isMoveInFinished(toLane)&& flag1;
        
        //      log debug - is move out possible
        getLogger().debug("Move into lane " + toLane + " is " + (flag?"" :"not") + " possible" );
        
        return flag;
        
    }
    
    protected boolean isMoveInPossibleIgnoreEntryGate(String fromLane, String toLane){
        
        boolean flag1 = true;
        if(!fromLane.equals(toLane)) 
            flag1 = !this.isPhysicallyFull(toLane)&&
                    !this.isLogicallyFull(toLane);
        boolean flag = 
               !this.isMoveInRequestCreated(toLane)&&
               this.isMoveInFinished(toLane)&& flag1;
        
        //      log debug - is move out possible
        getLogger().debug("Move into lane " + toLane + " is " + (flag?"" :"not") + " possible" );
        
        return flag;
        
    }
    
    /**
     * Check if it is possible to move out from a lane. This includes:
     * if the logical exit gate is open(if there is a logical exit gate)
     * if a carrier is present at head of lane 
     * if no move out request was created previously
     * if no other carrier is moving out from the lane 
     * @param laneName - lane name 
     * @return
     */
    
    protected boolean isMoveOutPossible(String laneName) {

        boolean flag = 
               isExitGateOpen(laneName)&&
               isCarrierPresent(laneName)&&
               !isLaneEmpty(laneName)&&
               !this.isMoveOutRequestCreated(laneName)&&
               this.isMoveOutFinished(laneName);
        
        //      log debug - is move out possible
        getLogger().debug("Move out from lane " + laneName + " is " + (flag?"" :"not") + " possible" );
        return flag;
        
    }
    
    protected boolean isMoveOutPhysicallyPossible(String laneName) {

        boolean flag = 
               isExitGateOpen(laneName)&&
 //              !isCarrierPresent(laneName)&&
               !this.isMoveOutRequestCreated(laneName)&&
               this.isMoveOutFinished(laneName);
        
        //      log debug - is move out possible
        getLogger().debug("Move out from lane " + laneName + " is " + (flag?"" :"not") + " possible" );
        return flag;
        
    }
    
    /**
     * check if any carrier is moving into the current lane
     * @param laneName
     * @return
     */
    
    protected boolean isMoveInFinished(String laneName){
        
        boolean flag = true;
        
        List<GtsIndicator> indicators = area.findIndicators(GtsIndicatorType.MOVE_IN_PROGRESS);
        for(GtsIndicator indicator:indicators){
            if(indicator.isStatusOn() && indicator.getDestLaneName().equals(laneName)){
                
                message = Message.logError("A carrier is moving from lane " + indicator.getSourceLaneName() + " to " + indicator.getDestLaneName());
                flag = false;
                break;
                
            }
        }

        //      log debug - is move in finished
        getLogger().debug("Move into lane " + laneName + " is " + (flag?"" :"not") + " finished" );

        return flag;
    }
    
    /**
     * check if any carrier is moving out from the lane 
     * @param laneName
     * @return
     */
    
    protected boolean isMoveOutFinished(String laneName){
        
        boolean flag = true;
        
        List<GtsIndicator> indicators = area.findIndicators(GtsIndicatorType.MOVE_IN_PROGRESS);
        for(GtsIndicator indicator:indicators){
            if(indicator.isStatusOn() && indicator.getSourceLaneName().equals(laneName)){
                
                message = Message.logError("A carrier is moving from lane " + indicator.getSourceLaneName() + " to " + indicator.getDestLaneName());
                flag = false;
                break;
                
            }
        }

        //      log debug - is move created
        getLogger().debug("Move out from lane " + laneName + " is " + (flag?"" :"not") + " finished" );

        return flag;
    }
    
    /**
     * check if any move request created to move into the lane
     * @param laneName
     * @return
     */
    
    protected boolean isMoveInRequestCreated(String laneName){
        
        boolean flag = false;
        
        for(GtsMove move:area.getMoves()){
            if(move.isCreated() && move.getDestination().equals(laneName)){
                message = Message.logError("Move request from " + move.getSource() + " to " + move.getDestination() + " has been created");
                flag = true;
                break;
            }
        }
        
        //      log debug - is move created
        getLogger().debug("Request to move into lane " + laneName + 
                        " is " + (flag?"" :"not") + " created" );

        return flag;
    }
    
    /**
     * check if any move request created to move out from the lane
     * @param laneName - lane name 
     * @return
     */
    
    protected boolean isMoveOutRequestCreated(String laneName){
        
        boolean flag = false; 
        
        for(GtsMove move:area.getMoves()){
            if(move.isCreated() && move.getSource().equals(laneName)){
                
                message = Message.logError("Move request from " + move.getSource() + " to " + move.getDestination() + " has been created");
                flag = true;
                break;
            }
        }
        
        //      log debug - is move created
        getLogger().debug("Request to move out from lane " + laneName + 
                        " is " + (flag?"" :"not") + " created" );

        return flag;
    }
    
    /**
     * check if any move request from "fromLane" to "toLane" created
     * @param fromLane - source lane name
     * @param toLane - destination lane name
     * @return
     */
    
    protected boolean isMoveRequestCreated(String fromLane,String toLane){
        
        boolean flag = false;
        
        for(GtsMove move:area.getMoves()){
            if(move.isCreated() && 
               move.getSource().equalsIgnoreCase(fromLane) && 
               move.getDestination().equalsIgnoreCase(toLane)){
                message = Message.logError("Move request from " + move.getSource() + " to " + move.getDestination() + " has been created");
                flag = true;
                break;
            }
        }
        
        //      log debug - is move created
        getLogger().debug("Move request from " + fromLane + " to " + toLane + 
                        " is " + (flag?"" :"not") + " created" );

        return flag;
    }
    
    /**
     * check if a move from sourceLane to destLane is finished
     * @param sourceLane
     * @param destLane
     * @return
     */
    
    protected boolean isMoveFinished(String sourceLane, String destLane){
        
        boolean flag = false;
        for(GtsMove move:area.getMoves()){
            if(move.getSource().equals(sourceLane) && move.getDestination().equals(destLane)){
                if(move.isCreated()) 
                    message = Message.logError("Move request from " + move.getSource() + " to " + move.getDestination() + " has been created");
                else if(move.isStarted()) 
                    message = Message.logError("Carrier is moving from lane " + move.getSource() + " to " + move.getDestination());
                flag = move.isFinished();
                
                break;
            }
        }
        
        //      log debug - is move finished
        getLogger().debug("Move from " + sourceLane + " to " + destLane +
                        " is " + (flag?"" :"not") + " finished" );
        
        return flag;
  }
    
    /**
     * check if the lane is physically full
     * @param laneName - lane name
     * @return
     */
    
    protected boolean isPhysicallyFull(String laneName){
        
        boolean flag = area.isLanePhysicallyFull(laneName); 
        if(flag == true) message = Message.logError("Lane " + laneName + " is physically full");
        
        //      log debug - lane is physically full
        getLogger().debug("Lane " + laneName + " is " + (flag? "" : "not") + " physically full" );
        
        return flag;
        
    }
    
    
    /**
     * check if the lane is logically full
     * @param laneName - lane name
     * @return
     */
    
    protected boolean isLogicallyFull(String laneName){
        
        GtsLane lane = area.findLane(laneName);
        if(lane == null) {
        	getLogger().error("Invalid configuration - lane name does not exist : " + laneName);
        	return false;
        }
        
    	boolean flag =  lane .isFull();
        
        if(flag == true) message = Message.logError("Lane " + laneName + " is logically full");
        
        //      log debug - logically Full 
        getLogger().debug("Lane " + laneName + " is " + (flag? "" : "not") + " logically full" );
        
        return flag;
    }
    
    public boolean isLogicallyAndPhysicallyFule(String laneName) {
    	return isLogicallyFull(laneName) && isPhysicallyFull(laneName);
    }
    
    /**
     * check if a carrier present at head of lane 
     * @param laneName - lane name
     * @return
     */
    
    protected boolean isCarrierPresent(String laneName){
        
        List<GtsIndicator> indicators = area.findIndicators(GtsIndicatorType.CARRIER_PRESENT);
        indicators.addAll(area.findIndicators(GtsIndicatorType.CPLF));
        for(GtsIndicator indicator:indicators){
            if(indicator.getLaneName().equals(laneName)){
                if(!indicator.isStatusOn()) 
                    message = Message.logError("No carrier is physically at head of lane " + laneName);
                return indicator.isStatusOn();
            }
        }
 
        return true;
    }
    
    protected boolean isStatusOn(String indicatorId) {
    	GtsIndicator indicator = area.findIndicator(indicatorId);
    	return indicator == null ? false : indicator.isStatusOn();
    }
    
    /**
     * check if the logical entry gate is open or not
     * @param lanename
     * @return
     */
    
    protected boolean isEntryGateOpen(String laneName){
    	
    	boolean flag =  area.isEntryGateOpen(laneName);
        
        if(flag) message = Message.logError("Entry gate of lane " + laneName + " is open");
        // log debug -  gate status
        getLogger().debug("Entry gate " + laneName + " is " + (flag ? "open" : "closed"));
        
        return flag;
    }

    /**
     * check if the logical exit gate is open or not
     * @param lanename
     * @return
     */
    
    protected boolean isExitGateOpen(String laneName){
        
        boolean flag = area.isExitGateOpen(laneName);
        
        if(flag) message = Message.logError("Exit gate of lane " + laneName + " is open");
        
        //      log debug -  gate status
        getLogger().debug("Exit gate " + laneName + " is " + (flag ? "Open" : "Closed"));
        
        return flag;
    }
    
    //may need to be removed Xiaofen 
    protected boolean isSameLastLot(boolean isMovePossible,String laneName, String prodLot) {
    	return isMovePossible && area.findLane(laneName).isSameLastReleasedProductLot(prodLot);
    }
    
    protected boolean isSameReleasedLastLot(boolean isMovePossible,String laneName, String prodLot) {
    	return isMovePossible && area.findLane(laneName).isSameLastReleasedProductLot(prodLot);
    }
    
    protected boolean isSameReleasedHeadLot(boolean isMovePossible,String laneName, String prodLot) {
    	if(!isMovePossible) return false;
    	
    	GtsLaneCarrier laneCarrier = area.findLane(laneName).getHeadCarrier();
    	return laneCarrier != null && laneCarrier.getProduct() !=null && laneCarrier.getProduct().isReleased()
    			&& prodLot.equalsIgnoreCase(laneCarrier.getProductionLot());
    }
    
    protected boolean isSameHeadLot(boolean isMovePossible,String laneName, String prodLot) {
    	if(!isMovePossible) return false;
    	
    	GtsLaneCarrier laneCarrier = area.findLane(laneName).getHeadCarrier();
    	return laneCarrier != null && laneCarrier.getProduct() !=null
    			&& prodLot.equalsIgnoreCase(laneCarrier.getProductionLot());
    }

    protected int getSameLotCount(boolean isMovePossible, String laneName, String prodLot) {
    	if(!isMovePossible) return 0;
    	GtsLane lane =area.findLane(laneName);
    	int count = 0;
    	for (GtsLaneCarrier laneCarrier: lane.getLaneCarriers())
    		if(laneCarrier.getProductionLot().equalsIgnoreCase(prodLot)) count++;
    	return count;
    }
    	
    protected boolean isSameLot(boolean isMovePossible,String laneName, int position,String prodLot) {
    	if(!isMovePossible) return false;
    	GtsLane lane = area.findLane(laneName);
    	if(position > (lane.getLaneCarriers().size() -1) ) return false;
    	
    	GtsLaneCarrier laneCarrier = lane.getLaneCarriers().get(position);
    	return (laneCarrier != null && laneCarrier.getProduct() !=null 
    			&& prodLot.equalsIgnoreCase(laneCarrier.getProductionLot()));
    }
    
    protected boolean isHeadOfLaneEmptyCarrier(boolean isMovePossible,String laneName) {
    	if(!isMovePossible) return false;
    	GtsLaneCarrier laneCarrier = area.findLane(laneName).getHeadCarrier();
    	return handler.isEmptyCarrier(laneCarrier.getCarrierId());
    }
    
    protected boolean isSameLot(String laneName, String prodLot) {
    	return area.findLane(laneName).sameLotCount(prodLot) > 0;
    }
    
    protected boolean isSameReleasedProductLot(String laneName, String prodLot) {
    	return area.findLane(laneName).sameReleasedProductLotCount(prodLot) > 0;
    }
    protected boolean isLaneEmpty(String laneName) {
    	return area.findLane(laneName).getLaneCarriers().isEmpty();
    }
    
    protected boolean isSmallerLotNumber(String laneName, String prodLot) {
    	GtsLaneCarrier laneCarrier = area.findLane(laneName).getTailCarrier();
    	if(laneCarrier != null) {
    		return prodLot.compareTo(laneCarrier.getProductionLot()) < 0;
    	}else return false;
    }
    
	protected boolean hasLotInStorage(String prodLot, String... lanes) {
		for(String laneName :lanes) {
			if(isSameReleasedProductLot(laneName, prodLot)) return true;
		}
		return false;
	}
	
	protected GtsLane getOldestReleasedHeadLotInStorage(boolean isMovePossible,String laneName, GtsLane currentLane) {
		if(!isMovePossible) return currentLane;
		GtsLane lane = area.findLane(laneName);
		GtsLaneCarrier laneCarrier = lane.getHeadCarrier();
		if(laneCarrier == null || laneCarrier.getProduct() == null || !laneCarrier.getProduct().isReleased()) return currentLane;
		if(currentLane == null || laneCarrier.getProductionLot().compareToIgnoreCase(currentLane.getHeadCarrier().getProductionLot()) < 0)
			return lane;
		else return currentLane; 
	}
	
	protected GtsLane getLowestLotInStorage(boolean isMovePossible,String laneName, GtsLane currentLane) {
		if(!isMovePossible) return currentLane;
		GtsLane lane = area.findLane(laneName);
		GtsLaneCarrier laneCarrier = lane.getReleasedTailCarrier();
		
		if(laneCarrier == null || laneCarrier.getProduct() == null) return currentLane;
		if(currentLane == null || laneCarrier.getProductionLot().compareToIgnoreCase(currentLane.getReleasedTailCarrier().getProductionLot()) < 0)
			return lane;
		else return currentLane; 
	}
	
	protected KeyValue<GtsLane,Integer> getGreatestEmptySpaceLaneInStorage(boolean isMovePossible,String laneName, KeyValue<GtsLane,Integer> currentItem) {
		if(!isMovePossible) return currentItem;
		GtsLane lane = area.findLane(laneName);
		int count = lane.getAvailableSpaces();
		if(count > 0 && (currentItem == null || count > currentItem.getValue())) {
			return new KeyValue<GtsLane,Integer>(lane,count);
		}else 
			return currentItem;
	}
    
    
    /**
     * execute the rule
     *
     */
    
    abstract public void executeRule(GtsArea area);

    
}
