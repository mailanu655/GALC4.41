package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;

/**
 * User: Jeffrey M Lutz
 * Date: Feb 10, 2011
 */
public interface StoreOutManager {
     StorageRow retrieve(Die die);
}
