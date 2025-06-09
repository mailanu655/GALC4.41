package com.honda.mfg.stamp.conveyor.manager;

import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.helper.Helper;

/**
 * User: Jeffrey M Lutz Date: 6/20/11
 */
public interface StorageStateContextHelper extends Helper {

	List<CarrierMes> findAllCarriersInStorage();

	boolean spaceAvailable(Stop stop);
}
