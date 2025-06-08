package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.common.validation.ValidationInfoMessage;
import com.honda.galc.common.validation.ValidationType;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.AbnormalReasonDao;
import com.honda.galc.entity.product.AbnormalReason;
import com.honda.galc.entity.product.AbnormalReasonId;
import com.honda.galc.service.Parameters;

public class AbnormalReasonDaoImpl extends BaseDaoImpl<AbnormalReason, AbnormalReasonId> implements AbnormalReasonDao {

	@Override
	public List<AbnormalReason> findAllByProductIdAndAbnormalType(String productId, String abnormalType) {
		Parameters parameters = Parameters.with("productId", productId);
		parameters.put("abnormalType", abnormalType);
		return findAll(parameters );
	}

	@Override
	public void removeByKey(AbnormalReasonId id) {
		throw new UnsupportedOperationException("AbnormalReason does not have a unique Id.");
	}

	@Override
	public AbnormalReason findByKey(AbnormalReasonId id) {
		throw new UnsupportedOperationException("AbnormalReason does not have a unique Id.");
	}

	@Override
	public List<ValidationInfoMessage> validate(AbnormalReason entity, AbnormalReasonId key, List<ValidationType> typeList) {
		throw new UnsupportedOperationException("AbnormalReason does not have a unique Id.");
	}
}
