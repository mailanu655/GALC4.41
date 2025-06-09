package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;

import org.aspectj.lang.annotation.Before;
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
public class QueueScenariosTest {
    WeldOrder order = null, order1 = null, order2 = null;
    OrderMgr orderMgr = null;
    Die dieL = null, emptyDie = null, dieR = null;
    Model model = null;
    Stop stop22 = null, stop23 = null;
    Stop stop5=null, stop37=null,  stop44=null, stop18=null, stop24=null;

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
    public void testNeedMoreCarriersForDieWithDeliveringOrder() {
        loadTestData1();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        assertEquals(true, helper.needMoreCarriersWithDie(order, dieL));
        assertEquals(false, helper.needMoreCarriersWithDie(order, dieR));
        
        order.setLeftQuantity(40);
        assertEquals(false, helper.needMoreCarriersWithDie(order, dieL));
        
    }

    void loadStops()  {
        stop22 = new Stop(1222L);
        stop22.setName("Row-22");
        stop22.setStopType(StopType.NO_ACTION);
        stop22.setStopArea(StopArea.Q_WELD_LINE_1);

        stop23 = new Stop(1223L);
        stop23.setName("Row-23");
        stop23.setStopType(StopType.NO_ACTION);
        stop23.setStopArea(StopArea.Q_WELD_LINE_1);

        stop5 = new Stop(5L);
        stop5.setName("WE1-Deelivery");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.WELD_LINE_1);

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
        
        stop24 = new Stop(24L);
        stop24.setName("Right Consumption Exit");
        stop24.setStopType(StopType.NO_ACTION);
        stop24.setStopArea(StopArea.WELD_LINE_1);

        stop22.persist();
        stop23.persist();
        stop5.persist();
        stop37.persist();
        stop44.persist();
        stop18.persist();
        stop24.persist();
    	
    }
    
    void loadRows()  {
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
    
    void loadDies()  {
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
    
    void loadOrderMgr()  {
        orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(12));
        orderMgr.setDeliveryStop(stop5);
        orderMgr.setLeftConsumptionExit(stop44);
        orderMgr.setLeftConsumptionStop(stop37);
        orderMgr.setRightConsumptionExit(stop24);
        orderMgr.setRightConsumptionStop(stop18);
        orderMgr.setLeftQueueStop(stop22);
        orderMgr.setRightQueueStop(stop23);
        orderMgr.persist();
    	
    }
    
    public void loadTestData1() {

    	//open order
        order = new WeldOrder();
        //order.setId(5L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(70);
        order.setLeftQuantity(70);
        order.setModel(model);
        order.setOrderStatus(OrderStatus.RetrievingCarriers);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.persist();

        //manually completed order with left-over carriers in queue
        order1 = new WeldOrder();
        //order1.setId(4L);
        order1.setOrderMgr(orderMgr);
        order1.setRightQuantity(40);
        order1.setLeftQuantity(100);
        order1.setModel(model);
        order1.setOrderStatus(OrderStatus.ManuallyCompleted);
        order1.setDeliveryStatus(OrderStatus.ManuallyCompleted);
        order1.persist();

        //Open order delivering
        order2 = new WeldOrder();
        //order2.setId(3L);
        order2.setOrderMgr(orderMgr);
        order2.setRightQuantity(40);
        order2.setLeftQuantity(40);
        order2.setModel(model);
        order2.setOrderStatus(OrderStatus.AutoCompleted);
        order2.setDeliveryStatus(OrderStatus.DeliveringCarriers);
        order2.persist();

        //put 4 carriers LEFT
        for (int i = 101, j = 0; i < 105; i++, j++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order2, i, 1+j/3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop22);
            thisFulfillment.setDestination(stop22);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieL);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

        //put 4 carriers RIGHT
        for (int i = 201, j = 6; i < 205; i++, j++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order2, i, 1+j/3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop23);
            thisFulfillment.setDestination(stop23);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieR);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

        ///put 4 left carriers in queue leftover from old order
        for (int i = 105, j = 0; i < 109; i++, j++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1+j/3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop22);
            thisFulfillment.setDestination(stop22);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieL);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

        //put 9 right carriers in queue leftover from old order
        for (int i = 205, j=6; i < 214; i++,j++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1+j/3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop23);
            thisFulfillment.setDestination(stop23);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieR);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

		//put 14x10=140 in left queue, stop 22
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

		//put 8x10=80 rights in right queue, stop 23
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

        WeldOrder oldOrder = WeldOrder.findWeldOrder(1L); //old order
        ///put 9 left carriers in queue leftover from old order
        for (int i = 101, j = 0; i < 110; i++, j++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1+j/3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop22);
            thisFulfillment.setDestination(stop22);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieL);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

        ///put 4 right carriers in queue leftover from old order
        for (int i = 206, j=9; i < 210; i++,j++) {
            OrderFulfillment thisFulfillment = new OrderFulfillment();
            OrderFulfillmentPk thisPk = new OrderFulfillmentPk(order1, i, 1+j/3);
            thisFulfillment.setId(thisPk);
            thisFulfillment.setCurrentLocation(stop23);
            thisFulfillment.setDestination(stop23);
            thisFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
            thisFulfillment.setDie(dieR);
            thisFulfillment.setQuantity(new Integer(10));
            thisFulfillment.persist();
        }

		//put 9x10=90 rights in left queue, stop 22
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


		//put 4x10=40 rights in right queue, stop 23
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

}
