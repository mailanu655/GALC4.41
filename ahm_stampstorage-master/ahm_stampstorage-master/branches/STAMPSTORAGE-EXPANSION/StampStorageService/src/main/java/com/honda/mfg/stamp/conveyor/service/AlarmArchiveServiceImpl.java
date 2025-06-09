package com.honda.mfg.stamp.conveyor.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 4/2/12 Time: 1:24 PM To
 * change this template use File | Settings | File Templates.
 */
public class AlarmArchiveServiceImpl implements AlarmArchiveService, Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(AlarmArchiveServiceImpl.class);

	private AlarmServiceHelper alarmServiceHelper;

	public AlarmArchiveServiceImpl(AlarmServiceHelper alarmServiceHelper) {
		this.alarmServiceHelper = alarmServiceHelper;
		serviceRoleWrapper = ServiceRoleWrapperImpl.getInstance();
	}

	@Override
	public void archiveAlarm(Long id, String user) {
		try {
			alarmServiceHelper.archiveAlarm(id, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		if (serviceRoleWrapper.isPassive()) {
			LOG.info("AlarmArchiveServiceImpl#run(): Passive mode...not running");
			return;
		}
		LOG.debug("AlarmArchiveServiceImpl#run(): Running...");

		try {
			List<AlarmEvent> alarmEvents = alarmServiceHelper.getExpiredAlarms();
			if (alarmEvents.size() == 0) {
				LOG.info("No Expired Alarms....");
			}
			for (AlarmEvent event : alarmEvents) {
				LOG.info(" Archiving Alarm -" + event.getAlarmNumber());
				archiveAlarm(event.getId(), StorageConfig.OHCV_APP_ALARM_ARCHIVE);
			}

		} catch (Exception e) {
			LOG.debug(e.getMessage());
			e.printStackTrace();
		}

		LOG.debug("Done running...");
	}

	private ServiceRoleWrapper serviceRoleWrapper = null;

//	public ServiceRoleWrapper getServiceRoleWrapper() {
//		return serviceRoleWrapper;
//	}
	@Override
	public void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper) {
		this.serviceRoleWrapper = serviceRoleWrapper;
	}

}
