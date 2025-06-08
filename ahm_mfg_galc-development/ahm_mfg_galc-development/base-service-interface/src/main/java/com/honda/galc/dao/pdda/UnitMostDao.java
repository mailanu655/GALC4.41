package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.UnitControlMethod;
import com.honda.galc.entity.pdda.UnitMost;
import com.honda.galc.entity.pdda.UnitMostId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitMostDao extends IDaoService<UnitMost, UnitMostId> {

	public List<UnitMost> findAllWorkingPointDtl(int maintenanceId);

}
