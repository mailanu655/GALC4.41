package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;

@Component
@Configurable
public class WeldOrderDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<WeldOrder> data;

	public WeldOrder getNewTransientWeldOrder(int index) {
		WeldOrder obj = new WeldOrder();
		setOrderMgr(obj, index);
		setOrderSequence(obj, index);
		setOrderStatus(obj, index);
		setLeftQuantity(obj, index);
		setRightQuantity(obj, index);
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setComments(obj, index);
		setLeftDeliveryComments(obj, index);
		setRightDeliveryComments(obj, index);
		setLeftFulfillmentComments(obj, index);
		setRightFulfillmentComments(obj, index);
		setDeliveryStatus(obj);
		return obj;
	}

	public void setLeftQuantity(WeldOrder obj, int index) {
		obj.setLeftQuantity(1);
	}

	public void setRightQuantity(WeldOrder obj, int index) {
		obj.setRightQuantity(1);
	}

	public void setOrderMgr(WeldOrder obj, int index) {
		OrderMgr orderMgr = null;
		obj.setOrderMgr(orderMgr);
	}

	public void setOrderSequence(WeldOrder obj, int index) {
		Integer orderSequence = new Integer(index);
		obj.setOrderSequence(orderSequence);
	}

	public void setOrderStatus(WeldOrder obj, int index) {
		OrderStatus orderStatus = OrderStatus.InProcess;
		obj.setOrderStatus(orderStatus);
	}

	public void setDeliveryStatus(WeldOrder obj) {
		OrderStatus deliveryStatus = OrderStatus.Initialized;
		obj.setDeliveryStatus(deliveryStatus);
	}

	public void setCreatedDate(WeldOrder obj, int index) {
		Timestamp createdDate = new Timestamp(System.currentTimeMillis());
		obj.setCreatedDate(createdDate);
	}

	public void setCreatedBy(WeldOrder obj, int index) {
		String user = "user";
		obj.setCreatedBy(user);
	}

	public void setComments(WeldOrder obj, int index) {
		String comments = "None";
		obj.setComments(comments);
	}

	public void setLeftDeliveryComments(WeldOrder obj, int index) {
		String comments = "None";
		obj.setLeftDeliveryComments(comments);
	}

	public void setRightDeliveryComments(WeldOrder obj, int index) {
		String comments = "None";
		obj.setRightDeliveryComments(comments);
	}

	public void setLeftFulfillmentComments(WeldOrder obj, int index) {
		String comments = "None";
		obj.setLeftFulfillmentComments(comments);
	}

	public void setRightFulfillmentComments(WeldOrder obj, int index) {
		String comments = "None";
		obj.setRightFulfillmentComments(comments);
	}

	public WeldOrder getSpecificWeldOrder(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		WeldOrder obj = data.get(index);
		return WeldOrder.findWeldOrder(obj.getId());
	}

	public WeldOrder getRandomWeldOrder() {
		init();
		WeldOrder obj = data.get(rnd.nextInt(data.size()));
		return WeldOrder.findWeldOrder(obj.getId());
	}

	public boolean modifyWeldOrder(WeldOrder obj) {
		return false;
	}

	public void init() {
		data = WeldOrder.findWeldOrderEntries(0, 10);
		if (data == null)
			throw new IllegalStateException("Find entries implementation for 'WeldOrder' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<WeldOrder>();
		for (int i = 0; i < 10; i++) {
			WeldOrder obj = getNewTransientWeldOrder(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
