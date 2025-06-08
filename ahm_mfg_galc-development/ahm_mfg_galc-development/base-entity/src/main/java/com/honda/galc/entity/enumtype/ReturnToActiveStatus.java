package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum ReturnToActiveStatus {
NOCHANGE(0),
INTERCHANGABLE(1),
RETURN_TO_ACTIVE_RULE(2),
DEPRECATED(3);
	
private int id;

private ReturnToActiveStatus(int id) {
	this.id = id;
}

public int getId() {
	return id;
}


}
