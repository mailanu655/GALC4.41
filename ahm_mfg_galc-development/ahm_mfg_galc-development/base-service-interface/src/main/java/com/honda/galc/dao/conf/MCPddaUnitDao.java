package com.honda.galc.dao.conf;

import java.util.List;
import com.honda.galc.entity.conf.MCPddaUnit;
import com.honda.galc.entity.conf.MCPddaUnitId;
import com.honda.galc.service.IDaoService;

public interface MCPddaUnitDao extends IDaoService<MCPddaUnit, MCPddaUnitId> {

	List<MCPddaUnit> findBy(int pddaPlatformId, long revId);
	List<MCPddaUnit> findBy(int pddaPlatformId, long revId, String operationName);
	public List<MCPddaUnit> findActivePddaUnits(int pddaPlatformId, long revId);
	public List<MCPddaUnit> findAllByPDDAPlatformIdSpecCodeMask(int pddaPlatformId, String specCodeMask);
}
