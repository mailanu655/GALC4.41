package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/11/12
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmDefinitionWrapperTest {

    @Test
    public void successfullyCreateAlarmDefinitionWrapper() {
        AlarmDefinitionWrapper alarmDefinitionWrapper = new AlarmDefinitionWrapper();
        alarmDefinitionWrapper.setNotificationRequired(true);
        alarmDefinitionWrapper.setQpcNotificationRequired(true);
        alarmDefinitionWrapper.setAutoArchiveTimeInMinutes(10.0);
        alarmDefinitionWrapper.setActive(true);
        alarmDefinitionWrapper.setAlarmNumber(1200);
        alarmDefinitionWrapper.setDescription("alarm definition");
        alarmDefinitionWrapper.setLocation(1201);
        alarmDefinitionWrapper.setName("alarm definition");
        alarmDefinitionWrapper.setNotificationCategory(AlarmNotificationCategory.INFORMATION);
        alarmDefinitionWrapper.setSeverity(SEVERITY.FOUR);
        alarmDefinitionWrapper.setContact1(new Contact());
        alarmDefinitionWrapper.setContactType1(ContactType.EMAIL);
        alarmDefinitionWrapper.setContact2(new Contact());
        alarmDefinitionWrapper.setContactType2(ContactType.EMAIL);
        alarmDefinitionWrapper.setContact3(new Contact());
        alarmDefinitionWrapper.setContactType3(ContactType.EMAIL);
        alarmDefinitionWrapper.setContact4(new Contact());
        alarmDefinitionWrapper.setContactType4(ContactType.EMAIL);
        alarmDefinitionWrapper.setContact5(new Contact());
        alarmDefinitionWrapper.setContactType5(ContactType.EMAIL);

        assertNotNull(alarmDefinitionWrapper.getAlarmNumber());
        assertNotNull(alarmDefinitionWrapper.getActive());
        assertNotNull(alarmDefinitionWrapper.getAutoArchiveTimeInMinutes());
        assertNotNull(alarmDefinitionWrapper.getDescription());
        assertNotNull(alarmDefinitionWrapper.getLocation());
        assertNotNull(alarmDefinitionWrapper.getName());
        assertNotNull(alarmDefinitionWrapper.getNotificationCategory());
        assertNotNull(alarmDefinitionWrapper.getNotificationRequired());
        assertNotNull(alarmDefinitionWrapper.getQpcNotificationRequired());
        assertNotNull(alarmDefinitionWrapper.getSeverity());
        assertNotNull(alarmDefinitionWrapper.getContact1());
        assertNotNull(alarmDefinitionWrapper.getContact2());
        assertNotNull(alarmDefinitionWrapper.getContact3());
        assertNotNull(alarmDefinitionWrapper.getContact4());
        assertNotNull(alarmDefinitionWrapper.getContact5());
        assertNotNull(alarmDefinitionWrapper.getContactType1());
        assertNotNull(alarmDefinitionWrapper.getContactType2());
        assertNotNull(alarmDefinitionWrapper.getContactType3());
        assertNotNull(alarmDefinitionWrapper.getContactType4());
        assertNotNull(alarmDefinitionWrapper.getContactType5());
    }
}
