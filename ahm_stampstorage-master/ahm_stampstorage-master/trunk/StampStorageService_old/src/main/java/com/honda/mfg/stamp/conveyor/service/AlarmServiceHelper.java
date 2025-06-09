package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.helper.Helper;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/2/12
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmServiceHelper extends Helper {

    void archiveAlarm(Long id, String user);

    List<AlarmEvent> getExpiredAlarms();

    List<AlarmEvent> getAlarmsToNotify();

    void alertContacts(AlarmEvent alarmEvent);

	List<AlarmEvent> getUnclearedAlarmsByType(int type);
    
    List<AlarmEvent> getAlarmsToNotifyPlc();

    void alertPlc(AlarmEvent alarmEvent);


}
