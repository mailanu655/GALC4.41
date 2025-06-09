package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.helper.Helper;

import java.util.List;

/**
 * User: vcc30690
 * Date: 7/15/11
 */
public interface EmptyCarriersServiceHelper extends Helper {
    Integer getCarrierCountForOldWeldLineEmptyArea();

    Integer getCarrierCountForEmptyArea();

    List<CarrierMes> getCarriersToMoveToEmptyStorageArea(int carrierCount);

    Stop getOldWeldLineEmptyCarrierDeliveryStop();

    Stop getEmptyCarrierDeliveryStop();

    Integer getCarrierCountForBAreaEmptyStorage();

    Stop getBAreaEmptyCarrierDeliveryStop();

    @Deprecated
    boolean activeOrderExistsForOrderMgr(OrderMgr orderMgr);

    boolean anyOrderInProcess();

    boolean anyCarrierSetToReleaseFromRows();

    CarrierMes getCarrier(Integer carrierNo);

    boolean canReleaseCarrier(Integer carrierNo, Stop stop);


}
