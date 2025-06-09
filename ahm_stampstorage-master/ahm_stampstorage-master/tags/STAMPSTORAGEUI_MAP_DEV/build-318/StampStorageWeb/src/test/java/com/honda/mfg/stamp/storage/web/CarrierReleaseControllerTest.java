package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierReleaseControllerTest {

    @Test
    public void successfullyTestCarrierReleaseController() {
        loadTestData();
        CarrierReleaseController controller = new CarrierReleaseController();
        assertNotNull(controller.populateCarrierReleases());
        assertNotNull(controller.populateStops());

    }

    public void loadTestData() {
        Die die, emptyDie;
        OrderMgr orderMgr;
        WeldOrder order, order1;

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
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101,1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop4);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 103,1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop4);
        fulfillment2.setDestination(stop4);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.SELECTED);
        fulfillment2.setDie(die);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();

        OrderFulfillment fulfillment3 = new OrderFulfillment();
        OrderFulfillmentPk pk3 = new OrderFulfillmentPk(weldOrder, 104,1);
        fulfillment3.setId(pk3);
        fulfillment3.setCurrentLocation(stop4);
        fulfillment3.setDestination(stop4);
        //fulfillment3.setReleaseCycle(1);
        fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
        fulfillment3.setDie(die);
        fulfillment3.setQuantity(new Integer(10));
        fulfillment3.persist();

        OrderFulfillment fulfillment4 = new OrderFulfillment();
        OrderFulfillmentPk pk4 = new OrderFulfillmentPk(weldOrder, 105,1);
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
        carrierRelease.setCurrentLocation(stop4);
        carrierRelease.setDestination(stop4);
        carrierRelease.setSource("user");
        carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
        carrierRelease.persist();
    }
}
