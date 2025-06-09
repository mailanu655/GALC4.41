package com.honda.mfg.stamp.storage.service;

import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

public interface CarrierManagementServiceProxy {
	void saveCarrier(Carrier carrier);

	void sendBulkCarrierStatusUpdate(List<Carrier> carriers, CarrierStatus status, String user);

	void recalculateCarrierDestination(Carrier carrier);

	void removeCarrierFromRow(Integer carrierNum);

	void addCarrierToRow(Integer carrierNumber, Integer position, Stop laneStop);

	void saveCarriersInToRow(List<Integer> carrierNumberList, Stop laneStop);

	void releaseEmptyCarriersFromRows(StorageArea area, boolean releaseMgr, String source);

	void releaseCarriers(StorageRow lane, Integer releaseCount, Stop destination, String source);

	void reorderCarriersInRow(Long stopId);

	void storeReworkCarrier(Carrier carrier);

	void updateRow(StorageRow row);

	void refreshStorageState();

	void resetStorageState();

	void clearAlarm(Integer alarmNumber);
}
