package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * User: Jeffrey M Lutz Date: Feb 10, 2011
 */
public interface EmptyManager {
	StorageRow retrieveEmptyCarrier();

	StorageRow storeEmptyCarrier(Carrier carrier);

	StorageRow retrieveEmptyCarrier(StorageArea area);

	StorageRow retrieveEmptyCarrierForOldWeldLineEmptyStorage();

	StorageRow retrieveEmptyCarrierForBAreaEmptyStorage();
}
