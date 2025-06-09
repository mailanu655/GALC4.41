package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.AbstractTestBase;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelper;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelperImpl;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/21/11
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ReleaseCycleManagerTest extends AbstractTestBase {

    Message message = null;

    @Test
    public void successfullyReleaseCarriers() {
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        when(helper.getNewCarriersToRelease(Matchers.<WeldOrder>any(), Matchers.<Integer>any())).thenReturn(new ArrayList<Integer>());
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(new WeldOrder(), new Integer(1), helper, releaseManager);
    }

    @Test
    public void successfullyWaitForCarriersToRelease() {
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        when(helper.getNewCarriersToRelease(Matchers.<WeldOrder>any(), Matchers.<Integer>any())).thenReturn(null);
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(new WeldOrder(), new Integer(1), helper, releaseManager);
    }

    @Test
    public void successfullyReleaseCarriers2() {
        AnnotationProcessor.process(this);
        loadTestData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.RetrievingCarriers).get(0);
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(weldOrder, new Integer(1), helper, releaseManager);

//        while (message == null) {
//        }
//        assertNotNull(message);

        WeldOrder weldOrder2 = WeldOrder.findWeldOrdersByDeliveryStatus(OrderStatus.DeliveringCarriers).get(0);
        weldOrder2.remove();

        releaseCycleManager.process(weldOrder, new Integer(1), helper, releaseManager);

        releaseCycleManager.process(weldOrder, new Integer(2), helper, releaseManager);

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullyReleaseCarriers3() {
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        ArrayList<Integer> carriers = new ArrayList<Integer>();
        carriers.add(101);
        when(helper.getNewCarriersToRelease(Matchers.<WeldOrder>any(), Matchers.<Integer>any())).thenReturn(carriers);
        when(helper.isCarrierInA(Matchers.<Integer>any())).thenThrow(new RuntimeException());
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(new WeldOrder(), new Integer(1), helper, releaseManager);
    }


    @Test
    public void successfullyReleaseCarriers4() {
        AnnotationProcessor.process(this);
        loadTestData2();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.RetrievingCarriers).get(0);
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(weldOrder, new Integer(1), helper, releaseManager);


        WeldOrder weldOrder2 = WeldOrder.findWeldOrdersByDeliveryStatus(OrderStatus.DeliveringCarriers).get(0);
        weldOrder2.remove();

        releaseCycleManager.process(weldOrder, new Integer(1), helper, releaseManager);

        AnnotationProcessor.unprocess(this);
    }

@Test
    public void successfullyReleaseCarriers5() {
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        ArrayList<Integer> carriers = new ArrayList<Integer>();
        carriers.add(101);
        when(helper.getNewCarriersToRelease(Matchers.<WeldOrder>any(), Matchers.<Integer>any())).thenReturn(carriers);
        when(helper.isCarrierInA(Matchers.<Integer>any())).thenReturn(true);
        when(helper.noOrderWithDeliveryStatus(OrderStatus.DeliveringCarriers)).thenReturn(false);
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(new WeldOrder(), new Integer(1), helper, releaseManager);
    }

    @Test
    public void successfullyReleaseCarriers6() {
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        ArrayList<Integer> carriers = new ArrayList<Integer>();
        carriers.add(101);
        when(helper.getNewCarriersToRelease(Matchers.<WeldOrder>any(), Matchers.<Integer>any())).thenReturn(carriers);
        when(helper.isCarrierInA(Matchers.<Integer>any())).thenReturn(true);
        when(helper.noOrderWithDeliveryStatus(OrderStatus.DeliveringCarriers)).thenReturn(true);
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
        releaseCycleManager.process(new WeldOrder(), new Integer(1), helper, releaseManager);
    }


    @EventSubscriber(eventClass = CarrierUpdateMessage.class)
    public void catchEvent(CarrierUpdateMessage updateMessage) {
        message = updateMessage;
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


        StorageRow row = new StorageRow();
        row.setRowName("ROW1");
        row.setStop(stop4);
        row.setCapacity(12);
        row.setAvailability(StopAvailability.AVAILABLE);
        row.setStorageArea(StorageArea.C_HIGH);

        Stop stop5 = new Stop(1221L);
        stop5.setName("row");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.UNDEFINED);

        StorageRow row1 = new StorageRow();
        row1.setRowName("ROW21");
        row1.setStop(stop5);
        row1.setCapacity(21);
        row1.setAvailability(StopAvailability.AVAILABLE);
        row1.setStorageArea(StorageArea.A_AREA);

        Stop stop24 = new Stop(1224L);
        stop24.setName("row");
        stop24.setStopType(StopType.NO_ACTION);
        stop24.setStopArea(StopArea.Q_WELD_LINE_2);

        StorageRow row2 = new StorageRow();
        row2.setRowName("ROW24");
        row2.setStop(stop24);
        row2.setCapacity(21);
        row2.setAvailability(StopAvailability.AVAILABLE);
        row2.setStorageArea(StorageArea.Q_AREA);

        Stop stop25 = new Stop(1225L);
        stop25.setName("row");
        stop25.setStopType(StopType.NO_ACTION);
        stop25.setStopArea(StopArea.Q_WELD_LINE_2);

        StorageRow row3 = new StorageRow();
        row3.setRowName("ROW25");
        row3.setStop(stop25);
        row3.setCapacity(21);
        row3.setAvailability(StopAvailability.AVAILABLE);
        row3.setStorageArea(StorageArea.A_AREA);

        stop1.persist();
        stop2.persist();
        stop3.persist();
        stop4.persist();
        stop5.persist();
        stop24.persist();
        stop25.persist();

        row.persist();
        row1.persist();
        row2.persist();
        row3.persist();

        OrderMgr orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(12));
        orderMgr.setDeliveryStop(stop3);
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
        order.setRightQuantity(100);
        order.setLeftQuantity(100);
        order.setOrderStatus(OrderStatus.RetrievingCarriers);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.persist();

        WeldOrder weldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101,1);
        fulfillment1.setId(pk1);
        fulfillment1.setDestination(stop4);
       // fulfillment1.setReleaseCycle(1);
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.SELECTED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 102,1);
        fulfillment2.setId(pk2);
        fulfillment2.setDestination(stop5);
       // fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment2.persist();

        CarrierMes carriermes = new CarrierMes();
        carriermes.setCarrierNumber(101);
        carriermes.setCurrentLocation(stop4.getId());
        carriermes.setDestination(stop4.getId());
        carriermes.setBuffer(new Integer(1));
        carriermes.persist();

        CarrierMes carriermes1 = new CarrierMes();
        carriermes1.setCarrierNumber(102);
        carriermes1.setCurrentLocation(stop5.getId());
        carriermes1.setDestination(stop5.getId());
        carriermes1.setBuffer(new Integer(1));
        carriermes1.persist();

        OrderMgr orderMgr2 = new OrderMgr();
        orderMgr2.setId(2L);
        orderMgr2.setLineName("line1");
        orderMgr2.setMaxDeliveryCapacity(new Integer(12));
        orderMgr2.setDeliveryStop(stop3);
        orderMgr2.setLeftConsumptionExit(stop3);
        orderMgr2.setLeftConsumptionStop(stop3);
        orderMgr2.setRightConsumptionExit(stop3);
        orderMgr2.setRightConsumptionStop(stop3);
        orderMgr2.setLeftQueueStop(stop24);
        orderMgr2.setRightQueueStop(stop25);
        orderMgr2.persist();

        WeldOrder order2 = new WeldOrder();
        order2.setId(2L);
        order2.setOrderMgr(orderMgr2);
        order2.setRightQuantity(80);
        order2.setLeftQuantity(80);
        order2.setDeliveryStatus(OrderStatus.DeliveringCarriers);
        order2.setOrderStatus(OrderStatus.InProcess);
        order2.persist();
    }


      public void loadTestData2() {

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

        Stop stop4 = new Stop(1229L);
        stop4.setName("row");
        stop4.setStopType(StopType.NO_ACTION);
        stop4.setStopArea(StopArea.UNDEFINED);

        StorageRow row = new StorageRow();
        row.setRowName("ROW21");
        row.setStop(stop4);
        row.setCapacity(21);
        row.setAvailability(StopAvailability.AVAILABLE);
        row.setStorageArea(StorageArea.A_AREA);

        Stop stop5 = new Stop(1221L);
        stop5.setName("row");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.UNDEFINED);

        StorageRow row1 = new StorageRow();
        row1.setRowName("ROW21");
        row1.setStop(stop5);
        row1.setCapacity(21);
        row1.setAvailability(StopAvailability.AVAILABLE);
        row1.setStorageArea(StorageArea.A_AREA);

        Stop stop24 = new Stop(1224L);
        stop24.setName("row");
        stop24.setStopType(StopType.NO_ACTION);
        stop24.setStopArea(StopArea.Q_WELD_LINE_2);

        StorageRow row2 = new StorageRow();
        row2.setRowName("ROW24");
        row2.setStop(stop24);
        row2.setCapacity(21);
        row2.setAvailability(StopAvailability.AVAILABLE);
        row2.setStorageArea(StorageArea.Q_AREA);

        Stop stop25 = new Stop(1225L);
        stop25.setName("row");
        stop25.setStopType(StopType.NO_ACTION);
        stop25.setStopArea(StopArea.Q_WELD_LINE_2);

        StorageRow row3 = new StorageRow();
        row3.setRowName("ROW25");
        row3.setStop(stop25);
        row3.setCapacity(21);
        row3.setAvailability(StopAvailability.AVAILABLE);
        row3.setStorageArea(StorageArea.A_AREA);
        
        stop1.persist();
        stop2.persist();
        stop3.persist();
        stop4.persist();
        stop5.persist();
        stop24.persist();
        stop25.persist();

        OrderMgr orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(12));
        orderMgr.setDeliveryStop(stop3);
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
        order.setRightQuantity(100);
        order.setLeftQuantity(100);
        order.setOrderStatus(OrderStatus.RetrievingCarriers);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.persist();

        WeldOrder weldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(weldOrder, 101,1);
        fulfillment1.setId(pk1);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.SELECTED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(weldOrder, 102,1);
        fulfillment2.setId(pk2);
        fulfillment2.setDestination(stop5);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment2.persist();

        CarrierMes carriermes = new CarrierMes();
        carriermes.setCarrierNumber(101);
        carriermes.setCurrentLocation(stop4.getId());
        carriermes.setDestination(stop4.getId());
        carriermes.setBuffer(new Integer(1));
        carriermes.setStatus(new Integer(0));
        carriermes.persist();

        CarrierMes carriermes1 = new CarrierMes();
        carriermes1.setCarrierNumber(102);
        carriermes1.setCurrentLocation(stop5.getId());
        carriermes1.setDestination(stop5.getId());
        carriermes1.setBuffer(new Integer(1));
        carriermes1.setStatus(new Integer(2));
        carriermes1.persist();

        OrderMgr orderMgr2 = new OrderMgr();
        orderMgr2.setId(2L);
        orderMgr2.setLineName("line1");
        orderMgr2.setMaxDeliveryCapacity(new Integer(12));
        orderMgr2.setDeliveryStop(stop3);
        orderMgr2.setLeftConsumptionExit(stop3);
        orderMgr2.setLeftConsumptionStop(stop3);
        orderMgr2.setRightConsumptionExit(stop3);
        orderMgr2.setRightConsumptionStop(stop3);
        orderMgr2.setLeftQueueStop(stop24);
        orderMgr2.setRightQueueStop(stop25);
        orderMgr2.persist();

        WeldOrder order2 = new WeldOrder();
        order2.setId(2L);
        order2.setOrderMgr(orderMgr2);
        order2.setRightQuantity(80);
        order2.setLeftQuantity(80);
        order2.setOrderStatus(OrderStatus.InProcess);
        order2.setDeliveryStatus(OrderStatus.DeliveringCarriers);
        order2.persist();
    }

}
