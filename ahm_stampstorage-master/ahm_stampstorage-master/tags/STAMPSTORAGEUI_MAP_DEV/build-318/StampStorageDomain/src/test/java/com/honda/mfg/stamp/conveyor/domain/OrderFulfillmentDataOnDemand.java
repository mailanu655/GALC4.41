package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class OrderFulfillmentDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<OrderFulfillment> data;

	private WeldOrderDataOnDemand wdod;

	public OrderFulfillment getNewTransientOrderFulfillment(int index) {
        com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = new com.honda.mfg.stamp.conveyor.domain.OrderFulfillment();
        setEmbeddedId(obj, index);
        setQuantity(obj, index);
        setDie(obj, index);
        setCurrentLocation(obj, index);
        setDestination(obj, index);
        setProductionRunNumber(obj, index);
        return obj;
    }

	private void setEmbeddedId(OrderFulfillment obj, int index) {
        com.honda.mfg.stamp.conveyor.domain.WeldOrder weldOrder = new com.honda.mfg.stamp.conveyor.domain.WeldOrder();
        weldOrder.setId(new Long(index));
        weldOrder.setOrderMgr(null);
        weldOrder.setOrderSequence(index);
        weldOrder.setOrderStatus(null);
        weldOrder.setDeliveryStatus(OrderStatus.Initialized);
        weldOrder.setLeftQuantity(index);
        weldOrder.setRightQuantity(index);
        weldOrder.setCreatedBy("user");
        weldOrder.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        weldOrder.setComments("None");
        weldOrder.setLeftDeliveryComments("None");
        weldOrder.setRightDeliveryComments("None");
        weldOrder.setLeftFulfillmentComments("None");
        weldOrder.setRightFulfillmentComments("None");
        weldOrder.persist();
        java.lang.Integer carrierNumber = new Integer(index);
         java.lang.Integer releaseCycle = new Integer(index);
        com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk embeddedIdClass = new com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk(weldOrder, carrierNumber,releaseCycle);
        obj.setId(embeddedIdClass);
    }

	public void setQuantity(OrderFulfillment obj, int index) {
        java.lang.Integer quantity = new Integer(index);
        obj.setQuantity(quantity);
    }

	public void setDie(OrderFulfillment obj, int index) {
        Die die = new Die();
        java.lang.Long dieNumber = new Long(index);
        die.setId(dieNumber);
        //die.setDieNumber(index);
        die.setPartProductionVolume(PartProductionVolume.LOW_VOLUME);
        die.setDescription("Die_" + dieNumber);
        die.persist();
        obj.setDie(die);
    }

	public void setCurrentLocation(OrderFulfillment obj, int index) {
        Stop stop = new Stop();
        stop.setId(new Long(index));
        stop.setStopType(StopType.NO_ACTION);
        stop.setName("Stop_" + index);
        stop.setStopArea(StopArea.findByType(index));

        stop.persist();

        obj.setCurrentLocation(stop);
    }

	public void setDestination(OrderFulfillment obj, int index) {
        Stop stop = new Stop();
        stop.setId(new Long(index + 10));
        stop.setStopType(StopType.NO_ACTION);
        stop.setStopArea(StopArea.findByType(index));
        stop.setName("Stop_" + index);
        stop.persist();
        obj.setDestination(stop);
    }

	public void setProductionRunNumber(OrderFulfillment obj, int index) {
        obj.seProductionRunNo(index);
    }

	public OrderFulfillment getSpecificOrderFulfillment(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        OrderFulfillment obj = data.get(index);
        return OrderFulfillment.findOrderFulfillment(obj.getId());
    }

	public OrderFulfillment getRandomOrderFulfillment() {
        init();
        OrderFulfillment obj = data.get(rnd.nextInt(data.size()));
        return OrderFulfillment.findOrderFulfillment(obj.getId());
    }

	public boolean modifyOrderFulfillment(OrderFulfillment obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.OrderFulfillment.findOrderFulfillmentEntries(0, 10);
        if (data == null)
            throw new IllegalStateException("Find entries implementation for 'OrderFulfillment' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }

        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.OrderFulfillment>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.OrderFulfillment obj = getNewTransientOrderFulfillment(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
