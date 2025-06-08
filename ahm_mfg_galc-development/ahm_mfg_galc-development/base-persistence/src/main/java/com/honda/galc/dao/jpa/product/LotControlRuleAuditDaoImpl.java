package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseAuditDaoImpl;
import com.honda.galc.dao.product.LotControlRuleAuditDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;

public class LotControlRuleAuditDaoImpl extends BaseAuditDaoImpl<LotControlRule, LotControlRuleId> implements LotControlRuleAuditDao {

	@Override
	protected LotControlRuleId getId(LotControlRule entity) {
		return entity.getId();
	}

}
