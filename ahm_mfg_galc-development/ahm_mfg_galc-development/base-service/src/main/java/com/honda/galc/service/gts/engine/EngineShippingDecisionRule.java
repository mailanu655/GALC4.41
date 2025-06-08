package com.honda.galc.service.gts.engine;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.service.gts.AbstractDecisionRule;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ShippingDecisionRule</code> is the engine shipping 
 * decision rule. Based on the current active shipping quorum 
 * which is set by the shipping sytem(or shipping client),
 * it tries to allocate the quorum or load the engines into the
 * allocated quorum. <br>
 * When allocating the quorum, after it allocates one engine to a specific
 * quorum sequence(pallet position), it assigns the sequence number to the
 * engine, and GUI clients will display the sequences.
 * If the quorum is the normal shipping quorum, the rule is to try to 
 * allocate the specific YMTO from lane Z15 first, then to try to allocate
 * from lane Z14.<br>
 * If the quorum is a repair quorum, the rule is to find the 
 * engines in the repair status and with the specific pallet type  
 * ("NP2", or "NP4") in the buffer lane Z15.<br>
 * If the quorum is an exceptional one, the rule is to find the engines
 * with the specific YMTO in the buffer lane Z15.<br>
 * When allocating the quorum, it also tries to detect if the buffer will
 * overflow. If it does overflow, the rule will send the GUI clients the
 * emergency messege.<br>
 * When it finishs allocating the quorum, it will send the quorum information
 * to the delivery PLC. The information includes trailer number,trailer row 
 * number,quorum size,pallet type("NP2","NP4") and quorum type(Normal,Repair,
 * Exceptional).
 * When loading the engines, it will move the engine to lane Z16 based on the
 * assigned quorum sequence one by one. If the engine not allocated to a 
 * quorum is in the way, the rule will either move it into buffer from lane Z14
 * or move it from head of lane Z3 to its end.
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

public class EngineShippingDecisionRule extends AbstractDecisionRule{
    
     
    private ShippingQuorum quorum;
    
    // previous allocated product ids
    private Map<String,Integer> oldAllocatedProductIds;
    
    // lane names
    private final String entryLane = "Z14";
    private final String loopLane = "Z15";
    private final String exitLane = "Z16";
    
    public EngineShippingDecisionRule(){
        super();
    }
    
    public EngineShippingDecisionRule(GtsEngineBodyTrackingServiceImpl handler){
        super(handler);
    }
    
    public GtsEngineBodyTrackingServiceImpl getHandler(){
        return (GtsEngineBodyTrackingServiceImpl) super.getHandler();
    }
    
    /**
     * execute the rule
     */
    
    public void executeRule(GtsArea area){
        
        Boolean aFlag = false;
        this.area = area;
        if(area == null) return;
        
        quorum = getHandler().getActiveShippingQuorum();
        
        if(quorum == null) return;
        
        getLogger().info("Current active Quorum : " + quorum);
        
        if(quorum != null) {
            if(quorum.getStatus()== ShippingQuorumStatus.WAITING || quorum.getStatus()== ShippingQuorumStatus.ALLOCATING || quorum.getStatus()== ShippingQuorumStatus.ALLOCATED)
            	allocateQuorum();
            else aFlag = loadEngine();
        };
        
        // no matter if there is a quorum or not, if no MR created 
        // then try to move the empty or nonshippable
        if(!aFlag) moveEmptyOrNonShippable();
        
    }
    
    /**
     *  allocate the quorum 
     */
    
    private void allocateQuorum(){
        
        int count;
        
//      get trailer number 
        getShippingTrailerNumber();
        
        // log allocating the shipping quorum
        getLogger().info("Allocating the active shipping quorum - ", getQuorumInfoString());
        
        getOldAllocatedProducts();
        
        int lastSeq = 0;
        if (quorum.isRepairQuorum()) count = allocateRepairQuorum(lastSeq);
        else if(quorum.isExceptionalQuorum()) count = allocateQuorum(lastSeq,"exceptional");
        else count = allocateQuorum(lastSeq, "normal");
        
        
        if((quorum.isWaiting() || quorum.isAllocating()) && count == quorum.getQuorumSize()){

            // notify shipping bean the quorum has been allocated
            activeShippingQuorumAllocated(quorum);
            
        }
        
        if(quorum.isAllocated() && count < quorum.getQuorumSize()){
            
            // notify shipping bean the quorum has been changed to allocating
            activeShippingQuorumAllocating(quorum);
            
        }
        
        // save the allocated engines into database && publish to the clients
        saveEngineSequences();
    }
    
    
    private void getOldAllocatedProducts(){
        
        oldAllocatedProductIds = new HashMap<String,Integer>();
       
        List<GtsLaneCarrier> laneCarriers = getLaneCarriers();
        
        for(GtsLaneCarrier lc : laneCarriers){
            if(lc.getProductSeq() != 0) oldAllocatedProductIds.put(lc.getProductId(),lc.getProductSeq());
            lc.setProductSeq(0);
        }
    }
    
    /**
     * save the engine quorum sequence into product table
     * and publish change to the clients
     */
    
    private void saveEngineSequences(){
        
        List<GtsProduct> products = new ArrayList<GtsProduct>();
        
        List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>();
        laneCarriers.addAll(area.findLane(exitLane).getLaneCarriers());
        laneCarriers.addAll(area.findLane(entryLane).getLaneCarriers());
        laneCarriers.addAll(area.findLane(loopLane).getLaneCarriers());
        
        // the logic is to make sure to only update those products which have sequence changes 
        
        for(GtsLaneCarrier lc : laneCarriers){
            if(lc.getProductSeq() != 0) {
                if(oldAllocatedProductIds.containsKey(lc.getProductId())){
                    if(oldAllocatedProductIds.get(lc.getProductId()) != lc.getProductSeq()) {
                        products.add(lc.getProduct());
                    };
                    oldAllocatedProductIds.remove(lc.getProductId());
                }else products.add(lc.getProduct());
            }
        }
        
        for(String productId:oldAllocatedProductIds.keySet()){
            for(GtsLaneCarrier lc : laneCarriers){
                if(productId.equals(lc.getProductId())) products.add(lc.getProduct());
            }    
        }
        
        if(products.isEmpty()) {
            
            // log no new engines allocated
            
            getLogger().info("No new engines allocated to the active shiping quorum");
            return;
        }
        
        try {
            
            getHandler().updateProductSequences(products);
            
            // log new engines allocated
            
            getLogger().info("New engines allocated to the active shipping quorum - ", getString(products));
            
        } catch (Exception e) {
            getLogger().error("Failed to update quorum sequence for engines into the data base ",quorum.toString() + " Cause: " + e.getMessage());
        }
    }
    
 private String getQuorumInfoString(){
        
        StringBuilder buf = new StringBuilder();
        buf.append("Quorum - ");
        buf.append(quorum.getTrailerNumber());
        buf.append(" , ");
        buf.append(quorum.getPalletType());
        buf.append(" , ");
        buf.append(quorum.getQuorumSize());
        buf.append(" , (");
        boolean isFirst = true;
        for(ShippingQuorumDetail detail: quorum.getShippingQuorumDetails()){
            if(!isFirst)buf.append(" , ");
            else isFirst = false;
            buf.append(detail.getYmto()); 
        }
        buf.append(")");
        return buf.toString();
 }   
    
    private String getString(List<GtsProduct> products){
        
        StringBuilder buf = new StringBuilder();
        
        buf.append("Engines - ");
        boolean isFirst = true;
        
        for(GtsProduct product: products) {
            
            if(product.getProductSeq() != 0) {
                
                if(!isFirst){
                    buf.append(" , ");
                }else isFirst = false;
                
                buf.append(product.getId().getProductId());
                buf.append(" : ");
                buf.append(product.getProductSeq());
                
            }
        }
        
        return buf.toString();
        
    }
    
    /**
     * allocate the repair quorum
     * find the engines in repair status in the loop buffer Z15
     * need to deal with NP2/NP4 (Pallet size)
     */
    
    private int allocateRepairQuorum(int lastSeq){
        
        int count = lastSeq;
        
        List<GtsLaneCarrier> laneCarriers = getLaneCarriers();
        
        for(GtsLaneCarrier laneCarrier: laneCarriers){
 
            GtsProduct product = laneCarrier.getProduct();
            if(!laneCarrier.isDuplicateDiscrepancy() && !laneCarrier.isPhotoEyeDiscrepancy() && 
               product != null && !product.isShippable() &&
               getHandler().isValidModelCode(product.getModelCode(), quorum.getPalletType())){
               
                laneCarrier.setProductSeq(++count);
        
                if(count >= quorum.getQuorumSize()) break;
            }
        }
        
        // log allocating the shipping quorum
        getLogger().info("Allocating the active shipping quorum - Totally " + count + "engines are allocated for the repair quorum");
        
        
        return count;
        
    }
    
    // get the lane carriers in the order of Z16, Z15 and Z14
    private List<GtsLaneCarrier> getLaneCarriers() {
        List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>();
        laneCarriers.addAll(area.findLane(exitLane).getLaneCarriers());
        laneCarriers.addAll(area.findLane(loopLane).getLaneCarriers());
        laneCarriers.addAll(area.findLane(entryLane).getLaneCarriers());
       return laneCarriers;
    }
    
      
    /**
     * allocate the normal shipping quorum
     *
     */
    
    private int allocateQuorum(int lastSeq, String quorumType){
        
        int count = lastSeq ;
        
        for(int i = lastSeq; i < quorum.getQuorumSize(); i++){

            if(allocateEngine(quorum.getShippingQuorumDetails().get(i))) count++;
            
            if(count >= quorum.getQuorumSize()) break;
        }
        
        // to speed up loading process check to see if we need to move engine into buffer
 //       if(checkBufferAvailability()) moveEngine();
        
        //   log allocating the shipping and exceptional quorum
        getLogger().info("Allocating the active shipping quorum - Totally " + count + " engines are allocated for the " + quorumType + " quorum");

        return count;
        
    }
    
    /**
     * allocate the specified engine at lane Z16 first, if not available, 
     * find it at lane Z15 then Z14
     * @param detail
     * @return
     */
    
    private boolean allocateEngine(ShippingQuorumDetail detail){
        
        if(allocateEngine(area.findLane(exitLane),detail)) return true;
        if(allocateEngine(area.findLane(loopLane),detail)) return true;
        return allocateEngine(area.findLane(entryLane),detail);
        
    }
    
    
    /**
     * allocate the specified engine at a specified lane
     * @param lane - lane name
     * @param detail - ShippingQuorumDetail contains the YMTO info of the engine
     * @return
     */
    
    private boolean allocateEngine(GtsLane lane,ShippingQuorumDetail detail){
        
        for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
            
            GtsProduct product = laneCarrier.getProduct();
            
            if(laneCarrier.getProductSeq() != 0 ||product == null) continue;
            
            if(!laneCarrier.isDuplicateDiscrepancy() && !laneCarrier.isPhotoEyeDiscrepancy() && product.isShippable() && 
                detail.getYmto().equals(product.getProductSpec())){
             
                laneCarrier.setProductSeq(detail.getQuorumSeq());
                if(exitLane.equalsIgnoreCase(lane.getLaneName()))
                    getLogger().info("Allocating the active shipping quorum - engine " + laneCarrier.getProductId() + " at exit lane is allocated. There was possible out of sync problem");

                return true;
            }
        }
        
        return false;
        
    }
    
    /**
     * check buffer Lane Z15 availability
     * this normally happens when the quorum seq are assigned to engines at lane Z14
     * if there are engines before an already assigned engine at lane Z14, those engines have to be moved into buffer Z15
     * in order to let the assigned engine to be able to be loaded. To allow thoes engines to be moved into buffer Z15,
     * the availability of the buffer Z15 space has to be checked
     * the buffer Z3 space should exclude those engines to be loaded to lane Z16
     * @return
     */
    
    private boolean checkBufferAvailability(){

        List <GtsLaneCarrier> laneCarriers = area.findLane(entryLane).getLaneCarriers();
        GtsLane laneZ3 = area.findLane(loopLane);
        
        for(int i = 0;i<laneCarriers.size();i++){
            GtsLaneCarrier laneCarrier = laneCarriers.get(i);
            if(laneCarrier.getProductSeq() == 0) continue;
            
            //  the count of engines which has to be moved into buffer Z15
            // empty carrier will be moved to Z16 directly
            int count = 0;
            for(int j=0; j<i-1; j++){
                GtsLaneCarrier lc = laneCarriers.get(j);
                if(lc.getProduct() == null) continue;
                if(!lc.getProduct().isShippable()|| 
                   lc.getProductSeq() == 0 || 
                   lc.getProductSeq() > laneCarrier.getProductSeq()) count++;
            }
            // if count of engines to be moved into buffer Z15 == 0, it is fine
            if(count == 0) continue;
            
            // otherwise, check if there is enough room in buffer Z15 to hold those engines 
            
            int loadableCount = 0;
            
            for(GtsLaneCarrier lc:laneZ3.getLaneCarriers()){
                if(lc.getProductSeq() != 0 && lc.getProductSeq() < laneCarrier.getProductSeq()) loadableCount++;
            }
            
            if(count + laneZ3.getLaneCarriers().size()- loadableCount > laneZ3.getLaneCapacity()){
                // Emergency - loop will be full can not load more engine into the loop in the future
                return false;
            }
        }
        return true;
    }
    
 
    /**
     * load engine when the shipping quorum is in loading status 
     *
     */
    
    private boolean loadEngine(){
        
        // get nex quroum seq which has to be loaded (Check lane Z14 & Z15)
        int seq = this.getNextQuorumSequence();
        
        getLogger().info("Loading the shipping quorum - Current quorum seq = " + seq + " " + quorum.toString());
        
        boolean aFlag = false;
        if(seq > 0) aFlag = moveEngine(seq);
        
        return aFlag;
        
    }
    
    
    private void moveEmptyOrNonShippable() {
        
        GtsLane lane = area.findLane(entryLane);
        if(!lane.getLaneCarriers().isEmpty()){
            GtsLaneCarrier lc = lane.getLaneCarriers().get(0);
            if(lc.isUnknownCarrier() ||lc.isPhotoEyeDiscrepancy() || lc.isDuplicateDiscrepancy() ||
               (lc.getProduct() != null && !lc.getProduct().isShippable())){
                // move repair engine to loop lane
                issueMoveRequest(entryLane,loopLane);
            }else if(lc.isEmptyCarrier()) {
                // move empty carrier from Z14 to Z16    
                issueMoveRequest(entryLane,exitLane);
            }    
        }    
    }
    
    /**
     * try to move engine with product sequence seq
     * return a flag indicating if a move request is created
     * @param seq
     */
    
    private boolean moveEngine(int seq) {
        
//      log info - load engine 
        getLogger().info("Trying to move the allocated engine into exit lane - Lane " + exitLane + ". Engine quorum sequence = " + seq + " of " + quorum.getQuorumSize());
        
        // check if we have qualified engine at head of lane Z15 to move from Z15 to Z16
        // if not, check if there is qualified engine at the middle of lane Z15 and try to move the engine to the head of lane Z3 
        GtsLane lane = area.findLane(loopLane);
        if(!lane.getLaneCarriers().isEmpty()){
            
            if(lane.getLaneCarriers().get(0).getProductSeq() == seq) {
                
                // carrier at head of lane Z15 has the required product seq
                getLogger().debug("Current engine has right quorum sequence - Engine " + lane.getLaneCarriers().get(0).getProductId() + " at head of Lane Z15");
                
                // to move from 15 to Z16 we have to make sure move finished from z14 to Z15
                if(this.isMoveFinished(entryLane,loopLane)){
                    // just move from Z15 to Z16
                    if(issueMoveRequest(loopLane,exitLane))return true;
                }
            }else {
                
                // if there is carrier with required product seq at middle of lane Z15, try to circle buffer to let the carrier move to head of lane
                GtsLaneCarrier lc = null;
                for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
                    if(laneCarrier.getProductSeq() == seq) lc = laneCarrier;
                }
                
                if(lc != null) {
                    if(circleBuffer()) return true;
                }
            }
        }
        
        // then check if there is qualified engine at head of lane Z14, 
        // if yes, move it from lane Z14 to Z16 and in the mean time check if we can circle the buffer
        // if not, check if there is qualified engine in the middle of lane Z14, then we have to try to move the engine from Z2 into buffer
        lane = area.findLane(entryLane);
        if(!lane.getLaneCarriers().isEmpty()){
            
            GtsLaneCarrier headCarrier = lane.getLaneCarriers().get(0);
            if(headCarrier.getProductSeq() == seq) {
                
                // just try to move from Z14 to Z16
                issueMoveRequest(entryLane,exitLane);
                
                // since move created from Z14 to Z16, check if it is able to circle buffer
                
                circleBuffer();
                
                return true;
            }
            
            GtsLaneCarrier lc = null;
        
            for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
                if(laneCarrier.getProductSeq() == seq) lc = laneCarrier;
            }
            
            if(lc != null && headCarrier.isEmptyCarrier() && !headCarrier.isPhotoEyeDiscrepancy()){
                
                // if the first carrier of z14 is an empty carrier, move into lane Z16
                issueMoveRequest(entryLane,exitLane);
                
            }else if(lc != null) {
                
                // try to move engine in the way into buffer 
                return moveEngineIntoBuffer();
                
            }
        }
        
        return false;
    }
    
    /**
     * try to move engine from Z14 into buffer Z15
     * if no engine is able to move into buffer Z15, try to circle the engines in the buffer Z15
     */
    
    private void moveEngine(){
        
        // try to move engine from lane Z14 into buffer Z15. if move created, no further move need to check
        if(moveEngineIntoBuffer()) return;
        
        circleBuffer();
        
        
    }
    
    /**
     * try to move engine into buffer Z15
     * @return if the move is created
     */
    
    private boolean moveEngineIntoBuffer() {
        
        if(isMoveIntoBufferPossible() && isMoveIntoBufferNeeded()){
            // issue move from Z14 to z15
            return issueMoveRequest(entryLane,loopLane);
        }
        
        return false;
    }
    
    /**
     * check if the engine at head of lane Z14 should be moved into buffer
     * qualified engine is:
     * engine not shippable has to be moved into buffer
     * engine which is not currently assigned to the shipping quorum
     * engine's quorum seq is greater than that of a engine after it at lane Z14 
     * @return
     */
    
    private boolean isMoveIntoBufferNeeded(){
        
        GtsLane lane = area.findLane(entryLane);
        
        // lane Z14 is empty, no engines to move 
        if(lane.getLaneCarriers().isEmpty()) return false;
        
        GtsLaneCarrier lc = lane.getLaneCarriers().get(0);
        
        GtsProduct product = lc.getProduct();
        
        // head of lane product is an empty carrier, and has phote eye discrepancy, has to move into buffer 
        if(product == null){
            return lc.isPhotoEyeDiscrepancy();
        }
        
        // head of lane engine is not shippable (repair, on hold), has to move into buffer
        if(!product.isShippable()) return true;
        
        // head of lane engine is not assigned to the current shipping quorum, has to move into buffer
        if(lc.getProductSeq() == 0) return true;
        
        // if engines behind the head of engine has to be loaded into shipping quorum first, head of lane engine has to be moved into buffer
        boolean moveFlag = false;
        for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
            if(laneCarrier.getProductSeq() !=0 && laneCarrier.getProductSeq() < lc.getProductSeq()) moveFlag = true;
        }
        
        return moveFlag;
    }
    
    /**
     * check if it is possible to move into buffer Z15
     * @return
     */
    
    private boolean isMoveIntoBufferPossible(){
        
        
        return this.isMoveFinished(loopLane, exitLane) && this.isMovePossible(entryLane, loopLane);
        
    }
    
    
    private boolean isCircleBufferPossible(){
        return this.isMovePossible(loopLane, loopLane);
    }
    
    /**
     * Circle the buffer Z15
     * to allow the assigned engine to be moved to the head of lane
     * @return
     */
    
    private boolean circleBuffer(){
        
        // check if it is possible to circle the engine in lane Z15 if not return
        if(!this.isCircleBufferPossible()) return false;
       
        GtsLane lane = area.findLane(loopLane);
        
        // lane Z15 is empty, no engines to move 
        if(lane.getLaneCarriers().isEmpty()) return false;
        
        GtsLaneCarrier lc = lane.getLaneCarriers().get(0);
        
        int minSeq = 0;
        for(GtsLaneCarrier laneCarrier:lane.getLaneCarriers()){
            if(laneCarrier.getProductSeq() > 0){
                if(minSeq == 0 || laneCarrier.getProductSeq()< minSeq)
                    minSeq = laneCarrier.getProductSeq();
            }
                
        }
        
        if(minSeq < lc.getProductSeq() || (minSeq > 0 && lc.getProductSeq() ==0)) {

            // if the min product seq is not the first engine at head of lane Z15
            // need to move the head of lane engine to end
            return issueMoveRequest(loopLane,loopLane);
            
        }
        
        return false;
        
    }
    
    /**
     * Find next quorum sequence at lane Z14 and Z15
     * @return
     */
    
    private int getNextQuorumSequence(){
        
        int seq = Integer.MAX_VALUE;
        
        List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>();
        laneCarriers.addAll(area.findLane(entryLane).getLaneCarriers());
        laneCarriers.addAll(area.findLane(loopLane).getLaneCarriers());
        
        for(GtsLaneCarrier laneCarrier:laneCarriers){
            
            if(laneCarrier.getProductSeq() != 0 && laneCarrier.getProductSeq()  < seq){
                
                if(laneCarrier.getProductSeq() < 0 || laneCarrier.getProductSeq() > quorum.getMaxSequence()) continue;
                
                // repair quorum or same product spec
                if(quorum.isRepairQuorum()||
                   laneCarrier.getProductSpec().equals(quorum.getProductSpec(laneCarrier.getProductSeq())))
                    seq = laneCarrier.getProductSeq();
            }
        }
        
        if(seq == Integer.MAX_VALUE) seq = 0;
        
        return seq;
    }
    
    /**
     * get shipping trailer number from the shipping trailer info table 
     *
     */
    
    private void getShippingTrailerNumber(){
        
        // for repair or exceptional quorum we use default trailer number
        if(quorum.isExceptionalQuorum() || quorum.isRepairQuorum()) return;
        quorum.setTrailerNumber(getHandler().getShippingTrailerNumber(quorum.getTrailerId()));
    }
    
    /**
     * notify shipping bean that the current active shipping quorum has been fully allocated
     * @return if shipping bean processes this and update the corresponding status return true
     *         basically shipping bean will update the quorum from allocating to loading
     */
    
    public boolean activeShippingQuorumAllocated(ShippingQuorum quorum){
        
    	 quorum.setStatus(ShippingQuorumStatus.ALLOCATED);
         getHandler().updateShippingQuorumStatus(quorum);
         return true;
    }
    
    public boolean activeShippingQuorumAllocating(ShippingQuorum quorum){
       quorum.setStatus(ShippingQuorumStatus.ALLOCATING);
       getHandler().updateShippingQuorumStatus(quorum);
       return true;
    }
 
}
