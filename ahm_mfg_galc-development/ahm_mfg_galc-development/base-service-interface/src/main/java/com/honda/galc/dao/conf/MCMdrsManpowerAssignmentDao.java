package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCMdrsManpowerAssignment;
import com.honda.galc.entity.conf.MCMdrsManpowerAssignmentId;
import com.honda.galc.service.IDaoService;

/**
 * @author vfc01778
 * @date December 09, 2014
 */
public interface MCMdrsManpowerAssignmentDao extends IDaoService<MCMdrsManpowerAssignment, MCMdrsManpowerAssignmentId> {

	
	public List<Object[]> getManPowerAssignmentsFromMDRS();
}
