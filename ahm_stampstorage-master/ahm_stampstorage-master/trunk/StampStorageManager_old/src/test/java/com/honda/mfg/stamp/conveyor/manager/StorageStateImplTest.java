package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * User: Jeffrey M Lutz
 * Date: 3/2/11
 */
public class StorageStateImplTest {

    @Test
    public void findLaneThatIsEmpty() {
        StorageRow actualLaneImpl = LaneOM.storageState.queryForRow(RowMatchers.isCurrentCapacityEmpty());
        assertEquals(LaneOM.emptyLaneImpl, actualLaneImpl);
    }

    @Test
    public void findLaneThatIsEmptyAndAttemptToRelease() {
        StorageRow actualLaneImpl = LaneOM.storageState.queryForRow(RowMatchers.isCurrentCapacityEmpty());
        assertEquals(LaneOM.emptyLaneImpl, actualLaneImpl);
    }

    @Test
    public void findLaneThatIsPartial() {
        StorageRow actualLaneImpl = LaneOM.storageState.queryForRow(RowMatchers.isCurrentCapacityPartial());
        assertEquals(LaneOM.partialPartOneLaneImpl, actualLaneImpl);
    }

    @Test
    public void findLaneThatIsFull() {
        StorageRow actualLaneImpl = LaneOM.storageState.queryForRow(
                not(RowMatchers.isCurrentCapacityEmpty())
        );
        assertEquals(LaneOM.partialPartOneLaneImpl, actualLaneImpl);
    }

    @Test
    public void findLaneThatIsEmptyAndGivenMaxCapacity() {
        StorageRow actualLaneImpl;
        actualLaneImpl = LaneOM.storageState.queryForRow(
                allOf(
                        RowMatchers.isCurrentCapacityEmpty(),
                        RowMatchers.hasMaxCapacityOf(LaneOM.MAX_CAPACITY)
                )
        );
        assertEquals(LaneOM.emptyLaneImpl, actualLaneImpl);
    }

    @Test
    public void findLaneThatIsPartialCapacityAndGivenMaxCapacityWithOnlySamePartType() {
        StorageRow actualLaneImpl;
        actualLaneImpl = LaneOM.storageState.queryForRow(
                allOf(
                        RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.hasMaxCapacityOf(LaneOM.MAX_CAPACITY),
                        RowMatchers.isLaneWithOnlySingleDieNumberOf(new Long(LaneOM.PART_A))
                )
        );
        assertEquals(LaneOM.partialPartOneLaneImpl, actualLaneImpl);
    }

    @Test
    public void findLanesThatAreMixedAndGivenMaxCapacity() {
        List<StorageRow> actualLaneImpl;
        actualLaneImpl = LaneOM.storageState.queryForRows(
                allOf(
                        RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.hasMaxCapacityOf(LaneOM.MAX_CAPACITY)
                )
        );
        assertEquals(2, actualLaneImpl.size());
    }

    @Test
    public void testEquals() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        int id = 0;
        Carrier carrier;
        Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
        Integer prodRunNo = 108;
        Stop currentLocation = new Stop("05-13");
        currentLocation.setId(10L);
        Stop destination = new Stop("05-13");
        destination.setId(10L);
        CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
        Integer tagID = 123;

        carrier = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus, tagID, new Die(10L, PartProductionVolume.HIGH_VOLUME));
        //carrier.setQuantity(10);

        Carrier carrier1 = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus, tagID, new Die(10L, PartProductionVolume.HIGH_VOLUME));
        // carrier1.setQuantity(10);

        StorageRow laneImpl1 = new StorageRow(0, "lane0", 12,1);

        laneImpl1.store(carrier);
        laneImpl1.store(carrier1);


        StorageRow laneImpl2 = new StorageRow(1, "lane1", 12,1);
        Carrier carrier3 = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus, tagID, new Die(10L, PartProductionVolume.HIGH_VOLUME));
        // carrier3.setQuantity(10);

        Carrier carrier4 = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus, tagID, new Die(10L, PartProductionVolume.HIGH_VOLUME));
        // carrier4.setQuantity(10);

        laneImpl2.store(carrier3);
        laneImpl2.store(carrier4);

        lanes.add(laneImpl1);
        lanes.add(laneImpl2);
        StorageStateImpl storageState1 = new StorageStateImpl(lanes);

        StorageStateImpl storageState2 = new StorageStateImpl(lanes);

        assertEquals(storageState1, storageState2);
    }

    @Test
    public void successfullyTestIfCarrierPartsAreaOnBackOrder() {
        StorageState storageState = new StorageStateImpl(LaneOM.storageState.getRows());
        LaneOM.storageState.toString();

        assertFalse(storageState.isCarrierPartsOnBackOrder(LaneOM.partOneCarrier));
    }

    @Test
    public void successfullyStoreReworkCarrierInLane() {
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        StorageRow laneImpl1 = new StorageRow(0, "lane0", 12,1);
        laneImpl1.setStop(new Stop(1201l));
        StorageRow laneImpl2 = new StorageRow(1, "lane1", 12,1);
        laneImpl2.setStop(new Stop(1202l));
         lanes.add(laneImpl1);
        lanes.add(laneImpl2);


        Stop currentLocation = new Stop();
        currentLocation.setStopType(StopType.REWORK);
        Carrier c = new Carrier();
        c.setCarrierNumber(101);
        c.setCurrentLocation(currentLocation);
        c.setCarrierStatus(CarrierStatus.SHIPPABLE);

        StorageState storageState = new StorageStateImpl(lanes);
        storageState.storeInLane(c, laneImpl1);

        assertNotNull(storageState);
    }

}
