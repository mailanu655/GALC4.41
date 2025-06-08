package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseAuditDaoImpl;
import com.honda.galc.dao.product.PartByProductSpecCodeAuditDao;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;

public class PartByProductSpecCodeAuditDaoImpl extends BaseAuditDaoImpl<PartByProductSpecCode, PartByProductSpecCodeId> implements PartByProductSpecCodeAuditDao {

	@Override
	protected PartByProductSpecCodeId getId(PartByProductSpecCode entity) {
		return entity.getId();
	}

}
