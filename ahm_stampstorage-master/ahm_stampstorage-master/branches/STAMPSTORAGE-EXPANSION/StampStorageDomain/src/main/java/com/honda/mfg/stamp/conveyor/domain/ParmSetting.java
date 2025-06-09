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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "PARM_SETTING_TBX")
public class ParmSetting {

	@NotNull
	@Column(name = "FIELD_NAME")
	private String fieldname;

	@NotNull

	@Column(name = "FIELD_VALUE")
	private String fieldvalue;

	@NotNull
	@Column(name = "DESCRIPTION")
	private String description;

	@NotNull
	@Column(name = "UPDATED_BY")
	private String updatedby;

	@NotNull
	@Column(name = "UPDATE_TSTP")
	private Timestamp updatetstp;

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
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
			ParmSetting attached = ParmSetting.findParmSetting(this.id);
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
	public ParmSetting merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ParmSetting merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new ParmSetting().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countParmSettings() {
		return entityManager().createQuery("SELECT COUNT(o) FROM ParmSetting o", Long.class).getSingleResult();
	}

	public static List<ParmSetting> findAllParmSettings() {
		return entityManager().createQuery("SELECT o FROM ParmSetting o", ParmSetting.class).getResultList();
	}

	public static ParmSetting findParmSetting(Long id) {
		if (id == null)
			return null;
		return entityManager().find(ParmSetting.class, id);
	}

	public static List<ParmSetting> findParmSettingEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM ParmSetting o", ParmSetting.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static ParmSetting findParmSettingsByFieldName(String fieldName) {
		Query q = entityManager().createQuery("SELECT o FROM ParmSetting o WHERE o.fieldname =:name",
				ParmSetting.class);
		q.setParameter("name", fieldName);
		return (ParmSetting) q.getSingleResult();
	}

	public String getFieldname() {
		return this.fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldvalue() {
		return this.fieldvalue;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public Timestamp getUpdatetstp() {
		return this.updatetstp;
	}

	public void setUpdatetstp(Timestamp updatetstp) {
		this.updatetstp = updatetstp;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: ").append(getDescription()).append(", ");
		sb.append("Fieldname: ").append(getFieldname()).append(", ");
		sb.append("Fieldvalue: ").append(getFieldvalue()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Updatedby: ").append(getUpdatedby()).append(", ");
		sb.append("Updatetstp: ").append(getUpdatetstp()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}
}
