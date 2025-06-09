package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import com.honda.mfg.stamp.conveyor.helper.AbstractHelperImpl;
import com.honda.mfg.stamp.conveyor.messages.PlcAlarmMessage;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/2/12
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmServiceHelperImpl extends AbstractHelperImpl implements AlarmServiceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmServiceHelperImpl.class);


    private AlertingService alertingService;

    public AlarmServiceHelperImpl(AlertingService alertingService) {
        this.alertingService = alertingService;
    }



    @Override
    public List<AlarmEvent> getExpiredAlarms() {
        List<AlarmEvent> expiredAlarms = new ArrayList<AlarmEvent>();
        List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarms(50);
        for (AlarmEvent event : alarmEvents) {
           // LOG.info("Alarm Number-" + event.getAlarmNumber() + " Location-" + event.getLocation());
            AlarmDefinition alarm = event.getAlarm();
            if (alarm.getAutoArchiveTimeInMinutes() != null && alarm.getAutoArchiveTimeInMinutes().intValue() > 0) {
                int timeInMin = 0 - alarm.getAutoArchiveTimeInMinutes().intValue();

                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE, timeInMin);

                Timestamp timestamp1 = new Timestamp(c.getTimeInMillis());

                if (event.getEventTimestamp().before(timestamp1)) {
                    expiredAlarms.add(event);
                }
            } else {
                expiredAlarms.add(event);
            }
        }
        return expiredAlarms;
    }

    @Override
    public List<AlarmEvent> getAlarmsToNotify() {
        List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarmsToNotify(50);

        return alarmEvents;
    }

    @Override
    public List<AlarmEvent> getAlarmsToNotifyPlc() {
        List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarmsForQpcNotify(50);

        return alarmEvents;
    }

    @Override
    public void alertPlc(AlarmEvent alarmEvent) {
        AlarmDefinition alarm = alarmEvent.getAlarm();

        PlcAlarmMessage plcAlarmMessage = new PlcAlarmMessage(alarm.getAlarmNumber().toString(),alarm.getLocation().toString(),alarm.getDescription());
        EventBus.publish(plcAlarmMessage);
    }

    @Override
    public List<AlarmEvent> getUnclearedAlarmsByType(int type) {
        List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarmsByType(type, 50);

        return alarmEvents;
    }

    @Override
    public void alertContacts(AlarmEvent alarmEvent) {
        AlarmDefinition alarmDefinition = alarmEvent.getAlarm();
        List<AlarmContact> alarmContacts = new ArrayList<AlarmContact>();

        alarmContacts = AlarmContact.findAlarmContactsByAlarm(alarmDefinition);

        if (alarmContacts.size() > 0) {
            for (AlarmContact alarmContact : alarmContacts) {
                Contact contact = alarmContact.getContact();
                if (alarmContact.getContactType().equals(ContactType.EMAIL) || alarmContact.getContactType().equals(ContactType.BOTH)) {
                    LOG.info(" sending email for alarm-" + alarmEvent.getAlarmNumber() + " to-" + contact.getContactName());
                    alertingService.sendEmail(contact.getEmail(), alarmDefinition.getDescription());
                }

                if (alarmContact.getContactType().equals(ContactType.PAGER) || alarmContact.getContactType().equals(ContactType.BOTH)) {
                    LOG.info(" sending page for alarm-" + alarmEvent.getAlarmNumber() + " to-" + contact.getContactName());
                    alertingService.sendEmail(contact.getPagerNo(), alarmDefinition.getDescription());
                }
            }

            AlarmEvent event = AlarmEvent.findCurrent_Alarm(alarmEvent.getId());
            event.setNotified(true);
            event.merge();
        }
    }
}
