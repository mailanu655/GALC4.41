package com.honda.mfg.stamp.conveyor.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;

/**
 * User: Jeffrey M Lutz Date: 6/20/11
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class StorageStateContextImplTest {

	@Test
	public void successfullyCreateValidStorageState() {
		// Pre-conditions
		loadDieTable();
		loadStopTable();
		loadCarrierMesTable();
		StorageStateContextHelper helper = new StorageStateContextHelperImpl();
		StorageStateContextImpl storageStateContextImpl = new StorageStateContextImpl(helper);
		StorageStateContext storageStateContext = storageStateContextImpl;
		StorageLifeCycle storageLifeCycle = storageStateContextImpl;
		StorageState expectedStorageState = getExpectedState();

		// when(helper.findAllCarriersInStorage()).thenReturn(getCarrierMesList());

		// Perform test
		storageLifeCycle.reload();
		StorageState actualStorageState = storageStateContext.getStorageState();

		// Post-conditions / assertions / validations
		// assertEquals(expectedStorageState, actualStorageState);
	}

	@Test
	public void successfullyCreateValidStorageStateBackedByDatabase() {
		// Pre-conditions
		loadDieTable();
		loadStopTable();
		loadCarrierMesTable();

		StorageStateContextHelper helper = null;
		helper = new StorageStateContextHelperImpl();
		StorageStateContextImpl storageStateContextImpl = new StorageStateContextImpl(helper);

		// Perform test
		StorageState storageState = storageStateContextImpl.getStorageState();

		// Post-conditions / assertions

		assertNotNull(storageState.getRows());
		StorageRow lane1 = storageState.queryForRow(RowMatchers.hasMaxCapacityOf(12));
		assertEquals(12, lane1.getCurrentCarrierCount());

		assertNotNull(storageState);
	}

	@Test
	public void successfullyReceiveStaleDataMessageStorageStateStale() {
		// Pre-conditions
		AnnotationProcessor.process(this);
		loadDieTable();
		loadStopTable();
		loadCarrierMesTable();

		StorageStateContextHelper helper = null;
		helper = new StorageStateContextHelperImpl();
		StorageStateContextImpl storageStateContextImpl = new StorageStateContextImpl(helper);
		StorageState storageState = storageStateContextImpl.getStorageState();

		assertNotNull(storageState);
		StaleDataMessage staleDataMessage = new StaleDataMessage(true);
		EventBus.publish(staleDataMessage);

		while (!storageStateContextImpl.getStorageState().isStale()) {

		}

		assertTrue(storageStateContextImpl.getStorageState().isStale());

		AnnotationProcessor.unprocess(this);
	}

	@Test
	public void successfullyReceiveStaleDataMessageStorageStateNotStale() {
		// Pre-conditions
		AnnotationProcessor.process(this);
		loadDieTable();
		loadStopTable();
		loadCarrierMesTable();

		StorageStateContextHelper helper = null;
		helper = new StorageStateContextHelperImpl();
		StorageStateContextImpl storageStateContextImpl = new StorageStateContextImpl(helper);
		StorageState storageState = storageStateContextImpl.getStorageState();

		assertNotNull(storageState);

		StaleDataMessage staleDataMessage2 = new StaleDataMessage(false);
		EventBus.publish(staleDataMessage2);

		while (storageStateContextImpl.getStorageState().isStale()) {

		}
		assertTrue(!storageStateContextImpl.getStorageState().isStale());

		AnnotationProcessor.unprocess(this);
	}

	private List<CarrierMes> getCarrierMesList() {
		long baseTime = System.currentTimeMillis();
		int carrierNumber = 1;
		int maxPosition = 4;
		int stopIdBeforeLane = 81;
		List<CarrierMes> carrierMesList = new ArrayList<CarrierMes>();
		for (Long lane = 1201L; lane < 1236L; lane++) {
			for (int position = 0; position <= maxPosition; position++) {
				CarrierMes carrierMes = new CarrierMes(lane + position);
				carrierMes.setQuantity(8 + position);
				carrierMes.setCarrierNumber(carrierNumber++);
				carrierMes.setCurrentLocation(position % 2 == 1 ? lane : stopIdBeforeLane);
				carrierMes.setDestination(lane);
				carrierMes.setBuffer(position % (maxPosition - 1));
				Date updateDate = new Date(baseTime - (lane * 1000) + (position * 2000));
				carrierMes.setUpdateDate(updateDate);
				carrierMes.setStatus(0);
				carrierMesList.add(carrierMes);
			}
		}
		return carrierMesList;
	}

	private StorageStateImpl getExpectedState() {
		return new StorageStateImpl(getLanes());
	}

	private List<StorageRow> getLanes() {
		List<StorageRow> lanes = new ArrayList<StorageRow>();
		StorageRow row1 = new StorageRow();
		row1.setRowName("ROW_1");
		row1.setStop(new Stop(1201l));
		row1.setCapacity(12);
		row1.setAvailability(StopAvailability.AVAILABLE);
		row1.setStorageArea(StorageArea.C_HIGH);

		StorageRow row2 = new StorageRow();
		row2.setRowName("ROW_30");
		row2.setStop(new Stop(1230));
		row2.setCapacity(30);
		row2.setAvailability(StopAvailability.AVAILABLE);
		row2.setStorageArea(StorageArea.C_LOW);

		StorageRow row3 = new StorageRow();
		row3.setRowName("ROW_32");
		row3.setStop(new Stop(1232));
		row3.setCapacity(30);
		row3.setAvailability(StopAvailability.AVAILABLE);
		row3.setStorageArea(StorageArea.C_HIGH);

		lanes.add(row1);
		lanes.add(row2);
		lanes.add(row3);

		return lanes;
	}

	private StorageRow getStorageRow(int id) {
		return new StorageRow(id, "ROW" + id, id * 1, 1);
	}

	private void loadCarrierMesTable() {
		CarrierMes mesCarrier1 = new CarrierMes();
		mesCarrier1.setCarrierNumber(111);
		mesCarrier1.setCurrentLocation(1230L);
		mesCarrier1.setDestination(1230L);
		mesCarrier1.setDieNumber(166);
		mesCarrier1.setQuantity(10);
		mesCarrier1.setStatus(0);
		mesCarrier1.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier1.setProductionRunNumber(100);
		mesCarrier1.setOriginationLocation(0);
		mesCarrier1.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		CarrierMes mesCarrier2 = new CarrierMes();
		mesCarrier2.setCarrierNumber(112);
		mesCarrier2.setCurrentLocation(1230L);
		mesCarrier2.setDestination(1230L);
		mesCarrier2.setDieNumber(172);
		mesCarrier2.setQuantity(10);
		mesCarrier2.setStatus(0);
		mesCarrier2.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier2.setProductionRunNumber(100);
		mesCarrier2.setOriginationLocation(0);
		mesCarrier2.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		CarrierMes mesCarrier3 = new CarrierMes();
		mesCarrier3.setCarrierNumber(113);
		mesCarrier3.setCurrentLocation(513L);
		mesCarrier3.setDestination(1232L);
		mesCarrier3.setDieNumber(172);
		mesCarrier3.setQuantity(10);
		mesCarrier3.setStatus(0);
		mesCarrier3.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier3.setProductionRunNumber(100);
		mesCarrier3.setOriginationLocation(0);
		mesCarrier3.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		CarrierMes mesCarrier4 = new CarrierMes();
		mesCarrier4.setCarrierNumber(113);
		mesCarrier4.setCurrentLocation(513l);
		mesCarrier4.setDestination(null);
		mesCarrier4.setDieNumber(-1);
		mesCarrier4.setQuantity(10);
		mesCarrier4.setStatus(null);
		mesCarrier4.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
		mesCarrier4.setProductionRunNumber(100);
		mesCarrier4.setOriginationLocation(0);
		mesCarrier4.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		mesCarrier1.persist();
		mesCarrier2.persist();
		mesCarrier3.persist();
		mesCarrier4.persist();

		for (int i = 0; i < 13; i++) {
			CarrierMes mesCarrier = new CarrierMes();
			mesCarrier.setCarrierNumber(200 + i);
			mesCarrier.setCurrentLocation(1201L);
			mesCarrier.setDestination(1201L);
			mesCarrier.setDieNumber(166);
			mesCarrier.setQuantity(10);
			mesCarrier.setStatus(0);
			mesCarrier.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
			mesCarrier.setProductionRunNumber(100);
			mesCarrier.setOriginationLocation(0);
			mesCarrier.setUpdateDate(new Timestamp(System.currentTimeMillis()));
			mesCarrier.persist();
		}

	}

	void loadDieTable() {
		Die die = new Die();
		die.setId(166L);
		die.setDescription("left Die");
		die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

		die.persist();
	}

	void loadStopTable() {
		Stop stop = new Stop();
		stop.setId(1230L);
		stop.setName("ST12-30");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.findByType(0));

		Stop stop1 = new Stop();
		stop1.setId(1232L);
		stop1.setName("ST12-32");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.findByType(0));

		Stop stop2 = new Stop();
		stop2.setId(1201L);
		stop2.setName("ST12-01");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.findByType(0));

		stop.persist();
		stop1.persist();
		stop2.persist();

		StorageRow row1 = new StorageRow();
		row1.setRowName("ROW_1");
		row1.setStop(stop2);
		row1.setCapacity(12);
		row1.setAvailability(StopAvailability.AVAILABLE);
		row1.setStorageArea(StorageArea.C_HIGH);

		StorageRow row2 = new StorageRow();
		row2.setRowName("ROW_30");
		row2.setStop(stop);
		row2.setCapacity(30);
		row2.setAvailability(StopAvailability.AVAILABLE);
		row2.setStorageArea(StorageArea.C_LOW);

		StorageRow row3 = new StorageRow();
		row3.setRowName("ROW_32");
		row3.setStop(stop1);
		row3.setCapacity(30);
		row3.setAvailability(StopAvailability.AVAILABLE);
		row3.setStorageArea(StorageArea.C_HIGH);

		row1.persist();
		row2.persist();
		row3.persist();
	}

}
