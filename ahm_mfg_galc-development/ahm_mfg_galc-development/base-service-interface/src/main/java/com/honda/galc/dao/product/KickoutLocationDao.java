package com.honda.galc.dao.product;

import com.honda.galc.entity.product.KickoutLocation;
import com.honda.galc.service.IDaoService;

public interface KickoutLocationDao extends IDaoService<KickoutLocation, Long> {
	String findProcessPointNameForKickout(long kickoutId);
}
