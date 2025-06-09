package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CommentCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.DieType;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.helper.Helper;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 11/21/11
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NewFulfillmentHelper extends Helper {

    void saveWeldOrderStatus(WeldOrder order, OrderStatus orderStatus);

    void saveCarriers(WeldOrder order, List<Carrier> carriers);

    Integer getMinCycleCountForOrderForFulfillmentStatus(WeldOrder order, CarrierFulfillmentStatus status);

    List<OrderFulfillment> getAllOrderFulfillmentsByOrder(WeldOrder order);

    boolean noOrderMgrWithOrderStatus(OrderStatus orderStatus);

    long getCurrentCapacityOfOrderMgr(OrderMgr orderMgr);

    WeldOrder getActiveOrder(OrderMgr orderMgr);

    WeldOrder getActiveOrderForDelivery(OrderMgr orderMgr);

    boolean isCarrierInA(Integer carrierNumber);

    void saveWeldOrderComments(WeldOrder order, String comments, DieType dieType, CommentCategory category);

    void saveWeldOrderStatusAndComments(WeldOrder order, OrderStatus orderStatus, String comments, DieType dieType);

    void deleteAllUnReleasedOrderFulfillments(WeldOrder order);

    boolean isEmpty(Die die);

    Stop getCarrierDestinationForRecirculation(StorageRow row, Die die);

    void generateInspectionAlarm(Integer carrierNumber);

    void deleteAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(WeldOrder order, CarrierFulfillmentStatus carrierFulfillmentStatus);


    boolean needMoreCarriersWithDie(WeldOrder weldOrder, Die rightDie);

    boolean isAnyOrderRetrieving(OrderMgr weldLine);

    long getCurrentQueueCapacityOfOrderMgr(Stop queueStop);

    List<OrderFulfillment> getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(WeldOrder order, CarrierFulfillmentStatus carrierFulfillmentStatus);

    List<OrderFulfillment> getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(WeldOrder order, CarrierFulfillmentStatus carrierFulfillmentStatus, Die die);

    boolean isSpaceAvailableToDeliver(OrderMgr orderMgr, int deliveryCycleSize);

    void updateOrderFulfillmentStatus(OrderFulfillment orderFulfillment, CarrierFulfillmentStatus carrierFulfillmentStatus, Stop destination);

    boolean noOrderWithDeliveryStatus(OrderStatus deliveringCarriers);

    List<Integer> getNewCarriersToRelease(WeldOrder order, Integer cycle);

    void saveFulfillment(WeldOrder order, Carrier carrier, Integer cycle, CarrierFulfillmentStatus fulfillmentStatus);

    void saveWeldDeliveryStatus(WeldOrder order, OrderStatus autoCompleted);

    void saveWeldDeliveryStatusAndComments(WeldOrder order, OrderStatus deliveringCarriers, String s,DieType dieType);

    List<WeldOrder> getWeldOrdersByOrderStatus(OrderStatus orderStatus);

    boolean isSpaceAvailableInQueue(Stop leftQueueStop);

    boolean canClearBackOrder(Stop queueStop, Die leftDie, String fulfillmentCycleSize);

     boolean isCarrierPartOfActiveOrder(WeldOrder order, CarrierMes carrierMes);
    @Deprecated
    Die getDieForLastCycle(WeldOrder order);

    @Deprecated
    int getCarrierCountForDie(WeldOrder weldOrder, Die die);
    List<WeldOrder> getActiveFulfillmentOrders();

}
