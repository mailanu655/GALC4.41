package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IClassicViewObserver;
import com.honda.galc.client.datacollection.property.ClassicViewProperty;
import com.honda.galc.client.datacollection.property.ClassicViewPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
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
import com.honda.galc.entity.product.Engine;
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
 * <h3>ClassicViewManager</h3>
 * <h4>
 * Classic view controller.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
 /** * *
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ClassicViewManager extends ViewManagerBase
implements IClassicViewObserver, ConnectionStatusListener{

	protected ClassicDataCollectionPanel view;
	protected JPanel viewPanel;
	public ProductButtonAction productButtonAction;
	public ClassicViewProperty viewProperty;


	public ClassicViewManager(ClientContext clientContext) {
		super(clientContext);

		init();
	}

	protected ClassicDataCollectionPanel createDataCollectionPanel(
			ClassicViewPropertyBean property) {
		if(view == null){
			view = new ClassicDataCollectionPanel(property, viewManagerProperty.getMainWindowWidth(),
					viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}

	protected void init()  {
		Logger.getLogger().debug(getClass().getName()+": entering init");
		try {
			getViewProperty();
			view = createDataCollectionPanel(viewProperty);
			getViewPanel();

			initConnections();
			refreshScreen();

			ServiceMonitor.getInstance().registerHttpServiceListener(this);

			//Exceptions may exist before client start, for example disable gun failed
			if(DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState().getErrorList().size() > 0){
				setErrorMessage(DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState().getErrorList().get(0).getMessage().getDescription());
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::init() exception.");
		}
		Logger.getLogger().debug(getClass().getName()+": exiting init");
	}

	protected JPanel getViewPanel() {
		if(viewPanel == null){
			viewPanel = new JPanel();
			viewPanel.setName("ClassicViewContentPanel");
			viewPanel.setLayout(new BorderLayout());
			viewPanel.add(view, BorderLayout.CENTER);

			messageArea = getMessagePanel();

		}
		return viewPanel;
	}

	protected void getViewProperty() {
		viewProperty = PropertyService.getPropertyBean(ClassicViewProperty.class, context.getProcessPointId());
	}

	protected void initConnections() throws Exception {
		Logger.getLogger().debug(getClass().getName()+": entering initConnections");

		super.initConnections();

		view.getJButtonProductId().setAction(new ProductButtonAction(context.getFrame(), viewProperty.getProductType(), getButtonLabel(), view.getTextFieldProdId()));
		view.getTextFieldProdId().setAction(new ProductIdInputAction(context, "ProductIdInput"));
		view.getTextFieldProdId().addKeyListener(this);
		view.getTextFieldProdId().addCaretListener(this);

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
		view.getButton(0).setAction(new RejectButtonAction(context, getButtonLabel(0)));
		view.setTestTorqueButtonAction(new TestTorqueButtonAction(context, "Test Torque"));

		Logger.getLogger().debug(getClass().getName()+": exiting initConnections");
	}

	protected String getButtonLabel(int idx) {
		return viewProperty.getButtonLabelMap().get(String.valueOf(idx));
	}

	protected String getButtonLabel()	{
		return viewProperty.getProductIdLabel();
	}

	@Override
	public void update(Observable o, Object arg) {
		Logger.getLogger().debug(getClass().getName()+": entering update");

		showErrorMessage(arg);
			super.update(o, arg);
			Logger.getLogger().debug(getClass().getName()+": exiting update");
	}

	private void showErrorMessage(Object arg) {
		Logger.getLogger().debug(getClass().getName()+": entering showErrorMessage");
		Message errorMsg = getErrorMessage((DataCollectionState)arg);
		if(errorMsg != null && !errorMsg.isEmpty()){
			setErrorMessage(errorMsg);
		}
		if(isUniqueScan(errorMsg)){
			handleUniqueScanCode(getUniqueScanType(errorMsg.getInfo()));
		}
		if(isPopupMessage(errorMsg)){
			boolean showErrorDialog = getProperty().isShowErrorDialog();
			if (showErrorDialog) {
				ErrorDialogManager mgr = new ErrorDialogManager();
				String request = mgr.showDialog(context.getFrame(), errorMsg.getInfo(), errorMsg.getId(), getProperty());
				if(request != null) {
					if (request.equalsIgnoreCase("cancel")) {
						((DataCollectionState) arg).getStateBean().setRepair(true);
					} else {
						((DataCollectionState) arg).getStateBean().setRepair(false);
					}
					if (request.equalsIgnoreCase("skipProduct")) {
						skipProduct();
					}
					if(errorMsg.getId().equalsIgnoreCase(LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED) && request.equalsIgnoreCase("skipPart") ){
						enableTorqueDevices((DataCollectionState)arg);
					}else{
						runInSeparateThread(new Request(request));
					}
				}
				 cleanMessage((DataCollectionState)arg);

			} else {
				String msg = errorMsg.getDescription() != null ?errorMsg.getDescription():""+"\n"+errorMsg.getInfo()!= null?errorMsg.getInfo():"";
					int result = JOptionPane.showOptionDialog(context.getFrame(),msg , errorMsg.getId(), JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE, null, new Object[] { "OK" }, null);
				if (result == JOptionPane.OK_OPTION) {
					cleanMessage((DataCollectionState)arg);
					if(errorMsg.getId().equals(LotControlConstants.UNEXPECTED_PRODUCT_SCAN))
						 new RefreshButtonAction(context, "cancel", true);
				}
			}
		}
		Logger.getLogger().debug(getClass().getName()+": exiting showErrorMessage");
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



	public void completeCollectTorques(ProcessTorque state) {

	}

	public void initPartSn(ProcessPart state) {

		requestFocus(view.getPartSerialNumber(state.getCurrentPartIndex()));
	}

	public void initTorque(ProcessTorque state) {
		Logger.getLogger().debug(getClass().getName()+": entering initTorque");

		JTextField torqueField = getCurrentTorqueField(state);

		requestFocus(torqueField);
		if(viewProperty.isMeasurementEditable()){
			torqueField.selectAll();
			dataCollectionEventHandler.disableKeyBuffer();
		} else {
			torqueField.setEditable(false);
			dataCollectionEventHandler.enableKeyBuffer();
		}

		Logger.getLogger().debug(getClass().getName()+": exiting initTorque");
	}

	private JTextField getCurrentTorqueField(ProcessTorque state) {
		view.getPartSerialNumber(state.getCurrentPartIndex());
		int torquePos = getCurrentTorquePosition(state);
		return view.getTorqueValueTextField(torquePos);
	}

	protected int getCurrentTorquePosition(ProcessTorque state) {
		int torquePos = 0;
		for(int i = 0; i < state.getLotControlRules().size() && i < state.getCurrentPartIndex(); i++ ){
			torquePos += state.getLotControlRules().get(i).getParts().get(0).getMeasurementCount();
		}

		torquePos += state.getCurrentTorqueIndex();
		return torquePos;
	}

	public void initProductId(ProcessProduct state) {
		Logger.getLogger().debug(getClass().getName()+": entering initProductId");
		if(isCheckExpectedProduct())
			renderExpPidOrProdSpec(view.getExpectProductIdLabel(), state.getExpectedProductId());
		if(isAfOnSeqNumExist())
			renderSeqNo(view.getAfOnSeqNumLabel(), state.getAfOnSeqNo());
		if(isProductLotCountExist()){
			view.renderProductCount(view.getProductCountLabel(), state.getProductCount(), state.getLotSize());
		}
		if(isAutoProcessExpectedProduct() && !context.isManualRefresh()){
			processNextProduct(state);
		}else{
			context.setManualRefresh(false);
		}
		Logger.getLogger().info(getClass().getName()+": exiting initProductId");

	}

	private void processNextProduct(ProcessProduct state) {
		// bug fix to avoid the vicious loop which consumes all available CPU on the client
		if (state.getExpectedProductId() != null && !state.getExpectedProductId().trim().equals("")) {
			if (!view.getTextFieldProdId().getText().trim().equals(state.getExpectedProductId())) {
				view.getTextFieldProdId().setText(state.getExpectedProductId());
			}
			if (view.getTextFieldProdId().getColor().equals(ViewControlUtil.VIEW_COLOR_INPUT)) {
				view.getTextFieldProdId().postActionEvent();
			}
		}
	}

	public void initRefreshDelay(ProcessRefresh state) {
		dataCollectionEventHandler.disableKeyBuffer();
		Logger.getLogger().debug(getClass().getName()+": entering initRefreshDelay");
		UpperCaseFieldBean productId = view.getTextFieldProdId();
		productId.setEnabled(true);
		productId.setEditable(true);
		view.getJButtonProductId().setEnabled(true);
		ViewControlUtil.setSelection(productId, ViewControlUtil.VIEW_COLOR_OK);
		productId.requestFocus();
		Logger.getLogger().debug(getClass().getName()+": exiting initRefreshDelay");
	}

	private boolean isCheckExpectedProduct() {
		return context.isCheckExpectedProductId();
	}

	private boolean isAutoProcessExpectedProduct() {
		return context.isAutoProcessExpectedProduct();
	}

	public void partSnNg(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		renderFieldBeanNg(partSerialNumber, state.getCurrentInstallPart().getPartSerialNumber());
		partSerialNumber.requestFocus();
	}
	
	protected void renderFieldBeanNg(UpperCaseFieldBean bean, String text) {
		bean.setText(text);
		bean.setColor(ViewControlUtil.VIEW_COLOR_NG);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_NG);
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		bean.setEnabled(true);
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
		Logger.getLogger().debug(getClass().getName()+": entering productIdOk");

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
		buttonControl(view.getButton(0), true, isButtonEnabled(0));
		buttonControl(view.getButton(1), true, isButtonEnabled(1));
		buttonControl(view.getButton(2), true, isButtonEnabled(2));
		buttonControl(view.getButton(3), true, isButtonEnabled(3));

		view.setTestTorqueButtonVisible(false);
		//Disable ProductId Button
		view.getJButtonProductId().setEnabled(false);

		//init display
		partVisibleControl(state.getLotControlRules());
		torqueVisibleControl(state.getLotControlRules());

		if(viewProperty.isEnableRefreshAfterProductScan()) {
			view.getButton(2).setEnabled(true);
			view.getButton(2).setVisible(true);
		}

		clearMessageArea(state);

		if(state.getProduct().isMissingRequiredPart())
			setErrorMessage(state.getProduct().getMissingRequiredPartMessage());
		notifyNewProduct(state.getProductId());
		Logger.getLogger().debug(getClass().getName()+": exiting productIdOk");
	}

	private String getSequenceNumberFromState(ProcessProduct state) {
		String sequenceNumber = state.getProduct().getAfOnSequenceNumber();
		if (StringUtil.isNullOrEmpty(sequenceNumber) && state.getProduct() != null && state.getProduct().getProductId() != null) {
			Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(state.getProduct().getProductId());
			if (frame !=  null) {
				sequenceNumber = frame.getLineRef(getProperty().getAfOnSeqNumDisplayLength()).toString();
			}
		}
		return sequenceNumber;
	}
	
	protected void torqueVisibleControl(List<LotControlRule> lotControlRules) {
		final String defaultTorque = viewProperty.getDefaultTorqueValue();
		int torqueCount = 0;

		for (int i = 0; i < lotControlRules.size(); i++) {
			List<MeasurementSpec> measurementSpecs = lotControlRules.get(i).getParts().get(0).getMeasurementSpecs();
			for (int j = 0; j < lotControlRules.get(i).getParts().get(0).getMeasurementCount(); j++) {

				// set Torque Point
				JTextArea textArea = view.getTorqueValueTextArea(torqueCount);
				JTextField textField = view.getTorqueValueTextField(torqueCount);
				textField.setBackground(Color.white);
				textField.setText(defaultTorque);
				textField.setVisible(true);
				textField.setEditable(false);

				MeasurementSpec measurementSpec = measurementSpecs.get(j);
				textArea.setText(lotControlRules.get(i).getId().getPartName() + "\n" + "Seq" + (j +1) + "   MIN-MAX "
						+ measurementSpec.getMinimumLimit() + " - " + measurementSpec.getMaximumLimit());
				textArea.setVisible(true);

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
		view.getLabelAfOnSeqNumValue().setText(text!= null?StringUtil.padLeft(text,5,'0'):"N/A");
		view.getLabelAfOnSeqNumValue().setVisible(true);
	}

	public void productIdNg(ProcessProduct state) {
		this.isCaretListenerActive = false;
		Logger.getLogger().debug(getClass().getName()+": entering productIdNg");

		view.getTextFieldProdId().setText(state.getProductId());
		view.getTextFieldProdId().setColor(ViewControlUtil.VIEW_COLOR_NG);
		ViewControlUtil.refreshObject(view.getTextFieldProdId(), null,
				ViewControlUtil.VIEW_COLOR_NG, true);
		Logger.getLogger().debug(getClass().getName()+": exiting productIdNg");
		view.getTextFieldProdId().selectAll();
		this.isCaretListenerActive = true;
	}

	public void setProductInputFocused() {
		view.getTextFieldProdId().requestFocus();

	}

	public void receivedPartSn(ProcessPart state) {
		clearMessageArea(state);
		int idx = state.getCurrentPartIndex();
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(idx);
		partSerialNumber.setText(state.getCurrentInstallPart().getPartSerialNumber());
		if(state.getCurrentPartScanCount() > 1){
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
		Logger.getLogger().debug(getClass().getName()+": entering torqueNg");
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

		if(state.getMessage() != null && state.getMessage().getType() == MessageType.EMERGENCY){
			view.displayMessageDialog(state.getMessage().getDescription());
		}
		Logger.getLogger().debug(getClass().getName()+": exiting torqueNg");
	}

	public void torqueOk(final ProcessTorque state) {
		Logger.getLogger().debug(getClass().getName()+": entering torqueOk");
		final int currentIndex = getCurrentTorquePosition(state);
		final Measurement bean = state.getCurrentTorque();
		final JTextField nextTorqueField = findNextTorqueField(state);
		Runnable r = new Runnable() {
			public void run()
			{
				JTextField torqueValueTextField = view.getTorqueValueTextField(currentIndex);
				view.showTorque(bean, torqueValueTextField, ViewControlUtil.VIEW_COLOR_OK);
				Logger.getLogger().check(getClass().getName()+": Good Torque value "  + bean.getMeasurementValue() + " displayed");

				moveTextFieldHighlight(torqueValueTextField, null, null);

				if(nextTorqueField != null && nextTorqueField.isVisible())
					requestFocus(nextTorqueField);
					nextTorqueField.selectAll();
			}
		};
		SwingUtilities.invokeLater(r);

		clearMessageArea(state);
		Logger.getLogger().debug(getClass().getName()+": exiting torqueOk");

	}

	private JTextField findNextTorqueField(ProcessTorque state) {
		int currentPos = getCurrentTorquePosition(state);

		if((currentPos +1) < view.getMaxNumTorq())
			return view.getTorqueValueTextField(currentPos +1);


		return null;

	}

	public void notifyError(DataCollectionState state) {
		if(state instanceof ProcessPart){
			if(state.getCurrentPartScanCount() > 1){
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

	public void completePartSerialNumber(ProcessPart state) {
		Logger.getLogger().debug(getClass().getName()+": entering completePartSerialNumber");
		Logger.getLogger().debug(getClass().getName()+": exiting completePartSerialNumber");
	}

	public void completeProductId(ProcessRefresh state) {
		Logger.getLogger().debug(getClass().getName()+": entering completeProductId");
		if(!StringUtils.isEmpty(state.getProductId())){
			view.getTextFieldLastPid().setText(state.getProductId());
			if (viewProperty.isShowLastAfOnSeqNumber()) {
				Frame frame = context.isOnLine() ? ServiceFactory.getDao(FrameDao.class).findByKey(state.getProductId()) : null;
				view.getTextFieldLastAfOnSeq().setText(frame != null ? frame.getLineRef(4).toString() : state.getProduct().getAfOnSequenceNumber());
			}
			if (viewProperty.isShowLastMto())
				view.getTextFieldLastMto().setText(state.getProduct().getProductSpec());
			if(viewProperty.isShowLastEngine()) {
				Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(state.getProductId());
				view.getTextFieldLastEngine().setText(frame.getEngineSerialNo());
			}
		}

		refreshScreen();
		notifyFinishProduct();
		notifySkippedPartProduct();
		refreshProcessedCounterPanel();
		Logger.getLogger().debug(getClass().getName()+": exiting completeProductId");
	}

	public void refreshScreen(DataCollectionState state) {
		Logger.getLogger().debug(getClass().getName()+": entering refreshScreen(DataCollectionState state)");
		refreshScreen();
		notifyFinishProduct();
		refreshProcessedCounterPanel();
		Logger.getLogger().debug(getClass().getName()+": exiting refreshScreen(DataCollectionState state)");
	}

	// Getters & setters
	@Override
	public DataCollectionPanelBase getView() {
		return view;
	}

	public void setView(ClassicDataCollectionPanel view) {
		this.view = view;
	}

	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		try {
			// check LotControlRuleInfo
			if (lotControlRules == null || lotControlRules.size() == 0)
				return;

			view.getTextFieldExpPidOrProdSpec().setVisible(true);

			int scanPartsIndex = 0;
			// control Part
			for (int i = 0;((i < view.getMaxNumPart()) && (i < lotControlRules.size())); i++) {
				if (lotControlRules.get(i) != null && (lotControlRules.get(i).getSerialNumberScanFlag() == 1 || lotControlRules.get(i).isDateScan())) {
					PartName partName = lotControlRules.get(i).getPartName();
					String partMask = CommonPartUtility.parsePartMaskDisplay(lotControlRules.get(i).getPartMasks());
					partMask = partMask.contains("<<")?partMask.replace("<", "&lt;"):partMask;
					partMask = partMask.contains(">>")?partMask.replace(">", "&gt;"):partMask;
					String label = "<HTML><p align='right'>" + partName.getWindowLabel()
							+ ":" + "<br>" + partMask + "</p></HTML>";
					view.getPartLabel(i).setText(label);
					view.setFont(new java.awt.Font("dialog", 0, 18));
					view.repositionPartLabel(view.getPartLabel(i), scanPartsIndex,isAfOnSeqNumExist());
					view.repositionSerialNumber(view.getPartSerialNumber(i), scanPartsIndex,isAfOnSeqNumExist());
					view.getPartLabel(i).setVisible(true);
					view.getPartSerialNumber(i).setVisible(true);

					scanPartsIndex++;
				}

			}
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
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
				ViewControlUtil.refreshObject(view.getPartSerialNumber(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEnable);
			}

			// set Enable/Visible Torque
			view.getLabelTorque().setVisible(bVisible);
			for (int i = 0; i < view.getMaxNumTorq(); i++) {
				view.getTorqueValueTextField(i).setVisible(bVisible);
				view.getTorqueValueTextField(i).setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
				view.getTorqueValueTextArea(i).setVisible(bVisible);
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEnable);
			}

			view.getButton(3).setVisible(bVisible);
			view.getButton(1).setVisible(bVisible);
			view.getButton(0).setVisible(bVisible);

			view.setTestTorqueButtonVisible(!bVisible);
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	public void refreshScreen() {
		Logger.getLogger().debug(getClass().getName()+": entering refreshScreen()");
		dataCollectionEventHandler.disableKeyBuffer();
		try
		{
			// set Visible
			view.setVisible(true);
			//Enable VIN Button
			view.getJButtonProductId().setEnabled(true);

			view.getTextFieldProdId().setText("");
			requestFocus(view.getTextFieldProdId());

			view.getLabelExpPIDOrProdSpec().setVisible(true);
			view.getButton(2).setVisible(true);
			view.setTestTorqueButtonVisible(true);
			ViewControlUtil.refreshObject(view.getLabelExpPIDOrProdSpec(), view.getProdSpecLabel());
			ViewControlUtil.refreshObject(view.getTextFieldExpPidOrProdSpec(),
					"",	ViewControlUtil.VIEW_COLOR_INPUT,false);
			view.getLabelAfOnSeqNum().setVisible(false);
			view.getLabelAfOnSeqNumValue().setVisible(false);
			view.getLabelProductCount().setVisible(false);
			view.getLabelProductCountValue().setVisible(false);
			if(isAfOnSeqNumExist()){
				view.getLabelAfOnSeqNum().setVisible(true);
				view.getLabelAfOnSeqNumValue().setVisible(true);
				ViewControlUtil.refreshObject(view.getLabelAfOnSeqNum(), view.getAfOnSeqNumLabel());
				ViewControlUtil.refreshObject(view.getLabelAfOnSeqNumValue(),	"",	ViewControlUtil.VIEW_COLOR_INPUT,false);
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
			view.getButton(2).setEnabled(true);
			view.getTextFieldProdId().setEnabled(true);

			view.requestFocusInvokeLater(view.getTextFieldProdId());

			messageArea.setErrorMessageArea(null);

			view.setInitBackgroundColor(ViewControlUtil.VIEW_COLOR_INPUT);
			view.setInitForegroundColor(ViewControlUtil.VIEW_COLOR_FONT);

		}
		catch (Exception e)
		{
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::refreshScreen() exception.");
		}

		Logger.getLogger().debug(getClass().getName()+": exiting refreshScreen()");
	}

	public JPanel getClientPanel() {
		return getViewPanel();
	}

	public void requestFocus(JTextField textField){
		highlightTextField(textField);
		textField.requestFocus();
	}

	public void requestFocus(UpperCaseFieldBean textField){
		textField.setColor(Color.WHITE);
		highlightTextField(textField);
		textField.requestFocus();
	}

	private void highlightTextField(JTextField textField) {
		textField.setBorder(BorderFactory.createLineBorder(Color.BLUE,8));
		textField.setBackground(Color.WHITE);
		textField.setSelectionColor(new Color(204, 204, 255));
		textField.setEnabled(true);
		textField.setEditable(true);
		textField.repaint();
		Logger.getLogger().check(textField.getName() + " has been highlighted");
	}

	protected void moveTextFieldHighlight(JTextField textField, Color foreground, Color background) {
		if(foreground != null) textField.setForeground(foreground);
		if(background != null) textField.setBackground(background);
		textField.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		textField.setEditable(false);
		textField.setEnabled(false);
		textField.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		if(foreground == null || background == null){
			Logger.getLogger().check(textField.getName() + " has been highlighted to null");
		}
		else{
			Logger.getLogger().check(textField.getName() + " has been highlighted to Foreground:" + foreground.toString() + " Background: " + background.toString());
		}
	}

	public void rejectPart(ProcessPart state) {
		dataCollectionEventHandler.disableKeyBuffer();
		int currentPartIndex = state.getCurrentPartIndex();
		Logger.getLogger().debug("--reject:" + currentPartIndex);

		UpperCaseFieldBean rejected = view.getPartSerialNumber(state.getCurrentPartIndex() + 1);
		if(rejected != null && rejected.isVisible()){
			rejected.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		}
		requestFocus(view.getPartSerialNumber(currentPartIndex));
		view.getPartSerialNumber(currentPartIndex).selectAll();
	}

	public void rejectTorque(ProcessTorque state) {
		Logger.getLogger().check("rejectTorque:" + state.getCurrentTorqueIndex());
		int pos = getCurrentTorquePosition(state);

		if (view.getTorqueValueTextField(pos + 1)!=null){
			view.getTorqueValueTextField(pos + 1).setText(viewProperty.getDefaultTorqueValue());
			view.getTorqueValueTextField(pos + 1).setBackground(Color.WHITE);
			view.getTorqueValueTextField(pos + 1).setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		}

		if(pos >= 0) {
			view.getTorqueValueTextField(pos).setText(viewProperty.getDefaultTorqueValue());
			requestFocus(view.getTorqueValueTextField(pos));
			if(viewProperty.isMeasurementEditable()){
				view.getTorqueValueTextField(pos).selectAll();
			}
		}
		if(state.getCurrentTorqueIndex()<0)
			state.setCurrentPartIndex(state.getCurrentPartIndex()-1);
		clearMessageArea(state);
	}

	public void rejectLastInput(DataCollectionState state) {
		Logger.getLogger().check("rejectLastInput:" + state.getCurrentPartIndex() + " - " + state.getCurrentTorqueIndex());
		clearMessageArea(state);

		//Disable Product Id button and text field
		view.getTextFieldProdId().setBackground(view.getTextFieldProdId().getColor());
		view.getJButtonProductId().setEnabled(false);
		view.getTextFieldProdId().setEnabled(false);
		view.getTextFieldProdId().setText(state.getProductId());
	}

	public void message(DataCollectionState state) {
		if (state.getMessage() == null)
			return;

		if(state.getMessage().getId().equals(StatusMessage.SERVER_ON_LINE) &&
				state instanceof ProcessProduct &&
				!StringUtils.isEmpty(state.getExpectedProductId()))
			initProductId((ProcessProduct)state);
	}

	public void serviceStatusChanged(Endpoint endpoint) {
		if(context.isOnLine() != endpoint.isConnected()){
			context.setOnLine(endpoint.isConnected());
		}
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, endpoint.isConnected()));
	}

	public void statusChanged(ConnectionStatus status) {
		if(context.isOnLine() != status.isConnected()){
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

				if(!state.isLastTorqueOnCurrentPart()){
				if(nextTorqueField != null && nextTorqueField.isVisible())
					requestFocus(nextTorqueField);
					nextTorqueField.selectAll();
				}
			}
		};
		SwingUtilities.invokeLater(r);

		clearMessageArea(state);
	}

	public void partSnOkButWait(ProcessPart state) {}

	public void receivedBypass(ProcessPart state) {}

	public void receivedAuto(ProcessPart state) {}

}