package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.manager.*;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Michael Grecol
 * Date: 8/5/2014
 */
public class StoreInManagerImplTestMixRowsWithNoEmpty {

    List<StorageRow> storageLanes;
    StoreInManager storeInManager;
    Die leftDie, rightDie, someDie;
    StorageState storageState;
    Storage storage;
    StorageRow expectedLane;
    int testMode = 1;

    public void initStorage() {
        someDie = new Die();
        someDie.setId(100l);
        someDie.setDescription("left_die_101");
        someDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Stop stop1 = new Stop(1232l);
        stop1.setStopType(StopType.NO_ACTION);

        Calendar c = Calendar.getInstance();
        Timestamp today = new Timestamp(c.getTimeInMillis());

        c.add(Calendar.DATE, -2);
        Timestamp twoDaysOld = new Timestamp(c.getTimeInMillis());

        Carrier carrier2 = new Carrier(1, today, new Integer(103), stop1, null, CarrierStatus.SHIPPABLE, new Integer(200), leftDie);

        StorageRow StorageRow = new StorageRow(0, "StorageRow 0", 12, testMode);
        StorageRow.setStop(new Stop(1201L));
        StorageRow.store(carrier2);
        StorageRow.setStorageArea(StorageArea.C_HIGH);

        expectedLane = new StorageRow(1, "StorageRow 1", 12, testMode);
        expectedLane.setStop(new Stop(1202L));
        Carrier carrier3 = new Carrier(1, today, new Integer(103), stop1, null, CarrierStatus.SHIPPABLE, new Integer(200), leftDie);
        expectedLane.store(carrier3);
        expectedLane.setStorageArea(StorageArea.C_LOW);

        StorageRow lane2 = new StorageRow(2, "StorageRow 2", 12, testMode);
        lane2.setStop(new Stop(1203L));
        Carrier carrier4 = new Carrier(1, today, new Integer(103), stop1, null, CarrierStatus.SHIPPABLE, new Integer(200), leftDie);
        lane2.store(carrier4);
        lane2.setStorageArea(StorageArea.C_HIGH);

         StorageRow lane3 = new StorageRow(3, "StorageRow 3", 21, testMode);
        lane3.setStop(new Stop(1221L));
        Carrier carrier5 = new Carrier(1, today, new Integer(103), stop1, null, CarrierStatus.SHIPPABLE, new Integer(200), leftDie);
        lane3.store(carrier5);
        lane3.setStorageArea(StorageArea.A_AREA);

        storageLanes = new ArrayList<StorageRow>();
        storageLanes.add(StorageRow);
        storageLanes.add(expectedLane);
        storageLanes.add(lane2);
        storageLanes.add(lane3);

        storageState = new StorageStateImpl(storageLanes);

        StorageStateContext context = mock(StorageStateContext.class);
        when(context.getStorageState()).thenReturn(storageState);
        storeInManager = new StoreInManagerImpl(context);
        storage = new StorageImpl(storeInManager, null, null, context);
    }

    @Test
    public void storeCarrierWhenNoRowsExistsWithDie(){
    	  initStorage();
    	  assertEquals(1, storageLanes.get(0).getCurrentCarrierCount());
        Stop storeInStop = new Stop(513l);
        storeInStop.setStopType(StopType.STORE_IN_ALL_LANES);
        Carrier carrier = new Carrier(15, someDie, 1, new Timestamp(System.currentTimeMillis()), 105);
        carrier.setCurrentLocation(storeInStop);
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);
        //storage.store(carrier);
        StorageRow lane2 = storeInManager.store(carrier);
        assertNotNull(lane2);
        assertEquals(expectedLane, lane2);
    }
    
   
}
