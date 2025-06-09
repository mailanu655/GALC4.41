package com.honda.mfg.stamp.conveyor.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.rules.empty.EmptyManagerImpl;

/**
 * User: Jeffrey M Lutz Date: 2/14/11
 */
public class StorageImplTest {
	private StoreInManager storeInManager;
	private StoreOutManager storeOutManager;
	private EmptyManagerImpl emptyManager;
	private StorageStateContext storageStateContext;
	private StorageRow lane1, lane2, lane3;

	@Before
	public void before() {
		storeInManager = mock(StoreInManager.class);
		storeOutManager = mock(StoreOutManager.class);
		emptyManager = mock(EmptyManagerImpl.class);
		storageStateContext = mock(StorageStateContext.class);
	}

	@Test
	public void rejectCarrierWithInvalidDie() {
		// Pre-conditions
		Carrier carrierWithInvalidDie = getCarrierWithInvalidDie();

		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test
		storage.store(carrierWithInvalidDie);

		// Post-conditions
		verifyZeroInteractions(storeInManager);
	}

	@Test
	public void successfullyStoreInEmptyCarrier() {
		// Pre-conditions
		Carrier expectedEmptyCarrier = getEmptyCarrier();

		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test
		storage.store(expectedEmptyCarrier);

		// Post-conditions
		verifyZeroInteractions(storeInManager);
		verifyZeroInteractions(storeOutManager);
		verify(emptyManager, times(1)).storeEmptyCarrier(Matchers.<Carrier>any());
	}

	@Test
	public void successfullyStoreInCarrierWithParts() {
		// Pre-conditions
		Carrier carrierWithParts = getCarrierWithParts();

		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test
		storage.store(carrierWithParts);

		// Post-conditions
		verifyZeroInteractions(emptyManager);
		verifyZeroInteractions(storeOutManager);
		verify(storeInManager, times(1)).store(Matchers.<Carrier>any());
	}

	@Test
	public void successfullySubStoreInCarrierWithParts() {
		// Pre-conditions
		Carrier carrierWithParts = getCarrierWithPartsToSubStore();

		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test
		storage.store(carrierWithParts);

		// Post-conditions
		verifyZeroInteractions(emptyManager);
		verifyZeroInteractions(storeOutManager);
		verify(storeInManager, times(1)).subStore(Matchers.<Carrier>any(), Matchers.<StorageArea>any());
	}

	@Test
	public void successfullyStoreOutCarrierWithParts() {
		// Pre-conditions
		Carrier carrierWithParts = getCarrierWithParts();
		StorageRow lane = new StorageRow(1, "row", 1, 1);
		lane.setStop(new Stop(1l));
		lane.store(carrierWithParts);

		List<StorageRow> lanes = new ArrayList<StorageRow>();
		lanes.add(lane);
		StorageState storageState = new StorageStateImpl(lanes);

		when(storeOutManager.retrieve(Matchers.<Die>any())).thenReturn(lane);
		when(storageStateContext.getStorageState()).thenReturn(storageState);
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test
		StorageRow tempLane = storeOutManager.retrieve(carrierWithParts.getDie());
		assertNotNull(tempLane);
		Carrier deliveredCarrier = storage.getStorageState().releaseCarrierFromLane(tempLane);

		// Post-conditions
		verifyZeroInteractions(emptyManager);
		verifyZeroInteractions(storeInManager);
		assertEquals(carrierWithParts.getCarrierNumber(), deliveredCarrier.getCarrierNumber());
	}

	@Test
	public void successfullyHandleCarrierUpdateForCarrierInStorageSystemScenario1() {
		// Pre-conditions
		CarrierMes carrierMes = new CarrierMes();
		carrierMes.setCarrierNumber(5);
		carrierMes.setCurrentLocation(1230l);
		carrierMes.setDestination(1230l);
		carrierMes.setDieNumber(1);
		carrierMes.setStatus(0);

		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		when(storageStateContext.getCarrier(Matchers.<Integer>any())).thenReturn(carrierMes);
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test updating a carrier to move out of a lane
		Carrier carrier = lane2.getCarrierAtRowOut();
		carrier.setCurrentLocation(lane2.getStop());
		carrier.setDestination(getStop(1300L, StopArea.STORE_OUT_ROUTE));
		storage.sendCarrierUpdateMessage(carrier);

		// PostConditions
		assertNotSame(carrier.getCarrierNumber(), lane2.getCarrierAtRowOut().getCarrierNumber());
		// assertEquals(carrier.getCarrierNumber(),lane1.getCarrierAtLaneIn().getCarrierNumber()
		// );
	}

	@Test
	public void successfullyHandleCarrierUpdateForCarrierInStorageSystemScenario2() {
		// Pre-conditions
		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test updating quantity only for carrier in a lane
		Carrier carrier = lane2.getCarrierAtRowOut();
		carrier.setCurrentLocation(lane2.getStop());
		carrier.setDestination(lane2.getStop());
		carrier.setQuantity(10);
		storage.sendCarrierUpdateMessage(carrier);

		// PostConditions
		assertEquals(carrier.getCarrierNumber(), lane2.getCarrierAtRowOut().getCarrierNumber());
	}

	@Test
	public void successfullyHandleCarrierUpdateForCarrierInStorageSystemScenario3() {
		// Pre-conditions
		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		Carrier carrier = OM.carrier_HighVolume_PartA();
		carrier.setCurrentLocation(getStop(513L, StopArea.STORE_IN_ROUTE));
		carrier.setDestination(lane2.getStop());

		CarrierMes carrierMes = new CarrierMes();
		carrierMes.setCarrierNumber(carrier.getCarrierNumber());
		carrierMes.setCurrentLocation(513l);
		carrierMes.setDestination(513l);
		carrierMes.setDieNumber(1);
		carrierMes.setStatus(0);

		when(storageStateContext.getCarrier(Matchers.<Integer>any())).thenReturn(carrierMes);

		// Perform test updating carrier to move into a lane
		storage.sendCarrierUpdateMessage(carrier);

		// PostConditions
		assertEquals(carrier.getCarrierNumber(), lane2.getCarrierAtLaneIn().getCarrierNumber());
	}

	@Test
	public void successfullyHandleCarrierUpdateForCarrierInStorageSystemScenario4() {
		// Pre-conditions
		when(storageStateContext.getStorageState()).thenReturn(getStorageState());
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		// Perform test updating carrier quantity moving into a lane
		Carrier carrier = lane2.getCarrierAtLaneIn();
		carrier.setCurrentLocation(getStop(803L, StopArea.STORE_IN_ROUTE));
		carrier.setDestination(lane2.getStop());
		carrier.setQuantity(10);
		storage.sendCarrierUpdateMessage(carrier);

		// PostConditions
		assertEquals(carrier.getCarrierNumber(), lane2.getCarrierAtLaneIn().getCarrierNumber());
		assertEquals(3, lane2.getCurrentCarrierCount());
	}

	@Test
	public void successfullyStoreReworkCarrier() {
		Stop currentLocation = getStop(405L, StopArea.STORE_IN_ROUTE);
		currentLocation.setStopType(StopType.REWORK);

		Carrier carrier = new Carrier();
		carrier.setCarrierNumber(101);
		carrier.setCurrentLocation(currentLocation);
		carrier.setQuantity(10);
		carrier.setDie(new Die(999L, PartProductionVolume.HIGH_VOLUME));

		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		storage.store(carrier);
	}

	@Test
	public void successfullyRetrieveCarrier() {
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, storageStateContext);

		storage.retrieve(new Die(101L, PartProductionVolume.HIGH_VOLUME));

		storage.retrieveEmptyCarrier();

		storage.retrieveEmptyCarrier(StorageArea.C_LOW);

		Map<OrderMgr, Die> backOrder = new HashMap<OrderMgr, Die>();
		OrderMgr orderMgr1 = new OrderMgr();
		OrderMgr orderMgr2 = new OrderMgr();
		Die die1 = new Die(1L, PartProductionVolume.LOW_VOLUME);
		Die die2 = new Die(2L, PartProductionVolume.LOW_VOLUME);

		backOrder.put(orderMgr1, die1);
		backOrder.put(orderMgr1, die2);
		backOrder.put(orderMgr2, die1);
		backOrder.put(orderMgr2, die2);

		System.out.println(backOrder.size());
		System.out.println(backOrder.get(orderMgr1));
		System.out.println(backOrder.get(orderMgr2));

		System.out.println(backOrder.remove(orderMgr1));
		System.out.println(backOrder.remove(orderMgr2));

		System.out.println(!backOrder.containsValue(die2));

	}

	private Carrier getCarrierWithInvalidDie() {
		Carrier c = getEmptyCarrier();
		Die d = new Die(4L, PartProductionVolume.HIGH_VOLUME);
		d.setActive(false);
		c.setDie(d);
		c.setQuantity(1);

		Stop stop = new Stop();
		stop.setId(513L);
		stop.setStopType(StopType.STORE_IN_ALL_LANES);
		c.setCurrentLocation(stop);
		return c;
	}

	private Carrier getCarrierWithParts() {
		Carrier c = getEmptyCarrier();
		Die d = new Die(4L, PartProductionVolume.HIGH_VOLUME);
		c.setDie(d);
		c.setQuantity(12);

		Stop stop = new Stop();
		stop.setId(513L);
		stop.setStopType(StopType.STORE_IN_ALL_LANES);
		c.setCurrentLocation(stop);
		return c;
	}

	private Carrier getCarrierWithPartsToSubStore() {
		Carrier c = getEmptyCarrier();
		Die d = new Die(4L, PartProductionVolume.HIGH_VOLUME);
		c.setDie(d);
		c.setQuantity(12);

		Stop stop = new Stop();
		stop.setId(704L);
		stop.setStopType(StopType.STORE_IN_C_LOW_LANES);
		c.setCurrentLocation(stop);
		return c;
	}

	private Carrier getEmptyCarrier() {
		Stop stop = new Stop();
		stop.setId(513L);
		stop.setStopType(StopType.STORE_IN_ALL_LANES);
		Carrier c = new Carrier();
		c.setCarrierNumber(123);
		c.setCurrentLocation(stop);
		c.setDie(new Die(999L, PartProductionVolume.MEDIUM_VOLUME));
		return c;
	}

	private Stop getStop(Long conveyorId, StopArea area) {
		Stop stop = new Stop();
		stop.setId(conveyorId);
		stop.setStopArea(area);
		return stop;
	}

	StorageState getStorageState() {
		List<StorageRow> lanes = new ArrayList<StorageRow>();

		lane1 = OM.lane_A_AREA_Empty();
		lane1.setStop(getStop(1221L, StopArea.ROW));

		lane2 = OM.lane_C_LOW_Empty();
		lane2.setStop(getStop(1230L, StopArea.ROW));

		lane3 = OM.lane_C_LOW_Empty();
		lane3.setStop(getStop(1231L, StopArea.ROW));

		lane1.store(OM.carrier_HighVolume_PartA());
		lane1.store(OM.carrier_HighVolume_PartA());
		lane1.store(OM.carrier_HighVolume_PartA());
		lane1.store(OM.carrier_HighVolume_PartA());

		lane2.store(OM.carrier_HighVolume_PartA());
		lane2.store(OM.carrier_HighVolume_PartA());
		lane2.store(OM.carrier_HighVolume_PartA());

		lane3.store(OM.carrier_HighVolume_PartA());
		lane3.store(OM.carrier_HighVolume_PartA());

		lanes.add(lane1);
		lanes.add(lane2);
		lanes.add(lane3);
		return new StorageStateImpl(lanes);
	}
}
