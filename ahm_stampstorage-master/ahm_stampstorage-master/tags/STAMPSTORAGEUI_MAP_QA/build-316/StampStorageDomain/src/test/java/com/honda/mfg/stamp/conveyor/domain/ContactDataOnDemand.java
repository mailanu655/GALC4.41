package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class ContactDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<Contact> data;

	public Contact getNewTransientContact(int index) {
        com.honda.mfg.stamp.conveyor.domain.Contact obj = new com.honda.mfg.stamp.conveyor.domain.Contact();
        setContactName(obj, index);
        setEmail(obj, index);
        setPagerNo(obj, index);
        return obj;
    }

	public void setContactName(Contact obj, int index) {
        java.lang.String contactName = "contactName_" + index;
        obj.setContactName(contactName);
    }

	public void setEmail(Contact obj, int index) {
        java.lang.String email = "email_" + index;
        obj.setEmail(email);
    }

	public void setPagerNo(Contact obj, int index) {
        java.lang.String pagerNo = "pagerNo_" + index;
        obj.setPagerNo(pagerNo);
    }

	public Contact getSpecificContact(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Contact obj = data.get(index);
        return Contact.findContact(obj.getId());
    }

	public Contact getRandomContact() {
        init();
        Contact obj = data.get(rnd.nextInt(data.size()));
        return Contact.findContact(obj.getId());
    }

	public boolean modifyContact(Contact obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.Contact.findContactEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Contact' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.Contact>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.Contact obj = getNewTransientContact(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
