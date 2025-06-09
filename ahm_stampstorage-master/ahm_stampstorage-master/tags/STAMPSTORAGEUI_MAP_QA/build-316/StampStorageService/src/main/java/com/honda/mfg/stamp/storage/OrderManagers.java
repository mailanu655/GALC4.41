package com.honda.mfg.stamp.storage;

import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 7/12/11
 */
public final class OrderManagers {
    private static final Logger LOG = LoggerFactory.getLogger(OrderManagers.class);

    private OrderManagers(){

    }

    public static OrderMgr getWeldLine1() {
        OrderMgr mgr = OrderMgr.findOrderMgr(1L);
        LOG.info("Retrieved weld line 1 order manager: " + mgr);
        return mgr;
    }

    public static OrderMgr getWeldLine2() {
        LOG.info("Retrieving weld line 2 order manager");
        return OrderMgr.findOrderMgr(2L);
    }
}
