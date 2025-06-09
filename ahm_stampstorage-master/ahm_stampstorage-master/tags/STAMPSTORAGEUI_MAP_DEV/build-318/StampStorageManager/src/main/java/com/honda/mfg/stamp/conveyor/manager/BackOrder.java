package com.honda.mfg.stamp.conveyor.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 11/17/14
 * Time: 9:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class BackOrder {

     private static final Logger LOG = LoggerFactory.getLogger(BackOrder.class);

        Long orderMgrId;
        Long dieId;

        public BackOrder(Long orderMgrId, Long dieId){
            this.orderMgrId = orderMgrId;
            this.dieId = dieId;
        }

        public String toString(){
            return  "OrderMgr - "+ orderMgrId +" Die -"+dieId;
        }

        public Long getOrderMgrId(){
            return this.orderMgrId;
        }

        public Long getDieId(){
            return this.dieId;
        }

       public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof BackOrder) {
            BackOrder lhs = this;
            BackOrder rhs = (BackOrder) obj;
            if (lhs.getOrderMgrId() == null || lhs.getDieId() == null) {
                return false;
            }
            return lhs.getOrderMgrId().equals(rhs.getOrderMgrId()) && lhs.getDieId().equals(rhs.getDieId());
        }
        return false;
    }




}
