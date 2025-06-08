package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibilityMappingId;
import com.honda.galc.service.Parameters;

public class QiResponsibilityMappingDaoImpl extends BaseDaoImpl<QiResponsibilityMapping, QiResponsibilityMappingId> implements QiResponsibilityMappingDao {
	
	@Transactional  
	public boolean insertResponsibleMapping(int defaultRespLevelId, int respLevelId, String plantCode, String userId) {
		if (isExist(defaultRespLevelId, plantCode)) return false;
		insert(new QiResponsibilityMapping(defaultRespLevelId, plantCode, respLevelId, userId));
		return true;
	}
	
	private boolean isExist(int defaultRespLevelId, String plantCode) {
		Parameters params = Parameters.with("id.defaultResponsibleLevelId", defaultRespLevelId).put("id.plantCode", plantCode);
		QiResponsibilityMapping result = findFirst(params);
		if (result == null) return false;
		return true;
	} 	
	
	@Transactional  
	public void deleteResponsibleMapping(int defaultRespLevelId, String plantCode) {
		Parameters params = Parameters.with("id.defaultResponsibleLevelId", defaultRespLevelId).put("id.plantCode", plantCode);
		delete(params);
	} 
	 
	public List<QiResponsibilityMapping> findAll() {
		return findAll(null, new String[] {"id.plantCode"});
	}
}
