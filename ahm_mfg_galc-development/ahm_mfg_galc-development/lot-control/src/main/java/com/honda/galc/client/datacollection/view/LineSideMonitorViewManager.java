package com.honda.galc.client.datacollection.view;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.view.action.CancelButtonAction;
import com.honda.galc.client.datacollection.view.action.PreviousProductButtonAction;
import com.honda.galc.client.datacollection.view.action.ResetSequenceButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipPartButtonAction;
import com.honda.galc.client.datacollection.view.action.SkipProductButtonAction;
import com.honda.galc.client.linesidemonitor.property.LineSideMonitorPropertyBean;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

/**
 * <h3>LineSideMonitorViewManager</h3>
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
public class LineSideMonitorViewManager extends ViewManager {

	protected LineSideMonitorPropertyBean lsmProperty;

	public LineSideMonitorViewManager(ClientContext clientContext) {
		super(clientContext);
	}

	@Override
	protected DataCollectionPanel createDataCollectionPanel(DefaultViewProperty property) {
		if(view == null){
			view = new LineSideMonitorDataCollectionPanel(property, getLsmProperty(), viewManagerProperty.getMainWindowWidth(),
					viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}

	protected LineSideMonitorPropertyBean getLsmProperty() {
		if (lsmProperty == null) {
			lsmProperty = PropertyService.getPropertyBean(LineSideMonitorPropertyBean.class, context.getProcessPointId());
		}
		return lsmProperty;
	}

	@Override
	protected void initConnections() throws Exception {

		super.initConnections();
		view.getButton(0).setAction(new SkipPartButtonAction(context, getButtonLabel(0)));
		view.getButton(1).setAction(new ResetSequenceButtonAction(context, getButtonLabel(1)));
		
	}

	@Override
	protected void initProductIdConnections() {
		if (!getLsmProperty().isUseExpectedAsScan()) {
			super.initProductIdConnections();
		}
	}

	@Override
	public void message(DataCollectionState state) {
		if (state == null || !state.hasMessage()) return;
		super.message(state);
	}

	@Override
	public void completeProductId(ProcessRefresh state) {
		super.completeProductId(state);
		buttonControl(view.getButton(0), false, isButtonEnabled(0));
		buttonControl(view.getButton(1), false, isButtonEnabled(1));
		buttonControl(view.getButton(2), false, isButtonEnabled(2));
		buttonControl(view.getButton(3), false, isButtonEnabled(3));
		EventBus.publish(new Event(this,EventType.CHANGED)); // inform the LineSideMonitorPanel of the change
	}

	@Override
	public void setProductInputFocused() {
		if (!getLsmProperty().isUseExpectedAsScan()) {
			super.setProductInputFocused();
		}
	}
	
	public void productIdOk(ProcessProduct state) {
		super.productIdOk(state);
	}

	public void productIdNg(ProcessProduct state) {
		super.productIdNg(state);
		buttonControl(view.getButton(1), true, isButtonEnabled(1));
		buttonControl(view.getButton(2), true, isButtonEnabled(2));
		buttonControl(view.getButton(3), true, isButtonEnabled(3));
	}
	@Override
	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		try {

			// check LotControlRuleInfo
			if (lotControlRules == null || lotControlRules.size() == 0)
				return;

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

	@Override
	public void partVisibleControl(boolean bEnable, boolean bVisible) {

		try {




			for (int i = 0; i < viewProperty.getMaxNumberOfPart(); i++) {
				view.getPartLabel(i).setVisible(bVisible);
				view.getPartSerialNumber(i).setVisible(bVisible);
				view.getPartSerialNumber(i).setColor(ViewControlUtil.VIEW_COLOR_INPUT);
				ViewControlUtil.refreshObject(view.getPartSerialNumber(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEnable);
				view.getTorqueResultLabel(i).setVisible(bVisible);
			}	

			// set Enable/Visible Torque
			view.getLabelTorque().setVisible(bVisible);
			for (int i = 0; i < viewProperty.getMaxNumberOfTorque(); i++) {
				view.getTorqueValueTextField(i).setVisible(bVisible);
				ViewControlUtil.refreshObject(view.getTorqueValueTextField(i), "",
						ViewControlUtil.VIEW_COLOR_INPUT, bEnable);
			}

			view.getButton(0).setVisible(bVisible);
			view.setTestTorqueButtonVisible(!bVisible);
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	@Override
	public void enableExpectedProduct(boolean enabled) {
		requestFocus(getView().getTextFieldProdId());
	}

	@Override
	protected void renderExpPidOrProdSpec(String label, String text) {

		if(text == null){
			Logger.getLogger().warn("WARN:", "Expected Pid or Product Spec is null.");
		} else {
			view.setProductSpecBackGroudColor(getColor(text));
		}

		view.getLabelExpPIDOrProdSpec().setText(label);

		view.getTextFieldExpPidOrProdSpec().setText(text);

		view.getLabelExpPIDOrProdSpec().repaint();
	}

	@Override
	protected boolean isButtonEnabled(int i) {
		switch (i) {
		case 0:
			return viewProperty.isEnableSkipPart();
		case 1:
			return viewProperty.isEnableResetSequence();
		case 2:
			return viewProperty.isEnableSkipProduct();
		case 3:
			return viewProperty.isEnablePrevProduct();
		default:
			return false;
		}
	}

	@Override
	protected void renderFieldBeanNg(UpperCaseFieldBean bean, String text) {
		if (!getLsmProperty().isUseExpectedAsScan() || ObjectUtils.notEqual(bean, view.getTextFieldProdId())) {
			super.renderFieldBeanNg(bean, text);
		}
	}

	@Override
	protected void renderFieldBeanInit(UpperCaseFieldBean bean, boolean requestFocus) {
		if (!getLsmProperty().isUseExpectedAsScan() || ObjectUtils.notEqual(bean, view.getTextFieldProdId())) {
			super.renderFieldBeanInit(bean, requestFocus);
		}
	}

	@Override
	protected void renderFieldBeanOk(UpperCaseFieldBean bean, String serialNumber) {
		if (!getLsmProperty().isUseExpectedAsScan() || ObjectUtils.notEqual(bean, view.getTextFieldProdId())) {
			super.renderFieldBeanOk(bean, serialNumber);
		}
	}

	@Override
	public boolean handleUniqueScanCode(UniqueScanType uniqueScanType) {
		if (uniqueScanType.equals(UniqueScanType.REFRESH) && viewProperty.isEnableCancel()) {
			if (lsmProperty.isUseExpectedAsScan()) {
				final String productId = getCurrentState(context.getProcessPointId()).getProductId();
				final ProductId request = new ProductId(productId);
				if (!context.isTrimProductId()) {
					request.setProductIdWithoutTrim(productId);
				}
				runInSeparateThread(request, ProcessRefresh.class);
			} else {
				CancelButtonAction action = new CancelButtonAction(context, UniqueScanType.REFRESH.name());
				action.actionPerformed(null);
			}
			return true;
		}
		return super.handleUniqueScanCode(uniqueScanType);
	}

	@Override
	protected int getButtonIndexForUniqueScan(UniqueScanType uniqueScanType) {
		switch (uniqueScanType) {
		case SKIP:
			return 0;
		case RESETSEQ:
			return 1;
		case NEXTVIN:
			return 2;
		case PREVVIN:
			return 3;
		default:
			return -1;
		}
	}

}
