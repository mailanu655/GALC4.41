package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class AlarmDefinitionDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<AlarmDefinition> data;

	public AlarmDefinition getNewTransientAlarm(int index) {
        AlarmDefinition obj = new AlarmDefinition();
        setId(obj,index);
        setAlarmNumber(obj, index);
        setLocation(obj, index);
        setName(obj, index);
        setDescription(obj, index);
        setCategory(obj, index);
        setSeverity(obj, index);
        setAutoArchiveTime(obj, index);
        setActive(obj, index);
        setNotificationRequired(obj, index);
        setQpcNotificationRequired(obj, index);
        //setNotification(obj, index);
        return obj;
    }

	public void setId(AlarmDefinition obj, int index) {
      obj.setId(new Long(index));
    }

	public void setLocation(AlarmDefinition obj, int index) {
        obj.setLocation(index);
    }

	public void setAlarmNumber(AlarmDefinition obj, int index) {
        Integer alarmNumber = new Integer(index);
        obj.setAlarmNumber(alarmNumber);
    }

	public void setName(AlarmDefinition obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setDescription(AlarmDefinition obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }

	public void setCategory(AlarmDefinition obj, int index) {
        AlarmNotificationCategory notificationCategory = AlarmNotificationCategory.class.getEnumConstants()[0];
        obj.setNotificationCategory(notificationCategory);
    }

	public void setSeverity(AlarmDefinition obj, int index) {
        SEVERITY severity = SEVERITY.class.getEnumConstants()[0];
        obj.setSeverity(severity);
    }

	public void setAutoArchiveTime(AlarmDefinition obj, int index) {
       obj.setAutoArchiveTimeInMinutes(Double.valueOf("0"));
    }

	public void setActive(AlarmDefinition obj, int index) {
       obj.setActive(true);
    }

	public void setNotificationRequired(AlarmDefinition obj, int index) {
       obj.setNotificationRequired(true);
    }

	public void setQpcNotificationRequired(AlarmDefinition obj, int index) {
          obj.setQpcNotificationRequired(true);
       }

	//    public void AlarmDefinitionDataOnDemand.setNotification(AlarmDefinition obj, int index) {
//        String notification = "notification_" + index;
//        obj.setNotification(notification);
//    }

    public AlarmDefinition getSpecificAlarm(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        AlarmDefinition obj = data.get(index);
        return AlarmDefinition.findAlarmDefinition(obj.getId());
    }

	public AlarmDefinition getRandomAlarm() {
        init();
        AlarmDefinition obj = data.get(rnd.nextInt(data.size()));
        return AlarmDefinition.findAlarmDefinition(obj.getId());
    }

	public boolean modifyAlarm(AlarmDefinition obj) {
        return false;
    }

	public void init() {
        data = AlarmDefinition.findAlarmEntries(0, 10);
        if (data == null)
            throw new IllegalStateException("Find entries implementation for 'AlarmDefinition' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }

        data = new java.util.ArrayList<AlarmDefinition>();
        for (int i = 0; i < 10; i++) {
            AlarmDefinition obj = getNewTransientAlarm(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
