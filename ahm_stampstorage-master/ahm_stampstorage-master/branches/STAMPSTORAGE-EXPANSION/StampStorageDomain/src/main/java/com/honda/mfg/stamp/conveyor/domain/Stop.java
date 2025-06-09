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
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

@Entity
@Table(name = "STOP_TBX")
@Configurable
/**
 * User: Jeffrey M Lutz Date: 5/3/11
 */

public class Stop {
	private static final Logger LOG = LoggerFactory.getLogger(Stop.class);

	public Stop() {
		super();
	}

	public Stop(long id) {
		this.setId(id);
	}

	public Stop(String name) {
		this.name = name;
	}

	@NotNull
	@Column(name = "NAME")
	String name;

	@NotNull
	@Column(name = "STOP_TYPE")
	@Enumerated
	StopType stopType;

	@Column(name = "DESCRIPTION")
	String description;

	@NotNull
	@Column(name = "STOP_AREA")
	@Enumerated
	StopArea stopArea;

//    @NotNull
//    @Column(name = "CAPACITY")
//    Integer capacity;
//
//    @NotNull
//    @Column(name = "AVAILABILITY")
//    @Enumerated
//    StopAvailability stopAvailability;

	public boolean equals(Object obj) {
		if (obj instanceof Stop) {

			Long lhsId = getId();
			Long rhsId = ((Stop) obj).getId();
			if (lhsId == null) {
				return false;
			}
			return lhsId.equals(rhsId);
		}

		return false;
	}

	public boolean isRowStop() {
		return this.stopArea.equals(StopArea.ROW);
	}

	public int hashCode() {
		// LOG.info("hashCode() called. hashcode: " + super.hashCode());
		return super.hashCode(); // To change body of overridden methods use File | Settings | File Templates.
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		return sb.toString();
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "STOP_ID")
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
			Stop attached = Stop.findStop(this.id);
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
	public Stop merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Stop merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new Stop().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countStops() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Stop o", Long.class).getSingleResult();
	}

	public static List<Stop> findAllStops() {
		return entityManager().createQuery("SELECT o FROM Stop o order by o.id ", Stop.class).getResultList();
	}

	public static Stop findStop(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Stop.class, id);
	}

	public static List<Stop> findStopEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Stop o order by o.id ", Stop.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<Stop> findAllStopsByType(StopType stopType) {
		Query q = entityManager().createQuery("SELECT o FROM Stop o where o.stopType=:type order by o.id desc",
				Stop.class);
		q.setParameter("type", stopType);
		return q.getResultList();
	}

	public static List<Stop> findAllStopsByArea(StopArea stopArea) {
		Query q = entityManager().createQuery("SELECT o FROM Stop o where o.stopArea=:area", Stop.class);
		q.setParameter("area", stopArea);
		return q.getResultList();
	}

	public static List<Stop> findAllStopsByTypeAndArea(StopType stopType, StopArea stopArea) {
		Query q = entityManager().createQuery(
				"SELECT o FROM Stop o where o.stopType=:type and o.stopArea=:area order by o.id asc", Stop.class);
		q.setParameter("type", stopType);
		q.setParameter("area", stopArea);
		return q.getResultList();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StopType getStopType() {
		return this.stopType;
	}

	public void setStopType(StopType stopType) {
		this.stopType = stopType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StopArea getStopArea() {
		return this.stopArea;
	}

	public void setStopArea(StopArea stopArea) {
		this.stopArea = stopArea;
	}
}
