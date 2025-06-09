package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.helper.AbstractHelperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/21/11
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */

public class NewFulfillmentHelperImpl extends AbstractHelperImpl implements NewFulfillmentHelper {
    private static final Logger LOG = LoggerFactory.getLogger(NewFulfillmentHelperImpl.class);


    @Override
    public synchronized void saveWeldOrderStatus(WeldOrder order, OrderStatus orderStatus) {
        LOG.info("saving WeldOrder Status-" + orderStatus.toString() + " for order-" + order.getId());
        WeldOrder activeOrder = WeldOrder.findWeldOrder(order.getId());
        if (!activeOrder.getOrderStatus().equals(OrderStatus.ManuallyCompleted)) {
            activeOrder.setOrderStatus(orderStatus);
            activeOrder.merge();
        } else {
            LOG.debug("order -" + order.getId() + " manually completed");
        }
    }

    @Override
    public Integer getMinCycleCountForOrderForFulfillmentStatus(WeldOrder order, CarrierFulfillmentStatus status) {
        return OrderFulfillment.getMinCycleCountForOrderForFulfillmentStatus(order, status);
    }

    @Override
    public List<OrderFulfillment> getAllOrderFulfillmentsByOrder(WeldOrder order) {
        return OrderFulfillment.findAllOrderFulfillmentsByOrder(order);
    }

    public List<OrderFulfillment> getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(WeldOrder order, CarrierFulfillmentStatus carrierFulfillmentStatus) {
        List<OrderFulfillment> fulfillments = getAllOrderFulfillmentsByOrder(order);

        List<OrderFulfillment> tempFulfillments = new ArrayList<OrderFulfillment>();

        for (OrderFulfillment fulfillment : fulfillments) {

            if (fulfillment.getCarrierFulfillmentStatus().equals(carrierFulfillmentStatus)) {
                tempFulfillments.add(fulfillment);
            }
        }

        return tempFulfillments;
    }

    public List<OrderFulfillment> getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(WeldOrder order, CarrierFulfillmentStatus carrierFulfillmentStatus, Die die) {
        List<OrderFulfillment> fulfillments = getAllOrderFulfillmentsByOrder(order);

        List<OrderFulfillment> tempFulfillments = new ArrayList<OrderFulfillment>();

        for (OrderFulfillment fulfillment : fulfillments) {

            if (fulfillment.getCarrierFulfillmentStatus().equals(carrierFulfillmentStatus) && fulfillment.getDie().equals(die)) {
                tempFulfillments.add(fulfillment);
            }
        }

        return tempFulfillments;
    }


    public synchronized void updateOrderFulfillmentStatus(OrderFulfillment orderFulfillment, CarrierFulfillmentStatus carrierFulfillmentStatus, Stop destination) {
        OrderFulfillment tempOrderFulfillment = OrderFulfillment.findOrderFulfillment(orderFulfillment.getId());
        if (carrierFulfillmentStatus != null) {
            tempOrderFulfillment.setCarrierFulfillmentStatus(carrierFulfillmentStatus);
        }
        if (destination != null) {
            tempOrderFulfillment.setDestination(destination);
        }
        tempOrderFulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        tempOrderFulfillment.merge();
    }

    @Override
    public boolean noOrderWithDeliveryStatus(OrderStatus deliveringCarriers) {
        List<WeldOrder> weldOrders = new ArrayList<WeldOrder>();
        weldOrders = WeldOrder.findWeldOrdersByDeliveryStatus(deliveringCarriers);

        if (weldOrders.size() > 0) {
            return false;
        }
        return true;
    }

    public boolean isSpaceAvailableToDeliver(OrderMgr orderMgr, int deliveryCycleSize) {
        boolean spaceAvailable;
        long currentCapacity = getCurrentCapacityOfOrderMgr(orderMgr);
        if (currentCapacity < deliveryCycleSize * 2) {
            spaceAvailable = false;
        } else {
            spaceAvailable = true;
        }
        return spaceAvailable;
    }


    /**
     * selects carriers in a given cycle that don't already have their destination set to a queue stop
     *
     * @param order
     * @param cycle: cycle to be processed (usually lowest cycle in SELECTED status)
     * @return list of carrier numbers
     */
    @Override
    public List<Integer> getNewCarriersToRelease(WeldOrder order, Integer cycle) {
        List<Integer> carrierNumbers = new ArrayList<Integer>();
        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(order, cycle);
        Stop queueStop = null;
        if (fulfillments != null) {
            for (OrderFulfillment fulfillment : fulfillments) {
                if (order.isLeftDie(fulfillment.getDie())) {
                    queueStop = order.getLeftQueueStop();
                } else {
                    queueStop = order.getRightQueueStop();
                }
                if (!fulfillment.getDestination().equals(queueStop) && fulfillment.isStatus(CarrierFulfillmentStatus.SELECTED)) {
                    carrierNumbers.add(fulfillment.getId().getCarrierNumber());
                }
            }
        }
        return carrierNumbers;
    }

    /**
     * saves fulfillment record with SELECTED status and new cycle number (max+1)
     *
     * @param order
     * @param carriers : list of carriers
     * @see com.honda.mfg.stamp.conveyor.newfulfillment.NewFulfillmentHelper#saveCarriers(com.honda.mfg.stamp.conveyor.domain.WeldOrder, java.util.List)
     */
    public void saveCarriers(WeldOrder order, List<Carrier> carriers) {
        Integer cycle = OrderFulfillment.getMaxCycleCountForOrder(order) + 1;
        LOG.info("Saving carrier info for cycle - " + cycle + " for Order-" + order.getId());

        for (Carrier carrier : carriers) {
            LOG.debug(" saving carrier - " + carrier.getCarrierNumber());
            saveFulfillment(order, carrier, cycle, CarrierFulfillmentStatus.SELECTED);
        }
    }

    public void saveFulfillment(WeldOrder order, Carrier carrier, Integer cycle, CarrierFulfillmentStatus fulfillmentStatus) {

        OrderFulfillmentPk orderFulfillmentPk = new OrderFulfillmentPk(order, carrier.getCarrierNumber(), cycle);
        OrderFulfillment fulfillment = OrderFulfillment.findOrderFulfillment(orderFulfillmentPk);

        if (fulfillment == null) {
            fulfillment = new OrderFulfillment();
            fulfillment.setId(orderFulfillmentPk);
            fulfillment.setDie(Die.findDie(carrier.getDie().getId()));
            fulfillment.setQuantity(carrier.getQuantity());
            fulfillment.seProductionRunNo(carrier.getProductionRunNo());
            fulfillment.setCurrentLocation(Stop.findStop(carrier.getCurrentLocation().getId()));
            fulfillment.setDestination(Stop.findStop(carrier.getDestination().getId()));
            // fulfillment.setReleaseCycle(cycle);
            fulfillment.setCarrierFulfillmentStatus(fulfillmentStatus);
            fulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));

            fulfillment.persist();
        }
    }

    @Override
    public synchronized void saveWeldDeliveryStatus(WeldOrder order, OrderStatus orderStatus) {
        LOG.info("saving WeldOrder delivery Status-" + orderStatus.toString() + " for order-" + order.getId());
        WeldOrder activeOrder = WeldOrder.findWeldOrder(order.getId());
        if (!activeOrder.getDeliveryStatus().equals(OrderStatus.ManuallyCompleted)) {
            activeOrder.setDeliveryStatus(orderStatus);
            activeOrder.merge();
        } else {
            LOG.debug("order -" + order.getId() + " manually completed");
        }
    }

    @Override
    public synchronized void saveWeldDeliveryStatusAndComments(WeldOrder order, OrderStatus orderStatus, String comments) {
        WeldOrder activeOrder = WeldOrder.findWeldOrder(order.getId());
        if (!activeOrder.getDeliveryStatus().equals(OrderStatus.ManuallyCompleted)) {
            activeOrder.setDeliveryStatus(orderStatus);
            activeOrder.setComments(comments);
            activeOrder.merge();
        } else {
            LOG.debug("order -" + order.getId() + " manually completed");
        }
    }

    @Override
    public List<WeldOrder> getWeldOrdersByOrderStatus(OrderStatus orderStatus) {
        return WeldOrder.findWeldOrdersByOrderStatus(orderStatus);
    }

    @Override
    public boolean isSpaceAvailableInQueue(Stop queueStop) {
        return getCurrentQueueCapacityOfOrderMgr(queueStop) > 0;
    }

    @Override
    public boolean canClearBackOrder(Stop queueStop, Die die, String deliveryCycleSize) {
        Long count = CarrierMes.countCarriersWithDestinationStopAndDie(queueStop, die);
        //List<OrderFulfillment> fulfillments = getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,CarrierFulfillmentStatus.QUEUED, die);
        int deliverySize = getParmValue(deliveryCycleSize);
        if (count >= (deliverySize * 2)) {
            return true;
        }
        return false;
    }

    public boolean noOrderMgrWithOrderStatus(OrderStatus orderStatus) {
        List<WeldOrder> weldOrders = new ArrayList<WeldOrder>();
        weldOrders = WeldOrder.findWeldOrdersByOrderStatus(orderStatus);

        if (weldOrders.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * determine if there are any orders currently Selecting or Retrieving for the given order manager (weld-line)
     *
     * @param weldLine
     * @return
     * @see RetrieveCycleManager#process(WeldOrder, NewFulfillmentHelper, com.honda.mfg.stamp.conveyor.release.ReleaseManager)
     * @see WeldOrder#findWeldOrdersByOrderMgrAndOrderStatus
     */
    @Override
    public boolean isAnyOrderRetrieving(OrderMgr weldLine) {

        List<WeldOrder> retrieving = null, selecting = null;
        retrieving = WeldOrder.findWeldOrdersByOrderMgrAndOrderStatus(weldLine, OrderStatus.RetrievingCarriers);
        selecting = WeldOrder.findWeldOrdersByOrderMgrAndOrderStatus(weldLine, OrderStatus.SelectingCarriers);

        if (retrieving.size() > 0 || selecting.size() > 0) {
            return true;
        }
        return false;
    }

    public long getCurrentCapacityOfOrderMgr(OrderMgr orderMgr) {
        long carrierAtDeliveryStop = CarrierMes.countCarriersWithDestinationStop(orderMgr.getDeliveryStop());
        long currentCapacity = orderMgr.getMaxDeliveryCapacity() - carrierAtDeliveryStop;

        return currentCapacity;
    }

    /**
     * get the current available capacity at left queue stop of order manager (weld line)
     *
     * @return long
     */
    @Override
    public long getCurrentQueueCapacityOfOrderMgr(Stop queueStop) {


        long carrierAtQueueStop = CarrierMes.countCarriersWithDestinationStop(queueStop);
        StorageRow qRow = StorageRow.findStorageRowsByStop(queueStop);
        long currentCapacity = qRow.getCapacity() - carrierAtQueueStop;

        return currentCapacity;
    }

    public WeldOrder getActiveOrder(OrderMgr orderMgr) {
        return WeldOrder.findActiveOrderForOrderMgr(orderMgr);
    }

    public WeldOrder getActiveOrderForDelivery(OrderMgr orderMgr) {
        return WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
    }


    public boolean isCarrierInA(Integer carrierNumber) {
        CarrierMes carrier = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
        Stop currentLocation = Stop.findStop(carrier.getCurrentLocation());
        if (currentLocation != null) {
            StorageRow row = StorageRow.findStorageRowsByStop(currentLocation);
            if (row != null && row.getStorageArea().equals(StorageArea.A_AREA)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void saveWeldOrderComments(WeldOrder order, String comments) {
        WeldOrder activeOrder = WeldOrder.findWeldOrder(order.getId());
        activeOrder.setComments(comments);
        activeOrder.merge();
    }

    @Override
    public synchronized void saveWeldOrderStatusAndComments(WeldOrder order, OrderStatus orderStatus, String comments) {
        WeldOrder activeOrder = WeldOrder.findWeldOrder(order.getId());
        if (!activeOrder.getOrderStatus().equals(OrderStatus.ManuallyCompleted)) {
            activeOrder.setOrderStatus(orderStatus);
            activeOrder.setComments(comments);
            activeOrder.merge();
        } else {
            LOG.debug("order -" + order.getId() + " manually completed");
        }
    }

    @Override
    public void deleteAllUnReleasedOrderFulfillments(WeldOrder order) {

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(order, CarrierFulfillmentStatus.RETRIEVED);

        for (OrderFulfillment fulfillment : fulfillments) {

            Long carrierNumber = Long.valueOf(fulfillment.getId().getCarrierNumber().intValue());
            CarrierRelease carrierRelease = CarrierRelease.findCarrierRelease(carrierNumber);

            if (carrierRelease != null && isCarrierInRow(fulfillment.getId().getCarrierNumber())) {
                carrierRelease.remove();
            }
        }
        deleteAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(order, CarrierFulfillmentStatus.RETRIEVED);
        deleteAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(order, CarrierFulfillmentStatus.SELECTED);
    }

    @Override
    public boolean isEmpty(Die die) {
        return die.equals(getEmptyDie());
    }

    @Override
    public Stop getCarrierDestinationForRecirculation(StorageRow lane, Die die) {
        StopArea area = StopArea.STORE_IN_ROUTE;
        StopType type = StopType.RECIRC_TO_ALL_ROWS;

//        if (isEmpty(die)) {
//            type = StopType.EMPTY_CARRIER_DELIVERY;
//            if (lane.getStorageArea().equals(StorageArea.B_AREA)) {
//                area = StopArea.B_AREA;
//            } else {
//                area = StopArea.EMPTY_AREA;
//            }
//        } else {
//            if (lane.getStorageArea().equals(StorageArea.B_AREA) || lane.getStorageArea().equals(StorageArea.S_AREA)) {
//                area = StopArea.B_AREA;
//            }
//        }
        if (isEmpty(die) && !lane.getStorageArea().equals(StorageArea.B_AREA) && !lane.getStorageArea().equals(StorageArea.S_AREA)) {
            area = StopArea.OLD_WELD_LINE;
            type = StopType.EMPTY_CARRIER_DELIVERY;

        }

        if (lane.getStorageArea().equals(StorageArea.B_AREA) || lane.getStorageArea().equals(StorageArea.S_AREA)) {
            area = StopArea.B_AREA;
        }


        List<Stop> stops = Stop.findAllStopsByTypeAndArea(type, area);

        if (stops != null && stops.size() > 0)

        {
            return stops.get(0);
        }

        return null;
    }

    @Override
    public void generateInspectionAlarm(Integer carrierNumber) {
        CarrierMes carrier = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
        if (carrier != null && carrier.getStatus().intValue() == CarrierStatus.INSPECTION_REQUIRED.type()) {
            generateAlarm(0, AlarmTypes.INSPECTION_REQUIRED_ALARM.type());
        }
    }

    @Override
    public void deleteAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(WeldOrder order, CarrierFulfillmentStatus carrierFulfillmentStatus) {

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);
        if (fulfillments != null && fulfillments.size() > 0) {
            for (OrderFulfillment fulfillment : fulfillments) {
                if (fulfillment.getCarrierFulfillmentStatus().equals(carrierFulfillmentStatus) && isCarrierInRow(fulfillment.getId().getCarrierNumber())) {
                    fulfillment.remove();
                }
            }
        }
    }

    private boolean isCarrierInRow(Integer carrierNumber) {
        boolean inRow = false;
        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
        Stop stop = Stop.findStop(carrierMes.getCurrentLocation());
        if (stop != null && stop.isRowStop()) {
            inRow = true;
        }

        return inRow;
    }


    public Die getDieForLastCycle(WeldOrder order) {
        Die die = null;
        Integer cycle = OrderFulfillment.getMaxCycleCountForOrder(order);

        List<OrderFulfillment> orderFulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(order, cycle);

        if (orderFulfillments != null && orderFulfillments.size() > 0) {
            die = orderFulfillments.get(0).getDie();
        }
        return die;
    }

    /*
    * gets the total number of fulfillments for given order and die,
    * including all cycles for given die
    * @see com.honda.mfg.stamp.conveyor.newfulfillment.NewFulfillmentHelper#getCarrierCountForDie(com.honda.mfg.stamp.conveyor.domain.WeldOrder, com.honda.mfg.stamp.conveyor.domain.Die)
    */
    @Override
    public int getCarrierCountForDie(WeldOrder weldOrder, Die die) {
        int count = 0;
        List<OrderFulfillment> orderFulfillments = getAllOrderFulfillmentsByOrder(weldOrder);

        if (orderFulfillments != null) {

            for (OrderFulfillment fulfillment : orderFulfillments) {
                if (fulfillment.getDie().equals(die)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * counts part quantities in all carrier fulfillments for this order, in any status and compares
     * it with ordered quantity
     *
     * @param weldOrder
     * @param die
     * @return true if requested quantity is more than fulfilled quantity
     * @see com.honda.mfg.stamp.conveyor.newfulfillment.NewFulfillmentHelper#needMoreCarriersWithDie(com.honda.mfg.stamp.conveyor.domain.WeldOrder, com.honda.mfg.stamp.conveyor.domain.Die)
     */
    @Override
    public boolean needMoreCarriersWithDie(WeldOrder weldOrder, Die die) {
        boolean flag = false;
        Stop queueStop = null;
        Integer orderedQty = 0;
        int requestedQty = 0;

        List<OrderFulfillment> orderFulfillments = getAllOrderFulfillmentsByOrder(weldOrder);
        if (orderFulfillments != null) {

            for (OrderFulfillment fulfillment : orderFulfillments) {
                if (fulfillment.getDie().equals(die)) {
                    orderedQty = orderedQty + fulfillment.getQuantity();
                }
            }
        }

        //Also count carriers with same die, left-over from prior order(s) that are still in the queue
        if (die.equals(weldOrder.getModel().getLeftDie())) {
            queueStop = weldOrder.getLeftQueueStop();
            requestedQty = weldOrder.getLeftQuantity();
        } else if (die.equals(weldOrder.getModel().getRightDie())) {
            queueStop = weldOrder.getRightQueueStop();
            requestedQty = weldOrder.getRightQuantity();
        }
        List<CarrierMes> queuedCarriers = CarrierMes.findAllCarriersWithCurrentLocation(queueStop.getId());


        for (CarrierMes carrier : queuedCarriers) {

            if (carrier.getDieNumber().intValue() == die.getId().intValue()){
                //check if this fulfillment is part of any order which is not delivery/complete
                int count = OrderFulfillment.countFulfillmentsByCarrierNotDeliveryCompleted(carrier.getCarrierNumber());
                if (count == 0) {  //=> it does not belong to any orders in progress
                    LOG.info(" carrier - " + carrier.getCarrierNumber() + " does not belong to any active order");
                    orderedQty += carrier.getQuantity();
                }
            }else{
                 LOG.info(" carrier -"+ carrier.getDieNumber()+" in queue does not have requested Die");
            }
        }

        LOG.info("qty of product in the queue - "+ orderedQty);
        if (requestedQty > orderedQty) {
            flag = true;
        }
        return flag;
    }


}