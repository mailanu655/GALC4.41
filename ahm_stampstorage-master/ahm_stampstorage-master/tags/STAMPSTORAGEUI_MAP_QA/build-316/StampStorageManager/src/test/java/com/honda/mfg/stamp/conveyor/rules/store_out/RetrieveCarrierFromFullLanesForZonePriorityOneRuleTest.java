package com.honda.mfg.stamp.conveyor.rules.store_out;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.*;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * User: vcc44349
 * Date: 10/17/2013
 */
public class RetrieveCarrierFromFullLanesForZonePriorityOneRuleTest {
    private static int index = 0;

    @Test
    public void successfullyRetrieveCarriersWithHighVolumeParts() {
        //Pre conditions
        StorageStateContext context = getContext();
        StoreOutRule storeOutRule =
                new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        Die part = new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME);

        //test
        StorageRow sRow = storeOutRule.processRule(part);

        // Post condition / assertions
        assertNotNull(sRow);
        assertEquals(StorageArea.S_AREA, sRow.getStorageArea());
        Carrier carrier = sRow.release();
        assertEquals(OM.PART_A, carrier.getDie().getId());
        assertEquals(OM.OLD_PRODUCTION_RUN_NUMBER, carrier.getProductionRunNo());
        assertEquals(sRow.getCapacity().intValue(), OM.S_AREA_CAPACITY.intValue());

    }

    @Test
    public void successfullyRetrieveCarriersWithMediumVolumeParts() {
        //Pre conditions
        StorageStateContext context = getContext();
        StoreOutRule storeOutRule =
                new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        Die part = new Die(OM.PART_B, PartProductionVolume.MEDIUM_VOLUME);

        //test
        StorageRow sRow = storeOutRule.processRule(part);

        // Post condition / assertions
        assertNotNull(sRow);
        assertEquals(StorageArea.C_LOW, sRow.getStorageArea());
        Carrier carrier = sRow.release();
        assertEquals(OM.PART_B, carrier.getDie().getId());
        assertEquals(OM.OLD_PRODUCTION_RUN_NUMBER, carrier.getProductionRunNo());
        assertEquals(sRow.getCapacity().intValue(), OM.C_LOW_CAPACITY.intValue());

    }

    @Test
    public void successfullyRetrieveCarriersWithLowVolumeParts() {
        //Pre conditions
        StorageStateContext context = getContext();
        StoreOutRule storeOutRule =
                new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        Die part = new Die(OM.PART_C, PartProductionVolume.LOW_VOLUME);

        //test
        StorageRow sRow = storeOutRule.processRule(part);

        // Post condition / assertions
        assertNotNull(sRow);
        assertEquals(StorageArea.C_HIGH, sRow.getStorageArea());
        Carrier carrier = sRow.release();
        assertEquals(OM.PART_C, carrier.getDie().getId());
        assertEquals(OM.OLD_PRODUCTION_RUN_NUMBER, carrier.getProductionRunNo());
        assertEquals(sRow.getCapacity().intValue(), OM.C_HIGH_CAPACITY.intValue());

    }

    @Test
    public void successfullyRetrieveEmptyCarriers() {
        //Pre conditions
        StorageStateContext context = getContext();
        StoreOutRule storeOutRule =
                new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        //test
        Die part = new Die(OM.EMPTY, PartProductionVolume.EMPTY);

        StorageRow sRow = storeOutRule.processRule(part);

        // Post condition / assertions
        assertNotNull(sRow);
        assertEquals(StorageArea.A_AREA, sRow.getStorageArea());
        Carrier carrier = sRow.release();
        assertEquals(new Long(999), carrier.getDie().getId());
        assertEquals(sRow.getCapacity(), OM.A_AREA_CAPACITY);
    }

    @Test(expected = NoApplicableRuleFoundException.class)
    public void AttemptingToRetrieveCarriersFromNoCapacityLanes() {
        //Pre conditions
        StorageStateContext context = getContext();
        StoreOutRule storeOutRule =
                new RetrieveCarrierFromRowOfZonePriorityAndRowCondition(context, null, LaneCondition.FULL, StoragePriority.Priority.ONE);
        Die part = new Die(103L, PartProductionVolume.HIGH_VOLUME);

        //test
        StorageRow sRow = storeOutRule.processRule(part);

        //Post condition / assertions
        assertNull(sRow);
    }


    private void pause(long duration) {
        long delta = System.currentTimeMillis();
        while (System.currentTimeMillis() - delta < duration) {
        }

    }

    private StorageStateContext getContext() {
        StorageStateContext context = new StorageStateContextMock(getStorageState());
        pause(1000);
        return context;
    }

    private StorageState getStorageState() {
        return new StorageStateImpl(getLanes());
    }

    private List<StorageRow> getLanes() {

        //same part oldest production run no and sorted in ascending by carrier count
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        //same part oldest production run no and sorted in ascending by carrier count
        StorageRow aAreaLane1 = OM.lane_A_AREA_Empty();
        StorageRow aAreaLane2 = OM.lane_A_AREA_Empty();
        StorageRow aAreaLane3 = OM.lane_A_AREA_Empty();
        StorageRow aAreaLane4 = OM.lane_A_AREA_Empty();
        StorageRow cHighLane1 = OM.lane_C_HIGH_Empty();
        StorageRow cHighLane2 = OM.lane_C_HIGH_Empty();
        StorageRow cHighLane3 = OM.lane_C_HIGH_Empty();
        StorageRow cHighLane4 = OM.lane_C_HIGH_Empty();
        StorageRow cLowLane1 = OM.lane_C_LOW_Empty();
        StorageRow cLowLane2 = OM.lane_C_LOW_Empty();
        StorageRow cLowLane3 = OM.lane_C_LOW_Empty();
        StorageRow cLowLane4 = OM.lane_C_LOW_Empty();

        StorageRow sAreaLane1 = OM.lane_S_AREA_Empty();
        sAreaLane1.setStop(new Stop(44L));
        StorageRow sAreaLane2 = OM.lane_S_AREA_Empty();
        sAreaLane2.setStop(new Stop(45L));


        StorageRow bAreaLane1 = OM.lane_B_AREA_Empty();
        bAreaLane1.setStop(new Stop(41L));
        StorageRow bAreaLane2 = OM.lane_B_AREA_Empty();
        bAreaLane2.setStop(new Stop(42L));
        StorageRow bAreaLane3 = OM.lane_B_AREA_Empty();
        bAreaLane3.setStop(new Stop(43L));
        StorageRow bAreaLane4 = OM.lane_B_AREA_Empty();
        bAreaLane4.setStop(new Stop(44L));

        aAreaLane1.setStop(new Stop(21L));
        aAreaLane2.setStop(new Stop(22L));
        aAreaLane3.setStop(new Stop(23L));
        aAreaLane4.setStop(new Stop(24L));

        cHighLane1.setStop(new Stop(1L));
        cHighLane2.setStop(new Stop(2L));
        cHighLane3.setStop(new Stop(3L));
        cHighLane4.setStop(new Stop(4L));

        cLowLane1.setStop(new Stop(30L));
        cLowLane2.setStop(new Stop(31L));
        cLowLane3.setStop(new Stop(32L));
        cLowLane4.setStop(new Stop(33L));


        //low volume capacity
        /**Setup the storage state as follows:
        A Area (capacity:10)
        ------
        lane1: high volume, full lane, new production run
        lane2: high volume, full lane, old production run
        lane3: low volume, old production run + 1 new in the end
        lane4: low volume, full lane, new production run
        
        C-High (capacity:20)
        ------
        lane1: empty, full lane
        lane2: low volume, full lane, old production run
        lane3: med volume, old production run + 1 high vol, new run in the end
        lane4: med volume, full lane, new production run

        C-Low (capacity:30)
        ------
        lane1: empty, full lane
        lane2: med volume, full lane, old production run
        lane3: low volume, old production run + 1 new in the end
        lane4: low volume, full lane, old production run
        
        B Area (capacity:31)
        ------
        lane1: med volume, full lane, old production run
        lane2: high volume, full lane, old production run
        lane3: high volume, old production run + 1 new med vol in the end
        lane4: empty, full lane
        
        S Area (capacity:87)
        ------
        lane1: med volume, full lane, old production run
        lane2: high volume, full lane, old production run
        
       */
        
        
        for (int i = 0; i < aAreaLane1.getCapacity(); i++) {
            aAreaLane1.store(getEmptyCarrier(OM.EMPTY, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, aAreaLane1.getStop(), aAreaLane1.getStop()));
        }

        for (int i = 0; i < aAreaLane2.getCapacity(); i++) {
            aAreaLane2.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane2.getStop(), aAreaLane2.getStop()));
        }

        for (int i = 0; i < aAreaLane3.getCapacity() - 1; i++) {
            aAreaLane3.store(getLowVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, aAreaLane3.getStop(), aAreaLane3.getStop()));
        }
        aAreaLane3.store(getLowVolumeCarrier(OM.PART_A, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, aAreaLane3.getStop(), aAreaLane3.getStop()));
        for (int i = 0; i < aAreaLane4.getCapacity() - 1; i++) {
            aAreaLane4.store(getLowVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, aAreaLane4.getStop(), aAreaLane4.getStop()));
        }
        //medium volume capacity
        for (int i = 0; i < cHighLane1.getCapacity(); i++) {
        	cHighLane1.store(getEmptyCarrier(OM.EMPTY, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, cHighLane1.getStop(), cHighLane1.getStop()));
        }

        for (int i = 0; i < cHighLane2.getCapacity(); i++) {
            cHighLane2.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, cHighLane2.getStop(), cHighLane2.getStop()));
        }

        for (int i = 0; i < cHighLane3.getCapacity() - 1; i++) {
            cHighLane3.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, cHighLane3.getStop(), cHighLane3.getStop()));
        }
        cHighLane3.store(getHighVolumeCarrier(OM.PART_A, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, cHighLane3.getStop(), cHighLane3.getStop()));
        for (int i = 0; i < cHighLane4.getCapacity() - 1; i++) {
            cHighLane4.store(getMediumVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, cHighLane4.getStop(), cHighLane4.getStop()));
        }

        //high volume capacity
        for (int i = 0; i < cLowLane1.getCapacity(); i++) {
            cLowLane1.store(getEmptyCarrier(OM.EMPTY, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, cLowLane1.getStop(), cLowLane1.getStop()));
        }

        for (int i = 0; i < cLowLane2.getCapacity(); i++) {
            cLowLane2.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, cLowLane2.getStop(), cLowLane2.getStop()));
        }

        for (int i = 0; i < cLowLane3.getCapacity() - 1; i++) {
            cLowLane3.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, cLowLane3.getStop(), cLowLane3.getStop()));
        }
        cLowLane3.store(getLowVolumeCarrier(OM.PART_C, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, cLowLane3.getStop(), cLowLane3.getStop()));
        for (int i = 0; i < cLowLane4.getCapacity() ; i++) {
        	cLowLane4.store(getLowVolumeCarrier(OM.PART_C, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, cLowLane4.getStop(), cLowLane4.getStop()));
        }

          //high volume capacity
        for (int i = 0; i < bAreaLane1.getCapacity(); i++) {
        	bAreaLane1.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, bAreaLane1.getStop(), bAreaLane1.getStop()));
        }
        for (int i = 0; i < bAreaLane2.getCapacity().intValue(); i++) {
            bAreaLane2.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, bAreaLane2.getStop(), bAreaLane2.getStop()));
        }

        for (int i = 0; i < bAreaLane3.getCapacity().intValue() - 1; i++) {
            bAreaLane3.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, bAreaLane3.getStop(), bAreaLane3.getStop()));
        }
        bAreaLane3.store(getMediumVolumeCarrier(OM.PART_B, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, bAreaLane3.getStop(), bAreaLane3.getStop()));
        
        for (int i = 0; i < bAreaLane4.getCapacity().intValue(); i++) {
            bAreaLane4.store(getEmptyCarrier(OM.EMPTY, OM.PRODUCTION_RUN_NUMBER, OM.NEW_PRODUCTION_RUN_TIMESTAMP, bAreaLane4.getStop(), bAreaLane4.getStop()));
        }

        for (int i = 0; i < sAreaLane1.getCapacity().intValue(); i++) {
            sAreaLane1.store(getMediumVolumeCarrier(OM.PART_B, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, sAreaLane1.getStop(), sAreaLane1.getStop()));
        }

        for (int i = 0; i < sAreaLane2.getCapacity().intValue(); i++) {
        	sAreaLane2.store(getHighVolumeCarrier(OM.PART_A, OM.OLD_PRODUCTION_RUN_NUMBER, OM.OLD_PRODUCTION_RUN_TIMESTAMP, sAreaLane2.getStop(), sAreaLane2.getStop()));
        }


        lanes.add(aAreaLane1);
        lanes.add(aAreaLane2);
        lanes.add(aAreaLane3);
        lanes.add(aAreaLane4);
        lanes.add(cHighLane1);
        lanes.add(cHighLane2);
        lanes.add(cHighLane3);
        lanes.add(cHighLane4);
        lanes.add(cLowLane1);
        lanes.add(cLowLane2);
        lanes.add(cLowLane3);
        lanes.add(cLowLane4);
        lanes.add(sAreaLane1);
        lanes.add(sAreaLane2);
        lanes.add(bAreaLane1);
        lanes.add(bAreaLane2);
        lanes.add(bAreaLane3);
        lanes.add(bAreaLane4);

        return lanes;
    }

    Carrier getLowVolumeCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp, Stop currentlocation, Stop destination) {
        return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.LOW_VOLUME, currentlocation, destination);
    }

    Carrier getMediumVolumeCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp, Stop currentlocation, Stop destination) {
        return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.MEDIUM_VOLUME, currentlocation, destination);
    }

    Carrier getHighVolumeCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp, Stop currentlocation, Stop destination) {
        return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.HIGH_VOLUME, currentlocation, destination);
    }

    Carrier getEmptyCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp, Stop currentlocation, Stop destination) {
        return getCarrier(dieNumber, prodRunRunNo, productionRuntimestamp, PartProductionVolume.EMPTY, currentlocation, destination);
    }

    private Carrier getCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp, PartProductionVolume volume, Stop currentlocation, Stop destination) {
        Carrier carrier = new Carrier(index++, new Die(dieNumber, volume), 1, productionRuntimestamp, prodRunRunNo);
        carrier.setCurrentLocation(currentlocation);
        carrier.setDestination(destination);
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);
        return carrier;
    }
}
