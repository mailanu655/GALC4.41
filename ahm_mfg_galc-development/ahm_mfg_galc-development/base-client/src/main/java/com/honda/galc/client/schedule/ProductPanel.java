/**
 * 
 */
package com.honda.galc.client.schedule;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.service.tracking.MbpnProductHelper;

/**
 * @author Subu Kathiresan
 * @date Jan 15, 2013
 */
public class ProductPanel extends JPanel implements EventSubscriber<SchedulingEvent> {

	private static final long serialVersionUID = -5196931976493406911L;
	private static final int MAX_RESULTS = 500;
	
	private static final String PROCESSED_PRODUCTS = "Processed Products";
	private static final String UPCOMING_PRODUCTS = "Upcoming Products";
	
	private String _panelName = "";
	private ProcessPoint _processPoint;
	private ProductTableModel _productTableModel;
	TablePane productTablePane;
	
	public ProductPanel(String panelName, PlanStatus planStatus, ProcessPoint processPoint) {
		super();
		_panelName = panelName;
		_processPoint = processPoint;
		EventBus.subscribe(SchedulingEvent.class, this);
		initializePanel();
	}
	
	protected void initializePanel() {
		productTablePane = new TablePane(getPanelName());
		productTablePane.setPreferredHeight(250);
		
		setProductTableModel(new ProductTableModel(populateProducts(), productTablePane.getTable()));
		getProductTableModel().pack();
		add(productTablePane);
		decorateCurrentProduct();
	}
	
	private void decorateCurrentProduct() {
		if (getPanelName().equalsIgnoreCase(UPCOMING_PRODUCTS) && productTablePane.getTable().getRowCount() > 0) {
			productTablePane.getTable().setRowSelectionInterval(0, 0);
			productTablePane.getTable().setSelectionBackground(Color.YELLOW);
		}
	}
	
	private List<ProductPriorityPlan> populateProducts() {
		List<ProductPriorityPlan> products = new ArrayList<ProductPriorityPlan>();
		if (getPanelName().equalsIgnoreCase(UPCOMING_PRODUCTS)){
			
			products = MbpnProductHelper.getProductsByPlanStatus(_processPoint.getProcessPointId(), PlanStatus.SCHEDULED, MAX_RESULTS);
		} else if (getPanelName().equalsIgnoreCase(PROCESSED_PRODUCTS)){
			products = MbpnProductHelper.getProductsByPlanStatus(_processPoint.getProcessPointId(), PlanStatus.ASSIGNED, MAX_RESULTS);
		}
		return products;
	}
	
	public void onEvent(SchedulingEvent event) {
		if (event.getEventType() == SchedulingEventType.UPDATE_VIEW){
			getProductTableModel().refresh(populateProducts());
		}
	}
	
	private String getPanelName() {
		return _panelName;
	}
	
	private ProductTableModel getProductTableModel() {
		return _productTableModel;
	}

	private void setProductTableModel(ProductTableModel productTableModel) {
		_productTableModel = productTableModel;
	}
}
