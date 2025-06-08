package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseAuditDaoImpl;
import com.honda.galc.dao.product.BuildAttributeAuditDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;

public class BuildAttributeAuditDaoImpl extends BaseAuditDaoImpl<BuildAttribute, BuildAttributeId> implements BuildAttributeAuditDao {

	@Override
	protected BuildAttributeId getId(BuildAttribute entity) {
		
		return entity.getId();
	}


}
