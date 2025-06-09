package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;

@Entity
@Table(name = "WELD_ORDER_TBX")
@Configurable
/**
 * 
 * The order for parts which the system should send to Weld.
 *
 */
public class WeldOrder {
	private static final Logger LOG = LoggerFactory.getLogger(WeldOrder.class);

	public WeldOrder() {
		orderSequence = 9999;
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		comments = "None";
		leftDeliveryComments = "None";
		leftFulfillmentComments = "None";
		rightFulfillmentComments = "None";
		rightDeliveryComments = "None";
	}

	@ManyToOne
	@JoinColumn(name="ORDER_MGR", referencedColumnName="ORDER_MGR_ID")
	private OrderMgr orderMgr;

	@NotNull
	@Column(name = "ORDER_SEQUENCE")
	private Integer orderSequence;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="MODEL", referencedColumnName="MODEL_ID")
	private Model model;

	@Column(name = "ORDER_STATUS")
	@Enumerated
	private OrderStatus orderStatus;

	@NotNull
	@Column(name = "LEFT_QTY")
	private Integer leftQuantity;

	@NotNull
	@Column(name = "RIGHT_QTY")
	private Integer rightQuantity;

	@Column(name = "LEFT_DELIVERED_QTY")
	private Integer leftDeliveredQuantity;

	@Column(name = "RIGHT_DELIVERED_QTY")
	private Integer rightDeliveredQuantity;

	@Column(name = "LEFT_CONSUMED_QTY")
	private Integer leftConsumedQuantity;

	@Column(name = "RIGHT_CONSUMED_QTY")
	private Integer rightConsumedQuantity;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "LEFT_DELIVERY_COMMENTS")
	private String leftDeliveryComments;

	@Column(name = "RIGHT_DELIVERY_COMMENTS")
	private String rightDeliveryComments;

	@Column(name = "LEFT_FULFILLMENT_COMMENTS")
	private String leftFulfillmentComments;

	@Column(name = "RIGHT_FULFILLMENT_COMMENTS")
	private String rightFulfillmentComments;

	@Column(name = "DELIVERY_STATUS")
	@Enumerated
	private OrderStatus deliveryStatus;

	@Column(name = "RIGHT_QUEUED_QTY")
	private Integer rightQueuedQty;

	@Column(name = "LEFT_QUEUED_QTY")
	private Integer leftQueuedQty;

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ID")
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
			WeldOrder attached = WeldOrder.findWeldOrder(this.id);
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
	public WeldOrder merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		WeldOrder merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new WeldOrder().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countWeldOrders() {
		return entityManager().createQuery("SELECT COUNT(o) FROM WeldOrder o", Long.class).getSingleResult();
	}

	public static List<WeldOrder> findAllWeldOrders() {
		String sql = "SELECT o FROM WeldOrder o where " + "o.orderStatus = :queuedOrderStatus OR "
				+ "o.orderStatus = :inProcessOrderStatus OR " + "o.orderStatus = :deliveredOrderStatus OR "
				+ "o.orderStatus = :holdOrderStatus OR " + "o.orderStatus = :orderCarriersStatus OR "
				+ "o.orderStatus = :deliveringCarriersStatus " + "order by o.createdDate asc";
		Query q = entityManager().createQuery(sql, WeldOrder.class);
		q.setParameter("queuedOrderStatus", OrderStatus.Queued);
		q.setParameter("inProcessOrderStatus", OrderStatus.InProcess);
		q.setParameter("deliveredOrderStatus", OrderStatus.Delivered);
		q.setParameter("holdOrderStatus", OrderStatus.OnHold);
		q.setParameter("orderCarriersStatus", OrderStatus.RetrievingCarriers);
		q.setParameter("deliveringCarriersStatus", OrderStatus.DeliveringCarriers);
		return q.setMaxResults(1000).getResultList();
	}

	public static List<WeldOrder> findAllWeldOrdersByOrderMgr(OrderMgr orderMgr) {
		String sql = "SELECT o FROM WeldOrder o where  o.orderMgr =:orderMgr and ("
				+ "o.orderStatus = :queuedOrderStatus OR " + "o.orderStatus = :inProcessOrderStatus OR "
				+ "o.orderStatus = :deliveredOrderStatus OR " + "o.orderStatus = :holdOrderStatus OR "
				+ "o.orderStatus = :orderCarriersStatus OR " + "o.orderStatus = :deliveringCarriersStatus ) "
				+ " order by o.createdDate asc";
		Query q = entityManager().createQuery(sql, WeldOrder.class);
		q.setParameter("queuedOrderStatus", OrderStatus.Queued);
		q.setParameter("inProcessOrderStatus", OrderStatus.InProcess);
		q.setParameter("deliveredOrderStatus", OrderStatus.Delivered);
		q.setParameter("holdOrderStatus", OrderStatus.OnHold);
		q.setParameter("orderCarriersStatus", OrderStatus.RetrievingCarriers);
		q.setParameter("deliveringCarriersStatus", OrderStatus.DeliveringCarriers);
		q.setParameter("orderMgr", orderMgr);

		return q.setMaxResults(1000).getResultList();
	}

	public static WeldOrder findWeldOrder(Long id) {
		if (id == null)
			return null;
		return entityManager().find(WeldOrder.class, id);
	}

	public static List<WeldOrder> findWeldOrderEntries(int firstResult, int maxResults) {
		String sql = "SELECT o FROM WeldOrder o where " + "o.orderStatus = :queuedOrderStatus OR "
				+ "o.orderStatus = :inProcessOrderStatus OR " + "o.orderStatus = :deliveredOrderStatus OR "
				+ "o.orderStatus = :holdOrderStatus OR " + "o.orderStatus = :orderCarriersStatus OR "
				+ "o.orderStatus = :deliveringCarriersStatus " + "order by o.createdDate asc";
		Query q = entityManager().createQuery(sql, WeldOrder.class);
		q.setParameter("queuedOrderStatus", OrderStatus.Queued);
		q.setParameter("inProcessOrderStatus", OrderStatus.InProcess);
		q.setParameter("deliveredOrderStatus", OrderStatus.Delivered);
		q.setParameter("holdOrderStatus", OrderStatus.OnHold);
		q.setParameter("orderCarriersStatus", OrderStatus.RetrievingCarriers);
		q.setParameter("deliveringCarriersStatus", OrderStatus.DeliveringCarriers);
		return q.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<WeldOrder> findWeldOrdersByOrderMgrAndOrderStatus(OrderMgr orderMgr, OrderStatus orderStatus) {
		Query q = null;

		if (orderMgr != null && orderStatus != null) {
			LOG.info(orderMgr.toString() + "----" + orderStatus);
			q = entityManager().createQuery(
					"SELECT o FROM WeldOrder o where o.orderMgr =:ordermgr and o.orderStatus =:orderStatus order by id desc",
					WeldOrder.class);
			q.setParameter("ordermgr", orderMgr);
			q.setParameter("orderStatus", orderStatus);
			return q.setMaxResults(1000).getResultList();
		}
		if (orderMgr != null && orderStatus == null) {
			LOG.info(orderMgr.toString());
			q = entityManager().createQuery("SELECT o FROM WeldOrder o where o.orderMgr =:ordermgr order by id desc ",
					WeldOrder.class);
			q.setParameter("ordermgr", orderMgr);
			return q.setMaxResults(1000).getResultList();
		}
		if (orderMgr == null && orderStatus != null) {
			LOG.info(orderStatus.name());
			q = entityManager().createQuery(
					"SELECT o FROM WeldOrder o where o.orderStatus =:orderStatus order by id desc", WeldOrder.class);
			q.setParameter("orderStatus", orderStatus);
			return q.setMaxResults(1000).getResultList();
		}

		return null;
	}

	public static List<WeldOrder> findWeldOrdersByOrderMgrAndDeliveryStatus(OrderMgr orderMgr,
			OrderStatus deliveryStatus) {
		Query q = null;

		if (orderMgr != null && deliveryStatus != null) {
			LOG.info(orderMgr.toString() + "----" + deliveryStatus);
			q = entityManager().createQuery(
					"SELECT o FROM WeldOrder o where o.orderMgr =:ordermgr and o.deliveryStatus =:orderStatus order by id desc",
					WeldOrder.class);
			q.setParameter("ordermgr", orderMgr);
			q.setParameter("orderStatus", deliveryStatus);
			return q.setMaxResults(1000).getResultList();
		}
		if (orderMgr != null && deliveryStatus == null) {
			LOG.info(orderMgr.toString());
			q = entityManager().createQuery("SELECT o FROM WeldOrder o where o.orderMgr =:ordermgr order by id desc ",
					WeldOrder.class);
			q.setParameter("ordermgr", orderMgr);
			return q.setMaxResults(1000).getResultList();
		}
		if (orderMgr == null && deliveryStatus != null) {
			LOG.info(deliveryStatus.name());
			q = entityManager().createQuery(
					"SELECT o FROM WeldOrder o where o.orderStatus =:deliveryStatus order by id desc", WeldOrder.class);
			q.setParameter("orderStatus", deliveryStatus);
			return q.setMaxResults(1000).getResultList();
		}

		return null;
	}

	public static List<WeldOrder> findWeldOrdersByOrderMgrNotNullAndOrderStatusAndDeliveryStatus(OrderMgr orderMgr,
			OrderStatus orderStatus, OrderStatus deliveryStatus) {
		Query q = null;
		String queryString = "";
		if (orderMgr == null)
			throw new IllegalArgumentException("The orderMgr argument is required");

		// q = entityManager().createQuery("SELECT o FROM WeldOrder o where o.orderMgr
		// =:ordermgr and o.orderStatus =:orderStatus order by id desc",
		// WeldOrder.class);

		queryString = "SELECT o FROM WeldOrder o where o.orderMgr =:ordermgr ";
		if (orderStatus != null) {
			LOG.info(orderMgr.toString() + "----" + orderStatus);
			queryString += " and o.orderStatus =:orderStatus ";
		}
		if (deliveryStatus != null) {
			LOG.info(orderMgr.toString());
			queryString += " and o.deliveryStatus =:deliveryStatus ";
		}

		queryString += " order by id desc ";
		q = entityManager().createQuery(queryString, WeldOrder.class);

		q.setParameter("ordermgr", orderMgr);
		if (orderStatus != null) {
			q.setParameter("orderStatus", orderStatus);
		}
		if (deliveryStatus != null) {
			q.setParameter("deliveryStatus", deliveryStatus);
		}
		return q.setMaxResults(1000).getResultList();
	}

	public static WeldOrder findActiveOrderForOrderMgr(OrderMgr orderMgr) {
		WeldOrder activeOrder = null;
		List<WeldOrder> orders = null;
		Query q = null;
		q = entityManager().createQuery(
				"SELECT o FROM WeldOrder o where o.orderMgr =:orderMgr AND (o.orderStatus = :orderStatus OR o.orderStatus = :orderingCarrierOrderStatus)",
				WeldOrder.class);
		q.setParameter("orderMgr", orderMgr);
		q.setParameter("orderStatus", OrderStatus.InProcess);
		q.setParameter("orderingCarrierOrderStatus", OrderStatus.RetrievingCarriers);
		orders = q.getResultList();
		if (orders != null && orders.size() > 0) {
			activeOrder = WeldOrder.findWeldOrder(orders.get(0).getId());
			return activeOrder;
		}

		return null;
	}

	public static WeldOrder findActiveOrderForDeliveryByOrderMgr(OrderMgr orderMgr) {
		WeldOrder activeOrder = null;
		List<WeldOrder> orders = null;
		Query q = null;
		q = entityManager().createQuery(
				"SELECT o FROM WeldOrder o where o.orderMgr =:orderMgr AND (o.deliveryStatus = :orderStatus OR o.deliveryStatus = :deliveringOrderStatus OR o.deliveryStatus = :holdOrderStatus) order by o.id",
				WeldOrder.class);
		q.setParameter("orderMgr", orderMgr);
		q.setParameter("orderStatus", OrderStatus.InProcess);
		q.setParameter("deliveringOrderStatus", OrderStatus.DeliveringCarriers);
		q.setParameter("holdOrderStatus", OrderStatus.OnHold);

		orders = q.getResultList();
		if (orders != null && orders.size() > 0) {
			activeOrder = WeldOrder.findWeldOrder(orders.get(0).getId());
			return activeOrder;
		}

		return null;
	}

	public static List<WeldOrder> findWeldOrdersByOrderStatus(OrderStatus orderStatus) {
		List<WeldOrder> orders = null;
		Query q = null;
		q = entityManager().createQuery("SELECT o FROM WeldOrder o where o.orderStatus = :orderStatus",
				WeldOrder.class);
		q.setParameter("orderStatus", orderStatus);

		orders = q.getResultList();

		return orders;
	}

	public static List<WeldOrder> findWeldOrdersByDeliveryStatus(OrderStatus orderStatus) {
		List<WeldOrder> orders = null;
		Query q = null;
		q = entityManager().createQuery("SELECT o FROM WeldOrder o where o.deliveryStatus = :orderStatus",
				WeldOrder.class);
		q.setParameter("orderStatus", orderStatus);

		orders = q.getResultList();

		return orders;
	}

	public static List<WeldOrder> findWeldOrdersInProcess() {
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		Query q = em.createQuery(
				"SELECT o FROM WeldOrder AS o WHERE o.orderStatus <> :autoCompleted  AND o.orderStatus <> :manuallyCompleted AND o.orderStatus <> :orderCancelled AND o.orderStatus <> :orderInitialized AND o.orderStatus <> :orderQueued",
				WeldOrder.class);
		q.setParameter("autoCompleted", OrderStatus.AutoCompleted);
		q.setParameter("manuallyCompleted", OrderStatus.ManuallyCompleted);
		q.setParameter("orderCancelled", OrderStatus.Cancelled);
		q.setParameter("orderInitialized", OrderStatus.Initialized);
		q.setParameter("orderQueued", OrderStatus.Queued);
		orders = q.getResultList();
		return orders;
	}

	public static List<WeldOrder> findWeldOrdersInProcessByOrderMgr(OrderMgr orderMgr) {
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		Query q = em.createQuery(
				"SELECT o FROM WeldOrder AS o WHERE o.orderMgr = :orderMgr AND (o.orderStatus <> :autoCompleted  AND o.orderStatus <> :manuallyCompleted AND o.orderStatus <> :orderCancelled AND o.orderStatus <> :orderInitialized AND o.orderStatus <> :orderQueued)",
				WeldOrder.class);
		q.setParameter("orderMgr", orderMgr);
		q.setParameter("autoCompleted", OrderStatus.AutoCompleted);
		q.setParameter("manuallyCompleted", OrderStatus.ManuallyCompleted);
		q.setParameter("orderCancelled", OrderStatus.Cancelled);
		q.setParameter("orderInitialized", OrderStatus.Initialized);
		q.setParameter("orderQueued", OrderStatus.Queued);
		orders = q.getResultList();
		return orders;
	}

	public static List<WeldOrder> findWeldOrdersDelivering() {
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		StringBuilder s = new StringBuilder();
		s.append("SELECT o FROM WeldOrder AS o WHERE o.deliveryStatus = :delivering ");
		s.append("OR o.deliveryStatus = :delivered OR o.deliveryStatus = :deliveryInProcess ");
		s.append("OR o.deliveryStatus = :deliveryOnHold ");
		s.append(
				"OR (o.deliveryStatus = :deliveryInitialized AND (o.orderStatus = :orderAutoCompleted OR o.orderStatus = :orderManCompleted  ))");
		Query q = em.createQuery(s.toString(), WeldOrder.class);
		q.setParameter("delivering", OrderStatus.DeliveringCarriers);
		q.setParameter("delivered", OrderStatus.Delivered);
		q.setParameter("deliveryInProcess", OrderStatus.InProcess);
		q.setParameter("deliveryOnHold", OrderStatus.OnHold);
		// Included Initialized & order complete -MG
		q.setParameter("deliveryInitialized", OrderStatus.Initialized);
		q.setParameter("orderAutoCompleted", OrderStatus.AutoCompleted);
		q.setParameter("orderManCompleted", OrderStatus.ManuallyCompleted);
		orders = q.getResultList();
		return orders;
	}

	public static List<WeldOrder> findWeldOrdersDeliveringByOrderMgr(OrderMgr orderMgr) {
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		StringBuilder s = new StringBuilder();
		s.append("SELECT o FROM WeldOrder AS o WHERE o.orderMgr = :orderMgr ");
		s.append(
				"AND (o.deliveryStatus = :delivering OR o.deliveryStatus = :delivered OR o.deliveryStatus = :deliveryInProcess ");
		s.append("OR o.deliveryStatus = :deliveryOnHold ");
		s.append(
				"OR (o.deliveryStatus = :deliveryInitialized AND (o.orderStatus = :orderAutoCompleted OR o.orderStatus = :orderManCompleted )))");
		Query q = em.createQuery(s.toString(), WeldOrder.class);
		q.setParameter("orderMgr", orderMgr);
		q.setParameter("delivering", OrderStatus.DeliveringCarriers);
		q.setParameter("delivered", OrderStatus.Delivered);
		q.setParameter("deliveryInProcess", OrderStatus.InProcess);
		q.setParameter("deliveryOnHold", OrderStatus.OnHold);
		// Included Initialized & order complete -MG
		q.setParameter("deliveryInitialized", OrderStatus.Initialized);
		q.setParameter("orderAutoCompleted", OrderStatus.AutoCompleted);
		q.setParameter("orderManCompleted", OrderStatus.ManuallyCompleted);
		orders = q.getResultList();
		return orders;
	}

	public static List<WeldOrder> findWeldOrdersByDeliveryStatusAndOrderStatus(OrderStatus deliveryStatus,
			OrderStatus orderStatus) {

		if (deliveryStatus == null && orderStatus == null)
			return null;
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		String queryString = "";
		Query q = null;
		// Query q = em.createQuery("SELECT o FROM WeldOrder AS o WHERE o.deliveryStatus
		// = :deliveryStatus AND o.orderStatus = :orderStatus", WeldOrder.class);
		if (orderStatus != null) {
			queryString = "SELECT o FROM WeldOrder AS o WHERE o.orderStatus = :orderStatus";
			if (deliveryStatus != null) {
				queryString += " and o.deliveryStatus =:deliveryStatus ";
			}
		} else { // orderStatus is null, but deliveryStatus is not null
			queryString = "SELECT o FROM WeldOrder AS o WHERE o.deliveryStatus = :deliveryStatus";
		}
		queryString += " order by id desc ";
		q = em.createQuery(queryString, WeldOrder.class);

		if (orderStatus != null) {
			q.setParameter("orderStatus", orderStatus);
		}
		if (deliveryStatus != null) {
			q.setParameter("deliveryStatus", deliveryStatus);
		}
		orders = q.setMaxResults(1000).getResultList();
		;
		return orders;
	}

	public static List<WeldOrder> findWeldOrdersPending() {
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		Query q = em.createQuery(
				"SELECT o FROM WeldOrder AS o WHERE o.orderStatus = :orderInitialized OR o.orderStatus = :orderQueued",
				WeldOrder.class);
		q.setParameter("orderInitialized", OrderStatus.Initialized);
		q.setParameter("orderQueued", OrderStatus.Queued);
		orders = q.getResultList();
		return orders;
	}

	public static List<WeldOrder> findWeldOrdersPendingByOrderMgr(OrderMgr orderMgr) {
		EntityManager em = WeldOrder.entityManager();
		List<WeldOrder> orders = null;
		Query q = em.createQuery(
				"SELECT o FROM WeldOrder AS o WHERE o.orderMgr = :orderMgr AND (o.orderStatus = :orderInitialized OR o.orderStatus = :orderQueued)",
				WeldOrder.class);
		q.setParameter("orderMgr", orderMgr);
		q.setParameter("orderInitialized", OrderStatus.Initialized);
		q.setParameter("orderQueued", OrderStatus.Queued);
		orders = q.getResultList();
		return orders;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("WeldOrder.id: ").append(getId()).append(", \n");
		sb.append("WeldOrder.orderMgr: ").append(getOrderMgr()).append(", \n");
		sb.append("WeldOrder.orderSequence: ").append(getOrderSequence()).append(", \n");
		sb.append("WeldOrder.model: \n[\n").append(getModel()).append("\n], \n");
		sb.append("WeldOrder.orderStatus: ").append(getOrderStatus()).append(", \n");
		sb.append("WeldOrder.deliveryStatus: ").append(getDeliveryStatus()).append(", \n");
		sb.append("WeldOrder.leftQuantity: ").append(getLeftQuantity()).append(", \n");
		sb.append("WeldOrder.rightQuantity: ").append(getRightQuantity()).append(", \n");
		sb.append("WeldOrder.Version: ").append(getVersion());
		return sb.toString();
	}

	public OrderMgr getOrderMgr() {
		return this.orderMgr;
	}

	public void setOrderMgr(OrderMgr orderMgr) {
		this.orderMgr = orderMgr;
	}

	public Integer getOrderSequence() {
		return this.orderSequence;
	}

	public void setOrderSequence(Integer orderSequence) {
		this.orderSequence = orderSequence;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public OrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getLeftQuantity() {
		return this.leftQuantity;
	}

	public void setLeftQuantity(Integer qty) {
		this.leftQuantity = qty;
	}

	public Integer getRightQuantity() {
		return this.rightQuantity;
	}

	public void setRightQuantity(Integer qty) {
		this.rightQuantity = qty;
	}

	// delivered qty
	public Integer getLeftDeliveredQuantity() {
		if (this.leftDeliveredQuantity == null) {
			return new Integer(0);
		}
		return this.leftDeliveredQuantity;
	}

	public void setLeftDeliveredQuantity(Integer qty) {
		this.leftDeliveredQuantity = qty;
	}

	public Integer getRightDeliveredQuantity() {
		if (this.rightDeliveredQuantity == null) {
			return new Integer(0);
		}
		return this.rightDeliveredQuantity;
	}

	public void setRightDeliveredQuantity(Integer qty) {
		this.rightDeliveredQuantity = qty;
	}

	// consumed qty
	public Integer getLeftConsumedQuantity() {
		if (this.leftConsumedQuantity == null) {
			return new Integer(0);
		}
		return this.leftConsumedQuantity;
	}

	public void setLeftConsumedQuantity(Integer qty) {
		this.leftConsumedQuantity = qty;
	}

	public Integer getRightConsumedQuantity() {
		if (this.rightConsumedQuantity == null) {
			return new Integer(0);
		}
		return this.rightConsumedQuantity;
	}

	public void setRightConsumedQuantity(Integer qty) {
		this.rightConsumedQuantity = qty;
	}

	public Integer getRemainingLeftQuantity() {
		if (this.getLeftDeliveredQuantity() == null) {
			return new Integer(0);
		}
		return this.leftQuantity - this.getLeftDeliveredQuantity();
	}

	public Integer getRemainingRightQuantity() {
		if (this.getRightDeliveredQuantity() == null) {
			return new Integer(0);
		}
		return this.rightQuantity - this.getRightDeliveredQuantity();
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getComments() {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (comments == null || comments.equals("") || comments.trim().equals("")) {
			return "None";
		} else
			return comments;
	}

	public void setComments(String comments) {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (comments == null || comments.equals("") || comments.trim().equals("")) {
			this.comments = "None";
		} else
			this.comments = comments;
	}

	public OrderStatus getDeliveryStatus() {
		return this.deliveryStatus;
	}

	public void setDeliveryStatus(OrderStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Integer getRightQueuedQty() {
		return this.rightQueuedQty;
	}

	public void setRightQueuedQty(Integer rightQueuedQty) {
		this.rightQueuedQty = rightQueuedQty;
	}

	public Integer getLeftQueuedQty() {
		return this.leftQueuedQty;
	}

	public void setLeftQueuedQty(Integer leftQueuedQty) {
		this.leftQueuedQty = leftQueuedQty;
	}

	/**
	 * check if order status equals the given status
	 * 
	 * @param checkStatus
	 * @return boolean
	 */
	public boolean isStatus(OrderStatus checkStatus) {
		return (checkStatus != null && this.getOrderStatus() == checkStatus);
	}

	/**
	 * determine if this is an order for only left dies
	 * 
	 * @return boolean
	 */
	public boolean isLeftOnly() {
		return this.getRightQuantity() == 0;
	}

	/**
	 * determine if this is an order for only right dies
	 * 
	 * @return boolean
	 */
	public boolean isRightOnly() {
		return this.getLeftQuantity() == 0;
	}

	/**
	 * get left die for this order
	 * 
	 * @return {@link Die}
	 */
	public Die getLeftDie() {
		Die die = null;
		if (this.getModel() != null)
			die = this.model.getLeftDie();
		return die;
	}

	/**
	 * get right die for this order
	 * 
	 * @return {@link Die}
	 */
	public Die getRightDie() {
		Die die = null;
		if (this.getModel() != null)
			die = this.model.getRightDie();
		return die;
	}

	/**
	 * check if the given die matches the left die for the order
	 * 
	 * @param {@link Die} thisDie
	 * @return boolean
	 */
	public boolean isLeftDie(Die thisDie) {
		Die leftDie = this.getLeftDie();
		return leftDie != null && leftDie.equals(thisDie);
	}

	/**
	 * check if the given die matches the right die for the order
	 * 
	 * @param {@link Die} thisDie
	 * @return boolean
	 */
	public boolean isRightDie(Die thisDie) {
		Die rDie = this.getRightDie();
		return rDie != null && rDie.equals(thisDie);
	}

	/**
	 * get the left queue stop for the associated order manager (weld line)
	 * 
	 * @return stop
	 */
	public Stop getLeftQueueStop() {
		Stop qStop = null;
		if (this.orderMgr != null) {
			qStop = this.orderMgr.getLeftQueueStop();
		}
		return qStop;
	}

	/**
	 * get the right queue stop for the associated order manager (weld line)
	 * 
	 * @return stop
	 */
	public Stop getRightQueueStop() {
		Stop qStop = null;
		if (this.orderMgr != null) {
			qStop = this.orderMgr.getRightQueueStop();
		}
		return qStop;
	}

	/**
	 * determine if this is order is in process
	 * 
	 * @return boolean
	 */
	public boolean isOrderInProcess() {
		return orderStatus == OrderStatus.InProcess || orderStatus == OrderStatus.OnHold
				|| orderStatus == OrderStatus.SelectingCarriers || orderStatus == OrderStatus.RetrievingCarriers;
	}

	/**
	 * determine if this is delivery is in process
	 * 
	 * @return boolean
	 */
	public boolean isDeliveryInProcess() {
		return deliveryStatus == OrderStatus.InProcess || deliveryStatus == OrderStatus.OnHold
				|| deliveryStatus == OrderStatus.DeliveringCarriers || deliveryStatus == OrderStatus.Delivered;
	}

	/**
	 * determine if this is a pending order
	 * 
	 * @return boolean
	 */
	public boolean isPending() {
		return orderStatus == OrderStatus.Initialized || orderStatus == OrderStatus.Queued;
	}

	public String getLeftDeliveryComments() {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (leftDeliveryComments == null || leftDeliveryComments.equals("") || leftDeliveryComments.trim().equals("")) {
			return "None";
		} else
			return leftDeliveryComments;
	}

	public void setLeftDeliveryComments(String leftDeliveryComments) {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (leftDeliveryComments == null || leftDeliveryComments.equals("") || leftDeliveryComments.trim().equals("")) {
			this.leftDeliveryComments = "None";
		} else
			this.leftDeliveryComments = leftDeliveryComments;
	}

	public String getRightDeliveryComments() {
		if (rightDeliveryComments == null || rightDeliveryComments.equals("")
				|| rightDeliveryComments.trim().equals("")) {
			// mg 11/9/14 set comments to "None" to resolve display issues with empty
			// strings.
			return "None";
		} else
			return rightDeliveryComments;
	}

	public void setRightDeliveryComments(String rightDeliveryComments) {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (rightDeliveryComments == null || rightDeliveryComments.equals("")
				|| rightDeliveryComments.trim().equals("")) {
			this.rightDeliveryComments = "None";
		} else
			this.rightDeliveryComments = rightDeliveryComments;
	}

	public String getLeftFulfillmentComments() {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (leftFulfillmentComments == null || leftFulfillmentComments.equals("")
				|| leftFulfillmentComments.trim().equals("")) {
			return "None";
		} else
			return leftFulfillmentComments;
	}

	public void setLeftFulfillmentComments(String leftFulfillmentComments) {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (leftFulfillmentComments == null || leftFulfillmentComments.equals("")
				|| leftFulfillmentComments.trim().equals("")) {
			this.leftFulfillmentComments = "None";
		} else
			this.leftFulfillmentComments = leftFulfillmentComments;
	}

	public String getRightFulfillmentComments() {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (rightFulfillmentComments == null || rightFulfillmentComments.equals("")
				|| rightFulfillmentComments.trim().equals("")) {
			return "None";
		} else
			return rightFulfillmentComments;
	}

	public void setRightFulfillmentComments(String rightFulfillmentComments) {
		// mg 11/9/14 set comments to "None" to resolve display issues with empty
		// strings.
		if (rightFulfillmentComments == null || rightFulfillmentComments.equals("")
				|| rightFulfillmentComments.trim().equals("")) {
			this.rightFulfillmentComments = "None";
		} else
			this.rightFulfillmentComments = rightFulfillmentComments;
	}
}
