package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.*;
import org.hamcrest.Matcher;

import java.util.List;

/**
 * User: Jeffrey M Lutz
 * Date: 2/16/11
 */
public interface StorageState {

    StorageRow queryForRow(Matcher<StorageRow> matcher);

    List<StorageRow> queryForRows(Matcher<StorageRow> matcher);

    Carrier storeInLane(Carrier carrier, StorageRow storageRow);

    Carrier releaseCarrierFromLane(StorageRow storageRow);

    boolean isCarrierPartsOnBackOrder(Die die);

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

    List<BackOrder> getBackOrder();

    void setBackOrder(List<BackOrder> backOrder);

    void removeCarrierFromStorageState(Carrier carrier);

    void removeCarrierFromRow(Integer carrierNumber, Long laneStopConveyorId);

    void reorderCarriersInRow(Long laneStopConveyorId);

    void updateLane(StorageRow row) ;
    void removeFromBackOrderList(Die die,OrderMgr orderMgr);
    void removeFromBackOrderList(OrderMgr orderMgr);

    String toString();
}
