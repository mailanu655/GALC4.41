package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "ORDER_MGR_TBX")
@Configurable
public class OrderMgr {

	@NotNull
	@Column(name = "LINE_NAME")
	private String lineName;

	@OneToOne
	@JoinColumn(name="DELIVERY_STOP", referencedColumnName="STOP_ID")
	private Stop deliveryStop;

	@OneToOne
	@JoinColumn(name="LEFT_CONSUMPTION_STOP", referencedColumnName="STOP_ID")
	private Stop leftConsumptionStop;

	@OneToOne
	@JoinColumn(name="RIGHT_CONSUMPTION_STOP", referencedColumnName="STOP_ID")
	private Stop rightConsumptionStop;

	@OneToOne
	@JoinColumn(name="LEFT_CONSUMPTION_EXIT", referencedColumnName="STOP_ID")
	private Stop leftConsumptionExit;

	@OneToOne
	@JoinColumn(name="RIGHT_CONSUMPTION_EXIT", referencedColumnName="STOP_ID")
	private Stop rightConsumptionExit;

	@NotNull
	@Column(name = "MAX_DELIVERY_CAPACITY")
	private Integer maxDeliveryCapacity;

	@OneToOne
	@JoinColumn(name="LEFT_QUEUE_STOP", referencedColumnName="STOP_ID")
	private Stop leftQueueStop;

	@OneToOne
	@JoinColumn(name="RIGHT_QUEUE_STOP", referencedColumnName="STOP_ID")
	private Stop rightQueueStop;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof OrderMgr) {
			OrderMgr lhs = this;
			OrderMgr rhs = (OrderMgr) obj;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_MGR_ID")
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
			OrderMgr attached = OrderMgr.findOrderMgr(this.id);
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
	public OrderMgr merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		OrderMgr merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new OrderMgr().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countOrderMgrs() {
		return entityManager().createQuery("SELECT COUNT(o) FROM OrderMgr o", Long.class).getSingleResult();
	}

	public static List<OrderMgr> findAllOrderMgrs() {
		return entityManager().createQuery("SELECT o FROM OrderMgr o", OrderMgr.class).getResultList();
	}

	public static OrderMgr findOrderMgr(Long id) {
		if (id == null)
			return null;
		OrderMgr orderMgr = entityManager().find(OrderMgr.class, id);
		return orderMgr;
	}

	public static List<OrderMgr> findOrderMgrEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM OrderMgr o", OrderMgr.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLineName());
		return sb.toString();
	}

	public String getLineName() {
		return this.lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Stop getDeliveryStop() {
		return this.deliveryStop;
	}

	public void setDeliveryStop(Stop deliveryStop) {
		this.deliveryStop = deliveryStop;
	}

	public Stop getLeftConsumptionStop() {
		return this.leftConsumptionStop;
	}

	public void setLeftConsumptionStop(Stop leftConsumptionStop) {
		this.leftConsumptionStop = leftConsumptionStop;
	}

	public Integer getMaxDeliveryCapacity() {
		return this.maxDeliveryCapacity;
	}

	public void setMaxDeliveryCapacity(Integer maxDeliveryCapacity) {
		this.maxDeliveryCapacity = maxDeliveryCapacity;
	}

	public Stop getRightConsumptionStop() {
		return this.rightConsumptionStop;
	}

	public void setRightConsumptionStop(Stop rightConsumptionStop) {
		this.rightConsumptionStop = rightConsumptionStop;
	}

	public Stop getLeftConsumptionExit() {
		return this.leftConsumptionExit;
	}

	public void setLeftConsumptionExit(Stop leftConsumptionExit) {
		this.leftConsumptionExit = leftConsumptionExit;
	}

	public Stop getRightConsumptionExit() {
		return this.rightConsumptionExit;
	}

	public void setRightConsumptionExit(Stop rightConsumptionExit) {
		this.rightConsumptionExit = rightConsumptionExit;
	}

	public Stop getLeftQueueStop() {
		return this.leftQueueStop;
	}

	public void setLeftQueueStop(Stop leftQueueStop) {
		this.leftQueueStop = leftQueueStop;
	}

	public Stop getRightQueueStop() {
		return this.rightQueueStop;
	}

	public void setRightQueueStop(Stop rightQueueStop) {
		this.rightQueueStop = rightQueueStop;
	}
}
