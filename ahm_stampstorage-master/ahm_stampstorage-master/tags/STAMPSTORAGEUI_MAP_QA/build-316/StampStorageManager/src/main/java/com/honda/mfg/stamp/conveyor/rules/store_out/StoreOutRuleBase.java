package com.honda.mfg.stamp.conveyor.rules.store_out;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: vcc30690
 * Date: 5/16/13
 */
public abstract class StoreOutRuleBase implements StoreOutRule {
    private static final Logger LOG = LoggerFactory.getLogger(StoreOutRuleBase.class);

    private StoreOutRule nextRule;
    private StorageStateContext storageStateContext;

    private StorageStateStoreOutWrapper storageStateStoreOutWrapper;
      private LaneCondition laneCondition;
    private StoragePriority.Priority priority;

    public StoreOutRuleBase(
             StorageStateContext storageStateContext
            , StoreOutRule nextRule, LaneCondition laneCondition, StoragePriority.Priority priority
    ) {
        this.storageStateContext = storageStateContext;
        this.nextRule = nextRule;
         storageStateStoreOutWrapper = new StorageStateStoreOutWrapper(storageStateContext);
        this.laneCondition = laneCondition ;
        this.priority = priority;
    }

    public abstract StorageRow computeLane(Die die);

    public final StorageRow processRule(Die die) {
        LOG.debug(this.getClass().getName());
        StorageRow lane = computeLane(die);
         String message="";
        if (lane != null) {

            if(laneCondition == null && priority == null){
                message = "" ;
            }else{

                message ="Fired Rule: " + "Retrieve "+die.getPartProductionVolume().name() +" Die from "+laneCondition.name()+" Row of StorageAreaPriority: "+ priority.name()
                    + " --->  " + lane.getRowName() ;
            }

            LOG.info(message);

            if(!lane.isOutOfOrder() && !lane.isBlocked()){
                 storageStateContext.saveToAuditLog(StorageConfig.OHCV_APP, message, StorageConfig.OHCV_APP_STOREOUT);
                 return lane;
            }
        }
        return processNextRule(die);
    }

    private StorageRow processNextRule(Die part) {
        StorageRow retVal;
        if (nextRule != null) {
            retVal = nextRule.processRule(part);

        } else {
            storageStateContext.saveToAuditLog(StorageConfig.OHCV_APP,"Unable to locate an applicable rule! to retrieve Die :"+ part.getDescription() , StorageConfig.OHCV_APP_STOREOUT);
            throw new NoApplicableRuleFoundException("Unable to locate an applicable rule!");
        }
        return retVal;
    }

    protected StorageState getStorageState() {
        return storageStateContext.getStorageState();
    }

    protected StorageStateStoreOutWrapper getStorageStateStoreOutWrapper() {
        return storageStateStoreOutWrapper;
    }
}