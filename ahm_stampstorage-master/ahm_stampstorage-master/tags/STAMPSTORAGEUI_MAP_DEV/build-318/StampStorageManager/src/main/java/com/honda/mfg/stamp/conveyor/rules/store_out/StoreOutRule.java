package com.honda.mfg.stamp.conveyor.rules.store_out;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;

/**
 * User: Jeffrey M Lutz
 * Date: Feb 10, 2011
 */
public interface StoreOutRule {

    StorageRow processRule(Die part);
}
