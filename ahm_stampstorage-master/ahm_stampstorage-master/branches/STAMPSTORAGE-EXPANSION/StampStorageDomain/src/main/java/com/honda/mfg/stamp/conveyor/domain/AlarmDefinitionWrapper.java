package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/26/12 Time: 2:40 PM To
 * change this template use File | Settings | File Templates.
 */
public class AlarmDefinitionWrapper {

	private long id;
	private Integer alarmNumber;

	private Integer location;

	private String name;

	private String description;

	private AlarmNotificationCategory notificationCategory;

	private SEVERITY severity;

	private Double autoArchiveTimeInMinutes;

	private Boolean notificationRequired;

	private Boolean qpcNotificationRequired;

	private Boolean active;

	// private String notification;

	private Contact contact1;

	private ContactType contactType1;

	private Contact contact2;

	private ContactType contactType2;

	private Contact contact3;

	private ContactType contactType3;

	private Contact contact4;

	private ContactType contactType4;

	private Contact contact5;

	private ContactType contactType5;

	public AlarmDefinitionWrapper() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getAlarmNumber() {
		return alarmNumber;
	}

	public void setAlarmNumber(Integer alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AlarmNotificationCategory getNotificationCategory() {
		return notificationCategory;
	}

	public void setNotificationCategory(AlarmNotificationCategory notificationCategory) {
		this.notificationCategory = notificationCategory;
	}

	public SEVERITY getSeverity() {
		return severity;
	}

	public void setSeverity(SEVERITY severity) {
		this.severity = severity;
	}

	public Double getAutoArchiveTimeInMinutes() {
		return autoArchiveTimeInMinutes;
	}

	public void setAutoArchiveTimeInMinutes(Double autoArchiveTimeInMinutes) {
		this.autoArchiveTimeInMinutes = autoArchiveTimeInMinutes;
	}

	public Boolean getNotificationRequired() {
		return notificationRequired;
	}

	public void setNotificationRequired(Boolean notificationRequired) {
		this.notificationRequired = notificationRequired;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

//    public String getNotification() {
//        return notification;
//    }
//
//    public void setNotification(String notification) {
//        this.notification = notification;
//    }

	public Contact getContact1() {
		return contact1;
	}

	public void setContact1(Contact contact1) {
		this.contact1 = contact1;
	}

	public ContactType getContactType1() {
		return contactType1;
	}

	public void setContactType1(ContactType contactType1) {
		this.contactType1 = contactType1;
	}

	public Contact getContact2() {
		return contact2;
	}

	public void setContact2(Contact contact2) {
		this.contact2 = contact2;
	}

	public ContactType getContactType2() {
		return contactType2;
	}

	public void setContactType2(ContactType contactType2) {
		this.contactType2 = contactType2;
	}

	public Contact getContact3() {
		return contact3;
	}

	public void setContact3(Contact contact3) {
		this.contact3 = contact3;
	}

	public ContactType getContactType3() {
		return contactType3;
	}

	public void setContactType3(ContactType contactType3) {
		this.contactType3 = contactType3;
	}

	public Contact getContact4() {
		return contact4;
	}

	public void setContact4(Contact contact4) {
		this.contact4 = contact4;
	}

	public ContactType getContactType4() {
		return contactType4;
	}

	public void setContactType4(ContactType contactType4) {
		this.contactType4 = contactType4;
	}

	public Contact getContact5() {
		return contact5;
	}

	public void setContact5(Contact contact5) {
		this.contact5 = contact5;
	}

	public ContactType getContactType5() {
		return contactType5;
	}

	public void setContactType5(ContactType contactType5) {
		this.contactType5 = contactType5;
	}

	public Boolean getQpcNotificationRequired() {
		return qpcNotificationRequired;
	}

	public void setQpcNotificationRequired(Boolean qpcNotificationRequired) {
		this.qpcNotificationRequired = qpcNotificationRequired;
	}
}
