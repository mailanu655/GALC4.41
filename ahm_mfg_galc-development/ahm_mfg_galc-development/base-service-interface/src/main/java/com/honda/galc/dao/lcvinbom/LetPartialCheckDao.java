package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.entity.lcvinbom.LetPartialCheck;
import com.honda.galc.entity.lcvinbom.LetPartialCheckId;
import com.honda.galc.service.IDaoService;

public interface LetPartialCheckDao extends IDaoService<LetPartialCheck, LetPartialCheckId> {
	List<LetPartialCheck> findAssignedInspectionsByCategoryCode(long categoryCodeId);
	int removeByCategoryCode(long categoryCodeId);
}
