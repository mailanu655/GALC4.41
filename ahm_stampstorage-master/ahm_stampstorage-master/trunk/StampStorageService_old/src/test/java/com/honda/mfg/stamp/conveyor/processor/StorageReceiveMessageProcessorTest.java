package com.honda.mfg.stamp.conveyor.processor;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.manager.*;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;
import com.honda.mfg.stamp.conveyor.newfulfillment.StorageStateContextMock;
import org.bushe.swing.event.*;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: vcc30690
 * Date: 4/29/11
 */

public class StorageReceiveMessageProcessorTest {

    EventService e;
    boolean reloadStorageState;
    int count = 0;
    int updateMessage = 0;

    @Before
    public void before() throws EventServiceExistsException {

        if (e != null && !(e instanceof SwingEventService)) {
            e = new SwingEventService();
            EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
                    e);
        }
    }

    @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForStoreIn() {

        Storage storage = mock(Storage.class);
        StorageState storageState  = mock(StorageState.class);

        when(storage.getStorageState()).thenReturn(storageState);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST5-10");
        stop.setId(510L);
        stop.setStopType(StopType.STORE_IN_ALL_LANES);
        stop.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);
        stop2.setStopArea(StopArea.STORE_IN_ROUTE);

        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.ON_HOLD, 1234, new Die());
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        verify(storage, times(1)).store(Matchers.<Carrier>any());

        storageMessageProcessor.turnOffSubscriber();
    }


    @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForRecircToStoreIn() {

        Storage storage = mock(Storage.class);
        StorageState storageState  = mock(StorageState.class);

        when(storage.getStorageState()).thenReturn(storageState);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);

        Stop stop3 = new Stop();
        stop3.setName("ST30-3");
        stop3.setId(3003L);
        stop3.setStopType(StopType.STORE_IN_ALL_LANES);
        stop3.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop4 = new Stop();
        stop4.setName("ST7-4");
        stop4.setId(704L);
        stop4.setStopType(StopType.RECIRC_TO_ALL_ROWS);
        stop4.setStopArea(StopArea.STORE_IN_ROUTE);

        Carrier carrier2 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop4, stop3, CarrierStatus.ON_HOLD, 1234, new Die());
        CarrierStatusMessage carrierMessage2 = new CarrierStatusMessage();
        carrierMessage2.setCarrier(carrier2);
        EventBus.publish(carrierMessage2);

        while (storageMessageProcessor.getMessage() == null) {

        }
        verify(storage, times(0)).store(Matchers.<Carrier>any());

        storageMessageProcessor.turnOffSubscriber();
    }

    @Test(timeout = 5000L)
       public void successfullyReceiveCarrierStatusMessageForRecirc() {

           Storage storage = mock(Storage.class);
           StorageState storageState  = mock(StorageState.class);

           when(storage.getStorageState()).thenReturn(storageState);
           StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);

           Stop stop4 = new Stop();
           stop4.setName("ST7-4");
           stop4.setId(704L);
           stop4.setStopType(StopType.RECIRC_TO_ALL_ROWS);
           stop4.setStopArea(StopArea.STORE_IN_ROUTE);

           Carrier carrier2 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop4, stop4, CarrierStatus.ON_HOLD, 1234, new Die());
           CarrierStatusMessage carrierMessage2 = new CarrierStatusMessage();
           carrierMessage2.setCarrier(carrier2);
           EventBus.publish(carrierMessage2);

           while (storageMessageProcessor.getMessage() == null) {

           }
           verify(storage, times(1)).store(Matchers.<Carrier>any());

           storageMessageProcessor.turnOffSubscriber();
       }



    @Test
     public void successfullyReceiveCarrierStatusMessageForBackOrderStoreIn() {
        AnnotationProcessor.process(this);

        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);

        when(storage.getStorageState()).thenReturn(storageState);

        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
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
        when(storageState.isCarrierPartsOnBackOrder(carrier)).thenReturn(true);
        carrierMessage.setCurrentLocation(stop.getId().toString());
        carrierMessage.setDestination(stop2.getId().toString());
        carrierMessage.setDieNumber("101");
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);
    }

    @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageNoStoreIn() {
        Storage storage = mock(Storage.class);

        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop2 = new Stop();
        stop2.setName("ST5-13");
        stop2.setId(513L);
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);
        stop2.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop3 = new Stop();
        stop3.setName("ST4-1");
        stop3.setId(410L);
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.STORE_IN_ROUTE);

        Carrier carrier2 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop3, stop2, CarrierStatus.ON_HOLD, 1234, new Die());
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        verify(storage, times(0)).store(Matchers.<Carrier>any());

        storageMessageProcessor.turnOffSubscriber();
    }

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForSubStoreIn() {

        Storage storage = mock(Storage.class);
        StorageState storageState  = mock(StorageState.class);

        when(storage.getStorageState()).thenReturn(storageState);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();


        Stop stop2 = new Stop();
        stop2.setName("ST7-4");
        stop2.setId(704L);
        stop2.setStopType(StopType.RECIRC_TO_ALL_ROWS);
        stop2.setStopArea(StopArea.STORE_IN_ROUTE);

        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop2, stop2, CarrierStatus.ON_HOLD, 1234, new Die());
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }

        verify(storage, times(1)).store(Matchers.<Carrier>any());
        storageMessageProcessor.turnOffSubscriber();
    }

   // @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForWrongLaneStoreIn() {
        AnnotationProcessor.process(this);
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        when(storage.getStorageState()).thenReturn(storageState);

        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST12-11");
        stop.setId(1211L);
        stop.setStopType(StopType.NO_ACTION);
        stop.setStopArea(StopArea.ROW);

        Stop stop2 = new Stop();
        stop2.setName("ST12-10");
        stop2.setId(1210L);
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.ROW);

        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.ON_HOLD, 1234, new Die(101L, PartProductionVolume.HIGH_VOLUME));
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        while (!reloadStorageState) {

        }
        assertEquals(1, count);
         //verify(storageState, times(1)).reorderCarriersInRow(Matchers.<Long>any());
        reloadStorageState= false;
        Carrier carrier2 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop, CarrierStatus.ON_HOLD, 1234, new Die(101L, PartProductionVolume.HIGH_VOLUME));
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        while (!reloadStorageState) {

        }
       // assertEquals(2, count);

        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);
    }

    @EventSubscriber(eventClass = StaleDataMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchStorageEvent(StaleDataMessage message) {
        reloadStorageState = !message.isStale();
        count++;
    }

    @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForWrongCarrierStoreout() {

        StorageState storageState = mock(StorageState.class);
        Storage storage = mock(Storage.class);

        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        when(storage.getStorageState()).thenReturn(storageState);
        when(storageState.carrierExistsInStorageState(Matchers.<Carrier>any())).thenReturn(true);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST9-4");
        stop.setId(904L);
        stop.setStopType(StopType.RELEASE_CHECK);
        stop.setStopArea(StopArea.STORE_OUT_ROUTE);


        Stop stop2 = new Stop();
        stop2.setName("ST20-3");
        stop2.setId(2003L);
        stop2.setStopArea(StopArea.EMPTY_AREA);


        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop2, CarrierStatus.ON_HOLD, 1234, new Die(101L, PartProductionVolume.HIGH_VOLUME));
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        //verify(storageState, times(1)).removeCarrierFromStorageState(Matchers.<Carrier>any());

        storageMessageProcessor.turnOffSubscriber();
    }

    @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForReorderCarriers() {
        AnnotationProcessor.process(this);
        StorageState storageState = mock(StorageState.class);

        StorageStateContext context = new StorageStateContextMock(storageState);
        Storage storage = new StorageImpl(null, null, null, context);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST9-4");
        stop.setId(904L);
        stop.setStopType(StopType.RELEASE_CHECK);
        stop.setStopArea(StopArea.STORE_OUT_ROUTE);

        Stop stop1 = new Stop();
        stop1.setName("ST12-21");
        stop1.setId(1221L);
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.ROW);


        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop1, CarrierStatus.ON_HOLD, 1234, new Die());
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
         verify(storageState, times(1)).reorderCarriersInRow(Matchers.<Long>any());

        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);


    }

   // @Test(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForInvalidDestinationCarriers() {

        StorageState storageState = mock(StorageState.class);

        StorageStateContext context = new StorageStateContextMock(storageState);
        StoreInManager storeInManager = mock(StoreInManager.class);
        Storage storage = new StorageImpl(storeInManager, null, null, context);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
        //when(storageState.hadValidLaneDestination(Matchers.<Carrier>any())).thenReturn(false);

        Stop stop = new Stop();
        stop.setName("ST12-10");
        stop.setId(1210L);
        stop.setStopType(StopType.NO_ACTION);
        stop.setStopArea(StopArea.ROW);

        Stop stop2 = new Stop();
        stop2.setName("ST16");
        stop2.setId(1600L);
        stop2.setStopType(StopType.STORE_IN_C_LOW_LANES);
        stop2.setStopArea(StopArea.STORE_IN_ROUTE);


        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop2, stop, CarrierStatus.ON_HOLD, 1234, new Die(101L, PartProductionVolume.HIGH_VOLUME));
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }
        verify(storageState, times(1)).hadValidLaneDestination(Matchers.<Carrier>any());
        verify(storeInManager, times(1)).subStore(Matchers.<Carrier>any(), Matchers.<StorageArea>any());
        storageMessageProcessor.turnOffSubscriber();
    }

    @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageAndPublishUpdateForWeldLineDeliveryStopCarriers() {
        AnnotationProcessor.process(this);
        StorageState storageState = mock(StorageState.class);
        StorageStateContext context = new StorageStateContextMock(storageState);
        StoreInManager storeInManager = mock(StoreInManager.class);
        Storage storage = new StorageImpl(storeInManager, null, null, context);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage, 1);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();

        Stop stop = new Stop();
        stop.setName("ST500");
        stop.setId(500L);
        stop.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop.setStopArea(StopArea.WELD_LINE_1);

        Stop stop2 = new Stop();
        stop2.setName("ST101");
        stop2.setId(10100L);
        stop2.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop2.setStopArea(StopArea.WELD_LINE_2);

//        Stop stop3 = new Stop();
//        stop3.setName("ST7");
//        stop3.setId(700L);
//        stop3.setStopType(StopType.FULL_CARRIER_DELIVERY);
//        stop3.setStopArea(StopArea.KD_LINE);

        Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop, CarrierStatus.ON_HOLD, 1234, new Die());
        carrierMessage.setCarrier(carrier);
        EventBus.publish(carrierMessage);

        while (storageMessageProcessor.getMessage() == null) {

        }

        Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop2, stop2, CarrierStatus.ON_HOLD, 1234, new Die());
        carrierMessage.setCarrier(carrier2);
        EventBus.publish(carrierMessage);

//        Carrier carrier3 = new Carrier(3, new Timestamp(System.currentTimeMillis()), 114, stop3, stop3, CarrierStatus.ON_HOLD, 1234, new Die());
//        carrierMessage.setCarrier(carrier3);
//        EventBus.publish(carrierMessage);


        while (updateMessage < 2) {

        }
        assertEquals(2, updateMessage);
        storageMessageProcessor.turnOffSubscriber();
        AnnotationProcessor.unprocess(this);
    }

    @EventSubscriber(eventClass = CarrierUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchUpdateEvent(CarrierUpdateMessage message) {
        updateMessage++;
    }



}
