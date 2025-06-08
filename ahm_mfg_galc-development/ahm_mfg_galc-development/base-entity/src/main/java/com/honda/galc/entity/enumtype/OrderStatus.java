package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Jan 15, 2013
 */
public enum OrderStatus implements IdEnum<OrderStatus> {

	SCHEDULED(0),
	CURRENT(1),
	ASSIGNED(2),
	SENT(3);

	int _id;

	private OrderStatus(int id) {
		_id = id;
	}

	public int getId() {
		return _id;
	}

	public static OrderStatus getType(int id) {
		return EnumUtil.getType(OrderStatus.class, id);
	}
}
