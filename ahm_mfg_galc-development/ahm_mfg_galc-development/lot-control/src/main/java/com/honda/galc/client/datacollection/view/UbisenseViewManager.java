package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.ClientContext.Observers;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlJobTorqueUbisenseManager;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.ubisense.UbisensePanel;
import com.honda.galc.client.device.ubisense.UbisenseProductId;
import com.honda.galc.client.device.ubisense.UbisenseToolStatus;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.PartLot;

/**
 * Ubisense View with new features for Lot Control client to communicate with Ubisense device
 * 
 * @author Bernard Leong
 * @date Oct 23, 2017
 */
public class UbisenseViewManager extends ViewManager implements DeviceListener {
	private UbisenseProductId productId = null;
	private LotControlJobTorqueUbisenseManager ubisenseManager;
	
	public UbisenseViewManager(ClientContext clientContext) {
		super(clientContext);
	}

	public void keyPressed(KeyEvent e) {
		if (KeyEvent.VK_F12 == e.getKeyCode()) {
			disableCommunicationWithUbisense();
			return;
		}
		super.keyPressed(e);
	}
	
	private void disableCommunicationWithUbisense() {
		// Disable communication with Ubisense
		setUbisenseButtonToBypass();
		enableTorqueDevices(DataCollectionController.getInstance().getState());
	}
	
	private void setUbisenseButtonToError() {
		setUbisenseButtonDisplay(Color.RED, "ERROR");
	}
	
	private void setUbisenseButtonToBypass() {
		setUbisenseButtonDisplay(Color.RED, "BYPASS");
	}
	
	private void setUbisenseButtonToConnected() {
		setUbisenseButtonDisplay(Color.GREEN, "CONNECTED");
	}

	private void setUbisenseButtonToOut() {
		setUbisenseButtonDisplay(Color.YELLOW, "OUT");
	}

	private void setUbisenseButtonToIn() {
		setUbisenseButtonDisplay(Color.GREEN, "IN");
	}
	
	private void setUbisenseButtonDisplay(Color c, String text) {
		getView().getUbisenseButton().setBackground(c);
		getView().getUbisenseButton().setText(text);
	}
	
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		
		if (deviceData instanceof UbisenseToolStatus) {
			UbisenseToolStatus status =(UbisenseToolStatus) deviceData;
			if(isSatusForCurrentZone(status)) {
				if (!status.isConnected()) {
					setUbisenseButtonToError();
					return deviceData;
				} 
				setUbisenseButtonToConnected();
				if (status.isConnected()) {

					if (checkToolStatus(status))
						setUbisenseButtonToIn();
					else
						setUbisenseButtonToOut();

				}
			}
		}
		
		if (deviceData instanceof UbisenseProductId) {
			SwingUtilities.invokeLater(
				new Runnable() {
				       @Override
				       public void run() {
				    	   view.getTextFieldProdId().setText(((UbisenseProductId)deviceData).getProductId());
				    	   view.getTextFieldProdId().postActionEvent();
				       }
				}
			);
		}

		return deviceData;
	}
	
	
	private boolean isSatusForCurrentZone(UbisenseToolStatus status) {
		DataCollectionState state = getCurrentState(context.getProcessPointId());
		return (state instanceof ProcessTorque && 
				!status.isSameZone(getZoneId(state))) ?
						false : true;
	}

	protected boolean checkToolStatus(UbisenseToolStatus status) {
		boolean enabled = false;
		DataCollectionState state = getCurrentState(context.getProcessPointId());
		if (!(state instanceof ProcessTorque)) return enabled;
		if (status.isInError()) return true;

		if (status.isSameProductId(state.getProductId()) && status.isToolInZone(getZoneId(state))) 
			enabled = true;
		
		return enabled;
	}

	private LotControlJobTorqueUbisenseManager getDeviceManager() {
		return (LotControlJobTorqueUbisenseManager)DataCollectionController.getInstance().getObserver(Observers.DEVICE_MANAGER.toString());
	}
	
	protected void checkUbisenseButtonStatus() {
		if (!getDeviceManager().isConnected()) return;
		boolean enabled = false;
		DataCollectionState state = getCurrentState(context.getProcessPointId());
		if (!(state instanceof ProcessTorque)) {
			setUbisenseButtonToOut();
			return;
		}

		if(getDeviceManager().checkToolStatus(getZoneId(state), state.getProductId()))
			enabled = true;

		if (enabled) 
			setUbisenseButtonToIn();
		else 
			setUbisenseButtonToOut();
	}

	private String getZoneId(DataCollectionState state) {
		return StringUtils.trimToEmpty(state.getCurrentLotControlRule().getDeviceId());
	}
	
	@Override
	public UbisensePanel getView(){
		return (UbisensePanel) view;
	}
	
	@Override
	public void initTorque(ProcessTorque state) {
		super.initTorque(state);
		checkUbisenseButtonStatus();
		
	}
	
	public void completeCollectTorques(ProcessTorque state) {
		super.completeCollectTorques(state);
		setButtonOut();
	}

	private void setButtonOut() {
		if (getDeviceManager() == null || !getDeviceManager().isConnected()) return;
		setUbisenseButtonToOut();
	}
	
	protected void refreshScreen() {
		super.refreshScreen();
		setButtonOut();
	}
	@Override
	public void initProductId(ProcessProduct state) {
		super.initProductId(state);
		if (productId != null) 
			// runInSeparateThread(productId.getProductId()); 
			runInSeparateThread(state.getProductId());
		setProductInputFocused();
	}
	
	@Override
	public void receivedProductId(ProcessProduct state) {
		view.getTextFieldProdId().setText(state.getProductId());
	}
	
	@Override
	public void initPartSn(ProcessPart state) {
		super.initPartSn(state);
		if(state.getCurrentLotControlRule().getSerialNumberScanType() == PartSerialNumberScanType.PART_MASK)
			initPartMask(state);
	}
	
	private void initPartMask(ProcessPart state) {
		PartLot partLot = getDbManager().findCurrentPartLot(state.getCurrentPartName());
		getView().getPartSerialNumber(state.getCurrentPartIndex()).setText(
				(partLot == null) ? "" : partLot.getId().getPartNumber());
		getView().getPartSerialNumber(state.getCurrentPartIndex()).postActionEvent();		
	}

	@Override
	protected DataCollectionPanel createDataCollectionPanel(DefaultViewProperty property) {
		if (view == null) {
			view =  new UbisensePanel(property, viewManagerProperty.getMainWindowWidth(),
				viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}
	
	public LotControlJobTorqueUbisenseManager getUbisenseDeviceManager() {
		if(ubisenseManager == null)
			ubisenseManager = getDeviceManager();
		return ubisenseManager;
	}
}