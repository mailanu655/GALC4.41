package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.KickoutLocationDao;
import com.honda.galc.entity.product.KickoutLocation;
import com.honda.galc.service.Parameters;

public class KickoutLocationDaoImpl extends BaseDaoImpl<KickoutLocation, Long> implements KickoutLocationDao{
	private static final String FIND_PROCESS_POINT_NAME_FOR_KICKOUT = "select p.processPointName from ProcessPoint p, KickoutLocation l where l.processPointId = p.processPointId and l.kickoutId = :kickoutId";

	public String findProcessPointNameForKickout(long kickoutId) {
		Parameters params = Parameters.with("kickoutId", kickoutId);
		return findFirstByQuery(FIND_PROCESS_POINT_NAME_FOR_KICKOUT, String.class, params);
	}
}
