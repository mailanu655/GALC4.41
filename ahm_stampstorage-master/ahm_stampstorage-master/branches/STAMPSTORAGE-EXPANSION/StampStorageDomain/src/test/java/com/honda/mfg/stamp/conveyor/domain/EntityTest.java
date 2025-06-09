package com.honda.mfg.stamp.conveyor.domain;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.messages.StorageMessageType;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 1:04 PM To
 * change this template use File | Settings | File Templates.
 */
public class EntityTest {

	@Test
	public void carrierHistoryTest() {
		CarrierHistory carrierHistory1 = new CarrierHistory(1L);
		CarrierHistory carrierHistory2 = new CarrierHistory(2L);
		CarrierHistory carrierHistory3 = new CarrierHistory();
		assertFalse(carrierHistory1.equals(carrierHistory2));
		assertTrue(carrierHistory1.equals(carrierHistory1));
		assertFalse(carrierHistory1.equals(null));
		assertFalse(carrierHistory1.equals(carrierHistory3));
		assertFalse(carrierHistory3.equals(carrierHistory1));
	}

	@Test
	public void orderFulfillmentPkTest() {
		WeldOrder weldOrder = new WeldOrder();
		OrderFulfillmentPk pk = new OrderFulfillmentPk(weldOrder, Integer.valueOf(1), Integer.valueOf(1));
		assertNotNull(pk.getWeldOrder());
		assertNotNull(pk.getCarrierNumber());
		assertFalse(pk.equals(null));
	}

	@Test
	public void weldScheduleTest() {
		WeldSchedule schedule = new WeldSchedule();
		assertNotNull(schedule);
		assertFalse(schedule.equals(null));

	}

	@Test
	public void alarmDefinitionTest() {
		AlarmDefinition alarmDefinition = new AlarmDefinition();
		assertNotNull(alarmDefinition);
		assertFalse(alarmDefinition.equals(null));
	}

	@Test
	public void auditErrorLogTest() {
		AuditErrorLog auditErrorLog = new AuditErrorLog();
		assertNotNull(auditErrorLog);
		assertFalse(auditErrorLog.equals(null));
	}

	@Test
	public void carrierReleaseTest() {
		CarrierRelease release = new CarrierRelease();
		assertNotNull(release);
		assertFalse(release.equals(null));
	}

	@Test
	public void orderFulfillmentTest() {
		OrderFulfillment fulfillment = new OrderFulfillment();
		assertNotNull(fulfillment);
		assertFalse(fulfillment.equals(null));
	}

	@Test
	public void orderMgrTest() {
		OrderMgr orderMgr = new OrderMgr();
		assertNotNull(orderMgr);
		assertFalse(orderMgr.equals(null));
	}

	@Test
	public void modelTest() {
		Model model = new Model();
		assertNotNull(model);
		assertFalse(model.equals(null));
	}

	@Test
	public void defectTest() {
		Defect defect = new Defect();
		assertNotNull(defect);
		assertFalse(defect.equals(null));
	}

	@Test
	public void carrierMesTest() {
		CarrierMes carrierMes1 = new CarrierMes(1L);
		CarrierMes carrierMes2 = new CarrierMes(2L);
		CarrierMes carrierMes3 = new CarrierMes();
		assertFalse(carrierMes1.equals(carrierMes2));
		assertTrue(carrierMes1.equals(carrierMes1));
		assertFalse(carrierMes1.equals(null));
		assertFalse(carrierMes1.equals(carrierMes3));
		assertFalse(carrierMes3.equals(carrierMes1));
	}

	@Test
	public void alarmEventTest() {
		AlarmEvent alarmEvent = new AlarmEvent();
		assertNotNull(alarmEvent);
		assertFalse(alarmEvent.equals(null));
	}

	@Test
	public void carrierTest() {
		Carrier carrier1 = new Carrier();
		carrier1.setCurrentLocation(new Stop(1L));
		carrier1.setDestination(new Stop(1L));
		carrier1.setCarrierNumber(1);
		assertNull(carrier1.getPartProductionVolume());
		assertTrue(carrier1.isMoving());

		Carrier carrier2 = new Carrier(1L);
		Carrier carrier3 = new Carrier(2L);
		carrier3.setCarrierNumber(3);

		assertFalse(carrier2.equals(carrier3));
		assertFalse(carrier1.equals(carrier2));
		assertFalse(carrier2.isMoving());
		assertFalse(carrier1.isLoadedLike(carrier2));
		assertFalse(carrier1.isLoadedLike(carrier3));
	}

	@Test
	public void stopTest() {
		Stop stop1 = new Stop(1L);
		stop1.setName("stop");
		stop1.setStopArea(StopArea.STORE_IN_ROUTE);

		Stop stop2 = new Stop(2L);
		stop2.setName("stop");
		Stop stop3 = new Stop("stop3");

		Stop stop4 = new Stop(1240L);
		stop4.setStopArea(StopArea.STORE_IN_ROUTE);

		assertNotSame(stop1, stop2);
		assertFalse(stop1.isRowStop());

		assertNotSame(stop1, stop3);

		assertFalse(stop1.equals(new Die()));

		assertFalse(stop4.isRowStop());
	}

	@Test
	public void dieTest() {
		Die die = new Die();

		Die die1 = new Die(1L, PartProductionVolume.HIGH_VOLUME);
		Die die2 = new Die(1L, PartProductionVolume.HIGH_VOLUME);
		Die die3 = new Die(3L, PartProductionVolume.HIGH_VOLUME);

		assertTrue(die1.equals(die2));
		assertFalse(die.equals(die1));
		assertFalse(die1.equals(die3));
		assertFalse(die.equals(null));
		assertFalse(die.equals(new Stop()));

	}

	@Test
	public void weldOrderTest() {
		WeldOrder weldOrder = new WeldOrder();
		assertNotNull(weldOrder);
		assertFalse(weldOrder.equals(null));
	}

	@Test
	public void storageMessageTypeTest() {
		assertEquals(StorageMessageType.CARRIER_UPDATE, StorageMessageType.valueOf("CARRIER_UPDATE"));
		assertEquals(StorageMessageType.STATUS_UPDATE, StorageMessageType.valueOf("STATUS_UPDATE"));
	}
}
