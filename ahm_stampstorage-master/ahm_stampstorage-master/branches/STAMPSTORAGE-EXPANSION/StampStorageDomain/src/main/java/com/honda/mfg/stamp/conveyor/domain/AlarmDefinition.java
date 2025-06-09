package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.AutoArchiveTime;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;

@Configurable
@Entity
@Table(name = "ALARMS_DEFINITION_TBX")
public class AlarmDefinition {

	@NotNull
	@Column(name = "ALARM_NUMBER")
	private Integer alarmNumber;

	@NotNull
	@Column(name = "LOCATION")
	private Integer location;

	@NotNull
	@Column(name = "ALARM_NAME")
	private String name;

	@NotNull
	@Column(name = "DESCRIPTION")
	private String description;

	@NotNull
	@Column(name = "NOTIFICATION_CATEGORY")
	@Enumerated
	private AlarmNotificationCategory notificationCategory;

	@NotNull
	@Column(name = "SEVERITY")
	@Enumerated
	private SEVERITY severity;

	@NotNull
	@Column(name = "AUTO_ARCHIVE_TIME")
	private Double autoArchiveTimeInMinutes;

	@NotNull
	@Column(name = "NOTIFICATION_REQUIRED")
	private Boolean notificationRequired;

	@NotNull
	@Column(name = "QPC_NOTIFICATION_REQUIRED")
	private Boolean qpcNotificationRequired;

	@NotNull
	@Column(name = "ACTIVE")
	private Boolean active;

	// private String notification;

	public String toString() {
		StringBuilder sb = new StringBuilder();
//        sb.append("ID: ").append(getId()).append(", ");
//        sb.append("AlarmNumber: ").append(getAlarmNumber()).append(", ");
//        sb.append("Location: ").append(getLocation()).append(", ");
//        sb.append("Description: ").append(getDescription()).append(", ");
//        sb.append("Category: ").append(getNotificationCategory()).append(", ");
//        sb.append("Notification: ").append(getNotification()).append(", ");
//        sb.append("Severity: ").append(getSeverity()).append(", ");
//        sb.append("Version: ").append(getVersion());
		sb.append(getDescription());
		return sb.toString();
	}

	public Integer getAlarmNumber() {
		return this.alarmNumber;
	}

	public void setAlarmNumber(Integer alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AlarmNotificationCategory getNotificationCategory() {
		return this.notificationCategory;
	}

	public void setNotificationCategory(AlarmNotificationCategory notificationCategory) {
		this.notificationCategory = notificationCategory;
	}

	public SEVERITY getSeverity() {
		return this.severity;
	}

	public void setSeverity(SEVERITY severity) {
		this.severity = severity;
	}

	public String getNotification() {

		List<AlarmContact> alarmContacts = AlarmContact.findAlarmContactsByAlarm(this);
		String notification = "";
		for (AlarmContact alarmContact : alarmContacts) {
			notification = notification + ", " + alarmContact.getContact().getContactName();
		}
		return notification;
	}

	// public void AlarmDefinition.setNotification(String notification) {
//        this.notification = notification;
//    }

	public Integer getLocation() {
		return this.location;
	}

	public void setLocation(Integer location) {
		this.location = location;
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

	public AutoArchiveTime getAutoArchiveTime() {
		if (autoArchiveTimeInMinutes == null)
			return AutoArchiveTime.findByTime(0);
		return AutoArchiveTime.findByTime(autoArchiveTimeInMinutes);
	}

	public Boolean getQpcNotificationRequired() {
		return qpcNotificationRequired;
	}

	public void setQpcNotificationRequired(Boolean qpcNotificationRequired) {
		this.qpcNotificationRequired = qpcNotificationRequired;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ALARM_DEFINITION_ID")
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
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			AlarmDefinition attached = AlarmDefinition.findAlarmDefinition(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public AlarmDefinition merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		AlarmDefinition merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new AlarmDefinition().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countAlarmDefinitions() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AlarmDefinition o", Long.class).getSingleResult();
	}

	public static List<AlarmDefinition> findAllAlarmDefinitions() {
		return entityManager().createQuery("SELECT o FROM AlarmDefinition o ORDER BY o.id asc", AlarmDefinition.class)
				.getResultList();
	}

	public static AlarmDefinition findAlarmDefinition(Long id) {
		if (id == null)
			return null;
		return entityManager().find(AlarmDefinition.class, id);
	}

	public static List<AlarmDefinition> findAlarmEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM AlarmDefinition o ORDER BY o.id asc", AlarmDefinition.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<AlarmDefinition> findAlarmEntriesByActiveStatus(int firstResult, int maxResults,
			Boolean active) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmDefinition o where o.active=:active ORDER BY o.id asc", AlarmDefinition.class);
		q.setParameter("active", active);
		return q.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static long countActiveAlarms(Boolean active) {
		Query q = entityManager().createQuery("SELECT COUNT(o) FROM AlarmDefinition o where o.active=:active ",
				Long.class);
		q.setParameter("active", active);

		return (Long) q.getSingleResult();
	}

	public static AlarmDefinition findAlarmByAlarmNumberAndLocation(Integer number, Integer location) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmDefinition o where o.alarmNumber=:alarmNumber and o.location=:location",
				AlarmDefinition.class);
		q.setParameter("alarmNumber", number);
		q.setParameter("location", location);
		List<AlarmDefinition> alarmDefinitions = q.getResultList();
		if (alarmDefinitions != null && alarmDefinitions.size() > 0) {
			return alarmDefinitions.get(0);
		}

		return null;
	}

	public static AlarmDefinition findNotificationRequiredAlarmsByAlarmNumberAndLocation(Integer number,
			Integer location) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmDefinition o where o.alarmNumber=:alarmNumber and o.location=:location and o.notificationRequired =:notificationRequired",
				AlarmDefinition.class);
		q.setParameter("alarmNumber", number);
		q.setParameter("location", location);
		q.setParameter("notificationRequired", true);
		System.out.println(number + "-" + location + "-" + true);
		List<AlarmDefinition> alarmDefinitions = q.getResultList();
		if (alarmDefinitions != null && alarmDefinitions.size() > 0) {
			return alarmDefinitions.get(0);
		}

		return null;
	}
}
