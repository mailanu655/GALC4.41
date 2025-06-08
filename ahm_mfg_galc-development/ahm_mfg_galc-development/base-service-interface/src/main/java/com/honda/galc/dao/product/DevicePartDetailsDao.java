package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.DevicePartDetails;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.IDaoService;

public interface DevicePartDetailsDao extends IDaoService<DevicePartDetails, LotControlRule> {
	public List<DevicePartDetails> findAllPartDetailsByLotControlRule(LotControlRule rule);
}
