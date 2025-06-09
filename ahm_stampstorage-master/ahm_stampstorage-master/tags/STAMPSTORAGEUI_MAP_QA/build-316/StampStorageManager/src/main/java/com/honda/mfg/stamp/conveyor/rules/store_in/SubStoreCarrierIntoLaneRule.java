package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;

import java.util.List;

/**
 * User: Adam S. Kendell
 * Date: Mar 8, 2011
 */
public class SubStoreCarrierIntoLaneRule extends StoreInRuleBase {
    private StorageStateStoreInWrapper storageStateStoreInWrapper;
    private StorageArea area;

    public SubStoreCarrierIntoLaneRule(StorageStateContext storageStateContext,StoreInRule nextRule, StorageArea area) {
        super(storageStateContext,nextRule);
        this.area= area;
        this.storageStateStoreInWrapper = new StorageStateStoreInWrapper(storageStateContext);
    }

    @Override
    public StorageRow  computeLane(Carrier carrier) {

        List<StorageRow> lanes = storageStateStoreInWrapper.getStorageRowsByArea(area);


        return lanes != null ? getAppropriateLane(lanes, carrier) : null;
    }

    private StorageRow getAppropriateLane(List<StorageRow> lanes, Carrier carrier) {
        StorageRow appropriateLane = null;

        LaneCondition[] laneConditions = {LaneCondition.PARTIAL, LaneCondition.VACANT, LaneCondition.EMPTY, LaneCondition.MIXED_BACK, LaneCondition.MIXED};
        for (LaneCondition condition : laneConditions) {
            switch (condition) {
                case PARTIAL:
                    appropriateLane = storageStateStoreInWrapper.getAppropriatePartialRowForDie(carrier.getDie().getId(), lanes);
                    break;
                case VACANT:
                    appropriateLane = storageStateStoreInWrapper.getAppropriateVacantRow(lanes);
                    break;
                case EMPTY:
                    appropriateLane = storageStateStoreInWrapper.getAppropriatePartialRowWithEmptyCarriers(lanes);
                    break;
                case MIXED_BACK:
                    appropriateLane = storageStateStoreInWrapper.getAppropriateMixedBackRowWithDie(carrier.getDie().getId(), lanes);
                    break;
                case MIXED:
                    appropriateLane = storageStateStoreInWrapper.getAppropriateMixedRows(lanes);
                    break;
            }
            if (appropriateLane != null) {
                break;
            }
        }
        return appropriateLane;
    }
}
