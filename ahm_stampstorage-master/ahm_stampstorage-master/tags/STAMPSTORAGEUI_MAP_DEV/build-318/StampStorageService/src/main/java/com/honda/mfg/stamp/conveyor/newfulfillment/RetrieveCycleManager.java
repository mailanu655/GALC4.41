package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmTypes;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CommentCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.DieType;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.exceptions.InvalidDieException;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.BackOrder;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/17/11
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class RetrieveCycleManager {
    private static final Logger LOG = LoggerFactory.getLogger(RetrieveCycleManager.class);
    private String carrierCycleSize;
    private String recirculationCarrierReleaseCount;
    private Storage storage;

    public RetrieveCycleManager(Storage storage, String cycleSize, String recirculationCarrierReleaseCount) {
        this.storage = storage;
        this.carrierCycleSize = cycleSize;
        this.recirculationCarrierReleaseCount = recirculationCarrierReleaseCount;
    }

    public void process(WeldOrder order, NewFulfillmentHelper helper, ReleaseManager releaseManager) {
        long currentLeftCapacity = 0L, currentRightCapacity = 0L;
        Die leftDie = null, rightDie = null;

        try {
            if (helper.isAnyOrderRetrieving(order.getOrderMgr()) || !order.isStatus(OrderStatus.InProcess)) {
                return;
            }

            leftDie = order.getLeftDie();
            rightDie = order.getRightDie();

            boolean bNeedLefts = helper.needMoreCarriersWithDie(order, leftDie);
            boolean bNeedRights = helper.needMoreCarriersWithDie(order, rightDie);

            currentLeftCapacity = helper.getCurrentQueueCapacityOfOrderMgr(order.getOrderMgr().getLeftQueueStop());
            currentRightCapacity = helper.getCurrentQueueCapacityOfOrderMgr(order.getOrderMgr().getRightQueueStop());

            LOG.info("Selecting Carriers for Order-" + order.getId());

            //carrier count is how many carriers to send in new cycle, initialize to 4
            int carrierCount = getFulfillmentCycleSize(helper);

            LOG.info("starting new cycle.....");

            if (bNeedLefts && currentLeftCapacity >= getFulfillmentCycleSize(helper)) {
                selectAndSaveNewCarriers(order, leftDie, helper, releaseManager, carrierCount);
            } else {
                helper.saveWeldOrderComments(order, "No Space available in Left queue", DieType.LEFT, CommentCategory.FULFILLMENT);
            }

            if (bNeedRights && currentRightCapacity >= getFulfillmentCycleSize(helper)) {
                selectAndSaveNewCarriers(order, rightDie, helper, releaseManager, carrierCount);
            } else {
                helper.saveWeldOrderComments(order, "No Space available in Right queue", DieType.RIGHT, CommentCategory.FULFILLMENT);
            }

            helper.saveWeldOrderStatus(order, OrderStatus.RetrievingCarriers);

        } catch (InvalidDieException e) {
            helper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
                    e.getMessage() + " cannot select carriers", DieType.BOTH);
            LOG.info(e.getMessage());

        } catch (Exception e) {
            helper.saveWeldOrderStatus(order, OrderStatus.InProcess);
            LOG.info(e.getMessage());
            e.printStackTrace();
        }

    }

    private int getFulfillmentCycleSize(NewFulfillmentHelper helper) {
        return helper.getParmValue(carrierCycleSize);
    }

    public int getRecircCarrierLimit(NewFulfillmentHelper helper) {
        return helper.getParmValue(recirculationCarrierReleaseCount);

    }

    private void placePartsOnBackOrder(OrderMgr orderMgr, Die die, NewFulfillmentHelper helper) {
        BackOrder newBackOrder = new BackOrder(orderMgr.getId(), die.getId());
        List<BackOrder> backOrder = storage.getStorageState().getBackOrder();

        if (backOrder == null) {
            backOrder = new ArrayList<BackOrder>();

            backOrder.add(newBackOrder);
        } else {

            if (!backOrder.contains(newBackOrder))
                backOrder.add(newBackOrder);
            else
                LOG.debug(" BackOrder Already Exists cannot add Multiple");

        }

        storage.getStorageState().setBackOrder(backOrder);

        helper.generateAlarm(0, AlarmTypes.BACK_ORDER_ALARM.type());
    }


    /**
     * get carriers for new cycle for given order and die
     *
     * @param die
     * @param orderedPartQty
     * @param queuedPartQty: includes carriers in RELEASED status
     * @param carrierCount
     * @param releaseManager
     * @param helper
     * @return : list of carriers
     */
    private List<Carrier> getCarriersForNewCycle(WeldOrder order, Die die, int orderedPartQty, int queuedPartQty, int carrierCount, ReleaseManager releaseManager, NewFulfillmentHelper helper) {
        ArrayList<Carrier> carriers = new ArrayList<Carrier>();

        StringBuilder sb = new StringBuilder();
        sb.append("carrier count for die ").append(die).append(" is-")
                .append(", carrier count-")
                .append(carrierCount).append(", orderedPartQty-")
                .append(orderedPartQty).append(", queuedPartQty-")
                .append(queuedPartQty);
        LOG.info(sb.toString());

        int totalPartQty = queuedPartQty;
        int thisCount = 0;
        try {
            while (orderedPartQty > totalPartQty && thisCount < carrierCount) {
                LOG.debug(" total PartQty-" + totalPartQty);
                StorageRow lane = storage.retrieve(die);
                boolean canRelease = true;
                Carrier carrier = null;
                if (lane != null) {

                    clearIfPartsOnBackOrder(order.getOrderMgr(), die, helper);

                    if (lane.getLaneConditionByDie(die).equals(LaneCondition.MIXED_BLOCK)) {
                        canRelease = recirculateCarriers(lane, die, releaseManager, helper);
                    }

                    if (canRelease) {
                        if (lane.getCarrierAtRowOut().getDie().equals(die) && !lane.getCarrierAtRowOut().getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {

                            carrier = storage.getStorageState().releaseCarrierFromLane(lane);
                            if (carrier.getCarrierStatus().equals(CarrierStatus.ON_HOLD) || !carrier.getDie().equals(die)) {
                                LOG.debug("carrier-" + carrier.getCarrierNumber() + " with Die - "+carrier.getDie().toString() +" carrier status - " + carrier.getCarrierStatus().toString());
                                break;
                            }

                            totalPartQty += carrier.getQuantity();
                            carriers.add(carrier);
                            thisCount++;
                        } else {
                            LOG.info(" Carrier At Lane Out for Row :" + lane.getRowName() + " does not have the required Die: " + die);
                        }
                    }

                }
            }

            helper.pause(2);
        } catch (NoApplicableRuleFoundException e) {
            LOG.info(e.getMessage());
            storage.reloadStorageState();
            placePartsOnBackOrder(order.getOrderMgr(), die, helper);
            DieType dieType = (order.getLeftDie().equals(die)) ? DieType.LEFT : DieType.RIGHT;
            helper.saveWeldOrderStatusAndComments(order, OrderStatus.RetrievingCarriers,
                    die + "Backorder: No Shippable Carriers.", dieType);
        } catch (CannotUnBlockCarriersException e) {
            LOG.info(e.getMessage());
            /**GSA 20140529 - Unable to Unblock alarm **/
            helper.generateAlarm(0, AlarmTypes.UNABLE_TO_UNBLOCK.type());
            DieType dieType = (order.getLeftDie().equals(die)) ? DieType.LEFT : DieType.RIGHT;
            helper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
                    "Backorder: Cannot Unblock Shippable Carriers.", dieType);
            storage.reloadStorageState();
        }
        return carriers;
    }

    private void clearIfPartsOnBackOrder(OrderMgr orderMgr, Die die, NewFulfillmentHelper helper) {
        try {
            if (storage.getStorageState().isCarrierPartsOnBackOrder(die)) {
                storage.getStorageState().removeFromBackOrderList(die, orderMgr);
                helper.resetAlarm(AlarmTypes.BACK_ORDER_ALARM.type());
            }
        } catch (Exception e) {
            LOG.info("Could Not Clear die " + die.getId() + " from BackOrder - " + e.getMessage());
        }
    }

    public boolean recirculateCarriers(StorageRow lane, Die die, ReleaseManager releaseManager, NewFulfillmentHelper helper) {
        boolean canRecirculate = false;
        int countOfCarriersBlocking = lane.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(die.getId());

        if (getRecircCarrierLimit(helper) < countOfCarriersBlocking) {
            /**GSA 20140529 - Unable to Unblock alarm **/
            helper.generateAlarm(0, AlarmTypes.UNABLE_TO_UNBLOCK.type());
            throw new CannotUnBlockCarriersException(" cannot unblock- " + lane.getRowName());

        } else {
            canRecirculate = true;
        }

        Carrier c = null;
        int count = 0;
        while (count < lane.getCapacity()) {
            if (!lane.isEmpty()) {
                c = lane.getCarrierAtRowOut();
                if (c != null && c.getDie().equals(die)) {
                    LOG.debug("carrier - " + c.getCarrierNumber() + " has requested die - STOP RECIRCULATING");
                    break;
                }

                Carrier blockingCarrier = c;
                Stop destination = helper.getCarrierDestinationForRecirculation(lane, blockingCarrier.getDie());
                blockingCarrier.setDestination(destination);

                LOG.info("moving blocking carrier with carrier number " + blockingCarrier.getCarrierNumber() + " from lane " + lane.getRowName() + " to " + destination);
                WeldOrder order = OrderFulfillment.findOrderByCarrier(blockingCarrier);

                if (order == null || (order != null && ((order.getOrderStatus().equals(OrderStatus.ManuallyCompleted) || order.getOrderStatus().equals(OrderStatus.AutoCompleted))))) {
                    releaseManager.releaseCarrier(blockingCarrier.getCarrierNumber(), blockingCarrier.getDestination(), StorageConfig.OHCV_APP_RECIRC, true);
                    storage.getStorageState().releaseCarrierFromLane(lane);
                }

            }

            count++;
        }
        return canRecirculate;
    }

    private class CannotUnBlockCarriersException extends RuntimeException {

        public CannotUnBlockCarriersException(String message) {
            super(message);
        }

    }

    void selectAndSaveNewCarriers(WeldOrder weldOrder, Die die,
                                  NewFulfillmentHelper helper, ReleaseManager releaseManager, Integer carrierCount) {

        LOG.info("Selecting Carriers for Die " + die);
        DieType dieType = (weldOrder.getLeftDie().equals(die)) ? DieType.LEFT : DieType.RIGHT;
        helper.saveWeldOrderComments(weldOrder, "Selecting Carriers for Die " + die, dieType, CommentCategory.FULFILLMENT);

        Integer queuedQuantity = 0;
        Integer orderedQuantity = 0;

        if (die.equals(weldOrder.getModel().getLeftDie())) {
            queuedQuantity = weldOrder.getLeftQueuedQty();
            orderedQuantity = weldOrder.getLeftQuantity();
        } else if (die.equals(weldOrder.getModel().getRightDie())) {
            queuedQuantity = weldOrder.getRightQueuedQty();
            orderedQuantity = weldOrder.getRightQuantity();
        }
        if (queuedQuantity == null) {
            queuedQuantity = 0;
        }
//        if (orderedQuantity == null) {
//            orderedQuantity = 0;
//        }

        //dont count released, retrieve only after prev. cycle is queued
        List<Carrier> carriers = getCarriersForNewCycle(weldOrder, die, orderedQuantity, queuedQuantity, carrierCount, releaseManager, helper);
        helper.saveCarriers(weldOrder, carriers);
    }


}
