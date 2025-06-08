package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseAuditDaoImpl;
import com.honda.galc.dao.product.MbpnProductAuditDao;
import com.honda.galc.entity.product.MbpnProduct;

public class MbpnProductAuditDaoImpl extends BaseAuditDaoImpl<MbpnProduct, String> implements MbpnProductAuditDao {

	@Override
	protected String getId(MbpnProduct entity) {
		return entity.getId();
	}

	
}
