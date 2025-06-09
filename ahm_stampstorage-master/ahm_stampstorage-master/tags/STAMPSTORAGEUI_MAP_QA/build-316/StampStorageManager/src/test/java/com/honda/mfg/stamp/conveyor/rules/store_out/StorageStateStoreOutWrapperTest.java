package com.honda.mfg.stamp.conveyor.rules.store_out;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.*;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * User: vcc30690
 * Date: 3/14/11
 */
public class StorageStateStoreOutWrapperTest {

    private static int index = 0;

    //@Test
    public void findIfTotalCurrentCapacityOfLowCapacityLanesIsOverThresholdLimit() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA ));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        assertTrue(wrapper.isTotalCurrentCapacityOfStorageRowsForAreaOverThresholdLimit(StorageArea.A_AREA ));
    }

    @Test
    public void attemptingToFindIfTotalCurrentCapacityOfLowCapacityLanesIsOverThresholdLimitWhenNoLowCapacityLanesAvailable() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.C_LOW_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        assertFalse(wrapper.isTotalCurrentCapacityOfStorageRowsForAreaOverThresholdLimit(StorageArea.A_AREA));
    }

    @Test
    public void attemptingToGetLowCapacityLanesContainingOldestProductionRunPartCarriersForUnknownPart() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        Die part = new Die(OM.PART_A, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);
        assertNull(lanes);
    }

    @Test
    public void successfullyGetLowCapacityLanesContainingOldestProductionRunPartCarriers() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);
        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);
        assertNotNull(lanes);
        assertEquals(7, lanes.size());
    }



    @Test
    public void attemptingToGetMediumCapacityLanesContainingOldestProductionRunPartCarriersWhenNoMediumCapacityAvailable() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);
        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.C_HIGH);
        assertNull(lanes);
    }



    @Test
    public void successfullyGetAppropriatePartialLaneForPartType() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);
        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);

        StorageRow StorageRow = wrapper.getAppropriatePartialRowForDie(part.getId(), lanes);
        assertNotNull(StorageRow);
        assertEquals(1, StorageRow.getId().longValue());
    }

    @Test
    public void successfullyGetAppropriateFullLaneForPartType() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);

        StorageRow StorageRow = wrapper.getAppropriateFullRowForDie(part.getId(), lanes);
        assertNotNull(StorageRow);
        assertEquals(2, StorageRow.getId().longValue());
    }

    @Test
    public void attemptingToGetAppropriateFullLaneForPartTypeWhenNoFullLanesAvailable() {
        List<StorageRow> lanes = getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA);
        lanes.remove(2);
        StorageState storageState = new StorageStateImpl(lanes);
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lowCapacityLanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);

        StorageRow StorageRow = wrapper.getAppropriateFullRowForDie(part.getId(), lowCapacityLanes);
        assertNull(StorageRow);
    }

    @Test
    public void successfullyGetAppropriateMixedFrontLaneForPartType() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);

        StorageRow StorageRow = wrapper.getAppropriateMixedFrontRowForDie(part.getId(), lanes);
        assertNotNull(StorageRow);
        assertEquals(5, StorageRow.getId().longValue());
    }

    @Test
    public void successfullyGetAppropriateMixedBlockedLaneForPartType() {
        StorageState storageState = new StorageStateImpl(getLanesForStoreOutTestMe(OM.A_AREA_CAPACITY, 0,StorageArea.A_AREA));
        StorageStateContext context = new StorageStateContextMock(storageState);
        StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(context);

        Die part = new Die(101L, PartProductionVolume.HIGH_VOLUME);
        List<StorageRow> lanes = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(part, StorageArea.A_AREA);

        StorageRow StorageRow = wrapper.getAppropriateMixedBlockedRowForDie(part.getId(), lanes);
        assertNotNull(StorageRow);
        assertEquals(4, StorageRow.getId().longValue());
    }

    public List<StorageRow> getLanesForStoreOutTestMe(int capacity, int index, StorageArea area) {

        List<StorageRow> lanes = new ArrayList<StorageRow>();

        Calendar c1 = Calendar.getInstance();
        Timestamp timestamp = null;

        int MAX_CAPACITY = capacity;
        int productionRunNo = 101;
        int productionRunNo2 = 102;

        StorageRow partialLane1 = new StorageRow(index++, "partial carrier lane1", MAX_CAPACITY,1);
        partialLane1.setStorageArea(area);
        partialLane1.setAvailability(StopAvailability.AVAILABLE);
        partialLane1.setStop(new Stop(1L));
        for (int i = 0; i < 5; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME,partialLane1.getStop(),partialLane1.getStop());
            partialLane1.store(c);
        }
        StorageRow partialLaneWithMinParts = new StorageRow(index++, "partial carrier lane2", MAX_CAPACITY,1);
        partialLaneWithMinParts.setStorageArea(area);
        partialLaneWithMinParts.setAvailability(StopAvailability.AVAILABLE);
        partialLaneWithMinParts.setStop(new Stop(2L));
        for (int i = 0; i < 3; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME, partialLaneWithMinParts.getStop(), partialLaneWithMinParts.getStop());
            partialLaneWithMinParts.store(c);
        }


        StorageRow fullLane = new StorageRow(index++, "full StorageRow", MAX_CAPACITY,1);
        fullLane.setStorageArea(area);
        fullLane.setAvailability(StopAvailability.AVAILABLE);
        fullLane.setStop(new Stop(3L));
        for (int i = MAX_CAPACITY; i > 0; i--) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME, fullLane.getStop(),fullLane.getStop());
            fullLane.store(c);
        }

        StorageRow mixedFrontLane = new StorageRow(index++, "mixed carrier StorageRow", MAX_CAPACITY,1);
        mixedFrontLane.setStorageArea(area);
        mixedFrontLane.setAvailability(StopAvailability.AVAILABLE);
        mixedFrontLane.setStop(new Stop(4L));
        for (int i = 0; i < 5; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME, mixedFrontLane.getStop(), mixedFrontLane.getStop());
            mixedFrontLane.store(c);
        }
        for (int i = 5; i < 9; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(102L, productionRunNo2, timestamp, PartProductionVolume.HIGH_VOLUME, mixedFrontLane.getStop(), mixedFrontLane.getStop());
            mixedFrontLane.store(c);
        }

        StorageRow mixedBlockedLane = new StorageRow(index++, "mixed carrier StorageRow", MAX_CAPACITY,1);
        mixedBlockedLane.setStorageArea(area);
        mixedBlockedLane.setAvailability(StopAvailability.AVAILABLE);
        mixedBlockedLane.setStop(new Stop(5L));
        for (int i = 0; i < 5; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(102L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME, mixedBlockedLane.getStop(), mixedBlockedLane.getStop());
            mixedBlockedLane.store(c);
        }

        for (int i = 5; i < 7; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo2, timestamp, PartProductionVolume.HIGH_VOLUME, mixedBlockedLane.getStop(), mixedBlockedLane.getStop());
            mixedBlockedLane.store(c);
        }

        StorageRow mixedFrontWithMinimumParts = new StorageRow(index++, "mixed carrier StorageRow", MAX_CAPACITY,1);
        mixedFrontWithMinimumParts.setStorageArea(area);
        mixedFrontWithMinimumParts.setAvailability(StopAvailability.AVAILABLE);
        mixedFrontWithMinimumParts.setStop(new Stop(6L));
        for (int i = 0; i < 3; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME, mixedFrontWithMinimumParts.getStop(), mixedFrontWithMinimumParts.getStop());
            mixedFrontWithMinimumParts.store(c);
        }
        for (int i = 3; i < 9; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(102L, productionRunNo2, timestamp, PartProductionVolume.HIGH_VOLUME, mixedFrontWithMinimumParts.getStop(), mixedFrontWithMinimumParts.getStop());
            mixedFrontWithMinimumParts.store(c);
        }

        StorageRow mixedBlockedLaneWithMax = new StorageRow(index++, "mixed carrier StorageRow", MAX_CAPACITY,1);
        mixedBlockedLaneWithMax.setStorageArea(area);
        mixedBlockedLaneWithMax.setAvailability(StopAvailability.AVAILABLE);
        mixedBlockedLaneWithMax.setStop(new Stop(7L));
        for (int i = 0; i < 8; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(102L, productionRunNo, timestamp, PartProductionVolume.HIGH_VOLUME, mixedBlockedLaneWithMax.getStop(), mixedBlockedLaneWithMax.getStop());
            mixedBlockedLaneWithMax.store(c);
        }

        for (int i = 8; i < 10; i++) {
            c1.add(Calendar.DATE, -i);
            timestamp = new Timestamp(c1.getTimeInMillis());
            Carrier c = getCarrier(101L, productionRunNo2, timestamp, PartProductionVolume.HIGH_VOLUME, mixedBlockedLaneWithMax.getStop(), mixedBlockedLaneWithMax.getStop());
            mixedBlockedLaneWithMax.store(c);
        }



        lanes.add(partialLane1);
        lanes.add(partialLaneWithMinParts);
        lanes.add(fullLane);
        lanes.add(mixedFrontLane);
        lanes.add(mixedBlockedLane);
        lanes.add(mixedFrontWithMinimumParts);
        lanes.add(mixedBlockedLane);

        return lanes;
    }

    private Carrier getCarrier(Long dieNumber, Integer prodRunRunNo, Timestamp productionRuntimestamp, PartProductionVolume volume, Stop currentLocation, Stop destination) {
        Carrier carrier = new Carrier(index++, new Die(dieNumber, volume), 1, productionRuntimestamp, prodRunRunNo);
        carrier.setCurrentLocation(currentLocation);
        carrier.setDestination(destination);
        carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);
        return carrier;
    }
}
