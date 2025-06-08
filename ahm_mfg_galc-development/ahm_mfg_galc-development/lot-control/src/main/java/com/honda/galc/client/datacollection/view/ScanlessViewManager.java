package com.honda.galc.client.datacollection.view;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;

public class ScanlessViewManager extends ViewManager {

	public static final String REFRESH_REQUEST = "cancel";

	public ScanlessViewManager(ClientContext clientContext) {
		super(clientContext);
	}

	protected boolean isPopupMessage(Message errorMsg) {
		if (errorMsg != null) {
			if (context.getProperty().isScanlessEnabled() && isInAuto()) {
				List<String> errorTypes = Arrays.asList(context.getProperty().getScanlessErrorTypePopupAllowed());
				if (!errorTypes.contains(errorMsg.getId())) {
					return false;
				}
			}
		}
		return super.isPopupMessage(errorMsg);
	}
	
	public void keyPressed(KeyEvent e) {
		if (KeyEvent.VK_F12 == e.getKeyCode()) {
			disableEnableCommunicationForScanlessTorque();
			return;
		}
		if (KeyEvent.VK_F11 == e.getKeyCode()) {
			disableEnableCommunicationForScanless();
		}
		if (KeyEvent.VK_F9 == e.getKeyCode()) {
			disableEnableCommunicationForScanlessScanner();
			return;
		}
		super.keyPressed(e);
	}

	protected void refreshScreen() {
		super.refreshScreen();
		if (isInAuto()) {
			renderFieldBeanInit(view.getTextFieldProdId(), true);
			//view.getTextFieldProdId().setEditable(false);
			//view.getTextFieldProdId().setEnabled(false);
		} else {
			renderFieldBeanInit(view.getTextFieldProdId(), true);
			view.getTextFieldProdId().setEditable(true);
			view.getTextFieldProdId().setEnabled(true);
		}
	}

	private boolean isInAuto() {
		ComponentStatus componentStatus = getComponentStatusScanless();
		String operationMode = componentStatus == null ? "N/A" : componentStatus.getStatusValue();
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(operationMode);
	}

	private void disableEnableCommunicationForScanlessTorque() {

		ComponentStatus componentStatus = getComponentStatusScanlessTorque();
		String operationMode = componentStatus.getStatusValue();
		if (operationMode.equalsIgnoreCase(OperationMode.AUTO_MODE.getName())) {
			//boolean confirm = MessageDialog.confirm(context.getFrame(),	" Are you Sure you want to switch to Manual mode?");
			//if (confirm) {
				Logger.getLogger().info(" updating Torque Operation Mode from Auto mode to Manual mode");
				componentStatus.setStatusValue(OperationMode.MANUAL_MODE.getName());
				getComponentStatusDao().save(componentStatus);
				enableTorqueDevices(DataCollectionController.getInstance().getState());
				
				//runInSeparateThread(new Request(REFRESH_REQUEST));
			//}
		} else if (operationMode.equalsIgnoreCase(OperationMode.MANUAL_MODE.getName())) {
			Logger.getLogger().info(" updating Torque Operation Mode from Manual mode to Auto mode");
			componentStatus.setStatusValue(OperationMode.AUTO_MODE.getName());
			getComponentStatusDao().save(componentStatus);
			//runInSeparateThread(new Request(REFRESH_REQUEST));
			disableTorqueDevices();
		}

	}
	
	private void disableEnableCommunicationForScanlessScanner() {

		ComponentStatus componentStatus = getComponentStatusScanlessScanner();
		String operationMode = componentStatus.getStatusValue();
		if (operationMode.equalsIgnoreCase(OperationMode.AUTO_MODE.getName())) {
			
				Logger.getLogger().info(" updating Scanner Operation Mode from Auto mode to Manual mode");
				componentStatus.setStatusValue(OperationMode.MANUAL_MODE.getName());
				getComponentStatusDao().save(componentStatus);
			
		} else if (operationMode.equalsIgnoreCase(OperationMode.MANUAL_MODE.getName())) {
			Logger.getLogger().info(" updating Scanner Operation Mode from Manual mode to Auto mode");
			componentStatus.setStatusValue(OperationMode.AUTO_MODE.getName());
			getComponentStatusDao().save(componentStatus);

		}

	}


	private void disableEnableCommunicationForScanless() {
		ComponentStatus componentStatus = getComponentStatusScanless();
		ComponentStatus componentStatusScanlessTorque = getComponentStatusScanlessTorque();
		ComponentStatus componenetStatusScanlessScanner = getComponentStatusScanlessScanner();
		String operationMode = componentStatus.getStatusValue();
		if (operationMode.equalsIgnoreCase(OperationMode.AUTO_MODE.getName())) {
			//boolean confirm = MessageDialog.confirm(context.getFrame()," Are you Sure you want to switch to Manual mode?");
			//if (confirm) {
				Logger.getLogger().info(" updating Scanless Mode from Auto mode to Manual mode");
				componentStatus.setStatusValue(OperationMode.MANUAL_MODE.getName());
				getComponentStatusDao().save(componentStatus);
				componentStatusScanlessTorque.setStatusValue(OperationMode.MANUAL_MODE.getName());
				getComponentStatusDao().save(componentStatusScanlessTorque);
				componenetStatusScanlessScanner.setStatusValue(OperationMode.MANUAL_MODE.getName());
				getComponentStatusDao().save(componenetStatusScanlessScanner);
				runInSeparateThread(new Request(REFRESH_REQUEST));
			//}
		} else if (operationMode.equalsIgnoreCase(OperationMode.MANUAL_MODE.getName())) {
			Logger.getLogger().info(" updating Operation Mode from Manual mode to Auto mode");
			componentStatus.setStatusValue(OperationMode.AUTO_MODE.getName());
			getComponentStatusDao().save(componentStatus);
			componentStatusScanlessTorque.setStatusValue(OperationMode.AUTO_MODE.getName());
			getComponentStatusDao().save(componentStatusScanlessTorque);
			componenetStatusScanlessScanner.setStatusValue(OperationMode.AUTO_MODE.getName());
			getComponentStatusDao().save(componenetStatusScanlessScanner);
			runInSeparateThread(new Request(REFRESH_REQUEST));
		}
	}

	private ComponentStatusDao getComponentStatusDao() {
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}

	private ComponentStatus getComponentStatusScanless() {
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "SCANLESS_OPERATION_MODE");
	}

	private ComponentStatus getComponentStatusScanlessTorque() {
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "SCANLESS_TORQUE_OPERATION_MODE");
	}
	private ComponentStatus getComponentStatusScanlessScanner() {
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "SCANLESS_SCANNER_OPERATION_MODE");
	}
}