package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class OrderMgrDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<OrderMgr> data;

	public OrderMgr getNewTransientOrderMgr(int index) {
		OrderMgr obj = new OrderMgr();
		setLineName(obj, index);
		setMaxDeliveryCapacity(obj, index);
		return obj;
	}

	public void setMaxDeliveryCapacity(OrderMgr obj, int index) {
		obj.setMaxDeliveryCapacity(index * 10);
	}

	public void setLineName(OrderMgr obj, int index) {
		String lineName = "lineName_" + index;
		obj.setLineName(lineName);
	}

	public OrderMgr getSpecificOrderMgr(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		OrderMgr obj = data.get(index);
		return OrderMgr.findOrderMgr(obj.getId());
	}

	public OrderMgr getRandomOrderMgr() {
		init();
		OrderMgr obj = data.get(rnd.nextInt(data.size()));
		return OrderMgr.findOrderMgr(obj.getId());
	}

	public boolean modifyOrderMgr(OrderMgr obj) {
		return false;
	}

	public void init() {
		data = OrderMgr.findOrderMgrEntries(0, 10);
		if (data == null)
			throw new IllegalStateException("Find entries implementation for 'OrderMgr' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<OrderMgr>();
		for (int i = 0; i < 10; i++) {
			OrderMgr obj = getNewTransientOrderMgr(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
