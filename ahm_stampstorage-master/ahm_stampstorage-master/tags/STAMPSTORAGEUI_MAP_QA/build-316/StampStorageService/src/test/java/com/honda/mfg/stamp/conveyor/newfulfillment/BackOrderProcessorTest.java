package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.processor.AbstractTestBase;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 7/1/13
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class BackOrderProcessorTest extends AbstractTestBase {

    int updateMessage = 0;

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForStoreIn() {
        AnnotationProcessor.process(this);
        loadTestData();
        loadTestParmData();
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        when(storage.getStorageState()).thenReturn(storageState);

        BackOrderProcessor storageMessageProcessor = new BackOrderProcessor(storage, helper, "deliveryCarrierReleaseCount");
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST5-10");
        stop.setId(510L);
        stop.setStopType(StopType.STORE_IN_ALL_LANES);

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);

        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1234, new Die(101l, PartProductionVolume.MEDIUM_VOLUME));
        when(storageState.isCarrierPartsOnBackOrder(carrier.getDie())).thenReturn(true);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("101");
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        while (updateMessage == 0) {
        }
        assertEquals(1, updateMessage);


//        Carrier carrier1 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1235, new Die(102l, PartProductionVolume.MEDIUM_VOLUME));
//        when(storageState.isCarrierPartsOnBackOrder(carrier1)).thenReturn(true);
//        carrierMessage.setCurrentLocation(stop.getId().toString());
//        carrierMessage.setDestination(stop2.getId().toString());
//        carrierMessage.setDieNumber("102");
//        carrierMessage.setCarrier(carrier1);
//        EventBus.publish(carrierMessage);
//
//        while (storageMessageProcessor.getMessage() == null) {
//
//        }
//        while (updateMessage == 0) {
//        }
//        assertEquals(2, updateMessage);
//
//        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1236, new Die(104l, PartProductionVolume.MEDIUM_VOLUME));
//        when(storageState.isCarrierPartsOnBackOrder(carrier2)).thenReturn(true);
//        carrierMessage.setCurrentLocation(stop.getId().toString());
//        carrierMessage.setDestination(stop2.getId().toString());
//        carrierMessage.setDieNumber("104");
//        carrierMessage.setCarrier(carrier2);
//        EventBus.publish(carrierMessage);
//
//        while (storageMessageProcessor.getMessage() == null) {
//
//        }
//
//
//        when(storageState.isCarrierPartsOnBackOrder(carrier2)).thenReturn(false);
//        EventBus.publish(carrierMessage);
//
//        while (storageMessageProcessor.getMessage() == null) {
//
//        }


        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);
    }

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForStoreIn2() {
        AnnotationProcessor.process(this);
        loadTestData();
        loadTestParmData();
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        when(storage.getStorageState()).thenReturn(storageState);

        BackOrderProcessor storageMessageProcessor = new BackOrderProcessor(storage, helper, "deliveryCarrierReleaseCount");
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST5-10");
        stop.setId(510L);
        stop.setStopType(StopType.STORE_IN_ALL_LANES);

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);


        Carrier carrier1 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1235, new Die(102l, PartProductionVolume.MEDIUM_VOLUME));
        when(storageState.isCarrierPartsOnBackOrder(carrier1.getDie())).thenReturn(true);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("102");
        carrierMessage.setCarrier(carrier1);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        while (updateMessage == 0) {
        }
        assertEquals(1, updateMessage);
//
//        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1236, new Die(104l, PartProductionVolume.MEDIUM_VOLUME));
//        when(storageState.isCarrierPartsOnBackOrder(carrier2)).thenReturn(true);
//        carrierMessage.setCurrentLocation(stop.getId().toString());
//        carrierMessage.setDestination(stop2.getId().toString());
//        carrierMessage.setDieNumber("104");
//        carrierMessage.setCarrier(carrier2);
//        EventBus.publish(carrierMessage);
//
//        while (storageMessageProcessor.getMessage() == null) {
//
//        }
//
//
//        when(storageState.isCarrierPartsOnBackOrder(carrier2)).thenReturn(false);
//        EventBus.publish(carrierMessage);
//
//        while (storageMessageProcessor.getMessage() == null) {
//
//        }


        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);
    }

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForStoreIn3() {
        AnnotationProcessor.process(this);
        loadTestData();
        loadTestParmData();
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        when(storage.getStorageState()).thenReturn(storageState);

        BackOrderProcessor storageMessageProcessor = new BackOrderProcessor(storage, helper, "deliveryCarrierReleaseCount");
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST5-10");
        stop.setId(510L);
        stop.setStopType(StopType.STORE_IN_ALL_LANES);

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);


        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.ON_HOLD, 1236, new Die(104l, PartProductionVolume.MEDIUM_VOLUME));
        when(storageState.isCarrierPartsOnBackOrder(carrier2.getDie())).thenReturn(true);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("103");
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }

        verify(storage, times(1)).store(Matchers.<Carrier>any());


        when(storageState.isCarrierPartsOnBackOrder(carrier2.getDie())).thenReturn(false);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }

//           while (updateMessage == 0) {
//        }
//        assertEquals(1, updateMessage);


        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);
    }

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForClearingBackOrder() {
        AnnotationProcessor.process(this);
        loadTestData2();
        loadTestParmData();
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        when(storage.getStorageState()).thenReturn(storageState);

        BackOrderProcessor storageMessageProcessor = new BackOrderProcessor(storage, helper, "deliveryCarrierReleaseCount");
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST5-10");
        stop.setId(510L);
        stop.setStopType(StopType.STORE_IN_ALL_LANES);

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);


        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1236, new Die(101l, PartProductionVolume.MEDIUM_VOLUME));
        when(storageState.isCarrierPartsOnBackOrder(carrier2.getDie())).thenReturn(true);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("101");
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
    }

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForClearingBackOrderWhenTwoWeldLinesOnBackOrderForSameProduct() {
        AnnotationProcessor.process(this);
        loadTestData3();
        loadTestParmData();
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        when(storage.getStorageState()).thenReturn(storageState);

        BackOrderProcessor storageMessageProcessor = new BackOrderProcessor(storage, helper, "deliveryCarrierReleaseCount");
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST5-10");
        stop.setId(510L);
        stop.setStopType(StopType.STORE_IN_ALL_LANES);

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);


        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.SHIPPABLE, 1236, new Die(101l, PartProductionVolume.MEDIUM_VOLUME));
        when(storageState.isCarrierPartsOnBackOrder(carrier2.getDie())).thenReturn(true).thenReturn(true);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("101");
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }

        Carrier carrier3 = new Carrier(3, new Timestamp(System.currentTimeMillis()), 115, stop, stop2, CarrierStatus.SHIPPABLE, 1237, new Die(101l, PartProductionVolume.MEDIUM_VOLUME));
        when(storageState.isCarrierPartsOnBackOrder(carrier3.getDie())).thenReturn(true).thenReturn(false);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("101");
        carrierMessage.setCarrier(carrier3);
        EventBus.publish(carrierMessage);

         while (storageMessageProcessor.getMessage() == null) {

        }
    }


    @EventSubscriber(eventClass = CarrierUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchUpdateEvent(CarrierUpdateMessage message) {
        updateMessage++;
    }


    private void loadTestData() {

        Stop stop1 = new Stop(1230L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        Stop stop2 = new Stop(1231L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        Stop stop3 = new Stop(1232L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        Stop stop4 = new Stop(1233L);
        stop4.setName("row");
        stop4.setStopType(StopType.NO_ACTION);
        stop4.setStopArea(StopArea.UNDEFINED);

        Stop stop5 = new Stop(1234L);
        stop5.setName("row");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.UNDEFINED);

        Stop stop6 = new Stop(1235L);
        stop6.setName("row");
        stop6.setStopType(StopType.NO_ACTION);
        stop6.setStopArea(StopArea.UNDEFINED);

        Stop stop7 = new Stop(1210L);
        stop7.setName("row");
        stop7.setStopType(StopType.NO_ACTION);
        stop7.setStopArea(StopArea.UNDEFINED);

        Stop stop8 = new Stop(1211L);
        stop8.setName("row");
        stop8.setStopType(StopType.NO_ACTION);
        stop8.setStopArea(StopArea.UNDEFINED);

        Stop stop9 = new Stop(1212L);
        stop9.setName("row");
        stop9.setStopType(StopType.NO_ACTION);
        stop9.setStopArea(StopArea.UNDEFINED);

        Stop stop10 = new Stop(904L);
        stop10.setName("release");
        stop10.setStopType(StopType.RELEASE_CHECK);
        stop10.setStopArea(StopArea.UNDEFINED);

        Stop stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        Stop stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);

        Stop stop13 = new Stop(1301L);
        stop13.setName("release");
        stop13.setStopType(StopType.RELEASE_CHECK);
        stop13.setStopArea(StopArea.UNDEFINED);


        Stop stop24 = new Stop(1224L);
        stop24.setName("row");
        stop24.setStopType(StopType.NO_ACTION);
        stop24.setStopArea(StopArea.Q_WELD_LINE_2);

        Stop stop25 = new Stop(1225L);
        stop25.setName("row");
        stop25.setStopType(StopType.NO_ACTION);
        stop25.setStopArea(StopArea.Q_WELD_LINE_2);

        Stop stop510 = new Stop(510L);
        stop510.setName("row");
        stop510.setStopType(StopType.STORE_IN_ALL_LANES);
        stop510.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop513 = new Stop(513L);
        stop513.setName("row");
        stop513.setStopType(StopType.STORE_IN_ALL_LANES);
        stop513.setStopArea(StopArea.STORE_IN_ROUTE);


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
        stop513.persist();
        stop510.persist();

        StorageRow rowLeftQueue = new StorageRow();
        rowLeftQueue.setStop(stop24);
        rowLeftQueue.setAvailability(StopAvailability.AVAILABLE);
        rowLeftQueue.setCapacity(21);
        rowLeftQueue.setRowName("Row 24");
        rowLeftQueue.setStorageArea(StorageArea.Q_AREA);

        StorageRow rowRightQueue = new StorageRow();
        rowRightQueue.setStop(stop25);
        rowRightQueue.setAvailability(StopAvailability.AVAILABLE);
        rowRightQueue.setCapacity(21);
        rowRightQueue.setRowName("Row 25");
        rowRightQueue.setStorageArea(StorageArea.Q_AREA);

        rowRightQueue.persist();
        rowLeftQueue.persist();

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

        OrderMgr orderMgr = new OrderMgr();
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
        //order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(60);
        order.setLeftQuantity(60);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();


    }

    private void loadTestData2() {

        Stop stop1 = new Stop(1230L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        Stop stop2 = new Stop(1231L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        Stop stop3 = new Stop(1232L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        Stop stop4 = new Stop(1233L);
        stop4.setName("row");
        stop4.setStopType(StopType.NO_ACTION);
        stop4.setStopArea(StopArea.UNDEFINED);

        Stop stop5 = new Stop(1234L);
        stop5.setName("row");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.UNDEFINED);

        Stop stop6 = new Stop(1235L);
        stop6.setName("row");
        stop6.setStopType(StopType.NO_ACTION);
        stop6.setStopArea(StopArea.UNDEFINED);

        Stop stop7 = new Stop(1210L);
        stop7.setName("row");
        stop7.setStopType(StopType.NO_ACTION);
        stop7.setStopArea(StopArea.UNDEFINED);

        Stop stop8 = new Stop(1211L);
        stop8.setName("row");
        stop8.setStopType(StopType.NO_ACTION);
        stop8.setStopArea(StopArea.UNDEFINED);

        Stop stop9 = new Stop(1212L);
        stop9.setName("row");
        stop9.setStopType(StopType.NO_ACTION);
        stop9.setStopArea(StopArea.UNDEFINED);

        Stop stop10 = new Stop(904L);
        stop10.setName("release");
        stop10.setStopType(StopType.RELEASE_CHECK);
        stop10.setStopArea(StopArea.UNDEFINED);

        Stop stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        Stop stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);

        Stop stop13 = new Stop(1301L);
        stop13.setName("release");
        stop13.setStopType(StopType.RELEASE_CHECK);
        stop13.setStopArea(StopArea.UNDEFINED);


        Stop stop24 = new Stop(1224L);
        stop24.setName("row");
        stop24.setStopType(StopType.NO_ACTION);
        stop24.setStopArea(StopArea.Q_WELD_LINE_2);

        Stop stop25 = new Stop(1225L);
        stop25.setName("row");
        stop25.setStopType(StopType.NO_ACTION);
        stop25.setStopArea(StopArea.Q_WELD_LINE_2);

        Stop stop510 = new Stop(510L);
        stop510.setName("row");
        stop510.setStopType(StopType.STORE_IN_ALL_LANES);
        stop510.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop513 = new Stop(513L);
        stop513.setName("row");
        stop513.setStopType(StopType.STORE_IN_ALL_LANES);
        stop513.setStopArea(StopArea.STORE_IN_ROUTE);


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
        stop513.persist();
        stop510.persist();

        StorageRow rowLeftQueue = new StorageRow();
        rowLeftQueue.setStop(stop24);
        rowLeftQueue.setAvailability(StopAvailability.AVAILABLE);
        rowLeftQueue.setCapacity(21);
        rowLeftQueue.setRowName("Row 24");
        rowLeftQueue.setStorageArea(StorageArea.Q_AREA);

        StorageRow rowRightQueue = new StorageRow();
        rowRightQueue.setStop(stop25);
        rowRightQueue.setAvailability(StopAvailability.AVAILABLE);
        rowRightQueue.setCapacity(21);
        rowRightQueue.setRowName("Row 25");
        rowRightQueue.setStorageArea(StorageArea.Q_AREA);

        rowRightQueue.persist();
        rowLeftQueue.persist();

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

        OrderMgr orderMgr = new OrderMgr();
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
        //order.setId(1L);
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

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(200), someDie);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(202), leftDie);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
        CarrierMes carrier11 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
        CarrierMes carrier12 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        CarrierMes carrier41 = getCarrierMes(oneDayOld, new Integer(100), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        CarrierMes carrier44 = getCarrierMes(oneDayOld, new Integer(100), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);
        CarrierMes carrier42 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
        CarrierMes carrier43 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(425), leftDie);
        CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
        CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
        CarrierMes carrier52 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
        CarrierMes carrier53 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
        CarrierMes carrier54 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(605), rightDie);

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

    private void loadTestData3() {

        Stop stop1 = new Stop(1230L);
        stop1.setName("row");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        Stop stop2 = new Stop(1231L);
        stop2.setName("row");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        Stop stop3 = new Stop(1232L);
        stop3.setName("row");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        Stop stop4 = new Stop(1233L);
        stop4.setName("row");
        stop4.setStopType(StopType.NO_ACTION);
        stop4.setStopArea(StopArea.UNDEFINED);

        Stop stop5 = new Stop(1234L);
        stop5.setName("row");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.UNDEFINED);

        Stop stop6 = new Stop(1235L);
        stop6.setName("row");
        stop6.setStopType(StopType.NO_ACTION);
        stop6.setStopArea(StopArea.UNDEFINED);

        Stop stop7 = new Stop(1210L);
        stop7.setName("row");
        stop7.setStopType(StopType.NO_ACTION);
        stop7.setStopArea(StopArea.UNDEFINED);

        Stop stop8 = new Stop(1211L);
        stop8.setName("row");
        stop8.setStopType(StopType.NO_ACTION);
        stop8.setStopArea(StopArea.UNDEFINED);

        Stop stop9 = new Stop(1212L);
        stop9.setName("row");
        stop9.setStopType(StopType.NO_ACTION);
        stop9.setStopArea(StopArea.UNDEFINED);

        Stop stop10 = new Stop(904L);
        stop10.setName("release");
        stop10.setStopType(StopType.RELEASE_CHECK);
        stop10.setStopArea(StopArea.UNDEFINED);

        Stop stop11 = new Stop(500L);
        stop11.setName("delivery");
        stop11.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop11.setStopArea(StopArea.UNDEFINED);

        Stop stop12 = new Stop(1300L);
        stop12.setName("release");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.UNDEFINED);

        Stop stop13 = new Stop(1301L);
        stop13.setName("release");
        stop13.setStopType(StopType.RELEASE_CHECK);
        stop13.setStopArea(StopArea.UNDEFINED);


        Stop stop24 = new Stop(1224L);
        stop24.setName("row");
        stop24.setStopType(StopType.NO_ACTION);
        stop24.setStopArea(StopArea.Q_WELD_LINE_2);

        Stop stop25 = new Stop(1225L);
        stop25.setName("row");
        stop25.setStopType(StopType.NO_ACTION);
        stop25.setStopArea(StopArea.Q_WELD_LINE_2);

        Stop stop510 = new Stop(510L);
        stop510.setName("row");
        stop510.setStopType(StopType.STORE_IN_ALL_LANES);
        stop510.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop513 = new Stop(513L);
        stop513.setName("row");
        stop513.setStopType(StopType.STORE_IN_ALL_LANES);
        stop513.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop22 = new Stop(1222L);
        stop22.setName("row");
        stop22.setStopType(StopType.NO_ACTION);
        stop22.setStopArea(StopArea.Q_WELD_LINE_1);

        Stop stop23 = new Stop(1223L);
        stop23.setName("row");
        stop23.setStopType(StopType.NO_ACTION);
        stop23.setStopArea(StopArea.Q_WELD_LINE_1);


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
        stop513.persist();
        stop510.persist();
        stop22.persist();
        stop23.persist();

        StorageRow rowLeftQueue = new StorageRow();
        rowLeftQueue.setStop(stop24);
        rowLeftQueue.setAvailability(StopAvailability.AVAILABLE);
        rowLeftQueue.setCapacity(21);
        rowLeftQueue.setRowName("Row 24");
        rowLeftQueue.setStorageArea(StorageArea.Q_AREA);

        StorageRow rowRightQueue = new StorageRow();
        rowRightQueue.setStop(stop25);
        rowRightQueue.setAvailability(StopAvailability.AVAILABLE);
        rowRightQueue.setCapacity(21);
        rowRightQueue.setRowName("Row 25");
        rowRightQueue.setStorageArea(StorageArea.Q_AREA);

        StorageRow rowLeftQueue2 = new StorageRow();
        rowLeftQueue2.setStop(stop22);
        rowLeftQueue2.setAvailability(StopAvailability.AVAILABLE);
        rowLeftQueue2.setCapacity(21);
        rowLeftQueue2.setRowName("Row 22");
        rowLeftQueue2.setStorageArea(StorageArea.Q_AREA);

        StorageRow rowRightQueue2 = new StorageRow();
        rowRightQueue2.setStop(stop23);
        rowRightQueue2.setAvailability(StopAvailability.AVAILABLE);
        rowRightQueue2.setCapacity(21);
        rowRightQueue2.setRowName("Row 23");
        rowRightQueue2.setStorageArea(StorageArea.Q_AREA);

        rowRightQueue.persist();
        rowLeftQueue.persist();
        rowRightQueue2.persist();
        rowLeftQueue2.persist();

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

        OrderMgr orderMgr = new OrderMgr();
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

         OrderMgr orderMgr2 = new OrderMgr();
        orderMgr2.setId(2L);
        orderMgr2.setLineName("line1");
        orderMgr2.setMaxDeliveryCapacity(new Integer(16));
        orderMgr2.setDeliveryStop(stop11);
        orderMgr2.setLeftConsumptionExit(stop3);
        orderMgr2.setLeftConsumptionStop(stop3);
        orderMgr2.setRightConsumptionExit(stop3);
        orderMgr2.setRightConsumptionStop(stop3);
        orderMgr2.setLeftQueueStop(stop22);
        orderMgr2.setRightQueueStop(stop23);
        orderMgr2.persist();

        WeldOrder order = new WeldOrder();
        //order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(60);
        order.setLeftQuantity(60);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();

         WeldOrder order2 = new WeldOrder();
        //order.setId(1L);
        order2.setOrderMgr(orderMgr2);
        order2.setRightQuantity(60);
        order2.setLeftQuantity(60);
        order2.setOrderStatus(OrderStatus.InProcess);
        order2.setDeliveryStatus(OrderStatus.Initialized);
        order2.setModel(model);
        order2.persist();


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        CarrierMes carrier2 = getCarrierMes(oneDayOld, new Integer(103), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(200), someDie);
        CarrierMes carrier3 = getCarrierMes(oneDayOld, new Integer(103), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(202), leftDie);
        CarrierMes carrier1 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
        CarrierMes carrier11 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
        CarrierMes carrier12 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);
        CarrierMes carrier4 = getCarrierMes(oneDayOld, new Integer(100), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        CarrierMes carrier41 = getCarrierMes(oneDayOld, new Integer(100), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        CarrierMes carrier44 = getCarrierMes(oneDayOld, new Integer(100), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);
        CarrierMes carrier42 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(35), leftDie);
        CarrierMes carrier43 = getCarrierMes(twoDaysOld, new Integer(90), stop24, stop24, CarrierStatus.SHIPPABLE, new Integer(425), leftDie);
        CarrierMes carrier5 = getCarrierMes(oneDayOld, new Integer(101), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(131), rightDie);
        CarrierMes carrier51 = getCarrierMes(oneDayOld, new Integer(101), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(203), rightDie);
        CarrierMes carrier52 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(126), rightDie);
        CarrierMes carrier53 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(465), rightDie);
        CarrierMes carrier54 = getCarrierMes(twoDaysOld, new Integer(99), stop25, stop25, CarrierStatus.SHIPPABLE, new Integer(605), rightDie);

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


    public void loadTestParmData() {
        ParmSetting setting = new ParmSetting();
        setting.setFieldname("deliveryCarrierReleaseCount");
        setting.setFieldvalue("4");
        setting.setDescription("deliveryCarrierReleaseCount");
        setting.setUpdatedby("test");
        setting.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

        setting.persist();
    }
}
