package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.OrderMgr;

/**
 * User: Jeffrey M Lutz Date: 7/11/11
 */
public interface Fulfillment {

	void run();

	OrderMgr getOrderManager();
}
