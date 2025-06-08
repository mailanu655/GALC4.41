package com.honda.galc.dao.jpa.qi;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiExternalSystemDefectIdMapDao;
import com.honda.galc.entity.qi.QiExternalSystemDefectId;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.service.Parameters;

public class QiExternalSystemDefectIdMapDaoImpl
extends BaseDaoImpl<QiExternalSystemDefectIdMap, QiExternalSystemDefectId>
implements QiExternalSystemDefectIdMapDao {

	public static final String FIND_BY_EXT_SYS_NAME_AND_EXT_SYS_KEY =
			"select e from QiExternalSystemDefectIdMap e, QiExternalSystemInfo f where e.id.externalSystemId = f.externalSystemId " +
			"and e.id.externalSystemDefectKey = :externalSystemKey and f.externalSystemName = :externalSystemName";

	@Override
	public QiExternalSystemDefectIdMap findByExternalSystemKey(String extSysName, Long extKey) {
		Parameters params = Parameters.with("externalSystemName", extSysName).put("externalSystemKey", extKey);
		return findFirstByQuery(FIND_BY_EXT_SYS_NAME_AND_EXT_SYS_KEY, params);
	}

	@Override
	public QiExternalSystemDefectIdMap findByDefectId(Long defectResultId) {
		Parameters params = Parameters.with("id.defectResultId", defectResultId);
		return findFirst(params);
	}
	
	@Override
	public boolean isExternalSystemIdUsed(short externalSystemId) {
		Parameters params = Parameters.with("id.externalSystemId", externalSystemId);
		return findFirst(params) != null? true : false;
	}

}
