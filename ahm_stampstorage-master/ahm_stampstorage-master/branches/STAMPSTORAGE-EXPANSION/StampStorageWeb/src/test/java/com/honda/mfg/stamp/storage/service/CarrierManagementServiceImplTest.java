package com.honda.mfg.stamp.storage.service;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.GroupHoldFinderCriteria;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.messages.StatusUpdateMessage;

/**
 * User: VCC30690 Date: 9/9/11
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierManagementServiceImplTest {
	int count = 0;

	@Test
	public void successfullyGetAllManualOrderDestinationStops() {
		loadStopTable();
		CarrierManagementService service = new CarrierManagementServiceImpl();

		List<Stop> deliveryStops = service.getManualOrderCarrierDeliveryStops();

		assertNotNull(deliveryStops);
	}

	@Test
	public void successfullyGetAllValidDestinationStops() {
		loadStopTable();
		CarrierManagementService service = new CarrierManagementServiceImpl();

		List<Stop> stops = service.getValidDestinationStops(Stop.findStop(1201l));

		assertNotNull(stops);
	}

	@Test
	public void successfullyRecalculateDestination() {
		// 2013-01-28:VB:
		CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
		service.recalculateCarrierDestination(getCarrier(1234));
	}

	@Test
	public void successfullyGetCarrier() {
		loadCarrierMesTable();
		CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(10);
		CarrierManagementService service = new CarrierManagementServiceImpl();
		Carrier carrier = service.getCarrier(carrierMes);

		assertNotNull(carrier);
	}

	@Test
	public void successfullyGetGroupHoldCarriers() {
		loadCarrierMesTable();
		CarrierManagementService service = new CarrierManagementServiceImpl();

		GroupHoldFinderCriteria groupHoldFinderCriteria = new GroupHoldFinderCriteria();
		groupHoldFinderCriteria.setProductionRunNumber(100);

		List<Carrier> carriers = service.getGroupHoldCarriers(groupHoldFinderCriteria, 1, 10);
		assertEquals(1, carriers.size());
	}

	@Test
	public void successfullyGetAlarmToDisplay() {
		loadAlarmTestData();
		CarrierManagementService service = new CarrierManagementServiceImpl();

		assertNotNull(service.getAlarmEventToDisplay());
	}

	@Test
	public void successfullyHandleWhenNoAlarmToDisplay() {
		// loadAlarmTestData();
		CarrierManagementService service = new CarrierManagementServiceImpl();

		assertNull(service.getAlarmEventToDisplay());
	}

	@EventSubscriber(eventClass = StatusUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void statusUpdateStorageMessageListener(StatusUpdateMessage infoMessage) {
		count++;
	}

	Carrier getCarrier(Integer carrierNumber) {
		Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
		Integer prodRunNo = 108;
		Stop currentLocation = new Stop(513);
		Stop destination = new Stop(513);
		CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
		Carrier carrier = new Carrier(1, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus,
				carrierNumber, null);

		return carrier;
	}

	void loadCarrierMesTable() {
		CarrierMes carrier = new CarrierMes();
		carrier.setCarrierNumber(10);
		carrier.setDieNumber(101);
		carrier.setQuantity(1);
		carrier.setCurrentLocation(1201L);
		carrier.setDestination(1201L);
		carrier.setBuffer(new Integer(1));
		carrier.setOriginationLocation(0);
		carrier.setStatus(0);
		carrier.setProductionRunNumber(100);

		CarrierMes carrier1 = new CarrierMes();
		carrier1.setCarrierNumber(11);
		carrier1.setDieNumber(999);
		carrier1.setQuantity(0);
		carrier1.setCurrentLocation(1201L);
		carrier1.setDestination(1201L);
		carrier1.setBuffer(new Integer(0));
		carrier1.setProductionRunNumber(100);

		CarrierMes carrier2 = new CarrierMes();
		carrier2.setCarrierNumber(12);
		carrier2.setDieNumber(999);
		carrier2.setQuantity(0);
		carrier2.setCurrentLocation(1201L);
		carrier2.setDestination(1201L);
		carrier2.setBuffer(new Integer(0));
		carrier2.setProductionRunNumber(100);

		CarrierMes carrier3 = new CarrierMes();
		carrier3.setCarrierNumber(13);
		carrier3.setDieNumber(999);
		carrier3.setQuantity(0);
		carrier3.setCurrentLocation(803L);
		carrier3.setDestination(1201L);
		carrier3.setBuffer(new Integer(0));

		CarrierMes carrier4 = new CarrierMes();
		carrier4.setCarrierNumber(1234);
		carrier4.setDieNumber(999);
		carrier4.setQuantity(0);
		carrier4.setCurrentLocation(1201L);
		carrier4.setDestination(1201L);
		carrier4.setBuffer(new Integer(0));

		carrier.persist();
		carrier1.persist();
		carrier2.persist();
		carrier3.persist();
		carrier4.persist();
	}

	void loadReleaseMgrTable() {
		Stop destination = new Stop();
		destination.setId(500l);
		destination.setStopArea(StopArea.WELD_LINE_1);
		destination.setStopType(StopType.FULL_CARRIER_DELIVERY);
		destination.setName("ST 5");

		destination.persist();

		Stop currentLocation = new Stop();
		currentLocation.setId(1201l);
		currentLocation.setStopArea(StopArea.ROW);
		currentLocation.setStopType(StopType.NO_ACTION);
		currentLocation.setName("ST12-1");

		currentLocation.persist();

		CarrierRelease carrierRelease = new CarrierRelease();
		carrierRelease.setId(1234l);
		carrierRelease.setDestination(destination);
		carrierRelease.setSource("source");
		carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease.setCurrentLocation(currentLocation);
		carrierRelease.persist();
	}

	void loadStopTable() {
		Stop stop = new Stop();
		stop.setId(1201l);
		stop.setStopArea(StopArea.ROW);
		stop.setStopType(StopType.NO_ACTION);
		stop.setName("ST 12-1");

		stop.persist();

		Stop stop1 = new Stop();
		stop1.setId(0l);
		stop1.setStopArea(StopArea.UNDEFINED);
		stop1.setStopType(StopType.MAINTENANCE);
		stop1.setName("ST 0");

		stop1.persist();

		Stop stop2 = new Stop();
		stop2.setId(500l);
		stop2.setStopArea(StopArea.WELD_LINE_1);
		stop2.setStopType(StopType.FULL_CARRIER_DELIVERY);
		stop2.setName("ST 0");

		stop2.persist();

		Stop stop3 = new Stop();
		stop3.setId(5200l);
		stop3.setStopArea(StopArea.OLD_WELD_LINE);
		stop3.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
		stop3.setName("ST 0");

		stop3.persist();
	}

	private void loadAlarmTestData() {

		AlarmDefinition alarmDefinition = new AlarmDefinition();
		alarmDefinition.setAlarmNumber(1);
		alarmDefinition.setLocation(1201);
		alarmDefinition.setAutoArchiveTimeInMinutes(1.0);
		alarmDefinition.setActive(true);
		alarmDefinition.setNotificationRequired(true);
		alarmDefinition.setQpcNotificationRequired(true);
		alarmDefinition.setDescription("alarm");
		alarmDefinition.setName("alarm");
		alarmDefinition.setNotificationCategory(AlarmNotificationCategory.INFORMATION);
		alarmDefinition.setSeverity(SEVERITY.ONE);
		alarmDefinition.persist();

		AlarmEvent event = new AlarmEvent();
		event.setAlarmNumber(1);
		event.setLocation(1201);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -1);

		Timestamp timestamp = new Timestamp(c.getTimeInMillis());
		event.setEventTimestamp(timestamp);
		// event.setCleared(false);
		// event.setClearedBy("none");
		// event.setNotified(false);
		event.persist();

		AlarmEvent event1 = new AlarmEvent();
		event1.setAlarmNumber(1);
		event1.setLocation(1201);

		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.HOUR, -1);

		Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
		event1.setEventTimestamp(timestamp1);
		// event1.setCleared(false);
		// event1.setClearedBy("none");
		// event1.setNotified(false);
		event1.persist();

//        AlarmEventArchive archive = new AlarmEventArchive();
//        archive.setAlarmEventId(1l);
//        archive.setAlarmNumber(1);
//        archive.setLocation(1210);
//        archive.setEventTimestamp(new Timestamp(System.currentTimeMillis()));
//        archive.setCleared(true);
//        archive.setClearedBy("some user");
//        archive.setClearedTimestamp(new Timestamp(System.currentTimeMillis()));
//        archive.setArchivedTimestamp(new Timestamp(System.currentTimeMillis()));
//        archive.setArchivedBy(StorageConfig.OHCV_APP_ALARM_ARCHIVE);
//        archive.persist();
//
//        AlarmEventArchive archive1 = new AlarmEventArchive();
//        archive1.setAlarmEventId(1l);
//        archive1.setAlarmNumber(1);
//        archive1.setLocation(1210);
//        archive1.setEventTimestamp(new Timestamp(System.currentTimeMillis()));
//        archive1.setCleared(true);
//        archive1.setClearedBy("some user");
//        archive1.setClearedTimestamp(new Timestamp(System.currentTimeMillis()));
//        archive1.setArchivedTimestamp(new Timestamp(System.currentTimeMillis()));
//        archive1.setArchivedBy(StorageConfig.OHCV_APP_ALARM_ARCHIVE);
//        archive1.persist();
//
//        Contact contact1 = new Contact();
//        contact1.setContactName("name1");
//        contact1.setEmail("a@honda.com");
//        contact1.setPagerNo("6586@honda.com");
//
//        contact1.persist();
//
//        Contact contact2 = new Contact();
//        contact2.setContactName("name2");
//        contact2.setEmail("b@honda.com");
//        contact2.setPagerNo("6666@honda.com");
//
//        contact2.persist();
//
//        AlarmContact alarmContact1 = new AlarmContact();
//        alarmContact1.setAlarm(alarmDefinition);
//        alarmContact1.setContact(contact1);
//        alarmContact1.setContactType(ContactType.EMAIL);
//        alarmContact1.persist();
//
//        AlarmContact alarmContact2 = new AlarmContact();
//        alarmContact2.setAlarm(alarmDefinition);
//        alarmContact2.setContact(contact2);
//        alarmContact2.setContactType(ContactType.EMAIL);
//        alarmContact2.persist();
	}
}
