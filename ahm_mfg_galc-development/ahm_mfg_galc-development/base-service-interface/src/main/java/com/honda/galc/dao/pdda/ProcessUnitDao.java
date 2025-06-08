package com.honda.galc.dao.pdda;

import com.honda.galc.entity.pdda.ProcessUnit;
import com.honda.galc.entity.pdda.ProcessUnitId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface ProcessUnitDao extends IDaoService<ProcessUnit, ProcessUnitId> {
	
	public ProcessUnit findBy(int apvdProcMaintId, String unitNo);

}
