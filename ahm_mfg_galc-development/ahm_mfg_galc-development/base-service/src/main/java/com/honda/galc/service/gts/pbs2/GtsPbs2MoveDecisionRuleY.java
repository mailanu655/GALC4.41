package com.honda.galc.service.gts.pbs2;

import java.sql.Timestamp;

import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;


/**
 * LANE T1 -> T2, T1 ->I1, 
 * LANE T5->I1
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleY extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleY() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_T1_T2 = isMovePossible(LANE_T1, LANE_T2);
        boolean movePossible_T1_I1 = isMovePossible(LANE_T1, LANE_I1);
        boolean movePossible_T5_I1 = isMovePossible(LANE_T5, LANE_I1);
        
        if(movePossible_T1_T2 && !isEntryGateOpen(LANE_I1)) {
        	issueMoveRequest(LANE_T1, LANE_T2);
        	deleteTimer();
        	return;
        }else if(movePossible_T5_I1 && !isExitGateOpen(LANE_T1)) {
        	issueMoveRequest(LANE_T5, LANE_I1);
        	deleteTimer();
        	return;
        }else if(movePossible_T1_I1 && !isEntryGateOpen(LANE_T2) && !isExitGateOpen(LANE_T5)) {
        	issueMoveRequest(LANE_T1, LANE_I1);
        	deleteTimer();
        	return;
        }else if(isEntryGateOpen(LANE_I1) && isEntryGateOpen(LANE_T2) && !isExitGateOpen(LANE_T1) && !isExitGateOpen(LANE_T5)) {
        	deleteTimer();
        	return;
        }else if(isEntryGateOpen(LANE_I1) && isExitGateOpen(LANE_T1)) {
        	GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_T1);
    		
        	Timestamp timestamp = getTimestamp();

    		if(lc != null && isCarrierPresent(LANE_T1)) {
	        	if (lc.isEmptyCarrier() || lc.isUnknownCarrier()) {
	        		if(timestamp == null && isMoveOutPossible(LANE_T1)) {
	        			saveTimestamp();
	        			getHandler().logEmergencyAndNotify("Empty, Scrap or Bad-label body at lane T1!\n Please close Entry_T2 and Exit_T5 or close Entry_I1 for the body to move into I1 or T2");
	        			return;
	        		}else if(timestamp != null && (System.currentTimeMillis() - timestamp.getTime() >= 90000)) {
	        			if(movePossible_T1_T2) {
	        				issueMoveRequest(LANE_T1, LANE_T2);
	        			}
	        			if(movePossible_T5_I1) {
	        				issueMoveRequest(LANE_T5, LANE_I1);
	        			}
	        			deleteTimer();
        				getHandler().logEmergencyAndNotify("Timer for waiting for issuing move from T1 is expired. The move request from T1 to T2 is issued.");
        				return;
	        		}else if(timestamp == null && (!isMoveOutFinished(LANE_T1) || isMoveOutRequestCreated (LANE_T1)) ){
	        			if(movePossible_T5_I1) {
	        				issueMoveRequest(LANE_T5, LANE_I1);
	        			}
	        			return;
	        		}
	        	}else {
	        		// good car at lane T1
	        		// not an empty carrier or a scrap body
	        		GtsLaneCarrier lc_T5 = getHeadOfLaneCarrier(LANE_T5);
	        		String lot_T5 = lc_T5 == null ? "" : lc_T5.getProductionLot();
	        		if(lc.getProductionLot().compareTo(lot_T5) < 0) {
	        			if(movePossible_T1_I1) {
	        				issueMoveRequest(LANE_T1, LANE_I1);
	        				deleteTimer();
	        				return;
	        			}else {
	        				if(movePossible_T5_I1) {
	        					issueMoveRequest(LANE_T5, LANE_I1);
	        				}
	        				
	        				if(!isEntryGateOpen(LANE_T2)) {
	        					deleteTimer();
	        				}else {
	        					if(timestamp == null && isMoveOutFinished(LANE_T1) && !isMoveOutRequestCreated(LANE_T1)) {
	        						saveTimestamp();
	        						return;
	        					}else if(timestamp != null && (System.currentTimeMillis() - timestamp.getTime() >= 90000)) {
	        						if(movePossible_T1_T2) {
	        							issueMoveRequest(LANE_T1, LANE_T2);
	        							deleteTimer();
		        	      				getHandler().logEmergencyAndNotify("Timer for waiting for issuing move from T1 is expired. The move request from T1 to T2 is issued.");
		        	      				return;
	        						}
	        					}
	        				}
	        			}
	        		}else {
	        			// lot T1 >=Lot T5
	        			if(movePossible_T1_T2) {
	        				issueMoveRequest(LANE_T1, LANE_T2);
	        			}
	        			if(movePossible_T5_I1) {
	        				issueMoveRequest(LANE_T5, LANE_I1);
	        			}
	        			
	        			deleteTimer();
	        			return;
	        		}
	        		
	        	}
        	}else {
        		if(movePossible_T5_I1) {
        			issueMoveRequest(LANE_T5, LANE_I1);
        		}
        		deleteTimer();
        	}
        }
        
    }
	
	private Timestamp getTimestamp() {
		ComponentStatus status = ServiceFactory.getDao(ComponentStatusDao.class).findByKey("P2PBS", "TBS_TIMER");
		
		Timestamp ts = null;
		
		try {
			if (status != null) ts = Timestamp.valueOf(status.getStatusValue().trim());
		} catch(Exception e) {
			getLogger().error(e, "Error parsing to Timestamp " + status.getStatusValue());
		}
		return ts;
	}
	
	private void saveTimestamp() {
	    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    ServiceFactory.getDao(ComponentStatusDao.class).updateComponentStatusValue("P2PBS", "TBS_TIMER", timestamp.toString());
	}
	
	private void deleteTimer() {
	    ServiceFactory.getDao(ComponentStatusDao.class).remove(new ComponentStatus("P2PBS", "TBS_TIMER", ""));
	}

}
