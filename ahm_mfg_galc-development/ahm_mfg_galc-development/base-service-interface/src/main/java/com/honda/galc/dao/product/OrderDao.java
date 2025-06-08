package com.honda.galc.dao.product;

import java.util.Hashtable;
import java.util.List;

import com.honda.galc.entity.enumtype.OrderStatus;
import com.honda.galc.entity.product.Order;
import com.honda.galc.service.IDaoService;

public interface OrderDao extends IDaoService<Order, String> {
	
	public List<Order> getProcessedOrders();
	
	public List<Order> getUpComingOrders();
	
	public Order getCurrentOrder();
	
	public Order getNextOrder(String planCode, int orderStatusId);
		
	public Order getNextOrderByLastOrder(String orderNo, String planCode);
	
	public Order findByOrderNumber(String orderNumber);
	
	public Hashtable<String, Integer> getOrderFilledQty(String lineId);
	
	public Integer getOrderFilledQty(String orderNumber, String lineId);
	
	public Order findByOrderNoAndStatusId(String orderNo, OrderStatus status);

	public Order getCurrentOrder(String planCode);

	public List<Order> getUpComingOrders(String planCode);

	public List<Order> getProcessedOrders(String planCode);
	
	public Order findByOrderNoPlanCodeAndProdSpecCode(String orderNo, String planCode, String prodSpecCode);

	public Integer findMaxSequence(String planCode);
	
	public Order findByOrderNoAndPlancode(String orderNumber, String planCode);
}
