package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseAuditDaoImpl;
import com.honda.galc.dao.product.MbpnAuditDao;
import com.honda.galc.entity.product.Mbpn;

public class MbpnAuditDaoImpl extends BaseAuditDaoImpl<Mbpn, String> implements MbpnAuditDao {

	@Override
	protected String getId(Mbpn entity) {
		return entity.getId();
	}


}
