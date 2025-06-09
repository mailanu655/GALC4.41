package com.honda.mfg.stamp.conveyor.rules.store_in;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;

/**
 * User: Jeffrey M Lutz Date: 2/16/11
 */
public class StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule extends StoreInRuleBase {
	private static final Logger LOG = LoggerFactory
			.getLogger(StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule.class);

	public StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(StorageStateContext storageStateContext,
			StoreInRule nextRule) {
		super(storageStateContext, nextRule);
	}

	public StorageRow computeLane(Carrier carrier) {

		StorageRow lane = getStorageStateContext().getRow(carrier.getDie());

		if (lane == null) {
			return null;
		}

		List<StorageRow> lanes = getStorageState().getRows();
		for (StorageRow row : lanes) {
			if (row.getStop().equals(lane.getStop())) {
				lane = row;
				break;
			}
		}
		// requested by Steve on 11/21/2013 To Include ProductionRun Number in the
		// criteria to make this rule applicable for store in
		if (lane.isPhysicalSpaceAvailable() && !lane.isBlocked()) {
			Carrier lastStoredCarrier = lane.getCarrierAtLaneIn();
			if (lastStoredCarrier != null && lastStoredCarrier.getProductionRunNo().equals(carrier.getProductionRunNo())
					&& carrier.getDie().equals(lastStoredCarrier.getDie())) {
				LOG.info("Row - " + lane.getRowName() + " Row Carrier Count : " + lane.getCurrentCarrierCount());
				return lane;
			} else {
				LOG.info(" Carrier At Lane In of Row-" + lane.getRowName()
						+ " Does not Have Same Die or Production Run");
			}
		} else {
			LOG.info(" Row -" + lane.getRowName() + " is Either Empty or Full or Blocked");
		}
		return null;
	}
}