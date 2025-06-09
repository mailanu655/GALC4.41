package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmTypes;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.exceptions.InvalidDieException;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Die die = null;
        try {
            if (helper.isAnyOrderRetrieving(order.getOrderMgr()) || !order.isStatus(OrderStatus.InProcess)) {
                return;
            }

            leftDie = order.getLeftDie();
            rightDie = order.getRightDie();

            boolean bNeedLefts = helper.needMoreCarriersWithDie(order, leftDie);
            boolean bNeedRights = helper.needMoreCarriersWithDie(order, rightDie);

//            if (bNeedLefts && bNeedRights) {
//                Set<Long> backOrderedParts = storage.getStorageState().getBackOrderedParts();
//                if (backOrderedParts != null && backOrderedParts.contains(leftDie.getId())) {
//                    helper.saveWeldOrderComments(order, leftDie + " Parts on back Order, No shippable carriers available to release");
//                    return;
//                }
//                if (backOrderedParts != null && backOrderedParts.contains(rightDie.getId())) {
//                    helper.saveWeldOrderComments(order, rightDie + " Parts on back Order, No shippable carriers available to release");
//                    return;
//                }
//            }

            currentLeftCapacity = helper.getCurrentQueueCapacityOfOrderMgr(order.getOrderMgr().getLeftQueueStop());
            currentRightCapacity = helper.getCurrentQueueCapacityOfOrderMgr(order.getOrderMgr().getRightQueueStop());

            //send only if there is space available for configurable cycle size
//            if (currentLeftCapacity >= getFulfillmentCycleSize(helper) && currentRightCapacity >= getFulfillmentCycleSize(helper)) {
//                helper.saveWeldOrderComments(order, "Space available in queues");
//            } else {
//                helper.saveWeldOrderComments(order, "No Space available in queues");
//                return;
//            }


            LOG.info("Selecting Carriers for Order-" + order.getId());

            //carrier count is how many carriers to send in new cycle, initialize to 4
            int carrierCount = getFulfillmentCycleSize(helper);

            LOG.info("starting new cycle.....");
            try {
                if (bNeedLefts && currentLeftCapacity >= getFulfillmentCycleSize(helper)) {
                    die = leftDie;
                    selectAndSaveNewCarriers(order, leftDie, helper, releaseManager, carrierCount);
                }else{
                     helper.saveWeldOrderComments(order, "No Space available in Left queue");
                }
            } catch (NoApplicableRuleFoundException e) {
                LOG.info(e.getMessage());
                storage.reloadStorageState();
                placePartsOnBackOrder(die.getId(), helper);
                helper.saveWeldOrderStatusAndComments(order, OrderStatus.RetrievingCarriers,
                        die + " Parts on back Order, No shippable carriers available to release");
            } catch (CannotUnBlockCarriersException e) {
                LOG.info(e.getMessage());
                helper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
                        " Cannot unblock");
                storage.reloadStorageState();
            }
            try {
                if (bNeedRights && currentRightCapacity >= getFulfillmentCycleSize(helper)) {
                    die = rightDie;
                    selectAndSaveNewCarriers(order, rightDie, helper, releaseManager, carrierCount);
                }else{
                     helper.saveWeldOrderComments(order, "No Space available in Right queue");
                }
            } catch (NoApplicableRuleFoundException e) {
                LOG.info(e.getMessage());
                storage.reloadStorageState();
                placePartsOnBackOrder(die.getId(), helper);
                helper.saveWeldOrderStatusAndComments(order, OrderStatus.RetrievingCarriers,
                        die + " Parts on back Order, No shippable carriers available to release");
            } catch (CannotUnBlockCarriersException e) {
                LOG.info(e.getMessage());
                helper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
                        " Cannot unblock");
                 storage.reloadStorageState();
            }

            helper.saveWeldOrderStatus(order, OrderStatus.RetrievingCarriers);

        } catch (InvalidDieException e) {
            helper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
                    e.getMessage() + " cannot select carriers");
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

    private void placePartsOnBackOrder(Long id, NewFulfillmentHelper helper) {
        Set<Long> backOrderParts = storage.getStorageState().getBackOrderedParts();

        if (backOrderParts == null) {
            backOrderParts = new HashSet<Long>();
        }
        backOrderParts.add(id);
        storage.getStorageState().setBackOrderedParts(backOrderParts);

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
    private List<Carrier> getCarriersForNewCycle(Die die, int orderedPartQty, int queuedPartQty, int carrierCount, ReleaseManager releaseManager, NewFulfillmentHelper helper) {
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

        while (orderedPartQty > totalPartQty && thisCount < carrierCount) {
            LOG.debug(" total PartQty-" + totalPartQty);
            StorageRow lane = storage.retrieve(die);
            boolean canRelease = true;
            Carrier carrier = null;
            if (lane != null) {

                clearIfPartsOnBackOrder(die);

                if (lane.getLaneConditionByDie(die).equals(LaneCondition.MIXED_BLOCK)) {
                    canRelease = recirculateCarriers(lane, die, releaseManager, helper);
                }

                if (canRelease) {
                    if (lane.getCarrierAtRowOut().getDie().equals(die)&& !lane.getCarrierAtRowOut().getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
                        carrier = storage.getStorageState().releaseCarrierFromLane(lane);
                        totalPartQty += carrier.getQuantity();
                        carriers.add(carrier);
                        thisCount++;
                    } else {
                        LOG.info(" Carrier At Lane Out for Row :" + lane.getRowName() + " does not have the required Die: " + die);
                    }
                }

            }

            helper.pause(2);
        }
        return carriers;
    }

    private void clearIfPartsOnBackOrder(Die die) {
        try {
            Set<Long> partOnBackOrder = storage.getStorageState().getBackOrderedParts();
            if (partOnBackOrder != null && !partOnBackOrder.isEmpty() && partOnBackOrder.contains(die.getId())) {
                storage.getStorageState().getBackOrderedParts().remove(die.getId());
            }
        } catch (Exception e) {
            LOG.info("Could Not Clear die " + die.getId() + " from BackOrder - " + e.getMessage());
        }
    }

    public boolean recirculateCarriers(StorageRow lane, Die die, ReleaseManager releaseManager, NewFulfillmentHelper helper) {
        boolean canRecirculate = false;
        int countOfCarriersBlocking = lane.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(die.getId());

        if (getRecircCarrierLimit(helper) < countOfCarriersBlocking) {
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
        helper.saveWeldOrderComments(weldOrder, "Selecting Carriers for Die " + die);

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
        List<Carrier> carriers = getCarriersForNewCycle(die, orderedQuantity, queuedQuantity, carrierCount, releaseManager, helper);
        helper.saveCarriers(weldOrder, carriers);
    }


}
