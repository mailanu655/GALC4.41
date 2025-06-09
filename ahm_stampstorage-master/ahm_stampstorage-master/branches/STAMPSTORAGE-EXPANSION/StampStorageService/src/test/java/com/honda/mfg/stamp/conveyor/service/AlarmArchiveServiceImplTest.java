package com.honda.mfg.stamp.conveyor.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.mockito.Matchers;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 4/3/12 Time: 1:31 PM To
 * change this template use File | Settings | File Templates.
 */
public class AlarmArchiveServiceImplTest {

	@Test
	public void successfullyArchiveAlarms() {
		List<AlarmEvent> alarmEvents = new ArrayList<AlarmEvent>();
		AlarmEvent event1 = new AlarmEvent();
		event1.setAlarmNumber(1);
		event1.setLocation(1201);
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.HOUR, -1);
		Timestamp timestamp1 = new Timestamp(c1.getTimeInMillis());
		event1.setEventTimestamp(timestamp1);
		event1.setCleared(false);
		event1.setClearedBy("none");
		event1.setNotified(true);

		alarmEvents.add(event1);

		AlarmServiceHelper helper = mock(AlarmServiceHelper.class);
		when(helper.getExpiredAlarms()).thenReturn(alarmEvents).thenThrow(new RuntimeException());
		ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
		when(serviceRoleWrap.isPassive()).thenReturn(false).thenReturn(false).thenReturn(true);
		AlarmArchiveService alarmArchiveService = new AlarmArchiveServiceImpl(helper);
		alarmArchiveService.setServiceRoleWrapper(serviceRoleWrap);
		alarmArchiveService.run();
		alarmArchiveService.run();
		alarmArchiveService.run();
	}

	@Test
	public void successfullyHandleExceptionWhenArchiving() {
		AlarmServiceHelper helper = mock(AlarmServiceHelper.class);
		ServiceRoleWrapper serviceRoleWrap = mock(ServiceRoleWrapper.class);
		when(serviceRoleWrap.isPassive()).thenReturn(false);
		AlarmArchiveService alarmArchiveService = new AlarmArchiveServiceImpl(helper);
		alarmArchiveService.setServiceRoleWrapper(serviceRoleWrap);
		doThrow(new RuntimeException()).when(helper).archiveAlarm(Matchers.<Long>any(), anyString());

		alarmArchiveService.archiveAlarm(1L, "user");
	}
}
