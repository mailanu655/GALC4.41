package com.honda.mfg.stamp.conveyor.processor;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.manager.*;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.newfulfillment.NewFulfillmentHelper;
import com.honda.mfg.stamp.conveyor.newfulfillment.NewFulfillmentHelperImpl;
import com.honda.mfg.stamp.conveyor.newfulfillment.StorageStateContextMock;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 2/27/12
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class StorageReceiveMessageProcessorIntegrationTest extends AbstractTestBase {

      int updateMessage = 0;

    @Test(timeout = 15000L)
    public void successfullyReceiveCarrierStatusMessageAndDeleteDefects() {
        loadDefectTable();

        List<Defect> defectList = Defect.findAllDefects();
        assertEquals(2, defectList.size());

        StorageState storageState = mock(StorageState.class);
        StorageStateContext context = new StorageStateContextMock(storageState);
        StoreInManager storeInManager = mock(StoreInManager.class);
        Storage storage = new StorageImpl(storeInManager, null, null, context);
        StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage, 0);
        CarrierStatusMessage carrierMessage = new CarrierStatusMessage();


        Stop stop4 = new Stop();
        stop4.setName("ST8");
        stop4.setId(800l);
        stop4.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
        stop4.setStopArea(StopArea.KD_LINE);

        Carrier carrier4 = new Carrier(3, new Timestamp(System.currentTimeMillis()), Integer.valueOf(111), stop4, stop4, CarrierStatus.ON_HOLD, Integer.valueOf(101), new Die());
        carrierMessage.setCarrier(carrier4);
        EventBus.publish(carrierMessage);


        long delta = System.currentTimeMillis();
        while (System.currentTimeMillis() - delta < (5 * 1000L)) {
        }

//        List<Defect> defectList2 = Defect.findAllDefects();
//        assertEquals(0, defectList2.size());


        storageMessageProcessor.turnOffSubscriber();
    }

    @Test//(timeout = 5000L)
       public void successfullyReceiveCarrierStatusMessageAndPublishUpdateForWeldLineDeliveryStopCarriers() {
           AnnotationProcessor.process(this);
           loadStopTable();
           StorageState storageState = mock(StorageState.class);
           StorageStateContext context = new StorageStateContextMock(storageState);
           StoreInManager storeInManager = mock(StoreInManager.class);
           Storage storage = new StorageImpl(storeInManager, null, null, context);
           StorageReceiveMessageProcessor storageMessageProcessor = new StorageReceiveMessageProcessor(storage, 0);
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

//           Stop stop3 = new Stop();
//           stop3.setName("ST7");
//           stop3.setId(700L);
//           stop3.setStopType(StopType.FULL_CARRIER_DELIVERY);
//           stop3.setStopArea(StopArea.KD_LINE);

           Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop, CarrierStatus.ON_HOLD, 1234, new Die());
           carrierMessage.setCarrier(carrier);
           EventBus.publish(carrierMessage);

           while (storageMessageProcessor.getMessage() == null) {

           }

           Carrier carrier2 = new Carrier(2, new Timestamp(System.currentTimeMillis()), 114, stop2, stop2, CarrierStatus.ON_HOLD, 1234, new Die());
           carrierMessage.setCarrier(carrier2);
           EventBus.publish(carrierMessage);

//           Carrier carrier3 = new Carrier(3, new Timestamp(System.currentTimeMillis()), 114, stop3, stop3, CarrierStatus.ON_HOLD, 1234, new Die());
//           carrierMessage.setCarrier(carrier3);
//           EventBus.publish(carrierMessage);


           while (updateMessage < 2) {

           }
           Assert.assertEquals(2, updateMessage);
           storageMessageProcessor.turnOffSubscriber();
           AnnotationProcessor.unprocess(this);
       }

    @EventSubscriber(eventClass = CarrierUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
      public void catchUpdateEvent(CarrierUpdateMessage message) {
          updateMessage++;
      }

   void loadStopTable() {
        Stop stop1 = new Stop(10900L);
        stop1.setName("stop1");
        stop1.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
        stop1.setStopArea(StopArea.WELD_LINE_2);
        stop1.persist();

//        Stop stop2 = new Stop(800L);
//        stop2.setName("stop2");
//        stop2.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
//        stop2.setStopArea(StopArea.KD_LINE);
//        stop2.setCapacity(1);
//        stop2.setStopAvailability(StopAvailability.AVAILABLE);

        Stop stop3 = new Stop(3700L);
        stop3.setName("stop3");
        stop3.setStopType(StopType.FULL_CARRIER_CONSUMPTION);
        stop3.setStopArea(StopArea.WELD_LINE_1);

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
        model.setDescription("model");
        model.setId(1L);
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.persist();


        OrderMgr orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(12));
        orderMgr.setDeliveryStop(stop1);
        orderMgr.setLeftConsumptionExit(stop1);
        orderMgr.setLeftConsumptionStop(stop1);
        orderMgr.setRightConsumptionExit(stop1);
        orderMgr.setRightConsumptionStop(stop1);
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
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 1234,1);
        fulfillment1.setId(pk1);
        fulfillment1.setDestination(stop1);
        fulfillment1.setDie(leftDie);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment1.persist();

   }


    void loadDefectTable() {
        Defect defect = new Defect();
        defect.setCarrierNumber(Integer.valueOf(101));
        defect.setProductionRunNo(Integer.valueOf(111));
        defect.setDefectType(DEFECT_TYPE.BAD_HANDWORK);
        defect.setReworkMethod(REWORK_METHOD.DA);
        defect.setXArea(17);
        defect.setYArea("S");
        defect.setDefectRepaired(false);
        defect.setDefectTimestamp(new Timestamp(System.currentTimeMillis()));
        defect.setSource("user");

        defect.persist();

        Defect defect1 = new Defect();
        defect1.setCarrierNumber(Integer.valueOf(101));
        defect1.setProductionRunNo(Integer.valueOf(111));
        defect1.setDefectType(DEFECT_TYPE.LASER_BLOWOUT);
        defect1.setReworkMethod(REWORK_METHOD.DEBURR);
        defect1.setXArea(18);
        defect1.setYArea("R");
        defect1.setDefectRepaired(false);
        defect1.setDefectTimestamp(new Timestamp(System.currentTimeMillis()));
        defect1.setSource("user");

        defect1.persist();
    }

        @Test//(timeout = 5000L)
    public void successfullyReceiveCarrierStatusMessageForBackOrderStoreIn() {
        AnnotationProcessor.process(this);

        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

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


}
