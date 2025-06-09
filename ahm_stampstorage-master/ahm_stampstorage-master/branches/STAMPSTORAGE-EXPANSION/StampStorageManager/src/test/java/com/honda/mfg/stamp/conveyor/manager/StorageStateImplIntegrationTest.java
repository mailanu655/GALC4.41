package com.honda.mfg.stamp.conveyor.manager;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * User: VCC30690 Date: 9/14/11
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class StorageStateImplIntegrationTest {

	StorageState storageState = null;

	@Test
	public void testReorderLane() {
		initStorage();
		loadDieTable();
		loadStopTable();
		loadCarrierMesTable();

		storageState.reorderCarriersInRow(1201L);

		int position1 = storageState.getCarrierPositionInLane(1201L, 111);
		int position2 = storageState.getCarrierPositionInLane(1201L, 112);
		int position3 = storageState.getCarrierPositionInLane(1201L, 113);
		int position4 = storageState.getCarrierPositionInLane(1201L, 114);

		assertEquals(2, position1);
		assertEquals(1, position2);
		assertEquals(3, position3);
		assertEquals(4, position4);
	}

	@Test
	public void testRemoveAndAddCarrierToLane() {
		initStorage();
		loadStopTable();
		loadDieTable();
		loadCarrierMesTable();
		Die die = new Die();
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Calendar c = Calendar.getInstance();
		Timestamp updateDate = new Timestamp(c.getTimeInMillis());
		c.add(Calendar.SECOND, 5);
		Timestamp updateDate2 = new Timestamp(c.getTimeInMillis());
		c.add(Calendar.SECOND, 5);
		Timestamp updateDate3 = new Timestamp(c.getTimeInMillis());
		c.add(Calendar.SECOND, 5);
		Timestamp updateDate4 = new Timestamp(c.getTimeInMillis());

		Carrier carrier115 = new Carrier(10, new Timestamp(System.currentTimeMillis()), new Integer(100),
				new Stop(1229L), new Stop(1229L), CarrierStatus.SHIPPABLE, new Integer(115), die);
		Carrier carrier116 = new Carrier(10, new Timestamp(System.currentTimeMillis()), new Integer(100),
				new Stop(1229L), new Stop(1229L), CarrierStatus.SHIPPABLE, new Integer(116), die);
		Carrier carrier117 = new Carrier(10, new Timestamp(System.currentTimeMillis()), new Integer(100),
				new Stop(513L), new Stop(1229L), CarrierStatus.SHIPPABLE, new Integer(117), die);
		Carrier carrier118 = new Carrier(10, new Timestamp(System.currentTimeMillis()), new Integer(100),
				new Stop(805L), new Stop(1229L), CarrierStatus.SHIPPABLE, new Integer(118), die);

		carrier115.setUpdateDate(updateDate);
		carrier116.setUpdateDate(updateDate2);
		carrier117.setUpdateDate(updateDate3);
		carrier118.setUpdateDate(updateDate4);

		List<Carrier> carrierList = new ArrayList<Carrier>();
		carrierList.add(carrier115);
		carrierList.add(carrier116);
		carrierList.add(carrier117);
		carrierList.add(carrier118);

		Stop stop = new Stop();
		stop.setId(1229L);
		stop.setName("ST12-29");
		stop.setStopType(StopType.NO_ACTION);

		storageState.addCarriersToLane(carrierList, stop);

		// assertEquals(185, storageState.spaceAvailableInA());

		int position1 = storageState.getCarrierPositionInLane(1229L, 115);
		int position2 = storageState.getCarrierPositionInLane(1229L, 116);
		int position3 = storageState.getCarrierPositionInLane(1229L, 117);
		int position4 = storageState.getCarrierPositionInLane(1229L, 118);

		assertEquals(1, position1);
		assertEquals(2, position2);
		assertEquals(3, position3);
		assertEquals(4, position4);

		storageState.removeCarrierFromRow(113, 1229L);

		position3 = storageState.getCarrierPositionInLane(1229L, 113);
		assertEquals(0, position3);

		Carrier carrier = new Carrier();
		carrier.setCarrierNumber(111);
		carrier.setDestination(Stop.findStop(1201L));
		storageState.removeCarrierFromStorageState(carrier);

		assertEquals(0, storageState.getCarrierPositionInLane(1201L, 111));

		Carrier carrier119 = new Carrier(10, new Timestamp(System.currentTimeMillis()), new Integer(100),
				new Stop("ST12-01"), new Stop("ST12-01"), CarrierStatus.SHIPPABLE, new Integer(115), die);

		carrier119.setUpdateDate(updateDate);

		List<Carrier> carrierList2 = new ArrayList<Carrier>();
		carrierList2.add(carrier119);

		Stop stop1 = new Stop();
		stop1.setId(1201L);
		stop1.setName("ST12-01");
		stop1.setStopType(StopType.NO_ACTION);

		storageState.addCarriersToLane(carrierList2, stop1);

		int position5 = storageState.getCarrierPositionInLane(1201L, 119);

		assertEquals(0, position5);
	}

	@Test
	public void testIfCarrierHasInvalidDestination() {
		initStorage();
		loadStopTable();
		loadDieTable();
		loadCarrierMesTable();

		Carrier carrier = new Carrier();
		carrier.setCarrierNumber(113);
		carrier.setDestination(Stop.findStop(1201L));

		assertEquals(true, storageState.hadValidLaneDestination(carrier));

		Carrier carrier2 = new Carrier();
		carrier2.setCarrierNumber(115);
		carrier2.setDestination(Stop.findStop(1201L));
		assertEquals(false, storageState.hadValidLaneDestination(carrier2));

	}

	private void loadCarrierMesTable() {
		Calendar c = Calendar.getInstance();
		Timestamp updateDate = new Timestamp(c.getTimeInMillis());

		CarrierMes mesCarrier1 = new CarrierMes();
		mesCarrier1.setCarrierNumber(111);
		mesCarrier1.setCurrentLocation(1201L);
		mesCarrier1.setDestination(1201L);
		mesCarrier1.setDieNumber(166);
		mesCarrier1.setQuantity(10);
		mesCarrier1.setStatus(0);
		mesCarrier1.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier1.setProductionRunNumber(100);
		mesCarrier1.setOriginationLocation(0);
		mesCarrier1.setUpdateDate(updateDate);
		mesCarrier1.setBuffer(0);

		c.add(Calendar.SECOND, 5);
		Timestamp updateDate2 = new Timestamp(c.getTimeInMillis());
		CarrierMes mesCarrier2 = new CarrierMes();
		mesCarrier2.setCarrierNumber(112);
		mesCarrier2.setCurrentLocation(1201L);
		mesCarrier2.setDestination(1201L);
		mesCarrier2.setDieNumber(166);
		mesCarrier2.setQuantity(10);
		mesCarrier2.setStatus(0);
		mesCarrier2.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier2.setProductionRunNumber(100);
		mesCarrier2.setOriginationLocation(0);
		mesCarrier2.setUpdateDate(updateDate2);
		mesCarrier2.setBuffer(1);

		c.add(Calendar.SECOND, -8);
		Timestamp updateDate3 = new Timestamp(c.getTimeInMillis());
		CarrierMes mesCarrier3 = new CarrierMes();
		mesCarrier3.setCarrierNumber(113);
		mesCarrier3.setCurrentLocation(513L);
		mesCarrier3.setDestination(1201L);
		mesCarrier3.setDieNumber(166);
		mesCarrier3.setQuantity(10);
		mesCarrier3.setStatus(0);
		mesCarrier3.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier3.setProductionRunNumber(100);
		mesCarrier3.setOriginationLocation(0);
		mesCarrier3.setUpdateDate(updateDate3);
		mesCarrier3.setBuffer(0);

		c.add(Calendar.SECOND, 15);
		Timestamp updateDate4 = new Timestamp(c.getTimeInMillis());
		CarrierMes mesCarrier4 = new CarrierMes();
		mesCarrier4.setCarrierNumber(114);
		mesCarrier4.setCurrentLocation(805L);
		mesCarrier4.setDestination(1201L);
		mesCarrier4.setDieNumber(166);
		mesCarrier4.setQuantity(10);
		mesCarrier4.setStatus(0);
		mesCarrier4.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier4.setProductionRunNumber(100);
		mesCarrier4.setOriginationLocation(0);
		mesCarrier4.setUpdateDate(updateDate4);
		mesCarrier4.setBuffer(0);

		mesCarrier1.persist();
		mesCarrier2.persist();
		mesCarrier3.persist();
		mesCarrier4.persist();

		CarrierMes mesCarrier5 = new CarrierMes();
		mesCarrier5.setCarrierNumber(115);
		mesCarrier5.setCurrentLocation(1229L);
		mesCarrier5.setDestination(1229L);
		mesCarrier5.setDieNumber(166);
		mesCarrier5.setQuantity(10);
		mesCarrier5.setStatus(0);
		mesCarrier5.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier5.setProductionRunNumber(100);
		mesCarrier5.setOriginationLocation(0);
		mesCarrier5.setUpdateDate(updateDate);
		mesCarrier5.setBuffer(0);

		CarrierMes mesCarrier6 = new CarrierMes();
		mesCarrier6.setCarrierNumber(116);
		mesCarrier6.setCurrentLocation(1229L);
		mesCarrier6.setDestination(1229L);
		mesCarrier6.setDieNumber(166);
		mesCarrier6.setQuantity(10);
		mesCarrier6.setStatus(0);
		mesCarrier6.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier6.setProductionRunNumber(100);
		mesCarrier6.setOriginationLocation(0);
		mesCarrier6.setUpdateDate(updateDate);
		mesCarrier6.setBuffer(0);

		CarrierMes mesCarrier7 = new CarrierMes();
		mesCarrier7.setCarrierNumber(117);
		mesCarrier7.setCurrentLocation(1229L);
		mesCarrier7.setDestination(1229L);
		mesCarrier7.setDieNumber(166);
		mesCarrier7.setQuantity(10);
		mesCarrier7.setStatus(0);
		mesCarrier7.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier7.setProductionRunNumber(100);
		mesCarrier7.setOriginationLocation(0);
		mesCarrier7.setUpdateDate(updateDate);
		mesCarrier7.setBuffer(0);

		CarrierMes mesCarrier8 = new CarrierMes();
		mesCarrier8.setCarrierNumber(118);
		mesCarrier8.setCurrentLocation(1229L);
		mesCarrier8.setDestination(1229L);
		mesCarrier8.setDieNumber(166);
		mesCarrier8.setQuantity(10);
		mesCarrier8.setStatus(0);
		mesCarrier8.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier8.setProductionRunNumber(100);
		mesCarrier8.setOriginationLocation(0);
		mesCarrier8.setUpdateDate(updateDate);
		mesCarrier8.setBuffer(0);

		mesCarrier5.persist();
		mesCarrier6.persist();
		mesCarrier7.persist();
		mesCarrier8.persist();
	}

	void loadDieTable() {
		Die die = new Die();
		// die.setDieNumber(166);
		die.setId(166l);
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die.persist();
	}

	void loadStopTable() {
		Stop stop = new Stop();
		stop.setId(1201L);
		stop.setName("ST12-01");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.ROW);

		Stop stop1 = new Stop();
		stop1.setId(513L);
		stop1.setName("ST5-13");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.STORE_IN_ROUTE);

		Stop stop2 = new Stop();
		stop2.setId(805L);
		stop2.setName("ST8-5");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.STORE_IN_ROUTE);

		Stop stop3 = new Stop();
		stop3.setId(0L);
		stop3.setName("ST0");
		stop3.setStopType(StopType.MAINTENANCE);
		stop3.setStopArea(StopArea.UNDEFINED);

		Stop stop4 = new Stop();
		stop4.setId(1229L);
		stop4.setName("ST12-01");
		stop4.setStopType(StopType.NO_ACTION);
		stop4.setStopArea(StopArea.ROW);

		stop.persist();
		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();
	}

	public void initStorage() {
		Die leftDie = new Die();
		// leftDie.setDieNumber(101);
		leftDie.setDescription("left_die_101");
		leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Die rightDie = new Die();
		// rightDie.setDieNumber(102);
		rightDie.setDescription("right_die_102");
		rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Die someDie = new Die();
		// someDie.setDieNumber(103);
		someDie.setDescription("right_die_103");
		someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Die die = new Die();
		// die.setDieNumber(166);
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Calendar c = Calendar.getInstance();
		Timestamp today = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -2);
		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		Carrier carrier2 = new Carrier(1, today, new Integer(103), new Stop("ST12-32"), null, CarrierStatus.SHIPPABLE,
				new Integer(200), someDie);

		Carrier carrier3 = new Carrier(2, today, new Integer(103), new Stop("ST12-33"), null, CarrierStatus.SHIPPABLE,
				new Integer(202), someDie);

		Carrier carrier1 = new Carrier(3, twoDaysOld, new Integer(90), new Stop("ST12-10"), null,
				CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
		Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), new Stop("ST12-11"), null,
				CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
		Carrier carrier12 = new Carrier(5, twoDaysOld, new Integer(90), new Stop("ST12-12"), null,
				CarrierStatus.SHIPPABLE, new Integer(720), leftDie);

		Carrier carrier4 = new Carrier(6, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(109), leftDie);
		Carrier carrier41 = new Carrier(7, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(596), leftDie);
		Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(90), new Stop("ST12-34"), null,
				CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
		Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(90), new Stop("ST12-34"), null,
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

		Carrier carrier111 = new Carrier(10, today, new Integer(100), new Stop("ST12-01"), new Stop("ST12-01"),
				CarrierStatus.SHIPPABLE, new Integer(111), die);
		Carrier carrier112 = new Carrier(10, today, new Integer(100), new Stop("ST12-01"), new Stop("ST12-01"),
				CarrierStatus.SHIPPABLE, new Integer(112), die);
		Carrier carrier113 = new Carrier(10, today, new Integer(100), new Stop("ST5-13"), new Stop("ST12-01"),
				CarrierStatus.SHIPPABLE, new Integer(113), die);
		Carrier carrier114 = new Carrier(10, today, new Integer(100), new Stop("ST8-5"), new Stop("ST12-01"),
				CarrierStatus.SHIPPABLE, new Integer(114), die);

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

		storageLanes.get(0).store(carrier111);
		storageLanes.get(0).store(carrier112);
		storageLanes.get(0).store(carrier113);
		storageLanes.get(0).store(carrier114);

		storageState = new StorageStateImpl(storageLanes);
	}

	List<StorageRow> getStorageRows() {
		List<StorageRow> storageRows = new ArrayList<StorageRow>();

		for (int i = 0; i < 35; i++) {
			StorageRow row = new StorageRow(i, "Row-" + i, 10, 1);
			row.setStop(new Stop(1200 + 1 + i));
			storageRows.add(row);
		}

		return storageRows;
	}
}
