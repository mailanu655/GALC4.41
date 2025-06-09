package com.honda.mfg.stamp.storage.service;

import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.BitInfo;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.GroupHoldFinderCriteria;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.ParmSetting;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;

/**
 * User: Jeffrey M Lutz Date: 6/24/11
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

	BitInfo setBitInfo(List<ParmSetting> parms);

	// List<LaneImpl> getStorageRowsForDetailedInventory();

	boolean isDisconnected();

	boolean isConnected();
}
