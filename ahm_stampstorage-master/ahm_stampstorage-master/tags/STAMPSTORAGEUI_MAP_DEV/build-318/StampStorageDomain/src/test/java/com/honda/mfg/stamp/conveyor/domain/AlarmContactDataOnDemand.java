package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class AlarmContactDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<AlarmContact> data;

	@Autowired
    private AlarmDefinitionDataOnDemand alarmDefinitionDataOnDemand;

	@Autowired
    private ContactDataOnDemand contactDataOnDemand;

	public AlarmContact getNewTransientAlarmContact(int index) {
        com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = new com.honda.mfg.stamp.conveyor.domain.AlarmContact();
        setAlarm(obj, index);
        setContact(obj, index);
        setContactType(obj, index);
        return obj;
    }

	public void setAlarm(AlarmContact obj, int index) {
        com.honda.mfg.stamp.conveyor.domain.AlarmDefinition alarm = alarmDefinitionDataOnDemand.getRandomAlarm();
        obj.setAlarm(alarm);
    }

	public void setContact(AlarmContact obj, int index) {
        com.honda.mfg.stamp.conveyor.domain.Contact contact = contactDataOnDemand.getRandomContact();
        obj.setContact(contact);
    }

	public void setContactType(AlarmContact obj, int index) {
        com.honda.mfg.stamp.conveyor.domain.enums.ContactType contactType = com.honda.mfg.stamp.conveyor.domain.enums.ContactType.class.getEnumConstants()[0];
        obj.setContactType(contactType);
    }

	public AlarmContact getSpecificAlarmContact(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        AlarmContact obj = data.get(index);
        return AlarmContact.findAlarmContact(obj.getId());
    }

	public AlarmContact getRandomAlarmContact() {
        init();
        AlarmContact obj = data.get(rnd.nextInt(data.size()));
        return AlarmContact.findAlarmContact(obj.getId());
    }

	public boolean modifyAlarmContact(AlarmContact obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.AlarmContact.findAlarmContactEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'AlarmContact' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.AlarmContact>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.AlarmContact obj = getNewTransientAlarmContact(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
