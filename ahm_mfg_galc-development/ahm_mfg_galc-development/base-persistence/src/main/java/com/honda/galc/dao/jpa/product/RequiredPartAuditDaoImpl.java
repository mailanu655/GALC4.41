package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseAuditDaoImpl;
import com.honda.galc.dao.product.RequiredPartAuditDao;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.entity.product.RequiredPartId;

public class RequiredPartAuditDaoImpl extends BaseAuditDaoImpl<RequiredPart, RequiredPartId> implements RequiredPartAuditDao {

	@Override
	protected RequiredPartId getId(RequiredPart entity) {
		return entity.getId();
	}

}
