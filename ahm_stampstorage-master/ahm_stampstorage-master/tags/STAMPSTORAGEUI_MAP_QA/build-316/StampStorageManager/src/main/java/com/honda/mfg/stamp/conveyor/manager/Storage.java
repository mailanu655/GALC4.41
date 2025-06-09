package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * Uses manager classes to determine rules for all storage operations.
 * User: Jeffrey M Lutz
 * Date: 2/14/11
 */
public interface Storage extends StoreOutManager {
    void store(Carrier carrier);

    void sendCarrierUpdateMessage(Carrier carrier);

    StorageState getStorageState();

    void recalculateCarrierDestination(Carrier carrier);

    void reloadStorageState();

    StorageRow retrieveEmptyCarrier();

    StorageRow retrieveEmptyCarrier(StorageArea area);

    StorageRow retrieveEmptyCarrierForOldWeldLine();

    StorageRow retrieveEmptyCarrierForBAreaEmptyStorage();

    void resetStorageStateAndBackOrder();
}
