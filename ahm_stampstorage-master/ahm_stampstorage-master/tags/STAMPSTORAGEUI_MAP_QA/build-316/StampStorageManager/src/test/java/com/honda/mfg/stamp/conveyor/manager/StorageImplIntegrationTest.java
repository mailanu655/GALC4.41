package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.exceptions.InvalidDieException;
import com.honda.mfg.stamp.conveyor.rules.empty.EmptyManagerImpl;
import com.honda.mfg.stamp.conveyor.rules.store_out.StoreOutManagerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: vcc30690
 * Date: 6/29/11
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class StorageImplIntegrationTest {

    Die die, leftDie;

    @Test
    public void testReload() {
        loadStopTable();
        loadCarrierMesTable();
        StorageStateContextHelper helper = new StorageStateContextHelperImpl();
        StorageStateContext context = new StorageStateContextImpl(helper);
        StorageState storageState = context.getStorageState();
        Storage storage = new StorageImpl(null, null, null, context);
        storage.reloadStorageState();
        assertNotNull(storageState);
    }

    @Test
    public void successfullyRecalculateCarrierDestination() {
        loadDieTable();
        loadStopTable();
        loadCarrierMesTable();
        StoreInManager storeInManager = mock(StoreInManager.class);
        StoreOutManager storeOutManager = mock(StoreOutManager.class);
        EmptyManagerImpl emptyManager = mock(EmptyManagerImpl.class);

        StorageStateContextHelper helper = new StorageStateContextHelperImpl();
        StorageStateContext context = new StorageStateContextImpl(helper);
        StorageState storageState = context.getStorageState();
        assertNotNull(storageState);
        Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, context);

        Carrier c = new Carrier();
        c.setCarrierNumber(113);
        c.setDie(Die.findDie(166L));
       // storage.recalculateCarrierDestination(c);
    }

    @Test
    public void successfullyUpdateCarrier() {
         loadDieTable();
        loadStopTable();
        loadCarrierMesTable();
        StoreInManager storeInManager = mock(StoreInManager.class);
        StoreOutManager storeOutManager = mock(StoreOutManager.class);
        EmptyManagerImpl emptyManager = mock(EmptyManagerImpl.class);


        StorageStateContext context =mock(StorageStateContext.class);
        when(context.getCarrier(114)).thenReturn(CarrierMes.findCarrierByCarrierNumber(114));
        StorageState storageState = mock(StorageState.class);
        when(context.getStorageState()).thenReturn(storageState);
        when(storageState.carrierExistsInStorageState(Matchers.<Carrier>any())).thenReturn(true);
        Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, context);

        Carrier c = new Carrier ();
        Stop stop =  new Stop(1232L);
        stop.setStopArea(StopArea.ROW);
        c.setCurrentLocation(stop);

        storage.sendCarrierUpdateMessage(c);

        Carrier c1 = new Carrier ();
        Stop stop1 = new Stop();
        stop1.setStopArea(StopArea.STORE_OUT_ROUTE);
        c1.setCurrentLocation(stop1);
        c1.setCarrierNumber(114);
        storage.sendCarrierUpdateMessage(c1);
    }

    @Test(expected = InvalidDieException.class)
    public void successfullyRetrieveCarrier(){
        loadDieTable();
        loadStopTable();
        loadCarrierMesTable();

        StoreInManager storeInManager = mock(StoreInManager.class);
        EmptyManagerImpl emptyManager = mock(EmptyManagerImpl.class);

        StorageStateContextHelper helper = new StorageStateContextHelperImpl();
        StorageStateContext context =new StorageStateContextImpl(helper)  ;
        StorageState storageState = context.getStorageState() ;
        assertNotNull(storageState);
        StoreOutManager storeOutManager = new StoreOutManagerImpl(context)  ;
        Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, context);

        storage.retrieve(die);
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
        mesCarrier4.setCarrierNumber(114);
        mesCarrier4.setCurrentLocation(513L);
        mesCarrier4.setDestination(500L);
        mesCarrier4.setDieNumber(172);
        mesCarrier4.setQuantity(10);
        mesCarrier4.setStatus(0);
        mesCarrier4.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
        mesCarrier4.setProductionRunNumber(100);
        mesCarrier4.setOriginationLocation(0);
        mesCarrier4.setUpdateDate(new Timestamp(System.currentTimeMillis()));

        mesCarrier1.persist();
        mesCarrier2.persist();
        mesCarrier3.persist();
        mesCarrier4.persist();
    }

    void loadDieTable() {
        die = new Die();
        die.setId(166L);
        die.setDescription("left Die");
        die.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        die.persist();
    }

    void loadStopTable() {
        Stop stop = new Stop();
        stop.setId(1230L);
        stop.setName("ST12-30");
        stop.setStopArea(StopArea.ROW);
        stop.setStopType(StopType.NO_ACTION);

        Stop stop1 = new Stop();
        stop1.setId(1232L);
        stop1.setName("ST12-32");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.ROW);


        Stop stop2 = new Stop();
        stop2.setId(513L);
        stop2.setName("ST5-13");
        stop2.setStopType(StopType.STORE_IN_ALL_LANES);
        stop2.setStopArea(StopArea.STORE_IN_ROUTE);


        Stop stop3 = new Stop();
        stop3.setId(500L);
        stop3.setName("ST5");
        stop3.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop3.setStopArea(StopArea.STORE_OUT_ROUTE);


        stop.persist();
        stop1.persist();
        stop2.persist();
        stop3.persist();



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


        row2.persist();
        row3.persist();
    }
}
