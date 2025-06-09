package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.conveyor.release.ReleaseManagerImpl;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: vcc30690
 * Date: 7/15/11
 */
public class OrderEmptyCarriersToBAreaEmptyStorageServiceImplTest {


    @Test
    public void runOrderEmptyCarrierService() {

        Stop stop = new Stop(1236l);
        stop.setStopArea(StopArea.ROW);
        StorageRow lane = new StorageRow(1l, "lane", 5, 1);
        lane.setStop(stop);

        Stop stop1 = new Stop(803l);
        stop1.setStopArea(StopArea.STORE_IN_ROUTE);


        CarrierMes carrierMes = new CarrierMes();
        carrierMes.setCarrierNumber(1);
        carrierMes.setCurrentLocation(1236l);
        carrierMes.setDestination(1236l);

        CarrierMes carrierMes2 = new CarrierMes();
        carrierMes2.setCarrierNumber(1);
        carrierMes2.setCurrentLocation(803l);
        carrierMes2.setDestination(1236l);

        Carrier carrier = new Carrier();
        carrier.setCarrierNumber(1);
        carrier.setCurrentLocation(stop);
        carrier.setDestination(stop);

        Carrier carrier2 = new Carrier();
        carrier2.setCarrierNumber(2);
        carrier2.setCurrentLocation(stop1);
        carrier2.setDestination(stop);

        lane.store(carrier);
        lane.store(carrier2);

        EmptyCarriersServiceHelper helper = mock(EmptyCarriersServiceHelper.class);
        Storage storage = mock(Storage.class);
        when(storage.retrieveEmptyCarrierForBAreaEmptyStorage()).thenReturn(lane).thenThrow(new NoApplicableRuleFoundException("no applicable rule found"));
        StorageState storageState = mock(StorageState.class);
        when(storage.getStorageState()).thenReturn(storageState);

        when(helper.getCarrierCountForBAreaEmptyStorage()).thenReturn(5).thenReturn(10).thenReturn(7).thenThrow(new RuntimeException("some exception"));
         ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
         when(serviceRoleWrap.isPassive()).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true);

        ReleaseManager releaseManager = mock(ReleaseManager.class);

        when(helper.getBAreaEmptyCarrierDeliveryStop()).thenReturn(new Stop());
        when(helper.canReleaseCarrier(Matchers.<Integer>any(), Matchers.<Stop>any())).thenReturn(true);
        when(helper.getCarrier(Matchers.<Integer>any())).thenReturn(carrierMes).thenReturn(carrierMes2);
        when(helper.getParmValue(Matchers.anyString())).thenReturn(6) ;
        OrderEmptyCarriersService emptyCarriersService = new OrderEmptyCarriersToBAreaEmptyStorageServiceImpl(helper, storage, releaseManager, "bAreaEmptyCarrierLowLimit", "emptyCarrierReleaseCount");
        emptyCarriersService.setServiceRoleWrapper(serviceRoleWrap);

        emptyCarriersService.run();
        emptyCarriersService.run();
        emptyCarriersService.run();
        emptyCarriersService.run();
        emptyCarriersService.run();
    }

    @Test
    public void runOrderEmptyCarrierServiceBAreaNotUnderFilled() {


        EmptyCarriersServiceHelper helper = mock(EmptyCarriersServiceHelper.class);
        Storage storage = mock(Storage.class);
        StorageState storageState = mock(StorageState.class);
        when(storage.getStorageState()).thenReturn(storageState);

        when(helper.getCarrierCountForBAreaEmptyStorage()).thenReturn(10)
                .thenThrow(new RuntimeException()).thenReturn(7).thenReturn(7).thenReturn(10);
        when(helper.anyOrderInProcess()).thenReturn(false).thenReturn(false).thenReturn(true).thenReturn(false).thenReturn(true);
        when(helper.anyCarrierSetToReleaseFromRows()).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true).thenReturn(true);
         ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
         when(serviceRoleWrap.isPassive()).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true);
        ReleaseManager releaseManager = mock(ReleaseManagerImpl.class);
        when(helper.getBAreaEmptyCarrierDeliveryStop()).thenReturn(new Stop());
        when(helper.getParmValue(Matchers.anyString())).thenReturn(6) ;
        OrderEmptyCarriersService emptyCarriersService = new OrderEmptyCarriersToBAreaEmptyStorageServiceImpl(helper, storage, releaseManager, "bAreaEmptyCarrierLowLimit", "emptyCarrierReleaseCount");
        emptyCarriersService.setServiceRoleWrapper(serviceRoleWrap);

        emptyCarriersService.run();
        emptyCarriersService.run();
        emptyCarriersService.run();
        emptyCarriersService.run();
        emptyCarriersService.run();

    }

       @Test
       public void runOrderEmptyCarrierServiceOLdWeldLIneWhenDeliveryStopNotSet() {

           EmptyCarriersServiceHelper helper = mock(EmptyCarriersServiceHelper.class);
           Storage storage = mock(Storage.class);
           StorageState storageState = mock(StorageState.class);
           when(storage.getStorageState()).thenReturn(storageState);

            ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
            when(serviceRoleWrap.isPassive()).thenReturn(false);
           ReleaseManager releaseManager = mock(ReleaseManagerImpl.class);
           when(helper.getBAreaEmptyCarrierDeliveryStop()).thenReturn(null);
            when(helper.getParmValue(Matchers.anyString())).thenReturn(6) ;
           OrderEmptyCarriersService emptyCarriersService = new OrderEmptyCarriersToBAreaEmptyStorageServiceImpl(helper, storage, releaseManager, "10", "6");
            emptyCarriersService.setServiceRoleWrapper(serviceRoleWrap);

           emptyCarriersService.run();

       }


}
