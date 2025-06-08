package com.honda.galc.dao.mdrs;

import java.util.List;

import com.honda.galc.entity.mdrs.ManpowerAssignment;
import com.honda.galc.entity.mdrs.ManpowerAssignmentId;
import com.honda.galc.service.IDaoService;

public interface ManpowerAssignmentDao 
	extends IDaoService<ManpowerAssignment, ManpowerAssignmentId> {
	public List<String> findDepts();
	public List<Object[]> getUnmappedQuarters();

}
