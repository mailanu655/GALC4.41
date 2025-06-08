package com.honda.galc.client.plastics;

import java.awt.Cursor;
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
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>SubProductShippingController Class description</h3>
 * <p> SubProductShippingController description </p>
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
 * Aug 26, 2014
 *
 *
 */
public class SubProductShippingController implements ActionListener, ListSelectionListener{

	private SubProductShippingModel model;
	private SubProductShippingView view;
	
	private ClientAudioManager audioManager = null;
	
	public SubProductShippingController(SubProductShippingModel model,SubProductShippingView view) {
		this.model = model;
		this.view = view;
		model.checkConfigurations();
		if(model.isSoundAlarmEnabled())
			audioManager =  new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
	}
	
	private void handleErrorMessage(String errorMessage) {
		view.getMainWindow().setErrorMessage(errorMessage);
		getLogger().error(errorMessage);
		if(audioManager != null)
			audioManager.playRepeatedNgSound();
	}
	
	private void handleMessage(String message) {
		view.getMainWindow().setMessage(message);
		getLogger().error(message);
		if(audioManager != null)audioManager.playOKSound();
	}
	
	private Logger getLogger() {
		return view.getLogger();
	}

	public void actionPerformed(ActionEvent e) {
		
		view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		view.getMainWindow().clearMessage();
		try {
			if(e.getSource().equals(view.removeButton)) {
				removeContainerFromScreen();
			}else if(e.getSource().equals(view.shipButton)) {
				shipContainers();
			}else if(e.getSource().equals(view.containerIdField.getComponent())){
				containerIdReceived(view.containerIdField.getComponent().getText());
			}
		}catch(TaskException ex) {
			handleErrorMessage(ex.getMessage());
		}catch(Exception ex) {
			handleErrorMessage("Exception occurred : " + ex.getMessage());
		}
		
		view.setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		view.containerIdField.getComponent().selectAll();
		view.containerIdField.getComponent().requestFocus();
	}

	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting()) return;
		
		if(event.getSource().equals(view.shippingContainerPane.getTable().getSelectionModel())) {
			containerSelected();
		}
	}

	private void containerSelected() {
		MultiValueObject<List<SubProductShippingDetail>> selectedContainer = view.shippingContainerPane.getSelectedItem();
		view.shippingDetailTablePane.reloadData(
				selectedContainer != null ?selectedContainer.getKeyObject() : null);
	}
	
	private boolean containerExists(String containerId) {
		List<MultiValueObject<List<SubProductShippingDetail>>> allContainers = view.shippingContainerPane.getItems();
		for(MultiValueObject<List<SubProductShippingDetail>> container :allContainers) {
			if(containerId.equalsIgnoreCase((String)container.getValue(0))) return true;
		}
		return false;
	}

	private void containerIdReceived(String text) {
		String containerId = StringUtils.trim(text);
		getLogger().info("Received Container ID " + containerId);
		if(StringUtils.isEmpty(containerId))
			throw new TaskException("Please Input Container ID");
		if(containerExists(containerId))
			throw new TaskException("Container Id " + containerId + " is scanned already");
		
		List<SubProductShippingDetail> details = model.findShippingDetails(containerId);
		if(details.isEmpty()) 
			throw new TaskException("Container Id " + containerId + " does not exist");
		 
		if(!model.isContainerLoaded(details))
			throw new TaskException("Container " + containerId + " is not loaded with any product");
		
		List<MultiValueObject<List<SubProductShippingDetail>>> containers = model.createShippingContainers(details);
		List<MultiValueObject<List<SubProductShippingDetail>>> allContainers = view.shippingContainerPane.getItems();
		allContainers.addAll(containers);
		view.shippingContainerPane.clearSelection();
		view.shippingContainerPane.reloadData(allContainers);
		view.shippingContainerPane.select(containers);
		view.containerIdField.getComponent().selectAll();
		view.containerIdField.getComponent().requestFocus();
	}

	private void removeContainerFromScreen() {
		MultiValueObject<List<SubProductShippingDetail>> container = view.shippingContainerPane.getSelectedItem();
		if(container == null) return;
        List<MultiValueObject<List<SubProductShippingDetail>>> containers = view.shippingContainerPane.getItems();
        containers.remove(container);
        view.shippingDetailTablePane.removeData();
        int index = view.shippingContainerPane.getTable().getSelectedRow();
        view.shippingContainerPane.clearSelection();
        view.shippingContainerPane.reloadData(containers);
        if(index + 1 >= containers.size()) index = containers.size() -1;
        view.shippingContainerPane.getTable().getSelectionModel().setSelectionInterval(index, index);
        handleMessage("Container " + container.getValue(0) + " is removed");
        view.containerIdField.getComponent().setText("");
		view.containerIdField.getComponent().requestFocus();
	}
	
	private void shipContainers() {
		
		if(!MessageDialog.confirm(view.getMainWindow(), "Are you sure to ship all containers?")) return;
		List<MultiValueObject<List<SubProductShippingDetail>>> containers = view.shippingContainerPane.getItems();
	    for(MultiValueObject<List<SubProductShippingDetail>> container : containers) {
	    	model.shipProducts(container.getKeyObject());
	    	getLogger().info("Container " + container.getValue(0) + " " + container.getValue(1) + " is shipped");
	    }
	    
	    view.shippingContainerPane.clearSelection();
		view.shippingContainerPane.reloadData(null);
		view.shippingDetailTablePane.reloadData(null);
		
		view.containerIdField.getComponent().setText("");
		view.containerIdField.getComponent().requestFocus();
		
	}
	
}
