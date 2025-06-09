package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "ALARM_EVENT_TBX")
@Configurable
public class AlarmEvent {
	private static final Logger LOG = LoggerFactory.getLogger(AlarmEvent.class);
	@NotNull
	@Column(name = "LOCATION")
	private Integer location;

	@NotNull
	@Column(name = "ALARM_NUMBER")
	private Integer alarmNumber;

	// @NotNull
	@Column(name = "EVENT_TIMESTAMP")
	private Timestamp eventTimestamp;

	@Column(name = "CLEARED")
	private Boolean cleared;

	@Column(name = "CLEARED_BY")
	private String clearedBy;

	@Column(name = "CLEARED_TIMESTAMP")
	private Timestamp clearedTimestamp;

	@Column(name = "NOTIFIED")
	private Boolean notified;

	public Integer getLocation() {
		return this.location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	public Integer getAlarmNumber() {
		return this.alarmNumber;
	}

	public void setAlarmNumber(Integer alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	public Timestamp getEventTimestamp() {
		return this.eventTimestamp;
	}

	public void setEventTimestamp(Timestamp eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public Boolean getCleared() {
		return cleared;
	}

	public void setCleared(Boolean cleared) {
		this.cleared = cleared;
	}

	public String getClearedBy() {
		return clearedBy;
	}

	public void setClearedBy(String clearedBy) {
		this.clearedBy = clearedBy;
	}

	public Timestamp getClearedTimestamp() {
		return clearedTimestamp;
	}

	public void setClearedTimestamp(Timestamp clearedTimestamp) {
		this.clearedTimestamp = clearedTimestamp;
	}

	public Boolean getNotified() {
		return notified;
	}

	public void setNotified(Boolean notified) {
		this.notified = notified;
	}

	public Stop getLocationStop() {
		Stop stop = Stop.findStop(Long.parseLong(location.toString()));

		if (stop != null) {
			return stop;
		} else {
			stop = new Stop();
			stop.setName(location.toString());
			return stop;
		}

	}

	public AlarmDefinition getAlarm() {
		AlarmDefinition definition = AlarmDefinition.findAlarmByAlarmNumberAndLocation(this.alarmNumber, this.location);

		if (definition != null) {
			return definition;
		} else {
			definition = new AlarmDefinition();
			definition.setDescription(alarmNumber.toString());
			return definition;
		}
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ALARM_EVENT_ID")
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
			AlarmEvent attached = AlarmEvent.findCurrent_Alarm(this.id);
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
	public AlarmEvent merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		AlarmEvent merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new AlarmEvent().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countCurrent_Alarms() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AlarmEvent o", Long.class).getSingleResult();
	}

	public static List<AlarmEvent> findAllCurrent_Alarms() {
		LOG.debug("findAllCurrent_Alarms!!!! ");
		return entityManager()
				.createQuery("SELECT o FROM AlarmEvent o ORDER BY o.eventTimestamp desc", AlarmEvent.class)
				.setFirstResult(1).setMaxResults(1000).getResultList();
	}

	public static AlarmEvent findCurrent_Alarm(Long id) {
		if (id == null)
			return null;
		return entityManager().find(AlarmEvent.class, id);
	}

	public static List<AlarmEvent> findCurrent_AlarmEntries(int firstResult, int maxResults) {
		LOG.debug("findCurrent_AlarmEntries - firstResult:" + firstResult + ", maxResults:" + maxResults);
		return entityManager()
				.createQuery("SELECT o FROM AlarmEvent o ORDER BY o.eventTimestamp desc", AlarmEvent.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<AlarmEvent> findCurrentUnClearedAlarms(int maxResults) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmEvent o WHERE o.cleared =:cleared OR o.cleared = null ORDER BY o.eventTimestamp asc",
				AlarmEvent.class);
		q.setParameter("cleared", false);
		return q.setMaxResults(maxResults).getResultList();
	}

	public static List<AlarmEvent> findCurrentUnClearedAlarmsToNotify(int maxResults) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmEvent o WHERE (o.cleared =:cleared OR o.cleared = null) AND (o.notified =:notified OR o.notified = null) ORDER BY o.eventTimestamp asc",
				AlarmEvent.class);
		q.setParameter("cleared", false);
		q.setParameter("notified", false);

		return q.setMaxResults(maxResults).getResultList();
	}

	public static List<AlarmEvent> findCurrentUnClearedAlarmsForAppNotify(int maxResults) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmEvent o WHERE (o.cleared =:cleared OR o.cleared = null) AND o.alarmNumber in (SELECT ad.alarmNumber FROM AlarmDefinition ad WHERE ad.notificationRequired=:notificationReq) ORDER BY o.eventTimestamp asc",
				AlarmEvent.class);
		q.setParameter("cleared", false);

		q.setParameter("notificationReq", true);
		return q.setMaxResults(maxResults).getResultList();
	}

	public static List<AlarmEvent> findCurrentUnClearedAlarmsForQpcNotify(int maxResults) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmEvent o WHERE (o.cleared =:cleared OR o.cleared = null) AND o.alarmNumber in (SELECT ad.alarmNumber FROM AlarmDefinition ad WHERE ad.qpcNotificationRequired=:qpcNotificationReq) ORDER BY o.eventTimestamp asc",
				AlarmEvent.class);
		q.setParameter("cleared", false);

		q.setParameter("qpcNotificationReq", true);
		return q.setMaxResults(maxResults).getResultList();
	}

	public static List<AlarmEvent> findCurrentUnClearedAlarmsByType(int type, int maxResults) {
		Query q = entityManager().createQuery(
				"SELECT o FROM AlarmEvent o WHERE (o.cleared =:cleared OR o.cleared = null) AND o.alarmNumber in (SELECT ad.alarmNumber FROM AlarmDefinition ad WHERE ad.alarmNumber=:whichAlarm) ORDER BY o.eventTimestamp asc",
				AlarmEvent.class);
		q.setParameter("cleared", false);
		q.setParameter("whichAlarm", type);
		return q.setMaxResults(maxResults).getResultList();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AlarmNumber: ").append(getAlarmNumber()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Location: ").append(getLocation()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}
}
