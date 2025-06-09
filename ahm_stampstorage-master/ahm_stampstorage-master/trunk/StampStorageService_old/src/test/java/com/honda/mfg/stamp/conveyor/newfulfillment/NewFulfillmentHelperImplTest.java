package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/23/11
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class NewFulfillmentHelperImplTest {
    WeldOrder order = null, order1 = null;
    OrderMgr orderMgr = null;
    Die die = null, emptyDie = null, dieR = null;
    Model model = null;

    @Test
    public void successfullySaveWeldOrderStatus() {
        loadTestData();
        OrderStatus orderStatus = OrderStatus.DeliveringCarriers;
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.saveWeldOrderStatus(order, orderStatus);
        helper.saveWeldOrderStatus(order1, orderStatus);
    }

    @Test
    public void successfullySaveCarriers() {
        loadTestData();
        List<Carrier> carriers = new ArrayList<Carrier>();
        Carrier carrier = new Carrier();
        carrier.setCarrierNumber(111);
        carrier.setDestination(Stop.findStop(1201L));
        carrier.setCurrentLocation(Stop.findStop(1201L));
        carrier.setDie(Die.findDie(1L));
        carrier.setProductionRunNo(new Integer(11111));
        carrier.setQuantity(new Integer(10));
        carriers.add(carrier);

        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.saveCarriers(order, carriers);
    }

    @Test
    public void successfullyGetMinCycleCountForOrderForFulfillmentStatus() {
        loadTestData();
        CarrierFulfillmentStatus status = CarrierFulfillmentStatus.RELEASED;
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(new Integer(1), helper.getMinCycleCountForOrderForFulfillmentStatus(order, status));
    }

    @Test
    public void successfullyGetAllOrderFulfillmentsByOrder() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertNotNull(helper.getAllOrderFulfillmentsByOrder(order));
    }

    /**
     * 2013-06-20:VB
     */
    @Test
    public void successfullyGetAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        List list = null;
        assertNotNull(list = helper.getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(order, CarrierFulfillmentStatus.RETRIEVED));
        assertEquals(2, list.size());
    }

    /**
     * 2013-06-20:VB
     */
    @Test
    public void successfullyGetAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        List list = null;
        assertNotNull(list = helper
                .getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(
                        order, CarrierFulfillmentStatus.SELECTED,
                        order.getLeftDie()));
        assertEquals(1, list.size());
    }

    @Test
    public void successfullyGetNewCarriersToRelease() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        List carriers = helper.getNewCarriersToRelease(order, 1);
        assertNotNull(carriers);
        assertEquals(1, carriers.size());
    }

    @Test
    public void successfullyTestNoOrderMgrOrderingCarriers() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(false, helper.noOrderMgrWithOrderStatus(OrderStatus.RetrievingCarriers));
    }

    @Test
    public void testGetCurrentQueueCapacityOfOrderMgr() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        long capacity = helper.getCurrentQueueCapacityOfOrderMgr(orderMgr.getLeftQueueStop());
        assertEquals(17, capacity);
    }

//    @Test
//    public void testGetCurrentRightCapacityOfOrderMgr() {
//        loadTestData();
//        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
//        long capacity = helper.getCurrentRightCapacityOfOrderMgr(orderMgr);
//        assertEquals(21, capacity);
//    }

    @Test
    public void successfullyGetActiveOrder() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertNotNull(helper.getActiveOrder(orderMgr));
    }

    /**
     * 2013-06-20:VB
     */
    @Test
    public void successfullyGetActiveOrderForDelivery() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        order.setDeliveryStatus(OrderStatus.InProcess);
        order.persist();
        assertNotNull(helper.getActiveOrderForDelivery(orderMgr));

        order.setDeliveryStatus(OrderStatus.DeliveringCarriers);
        order.persist();
        assertNotNull(helper.getActiveOrderForDelivery(orderMgr));
    }

    @Test
    public void succcessfullyIsAnotherOrderDeliveringCarriers() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(true, helper.noOrderWithDeliveryStatus(OrderStatus.DeliveringCarriers));
    }

    @Test
    public void successfullyIsCarrierInA() {
        loadTestData();
        Integer carrierNumber = new Integer(101);
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(false, helper.isCarrierInA(carrierNumber));
    }

    @Test
    public void testNeedMoreCarriersForDie() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(true, helper.needMoreCarriersWithDie(order, die));
        assertEquals(false, helper.needMoreCarriersWithDie(order, dieR));
    }

    @Test
    public void testIsAnyOrderRetrieving() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(true, helper.isAnyOrderRetrieving(orderMgr));
    }

    @Test
    public void successfullySaveWeldOrderComments() {
        loadTestData();
        String comments = " test save comments";
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.saveWeldOrderComments(order, comments);
    }

    @Test
    public void successfullySaveWeldOrderStatusAndComments() {
        loadTestData();
        OrderStatus orderStatus = OrderStatus.DeliveringCarriers;
        String comments = " test save comments";
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.saveWeldOrderStatusAndComments(order, orderStatus, comments);
        helper.saveWeldOrderStatusAndComments(order1, orderStatus, comments);
    }

    @Test
    public void successfullyGetCarrierCountForDie() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        int count = helper.getCarrierCountForDie(order, die);

        assertEquals(8, count);
    }

    @Test
    public void successfullyDeleteAllUnReleasedOrderFulfillments() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.deleteAllUnReleasedOrderFulfillments(order);

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);
        //in carriermes, only 103 satisfies necessary conditions
        //viz., status=(retrieved|selected*) and current location is a row stop
        assertEquals(11, fulfillments.size());
    }

    @Test
    public void successfullyGetDieForLastCycle() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        Die thisDie = helper.getDieForLastCycle(order);
        assertNotNull(thisDie);
    }

    @Test
    public void successfullyGenerateAlarm() {
        loadTestData();

        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.generateInspectionAlarm(105);
        List<AlarmEvent> alarmEvents = AlarmEvent.findAllCurrent_Alarms();
        // assertEquals(1,alarmEvents.size());
    }


    @Test
    public void successfullySaveWeldDeliveryStatus() {
        loadTestData();
        OrderStatus orderStatus = OrderStatus.DeliveringCarriers;
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.saveWeldDeliveryStatus(order, orderStatus);
        helper.saveWeldDeliveryStatus(order1, orderStatus);
    }

    @Test
    public void successfullySaveWeldDeliveryStatusAndComments() {
        loadTestData();
        OrderStatus orderStatus = OrderStatus.DeliveringCarriers;
        String comments = " test save comments";
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        helper.saveWeldDeliveryStatusAndComments(order, orderStatus, comments);
        helper.saveWeldDeliveryStatusAndComments(order1, orderStatus, comments);
    }

    @Test
    public void successfullyGetCarrierDestinationForRecirculation() {
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();

        Stop destination1 = helper.getCarrierDestinationForRecirculation(StorageRow.findStorageRowsByStop(Stop.findStop(1201L)), Die.findDie(1L));
        Stop emptyDestination1 = helper.getCarrierDestinationForRecirculation(StorageRow.findStorageRowsByStop(Stop.findStop(1201L)), Die.findDie(999L));
        assertNotSame(destination1,emptyDestination1);

        Stop destination2 = helper.getCarrierDestinationForRecirculation(StorageRow.findStorageRowsByStop(Stop.findStop(1236L)), Die.findDie(1L));
        Stop emptyDestination2 = helper.getCarrierDestinationForRecirculation(StorageRow.findStorageRowsByStop(Stop.findStop(1236L)), Die.findDie(999L));

        assertEquals(destination2,emptyDestination2);
        assertEquals(destination1,Stop.findStop(704L));
        assertEquals(emptyDestination1,Stop.findStop(5200L));
        assertEquals(destination2,Stop.findStop(3018L));
    }


    public void loadStopTest() {
        Stop stop1 = new Stop(704L);
        stop1.setName("stop1");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop2 = new Stop(5200L);
        stop2.setName("stop2");
        stop2.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
        stop2.setStopArea(StopArea.OLD_WELD_LINE);

        Stop stop3 = new Stop(2003L);
        stop3.setName("stop3");
        stop3.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
        stop3.setStopArea(StopArea.EMPTY_AREA);

        Stop stop4 = new Stop(1301L);
        stop4.setName("ST13-1");
        stop4.setStopType(StopType.NO_ACTION);
        stop4.setStopArea(StopArea.UNDEFINED);

        Stop stop5 = new Stop(1300L);
        stop5.setName("ST13");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.UNDEFINED);


        stop1.persist();
        stop2.persist();
        stop3.persist();
        stop4.persist();
        stop5.persist();
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
        stop4.setStopArea(StopArea.ROW);


        StorageRow row = new StorageRow();
        row.setRowName("ROW1");
        row.setStop(stop4);
        row.setCapacity(12);
        row.setAvailability(StopAvailability.AVAILABLE);
        row.setStorageArea(StorageArea.C_HIGH);

        Stop stop5 = new Stop(708L);
        stop5.setName("kd");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.KD_LINE);

        Stop stop12 = new Stop(1212L);
        stop12.setName("Row-12");
        stop12.setStopType(StopType.NO_ACTION);
        stop12.setStopArea(StopArea.C_PRESS);

        StorageRow row2 = new StorageRow();
        row2.setRowName("ROW12");
        row2.setStop(stop12);
        row2.setCapacity(12);
        row2.setAvailability(StopAvailability.AVAILABLE);
        row2.setStorageArea(StorageArea.C_HIGH);

        Stop stop13 = new Stop(1213L);
        stop13.setName("Row-13");
        stop13.setStopType(StopType.NO_ACTION);
        stop13.setStopArea(StopArea.C_PRESS);

        StorageRow row3 = new StorageRow();
        row3.setRowName("ROW13");
        row3.setStop(stop13);
        row3.setCapacity(12);
        row3.setAvailability(StopAvailability.AVAILABLE);
        row3.setStorageArea(StorageArea.C_HIGH);

        Stop stop22 = new Stop(1222L);
        stop22.setName("Row-22");
        stop22.setStopType(StopType.NO_ACTION);
        stop22.setStopArea(StopArea.Q_WELD_LINE_1);

        StorageRow row4 = new StorageRow();
        row4.setRowName("ROW22");
        row4.setStop(stop22);
        row4.setCapacity(21);
        row4.setAvailability(StopAvailability.AVAILABLE);
        row4.setStorageArea(StorageArea.Q_AREA);

        Stop stop23 = new Stop(1223L);
        stop23.setName("Row-23");
        stop23.setStopType(StopType.NO_ACTION);
        stop23.setStopArea(StopArea.Q_WELD_LINE_1);

        StorageRow row5 = new StorageRow();
        row5.setRowName("ROW23");
        row5.setStop(stop23);
        row5.setCapacity(21);
        row5.setAvailability(StopAvailability.AVAILABLE);
        row5.setStorageArea(StorageArea.Q_AREA);

        Stop stop36 = new Stop(1236L);
        stop36.setName("Row-36");
        stop36.setStopType(StopType.NO_ACTION);
        stop36.setStopArea(StopArea.ROW);

        StorageRow row6 = new StorageRow();
        row6.setRowName("ROW36");
        row6.setStop(stop36);
        row6.setCapacity(31);
        row6.setAvailability(StopAvailability.AVAILABLE);
        row6.setStorageArea(StorageArea.B_AREA);

        Stop stop3018 = new Stop(3018L);
        stop3018.setName("ST 30-18");
        stop3018.setStopType(StopType.RECIRC_TO_ALL_ROWS);
        stop3018.setStopArea(StopArea.B_AREA);

        Stop stop74 = new Stop(704L);
        stop74.setName("ST7-4");
        stop74.setStopType(StopType.RECIRC_TO_ALL_ROWS);
        stop74.setStopArea(StopArea.STORE_IN_ROUTE);

        Stop stop52 = new Stop(5200L);
        stop52.setName("stop2");
        stop52.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
        stop52.setStopArea(StopArea.OLD_WELD_LINE);


        stop1.persist();
        stop2.persist();
        stop3.persist();
        stop4.persist();
        stop5.persist();
        stop12.persist();
        stop13.persist();
        stop22.persist();
        stop23.persist();
        stop36.persist();
        stop3018.persist();
        stop74.persist();
        stop52.persist();

        row.persist();
        row2.persist();
        row3.persist();
        row4.persist();
        row5.persist();
        row6.persist();

        die = new Die();
        die.setId(1L);
        die.setBpmPartNumber("100A");
        die.setDescription("die");
        die.setPartProductionVolume(PartProductionVolume.HIGH_VOLUME);
        die.persist();

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
        model.setLeftDie(die);
        model.setRightDie(dieR);
        model.persist();

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
        orderMgr.setLeftQueueStop(stop22);
        orderMgr.setRightQueueStop(stop23);
        orderMgr.persist();

        order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(40);
        order.setLeftQuantity(100);
        order.setModel(model);
        order.setOrderStatus(OrderStatus.RetrievingCarriers);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.persist();

        order1 = new WeldOrder();
        order1.setId(2L);
        order1.setOrderMgr(orderMgr);
        order1.setRightQuantity(100);
        order1.setLeftQuantity(100);
        order1.setOrderStatus(OrderStatus.ManuallyCompleted);
        order1.setDeliveryStatus(OrderStatus.Initialized);
        order1.persist();

        WeldOrder weldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101, 1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop4);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setDie(die);
        fulfillment1.setQuantity(new Integer(10));
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 103, 1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop4);
        fulfillment2.setDestination(stop4);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.SELECTED);
        fulfillment2.setDie(die);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();

        OrderFulfillment fulfillment3 = new OrderFulfillment();
        OrderFulfillmentPk pk3 = new OrderFulfillmentPk(weldOrder, 104, 1);
        fulfillment3.setId(pk3);
        fulfillment3.setCurrentLocation(stop4);
        fulfillment3.setDestination(stop4);
        //fulfillment3.setReleaseCycle(1);
        fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
        fulfillment3.setDie(die);
        fulfillment3.setQuantity(new Integer(10));
        fulfillment3.persist();

        OrderFulfillment fulfillment4 = new OrderFulfillment();
        OrderFulfillmentPk pk4 = new OrderFulfillmentPk(weldOrder, 105, 1);
        fulfillment4.setId(pk4);
        fulfillment4.setCurrentLocation(stop4);
        fulfillment4.setDestination(stop4);
        //fulfillment4.setReleaseCycle(1);
        fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
        fulfillment4.setDie(die);
        fulfillment4.setQuantity(new Integer(10));
        fulfillment4.persist();

        for (int i = 106; i < 110; i++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(weldOrder, i, 2);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop22);
            thisFulfillment.setDestination(stop22);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(die);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

		//put 4x1=40 right fulfillments in right queue, stop 23
        for (int i = 206; i < 210; i++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(weldOrder, i, 3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop23);
            thisFulfillment.setDestination(stop23);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieR);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

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
        carriermes4.setStatus(Integer.valueOf(2));
        carriermes4.persist();

        for (int i = 106; i < 110; i++) {
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


		//put 4x1=40 rights in right queue, stop 23
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


        CarrierRelease carrierRelease = new CarrierRelease();
        carrierRelease.setId(104l);
        carrierRelease.setCurrentLocation(stop4);
        carrierRelease.setDestination(stop4);
        carrierRelease.setSource("user");
        carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
        carrierRelease.persist();
    }

}
