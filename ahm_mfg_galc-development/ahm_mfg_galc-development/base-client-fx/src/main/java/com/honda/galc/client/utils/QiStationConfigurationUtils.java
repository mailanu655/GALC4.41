package com.honda.galc.client.utils;

import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.ServiceFactory;

public class QiStationConfigurationUtils {

	public static boolean isGlobalDirectPass(String processPointId) {
		return getBooleanValue(processPointId, QiEntryStationConfigurationSettings.GDP_PROCESS_POINT);
	}

	public static boolean isTrpuProcessPoint(String processPointId) {
		return getBooleanValue(processPointId, QiEntryStationConfigurationSettings.TRPU_PROCESS_POINT);
	}

	public static boolean getBooleanValue(String processPointId, QiEntryStationConfigurationSettings setting) {
		QiStationConfiguration config = ServiceFactory.getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(processPointId, setting.getSettingsName());
		return QiConstant.YES.equalsIgnoreCase(config == null ? setting.getDefaultPropertyValue() : config.getPropertyValue());
	}
}

