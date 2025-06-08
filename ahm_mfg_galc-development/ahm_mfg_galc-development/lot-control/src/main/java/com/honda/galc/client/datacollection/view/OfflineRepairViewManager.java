package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IOfflineRepairViewObserver;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.IProcessProductRepair;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.action.CancelButtonAction;
import com.honda.galc.client.datacollection.view.action.MeasurementValueInputAction;
import com.honda.galc.client.datacollection.view.action.PartSerialNumberInputAction;
import com.honda.galc.client.datacollection.view.action.ProductButtonAction;
import com.honda.galc.client.datacollection.view.action.ProductIdInputAction;
import com.honda.galc.client.datacollection.view.action.TestTorqueButtonAction;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.RepairProcessPointDao;
import com.honda.galc.datacollection.BasePartResult;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.RepairProcessPoint;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
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
public class OfflineRepairViewManager extends ViewManagerBase
implements IOfflineRepairViewObserver, ConnectionStatusListener,ListSelectionListener, ActionListener{

	public static String TEXT_EXPECTED_PRODUCT_ID ="Expected";
	
	protected OfflineRepairDataCollectionPanel view;
	protected JPanel viewPanel;
	protected DefaultViewProperty viewProperty;
	private int index = -1;
		
	public OfflineRepairViewManager(ClientContext clientContext) {

		super(clientContext);

		init();
	}

	protected OfflineRepairDataCollectionPanel createDataCollectionPanel(DefaultViewProperty property) {
		if(view == null){
			view =  new OfflineRepairDataCollectionPanel(property, viewManagerProperty.getMainWindowWidth(),
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

	private JPanel getViewPanel() {
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
		view.getJButtonProductId().setAction(new ProductButtonAction(context.getFrame(), viewProperty.getProductType(), getButtonLabel(), view.getTextFieldProdId()));
		view.getTextFieldProdId().setAction(new ProductIdInputAction(context, "ProductIdInput"));
		view.getTextFieldProdId().addKeyListener(this);
		view.getTextFieldProdId().addCaretListener(this);
		
		view.getPartSerialNumber(0).setAction(new PartSerialNumberInputAction(context, "PartSnInput"));
		view.getPartSerialNumber(0).addKeyListener(this);
		view.getPartSerialNumber(0).addActionListener(dataCollectionEventHandler);
		
		
		for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
			view.getTorqueValueTextField(i).addKeyListener(dataCollectionEventHandler);
			if(!viewProperty.isMeasurementEditable()){
				view.getTorqueValueTextField(i).setEditable(false);
			}else{
				view.getTorqueValueTextField(i).setAction(new MeasurementValueInputAction(context, "MeasurementValueInput"));
				view.getTorqueValueTextField(i).setEditable(true);
			}
		}
				
		view.getButton(0).setAction(new CancelButtonAction(context, getButtonLabel(0)));
		view.setTestTorqueButtonAction(new TestTorqueButtonAction(context, "Test Torque"));
		view.getRemoveResultsButton().addActionListener(this);
		view.getWiderButton().setVisible(false);
		view.getRepairPartsTable().addListSelectionListener(this);
		
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
		
		showErrorMessage(arg);
		super.update(o, arg);
	}

	protected void showErrorMessage(Object arg) {
		if(arg != null){
			Message errorMsg = getErrorMessage((DataCollectionState)arg);
			if(errorMsg != null && !errorMsg.isEmpty()){
				setErrorMessage(errorMsg);
				if(LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED.equalsIgnoreCase(errorMsg.getId())){
					disableTorqueDevices();
				}
			}
		}
		
	}

	@Override
	protected boolean isButtonEnabled(int i) {
		switch (i) {
			case 0:
				return viewProperty.isEnableCancel();
			default:
				return false;
		}
	}
	
	

	public void completeCollectTorques(ProcessTorque state) {
	
	}

	public void initPartSn(ProcessPart state) {
		disableTorqueDevices();
		int partResultSize = view.getRepairProcessPointTableModel().getItems().size();
		BasePartResult partResult = view.getRepairProcessPointTableModel().getSelectedItem();
		if(!(partResultSize > 0)) return;
		
		if (isAutoAdvancePart()) {
			clearPartMeasurements(state);
		}
		if(state.getLotControlRules().size() > 0){
			setPartLabel(state.getCurrentLotControlRule());
		
			if(partResult != null && partResult.getLotControlRule().isScan()){
				if(!isAutoAdvancePart()){
					showPartSerialNumber(partResult);
					if(partResult.getMeasurementCount()>0){
						torqueValueVisibleControl(state.getCurrentPartTorqueCount(), "Torque:", partResult.getInstalledPart().getMeasurements());
					}else{
						torqueVisibleControl(0, true,  "Torque:", state.getCurrentTorqueIndex());
					}
				}else{
					renderFieldBeanInit(view.getPartSerialNumber(state.getCurrentPartIndex()), true);
					int torqueCount = state.getCurrentPartTorqueCount();
					String torqueLabel = "Torque:";
					torqueVisibleControl(torqueCount, true, torqueLabel, state.getCurrentTorqueIndex());
					view.getRemoveResultsButton().setVisible(false);
				}
			}
		}
	}
	
	private void clearPartMeasurements(DataCollectionState state) {
		for (InstalledPart installedPart : state.getStateBean().getProduct().getPartList()) {
			installedPart.getMeasurements().clear();
		}
	}

	public void initTorque(ProcessTorque state) {
		disableTorqueDevices();
		int partResultSize = view.getRepairProcessPointTableModel().getItems().size();
		if(!(partResultSize > 0)) return;
		BasePartResult partResult = isAutoAdvancePart() ?null :view.getRepairProcessPointTableModel().getSelectedItem();
		
		if(state.getLotControlRules().size() > 0){
			UpperCaseFieldBean partSn = view.getPartSerialNumber(state.getCurrentPartIndex());
			final String PROMPT_INPUT_TORQUE = "Please input Torques.";
			String torqueLabel = getTorqueLabelText(state);
			//if not verify part SN, then prompt input torque
			if(!state.isScanPartSerialNumber())
				renderFieldBeanPrompt(partSn, PROMPT_INPUT_TORQUE);
			if(showTorque(partResult)){
				torqueVisibleControl(state.getCurrentPartTorqueCount(), true, torqueLabel, state.getCurrentTorqueIndex() );
				view.getTorqueValueTextField(state.getCurrentTorqueIndex()).requestFocus();
				enableTorqueDevices(state);
				view.getRemoveResultsButton().setVisible(false);
			}else{
				torqueValueVisibleControl(state.getCurrentPartTorqueCount(), torqueLabel, partResult.getInstalledPart().getMeasurements());
			}
		}
	}


	public void initProductId(ProcessProduct state) {
		Logger.getLogger().debug("viewManager:initProductId()-expectedProductId:" + state.getExpectedProductId());
		if(isCheckExpectedProduct() && !StringUtils.isEmpty(state.getExpectedProductId())) {
			renderExpPidOrProdSpec(view.getExpectProductIdLabel(), state.getExpectedProductId());
			enableExpectedProductIdControlButton(false);
			view.repaint();
		}
		
		if(isAfOnSeqNumExist() && !StringUtils.isEmpty(getCurrentState().getAfOnSeqNo()))
			renderSeqNo(view.getAfOnSeqNumLabel(), state.getAfOnSeqNo());
		
		view.getJButtonProductId().setEnabled(true);
		view.getTestTorqueButton().setVisible(true);
		setProductInputFocused();
		index = -1;
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
		if(!isAutoAdvancePart())view.getRemoveResultsButton().setVisible(true);
	}
	
	public void productIdOk(ProcessProduct state) {

		//Show product Id status
		renderFieldBeanOk(view.getTextFieldProdId(), state.getProductId());
		
		renderExpPidOrProdSpecOnOk(state);
		
		view.getJButtonProductId().setEnabled(false);
		
		if(isAfOnSeqNumExist()){
			renderSeqNo(view.getAfOnSeqNumLabel(), getSequenceNumberFromState(state));
		}
		//Show data collection buttons
		buttonControl(view.getButton(0), true, isButtonEnabled(0));
		clearMessageArea(state);
		try{
			List<BasePartResult> partResults = 	getPartResults(state.getProductId());	
			sortAndrefreshRepairParts(partResults);
			view.getRepairPartsTable().clearSelection();
			partVisibleControl(getLotControlRules(partResults));
			notifyNewProduct(state.getProductId());
			advanceToNextRepairPart();
			view.getTestTorqueButton().setVisible(false);
		}catch(LotControlTaskException e){
			handleException(e);
		}
		catch(Exception e){
			handleException(e);
		}
			
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

	protected void handleException(Exception e) {
		
		this.setErrorMessage(e.getMessage());
		
	}

	private List<LotControlRule> getLotControlRules(List<BasePartResult> partResults) {
		List<LotControlRule> lotControlRules = new ArrayList<LotControlRule>();
		
		for(BasePartResult partResult:partResults){
			lotControlRules.add(partResult.getLotControlRule());
		}
		return lotControlRules;
	}

	private List<BasePartResult> getPartResults(final String productId){
		final boolean needDeviceId = hasMultipleToolsAttached();
		final List<BasePartResult> partResults = new Vector<BasePartResult>();
		
		final Map<String, List<RepairProcessPoint>> partNameRepairPartMap = getPartnames();
		final Set<String> partNames = partNameRepairPartMap.keySet();
		final List<InstalledPart> installedParts = getInstalledParts(productId,partNames);
		final BaseProductSpec productSpec = ProductTypeUtil.getProductSpecDao(context.getProductType()).findByProductSpecCode(DataCollectionController.getInstance().getState().getProductSpecCode(), context.getProductType().toString());
		
		List<Thread> partResultLoadThreads = new ArrayList<Thread>();
		for(final String partName:partNames){
			Thread partResultLoadThread = new Thread(new Runnable() {
				public void run() {
					LotControlRule rule = populateLotControlRule(partNameRepairPartMap.get(partName),getBestMatchedRule(partName, productSpec), needDeviceId);
					InstalledPart part = getInstalledPart(productId,partName,installedParts);
					if(rule != null && part!= null){
						BasePartResult partResult = new BasePartResult(rule,part);
						partResults.add(partResult);
					}
				}
			});
			partResultLoadThreads.add(partResultLoadThread);
		}
		for (Thread partResultLoadThread : partResultLoadThreads) {
			partResultLoadThread.start();
		}
		for (Thread partResultLoadThread : partResultLoadThreads) {
			try {
				partResultLoadThread.join(30000);
			} catch (InterruptedException e) {
				continue;
			}
		}
		return partResults;
	}
	private LotControlRule getBestMatchedRule(String partName, BaseProductSpec productSpec){
		List<LotControlRule> rulesOfSamePart = ServiceFactory.getDao(LotControlRuleDao.class).findAllByPartName(partName);
		
		return LotControlPartUtil.findBestMatchedRulesOfSameRuleSequenceByPartNameAndSpec(productSpec, rulesOfSamePart);
		
	}
	
	private Map<String, List<RepairProcessPoint>> getPartnames(){
		List<RepairProcessPoint> repairParts = getRepairParts();
		Map<String, List<RepairProcessPoint>> partNameRepairPartMap = new LinkedHashMap<String, List<RepairProcessPoint>>();
		List<String> partNames= new ArrayList<String>();
		
		for(RepairProcessPoint repairProcessPoint: repairParts ){
			String pName = repairProcessPoint.getId().getPartName().trim();
			
			if(!partNames.contains(pName)){
				List<RepairProcessPoint> parts = new ArrayList<RepairProcessPoint>();
				partNames.add(pName);
				parts.add(repairProcessPoint);
				partNameRepairPartMap.put(pName, parts);
			}else{
				partNameRepairPartMap.get(pName).add(repairProcessPoint);
			}
		}
		
		return partNameRepairPartMap;
	}
	
	private List<RepairProcessPoint> getRepairParts() {
		return ServiceFactory.getDao(RepairProcessPointDao.class).findAllRepairPartsForProcessPoint(context.getProcessPointId());
	}
	
	private List<InstalledPart> getInstalledParts(String productId, Set<String> partNames){
		List<String> pNames = new ArrayList<String>();
		for(String pname:partNames){
			pNames.add(pname);
		}
		
		List<InstalledPart> installedParts = ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(productId, pNames);
		if(installedParts != null && installedParts.size()> 0){
			List<InstalledPart> installedPartsWithMeasurements = ServiceFactory.getDao(MeasurementDao.class).findMeasurementsForInstalledParts(installedParts, true);
			if(installedPartsWithMeasurements != null && installedPartsWithMeasurements.size()> 0) return installedPartsWithMeasurements;
		}
		return new ArrayList<InstalledPart>();
	}

	private InstalledPart getInstalledPart(String productId, String partName, List<InstalledPart> installedParts){
		
		InstalledPart installedPart = null;
		for(InstalledPart part: installedParts){
			if(part.getPartName().trim().equalsIgnoreCase(partName)){
				installedPart = part;
				break;
			}
		}
		if(installedPart == null){
			InstalledPartId partId = new InstalledPartId();
			partId.setPartName(partName);partId.setProductId(productId);
			installedPart = new InstalledPart();
			installedPart.setId(partId);
			installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
			
		}
		return installedPart;
	}
	
	private LotControlRule populateLotControlRule(List<RepairProcessPoint> rParts,LotControlRule rule, boolean needDeviceId){
		if(rule == null) return null;
		
		if(rParts != null){
			for(RepairProcessPoint rp: rParts){
				PartByProductSpecCode part = rule.getPartByProductSpecs().get(0);
				if(part.getId().getPartId().equalsIgnoreCase(rp.getId().getPartId()) && part.getId().getPartName().equalsIgnoreCase(rp.getId().getPartName())){
					if(StringUtils.isNotEmpty(rp.getDeviceId()))
					{
						rule.setDeviceId(rp.getDeviceId());
					}else{
						if(needDeviceId && part.getPartSpec().getMeasurementCount()>0){ 
							LotControlTaskException e = new LotControlTaskException("Multiple Tools attached DeviceId not defined for repair parts","Message", MessageType.ERROR);
							throw e;
						}else{
							rule.setDeviceId(getDefaultDeviceName());
						}
						
					}
					if(StringUtils.isNotEmpty(rp.getInstructionCode()))
						rule.setInstructionCode(rp.getInstructionCode());
					
					break;
				}
			}
		}
		
		
		return rule;
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
		
	}

	public void receivedProductId(ProcessProduct state) {
		view.getTextFieldProdId().setText(state.getProductId());
	}

	public void torqueNg(ProcessTorque state) {
		String mvalue = state.getCurrentTorque()!= null?Double.toString(state.getCurrentTorque().getMeasurementValue()):"0.0";
		ViewControlUtil.refreshObject(view.getTorqueValueTextField(state.getCurrentTorqueIndex()),
				mvalue, ViewControlUtil.VIEW_COLOR_NG, true);
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
		
		if(!isAutoAdvancePart())view.getRemoveResultsButton().setVisible(true);
	}

	public void notifyError(DataCollectionState state) {
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
		
	}

	protected void renderResultLabelNg(JLabel torqueResultLabel) {
		torqueResultLabel.setIcon(view.getImgNg());
		torqueResultLabel.setVisible(true);
	}


	protected void renderResultLabelOk(JLabel torqueResultLabel) {
		torqueResultLabel.setIcon(view.getImgOk());
		torqueResultLabel.setVisible(true);
	}

	public void completeProductId(ProcessRefresh state) {
		
	}

	public void refreshScreen(DataCollectionState state) {
		
		refreshScreen(0);
	}

	// Getters & setters
	@Override
	public DataCollectionPanelBase getView() {
		return view;
	}

	public void setView(OfflineRepairDataCollectionPanel view) {
		this.view = view;
	}
	
	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		try {

			// check LotControlRuleInfo
			if (lotControlRules == null || lotControlRules.size() == 0)
				return;

			view.getTextFieldExpPidOrProdSpec().setVisible(true);

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

			
				view.getPartLabel(0).setVisible(bVisible);
				view.getPartSerialNumber(0).setVisible(bVisible);
				view.getTorqueResultLabel(0).setVisible(bVisible);
		

			// set Enable/Visible Torque
			view.getLabelTorque().setVisible(bVisible);
			for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
				view.getTorqueValueTextField(i).setVisible(bVisible);
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEneble);
			}
			
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

	private boolean isSkipProduct() {
		if(getCurrentState().getProduct() == null) return true; //don't wait if event no product
		
		return getCurrentState().getProduct().isSkipped();
	}

	protected void refreshScreen() {

		// refresh Part/Torque
		partVisibleControl(false, false);

		// refresh Button			
	
		view.getLabelExpPIDOrProdSpec().setVisible(false);	
		ViewControlUtil.refreshObject(view.getLabelExpPIDOrProdSpec(), view.getProdSpecLabel());
		ViewControlUtil.refreshObject(view.getTextFieldExpPidOrProdSpec(), "",	ViewControlUtil.VIEW_COLOR_INPUT,false);
		
		view.getLabelAfOnSeqNum().setVisible(false);
		view.getLabelAfOnSeqNumValue().setVisible(false);
		if(isAfOnSeqNumExist() && !StringUtils.isEmpty(getCurrentState().getAfOnSeqNo())){
			ViewControlUtil.refreshObject(view.getLabelAfOnSeqNum(), view.getAfOnSeqNumLabel());
			ViewControlUtil.refreshObject(view.getLabelAfOnSeqNumValue(),	"",	ViewControlUtil.VIEW_COLOR_INPUT,false);			
		}
		
		enableExpectedProductIdControlButton(false);
		
		renderFieldBeanInit(view.getTextFieldProdId(), true);
		
		messageArea.setErrorMessageArea(null);
		view.getRepairProcessPointTableModel().refresh(new ArrayList<BasePartResult>());
		view.getRemoveResultsButton().setVisible(false);
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
			DataCollectionController.getInstance().getFsm().message(msg);

		}

		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, status.isConnected()));
	}
	
	public DataCollectionState getCurrentState(){
		return DataCollectionController.getInstance().getState();
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
		bean.setVisible(true);
	}
	
	protected void renderFieldBeanPrompt(UpperCaseFieldBean bean, String text) {
		bean.setText(text);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_PROMPT);
		bean.setColor(ViewControlUtil.VIEW_COLOR_PROMPT);
		bean.setEditable(false);
		bean.setEnabled(false);
		bean.setVisible(true);
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
		bean.setVisible(true);		
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
	
	
	
	public void skipCurrentInput(final ProcessTorque state) {
		
	}

	public void initRefreshDelay(ProcessRefresh state) {
	}
	
	protected void renderSeqNo(String label, String text) {
		view.getLabelAfOnSeqNum().setText(label);
		view.getLabelAfOnSeqNumValue().setText(text!= null?StringUtil.padLeft(text,5,'0'):"N/A");
		view.getLabelAfOnSeqNum().setVisible(true);
		view.getLabelAfOnSeqNumValue().setVisible(true);
	}
	 
	protected String getTorqueLabelText(DataCollectionState state, int torqueIndex) {
		String torqueLabel = "Torque:";
		List<MeasurementSpec> measurementSpecs = state.getCurrentLotControlRulePartList().get(0).getMeasurementSpecs();
		MeasurementSpec measurementSpec = torqueIndex < measurementSpecs.size()?measurementSpecs.get(torqueIndex): null;
		if (measurementSpec != null) {
			String minLimit = String.valueOf(measurementSpec.getMinimumLimit());
			String maxLimit = String.valueOf(measurementSpec.getMaximumLimit());
			String instructionCode = state.getCurrentLotControlRule().getInstructionCode();
			String deviceId = state.getCurrentLotControlRule().getDeviceId();
			String label = "<HTML><p align='right'>" + torqueLabel + "<br> Min/Max " + minLimit + "/" + maxLimit
					+ " <br>PSet# ("+instructionCode+")<br> "+deviceId+" </p></HTML>";
			return label;
		}
		return torqueLabel;
	}
	 
	protected String getTorqueLabelText(DataCollectionState state) {
		return getTorqueLabelText(state, state.getCurrentTorqueIndex());
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		clearMessageArea(null);
		if(e.getSource() == (view.getRepairPartsTable().getTable().getSelectionModel())){
			BasePartResult partResult = view.getRepairProcessPointTableModel().getSelectedItem();
			if(partResult != null){
				if(partResult.getInstalledPart()!= null){
					view.getRemoveResultsButton().setVisible(true);	
				}else{
					view.getRemoveResultsButton().setVisible(false);
				}
				loadLotControlRulesForSelectedPartResult(partResult);	
			}
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == view.getRemoveResultsButton()){
			removeInstalledPartResults();
		}
	}
	
	private void removeInstalledPartResults() {
		clearMessageArea(null);
		BasePartResult partResult = view.getRepairProcessPointTableModel().getSelectedItem();
		if(partResult != null){
			ServiceFactory.getDao(MeasurementDao.class).removeAll(partResult.getInstalledPart().getMeasurements());
			ServiceFactory.getDao(InstalledPartDao.class).remove(partResult.getBuildResult());
	
			List<BasePartResult> partResults = 	getPartResults(DataCollectionController.getInstance().getState().getProductId());	
			sortAndrefreshRepairParts(partResults);
			
			resetAfterTableRefresh(partResult);
			Logger.getLogger().info("Removed installed part results: " + partResult);
		}
	}

	
	private boolean isAutoAdvancePart(){
		return getProperty().isAutoAdvancePart();
	}
	
	protected TerminalPropertyBean getProperty() {
		return PropertyService.getPropertyBean(TerminalPropertyBean.class, context.getAppContext().getApplicationId());
	}

	public void refreshRepairParts(ProcessProduct state) {
		BasePartResult partResult = view.getRepairProcessPointTableModel().getSelectedItem();
		List<BasePartResult> partResults = 	getPartResults(state.getProductId());	
		sortAndrefreshRepairParts(partResults);
		partVisibleControl(getLotControlRules(partResults));
		
		clearMessageArea(state);
		
		notifyNewProduct(state.getProductId());
		advanceToNextRepairPart();
		if(!isAutoAdvancePart()){
			view.getRepairPartsTable().clearSelection();
			resetAfterTableRefresh(partResult);
		}
	}

	public void advanceToNextRepairPart() {
		int partResultSize = view.getRepairProcessPointTableModel().getItems().size();
		if(isAutoAdvancePart()){
			if(partResultSize > 0){
				if(index <  (partResultSize-1)){
					index = index +1;
					if(index < partResultSize){
						BasePartResult partResult = view.getRepairProcessPointTableModel().getItems().get(index);
						view.getRepairProcessPointTableModel().selectItem(partResult);
						loadLotControlRulesForSelectedPartResult(partResult);
					}
			    }else{
			     	notifyFinishProduct();
			    	index = -1;
			    	clickButton(0);
			    }
			}
		}else{
			DataCollectionController.getInstance().getState().getStateBean().setLotControlRules(new ArrayList<LotControlRule>());
			DataCollectionController.getInstance().getState().getStateBean().setCurrentPartIndex(-1);
		}
		
	}
	
	private void loadLotControlRulesForSelectedPartResult(BasePartResult partResult){
		LotControlRule rule = partResult.getLotControlRule();
		List<LotControlRule> lotControlRules = new ArrayList<LotControlRule>();
		lotControlRules.add(rule);
		DataCollectionController.getInstance().getState().getStateBean().setLotControlRules(lotControlRules);
		if(!(DataCollectionController.getInstance().getState() instanceof IProcessProductRepair))
			DataCollectionController.getInstance().getFsm().repairPartSelected();
	}
	
	private void setPartLabel(LotControlRule rule){
		String partMask = CommonPartUtility.parsePartMaskDisplay(rule.getPartMasks());
		partMask = partMask.contains("<<")?partMask.replace("<", "&lt;"):partMask;
		partMask = partMask.contains(">>")?partMask.replace(">", "&gt;"):partMask;
		String label = "<HTML><p align='right'>" + rule.getPartNameString()+ ":" + "<br>" + partMask + "</p></HTML>";
		view.getPartLabel(0).setText(label);
		view.getPartLabel(0).setVisible(true);
	}
	
	private List<BasePartResult> sortAndrefreshRepairParts(List<BasePartResult> partResults){
		if (!isAutoAdvancePart()) {
			Collections.sort(partResults, new Comparator<BasePartResult>() {
				public int compare(BasePartResult part0, BasePartResult part1) {
					int result = part0.getStatus().getId() - part1.getStatus().getId();
					if (result == 0) {
						return stringCompare(part0.getLotControlRule().getPartName().getPartName(), part1.getLotControlRule().getPartName().getPartName());
					}
					return result;
				}
			});
			view.getRepairProcessPointTableModel().refresh(partResults);
		} else {
			Collections.sort(partResults, new Comparator<BasePartResult>() {
				public int compare(BasePartResult part0, BasePartResult part1) {
					return stringCompare(part0.getLotControlRule().getPartName().getPartName(), part1.getLotControlRule().getPartName().getPartName());
				}
			});
			view.getRepairProcessPointTableModel().refresh(partResults);
		}
		return partResults;
	}
	private int stringCompare(String str1, String str2) {
		if (str1 == null) {
			if (str2 == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (str2 == null) {
			return 1;
		}
		return str1.compareTo(str2);
	}
	
	private String getDefaultDeviceName(){
		String deviceName = "";
		Set<String> deviceNames = DeviceManager.getInstance().getDeviceNames();
		for(String name:deviceNames){
			deviceName = name;
			break;
		}
		return deviceName;
	}
	
	private boolean hasMultipleToolsAttached(){
		Set<String> deviceNames = DeviceManager.getInstance().getDeviceNames();
		if(deviceNames != null && deviceNames.size() > 1) return true;
		
		return false;
	}
	
	private void resetAfterTableRefresh(BasePartResult partResult){
		
		if(partResult != null){
			List<BasePartResult> sortedList = view.getRepairProcessPointTableModel().getItems();
			for(int i=0;i<sortedList.size();i++){
				if(sortedList.get(i).getInstalledPart().getPartName().trim().equalsIgnoreCase(partResult.getInstalledPart().getPartName().trim())){
					DataCollectionController.getInstance().getState().getStateBean().setCurrentPartIndex(-1);
					DataCollectionController.getInstance().getState().getStateBean().setCurrentTorqueIndex(-1);
					BasePartResult pResult = view.getRepairProcessPointTableModel().getItems().get(i);
					view.getRepairProcessPointTableModel().selectItem(pResult);
					break;
				}
			}
		}
	}
	
	private boolean showTorque(BasePartResult partResult){
		if(partResult == null) return true;
		if(partResult.getInstalledPart() == null) return true;
		if(partResult.getInstalledPart().getMeasurements().isEmpty()) return true;
		
		return false;
	}
	
	public void torqueValueVisibleControl(int aTorqueCount,String torqueLabel, List<Measurement> measurements) {

		if (aTorqueCount > 0){	
			view.getLabelTorque().setText(torqueLabel);
			view.getLabelTorque().setVisible(true);
		}else{
			view.getLabelTorque().setVisible(false);
		}
		
		for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
			if(i<measurements.size()){
				double meas = measurements.get(i).getMeasurementValue();
				String measValue =  Double.toString(meas);
				if(measurements.get(i).isStatus())
					ViewControlUtil.refreshObject(view.getTorqueValueTextField(i),
						measValue, ViewControlUtil.VIEW_COLOR_OK, false);
				else
					ViewControlUtil.refreshObject(view.getTorqueValueTextField(i),
							measValue, ViewControlUtil.VIEW_COLOR_NG, false);
					
			}else{
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i),
						"0.0", ViewControlUtil.VIEW_COLOR_INPUT, false);
				
			}
			view.getTorqueValueTextField(i).setVisible(i < aTorqueCount);
		}
		view.getRemoveResultsButton().setVisible(true);
		disableTorqueDevices();
	}
	
	public void showPartSerialNumber(BasePartResult partResult){
		if(partResult.getInstalledPart()!= null){
			if(partResult.getInstalledPart().isStatusOk()){
				if(partResult.getLotControlRule().isScan()){
					renderFieldBeanOk(view.getPartSerialNumber(0), partResult.getInstalledPart().getPartSerialNumber());
				}else{
					renderFieldBeanPrompt(view.getPartSerialNumber(0), "");
				}
				view.getRemoveResultsButton().setVisible(true);	
			}else{
				if(partResult.getLotControlRule().isScan()){
					String partSerialNumber = partResult.getInstalledPart().getPartSerialNumber();
					if(StringUtils.isEmpty(partSerialNumber)){
						renderFieldBeanInit(view.getPartSerialNumber(0), true);
						view.getRemoveResultsButton().setVisible(false);	
					}else{
						renderFieldBeanNg(view.getPartSerialNumber(0),partSerialNumber );
						view.getRemoveResultsButton().setVisible(true);	
					}
				}else{
					renderFieldBeanPrompt(view.getPartSerialNumber(0), "");
				}
			}
		}
	}

	public void completePartSerialNumber(ProcessPart state) {

	}
	

	public void partSnOkButWait(ProcessPart state) {}

	public void receivedBypass(ProcessPart state) {}

	public void receivedAuto(ProcessPart state) {}
}
