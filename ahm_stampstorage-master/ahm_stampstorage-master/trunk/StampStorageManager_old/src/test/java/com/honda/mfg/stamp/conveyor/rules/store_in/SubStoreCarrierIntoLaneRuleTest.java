package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.manager.OM;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContextMock;
import com.honda.mfg.stamp.conveyor.manager.StorageStateImpl;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: vcc30690
 * Date: 7/27/11
 */
public class SubStoreCarrierIntoLaneRuleTest {

    StorageRow lowLane1, lowLane2, lowLane3, lowLane4, lowLane5;


    @Test
    public void successfullyStoreCarriersIntoLowCapacityPartialLane() {
        //Pre Condition
        StorageStateContext context = new StorageStateContextMock(new StorageStateImpl(getPartialLanes()));
        pause(1000);

       // RulesConfig rulesConfig = getRulesConfig();
        StoreInRule storeInRule =
                new SubStoreCarrierIntoLaneRule(context, null, StorageArea.A_AREA);

        Carrier carrier = new Carrier(101, new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME), 10, new Timestamp(System.currentTimeMillis()), 500);
        carrier.setCurrentLocation(new Stop());
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);

        // Perform test
        StorageRow StorageRow = storeInRule.processRule(carrier);

        // Post condition / assertions
        assertNotNull(StorageRow);
        assertEquals(LaneCondition.PARTIAL, StorageRow.getLaneCondition());
        assertEquals(1, StorageRow.getCurrentCarrierCount());
    }

    @Test
    public void successfullyStoreCarriersIntoLowCapacityVacantLane() {
        //Pre Condition
        StorageStateContext context = new StorageStateContextMock(new StorageStateImpl(getVacantLanes()));
        pause(1000);
       // RulesConfig rulesConfig = getRulesConfig();
        StoreInRule storeInRule =
                new SubStoreCarrierIntoLaneRule(context, null, StorageArea.A_AREA);

        Carrier carrier = new Carrier(101, new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME), 10, new Timestamp(System.currentTimeMillis()), 500);
        carrier.setCurrentLocation(new Stop());
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);

        // Perform test
        StorageRow StorageRow = storeInRule.processRule(carrier);

        // Post condition / assertions
        assertNotNull(StorageRow);
        assertEquals(LaneCondition.VACANT, StorageRow.getLaneCondition());
        assertEquals(0, StorageRow.getCurrentCarrierCount());
    }

    @Test
    public void successfullyStoreCarriersIntoLowCapacityEmptyLane() {
        //Pre Condition
        StorageStateContext context = new StorageStateContextMock(new StorageStateImpl(getEmptyLanes()));
        pause(1000);
       // RulesConfig rulesConfig = getRulesConfig();
        StoreInRule storeInRule =
                new SubStoreCarrierIntoLaneRule(context, null, StorageArea.A_AREA);

        Carrier carrier = new Carrier(101, new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME), 10, new Timestamp(System.currentTimeMillis()), 500);
        carrier.setCurrentLocation(new Stop());
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);

        // Perform test
        StorageRow StorageRow = storeInRule.processRule(carrier);

        // Post condition / assertions
        assertNotNull(StorageRow);
        assertEquals(LaneCondition.EMPTY, StorageRow.getLaneCondition());
        assertEquals(1, StorageRow.getCurrentCarrierCount());
    }


    @Test
    public void successfullyStoreCarriersIntoLowCapacityMixedBackLane() {
        //Pre Condition
        StorageStateContext context = new StorageStateContextMock(new StorageStateImpl(getMixedBackLanes()));
        pause(1000);
        //RulesConfig rulesConfig = getRulesConfig();
        StoreInRule storeInRule =
                new SubStoreCarrierIntoLaneRule(context, null, StorageArea.A_AREA);

        Carrier carrier = new Carrier(101, new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME), 10, new Timestamp(System.currentTimeMillis()), 500);
        carrier.setCurrentLocation(new Stop());
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);

        // Perform test
        StorageRow StorageRow = storeInRule.processRule(carrier);

        // Post condition / assertions
        assertNotNull(StorageRow);
        assertEquals(LaneCondition.MIXED, StorageRow.getLaneCondition());
        assertEquals(3, StorageRow.getCurrentCarrierCount());

    }

     @Test
    public void successfullyStoreCarriersIntoLowCapacityMixedLane() {
        //Pre Condition
        StorageStateContext context = new StorageStateContextMock(new StorageStateImpl(getMixedLanes()));
        pause(1000);
        //RulesConfig rulesConfig = getRulesConfig();
        StoreInRule storeInRule =
                new SubStoreCarrierIntoLaneRule(context, null, StorageArea.A_AREA);

        Carrier carrier = new Carrier(101, new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME), 10, new Timestamp(System.currentTimeMillis()), 500);
        carrier.setCurrentLocation(new Stop());
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);

        // Perform test
        StorageRow StorageRow = storeInRule.processRule(carrier);

        // Post condition / assertions
        assertNotNull(StorageRow);
        assertEquals(LaneCondition.MIXED, StorageRow.getLaneCondition());
        assertEquals(3, StorageRow.getCurrentCarrierCount());

    }

    private void pause(long duration) {
        long delta = System.currentTimeMillis();
        while (System.currentTimeMillis() - delta < duration) {
        }

    }


//    private RulesConfig getRulesConfig() {
//        Map<LaneVolumeStorage, Integer> capacityMap =
//                new HashMap<LaneVolumeStorage, Integer>();
//        capacityMap.put(LaneVolumeStorage.LOW_VOLUME_STORAGE, 5);
//        capacityMap.put(LaneVolumeStorage.MEDIUM_VOLUME_STORAGE, 7);
//        capacityMap.put(LaneVolumeStorage.HIGH_VOLUME_STORAGE, 12);
//        capacityMap.put(LaneVolumeStorage.ROW_35_HIGH_VOLUME_STORAGE,10);
//
//        return new RulesConfig(capacityMap, 7);
//    }

    private List<StorageRow> getVacantLanes() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        lowLane1 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane2 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane3 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane4 = new StorageRow(0, "StorageRow  0", 5,1);

        lowLane1.setStop(new Stop());
        lowLane2.setStop(new Stop());
        lowLane3.setStop(new Stop());
        lowLane4.setStop(new Stop());

          lowLane1.setStorageArea(StorageArea.A_AREA);
        lowLane1.setAvailability(StopAvailability.AVAILABLE);
         lowLane2.setStorageArea(StorageArea.A_AREA);
        lowLane2.setAvailability(StopAvailability.AVAILABLE);
         lowLane3.setStorageArea(StorageArea.A_AREA);
        lowLane3.setAvailability(StopAvailability.AVAILABLE);
         lowLane4.setStorageArea(StorageArea.A_AREA);
        lowLane4.setAvailability(StopAvailability.AVAILABLE);

        lanes.add(lowLane1);
        lanes.add(lowLane2);
        lanes.add(lowLane3);
        lanes.add(lowLane4);

        return lanes;
    }

    List<StorageRow> getEmptyLanes() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        lowLane1 = new StorageRow(0, "StorageRow  0", 12,1);
        lowLane2 = new StorageRow(0, "StorageRow  0", 12,1);
        lowLane3 = new StorageRow(0, "StorageRow  0", 12,1);
        lowLane4 = new StorageRow(0, "StorageRow  0", 12,1);
        lowLane5 = new StorageRow(0, "StorageRow  0", 10,1);

        lowLane1.setStop(new Stop());
        lowLane2.setStop(new Stop());
        lowLane3.setStop(new Stop());
        lowLane4.setStop(new Stop());
        lowLane5.setStop(new Stop());

          lowLane1.setStorageArea(StorageArea.A_AREA);
        lowLane1.setAvailability(StopAvailability.AVAILABLE);
         lowLane2.setStorageArea(StorageArea.A_AREA);
        lowLane2.setAvailability(StopAvailability.AVAILABLE);
         lowLane3.setStorageArea(StorageArea.A_AREA);
        lowLane3.setAvailability(StopAvailability.AVAILABLE);
         lowLane4.setStorageArea(StorageArea.A_AREA);
        lowLane4.setAvailability(StopAvailability.AVAILABLE);
        lowLane5.setStorageArea(StorageArea.A_AREA);
        lowLane5.setAvailability(StopAvailability.AVAILABLE);

        Carrier lowCapacityCarrier1 = OM.carrier_Empty();

        Carrier lowCapacityCarrier2 = OM.carrier_Empty();

        Carrier lowCapacityCarrier3 = OM.carrier_Empty();


        lowLane1.store(lowCapacityCarrier1);
        lowLane2.store(lowCapacityCarrier2);
        lowLane3.store(lowCapacityCarrier2);
        lowLane3.store(lowCapacityCarrier3);
        lowLane4.store(OM.carrier_LowVolume_PartB());
        lowLane5.store(OM.carrier_LowVolume_PartB());

        lanes.add(lowLane1);
        lanes.add(lowLane2);
        lanes.add(lowLane3);
        lanes.add(lowLane4);
        lanes.add(lowLane5);

        return lanes;
    }

    List<StorageRow> getPartialLanes() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        lowLane1 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane2 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane3 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane4 = new StorageRow(0, "StorageRow  0", 5,1);

        lowLane1.setStop(new Stop());
        lowLane2.setStop(new Stop());
        lowLane3.setStop(new Stop());
        lowLane4.setStop(new Stop());

        lowLane1.setStorageArea(StorageArea.A_AREA);
        lowLane1.setAvailability(StopAvailability.AVAILABLE);
         lowLane2.setStorageArea(StorageArea.A_AREA);
        lowLane2.setAvailability(StopAvailability.AVAILABLE);
         lowLane3.setStorageArea(StorageArea.A_AREA);
        lowLane3.setAvailability(StopAvailability.AVAILABLE);
         lowLane4.setStorageArea(StorageArea.A_AREA);
        lowLane4.setAvailability(StopAvailability.AVAILABLE);

        Carrier lowCapacityCarrier1 = getCarrier(OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP);

        Carrier lowCapacityCarrier2 = getCarrier(OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP);

        Carrier lowCapacityCarrier3 = getCarrier(OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP);


        lowLane1.store(lowCapacityCarrier1);
        lowLane2.store(lowCapacityCarrier2);
        lowLane3.store(lowCapacityCarrier2);
        lowLane3.store(lowCapacityCarrier3);
        lowLane4.store(OM.carrier_LowVolume_PartB());


        lanes.add(lowLane1);
        lanes.add(lowLane2);
        lanes.add(lowLane3);
        lanes.add(lowLane4);

        return lanes;
    }

    List<StorageRow> getMixedLanes() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        lowLane1 = new StorageRow(0, "StorageRow  0", 7,1);
        lowLane2 = new StorageRow(0, "StorageRow  0", 7,1);
        lowLane3 = new StorageRow(0, "StorageRow  0", 7,1);
        lowLane4 = new StorageRow(0, "StorageRow  0", 7,1);

        lowLane1.setStop(new Stop());
        lowLane2.setStop(new Stop());
        lowLane3.setStop(new Stop());
        lowLane4.setStop(new Stop());

          lowLane1.setStorageArea(StorageArea.A_AREA);
        lowLane1.setAvailability(StopAvailability.AVAILABLE);
         lowLane2.setStorageArea(StorageArea.A_AREA);
        lowLane2.setAvailability(StopAvailability.AVAILABLE);
         lowLane3.setStorageArea(StorageArea.A_AREA);
        lowLane3.setAvailability(StopAvailability.AVAILABLE);
         lowLane4.setStorageArea(StorageArea.A_AREA);
        lowLane4.setAvailability(StopAvailability.AVAILABLE);

        Carrier lowCapacityCarrier1 = getCarrier(OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP);

        Carrier lowCapacityCarrier2 = getCarrier(OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP);

        Carrier lowCapacityCarrier3 = getCarrier(OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP);

        lowLane1.store(lowCapacityCarrier2);
        lowLane1.store(lowCapacityCarrier1);
        lowLane1.store(OM.carrier_LowVolume_PartB());
        lowLane2.store(lowCapacityCarrier2);
        lowLane2.store(OM.carrier_LowVolume_PartB());
        lowLane2.store(OM.carrier_LowVolume_PartB());
        lowLane3.store(lowCapacityCarrier2);
        lowLane3.store(lowCapacityCarrier3);
        lowLane3.store(OM.carrier_LowVolume_PartB());
        lowLane4.store(OM.carrier_LowVolume_PartB());

        lanes.add(lowLane1);
        lanes.add(lowLane2);
        lanes.add(lowLane3);
        lanes.add(lowLane4);

        return lanes;
    }


    List<StorageRow> getMixedBackLanes() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();

        lowLane1 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane2 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane3 = new StorageRow(0, "StorageRow  0", 5,1);
        lowLane4 = new StorageRow(0, "StorageRow  0", 5,1);

        lowLane1.setStop(new Stop());
        lowLane2.setStop(new Stop());
        lowLane3.setStop(new Stop());
        lowLane4.setStop(new Stop());

          lowLane1.setStorageArea(StorageArea.A_AREA);
        lowLane1.setAvailability(StopAvailability.AVAILABLE);
         lowLane2.setStorageArea(StorageArea.A_AREA);
        lowLane2.setAvailability(StopAvailability.AVAILABLE);
         lowLane3.setStorageArea(StorageArea.A_AREA);
        lowLane3.setAvailability(StopAvailability.AVAILABLE);
         lowLane4.setStorageArea(StorageArea.A_AREA);
        lowLane4.setAvailability(StopAvailability.AVAILABLE);

        Carrier lowCapacityCarrier1 = getCarrier(OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP);

        Carrier lowCapacityCarrier2 = getCarrier(OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP);


        Carrier lowCapacityCarrier3 = getCarrier(OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP);


        lowLane2.store(OM.carrier_LowVolume_PartB());
        lowLane2.store(OM.carrier_LowVolume_PartB());
        lowLane2.store(lowCapacityCarrier2);

        lowLane3.store(OM.carrier_LowVolume_PartB());
        lowLane3.store(lowCapacityCarrier2);
        lowLane3.store(lowCapacityCarrier3);
        lowLane3.store(lowCapacityCarrier1);

        lowLane4.store(OM.carrier_LowVolume_PartB());
        lowLane4.store(lowCapacityCarrier1);
        lowLane4.store(lowCapacityCarrier2);


        lanes.add(lowLane2);
        lanes.add(lowLane3);
        lanes.add(lowLane4);

        return lanes;
    }

    Carrier getCarrier(Integer prodRunRunNo, Timestamp productionRuntimestamp) {
        Carrier lowCapacityCarrier1 = OM.carrier_LowVolume_PartA();
        lowCapacityCarrier1.setProductionRunNo(prodRunRunNo);
        lowCapacityCarrier1.setStampingProductionRunTimestamp(productionRuntimestamp);
        lowCapacityCarrier1.setCurrentLocation(new Stop());
        lowCapacityCarrier1.setCarrierStatus(CarrierStatus.SHIPPABLE);
        return lowCapacityCarrier1;
    }
}
