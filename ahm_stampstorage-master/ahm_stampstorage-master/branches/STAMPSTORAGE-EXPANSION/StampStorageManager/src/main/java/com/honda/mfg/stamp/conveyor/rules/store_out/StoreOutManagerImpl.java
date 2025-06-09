package com.honda.mfg.stamp.conveyor.rules.store_out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StoreOutManager;

/**
 * User: Jeffrey M Lutz Date: Jan 24, 2011
 */
public class StoreOutManagerImpl implements StoreOutManager {
	private static final Logger LOG = LoggerFactory.getLogger(StoreOutManagerImpl.class);
	private StorageStateContext storageStateContext;
	private StoreOutRule firstRule;

	public StoreOutManagerImpl(StorageStateContext storageStateContext) {
		this.storageStateContext = storageStateContext;
		this.firstRule = getStoreOutFirstRule(storageStateContext);
	}

	private StorageRow findLaneByDie(Die die) {
		StorageRow lane = null;
		LOG.info("Calculating New Lane to Retrieve Carrier ");

		lane = firstRule.processRule(die);

		return lane;
	}

	public StorageRow retrieve(Die die) {
		StorageRow lane = null;

		if (!storageStateContext.getStorageState().isStale()) {
			lane = findLaneByDie(die);
		} else {
			LOG.info("StorageState is Stale");
		}
		return lane;
	}

	public StoreOutRule getStoreOutFirstRule(StorageStateContext storageStateContext) {
		// RulesConfig rulesConfig = new RulesConfig(null,
		// StorageConfig.A_AREA_OVER_FILL_THRESHOLD_PERCENT);

		StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, null, LaneCondition.MIXED_BLOCK, StoragePriority.Priority.ONE);
		StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedBlockRowsForZonePriorityOneRule, LaneCondition.MIXED_BLOCK,
				StoragePriority.Priority.TWO);
		StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedBlockRowsForZonePriorityTwoRule, LaneCondition.MIXED_BLOCK,
				StoragePriority.Priority.THREE);
		StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedBlockRowsForZonePriorityThreeRule,
				LaneCondition.MIXED_BLOCK, StoragePriority.Priority.FOUR);
		StoreOutRule retrieveCarrierFromMixedBlockRowsForZonePriorityFiveRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedBlockRowsForZonePriorityFourRule,
				LaneCondition.MIXED_BLOCK, StoragePriority.Priority.FIVE);

		StoreOutRule retrieveCarrierFromFullRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedBlockRowsForZonePriorityFiveRule, LaneCondition.FULL,
				StoragePriority.Priority.ONE);
		StoreOutRule retrieveCarrierFromFullRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromFullRowsForZonePriorityOneRule, LaneCondition.FULL,
				StoragePriority.Priority.TWO);
		StoreOutRule retrieveCarrierFromFullRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromFullRowsForZonePriorityTwoRule, LaneCondition.FULL,
				StoragePriority.Priority.THREE);
		StoreOutRule retrieveCarrierFromFullRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromFullRowsForZonePriorityThreeRule, LaneCondition.FULL,
				StoragePriority.Priority.FOUR);
		StoreOutRule retrieveCarrierFromFullRowsForZonePriorityFiveRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromFullRowsForZonePriorityFourRule, LaneCondition.FULL,
				StoragePriority.Priority.FIVE);

		StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromFullRowsForZonePriorityFiveRule, LaneCondition.PARTIAL,
				StoragePriority.Priority.ONE);
		StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityOneRule, LaneCondition.PARTIAL,
				StoragePriority.Priority.TWO);
		StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityTwoRule, LaneCondition.PARTIAL,
				StoragePriority.Priority.THREE);
		StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityThreeRule, LaneCondition.PARTIAL,
				StoragePriority.Priority.FOUR);
		StoreOutRule retrieveCarrierFromPartialRowsForZonePriorityFiveRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityFourRule, LaneCondition.PARTIAL,
				StoragePriority.Priority.FIVE);

		StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityOneRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromPartialRowsForZonePriorityFiveRule, LaneCondition.MIXED_FRONT,
				StoragePriority.Priority.ONE);
		StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityTwoRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityOneRule, LaneCondition.MIXED_FRONT,
				StoragePriority.Priority.TWO);
		StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityThreeRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityTwoRule, LaneCondition.MIXED_FRONT,
				StoragePriority.Priority.THREE);
		StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityFourRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityThreeRule,
				LaneCondition.MIXED_FRONT, StoragePriority.Priority.FOUR);
		StoreOutRule retrieveCarrierFromMixedFrontRowsForZonePriorityFiveRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(
				storageStateContext, retrieveCarrierFromMixedFrontRowsForZonePriorityFourRule,
				LaneCondition.MIXED_FRONT, StoragePriority.Priority.FIVE);

		return retrieveCarrierFromMixedFrontRowsForZonePriorityFiveRule;
	}

}