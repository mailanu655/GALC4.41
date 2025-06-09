package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/4/12
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmAlertServiceImpl implements AlarmAlertService, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmAlertServiceImpl.class);

    private AlarmServiceHelper alarmServiceHelper;

    public AlarmAlertServiceImpl(AlarmServiceHelper alarmServiceHelper) {
        this.alarmServiceHelper = alarmServiceHelper;
        serviceRoleWrapper = ServiceRoleWrapperImpl.getInstance();
    }

    @Override
    public void run() {
    	if(serviceRoleWrapper.isPassive())  {
            LOG.info("AlarmAlertServiceImpl#run(): Passive mode...not running");
    		return;
    	}
        LOG.debug("AlarmAlertServiceImpl#run(): Running...");
        try {
            List<AlarmEvent> alarmEvents = alarmServiceHelper.getAlarmsToNotify();

            if (alarmEvents.size() == 0) {
                LOG.info(" no alarms to notify....");
            } else {
                for (AlarmEvent alarmEvent : alarmEvents) {
                    alarmServiceHelper.alertContacts(alarmEvent);
                }
            }

            List<AlarmEvent> plcAlarmEvents = alarmServiceHelper.getAlarmsToNotifyPlc();
            if (plcAlarmEvents.size() == 0) {
                LOG.info(" no alarms to notify plc....");
            } else {
                 LOG.info(" found alarms to notify plc....");
                for (AlarmEvent alarmEvent : plcAlarmEvents) {
                    LOG.info(" Notifying plc about alarm-"+alarmEvent.getAlarmNumber());
                    alarmServiceHelper.alertPlc(alarmEvent);
                }
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
