package com.honda.galc.client.datacollection.view;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventObject;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.scanless.ScanlessScannerMessage;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class DataCollectionEventHandler implements KeyListener, ActionListener {

	StringBuffer buffer = new StringBuffer();
	protected ClientContext context;

	protected KeyEventDispatcher eventDispatcher = new KeyEventDispatcher() {
		public boolean dispatchKeyEvent(KeyEvent e) {

			if (e.getID() == KeyEvent.KEY_TYPED)
				buffer.append(e.getKeyChar());

			if ((e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == KeyEvent.VK_ENTER)) {

				final String scanned = buffer.toString().replace("\n", "");
				buffer.setLength(0);

				handleScanReceived(e, scanned.toUpperCase());
			}
			return true;
		}
	};

	public void enableKeyBuffer() {
		getKeyboardFocusMgr().removeKeyEventDispatcher(eventDispatcher);
		getKeyboardFocusMgr().addKeyEventDispatcher(eventDispatcher);
	}

	public void disableKeyBuffer() {
		getKeyboardFocusMgr().removeKeyEventDispatcher(eventDispatcher);
	}

	public KeyboardFocusManager getKeyboardFocusMgr() {
		return KeyboardFocusManager.getCurrentKeyboardFocusManager();
	}

	public DataCollectionEventHandler(ClientContext context) {
		this.context = context;
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		buffer.append(e.getKeyChar());
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && buffer.length() > 0) {
			String scanned = "";
			scanned = buffer.toString();
			buffer.delete(0, buffer.length());
			handleScanReceived(e, scanned);
		}
	}

	public void actionPerformed(ActionEvent e) {
		JTextField currentTextField = (JTextField) e.getSource();
		String scanned = currentTextField.getText();

		handleScanReceived(e, scanned);
	}

	public void handleScanReceived(EventObject e, String scanned) {
		DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState().clearMessage();

		Logger.getLogger().info(" Received Scan: " + scanned);
		if (scanned.length() > 0 && !handleUniqueScanCode(scanned)) {
			if (!handleScanlessChecks()) {
				String message = "Scanner not in Geofence ";
				showErrorMessage(message, MessageType.ERROR, LotControlConstants.NOT_IN_ZONE);
				ViewManagerBase vmb = context.getCurrentViewManager();
				vmb.setErrorMessage(message);
				if (JTextField.class.isAssignableFrom(e.getSource().getClass())) {
					((JTextField) e.getSource()).selectAll();
				}
				//DataCollectionController.getInstance().getFsm().error(new Message(LotControlConstants.NOT_IN_ZONE, message));
				throw new TaskException(message, LotControlConstants.NOT_IN_ZONE);
				
			} else {
				String currentProdId = DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState().getProductId();
				if (context.getProperty().isUnexpectedProductScanCheckEnabled()
						&& checkIfProductExistsAndNotCurrentProduct(scanned)) {
					handleUnexpectedProductScan(currentProdId, scanned);
				} else {
					if (DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState() instanceof ProcessTorque) {
						handleUnexpectedScanInTorque(scanned);
					} else if (StrategyType.DEVICE_DATA_RESPONSE.equals(getCurrentStrategyType())) {
						handleUnexpectedScanInBroadcast(e, scanned);
					}
				}
			}
		}
	}

	private boolean handleScanlessChecks() {
		if (context.getProperty().isScanlessEnabled() && isScannerInAuto()) {

			String scannerToolId = context.getProperty().getScanToolId();
			ScanlessScannerMessage scanlessScannerMessage = DataCollectionController.getInstance()
					.getScannerStatus(scannerToolId);
			DataCollectionState state = DataCollectionController.getInstance().getState();
			if (scanlessScannerMessage != null) {
				if (scanlessScannerMessage.isSameProductId(state.getProductId())) {

					if (!scanlessScannerMessage.isSameToolInZone()) {
						Logger.getLogger()
								.info("Tool Not in Zone (expected=" + scanlessScannerMessage.ENTER_ZONE + " OR "
										+ scanlessScannerMessage.IN_ZONE + ", actual="
										+ scanlessScannerMessage.getToolEventType() + ")", ", Disabling Torque");

						return false;
					}
				} else {
					Logger.getLogger().info("Tool Product Id does not Match(expected=" + state.getProductId()
							+ " actual=" + scanlessScannerMessage.getToolProductId() + ")", ", Disabling Torque");

					return false;
				}
			} else {
				Logger.getLogger().info(" No Message Found for Device-" + scannerToolId);

				return false;
			}
		}
		return true;
	}

	private StrategyType getCurrentStrategyType() {
		LotControlRule currentLotControlRule = DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState()
				.getCurrentLotControlRule();
    
		if (currentLotControlRule == null) {
			return null;
		}
		String strategy = DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState().getCurrentLotControlRule().getStrategy();
		if (StringUtils.isBlank(strategy)) {
			return null;
		}
		try {
			return StrategyType.valueOf(strategy);
		} catch (Exception e) {
			throw new TaskException("Unable to retrieve StrategyType for strategy: " + strategy, e);
		}
	}

	private void handleUnexpectedProductScan(String currentProdId, String scanned) {
		disableTorqueControllers();
		String message = "An unexpected scan of a product has been detected. \n" + "Scanned (" + scanned
				+ ") while still completing \n data collection for (" + currentProdId + "). ";

		Logger.getLogger().error(message);
		showErrorMessage(message, MessageType.EMERGENCY, LotControlConstants.UNEXPECTED_PRODUCT_SCAN);
	}

	private void handleUnexpectedScanInTorque(String scanned) {
		if (context.getProperty().isMeasurementEditable()) {
			return;
		}

		final String message = "Unexpected scan or keyboard entry received, waiting for torque. \n" + "Scanned ("
				+ scanned + "). ";
		Runnable r = new Runnable() {
			public void run() {
				Logger.getLogger().error(message);
				showErrorMessage(message, MessageType.ERROR, LotControlConstants.SCAN_DURING_TORQUE);
				ViewManagerBase vmb = context.getCurrentViewManager();
				vmb.setErrorMessage(message);
			}
		};
		new Thread(r).start();
	}

	private void handleUnexpectedScanInBroadcast(final EventObject e, final String scanned) {
		final String message = "Unexpected scan or keyboard entry received, waiting for broadcast response. \n"
				+ "Scanned (" + scanned + "). ";
		Runnable r = new Runnable() {
			public void run() {
				Logger.getLogger().error(message);
				showErrorMessage(message, MessageType.ERROR, LotControlConstants.SCAN_DURING_BROADCAST);
				ViewManagerBase vmb = context.getCurrentViewManager();
				vmb.setErrorMessage(message);
				if (JTextField.class.isAssignableFrom(e.getSource().getClass())) {
					((JTextField) e.getSource()).selectAll();
				}
			}
		};
		new Thread(r).start();
	}

	private void showErrorMessage(String msg, MessageType msgType, String msgId) {
		Message m = new Message();
		m.setType(msgType);
		m.setId(msgId);
		m.setInfo(msg);

		Object[] params = { m };
		runInSeparateThread(new Request("message", params));
	}

	private void disableTorqueControllers() {
		Event event = new Event(DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState(), EventType.TORQUE_DISABLE_ALL);
		EventBus.publish(event);
	}

	private boolean handleUniqueScanCode(String scanned) {
		ViewManagerBase vmb = context.getCurrentViewManager();
		if (vmb == null) {
			context.createViewManager();
			vmb = context.getCurrentViewManager();
		}
		ViewManagerBase.UniqueScanType scanType = vmb.getUniqueScanType(scanned);
		if (scanType != ViewManagerBase.UniqueScanType.NONE) {
			return vmb.handleUniqueScanCode(scanType);
		} else {
			return false;
		}
	}

	protected void runInSeparateThread(final Request request) {
		Thread t = new Thread() {
			public void run() {
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).received(request);
			}
		};

		t.start();
	}

	protected boolean checkIfProductExistsAndNotCurrentProduct(String scanned) {

		// 2016-01-15 - BAK - Add try/catch block and remove "I"
		BaseProduct product = null;
		try {
			String prodId = scanned.trim();

			if (context.isRemoveIEnabled()) {
				if (context.getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString())) {
					prodId = context.removeLeadingVinChars(prodId);
				}
			}

			if (!isSameLength(prodId)) {
				return false;
			}
			product = ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findBySn(prodId);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Error retrieving product");
		}

		if (product == null)
			return false;
		String currentProdId = DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState().getProductId();

		return !product.getProductId().equalsIgnoreCase(currentProdId);
	}

	protected boolean isSameLength(String sn) {
		ProductType prodType = ProductType.getType(context.getProperty().getProductType());
		for (ProductNumberDef def : ProductNumberDef.getProductNumberDef(prodType)) {
			if (sn.length() == def.getLength()) {
				return true;
			}
		}
		return false;
	}

	private boolean isScannerInAuto() {
		ComponentStatus scanlessScannerComponentStatus = ServiceFactory.getDao(ComponentStatusDao.class)
				.findByKey(context.getProcessPointId(), "SCANLESS_SCANNER_OPERATION_MODE");
		String scanlessScannerOperationMode = scanlessScannerComponentStatus == null ? "N/A"
				: scanlessScannerComponentStatus.getStatusValue();
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(scanlessScannerOperationMode);
	}
}