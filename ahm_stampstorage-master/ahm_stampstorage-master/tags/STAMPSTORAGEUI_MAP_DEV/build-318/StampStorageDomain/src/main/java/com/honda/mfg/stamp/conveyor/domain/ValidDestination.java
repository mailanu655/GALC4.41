package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "VALID_DESTINATION_TBX")
@Configurable
/**
 * 
 * List of Destinations to validate data.
 *
 */
public class ValidDestination {

    @NotNull
    @ManyToOne
    private Stop stop;

    @NotNull
    @ManyToOne
    private Stop destination;

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Destination: ").append(getDestination()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Stop: ").append(getStop()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

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
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ValidDestination attached = ValidDestination.findValidDestination(this.id);
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
    public ValidDestination merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ValidDestination merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ValidDestination().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countValidDestinations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ValidDestination o", Long.class).getSingleResult();
    }

	public static List<ValidDestination> findAllValidDestinations() {
        return entityManager().createQuery("SELECT o FROM ValidDestination o", ValidDestination.class).getResultList();
    }

	public static ValidDestination findValidDestination(Long id) {
        if (id == null) return null;
        return entityManager().find(ValidDestination.class, id);
    }

	public static List<ValidDestination> findValidDestinationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ValidDestination o", ValidDestination.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	//GSA 20130708
    public static List<ValidDestination> findValidDestinationForGivenStopAndDestination(Stop stop, Stop destination) {
	    String sql = "SELECT o FROM ValidDestination o where o.stop = :stop and o.destination = :destination ";
	    Query q = null;
	    q = entityManager().createQuery(sql, ValidDestination.class);
	    q.setParameter("stop", stop);
	    q.setParameter("destination", destination);
	    return q.setMaxResults(1000).getResultList();		
    }

	//GSA 20130708
    public static List<Stop> findValidDestinationsForGivenStop(Stop stop) {
	    String sql = "SELECT o.destination FROM ValidDestination o where " +
	            "o.stop = :stop ";
	    Query q = entityManager().createQuery(sql, Stop.class);
	    q.setParameter("stop", stop);
	    return q.setMaxResults(1000).getResultList();		
    }

	public Stop getStop() {
        return this.stop;
    }

	public void setStop(Stop stop) {
        this.stop = stop;
    }

	public Stop getDestination() {
        return this.destination;
    }

	public void setDestination(Stop destination) {
        this.destination = destination;
    }
}
