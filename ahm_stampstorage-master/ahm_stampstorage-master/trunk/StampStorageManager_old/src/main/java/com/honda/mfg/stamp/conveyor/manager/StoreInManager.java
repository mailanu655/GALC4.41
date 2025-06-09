package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * Methods and rules for storing in.
 * User: Jeffrey M Lutz
 * Date: Feb 10, 2011
 */
public interface StoreInManager {
	/**
	 * @param carrier
	 * @return Storage row as a result of the rules being processed.
	 */
    StorageRow store(Carrier carrier);
/**
 * @param carrier
 * @param area
 * @return Storage row as a result of the substore rule
 */
    StorageRow subStore(Carrier carrier, StorageArea area);
}
