package com.honda.mfg.stamp.conveyor.rules.empty;

import static junit.framework.Assert.assertNotNull;
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
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.EmptyManager;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContextMock;
import com.honda.mfg.stamp.conveyor.manager.StorageStateImpl;

/**
 * User: vcc30690 Date: 6/21/11
 */
public class EmptyManagerTest {
	EmptyManager emptyManager;
	Die leftDie, rightDie, someDie, emptyDie;
	StorageState storageState;

	@Test
	public void retrieveTest() {

		initStorage();

		Stop destination = new Stop("ST136");
		destination.setId(13600L);

		emptyManager.retrieveEmptyCarrier();

		emptyManager.retrieveEmptyCarrierForOldWeldLineEmptyStorage();

		emptyManager.retrieveEmptyCarrierForBAreaEmptyStorage();

		Carrier carrier55 = new Carrier(14, new Timestamp(Calendar.getInstance().getTimeInMillis()), null,
				new Stop("ST5-13"), null, CarrierStatus.SHIPPABLE, new Integer(605), emptyDie);
		carrier55.setQuantity(new Integer(0));

		emptyManager.storeEmptyCarrier(carrier55);

		Carrier carrier56 = new Carrier(15, new Timestamp(Calendar.getInstance().getTimeInMillis()), null,
				new Stop("ST5-13"), null, CarrierStatus.SHIPPABLE, new Integer(650), emptyDie);
		carrier56.setQuantity(new Integer(0));

		emptyManager.storeEmptyCarrier(carrier56);
	}

	public void initStorage() {
		int testMode = 1;
		leftDie = new Die();
		leftDie.setId(101l);
		leftDie.setDescription("left_die_101");
		leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		rightDie = new Die();
		rightDie.setId(102l);
		rightDie.setDescription("right_die_102");
		rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		someDie = new Die();
		someDie.setId(103l);
		someDie.setDescription("right_die_103");
		someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		emptyDie = new Die();
		emptyDie.setId(999l);
		emptyDie.setDescription("empty_die_999");
		emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Calendar c = Calendar.getInstance();
		Timestamp today = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -2);
		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		Carrier carrier2 = new Carrier(1, today, new Integer(103), new Stop(1232L), new Stop(1232L),
				CarrierStatus.SHIPPABLE, new Integer(200), someDie);
		Carrier carrier3 = new Carrier(2, today, new Integer(103), new Stop(1233L), new Stop(1233L),
				CarrierStatus.SHIPPABLE, new Integer(202), someDie);

		Carrier carrier1 = new Carrier(3, twoDaysOld, new Integer(90), new Stop(1210L), new Stop(1210L),
				CarrierStatus.SHIPPABLE, new Integer(696), emptyDie);
		Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), new Stop(1211L), new Stop(1211L),
				CarrierStatus.SHIPPABLE, new Integer(130), emptyDie);
		Carrier carrier12 = new Carrier(5, twoDaysOld, new Integer(90), new Stop(1212L), new Stop(1212L),
				CarrierStatus.SHIPPABLE, new Integer(720), emptyDie);

		Carrier carrier111 = new Carrier(15, twoDaysOld, new Integer(90), new Stop(1208L), new Stop(1208L),
				CarrierStatus.SHIPPABLE, new Integer(696), emptyDie);
		Carrier carrier112 = new Carrier(16, twoDaysOld, new Integer(99), new Stop(1207L), new Stop(1207L),
				CarrierStatus.SHIPPABLE, new Integer(130), emptyDie);
		Carrier carrier113 = new Carrier(17, twoDaysOld, new Integer(90), new Stop(1207L), new Stop(1207L),
				CarrierStatus.SHIPPABLE, new Integer(720), emptyDie);

		Carrier carrier4 = new Carrier(6, today, null, new Stop(1235L), new Stop(1235L), CarrierStatus.SHIPPABLE,
				new Integer(109), emptyDie);
		Carrier carrier41 = new Carrier(7, today, null, new Stop(1235L), new Stop(1235L), CarrierStatus.SHIPPABLE,
				new Integer(596), emptyDie);
		Carrier carrier42 = new Carrier(8, twoDaysOld, null, new Stop(1234L), new Stop(1234L), CarrierStatus.SHIPPABLE,
				new Integer(35), emptyDie);
		Carrier carrier43 = new Carrier(9, twoDaysOld, null, new Stop(1234L), new Stop(1234L), CarrierStatus.SHIPPABLE,
				new Integer(425), emptyDie);

		Carrier carrier5 = new Carrier(10, today, new Integer(101), new Stop(1231L), new Stop(1231L),
				CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
		Carrier carrier51 = new Carrier(11, today, new Integer(101), new Stop(1231L), new Stop(1231L),
				CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
		Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), new Stop(1230L), new Stop(1230L),
				CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
		Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), new Stop(1230L), new Stop(1230L),
				CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
		Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), new Stop(1230L), new Stop(1230L),
				CarrierStatus.SHIPPABLE, new Integer(605), rightDie);

		Carrier carrier121 = new Carrier(18, twoDaysOld, new Integer(90), new Stop(1221L), new Stop(1221L),
				CarrierStatus.SHIPPABLE, new Integer(696), emptyDie);
		Carrier carrier122 = new Carrier(19, twoDaysOld, new Integer(99), new Stop(1221L), new Stop(1221L),
				CarrierStatus.SHIPPABLE, new Integer(130), emptyDie);
		Carrier carrier123 = new Carrier(20, twoDaysOld, new Integer(90), new Stop(1221L), new Stop(1221L),
				CarrierStatus.SHIPPABLE, new Integer(720), emptyDie);

		Carrier carrier131 = new Carrier(18, twoDaysOld, new Integer(90), new Stop(1240L), new Stop(1240L),
				CarrierStatus.SHIPPABLE, new Integer(700), emptyDie);
		Carrier carrier132 = new Carrier(19, twoDaysOld, new Integer(99), new Stop(1240L), new Stop(1240L),
				CarrierStatus.SHIPPABLE, new Integer(701), emptyDie);
		Carrier carrier133 = new Carrier(20, twoDaysOld, new Integer(90), new Stop(1240L), new Stop(1240L),
				CarrierStatus.SHIPPABLE, new Integer(721), emptyDie);

		List<StorageRow> storageLanes = getStorageRows();

		carrier2.setCurrentLocation(storageLanes.get(31).getStop());
		storageLanes.get(31).store(carrier2);
		carrier3.setCurrentLocation(storageLanes.get(32).getStop());
		storageLanes.get(32).store(carrier3);

		storageLanes.get(7).store(carrier111);
		storageLanes.get(6).store(carrier112);
		storageLanes.get(6).store(carrier113);
		carrier1.setCurrentLocation(storageLanes.get(9).getStop());
		storageLanes.get(9).store(carrier1);
		carrier11.setCurrentLocation(storageLanes.get(10).getStop());
		storageLanes.get(10).store(carrier11);
		carrier12.setCurrentLocation(storageLanes.get(11).getStop());
		storageLanes.get(11).store(carrier12);

		carrier4.setCurrentLocation(storageLanes.get(34).getStop());
		storageLanes.get(34).store(carrier4);
		carrier41.setCurrentLocation(storageLanes.get(34).getStop());
		storageLanes.get(34).store(carrier41);
		carrier42.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier42);
		carrier43.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier43);

		carrier5.setCurrentLocation(storageLanes.get(30).getStop());
		storageLanes.get(30).store(carrier5);
		carrier51.setCurrentLocation(storageLanes.get(30).getStop());
		storageLanes.get(30).store(carrier51);
		carrier52.setCurrentLocation(storageLanes.get(29).getStop());
		storageLanes.get(29).store(carrier52);
		carrier53.setCurrentLocation(storageLanes.get(29).getStop());
		storageLanes.get(29).store(carrier53);
		carrier54.setCurrentLocation(storageLanes.get(29).getStop());
		storageLanes.get(29).store(carrier54);

		storageLanes.get(20).store(carrier121);
		storageLanes.get(20).store(carrier122);
		storageLanes.get(20).store(carrier123);

		storageLanes.get(39).store(carrier131);
		storageLanes.get(39).store(carrier132);
		storageLanes.get(39).store(carrier133);

		storageState = new StorageStateImpl(storageLanes);
		StorageStateContext context = new StorageStateContextMock(storageState);
		emptyManager = new EmptyManagerImpl(context);
	}

	List<StorageRow> getStorageRows() {
		List<StorageRow> storageRows = new ArrayList<StorageRow>();
		int i = 0;
		for (i = 1; i < 21; i++) {
			StorageRow row = new StorageRow(i, "Row-" + i, 12, 1);
			row.setStop(new Stop(1200 + i));
			row.setStorageArea(StorageArea.C_HIGH);
			row.setAvailability(StopAvailability.AVAILABLE);
			storageRows.add(row);
		}
		for (i = 21; i < 30; i++) {
			StorageRow row = new StorageRow(i, "Row-" + i, 21, 1);
			row.setStop(new Stop(1200 + i));
			row.setStorageArea(StorageArea.A_AREA);
			row.setAvailability(StopAvailability.AVAILABLE);
			storageRows.add(row);
		}
		for (i = 30; i < 36; i++) {
			StorageRow row = new StorageRow(i, "Row-" + i, 30, 1);
			row.setStop(new Stop(1200 + i));
			row.setStorageArea(StorageArea.C_LOW);
			row.setAvailability(StopAvailability.AVAILABLE);
			storageRows.add(row);
		}

		for (i = 36; i < 44; i++) {
			StorageRow row = new StorageRow(i, "Row-" + i, 31, 1);
			row.setStop(new Stop(1200 + i));
			row.setStorageArea(StorageArea.B_AREA);
			row.setAvailability(StopAvailability.AVAILABLE);
			storageRows.add(row);
		}
		return storageRows;
	}

	@Test
	public void retrieveEmptyCarrierTest() {

		initStorage();

		StorageRow lane = emptyManager.retrieveEmptyCarrier(StorageArea.C_LOW);
		assertEquals(2, lane.getCurrentCarrierCount());
		assertEquals(StorageArea.C_LOW, lane.getStorageArea());
		storageState.releaseCarrierFromLane(lane);
		storageState.releaseCarrierFromLane(lane);

		StorageRow lane2 = emptyManager.retrieveEmptyCarrier(StorageArea.A_AREA);
		assertEquals(3, lane2.getCurrentCarrierCount());
		assertEquals(StorageArea.A_AREA, lane2.getStorageArea());
		StorageRow lane3 = emptyManager.retrieveEmptyCarrier(StorageArea.C_HIGH);

		// assertEquals(2, lane3.getCurrentCarrierCount());

		StorageRow lane4 = emptyManager.retrieveEmptyCarrier();

		assertNotNull(lane4);
	}

	@Test
	public void doesNotThrowExceptionWhenNoRowFound() {
		initStorage();

		StorageRow lane = emptyManager.retrieveEmptyCarrier(StorageArea.C_LOW);
		assertEquals(2, lane.getCurrentCarrierCount());
		storageState.releaseCarrierFromLane(lane);
		storageState.releaseCarrierFromLane(lane);
		StorageRow lane35 = emptyManager.retrieveEmptyCarrier(StorageArea.C_LOW);
		storageState.releaseCarrierFromLane(lane35);
		StorageRow lane1 = emptyManager.retrieveEmptyCarrier(StorageArea.C_LOW);
		storageState.releaseCarrierFromLane(lane1);
		StorageRow lane2 = emptyManager.retrieveEmptyCarrier(StorageArea.C_LOW);
		assertNull(lane2);
	}

	@Test
	public void retrieveEmptyCarriersWhenStorageStateStale() {
		StorageStateContext context = mock(StorageStateContext.class);
		StorageState storageState = mock(StorageState.class);
		when(context.getStorageState()).thenReturn(storageState);
		when(context.getEmptyDie()).thenReturn(new Die(999L, PartProductionVolume.EMPTY));
		when(storageState.isStale()).thenReturn(true);

		EmptyManager emptyManager = new EmptyManagerImpl(context);

		emptyManager.retrieveEmptyCarrier();
		Carrier c = new Carrier();
		c.setDie(new Die(999L, PartProductionVolume.MEDIUM_VOLUME));
		emptyManager.storeEmptyCarrier(c);
		emptyManager.retrieveEmptyCarrier(StorageArea.C_LOW);

	}
}
