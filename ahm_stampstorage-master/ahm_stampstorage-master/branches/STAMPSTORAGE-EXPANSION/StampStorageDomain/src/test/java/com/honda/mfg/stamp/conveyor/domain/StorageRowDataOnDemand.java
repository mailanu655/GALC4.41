package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class StorageRowDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<StorageRow> data;

	@Autowired
	private StopDataOnDemand stopDataOnDemand;

	public StorageRow getNewTransientStorageRow(int index) {
		com.honda.mfg.stamp.conveyor.domain.StorageRow obj = new com.honda.mfg.stamp.conveyor.domain.StorageRow();
		setRowName(obj, index);
		setStop(obj, index);
		setCapacity(obj, index);
		setStorageArea(obj, index);
		setAvailability(obj, index);
		return obj;
	}

	public void setRowName(StorageRow obj, int index) {
		java.lang.String rowName = "rowName_" + index;
		obj.setRowName(rowName);
	}

	public void setStop(StorageRow obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.Stop stop = stopDataOnDemand.getRandomStop();
		obj.setStop(stop);
	}

	public void setCapacity(StorageRow obj, int index) {
		java.lang.Integer capacity = new Integer(index);
		obj.setCapacity(capacity);
	}

	public void setStorageArea(StorageRow obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.enums.StorageArea storageArea = com.honda.mfg.stamp.conveyor.domain.enums.StorageArea.class
				.getEnumConstants()[0];
		obj.setStorageArea(storageArea);
	}

	public void setAvailability(StorageRow obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability availability = com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability.class
				.getEnumConstants()[0];
		obj.setAvailability(availability);
	}

	public StorageRow getSpecificStorageRow(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		StorageRow obj = data.get(index);
		return StorageRow.findStorageRow(obj.getId());
	}

	public StorageRow getRandomStorageRow() {
		init();
		StorageRow obj = data.get(rnd.nextInt(data.size()));
		return StorageRow.findStorageRow(obj.getId());
	}

	public boolean modifyStorageRow(StorageRow obj) {
		return false;
	}

	public void init() {
		data = com.honda.mfg.stamp.conveyor.domain.StorageRow.findStorageRowEntries(0, 10);
		if (data == null)
			throw new IllegalStateException("Find entries implementation for 'StorageRow' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.StorageRow>();
		for (int i = 0; i < 10; i++) {
			com.honda.mfg.stamp.conveyor.domain.StorageRow obj = getNewTransientStorageRow(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
