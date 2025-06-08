package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.UnitSpecialControl;
import com.honda.galc.entity.pdda.UnitSpecialControlId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitSpecialControlDao extends IDaoService<UnitSpecialControl, UnitSpecialControlId> {
	public List<UnitSpecialControl> findAllSpecialControl(int maintenanceId);
}
