package com.honda.mfg.stamp.conveyor.rules.store_out;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StorageStateImpl;
import com.honda.mfg.stamp.conveyor.manager.StoreOutManager;

/**
 * User: vcc30690 Date: 6/20/11
 */
public class StoreOutManagerImplTest {

	StoreOutManager storeOutManager;
	StorageState storageState;
	Die leftDie, rightDie, someDie, emptyDie;

	public void initStorage() {
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
		someDie.setPartProductionVolume(PartProductionVolume.LOW_VOLUME);

		Calendar c = Calendar.getInstance();
		Timestamp today = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -2);
		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		Carrier carrier2 = new Carrier(1, today, new Integer(103), new Stop("ST12-32"), null, CarrierStatus.SHIPPABLE,
				new Integer(200), someDie);

		Carrier carrier3 = new Carrier(2, today, new Integer(103), new Stop("ST12-33"), null, CarrierStatus.SHIPPABLE,
				new Integer(202), someDie);

		Carrier carrier1 = new Carrier(3, today, new Integer(90), new Stop("ST12-10"), null, CarrierStatus.SHIPPABLE,
				new Integer(696), leftDie);
		Carrier carrier11 = new Carrier(4, today, new Integer(99), new Stop("ST12-11"), null, CarrierStatus.SHIPPABLE,
				new Integer(130), rightDie);
		Carrier carrier12 = new Carrier(5, today, new Integer(90), new Stop("ST12-12"), null, CarrierStatus.SHIPPABLE,
				new Integer(720), leftDie);

		Carrier carrier4 = new Carrier(6, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(109), leftDie);
		Carrier carrier41 = new Carrier(7, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(596), leftDie);
		Carrier carrier44 = new Carrier(17, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(505), leftDie);
		Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(190), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
		Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(190), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(425), leftDie);

		Carrier carrier5 = new Carrier(10, today, new Integer(101), new Stop("ST12-31"), null, CarrierStatus.SHIPPABLE,
				new Integer(131), rightDie);
		Carrier carrier51 = new Carrier(11, today, new Integer(101), new Stop("ST12-31"), null, CarrierStatus.SHIPPABLE,
				new Integer(203), rightDie);
		Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), new Stop("ST12-30"), null,
				CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
		Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), new Stop("ST12-30"), null,
				CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
		Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), new Stop("ST12-30"), null,
				CarrierStatus.SHIPPABLE, new Integer(605), rightDie);

		List<StorageRow> storageLanes = getStorageRows();

		carrier2.setCurrentLocation(storageLanes.get(31).getStop());
		storageLanes.get(31).store(carrier2);
		carrier3.setCurrentLocation(storageLanes.get(32).getStop());
		storageLanes.get(32).store(carrier3);

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
		carrier44.setCurrentLocation(storageLanes.get(34).getStop());
		storageLanes.get(34).store(carrier44);
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

		storageState = new StorageStateImpl(storageLanes);
		StorageStateContext context = mock(StorageStateContext.class);
		when(context.getStorageState()).thenReturn(storageState);
		storeOutManager = new StoreOutManagerImpl(context);
	}

	public void initStorage2() {
		leftDie = new Die();
		leftDie.setId(101l);
		leftDie.setDescription("left_die_101");
		leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		emptyDie = new Die();
		emptyDie.setId(999l);
		emptyDie.setDescription("empty_die_999");
		emptyDie.setPartProductionVolume(PartProductionVolume.EMPTY);

		Calendar c = Calendar.getInstance();
		Timestamp today = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -2);
		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		Carrier carrier2 = new Carrier(1, today, new Integer(103), new Stop("ST12-32"), null, CarrierStatus.ON_HOLD,
				new Integer(200), leftDie);

		Carrier carrier3 = new Carrier(2, today, new Integer(103), new Stop("ST12-33"), null, CarrierStatus.ON_HOLD,
				new Integer(202), leftDie);

		Carrier carrier1 = new Carrier(3, today, new Integer(90), new Stop("ST12-10"), null, CarrierStatus.SHIPPABLE,
				new Integer(696), leftDie);
		Carrier carrier11 = new Carrier(4, today, new Integer(99), new Stop("ST12-11"), null, CarrierStatus.SHIPPABLE,
				new Integer(130), leftDie);
		Carrier carrier12 = new Carrier(5, today, new Integer(90), new Stop("ST12-12"), null, CarrierStatus.SHIPPABLE,
				new Integer(720), leftDie);

		Carrier carrier4 = new Carrier(6, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.ON_HOLD,
				new Integer(109), leftDie);
		Carrier carrier41 = new Carrier(7, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.ON_HOLD,
				new Integer(596), leftDie);
		Carrier carrier44 = new Carrier(17, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.ON_HOLD,
				new Integer(505), leftDie);
		Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(190), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
		Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(190), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(425), leftDie);

		Carrier carrier5 = new Carrier(10, today, new Integer(101), new Stop("ST12-31"), null, CarrierStatus.ON_HOLD,
				new Integer(131), leftDie);
		Carrier carrier51 = new Carrier(11, today, new Integer(101), new Stop("ST12-31"), null, CarrierStatus.ON_HOLD,
				new Integer(203), leftDie);
		Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), new Stop("ST12-30"), null,
				CarrierStatus.ON_HOLD, new Integer(126), leftDie);
		Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), new Stop("ST12-30"), null,
				CarrierStatus.SHIPPABLE, new Integer(465), leftDie);
		Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), new Stop("ST12-30"), null,
				CarrierStatus.SHIPPABLE, new Integer(605), leftDie);

		Carrier carrier6 = new Carrier(15, twoDaysOld, new Integer(99), new Stop("ST12-34"), null,
				CarrierStatus.ON_HOLD, new Integer(135), leftDie);
		Carrier carrier61 = new Carrier(16, twoDaysOld, new Integer(99), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(136), leftDie);
		Carrier carrier62 = new Carrier(16, twoDaysOld, new Integer(99), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(137), emptyDie);
		Carrier carrier63 = new Carrier(16, twoDaysOld, new Integer(99), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(138), emptyDie);
		List<StorageRow> storageLanes = getStorageRows();

		carrier2.setCurrentLocation(storageLanes.get(31).getStop());
		storageLanes.get(31).store(carrier2);
		carrier3.setCurrentLocation(storageLanes.get(32).getStop());
		storageLanes.get(32).store(carrier3);

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
		carrier44.setCurrentLocation(storageLanes.get(34).getStop());
		storageLanes.get(34).store(carrier44);
		carrier42.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier42);
		carrier43.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier43);
		carrier6.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier6);
		carrier61.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier61);

		carrier62.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier62);

		carrier63.setCurrentLocation(storageLanes.get(33).getStop());
		storageLanes.get(33).store(carrier63);

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

		storageState = new StorageStateImpl(storageLanes);
		StorageStateContext context = mock(StorageStateContext.class);
		when(context.getStorageState()).thenReturn(storageState);
		storeOutManager = new StoreOutManagerImpl(context);
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
		return storageRows;
	}

	@Test
	public void retrieveMediumVolumeDieTest() {
		initStorage();
		StorageRow StorageRow = storeOutManager.retrieve(leftDie);
		assertNotNull(StorageRow);
		assertEquals(34L, StorageRow.getId().longValue());
	}

	@Test
	public void retrieveMediumVolumeDieTest2() {
		initStorage2();
		StorageRow storageRow = null;
		for (int i = 0; i < 4; i++) {
			storageRow = storeOutManager.retrieve(leftDie);
			assertNotNull(storageRow);
			System.out.println(storageRow.getRowName());
			storageState.releaseCarrierFromLane(storageRow);

			if (i == 1) {
				Calendar c = Calendar.getInstance();
				Timestamp today = new Timestamp(c.getTimeInMillis());
				c.add(Calendar.DATE, -2);
				Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());
				Carrier carrier6 = new Carrier(15, twoDaysOld, new Integer(99), new Stop("ST12-34"),
						new Stop("ST12-34"), CarrierStatus.SHIPPABLE, new Integer(135), leftDie);
				storageState.updateCarrier(carrier6);
			}
			pause(1);

		}

	}

	@Test
	public void retrieveLowVolumeDieTest() {
		initStorage();

//        StorageRow lane1 = storeOutManager.retrieve(someDie);
//        assertNotNull(lane1);
//          assertEquals(32L,lane1.getId());
	}

	@Test
	public void retrieveWhenStorageStateStale() {
		StorageState storageState = mock(StorageState.class);
		when(storageState.isStale()).thenReturn(true);
		StorageStateContext context = mock(StorageStateContext.class);
		when(context.getStorageState()).thenReturn(storageState);
		storeOutManager = new StoreOutManagerImpl(context);
		StorageRow StorageRow = storeOutManager.retrieve(new Die());

		assertNull(StorageRow);
	}

	private void pause(int pauseSec) {
		long delta = System.currentTimeMillis();
		while (System.currentTimeMillis() - delta < (pauseSec * 100L)) {
		}
	}

}
