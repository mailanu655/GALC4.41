package com.honda.mfg.stamp.conveyor.rules.empty;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.EmptyManager;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreCarrierIntoRowOfZonePriorityAndRowCondition;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreInRule;
import com.honda.mfg.stamp.conveyor.rules.store_out.RetrieveCarrierFromRowOfZonePriorityAndRowCondition;
import com.honda.mfg.stamp.conveyor.rules.store_out.StoreOutRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vcc30690
 * Date: 5/18/11
 */
public class EmptyManagerImpl implements EmptyManager {
    private static final Logger LOG = LoggerFactory.getLogger(EmptyManagerImpl.class);
    private StoreOutRule emptyStoreOutFirstRule;
    private StoreInRule emptyStoreInFirstRule;
    private StorageStateContext storageStateContext;
    private StoreOutRule highVolumeEmptyStoreOutFirstRule;
    private StoreOutRule mediumVolumeEmptyStoreOutFirstRule;
    private StoreOutRule lowVolumeEmptyStoreOutFirstRule;
    private StoreOutRule emptyStoreOutFirstRuleForOldWeldLine;
    private StoreOutRule emptyStoreOutFirstRuleForBArea;

    public EmptyManagerImpl(StorageStateContext storageStateContext) {
        this.storageStateContext = storageStateContext;
        this.emptyStoreInFirstRule = getEmptyStoreInFirstRule(storageStateContext);
        this.highVolumeEmptyStoreOutFirstRule = getEmptyStoreOutFirstRuleByStorageArea(storageStateContext, StorageArea.C_LOW);
        this.mediumVolumeEmptyStoreOutFirstRule = getEmptyStoreOutFirstRuleByStorageArea(storageStateContext, StorageArea.C_HIGH);
        this.lowVolumeEmptyStoreOutFirstRule = getEmptyStoreOutFirstRuleByStorageArea(storageStateContext, StorageArea.A_AREA);
        this.emptyStoreOutFirstRule = getEmptyStoreOutFirstRule(storageStateContext);
        this.emptyStoreOutFirstRuleForOldWeldLine  = getEmptyStoreOutFirstRuleForOldWeldLine(storageStateContext);
        this.emptyStoreOutFirstRuleForBArea  = getEmptyStoreOutFirstRuleForBAreaEmptyStorage(storageStateContext);
    }

    @Override
    public StorageRow retrieveEmptyCarrier() {
        StorageRow lane = null;
        Die emptyDie = storageStateContext.getEmptyDie();

        LOG.debug("Empty Manager retrieve");
        if (!storageStateContext.getStorageState().isStale()) {
            lane = emptyStoreOutFirstRule.processRule(emptyDie);
        } else {
            LOG.info("StorageState is Stale");
        }
        return lane;
    }

    public StorageRow retrieveEmptyCarrier(StorageArea area) {
        StorageRow lane = null;
        LOG.info("Empty Manager retrieve-- "+area);
        try {
            Die emptyDie = storageStateContext.getEmptyDie();
            if (!storageStateContext.getStorageState().isStale()) {
                if (area != null) {
                    if (area.equals(StorageArea.C_LOW)) {
                        lane = highVolumeEmptyStoreOutFirstRule.processRule(emptyDie);
                    } else if (area.equals(StorageArea.C_HIGH)) {
                        lane = mediumVolumeEmptyStoreOutFirstRule.processRule(emptyDie);
                    } else if (area.equals(StorageArea.A_AREA)) {
                        lane = lowVolumeEmptyStoreOutFirstRule.processRule(emptyDie);
                    }else if(area.equals(StorageArea.B_AREA))  {
                        lane = emptyStoreOutFirstRuleForBArea.processRule(emptyDie);
                    }

                }
            } else {
                LOG.info("StorageState is Stale");
            }
        } catch (NoApplicableRuleFoundException e) {
            LOG.info("No Row With Empty Carriers Found in " + area);
        }
        return lane;
    }

    @Override
    public StorageRow retrieveEmptyCarrierForOldWeldLineEmptyStorage() {
        StorageRow lane = null;
        Die emptyDie = storageStateContext.getEmptyDie();

        LOG.debug("Empty Manager retrieve");
        if (!storageStateContext.getStorageState().isStale()) {
            lane = emptyStoreOutFirstRuleForOldWeldLine.processRule(emptyDie);
        } else {
            LOG.info("StorageState is Stale");
        }
        return lane;
    }

    @Override
    public StorageRow retrieveEmptyCarrierForBAreaEmptyStorage() {
         StorageRow lane = null;
        Die emptyDie = storageStateContext.getEmptyDie();

        LOG.debug("Empty Manager retrieve");
        if (!storageStateContext.getStorageState().isStale()) {
            lane = emptyStoreOutFirstRuleForBArea.processRule(emptyDie);
        } else {
            LOG.info("StorageState is Stale");
        }
        return lane;
    }

    @Override
    public StorageRow storeEmptyCarrier(Carrier carrier) {
        StorageRow lane = null;
        if (!storageStateContext.getStorageState().isStale()) {
            if (!storageStateContext.getStorageState().carrierExistsInStorageState(carrier)) {
                lane = emptyStoreInFirstRule.processRule(carrier);
            } else {
                LOG.info("Carrier with CarrierNumber " + carrier.getCarrierNumber() + " Already exists in Storage cannot Store again");
            }
        } else {
            LOG.info("StorageState is Stale");
        }
         storageStateContext.addDie(carrier.getDie().getId(),lane);
        return lane;
    }

    public StoreInRule getEmptyStoreInFirstRule(StorageStateContext storageStateContext) {

               StoreInRule storeCarrierIntoMixedRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, null,LaneCondition.MIXED, StoragePriority.Priority.FOUR);
               StoreInRule storeCarrierIntoMixedRowForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityFourRule,LaneCondition.MIXED, StoragePriority.Priority.THREE);
               StoreInRule storeCarrierIntoMixedRowForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityThreeRule,LaneCondition.MIXED, StoragePriority.Priority.TWO);
               StoreInRule storeCarrierIntoMixedRowForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityTwoRule,LaneCondition.MIXED, StoragePriority.Priority.ONE);


               StoreInRule storeCarrierIntoMixedSameRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedRowForZonePriorityOneRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.FOUR);
               StoreInRule storeCarrierIntoMixedSameRowForZonePriorityThreeRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityFourRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.THREE);
               StoreInRule storeCarrierIntoMixedSameRowForZonePriorityTwoRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityThreeRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.TWO);
               StoreInRule storeCarrierIntoMixedSameRowForZonePriorityOneRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityTwoRule,LaneCondition.MIXED_BACK, StoragePriority.Priority.ONE);

               StoreInRule storeCarrierIntoEmptyCarrierOnlyRowForZonePriorityFourRule = new StoreCarrierIntoRowOfZonePriorityAndRowCondition(storageStateContext, storeCarrierIntoMixedSameRowForZonePriorityOneRule,LaneCondition.EMPTY, StoragePriority.Priority.FOUR);
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

               StoreInRule storeCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule = new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(storageStateContext,storeCarrierIntoPartialRowWithSamePartsForZonePriorityOneRule);

               return storeCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule;

    }

    public StoreOutRule getEmptyStoreOutFirstRule(StorageStateContext storageStateContext) {

       // StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, rulesConfig, null, LaneCondition.MIXED_FRONT, StoragePriority.Priority.ONE);
       // StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, rulesConfig, retrieveCarrierFromMixedBlockRowsForZonePriorityOneRule, LaneCondition.MIXED_BLOCK, StoragePriority.Priority.TWO);
       // StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, rulesConfig, retrieveCarrierFromMixedBlockRowsForZonePriorityTwoRule, LaneCondition.MIXED_BLOCK, StoragePriority.Priority.THREE);
       // StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, rulesConfig, retrieveCarrierFromMixedBlockRowsForZonePriorityThreeRule, LaneCondition.MIXED_BLOCK, StoragePriority.Priority.FOUR);

        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityOneRule, LaneCondition.FULL, StoragePriority.Priority.TWO);
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityTwoRule, LaneCondition.FULL, StoragePriority.Priority.THREE);
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityThreeRule, LaneCondition.FULL, StoragePriority.Priority.FOUR);

        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityFourRule, LaneCondition.PARTIAL, StoragePriority.Priority.ONE);
        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityOneRule, LaneCondition.PARTIAL, StoragePriority.Priority.TWO);
        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityTwoRule, LaneCondition.PARTIAL, StoragePriority.Priority.THREE);
        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityThreeRule, LaneCondition.PARTIAL, StoragePriority.Priority.FOUR);

        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityFourRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.ONE);
        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityOneRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.TWO);
        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityTwoRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.THREE);
        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityThreeRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.FOUR);


        return retrieveCarrierFromMixedFrontRowsForZonePriorityFourRule;
    }
    public StoreOutRule getEmptyStoreOutFirstRuleByStorageArea(StorageStateContext storageStateContext, StorageArea area){
        StoragePriority.Priority p = StoragePriority.getStoragePriorityByArea(PartProductionVolume.EMPTY,area);
        LOG.info(" priority: "+p.name());
        StoreOutRule retrieveEmptyCarrierFromFullLanesRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, null, LaneCondition.FULL, p);
        StoreOutRule retrieveEmptyCarrierFromPartialLanesRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveEmptyCarrierFromFullLanesRule, LaneCondition.PARTIAL, p);
        StoreOutRule retrieveEmptyCarrierFromMixedFrontLanesRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveEmptyCarrierFromPartialLanesRule, LaneCondition.MIXED_FRONT, p);

        return retrieveEmptyCarrierFromMixedFrontLanesRule;
    }


    public  StoreOutRule getEmptyStoreOutFirstRuleForOldWeldLine(StorageStateContext storageStateContext){
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityOneRule, LaneCondition.FULL, StoragePriority.Priority.TWO);
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityTwoRule, LaneCondition.FULL, StoragePriority.Priority.THREE);
        StoreOutRule retrieveCarrierFromFullRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityThreeRule, LaneCondition.FULL, StoragePriority.Priority.FOUR);

        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromFullRowsForZonePriorityFourRule, LaneCondition.PARTIAL, StoragePriority.Priority.ONE);
        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityOneRule, LaneCondition.PARTIAL, StoragePriority.Priority.TWO);
        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityTwoRule, LaneCondition.PARTIAL, StoragePriority.Priority.THREE);
        StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityThreeRule, LaneCondition.PARTIAL, StoragePriority.Priority.FOUR);

         StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityFourRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.ONE);
        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityOneRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.TWO);
        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityTwoRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.THREE);
        StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityThreeRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.FOUR);


        return retrieveCarrierFromMixedFrontRowsForZonePriorityFourRule;

    }

    public StoreOutRule getEmptyStoreOutFirstRuleForBAreaEmptyStorage(StorageStateContext storageStateContext){
        StoreOutRule retrieveEmptyCarrierFromFullLanesRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, null, LaneCondition.FULL, StoragePriority.Priority.THREE);
        StoreOutRule retrieveEmptyCarrierFromPartialLanesRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveEmptyCarrierFromFullLanesRule, LaneCondition.PARTIAL, StoragePriority.Priority.THREE);
        StoreOutRule retrieveEmptyCarrierFromMixedFrontLanesRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(storageStateContext, retrieveEmptyCarrierFromPartialLanesRule, LaneCondition.MIXED_FRONT, StoragePriority.Priority.THREE);

        return retrieveEmptyCarrierFromMixedFrontLanesRule;
    }

}
