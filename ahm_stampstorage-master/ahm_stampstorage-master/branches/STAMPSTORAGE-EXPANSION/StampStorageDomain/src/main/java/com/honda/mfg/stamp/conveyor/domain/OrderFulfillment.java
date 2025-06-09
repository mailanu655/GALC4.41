package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;

@Entity
@Table(name = "ORDER_FULFILLMENT_TBX")
@Configurable
public class OrderFulfillment {

	@Column(name = "QUANTITY")
	private Integer quantity;

	@ManyToOne
	@JoinColumn(name="DIE", referencedColumnName="DIE_ID")
	private Die die;

	@ManyToOne
	@JoinColumn(name="CURRENT_LOCATION", referencedColumnName="STOP_ID")
	private Stop currentLocation;

	@ManyToOne
	@JoinColumn(name="DESTINATION", referencedColumnName="STOP_ID")
	private Stop destination;

	@Column(name = "PRODUCTION_RUN_NO")
	private Integer productionRunNo;

	// @Column(name = "RELEASE_CYCLE")
	// private Integer releaseCycle;

	@Column(name = "CARRIER_FULFILLMENT_STATUS")
	private CarrierFulfillmentStatus carrierFulfillmentStatus;

	@Column(name = "UPDATE_DATE")
	private Timestamp updateDate;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// sb.append("DieNumber: ").append(getDie()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Quantity: ").append(getQuantity()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@EmbeddedId
	private OrderFulfillmentPk id;

	@Version
	@Column(name = "version")
	private Integer version;

	public OrderFulfillmentPk getId() {
		return this.id;
	}

	public void setId(OrderFulfillmentPk id) {
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
			OrderFulfillment attached = OrderFulfillment.findOrderFulfillment(this.id);
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
	public OrderFulfillment merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		OrderFulfillment merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new OrderFulfillment().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countOrderFulfillments() {
		return entityManager().createQuery("SELECT COUNT(o) FROM OrderFulfillment o", Long.class).getSingleResult();
	}

	public static List<OrderFulfillment> findAllOrderFulfillments() {
		return entityManager().createQuery("SELECT o FROM OrderFulfillment o", OrderFulfillment.class).getResultList();
	}

	public static OrderFulfillment findOrderFulfillment(OrderFulfillmentPk id) {
		if (id == null)
			return null;
		OrderFulfillment fulfillment = entityManager().find(OrderFulfillment.class, id);
		return fulfillment;
	}

	public static List<OrderFulfillment> findOrderFulfillmentEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM OrderFulfillment o", OrderFulfillment.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<OrderFulfillment> findAllOrderFulfillmentsByOrder(WeldOrder weldOrder) {
		Query query = null;
		String sql = "SELECT o FROM OrderFulfillment o where o.id.weldOrder =:weldOrder order by o.id.releaseCycle";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("weldOrder", weldOrder);
		List<OrderFulfillment> fulfillments = query.getResultList();
		return fulfillments;
	}

	/**
	 * find the total number of fulfillment rows for a given order for all cycles
	 * 
	 * @param weldOrder
	 * @return {@link Integer}
	 */
	public static Integer countAllOrderFulfillmentsByOrder(WeldOrder weldOrder) {
		Long nRows = 0L;
		Query query = null;
		String sql = "SELECT COUNT(*) FROM OrderFulfillment o where o.id.weldOrder =:weldOrder";
		query = entityManager().createQuery(sql, Integer.class);
		query.setParameter("weldOrder", weldOrder);
		Object result = query.getSingleResult();
		if (result != null) {
			nRows = (Long) result;
		}
		return nRows.intValue();
	}

	public static WeldOrder findOrderByCarrier(Carrier carrier) {
		Query query = null;
		WeldOrder order = null;
		List<OrderFulfillment> fulfillments = new ArrayList<OrderFulfillment>();
		String sql = "SELECT o FROM OrderFulfillment o where o.id.carrierNumber=:carrierNumber order by updateDate desc";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("carrierNumber", carrier.getCarrierNumber());
		fulfillments = query.getResultList();

		if (fulfillments != null && fulfillments.size() > 0) {

			for (OrderFulfillment fulfillment : fulfillments) {
				if (!fulfillment.getCarrierFulfillmentStatus().equals(CarrierFulfillmentStatus.CONSUMED)
						&& carrier.getDie().equals(fulfillment.getDie())) {
					order = fulfillment.getId().getWeldOrder();
					if (order.getModel().getLeftDie().equals(carrier.getDie())
							|| order.getModel().getRightDie().equals(carrier.getDie())) {
						break;
					}
				}
			}

		}

		return order;
	}

	public static WeldOrder findOrderByCarrierForFulfillment(Integer carrier, Die die) {
		WeldOrder order = null;
		List<OrderFulfillment> fulfillments = new ArrayList<OrderFulfillment>();
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.RETRIEVED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.RELEASED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.DELIVERED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.QUEUED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.SELECTED_TO_DELIVER));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.READY_TO_DELIVER));

		if (fulfillments.size() > 0) {

			for (OrderFulfillment fulfillment : fulfillments) {
				if (fulfillment != null && die.equals(fulfillment.getDie())) {
					WeldOrder tempOrder = fulfillment.getId().getWeldOrder();
					if (!tempOrder.getOrderStatus().equals(OrderStatus.AutoCompleted)
							&& !tempOrder.getOrderStatus().equals(OrderStatus.ManuallyCompleted)) {
						if (tempOrder.getModel().getLeftDie().equals(die)
								|| tempOrder.getModel().getRightDie().equals(die)) {
							order = tempOrder;
							break;
						}
					}
				}
			}
		}
		return order;
	}

	public static WeldOrder findFulfillingOrderByCarrierForFulfillment(Integer carrier, Die die) {
		WeldOrder order = null;
		List<OrderFulfillment> fulfillments = new ArrayList<OrderFulfillment>();
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.RETRIEVED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.RELEASED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.QUEUED));

		if (fulfillments.size() > 0) {

			for (OrderFulfillment fulfillment : fulfillments) {
				if (fulfillment != null && die.equals(fulfillment.getDie())) {
					WeldOrder tempOrder = fulfillment.getId().getWeldOrder();
					if (!tempOrder.getOrderStatus().equals(OrderStatus.ManuallyCompleted)) {
						if (tempOrder.getModel().getLeftDie().equals(die)
								|| tempOrder.getModel().getRightDie().equals(die)) {
							order = tempOrder;
							break;
						}
					}
				}
			}
		}
		return order;
	}

	public static WeldOrder findDeliveringOrderByCarrierForFulfillment(Integer carrier, Die die) {
		WeldOrder order = null;
		List<OrderFulfillment> fulfillments = new ArrayList<OrderFulfillment>();
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.READY_TO_DELIVER));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.SELECTED_TO_DELIVER));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.DELIVERED));

		if (fulfillments.size() > 0) {

			for (OrderFulfillment fulfillment : fulfillments) {
				if (fulfillment != null && die.equals(fulfillment.getDie())) {
					WeldOrder tempOrder = fulfillment.getId().getWeldOrder();
					if (tempOrder.getDeliveryStatus().equals(OrderStatus.InProcess)
							|| tempOrder.getDeliveryStatus().equals(OrderStatus.DeliveringCarriers)) {
						if (tempOrder.getModel().getLeftDie().equals(die)
								|| tempOrder.getModel().getRightDie().equals(die)) {
							order = tempOrder;
							break;
						}
					}
				}
			}
		}
		return order;
	}

	public static Integer getMaxCycleCountForOrder(WeldOrder weldOrder) {
		Query query = null;
		List<OrderFulfillment> fulfillments = findAllOrderFulfillmentsByOrder(weldOrder);
		if (fulfillments != null && fulfillments.size() > 0) {
			String sql = "SELECT MAX(o.id.releaseCycle) FROM OrderFulfillment o where o.id.weldOrder=:weldOrder";
			query = entityManager().createQuery(sql, Integer.class);
			query.setParameter("weldOrder", weldOrder);
			Object result = query.getSingleResult();
			if (result != null) {
				Integer fulfillmentCycle = (Integer) result;
				return fulfillmentCycle;
			}

		}
		return 0;
	}

	public static Integer getMaxCycleCountForOrderByCarrier(WeldOrder weldOrder, Integer carrier) {
		Query query = null;
		List<OrderFulfillment> fulfillments = findAllOrderFulfillmentsByOrder(weldOrder);
		if (fulfillments != null && fulfillments.size() > 0) {
			String sql = "SELECT MAX(o.id.releaseCycle) FROM OrderFulfillment o where o.id.weldOrder=:weldOrder and o.id.carrierNumber=:carrierNumber";
			query = entityManager().createQuery(sql, Integer.class);
			query.setParameter("weldOrder", weldOrder);
			query.setParameter("carrierNumber", carrier);
			Object result = query.getSingleResult();
			if (result != null) {
				Integer fulfillmentCycle = (Integer) result;
				return fulfillmentCycle;
			}

		}
		return 0;
	}

	public static Integer getMaxCycleCountForOrderForDie(WeldOrder weldOrder, Die die) {
		Query query = null;
		List<OrderFulfillment> fulfillments = findAllOrderFulfillmentsByOrder(weldOrder);
		if (fulfillments != null && fulfillments.size() > 0) {
			String sql = "SELECT MAX(o.id.releaseCycle) FROM OrderFulfillment o where o.id.weldOrder=:weldOrder and o.die=:die";
			query = entityManager().createQuery(sql, Integer.class);
			query.setParameter("weldOrder", weldOrder);
			query.setParameter("die", die);
			Object result = query.getSingleResult();
			if (result != null) {
				Integer fulfillmentCycle = (Integer) result;
				return fulfillmentCycle;
			}

		}
		return 0;
	}

	public static Integer getMinCycleCountForOrderForFulfillmentStatus(WeldOrder order,
			CarrierFulfillmentStatus retrieved) {
		Query query = null;
		List<OrderFulfillment> fulfillments = findAllOrderFulfillmentsByOrder(order);
		if (fulfillments != null && fulfillments.size() > 0) {
			String sql = "SELECT MIN(o.id.releaseCycle) FROM OrderFulfillment o where o.id.weldOrder=:weldOrder AND o.carrierFulfillmentStatus=:carrierFulfillmentStatus";
			query = entityManager().createQuery(sql, Integer.class);
			query.setParameter("weldOrder", order);
			query.setParameter("carrierFulfillmentStatus", retrieved);

			Object result = query.getSingleResult();
			if (result != null) {
				Integer fulfillmentCycle = (Integer) result;
				return fulfillmentCycle;
			}

		}
		return 0;
	}

	/**
	 * find the number of fulfillment rows for a given order cycle
	 * 
	 * @param weldOrder
	 * @param cycle
	 * @return {@link Integer}
	 */
	public static Integer countFulfillmentsForCycle(WeldOrder weldOrder, Integer cycle) {
		Query query = null;
		Long nRows = 0L;
		String sql = "SELECT COUNT(*) FROM OrderFulfillment o where o.id.weldOrder=:weldOrder and o.id.releaseCycle=:cycle";
		query = entityManager().createQuery(sql, Long.class);
		query.setParameter("weldOrder", weldOrder);
		query.setParameter("cycle", cycle);
		Object result = query.getSingleResult();
		if (result != null) {
			nRows = (Long) result;
		}
		return nRows.intValue();
	}

	public static Integer countFulfillmentsByOrderWithCarrierFulfillmentStatusAndDie(WeldOrder order, Die die,
			CarrierFulfillmentStatus carrierFulfillmentStatus) {
		Query query = null;
		Long nRows = 0L;
		String sql = "SELECT COUNT(*) FROM OrderFulfillment o where o.id.weldOrder=:weldOrder and o.die=:die and o.carrierFulfillmentStatus=:carrierFulfillmentStatus";
		query = entityManager().createQuery(sql, Long.class);
		query.setParameter("weldOrder", order);
		query.setParameter("die", die);
		query.setParameter("carrierFulfillmentStatus", carrierFulfillmentStatus);
		Object result = query.getSingleResult();
		if (result != null) {
			nRows = (Long) result;
		}
		return nRows.intValue();
	}

	public static List<OrderFulfillment> findAllOrderFulfillmentsByOrderForCycle(WeldOrder order, Integer cycle) {

		Query query = null;
		String sql = "SELECT o FROM OrderFulfillment o where o.id.weldOrder =:weldOrder and o.id.releaseCycle=:releaseCycle";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("weldOrder", order);
		query.setParameter("releaseCycle", cycle);
		List<OrderFulfillment> fulfillments = query.getResultList();
		return fulfillments;
	}

	public static List<OrderFulfillment> findAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(WeldOrder order,
			CarrierFulfillmentStatus carrierFulfillmentStatus) {
		Query query = null;
		List<OrderFulfillment> fulfillments = findAllOrderFulfillmentsByOrder(order);
		if (fulfillments != null && fulfillments.size() > 0) {
			String sql = "SELECT o FROM OrderFulfillment o where o.id.weldOrder=:weldOrder AND o.carrierFulfillmentStatus=:carrierFulfillmentStatus";
			query = entityManager().createQuery(sql, OrderFulfillment.class);
			query.setParameter("weldOrder", order);
			query.setParameter("carrierFulfillmentStatus", carrierFulfillmentStatus);
			List<OrderFulfillment> selectedFulfillments = query.getResultList();
			return selectedFulfillments;
		}
		return null;
	}

	@Transactional
	public static void deleteAllOrderFulfillmentsByOrderWithCarrierFulfillmentStatus(WeldOrder order,
			CarrierFulfillmentStatus carrierFulfillmentStatus) {

		List<OrderFulfillment> fulfillments = findAllOrderFulfillmentsByOrder(order);
		if (fulfillments != null && fulfillments.size() > 0) {
//            String sql = "DELETE FROM OrderFulfillment o where o.id.weldOrder=:weldOrder AND o.carrierFulfillmentStatus=:carrierFulfillmentStatus";
//            query = entityManager().createQuery(sql);
//            query.setParameter("weldOrder", order);
//            query.setParameter("carrierFulfillmentStatus", carrierFulfillmentStatus);
//            int result = query.executeUpdate();

			for (OrderFulfillment fulfillment : fulfillments) {

				if (fulfillment.getCarrierFulfillmentStatus().equals(carrierFulfillmentStatus)) {
					fulfillment.remove();
				}
			}
		}
	}

	public static List<OrderFulfillment> findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(
			Integer carrierNumber, CarrierFulfillmentStatus carrierFulfillmentStatus) {
		Query query = null;

		String sql = "SELECT o FROM OrderFulfillment o where o.id.carrierNumber=:carrierNumber AND o.carrierFulfillmentStatus=:carrierFulfillmentStatus order by o.id.weldOrder desc";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("carrierNumber", carrierNumber);
		query.setParameter("carrierFulfillmentStatus", carrierFulfillmentStatus);
		List<OrderFulfillment> selectedFulfillments = query.getResultList();
		return selectedFulfillments;
	}

	public static List<OrderFulfillment> findAllNotConsumedOrderFulfillmentsByCarrier(Carrier carrier) {
		Query query = null;

		String sql = "SELECT o FROM OrderFulfillment o where o.id.carrierNumber=:carrierNumber  AND o.carrierFulfillmentStatus <> :carrierFulfillmentStatus";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("carrierNumber", carrier.getCarrierNumber());
		query.setParameter("carrierFulfillmentStatus", CarrierFulfillmentStatus.CONSUMED);
		List<OrderFulfillment> selectedFulfillments = query.getResultList();
		return selectedFulfillments;
	}

	public static List<OrderFulfillment> findAllOrderFulfillmentsWithCurrentLocation(Stop currentLoc) {
		Query query = null;
		if (currentLoc == null)
			return null;
		String sql = "SELECT o FROM OrderFulfillment o, CarrierMes m where o.currentLocation=:queueStop AND o.id.carrierNumber=m.carrierNumber order by m.updateDate asc";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("queueStop", currentLoc);
		List<OrderFulfillment> selectedFulfillments = query.getResultList();
		return selectedFulfillments;
	}

	public static List<OrderFulfillment> findAllOrderFulfillmentsByCarrierNotDeliveryCompleted(Integer carrierNumber) {
		Query query = null;

		String sql = "SELECT o FROM OrderFulfillment o, WeldOrder w where o.id.carrierNumber=:carrierNumber AND o.id.weldOrder.id=w.id and w.orderStatus <> :deliveryStatus1 and w.orderStatus <> :deliveryStatus2";
		query = entityManager().createQuery(sql, OrderFulfillment.class);
		query.setParameter("carrierNumber", carrierNumber);
		query.setParameter("deliveryStatus1", OrderStatus.AutoCompleted);
		query.setParameter("deliveryStatus2", OrderStatus.ManuallyCompleted);

		List<OrderFulfillment> selectedFulfillments = query.getResultList();
		return selectedFulfillments;
	}

	public static Integer countFulfillmentsByCarrierNotDeliveryCompleted(Integer carrierNumber) {
		Query query = null;
		Long nRows = 0L;
		String sql = "SELECT count(*) FROM OrderFulfillment o, WeldOrder w where o.id.carrierNumber=:carrierNumber AND o.id.weldOrder.id=w.id and w.deliveryStatus <> :deliveryStatus1 and w.deliveryStatus <> :deliveryStatus2";
		query = entityManager().createQuery(sql, Long.class);
		query.setParameter("carrierNumber", carrierNumber);
		query.setParameter("deliveryStatus1", OrderStatus.AutoCompleted);
		query.setParameter("deliveryStatus2", OrderStatus.ManuallyCompleted);

		Object result = query.getSingleResult();
		if (result != null) {
			nRows = (Long) result;
		}
		return nRows.intValue();
	}

	public static WeldOrder findOrderByCarrierInRows(Integer carrier) {
		WeldOrder order = null;
		List<OrderFulfillment> fulfillments = new ArrayList<OrderFulfillment>();
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.RETRIEVED));
		fulfillments.addAll(findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(carrier,
				CarrierFulfillmentStatus.SELECTED));

		if (fulfillments.size() > 0) {

			for (OrderFulfillment fulfillment : fulfillments) {
				if (fulfillment != null) {
					WeldOrder tempOrder = fulfillment.getId().getWeldOrder();
					if (!tempOrder.getOrderStatus().equals(OrderStatus.ManuallyCompleted)) {
						order = tempOrder;
						break;
					}
				}
			}
		}
		return order;

	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Die getDie() {
		return this.die;
	}

	public void setDie(Die die) {
		this.die = die;
	}

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

	public Integer getProductionRunNo() {
		return this.productionRunNo;
	}

	public void seProductionRunNo(Integer productionRunNo) {
		this.productionRunNo = productionRunNo;
	}

	// public Integer OrderFulfillment.getReleaseCycle() {
//        return releaseCycle;
//    }
//
//    public void OrderFulfillment.setReleaseCycle(Integer releaseCycle) {
//        this.releaseCycle = releaseCycle;
//    }

	public CarrierFulfillmentStatus getCarrierFulfillmentStatus() {
		return carrierFulfillmentStatus;
	}

	public void setCarrierFulfillmentStatus(CarrierFulfillmentStatus carrierFulfillmentStatus) {
		this.carrierFulfillmentStatus = carrierFulfillmentStatus;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * determine if the fulfillment status of this fulfillment record is equal to
	 * given status
	 * 
	 * @param checkStatus
	 * @return boolean true or false
	 */
	public boolean isStatus(CarrierFulfillmentStatus checkStatus) {
		return this.carrierFulfillmentStatus != null && checkStatus == this.carrierFulfillmentStatus;
	}
}
