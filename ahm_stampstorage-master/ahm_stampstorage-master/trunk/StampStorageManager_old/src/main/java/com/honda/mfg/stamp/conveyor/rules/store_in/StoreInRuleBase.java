package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 2/16/11
 */
public abstract class StoreInRuleBase implements StoreInRule {
    private static final Logger LOG = LoggerFactory.getLogger(StoreInRuleBase.class);

    private StoreInRule nextRule;
    private StorageStateContext storageStateContext;

    private StorageStateStoreInWrapper storageStateStoreInWrapper;
    private LaneCondition laneCondition;
    private StoragePriority.Priority priority;

     public StoreInRuleBase(
            StorageStateContext storageStateContext
            , StoreInRule nextRule
    ) {
        this.storageStateContext = storageStateContext;
        this.nextRule = nextRule;
        this.storageStateStoreInWrapper = new StorageStateStoreInWrapper(storageStateContext);
    }
    public StoreInRuleBase(
            StorageStateContext storageStateContext
            , StoreInRule nextRule, LaneCondition laneCondition, StoragePriority.Priority priority
    ) {
        this.storageStateContext = storageStateContext;
        this.nextRule = nextRule;
        this.storageStateStoreInWrapper = new StorageStateStoreInWrapper(storageStateContext);
        this.laneCondition = laneCondition ;
        this.priority = priority;
    }

    public abstract StorageRow computeLane(Carrier carrier);

    public final StorageRow processRule(Carrier carrier) {
        StorageRow lane = computeLane(carrier);

          String message="";
        if (lane != null) {
            if(laneCondition == null && priority == null){
                message = "Fired Rule: StoreCarrierIntoRowOfLastCarrierIfSamePartsAndRowNotFullRule  --> carrierNumber: " + carrier.getCarrierNumber()+ "  --> " + lane.getRowName() ;
            }else{
            	String lcondition = laneCondition!=null?laneCondition.name():"";
            	String priorityName = priority != null?priority.name():"";
                message =    "Fired Rule: " + "Store "+carrier.getDie().getPartProductionVolume().name() +" Carrier Into "+lcondition+" Row of StorageAreaPriority: "+ priorityName
                    + "  --> carrierNumber: " + carrier.getCarrierNumber()+ "  --> " + lane.getRowName() ;
            }
            LOG.info(message)  ;
            if(!lane.isOutOfOrder() && (lane.getAvailability().equals(StopAvailability.AVAILABLE))){
                storageStateContext.saveToAuditLog(StorageConfig.OHCV_APP, message, StorageConfig.OHCV_APP_STOREIN);

                return lane;
            }
        }
        return processNextRule(carrier);
    }

    private StorageRow processNextRule(Carrier carrier) {
        StorageRow retVal;
        if (nextRule != null) {
            LOG.info(nextRule.getClass().getName());
            retVal = nextRule.processRule(carrier);
        } else {
             storageStateContext.saveToAuditLog(StorageConfig.OHCV_APP, "Unable to locate an applicable rule! to Store Carrier : "+carrier.getCarrierNumber(), StorageConfig.OHCV_APP_STOREIN);
            throw new NoApplicableRuleFoundException("Unable to locate an applicable rule!");
        }
        return retVal;
    }


    protected StorageState getStorageState() {
        return storageStateContext.getStorageState();
    }
    protected StorageStateStoreInWrapper getStorageStateStoreInWrapper() {
        return storageStateStoreInWrapper;
    }

    protected StorageStateContext getStorageStateContext(){
        return storageStateContext;
    }


}