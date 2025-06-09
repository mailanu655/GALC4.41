package com.honda.mfg.stamp.storage.web;

import static org.junit.Assert.assertNotNull;

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
import com.honda.mfg.stamp.conveyor.domain.Model;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class WeldOrderControllerTest {

	WeldOrder order = null, order1 = null, order2 = null, order3 = null;
	OrderMgr orderMgr = null;
	Die die = null, dieL = null, dieR = null, emptyDie = null;
	Model model = null;
	Stop stop22 = null, stop23 = null, stop7 = null;

	enum OrderType {
		L_ONLY, R_ONLY, LR
	};

	@Test
	public void successfullyTestWeldOrderController() {
		WeldOrderController controller = new WeldOrderController();
		assertNotNull(controller.populateCarrierFulfillmentStatuses());
		assertNotNull(controller.populateOrderStatuses());
		assertNotNull(controller.populateOrderMgrs());
		assertNotNull(controller.populateModels());
		assertNotNull(controller.populateStops());
		// assertNotNull(controller.populateWeldOrders());
	}

	@Test
	public void testIsNextOrderForDelivery() {
		loadTestQueueData();
		WeldOrderController controller = new WeldOrderController();

		boolean isNext = controller.isNextForDelivery(order2);
		junit.framework.Assert.assertEquals(true, isNext);

		isNext = controller.isNextForDelivery(order3);
		junit.framework.Assert.assertEquals(false, isNext);
	}

	@Test
	public void testIsNextRightOnlyOrderForDelivery() {
		loadTestQueueData1();
		WeldOrderController controller = new WeldOrderController();

		boolean isNext = controller.isNextForDelivery(order2);
		junit.framework.Assert.assertEquals(true, isNext);
	}

	public void loadStopData() {

		stop22 = new Stop(1222L);
		stop22.setName("stop22");
		stop22.setStopType(StopType.NO_ACTION);
		stop22.setStopArea(StopArea.Q_WELD_LINE_1);

		stop23 = new Stop(1223L);
		stop23.setName("stop23");
		stop23.setStopType(StopType.NO_ACTION);
		stop23.setStopArea(StopArea.Q_WELD_LINE_2);

		stop7 = new Stop(700L);
		stop7.setName("stop7");
		stop7.setStopType(StopType.NO_ACTION);
		stop7.setStopArea(StopArea.UNDEFINED);

		stop22.persist();
		stop23.persist();
		stop7.persist();

	}

	public void loadModel() {
		Die dieL = new Die();
		dieL.setId(1L);
		dieL.setBpmPartNumber("100L");
		dieL.setDescription("die");
		dieL.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		dieL.persist();

		Die dieR = new Die();
		dieR.setId(2L);
		dieR.setBpmPartNumber("100R");
		dieR.setDescription("die");
		dieR.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		dieR.persist();

		Model model = new Model();
		model.setName("model");
		model.setLeftDie(dieL);
		model.setRightDie(dieR);
		model.setDescription("model");

		model.persist();

	}

	public void loadOrderMgr() {
		orderMgr = new OrderMgr();
		orderMgr.setId(1L);
		orderMgr.setLineName("line1");
		orderMgr.setMaxDeliveryCapacity(new Integer(12));
		orderMgr.setDeliveryStop(stop7);
		orderMgr.setLeftConsumptionExit(stop7);
		orderMgr.setLeftConsumptionStop(stop7);
		orderMgr.setRightConsumptionExit(stop7);
		orderMgr.setRightConsumptionStop(stop7);
		orderMgr.setLeftQueueStop(stop22);
		orderMgr.setRightQueueStop(stop23);

		orderMgr.persist();
	}

	public WeldOrder createOrder(OrderStatus orderStatus, OrderStatus deliveryStatus, OrderType oType) {
		WeldOrder wo = new WeldOrder();
		// wo.setId(orderId);
		wo.setOrderMgr(orderMgr);
		wo.setModel(model);

		if (oType == OrderType.L_ONLY)
			wo.setRightQuantity(0);
		else
			wo.setRightQuantity(100);
		if (oType == OrderType.R_ONLY)
			wo.setLeftQuantity(0);
		else
			wo.setLeftQuantity(100);

		wo.setOrderStatus(orderStatus);
		wo.setDeliveryStatus(deliveryStatus);
		wo.persist();
		return wo;
	}

	public void loadTestQueueData() {

		loadStopData();
		loadModel();
		loadOrderMgr();

		order1 = createOrder(OrderStatus.AutoCompleted, OrderStatus.DeliveringCarriers, OrderType.LR);
		order2 = createOrder(OrderStatus.AutoCompleted, OrderStatus.Initialized, OrderType.LR);
		order3 = createOrder(OrderStatus.AutoCompleted, OrderStatus.Initialized, OrderType.R_ONLY);

		for (int i = 0; i < 10; i++) {
			// create left fulfillments with carrier numbers 100 to 109
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order1, 100 + i, 1 + i / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop22);
			fulfillment1.setDestination(stop22);
			fulfillment1.setQuantity(10);
			fulfillment1.setDie(dieL);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 100 to 109
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(100 + i);
			carriermes1.setCurrentLocation(stop22.getId());
			carriermes1.setDestination(stop22.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(1));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();

		}

		for (int i = 0; i < 10; i++) {
			// create right fulfillments with carrier numbers 200 to 209
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order1, 200 + i, 1 + i / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop23);
			fulfillment1.setDestination(stop23);
			fulfillment1.setDie(dieR);
			fulfillment1.setQuantity(10);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 200 to 209
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(200 + i);
			carriermes1.setCurrentLocation(stop23.getId());
			carriermes1.setDestination(stop23.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(2));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();

		}

		for (int i = 10; i < 15; i++) {
			// create left fulfillments with carrier numbers 110 to 114
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order2, 100 + i, 1 + (i - 10) / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop22);
			fulfillment1.setDestination(stop22);
			fulfillment1.setQuantity(10);
			fulfillment1.setDie(dieL);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 110 to 114
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(100 + i);
			carriermes1.setCurrentLocation(stop22.getId());
			carriermes1.setDestination(stop22.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(1));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();

		}

		for (int i = 10; i < 15; i++) {
			// create right fulfillments with carrier numbers 210 to 214
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order2, 200 + i, 1 + (i - 10) / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop23);
			fulfillment1.setDestination(stop23);
			fulfillment1.setDie(dieR);
			fulfillment1.setQuantity(10);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 210 to 214
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(200 + i);
			carriermes1.setCurrentLocation(stop23.getId());
			carriermes1.setDestination(stop23.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(2));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();

		}

		for (int i = 15; i < 20; i++) {
			// create right fulfillments with carrier numbers 215 to 219
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order3, 200 + i, 1 + (i - 15) / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop23);
			fulfillment1.setDestination(stop23);
			fulfillment1.setDie(dieR);
			fulfillment1.setQuantity(10);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 215 to 219
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(200 + i);
			carriermes1.setCurrentLocation(stop23.getId());
			carriermes1.setDestination(stop23.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(2));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();
		}
	}

	public void loadTestQueueData1() {

		loadStopData();
		loadModel();
		loadOrderMgr();

		order1 = createOrder(OrderStatus.AutoCompleted, OrderStatus.DeliveringCarriers, OrderType.LR);
		order2 = createOrder(OrderStatus.AutoCompleted, OrderStatus.Initialized, OrderType.R_ONLY);

		for (int i = 0; i < 10; i++) {
			// create left fulfillments with carrier numbers 100 to 109
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order1, 100 + i, 1 + i / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop22);
			fulfillment1.setDestination(stop22);
			fulfillment1.setQuantity(10);
			fulfillment1.setDie(dieL);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 100 to 109
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(100 + i);
			carriermes1.setCurrentLocation(stop22.getId());
			carriermes1.setDestination(stop22.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(1));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();

		}

		for (int i = 0; i < 10; i++) {
			// create right fulfillments with carrier numbers 200 to 209
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order1, 200 + i, 1 + i / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop23);
			fulfillment1.setDestination(stop23);
			fulfillment1.setDie(dieR);
			fulfillment1.setQuantity(10);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 200 to 209
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(200 + i);
			carriermes1.setCurrentLocation(stop23.getId());
			carriermes1.setDestination(stop23.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(2));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();

		}

		for (int i = 15; i < 20; i++) {
			// create right fulfillments with carrier numbers 215 to 219
			OrderFulfillment fulfillment1 = new OrderFulfillment();
			OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order2, 200 + i, 1 + (i - 15) / 4);
			fulfillment1.setId(pk1);
			fulfillment1.setCurrentLocation(stop23);
			fulfillment1.setDestination(stop23);
			fulfillment1.setDie(dieR);
			fulfillment1.setQuantity(10);
			fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
			fulfillment1.persist();

			// create corresponding CarrierMes with carrier number 215 to 219
			CarrierMes carriermes1 = new CarrierMes();
			carriermes1.setCarrierNumber(200 + i);
			carriermes1.setCurrentLocation(stop23.getId());
			carriermes1.setDestination(stop23.getId());
			carriermes1.setQuantity(new Integer(10));
			carriermes1.setDieNumber(new Integer(2));
			carriermes1.setOriginationLocation(new Integer(0));
			carriermes1.setProductionRunNumber(new Integer(111));
			carriermes1.persist();
		}
	}

	public void loadTestData() {

		Stop stop1 = new Stop(1300L);
		stop1.setName("stop1");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.UNDEFINED);

		Stop stop2 = new Stop(1301L);
		stop2.setName("stop2");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.UNDEFINED);

		Stop stop3 = new Stop(700L);
		stop3.setName("stop3");
		stop3.setStopType(StopType.NO_ACTION);
		stop3.setStopArea(StopArea.UNDEFINED);

		Stop stop4 = new Stop(1201L);
		stop4.setName("row");
		stop4.setStopType(StopType.NO_ACTION);
		stop4.setStopArea(StopArea.UNDEFINED);

		Stop stop5 = new Stop(708L);
		stop5.setName("kd");
		stop5.setStopType(StopType.NO_ACTION);
		stop5.setStopArea(StopArea.KD_LINE);

		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();
		stop5.persist();

		die = new Die();
		die.setId(1L);
		die.setBpmPartNumber("100A");
		die.setDescription("die");
		die.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		die.persist();

		emptyDie = new Die();
		emptyDie.setId(999L);
		emptyDie.setBpmPartNumber("100B");
		emptyDie.setDescription("emptydie");
		emptyDie.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		emptyDie.persist();

		orderMgr = new OrderMgr();
		orderMgr.setId(1L);
		orderMgr.setLineName("line1");
		orderMgr.setMaxDeliveryCapacity(new Integer(12));
		orderMgr.setDeliveryStop(stop3);
		orderMgr.setLeftConsumptionExit(stop3);
		orderMgr.setLeftConsumptionStop(stop3);
		orderMgr.setRightConsumptionExit(stop3);
		orderMgr.setRightConsumptionStop(stop3);
		orderMgr.persist();

		order = new WeldOrder();
		order.setId(1L);
		order.setOrderMgr(orderMgr);
		order.setRightQuantity(100);
		order.setLeftQuantity(100);
		order.setOrderStatus(OrderStatus.RetrievingCarriers);
		order.persist();

		order1 = new WeldOrder();
		order1.setId(2L);
		order1.setOrderMgr(orderMgr);
		order1.setRightQuantity(100);
		order1.setLeftQuantity(100);
		order1.setOrderStatus(OrderStatus.ManuallyCompleted);
		order1.persist();

		WeldOrder weldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
		OrderFulfillment fulfillment1 = new OrderFulfillment();
		OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101, 1);
		fulfillment1.setId(pk1);
		fulfillment1.setCurrentLocation(stop4);
		fulfillment1.setDestination(stop4);
		// fulfillment1.setReleaseCycle(1);
		fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
		fulfillment1.persist();

		OrderFulfillment fulfillment2 = new OrderFulfillment();
		OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 103, 1);
		fulfillment2.setId(pk2);
		fulfillment2.setCurrentLocation(stop4);
		fulfillment2.setDestination(stop4);
		// fulfillment2.setReleaseCycle(1);
		fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.SELECTED);
		fulfillment2.setDie(die);
		fulfillment2.setQuantity(new Integer(10));
		fulfillment2.persist();

		OrderFulfillment fulfillment3 = new OrderFulfillment();
		OrderFulfillmentPk pk3 = new OrderFulfillmentPk(weldOrder, 104, 1);
		fulfillment3.setId(pk3);
		fulfillment3.setCurrentLocation(stop4);
		fulfillment3.setDestination(stop4);
		// fulfillment3.setReleaseCycle(1);
		fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
		fulfillment3.setDie(die);
		fulfillment3.setQuantity(new Integer(10));
		fulfillment3.persist();

		OrderFulfillment fulfillment4 = new OrderFulfillment();
		OrderFulfillmentPk pk4 = new OrderFulfillmentPk(weldOrder, 105, 1);
		fulfillment4.setId(pk4);
		fulfillment4.setCurrentLocation(stop4);
		fulfillment4.setDestination(stop4);
		// fulfillment4.setReleaseCycle(1);
		fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
		fulfillment4.setDie(die);
		fulfillment4.setQuantity(new Integer(10));
		fulfillment4.persist();

		CarrierMes carriermes = new CarrierMes();
		carriermes.setCarrierNumber(101);
		carriermes.setCurrentLocation(stop4.getId());
		carriermes.setDestination(stop4.getId());
		carriermes.persist();

		CarrierMes carriermes2 = new CarrierMes();
		carriermes2.setCarrierNumber(103);
		carriermes2.setCurrentLocation(stop4.getId());
		carriermes2.setDestination(stop4.getId());
		carriermes2.setQuantity(new Integer(10));
		carriermes2.setDieNumber(new Integer(1));
		carriermes2.persist();

		CarrierMes carriermes1 = new CarrierMes();
		carriermes1.setCarrierNumber(102);
		carriermes1.setCurrentLocation(stop5.getId());
		carriermes1.setDestination(stop5.getId());
		carriermes1.setQuantity(new Integer(0));
		carriermes1.setDieNumber(new Integer(1));
		carriermes1.setOriginationLocation(new Integer(0));
		carriermes1.setProductionRunNumber(new Integer(111));
		carriermes1.persist();

		CarrierMes carriermes3 = new CarrierMes();
		carriermes3.setCarrierNumber(104);
		carriermes3.setCurrentLocation(stop5.getId());
		carriermes3.setDestination(stop5.getId());
		carriermes3.setQuantity(new Integer(0));
		carriermes3.setDieNumber(new Integer(1));
		carriermes3.setOriginationLocation(new Integer(0));
		carriermes3.setProductionRunNumber(new Integer(111));
		carriermes3.persist();

		CarrierMes carriermes4 = new CarrierMes();
		carriermes4.setCarrierNumber(105);
		carriermes4.setCurrentLocation(stop5.getId());
		carriermes4.setDestination(stop5.getId());
		carriermes4.setQuantity(new Integer(0));
		carriermes4.setDieNumber(new Integer(1));
		carriermes4.setOriginationLocation(new Integer(0));
		carriermes4.setProductionRunNumber(new Integer(111));
		carriermes4.persist();

		CarrierRelease carrierRelease = new CarrierRelease();
		carrierRelease.setId(104l);
		carrierRelease.setDestination(stop4);
		carrierRelease.setSource("user");
		carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease.persist();
	}

}
