package com.honda.mfg.stamp.conveyor.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * User: VCC30690 Date: 9/9/11
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class EmptyCarriersServiceHelperImplTest {

	OrderMgr orderMgr, orderMgr2;

	@Test
	public void successfullyGetCarrierCountForOldWeldLineEmptyArea() {
		loadCarrierMesTable();
		loadStopTable();
		loadDieTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(new Integer(3), helper.getCarrierCountForOldWeldLineEmptyArea());

	}

	@Test
	public void successfullyGetCarrierCountForBAreaEmptyArea() {
		loadCarrierMesTable();
		loadStopTable();
		loadDieTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(new Integer(1), helper.getCarrierCountForBAreaEmptyStorage());

	}

	@Test
	public void successfullyGetCarrierCountForOldWeldLineEmptyArea2() {
		// loadCarrierMesTable();
		loadStopTable();
		loadDieTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(new Integer(0), helper.getCarrierCountForOldWeldLineEmptyArea());

	}

	@Test
	public void successfullyGetCarrierCountForEmptyArea() {
		loadCarrierMesTable();
		loadStopTable();
		loadDieTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(new Integer(1), helper.getCarrierCountForEmptyArea());
	}

	@Test
	public void successfullyGetCarrierCountForEmptyArea2() {
		// loadCarrierMesTable();
		loadStopTable();
		loadDieTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(new Integer(0), helper.getCarrierCountForEmptyArea());
	}

	@Test
	public void successFullyGetCarriersToMoveToEmptyStorageArea() {
		loadCarrierMesTable();
		loadStopTable();
		loadDieTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(1, helper.getCarriersToMoveToEmptyStorageArea(3).size());

		CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(4);
		if (carrierMes != null) {
			carrierMes.remove();
		}
		// assertEquals(1, helper.getCarriersToMoveToEmptyStorageArea(3).size());
	}

	@Test
	public void successfullyReturnActiveOrderExistsForOrderMgr() {
		loadStopsAndOrderMgrTable();
		loadWeldOrderTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(true, helper.activeOrderExistsForOrderMgr(orderMgr));
		assertEquals(false, helper.activeOrderExistsForOrderMgr(orderMgr2));
	}

	@Test
	public void successfullyGetStopsByConveyorId() {

		loadDieTable();
		loadStopTable();

		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();

		assertNotNull(helper.getOldWeldLineEmptyCarrierDeliveryStop());
		assertNotNull(helper.getEmptyCarrierDeliveryStop());
		assertNotNull(helper.getBAreaEmptyCarrierDeliveryStop());

	}

	@Test
	public void successfullyGetStopsByConveyorId2() {

		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();

		assertNull(helper.getOldWeldLineEmptyCarrierDeliveryStop());
		assertNull(helper.getEmptyCarrierDeliveryStop());
		assertNull(helper.getBAreaEmptyCarrierDeliveryStop());
	}

	// @Test
	public void successfullyReturnAnyOrderRetrievingOrDeliveringCarriers() {
		loadStopsAndOrderMgrTable();
		loadWeldOrderTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(true, helper.anyOrderInProcess());
	}

	// @Test
	public void successfullyReturnAnyOrderRetrievingOrDeliveringCarriers2() {
		loadStopsAndOrderMgrTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(false, helper.anyOrderInProcess());
	}

	@Test
	public void successfullyFindIfThePreviousSetReleasedFromRows() {
		loadStopTable();
		loadWeldOrderTable();
		loadCarrierMesTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(true, helper.anyCarrierSetToReleaseFromRows());
	}

	@Test
	public void successfullyGetCarrier() {
		loadCarrierMesTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertNotNull(helper.getCarrier(1));
	}

	@Test
	public void successfullyFindAnyCarrierSetToReleaseFromRowsInReleaseQueue() {
		loadCarrierReleaseAndMesTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(true, helper.anyCarrierSetToReleaseFromRows());
	}

	@Test
	public void successfullyFindAnyCarrierSetToReleaseFromRows2() {
		// loadCarrierReleaseAndMesTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(false, helper.anyCarrierSetToReleaseFromRows());
	}

	@Test
	public void successfullyFindAnyCarrierSetToReleaseFromRows() {
		loadCarrierMesTable();
		loadStopTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(true, helper.anyCarrierSetToReleaseFromRows());
	}

	@Test
	public void successfullyReturnCanReleaseCarrier() {
		loadStopTable();
		loadCarrierMesTable();
		EmptyCarriersServiceHelper helper = new EmptyCarriersServiceHelperImpl();
		assertEquals(true, helper.canReleaseCarrier(3, Stop.findStop(1202l)));
		assertEquals(false, helper.canReleaseCarrier(2, Stop.findStop(1202l)));
		assertEquals(false, helper.canReleaseCarrier(5, Stop.findStop(1202l)));
	}

	void loadDieTable() {
		Die die = new Die();
		// die.setDieNumber(999);
		die.setId(999L);
		die.setDescription("empty die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die.persist();
	}

	void loadStopTable() {
		Stop stop = new Stop();
		stop.setId(5200L);
		stop.setName("ST52");
		stop.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
		stop.setStopArea(StopArea.OLD_WELD_LINE);

		Stop stop1 = new Stop();
		stop1.setId(13400L);
		stop1.setName("ST134");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.EMPTY_AREA);

		Stop stop2 = new Stop();
		stop2.setId(2003L);
		stop2.setName("ST20-03");
		stop2.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
		stop2.setStopArea(StopArea.EMPTY_AREA);

		Stop stop3 = new Stop();
		stop3.setId(702L);
		stop3.setName("ST7-02");
		stop3.setStopType(StopType.NO_ACTION);
		stop3.setStopArea(StopArea.KD_LINE);

		Stop stop4 = new Stop();
		stop4.setId(800L);
		stop4.setName("ST8");
		stop4.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
		stop4.setStopArea(StopArea.KD_LINE);

		Stop stop5 = new Stop();
		stop5.setId(700L);
		stop5.setName("ST7");
		stop5.setStopType(StopType.FULL_CARRIER_DELIVERY);
		stop5.setStopArea(StopArea.KD_LINE);

		Stop stop6 = new Stop();
		stop6.setId(1202L);
		stop6.setName("ST12-2");
		stop6.setStopType(StopType.NO_ACTION);
		stop6.setStopArea(StopArea.ROW);

		Stop stop7 = new Stop();
		stop7.setId(12600L);
		stop7.setName("ST126");
		stop7.setStopType(StopType.NO_ACTION);
		stop7.setStopArea(StopArea.EMPTY_AREA);

		Stop stop8 = new Stop();
		stop8.setId(3000L);
		stop8.setName("ST30");
		stop8.setStopType(StopType.NO_ACTION);
		stop8.setStopArea(StopArea.EMPTY_AREA);

		Stop stop9 = new Stop();
		stop9.setId(3004L);
		stop9.setName("ST30-4");
		stop9.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
		stop9.setStopArea(StopArea.B_PRESS);

		stop.persist();
		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();
		stop5.persist();
		stop6.persist();
		stop7.persist();
		stop8.persist();
		stop9.persist();
	}

	void loadCarrierMesTable() {
		CarrierMes carrier = new CarrierMes();
		carrier.setCarrierNumber(1);
		carrier.setDieNumber(999);
		carrier.setQuantity(0);
		carrier.setCurrentLocation(3000L);
		carrier.setDestination(5200L);

		CarrierMes carrier1 = new CarrierMes();
		carrier1.setCarrierNumber(2);
		carrier1.setDieNumber(999);
		carrier1.setQuantity(0);
		carrier1.setCurrentLocation(12600L);
		carrier1.setDestination(2003L);

		CarrierMes carrier2 = new CarrierMes();
		carrier2.setCarrierNumber(2);
		carrier2.setDieNumber(999);
		carrier2.setQuantity(0);
		carrier2.setCurrentLocation(702L);
		carrier2.setDestination(800L);

		CarrierMes carrier3 = new CarrierMes();
		carrier3.setCarrierNumber(3);
		carrier3.setDieNumber(999);
		carrier3.setQuantity(0);
		carrier3.setCurrentLocation(1202L);
		carrier3.setDestination(5200L);

		CarrierMes carrier4 = new CarrierMes();
		carrier4.setCarrierNumber(4);
		carrier4.setDieNumber(999);
		carrier4.setQuantity(0);
		carrier4.setCurrentLocation(5200L);
		carrier4.setDestination(5200L);

		CarrierMes carrier5 = new CarrierMes();
		carrier5.setCarrierNumber(5);
		carrier5.setDieNumber(999);
		carrier5.setQuantity(0);
		carrier5.setCurrentLocation(3004L);
		carrier5.setDestination(3004L);

		carrier.persist();
		carrier1.persist();
		carrier2.persist();
		carrier3.persist();
		carrier4.persist();
		carrier5.persist();
	}

	void loadWeldOrderTable() {
		WeldOrder order = new WeldOrder();

		order.setModel(null);
		order.setOrderMgr(orderMgr);
		order.setOrderStatus(OrderStatus.RetrievingCarriers);
		order.setDeliveryStatus(OrderStatus.Initialized);
		order.setLeftQuantity(new Integer(10));
		order.setRightQuantity(new Integer(10));
		order.setOrderSequence(new Integer(999));
		order.setCreatedBy("user");
		order.setCreatedDate(new Timestamp(System.currentTimeMillis()));

		order.persist();
	}

	void loadStopsAndOrderMgrTable() {

		Stop weldLineDeliveryStop = new Stop();
		weldLineDeliveryStop.setName("ST5");
		weldLineDeliveryStop.setId(700L);
		weldLineDeliveryStop.setStopType(StopType.FULL_CARRIER_DELIVERY);
		weldLineDeliveryStop.setStopArea(StopArea.KD_LINE);

		Stop weldLineRobotStopLeftEntry = new Stop();
		weldLineRobotStopLeftEntry.setName("ST44");
		weldLineRobotStopLeftEntry.setId(4400L);
		weldLineRobotStopLeftEntry.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
		weldLineRobotStopLeftEntry.setStopArea(StopArea.WELD_LINE_1);

		Stop weldLineRobotStopRightEntry = new Stop();
		weldLineRobotStopRightEntry.setName("ST24");
		weldLineRobotStopRightEntry.setId(2400L);
		weldLineRobotStopRightEntry.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
		weldLineRobotStopRightEntry.setStopArea(StopArea.WELD_LINE_1);

		Stop weldLineRobotStopLeftExit = new Stop();
		weldLineRobotStopLeftExit.setName("ST54");
		weldLineRobotStopLeftExit.setId(5400L);
		weldLineRobotStopLeftExit.setStopType(StopType.LEFT_CONSUMED_CARRIER_EXIT);
		weldLineRobotStopLeftExit.setStopArea(StopArea.WELD_LINE_1);

		Stop weldLineRobotStopRightExit = new Stop();
		weldLineRobotStopRightExit.setName("ST24");
		weldLineRobotStopRightExit.setId(2500L);
		weldLineRobotStopRightExit.setStopType(StopType.RIGHT_CONSUMED_CARRIER_EXIT);
		weldLineRobotStopRightExit.setStopArea(StopArea.WELD_LINE_1);

		weldLineDeliveryStop.persist();
		weldLineRobotStopLeftEntry.persist();
		weldLineRobotStopLeftExit.persist();
		weldLineRobotStopRightEntry.persist();
		weldLineRobotStopRightExit.persist();

		orderMgr = new OrderMgr();
		orderMgr.setLineName("KDLine");
		orderMgr.setMaxDeliveryCapacity(new Integer(12));
		orderMgr.setDeliveryStop(weldLineDeliveryStop);
		orderMgr.setLeftConsumptionStop(weldLineRobotStopLeftEntry);
		orderMgr.setLeftConsumptionExit(weldLineRobotStopLeftExit);
		orderMgr.setRightConsumptionStop(weldLineRobotStopRightEntry);
		orderMgr.setRightConsumptionExit(weldLineRobotStopLeftExit);

		orderMgr.persist();

		orderMgr2 = new OrderMgr();
		orderMgr2.setLineName("WELD_Line1");
		orderMgr2.setMaxDeliveryCapacity(new Integer(30));
		orderMgr2.setDeliveryStop(weldLineDeliveryStop);
		orderMgr2.setLeftConsumptionStop(weldLineRobotStopLeftEntry);
		orderMgr2.setLeftConsumptionExit(weldLineRobotStopLeftExit);
		orderMgr2.setRightConsumptionStop(weldLineRobotStopRightEntry);
		orderMgr2.setRightConsumptionExit(weldLineRobotStopLeftExit);

		orderMgr2.persist();
	}

	void loadCarrierReleaseAndMesTable() {
		Stop stop = new Stop();
		stop.setId(5200L);
		stop.setName("ST52");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.OLD_WELD_LINE);

		stop.persist();

		Stop stop1 = new Stop();
		stop1.setId(1201L);
		stop1.setName("ST12-1");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.ROW);

		stop1.persist();

		CarrierMes carrier = new CarrierMes();
		carrier.setCarrierNumber(1);
		carrier.setDieNumber(999);
		carrier.setQuantity(0);
		carrier.setCurrentLocation(1201L);
		carrier.setDestination(1201L);

		carrier.persist();

		CarrierRelease release = new CarrierRelease();
		release.setId(1l);
		release.setCurrentLocation(stop1);
		release.setDestination(stop);
		release.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		release.setSource("user");

		release.persist();
	}
}
