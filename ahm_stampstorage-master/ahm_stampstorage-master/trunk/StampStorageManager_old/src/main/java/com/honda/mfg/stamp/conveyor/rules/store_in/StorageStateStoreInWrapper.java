package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.google.common.collect.Ordering;
import com.honda.mfg.stamp.conveyor.comparators.RowComparators;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.RowMatchers;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.core.AllOf.allOf;

/**
 * User: vcc30690
 * Date: 3/14/11
 */
public class StorageStateStoreInWrapper {
      private static final Logger LOG = LoggerFactory.getLogger(StorageStateStoreInWrapper.class);
    private StorageStateContext storageStateContext;


    public StorageStateStoreInWrapper(StorageStateContext storageStateContext) {
        this.storageStateContext = storageStateContext;
    }


    private StorageState getStorageState() {
        return storageStateContext.getStorageState();
    }

    public StorageRow getAppropriatePartialRowForDie(Long die, List<StorageRow> rows) {
        List<StorageRow> unsortedPartialRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.isLaneWithOnlySingleDieNumberOf(die)
                )
                , rows);
        List<StorageRow> partialRowsWithMinimumPartsOfDie = unsortedPartialRows == null ? unsortedPartialRows : Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(die))
                .sortedCopy(unsortedPartialRows);

        if (partialRowsWithMinimumPartsOfDie == null || partialRowsWithMinimumPartsOfDie.size() == 0) {
            return null;
        }
        return partialRowsWithMinimumPartsOfDie.get(0);
    }

//    public StorageRow getAppropriatePartialRow(List<StorageRow> rows) {
//        List<StorageRow> unsortedPartialRows = getStorageState().queryForRows(
//                RowMatchers.isCurrentCapacityPartial()
//                , rows);
//        List<StorageRow> partialRows = unsortedPartialRows == null ? unsortedPartialRows : Ordering.from(RowComparators.getComparatorByCurrentCarrierCount())
//                .sortedCopy(unsortedPartialRows);
//
//        if (partialRows == null || partialRows.size() == 0) {
//            return null;
//        }
//        return partialRows.get(0);
//    }

    public StorageRow getAppropriateVacantRow(List<StorageRow> rows) {
        List<StorageRow> vacantRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityEmpty()),
                rows);

        return vacantRows == null || vacantRows.size() == 0 ? null : vacantRows.get(0);
    }

    public StorageRow getAppropriatePartialRowWithEmptyCarriers(List<StorageRow> rows) {
        List<StorageRow> unsortedEmptyOnlyPartialRows = getStorageState().queryForRows(
                allOf(
                        RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.hasEmptyCarriers()
                ), rows);

        List<StorageRow> sortedRows = unsortedEmptyOnlyPartialRows == null ? unsortedEmptyOnlyPartialRows : Ordering.from(RowComparators.getComparatorByCurrentCarrierCount())
                .sortedCopy(unsortedEmptyOnlyPartialRows);

        return sortedRows == null || sortedRows.size() == 0 ? null : sortedRows.get(0);
    }

    public StorageRow getAppropriateMixedBackRowWithDie(Long die, List<StorageRow> rows) {
        List<StorageRow> unsortedMixedBackRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.hasCarrierAtTheLaneInWithDieNumber(die)
                )
                , rows);
        List<StorageRow> mixedRowsWithMinimumPartsOfDieAtRowIn = unsortedMixedBackRows == null ? unsortedMixedBackRows :
                Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(die))
                        .sortedCopy(unsortedMixedBackRows);

        if (mixedRowsWithMinimumPartsOfDieAtRowIn == null || mixedRowsWithMinimumPartsOfDieAtRowIn.size() == 0) {
            return null;
        }
        return mixedRowsWithMinimumPartsOfDieAtRowIn.get(0);
    }

    public StorageRow getAppropriateMixedRows(List<StorageRow> rows) {
        List<StorageRow> unsortedMixedRows = getStorageState().queryForRows(
                allOf(RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.isCurrentCapacityPartial()
                )
                , rows);
        List<StorageRow> mixedRows = unsortedMixedRows == null ? unsortedMixedRows :
                Ordering.from(RowComparators.getComparatorByCurrentCarrierCount())
                        .sortedCopy(unsortedMixedRows);

        if (mixedRows == null || mixedRows.size() == 0) {
            return null;
        }
        return mixedRows.get(mixedRows.size() - 1);
    }

     public List<StorageRow> getStorageRowsByArea(StorageArea area) {
        List<StorageRow> rows = getStorageState().queryForRows(
                allOf(RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable())
        );
        return rows;
    }

    public StorageRow getAppropriatePartialRowForDieInStorageArea(Long die, StorageArea area) {
        if(area == null){return null;}
    	List<StorageRow> unsortedPartialRows = getStorageState().queryForRows(
                allOf(RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable(),
                        RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.isLaneWithOnlySingleDieNumberOf(die)
                ));
        List<StorageRow> partialRowsWithMinimumPartsOfDie = unsortedPartialRows == null ? unsortedPartialRows : Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(die))
                .sortedCopy(unsortedPartialRows);

        if (partialRowsWithMinimumPartsOfDie == null || partialRowsWithMinimumPartsOfDie.size() == 0) {
            return null;
        }

        return partialRowsWithMinimumPartsOfDie.get(0);
    }

     public StorageRow getAppropriateVacantRowInStorageArea(StorageArea area) {
    	if(area == null){return null;}
        List<StorageRow> vacantRows = getStorageState().queryForRows(
                allOf(RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable(),
                        RowMatchers.isCurrentCapacityEmpty())
                );
         List<StorageRow> sortedById = vacantRows == null || vacantRows.size() == 0 ? null :Ordering.from(RowComparators.getComparatorByRowStopId())
                .sortedCopy(vacantRows);
        //return vacantRows == null || vacantRows.size() == 0 ? null : vacantRows.get(0);

        return sortedById == null || sortedById.size() == 0 ? null :sortedById.get(sortedById.size() - 1);
    }

    public StorageRow getAppropriateMixedBackRowWithDieInStorageArea(Long die, StorageArea area) {
   	 	if(area == null){return null;}
    	List<StorageRow> unsortedMixedBackRows = getStorageState().queryForRows(
                allOf(RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable(),
                        RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.hasCarrierAtTheLaneInWithDieNumber(die)
                ));
        List<StorageRow> mixedRowsWithMinimumPartsOfDieAtRowIn = unsortedMixedBackRows == null ? unsortedMixedBackRows :
                Ordering.from(RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(die))
                        .sortedCopy(unsortedMixedBackRows);

        if (mixedRowsWithMinimumPartsOfDieAtRowIn == null || mixedRowsWithMinimumPartsOfDieAtRowIn.size() == 0) {
            return null;
        }
        return mixedRowsWithMinimumPartsOfDieAtRowIn.get(0);
    }

     public StorageRow getAppropriateMixedRowInStorageArea(StorageArea area) {
    	 if(area == null){return null;}
    	 List<StorageRow> unsortedMixedRows = getStorageState().queryForRows(
                allOf(RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable(),
                        RowMatchers.isCurrentCapacityMixed(),
                        RowMatchers.isCurrentCapacityPartial()
                ));
        List<StorageRow> mixedRows = unsortedMixedRows == null ? unsortedMixedRows :
                Ordering.from(RowComparators.getComparatorByCurrentCarrierCount())
                        .sortedCopy(unsortedMixedRows);

        if (mixedRows == null || mixedRows.size() == 0) {
            return null;
        }
        return mixedRows.get(mixedRows.size() - 1);
    }

     public StorageRow getAppropriatePartialRowWithEmptyCarriersInStorageArea(StorageArea area) {
    	 if(area == null){return null;}
    	 List<StorageRow> unsortedEmptyOnlyPartialRows = getStorageState().queryForRows(
                allOf( RowMatchers.inStorageArea(area),
                        RowMatchers.isLaneAvailable(),
                        RowMatchers.isCurrentCapacityPartial(),
                        RowMatchers.hasEmptyCarriers()
                ));

        List<StorageRow> sortedRows = unsortedEmptyOnlyPartialRows == null ? unsortedEmptyOnlyPartialRows : Ordering.from(RowComparators.getComparatorByCurrentCarrierCount())
                .sortedCopy(unsortedEmptyOnlyPartialRows);

        return sortedRows == null || sortedRows.size() == 0 ? null : sortedRows.get(0);
    }
}
