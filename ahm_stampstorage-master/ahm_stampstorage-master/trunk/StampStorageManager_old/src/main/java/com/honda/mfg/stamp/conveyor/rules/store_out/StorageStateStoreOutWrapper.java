package com.honda.mfg.stamp.conveyor.rules.store_out;

import com.google.common.collect.Ordering;
import com.honda.mfg.stamp.conveyor.comparators.RowComparators;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.RowMatchers;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.AllOf.allOf;

/**
 * User: vcc30690
 * Date: 3/14/11
 */
public class StorageStateStoreOutWrapper {

    private StorageStateContext storageStateContext;

    private static final Logger LOG = LoggerFactory.getLogger(StorageStateStoreOutWrapper.class);

    public StorageStateStoreOutWrapper(StorageStateContext storageStateContext) {
        this.storageStateContext = storageStateContext;
    }
    private StorageState getStorageState() {
        return storageStateContext.getStorageState();
    }

//    private int getTotalCurrentCapacity(List<StorageRow> rows) {
//        int totalCurrentCapacity = 0;
//        for (StorageRow laneImpl : rows) {
//            totalCurrentCapacity = totalCurrentCapacity + laneImpl.getCurrentCarrierCount();
//        }
//        return totalCurrentCapacity;
//    }

    public boolean isTotalCurrentCapacityOfStorageRowsForAreaOverThresholdLimit(StorageArea area) {
//        List<StorageRow> rows = getStorageRowsByArea(area);
//
//        if (rows != null && rows.size() > 0) {
//            int thresholdLimit = rulesConfig.getLowCapacityLanesOverfillThresholdPercentage();
//            double totalMax = rows.size() * rows.get(0).getCapacity();
//            double totalCurrentCapacity = getTotalCurrentCapacity(rows);
//            double fraction = totalCurrentCapacity / totalMax;
//            return !((fraction * 100) < thresholdLimit);
//        }
        return false;
    }

    private Integer getOldestProductionRunNo(Long die) {

        Integer oldestProductionRunNo = null;
        Carrier carrier;
        Timestamp productionRunTimestamp;
        productionRunTimestamp = new Timestamp(new Date().getTime());
        List<StorageRow> rows = getStorageState().getRows();
        if (rows != null) {
            for (StorageRow storageRow : rows) {
                // if (!(StorageRow.getDieNumbersForAllCarriers().size() == 1 && StorageRow.getDieNumbersForAllCarriers().contains(null))) {
                carrier = storageRow.getOldestProductionRunCarrierForDieNumber(die);
                if (carrier != null && carrier.getStampingProductionRunTimestamp().before(productionRunTimestamp)) {
                    productionRunTimestamp = new Timestamp(carrier.getStampingProductionRunTimestamp().getTime());
                    oldestProductionRunNo = carrier.getProductionRunNo();
                }
            }
        }
        LOG.info("Oldest ProductionRunNo - " + oldestProductionRunNo);
        return oldestProductionRunNo;
    }

    private List<StorageRow> getRowsWithOldestProductionRunPartCarriers(Long die, List<StorageRow> rows) {
        List<StorageRow> rowsWithOldestProductionRunPartCarriers = getStorageState().queryForRows(
               allOf( RowMatchers.hasCarriersWithDieNumber(die),
                RowMatchers.hasCarrierWithProdRunNoAndCarrierStatusNormal(getOldestProductionRunNo(die)))
                , rows);
        return rowsWithOldestProductionRunPartCarriers;
    }



    public List<StorageRow> getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(Die part, StorageArea area) {
        List<StorageRow> rows = getStorageRowsByArea(area);
        if (part.getPartProductionVolume().equals(PartProductionVolume.EMPTY)) {
            return rows;
        } else {
            List<StorageRow> rowsWithOldestProductionRunPartCarriers =
                    getRowsWithOldestProductionRunPartCarriers(part.getId(), rows);
            return rowsWithOldestProductionRunPartCarriers;
        }
    }

    public StorageRow getAppropriatePartialRowForDie(Long dieNumber, List<StorageRow> rows) {
        List<StorageRow> unsortedPartialRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.isLaneWithOnlySingleDieNumberOf(dieNumber)

                )
                , rows);
        LOG.debug("querying partial Lanes ");
        List<StorageRow> partialLanesWithMinimumPartsOfDieNumber = unsortedPartialRows == null ? unsortedPartialRows : Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(dieNumber))
                .sortedCopy(unsortedPartialRows);

        return partialLanesWithMinimumPartsOfDieNumber == null || partialLanesWithMinimumPartsOfDieNumber.size() == 0 ? null : partialLanesWithMinimumPartsOfDieNumber.get(0);
    }

    public StorageRow getAppropriateMixedFrontRowForDie(Long dieNumber, List<StorageRow> rows) {
        List<StorageRow> unsortedMixedFrontRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.hasCarrierAtTheLaneOutWithDieNumber(dieNumber)
                )
                , rows);
        LOG.debug("querying Mixed Front Lanes ");
        List<StorageRow> mixedRowsWithMinimumPartsOfDieAtLaneOut = unsortedMixedFrontRows == null ? unsortedMixedFrontRows :
                Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(dieNumber))
                        .sortedCopy(unsortedMixedFrontRows);

        return mixedRowsWithMinimumPartsOfDieAtLaneOut == null || mixedRowsWithMinimumPartsOfDieAtLaneOut.size() == 0 ? null : mixedRowsWithMinimumPartsOfDieAtLaneOut.get(0);
    }

    public StorageRow getAppropriateFullRowForDie(Long dieNumber, List<StorageRow> rows) {
        List<StorageRow> fullRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityFull(),
                        RowMatchers.isLaneWithOnlySingleDieNumberOf(dieNumber)),
                rows);
        LOG.debug("querying full Lanes ");
        return fullRows == null || fullRows.size() == 0 ? null : fullRows.get(0);
    }

    public StorageRow getAppropriateMixedBlockedRowForDie(Long dieNumber, List<StorageRow> rows) {
        List<StorageRow> unsortedMixedBlockedRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.hasBlockedCarrierWithDieNumberAtTheLaneOut(dieNumber),
                        RowMatchers.hasCarrierWithDieAndCarrierStatusShippable(dieNumber)
                )
                , rows);
        LOG.info("querying Mixed blocked Lanes ");
        List<StorageRow> mixedRowsHavingDieWithMinimumBlockingCarriersAtLaneOut = unsortedMixedBlockedRows == null ? unsortedMixedBlockedRows :
                Ordering.from(RowComparators.getComparatorByDieNumberBlockingCarriersCountAtLaneOut(dieNumber))
                        .sortedCopy(unsortedMixedBlockedRows);

        return mixedRowsHavingDieWithMinimumBlockingCarriersAtLaneOut == null || mixedRowsHavingDieWithMinimumBlockingCarriersAtLaneOut.size() == 0 ? null : mixedRowsHavingDieWithMinimumBlockingCarriersAtLaneOut.get(0);
    }

//    public boolean dieNumberExistsInStorageArea(long dieNumber, StorageArea area) {
//
//        List<StorageRow> rows = getStorageRowsByArea(area);
//
//        if (rows != null) {
//            List<StorageRow> rowsWithDie = getStorageState().queryForRows(
//                    RowMatchers.hasCarriersWithDieNumber(dieNumber), rows
//            );
//
//            if (rowsWithDie != null && rowsWithDie.size() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }

    public List<StorageRow> getStorageRowsByArea(StorageArea area) {

        List<StorageRow> rows = getStorageState().queryForRows(
                allOf(RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable())
        );

        return rows;
    }
}
