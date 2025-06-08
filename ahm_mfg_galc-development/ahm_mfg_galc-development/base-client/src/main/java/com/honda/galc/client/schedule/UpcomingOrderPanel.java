/**
 * 
 */
package com.honda.galc.client.schedule;

import java.awt.Color;

import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.tracking.MbpnProductHelper;

/**
 * @author Subu Kathiresan
 * @date Jan 29, 2013
 */
public class UpcomingOrderPanel extends JPanel implements EventSubscriber<SchedulingEvent> {
	
	private static final long serialVersionUID = 3069117003875767647L;
	
	private String _panelName = "";
	private UpcomingOrderTableModel _upcomingOrderTableModel;
	private TablePane orderTablePane;
	private ProcessPoint _processPoint;
	
	public UpcomingOrderPanel(String panelName, ProcessPoint processPoint) {
		super();
		_panelName = panelName;
		_processPoint = processPoint;
		EventBus.subscribe(SchedulingEvent.class, this);
		initializePanel();
	}
	
	protected void initializePanel() {
		orderTablePane = new TablePane(getPanelName());
		orderTablePane.setPreferredHeight(300);
		orderTablePane.setPreferredWidth(935);
		
		setOrderTableModel(new UpcomingOrderTableModel(MbpnProductHelper.getUpComingOrders(_processPoint.getProcessPointId()), orderTablePane.getTable(), getLineId(), _processPoint.getId()));
		getOrderTableModel().pack();
		add(orderTablePane);
		decorateCurrentOrder();
	}

	private void decorateCurrentOrder() {
		orderTablePane.getTable().setRowSelectionInterval(0, 0);
		orderTablePane.getTable().setSelectionBackground(Color.YELLOW);
	}

	public void onEvent(SchedulingEvent event) {
		if (event.getEventType() == SchedulingEventType.UPDATE_VIEW){
			getOrderTableModel().refresh(MbpnProductHelper.getUpComingOrders(_processPoint.getProcessPointId()));
			decorateCurrentOrder();
		}
	}


	private String getPanelName() {
		return _panelName;
	}
	
	private String getLineId() {
		return _processPoint.getLineId();
	}
	
	private UpcomingOrderTableModel getOrderTableModel() {
		return _upcomingOrderTableModel;
	}
	
	private void setOrderTableModel(UpcomingOrderTableModel orderTableModel) {
		_upcomingOrderTableModel = orderTableModel;
	}
}


