package com.honda.galc.dao.jpa.qi;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiExternalSystemInfoDao;
import com.honda.galc.entity.qi.QiExternalSystemInfo;
import com.honda.galc.service.Parameters;

public class QiExternalSystemInfoDaoImpl extends BaseDaoImpl<QiExternalSystemInfo, Short> implements QiExternalSystemInfoDao {
	
	private static final String UPDATE_EXTERNAL_SYSTEM = "UPDATE GALADM.QI_EXTERNAL_SYSTEM_INFO_TBX "
			+ "SET EXTERNAL_SYSTEM_NAME=?1, EXTERNAL_SYSTEM_DESC=?2, UPDATE_USER=?3 WHERE EXTERNAL_SYSTEM_ID=?4";
	
	@Override
	public QiExternalSystemInfo findByExternalSystemName(String extSysName) {
		Parameters params = Parameters.with("externalSystemName", extSysName);
		return findFirst(params);
	}

	@Override
	public QiExternalSystemInfo findByExternalSystemId(Short extSysId) {
		Parameters params = Parameters.with("externalSystemId", extSysId);
		return findFirst(params);
	}
	
	@Override
	@Transactional
	public QiExternalSystemInfo createExternalSystemInfo(String name, String desc, String createUser) {
		QiExternalSystemInfo qiExternalSystemInfo = new QiExternalSystemInfo();
		qiExternalSystemInfo.setExternalSystemName(name);
		qiExternalSystemInfo.setExternalSystemDesc(desc);
		qiExternalSystemInfo.setCreateUser(createUser);
		return super.insert(qiExternalSystemInfo);
	}
	
	@Override
	@Transactional
	public void updateExternalSystemInfo(short id, String name, String desc, String updateUser) {
		Parameters params = Parameters.with("1", name)
				.put("2", desc).put("3", updateUser).put("4", id);
		executeNativeUpdate(UPDATE_EXTERNAL_SYSTEM, params);
	}
}
