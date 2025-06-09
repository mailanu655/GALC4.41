package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/16/11
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseCycleManager {
    private static final Logger LOG = LoggerFactory.getLogger(ReleaseCycleManager.class);


    public void process(WeldOrder order, Integer cycle, NewFulfillmentHelper helper, ReleaseManager releaseManager) {
        LOG.info("Get carriers to release to queues for cycle - " + cycle);
        List<Integer> carrierNumbers = helper.getNewCarriersToRelease(order, cycle);
        if (carrierNumbers != null) {
            for (Integer carrier : carrierNumbers) {
                LOG.debug("releasing Carrier " + carrier);
                releaseCarrier(carrier, helper, order, releaseManager, cycle);
                helper.pause(1);
            }
        } else {
            LOG.info("waiting for carriers to be Released for Order-" + order.getId());
            helper.saveWeldOrderComments(order, "Waiting for carriers to Release");
        }
    }

    private void releaseCarrier(Integer carrierNumber, NewFulfillmentHelper helper, WeldOrder order, ReleaseManager releaseManager, Integer cycle) {

        try {
            if (helper.isCarrierInA(carrierNumber) && !helper.noOrderWithDeliveryStatus(OrderStatus.DeliveringCarriers)) {
                LOG.info("waiting for other order to finish Delivering");
                helper.saveWeldOrderComments(order, "waiting for other order to finish Delivering");
            } else {
                LOG.info("issuing carrier update for carrier-" + carrierNumber + " to move to queue");
                updateOrderFulfillmentAndReleaseCarrier(order, carrierNumber, cycle, releaseManager);
                generateRequiredAlarms(helper,carrierNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug(e.getMessage());
        }
    }


    private void generateRequiredAlarms(NewFulfillmentHelper helper, Integer carrierNumber) {
        helper.generateInspectionAlarm(carrierNumber);
    }

    /**
     * sets the fulfillment status to RETRIEVED and given destination
     *
     * @param order
     * @param carrierNumber
     * @param cycle
     */
    private void  updateOrderFulfillmentAndReleaseCarrier(WeldOrder order, Integer carrierNumber, Integer cycle,ReleaseManager releaseManager) {
        OrderFulfillmentPk pk = new OrderFulfillmentPk(order, carrierNumber, cycle);
        OrderFulfillment orderFulfillment = OrderFulfillment.findOrderFulfillment(pk);
        if (orderFulfillment != null) {
            Stop destination = null;
            if (order.isLeftDie(orderFulfillment.getDie())) {
                destination = order.getLeftQueueStop();
            } else {
                destination = order.getRightQueueStop();
            }

            orderFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
            orderFulfillment.setDestination(destination);
            orderFulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            orderFulfillment.merge();

            releaseManager.releaseCarrier(carrierNumber,destination, StorageConfig.OHCV_APP_FULFILLMENT, true);
        }
    }

}
