package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;

@Entity
@Table(name = "DIE_TBX")
@Configurable
public class Die {
	private static final Logger LOG = LoggerFactory.getLogger(Die.class);

//    @NotNull
//    @Column(name = "DIE_NUMBER")
//    private Integer dieNumber;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PART_PRODUCTION_VOLUME")
	@Enumerated
	private PartProductionVolume partProductionVolume;

	@Column(name = "BPM_PART_NUMBER")
	private String bpmPartNumber;

	@Column(name = "IMAGE_FILE_NAME")
	private String imageFileName;

	@Column(name = "ACTIVE")
	private Boolean active;

	@Column(name = "TEXT_COLOR")
	private String textColor;

	@Column(name = "BACKGROUND_COLOR")
	private String backgroundColor;

	public Die() {
	}

	public Die(Long dieNumber, PartProductionVolume volume) {

		if (dieNumber != null) {
			this.setId(dieNumber);
		} else {
			this.setId(0L);
		}
		// this.dieNumber = dieNumber;
		this.partProductionVolume = volume;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Die) {
			Die lhs = this;
			Die rhs = (Die) obj;
			if (lhs.getId() == null) {
				return false;
			}
			return lhs.getId().equals(rhs.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return 0;
		} else
			return this.getId().hashCode();
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DIE_ID")
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
			Die attached = Die.findDie(this.id);
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
	public Die merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Die merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new Die().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countDies() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Die o", Long.class).getSingleResult();
	}

	public static List<Die> findAllDies() {
		return entityManager().createQuery("SELECT o FROM Die o order by o.id", Die.class).getResultList();
	}

	public static List<Die> findActiveDies() {
		Boolean active = true;
		Query q = entityManager().createQuery("SELECT o FROM Die o where o.active=:active or o.active=null", Die.class);
		q.setParameter("active", true);
		return q.getResultList();
	}

	public static Die findDie(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Die.class, id);
	}

	public static List<Die> findDieEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Die o", Die.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	// public Integer Die.getDieNumber() {
//        return this.dieNumber;
//    }
//
//    public void Die.setDieNumber(Integer dieNumber) {
//        this.dieNumber = dieNumber;
//    }

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PartProductionVolume getPartProductionVolume() {
		return this.partProductionVolume;
	}

	public void setPartProductionVolume(PartProductionVolume partProductionVolume) {
		this.partProductionVolume = partProductionVolume;
	}

	public String getBpmPartNumber() {
		return this.bpmPartNumber;
	}

	public void setBpmPartNumber(String bpmPartNumber) {
		this.bpmPartNumber = bpmPartNumber;
	}

	public String getImageFileName() {
		return this.imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public Boolean getActive() {
		return this.active == null ? true : active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getTextColor() {
		return this.textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getBackgroundColor() {
		return this.backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// sb.append("Description: ").append(getDescription()).append(", ");
		// sb.append("DieNumber: ").append(getDieNumber()).append(", ");
		// sb.append("Id: ").append(getId()).append(", ");
		// sb.append("Version: ").append(getVersion());

		sb.append(getId()).append("-").append(getDescription());
		return sb.toString();
	}
}
