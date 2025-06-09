package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmTypes;
import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.messages.PlcAlarmMessage;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/3/12
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class AlarmServiceHelperImplTest {
     int count= 0 ;
    //@Test
    public void successfullyArchiveAlarm() {
        loadTestData();

        List<AlarmEvent> events = AlarmEvent.findAllCurrent_Alarms();
        assertEquals("events before archival-", 3, AlarmEvent.countCurrent_Alarms());
        assertEquals("archived alarms before archival-", 2, AlarmEventArchive.countArchivedAlarms());
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        alarmServiceHelper.archiveAlarm(events.get(0).getId(), StorageConfig.OHCV_APP_ALARM_ARCHIVE);
        assertEquals("events after archival-", 2, AlarmEvent.countCurrent_Alarms());
        assertEquals("archived alarms -", 3, AlarmEventArchive.countArchivedAlarms());
    }

    //@Test
    public void successfullyGetExpiredAlarms() {
        loadTestData();
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        List<AlarmEvent> expiredAlarms = alarmServiceHelper.getExpiredAlarms();

        assertEquals(3, expiredAlarms.size());
    }

     @Test
    public void successfullyGetUnClearedAlarmsByType() {
        loadTestData();
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        List<AlarmEvent> expiredAlarms = alarmServiceHelper.getUnclearedAlarmsByType(AlarmTypes.MES_CONNECTION_UNHEALTHY.type());

        assertEquals(0, expiredAlarms.size());
    }

    //@Test
    public void successfullyGetAlarmsToNotify() {
        loadTestData();
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        List<AlarmEvent> alarms = alarmServiceHelper.getAlarmsToNotify();

        assertEquals(3, alarms.size());
    }

     @Test
    public void successfullyGetAlarmsToNotifyPLC() {
        loadTestData();
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        List<AlarmEvent> alarms = alarmServiceHelper.getAlarmsToNotifyPlc();

        assertEquals(2, alarms.size());
    }
     //@Test
    public void successfullyAlertContacts() {
        loadTestData();
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        List<AlarmEvent> alarms = alarmServiceHelper.getAlarmsToNotify();
        alarmServiceHelper.alertContacts(alarms.get(1));

        verify(alertingService, times(2)).sendEmail(anyString(), anyString());

        List<AlarmEvent> alarms2 = alarmServiceHelper.getAlarmsToNotify();
        assertEquals(2, alarms2.size());

          alarmServiceHelper.alertContacts(alarms.get(0));
    }


    @Test
    public void successfullyAlertPlc() {
        loadTestData();
        AlertingService alertingService = mock(AlertingService.class);
        AlarmServiceHelper alarmServiceHelper = new AlarmServiceHelperImpl(alertingService);
        List<AlarmEvent> alarms = alarmServiceHelper.getAlarmsToNotifyPlc();
        alarmServiceHelper.alertPlc(alarms.get(1));
        //assertEquals(1,count);
    }
     @EventSubscriber(eventClass = PlcAlarmMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void catchStorageEvent(PlcAlarmMessage message) {
        count++;
    }



    private void loadTestData() {

        AlarmDefinition alarmDefinition = new AlarmDefinition();
        alarmDefinition.setAlarmNumber(1);
        alarmDefinition.setLocation(1201);
        alarmDefinition.setAutoArchiveTimeInMinutes(1.0);
        alarmDefinition.setActive(true);
        alarmDefinition.setNotificationRequired(true);
        alarmDefinition.setQpcNotificationRequired(true);
        alarmDefinition.setDescription("alarm");
        alarmDefinition.setName("alarm");
        alarmDefinition.setNotificationCategory(AlarmNotificationCategory.INFORMATION);
        alarmDefinition.setSeverity(SEVERITY.NONE);
        alarmDefinition.persist();


        AlarmEvent event = new AlarmEvent();
        event.setAlarmNumber(1);
        event.setLocation(1201);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -1);

        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        event.setEventTimestamp(timestamp);
        //event.setCleared(false);
        //event.setClearedBy("none");
        // event.setNotified(false);
        event.persist();


        AlarmEvent event1 = new AlarmEvent();
        event1.setAlarmNumber(1);
        event1.setLocation(1201);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.HOUR, -1);

        Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
        event1.setEventTimestamp(timestamp1);
        //event1.setCleared(false);
        //event1.setClearedBy("none");
        //event1.setNotified(false);
        event1.persist();

         AlarmEvent event2 = new AlarmEvent();
        event2.setAlarmNumber(2);
        event2.setLocation(1201);

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.HOUR, -1);

        Timestamp timestamp2 = new Timestamp(c2.getTimeInMillis());
        event2.setEventTimestamp(timestamp2);
        //event1.setCleared(false);
        //event1.setClearedBy("none");
        //event1.setNotified(false);
        event2.persist();

        AlarmEventArchive archive = new AlarmEventArchive();
        archive.setAlarmEventId(1l);
        archive.setAlarmNumber(1);
        archive.setLocation(1210);
        archive.setEventTimestamp(new Timestamp(System.currentTimeMillis()));
        archive.setCleared(true);
        archive.setClearedBy("some user");
        archive.setClearedTimestamp(new Timestamp(System.currentTimeMillis()));
        archive.setArchivedTimestamp(new Timestamp(System.currentTimeMillis()));
        archive.setArchivedBy(StorageConfig.OHCV_APP_ALARM_ARCHIVE);
        archive.persist();

        AlarmEventArchive archive1 = new AlarmEventArchive();
        archive1.setAlarmEventId(1l);
        archive1.setAlarmNumber(1);
        archive1.setLocation(1210);
        archive1.setEventTimestamp(new Timestamp(System.currentTimeMillis()));
        archive1.setCleared(true);
        archive1.setClearedBy("some user");
        archive1.setClearedTimestamp(new Timestamp(System.currentTimeMillis()));
        archive1.setArchivedTimestamp(new Timestamp(System.currentTimeMillis()));
        archive1.setArchivedBy(StorageConfig.OHCV_APP_ALARM_ARCHIVE);
        archive1.persist();

        Contact contact1 = new Contact();
        contact1.setContactName("name1");
        contact1.setEmail("a@honda.com");
        contact1.setPagerNo("6586@honda.com");

        contact1.persist();

        Contact contact2 = new Contact();
        contact2.setContactName("name2");
        contact2.setEmail("b@honda.com");
        contact2.setPagerNo("6666@honda.com");

        contact2.persist();

        AlarmContact alarmContact1 = new AlarmContact();
        alarmContact1.setAlarm(alarmDefinition);
        alarmContact1.setContact(contact1);
        alarmContact1.setContactType(ContactType.EMAIL);
        alarmContact1.persist();

        AlarmContact alarmContact2 = new AlarmContact();
        alarmContact2.setAlarm(alarmDefinition);
        alarmContact2.setContact(contact2);
        alarmContact2.setContactType(ContactType.PAGER);
        alarmContact2.persist();
    }
}
