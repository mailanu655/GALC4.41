package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCPddaChange;
import com.honda.galc.entity.conf.MCPddaChangeId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCPddaChangeDao
	extends IDaoService<MCPddaChange, MCPddaChangeId>{
	
	public List<Long> getUnapprovedOldRevisions(long revisionId);
}
