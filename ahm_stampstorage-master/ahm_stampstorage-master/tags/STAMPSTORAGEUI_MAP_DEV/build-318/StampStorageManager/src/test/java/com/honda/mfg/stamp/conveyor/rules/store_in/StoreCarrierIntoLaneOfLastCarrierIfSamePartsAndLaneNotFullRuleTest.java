package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.OM;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 3/6/11
 */
public class StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRuleTest {

    @Test
    public void successfullyStoreCarrierWithSamePartsAsLastCarrierIntoLastLaneThatIsNotFull() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
        ignoredLaneImpl.setStop(new Stop(1L));
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();
        expectedLaneImpl.setStop(new Stop(2L));


        expectedLaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow
        StorageStateContext storageStateContext = OM.storageState(ignoredLaneImpl, expectedLaneImpl);
        storageStateContext.addDie(OM.PART_A,expectedLaneImpl);
        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                        storageStateContext,
                        null);
        Carrier carrier = OM.carrier_MediumVolume_PartA();
        StorageRow actualLaneImpl = storeInRule.processRule(carrier);
        assertEquals(expectedLaneImpl, actualLaneImpl);
    }
     @Test(expected = NoApplicableRuleFoundException.class)
    public void throwsExceptionWhenLastUsedLaneIsSamePartDifferentProductionRunNumber() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
        ignoredLaneImpl.setStop(new Stop(1L));
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();
        expectedLaneImpl.setStop(new Stop(2L));

        Carrier c= OM.carrier_MediumVolume_PartA();
         c.setProductionRunNo(999);
        expectedLaneImpl.store(c);  // last in StorageRow
        StorageStateContext storageStateContext = OM.storageState(ignoredLaneImpl, expectedLaneImpl);
        storageStateContext.addDie(OM.PART_A,expectedLaneImpl);
        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                        storageStateContext,
                        null);
        Carrier carrier = OM.carrier_MediumVolume_PartA();
        StorageRow actualLaneImpl = storeInRule.processRule(carrier);
        assertEquals(expectedLaneImpl, actualLaneImpl);
    }



    @Test(expected = NoApplicableRuleFoundException.class)
    public void throwsExceptionWhenLastUsedLaneIsDifferentPart() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();

        expectedLaneImpl.store(OM.carrier_MediumVolume_PartB());  // last in StorageRow

        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                        OM.storageState(ignoredLaneImpl, expectedLaneImpl),
                        null);
        Carrier carrier = OM.carrier_MediumVolume_PartA();
        storeInRule.processRule(carrier);
    }

    @Test(expected = NoApplicableRuleFoundException.class)
    public void throwsExceptionWhenThereIsNoLastUsedLane() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();

        expectedLaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow

        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                        OM.storageState(ignoredLaneImpl, expectedLaneImpl),
                        null);
        Carrier carrier = OM.carrier_MediumVolume_PartA();
        storeInRule.processRule(carrier);
    }

    @Test
    public void addCarrierAndCheckCarrierIsAddedUntilFull() {
        int MAX_CAP = 12;
        StorageRow StorageRow;

        StorageRow = new StorageRow(1, "MyFirstLane", MAX_CAP,1);

        int currentCapacity;
        for (int i = 0; i < MAX_CAP; i++) {
            Carrier c = OM.carrier(OM.PART_A, PartProductionVolume.HIGH_VOLUME);
            currentCapacity = StorageRow.getCurrentCarrierCount();
            assertEquals(i, currentCapacity);
            c.setCarrierNumber(i);
            StorageRow.store(c);
            currentCapacity = StorageRow.getCurrentCarrierCount();
            assertEquals(i + 1, currentCapacity);
            int carrierNumber = StorageRow.getCarrierAtLaneIn().getCarrierNumber();
            assertEquals(i,carrierNumber);
        }
    }

    private void pause(int secs) {
        long delta = System.currentTimeMillis();
        while (System.currentTimeMillis() - delta < (secs * 1000)) {
        }
    }

    @Test(expected = NoApplicableRuleFoundException.class)
    public void throwsExceptionWhenLastUsedLaneIsFull() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
         ignoredLaneImpl.setStop(new Stop(1L));
        StorageRow expectedLaneImpl = OM.lane_HighCapacity_Full(PartProductionVolume.MEDIUM_VOLUME,OM.PART_A);
        expectedLaneImpl.setStop(new Stop(2L));

        StorageStateContext storageStateContext = OM.storageState(ignoredLaneImpl, expectedLaneImpl);
        storageStateContext.addDie(OM.PART_A,expectedLaneImpl);

        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                       storageStateContext,
                        null);
        Carrier carrier2 = OM.carrier_MediumVolume_PartA();
        carrier2.setCarrierNumber(101);
        storeInRule.processRule(carrier2);
    }

     @Test(expected = NoApplicableRuleFoundException.class)
    public void throwsExceptionWhenLastUsedLaneIsEmpty() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
         ignoredLaneImpl.setStop(new Stop(1L));
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();
        expectedLaneImpl.setStop(new Stop(2L));

        StorageStateContext storageStateContext = OM.storageState(ignoredLaneImpl, expectedLaneImpl);
        storageStateContext.addDie(OM.PART_A,expectedLaneImpl);

        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                       storageStateContext,
                        null);
        Carrier carrier2 = OM.carrier_MediumVolume_PartA();
        carrier2.setCarrierNumber(101);
        storeInRule.processRule(carrier2);
    }

     @Test(expected = NoApplicableRuleFoundException.class)
    public void throwsExceptionWhenLastUsedLaneIsBlocked() {

        StorageRow ignoredLaneImpl = OM.lane_C_LOW_Empty();
        StorageRow expectedLaneImpl = OM.lane_C_LOW_Empty();

        expectedLaneImpl.store(OM.carrier_MediumVolume_PartA());  // last in StorageRow
        expectedLaneImpl.setAvailability(StopAvailability.BLOCKED);
        StoreInRule storeInRule =
                new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(
                        OM.storageState(ignoredLaneImpl, expectedLaneImpl),
                        null);
        Carrier carrier = OM.carrier_MediumVolume_PartA();
        storeInRule.processRule(carrier);
    }
}
