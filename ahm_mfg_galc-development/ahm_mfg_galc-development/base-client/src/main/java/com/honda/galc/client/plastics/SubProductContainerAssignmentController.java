package com.honda.galc.client.plastics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.enumtype.SubProductShippingDetailStatus;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>BlockLoadController Class description</h3>
 * <p> BlockLoadController description </p>
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
 * @author Jeffray Huang<br>
 * Mar 26, 2014
 *
 *
 */
public class SubProductContainerAssignmentController implements ActionListener, ListSelectionListener{
	private SubProductContainerAssignmentModel model;
	private SubProductContainerAssignmentView view;
	
	private ClientAudioManager audioManager = null;
	
	private SubProductShipping lastSelectedLot = null;
	
	public SubProductContainerAssignmentController(SubProductContainerAssignmentModel model,SubProductContainerAssignmentView view) {
		this.model = model;
		this.view = view;
		model.checkConfigurations();
        if(model.isSoundAlarmEnabled()) 
        	audioManager =  new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
	}

	private void displayMessage(String message) {
		view.getMainWindow().setMessage(message);
	}
	
	private void handleErrorMessage(String errorMessage) {
		view.getMainWindow().setErrorMessage(errorMessage);
		getLogger().error(errorMessage);
		if(audioManager != null)
			audioManager.playRepeatedNgSound();
	}
	
	private void clearMessage() {
		view.getMainWindow().clearMessage();
	}
	
	private Logger getLogger() {
		return view.getLogger();
	}
	
	private void selectKdLot(){
		SubProductShipping selectedLot = view.shippingTablePane.getSelectedItem();
		int[] rows = view.shippingTablePane.getTable().getSelectedRows();
		if(selectedLot == null){
			lastSelectedLot = null;
			return;
		}
		
		List<SubProductShipping> allShippingLots = view.shippingTablePane.getItems();
		int minIndex = rows[0];
		int maxIndex = rows[0];
		for(int i =maxIndex + 1; i<allShippingLots.size();i++) {
			SubProductShipping shippingLot = allShippingLots.get(i);
			if(shippingLot.isSameKdLot(selectedLot)) maxIndex = i;
			else break;
		}
		
		for(int i =minIndex - 1; i>=0;i--) {
			SubProductShipping shippingLot = allShippingLots.get(i);
			if(shippingLot.isSameKdLot(selectedLot)) minIndex = i;
			else break;
		}
		if(rows[0] != minIndex || rows[rows.length -1] != maxIndex || !selectedLot.equals(lastSelectedLot)){
			view.shippingTablePane.clearSelection();
			lastSelectedLot = selectedLot;
			view.shippingTablePane.getTable().getSelectionModel().setSelectionInterval(minIndex, maxIndex);
		}
	}

	private void loadContainers() {
		List<SubProductShipping> shippingLots = view.shippingTablePane.getSelectedItems();
		List<MultiValueObject<List<SubProductShippingDetail>>> shippingContainers = 
			model.selectShippingContainers(shippingLots);
		view.shippingContainerPane.reloadData(shippingContainers);
		view.shippingContainerPane.clearSelection();
	}
	
	private void refreshContainers() {
		int selectionIndex = view.shippingContainerPane.getTable().getSelectedRow();
		loadContainers();
		if(selectionIndex >=0 )
			view.shippingContainerPane.getTable().getSelectionModel().setSelectionInterval(selectionIndex, selectionIndex);
	}
	
	private void containerSelected() {
		MultiValueObject<List<SubProductShippingDetail>> selectedContainer = view.shippingContainerPane.getSelectedItem();
		view.shippingDetailTablePane.clearSelection();
		view.shippingDetailTablePane.reloadData(
				selectedContainer != null ?selectedContainer.getKeyObject() : null);
		if(selectedContainer != null) {
			if(model.isShipped(selectedContainer.getKeyObject())){
				view.addButton.setEnabled(false);
				view.productIdField.setEnabled(false);
				view.productIdField.getComponent().setText("");
				throw new TaskException("Container " + selectedContainer.getValue(2) + " is shipped");
			}
		}
		view.shippingDetailTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		view.addButton.setEnabled(true);
		view.productIdField.setEnabled(true);
		view.productIdField.getComponent().setText("");
		view.productIdField.getComponent().requestFocus();
	}
	
	private void setContainer() {
		MultiValueObject<List<SubProductShippingDetail>> selectedContainer = view.shippingContainerPane.getSelectedItem();
		if(selectedContainer == null) return;
		
		String containerId = MessageDialog.showInputDialog(view.getMainWindow(),"Please Input Container ID", "CONTAINER ID", 10, true);
		if(StringUtils.isEmpty(containerId)) return;
		
		if(model.isContainerInUse(StringUtils.trim(containerId)))
			throw new TaskException("Container " + containerId + " is in use");
		
		model.updateContainerId(selectedContainer.getKeyObject(), containerId);
		
		selectedContainer.setValue(2, containerId);
		view.shippingContainerPane.updateUI();
	}
	
	private void refreshShippingLots() {
		int index = view.shippingTablePane.getTable().getSelectedRow();
		view.shippingTablePane.clearSelection();
		view.shippingTablePane.reloadData(model.getAllShippingLots());
		view.shippingTablePane.getTable().getSelectionModel().setSelectionInterval(index, index);
	}
	
	private void shippingDetailSelected() {
		view.productIdField.getComponent().requestFocus();
	}
	
	private void productIdReceived(String productId) {
		try{
			basicProductIdReceived(productId);
		}catch(TaskException ex){
			handleErrorMessage(ex.getMessage());
			view.productIdField.getComponent().selectAll();
		}catch(Exception ex){
			handleErrorMessage("Exception Occured: " + ex.getMessage());
			view.productIdField.getComponent().selectAll();
		}
	}	
	
	private void basicProductIdReceived(String productId) {
		getLogger().info("received product id : " + productId);
		
		SubProductShippingDetail detail = view.shippingDetailTablePane.getSelectedItem();
		int index = view.shippingDetailTablePane.getTable().getSelectedRow();
		SubProduct subProduct = model.validateProductId(detail, StringUtils.trim(productId));
		
		if(!ProductSpec.excludeToModelCode(subProduct.getProductSpecCode())
				.equalsIgnoreCase(ProductSpec.excludeToModelCode(detail.getProductSpecCode()))){
			if(!MessageDialog.confirm(view.getMainWindow(), "Do you want to load a product with a different MTOC: " + subProduct.getProductSpecCode() + " ?")){
				throw new TaskException("Product " + productId + "'s MTOC " + subProduct.getProductSpecCode() + " does not match");
			}
			detail.setStatus(SubProductShippingDetailStatus.MTOC_NOT_MATCH);
		}
		List<SubProductShippingDetail> deletedDetails = model.removeSubProductFromShipping(productId);
		if(!StringUtils.isEmpty(detail.getProductId())) {
			getLogger().warn("Old product " + detail.getProductId() + " is replaced by " + productId);
		}
		
		detail.setProductId(subProduct.getProductId());
		SubProductShipping subProductShipping = model.saveShippingProduct(detail);
		List<SubProductShipping> shippingList = view.shippingTablePane.getItems();
		
		boolean isFullyLoaded = model.isFullyLoaded(view.shippingDetailTablePane.getItems());
		if(!isFullyLoaded && subProductShipping != null && deletedDetails.isEmpty()) 
			model.updateShippingData(shippingList, subProductShipping);
		else
			shippingList = model.getAllShippingLots();
		
		view.shippingTablePane.reloadData(shippingList);
		
		SubProductShipping selectedLot = view.shippingTablePane.getSelectedItem();
		
		if(selectedLot != null && !selectedLot.equals(lastSelectedLot)) {
			selectKdLot();
			loadContainers();
		}else {
			MultiValueObject<List<SubProductShippingDetail>> selectedContainer = view.shippingContainerPane.getSelectedItem();
			if(!deletedDetails.isEmpty()) {
				refreshContainers();
			}else {
				view.shippingDetailTablePane.reloadData(
						selectedContainer != null ?selectedContainer.getKeyObject() : null);
			}
			if(view.shippingDetailTablePane.getItems().size() > (index))
				view.shippingDetailTablePane.getTable().getSelectionModel().setSelectionInterval(index +1, index + 1);
		}	
		if(audioManager != null)audioManager.playOKSound();
		
		view.productIdField.getComponent().setText("");
		view.productIdField.getComponent().requestFocus();
		
		displayMessage("Product " + productId + " is loaded successfully"); 
		getLogger().info("Product " + productId + " is loaded: " + 
				" Kd Lot: " + detail.getKdLot() + ", Prod Lot:" + detail.getProductionLot() + 
				" Position: " + detail.getProductSeqNo() + " Container: " + detail.getContainerId());
	}
	
	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting()) return;
		
		clearMessage();
		try{
			if(event.getSource().equals(view.shippingTablePane.getTable().getSelectionModel())) {
				selectKdLot();
				loadContainers();
			}else if(event.getSource().equals(view.shippingContainerPane.getTable().getSelectionModel())) {
				containerSelected();
			}else if(event.getSource().equals(view.shippingDetailTablePane.getTable().getSelectionModel())) {
				shippingDetailSelected();
			}
		}catch(TaskException ex) {
			handleErrorMessage(ex.getMessage());
		}catch(Exception ex) {
			handleErrorMessage("Exception occurred : " + ex.getMessage());
		}finally{
			view.productIdField.getComponent().requestFocus();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		clearMessage();
		try {
			if(e.getSource().equals(view.refreshButton)) {
				refreshShippingLots();
			}else if(e.getSource().equals(view.addButton)) {
				setContainer();
			}else if(e.getSource().equals(view.productIdField.getComponent())){
				productIdReceived(view.productIdField.getComponent().getText());
			}
		}catch(TaskException ex) {
			handleErrorMessage(ex.getMessage());
		}catch(Exception ex) {
			handleErrorMessage("Exception occurred : " + ex.getMessage());
		}finally{
			view.productIdField.getComponent().requestFocus();
		}
		
	}


	
}
