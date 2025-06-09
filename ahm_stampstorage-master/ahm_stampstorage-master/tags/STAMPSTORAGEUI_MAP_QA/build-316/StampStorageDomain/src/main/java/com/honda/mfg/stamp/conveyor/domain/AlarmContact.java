package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import java.util.List;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ALARM_CONTACT_TBX")
@Configurable

public class AlarmContact {

    @NotNull
    @OneToOne
    private AlarmDefinition alarm;

    @NotNull
    @OneToOne
    private Contact contact;

    @Column(name = "CONTACT_TYPE")
    @Enumerated
    private ContactType contactType;

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ALARM_CONTACT_ID")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AlarmContact attached = AlarmContact.findAlarmContact(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public AlarmContact merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AlarmContact merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new AlarmContact().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAlarmContacts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AlarmContact o", Long.class).getSingleResult();
    }

	public static List<AlarmContact> findAllAlarmContacts() {
        return entityManager().createQuery("SELECT o FROM AlarmContact o", AlarmContact.class).getResultList();
    }

	public static AlarmContact findAlarmContact(Long id) {
        if (id == null) return null;
        return entityManager().find(AlarmContact.class, id);
    }

	public static AlarmContact findAlarmContactByAlarmAndContact(AlarmDefinition alarm, Contact contact){
        Query q = entityManager().createQuery("SELECT o FROM AlarmContact o where o.alarm=:alarm And o.contact=:contact", AlarmContact.class);
        q.setParameter("alarm", alarm);
        q.setParameter("contact", contact);

        List<AlarmContact> alarmContacts = q.getResultList();
        if(alarmContacts != null || alarmContacts.size() > 0){
            return alarmContacts.get(0);
        }

        return null;
    }

	public static List<AlarmContact> findAlarmContactsByAlarm(AlarmDefinition alarm){
        Query q = entityManager().createQuery("SELECT o FROM AlarmContact o where o.alarm.id=:alarm ", AlarmContact.class);
        q.setParameter("alarm", alarm.getId());

        List<AlarmContact> alarmContacts = q.getResultList();
        if(alarmContacts != null || alarmContacts.size() > 0){
            return alarmContacts;
        }

        return null;
    }

	public static List<AlarmContact> findAlarmContactEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AlarmContact o", AlarmContact.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public AlarmDefinition getAlarm() {
        return this.alarm;
    }

	public void setAlarm(AlarmDefinition alarm) {
        this.alarm = alarm;
    }

	public Contact getContact() {
        return this.contact;
    }

	public void setContact(Contact contact) {
        this.contact = contact;
    }

	public ContactType getContactType() {
        return this.contactType;
    }

	public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alarm: ").append(getAlarm()).append(", ");
        sb.append("Contact: ").append(getContact()).append(", ");
        sb.append("ContactType: ").append(getContactType()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
