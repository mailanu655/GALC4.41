package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.constraints.NotNull;
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

@Configurable
@Entity
@Table(name = "WELD_SCHEDULE_TBX")
public class WeldSchedule {

    @NotNull
    @Column(name = "WELDLINE")
    private Integer weldLine;

    @NotNull
    @Column(name = "LHPRODPLAN")
    private Integer leftHandProdPlan;

    @NotNull
    @Column(name = "LHPRODREMAIN")
    private Integer leftHandProdRemaining;

    @NotNull
    @Column(name = "RHPRODPLAN")
    private Integer rightHandProdPlan;

    @NotNull
    @Column(name = "RHPRODREMAIN")
    private Integer rightHandProdRemaining;

    @NotNull
    @Column(name = "CURRENTMODEL")
    private Integer currentModel;

    @NotNull
    @Column(name = "NEXTMODEL")
    private Integer nextModel;

    @NotNull
    @Column(name = "NEXTQUANTITY")
    private Integer nextQuantity;



	public Integer getWeldLine() {
        return weldLine;
    }

	public void setWeldLine(Integer weldLine) {
        this.weldLine = weldLine;
    }

	public Integer getLeftHandProdPlan() {
        return leftHandProdPlan;
    }

	public void setLeftHandProdPlan(Integer leftHandProdPlan) {
        this.leftHandProdPlan = leftHandProdPlan;
    }

	public Integer getLeftHandProdRemaining() {
        return leftHandProdRemaining;
    }

	public void setLeftHandProdRemaining(Integer leftHandProdRemaining) {
        this.leftHandProdRemaining = leftHandProdRemaining;
    }

	public Integer getRightHandProdPlan() {
        return rightHandProdPlan;
    }

	public void setRightHandProdPlan(Integer rightHandProdPlan) {
        this.rightHandProdPlan = rightHandProdPlan;
    }

	public Integer getRightHandProdRemaining() {
        return rightHandProdRemaining;
    }

	public void setRightHandProdRemaining(Integer rightHandProdRemaining) {
        this.rightHandProdRemaining = rightHandProdRemaining;
    }

	public Integer getCurrentModel() {
        return currentModel;
    }

	public void setCurrentModel(Integer currentModel) {
        this.currentModel = currentModel;
    }

	public Integer getNextModel() {
        return nextModel;
    }

	public void setNextModel(Integer nextModel) {
        this.nextModel = nextModel;
    }

	public Integer getNextQuantity() {
        return nextQuantity;
    }

	public void setNextQuantity(Integer nextQuantity) {
        this.nextQuantity = nextQuantity;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("WeldLine: ").append(getWeldLine()).append(", ");
        sb.append("LeftHandProdRemaining: ").append(getLeftHandProdRemaining()).append(", ");
        sb.append("LeftHandProdPlan: ").append(getLeftHandProdPlan()).append(", ");
        sb.append("RightHandProdRemaining: ").append(getRightHandProdRemaining()).append(", ");
        sb.append("RightHandProdPlan: ").append(getRightHandProdPlan()).append(", ");
        sb.append("CurrentModel: ").append(getCurrentModel()).append(", ");
        sb.append("NextModel: ").append(getNextModel()).append(", ");
        sb.append("NextQuantity: ").append(getNextQuantity()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SCHEDULE_ID")
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
            WeldSchedule attached = WeldSchedule.findWeldSchedule(this.id);
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
    public WeldSchedule merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        WeldSchedule merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new WeldSchedule().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countWeldSchedules() {
        return entityManager().createQuery("SELECT COUNT(o) FROM WeldSchedule o", Long.class).getSingleResult();
    }

	public static List<WeldSchedule> findAllWeldSchedules() {
        return entityManager().createQuery("SELECT o FROM WeldSchedule o", WeldSchedule.class).getResultList();
    }

	public static WeldSchedule findWeldSchedule(Long id) {
        if (id == null) return null;
        return entityManager().find(WeldSchedule.class, id);
    }

	public static List<WeldSchedule> findWeldScheduleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM WeldSchedule o", WeldSchedule.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
