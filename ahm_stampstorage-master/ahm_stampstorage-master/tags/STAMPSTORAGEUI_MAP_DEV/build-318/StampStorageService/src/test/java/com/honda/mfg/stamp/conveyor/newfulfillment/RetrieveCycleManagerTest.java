package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.exceptions.InvalidDieException;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.*;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.AbstractTestBase;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelper;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerHelperImpl;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.empty.EmptyManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreInManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_out.StoreOutManagerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/22/11
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class RetrieveCycleManagerTest extends AbstractTestBase {

    Message msg = null;
    Stop stop1, stop2, stop3, stop4, stop5, stop6, stop7, stop8, stop9, stop10, stop24, stop25;

    @Test
    public void successfullyRetrieveCarriers() {
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        when(helper.getNewCarriersToRelease(Matchers.<WeldOrder>any(), Matchers.<Integer>any())).thenReturn(new ArrayList<Integer>());

        Storage storage = mock(Storage.class);
        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(storage, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(new WeldOrder(), helper, releaseManager);
    }

    @Test
    public void successfullyRetrieveCarriers2() {
        loadTestData();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);

        assertEquals(8, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
    }

    @Test
    public void successfullyRetrieveCarriersWithPrevLeftCycleIncomplete() {
        loadTestData5();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);
        List<OrderFulfillment> carriersCycle3 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 3);

        assertEquals(10, fulfillments.size());
        assertEquals(2, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
        assertEquals(4, carriersCycle3.size());
    }

    @Test
    public void successfullyRetrieveCarriersWithPrevLeftCycleComplete() {
        loadTestData6();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);


        assertEquals(12, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
    }

    @Test
    public void successfullyRetrieveCarriersWithPrevRightCycleIncomplete() {
        loadTestData7();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);
        List<OrderFulfillment> carriersCycle3 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 3);

        assertEquals(14, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(2, carriersCycle2.size());
        assertEquals(4, carriersCycle3.size());
    }

    @Test
    public void successfullyRetrieveCarriersWithPrevRightCycleComplete() {
        loadTestData8();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
        ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        assertEquals(OrderStatus.RetrievingCarriers, (WeldOrder.findWeldOrder(weldOrder.getId())).getOrderStatus());

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(weldOrder);
        List<OrderFulfillment> carriersCycle1 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 1);
        List<OrderFulfillment> carriersCycle2 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 2);
        List<OrderFulfillment> carriersCycle3 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 3);
        List<OrderFulfillment> carriersCycle4 = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(weldOrder, 4);

        assertEquals(16, fulfillments.size());
        assertEquals(4, carriersCycle1.size());
        assertEquals(4, carriersCycle2.size());
        assertEquals(4, carriersCycle3.size());
        assertEquals(4, carriersCycle4.size());
    }


    @Test
    public void successfullyNotRetrieveCarriersIfNoSpaceAvailable() {
        loadTestData2();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);
        ReleaseManager releaseManager = mock(ReleaseManager.class);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        verifyZeroInteractions(releaseManager);
    }

    @Test
    public void successfullyPlacePartsOnBackOrder() {
        loadTestData3();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        assertNotNull(weldOrder);
        Storage storage = getStorage();

        ReleaseManager releaseManager = mock(ReleaseManager.class);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(storage, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        assertEquals(1, storage.getStorageState().getBackOrder().size());

        retrieveCycleManager.process(weldOrder, helper, releaseManager);

        WeldOrder weldOrder1 = WeldOrder.findAllWeldOrders().get(1);
        assertNotNull(weldOrder1);

        retrieveCycleManager.process(weldOrder1, helper, releaseManager);

    }

    @Test
    public void successfullyRecirculateCarriers() {
        loadTestData();
        loadTestParmData();
        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        ReleaseManager releaseManager = mock(ReleaseManager.class);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorageWithBlockedRows(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);
        verify(releaseManager, times(2)).releaseCarrier(Matchers.<Integer>any(), Matchers.<Stop>any(), anyString(), anyBoolean());
    }


    @Test
    public void successfullyRecirculateCarriersWhenOverLimitToRecirculate() {
        loadTestData();
        loadTestParmData();

        NewFulfillmentHelper helper = new NewFulfillmentHelperImpl();
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);

        Die leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        ReleaseManager releaseManager = mock(ReleaseManager.class);
        Storage storage = mock(Storage.class);
        ArrayList<StorageRow> rows = new ArrayList<StorageRow>();
        rows.add(getBlockedLane());
        StorageState storageState = new StorageStateImpl(rows);
        when(storage.getStorageState()).thenReturn(storageState);
        when(storage.retrieve(leftDie)).thenReturn(getBlockedLane());
        when(storage.retrieve(rightDie)).thenThrow(new NoApplicableRuleFoundException(" no row found"));

        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(storage, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");

        retrieveCycleManager.process(weldOrder, helper, releaseManager);

    }


    @Test
    public void successfullyEndRetrieveCycleOnException() {
        loadTestData3();
        loadTestParmData();
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        when(helper.noOrderMgrWithOrderStatus(Matchers.<OrderStatus>any())).thenReturn(true);
        when(helper.getCurrentCapacityOfOrderMgr(Matchers.<OrderMgr>any())).thenReturn(13L);
        when(helper.getCurrentQueueCapacityOfOrderMgr(Matchers.<Stop>any())).thenThrow(new RuntimeException());
        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);
        ReleaseManager releaseManager = mock(ReleaseManager.class);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(getStorage(), "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);
    }

    @Test
    public void successfullyEndRetrieveCycleOnInvalidDieException() {
        loadTestData3();
        loadTestParmData();
        NewFulfillmentHelper helper = mock(NewFulfillmentHelper.class);
        Storage storage = mock(StorageImpl.class);
        StorageState storageState = mock(StorageStateImpl.class);
        when(storageState.getBackOrder()).thenReturn(new ArrayList<BackOrder>());
        when(storage.getStorageState()).thenReturn(storageState);
        when(helper.noOrderMgrWithOrderStatus(Matchers.<OrderStatus>any())).thenReturn(true);
        when(helper.needMoreCarriersWithDie(Matchers.<WeldOrder>any(), Matchers.<Die>any() )).thenReturn(true);
        when(helper.getCurrentCapacityOfOrderMgr(Matchers.<OrderMgr>any())).thenReturn(13L);
        when(helper.getCurrentQueueCapacityOfOrderMgr(Matchers.<Stop>any())).thenReturn(6L);
        when(storage.retrieve(Matchers.<Die>any())).thenThrow(new InvalidDieException(" invalid Die")) ;
        when(helper.getParmValue(Matchers.<String>any())).thenReturn(4);

        WeldOrder weldOrder = WeldOrder.findAllWeldOrders().get(0);
        ReleaseManager releaseManager = mock(ReleaseManager.class);
        RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(storage, "fulfillmentCarrierReleaseCount", "recirculationCarrierReleaseCount");
        retrieveCycleManager.process(weldOrder, helper, releaseManager);
    }


    public void loadTestData() {

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

        Die unavailableDie = new Die();
        unavailableDie.setId(104l);
        unavailableDie.setDescription("unavailable_die_103");
        unavailableDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die emptyDie = new Die();
        emptyDie.setId(999l);
        emptyDie.setDescription("unavailable_die_103");
        emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();
        unavailableDie.persist();
        emptyDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");


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
        orderMgr.setLeftQueueStop(stop24);
        orderMgr.setRightQueueStop(stop25);

        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(80);
        order.setLeftQuantity(80);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();

        OrderMgr orderMgr2 = new OrderMgr();
        orderMgr2.setId(2L);
        orderMgr2.setLineName("line2");
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
        order2.setDeliveryStatus(OrderStatus.Initialized);
        order2.setModel(model);
        order2.persist();

    }

    public void loadTestData2() {

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
        stop24.persist();
        stop25.persist();


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

        OrderMgr orderMgr2 = new OrderMgr();
        orderMgr2.setId(2L);
        orderMgr2.setLineName("line1");
        orderMgr2.setMaxDeliveryCapacity(new Integer(0));
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
        order2.setDeliveryStatus(OrderStatus.Initialized);
        order2.setModel(model);
        order2.persist();
    }

    public void loadTestData3() {
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
        stop24.persist();
        stop25.persist();

        stop10 = new Stop(700L);
        stop10.setName("row");
        stop10.setStopType(StopType.NO_ACTION);
        stop10.setStopArea(StopArea.UNDEFINED);

        stop10.persist();

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

        Die unavailableDie = new Die();
        unavailableDie.setId(104l);
        unavailableDie.setDescription("unavailable_die_103");
        unavailableDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        unavailableDie.persist();

        Model model1 = new Model();
        model1.setName("model");
        model1.setLeftDie(unavailableDie);
        model1.setRightDie(rightDie);
        model1.setDescription("model");
        model1.persist();

        Model model2 = new Model();
        model2.setName("model");
        model2.setLeftDie(leftDie);
        model2.setRightDie(unavailableDie);
        model2.setDescription("model");
        model2.persist();


        OrderMgr orderMgr3 = new OrderMgr();
        orderMgr3.setId(3L);
        orderMgr3.setLineName("line1");
        orderMgr3.setMaxDeliveryCapacity(new Integer(12));
        orderMgr3.setDeliveryStop(stop10);
        orderMgr3.setLeftConsumptionExit(stop10);
        orderMgr3.setLeftConsumptionStop(stop10);
        orderMgr3.setRightConsumptionExit(stop10);
        orderMgr3.setRightConsumptionStop(stop10);
        orderMgr3.setLeftQueueStop(stop24);
        orderMgr3.setRightQueueStop(stop25);
        orderMgr3.persist();

        WeldOrder order3 = new WeldOrder();
        order3.setId(3L);
        order3.setOrderMgr(orderMgr3);
        order3.setRightQuantity(80);
        order3.setLeftQuantity(80);
        order3.setOrderStatus(OrderStatus.InProcess);
        order3.setDeliveryStatus(OrderStatus.Initialized);
        order3.setModel(model1);
        order3.persist();


        OrderMgr orderMgr4 = new OrderMgr();
        orderMgr4.setId(4L);
        orderMgr4.setLineName("line1");
        orderMgr4.setMaxDeliveryCapacity(new Integer(12));
        orderMgr4.setDeliveryStop(stop10);
        orderMgr4.setLeftConsumptionExit(stop10);
        orderMgr4.setLeftConsumptionStop(stop10);
        orderMgr4.setRightConsumptionExit(stop10);
        orderMgr4.setRightConsumptionStop(stop10);
        orderMgr4.setLeftQueueStop(stop24);
        orderMgr4.setRightQueueStop(stop25);
        orderMgr4.persist();

        WeldOrder order4 = new WeldOrder();
        order4.setId(4L);
        order4.setOrderMgr(orderMgr4);
        order4.setRightQuantity(80);
        order4.setLeftQuantity(80);
        order4.setOrderStatus(OrderStatus.InProcess);
        order4.setDeliveryStatus(OrderStatus.Initialized);
        order4.setModel(model2);
        order4.persist();
    }

    public void loadTestData4() {
        stop1 = new Stop(702L);
        stop1.setName("KD");
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

        stop1.persist();
        stop2.persist();
        stop3.persist();
        stop4.persist();
        stop5.persist();
        stop6.persist();
        stop7.persist();
        stop8.persist();
        stop9.persist();


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
        orderMgr.setMaxDeliveryCapacity(new Integer(0));
        orderMgr.setDeliveryStop(stop3);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.persist();

        OrderMgr orderMgr1 = new OrderMgr();
        orderMgr1.setId(2L);
        orderMgr1.setLineName("line2");
        orderMgr1.setMaxDeliveryCapacity(new Integer(0));
        orderMgr1.setDeliveryStop(stop3);
        orderMgr1.setLeftConsumptionExit(stop3);
        orderMgr1.setLeftConsumptionStop(stop3);
        orderMgr1.setRightConsumptionExit(stop3);
        orderMgr1.setRightConsumptionStop(stop3);
        orderMgr1.persist();


        OrderMgr orderMgr2 = new OrderMgr();
        orderMgr2.setId(3L);
        orderMgr2.setLineName("kd-line");
        orderMgr2.setMaxDeliveryCapacity(new Integer(0));
        orderMgr2.setDeliveryStop(stop3);
        orderMgr2.setLeftConsumptionExit(stop3);
        orderMgr2.setLeftConsumptionStop(stop3);
        orderMgr2.setRightConsumptionExit(stop3);
        orderMgr2.setRightConsumptionStop(stop3);
        orderMgr2.persist();

        WeldOrder order2 = new WeldOrder();
        order2.setId(2L);
        order2.setOrderMgr(orderMgr2);
        order2.setRightQuantity(80);
        order2.setLeftQuantity(80);
        order2.setOrderStatus(OrderStatus.InProcess);
        order2.setDeliveryStatus(OrderStatus.Initialized);
        order2.setModel(model);
        order2.persist();

        CarrierMes carriermes1 = new CarrierMes();
        carriermes1.setCarrierNumber(102);
        carriermes1.setCurrentLocation(stop1.getId());
        carriermes1.setDestination(stop1.getId());
        carriermes1.setQuantity(new Integer(0));
        carriermes1.setDieNumber(new Integer(101));
        carriermes1.setOriginationLocation(new Integer(0));
        carriermes1.setProductionRunNumber(new Integer(111));
        carriermes1.persist();

        CarrierMes carriermes3 = new CarrierMes();
        carriermes3.setCarrierNumber(104);
        carriermes3.setCurrentLocation(stop1.getId());
        carriermes3.setDestination(stop1.getId());
        carriermes3.setQuantity(new Integer(0));
        carriermes3.setDieNumber(new Integer(101));
        carriermes3.setOriginationLocation(new Integer(0));
        carriermes3.setProductionRunNumber(new Integer(111));
        carriermes3.persist();

        CarrierMes carriermes4 = new CarrierMes();
        carriermes4.setCarrierNumber(105);
        carriermes4.setCurrentLocation(stop1.getId());
        carriermes4.setDestination(stop1.getId());
        carriermes4.setQuantity(new Integer(0));
        carriermes4.setDieNumber(new Integer(101));
        carriermes4.setOriginationLocation(new Integer(0));
        carriermes4.setProductionRunNumber(new Integer(111));
        carriermes4.persist();

    }

    public Storage getStorage() {
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
        Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
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
        storageLanes.get(10).store(carrier11);
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

    public Storage getStorageWithBlockedRows() {
        Die leftDie, rightDie, someDie, emptyDie;

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

        emptyDie = new Die();
        emptyDie.setId(999l);
        emptyDie.setDescription("empty_die_999");
        emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(200), someDie);

        Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(202), emptyDie);

        Carrier carrier1 = new Carrier(3, oneDayOld, new Integer(90), stop7, stop7, CarrierStatus.SHIPPABLE, new Integer(696), leftDie);
        Carrier carrier11 = new Carrier(4, twoDaysOld, new Integer(99), stop8, stop8, CarrierStatus.SHIPPABLE, new Integer(130), rightDie);
        Carrier carrier12 = new Carrier(5, oneDayOld, new Integer(90), stop9, stop9, CarrierStatus.SHIPPABLE, new Integer(720), leftDie);


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

        storageLanes.get(9).store(carrier1);
        storageLanes.get(10).store(carrier11);
        storageLanes.get(11).store(carrier12);
        storageLanes.get(34).store(carrier2);
        storageLanes.get(34).store(carrier3);
        storageLanes.get(34).store(carrier4);
        storageLanes.get(34).store(carrier42);
        storageLanes.get(34).store(carrier43);
        storageLanes.get(33).store(carrier41);
        storageLanes.get(33).store(carrier44);
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
        return storageRows;
    }


    void loadTestDataForBlockedRows() {
        CarrierMes carriermes1 = new CarrierMes();
        carriermes1.setCarrierNumber(200);
        carriermes1.setCurrentLocation(1235l);
        carriermes1.setDestination(1235l);
        carriermes1.setQuantity(new Integer(0));
        carriermes1.setDieNumber(new Integer(101));
        carriermes1.setOriginationLocation(new Integer(0));
        carriermes1.setProductionRunNumber(new Integer(111));
        carriermes1.persist();

        CarrierMes carriermes3 = new CarrierMes();
        carriermes3.setCarrierNumber(202);
        carriermes3.setCurrentLocation(1235l);
        carriermes3.setDestination(1235l);
        carriermes3.setQuantity(new Integer(0));
        carriermes3.setDieNumber(new Integer(101));
        carriermes3.setOriginationLocation(new Integer(0));
        carriermes3.setProductionRunNumber(new Integer(111));
        carriermes3.persist();
    }

    StorageRow getBlockedLane() {

        Die leftDie, rightDie, someDie;

        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        someDie = new Die();
        someDie.setId(103l);
        someDie.setDescription("right_die_103");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp oneDayOld = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -1);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier1 = new Carrier(8, oneDayOld, new Integer(103), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(201), someDie);
        Carrier carrier2 = new Carrier(1, oneDayOld, new Integer(103), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(200), someDie);
        Carrier carrier3 = new Carrier(2, oneDayOld, new Integer(103), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(202), someDie);
        Carrier carrier4 = new Carrier(6, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(109), leftDie);
        Carrier carrier41 = new Carrier(7, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(596), leftDie);
        Carrier carrier44 = new Carrier(17, oneDayOld, new Integer(100), stop6, stop6, CarrierStatus.SHIPPABLE, new Integer(505), leftDie);

        StorageRow lane = new StorageRow(1l, "lane", 10, 1);
        lane.setStop(new Stop(1));
        lane.setStorageArea(StorageArea.C_LOW);
        lane.store(carrier1);
        lane.store(carrier2);
        lane.store(carrier3);
        lane.store(carrier4);
        lane.store(carrier41);
        lane.store(carrier44);

        return lane;
    }

    public void loadTestData5() {

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

        Die unavailableDie = new Die();
        unavailableDie.setId(104l);
        unavailableDie.setDescription("unavailable_die_103");
        unavailableDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die emptyDie = new Die();
        emptyDie.setId(999l);
        emptyDie.setDescription("unavailable_die_103");
        emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();
        unavailableDie.persist();
        emptyDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");


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
        orderMgr.setLeftQueueStop(stop24);
        orderMgr.setRightQueueStop(stop25);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(80);
        order.setLeftQuantity(80);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();

        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order, 101, 1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop4);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setDie(leftDie);
        fulfillment1.setQuantity(new Integer(10));
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(order, 103, 1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop4);
        fulfillment2.setDestination(stop4);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment2.setDie(leftDie);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();


    }

    public void loadTestData6() {

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

        Die unavailableDie = new Die();
        unavailableDie.setId(104l);
        unavailableDie.setDescription("unavailable_die_103");
        unavailableDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die emptyDie = new Die();
        emptyDie.setId(999l);
        emptyDie.setDescription("unavailable_die_103");
        emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();
        unavailableDie.persist();
        emptyDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");


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
        orderMgr.setLeftQueueStop(stop24);
        orderMgr.setRightQueueStop(stop25);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(80);
        order.setLeftQuantity(80);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();

        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order, 101, 1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop4);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setDie(leftDie);
        fulfillment1.setQuantity(new Integer(10));
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(order, 103, 1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop4);
        fulfillment2.setDestination(stop4);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment2.setDie(leftDie);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();

        OrderFulfillment fulfillment3 = new OrderFulfillment();
        OrderFulfillmentPk pk3 = new OrderFulfillmentPk(order, 104, 1);
        fulfillment3.setId(pk3);
        fulfillment3.setCurrentLocation(stop4);
        fulfillment3.setDestination(stop4);
        //fulfillment3.setReleaseCycle(1);
        fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment3.setDie(leftDie);
        fulfillment3.setQuantity(new Integer(10));
        fulfillment3.persist();

        OrderFulfillment fulfillment4 = new OrderFulfillment();
        OrderFulfillmentPk pk4 = new OrderFulfillmentPk(order, 105, 1);
        fulfillment4.setId(pk4);
        fulfillment4.setCurrentLocation(stop4);
        fulfillment4.setDestination(stop4);
        //fulfillment4.setReleaseCycle(1);
        fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment4.setDie(leftDie);
        fulfillment4.setQuantity(new Integer(10));
        fulfillment4.persist();

    }

    public void loadTestData7() {

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

        Die unavailableDie = new Die();
        unavailableDie.setId(104l);
        unavailableDie.setDescription("unavailable_die_103");
        unavailableDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die emptyDie = new Die();
        emptyDie.setId(999l);
        emptyDie.setDescription("unavailable_die_103");
        emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();
        unavailableDie.persist();
        emptyDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");


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
        orderMgr.setLeftQueueStop(stop24);
        orderMgr.setRightQueueStop(stop25);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(80);
        order.setLeftQuantity(80);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();

        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order, 101, 1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop4);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setDie(leftDie);
        fulfillment1.setQuantity(new Integer(10));
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(order, 103, 1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop4);
        fulfillment2.setDestination(stop4);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment2.setDie(leftDie);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();

        OrderFulfillment fulfillment3 = new OrderFulfillment();
        OrderFulfillmentPk pk3 = new OrderFulfillmentPk(order, 104, 1);
        fulfillment3.setId(pk3);
        fulfillment3.setCurrentLocation(stop4);
        fulfillment3.setDestination(stop4);
        // fulfillment3.setReleaseCycle(1);
        fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment3.setDie(leftDie);
        fulfillment3.setQuantity(new Integer(10));
        fulfillment3.persist();

        OrderFulfillment fulfillment4 = new OrderFulfillment();
        OrderFulfillmentPk pk4 = new OrderFulfillmentPk(order, 105, 1);
        fulfillment4.setId(pk4);
        fulfillment4.setCurrentLocation(stop4);
        fulfillment4.setDestination(stop4);
        //fulfillment4.setReleaseCycle(1);
        fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment4.setDie(leftDie);
        fulfillment4.setQuantity(new Integer(10));
        fulfillment4.persist();

        OrderFulfillment fulfillment5 = new OrderFulfillment();
        OrderFulfillmentPk pk5 = new OrderFulfillmentPk(order, 106, 2);
        fulfillment5.setId(pk5);
        fulfillment5.setCurrentLocation(stop4);
        fulfillment5.setDestination(stop4);
        // fulfillment5.setReleaseCycle(2);
        fulfillment5.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment5.setDie(rightDie);
        fulfillment5.setQuantity(new Integer(10));
        fulfillment5.persist();

        OrderFulfillment fulfillment6 = new OrderFulfillment();
        OrderFulfillmentPk pk6 = new OrderFulfillmentPk(order, 107, 2);
        fulfillment6.setId(pk6);
        fulfillment6.setCurrentLocation(stop4);
        fulfillment6.setDestination(stop4);
        // fulfillment6.setReleaseCycle(2);
        fulfillment6.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
        fulfillment6.setDie(rightDie);
        fulfillment6.setQuantity(new Integer(10));
        fulfillment6.persist();
    }

    public void loadTestData8() {

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

        Die unavailableDie = new Die();
        unavailableDie.setId(104l);
        unavailableDie.setDescription("unavailable_die_103");
        unavailableDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die emptyDie = new Die();
        emptyDie.setId(999l);
        emptyDie.setDescription("unavailable_die_103");
        emptyDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();
        unavailableDie.persist();
        emptyDie.persist();

        Model model = new Model();
        model.setName("model");
        model.setLeftDie(leftDie);
        model.setRightDie(rightDie);
        model.setDescription("model");


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
        orderMgr.setLeftQueueStop(stop24);
        orderMgr.setRightQueueStop(stop25);
        orderMgr.persist();


        WeldOrder order = new WeldOrder();
        order.setId(1L);
        order.setOrderMgr(orderMgr);
        order.setRightQuantity(80);
        order.setLeftQuantity(80);
        order.setOrderStatus(OrderStatus.InProcess);
        order.setDeliveryStatus(OrderStatus.Initialized);
        order.setModel(model);
        order.persist();

        OrderFulfillment fulfillment1 = new OrderFulfillment();
        OrderFulfillmentPk pk1 = new OrderFulfillmentPk(order, 101, 1);
        fulfillment1.setId(pk1);
        fulfillment1.setCurrentLocation(stop4);
        fulfillment1.setDestination(stop4);
        //fulfillment1.setReleaseCycle(1);
        fulfillment1.setDie(leftDie);
        fulfillment1.setQuantity(new Integer(10));
        fulfillment1.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment1.persist();

        OrderFulfillment fulfillment2 = new OrderFulfillment();
        OrderFulfillmentPk pk2 = new OrderFulfillmentPk(order, 103, 1);
        fulfillment2.setId(pk2);
        fulfillment2.setCurrentLocation(stop4);
        fulfillment2.setDestination(stop4);
        //fulfillment2.setReleaseCycle(1);
        fulfillment2.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment2.setDie(leftDie);
        fulfillment2.setQuantity(new Integer(10));
        fulfillment2.persist();

        OrderFulfillment fulfillment3 = new OrderFulfillment();
        OrderFulfillmentPk pk3 = new OrderFulfillmentPk(order, 104, 1);
        fulfillment3.setId(pk3);
        fulfillment3.setCurrentLocation(stop4);
        fulfillment3.setDestination(stop4);
        //fulfillment3.setReleaseCycle(1);
        fulfillment3.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment3.setDie(leftDie);
        fulfillment3.setQuantity(new Integer(10));
        fulfillment3.persist();

        OrderFulfillment fulfillment4 = new OrderFulfillment();
        OrderFulfillmentPk pk4 = new OrderFulfillmentPk(order, 105, 1);
        fulfillment4.setId(pk4);
        fulfillment4.setCurrentLocation(stop4);
        fulfillment4.setDestination(stop4);
        //fulfillment4.setReleaseCycle(1);
        fulfillment4.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment4.setDie(leftDie);
        fulfillment4.setQuantity(new Integer(10));
        fulfillment4.persist();

        OrderFulfillment fulfillment5 = new OrderFulfillment();
        OrderFulfillmentPk pk5 = new OrderFulfillmentPk(order, 106, 2);
        fulfillment5.setId(pk5);
        fulfillment5.setCurrentLocation(stop4);
        fulfillment5.setDestination(stop4);
        // fulfillment5.setReleaseCycle(2);
        fulfillment5.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment5.setDie(rightDie);
        fulfillment5.setQuantity(new Integer(10));
        fulfillment5.persist();

        OrderFulfillment fulfillment6 = new OrderFulfillment();
        OrderFulfillmentPk pk6 = new OrderFulfillmentPk(order, 107, 2);
        fulfillment6.setId(pk6);
        fulfillment6.setCurrentLocation(stop4);
        fulfillment6.setDestination(stop4);
        //fulfillment6.setReleaseCycle(2);
        fulfillment6.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment6.setDie(rightDie);
        fulfillment6.setQuantity(new Integer(10));
        fulfillment6.persist();

        OrderFulfillment fulfillment7 = new OrderFulfillment();
        OrderFulfillmentPk pk7 = new OrderFulfillmentPk(order, 108, 2);
        fulfillment7.setId(pk7);
        fulfillment7.setCurrentLocation(stop4);
        fulfillment7.setDestination(stop4);
        //fulfillment7.setReleaseCycle(2);
        fulfillment7.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment7.setDie(rightDie);
        fulfillment7.setQuantity(new Integer(10));
        fulfillment7.persist();

        OrderFulfillment fulfillment8 = new OrderFulfillment();
        OrderFulfillmentPk pk8 = new OrderFulfillmentPk(order, 109, 2);
        fulfillment8.setId(pk8);
        fulfillment8.setCurrentLocation(stop4);
        fulfillment8.setDestination(stop4);
        //fulfillment8.setReleaseCycle(2);
        fulfillment8.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
        fulfillment8.setDie(rightDie);
        fulfillment8.setQuantity(new Integer(10));
        fulfillment8.persist();
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
        setting1.setFieldvalue("2");
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
