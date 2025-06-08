package com.honda.galc.dao.conf;

import java.sql.Timestamp;

import com.honda.galc.entity.conf.MCProcessAssignment;
import com.honda.galc.entity.conf.MCProcessAssignmentId;
import com.honda.galc.service.IDaoService;

/**
 * @author vfc01778
 * @date December 09, 2014
 */
public interface MCProcessAssignmentDao extends IDaoService<MCProcessAssignment, MCProcessAssignmentId> {
	
	public boolean validateUserAssignment(String userid, String processPoint);

}
