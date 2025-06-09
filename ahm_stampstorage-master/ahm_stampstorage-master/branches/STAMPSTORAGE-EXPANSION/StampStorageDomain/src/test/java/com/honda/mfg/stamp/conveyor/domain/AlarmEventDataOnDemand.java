package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class AlarmEventDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<AlarmEvent> data;

	public AlarmEvent getNewTransientCurrent_Alarm(int index) {
		AlarmEvent obj = new AlarmEvent();
		setLocation(obj, index);
		setAlarmNumber(obj, index);
		return obj;
	}

	public void setLocation(AlarmEvent obj, int index) {
		Integer location = new Integer(index);
		obj.setLocation(location);
	}

	public void setAlarmNumber(AlarmEvent obj, int index) {
		Integer alarmNumber = new Integer(index);
		obj.setAlarmNumber(alarmNumber);
	}

	public AlarmEvent getSpecificCurrent_Alarm(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		AlarmEvent obj = data.get(index);
		return AlarmEvent.findCurrent_Alarm(obj.getId());
	}

	public AlarmEvent getRandomCurrent_Alarm() {
		init();
		AlarmEvent obj = data.get(rnd.nextInt(data.size()));
		return AlarmEvent.findCurrent_Alarm(obj.getId());
	}

	public boolean modifyCurrent_Alarm(AlarmEvent obj) {
		return false;
	}

	public void init() {
		data = AlarmEvent.findCurrent_AlarmEntries(0, 10);
		if (data == null)
			throw new IllegalStateException("Find entries implementation for 'AlarmEvent' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<AlarmEvent>();
		for (int i = 0; i < 10; i++) {
			AlarmEvent obj = getNewTransientCurrent_Alarm(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
