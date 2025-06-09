package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Configurable
@Entity
@Table(name = "CARRIER_RELEASE_TBX")
public class CarrierRelease {

    @NotNull
    @ManyToOne
    private Stop currentLocation;

    @NotNull
    @ManyToOne
    private Stop destination;

    @NotNull
    @Column(name = "SOURCE")
    private String source;

    @NotNull
    @Column(name = "EVENT_TIMESTAMP")
    private Timestamp requestTimestamp;

	public Stop getCurrentLocation() {
        return this.currentLocation;
    }

	public void setCurrentLocation(Stop currentLocation) {
        this.currentLocation = currentLocation;
    }

	public Stop getDestination() {
        return this.destination;
    }

	public void setDestination(Stop destination) {
        this.destination = destination;
    }

	public String getSource() {
        return this.source;
    }

	public void setSource(String source) {
        this.source = source;
    }

	public Timestamp getRequestTimestamp() {
        return this.requestTimestamp;
    }

	public void setRequestTimestamp(Timestamp requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CurrentLocation: ").append(getCurrentLocation()).append(", ");
        sb.append("Destination: ").append(getDestination()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Source: ").append(getSource()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CARRIER_NUMBER")
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
            CarrierRelease attached = CarrierRelease.findCarrierRelease(this.id);
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
    public CarrierRelease merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CarrierRelease merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new CarrierRelease().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCarrierReleases() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CarrierRelease o", Long.class).getSingleResult();
    }

	public static List<CarrierRelease> findAllCarrierReleases() {
        return entityManager().createQuery("SELECT o FROM CarrierRelease o", CarrierRelease.class).getResultList();
    }

	public static CarrierRelease findCarrierRelease(Long id) {
        if (id == null) return null;
        return entityManager().find(CarrierRelease.class, id);
    }

	public static List<CarrierRelease> findCarrierReleaseEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CarrierRelease o", CarrierRelease.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public static void saveCarrierRelease(Long carrierNumber, Stop currentLocation, Stop destination, String source) {
        CarrierRelease carrierRelease = CarrierRelease.findCarrierRelease(carrierNumber);
        if (carrierRelease == null) {
            carrierRelease = new CarrierRelease();
            carrierRelease.setId(carrierNumber);
            carrierRelease.setCurrentLocation(currentLocation);
            carrierRelease.setDestination(destination);
            carrierRelease.setSource(source);
            carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
            carrierRelease.persist();
        } else {
            carrierRelease.setCurrentLocation(currentLocation);
            carrierRelease.setDestination(destination);
            carrierRelease.setSource(source);
            carrierRelease.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
            carrierRelease.merge();
        }
    }

	public static List<CarrierRelease> findCarrierReleaseEntriesAtStop(Long stopNumber) {
        Query q = entityManager().createQuery("SELECT o FROM CarrierRelease o WHERE o.id IN (SELECT x.carrierNumber FROM CarrierMes x WHERE x.currentLocation = :stopNumber)", CarrierRelease.class);
        q.setParameter("stopNumber", stopNumber);
        return q.getResultList();
    }

	public static List<CarrierRelease> findCarrierReleaseEntriesInStorageArea(List<Long> stopNumbers) {
        Query q = entityManager().createQuery("SELECT o FROM CarrierRelease o WHERE o.id IN (SELECT x.carrierNumber FROM CarrierMes x WHERE x.currentLocation IN (:stopNumbers) AND buffer = 1) order by o.requestTimestamp asc", CarrierRelease.class);
        q.setParameter("stopNumbers", stopNumbers);

        return q.getResultList();
    }

	public static List<CarrierRelease> findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(List<Long> stopNumbers) {
        Query q = entityManager().createQuery("SELECT o FROM CarrierRelease o WHERE o.id IN (SELECT x.carrierNumber FROM CarrierMes x WHERE x.currentLocation IN (:stopNumbers) AND buffer = 1 and  x.currentLocation = x.destination) order by o.requestTimestamp asc", CarrierRelease.class);
        q.setParameter("stopNumbers", stopNumbers);

        return q.getResultList();
    }
}
