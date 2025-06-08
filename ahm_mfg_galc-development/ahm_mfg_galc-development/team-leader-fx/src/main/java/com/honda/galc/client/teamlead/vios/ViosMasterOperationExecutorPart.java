package com.honda.galc.client.teamlead.vios;


import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosMasterOperationExecutorPart implements ViosMasterExecutor<MCViosMasterOperationPart> {


	@Override
	public String doValidation(MCViosMasterOperationPart entity) {
		return ViosMasterValidator.masterPartValidate(entity);
	}

	@Override
	public void uploadEntity(MCViosMasterOperationPart entity, String viosPlatform, String userId) throws Exception {
		entity.getId().setViosPlatformId(viosPlatform);
		entity.setUserId(userId);
		String propertyValuePartMask = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class).getPartMask();
		if(StringUtils.isEmpty(entity.getPartMask())) {
			entity.setPartMask(propertyValuePartMask);
		}
		ServiceFactory.getService(ViosMaintenanceService.class).uploadViosMasterPart(entity);
	}

}
