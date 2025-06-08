package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IPartLotViewObserver;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * <h3>PartLotViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartLotViewManager description </p>
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
 * Nov 27, 2010
 *
 */

public class PartLotViewManager extends ViewManager implements IPartLotViewObserver, ActionListener{
	private PartLotControlScreen partLotScreen;
	
	public PartLotViewManager(ClientContext clientContext) {
		super(clientContext);
		
		initButtonList();
	}
	
	private void initButtonList() {
		for(int i = 0; i < getView().getCartButtonList().size(); i++){
				getView().getCartButton(i).addActionListener(this);
	    }
	}
	
	@Override
	protected void refreshScreen() {
		super.refreshScreen();
		getView().getTextFieldSubId().setText("");
		getView().getTextFieldSubId().setVisible(isCheckExpectedProduct());

		view.getButton(3).setVisible(false);
		
		for(int i = 0; i < getView().getCartButtonList().size(); i++){
			getView().getCartButton(i).setVisible(false);
		}
		
		for (int i = 0; i < viewProperty.getMaxNumberOfPart(); i++) {
			view.getTorqueResultLabel(i).setIcon(null);
		}	
		
		view.requestFocusInvokeLater(view.getTextFieldProdId());
	}

	@Override
	protected void initConnections() throws Exception {
		super.initConnections();
		view.getLabelTorque().setVisible(false);
	}
	
	
	@Override
	public void initPartSn(ProcessPart state) {
		int index = state.getCurrentPartIndex();
		boolean result = false;
		if(isPartLotControl(index)){
			
			initPartLotControl(state, false);

		} else {
			super.initPartSn(state);
			LotControlRule rule = getLotControlRule(index);
			if(rule.isPartMaskScan()){
				initPartMask(state);
			} else if(rule.isProdLotScan()) {
				result = isProdLotChanged(state);
				if(result) {
					closeCurrentPartLot(findProductionLotScanRules(state));
				}
				displayPartLot(state, result);
			} else if(rule.isKdLotScan()) {
				result = isKdLotChanged(state);
				if(result) {
					closeCurrentPartLot(findKdLotScanRules(state));
				}
				displayPartLot(state, result);
			} else if(isPartForSideChecking(state.getCurrentPartName())) {//disable user input for field to check sub Id
					view.getPartSerialNumber(state.getCurrentPartIndex()).setEditable(false);
			}
			
		} 
		
	}
	

	@Override
	public void productIdOk(ProcessProduct state) {
		super.productIdOk(state);
		
		getView().getTextFieldSubId().getRender().renderField(new Text(state.getProduct().getSubId(),true));
		
		for(int i = 0; i < state.getLotControlRules().size(); i++){
			LotControlRule rule = state.getLotControlRules().get(i);
			if(rule.isPartLotScan()){
				getView().getCartButton(i).setVisible(true);
				getView().getCartButton(i).setEnabled(true);
			}
		}
		
	}


	@Override
	public void partSnNg(ProcessPart state) {
		super.partSnNg(state);
		
		if(isPartLotControl(state.getCurrentPartIndex()))
			initPartLotControl(state, true); 

		if(isProductSubIdPartSn(state.getCurrentPartName()) ||isPartLotControl(state.getCurrentPartIndex()))
			view.getPartSerialNumber(state.getCurrentPartIndex()).setEditable(false);
		
	}
	
	private boolean isProductSubIdPartSn(String partName) {
		return partName.contains(viewProperty.getProductSubIdLotControlPartName());
	}
	
	protected void initPartLotControl(ProcessPart state, boolean psnNg) {
		PartLot partLot = getDbManager().findCurrentPartLot(state.getCurrentPartName());
		if(context.isRemake()){
			partLotScreen = getPartLotScreen();
			partLotScreen.screenOpen(state.getCurrentPartName(), true, this);
			
			if(!partLotScreen.isOk()) return;
			partLot = partLotScreen.getCurrentPartLot();
		} else {

			if(partLot == null || partLot.getCurrentQuantity() == 0 || psnNg){
				partLotScreen = getPartLotScreen();
				partLotScreen.screenOpen(state.getCurrentPartName(), false, this);

				if(!partLotScreen.isOk()) return;
				partLot = partLotScreen.getCurrentPartLot();
			}
		}
		
		setPartSerialNumberText(partLot, state.getCurrentPartIndex());
	}
	
	
	private void initPartMask(ProcessPart state) {
		PartLot partLot = getDbManager().findCurrentPartLot(state.getCurrentPartName());
		
		getView().getPartSerialNumber(state.getCurrentPartIndex()).setText(
				(partLot == null) ? "" : partLot.getId().getPartNumber());
		getView().getPartSerialNumber(state.getCurrentPartIndex()).postActionEvent();		
	}

	private void displayPartLot(ProcessPart state, boolean needScan) {
		String text = "";
		if(!needScan) {
			PartLot partLot = getDbManager().findCurrentPartLot(state.getCurrentPartName());
			if(partLot != null) {
				text = partLot.getId().getPartNumber();
			}
		}

		UpperCaseFieldBean field = getView().getPartSerialNumber(state.getCurrentPartIndex());
		field.setText(text);
		field.postActionEvent();		
	}

	private boolean isProdLotChanged(ProcessPart state) {
		String prodLot = state.getProduct().getProductionLot();
		ExpectedProduct anObject = ServiceFactory.getDao(ExpectedProductDao.class).findByKey(context.getProcessPointId());
		BaseProduct aProduct = context.getDbManager().confirmProductOnServer(anObject.getProductId());
		return prodLot == null || aProduct == null || !prodLot.equals(aProduct.getProductionLot());
	}
	
	private void closeCurrentPartLot(List<LotControlRule> rules) {
		PartLotDao partLotDao = ServiceFactory.getDao(PartLotDao.class);
		PartLot currentPartLot;
		for(LotControlRule rule : rules) {
			currentPartLot = partLotDao.findCurrentPartLot(rule.getPartNameString(), context.isRemake());
			if(currentPartLot.getStatus() != PartLotStatus.CLOSED) {
				currentPartLot.setStatus(PartLotStatus.CLOSED);
				partLotDao.save(currentPartLot);
			}
		}
	}
	
	private List<LotControlRule> findProductionLotScanRules(ProcessPart state) {
		List<LotControlRule> allRules = findApplicableRules(state);
		List<LotControlRule> result = new ArrayList<LotControlRule>();
		for(LotControlRule rule : allRules) {
			if(rule.isProdLotScan()) {
				result.add(rule);
			}
		}
		return result;
	}
 	
	private List<LotControlRule> findKdLotScanRules(ProcessPart state) {
		List<LotControlRule> allRules = findApplicableRules(state);
		List<LotControlRule> result = new ArrayList<LotControlRule>();
		for(LotControlRule rule : allRules) {
			if(rule.isKdLotScan()) {
				result.add(rule);
			}
		}
		return result;
	}

	private List<LotControlRule> findApplicableRules(ProcessPart state) {
		List<LotControlRule> allRules = ServiceFactory.getDao(LotControlRuleDao.class).findAllByProcessPoint(context.getProcessPointId());
		List<LotControlRule> result = new ArrayList<LotControlRule>();
		String spec = state.getProduct().getProductSpec();
		String modelYearCode = ProductSpec.extractModelYearCode(spec);
		String modelCode = ProductSpec.extractModelCode(spec);
		String modelTypeCode = ProductSpec.extractModelTypeCode(spec);
		String modelOptionCode = ProductSpec.extractModelOptionCode(spec);
		LotControlRuleId id;
		boolean match;
		for(LotControlRule rule : allRules) {
			id = rule.getId();
			match = id.getModelYearCode().equals(modelYearCode) && id.getModelCode().equals(modelCode);
			if(!ProductSpec.WILDCARD.equals(id.getModelTypeCode())) {
				match = match && id.getModelTypeCode().equals(modelTypeCode);
			}
			if(!ProductSpec.WILDCARD.equals(id.getModelOptionCode())) {
				match = match && id.getModelOptionCode().equals(modelOptionCode);
			}
			if(match) {
				result.add(rule);
			}
		}
		return result;
	}
	
	private boolean isKdLotChanged(ProcessPart state) {
		String kdLot = state.getProduct().getKdLotNumber();
		ExpectedProduct anObject = ServiceFactory.getDao(ExpectedProductDao.class).findByKey(context.getProcessPointId());
		Product aProduct = (Product) context.getDbManager().confirmProductOnServer(anObject.getProductId());
		return kdLot == null || aProduct == null || !kdLot.equals(aProduct.getKdLotNumber());
	}
	
	private void setPartSerialNumberText(PartLot partLot, int index) {
		if(partLot == null){
			Logger.getLogger().error("ERROR: ", "Can not set part serial number. Part Lot is null");
			return;
		}
		
		if(!context.isRemake() && partLot.getCurrentQuantity() < 1){
			Logger.getLogger().error("ERROR: ", "Part Lot is empty. Current quantity:" + partLot.getCurrentQuantity());
			messageArea.setErrorMessageArea("No Part left in Part Lot");
		} else {
			getView().getTorqueResultLabel(index).setText("" + partLot.getCurrentQuantity());
			getView().getPartSerialNumber(index).setText(partLot.getId().getPartNumber());
			getView().getPartSerialNumber(index).postActionEvent();
		}
	}
	
	public PartLotControlScreen getPartLotScreen() {
		return new PartLotControlScreen(context.getFrame());
	}
	
	
	private boolean isPartForSideChecking(String partName) {
		String excludePartsToSave = context.getProperty().getExcludePartsToSave();
		return LotControlPartUtil.isExcludedToSave(partName.trim(), excludePartsToSave);
	}


	/**
	 * part lot or part mask for a given rule
	 * @param index
	 * @return
	 */
	protected boolean isPartLotControl(int index) {
		if(index >= DataCollectionController.getInstance().getState().getLotControlRules().size()) return false;
		LotControlRule lotControlRule = DataCollectionController.getInstance().getState().getLotControlRules().get(index);
		return lotControlRule.isPartLotScan();
	}


	protected LotControlRule getLotControlRule(int index) {
		return DataCollectionController.getInstance().getState().getLotControlRules().get(index);
	}
	
	public void rejectPart(ProcessPart state) {
		Logger.getLogger().debug("--reject:" + state.getCurrentPartIndex());

		if (isPartLotControl(state.getCurrentPartIndex())) {
			UpperCaseFieldBean current = view.getPartSerialNumber(state.getCurrentPartIndex());
			if (current.isVisible())
				current.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			
			getView().getCartButton(state.getCurrentPartIndex() + 1).setEnabled(false);
			getView().getCartButton(state.getCurrentPartIndex()).setEnabled(true);
			getView().getCartButton(state.getCurrentPartIndex()).requestFocus();
			
			if(isTheFirstCartPartLotControl(state.getCurrentPartIndex()))
				getView().getButton(3).setEnabled(false);
		} else{
			getView().getButton(3).doClick();
		}
		
		clearMessageArea(state);

	}

	private boolean isTheFirstCartPartLotControl(int index){
		for(int i = 0; i < index; i++)
			if(context.getAllRules().get(i).getSerialNumberScanFlag() > 1) return false;
			
		return context.getAllRules().get(index).getSerialNumberScanFlag() > 1;
	}

	public void rejectTorque(ProcessTorque state) {
		Logger.getLogger().debug("rejectTorque:" + state.getCurrentTorqueIndex());
		
		int pos = getCurrentTorquePosition(state);

		view.getTorqueValueTextField(pos + 1).setText(viewProperty.getDefaultTorqueValue());
		view.getTorqueValueTextField(pos + 1).setBackground(Color.WHITE);

		if(pos >= 0)
		{
			view.getTorqueValueTextField(pos).setText(viewProperty.getDefaultTorqueValue());
			view.getTorqueValueTextField(pos).requestFocus();
		} else{
			view.getPartSerialNumber(state.getCurrentPartIndex()).setBackground(Color.white);
		}
		
		clearMessageArea(state);

	}
	
	private int getCurrentTorquePosition(ProcessTorque state) {
		int torquePos = 0;
		for(int i = 0; i < state.getLotControlRules().size() && i < state.getCurrentPartIndex(); i++ ){
			torquePos += state.getLotControlRules().get(i).getParts().get(0).getMeasurementCount();
		}

		torquePos += state.getCurrentTorqueIndex();
		return torquePos;
	}
	

	public void actionPerformed(ActionEvent e) {

		for(int i = 0; i < getViewProperty().getMaxNumberOfPart(); i++){
			if(e.getSource() == this.getView().getCartButton(i) && isPartLotControl(i)){
				partLotScreen = getPartLotScreen();
				partLotScreen.screenOpen(getPartNameFromRule(i), context.isRemake(), this);

				if(partLotScreen.isOk()){
					PartLot partLot = partLotScreen.getCurrentPartLot();
					setPartSerialNumberText(partLot, i);
				}
			}
		}
		
	}
	

	private String getPartNameFromRule(int index) {
		return DataCollectionController.getInstance().getCurrentLotControlRule().getPartName().getPartName();
	}
	
	
	@Override
	public void initProductId(ProcessProduct state) {
		Logger.getLogger().debug("viewManager:initProductId()-expectedProductId:" + state.getExpectedProductId());
		
		if(isCheckExpectedProduct() && !StringUtils.isEmpty(state.getExpectedProductId())) 
			getView().getTextFieldSubId().getRender().renderField(new Text(state.getExpectedSubId(),true));
	
		super.initProductId(state);
		
	}

	@Override
	protected void renderFieldBeanPrompt(UpperCaseFieldBean bean, String text) {
		bean.setText(text);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_PROMPT);
		bean.setColor(ViewControlUtil.VIEW_COLOR_PROMPT);
		bean.setEditable(false);
		bean.setEnabled(false);
	}
	
	@Override
	public void partSnOk(ProcessPart state){
		super.partSnOk(state);
		if(isPartLotControl(state.getCurrentPartIndex())){

			JLabel resultLabel = getView().getTorqueResultLabel(state.getCurrentPartIndex());
			if(!resultLabel.isVisible()) resultLabel.setVisible(true);

			if(!StringUtils.isEmpty(resultLabel.getText()))
				resultLabel.setText(String.valueOf((Integer.parseInt(resultLabel.getText()) -1)));

			getView().getCartButton(state.getCurrentPartIndex()).setEnabled(false);

			if(!getView().getButton(3).isEnabled())
				getView().getButton(3).setEnabled(true);
		}
		

	}
	
	@Override
	public void enableExpectedProduct(boolean enabled) {
		getView().getTextFieldSubId().setVisible(enabled);
		super.enableExpectedProduct(enabled);
	}
	

	@Override
	protected void notifySkippedPartProduct() {
		if(!context.getProperty().isSaveSkippedExpectedProductOnly() && hasSkippedPart())
			EventBus.publish(new SkippedProduct(new SkippedProductId(getCurrentState().getProductId(),context.getProcessPointId())));
	}
	
	public DefaultDataCollectionPanel getView(){
		return (DefaultDataCollectionPanel)view;
	}
	
	public void completeCollectTorques(ProcessTorque state) {
		JLabel torqueResultLabel = view.getTorqueResultLabel(state.getCurrentPartIndex());
		if(!isPartLotControl(state.getCurrentPartIndex())) torqueResultLabel.setIcon(view.getImgOk());
		torqueResultLabel.setVisible(true);
	}
	
	protected void renderResultLabelNg(JLabel torqueResultLabel) {
		torqueResultLabel.setBackground(ViewControlUtil.VIEW_COLOR_NG);
		torqueResultLabel.setVisible(true);
	}
	
	

}
