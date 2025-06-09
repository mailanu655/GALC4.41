package com.honda.mfg.stamp.conveyor.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * User: Jeffrey M Lutz Date: 3/4/11
 */
public class OM {
	private static int index = 0;

	private static final Timestamp NOW = new Timestamp(System.currentTimeMillis());

	public static final Integer A_AREA_CAPACITY = 10;
	public static final Integer C_HIGH_CAPACITY = 20;
	public static final Integer C_LOW_CAPACITY = 30;
	public static final Integer B_AREA_CAPACITY = 31;
	public static final Integer S_AREA_CAPACITY = 87;
	public static final Integer ROW_35_HIGH_CAPACITY = 28;
	public static final Integer LOW_VOLUME = 4;
	public static final Integer MEDIUM_VOLUME = 500;
	public static final Integer HIGH_VOLUME = 1000;

	public static final Long PART_A = 1011L;
	public static final Long PART_B = 1012L;
	public static final Long PART_C = 1013L;
	public static final Long EMPTY = 999L;

	public static final Timestamp NEW_PRODUCTION_RUN_TIMESTAMP = NOW;
	public static final Timestamp OLD_PRODUCTION_RUN_TIMESTAMP = new Timestamp(NOW.getTime() - (1000 * 60 * 60 * 24));
	public static final Integer PRODUCTION_RUN_NUMBER = 1;
	public static final Integer OLD_PRODUCTION_RUN_NUMBER = 2;
	public static final Integer OTHER_PRODUCTION_RUN_NUMBER = 20;
	public static final int QUANTITY = 1;

	public static StorageStateContext storageState(StorageRow laneImplOne, StorageRow laneImplTwo) {
		return new StorageStateContextMock(storageStateMe(laneImplOne, laneImplTwo));
	}

	public static StorageStateContext storageState(StorageRow laneImplOne, StorageRow laneImplTwo,
			Die backOrderedPart) {
		return new StorageStateContextMock(storageStateMe(laneImplOne, laneImplTwo, backOrderedPart));
	}

	public static StorageState storageStateMe(StorageRow laneImplOne, StorageRow laneImplTwo) {
		return storageStateMe(laneImplOne, laneImplTwo, null);
	}

	public static StorageState storageStateMe(StorageRow laneImplOne, StorageRow laneImplTwo, Die backOrderedPart) {
		List<StorageRow> lanes = new ArrayList<StorageRow>();
		lanes.add(laneImplOne);
		lanes.add(laneImplTwo);
		return storageStateMe(lanes, backOrderedPart, new OrderMgr());
	}

	public static StorageStateContext storageState(List<StorageRow> lanes, Die backOrderedPart) {
		return new StorageStateContextMock(storageStateMe(lanes, backOrderedPart, new OrderMgr()));
	}

	public static StorageState storageStateMe(List<StorageRow> lanes, Die backOrderedPart, OrderMgr orderMgr) {
		List<BackOrder> backOrder = new ArrayList<BackOrder>();
		if (orderMgr != null && backOrderedPart != null) {
			backOrder.add(new BackOrder(orderMgr.getId(), backOrderedPart.getId()));
		}
		StorageState storageState = new StorageStateImpl(lanes);
		storageState.setBackOrder(backOrder);
		return storageState;
	}

	public static StorageRow lane_HighCapacity_Full(PartProductionVolume volume, Long dieNumber) {
		StorageRow lane = lane_C_LOW_Empty();
		int i = 0;
		while (!lane.isFull()) {
			Carrier carrier = carrier(dieNumber, volume);
			carrier.setCarrierNumber(i);
			lane.store(carrier);
			i++;
		}

		return lane;
	}

	public static StorageRow lane_C_LOW_Empty() {
		StorageRow row = new StorageRow(i(), "C_LOW row", C_LOW_CAPACITY, 1);
		row.setStorageArea(StorageArea.C_LOW);
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setStop(new Stop(i()));
		return row;
	}

	public static StorageRow lane_C_HIGH_Empty() {
		StorageRow row = new StorageRow(i(), "C_HIGH row", C_HIGH_CAPACITY, 1);
		row.setStorageArea(StorageArea.C_HIGH);
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setStop(new Stop(i()));
		return row;
	}

	public static StorageRow lane_A_AREA_Empty() {
		StorageRow row = new StorageRow(i(), "A_AREA row", A_AREA_CAPACITY, 1);
		row.setStorageArea(StorageArea.A_AREA);
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setStop(new Stop(i()));
		return row;
	}

	public static StorageRow lane_S_AREA_Empty() {
		StorageRow row = new StorageRow(i(), "S_AREA row", S_AREA_CAPACITY, 1);
		row.setStorageArea(StorageArea.S_AREA);
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setStop(new Stop(i()));
		return row;
	}

	public static StorageRow lane_B_AREA_Empty() {
		StorageRow row = new StorageRow(i(), "B_AREA row", B_AREA_CAPACITY, 1);
		row.setStorageArea(StorageArea.B_AREA);
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setStop(new Stop(i()));
		return row;
	}

	public static StorageRow lane_B_AREA_Empty(String row_name) {
		StorageRow row = new StorageRow(i(), row_name, B_AREA_CAPACITY, 1);
		row.setStorageArea(StorageArea.B_AREA);
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setStop(new Stop(i()));
		return row;
	}

	public static Carrier carrier_HighVolume_PartA() {
		return carrier_HighVolume(PART_A);
	}

	public static Carrier carrier_MediumVolume_PartA() {
		return carrier_MediumVolume(PART_A);
	}

	public static Carrier carrier_LowVolume_PartA() {
		return carrier_LowVolume(PART_A);
	}

	public static Carrier carrier_HighVolume_PartB() {
		return carrier_HighVolume(PART_B);
	}

	public static Carrier carrier_MediumVolume_PartB() {
		return carrier_MediumVolume(PART_B);
	}

	public static Carrier carrier_LowVolume_PartB() {
		return carrier_LowVolume(PART_B);
	}

	public static Carrier carrier_HighVolume(Long partType) {
		return carrier(partType, PartProductionVolume.HIGH_VOLUME);
	}

	public static Carrier carrier_MediumVolume(Long partType) {
		return carrier(partType, PartProductionVolume.MEDIUM_VOLUME);
	}

	public static Carrier carrier_LowVolume(Long partType) {
		return carrier(partType, PartProductionVolume.LOW_VOLUME);
	}

	public static Carrier carrier_Empty() {
		return carrier(EMPTY, PartProductionVolume.EMPTY);
	}

	public static Carrier carrier(Long dieNumber, PartProductionVolume volume) {
		return new Carrier(index++, new Die(dieNumber, volume), QUANTITY, NEW_PRODUCTION_RUN_TIMESTAMP,
				PRODUCTION_RUN_NUMBER);
	}

	private static int i() {
		return index++;
	}
}
