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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "ALARM_EVENT_ARCHIVE_TBX")
@Configurable
public class AlarmEventArchive {
	private static final Logger LOG = LoggerFactory.getLogger(AlarmEventArchive.class);

	@NotNull
	@Column(name = "ALARM_EVENT_ID")
	private Long alarmEventId;

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

	@Column(name = "ARCHIVAL_TIMESTAMP")
	private Timestamp archivalTimestamp;

	@Column(name = "ARCHIVED_BY")
	private String archivedBy;

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

	public Timestamp getArchivedTimestamp() {
		return archivalTimestamp;
	}

	public void setArchivedTimestamp(Timestamp archivalTimestamp) {
		this.archivalTimestamp = archivalTimestamp;
	}

	public String getArchivedBy() {
		return archivedBy;
	}

	public void setArchivedBy(String archivedBy) {
		this.archivedBy = archivedBy;
	}

	public Long getAlarmEventId() {
		return alarmEventId;
	}

	public void setAlarmEventId(Long alarmEventId) {
		this.alarmEventId = alarmEventId;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ALARM_EVENT_ARCHIVE_ID")
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

	// @Transactional
//    public void AlarmEventArchive.remove() {
//        if (this.entityManager == null) this.entityManager = entityManager();
//        if (this.entityManager.contains(this)) {
//            this.entityManager.remove(this);
//        } else {
//            AlarmEvent attached = AlarmEventArchive.findCurrent_Alarm(this.id);
//            this.entityManager.remove(attached);
//        }
//    }

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

	// @Transactional
//    public AlarmEventArchive AlarmEventArchive.merge() {
//        if (this.entityManager == null) this.entityManager = entityManager();
//        AlarmEventArchive merged = this.entityManager.merge(this);
//        this.entityManager.flush();
//        return merged;
//    }

	public static EntityManager entityManager() {
		EntityManager em = new AlarmEventArchive().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countArchivedAlarms() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AlarmEventArchive o", Long.class).getSingleResult();
	}

	public static List<AlarmEventArchive> findAllArchivedAlarms() {
		LOG.debug("findAllArchivedAlarms!!!! ");
		return entityManager().createQuery("SELECT o FROM AlarmEventArchive o ORDER BY o.eventTimestamp desc",
				AlarmEventArchive.class).setFirstResult(1).setMaxResults(1000).getResultList();
	}

	public static AlarmEventArchive findArchivedAlarm(Long id) {
		if (id == null)
			return null;
		return entityManager().find(AlarmEventArchive.class, id);
	}

	public static List<AlarmEventArchive> findArchivedAlarmEntries(int firstResult, int maxResults) {
		LOG.debug("findArchivedAlarmEntries - firstResult:" + firstResult + ", maxResults:" + maxResults);
		return entityManager().createQuery("SELECT o FROM AlarmEventArchive o ORDER BY o.eventTimestamp desc",
				AlarmEventArchive.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
