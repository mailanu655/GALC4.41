package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: vcc30690
 * Date: 7/15/11
 */
public class ReleaseEmptyCarriersServiceImplTest {


    @Test
    public void runReleaseEmptyCarrierService() {

        EmptyCarriersServiceHelper helper = mock(EmptyCarriersServiceHelper.class);
        ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
        when(helper.getCarrierCountForEmptyArea()).thenReturn(5)
                .thenReturn(8)
                .thenReturn(10)
                .thenReturn(10)
                .thenReturn(11)
                .thenThrow(new RuntimeException());
        when(helper.getCarriersToMoveToEmptyStorageArea(anyInt())).thenReturn(getCarrierMesList(3))
                .thenReturn(getCarrierMesList(2))
                .thenReturn(getCarrierMesList(0))
                .thenReturn(getCarrierMesList(3))
                .thenThrow(new RuntimeException());
        when(helper.getEmptyCarrierDeliveryStop()).thenReturn(new Stop());

        when(serviceRoleWrap.isPassive()).thenReturn(false);
        ReleaseManager releaseManager = mock(ReleaseManager.class);
         when(helper.getParmValue("emptyAreaUnderFilledQuantity")).thenReturn(50) ;
        when(helper.getParmValue("releaseCarrierCount")).thenReturn(6);
        ReleaseEmptyCarriersService releaseEmptyCarriersService = new ReleaseEmptyCarriersServiceImpl(helper, releaseManager, "emptyAreaUnderFilledQuantity", "releaseCarrierCount");
        releaseEmptyCarriersService.setServiceRoleWrapper(serviceRoleWrap);

        releaseEmptyCarriersService.run();
        releaseEmptyCarriersService.run();
        releaseEmptyCarriersService.run();
        releaseEmptyCarriersService.run();
        releaseEmptyCarriersService.run();
        releaseEmptyCarriersService.run();
    }

     @Test
    public void runReleaseEmptyCarrierServiceWhenNoDeliveryStopDefined() {

        EmptyCarriersServiceHelper helper = mock(EmptyCarriersServiceHelper.class);
        ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
        when(helper.getCarrierCountForEmptyArea()).thenReturn(5);
        when(helper.getEmptyCarrierDeliveryStop()).thenReturn(null);

        when(serviceRoleWrap.isPassive()).thenReturn(false).thenReturn(true);
        ReleaseManager releaseManager = mock(ReleaseManager.class);
         when(helper.getParmValue("emptyAreaUnderFilledQuantity")).thenReturn(50) ;
        when(helper.getParmValue("releaseCarrierCount")).thenReturn(6);
        ReleaseEmptyCarriersService releaseEmptyCarriersService = new ReleaseEmptyCarriersServiceImpl(helper, releaseManager, "emptyAreaUnderFilledQuantity", "releaseCarrierCount");
        releaseEmptyCarriersService.setServiceRoleWrapper(serviceRoleWrap);

        releaseEmptyCarriersService.run();
        releaseEmptyCarriersService.run();

    }

    List<CarrierMes> getCarrierMesList(int count) {
        int quantity = 0;
        Long location = 5200L;
        int dieNumber = 999;

        List<CarrierMes> carrierList = new ArrayList<CarrierMes>();
        for (int i = 1; i <= count; i++) {
            CarrierMes carriermes = new CarrierMes();
            carriermes.setCarrierNumber(i);
            carriermes.setCurrentLocation(location);
            carriermes.setDestination(location);
            carriermes.setDieNumber(dieNumber);
            carriermes.setQuantity(quantity);
            carrierList.add(carriermes);
        }
        return carrierList;
    }


}
