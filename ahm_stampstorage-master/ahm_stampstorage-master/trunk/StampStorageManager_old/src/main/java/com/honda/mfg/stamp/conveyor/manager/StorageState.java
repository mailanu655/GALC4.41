package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.Set;

/**
 * User: Jeffrey M Lutz
 * Date: 2/16/11
 */
public interface StorageState {

    StorageRow queryForRow(Matcher<StorageRow> matcher);

    List<StorageRow> queryForRows(Matcher<StorageRow> matcher);

    Carrier storeInLane(Carrier carrier, StorageRow storageRow);

    Carrier releaseCarrierFromLane(StorageRow storageRow);

    boolean isCarrierPartsOnBackOrder(Carrier carrier);

    List<StorageRow> queryForRows(Matcher<StorageRow> matcher, List<StorageRow> laneImpls);

    void sendCarrierUpdateMessage(Carrier carrier);

    void updateCarrier(Carrier carrier);

    List<StorageRow> getRows();

    void releaseCarrierIfExistsAtHeadOfLane(Carrier carrier);

    void storeInLaneIfDestinationIsALaneAndAlreadyNotExistsInStorageSystem(Carrier carrier);

    boolean carrierExistsInStorageState(Carrier carrier);

    int getCarrierPositionInLane(Long laneStopConveyorId, Integer carrierNumber);

    void addCarriersToLane(List<Carrier> carriers, Stop lanestop);

    boolean hadValidLaneDestination(Carrier carrier);

    boolean isStale();

    void setStale(boolean stale);

    Set<Long> getBackOrderedParts();

    void setBackOrderedParts(Set<Long> backOrderedParts);

    void removeCarrierFromStorageState(Carrier carrier);

    void removeCarrierFromRow(Integer carrierNumber, Long laneStopConveyorId);

    void reorderCarriersInRow(Long laneStopConveyorId);

    void updateLane(StorageRow row) ;

    String toString();
}
