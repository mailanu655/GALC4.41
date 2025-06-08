package com.honda.galc.client.teamleader.mcshipping;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>ProductShippingManifestController Class description</h3>
 * <p> ProductShippingManifestController description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou<br>
 * Jul. 27, 2022
 */
public class ProductShippingManifestController 
         extends AbstractController<ProductShippingManifestModel, ProductShippingManifestView> 
         implements ActionListener, ListSelectionListener {

	public ProductShippingManifestController(ProductShippingManifestModel model, ProductShippingManifestView view) {
		super(model, view);
	}

	@Override
	public void activate() {
		//getView().mapActions();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;

		clearMessage();
		if(e.getSource().equals(getView().dunnageList.getTable().getSelectionModel())) {
			if(getView().dunnageList.getTable().getSelectedRowCount() > 0) {
				dunnageSelected();
			}
		} else if(e.getSource().equals(getView().productList.getTable().getSelectionModel())) {
			getView().sendButton.setEnabled(true);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		clearMessage();
		try{
			if(e.getSource().equals(getView().destinationComboBox.getComponent()))
				loadTrailers();
			else if(e.getSource().equals(getView().trailerComboBox.getComponent()))
				trailerNumberSelected();
			else if(e.getSource().equals(getView().getjDatePicker()))
				datePicked();
			else if(e.getSource().equals(getView().sendAllButton))
				doSendDunnages();
			else if(e.getSource().equals(getView().sendButton))
				doSendProducts();
				
				
			
		}catch(Exception ex) {
			ex.printStackTrace();
			showAndLogErrorMessage("Exception occured : " + ex.getMessage());
		}
		
	}

	private void doSendProducts() {
		List<ProductShipping> selectedItems = getView().productList.getSelectedItems();
		if(selectedItems == null || selectedItems.size() ==0)
			selectedItems = getView().productList.getItems();
		
		broadcastDunnage(selectedItems);
		
		getView().sendButton.setEnabled(false);
		String message = "" +selectedItems.size() + " products in dunnage:" + getView().dunnageList.getSelectedItem().getDunnage() + " was sent!";
		getLogger().info(message);
		MessageDialog.showInfo(view.getMainWindow(), message, "Send Selected Products");
		
		getView().productList.getTable().clearSelection();
		getView().dunnageList.getTable().clearSelection();
	}

	private void broadcastDunnage(List<ProductShipping> selectedItems) {
		for(ProductShipping ps : selectedItems) {
			doBroadcast(ps);
		}
	}

	private void doSendDunnages() {
		List<ProductShipping> selectedItems = getView().dunnageList.getItems();
		
		for(ProductShipping ps : selectedItems) {
			Logger.getLogger().info("sending Manifest data for dunnage:" + ps.getDunnage());
			List<ProductShipping> products = getModel().getAllByDunnage(ps.getId().getTrailerNumber(), ps.getDunnage(), ps.getShipDate());
			broadcastDunnage(products);
		}
		
		getView().sendAllButton.setEnabled(false);
		String msg = "" + selectedItems.size() + " dunnages in trailer:" + getView().trailerComboBox.getComponent().getSelectedItem() + " was sent!";
		getLogger().info(msg);
		MessageDialog.showInfo(view.getMainWindow(), msg, "Send All In Trailer");
		getView().dunnageList.getTable().clearSelection();
		getView().productList.getTable().clearSelection();
		
		
	}

	private void dunnageSelected() {
		ProductShipping productShipping = getView().dunnageList.getSelectedItem();
		if(productShipping == null) return;
		
		Logger.getLogger().info("Dunnage:", productShipping.getDunnage(), " was selected.");
		
		getModel().getAllByDunnage(productShipping.getId().getTrailerNumber(),productShipping.getDunnage(), productShipping.getShipDate());
		
		getView().productList.reloadData(getModel().getProductList());
		getView().productList.clearSelection();
		getView().productList.getTable().clearSelection();
		getView().sendButton.setEnabled(true);
		}

	public void loadTrailers() {
		String destination = (String)getView().destinationComboBox.getComponent().getSelectedItem();
		destination = StringUtils.trim(destination);
		Logger.getLogger().info("Shipping Destination:" + destination + " was selected.");
		
		String dateStr = getDateFilter();
		getModel().loadTrillers(destination, dateStr);
		
		// update GUI for trailers
		getView().trailerComboBox.setModel(getModel().getTrailers(), -1);
		getView().productList.refresh();
		
	}

	private String getDateFilter() {
		Date pickedDate = (Date) getView().getjDatePicker().getModel().getValue();

		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		String dateOnly = dateFormat. format(pickedDate);
		
		Logger.getLogger().info("Date filter :" + dateOnly + " was selected.");
		return dateOnly;
	}

	private void datePicked() {
		getDateFilter();
		
	}

	private void trailerNumberSelected() {
		String trailerNumber = getSelectedTrailerNumber();
		String shipDate = getDateFilter();
		
		getModel().getAllDunnages(trailerNumber, shipDate);
		
		getView().dunnageList.reloadData(getModel().getDunnages());
		getView().dunnageList.getTable().clearSelection();
		
		if(getView().dunnageList.getTableModel().getItems().size() >0)
			getView().sendAllButton.setEnabled(true);
		
		getView().sendButton.setEnabled(false);
		Logger.getLogger().info("Trailer Number :" + trailerNumber + " was selected.");
		
	}

	private String getSelectedTrailerNumber() {
		String trailerNumber = (String)getView().trailerComboBox.getComponent().getSelectedItem();
		return StringUtils.trim(trailerNumber);
	}

	public void findShippingDestinations() {
		String destination = getSelectedDestination();
		getModel().findShippingDestinations();
		getView().destinationComboBox.getComponent().setSelectedIndex(-1);
		getView().destinationComboBox.setModel(getModel().getDestinations(), 0);
		getView().destinationComboBox.getComponent().setSelectedItem(destination);
		
	}

	private String getSelectedDestination() {
		String destination = (String)getView().destinationComboBox.getComponent().getSelectedItem();
		return StringUtils.trim(destination);
	}
	
	private void doBroadcast(ProductShipping shipItem) {
		try{
			Logger.getLogger().info("Broadcast:", shipItem.getId().getProductId(), shipItem.getProductTypeString(), shipItem.getId().getProcessPointId());
			BroadcastService broadcastService = ServiceFactory.getService(BroadcastService.class);
			DataContainer dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, shipItem.getId().getProductId());
			dc.put(DataContainerTag.PRODUCT_TYPE, shipItem.getProductTypeString());
			broadcastService.broadcast(shipItem.getId().getProcessPointId(), dc);
			Logger.getLogger().info("invoke broadcast service:" + dc.toString());
		}catch(Exception e){
			Logger.getLogger().warn(e, "Failed to invoke broadcast service");
		}
	}
	

}
