package com.honda.mfg.stamp.conveyor.service;

import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.StorageState;

/**
 * User: Jeffrey M Lutz Date: 6/24/11
 */
public interface StorageStateUpdateService {
	void saveCarrier(Carrier carrier);

	void storeReworkCarrier(Carrier carrier);

	StorageState getStorageState();

	void saveCarriersInToRow(List<Carrier> carriers, Stop laneStop);

	void recalculateCarrierDestination(Carrier carrier);

	void releaseEmptyCarriersFromRows(StorageArea area, boolean releaseMgr, String source);

	void removeCarrierFromRow(Integer carrierNumber);

	void addCarrierToRow(Integer carrierNumber, Integer position, Stop laneStop);

	void reloadStorageState();

	void sendBulkCarrierStatusUpdate(List<Carrier> carriers, CarrierStatus status, String user);

	void releaseCarriers(Stop laneStop, int count, Stop destination, String source);

	void reorderCarriersInRow(Long stopId);

	void updateStorageRow(Long rowId);

	void clearAlarm(Integer alarmNumber);

	void resetStorageState();
}
