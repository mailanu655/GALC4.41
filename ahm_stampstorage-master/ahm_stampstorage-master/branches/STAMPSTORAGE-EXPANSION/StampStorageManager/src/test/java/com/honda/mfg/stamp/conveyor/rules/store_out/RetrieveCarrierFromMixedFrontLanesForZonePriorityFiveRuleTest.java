package com.honda.mfg.stamp.conveyor.rules.store_out;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StoragePriority;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.OM;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContextMock;
import com.honda.mfg.stamp.conveyor.manager.StorageStateImpl;

/**
 * User: vcc30690 Date: 3/11/11
 */
public class RetrieveCarrierFromMixedFrontLanesForZonePriorityFiveRuleTest {

	private static int index = 0;

	@Test
	public void successfullyRetrieveCarriersWithHighVolumeParts() {
		// Pre conditions
		StorageStateContext context = getContext();
		StoreOutRule storeOutRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null,
				LaneCondition.MIXED_FRONT, StoragePriority.Priority.FIVE);

		Die part = new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME);

		// test
		StorageRow StorageRow = storeOutRule.processRule(part);

		// Post condition / assertions
		assertNotNull(StorageRow);
		assertEquals(StorageArea.A_AREA, StorageRow.getStorageArea());
		Carrier carrier = StorageRow.release();
		assertEquals(OM.PART_A, carrier.getDie().getId());
		assertEquals(OM.OLD_PRODUCTION_RUN_NUMBER, carrier.getProductionRunNo());
		assertEquals(StorageRow.getCapacity().intValue(), OM.A_AREA_CAPACITY.intValue());
	}

	@Test
	public void successfullyRetrieveCarriersWithMediumVolumeParts() {
		// Pre conditions
		StorageStateContext context = getContext();
		StoreOutRule storeOutRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null,
				LaneCondition.MIXED_FRONT, StoragePriority.Priority.FIVE);
		Die part = new Die(OM.PART_B, PartProductionVolume.MEDIUM_VOLUME);

		// test
		StorageRow StorageRow = storeOutRule.processRule(part);

		// Post condition / assertions
		assertNotNull(StorageRow);
		assertEquals(StorageArea.S_AREA, StorageRow.getStorageArea());
		Carrier carrier = StorageRow.release();
		assertEquals(OM.PART_B, carrier.getDie().getId());
		assertEquals(OM.OLD_PRODUCTION_RUN_NUMBER, carrier.getProductionRunNo());
		assertEquals(StorageRow.getCapacity().intValue(), OM.S_AREA_CAPACITY.intValue());
	}

	@Test
	public void successfullyRetrieveCarriersWithLowVolumeParts() {
		// Pre conditions
		StorageStateContext context = getContext();
		StoreOutRule storeOutRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null,
				LaneCondition.MIXED_FRONT, StoragePriority.Priority.FIVE);
		Die part = new Die(OM.PART_C, PartProductionVolume.LOW_VOLUME);

		// test
		StorageRow StorageRow = storeOutRule.processRule(part);

		// Post condition / assertions
		assertNotNull(StorageRow);
		assertEquals(StorageArea.S_AREA, StorageRow.getStorageArea());
		Carrier carrier = StorageRow.release();
		assertEquals(OM.PART_C, carrier.getDie().getId());
		assertEquals(OM.OLD_PRODUCTION_RUN_NUMBER, carrier.getProductionRunNo());
		assertEquals(StorageRow.getCapacity().intValue(), OM.S_AREA_CAPACITY.intValue());
	}

	@Test(expected = NoApplicableRuleFoundException.class)
	public void AttemptingToRetrieveCarriersFromNoCapacityLanes() {
		// Pre conditions
		StorageStateContext context = getContext();
		StoreOutRule storeOutRule = new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null,
				LaneCondition.MIXED_FRONT, StoragePriority.Priority.FIVE);
		Die part = new Die(103L, PartProductionVolume.HIGH_VOLUME);

		// test
		StorageRow StorageRow = storeOutRule.processRule(part);

		// Post Conditions / assertions
		assertNull(StorageRow);
	}

	private void pause(long duration) {
		long delta = System.currentTimeMillis();
		while (System.currentTimeMillis() - delta < duration) {
		}

	}

	private StorageStateContext getContext() {
		StorageStateContext context = new StorageStateContextMock(getStorageState());
		pause(1000);
		return context;
	}

//    private RulesConfig getRulesConfig() {
//        Map<LaneVolumeStorage, Integer> capacityMap =
//                new HashMap<LaneVolumeStorage, Integer>();
//        capacityMap.put(LaneVolumeStorage.LOW_VOLUME_STORAGE, 4);
//        capacityMap.put(LaneVolumeStorage.MEDIUM_VOLUME_STORAGE, 7);
//        capacityMap.put(LaneVolumeStorage.HIGH_VOLUME_STORAGE, 12);
//
//        return new RulesConfig(capacityMap, 7);
//    }

	private StorageState getStorageState() {
		return new StorageStateImpl(getMixedFrontLanes());
	}

	List<StorageRow> getMixedFrontLanes() {

		List<StorageRow> lanes = new ArrayList<StorageRow>();
		// same part oldest production run no and sorted in ascending by carrier count

		StorageRow aAreaLane1 = OM.lane_A_AREA_Empty();
		aAreaLane1.setStop(new Stop(21L));

		StorageRow aAreaLane2 = OM.lane_A_AREA_Empty();
		aAreaLane2.setStop(new Stop(22L));
		StorageRow aAreaLane3 = OM.lane_A_AREA_Empty();
		aAreaLane3.setStop(new Stop(23L));
		StorageRow aAreaLane4 = OM.lane_A_AREA_Empty();
		aAreaLane4.setStop(new Stop(24L));
		StorageRow cHighLane1 = OM.lane_C_HIGH_Empty();
		cHighLane1.setStop(new Stop(1L));
		StorageRow cHighLane2 = OM.lane_C_HIGH_Empty();
		cHighLane2.setStop(new Stop(2L));
		StorageRow cHighLane3 = OM.lane_C_HIGH_Empty();
		cHighLane3.setStop(new Stop(3L));
		StorageRow cHighLane4 = OM.lane_C_HIGH_Empty();
		cHighLane4.setStop(new Stop(4L));
		StorageRow cLowLane1 = OM.lane_C_LOW_Empty();
		cLowLane1.setStop(new Stop(30L));
		StorageRow cLowLane2 = OM.lane_C_LOW_Empty();
		cLowLane2.setStop(new Stop(31L));
		StorageRow cLowLane3 = OM.lane_C_LOW_Empty();
		cLowLane3.setStop(new Stop(32L));
		StorageRow cLowLane4 = OM.lane_C_LOW_Empty();
		cLowLane4.setStop(new Stop(33L));

		StorageRow sAreaLane1 = OM.lane_S_AREA_Empty();
		sAreaLane1.setStop(new Stop(44L));
		StorageRow sAreaLane2 = OM.lane_S_AREA_Empty();
		sAreaLane2.setStop(new Stop(45L));

		StorageRow bAreaLane1 = OM.lane_B_AREA_Empty();
		bAreaLane1.setStop(new Stop(41L));
		StorageRow bAreaLane2 = OM.lane_B_AREA_Empty();
		bAreaLane2.setStop(new Stop(42L));
		StorageRow bAreaLane3 = OM.lane_B_AREA_Empty();
		bAreaLane3.setStop(new Stop(43L));
		StorageRow bAreaLane4 = OM.lane_B_AREA_Empty();
		bAreaLane4.setStop(new Stop(44L));

		// low volume capacity
		for (int i = 0; i < aAreaLane1.getCapacity().intValue() - 3; i++) {
			aAreaLane1.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane1.getStop(), aAreaLane1.getStop()));
		}
		for (int i = 0; i < 3; i++) {
			aAreaLane1.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane1.getStop(), aAreaLane1.getStop()));
		}

		for (int i = 0; i < aAreaLane2.getCapacity().intValue(); i++) {
			aAreaLane2.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane2.getStop(), aAreaLane2.getStop()));
		}

		for (int i = 0; i < aAreaLane3.getCapacity().intValue() - 1; i++) {
			aAreaLane3.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane3.getStop(), aAreaLane3.getStop()));
		}
		aAreaLane3.store(getLowVolumeCarrier(OM.PART_C, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP,
				aAreaLane3.getStop(), aAreaLane3.getStop()));
		for (int i = 0; i < aAreaLane4.getCapacity().intValue() - 1; i++) {
			aAreaLane4.store(getLowVolumeCarrier(OM.PART_C, OM.OTHER_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane4.getStop(), aAreaLane4.getStop()));
		}

		// medium volume capacity
		for (int i = 0; i < cHighLane1.getCapacity().intValue(); i++) {
			cHighLane1.store(getMediumVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER,
					OM.NEW_PRODUCTION_RUN_TIMESTAMP, cHighLane1.getStop(), cHighLane1.getStop()));
		}

		for (int i = 0; i < cHighLane2.getCapacity().intValue(); i++) {
			cHighLane2.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, cHighLane2.getStop(), cHighLane2.getStop()));
		}

		for (int i = 0; i < cHighLane3.getCapacity().intValue() - 1; i++) {
			cHighLane3.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, cHighLane3.getStop(), cHighLane3.getStop()));
		}
		cHighLane3.store(getMediumVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP,
				cHighLane3.getStop(), cHighLane3.getStop()));
		for (int i = 0; i < cHighLane4.getCapacity().intValue() - 1; i++) {
			cHighLane4.store(getMediumVolumeCarrier(OM.PART_B, OM.OTHER_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, cHighLane4.getStop(), cHighLane4.getStop()));
		}

		// high volume capacity
		for (int i = 0; i < cLowLane1.getCapacity().intValue(); i++) {
			cLowLane1.store(getHighVolumeCarrier(OM.PART_A, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP,
					cLowLane1.getStop(), cLowLane1.getStop()));
		}

		for (int i = 0; i < cLowLane2.getCapacity().intValue(); i++) {
			cLowLane2.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, cLowLane2.getStop(), cLowLane2.getStop()));
		}

		for (int i = 0; i < cLowLane3.getCapacity().intValue() - 1; i++) {
			cLowLane3.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, cLowLane3.getStop(), cLowLane3.getStop()));
		}
		cLowLane3.store(getMediumVolumeCarrier(OM.PART_A, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP,
				cLowLane3.getStop(), cLowLane3.getStop()));
		for (int i = 0; i < cLowLane4.getCapacity().intValue() - 1; i++) {
			cLowLane4.store(getHighVolumeCarrier(OM.PART_B, OM.OTHER_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, cLowLane4.getStop(), cLowLane4.getStop()));
		}

		// high volume capacity
		for (int i = 0; i < bAreaLane1.getCapacity().intValue(); i++) {
			bAreaLane1.store(getMediumVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER,
					OM.NEW_PRODUCTION_RUN_TIMESTAMP, bAreaLane1.getStop(), bAreaLane1.getStop()));
		}

		for (int i = 0; i < bAreaLane2.getCapacity().intValue(); i++) {
			bAreaLane2.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, bAreaLane2.getStop(), bAreaLane2.getStop()));
		}

		for (int i = 0; i < bAreaLane3.getCapacity().intValue() - 1; i++) {
			bAreaLane3.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, bAreaLane3.getStop(), bAreaLane3.getStop()));
		}
		bAreaLane3.store(getMediumVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP,
				bAreaLane3.getStop(), bAreaLane3.getStop()));
		for (int i = 0; i < bAreaLane4.getCapacity().intValue() - 1; i++) {
			bAreaLane4.store(getHighVolumeCarrier(OM.PART_A, OM.OTHER_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, bAreaLane4.getStop(), bAreaLane4.getStop()));
		}

		for (int i = 0; i < sAreaLane1.getCapacity().intValue() - 3; i++) {
			sAreaLane1.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, sAreaLane1.getStop(), sAreaLane1.getStop()));
		}
		for (int i = 0; i < 3; i++) {
			sAreaLane1.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, sAreaLane1.getStop(), sAreaLane1.getStop()));
		}

		for (int i = 0; i < sAreaLane2.getCapacity().intValue() - 4; i++) {
			sAreaLane2.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, sAreaLane2.getStop(), sAreaLane2.getStop()));
		}

		for (int i = 0; i < 4; i++) {
			sAreaLane2.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER,
					OM.OLD_PRODUCTION_RUN_TIMESTAMP, sAreaLane2.getStop(), sAreaLane2.getStop()));
		}

		lanes.add(aAreaLane1);
		lanes.add(aAreaLane2);
		lanes.add(aAreaLane3);
		lanes.add(aAreaLane4);
		lanes.add(cHighLane1);
		lanes.add(cHighLane2);
		lanes.add(cHighLane3);
		lanes.add(cHighLane4);
		lanes.add(cLowLane1);
		lanes.add(cLowLane2);
		lanes.add(cLowLane3);
		lanes.add(cLowLane4);
		lanes.add(sAreaLane1);
		lanes.add(sAreaLane2);
		lanes.add(bAreaLane1);
		lanes.add(bAreaLane2);
		lanes.add(bAreaLane3);
		lanes.add(bAreaLane4);

		return lanes;
	}

	Carrier getLowVolumeCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp,
			Stop currentlocation, Stop destination) {
		return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.LOW_VOLUME,
				currentlocation, destination);
	}

	Carrier getMediumVolumeCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp,
			Stop currentlocation, Stop destination) {
		return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.MEDIUM_VOLUME,
				currentlocation, destination);
	}

	Carrier getHighVolumeCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp,
			Stop currentlocation, Stop destination) {
		return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.HIGH_VOLUME,
				currentlocation, destination);
	}

	private Carrier getCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp,
			PartProductionVolume volume, Stop currentlocation, Stop destination) {
		Carrier carrier = new Carrier(index++, new Die(dieNumber, volume), 1, productionRuntimestamp, prodRunRunNo);
		carrier.setCurrentLocation(currentlocation);
		carrier.setDestination(destination);
		carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);
		return carrier;
	}
}
