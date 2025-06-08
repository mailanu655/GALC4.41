package com.honda.galc.client.product.view.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.product.event.ProcessEvent;
import com.honda.galc.client.product.event.ProductCancelledEvent;
import com.honda.galc.client.product.event.ProductFinishedEvent;
import com.honda.galc.client.product.event.ProductStartedEvent;
import com.honda.galc.client.product.process.controller.ProcessController.State;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>InfoPanel</code> //TODO just sample panel
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<Map<String, Object>> productPanel;
	private List<Map<String, Object>> products;

	private int productListCapacity;

	public InfoPanel() {
		this.productPanel = createProductPanel();
		this.products = new ArrayList<Map<String, Object>>();
		this.productListCapacity = 50;
		setLayout(new MigLayout("", "[grow,fill]", "grow,fill"));
		add(getProductPanel());
		AnnotationProcessor.process(this);
	}

	protected ObjectTablePane<Map<String, Object>> createProductPanel() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
//		mapping.put("Product Type", "product.productType");
		mapping.put("ProductId", "product.id");
		mapping.put("YMTO", "product.productSpecCode");
		mapping.put("Product Status", "status");
		mapping.put("Start Time", "startTime", formatter);
		mapping.put("Finish Time", "finishTime", formatter);
		mapping.put("Bearing Select Status", "Bearing Select");
		ObjectTablePane<Map<String, Object>> pane = new ObjectTablePane<Map<String, Object>>(mapping.get(), true, true);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pane.getTable().setName("productPanel");
		return pane;
	}

	// === event handlers === //
	@EventSubscriber()
	public void onProductStarted(ProductStartedEvent event) {
		if (event == null) {
			return;
		}
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("product", event.getProduct());
		item.put("status", "In Process");
		item.put("startTime", new Date());
		getProducts().add(0, item);
		getProductPanel().reloadData(getProducts());

		while (getProducts().size() > getProductListCapacity() && getProducts().size() > 0) {
			getProducts().remove(getProducts().size() - 1);
		}
	}

	@EventSubscriber()
	public void onProductFinished(ProductFinishedEvent event) {
		if (event == null) {
			return;
		}
		if (getProducts().isEmpty()) {
			return;
		}
		Map<String, Object> item = getProducts().get(0);
		if (item == null) {
			return;
		}
		item.put("status", "Finished");
		item.put("finishTime", new Date());
		getProductPanel().reloadData(getProducts());
	}

	@EventSubscriber()
	public void onProductCancelled(ProductCancelledEvent event) {
		if (getProducts().isEmpty()) {
			return;
		}
		Map<String, Object> item = getProducts().get(0);
		if (item == null) {
			return;
		}
		item.put("status", "Cancelled");
		item.put("finishTime", new Date());
		getProductPanel().reloadData(getProducts());
	}

	@EventSubscriber()
	public void onProcessEvent(ProcessEvent event) {
		if (getProducts().isEmpty()) {
			return;
		}
		Map<String, Object> item = getProducts().get(0);
		if (item == null) {
			return;
		}
		State state = event.getProcessState();
		if (State.FINISHED.equals(state) || State.ALREADY_PROCESSED.equals(state) || State.NOT_PROCESSABLE.equals(state)) {
			item.put(event.getProcessName(), event.getProcessState());
			getProductPanel().reloadData(getProducts());
		}
	}

	// === get/set === //
	public ObjectTablePane<Map<String, Object>> getProductPanel() {
		return productPanel;
	}

	public List<Map<String, Object>> getProducts() {
		return products;
	}

	protected int getProductListCapacity() {
		return productListCapacity;
	}
}
