package com.honda.mfg.stamp.conveyor.integrationTests;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.manager.*;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.newfulfillment.*;
import com.honda.mfg.stamp.conveyor.newfulfillment.StorageStateContextMock;
import com.honda.mfg.stamp.conveyor.processor.AbstractTestBase;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelper;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelperImpl;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.empty.EmptyManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreInManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_out.StoreOutManagerImpl;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static junit.framework.Assert.assertEquals;

//import com.honda.mfg.stamp.conveyor.manager.StorageStateContextMock;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 6/4/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class BackOrderFulfillmentIntegrationTest extends AbstractTestBase {


    OrderMgr orderMgr = null;
    Stop stop1, stop2, stop3, stop4, stop5, stop6, stop7, stop8, stop9, stop10, stop11, stop12, stop13, stop24, stop25;


    @Test
    public void backOrderLeftsSecondCycleTest() {
        loadTestData();
        loadTestParmData();
        AnnotationProcessor.process(this);
        Storage storage = getStorageSecondLeftCycleBackOrder();

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        //BackOrderProcessor backOrderProcessor = new BackOrderProcessor(storage,helper);
        WeldOrder weldOrder = WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.InProcess).get(0);
        OrderFulfillmentManager manager = new OrderFulfillmentManager(orderMgr, storage, helper, releaseManager, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());

        manager.run();
        assertEquals(OrderStatus.InProcess, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);
        List<OrderFulfillment> carriersCycle3 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 3);
        List<OrderFulfillment> carriersCycle4 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 4);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
        assertEquals(0, carriersCycle3.size());
        assertEquals(0, carriersCycle4.size());

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);
        List<OrderFulfillment> carriersCycle31 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 3);
        List<OrderFulfillment> carriersCycle41 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 4);

        assertEquals(10, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
        assertEquals(1, carriersCycle31.size());
        assertEquals(1, carriersCycle41.size());


        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void backOrderRightsSecondCycleTest() {
        loadTestData();
        loadTestParmData();
        AnnotationProcessor.process(this);
        Storage storage = getStorageSecondRightCycleBackOrder();

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.InProcess).get(0);
        OrderFulfillmentManager manager = new OrderFulfillmentManager(orderMgr, storage, helper, releaseManager, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());

        manager.run();

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());

        manager.run();
        fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);
        List<OrderFulfillment> carriersCycle3 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 3);
        List<OrderFulfillment> carriersCycle4 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 4);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
        assertEquals(0, carriersCycle3.size());
        assertEquals(0, carriersCycle4.size());

        AnnotationProcessor.unprocess(this);
    }


    @Test
    public void backOrderLeftsFirstCycleTest() {
        loadTestData();
        loadTestParmData();
        Storage storage = getStorageLeftsBackOrder();

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.InProcess).get(0);
        OrderFulfillmentManager manager = new OrderFulfillmentManager(orderMgr, storage, helper, releaseManager, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");

        manager.run();

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);

        assertEquals(4, fulfillments.size());
    }

    @Test
    public void backOrderRightsFirstCycleTest() {
        loadTestData();
        loadTestParmData();
        Storage storage = getStorageRightsBackOrder();

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        FulfillmentProcessor processor = new FulfillmentProcessor(helper, "fulfillmentCarrierInspectionStop");
        WeldOrder weldOrder = WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.InProcess).get(0);
        OrderFulfillmentManager manager = new OrderFulfillmentManager(orderMgr, storage, helper, releaseManager, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");

        manager.run();

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);

        assertEquals(4, fulfillments.size());

        manager.run();

        List<OrderFulfillment> fulfillments2 = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);

        assertEquals(4, fulfillments2.size());

        manager.run();

        List<OrderFulfillment> fulfillments3 = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);

        assertEquals(4, fulfillments3.size());

        manager.run();

        List<OrderFulfillment> fulfillments4 = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);

        assertEquals(4, fulfillments4.size());
    }


    @EventSubscriber(eventClass = CarrierUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
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
        } else if (message.getDestination().equals("1301")) {
            carrierMessage.setCurrentLocation("904");
            carrierMes.setCurrentLocation(904L);
            carrierMes.setDestination(1301L);
            carrierMes.merge();
        } else if (message.getDestination().equals("1224")) {
            carrierMessage.setCurrentLocation("1224");
            carrierMes.setCurrentLocation(1224L);
            carrierMes.setDestination(1224L);
            carrierMes.merge();
        } else if (message.getDestination().equals("1225")) {
            carrierMessage.setCurrentLocation("1225");
            carrierMes.setCurrentLocation(1225L);
            carrierMes.setDestination(1225L);
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

    @EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchStatusEvent(CarrierStatusMessage message) {
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(Integer.parseInt(message.getCarrierNumber()));
        carrierMessage.setCarrierNumber(carrierMes.getCarrierNumber().toString());

        if (message.getCurrentLocation().equals("903") || message.getCurrentLocation().equals("904")) {
            carrierMessage.setCurrentLocation("1301");
            carrierMes.setCurrentLocation(1301L);
            carrierMes.setDestination(1301L);
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

    private void loadTestData() {

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

        StorageRow row24 = new StorageRow();
        row24.setRowName("row 24");
        row24.setAvailability(StopAvailability.AVAILABLE);
        row24.setCapacity(21);
        row24.setStop(stop24);
        row24.setStorageArea(StorageArea.Q_AREA);
        row24.persist();

        StorageRow row25 = new StorageRow();
        row25.setRowName("row 25");
        row25.setAvailability(StopAvailability.AVAILABLE);
        row25.setCapacity(21);
        row25.setStop(stop25);
        row25.setStorageArea(StorageArea.Q_AREA);
        row25.persist();

        Die leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die rightDie = new Die();
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
        orderMgr.setMaxDeliveryCapacity(new Integer(16));
        orderMgr.setDeliveryStop(stop11);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.setLeftQueueStop(stop24);
        orderMgr.setRightQueueStop(stop25);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(60);
        order.setLeftQuantity(60);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);

        order.setModel(model);
        order.persist();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop4, stop4, CarrierStatus.SHIPPABLE, new Integer(202), someDie);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop7, stop7, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
        CarrierMes carrier11 = getCarrierMes(twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
        CarrierMes carrier12 = getCarrierMes(twoDaysOld, new Integer(90), stop9, stop9, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        CarrierMes carrier41 = getCarrierMes(oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        CarrierMes carrier44 = getCarrierMes(oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);
        CarrierMes carrier42 = getCarrierMes(twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
        CarrierMes carrier43 = getCarrierMes(twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(425), leftDie);
        CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
        CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
        CarrierMes carrier52 = getCarrierMes(twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
        CarrierMes carrier53 = getCarrierMes(twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
        CarrierMes carrier54 = getCarrierMes(twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(605), rightDie);

        carrier2.persist();
        carrier3.persist();
        carrier1.persist();
        carrier11.persist();
        carrier12.persist();
        carrier4.persist();
        carrier41.persist();
        carrier42.persist();
        carrier43.persist();
        carrier44.persist();
        carrier5.persist();
        carrier51.persist();
        carrier52.persist();
        carrier53.persist();
        carrier54.persist();
    }

    private CarrierMes getCarrierMes(Timestamp oneDayOld, Integer productionRunNumber, Stop stop3, Stop stop31, CarrierStatus status, Integer carrierNumber, Die die) {
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
        carrierMes.setBuffer(new Integer(1));
        return carrierMes;  //To change body of created methods use File | Settings | File Templates.
    }

    private Storage getStorageLeftsBackOrder() {
        Die leftDie, rightDie, someDie;

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


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie);

        Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop4, stop4, CarrierStatus.SHIPPABLE, new Integer(202), someDie);

        Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);

        Carrier carrier5 = new Carrier(10, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
        Carrier carrier51 = new Carrier(11, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
        Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
        Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
        Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(605), rightDie);


        List<StorageRow> storageLanes = getStorageRows();
        storageLanes.get(31).store(carrier2);
        storageLanes.get(32).store(carrier3);
        storageLanes.get(10).store(carrier11);
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

    private Storage getStorageRightsBackOrder() {
        Die leftDie, rightDie, someDie;

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


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie);

        Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop4, stop4, CarrierStatus.SHIPPABLE, new Integer(202), someDie);

        Carrier carrier1 = new Carrier(3, twoDaysOld, new Integer(90), stop7, stop7, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
        Carrier carrier12 = new Carrier(5, twoDaysOld, new Integer(90), stop9, stop9, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);


        Carrier carrier4 = new Carrier(6, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        Carrier carrier41 = new Carrier(7, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        Carrier carrier44 = new Carrier(17, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);
        Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
        Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(425), leftDie);

        List<StorageRow> storageLanes = getStorageRows();
        storageLanes.get(31).store(carrier2);
        storageLanes.get(32).store(carrier3);
        storageLanes.get(9).store(carrier1);

        storageLanes.get(11).store(carrier12);
        storageLanes.get(34).store(carrier4);
        storageLanes.get(34).store(carrier41);
        storageLanes.get(34).store(carrier44);
        storageLanes.get(33).store(carrier42);
        storageLanes.get(33).store(carrier43);

        StorageState storageState = new StorageStateImpl(storageLanes);

        StorageStateContext context = new StorageStateContextMock(storageState);
        StoreOutManager storeOutManager = new StoreOutManagerImpl(context);
        StoreInManager storeInManager = new StoreInManagerImpl(context);
        EmptyManagerImpl emptyManager = new EmptyManagerImpl(context);
        Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, context);
        return storage;
    }

    private Storage getStorageSecondLeftCycleBackOrder() {
        Die leftDie, rightDie, someDie;

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


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie);

        Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop4, stop4, CarrierStatus.SHIPPABLE, new Integer(202), someDie);

//        Carrier carrier1 = new Carrier(3, twoDaysOld, new Integer(90), stop7, stop7, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
//        Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
//        Carrier carrier12 = new Carrier(5, twoDaysOld, new Integer(90), stop9, stop9, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);


        Carrier carrier4 = new Carrier(6, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        Carrier carrier41 = new Carrier(7, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        Carrier carrier44 = new Carrier(17, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);
        Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
        Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(425), leftDie);

        Carrier carrier5 = new Carrier(10, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
        Carrier carrier51 = new Carrier(11, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
        Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
        Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
        Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(605), rightDie);


        List<StorageRow> storageLanes = getStorageRows();
        storageLanes.get(31).store(carrier2);
        storageLanes.get(32).store(carrier3);
//        storageLanes.get(9).store(carrier1);
//        storageLanes.get(10).store(carrier11);
//        storageLanes.get(11).store(carrier12);
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

    private Storage getStorageSecondRightCycleBackOrder() {
        Die leftDie, rightDie, someDie;

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


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop3, stop3, CarrierStatus.SHIPPABLE, new Integer(200), someDie);

        Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop4, stop4, CarrierStatus.SHIPPABLE, new Integer(202), someDie);

        Carrier carrier1 = new Carrier(3, twoDaysOld, new Integer(90), stop7, stop7, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
//        Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
        Carrier carrier12 = new Carrier(5, twoDaysOld, new Integer(90), stop9, stop9, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);


        Carrier carrier4 = new Carrier(6, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        Carrier carrier41 = new Carrier(7, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        Carrier carrier44 = new Carrier(17, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);
        Carrier carrier42 = new Carrier(8, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
        Carrier carrier43 = new Carrier(9, twoDaysOld, new Integer(90), stop5, stop5, CarrierStatus.SHIPPABLE, new Integer(425), leftDie);

        Carrier carrier5 = new Carrier(10, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
        Carrier carrier51 = new Carrier(11, oneDayOld, new Integer(101), stop2, stop2, CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
        Carrier carrier52 = new Carrier(12, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
        Carrier carrier53 = new Carrier(13, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
        Carrier carrier54 = new Carrier(14, twoDaysOld, new Integer(99), stop1, stop1, CarrierStatus.SHIPPABLE, new Integer(605), rightDie);


        List<StorageRow> storageLanes = getStorageRows();
        storageLanes.get(31).store(carrier2);
        storageLanes.get(32).store(carrier3);
        storageLanes.get(9).store(carrier1);
        //storageLanes.get(10).store(carrier11);
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
         for (i = 36; i < 44; i++) {
            StorageRow row = new StorageRow(i, "Row-" + i, 31, 1);
            row.setStop(new Stop(1200 + i));
            row.setStorageArea(StorageArea.B_AREA);
            row.setAvailability(StopAvailability.AVAILABLE);
            storageRows.add(row);
        }
         for (i = 44; i < 46; i++) {
            StorageRow row = new StorageRow(i, "Row-" + i,87, 1);
            row.setStop(new Stop(1200 + i));
            row.setStorageArea(StorageArea.S_AREA);
            row.setAvailability(StopAvailability.AVAILABLE);
            storageRows.add(row);
        }
        StorageRow row = new StorageRow(i, "Row-" + i,29, 1);
            row.setStop(new Stop(1200 + i));
            row.setStorageArea(StorageArea.B_AREA);
            row.setAvailability(StopAvailability.AVAILABLE);
            storageRows.add(row);
        return storageRows;
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

}
