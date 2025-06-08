package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.Process;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface ProcessDao extends IDaoService<Process, Integer> {
	public List<Process> getAllBy(long revId, String asmProcNo);
}
