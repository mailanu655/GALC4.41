package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.AbnormalReason;
import com.honda.galc.entity.product.AbnormalReasonId;
import com.honda.galc.service.IDaoService;

public interface AbnormalReasonDao extends IDaoService<AbnormalReason, AbnormalReasonId> {
	public List<AbnormalReason> findAllByProductIdAndAbnormalType(String productId, String abnormalType);
}
