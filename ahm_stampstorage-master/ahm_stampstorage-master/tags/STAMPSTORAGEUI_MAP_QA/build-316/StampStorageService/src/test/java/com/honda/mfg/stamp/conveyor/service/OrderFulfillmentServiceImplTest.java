package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageImpl;
import com.honda.mfg.stamp.conveyor.newfulfillment.*;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * User: VCC30690
 * Date: 9/9/11
 */
public class OrderFulfillmentServiceImplTest {

    @Test
    public void successfullyRunOrderFulfillmentService(){

        ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
        OrderMgr orderMgr = new OrderMgr();
        orderMgr.setLineName("orderMgr");

        Storage storage = mock(StorageImpl.class);
        NewFulfillmentHelper helper = mock(NewFulfillmentHelperImpl.class);
        ReleaseManager releaseManager = mock(ReleaseManagerImpl.class);
         Fulfillment fulfillment = new OrderFulfillmentManager(orderMgr,storage,helper,releaseManager,"fulfillmentCycleSize", "recirculationCarrierReleaseCount");

       // when(fulfillment.getOrderManager()).thenReturn(orderMgr);
        when(serviceRoleWrap.isPassive()).thenReturn(false).thenReturn(true);
        OrderFulfillmentService service = new OrderFulfillmentServiceImpl(fulfillment);
        service.setServiceRoleWrap(serviceRoleWrap);
        service.run();

        Fulfillment delivery = new DeliveryManager(orderMgr,helper,releaseManager,"deliveryCycleSize","fulfillmentCarrierInspectionStop") ;
         when(serviceRoleWrap.isPassive()).thenReturn(false).thenReturn(true);
        OrderFulfillmentService service1 = new OrderFulfillmentServiceImpl(delivery);
        service1.setServiceRoleWrap(serviceRoleWrap);
        service1.run();

    }
}
