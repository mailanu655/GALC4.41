package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class AlarmEventArchiveDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<AlarmEventArchive> data;

	public AlarmEventArchive getNewTransientArchivedAlarm(int index) {
		AlarmEventArchive obj = new AlarmEventArchive();
		setLocation(obj, index);
		setAlarmNumber(obj, index);
		setAlarmEventId(obj, index);
		return obj;
	}

	public void setAlarmEventId(AlarmEventArchive obj, int index) {
		Long alarmEventId = new Long(index);
		obj.setAlarmEventId(alarmEventId);
	}

	public void setLocation(AlarmEventArchive obj, int index) {
		Integer location = new Integer(index);
		obj.setLocation(location);
	}

	public void setAlarmNumber(AlarmEventArchive obj, int index) {
		Integer alarmNumber = new Integer(index);
		obj.setAlarmNumber(alarmNumber);
	}

	public AlarmEventArchive getSpecificArchivedAlarm(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		AlarmEventArchive obj = data.get(index);
		return AlarmEventArchive.findArchivedAlarm(obj.getId());
	}

	public AlarmEventArchive getRandomArchivedAlarm() {
		init();
		AlarmEventArchive obj = data.get(rnd.nextInt(data.size()));
		return AlarmEventArchive.findArchivedAlarm(obj.getId());
	}

	public boolean modifyArchivedAlarm(AlarmEventArchive obj) {
		return false;
	}

	public void init() {
		data = AlarmEventArchive.findArchivedAlarmEntries(0, 10);
		if (data == null)
			throw new IllegalStateException(
					"Find entries implementation for 'AlarmEventArchive' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<AlarmEventArchive>();
		for (int i = 0; i < 10; i++) {
			AlarmEventArchive obj = getNewTransientArchivedAlarm(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
