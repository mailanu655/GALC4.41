package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibilityMappingId;
import com.honda.galc.service.IDaoService;

public interface QiResponsibilityMappingDao extends IDaoService<QiResponsibilityMapping, QiResponsibilityMappingId> {
	public boolean insertResponsibleMapping(int defaultRespLevel, int respLevel, String plantCode, String userId);
	public void deleteResponsibleMapping(int defaultRespLevel, String plantCode);
	public List<QiResponsibilityMapping> findAll();
} 
