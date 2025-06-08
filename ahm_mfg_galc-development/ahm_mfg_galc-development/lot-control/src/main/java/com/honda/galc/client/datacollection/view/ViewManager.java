package com.honda.galc.client.datacollection.view;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.ColorUIResource;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.action.CancelButtonAction;
import com.honda.galc.client.datacollection.view.action.DisableExpectedProductAction;
import com.honda.galc.client.datacollection.view.action.MeasurementValueInputAction;
import com.honda.galc.client.datacollection.view.action.PartSerialNumberInputAction;
import com.honda.galc.client.datacollection.view.action.ProductButtonAction;
import com.honda.galc.client.datacollection.view.action.ProductIdInputAction;
import com.honda.galc.client.datacollection.view.action.RefreshButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipPartButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipProductButtonAction;
import com.honda.galc.client.datacollection.view.action.TestTorqueButtonAction;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.OptionDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.ColorUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.net.Request;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductHoldService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

/**
 * <h3>ViewManager</h3>
 * <h4>
 * Common Data Collection view controller implementation.
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
public class ViewManager extends ViewManagerBase
implements IViewObserver, ConnectionStatusListener{

	public static String TEXT_EXPECTED_PRODUCT_ID ="Expected";

	protected DataCollectionPanel view;
	protected JPanel viewPanel;
	protected DefaultViewProperty viewProperty;
	private int badTorqueCount;
	private ProductHoldService holdService;
	private ProductCheckPropertyBean productCheckPropertyBean;
	private LotControlPropertyBean lotControlPropertyBean;

	public ViewManager(ClientContext clientContext) {

		super(clientContext);

		init();
	}

	protected DataCollectionPanel createDataCollectionPanel(DefaultViewProperty property) {
		if(view == null){
			view =  new DefaultDataCollectionPanel(property, viewManagerProperty.getMainWindowWidth(),
				viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}

	private void init()  {
		try {

			createDataCollectionPanel(getViewProperty());
			getViewPanel();

			initConnections();
			refreshScreen();

			ServiceMonitor.getInstance().registerHttpServiceListener(this);

			//Exceptions may exist before client start, for example disable gun failed
			if(getCurrentState().getErrorList().size() > 0){
				setErrorMessage(getCurrentState().getErrorList().get(0).getMessage().getDescription());
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to init ViewManager.");
		}
	}

	protected DefaultViewProperty getViewProperty() {
		if(viewProperty == null)
			viewProperty = PropertyService.getPropertyBean(DefaultViewProperty.class, context.getProcessPointId());
		return viewProperty;
	 }

	protected JPanel getViewPanel() {
		if(viewPanel == null){
			viewPanel = new JPanel();
			viewPanel.setName("ViewContentPanel");
			viewPanel.setLayout(new BorderLayout());
			viewPanel.add(view, BorderLayout.CENTER);

			messageArea = getMessagePanel();

		}
		return viewPanel;

	}

	protected void initConnections() throws Exception {

		super.initConnections();
		initProductIdConnections();

		for (int i = 0; i < viewProperty.getMaxNumberOfPart(); i++) {
			view.getPartSerialNumber(i).setAction(new PartSerialNumberInputAction(context, "PartSnInput"));
			view.getPartSerialNumber(i).addKeyListener(this);
			view.getPartSerialNumber(i).addActionListener(dataCollectionEventHandler);
		}

		for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
			view.getTorqueValueTextField(i).addKeyListener(this);
			view.getTorqueValueTextField(i).addKeyListener(dataCollectionEventHandler);
			if(!viewProperty.isMeasurementEditable()){
				view.getTorqueValueTextField(i).setEditable(false);
			}else{
				view.getTorqueValueTextField(i).setAction(new MeasurementValueInputAction(context, "MeasurementValueInput"));
				view.getTorqueValueTextField(i).setEditable(true);
			}
		}

		view.getButton(0).setAction(new CancelButtonAction(context, getButtonLabel(0)));
		view.getButton(1).setAction(new SkipPartButtonAction(context, getButtonLabel(1)));
		view.getButton(2).setAction(new SkipProductButtonAction(context, getButtonLabel(2)));
		view.getWiderButton().setAction(new DisableExpectedProductAction(context, "Disable " + TEXT_EXPECTED_PRODUCT_ID, this));
		view.setTestTorqueButtonAction(new TestTorqueButtonAction(context, "Test Torque"));
	}

	protected void initProductIdConnections() {
		view.getJButtonProductId().setAction(new ProductButtonAction(context.getFrame(), viewProperty.getProductType(), getButtonLabel(), view.getTextFieldProdId()));
		view.getTextFieldProdId().setAction(new ProductIdInputAction(context, "ProductIdInput"));
		view.getTextFieldProdId().addKeyListener(this);
		view.getTextFieldProdId().addCaretListener(this);
	}

	protected String getButtonLabel(int idx) {
		return viewProperty.getButtonLabelMap().get(String.valueOf(idx));
	}
	protected String getButtonLabel()
	{
		return viewProperty.getProductIdLabel();

	}
	@Override
	public void update(Observable o, Object arg) {

		//show error message from other observer
		showErrorMessage(arg);

		super.update(o, arg);
	}

	protected void showErrorMessage(Object arg) {
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
				ErrorDialogManager mgr = new ErrorDialogManager(context.getProcessPointId());
				String request = mgr.showDialog(context.getFrame(), errorMsg.getInfo(), errorMsg.getId(), getProperty());
				if(request != null) {
					if (request.equalsIgnoreCase("cancel")) {
						((DataCollectionState) arg).getStateBean().setRepair(true);
					} else {
						((DataCollectionState) arg).getStateBean().setRepair(false);
					}
					if (request.equalsIgnoreCase("skipProduct")) {
						if (this.getLotControlPropertyBean().isSkipToNextFromErrorDialog()) {
							skipNextProduct();
						} else {
							skipProduct();
						}
					}
					if(errorMsg.getId().equalsIgnoreCase(LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED) && request.equalsIgnoreCase("skipPart") ){
						enableTorqueDevices((DataCollectionState)arg);
					}else{
						runInSeparateThread(new Request(request));
					}
				}
				cleanMessage((DataCollectionState)arg);
			} else {
				String msg = errorMsg.getDescription() != null ? errorMsg.getDescription() : ""+"\n"+errorMsg.getInfo()!= null?errorMsg.getInfo():"";
		        int result = MessageDialog.showErrorUnfocused(msg, errorMsg.getId());
		        if (result == JOptionPane.OK_OPTION) {
					cleanMessage((DataCollectionState)arg);
					if( errorMsg.getId().equals(LotControlConstants.UNEXPECTED_PRODUCT_SCAN))
						new RefreshButtonAction(context, "cancel", true);
				}
			}
		}
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
			default:
				return false;
		}
	}


	public void completeCollectTorques(ProcessTorque state) {
		JLabel torqueResultLabel = view.getTorqueResultLabel(state.getCurrentPartIndex());
		if((0-state.getCurrentPartTorqueCount()) == badTorqueCount){
		 renderResultLabelOk(torqueResultLabel);
		}else{
			renderResultLabelNg(torqueResultLabel);
		}
	}

	public void initPartSn(ProcessPart state) {
		if(!state.isPartMark()){
			renderFieldBeanInit(view.getPartSerialNumber(state.getCurrentPartIndex()), true);
		} else {
			renderFieldBeanPartMark(state);
		}

		int torqueCount = state.getCurrentPartTorqueCount();
		String torqueLabel = "Torque:";
		torqueVisibleControl(torqueCount, true, torqueLabel, state.getCurrentTorqueIndex());
	}

	private void delay(long time) {
		try {
			if(time == 0) return;
			Thread.sleep(time);
		} catch (Exception e) {
			Logger.getLogger().info(e, " exception on sleep.");
		}

	}

	public void initTorque(ProcessTorque state) {
		UpperCaseFieldBean partSn = view.getPartSerialNumber(state.getCurrentPartIndex());
		final String PROMPT_INPUT_TORQUE = "Please input Torques.";
		String torqueLabel = getTorqueLabelText(state);

		torqueVisibleControl(state.getCurrentPartTorqueCount(), true, torqueLabel, state.getCurrentTorqueIndex() );
		//if not verify part SN, then prompt input torque
		if(!state.isScanPartSerialNumber())
			renderFieldBeanPrompt(partSn, PROMPT_INPUT_TORQUE);



		view.getTorqueValueTextField(state.getCurrentTorqueIndex()).requestFocus();
		badTorqueCount=0;
	}


	public void initProductId(ProcessProduct state) {
		Logger.getLogger().debug("viewManager:initProductId()-expectedProductId:" + state.getExpectedProductId());
		if(isCheckExpectedProduct() && !StringUtils.isEmpty(state.getExpectedProductId())) {
			renderExpPidOrProdSpec(view.getExpectProductIdLabel(), state.getExpectedProductId());
			enableExpectedProductIdControlButton(true);

		} else {
			enableExpectedProductIdControlButton(false);
			view.repaint();
		}

		if(isAfOnSeqNumExist() && !StringUtils.isEmpty(getCurrentState().getAfOnSeqNo()))
			renderSeqNo(view.getAfOnSeqNumLabel(), state.getAfOnSeqNo());
		if(isProductLotCountExist()){
			view.renderProductCount(view.getProductCountLabel(), state.getProductCount(), state.getLotSize());
		}

		if(context.isAutoProcessExpectedProduct() && !context.isManualRefresh()){
			// bug fix to avoid the vicious loop which consumes all available CPU on the client
			if (state.getExpectedProductId() != null && !state.getExpectedProductId().trim().equals("")) {
			view.getTextFieldProdId().setText(state.getExpectedProductId());
			view.getTextFieldProdId().postActionEvent();
			}
		}else{
			context.setManualRefresh(false);
		}
		
		if(getCurrentState().isResetSequence()) { 
			view.getTextFieldProdId().setText(state.getExpectedProductId());
			getCurrentState().setResetSequence(false);
		}

		boolean isUseNextExpectedStampingSequence = viewManagerProperty.isUseExpectedStampingSequence();
		if (isUseNextExpectedStampingSequence && StringUtils.isNotEmpty(state.getExpectedProductId())) {
			ProductId request = new ProductId(state.getExpectedProductId());
			super.runInSeparateThread(request);
		} else {
			setProductInputFocused();
		}
	}

	protected void enableExpectedProductIdControlButton(boolean b) {
		view.getWiderButton().setVisible(b);
		view.getWiderButton().setEnabled(b);

		if(b){
			view.getWiderButton().setText(getExpectedProductIdControlButtonName());
		}

	}

	public String getExpectedProductIdControlButtonName() {
		return context.isDisabledExpectedProductCheck() ? "Enable " + TEXT_EXPECTED_PRODUCT_ID : "Disable " + TEXT_EXPECTED_PRODUCT_ID;
	}

	public boolean isCheckExpectedProduct() {
		return context.isCheckExpectedProductId();
	}

	public void partSnNg(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		renderFieldBeanNg(partSerialNumber, state.getCurrentInstallPart().getPartSerialNumber());
		partSerialNumber.requestFocus();


	}

	public void partSnOk(ProcessPart state) {

		renderFieldBeanOk(view.getPartSerialNumber(state.getCurrentPartIndex()), state.getCurrentInstallPart().getPartSerialNumber());
		clearMessageArea(state);
	}

	public void productIdOk(ProcessProduct state) {

		//Show product Id status
		renderFieldBeanOk(view.getTextFieldProdId(), state.getProductId());

		renderExpPidOrProdSpecOnOk(state);

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

		view.setTestTorqueButtonVisible(false);
		//init display
		partVisibleControl(state.getLotControlRules());
		clearMessageArea(state);

		if(state.getProduct().isMissingRequiredPart())
			setErrorMessage(state.getProduct().getMissingRequiredPartMessage());

		if(state.isSpecChanged() && viewProperty.isHoldOnSpecChange()) {
			//put product on Hold
			holdProduct(state);

			if(viewProperty.isConfirmSpecCheck()) {
				if(MessageDialog.confirm (this.view, "Spec Check Complete?")) {
					if(MessageDialog.confirm(this.view, "The Spec Check Product: " + state.getProductId() + " released from hold?" )) {
						//release the hold product
						releaseProduct(state);
					}
				}
			} else {
				MessageDialog.showInfo(this.view, "Spec Check Required!");
			}
		}

		notifyNewProduct(state.getProductId());
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

	private void releaseProduct(ProcessProduct state) {

		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), state.getProductId());
		dc.put(TagNames.PROCESS_POINT_ID.name(), context.getProcessPointId());
		dc.put(TagNames.HOLD_REASON.name(), getProductCheckPropertyBean().getSpecCheckHoldReason());
		dc.put(TagNames.PRODUCT.name(), state.getProduct().getBaseProduct());
		dc.put(TagNames.RELEASE_REASON.name(), getProductCheckPropertyBean().getSpecCheckHoldReason());
		dc.put(TagNames.ASSOCIATE_ID.name(), context.getUserId());


		getHoldService().release(dc);
	}

	private void holdProduct(ProcessProduct state) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), state.getProductId());
		dc.put(TagNames.PROCESS_POINT_ID.name(), context.getProcessPointId());
		dc.put(TagNames.PRODUCT.name(), state.getProduct().getBaseProduct());
		dc.put(TagNames.HOLD_REASON.name(), getProductCheckPropertyBean().getSpecCheckHoldReason());
		dc.put(TagNames.ASSOCIATE_ID.name(), context.getUserId());
		dc.put(TagNames.HOLD_SOURCE.name(), 0);

		getHoldService().execute(dc);
	}

	protected void renderExpPidOrProdSpecOnOk(ProcessProduct state) {
		//Show product spec
		renderExpPidOrProdSpec(view.getProdSpecLabel(), state.getProductSpecCode());
		enableExpectedProductIdControlButton(false);
	}



	protected String getColor(String specCode) {

		return ViewControlUtil.getColor(viewManagerProperty.getModelColorMap(), specCode, true);
	}

	public void productIdNg(ProcessProduct state) {
		this.isCaretListenerActive = false;
		renderFieldBeanNg(view.getTextFieldProdId(), state.getProductId());
		this.isCaretListenerActive = true;
	}

	public void setProductInputFocused() {
		view.getTextFieldProdId().requestFocus();
	}

	public void receivedPartSn(ProcessPart state) {
		clearMessageArea(state);
		if(state.getCurrentPartScanCount() > 1) {
			int idx = state.getCurrentPartIndex();
			UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(idx);
			partSerialNumber.setText(state.getCurrentInstallPart().getPartSerialNumber());
			ViewControlUtil.refreshObject(partSerialNumber,state.getCurrentInstallPart().getPartSerialNumber(), ViewControlUtil.VIEW_COLOR_WAIT, true);
			partSerialNumber.selectAll();
			partSerialNumber.requestFocus();
		}
	}

	public void receivedProductId(ProcessProduct state) {
		if (context.getProperty().isNotAutoPopulateProductIdCellExit()
				&& isSkipProduct()) {
			view.getTextFieldProdId().setBackground(Color.RED);
			view.getTextFieldProdId().requestFocus();
			return;
		}
		view.getTextFieldProdId().setText(state.getProductId());
	}

	public void torqueNg(ProcessTorque state) {
		String mvalue = "";
		if(viewProperty.isShowTorqueAsValue()) 
			mvalue = state.getCurrentTorque()!= null?Double.toString(state.getCurrentTorque().getMeasurementValue()):"0.0";
			else 
				mvalue = state.getCurrentTorque().getMeasurementStatus().toString();

		ViewControlUtil.refreshObject(view.getTorqueValueTextField(state.getCurrentTorqueIndex()),mvalue, ViewControlUtil.VIEW_COLOR_NG, true);
		view.getTorqueValueTextField(state.getCurrentTorqueIndex()).requestFocus();
	}

	public void torqueOk(ProcessTorque state) {
		Measurement bean = state.getCurrentTorque();
		JTextField torqueValueTextField = view.getTorqueValueTextField(state.getCurrentTorqueIndex());
		view.showTorque(bean, torqueValueTextField, ViewControlUtil.VIEW_COLOR_OK);

		clearMessageArea(state);
		if(!state.isLastTorqueOnCurrentPart()){
			ViewControlUtil.refreshObject(view.getTorqueValueTextField(state.getCurrentTorqueIndex()+1),
					"0.0", ViewControlUtil.VIEW_COLOR_CURRENT, true);
			String torqueLabel = getTorqueLabelText(state,state.getCurrentTorqueIndex()+1 );
			view.getLabelTorque().setText(torqueLabel);
			view.getTorqueValueTextField(state.getCurrentTorqueIndex()+1).requestFocus();
		}
		badTorqueCount--;
	}

	public void notifyError(DataCollectionState state) {
		if(state instanceof ProcessPart){
			if(state.getCurrentPartScanCount() > 1) {
				int idx = state.getCurrentPartIndex();
				UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(idx);
				ViewControlUtil.refreshObject(partSerialNumber,"", ViewControlUtil.VIEW_COLOR_CURRENT, true);
				partSerialNumber.requestFocus();
			}
		}
	}

	public void message(DataCollectionState state) {
		if(state.getMessage()!= null && StatusMessage.SERVER_ON_LINE.equals(state.getMessage().getId()) &&
				state instanceof ProcessProduct &&
				!StringUtils.isEmpty(state.getExpectedProductId()))
			initProductId((ProcessProduct)state);

	}

	public void skipProduct(DataCollectionState state) {
		view.skipProduct();
	}

	public void skipPart(DataCollectionState state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		if(!state.getProduct().getPartList().get(state.getCurrentPartIndex()).isValidPartSerialNumber()){
			//Part Serial Number not pass
			partSerialNumber.setColor(ViewControlUtil.VIEW_COLOR_NG);
			ViewControlUtil.refreshObject(partSerialNumber, null, ViewControlUtil.VIEW_COLOR_NG, false);
			partSerialNumber.setEditable(false);
		} else {//Torque collection is not complete
			JLabel torqueResultLabel = view.getTorqueResultLabel(state.getCurrentPartIndex());
			renderResultLabelNg(torqueResultLabel);

			ViewControlUtil.refreshObject(view.getTorqueValueTextField(state.getCurrentTorqueIndex()),
					viewProperty.getDefaultTorqueValue(), ViewControlUtil.VIEW_COLOR_NG, true);
		}

		clearMessageArea(state);

	}

	protected void renderResultLabelNg(JLabel torqueResultLabel) {
		torqueResultLabel.setIcon(view.getImgNg());
		torqueResultLabel.setVisible(true);
	}


	protected void renderResultLabelOk(JLabel torqueResultLabel) {
		torqueResultLabel.setIcon(view.getImgOk());
		torqueResultLabel.setVisible(true);
	}

	public void completePartSerialNumber(ProcessPart state) {
		//add dely for part mark only
		if(state.isPartMark()){
			delay(viewManagerProperty.getPartMarkDelay()*1000);
		}
	}

	public void completeProductId(ProcessRefresh state) {

		if(!StringUtils.isEmpty(state.getProductId()) && state.getProduct().isValidProductId()) {
			view.getTextFieldLastPid().setText(state.getProductId());
			if (viewProperty.isShowLastAfOnSeqNumber()) {
				Frame frame = context.isOnLine() ? ServiceFactory.getDao(FrameDao.class).findByKey(state.getProductId()) : null;
				view.getTextFieldLastAfOnSeq().setText(frame != null ? frame.getLineRef(4).toString() : state.getProduct().getAfOnSequenceNumber());
			}
		}

		if (viewProperty.isShowLastMto())
			view.getTextFieldLastMto().setText(state.getProduct().getProductSpec().substring(0,9));

		if(viewProperty.isShowLastEngine()) {
			Frame frame = (Frame)state.getProduct().getBaseProduct();
			view.getTextFieldLastEngine().setText(frame.getEngineSerialNo());
		}

		refreshScreen(viewManagerProperty.getScreenRefreshingDelay());

		notifyFinishProduct();
		notifySkippedPartProduct();
		refreshProcessedCounterPanel();
	}

	public void refreshScreen(DataCollectionState state) {

		refreshScreen(0);
		notifyFinishProduct();

	}

	// Getters & setters
	@Override
	public DataCollectionPanelBase getView() {
		return view;
	}

	public void setView(DataCollectionPanel view) {
		this.view = view;
	}

	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		try {

			// check LotControlRuleInfo
			if (lotControlRules == null || lotControlRules.size() == 0)
				return;

			view.getTextFieldExpPidOrProdSpec().setVisible(true);

			// control Part
			for (int i = 0;((i < viewProperty.getMaxNumberOfPart()) && (i < lotControlRules.size())); i++) {
				if (lotControlRules.get(i) != null) {

					view.repositionPartLabel(view.getPartLabel(i), i,isAfOnSeqNumExist());
					view.repositionSerialNumber(view.getPartSerialNumber(i), i,isAfOnSeqNumExist());
					String partMask = CommonPartUtility.parsePartMaskDisplay(lotControlRules.get(i).getPartMasks());
					partMask = partMask.contains("<<")?partMask.replace("<", "&lt;"):partMask;
					partMask = partMask.contains(">>")?partMask.replace(">", "&gt;"):partMask;
					String label = "<HTML><p align='right'>" + lotControlRules.get(i).getPartName().getWindowLabel()
							+ ":" + "<br>" + partMask + "</p></HTML>";
					view.getPartLabel(i).setText(label);
					view.getPartLabel(i).setVisible(true);

					view.getPartSerialNumber(i).setVisible(true);
					view.getTorqueResultLabel(i).setVisible(false);

				}

			}
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	public void torqueVisibleControl(int aTorqueCount, boolean isVisible,String torqueLabel, int index) {

		final String DEFAULT_TORQUE = viewProperty.getDefaultTorqueValue();

		if (aTorqueCount > 0){
			view.getLabelTorque().setText(torqueLabel);
			view.getLabelTorque().setVisible(true);
		}else{
			view.getLabelTorque().setVisible(false);
		}

		for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
			if(i!= index){
			ViewControlUtil.refreshObject(view.getTorqueValueTextField(i),
					DEFAULT_TORQUE, ViewControlUtil.VIEW_COLOR_INPUT, false);
			}else{
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i),
						DEFAULT_TORQUE, ViewControlUtil.VIEW_COLOR_CURRENT, true);
			}
			view.getTorqueValueTextField(i).setVisible(i < aTorqueCount);
		}
	}

	public void partVisibleControl(boolean bEneble, boolean bVisible) {

		try {

			view.getTextFieldExpPidOrProdSpec().setVisible(bVisible);
			view.getLabelExpPIDOrProdSpec().setVisible(bVisible);

			for (int i = 0; i < viewProperty.getMaxNumberOfPart(); i++) {
				view.getPartLabel(i).setVisible(bVisible);
				view.getPartSerialNumber(i).setVisible(bVisible);
				view.getPartSerialNumber(i).setColor(ViewControlUtil.VIEW_COLOR_INPUT);
				ViewControlUtil.refreshObject(view.getPartSerialNumber(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEneble);
				view.getTorqueResultLabel(i).setVisible(bVisible);
			}

			// set Enable/Visible Torque
			view.getLabelTorque().setVisible(bVisible);
			for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
				view.getTorqueValueTextField(i).setVisible(bVisible);
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEneble);
			}

			view.getButton(1).setVisible(bVisible);
			view.getButton(0).setVisible(bVisible);
			view.setTestTorqueButtonVisible(!bVisible);
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	public void refreshScreen(int refreshingDelay) {
		try
		{
			//disable expected expired.
			context.setDisabledExpectedProductCheck(false);

			if(refreshingDelay > 0 && !isSkipProduct())
				refreshScreenWithDelay(refreshingDelay);
			else
				refreshScreen();

		}
		catch (Exception e)
		{
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::refreshScreen() exception.");
		}

	}

	protected boolean isSkipProduct() {
		if(getCurrentState().getProduct() == null) return true; //don't wait if event no product

		return getCurrentState().getProduct().isSkipped();
	}

	protected void refreshScreen() {

		for (int i = 0; i < viewProperty.getMaxNumberOfPart(); i++) {
			view.getPartSerialNumber(i).setMaximumLength(view.getMaxPartTexLength());
		}

		// refresh Part/Torque
		partVisibleControl(false, false);

		// refresh Button
		boolean showSkipButton =  this.viewProperty.isHideSkipButtonWhenExpCheckDisabled() ? isCheckExpectedProduct() : true;
		view.getButton(2).setEnabled(showSkipButton);
		view.getButton(2).setVisible(showSkipButton);
		view.setTestTorqueButtonVisible(true);
		view.getLabelExpPIDOrProdSpec().setVisible(false);
		ViewControlUtil.refreshObject(view.getLabelExpPIDOrProdSpec(), view.getProdSpecLabel());
		ViewControlUtil.refreshObject(view.getTextFieldExpPidOrProdSpec(), "",	ViewControlUtil.VIEW_COLOR_INPUT,false);

		view.getLabelAfOnSeqNum().setVisible(false);
		view.getLabelAfOnSeqNumValue().setVisible(false);
		view.getLabelProductCount().setVisible(false);
		view.getLabelProductCountValue().setVisible(false);
		if(isAfOnSeqNumExist() && !StringUtils.isEmpty(getCurrentState().getAfOnSeqNo())){
			ViewControlUtil.refreshObject(view.getLabelAfOnSeqNum(), view.getAfOnSeqNumLabel());
			ViewControlUtil.refreshObject(view.getLabelAfOnSeqNumValue(),	"",	ViewControlUtil.VIEW_COLOR_INPUT,false);
		}
		if(isProductLotCountExist()){
			view.getLabelProductCount().setVisible(true);
//			view.getLabelProductCountValue().setVisible(true);
			ViewControlUtil.refreshObject(view.getLabelProductCount(), view.getProductCountLabel());
//			ViewControlUtil.refreshObject(view.getLabelProductCountValue(),	"",	ViewControlUtil.VIEW_COLOR_INPUT,false);
		}
		if(isCheckExpectedProduct() && !StringUtils.isEmpty(getCurrentState().getExpectedProductId()))
			enableExpectedProductIdControlButton(true);

		renderFieldBeanInit(view.getTextFieldProdId(), true);

		messageArea.setErrorMessageArea(null);

	}

	private void refreshScreenWithDelay(final int screenRefreshingDelay) {
		try {
			for (int i = screenRefreshingDelay; i > 0; i--) {
				if(!messageArea.isError())
					messageArea.setErrorMessageArea("Refreshing Screen in " + i + " seconds.");

				Thread.sleep(1000);
			}

			refreshScreen();
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::refreshScreenWithDelay() exception.");
		}

	}

	public JPanel getClientPanel() {
		return getViewPanel();
	}

	public void statusChanged(ConnectionStatus status) {
		if(context.isOnLine() != status.isConnected()){
			context.setOnLine(status.isConnected());

			Message msg = new Message(status.isConnected()? StatusMessage.SERVER_ON_LINE : StatusMessage.SERVER_OFF_LINE,
					status.isConnected()? "Server back on line" : "Server off line", MessageType.INFO);
			DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getFsm().message(msg);

		}

		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, status.isConnected()));
	}

	public DataCollectionState getCurrentState(){
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState();
	}

	public void enableExpectedProduct(boolean enabled) {
		getView().getTextFieldExpPidOrProdSpec().setVisible(enabled);
		getView().getLabelExpPIDOrProdSpec().setVisible(enabled);

		requestFocus(getView().getTextFieldProdId());
	}

	protected void requestFocus(UpperCaseFieldBean bean) {
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		bean.requestFocus();
	}

	protected void renderFieldBeanNg(UpperCaseFieldBean bean, String text) {
		bean.setText(text);
		bean.setColor(ViewControlUtil.VIEW_COLOR_NG);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_NG);
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		bean.setEnabled(true);
	}

	protected void renderFieldBeanPrompt(UpperCaseFieldBean bean, String text) {
		bean.setText(text);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_PROMPT);
		bean.setColor(ViewControlUtil.VIEW_COLOR_PROMPT);
		bean.setEditable(false);
		bean.setEnabled(false);

	}

	protected void renderFieldBeanInit(UpperCaseFieldBean bean, boolean requestFocus) {
		bean.setColor(ViewControlUtil.VIEW_COLOR_CURRENT);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_CURRENT);
		bean.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setForeground(ViewControlUtil.VIEW_COLOR_INPUT);
		bean.setText("");
		bean.setEnabled(true);
		bean.setEditable(true);
		bean.setVisible(true);

		if(requestFocus) bean.requestFocus();
	}

	protected void renderFieldBeanOk(UpperCaseFieldBean bean, String serialNumber) {
		bean.setText(serialNumber);
		bean.setColor(ViewControlUtil.VIEW_COLOR_OK);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_OK);
		bean.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setForeground(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setEditable(false);
		bean.setEnabled(false);

	}

	protected void renderExpPidOrProdSpec(String label, String text) {

		if(text == null){
			Logger.getLogger().warn("WARN:", "Expected Pid or Product Spec is null.");
		} else {
			view.setProductSpecBackGroudColor(getColor(text));
		}

		view.getLabelExpPIDOrProdSpec().setText(label);
		view.getLabelExpPIDOrProdSpec().setVisible(true);
		view.getTextFieldExpPidOrProdSpec().setText(text);
		view.getTextFieldExpPidOrProdSpec().setVisible(true);
		view.getLabelExpPIDOrProdSpec().repaint();
	}


	private void renderFieldBeanPartMark(ProcessPart state) {
		UpperCaseFieldBean bean = view.getPartSerialNumber(state.getCurrentPartIndex());
		String partMask = state.getCurrentLotControlRule().getParts().get(0).getPartSerialNumberMask();
		bean.setText(partMask);
		String colorName = ViewControlUtil.getColor(viewManagerProperty.getPartMarkColorMap(), state.getProductSpecCode(), true);
		Logger.getLogger().info("Part mark color for product spec code:" + state.getProductSpecCode() + " is:" + colorName);
		Color color = ColorUtil.getColor(colorName);
		bean.setColor(color);
		bean.setBackground(color);
		bean.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setEnabled(true);
		bean.setVisible(true);
		bean.setEditable(false);

	}

	public void skipCurrentInput(final ProcessTorque state) {
		if(!state.isLastTorqueOnCurrentPart()){
			ViewControlUtil.refreshObject(view.getTorqueValueTextField(state.getCurrentTorqueIndex()),
				viewProperty.getDefaultTorqueValue(), ViewControlUtil.VIEW_COLOR_NG, false);
			ViewControlUtil.refreshObject(view.getTorqueValueTextField(state.getCurrentTorqueIndex()+1),
				viewProperty.getDefaultTorqueValue(), ViewControlUtil.VIEW_COLOR_CURRENT, true);
			view.getTorqueValueTextField(state.getCurrentTorqueIndex()+1).requestFocus();
			String torqueLabel = getTorqueLabelText(state,state.getCurrentTorqueIndex()+1 );
			view.getLabelTorque().setText(torqueLabel);
		}
		badTorqueCount++;
	}

	public void initRefreshDelay(ProcessRefresh state) {
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

	protected String getTorqueLabelText(DataCollectionState state, int torqueIndex) {
		String torqueLabel = "Torque:";
		List<MeasurementSpec> measurementSpecs = state.getCurrentLotControlRulePartList().get(0).getMeasurementSpecs();
		MeasurementSpec measurementSpec = torqueIndex < measurementSpecs.size()?measurementSpecs.get(torqueIndex): null;
		if (measurementSpec != null) {
			String minLimit = String.valueOf(measurementSpec.getMinimumLimit());
			String maxLimit = String.valueOf(measurementSpec.getMaximumLimit());
			String instructionCode = state.getCurrentLotControlRule().getInstructionCode();
			String label = "<HTML><p align='right'>" + torqueLabel + "<br> Min/Max " + minLimit + "/" + maxLimit
					+ " <br>PSet# ("+instructionCode+") </p></HTML>";
			return label;
		}
		return torqueLabel;
	}

	protected String getTorqueLabelText(DataCollectionState state) {

			return getTorqueLabelText(state, state.getCurrentTorqueIndex());
	}

	@Override
	protected int getButtonIndexForUniqueScan(UniqueScanType uniqueScanType) {
		switch (uniqueScanType) {
		case REFRESH:
			return 0;
		case SKIP:
			return 1;
		case NEXTVIN:
			return 2;
		default:
			return -1;
		}
	}

	public ProductHoldService getHoldService() {
		if(holdService == null)
			holdService = ServiceFactory.getService(ProductHoldService.class);
		return holdService;
	}

	public ProductCheckPropertyBean getProductCheckPropertyBean() {
		if(productCheckPropertyBean == null)
			productCheckPropertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, context.getProcessPointId());
		return productCheckPropertyBean;
	}
	
	public LotControlPropertyBean getLotControlPropertyBean() {
		if(lotControlPropertyBean == null)
			lotControlPropertyBean = PropertyService.getPropertyBean(LotControlPropertyBean.class, context.getProcessPointId());
		return lotControlPropertyBean;
	}

	public void partSnOkButWait(ProcessPart state) {
		setPartSerialNumberBackground(state, null, ViewControlUtil.VIEW_COLOR_OK, true);
	}

	public void receivedBypass(ProcessPart state) {
		setPartSerialNumberBackground(state, Color.RED, ViewControlUtil.VIEW_COLOR_NG, false);
	}

	public void receivedAuto(ProcessPart state) {
		setPartSerialNumberBackground(state, Color.WHITE, ViewControlUtil.VIEW_COLOR_CURRENT, true);
	}

	private void setPartSerialNumberBackground(ProcessPart state, Color color1, Color color2, Boolean flag) {
		if (getView().getPartSNList().isEmpty()) return;
		clearMessageArea(state);
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		if (color1 == null) {
			partSerialNumber.setText(state.getCurrentInstallPart().getPartSerialNumber());
			ViewControlUtil.refreshObject(partSerialNumber, state.getCurrentInstallPart().getPartSerialNumber(),
					color2, flag);
		} else {
			getView().setBackground(color1);
			partSerialNumber.setText("");
			ViewControlUtil.refreshObject(partSerialNumber, "",	color2, flag);
		}
		partSerialNumber.selectAll();
		partSerialNumber.requestFocus();
	}
}