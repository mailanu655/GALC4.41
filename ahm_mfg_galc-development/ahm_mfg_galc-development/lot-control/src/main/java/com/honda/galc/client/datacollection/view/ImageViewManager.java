package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.DataCollectionError;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IImageViewObserver;
import com.honda.galc.client.datacollection.property.ImageViewProperty;
import com.honda.galc.client.datacollection.property.ImageViewPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.action.DisableExpectedProductAction;
import com.honda.galc.client.datacollection.view.action.MeasurementValueInputAction;
import com.honda.galc.client.datacollection.view.action.PartSerialNumberInputAction;
import com.honda.galc.client.datacollection.view.action.ProductButtonAction;
import com.honda.galc.client.datacollection.view.action.ProductIdInputAction;
import com.honda.galc.client.datacollection.view.action.RefreshButtonAction;
import com.honda.galc.client.datacollection.view.action.RejectButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipPartButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipProductButtonAction;
import com.honda.galc.client.datacollection.view.action.TestTorqueButtonAction;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.net.Endpoint;
import com.honda.galc.net.Request;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3>
 * The view manager for lot control clients to display images.
 *
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 *
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 12, 2017</TD>
 * <TD>1.0</TD>
 * <TD>20170912</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Mar. 20, 2018</TD>
 * <TD>1.1</TD>
 * <TD>20180320</TD>
 * <TD>Modified for multiple parts</TD>
 * </TR>
 * </TABLE>
 */
public class ImageViewManager extends ViewManagerBase implements IImageViewObserver, ConnectionStatusListener{
	public static String TEXT_EXPECTED_PRODUCT_ID ="Expected";
	protected ImageDataCollectionPanel view;
	protected JPanel viewPanel;
	public ImageViewProperty viewProperty;
	private Logger logger = Logger.getLogger();

	public ImageViewManager(ClientContext clientContext) {
		super(clientContext);
		init();
	}

	protected ImageDataCollectionPanel createDataCollectionPanel(ImageViewPropertyBean property) {
		if(view == null){
			view = new ImageDataCollectionPanel(property, viewManagerProperty.getMainWindowWidth(), viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}

	protected void init()  {
		logger.debug("Entering init");
		try {
			getViewProperty();
			view = createDataCollectionPanel(viewProperty);
			getViewPanel();

			initConnections();
			refreshScreen();

			ServiceMonitor.getInstance().registerHttpServiceListener(this);

			//Exceptions may exist before client start, for example disable gun failed
			List<DataCollectionError> errorList = getController().getState().getErrorList();
			if(errorList.size() > 0) {
				setErrorMessage(errorList.get(0).getMessage().getDescription());
			}
		} catch (Exception e) {
			logger.error(e, this.getClass().getSimpleName() + "::init() exception.");
		}
		logger.debug("Exiting init");
	}

	protected JPanel getViewPanel() {
		if(viewPanel == null) {
			viewPanel = new JPanel();
			viewPanel.setName("ImageViewPanel");
			viewPanel.setLayout(new BorderLayout());
			viewPanel.add(view, BorderLayout.CENTER);

			messageArea = getMessagePanel();
		}
		return viewPanel;
	}

	protected void getViewProperty() {
		viewProperty = PropertyService.getPropertyBean(ImageViewProperty.class, context.getProcessPointId());
	}

	protected void initConnections() throws Exception {
		logger.debug("Entering initConnections");

		super.initConnections();

		view.getJButtonProductId().setAction(new ProductButtonAction(context.getFrame(), viewProperty.getProductType(), viewProperty.getProductIdLabel(), view.getTextFieldProdId()));
		view.getTextFieldProdId().setAction(new ProductIdInputAction(context, "ProductIdInput"));
		view.getTextFieldProdId().addKeyListener(this);

		for (int i = 0; i < view.getMaxNumPart(); i++) {
			view.getPartSerialNumber(i).setAction(new PartSerialNumberInputAction(context, "PartSnInput"));
			view.getPartSerialNumber(i).addKeyListener(this);
			view.getPartSerialNumber(i).addActionListener(dataCollectionEventHandler);
		}

		for (int i = 0; i < view.getMaxNumTorq(); i++) {
			view.getTorqueValueTextField(i).setAction(new MeasurementValueInputAction(context, "MeasurementValueInput"));
			view.getTorqueValueTextField(i).addKeyListener(this);
			view.getTorqueValueTextField(i).addActionListener(dataCollectionEventHandler);
		}

		view.getButton(3).setAction(new SkipProductButtonAction(context, getButtonLabel(3)));
		view.getButton(2).setAction(new RefreshButtonAction(context, getButtonLabel(2)));
		view.getButton(1).setAction(new SkipPartButtonAction(context, getButtonLabel(1)));
		if (viewProperty.isUseDefaultViewLook())
			getEnableDisableExpectedProductOrRejectButton().setAction(new DisableExpectedProductAction(context, "Disable " + TEXT_EXPECTED_PRODUCT_ID, this));
		else
			getEnableDisableExpectedProductOrRejectButton().setAction(new RejectButtonAction(context, getButtonLabel(0)));
		view.setTestTorqueButtonAction(new TestTorqueButtonAction(context, "Test Torque"));

		logger.debug("Exiting initConnections");
	}

	protected String getButtonLabel(int idx) {
		return viewProperty.getButtonLabelMap().get(String.valueOf(idx));
	}

	@Override
	public void update(Observable o, Object arg) {
		logger.debug("Entering update");

		showErrorMessage(arg);
		super.update(o, arg);
		logger.debug("Exiting update");
	}

	private void showErrorMessage(Object arg) {
		logger.debug("Entering showErrorMessage");
		Message errorMsg = getErrorMessage((DataCollectionState) arg);
		if(errorMsg != null && !errorMsg.isEmpty()){
			setErrorMessage(errorMsg);
		}

		if(isUniqueScan(errorMsg)){
			handleUniqueScanCode(getUniqueScanType(errorMsg.getInfo()));
		}
		if(isPopupMessage(errorMsg)){
			if (getProperty().isShowErrorDialog()) {
				ErrorDialogManager mgr = new ErrorDialogManager();
				String request = mgr.showDialog(context.getFrame(), errorMsg.getInfo(), errorMsg.getId(), getProperty());
				if(request != null) {
					((DataCollectionState) arg).getStateBean().setRepair(request.equalsIgnoreCase("cancel"));

					if (request.equalsIgnoreCase("skipProduct")) {
							skipProduct();
					}
					if(errorMsg.getId().equalsIgnoreCase(LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED) && request.equalsIgnoreCase("skipPart")) {
						enableTorqueDevices((DataCollectionState) arg);
					} else {
						runInSeparateThread(new Request(request));
					}
				}

				cleanMessage((DataCollectionState) arg);

			} else {
				String msg = errorMsg.getDescription() != null ? errorMsg.getDescription() : "\n" + errorMsg.getInfo() != null ? errorMsg.getInfo() : "";
				int result = JOptionPane.showOptionDialog(context.getFrame(),msg , errorMsg.getId(), JOptionPane.DEFAULT_OPTION,
																JOptionPane.ERROR_MESSAGE, null, new Object[] { "OK" }, null);
				if (result == JOptionPane.OK_OPTION) {
					cleanMessage((DataCollectionState) arg);
					if(errorMsg.getId().equals(LotControlConstants.UNEXPECTED_PRODUCT_SCAN))
						 new RefreshButtonAction(context, "cancel", true);
				}
			}
		}
		logger.debug("Exiting showErrorMessage");
	}

	@Override
	protected boolean isButtonEnabled(int i) {
		switch (i) {
			case 0:
				return viewProperty.isEnableCancel();
			case 1:
				return viewProperty.isEnableSkipPart();
			case 2:
				return viewProperty.isEnableSkipProduct();
			case 3:
				return viewProperty.isEnableNextProduct();
			default:
				return false;
		}
	}

	public void initPartSn(ProcessPart state) {
		logger.debug("Entering initPartSn");
		requestFocus(view.getPartSerialNumber(state.getCurrentPartIndex()));
		dataCollectionEventHandler.disableKeyBuffer();
		showImage(state);
		logger.debug("Exiting initPartSn");
	}

	public void initTorque(ProcessTorque state) {
		logger.debug("Entering initTorque");

		if (viewProperty.isUseDefaultViewLook())
			this.view.getPartViewPanel().createTorqueLabelPanel(getTorqueLabelText(state));

		JTextField torqueField = getCurrentTorqueField(state);
		requestFocus(torqueField);

		if(viewProperty.isMeasurementEditable()){
			torqueField.selectAll();
			dataCollectionEventHandler.disableKeyBuffer();
		} else {
			torqueField.setEditable(true);
			if (!viewProperty.isUseDefaultViewLook()) {
				torqueField.setEditable(false);
				dataCollectionEventHandler.enableKeyBuffer();
			}
		}

		showImage(state);
		logger.debug("Exiting initTorque");
	}

	private void showImage(DataCollectionState state) {
		int imageId = 0;
		if(state.getCurrentLotControlRule().getParts().size() > 0) {
			imageId = state.getCurrentLotControlRule().getParts().get(0).getImageId();
		}
		view.showImage(imageId);
	}

	private JTextField getCurrentTorqueField(ProcessTorque state) {
		int torquePos = getCurrentTorquePosition(state);
		return view.getTorqueValueTextField(torquePos);
	}

	protected int getCurrentTorquePosition(ProcessTorque state) {
		int torquePos = 0;
		for(int i = 0; i < state.getLotControlRules().size() && i < state.getCurrentPartIndex(); i++ ) {
			torquePos += state.getLotControlRules().get(i).getParts().get(0).getMeasurementCount();
		}

		torquePos += state.getCurrentTorqueIndex();
		return torquePos;
	}

	protected void enableExpectedProductIdControlButton(boolean enabled) {
		//if the button is acting as a reject button don't touch
		if (!viewProperty.isUseDefaultViewLook())
			return;
		
		getEnableDisableExpectedProductOrRejectButton().setVisible(enabled);
		getEnableDisableExpectedProductOrRejectButton().setEnabled(enabled);
		
		if(enabled){
			getEnableDisableExpectedProductOrRejectButton().setText(getExpectedProductIdControlButtonName());
		}
	}

	private JButton getEnableDisableExpectedProductOrRejectButton() {
		return view.getButton(0);
	}

	public String getExpectedProductIdControlButtonName() {
		return context.isDisabledExpectedProductCheck() ? "Enable " + TEXT_EXPECTED_PRODUCT_ID : "Disable " + TEXT_EXPECTED_PRODUCT_ID;
	}

	public void initProductId(ProcessProduct state) {
		logger.debug("Entering initProductId");
		if(isCheckExpectedProduct() && !StringUtils.isEmpty(state.getExpectedProductId())) {
			renderExpPidOrProdSpec(view.getExpectProductIdLabel(), state.getExpectedProductId());
			enableExpectedProductIdControlButton(true);

		} else {
			enableExpectedProductIdControlButton(false);
			view.repaint();
		}

		if(isAfOnSeqNumExist()) {
			renderSeqNo(view.getAfOnSeqNumLabel(), state.getAfOnSeqNo());
		}
		if(isProductLotCountExist()){
			view.renderProductCount(view.getProductCountLabel(), state.getProductCount(), state.getLotSize());
		}

		if(isAutoProcessExpectedProduct() && !context.isManualRefresh()) {
			if(state.getExpectedProductId() != null && !state.getExpectedProductId().trim().equals("")) {
				view.getTextFieldProdId().setText(state.getExpectedProductId());
				view.getTextFieldProdId().postActionEvent();
			}
		} else {
			context.setManualRefresh(false);
		}

		logger.debug("Exiting initProductId");
	}

	public void initRefreshDelay(ProcessRefresh state) {
		dataCollectionEventHandler.disableKeyBuffer();
		logger.debug("Entering initRefreshDelay");
		UpperCaseFieldBean productId = view.getTextFieldProdId();
		productId.setEnabled(true);
		productId.setEditable(true);
		view.getJButtonProductId().setEnabled(true);
		ViewControlUtil.setSelection(productId, ViewControlUtil.VIEW_COLOR_OK);
		productId.requestFocus();
		logger.debug("Exiting initRefreshDelay");
	}

	private boolean isCheckExpectedProduct() {
		return context.isCheckExpectedProductId();
	}

	private boolean isAutoProcessExpectedProduct() {
		return context.isAutoProcessExpectedProduct();
	}

	public void partSnNg(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		partSerialNumber.setText(state.getCurrentInstallPart().getPartSerialNumber());
		partSerialNumber.setColor(ViewControlUtil.VIEW_COLOR_NG);
		ViewControlUtil.refreshObject(partSerialNumber, null, ViewControlUtil.VIEW_COLOR_NG, true);
		ViewControlUtil.setSelection(partSerialNumber, null);
		partSerialNumber.requestFocus();
	}

	public void partSnOk(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		partSerialNumber.setText(state.getCurrentInstallPart().getPartSerialNumber());
		partSerialNumber.setColor(ViewControlUtil.VIEW_COLOR_OK);
		ViewControlUtil.refreshObject(partSerialNumber, null, ViewControlUtil.VIEW_COLOR_OK, false);
		moveTextFieldHighlight(partSerialNumber, ViewControlUtil.VIEW_COLOR_FONT, ViewControlUtil.VIEW_COLOR_OK);

		clearMessageArea(state);
	}


	public void productIdOk(ProcessProduct state) {
		logger.debug("Entering productIdOk");

		//Show product Id status
		view.getTextFieldProdId().setText(state.getProductId());
		view.getTextFieldProdId().setColor(ViewControlUtil.VIEW_COLOR_OK);
		moveTextFieldHighlight(view.getTextFieldProdId(), null, ViewControlUtil.VIEW_COLOR_OK);

		//Show product spec
		renderExpPidOrProdSpec(view.getProdSpecLabel(), state.getProductSpecCode());
		if(isAfOnSeqNumExist()){
			renderSeqNo(view.getAfOnSeqNumLabel(), getSequenceNumberFromState(state));
		}
		if(isProductLotCountExist()){
			view.renderProductCount(view.getProductCountLabel(), state.getProductCount(), state.getLotSize());
		}

		//Show data collection buttons
		for(int i = 0; i < 4; i++) {
			if (i == 0)
				buttonControl(view.getButton(i), !viewProperty.isUseDefaultViewLook(), isButtonEnabled(i));
			else
				buttonControl(view.getButton(i), true, isButtonEnabled(i));
		}

		view.setTestTorqueButtonVisible(false);
		view.getJButtonProductId().setEnabled(false);

		//init display
		view.createView(state.getLotControlRules());
		clearMessageArea(state);

		if(state.getProduct().isMissingRequiredPart()) {
			setErrorMessage(state.getProduct().getMissingRequiredPartMessage());
		}
		notifyNewProduct(state.getProductId());
		logger.debug("Exiting productIdOk");
	}

	private String getSequenceNumberFromState(ProcessProduct state) {
		String sequenceNumber = state.getProduct().getAfOnSequenceNumber();
		if (StringUtil.isNullOrEmpty(sequenceNumber) && state.getProduct() != null && state.getProduct().getProductId() != null) {
			Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(state.getProduct().getProductId());
			if (frame !=  null) {
				Integer sequenceNumberFromFrame = frame.getLineRef(getProperty().getAfOnSeqNumDisplayLength());
				if (sequenceNumberFromFrame != null)
					sequenceNumber = sequenceNumberFromFrame.toString();
			}
		}
		return sequenceNumber;
	}

	protected void torqueVisibleControl(List<LotControlRule> lotControlRules) {
		final String defaultTorque = viewProperty.getDefaultTorqueValue();
		int torqueCount = 0;

		for (int i = 0; i < lotControlRules.size(); i++) {
			for (int j = 0; j < lotControlRules.get(i).getParts().get(0).getMeasurementCount(); j++) {

				// set Torque Point
				JTextField textField = view.getTorqueValueTextField(torqueCount);
				textField.setBackground(Color.white);
				textField.setText(defaultTorque);
				textField.setVisible(true);
				textField.setEditable(false);

				torqueCount++;
			}
		}
	}

	private void renderExpPidOrProdSpec(String label, String text) {
		view.getLabelExpPIDOrProdSpec().setText(label);
		view.getLabelExpPIDOrProdSpec().setVisible(true);

		view.getTextFieldExpPidOrProdSpec().setText(text);
		view.getTextFieldExpPidOrProdSpec().setVisible(true);
		view.getLabelExpPIDOrProdSpec().repaint();
	}

	protected void renderSeqNo(String label, String text) {
		view.getLabelAfOnSeqNum().setText(label);
		view.getLabelAfOnSeqNumValue().setText(getSequenceNumberText(text));
		view.getLabelAfOnSeqNum().setVisible(true);
		view.getLabelAfOnSeqNumValue().setVisible(true);
	}

	private String getSequenceNumberText(String text) {
		if (StringUtil.isNullOrEmpty(text))
			return "N/A";
		
		int displayLength = getProperty().getAfOnSeqNumDisplayLength();
		return StringUtil.padLeft(
				text.length()> displayLength
					? text.substring(text.length() - displayLength)
					: text,
				displayLength,
				'0');
	}

	public void productIdNg(ProcessProduct state) {
		logger.debug("Entering productIdNg");

		view.getTextFieldProdId().setText(state.getProductId());
		view.getTextFieldProdId().setColor(ViewControlUtil.VIEW_COLOR_NG);
		ViewControlUtil.refreshObject(view.getTextFieldProdId(), null, ViewControlUtil.VIEW_COLOR_NG, true);
		logger.debug("Exiting productIdNg");
		view.getTextFieldProdId().selectAll();
	}

	public void setProductInputFocused() {
		view.getTextFieldProdId().requestFocus();
	}

	public void receivedPartSn(ProcessPart state) {
		int idx = state.getCurrentPartIndex();
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(idx);
		partSerialNumber.setText(state.getCurrentInstallPart().getPartSerialNumber());
		if(state.getCurrentPartScanCount() > 1) {
			ViewControlUtil.refreshObject(partSerialNumber,state.getCurrentInstallPart().getPartSerialNumber(), ViewControlUtil.VIEW_COLOR_WAIT, true);
			partSerialNumber.selectAll();
			partSerialNumber.requestFocus();
		}
	}

	public void receivedProductId(ProcessProduct state) {
		view.getTextFieldProdId().setText(state.getProductId());
	}

	public void skipPart(int idx) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(idx);
		partSerialNumber.setColor(ViewControlUtil.VIEW_COLOR_NG);
		ViewControlUtil.refreshObject(partSerialNumber, null, ViewControlUtil.VIEW_COLOR_NG, false);
		partSerialNumber.setEditable(false);
	}

	public void torqueNg(ProcessTorque state) {
		logger.debug("Entering torqueNg");
		Measurement bean = state.getCurrentTorque();
		JTextField torqueValueTextField = view.getTorqueValueTextField(getCurrentTorquePosition(state));

		view.showTorque(bean, torqueValueTextField, ViewControlUtil.VIEW_COLOR_NG);
		//if isMeasurementEditable is true, measurement value can be entered manually
		if(viewProperty.isMeasurementEditable()){
			torqueValueTextField.setBackground(ViewControlUtil.VIEW_COLOR_NG);
			torqueValueTextField.selectAll();
		}
		requestFocus(torqueValueTextField);
		torqueValueTextField.setBackground(ViewControlUtil.VIEW_COLOR_NG);
		Logger.getLogger().check(getClass().getName()+": Bad Torque value "  + bean.getMeasurementValue() + " displayed");

		if(state.getMessage() != null && state.getMessage().getType() == MessageType.EMERGENCY) {
			view.displayMessageDialog(state.getMessage().getDescription());
		}
		logger.debug("Exiting torqueNg");
	}

	public void torqueOk(final ProcessTorque state) {
		logger.debug("Entering torqueOk");
		final int currentIndex = getCurrentTorquePosition(state);
		final Measurement bean = state.getCurrentTorque();
		final JTextField nextTorqueField = findNextTorqueField(state);
		Runnable r = new Runnable() {
			public void run()
			{
				JTextField torqueValueTextField = view.getTorqueValueTextField(currentIndex);
				view.showTorque(bean, torqueValueTextField, ViewControlUtil.VIEW_COLOR_OK);
				logger.check("Good Torque value "  + bean.getMeasurementValue() + " displayed");

				moveTextFieldHighlight(torqueValueTextField, null, null);

				if(nextTorqueField != null && nextTorqueField.isVisible()) {
					requestFocus(nextTorqueField);
					nextTorqueField.selectAll();
				}
			}
		};
		SwingUtilities.invokeLater(r);

		clearMessageArea(state);
		logger.debug("Exiting torqueOk");

	}

	private JTextField findNextTorqueField(ProcessTorque state) {
		int currentPos = getCurrentTorquePosition(state);

		if((currentPos + 1) < view.getMaxNumTorq()) {
			return view.getTorqueValueTextField(currentPos + 1);
		}
		return null;
	}

	public void notifyError(DataCollectionState state) {
		if(state instanceof ProcessPart){
			if(state.getCurrentPartScanCount() > 1) {
				int idx = state.getCurrentPartIndex();
				view.getPartSerialNumber(idx).setText("");
				requestFocus(view.getPartSerialNumber(idx));
			}
		}
	}

	public void skipProduct(DataCollectionState state) {
		view.skipProduct();
	}

	public void skipPart(DataCollectionState state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		partSerialNumber.setColor(ViewControlUtil.VIEW_COLOR_NG);
		ViewControlUtil.refreshObject(partSerialNumber, null, ViewControlUtil.VIEW_COLOR_NG, false);
		moveTextFieldHighlight(partSerialNumber, ViewControlUtil.VIEW_COLOR_FONT, ViewControlUtil.VIEW_COLOR_NG);
		partSerialNumber.setEditable(false);

	}

	public void completeProductId(ProcessRefresh state) {
		logger.debug("Entering completeProductId");
		if(!StringUtils.isEmpty(state.getProductId())){
			view.getTextFieldLastPid().setText(state.getProductId());
		}

		refreshScreen();
		notifyFinishProduct();
		notifySkippedPartProduct();
		logger.debug("Exiting completeProductId");
	}

	public void refreshScreen(DataCollectionState state) {
		logger.debug("Entering refreshScreen(DataCollectionState state)");
		refreshScreen();
		notifyFinishProduct();
		logger.debug("Exiting refreshScreen(DataCollectionState state)");
	}

	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		if (lotControlRules == null || lotControlRules.size() == 0) {
			return;
		}

		try {
			view.getTextFieldExpPidOrProdSpec().setVisible(true);

			// control Part
			for (int i = 0;((i < view.getMaxNumPart()) && (i < lotControlRules.size())); i++) {
				if (lotControlRules.get(i) != null && (lotControlRules.get(i).getSerialNumberScanFlag() == 1 || lotControlRules.get(i).isDateScan())) {
					PartName partName = lotControlRules.get(i).getPartName();
					String partMask = CommonPartUtility.parsePartMaskDisplay(lotControlRules.get(i).getPartMasks());
					partMask = partMask.contains("<<")?partMask.replace("<", "&lt;"):partMask;
					partMask = partMask.contains(">>")?partMask.replace(">", "&gt;"):partMask;
					String label = "<HTML><p align='right'>" + partName.getWindowLabel() + ":" + "<br>" + partMask + "</p></HTML>";
					view.getPartLabel(i).setText(label);
					view.setFont(new java.awt.Font("dialog", 0, 18));
					view.getPartLabel(i).setVisible(true);
					view.getPartSerialNumber(i).setVisible(true);
				}

			}
		} catch (Exception e) {
			logger.error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	public void partVisibleControl(boolean bEnable, boolean bVisible) {
		try {
			view.getTextFieldExpPidOrProdSpec().setVisible(bVisible);
			view.getLabelExpPIDOrProdSpec().setVisible(bVisible);

			for (int i = 0; i < view.getMaxNumPart(); i++) {
				view.getPartLabel(i).setVisible(bVisible);
				view.getPartSerialNumber(i).setVisible(bVisible);
				view.getPartSerialNumber(i).setColor(ViewControlUtil.VIEW_COLOR_INPUT);
				view.getPartSerialNumber(i).setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
				ViewControlUtil.refreshObject(view.getPartSerialNumber(i), "", ViewControlUtil.VIEW_COLOR_INPUT, bEnable);
			}

			// set Enable/Visible Torque
			view.getLabelTorque().setVisible(bVisible);
			for (int i = 0; i < view.getMaxNumTorq(); i++) {
				view.getTorqueValueTextField(i).setVisible(bVisible);
				view.getTorqueValueTextField(i).setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i), "", ViewControlUtil.VIEW_COLOR_INPUT, bEnable);
			}

			view.getButton(3).setVisible(viewProperty.isUseDefaultViewLook());
			view.getButton(2).setVisible(!viewProperty.isUseDefaultViewLook());
			view.getButton(1).setVisible(bVisible);
			getEnableDisableExpectedProductOrRejectButton().setVisible(viewProperty.isUseDefaultViewLook());

			view.setTestTorqueButtonVisible(!bVisible);
		} catch (Exception e) {
			logger.error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	public void refreshScreen() {
		logger.debug("Entering refreshScreen()");
		dataCollectionEventHandler.disableKeyBuffer();
		view.reset();
		try
		{
			//disable expected expired.
			context.setDisabledExpectedProductCheck(false);

			// set Visible
			view.setVisible(true);
			//Enable VIN Button
			view.getJButtonProductId().setEnabled(true);

			view.getTextFieldProdId().setText("");
			requestFocus(view.getTextFieldProdId());

			view.getLabelExpPIDOrProdSpec().setVisible(true);
			getEnableDisableExpectedProductOrRejectButton().setVisible(viewProperty.isUseDefaultViewLook());
			view.getButton(2).setVisible(!viewProperty.isUseDefaultViewLook());
			view.getButton(3).setVisible(viewProperty.isUseDefaultViewLook());
			view.setTestTorqueButtonVisible(true);
			ViewControlUtil.refreshObject(view.getLabelExpPIDOrProdSpec(), view.getProdSpecLabel());
			ViewControlUtil.refreshObject(view.getTextFieldExpPidOrProdSpec(), "", ViewControlUtil.VIEW_COLOR_INPUT, false);
			view.getLabelAfOnSeqNum().setVisible(false);
			view.getLabelAfOnSeqNumValue().setVisible(false);
			view.getLabelProductCount().setVisible(false);
			view.getLabelProductCountValue().setVisible(false);
			if(isAfOnSeqNumExist()) {
				view.getLabelAfOnSeqNum().setVisible(true);
				view.getLabelAfOnSeqNumValue().setVisible(true);
				ViewControlUtil.refreshObject(view.getLabelAfOnSeqNum(), view.getAfOnSeqNumLabel());
				ViewControlUtil.refreshObject(view.getLabelAfOnSeqNumValue(), "",	ViewControlUtil.VIEW_COLOR_INPUT, false);
			}
			if(isProductLotCountExist()){
				view.getLabelProductCount().setVisible(true);
				view.getLabelProductCountValue().setVisible(true);
				ViewControlUtil.refreshObject(view.getLabelProductCount(), view.getProductCountLabel());
				ViewControlUtil.refreshObject(view.getLabelProductCountValue(),	"",	ViewControlUtil.VIEW_COLOR_INPUT,false);
			}
			for (int i = 0; i < view.getMaxNumPart(); i++) {
				view.getPartSerialNumber(i).setMaximumLength(view.getMaxPartTexLength());
			}

			// refresh Part/Torque
			partVisibleControl(false, false);
			// refresh Button
			getEnableDisableExpectedProductOrRejectButton().setEnabled(viewProperty.isUseDefaultViewLook());
			view.getButton(2).setEnabled(!viewProperty.isUseDefaultViewLook());
			view.getButton(3).setEnabled(viewProperty.isUseDefaultViewLook());
			view.getTextFieldProdId().setEnabled(true);
			view.requestFocusInvokeLater(view.getTextFieldProdId());

			if(isCheckExpectedProduct() && !StringUtils.isEmpty(getCurrentState().getExpectedProductId()))
				enableExpectedProductIdControlButton(true);

			messageArea.setErrorMessageArea(null);

		} catch (Exception e) {
			logger.error(e, this.getClass().getSimpleName() + "::refreshScreen() exception.");
		}

		logger.debug("Exiting refreshScreen()");
	}

	public DataCollectionState getCurrentState(){
		return DataCollectionController.getInstance().getState();
	}

	public JPanel getClientPanel() {
		return getViewPanel();
	}

	public void requestFocus(JTextField textField) {
		highlightTextField(textField);
		textField.requestFocus();
		view.showCurrentFocusField(textField);
	}

	public void requestFocus(UpperCaseFieldBean textField) {
		textField.setColor(Color.WHITE);
		highlightTextField(textField);
		textField.requestFocus();
		view.showCurrentFocusField(textField);
	}

	private void highlightTextField(JTextField textField) {
		if(this.viewProperty.isUseDefaultViewLook()) {
			textField.setBackground(Color.BLUE);
			textField.setForeground(Color.WHITE);
		} else {
			textField.setBorder(BorderFactory.createLineBorder(Color.BLUE,8));
			textField.setBackground(Color.WHITE);
		}
		textField.setSelectionColor(new Color(204, 204, 255));
		textField.setEnabled(true);
		textField.setEditable(true);
		textField.repaint();
		logger.check(textField.getName() + " has been highlighted");
	}

	protected void moveTextFieldHighlight(JTextField textField, Color foreground, Color background) {
		if(foreground != null) {
			textField.setForeground(foreground);
		}

		if(background != null) {
			textField.setBackground(background);
		}
		textField.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		textField.setEditable(false);
		textField.setEnabled(false);
		textField.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		if(foreground == null || background == null) {
			logger.check(textField.getName() + " has been highlighted to null");
		} else {
			logger.check(textField.getName() + " has been highlighted to Foreground:" + foreground.toString() + " Background: " + background.toString());
		}
	}

	public void rejectPart(ProcessPart state) {
		dataCollectionEventHandler.disableKeyBuffer();
		int currentPartIndex = state.getCurrentPartIndex();
		logger.debug("--reject:" + currentPartIndex);

		UpperCaseFieldBean rejected = view.getPartSerialNumber(state.getCurrentPartIndex() + 1);
		if(rejected != null && rejected.isVisible()) {
			rejected.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		}

		requestFocus(view.getPartSerialNumber(currentPartIndex));
		view.getPartSerialNumber(currentPartIndex).selectAll();
		showImage(state);
	}

	public void rejectTorque(ProcessTorque state) {
		logger.check("rejectTorque:" + state.getCurrentTorqueIndex());
		int pos = getCurrentTorquePosition(state);

		if (view.getTorqueValueTextField(pos + 1) != null) {
			view.getTorqueValueTextField(pos + 1).setText(viewProperty.getDefaultTorqueValue());
			view.getTorqueValueTextField(pos + 1).setBackground(Color.WHITE);
			view.getTorqueValueTextField(pos + 1).setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		}

		if(pos >= 0) {
			view.getTorqueValueTextField(pos).setText(viewProperty.getDefaultTorqueValue());
			requestFocus(view.getTorqueValueTextField(pos));
			if(viewProperty.isMeasurementEditable()){
				view.getTorqueValueTextField(pos).selectAll();
			}
		}

		if(state.getCurrentTorqueIndex() < 0) {
			state.setCurrentPartIndex(state.getCurrentPartIndex() - 1);
		}
		showImage(state);
		clearMessageArea(state);
	}

	public void rejectLastInput(DataCollectionState state) {
		logger.check("rejectLastInput:" + state.getCurrentPartIndex() + " - " + state.getCurrentTorqueIndex());
		clearMessageArea(state);

		//Disable Product Id button and text field
		view.getTextFieldProdId().setBackground(view.getTextFieldProdId().getColor());
		view.getJButtonProductId().setEnabled(false);
		view.getTextFieldProdId().setEnabled(false);
		view.getTextFieldProdId().setText(state.getProductId());
		showImage(state);
	}

	public void message(DataCollectionState state) {
		if (state.getMessage() == null) {
			return;
		}

		if(state.getMessage().getId().equals(StatusMessage.SERVER_ON_LINE) &&
				state instanceof ProcessProduct && !StringUtils.isEmpty(state.getExpectedProductId())) {
			initProductId((ProcessProduct)state);
		}
	}

	public void serviceStatusChanged(Endpoint endpoint) {
		if(context.isOnLine() != endpoint.isConnected()) {
			context.setOnLine(endpoint.isConnected());
		}
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, endpoint.isConnected()));
	}

	public void statusChanged(ConnectionStatus status) {
		if(context.isOnLine() != status.isConnected()) {
			context.setOnLine(status.isConnected());
		}
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, status.isConnected()));
	}

	public void skipCurrentInput(final ProcessTorque state) {
		final int currentIndex = getCurrentTorquePosition(state);
		final Measurement bean = state.getCurrentTorque();
		final JTextField nextTorqueField = findNextTorqueField(state);
		Runnable r = new Runnable() {
			public void run()
			{
				JTextField torqueValueTextField = view.getTorqueValueTextField(currentIndex);
				view.showTorque(bean, torqueValueTextField, ViewControlUtil.VIEW_COLOR_NG);

				moveTextFieldHighlight(torqueValueTextField, null, null);

				if(!state.isLastTorqueOnCurrentPart()) {
					if(nextTorqueField != null && nextTorqueField.isVisible()) {
						requestFocus(nextTorqueField);
					}
					nextTorqueField.selectAll();
				}
			}
		};
		SwingUtilities.invokeLater(r);

		clearMessageArea(state);
	}

	public DataCollectionPanelBase getView() {
		return view;
	}

	public void setView(ImageDataCollectionPanel view) {
		this.view = view;
	}

	protected DataCollectionController getController() {
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId());
	}

	//Required by interface. Do nothing here.
	public void completePartSerialNumber(ProcessPart state) {
	}

	public void completeCollectTorques(ProcessTorque state) {
	}

	public void enableExpectedProduct(boolean enabled) {
		getView().getTextFieldExpPidOrProdSpec().setVisible(enabled);
		getView().getLabelExpPIDOrProdSpec().setVisible(enabled);

		getView().getTextFieldProdId().repaint();
		requestFocus(getView().getTextFieldProdId());
	}

	protected String getTorqueLabelText(DataCollectionState state) {
		return getTorqueLabelText(state, state.getCurrentTorqueIndex());
	}

	protected String getTorqueLabelText(DataCollectionState state, int torqueIndex) {
		String torqueLabel = "Torque:";
		List<MeasurementSpec> measurementSpecs = state.getCurrentLotControlRulePartList().get(0).getMeasurementSpecs();
		MeasurementSpec measurementSpec = torqueIndex < measurementSpecs.size()?measurementSpecs.get(torqueIndex): null;
		if (measurementSpec != null) {
			String minLimit = String.valueOf(measurementSpec.getMinimumLimit());
			String maxLimit = String.valueOf(measurementSpec.getMaximumLimit());
			String partName = String.valueOf(state.getCurrentPartName());
			String instructionCode = state.getCurrentLotControlRule().getInstructionCode();
			String label = "<HTML><p align='right'>" + torqueLabel + "<br>" + partName + "<br> Min/Max " + minLimit + "/" + maxLimit
					+ " <br>PSet# ("+instructionCode+") </p></HTML>";
			return label;
		}
		return torqueLabel;
	}


	public void partSnOkButWait(ProcessPart state) {}

	public void receivedBypass(ProcessPart state) {}

	public void receivedAuto(ProcessPart state) {}

}