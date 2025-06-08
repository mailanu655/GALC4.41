package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.IMessageArea;
import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.EnhancedStatusMessagePanel;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.DataCollectionObserverBase;
import com.honda.galc.client.datacollection.observer.DataCollectionObserverInvoker;
import com.honda.galc.client.datacollection.observer.ILotControlDbManager;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.view.action.PreviousProductButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipNextExpectedProductAction;
import com.honda.galc.client.datacollection.view.action.SkipPartButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipProductButtonAction;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.CounterId;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ViewManagerBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ViewManagerBase description </p>
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
 * @author Paul Chou
 * May 5, 2010
 *
 */
/**
 * * *
 * 
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ViewManagerBase extends DataCollectionObserverBase implements KeyListener, ConnectionStatusListener, CaretListener {
	
	public enum UniqueScanType { NONE, SKIP, REJECT, NEXTVIN, REFRESH, RESETSEQ, TESTTORQUE,PREVVIN};
	protected static String[] popupMsgsFilter = { LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED,
													LotControlConstants.MSG_FAILED_TO_SEND_STATUS_DATA_TO_EI_DEVICE,
													LotControlConstants.UNEXPECTED_PRODUCT_SCAN, LotControlConstants.FAILED_PRODUCT_CHECKS };

	protected IMessageArea messageArea;
	protected EnhancedStatusMessagePanel messagePanel;
	protected ViewManagerPropertyBean viewManagerProperty;
	protected ClientContext context;
	protected DataCollectionEventHandler dataCollectionEventHandler;
	protected boolean isCaretListenerActive = true;

	public ViewManagerBase(ClientContext context) {
		super();
		this.context = context;
		this.viewManagerProperty = context.getProperty();
	}

	public void addKeyListener(List<JComponent> list) {
		for (JComponent jc : list)
			jc.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		if (isSkipProductKeyEnabled(e))
			skipProduct();
		else if (isSkipProductKeyAutoEnabled(e))
			skipNextProduct();
		else if (isSkipPartKeyEnabled(e)) 
			skipPart();
		else if (isSkipPrevProductKeyEnabled(e)) 
			skipPrevProduct();
	}
	
	private boolean isInProductInputState() {
		DataCollectionState state = getCurrentState(context.getProcessPointId());
		if (state instanceof ProcessProduct)
			return true;

		return false;
	}
	
	private boolean isSkipProductKeyEnabled(KeyEvent e) {
		return (context.getProperty().isEnableSkipProductIdKey() && isInProductInputState() && (KeyEvent.VK_F3 == e.getKeyCode() || KeyEvent.VK_F7 == e.getKeyCode()));
	}
	private boolean isSkipPrevProductKeyEnabled(KeyEvent e) {
		return (context.getProperty().isEnablePreviousProductIdKey() && isInProductInputState() && (KeyEvent.VK_F4 == e.getKeyCode() || KeyEvent.VK_F7 == e.getKeyCode()));
	}
	
	private boolean isSkipProductKeyAutoEnabled(KeyEvent e) {
		return (context.getProperty().isEnableSkipProductIdKey() && 
				KeyEvent.VK_F5 == e.getKeyCode() &&	!isInProductInputState());
	}
	
	private boolean isSkipPartKeyEnabled(KeyEvent e) {
		return (context.getProperty().isEnableSkipPartKey() && KeyEvent.VK_F9 == e.getKeyCode() && !isInProductInputState());
	}
	
	protected void skipProduct() {
		SkipProductButtonAction action = new SkipProductButtonAction(context, "Skip Product");
		action.doNextVIN();
	}
	
	private void skipPrevProduct() {
		PreviousProductButtonAction action = new PreviousProductButtonAction(context, "Prev Product");
		action.doPrevVIN();
	}
	
	protected void skipNextProduct() {
		SkipNextExpectedProductAction action = new SkipNextExpectedProductAction(context, "Skip Product Ok");
		action.doNextVIN();
	}
	
	private void skipPart() {
		SkipPartButtonAction action = new SkipPartButtonAction(context,
				"Skip Part");
		action.skipCurrentPart();
	}
	
	public void keyReleased(KeyEvent e) {
		if (KeyEvent.VK_ENTER == e.getKeyCode() || e.isActionKey())
			return;
		if (LotControlAudioManager.isExist())
			stopPlayingNgSound();

	}

	private void stopPlayingNgSound() {
		LotControlAudioManager.getInstance().stopRepeatedSound();

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void setErrorMessage(String msg) {
		if (msg == null)
			messageArea.setErrorMessageArea(null, Color.lightGray);
		else
			messageArea.setErrorMessageArea(msg, Color.red);
	}

	public void setErrorMessage(String msg, DeviceMessageSeverity severity) {
		if (msg == null) {
			clearMessageArea(null);
		} else if (severity == DeviceMessageSeverity.warning) {
			messageArea.setErrorMessageArea(msg, Color.lightGray);
		} else if (severity == DeviceMessageSeverity.error) {
			messageArea.setErrorMessageArea(msg, Color.red);
		}

	}

	public void displayTorqueErrorMessage(String message, DeviceMessageSeverity severity) {
		setErrorMessage(message, severity);
	}

	protected boolean hasMessage(DataCollectionState state) {
		return state.hasMessage() && !StringUtils.isEmpty(state.getMessage().getDescription());
	}

	protected void clearMessageArea(DataCollectionState state) {
		if (state == null)
			setErrorMessage((String) null);
		else if (!hasMessage(state) && !hasException(state))
			setErrorMessage((String) null);
	}

	protected boolean hasException(DataCollectionState state) {
		return (state.getExceptionList().size()) > 0;
	}

	protected Message getErrorMessage(DataCollectionState state) {
		if (hasException(state))
			return state.getExceptionList().get(0).toMessage();
		else
			return state.getMessage();
	}

	public EnhancedStatusMessagePanel getMessagePanel() {
		if (messagePanel == null) {
			messagePanel = (EnhancedStatusMessagePanel) ((MainWindow) context.getFrame()).getStatusMessagePanel();

		}
		return messagePanel;
	}

	protected void notifyFinishProduct() {
		getMessagePanel().getStatusPanel().getLotControlSystemInfo().getLogManager().finishProduct();
	}

	protected void notifyNewProduct(String productId) {
		getMessagePanel().getStatusPanel().getLotControlSystemInfo().getLogManager().newProduct(productId);
	}

	protected void initConnections() throws Exception {
		// create SysInfo object to accept log
		getMessagePanel().getStatusPanel().getLotControlSystemInfo();
		dataCollectionEventHandler = new DataCollectionEventHandler(this.context);
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = LogRecord.class)
	public void showError(LogRecord event) {
		// Only Emergency message is shown
		if (event.getLogLevel() == LogLevel.EMERGENCY) {
			setErrorMessage(event.getMessage());

			playNGSound();
		}

	}

	protected void playNGSound() {
		if (LotControlAudioManager.isExist())
			LotControlAudioManager.getInstance().playNGSound();

	}

	@Override
	public void update(Observable o, Object arg) {

		boolean processQics = context.isQicsSupport();
		Event event = null;
		if (processQics) {
			// Remark: we capture action value before invoking observers as
			// action
			// might be modified by handlers.
			event = new Event(arg, ((DataCollectionState) arg).getAction(), EventType.CHANGED);
		}

		try {
			if (SwingUtilities.isEventDispatchThread()) {
				Logger.getLogger().debug(getClass().getName() + ": calling invoke");
				DataCollectionObserverInvoker.invoke(this, o, arg);
			} else {
				Logger.getLogger().debug(getClass().getName() + ": calling invokeAndWait");
				DataCollectionObserverInvoker.invokeAndWait(this, o, arg);
			}
		} catch (Exception e) {
			Logger.getLogger().error("Failed to update view:" + e.getCause().toString());
			setErrorMessage(e.getCause().toString());
		}

		if (processQics) {
			publishQicsEvent(event);
		}

	}

	private void publishQicsEvent(Event event) {
		EventBus.publish(event);
	}

	protected void cleanMessage(DataCollectionState state) {
		state.clearMessage();

	}

	protected void setErrorMessage(Message errorMsg) {
		if (errorMsg == null)
			messageArea.setErrorMessageArea(null, Color.lightGray);
		else
			messageArea.setErrorMessageArea(errorMsg.getDescription(),
					errorMsg.getType() == MessageType.INFO ? Color.lightGray : Color.red);

	}

	public void enableExpectedProduct(boolean b) {
	}

	public void buttonControl(JButton button, boolean visible, boolean enabled) {
		button.setVisible(visible);
		button.setEnabled(enabled);
	};

	public void statusChanged(ConnectionStatus status) {
		if (context.isOnLine() != status.isConnected()) {
			context.setOnLine(status.isConnected());

			Message msg = new Message(
					status.isConnected() ? StatusMessage.SERVER_ON_LINE : StatusMessage.SERVER_OFF_LINE,
					status.isConnected() ? "Server back on line" : "Server off line", MessageType.INFO);
			DataCollectionController.getInstance().getFsm().message(msg);

		}

		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, status.isConnected()));
	}

	protected String getProductSpecText(ProcessProduct state) {
		String methodName = "get" + viewManagerProperty.getProdSpecText();

		try {
			Method method = state.getProduct().getClass().getMethod(methodName, new Class[] {});
			return (String) method.invoke(state.getProduct(), new Object[] {});
		} catch (Exception e) {
			throw new TaskException("Invalid product spec text property:" + viewManagerProperty.getProdSpecText());
		}
	}

	protected ILotControlDbManager getDbManager() {
		return DataCollectionController.getInstance().getClientContext().getDbManager();
	}

	protected void notifySkippedPartProduct() {
		if (hasSkippedPart())
			EventBus.publish(new SkippedProduct(new SkippedProductId(
					getCurrentState(context.getProcessPointId()).getProductId(), context.getProcessPointId())));

	}

	protected void refreshProcessedCounterPanel() {
		EventBus.publish(new Counter(new CounterId()));
	}
	
	protected boolean hasSkippedPart() {
		boolean result = false;
		if (getCurrentState(context.getProcessPointId()).getProduct() == null)
			return false;

		for (InstalledPart p : getCurrentState(context.getProcessPointId()).getProduct().getPartList()) {
			if (p.isSkipped())
				return true;
		}
		return result;
	}

	protected boolean isMeasurementEditable() {
		return context.getProperty().isMeasurementEditable();
	}

	protected boolean isAfOnSeqNumExist() {
		return context.isAfOnSeqNumExist();
	}

	protected boolean isProductLotCountExist() {
		return context.isProductLotCountExist();
	}
	public UniqueScanType getUniqueScanType(String scan) {
		if (scan == null)
			return UniqueScanType.NONE;
		if (scan.equals(getScanCode(UniqueScanType.SKIP)) || (StringUtils.contains(scan, getScanCode(UniqueScanType.SKIP))))
			return UniqueScanType.SKIP;
		if (scan.equals(getScanCode(UniqueScanType.REJECT)) || (StringUtils.contains(scan, getScanCode(UniqueScanType.REJECT))))
			return UniqueScanType.REJECT;
		if (scan.equals(getScanCode(UniqueScanType.NEXTVIN)) || (StringUtils.contains(scan, getScanCode(UniqueScanType.NEXTVIN))))
			return UniqueScanType.NEXTVIN;
		if (scan.equals(getScanCode(UniqueScanType.PREVVIN)) || (StringUtils.contains(scan, getScanCode(UniqueScanType.PREVVIN))))
			return UniqueScanType.PREVVIN;
		if (scan.equals(getScanCode(UniqueScanType.REFRESH)) || (StringUtils.contains(scan, getScanCode(UniqueScanType.REFRESH))))
			return UniqueScanType.REFRESH;
		if (scan.equals(getScanCode(UniqueScanType.RESETSEQ)) || (StringUtils.contains(scan, getScanCode(UniqueScanType.RESETSEQ))))
			return UniqueScanType.RESETSEQ;
		return UniqueScanType.NONE;
	}

	public static boolean isUniqueScan(Message errorMsg) {
		if (errorMsg == null)
			return false;
		if (errorMsg.getId() != null && errorMsg.getId().equals(LotControlConstants.UNIQUE_BARCODE_SCAN))
			return true;
		return false;
	}

	public boolean handleUniqueScanCode(UniqueScanType uniqueScanType) {
		final int buttonIndex = getButtonIndexForUniqueScan(uniqueScanType);
		if (isButtonEnabled(buttonIndex)) {
			clickButton(buttonIndex);
			return true;
		}
		return false;
	}

	protected void clickButton(final int i) {
		Thread t = new Thread() {
			public void run() {
				getView().getButton(i).doClick();
			}
		};
		t.start();
	}

	public DataCollectionPanelBase getView() {
		return null;
	}

	protected boolean isButtonEnabled(int i) {
		return false;
	}

	protected void runInSeparateThread(final Object request) {
		Thread t = new Thread() {
			public void run() {
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).received(request);

			}
		};

		t.start();
	}

	protected void runInSeparateThread(final IDeviceData request, final Class<?> processorClass) {
		Thread t = new Thread() {
			public void run() {
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getProcessor(processorClass).processReceived(request);
			}
		};
		t.start();
	}

	protected TerminalPropertyBean getProperty() {
		return PropertyService.getPropertyBean(TerminalPropertyBean.class, context.getAppContext().getApplicationId());
	}

	protected boolean isPopupMessage(Message errorMsg) {
		if (errorMsg == null)
			return false;

		boolean result = errorMsg.getType() == MessageType.EMERGENCY
				&& Arrays.asList(popupMsgsFilter).contains(errorMsg.getId());

		// When torque max re-try was exceeded, only pop up for the last part
		if (LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED.equals(errorMsg.getId()))
			if (DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState()
					.isLastPart())
				result = true;
		if (result) {
			disableTorqueDevices();
		}
		return result;
	}

	protected void disableTorqueDevices() {
		Event event = new Event(this, EventType.TORQUE_DISABLE_ALL);
		EventBus.publish(event);
	}

	protected void enableTorqueDevices(DataCollectionState state) {
		Event event = new Event(state, EventType.TORQUE_ENABLE);
		EventBus.publish(event);
	}

	protected int getButtonIndexForUniqueScan(UniqueScanType uniqueScanType) {
		switch (uniqueScanType) {
		case REJECT:
			return 0;
		case SKIP:
			return 1;
		case REFRESH:
			return 2;
		case NEXTVIN:
			return 3;
		case PREVVIN:
			return 4;
		default:
			return -1;
		}
	}

	protected String getScanCode(UniqueScanType uniqueScanType) {
		Map<String, String> scanMap = viewManagerProperty.getScanMap();
		return (scanMap == null ? null : scanMap.get(uniqueScanType.name()));
	}

	public void caretUpdate(CaretEvent e) {
		if(this.isCaretListenerActive)
			processChange();
	}
	
	protected void processChange() {
		if (getMessagePanel().isError()) {
			getView().getTextFieldProdId().setBackground(getView().getInitBackgroundColor());
			getView().getTextFieldProdId().setForeground(getView().getInitForegroundColor());
			getMessagePanel().clearErrorMessageArea();
		}
	}
}
