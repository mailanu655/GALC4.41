package com.honda.mfg.stamp.conveyor.domain;

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
@Table(name = "PRESS_ACTIVITY_TBX")
public class PressActivity {

	@NotNull
	@Column(name = "PRESS_NAME")
	private String pressName;

	@NotNull
	@Column(name = "PRODUCTIONRUNNUMBER")
	private Integer prodRunNumber;

	@NotNull
	@Column(name = "DIENUMBER")
	private Integer dieNumber;

	@NotNull
	@Column(name = "QUANTITY_PRODUCED")
	private Integer quantityProduced;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DieNumber: ").append(getDieNumber()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("PressName: ").append(getPressName()).append(", ");
		sb.append("ProdRunNumber: ").append(getProdRunNumber()).append(", ");
		sb.append("QuantityProduced: ").append(getQuantityProduced()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}

	public String getPressName() {
		return this.pressName;
	}

	public void setPressName(String pressName) {
		this.pressName = pressName;
	}

	public Integer getProdRunNumber() {
		return this.prodRunNumber;
	}

	public void setProdRunNumber(Integer prodRunNumber) {
		this.prodRunNumber = prodRunNumber;
	}

	public Integer getDieNumber() {
		return this.dieNumber;
	}

	public void setDieNumber(Integer dieNumber) {
		this.dieNumber = dieNumber;
	}

	public Integer getQuantityProduced() {
		return this.quantityProduced;
	}

	public void setQuantityProduced(Integer quantityProduced) {
		this.quantityProduced = quantityProduced;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PRESS_ACTIVITY_ID")
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
			PressActivity attached = PressActivity.findPressActivity(this.id);
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
	public PressActivity merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PressActivity merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new PressActivity().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countPressActivitys() {
		return entityManager().createQuery("SELECT COUNT(o) FROM PressActivity o", Long.class).getSingleResult();
	}

	public static List<PressActivity> findAllPressActivitys() {
		return entityManager().createQuery("SELECT o FROM PressActivity o", PressActivity.class).getResultList();
	}

	public static PressActivity findPressActivity(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PressActivity.class, id);
	}

	public static List<PressActivity> findPressActivityEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM PressActivity o", PressActivity.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static PressActivity findPressActivityByPressName(String pressName) {
		Query q = entityManager().createQuery("SELECT o FROM PressActivity o WHERE o.pressName =:pressName",
				PressActivity.class);
		q.setParameter("pressName", pressName);

		List<PressActivity> pressActivities = q.getResultList();

		if (pressActivities.size() > 0) {
			return pressActivities.get(0);
		}

		return null;
	}
}
