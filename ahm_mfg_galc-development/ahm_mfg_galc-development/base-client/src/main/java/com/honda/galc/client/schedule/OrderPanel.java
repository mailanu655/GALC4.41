/**
 * 
 */
package com.honda.galc.client.schedule;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Order;
import com.honda.galc.service.tracking.MbpnProductHelper;

/**
 * @author Subu Kathiresan
 * @date Jan 15, 2013
 */
public class OrderPanel extends JPanel implements EventSubscriber<SchedulingEvent> {

	private static final long serialVersionUID = 6177561231537336426L;
	
	private static final String PROCESSED_ORDERS = "Processed Orders";
	
	private String _panelName = "";
	private ProcessPoint _processPoint;
	private OrderTableModel _orderTableModel;
	private TablePane orderTablePane;
	
	
	public OrderPanel(String panelName, ProcessPoint processPoint) {
		super();
		_panelName = panelName;
		_processPoint = processPoint;
		EventBus.subscribe(SchedulingEvent.class, this);
		initializePanel();
	}

	protected void initializePanel() {
		orderTablePane = new TablePane(getPanelName());
		orderTablePane.setPreferredHeight(250);
		
		setOrderTableModel(new OrderTableModel(getUpdatedOrders(), orderTablePane.getTable()));
		getOrderTableModel().pack();
		add(orderTablePane);
	}


	public void onEvent(SchedulingEvent event) {
		if (event.getEventType() == SchedulingEventType.UPDATE_VIEW){
			getOrderTableModel().refresh(getUpdatedOrders());
		}
	}

	private List<Order> getUpdatedOrders() {
		List<Order> orders =  new ArrayList<Order>();
		if (getPanelName().equalsIgnoreCase(PROCESSED_ORDERS)) {
			orders = MbpnProductHelper.getProcessedOrders(_processPoint.getProcessPointId());
		}
		return orders;
	}
	
	private String getPanelName() {
		return _panelName;
	}
	
	private OrderTableModel getOrderTableModel() {
		return _orderTableModel;
	}
	
	private void setOrderTableModel(OrderTableModel orderTableModel) {
		_orderTableModel = orderTableModel;
	}
}

