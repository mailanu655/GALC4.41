package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.processor.AbstractTestBase;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelper;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelperImpl;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 6/14/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class DeliveryManagerTest extends AbstractTestBase {

    OrderMgr orderMgr = null, orderMgr2 = null;
    Stop stop1, stop2, stop3, stop11, stop12;
    Die leftDie, rightDie;

    @Test
    public void successfullyRunDeliveryManager() {
        loadTestData();
        loadOrderFulfillmentTable();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(8, fulfillments.size());

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());
        List<OrderFulfillment> fulfillments1 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.READY_TO_DELIVER);
        assertEquals(0, fulfillments1.size());
        List<OrderFulfillment> fulfillments2 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(4, fulfillments2.size());

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());
        List<OrderFulfillment> fulfillments3 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.READY_TO_DELIVER);
        assertEquals(0, fulfillments3.size());
        List<OrderFulfillment> fulfillments4 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(0, fulfillments4.size());

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(2, fulfillments.size());

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());
        fulfillments1 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.READY_TO_DELIVER);
        assertEquals(0, fulfillments1.size());
        fulfillments2 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(1, fulfillments2.size());

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());
        fulfillments3 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.READY_TO_DELIVER);
        assertEquals(0, fulfillments3.size());
        fulfillments4 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(0, fulfillments4.size());

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        manager.run();

        assertEquals(OrderStatus.AutoCompleted, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        manager.run();

        AnnotationProcessor.unprocess(this);
    }


    @Test
    public void successfullyRunDeliveryManagerWhenNoSpaceAvailable() {
        loadTestDataForNoSpaceAvailable();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButAnotherOrderDeliveringCarriers() {
        loadTestDataWithAnotherOrderDelivering();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButNotEnoughCarriersToDeliver() {
        loadTestDataWhenNotEnoughCarriersToDeliver();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButNotEnoughRightCarriersToDeliver() {
        loadTestDataWhenNotEnoughRightCarriersToDeliver();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButNotEnoughLeftCarriersToDeliver() {
        loadTestDataWhenNotEnoughLeftCarriersToDeliver();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButLessThanDeliverySizeOfCarriersRequiredByOrderToComplete() {
        loadTestDataWhenLessThanDeliverySizeOfCarriersRequiredByOrderToComplete();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.DeliveringCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButNotCorrectDieToDeliver() {
        loadTestDataNotCorrectDieAtHead();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyRunDeliveryManagerWhenSpaceAvailableButInCorrectDieInMiddleOfTheQueue() {
        loadTestDataWithIncorrectDieInMiddle();
        loadTestParmData();
        AnnotationProcessor.process(this);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");

        manager.run();

        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getDeliveryStatus());
        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(0, fulfillments.size());
        List<OrderFulfillment> fulfillments3 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.READY_TO_DELIVER);
        assertEquals(1, fulfillments3.size());
        List<OrderFulfillment> fulfillments4 = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(weldOrder, CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
        assertEquals(0, fulfillments4.size());

        manager.run();

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void throwException() {
        ReleaseManagerHelper releaseManagerHelper = mock(ReleaseManagerHelperImpl.class);
        ReleaseManager releaseManager = mock(ReleaseManagerImpl.class);
        NewFulfillmentHelper helper = mock(NewFulfillmentHelperImpl.class);

        DeliveryManager manager = new DeliveryManager(orderMgr, helper, releaseManager, "deliveryCarrierReleaseCount", "fulfillmentCarrierInspectionStop");
        when(helper.getActiveOrderForDelivery(Matchers.<OrderMgr>any())).thenThrow(new RuntimeException());
        manager.run();

    }

    @EventSubscriber(eventClass = CarrierUpdateMessage.class)
    public void catchEvent(CarrierUpdateMessage message) {
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(Integer.parseInt(message.getCarrierNumber()));
        carrierMessage.setCarrierNumber(carrierMes.getCarrierNumber().toString());

        if (message.getDestination().equals("500")) {
            carrierMessage.setCurrentLocation("500");
            carrierMes.setCurrentLocation(500L);
            carrierMes.setDestination(500L);
            carrierMes.merge();
        } else if (message.getDestination().equals("1300")) {
            carrierMessage.setCurrentLocation("1300");
            carrierMes.setCurrentLocation(1300L);
            carrierMes.setDestination(1300L);
            carrierMes.merge();
        }


        carrierMessage.setDestination(carrierMes.getDestination().toString());
        carrierMessage.setDieNumber(carrierMes.getDieNumber().toString());
        carrierMessage.setQuantity(carrierMes.getQuantity().toString());
        carrierMessage.setStatus(carrierMes.getStatus().toString());
        carrierMessage.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate().toString());
        carrierMessage.setProductionRunNo(carrierMes.getProductionRunNumber().toString());
        carrierMessage.setBuffer("1");
        carrierMessage.setOriginationLocation(carrierMes.getOriginationLocation().toString());
        EventBus.publish(carrierMessage);
    }

    public void loadTestData() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(50);
        order.setLeftQuantity(50);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 1);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 1);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(109), leftDie, 1);
        CarrierMes carrier41 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(596), leftDie, 1);
        CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(131), rightDie, 1);
        CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(203), rightDie, 1);
        CarrierMes carrier52 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie, 1);
        CarrierMes carrier53 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(120), rightDie, 1);
        CarrierMes carrier54 = getCarrierMes(oneDayOld, new Integer(99), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(121), leftDie, 1);


        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier4.persist();
        carrier41.persist();

        carrier5.persist();
        carrier51.persist();
        carrier52.persist();
        carrier53.persist();
        carrier54.persist();

    }

    private CarrierMes getCarrierMes(Timestamp oneDayOld, Integer productionRunNumber, Stop stop3, Stop stop31, CarrierStatus status, Integer carrierNumber, Die die, Integer buffer) {
        CarrierMes carrierMes = new CarrierMes();
        carrierMes.setCarrierNumber(carrierNumber);
        carrierMes.setProductionRunDate(oneDayOld);
        carrierMes.setProductionRunNumber(productionRunNumber);
        carrierMes.setCurrentLocation(stop3.getId());
        carrierMes.setDestination(stop31.getId());
        carrierMes.setStatus(status.type());
        carrierMes.setDieNumber(Integer.parseInt(die.getId().toString()));
        carrierMes.setQuantity(10);
        carrierMes.setOriginationLocation(Press.REWORK_C_LINE.type());
        carrierMes.setBuffer(buffer);
        carrierMes.setUpdateDate(oneDayOld);
        return carrierMes;
    }


    void loadOrderFulfillmentTable() {

        WeldOrder weldOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);

        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 696, 1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop2);
        fulfillment1.setDestination(stop2);
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment1.setDie(leftDie);
        fulfillment1.setQuantity(new Integer(10));
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 720, 1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop2);
        fulfillment2.setDestination(stop2);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment2.setDie(leftDie);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();

        OrderFulfillment fulfillment3 = new OrderFulfillment();
        OrderFulfillmentPk pk3 = new OrderFulfillmentPk(weldOrder, 109, 1);
        fulfillment3.setId(pk3);
        fulfillment3.setCurrentLocation(stop2);
        fulfillment3.setDestination(stop2);
        fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment3.setDie(leftDie);
        fulfillment3.setQuantity(new Integer(10));
        fulfillment3.persist();


        OrderFulfillment fulfillment4 = new OrderFulfillment();
        OrderFulfillmentPk pk4 = new OrderFulfillmentPk(weldOrder, 596, 1);
        fulfillment4.setId(pk4);
        fulfillment4.setCurrentLocation(stop2);
        fulfillment4.setDestination(stop2);
        fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment4.setDie(leftDie);
        fulfillment4.setQuantity(new Integer(10));
        fulfillment4.persist();


        OrderFulfillment fulfillment6 = new OrderFulfillment();
        OrderFulfillmentPk pk6 = new OrderFulfillmentPk(weldOrder, 130, 1);
        fulfillment6.setId(pk6);
        fulfillment6.setCurrentLocation(stop1);
        fulfillment6.setDestination(stop1);
        fulfillment6.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment6.setDie(rightDie);
        fulfillment6.setQuantity(new Integer(10));
        fulfillment6.persist();

        OrderFulfillment fulfillment7 = new OrderFulfillment();
        OrderFulfillmentPk pk7 = new OrderFulfillmentPk(weldOrder, 131, 1);
        fulfillment7.setId(pk7);
        fulfillment7.setCurrentLocation(stop1);
        fulfillment7.setDestination(stop1);
        fulfillment7.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment7.setDie(rightDie);
        fulfillment7.setQuantity(new Integer(10));
        fulfillment7.persist();

        OrderFulfillment fulfillment8 = new OrderFulfillment();
        OrderFulfillmentPk pk8 = new OrderFulfillmentPk(weldOrder, 203, 1);
        fulfillment8.setId(pk8);
        fulfillment8.setCurrentLocation(stop1);
        fulfillment8.setDestination(stop1);
        fulfillment8.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment8.setDie(rightDie);
        fulfillment8.setQuantity(new Integer(10));
        fulfillment8.persist();

        OrderFulfillment fulfillment9 = new OrderFulfillment();
        OrderFulfillmentPk pk9 = new OrderFulfillmentPk(weldOrder, 126, 1);
        fulfillment9.setId(pk9);
        fulfillment9.setCurrentLocation(stop1);
        fulfillment9.setDestination(stop1);
        fulfillment9.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment9.setDie(rightDie);
        fulfillment9.setQuantity(new Integer(10));
        fulfillment9.persist();

        List<WeldOrder> weldOrders = WeldOrder.findWeldOrdersByDeliveryStatusAndOrderStatus(OrderStatus.ManuallyCompleted, OrderStatus.ManuallyCompleted);

        OrderFulfillment fulfillment10 = new OrderFulfillment();
        OrderFulfillmentPk pk10 = new OrderFulfillmentPk(weldOrders.get(0), 120, 1);
        fulfillment10.setId(pk10);
        fulfillment10.setCurrentLocation(stop1);
        fulfillment10.setDestination(stop1);
        fulfillment10.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
        fulfillment10.setDie(rightDie);
        fulfillment10.setQuantity(new Integer(10));
        fulfillment10.persist();
    }

    public void loadTestDataForNoSpaceAvailable() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);

        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(8));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(40);
        order.setLeftQuantity(40);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop11, stop11, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop11, stop11, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 1);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 1);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(109), leftDie, 1);
        CarrierMes carrier41 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(596), leftDie, 1);
        CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(131), rightDie, 1);
        CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(203), rightDie, 1);
        CarrierMes carrier52 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie, 1);


        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier4.persist();
        carrier41.persist();

        carrier5.persist();
        carrier51.persist();
        carrier52.persist();

    }

    public void loadTestDataWithAnotherOrderDelivering() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(10100L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);

        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(8));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(40);
        order.setLeftQuantity(40);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        orderMgr2 = new OrderMgr();
        orderMgr2.setId(2L);
        orderMgr2.setLineName("line2");
        orderMgr2.setMaxDeliveryCapacity(new Integer(8));
        orderMgr2.setDeliveryStop(stop12);
        orderMgr2.setLeftConsumptionExit(stop3);
        orderMgr2.setLeftConsumptionStop(stop3);
        orderMgr2.setRightConsumptionExit(stop3);
        orderMgr2.setRightConsumptionStop(stop3);
        orderMgr2.setLeftQueueStop(stop2);
        orderMgr2.setRightQueueStop(stop1);
        orderMgr2.persist();


        WeldOrder order2 = new WeldOrder();
        order2.setId(1L);
        order2.setOrderMgr(orderMgr2);
        order2.setRightQuantity(40);
        order2.setLeftQuantity(40);
        order2.setOrderStatus(OrderStatus.InProcess);
        order2.setDeliveryStatus(OrderStatus.DeliveringCarriers);
        order2.setModel(model);
        order2.persist();
    }

    public void loadTestDataWhenNotEnoughCarriersToDeliver() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(50);
        order.setLeftQuantity(50);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 1);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 1);


        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
    }

    public void loadTestDataNotCorrectDieAtHead() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(50);
        order.setLeftQuantity(50);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 1);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 1);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 0);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 0);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 0);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(109), leftDie, 0);
        CarrierMes carrier41 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(596), leftDie, 0);
        CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(131), rightDie, 0);
        CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(203), rightDie, 0);
        CarrierMes carrier52 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie, 0);

        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier4.persist();
        carrier41.persist();
        carrier5.persist();
        carrier51.persist();
        carrier52.persist();

    }

    public void loadTestDataWithIncorrectDieInMiddle() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);

        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(0);
        order.setLeftQuantity(50);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        //CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        //CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 0);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 0);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(109), leftDie, 0);

        //CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(131), rightDie, 0);
        // CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(203), rightDie, 0);


        //carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        // carrier11.persist();
        carrier12.persist();
        carrier4.persist();
        //carrier5.persist();
        // carrier51.persist();

        OrderFulfillment fulfillment9 = new OrderFulfillment();
        OrderFulfillmentPk pk9 = new OrderFulfillmentPk(order, 126, 1);
        fulfillment9.setId(pk9);
        fulfillment9.setCurrentLocation(stop1);
        fulfillment9.setDestination(stop1);
        fulfillment9.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.READY_TO_DELIVER);
        fulfillment9.setDie(rightDie);
        fulfillment9.setQuantity(new Integer(10));
        fulfillment9.persist();
    }

    public void loadTestDataWhenNotEnoughRightCarriersToDeliver() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(50);
        order.setLeftQuantity(50);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 1);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 1);
        CarrierMes carrier13 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(820), leftDie, 1);
        CarrierMes carrier14 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(920), leftDie, 1);

        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier13.persist();
        carrier14.persist();
    }

    public void loadTestDataWhenNotEnoughLeftCarriersToDeliver() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(50);
        order.setLeftQuantity(50);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 1);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(720), rightDie, 1);
        CarrierMes carrier13 = getCarrierMes(oneDayOld, new Integer(90), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(820), rightDie, 1);
        CarrierMes carrier14 = getCarrierMes(oneDayOld, new Integer(90), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(920), rightDie, 1);

        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier13.persist();
        carrier14.persist();
    }

    public void loadTestDataWhenLessThanDeliverySizeOfCarriersRequiredByOrderToComplete() {

        stop1 = new Stop(1222L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        stop2 = new Stop(1223L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        stop3 = new Stop(1800L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();

        stop11.persist();
        stop12.persist();


        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");

        model.persist();

        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(30));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop2);
        orderMgr.setRightQueueStop(stop1);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(10);
        order.setLeftQuantity(20);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.InProcess);
        order.setModel(model);
        order.persist();

        WeldOrder order0 = new WeldOrder();
        order0.setId(2L);
        order0.setOrderMgr(orderMgr);
        order0.setRightQuantity(40);
        order0.setLeftQuantity(40);
        order0.setOrderStatus(OrderStatus.ManuallyCompleted);
        order0.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order0.setModel(model);
        order0.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie, 0);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(202), someDie, 0);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(696), leftDie, 1);
        CarrierMes carrier11 = getCarrierMes(oneDayOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(130), rightDie, 1);
        CarrierMes carrier12 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(720), leftDie, 1);
        CarrierMes carrier22 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(820), someDie, 0);
        CarrierMes carrier32 = getCarrierMes(oneDayOld, new Integer(90), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(920), someDie, 0);
        CarrierMes carrier42 = getCarrierMes(oneDayOld, new Integer(90), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(620), someDie, 0);
        CarrierMes carrier52 = getCarrierMes(oneDayOld, new Integer(90), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(520), someDie, 0);
        CarrierMes carrier62 = getCarrierMes(oneDayOld, new Integer(90), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(420), someDie, 0);

        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier22.persist();
        carrier32.persist();
        carrier42.persist();
        carrier52.persist();
        carrier62.persist();

    }


    public void loadTestParmData() {
        ParmSetting setting = new ParmSetting();
        setting.setFieldname("deliveryCarrierReleaseCount");
        setting.setFieldvalue("4");
        setting.setDescription("deliveryCarrierReleaseCount");
        setting.setUpdatedby("test");
        setting.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

        setting.persist();

        ParmSetting setting1 = new ParmSetting();
        setting1.setFieldname("fulfillmentCarrierInspectionStop");
        setting1.setFieldvalue("1300");
        setting1.setDescription("fulfillmentCarrierInspectionStopt");
        setting1.setUpdatedby("test");
        setting1.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

        setting1.persist();
    }
}
