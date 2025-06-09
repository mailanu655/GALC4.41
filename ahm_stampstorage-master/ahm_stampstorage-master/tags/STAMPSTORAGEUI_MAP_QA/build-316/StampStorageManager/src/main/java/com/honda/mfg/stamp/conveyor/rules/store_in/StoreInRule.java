package com.honda.mfg.stamp.conveyor.rules.store_in;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;

/**
 * User: Jeffrey M Lutz
 * Date: Feb 10, 2011
 */
public interface StoreInRule {
    StorageRow processRule(Carrier carrier);
}
