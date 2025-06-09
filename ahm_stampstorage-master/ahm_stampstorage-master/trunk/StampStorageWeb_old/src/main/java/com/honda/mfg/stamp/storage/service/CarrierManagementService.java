package com.honda.mfg.stamp.storage.service;

import com.honda.mfg.stamp.conveyor.domain.*;

import java.util.List;

/**
 * User: Jeffrey M Lutz
 * Date: 6/24/11
 */
public interface CarrierManagementService {
	
    Integer getPositionInLane(Long currentLocation, Integer carrierNumber);

    List<Stop> getManualOrderCarrierDeliveryStops();

    List<Stop> getValidDestinationStops(Stop stop);

    void reloadStorageState();

    Carrier getCarrier(CarrierMes carrierMes);

    List<Carrier> getGroupHoldCarriers(GroupHoldFinderCriteria finderCriteria, Integer page, Integer size);

    AlarmEvent getAlarmEventToDisplay();

    List<OrderFulfillment> getOrderFulfillmentsByOrder(WeldOrder order);

   // List<LaneImpl> getStorageRowsForDetailedInventory();

	boolean isDisconnected();

	boolean isConnected();
}
