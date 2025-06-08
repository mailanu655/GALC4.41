package com.honda.galc.dao.jpa.product;

import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.OrderDao;
import com.honda.galc.entity.enumtype.OrderStatus;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.Order;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @date Nov 1, 2012
 */
public class OrderDaoImpl extends BaseDaoImpl<Order, String> implements OrderDao {

	private static final String FIND_ORDER_SQL = "select o from Order o where o.id.orderNo = :orderNo";
	private static final String FIND_UPCOMING_ORDERS_SQL = "select o from Order o where o.orderStatusId = "
			+ OrderStatus.SCHEDULED.getId() + " order by o.prioritySeq";
	private static final String FIND_PLAN_CODE_UPCOMING_ORDERS = "select o from Order o where o.id.planCode = :planCode and o.orderStatusId = "
			+ OrderStatus.SCHEDULED.getId() + " order by o.prioritySeq";
	private static final String FIND_UPCOMING_ORDERS_BY_STATUS_SQL = "select o from Order o where o.orderStatusId = :orderStatusId order by o.prioritySeq";
	private static final String FIND_PLAN_CODE_UPCOMING_ORDERS_BY_STATUS = "select o from Order o where o.id.planCode = :planCode and o.orderStatusId = :orderStatusId order by o.prioritySeq";
	private static final String FIND_CURRENT_PLAN_CODE_ORDER = "select o from Order o where o.id.planCode = :planCode and o.orderStatusId <= "
			+ OrderStatus.CURRENT.getId() + " order by o.orderStatusId desc, o.prioritySeq";
	private static final String FIND_CURRENT_ORDER_SQL = "select o from Order o where o.orderStatusId <= "
			+ OrderStatus.CURRENT.getId() + " order by o.orderStatusId desc, o.prioritySeq";
	private static final String FIND_PROCESSED_ORDERS_SQL = "select o from Order o where o.orderStatusId >= "
			+ OrderStatus.ASSIGNED.getId() + " order by o.prioritySeq";
	private static final String FIND_PLAN_CODE_PROCESSED_ORDERS = "select o from Order o where o.id.planCode = :planCode and o.orderStatusId >= "
			+ OrderStatus.ASSIGNED.getId() + " order by o.prioritySeq";
	private static final String FIND_NEXT_ORDER_BY_LAST_ORDER_AND_PLAN_CODE_SQL = "select o from Order o where (o.orderStatusId = "
			+ OrderStatus.SCHEDULED.getId() + " or o.orderStatusId = " + OrderStatus.SENT.getId() + ") and o.id.planCode = :planCode and o.prioritySeq > "
			+ "(select o2.prioritySeq from Order o2 where o2.id.orderNo = :orderNo and o2.id.planCode = :planCode) order by o.prioritySeq";
	private static final String FIND_NEXT_ORDER_BY_LAST_ORDER_SQL = "select o from Order o where (o.orderStatusId = "
			+ OrderStatus.SCHEDULED.getId() + " or o.orderStatusId = " + OrderStatus.SENT.getId() + ") and o.prioritySeq > "
			+ "(select o2.prioritySeq from Order o2 where o2.id.orderNo = :orderNo) order by o.prioritySeq";

	private static final String GET_ORDER_FILLED_QTY_NATIVE_SQL = "select count(*) from product_priority_plan_tbx p, gal195tbx line where p.order_no = ?1 and p.tracking_status = line.line_id and line.line_id = ?2 and p.plan_status_id >= "
			+ PlanStatus.ASSIGNED.getId();
	private static final String GET_ORDER_FILLED_QTY_BULK_NATIVE_SQL = "select order_no, count(*) from product_priority_plan_tbx where tracking_status = ?1 and plan_status_id >= "
			+ PlanStatus.ASSIGNED.getId() + " group by order_no";

	public Order findByOrderNumber(String orderNumber) {
		Parameters params = Parameters.with("orderNo", orderNumber);
		return findFirstByQuery(FIND_ORDER_SQL, params);
	}

	public List<Order> getProcessedOrders() {
		return findAllByQuery(FIND_PROCESSED_ORDERS_SQL);
	}

	public List<Order> getUpComingOrders() {
		return findAllByQuery(FIND_UPCOMING_ORDERS_SQL);
	}

	public Order getCurrentOrder() {
		return findFirstByQuery(FIND_CURRENT_ORDER_SQL);
	}

	public Order getNextOrder(String planCode, int orderStatusId) {
		if (StringUtils.isBlank(planCode)) {
			return findFirstByQuery(FIND_UPCOMING_ORDERS_BY_STATUS_SQL, Parameters.with("orderStatusId", orderStatusId));
		} else {
			return findFirstByQuery(FIND_PLAN_CODE_UPCOMING_ORDERS_BY_STATUS, Parameters.with("planCode", planCode).put("orderStatusId", orderStatusId));
		}
	}

	public Order getNextOrderByLastOrder(String orderNo, String planCode) {
		if (StringUtils.isBlank(planCode)) {
			return findFirstByQuery(FIND_NEXT_ORDER_BY_LAST_ORDER_SQL, Parameters.with("orderNo", orderNo));
		}else{
			return findFirstByQuery(FIND_NEXT_ORDER_BY_LAST_ORDER_AND_PLAN_CODE_SQL, Parameters.with("orderNo", orderNo).put("planCode", planCode));
		}
	}

	public Integer getOrderFilledQty(String orderNumber, String lineId) {
		Parameters params = Parameters.with("1", orderNumber);
		params.put("2", lineId);
		Object[] objects = findFirstByNativeQuery(GET_ORDER_FILLED_QTY_NATIVE_SQL, params, Object[].class);
		return objects[0] == null ? null : (Integer) objects[0];
	}

	public Hashtable<String, Integer> getOrderFilledQty(String lineId) {
		Parameters params = Parameters.with("1", lineId);
		List<Object[]> list = findAllByNativeQuery(GET_ORDER_FILLED_QTY_BULK_NATIVE_SQL, params, Object[].class);
		Hashtable<String, Integer> resultsMap = new Hashtable<String, Integer>();
		for (Object[] item : list) {
			String key = StringUtils.trimToEmpty((String) item[0]);
			Integer value = (Integer) item[1];
			if (!resultsMap.containsKey(key))
				resultsMap.put(key, value);
		}
		return resultsMap;
	}

	public Order findByOrderNoAndStatusId(String orderNo, OrderStatus status) {
		if (null == orderNo || null == status) {
			return null;
		}
		return super.findFirst(Parameters.with("id.orderNo", orderNo).put("orderStatusId", status.getId()));
	}

	public Order getCurrentOrder(String planCode) {
		if (StringUtils.isEmpty(planCode))
			return findFirstByQuery(FIND_CURRENT_ORDER_SQL);
		else
			return findFirstByQuery(FIND_CURRENT_PLAN_CODE_ORDER, Parameters.with("planCode", planCode));

	}

	public List<Order> getUpComingOrders(String planCode) {
		if (StringUtils.isEmpty(planCode))
			return findAllByQuery(FIND_UPCOMING_ORDERS_SQL);
		else
			return findAllByQuery(FIND_PLAN_CODE_UPCOMING_ORDERS, Parameters.with("planCode", planCode));
	}

	public List<Order> getProcessedOrders(String planCode) {
		if (StringUtils.isEmpty(planCode))
			return findAllByQuery(FIND_PROCESSED_ORDERS_SQL);
		else
			return findAllByQuery(FIND_PLAN_CODE_PROCESSED_ORDERS, Parameters.with("planCode", planCode));
	
	}
	
	public Order findByOrderNoPlanCodeAndProdSpecCode(String orderNo, String planCode, String prodSpecCode){
		if(orderNo == null || planCode == null || prodSpecCode == null){
			return null;
		}
		return super.findFirst(Parameters.with("id.orderNo", orderNo).put("id.planCode", planCode).put("productSpecCode", prodSpecCode));
	}

	public Integer findMaxSequence(String planCode) {
		try {
			return max("prioritySeq", Integer.class, Parameters.with("id.planCode",planCode));
		} catch (Exception e) {
			return 0;
		}
	}

	public Order findByOrderNoAndPlancode(String orderNo, String planCode) {
		if (null == orderNo || null == planCode) {
			return null;
		}
		return super.findFirst(Parameters.with("id.orderNo", orderNo).put("id.planCode",planCode));
	}
}
