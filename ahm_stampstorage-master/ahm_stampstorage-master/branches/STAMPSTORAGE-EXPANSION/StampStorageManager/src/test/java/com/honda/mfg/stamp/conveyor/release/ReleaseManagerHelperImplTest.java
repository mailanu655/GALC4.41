package com.honda.mfg.stamp.conveyor.release;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 2/29/12 Time: 4:18 PM To
 * change this template use File | Settings | File Templates.
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ReleaseManagerHelperImplTest {

	Stop stop, stop3, stop2, stop4, stop5, stop6, stop7, stop8, stop9, stop10;

	@Test
	public void successfullyGetAnyCarriersReleasingInStorageArea() {
		loadStopTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		loadRowTable();

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		assertEquals(false, releaseManagerHelper.anyCarriersReleasingInStorageArea(stop5));
		assertEquals(true, releaseManagerHelper.anyCarriersReleasingInStorageArea(stop2));
	}

	@Test
	public void successfullyGetAnyCarriersSetToReleaseFromThisRow() {
		loadStopTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		loadRowTable();

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		assertEquals(false, releaseManagerHelper.anyCarriersSetToReleaseFromThisRow(stop6));
		assertEquals(true, releaseManagerHelper.anyCarriersSetToReleaseFromThisRow(stop2));
	}

	@Test
	public void successfullyGetCarriersSetToReleaseFromThisStorageArea() {
		loadStopTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		loadRowTable();

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		assertNotNull(releaseManagerHelper.getCarriersSetToReleaseFromThisStorageArea(stop5));
		assertNull(releaseManagerHelper.getCarriersSetToReleaseFromThisStorageArea(stop3));
	}

	@Test
	public void successfullyGetCarriersSetToReleaseFromStorage() {
		loadStopTable();
		loadCarrierMesTable();
		loadCarrierReleaseTable();
		loadRowTable();

		ReleaseManagerHelper releaseManagerHelper = new ReleaseManagerHelperImpl();
		assertNotNull(releaseManagerHelper.getCarrierToReleaseFromStorage(stop7));
		assertNotNull(releaseManagerHelper.getCarrierToReleaseFromStorage(stop8));
		assertNotNull(releaseManagerHelper.getCarrierToReleaseFromStorage(stop9));
		assertNull(releaseManagerHelper.getCarrierToReleaseFromStorage(stop3));
	}

	void loadStopTable() {
		stop = new Stop();
		stop.setId(1201L);
		stop.setName("ST12-1");
		stop.setStopType(StopType.NO_ACTION);
		stop.setStopArea(StopArea.ROW);

		stop2 = new Stop();
		stop2.setId(1230L);
		stop2.setName("ST12-30");
		stop2.setStopType(StopType.NO_ACTION);
		stop2.setStopArea(StopArea.ROW);

		stop3 = new Stop();
		stop3.setId(803L);
		stop3.setName("ST8-3");
		stop3.setStopType(StopType.NO_ACTION);
		stop3.setStopArea(StopArea.STORE_IN_ROUTE);

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
		stop6.setId(1232L);
		stop6.setName("ST12-32");
		stop6.setStopType(StopType.NO_ACTION);
		stop6.setStopArea(StopArea.ROW);

		stop7 = new Stop();
		stop7.setId(903L);
		stop7.setName("ST9-3");
		stop7.setStopType(StopType.RELEASE_CHECK);
		stop7.setStopArea(StopArea.STORE_OUT_ROUTE);

		stop8 = new Stop();
		stop8.setId(904L);
		stop8.setName("ST9-4");
		stop8.setStopType(StopType.RELEASE_CHECK);
		stop8.setStopArea(StopArea.STORE_OUT_ROUTE);

		stop9 = new Stop();
		stop9.setId(1300L);
		stop9.setName("ST13");
		stop9.setStopType(StopType.RELEASE_CHECK);
		stop9.setStopArea(StopArea.STORE_OUT_ROUTE);

		stop10 = new Stop();
		stop10.setId(1231L);
		stop10.setName("ST12-32");
		stop10.setStopType(StopType.NO_ACTION);
		stop10.setStopArea(StopArea.ROW);

		stop.persist();
		stop2.persist();
		stop3.persist();
		stop4.persist();
		stop5.persist();
		stop6.persist();
		stop7.persist();
		stop8.persist();
		stop9.persist();
		stop10.persist();
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
		row3.setStop(stop6);
		row3.setRowName("Row32");
		row3.setAvailability(StopAvailability.AVAILABLE);
		row3.setCapacity(30);
		row3.setStorageArea(StorageArea.C_LOW);

		StorageRow row4 = new StorageRow();
		row4.setStop(stop10);
		row4.setRowName("Row31");
		row4.setAvailability(StopAvailability.AVAILABLE);
		row4.setCapacity(30);
		row4.setStorageArea(StorageArea.C_LOW);

		row.persist();
		row1.persist();
		row2.persist();
		row3.persist();
		row4.persist();
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
		carrierRelease1.setCurrentLocation(stop);
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
		carrierRelease3.setCurrentLocation(stop2);
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
		carrierRelease5.setCurrentLocation(stop4);
		carrierRelease5.setDestination(stop4);
		carrierRelease5.setSource("user");
		carrierRelease5.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		carrierRelease5.persist();
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
		carrier1.setCurrentLocation(1201L);
		carrier1.setDestination(1201L);
		carrier1.setBuffer(new Integer(0));
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
		carrier5.setDestination(5200L);
		carrier5.setBuffer(new Integer(1));

		CarrierMes carrier6 = new CarrierMes();
		carrier6.setCarrierNumber(107);
		carrier6.setDieNumber(999);
		carrier6.setQuantity(0);
		carrier6.setCurrentLocation(1222L);
		carrier6.setDestination(1222L);
		carrier6.setBuffer(new Integer(1));

		carrier.persist();
		carrier1.persist();
		carrier2.persist();
		carrier3.persist();
		carrier4.persist();
		carrier5.persist();
		carrier6.persist();
	}
}
