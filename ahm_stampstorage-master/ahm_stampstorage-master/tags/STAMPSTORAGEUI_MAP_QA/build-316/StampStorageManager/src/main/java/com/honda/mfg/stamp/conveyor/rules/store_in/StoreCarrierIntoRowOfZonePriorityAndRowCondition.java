package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is used 
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 5/14/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoreCarrierIntoRowOfZonePriorityAndRowCondition extends StoreInRuleBase {
        private static final Logger LOG = LoggerFactory.getLogger(StoreCarrierIntoRowOfZonePriorityAndRowCondition.class);
    StoragePriority.Priority priority;
    LaneCondition laneCondition ;

    public StoreCarrierIntoRowOfZonePriorityAndRowCondition(StorageStateContext storageStateContext, StoreInRule nextRule, LaneCondition laneCondition, StoragePriority.Priority priority) {
        super(storageStateContext, nextRule,laneCondition,priority);
        this.priority=priority;
        this.laneCondition = laneCondition;
    }
/**
 * Chooses the appropriate Storage area and Row based on lane condition 
 */
    @Override
    public StorageRow computeLane(Carrier carrier) {
       StorageRow appropriateRow = null;
       StorageArea area = StoragePriority.getStorageAreaByPriorityForVolume(carrier.getPartProductionVolume(), priority);

       switch(laneCondition){
             	case PARTIAL:
                    appropriateRow = getStorageStateStoreInWrapper().getAppropriatePartialRowForDieInStorageArea(carrier.getDie().getId(), area)  ;
                    break;
                case VACANT:
                    appropriateRow = getStorageStateStoreInWrapper().getAppropriateVacantRowInStorageArea(area);
                    break;
                case EMPTY:
                    appropriateRow = getStorageStateStoreInWrapper().getAppropriatePartialRowWithEmptyCarriersInStorageArea(area);
                    break;
                case MIXED_BACK:
                    appropriateRow = getStorageStateStoreInWrapper().getAppropriateMixedBackRowWithDieInStorageArea(carrier.getDie().getId(), area);
                    break;
                case MIXED:
                    appropriateRow = getStorageStateStoreInWrapper().getAppropriateMixedRowInStorageArea(area);
                    break;
                  //MG Request from customer Exhaust Storein Rules - does not mix without empty
                case PARTIAL_DIFFERENT_DIE:
                	appropriateRow = getStorageStateStoreInWrapper().getAppropriatePartialDifferentDieRowInStorageArea(carrier.getDie().getId(), area);
                	break;
       }
       if(appropriateRow != null && appropriateRow.isPhysicalSpaceAvailable())
       return  appropriateRow;
       else return null;
    }
}
