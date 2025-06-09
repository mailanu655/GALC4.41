package com.honda.mfg.stamp.conveyor.rules.store_in;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.BackOrder;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageImpl;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StorageStateImpl;
import com.honda.mfg.stamp.conveyor.manager.StoreInManager;

/**
 * User: Jeffrey M Lutz Date: 2/14/11
 */
public class StoreInManagerImplTest {

	List<StorageRow> storageLanes;
	StoreInManager storeInManager;
	Die leftDie, rightDie, someDie;
	StorageState storageState;
	Storage storage;
	int testMode = 1;

	public void initStorage() {
		someDie = new Die();
		someDie.setId(100l);
		someDie.setDescription("left_die_101");
		someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		leftDie = new Die();
		leftDie.setId(101l);
		leftDie.setDescription("left_die_101");
		leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		rightDie = new Die();
		rightDie.setId(102l);
		rightDie.setDescription("right_die_101");
		rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Stop stop1 = new Stop(1232l);
		stop1.setStopType(StopType.NO_ACTION);

		Calendar c = Calendar.getInstance();
		Timestamp today = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -2);
		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		Carrier carrier2 = new Carrier(1, today, new Integer(103), stop1, null, CarrierStatus.SHIPPABLE,
				new Integer(200), someDie);

		StorageRow StorageRow = new StorageRow(0, "StorageRow 0", 12, testMode);
		StorageRow.setStop(new Stop(1201L));
		StorageRow.store(carrier2);
		StorageRow.setStorageArea(StorageArea.C_HIGH);

		StorageRow lane1 = new StorageRow(1, "StorageRow 1", 12, testMode);
		lane1.setStop(new Stop(1202L));
		lane1.setStorageArea(StorageArea.C_HIGH);

		StorageRow lane2 = new StorageRow(2, "StorageRow 2", 12, testMode);
		lane2.setStop(new Stop(1203L));
		lane2.setStorageArea(StorageArea.C_HIGH);

		StorageRow lane3 = new StorageRow(3, "StorageRow 3", 21, testMode);
		lane3.setStop(new Stop(1221L));
		lane3.setStorageArea(StorageArea.A_AREA);

		storageLanes = new ArrayList<StorageRow>();
		storageLanes.add(StorageRow);
		storageLanes.add(lane1);
		storageLanes.add(lane2);
		storageLanes.add(lane3);

		storageState = new StorageStateImpl(storageLanes);

		List<BackOrder> backOrder = new ArrayList<BackOrder>();
		backOrder.add(new BackOrder(1L, rightDie.getId()));
		storageState.setBackOrder(backOrder);

		StorageStateContext context = mock(StorageStateContext.class);
		when(context.getStorageState()).thenReturn(storageState);
		storeInManager = new StoreInManagerImpl(context);
		storage = new StorageImpl(storeInManager, null, null, context);
	}

	@Test
	public void cannotStoreCarrierTwiceTest() {
		initStorage();

		assertEquals(1, storageLanes.get(0).getCurrentCarrierCount());

		Stop storeInStop = new Stop(513l);
		storeInStop.setStopType(StopType.STORE_IN_ALL_LANES);
		Carrier carrier = new Carrier(15, someDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier.setCurrentLocation(storeInStop);
		carrier.setCarrierStatus(CarrierStatus.ON_HOLD);

		storage.store(carrier);
		StorageRow lane2 = storeInManager.store(carrier);

		assertNull(lane2);
	}

	@Test
	public void storeCarrierInPreviousLane() {
		initStorage();
		Stop storeInStop = new Stop(513l);
		storeInStop.setStopType(StopType.STORE_IN_ALL_LANES);

		assertEquals(0, storageLanes.get(1).getCurrentCarrierCount());

		Carrier carrier = new Carrier(15, leftDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier.setCurrentLocation(storeInStop);
		carrier.setCarrierStatus(CarrierStatus.INSPECTION_REQUIRED);
		storage.store(carrier);

		Carrier carrier1 = new Carrier(16, leftDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier1.setCurrentLocation(storeInStop);
		carrier1.setCarrierStatus(CarrierStatus.SHIPPABLE);
		storage.store(carrier1);

		assertEquals(2, storageLanes.get(2).getCurrentCarrierCount());
	}

	@Test
	public void storeCarrierForBackOrderParts() {
		initStorage();

		Stop storeInStop = new Stop(513l);
		storeInStop.setStopType(StopType.STORE_IN_ALL_LANES);

		assertEquals(0, storageLanes.get(2).getCurrentCarrierCount());

		Carrier carrier = new Carrier(15, leftDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier.setCurrentLocation(storeInStop);
		carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);
		storage.store(carrier);

		Carrier carrier1 = new Carrier(16, rightDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier1.setCurrentLocation(storeInStop);
		carrier1.setCarrierStatus(CarrierStatus.SHIPPABLE);
		storage.store(carrier1);

		assertEquals(1, storageLanes.get(2).getCurrentCarrierCount());
	}

	@Test
	public void subStoreCarriers() {
		initStorage();
		Stop storeInStop = new Stop(704l);
		storeInStop.setStopType(StopType.RECIRC_TO_ALL_ROWS);
		storeInStop.setStopArea(StopArea.STORE_IN_ROUTE);
		assertEquals(1, storageLanes.get(0).getCurrentCarrierCount());

		Carrier carrier = new Carrier(15, someDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier.setCurrentLocation(storeInStop);
		carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);

		storage.store(carrier);
		assertEquals(0, storageLanes.get(3).getCurrentCarrierCount());
		storage.store(carrier);
		assertEquals(0, storageLanes.get(3).getCurrentCarrierCount());
	}

	@Test
	public void storeCarrierWhenStorageStateStale() {
		StorageState storageState = mock(StorageState.class);
		when(storageState.isStale()).thenReturn(true);
		StorageStateContext context = mock(StorageStateContext.class);
		when(context.getStorageState()).thenReturn(storageState);
		storeInManager = new StoreInManagerImpl(context);

		Stop subStoreInStop = new Stop(704l);
		subStoreInStop.setStopType(StopType.RECIRC_TO_ALL_ROWS);

		Stop storeInStop = new Stop(513l);
		storeInStop.setStopType(StopType.STORE_IN_ALL_LANES);

		Carrier carrier = new Carrier(15, someDie, 1, new Timestamp(System.currentTimeMillis()), 105);
		carrier.setCurrentLocation(storeInStop);
		carrier.setDie(new Die(101L, PartProductionVolume.MEDIUM_VOLUME));

		StorageRow StorageRow = storeInManager.store(carrier);
		assertNull(StorageRow);

		carrier.setCurrentLocation(subStoreInStop);
		StorageRow lane2 = storeInManager.subStore(carrier, StorageArea.A_AREA);
		assertNull(lane2);
	}
}
