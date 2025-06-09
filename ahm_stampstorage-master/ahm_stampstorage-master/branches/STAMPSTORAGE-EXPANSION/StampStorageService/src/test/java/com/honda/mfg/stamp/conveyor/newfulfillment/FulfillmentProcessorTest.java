package com.honda.mfg.stamp.conveyor.newfulfillment;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.sql.Timestamp;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Model;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.ParmSetting;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.processor.AbstractTestBase;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 11/30/11 Time: 10:02 AM To
 * change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class FulfillmentProcessorTest extends AbstractTestBase {
	String carrier = null;

	@Test
	public void successfullyPublishFulfillmentProcessorCarrierShippable() {
		loadTestData();
		loadTestParmData();
		AnnotationProcessor.process(this);
		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		carrierMessage.setCarrierNumber("101");
		carrierMessage.setCurrentLocation("903");
		carrierMessage.setDestination("1222");
		carrierMessage.setDieNumber("1");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");
		EventBus.publish(carrierMessage);
		assertNotNull(processor.getCarrier());
		// 2013-06-14:VB:following line will be removed because when a carrier is
		// QUEUED,
		// a carrier update is not published by the fulfillment processor anymore for
		// delivery destination.
		// assertNotNull(carrier);

		carrier = null;
		CarrierStatusMessage carrierMessage2 = new CarrierStatusMessage();
		carrierMessage2.setCarrierNumber("102");
		carrierMessage2.setCurrentLocation("1222");
		carrierMessage2.setDestination("1222");
		carrierMessage2.setDieNumber("2");
		carrierMessage2.setQuantity("10");
		carrierMessage2.setStatus("0");
		carrierMessage2.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage2.setProductionRunNo("100");
		carrierMessage2.setOriginationLocation("0");
		carrierMessage2.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage2.setTagID("10");
		carrierMessage2.setBuffer("1");
		EventBus.publish(carrierMessage2);
		assertNotNull(processor.getCarrier());
		// 2013-06-14:VB:following line will be removed because when a carrier is
		// QUEUED,
		// a carrier update is not published by the fulfillment processor anymore for
		// delivery destination.
		// assertNotNull(carrier);

		carrier = null;
		CarrierStatusMessage carrierMessage3 = new CarrierStatusMessage();
		carrierMessage3.setCarrierNumber("103");
		carrierMessage3.setCurrentLocation("1222");
		carrierMessage3.setDestination("1222");
		carrierMessage3.setDieNumber("999");
		carrierMessage3.setQuantity("0");
		carrierMessage3.setStatus("0");
		carrierMessage3.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage3.setProductionRunNo("100");
		carrierMessage3.setOriginationLocation("0");
		carrierMessage3.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage3.setTagID("10");
		carrierMessage3.setBuffer("1");
		EventBus.publish(carrierMessage3);
		assertNotNull(processor.getCarrier());
		assertNull(carrier);

		AnnotationProcessor.unprocess(this);

	}

	@Test
	public void successfullyPublishFulfillmentProcessorCarrierInspectionRequired() {
		loadTestData1();
		loadTestParmData();
		AnnotationProcessor.process(this);
		carrier = null;
		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		carrierMessage.setCarrierNumber("103");
		carrierMessage.setCurrentLocation("1300");
		carrierMessage.setDestination("1300");
		carrierMessage.setDieNumber("1");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("2");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");
		EventBus.publish(carrierMessage);
		assertNotNull(processor.getCarrier());
		assertNull(carrier);
		carrierMessage.setCarrierNumber("103");
		carrierMessage.setCurrentLocation("1300");
		carrierMessage.setDestination("1300");
		carrierMessage.setDieNumber("1");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");
		EventBus.publish(carrierMessage);
		assertNotNull(processor.getCarrier());
		assertNotNull(carrier);
		AnnotationProcessor.unprocess(this);

	}

	@Test
	public void successfullyPublishFulfillmentProcessorWithDeliveryDestination() {
		loadTestData();
		loadTestParmData();
		AnnotationProcessor.process(this);
		carrier = null;
		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		carrierMessage.setCarrierNumber("103");
		carrierMessage.setCurrentLocation("1300");
		carrierMessage.setDestination("700");
		carrierMessage.setDieNumber("1");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");
		EventBus.publish(carrierMessage);
		assertNotNull(processor.getCarrier());
		assertNull(carrier);
		AnnotationProcessor.unprocess(this);

	}

	@Test
	public void successfullyPublishFulfillmentProcessorNoFulfillmentRecordExists() {
		loadTestData();
		loadTestParmData();
		AnnotationProcessor.process(this);
		carrier = null;
		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		carrierMessage.setCarrierNumber("104");
		carrierMessage.setCurrentLocation("1222");
		carrierMessage.setDestination("700");
		carrierMessage.setDieNumber("1");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");
		EventBus.publish(carrierMessage);
		assertNotNull(processor.getCarrier());
		assertNull(carrier);
		AnnotationProcessor.unprocess(this);

	}

	@Test
	public void successfullyPublishCarrierUpdateForSameCarrierInDifferentReleaseCycle() {
		loadTestData1();
		loadTestParmData();
		AnnotationProcessor.process(this);
		carrier = null;
		NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
		FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		carrierMessage.setCarrierNumber("102");
		carrierMessage.setCurrentLocation("1222");
		carrierMessage.setDestination("1222");
		carrierMessage.setDieNumber("2");
		carrierMessage.setQuantity("10");
		carrierMessage.setStatus("0");
		carrierMessage.setStampingProductionRunTimestamp("2011-06-29 12:10:00.000");
		carrierMessage.setProductionRunNo("100");
		carrierMessage.setOriginationLocation("0");
		carrierMessage.setUpdateDate("2011-06-29 12:10:00.000");
		carrierMessage.setTagID("10");
		carrierMessage.setBuffer("1");
		EventBus.publish(carrierMessage);
		assertNotNull(processor.getCarrier());
		// 2013-06-14:VB:following line will be removed because when a carrier is
		// QUEUED,
		// a carrier update is not published by the fulfillment processor anymore for
		// delivery destination.
		// assertNotNull(carrier);
		AnnotationProcessor.unprocess(this);

	}

	@EventSubscriber(eventClass = CarrierUpdateMessage.class)
	public void catchEvent(CarrierUpdateMessage message) {
		carrier = message.getCarrierNumber();
	}

	public void loadTestData() {

		Stop stop = new Stop(1300L);
		stop.setName("stop");
		stop.setStopType(StopType.RELEASE_CHECK);
		stop.setStopArea(StopArea.Q_WELD_LINE_1);

		Stop stop1 = new Stop(903L);
		stop1.setName("stop1");
		stop1.setStopType(StopType.RELEASE_CHECK);
		stop1.setStopArea(StopArea.Q_WELD_LINE_2);

		Stop stop2 = new Stop(1223L);
		stop2.setName("stop2");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.Q_WELD_LINE_1);

		Stop stop3 = new Stop(700L);
		stop3.setName("stop3");
		stop3.setStopType(StopType.FULL_CARRIER_DELIVERY);
		stop3.setStopArea(StopArea.UNDEFINED);

		Stop stop4 = new Stop(1201L);
		stop4.setName("row");
		stop4.setStopType(StopType.NO_ACTION);
		stop4.setStopArea(StopArea.UNDEFINED);

		stop.persist();
		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();

		Die die = new Die();
		die.setId(1L);
		die.setBpmPartNumber("100A");
		die.setDescription("die");
		die.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		die.persist();

		Die die2 = new Die();
		die2.setId(2L);
		die2.setBpmPartNumber("100A");
		die2.setDescription("die");
		die2.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		die2.persist();

		Die die3 = new Die();
		die3.setId(999L);
		die3.setBpmPartNumber("999");
		die3.setDescription("empty die");
		die3.setPartProductionVolume(PartProductionVolume.LOW_VOLUME);
		die3.persist();

		Model model = new Model();
		model.setDescription("model");
		model.setId(1L);
		model.setName("model");
		model.setLeftDie(die);
		model.setRightDie(die2);
		model.persist();

		OrderMgr orderMgr = new OrderMgr();
		orderMgr.setId(1L);
		orderMgr.setLineName("line1");
		orderMgr.setMaxDeliveryCapacity(new Integer(12));
		orderMgr.setDeliveryStop(stop3);
		orderMgr.setLeftConsumptionExit(stop3);
		orderMgr.setLeftConsumptionStop(stop3);
		orderMgr.setRightConsumptionExit(stop3);
		orderMgr.setRightConsumptionStop(stop3);
		orderMgr.persist();

		WeldOrder order = new WeldOrder();
		order.setId(1L);
		order.setOrderMgr(orderMgr);
		order.setRightQuantity(100);
		order.setLeftQuantity(100);
		order.setModel(model);
		order.setOrderStatus(OrderStatus.RetrievingCarriers);
		order.setDeliveryStatus(OrderStatus.Initialized);
		order.persist();

		WeldOrder weldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
		OrderFulfillment fulfillment1 = new OrderFulfillment();
		OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101, 1);
		fulfillment1.setId(pk1);
		fulfillment1.setDestination(stop4);
		fulfillment1.setDie(die);
		// fulfillment1.setReleaseCycle(1);
		fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
		fulfillment1.persist();

		OrderFulfillment fulfillment2 = new OrderFulfillment();
		OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 102, 2);
		fulfillment2.setId(pk2);
		fulfillment2.setDestination(stop4);
		fulfillment2.setDie(die2);
		// fulfillment2.setReleaseCycle(2);
		fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
		fulfillment2.persist();

		OrderFulfillment fulfillment3 = new OrderFulfillment();
		OrderFulfillmentPk pk3 = new OrderFulfillmentPk(weldOrder, 103, 2);
		fulfillment3.setId(pk3);
		fulfillment3.setDestination(stop3);
		fulfillment3.setDie(die);
		// fulfillment3.setReleaseCycle(2);
		fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.READY_TO_DELIVER);
		fulfillment3.persist();

		CarrierMes carriermes = new CarrierMes();
		carriermes.setCarrierNumber(101);
		carriermes.setCurrentLocation(stop4.getId());
		carriermes.setDestination(stop4.getId());
		carriermes.setBuffer(new Integer(1));
		carriermes.persist();

		CarrierMes carriermes1 = new CarrierMes();
		carriermes1.setCarrierNumber(102);
		carriermes1.setCurrentLocation(stop4.getId());
		carriermes1.setDestination(stop4.getId());
		carriermes1.setBuffer(new Integer(0));
		carriermes1.persist();
	}

	public void loadTestData1() {

		Stop stop = new Stop(1300L);
		stop.setName("stop");
		stop.setStopType(StopType.RELEASE_CHECK);
		stop.setStopArea(StopArea.Q_WELD_LINE_2);

		Stop stop1 = new Stop(1222L);
		stop1.setName("stop1");
		stop1.setStopType(StopType.RELEASE_CHECK);
		stop1.setStopArea(StopArea.Q_WELD_LINE_1);

		Stop stop2 = new Stop(1223L);
		stop2.setName("stop2");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.Q_WELD_LINE_1);

		Stop stop3 = new Stop(700L);
		stop3.setName("stop3");
		stop3.setStopType(StopType.FULL_CARRIER_DELIVERY);
		stop3.setStopArea(StopArea.UNDEFINED);

		Stop stop4 = new Stop(1201L);
		stop4.setName("row");
		stop4.setStopType(StopType.NO_ACTION);
		stop4.setStopArea(StopArea.UNDEFINED);

		stop.persist();
		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();

		Die die = new Die();
		die.setId(1L);
		die.setBpmPartNumber("100A");
		die.setDescription("die");
		die.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		die.persist();

		Die die2 = new Die();
		die2.setId(2L);
		die2.setBpmPartNumber("100A");
		die2.setDescription("die");
		die2.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
		die2.persist();

		Die die3 = new Die();
		die3.setId(999L);
		die3.setBpmPartNumber("999");
		die3.setDescription("empty die");
		die3.setPartProductionVolume(PartProductionVolume.LOW_VOLUME);
		die3.persist();

		Model model = new Model();
		model.setDescription("model");
		model.setId(1L);
		model.setName("model");
		model.setLeftDie(die);
		model.setRightDie(die2);
		model.persist();

		OrderMgr orderMgr = new OrderMgr();
		orderMgr.setId(1L);
		orderMgr.setLineName("line1");
		orderMgr.setMaxDeliveryCapacity(new Integer(12));
		orderMgr.setDeliveryStop(stop3);
		orderMgr.setLeftConsumptionExit(stop3);
		orderMgr.setLeftConsumptionStop(stop3);
		orderMgr.setRightConsumptionExit(stop3);
		orderMgr.setRightConsumptionStop(stop3);
		orderMgr.persist();

		WeldOrder order = new WeldOrder();
		order.setId(1L);
		order.setOrderMgr(orderMgr);
		order.setRightQuantity(100);
		order.setLeftQuantity(100);
		order.setModel(model);
		order.setOrderStatus(OrderStatus.RetrievingCarriers);
		order.setDeliveryStatus(OrderStatus.InProcess);
		order.persist();

		WeldOrder weldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
		OrderFulfillment fulfillment1 = new OrderFulfillment();
		OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101, 1);
		fulfillment1.setId(pk1);
		fulfillment1.setDestination(stop4);
		fulfillment1.setDie(die);
		// fulfillment1.setReleaseCycle(1);
		fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
		fulfillment1.persist();

		OrderFulfillment fulfillment2 = new OrderFulfillment();
		OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 102, 2);
		fulfillment2.setId(pk2);
		fulfillment2.setDestination(stop4);
		fulfillment2.setDie(die2);
		// fulfillment2.setReleaseCycle(2);
		fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
		fulfillment2.persist();

		OrderFulfillment fulfillment3 = new OrderFulfillment();
		OrderFulfillmentPk pk3 = new OrderFulfillmentPk(weldOrder, 103, 2);
		fulfillment3.setId(pk3);
		fulfillment3.setDestination(stop);
		fulfillment3.setDie(die);
		// fulfillment3.setReleaseCycle(2);
		fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.READY_TO_DELIVER);
		fulfillment3.persist();

		OrderFulfillment fulfillment4 = new OrderFulfillment();
		OrderFulfillmentPk pk4 = new OrderFulfillmentPk(weldOrder, 102, 4);
		fulfillment4.setId(pk4);
		fulfillment4.setDestination(stop1);
		fulfillment4.setDie(die2);
		// fulfillment2.setReleaseCycle(2);
		fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
		fulfillment4.persist();

		CarrierMes carriermes = new CarrierMes();
		carriermes.setCarrierNumber(101);
		carriermes.setCurrentLocation(stop4.getId());
		carriermes.setDestination(stop4.getId());
		carriermes.setBuffer(new Integer(1));
		carriermes.persist();

		CarrierMes carriermes1 = new CarrierMes();
		carriermes1.setCarrierNumber(102);
		carriermes1.setCurrentLocation(stop1.getId());
		carriermes1.setDestination(stop1.getId());
		carriermes1.setBuffer(new Integer(0));
		carriermes1.persist();
	}

	public void loadTestParmData() {
		ParmSetting setting = new ParmSetting();
		setting.setFieldname("fulfillmentCarrierInspectionStop");
		setting.setFieldvalue("1300");
		setting.setDescription("fulfillmentCarrierInspectionStopt");
		setting.setUpdatedby("test");
		setting.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

		setting.persist();

	}
}
