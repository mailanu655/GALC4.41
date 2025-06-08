package com.honda.galc.dao.jpa.pdda;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.ProcessUnitDao;
import com.honda.galc.entity.pdda.ProcessUnit;
import com.honda.galc.entity.pdda.ProcessUnitId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class ProcessUnitDaoImpl extends BaseDaoImpl<ProcessUnit, ProcessUnitId>
		implements ProcessUnitDao {

	public ProcessUnit findBy(int apvdProcMaintId, String unitNo) {
		Parameters params = Parameters.with("id.maintenanceId", apvdProcMaintId)
				.put("id.unitNo", unitNo);
		return findFirst(params, new String[]{"createTimestamp"}, false);
	}

}
