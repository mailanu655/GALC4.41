package com.honda.mfg.stamp.conveyor.release;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 1/6/12 Time: 8:53 AM To change
 * this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierReleaseProcessorTest extends AbstractTestBase {

	Stop stop, stop1, stop2, stop3, stop4, stop5, stop6, stop7, stop8, stop9, stop10, stop11, stop12, stop13;

	Message message;
	Integer count = 0;
	boolean isStale = false;

	@Test(timeout = 5000L)
	public void successfullyReceiveCarrierStatusMessageForRelease() {

		loadStopTable();
		loadRowTable();
		loadCarrierReleaseTable();
		loadCarrierMesTable();

		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);

		when(storage.getStorageState()).thenReturn(storageState);

		AnnotationProcessor.process(this);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop,
				CarrierStatus.ON_HOLD, 101, new Die());
		carrier.setBuffer(1);
		carrierMessage.setBuffer("1");
		carrierMessage.setCurrentLocation(stop.getId().toString());
		carrierMessage.setDestination(stop.getId().toString());
		carrierMessage.setCarrierNumber("101");
		carrierMessage.setCarrier(carrier);
		EventBus.publish(carrierMessage);

//        while (count < 1) {
//
//        }
//        CarrierRelease released1 = CarrierRelease.findCarrierRelease(101L);
//        assertNull(released1);
//
//        CarrierRelease released2 = CarrierRelease.findCarrierRelease(102L);
//        assertNull(released2);

		processor.turnOffSubscriber();

	}

	@EventSubscriber(eventClass = CarrierUpdateMessage.class)
	public void catchStorageEvent(CarrierUpdateMessage response) {
		message = response;
		count++;
	}

	@EventSubscriber(eventClass = StaleDataMessage.class)
	public void catchStorageEvent(StaleDataMessage msg) {
		isStale = msg.isStale();
	}

	@Test(timeout = 5000L)
	public void successfullyReceiveCarrierStatusMessageForRelease1() {
		loadStopTable();
		loadRowTable();
		loadCarrierReleaseTable();
		loadCarrierMesTable();

		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);

		when(storage.getStorageState()).thenReturn(storageState);

		AnnotationProcessor.process(this);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage2 = new CarrierStatusMessage();
		Carrier carrier2 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop2, stop2,
				CarrierStatus.ON_HOLD, 105, new Die());
		carrier2.setBuffer(1);
		carrierMessage2.setBuffer("1");
		carrierMessage2.setCurrentLocation(stop2.getId().toString());
		carrierMessage2.setDestination(stop2.getId().toString());
		carrierMessage2.setCarrierNumber("105");
		carrierMessage2.setCarrier(carrier2);
		EventBus.publish(carrierMessage2);

//        while (count < 1) {
//
//        }
//
//          CarrierRelease released1 = CarrierRelease.findCarrierRelease(105L);
//        assertNull(released1);

		processor.turnOffSubscriber();

	}

	@Test(timeout = 5000L)
	public void successfullyReceiveCarrierStatusMessageForRelease3() {
		loadStopTable();
		loadRowTable();
		loadCarrierReleaseTable();
		loadCarrierMesTable();

		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);

		when(storage.getStorageState()).thenReturn(storageState);

		AnnotationProcessor.process(this);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage3 = new CarrierStatusMessage();
		Carrier carrier3 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop5, stop5,
				CarrierStatus.ON_HOLD, 107, new Die());
		carrier3.setBuffer(1);
		carrierMessage3.setBuffer("1");
		carrierMessage3.setCurrentLocation(stop5.getId().toString());
		carrierMessage3.setDestination(stop5.getId().toString());
		carrierMessage3.setCarrierNumber("107");
		carrierMessage3.setCarrier(carrier3);
		EventBus.publish(carrierMessage3);

//        while (count < 1) {
//
//        }
//          CarrierRelease released1 = CarrierRelease.findCarrierRelease(107L);
//        assertNull(released1);
		processor.turnOffSubscriber();

	}

	@Test(timeout = 5000L)
	public void successfullyReceiveCarrierStatusMessageForRelease4() {
		loadStopTable();
		loadRowTable();
		loadCarrierReleaseTable();
		loadCarrierMesTable();

		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);

		when(storage.getStorageState()).thenReturn(storageState);

		AnnotationProcessor.process(this);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage4 = new CarrierStatusMessage();
		Carrier carrier4 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop2, stop2,
				CarrierStatus.ON_HOLD, 109, new Die());
		carrier4.setBuffer(1);
		carrierMessage4.setBuffer("1");
		carrierMessage4.setCurrentLocation(stop2.getId().toString());
		carrierMessage4.setDestination(stop2.getId().toString());
		carrierMessage4.setCarrierNumber("109");
		carrierMessage4.setCarrier(carrier4);
		EventBus.publish(carrierMessage4);

//        while (count < 1) {
//
//        }
//           CarrierRelease released1 = CarrierRelease.findCarrierRelease(109L);
//        assertNull(released1);
		processor.turnOffSubscriber();
	}

	@Test(timeout = 5000L)
	public void successfullyReceiveCarrierStatusMessageForRelease5() {
		loadStopTable();
		loadRowTable();
		loadCarrierReleaseTable();
		loadCarrierMesTable();

		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);

		when(storage.getStorageState()).thenReturn(storageState);

		AnnotationProcessor.process(this);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage5 = new CarrierStatusMessage();
		Carrier carrier5 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop6, stop6,
				CarrierStatus.ON_HOLD, 110, new Die());
		carrier5.setBuffer(1);
		carrierMessage5.setBuffer("1");
		carrierMessage5.setCurrentLocation(stop6.getId().toString());
		carrierMessage5.setDestination(stop6.getId().toString());
		carrierMessage5.setCarrierNumber("110");
		carrierMessage5.setCarrier(carrier5);
		EventBus.publish(carrierMessage5);

//        while (count < 1) {
//
//        }
//
//          CarrierRelease released1 = CarrierRelease.findCarrierRelease(110L);
//        assertNull(released1);
		processor.turnOffSubscriber();
	}

	@Test(timeout = 5000L)
	public void successfullyReceiveCarrierStatusMessageForRelease2() {
		AnnotationProcessor.process(this);
		stop = new Stop();
		stop.setId(1201L);
		stop.setName("ST12-1");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.ROW);

		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);

		when(storage.getStorageState()).thenReturn(storageState);

		ReleaseManagerHelper releaseManagerHelper = mock(ReleaseManagerHelper.class);
		when(releaseManagerHelper.anyCarriersSetToReleaseFromThisRow(Matchers.<Stop>any())).thenReturn(true);
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage = new CarrierStatusMessage();
		Carrier carrier = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop, stop,
				CarrierStatus.ON_HOLD, 101, new Die());
		carrier.setBuffer(1);
		carrierMessage.setBuffer("1");
		carrierMessage.setCurrentLocation(stop.getId().toString());
		carrierMessage.setDestination(stop.getId().toString());
		carrierMessage.setCarrierNumber("101");
		carrierMessage.setCarrier(carrier);
		EventBus.publish(carrierMessage);

		while (isStale) {

		}
	}

	@Test(timeout = 5000L)
	public void sucessfullyReceiveCarrierStatusForReleaseCheckProcessing() {

		loadStopTable();
		loadRowTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		AnnotationProcessor.process(this);
		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);
		count = 0;
		when(storage.getStorageState()).thenReturn(storageState);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage3 = new CarrierStatusMessage();
		Carrier carrier3 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 114, stop7, stop4,
				CarrierStatus.SHIPPABLE, 105, new Die());
		carrier3.setBuffer(1);
		carrierMessage3.setBuffer("1");
		carrierMessage3.setCurrentLocation(stop7.getId().toString());
		carrierMessage3.setDestination(stop4.getId().toString());
		carrierMessage3.setCarrierNumber("105");
		carrierMessage3.setCarrier(carrier3);
		EventBus.publish(carrierMessage3);

//        while (count < 1) {
//
//        }
//

		processor.turnOffSubscriber();
		AnnotationProcessor.unprocess(this);
	}

	@Test(timeout = 5000L)
	public void sucessfullyReceiveCarrierStatusForReleaseCheckProcessing2() {

		loadStopTable();
		loadRowTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		AnnotationProcessor.process(this);
		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);
		count = 0;
		when(storage.getStorageState()).thenReturn(storageState);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage4 = new CarrierStatusMessage();
		Carrier carrier4 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 111, stop13, stop9,
				CarrierStatus.SHIPPABLE, 105, new Die());
		carrier4.setBuffer(1);
		carrierMessage4.setBuffer("1");
		carrierMessage4.setCurrentLocation(stop13.getId().toString());
		carrierMessage4.setDestination(stop9.getId().toString());
		carrierMessage4.setCarrierNumber("111");
		carrierMessage4.setCarrier(carrier4);
		EventBus.publish(carrierMessage4);

//        while (count < 1) {
//
//        }

		processor.turnOffSubscriber();
		AnnotationProcessor.unprocess(this);
	}

	@Test(timeout = 5000L)
	public void sucessfullyReceiveCarrierStatusForReleaseCheckProcessing3() {

		loadStopTable();
		loadRowTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		AnnotationProcessor.process(this);
		Storage storage = mock(Storage.class);
		StorageState storageState = mock(StorageState.class);
		count = 0;
		when(storage.getStorageState()).thenReturn(storageState);

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, releaseManagerHelper);

		CarrierStatusMessage carrierMessage5 = new CarrierStatusMessage();
		Carrier carrier5 = new Carrier(1, new Timestamp(System.currentTimeMillis()), 112, stop12, stop8,
				CarrierStatus.SHIPPABLE, 105, new Die());
		carrier5.setBuffer(1);
		carrierMessage5.setBuffer("1");
		carrierMessage5.setCurrentLocation(stop13.getId().toString());
		carrierMessage5.setDestination(stop9.getId().toString());
		carrierMessage5.setCarrierNumber("111");
		carrierMessage5.setCarrier(carrier5);
		EventBus.publish(carrierMessage5);

//        while (count < 1) {
//
//        }

		processor.turnOffSubscriber();
		AnnotationProcessor.unprocess(this);
	}

	void loadStopTable() {
		stop = new Stop();
		stop.setId(1201L);
		stop.setName("ST12-1");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.ROW);

		stop1 = new Stop();
		stop1.setId(1202L);
		stop1.setName("ST12-2");
		stop1.setStopType(StopType.NO_ACTION);
		stop1.setStopArea(StopArea.ROW);

		stop2 = new Stop();
		stop2.setId(1230L);
		stop2.setName("ST12-30");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.ROW);

		stop3 = new Stop();
		stop3.setId(803L);
		stop3.setName("ST8-3");
		stop3.setStopType(StopType.NO_ACTION);
		stop3.setStopArea(StopArea.ROW);

		stop4 = new Stop();
		stop4.setId(5200L);
		stop4.setName("ST52");
		stop4.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
		stop4.setStopArea(StopArea.OLD_WELD_LINE);

		stop5 = new Stop();
		stop5.setId(1222L);
		stop5.setName("ST12-22");
		stop5.setStopType(StopType.NO_ACTION);
		stop5.setStopArea(StopArea.ROW);

		stop6 = new Stop();
		stop6.setId(1301L);
		stop6.setName("ST13-1");
		stop6.setStopType(StopType.NO_ACTION);
		stop6.setStopArea(StopArea.STORE_OUT_ROUTE);

		stop7 = new Stop();
		stop7.setId(903L);
		stop7.setName("ST9-3");
		stop7.setStopType(StopType.RELEASE_CHECK);
		stop7.setStopArea(StopArea.STORE_OUT_ROUTE);

		stop8 = new Stop();
		stop8.setId(1231L);
		stop8.setName("ST12-31");
		stop8.setStopType(StopType.NO_ACTION);
		stop8.setStopArea(StopArea.ROW);

		stop9 = new Stop();
		stop9.setId(1223L);
		stop9.setName("ST12-22");
		stop9.setStopType(StopType.NO_ACTION);
		stop9.setStopArea(StopArea.ROW);

		stop10 = new Stop();
		stop10.setId(1246L);
		stop10.setName("ST12-46");
		stop10.setStopType(StopType.NO_ACTION);
		stop10.setStopArea(StopArea.ROW);

		stop11 = new Stop();
		stop11.setId(1244L);
		stop11.setName("ST12-44");
		stop11.setStopType(StopType.NO_ACTION);
		stop11.setStopArea(StopArea.ROW);

		stop12 = new Stop();
		stop12.setId(3018L);
		stop12.setName("ST30-18");
		stop12.setStopType(StopType.RECIRC_TO_ALL_ROWS);
		stop12.setStopArea(StopArea.STORE_IN_ROUTE);

		stop13 = new Stop();
		stop13.setId(3027L);
		stop13.setName("ST30-27");
		stop13.setStopType(StopType.RELEASE_CHECK);
		stop13.setStopArea(StopArea.STORE_OUT_ROUTE);

		stop.persist();
		stop1.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();
		stop5.persist();
		stop6.persist();
		stop7.persist();
		stop8.persist();
		stop9.persist();
		stop10.persist();
		stop11.persist();
		stop12.persist();
		stop13.persist();
	}

	void loadCarrierReleaseTable() {
		CarrierRelease carrierRelease = new CarrierRelease();
		carrierRelease.setId(101l);
		carrierRelease.setCurrentLocation(stop);
		carrierRelease.setDestination(stop4);
		carrierRelease.setSource("user");
		carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease.persist();

		CarrierRelease carrierRelease1 = new CarrierRelease();
		carrierRelease1.setId(102l);
		carrierRelease1.setCurrentLocation(stop1);
		carrierRelease1.setDestination(stop4);
		carrierRelease1.setSource("user");
		carrierRelease1.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease1.persist();

		CarrierRelease carrierRelease2 = new CarrierRelease();
		carrierRelease2.setId(105l);
		carrierRelease2.setCurrentLocation(stop2);
		carrierRelease2.setDestination(stop4);
		carrierRelease2.setSource("user");
		carrierRelease2.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease2.persist();

		CarrierRelease carrierRelease3 = new CarrierRelease();
		carrierRelease3.setId(106l);
		carrierRelease3.setCurrentLocation(stop8);
		carrierRelease3.setDestination(stop4);
		carrierRelease3.setSource("user");
		carrierRelease3.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease3.persist();

		CarrierRelease carrierRelease4 = new CarrierRelease();
		carrierRelease4.setId(107l);
		carrierRelease4.setCurrentLocation(stop5);
		carrierRelease4.setDestination(stop4);
		carrierRelease4.setSource("user");
		carrierRelease4.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease4.persist();

		CarrierRelease carrierRelease5 = new CarrierRelease();
		carrierRelease5.setId(108l);
		carrierRelease5.setCurrentLocation(stop5);
		carrierRelease5.setDestination(stop4);
		carrierRelease5.setSource("user");
		carrierRelease5.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease5.persist();

		CarrierRelease carrierRelease6 = new CarrierRelease();
		carrierRelease6.setId(109l);
		carrierRelease6.setCurrentLocation(stop2);
		carrierRelease6.setDestination(stop5);
		carrierRelease6.setSource("user");
		carrierRelease6.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease6.persist();

		CarrierRelease carrierRelease7 = new CarrierRelease();
		carrierRelease7.setId(110l);
		carrierRelease7.setCurrentLocation(stop6);
		carrierRelease7.setDestination(stop4);
		carrierRelease7.setSource("user");
		carrierRelease7.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease7.persist();

		CarrierRelease carrierRelease8 = new CarrierRelease();
		carrierRelease8.setId(111l);
		carrierRelease8.setCurrentLocation(stop10);
		carrierRelease8.setDestination(stop9);
		carrierRelease8.setSource("user");
		carrierRelease8.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease8.persist();

		CarrierRelease carrierRelease9 = new CarrierRelease();
		carrierRelease9.setId(112l);
		carrierRelease9.setCurrentLocation(stop11);
		carrierRelease9.setDestination(stop8);
		carrierRelease9.setSource("user");
		carrierRelease9.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease9.persist();
	}

	void loadCarrierMesTable() {
		CarrierMes carrier = new CarrierMes();
		carrier.setCarrierNumber(101);
		carrier.setDieNumber(999);
		carrier.setQuantity(1);
		carrier.setCurrentLocation(1201L);
		carrier.setDestination(1201L);
		carrier.setBuffer(new Integer(1));
		carrier.setOriginationLocation(0);
		carrier.setStatus(0);
		carrier.setProductionRunNumber(100);

		CarrierMes carrier1 = new CarrierMes();
		carrier1.setCarrierNumber(102);
		carrier1.setDieNumber(999);
		carrier1.setQuantity(0);
		carrier1.setCurrentLocation(1202L);
		carrier1.setDestination(1202L);
		carrier1.setBuffer(new Integer(1));
		carrier1.setProductionRunNumber(100);

		CarrierMes carrier2 = new CarrierMes();
		carrier2.setCarrierNumber(103);
		carrier2.setDieNumber(999);
		carrier2.setQuantity(0);
		carrier2.setCurrentLocation(1201L);
		carrier2.setDestination(1201L);
		carrier2.setBuffer(new Integer(0));
		carrier2.setProductionRunNumber(100);

		CarrierMes carrier3 = new CarrierMes();
		carrier3.setCarrierNumber(104);
		carrier3.setDieNumber(999);
		carrier3.setQuantity(0);
		carrier3.setCurrentLocation(803L);
		carrier3.setDestination(1201L);
		carrier3.setBuffer(new Integer(0));

		CarrierMes carrier4 = new CarrierMes();
		carrier4.setCarrierNumber(105);
		carrier4.setDieNumber(999);
		carrier4.setQuantity(0);
		carrier4.setCurrentLocation(1230L);
		carrier4.setDestination(1230L);
		carrier4.setBuffer(new Integer(1));

		CarrierMes carrier5 = new CarrierMes();
		carrier5.setCarrierNumber(106);
		carrier5.setDieNumber(999);
		carrier5.setQuantity(0);
		carrier5.setCurrentLocation(1231L);
		carrier5.setDestination(1231L);
		carrier5.setBuffer(new Integer(1));

		CarrierMes carrier6 = new CarrierMes();
		carrier6.setCarrierNumber(107);
		carrier6.setDieNumber(999);
		carrier6.setQuantity(0);
		carrier6.setCurrentLocation(1222L);
		carrier6.setDestination(1222L);
		carrier6.setBuffer(new Integer(1));

		CarrierMes carrier7 = new CarrierMes();
		carrier7.setCarrierNumber(108);
		carrier7.setDieNumber(999);
		carrier7.setQuantity(0);
		carrier7.setCurrentLocation(1222L);
		carrier7.setDestination(1222L);
		carrier7.setBuffer(new Integer(0));

		CarrierMes carrier8 = new CarrierMes();
		carrier8.setCarrierNumber(109);
		carrier8.setDieNumber(999);
		carrier8.setQuantity(0);
		carrier8.setCurrentLocation(1223L);
		carrier8.setDestination(1223L);
		carrier8.setBuffer(new Integer(1));

		CarrierMes carrier9 = new CarrierMes();
		carrier9.setCarrierNumber(110);
		carrier9.setDieNumber(999);
		carrier9.setQuantity(0);
		carrier9.setCurrentLocation(1301L);
		carrier9.setDestination(1301L);
		carrier9.setBuffer(new Integer(1));

		CarrierMes carrier10 = new CarrierMes();
		carrier10.setCarrierNumber(111);
		carrier10.setDieNumber(999);
		carrier10.setQuantity(0);
		carrier10.setCurrentLocation(1246L);
		carrier10.setDestination(1246L);
		carrier10.setBuffer(new Integer(1));

		CarrierMes carrier11 = new CarrierMes();
		carrier11.setCarrierNumber(112);
		carrier11.setDieNumber(999);
		carrier11.setQuantity(0);
		carrier11.setCurrentLocation(1244L);
		carrier11.setDestination(1244L);
		carrier11.setBuffer(new Integer(1));

		carrier.persist();
		carrier1.persist();
		carrier2.persist();
		carrier3.persist();
		carrier4.persist();
		carrier5.persist();
		carrier6.persist();
		carrier7.persist();
		carrier8.persist();
		carrier9.persist();
		carrier10.persist();
		carrier11.persist();
	}

	void loadRowTable() {
		StorageRow row = new StorageRow();
		row.setStop(stop);
		row.setRowName("Row1");
		row.setAvailability(StopAvailability.AVAILABLE);
		row.setCapacity(12);
		row.setStorageArea(StorageArea.C_HIGH);

		StorageRow row1 = new StorageRow();
		row1.setStop(stop2);
		row1.setRowName("Row30");
		row1.setAvailability(StopAvailability.AVAILABLE);
		row1.setCapacity(30);
		row1.setStorageArea(StorageArea.C_LOW);

		StorageRow row2 = new StorageRow();
		row2.setStop(stop5);
		row2.setRowName("Row22");
		row2.setAvailability(StopAvailability.AVAILABLE);
		row2.setCapacity(21);
		row2.setStorageArea(StorageArea.A_AREA);

		StorageRow row3 = new StorageRow();
		row3.setStop(stop9);
		row3.setRowName("Row23");
		row3.setAvailability(StopAvailability.AVAILABLE);
		row3.setCapacity(21);
		row3.setStorageArea(StorageArea.A_AREA);

		StorageRow row4 = new StorageRow();
		row4.setStop(stop8);
		row4.setRowName("Row31");
		row4.setAvailability(StopAvailability.AVAILABLE);
		row4.setCapacity(30);
		row4.setStorageArea(StorageArea.C_LOW);

		StorageRow row5 = new StorageRow();
		row5.setStop(stop1);
		row5.setRowName("Row2");
		row5.setAvailability(StopAvailability.AVAILABLE);
		row5.setCapacity(12);
		row5.setStorageArea(StorageArea.C_HIGH);

		StorageRow row6 = new StorageRow();
		row6.setStop(stop10);
		row6.setRowName("Row46");
		row6.setAvailability(StopAvailability.AVAILABLE);
		row6.setCapacity(29);
		row6.setStorageArea(StorageArea.B_AREA);

		StorageRow row7 = new StorageRow();
		row7.setStop(stop11);
		row7.setRowName("Row44");
		row7.setAvailability(StopAvailability.AVAILABLE);
		row7.setCapacity(87);
		row7.setStorageArea(StorageArea.S_AREA);

		row.persist();
		row1.persist();
		row2.persist();
		row3.persist();
		row4.persist();
		row5.persist();
		row6.persist();
		row7.persist();
	}
}
