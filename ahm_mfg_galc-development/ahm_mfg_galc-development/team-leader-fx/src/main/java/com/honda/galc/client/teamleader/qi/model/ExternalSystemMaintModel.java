package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectIdMapDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiExternalSystemInfoDao;
import com.honda.galc.entity.qi.QiExternalSystemInfo;

/**
 * 
 * <h3>ExternalSystemMaintModel Class description</h3>
 * <p>
 * ExternalSystemMaintModel description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author Justin Jiang<br>
 *         January 14, 2021
 *
 */

public class ExternalSystemMaintModel extends QiModel{	
	
	public List<QiExternalSystemInfo> findAllExternalSystem() {
		return getDao(QiExternalSystemInfoDao.class).findAll();
	}
	
	public boolean isExisted(String name) {
		QiExternalSystemInfo existingQiExternalSystemInfo = getDao(QiExternalSystemInfoDao.class).findByExternalSystemName(name);
		return existingQiExternalSystemInfo == null? false : true;
	}
	
	public QiExternalSystemInfo createExternalSystemInfo(String name, String desc, String createUser) {
		return getDao(QiExternalSystemInfoDao.class).createExternalSystemInfo(name, desc, createUser);
	}
	
	public void updateExternalSystemInfo(short id, String name, String desc, String updateUser) {
		getDao(QiExternalSystemInfoDao.class).updateExternalSystemInfo(id, name, desc, updateUser);
	}
	
	public void deleteExternalSystemInfo(QiExternalSystemInfo entity) {
		getDao(QiExternalSystemInfoDao.class).remove(entity);
	}
	
	public boolean isExternalSystemNameUsed(String externalSystemName) {
		if (getDao(QiExternalSystemDataDao.class).isExternalSystemNameUsed(externalSystemName)) {
			return true;
		} else if (getDao(QiExternalSystemDefectMapDao.class).isExternalSystemNameUsed(externalSystemName)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isExternalSystemIdUsed(short externalSystemId) {
		if (getDao(QiExternalSystemDefectIdMapDao.class).isExternalSystemIdUsed(externalSystemId)) {
			return true;
		} else {
			return false;
		}
	}
}
