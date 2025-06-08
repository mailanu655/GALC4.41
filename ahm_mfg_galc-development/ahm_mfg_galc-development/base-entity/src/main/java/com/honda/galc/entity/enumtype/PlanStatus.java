package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Jan 15, 2013
 */
public enum PlanStatus implements IdEnum<PlanStatus> {

	SCHEDULED(0),
	ASSIGNED(1),
	LOADED(2),
	UNLOADED(3);

	int _id;

	private PlanStatus(int id) {
		_id = id;
	}

	public int getId() {
		return _id;
	}

	public static PlanStatus getType(int id) {
		return EnumUtil.getType(PlanStatus.class, id);
	}
}
