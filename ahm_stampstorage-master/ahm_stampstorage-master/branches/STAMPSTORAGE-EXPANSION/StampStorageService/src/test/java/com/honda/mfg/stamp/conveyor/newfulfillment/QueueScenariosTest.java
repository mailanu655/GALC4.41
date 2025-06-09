package com.honda.mfg.stamp.conveyor.newfulfillment;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
import com.honda.mfg.stamp.conveyor.domain.Model;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.ParmSetting;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageImpl;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StorageStateImpl;
import com.honda.mfg.stamp.conveyor.manager.StoreInManager;
import com.honda.mfg.stamp.conveyor.manager.StoreOutManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.empty.EmptyManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreInManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_out.StoreOutManagerImpl;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 11/23/11 Time: 1:06 PM To
 * change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class QueueScenariosTest {
	WeldOrder order = null, order1 = null, order2 = null;
	OrderMgr orderMgr = null;
	Die dieL = null, emptyDie = null, dieR = null;
	Model model = null;
	Stop stop22 = null, stop23 = null;
	Stop deliveryStop5 = null, stop37 = null, stop44 = null, stop18 = null, exitStop24 = null;
	Stop stop1, stop2, stop3, stop4, stop5, stop6, stop7, stop8, stop9, stop10, stop11, stop12, stop13, stop24, stop25;

	@org.junit.Before
	public void setUp() throws Exception {
		loadStops();
		loadRows();
		loadDies();
		loadOrderMgr();
	}

	@Test
	public void testNeedMoreCarriersForDie() {
		loadTestData();

		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		assertEquals(false, helper.needMoreCarriersWithDie(order, dieL));
		assertEquals(true, helper.needMoreCarriersWithDie(order, dieR));

		order.setRightQuantity(40);
		assertEquals(false, helper.needMoreCarriersWithDie(order, dieR));

	}

	@Test
	public void testNeedMoreCarriersForDieButLessThanCycleSize() {
		loadTestData2();
		loadStopTestData();
		loadTestParmData();

		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		// WeldOrder weldOrder =
		// WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.InProcess).get(0);
		Storage storage = getStorage();

		ReleaseManager releaseManager = mock(ReleaseManagerImpl.class);
		OrderFulfillmentManager manager = new OrderFulfillmentManager(orderMgr, storage, helper, releaseManager,
				"fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");

		manager.run();
	}

	@Test
	public void testNeedMoreCarriersForDieWithDeliveringOrder() {
		loadTestData1();
		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		assertEquals(true, helper.needMoreCarriersWithDie(order, dieL));
		assertEquals(false, helper.needMoreCarriersWithDie(order, dieR));

		order.setLeftQuantity(40);
		assertEquals(false, helper.needMoreCarriersWithDie(order, dieL));

	}

	void loadStops() {
		stop22 = new Stop(1222L);
		stop22.setName("Row-22");
		stop22.setStopType(StopType.NO_ACTION);
		stop22.setStopArea(StopArea.Q_WELD_LINE_1);

		stop23 = new Stop(1223L);
		stop23.setName("Row-23");
		stop23.setStopType(StopType.NO_ACTION);
		stop23.setStopArea(StopArea.Q_WELD_LINE_1);

		deliveryStop5 = new Stop(5L);
		deliveryStop5.setName("WE1-Deelivery");
		deliveryStop5.setStopType(StopType.NO_ACTION);
		deliveryStop5.setStopArea(StopArea.WELD_LINE_1);

		stop37 = new Stop(37L);
		stop37.setName("Left Consumption");
		stop37.setStopType(StopType.NO_ACTION);
		stop37.setStopArea(StopArea.WELD_LINE_1);

		stop44 = new Stop(44L);
		stop44.setName("Left Consumption Exit");
		stop44.setStopType(StopType.NO_ACTION);
		stop44.setStopArea(StopArea.WELD_LINE_1);

		stop18 = new Stop(18L);
		stop18.setName("Right Consumption");
		stop18.setStopType(StopType.NO_ACTION);
		stop18.setStopArea(StopArea.WELD_LINE_1);

		exitStop24 = new Stop(24L);
		exitStop24.setName("Right Consumption Exit");
		exitStop24.setStopType(StopType.NO_ACTION);
		exitStop24.setStopArea(StopArea.WELD_LINE_1);

		stop22.persist();
		stop23.persist();
		deliveryStop5.persist();
		stop37.persist();
		stop44.persist();
		stop18.persist();
		exitStop24.persist();

	}

	void loadRows() {
		StorageRow row22 = new StorageRow();
		row22.setRowName("ROW22");
		row22.setStop(stop22);
		row22.setCapacity(21);
		row22.setAvailability(StopAvailability.AVAILABLE);
		row22.setStorageArea(StorageArea.Q_AREA);

		StorageRow row23 = new StorageRow();
		row23.setRowName("ROW23");
		row23.setStop(stop23);
		row23.setCapacity(21);
		row23.setAvailability(StopAvailability.AVAILABLE);
		row23.setStorageArea(StorageArea.Q_AREA);
		row22.persist();
		row23.persist();

	}

	void loadDies() {
		dieL = new Die();
		dieL.setId(1L);
		dieL.setBpmPartNumber("100A");
		dieL.setDescription("left die");
		dieL.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		dieL.persist();

		dieR = new Die();
		dieR.setId(2L);
		dieR.setBpmPartNumber("100B");
		dieR.setDescription(" right die");
		dieR.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		dieR.persist();

		model = new Model();
		model.setId(1L);
		model.setName("TestModel");
		model.setDescription("Test model");
		model.setLeftDie(dieL);
		model.setRightDie(dieR);
		model.persist();

		emptyDie = new Die();
		emptyDie.setId(999L);
		emptyDie.setBpmPartNumber("100B");
		emptyDie.setDescription("emptydie");
		emptyDie.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		emptyDie.persist();

	}

	void loadOrderMgr() {
		orderMgr = new OrderMgr();
		orderMgr.setId(1L);
		orderMgr.setLineName("line1");
		orderMgr.setMaxDeliveryCapacity(new Integer(12));
		orderMgr.setDeliveryStop(deliveryStop5);
		orderMgr.setLeftConsumptionExit(stop44);
		orderMgr.setLeftConsumptionStop(stop37);
		orderMgr.setRightConsumptionExit(exitStop24);
		orderMgr.setRightConsumptionStop(stop18);
		orderMgr.setLeftQueueStop(stop22);
		orderMgr.setRightQueueStop(stop23);
		orderMgr.persist();

	}

	public void loadTestData1() {

		// open order
		order = new WeldOrder();
		// order.setId(5L);
		order.setOrderMgr(orderMgr);
		order.setRightQuantity(70);
		order.setLeftQuantity(70);
		order.setModel(model);
		order.setOrderStatus(OrderStatus.RetrievingCarriers);
		order.setDeliveryStatus(OrderStatus.Initialized);
		order.persist();

		// manually completed order with left-over carriers in queue
		order1 = new WeldOrder();
		// order1.setId(4L);
		order1.setOrderMgr(orderMgr);
		order1.setRightQuantity(40);
		order1.setLeftQuantity(100);
		order1.setModel(model);
		order1.setOrderStatus(OrderStatus.ManuallyCompleted);
		order1.setDeliveryStatus(OrderStatus.ManuallyCompleted);
		order1.persist();

		// Open order delivering
		order2 = new WeldOrder();
		// order2.setId(3L);
		order2.setOrderMgr(orderMgr);
		order2.setRightQuantity(40);
		order2.setLeftQuantity(40);
		order2.setModel(model);
		order2.setOrderStatus(OrderStatus.AutoCompleted);
		order2.setDeliveryStatus(OrderStatus.DeliveringCarriers);
		order2.persist();

		// put 4 carriers LEFT
		for (int i = 101, j = 0; i < 105; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order2, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop22);
			thisFulfillment.setDestination(stop22);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieL);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		// put 4 carriers RIGHT
		for (int i = 201, j = 6; i < 205; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order2, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop23);
			thisFulfillment.setDestination(stop23);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieR);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		/// put 4 left carriers in queue leftover from old order
		for (int i = 105, j = 0; i < 109; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop22);
			thisFulfillment.setDestination(stop22);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieL);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		// put 9 right carriers in queue leftover from old order
		for (int i = 205, j = 6; i < 214; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop23);
			thisFulfillment.setDestination(stop23);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieR);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		// put 14x10=140 in left queue, stop 22
		for (int i = 101; i < 109; i++) {
			CarrierMes thisMes = new CarrierMes();
			thisMes.setCarrierNumber(i);
			thisMes.setCurrentLocation(stop22.getId());
			thisMes.setDestination(stop22.getId());
			thisMes.setQuantity(new Integer(10));
			thisMes.setDieNumber(new Integer(1));
			thisMes.setOriginationLocation(new Integer(0));
			thisMes.setProductionRunNumber(new Integer(111));
			thisMes.persist();
		}

		// put 8x10=80 rights in right queue, stop 23
		for (int i = 201; i < 214; i++) {
			CarrierMes thisMes = new CarrierMes();
			thisMes.setCarrierNumber(i);
			thisMes.setCurrentLocation(stop23.getId());
			thisMes.setDestination(stop23.getId());
			thisMes.setQuantity(new Integer(10));
			thisMes.setDieNumber(new Integer(2));
			thisMes.setOriginationLocation(new Integer(0));
			thisMes.setProductionRunNumber(new Integer(111));
			thisMes.persist();
		}

	}

	public void loadTestData() {

		order = new WeldOrder();
		order.setId(2L);
		order.setOrderMgr(orderMgr);
		order.setRightQuantity(70);
		order.setLeftQuantity(70);
		order.setModel(model);
		order.setOrderStatus(OrderStatus.RetrievingCarriers);
		order.setDeliveryStatus(OrderStatus.Initialized);
		order.persist();

		order1 = new WeldOrder();
		order1.setId(1L);
		order1.setOrderMgr(orderMgr);
		order1.setRightQuantity(40);
		order1.setLeftQuantity(100);
		order1.setModel(model);
		order1.setOrderStatus(OrderStatus.ManuallyCompleted);
		order1.setDeliveryStatus(OrderStatus.ManuallyCompleted);
		order1.persist();

		WeldOrder oldOrder = WeldOrder.findWeldOrder(1L); // old order
		/// put 9 left carriers in queue leftover from old order
		for (int i = 101, j = 0; i < 110; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop22);
			thisFulfillment.setDestination(stop22);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieL);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		/// put 4 right carriers in queue leftover from old order
		for (int i = 206, j = 9; i < 210; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop23);
			thisFulfillment.setDestination(stop23);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieR);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		// put 9x10=90 rights in left queue, stop 22
		for (int i = 101; i < 110; i++) {
			CarrierMes thisMes = new CarrierMes();
			thisMes.setCarrierNumber(i);
			thisMes.setCurrentLocation(stop22.getId());
			thisMes.setDestination(stop22.getId());
			thisMes.setQuantity(new Integer(10));
			thisMes.setDieNumber(new Integer(1));
			thisMes.setOriginationLocation(new Integer(0));
			thisMes.setProductionRunNumber(new Integer(111));
			thisMes.persist();
		}

		// put 4x10=40 rights in right queue, stop 23
		for (int i = 206; i < 210; i++) {
			CarrierMes thisMes = new CarrierMes();
			thisMes.setCarrierNumber(i);
			thisMes.setCurrentLocation(stop23.getId());
			thisMes.setDestination(stop23.getId());
			thisMes.setQuantity(new Integer(10));
			thisMes.setDieNumber(new Integer(2));
			thisMes.setOriginationLocation(new Integer(0));
			thisMes.setProductionRunNumber(new Integer(111));
			thisMes.persist();
		}

	}

	public void loadTestParmData() {
		ParmSetting setting = new ParmSetting();
		setting.setFieldname("fulfillmentCarrierReleaseCount");
		setting.setFieldvalue("4");
		setting.setDescription("fulfillmentCarrierReleaseCount");
		setting.setUpdatedby("test");
		setting.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

		setting.persist();

		ParmSetting setting1 = new ParmSetting();
		setting1.setFieldname("recirculationCarrierReleaseCount");
		setting1.setFieldvalue("6");
		setting1.setDescription("recirculationCarrierReleaseCount");
		setting1.setUpdatedby("test");
		setting1.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

		setting1.persist();

		ParmSetting setting2 = new ParmSetting();
		setting2.setFieldname("fulfillmentCarrierInspectionStop");
		setting2.setFieldvalue("1300");
		setting2.setDescription("fulfillmentCarrierInspectionStopt");
		setting2.setUpdatedby("test");
		setting2.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

		setting2.persist();
	}

	public void loadTestData2() {

		order = new WeldOrder();
		order.setId(2L);
		order.setOrderMgr(orderMgr);
		order.setRightQuantity(70);
		order.setLeftQuantity(70);
		order.setModel(model);
		order.setOrderStatus(OrderStatus.InProcess);
		order.setDeliveryStatus(OrderStatus.InProcess);
		order.persist();

		order1 = new WeldOrder();
		order1.setId(1L);
		order1.setOrderMgr(orderMgr);
		order1.setRightQuantity(40);
		order1.setLeftQuantity(100);
		order1.setModel(model);
		order1.setOrderStatus(OrderStatus.ManuallyCompleted);
		order1.setDeliveryStatus(OrderStatus.ManuallyCompleted);
		order1.persist();

		WeldOrder oldOrder = WeldOrder.findWeldOrder(1L); // old order
		/// put 9 left carriers in queue leftover from old order
		for (int i = 101, j = 0; i < 110; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop22);
			thisFulfillment.setDestination(stop22);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieL);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		/// put 4 right carriers in queue leftover from old order
		for (int i = 206, j = 9; i < 210; i++, j++) {
			OrderFulfillment thisFulfillment = new OrderFulfillment();
			OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1 + j / 3);
			thisFulfillment.setId(thisPk);
			thisFulfillment.setCurrentLocation(stop23);
			thisFulfillment.setDestination(stop23);
			thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			thisFulfillment.setDie(dieR);
			thisFulfillment.setQuantity(new Integer(10));
			thisFulfillment.persist();
		}

		// put 9x10=90 rights in left queue, stop 22
		for (int i = 101; i < 110; i++) {
			CarrierMes thisMes = new CarrierMes();
			thisMes.setCarrierNumber(i);
			thisMes.setCurrentLocation(stop22.getId());
			thisMes.setDestination(stop22.getId());
			thisMes.setQuantity(new Integer(10));
			thisMes.setDieNumber(new Integer(1));
			thisMes.setOriginationLocation(new Integer(0));
			thisMes.setProductionRunNumber(new Integer(111));
			thisMes.persist();
		}

		// put 4x10=40 rights in right queue, stop 23
		for (int i = 206; i < 210; i++) {
			CarrierMes thisMes = new CarrierMes();
			thisMes.setCarrierNumber(i);
			thisMes.setCurrentLocation(stop23.getId());
			thisMes.setDestination(stop23.getId());
			thisMes.setQuantity(new Integer(10));
			thisMes.setDieNumber(new Integer(2));
			thisMes.setOriginationLocation(new Integer(0));
			thisMes.setProductionRunNumber(new Integer(111));
			thisMes.persist();
		}

	}

	public Storage getStorage() {
		Die leftDie, rightDie, someDie;

		leftDie = new Die();
		leftDie.setId(1l);
		leftDie.setDescription("left_die_101");
		leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		rightDie = new Die();
		rightDie.setId(2l);
		rightDie.setDescription("right_die_102");
		rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		someDie = new Die();
		someDie.setId(3l);
		someDie.setDescription("right_die_103");
		someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -1);
		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE,
				new Integer(200), someDie);

		Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop4, stop4, CarrierStatus.SHIPPABLE,
				new Integer(202), someDie);

		Carrier carrier1 = new Carrier(3, twoDaysOld, new Integer(90), stop7, stop7, CarrierStatus.SHIPPABLE,
				new Integer(696), leftDie);
		Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE,
				new Integer(130), rightDie);
		Carrier carrier12 = new Carrier(5, twoDaysOld, new Integer(90), stop9, stop9, CarrierStatus.SHIPPABLE,
				new Integer(720), leftDie);

		Carrier carrier4 = new Carrier(6, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE,
				new Integer(109), leftDie);
		Carrier carrier41 = new Carrier(7, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE,
				new Integer(596), leftDie);
		Carrier carrier44 = new Carrier(17, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE,
				new Integer(505), leftDie);
		Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE,
				new Integer(35), leftDie);
		Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE,
				new Integer(425), leftDie);

		Carrier carrier5 = new Carrier(10, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE,
				new Integer(131), rightDie);
		Carrier carrier51 = new Carrier(11, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE,
				new Integer(203), rightDie);
		Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE,
				new Integer(126), rightDie);
		Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE,
				new Integer(465), rightDie);
		Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE,
				new Integer(605), rightDie);

		List<StorageRow> storageLanes = getStorageRows();
		storageLanes.get(31).store(carrier2);
		storageLanes.get(32).store(carrier3);
		storageLanes.get(9).store(carrier1);
		storageLanes.get(10).store(carrier11);
		storageLanes.get(11).store(carrier12);
		storageLanes.get(34).store(carrier4);
		storageLanes.get(34).store(carrier41);
		storageLanes.get(34).store(carrier44);
		storageLanes.get(33).store(carrier42);
		storageLanes.get(33).store(carrier43);
		storageLanes.get(30).store(carrier5);
		storageLanes.get(30).store(carrier51);
		storageLanes.get(29).store(carrier52);
		storageLanes.get(29).store(carrier53);
		storageLanes.get(29).store(carrier54);

		StorageState storageState = new StorageStateImpl(storageLanes);

		StorageStateContext context = new StorageStateContextMock(storageState);
		StoreOutManager storeOutManager = new StoreOutManagerImpl(context);
		StoreInManager storeInManager = new StoreInManagerImpl(context);
		EmptyManagerImpl emptyManager = new EmptyManagerImpl(context);
		Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, context);
		return storage;
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

	public void loadStopTestData() {

		stop1 = new Stop(1230L);
		stop1.setName("row");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.UNDEFINED);

		stop2 = new Stop(1231L);
		stop2.setName("row");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.UNDEFINED);

		stop3 = new Stop(1232L);
		stop3.setName("row");
		stop3.setStopType(StopType.NO_ACTION);
		stop3.setStopArea(StopArea.UNDEFINED);

		stop4 = new Stop(1233L);
		stop4.setName("row");
		stop4.setStopType(StopType.NO_ACTION);
		stop4.setStopArea(StopArea.UNDEFINED);

		stop5 = new Stop(1234L);
		stop5.setName("row");
		stop5.setStopType(StopType.NO_ACTION);
		stop5.setStopArea(StopArea.UNDEFINED);

		stop6 = new Stop(1235L);
		stop6.setName("row");
		stop6.setStopType(StopType.NO_ACTION);
		stop6.setStopArea(StopArea.UNDEFINED);

		stop7 = new Stop(1210L);
		stop7.setName("row");
		stop7.setStopType(StopType.NO_ACTION);
		stop7.setStopArea(StopArea.UNDEFINED);

		stop8 = new Stop(1211L);
		stop8.setName("row");
		stop8.setStopType(StopType.NO_ACTION);
		stop8.setStopArea(StopArea.UNDEFINED);

		stop9 = new Stop(1212L);
		stop9.setName("row");
		stop9.setStopType(StopType.NO_ACTION);
		stop9.setStopArea(StopArea.UNDEFINED);

		stop10 = new Stop(904L);
		stop10.setName("release");
		stop10.setStopType(StopType.RELEASE_CHECK);
		stop10.setStopArea(StopArea.UNDEFINED);

		stop11 = new Stop(500L);
		stop11.setName("delivery");
		stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
		stop11.setStopArea(StopArea.UNDEFINED);

		stop12 = new Stop(1300L);
		stop12.setName("release");
		stop12.setStopType(StopType.NO_ACTION);
		stop12.setStopArea(StopArea.UNDEFINED);

		stop13 = new Stop(1301L);
		stop13.setName("release");
		stop13.setStopType(StopType.RELEASE_CHECK);
		stop13.setStopArea(StopArea.UNDEFINED);

		stop24 = new Stop(1224L);
		stop24.setName("row");
		stop24.setStopType(StopType.NO_ACTION);
		stop24.setStopArea(StopArea.Q_WELD_LINE_2);

		stop25 = new Stop(1225L);
		stop25.setName("row");
		stop25.setStopType(StopType.NO_ACTION);
		stop25.setStopArea(StopArea.Q_WELD_LINE_2);

		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();
		stop5.persist();
		stop6.persist();
		stop7.persist();
		stop8.persist();
		stop9.persist();
		stop10.persist();
		stop11.persist();
		stop12.persist();
		stop13.persist();
		stop24.persist();
		stop25.persist();
	}

}
