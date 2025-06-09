package com.honda.mfg.stamp.conveyor.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

/**
 * User: Jeffrey M Lutz Date: 6/22/11
 */
public class StorageStateContextMock implements StorageStateContext, StorageLifeCycle {
	private StorageState storageState;

	private Carrier carrier1, carrier11, carrier12, carrier2, carrier3, carrier4, carrier41, carrier42, carrier43,
			carrier5, carrier51, carrier52, carrier53, carrier54;

	private HashMap<Long, StorageRow> lastUsedRowMap;

	public StorageStateContextMock(StorageState storageState) {
		this.storageState = storageState;
		this.lastUsedRowMap = new HashMap<Long, StorageRow>();
	}

	@Override
	public void reload() {
		Die leftDie = new Die();
		leftDie.setId(101L);
		leftDie.setDescription("left_die_101");
		leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Die rightDie = new Die();
		rightDie.setId(102L);
		rightDie.setDescription("right_die_102");
		rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Die someDie = new Die();
		someDie.setId(103L);
		someDie.setDescription("right_die_103");
		someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		Calendar c = Calendar.getInstance();
		Timestamp today = new Timestamp(c.getTimeInMillis());

		c.add(Calendar.DATE, -2);

		Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

		carrier2 = new Carrier(1, today, 103, new Stop("ST12-32"), null, CarrierStatus.SHIPPABLE, new Integer(200),
				someDie);
		carrier3 = new Carrier(2, today, 103, new Stop("ST12-33"), null, CarrierStatus.SHIPPABLE, new Integer(202),
				someDie);

		carrier1 = new Carrier(3, twoDaysOld, 90, new Stop("ST12-10"), null, CarrierStatus.SHIPPABLE, new Integer(696),
				leftDie);
		carrier11 = new Carrier(4, twoDaysOld, new Integer(99), new Stop("ST12-11"), null, CarrierStatus.SHIPPABLE,
				new Integer(130), rightDie);
		carrier12 = new Carrier(5, twoDaysOld, 90, new Stop("ST12-12"), null, CarrierStatus.SHIPPABLE, new Integer(720),
				leftDie);

		carrier4 = new Carrier(6, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(728), leftDie);
		carrier41 = new Carrier(7, today, new Integer(100), new Stop("ST12-35"), null, CarrierStatus.SHIPPABLE,
				new Integer(743), leftDie);
		carrier42 = new Carrier(8, twoDaysOld, 90, new Stop("ST12-34"), null, CarrierStatus.SHIPPABLE, new Integer(35),
				leftDie);
		carrier43 = new Carrier(9, twoDaysOld, 90, new Stop("ST12-34"), null, CarrierStatus.SHIPPABLE, new Integer(425),
				leftDie);

		carrier5 = new Carrier(10, today, new Integer(101), new Stop("ST12-31"), null, CarrierStatus.SHIPPABLE,
				new Integer(131), rightDie);
		carrier51 = new Carrier(11, today, new Integer(101), new Stop("ST12-31"), null, CarrierStatus.SHIPPABLE,
				new Integer(203), rightDie);
		carrier52 = new Carrier(12, twoDaysOld, new Integer(99), new Stop("ST12-30"), null, CarrierStatus.SHIPPABLE,
				new Integer(126), rightDie);
		carrier53 = new Carrier(13, twoDaysOld, new Integer(99), new Stop("ST12-30"), null, CarrierStatus.SHIPPABLE,
				new Integer(465), rightDie);
		carrier54 = new Carrier(14, twoDaysOld, new Integer(99), new Stop("ST12-30"), null, CarrierStatus.SHIPPABLE,
				new Integer(605), rightDie);

		List<StorageRow> storageLanes = getStorageRows();

		storageLanes.get(31).store(carrier2);
		storageLanes.get(32).store(carrier3);

		storageLanes.get(9).store(carrier1);
		storageLanes.get(10).store(carrier11);
		storageLanes.get(11).store(carrier12);

		storageLanes.get(34).store(carrier4);
		storageLanes.get(34).store(carrier41);
		storageLanes.get(33).store(carrier42);
		storageLanes.get(33).store(carrier43);

		storageLanes.get(30).store(carrier5);
		storageLanes.get(30).store(carrier51);
		storageLanes.get(29).store(carrier52);
		storageLanes.get(29).store(carrier53);
		storageLanes.get(29).store(carrier54);

		this.storageState = new StorageStateImpl(storageLanes);
	}

	@Override
	public void addDie(Long dieNumber, StorageRow row) {
		lastUsedRowMap.put(dieNumber, row);
	}

	@Override
	public StorageRow getRow(Die die) {
		return lastUsedRowMap.get(die.getId()); // To change body of implemented methods use File | Settings | File
												// Templates.
	}

	@Override
	public void saveToAuditLog(String nodeId, String message, String source) {
		System.out.println(message);
	}

	@Override
	public Die getEmptyDie() {
		return new Die(999l, PartProductionVolume.EMPTY); // To change body of implemented methods use File | Settings |
															// File Templates.
	}

	@Override
	public boolean spaceAvailable(Stop stop) {
		return false; // To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public StorageState getStorageState() {
		return this.storageState;
	}

	@Override
	public List<CarrierMes> getCarriersWithInvalidDestination() {
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Carrier populateCarrier(CarrierMes carrierMes) {
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public CarrierMes getCarrier(Integer carrierNumber) {
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}

	List<StorageRow> getStorageRows() {
		List<StorageRow> storageRows = new ArrayList<StorageRow>();

		for (int i = 0; i < 35; i++) {
			StorageRow row = new StorageRow(i, "Row-" + i, 10, 1);

			storageRows.add(row);
		}

		return storageRows;
	}

}
