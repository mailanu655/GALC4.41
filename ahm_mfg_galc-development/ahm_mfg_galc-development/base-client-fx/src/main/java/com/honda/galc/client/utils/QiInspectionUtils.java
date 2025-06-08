package com.honda.galc.client.utils;

import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class QiInspectionUtils {

	public static boolean isGdpProcessPoint(String processPointId) {
		return getBooleanValue(processPointId, QiEntryStationConfigurationSettings.GDP_PROCESS_POINT);
	}

	public static boolean isTrpuProcessPoint(String processPointId) {
		return getBooleanValue(processPointId, QiEntryStationConfigurationSettings.TRPU_PROCESS_POINT);
	}

	public static boolean getBooleanValue(String processPointId, QiEntryStationConfigurationSettings setting) {
		QiStationConfiguration config = ServiceFactory.getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(processPointId, setting.getSettingsName());
		return QiConstant.YES.equalsIgnoreCase(config == null ? setting.getDefaultPropertyValue() : config.getPropertyValue());
	}
	
	public static boolean isGlobalGdpEnabled() {
		return PropertyService.getPropertyBean(QiPropertyBean.class).isGlobalGdp();
	}

	public static boolean isGlobalGdpWriteUpDept(String writeUpDepartment) {
		String[] globalGdpDepts = PropertyService.getPropertyBean(QiPropertyBean.class).getGlobalGdpWriteUpDepts();
		for(String dept : globalGdpDepts) {
			if(dept.equalsIgnoreCase(writeUpDepartment)) {
				return true;
			}
		}
		return false;
	}
	
}
