package com.honda.mfg.stamp.conveyor.rules.store_out;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;

/**
 * User: Jeffrey M Lutz Date: Feb 10, 2011
 */
public class RetrieveCarrierFromRowOfZonePriorityAndRowCondition extends StoreOutRuleBase {
	private static final Logger LOG = LoggerFactory
			.getLogger(RetrieveCarrierFromRowOfZonePriorityAndRowCondition.class);
	StoragePriority.Priority priority;
	LaneCondition laneCondition;

	public RetrieveCarrierFromRowOfZonePriorityAndRowCondition(StorageStateContext storageStateContext,
			StoreOutRule nextRule, LaneCondition laneCondition, StoragePriority.Priority priority) {
		super(storageStateContext, nextRule, laneCondition, priority);
		this.priority = priority;
		this.laneCondition = laneCondition;
	}

	@Override
	public StorageRow computeLane(Die die) {
		StorageRow appropriateRow = null;
		StorageArea area = StoragePriority.getStorageAreaByPriorityForVolume(die.getPartProductionVolume(), priority);
		List<StorageRow> rows = getStorageStateStoreOutWrapper()
				.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(die, area);
		switch (laneCondition) {
		case MIXED_FRONT:
			appropriateRow = getStorageStateStoreOutWrapper().getAppropriateMixedFrontRowForDie(die.getId(), rows);
			break;
		case PARTIAL:
			appropriateRow = getStorageStateStoreOutWrapper().getAppropriatePartialRowForDie(die.getId(), rows);
			break;
		case FULL:
			appropriateRow = getStorageStateStoreOutWrapper().getAppropriateFullRowForDie(die.getId(), rows);
			break;
		case MIXED_BLOCK:
			appropriateRow = getStorageStateStoreOutWrapper().getAppropriateMixedBlockedRowForDie(die.getId(), rows);
			break;

		}
		LOG.info("Computed Appropriate Row for Die " + ((die == null) ? "null" : die.getId()) + " as "
				+ ((appropriateRow == null) ? "null" : appropriateRow.getRowName()) + " "
				+ ((laneCondition == null) ? "null" : laneCondition.toString()));
		return appropriateRow;
	}

}
