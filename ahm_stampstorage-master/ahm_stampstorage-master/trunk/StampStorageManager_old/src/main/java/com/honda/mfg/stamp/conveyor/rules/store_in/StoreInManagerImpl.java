package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StoreInManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Jan 24, 2011
 */
public class StoreInManagerImpl implements StoreInManager {
    private static final Logger LOG = LoggerFactory.getLogger(StoreInManagerImpl.class);

    private StorageStateContext storageStateContext;
    private StoreInRule firstRule;


    public StoreInManagerImpl(StorageStateContext storageStateContext) {
        this.storageStateContext = storageStateContext;
        this.firstRule = getStoreInFirstRule1(storageStateContext);
    }
	/**
	 * @param carrier
	 * @return Storage row as a result of the rules being processed.
	 */
    public StorageRow store(Carrier carrier) {
        StorageRow lane = null;

        if (!storageStateContext.getStorageState().isStale()) {
            if (!storageStateContext.getStorageState().carrierExistsInStorageState(carrier)) {
                lane = firstRule.processRule(carrier);
            } else {
                LOG.info("Carrier with CarrierNumber " + carrier.getCarrierNumber() + " Already exists in Storage cannot Store again");
            }
        } else {
            LOG.info("StorageState is Stale");
        }
         storageStateContext.addDie(carrier.getDie().getId(),lane);
        return lane;
    }
    /**
     * Only used at stop 7-4 to store into A area.
     * @param carrier
     * @param area
     * @return Storage row as a result of the substorein rule
     */
    public StorageRow subStore(Carrier carrier, StorageArea area) {
        StorageRow lane = null;
        StoreInRule subStoreInRule = getSubStoreInFirstRule(storageStateContext, area);

        if (!storageStateContext.getStorageState().isStale()) {
            if (!storageStateContext.getStorageState().carrierExistsInStorageState(carrier)) {

                lane = subStoreInRule.processRule(carrier);
            } else {
                LOG.info("Carrier with CarrierNumber " + carrier.getCarrierNumber() + " Already exists in Storage cannot Store again");
            }
        } else {
            LOG.info("StorageState is Stale");
        }

        return lane;
    }




    private StoreInRule getSubStoreInFirstRule(StorageStateContext storageStateContext,StorageArea area) {
        return new SubStoreCarrierIntoLaneRule(storageStateContext, null, area);
    }

    private StoreInRule getStoreInFirstRule1(StorageStateContext storageStateContext) {

        StoreInRule storeCarrierIntoMixedRowForZonePriorityFiveRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, null,LaneCondition.MIXED, StoragePriority.Priority.FIVE);
        StoreInRule storeCarrierIntoMixedRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityFiveRule,LaneCondition.MIXED, StoragePriority.Priority.FOUR);
        StoreInRule storeCarrierIntoMixedRowForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityFourRule,LaneCondition.MIXED, StoragePriority.Priority.THREE);
        StoreInRule storeCarrierIntoMixedRowForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityThreeRule,LaneCondition.MIXED, StoragePriority.Priority.TWO);
        StoreInRule storeCarrierIntoMixedRowForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityTwoRule,LaneCondition.MIXED, StoragePriority.Priority.ONE);

        StoreInRule storeCarrierIntoMixedSameRowForZonePriorityFiveRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityOneRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.FIVE);
        StoreInRule storeCarrierIntoMixedSameRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityFiveRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.FOUR);
        StoreInRule storeCarrierIntoMixedSameRowForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityFourRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.THREE);
        StoreInRule storeCarrierIntoMixedSameRowForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityThreeRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.TWO);
        StoreInRule storeCarrierIntoMixedSameRowForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityTwoRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.ONE);

        StoreInRule storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityFiveRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityOneRule,LaneCondition.EMPTY, StoragePriority.Priority.FIVE);
        StoreInRule storeCarrierIntoVacantRowForZonePriorityFiveRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityFiveRule,LaneCondition.VACANT, StoragePriority.Priority.FIVE);
        StoreInRule storeCarrierIntoPartialRowWithSamePartsForZonePriorityFiveRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoVacantRowForZonePriorityFiveRule, LaneCondition.PARTIAL, StoragePriority.Priority.FIVE);

        StoreInRule storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoPartialRowWithSamePartsForZonePriorityFiveRule,LaneCondition.EMPTY, StoragePriority.Priority.FOUR);
        StoreInRule storeCarrierIntoVacantRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityFourRule,LaneCondition.VACANT, StoragePriority.Priority.FOUR);
        StoreInRule storeCarrierIntoPartialRowWithSamePartsForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoVacantRowForZonePriorityFourRule, LaneCondition.PARTIAL, StoragePriority.Priority.FOUR);

        StoreInRule storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoPartialRowWithSamePartsForZonePriorityFourRule,LaneCondition.EMPTY, StoragePriority.Priority.THREE);
        StoreInRule storeCarrierIntoVacantRowForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityThreeRule,LaneCondition.VACANT, StoragePriority.Priority.THREE);
        StoreInRule storeCarrierIntoPartialRowWithSamePartsForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoVacantRowForZonePriorityThreeRule, LaneCondition.PARTIAL, StoragePriority.Priority.THREE);

        StoreInRule storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoPartialRowWithSamePartsForZonePriorityThreeRule,LaneCondition.EMPTY, StoragePriority.Priority.TWO);
        StoreInRule storeCarrierIntoVacantRowForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityTwoRule,LaneCondition.VACANT, StoragePriority.Priority.TWO);
        StoreInRule storeCarrierIntoPartialRowWithSamePartsForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoVacantRowForZonePriorityTwoRule, LaneCondition.PARTIAL, StoragePriority.Priority.TWO);

        StoreInRule storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoPartialRowWithSamePartsForZonePriorityTwoRule, LaneCondition.EMPTY, StoragePriority.Priority.ONE);
        StoreInRule storeCarrierIntoVacantRowForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityOneRule, LaneCondition.VACANT, StoragePriority.Priority.ONE);
        StoreInRule storeCarrierIntoPartialRowWithSamePartsForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoVacantRowForZonePriorityOneRule, LaneCondition.PARTIAL, StoragePriority.Priority.ONE);

        StoreInRule storeCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule = new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(storageStateContext, storeCarrierIntoPartialRowWithSamePartsForZonePriorityOneRule);

        return storeCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule;

    }
}
