package com.honda.galc.client.datacollection.view.knuckles;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JTextField;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.view.PartLotViewManager;
import com.honda.galc.client.datacollection.view.ViewControlUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;

/**
 * 
 * <h3>HubPressViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HubPressViewManager description </p>
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
 * Dec 8, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class HubPressViewManager extends PartLotViewManager{

	public HubPressViewManager(ClientContext clientContext) {
		super(clientContext);
	}

	@Override
	protected void initConnections() throws Exception {
		// TODO Auto-generated method stub
		super.initConnections();
		
		setTorqueDisplayProperties();
	}



	private void setTorqueDisplayProperties() {
		
		Rectangle bounds0 = view.getPartSerialNumber(0).getBounds();
		view.getTorqueValueTextField(0).setBounds((int)bounds0.getX() + viewProperty.getGap(),
				(int)bounds0.getY(), viewProperty.getTorqueFieldWidth(), viewProperty.getTorqueFieldHeight());
		
		Rectangle bounds1 = view.getPartSerialNumber(1).getBounds();
		view.getTorqueValueTextField(1).setBounds((int)bounds1.getX() + viewProperty.getGap(),
				(int)(bounds1.getY()*1.1), viewProperty.getTorqueFieldWidth(), viewProperty.getTorqueFieldHeight());
		
		Rectangle bounds = view.getPartLabel(1).getBounds();
		view.getPartLabel(1).setBounds((int)bounds.getX(), (int)(bounds.getY()*1.1), 
				(int)bounds.getWidth(), (int)bounds.getHeight());
	}



	@Override
	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		try {

			// check LotControlRuleInfo
			if (lotControlRules == null || lotControlRules.size() == 0)
				return;

			view.getTextFieldExpPidOrProdSpec().setVisible(true);

			// control Part
			for (int i = 0;((i < viewProperty.getMaxNumberOfPart()) && (i < lotControlRules.size())); i++) {
				if (lotControlRules.get(i) != null) {

					view.getPartLabel(i).setText(lotControlRules.get(i).getPartName().getWindowLabel() + ":");
					view.getPartLabel(i).setVisible(true);

					view.getTorqueResultLabel(i).setVisible(false);

				}

			}
			
			for(int i = 0; i < 2; i++){
				view.getTorqueValueTextField(i).setVisible(true);
			}
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}

	@Override
	public void initTorque(ProcessTorque state) {
		int index = state.getCurrentPartIndex();
		if(!view.getTorqueValueTextField(index).isVisible())
			view.getTorqueValueTextField(index).setVisible(true);

		view.getTorqueValueTextField(index).setBackground(Color.blue);
		view.getTorqueValueTextField(index).setEnabled(false);
	}

	@Override
	public void initPartSn(ProcessPart state) {
	}

	@Override
	public void partSnOk(ProcessPart state) {
	}

	@Override
	public void partSnNg(ProcessPart state) {
	}

	@Override
	public void completeCollectTorques(ProcessTorque state) {
	}

	@Override
	public void rejectPart(ProcessPart state) {
	}

	@Override
	public void rejectTorque(ProcessTorque state) {
	}

	@Override
	public void skipPart(DataCollectionState state) {
	}

	
	public void torqueNg(ProcessTorque state) {
		Measurement bean = state.getCurrentTorque();
		JTextField torqueValueTextField = view.getTorqueValueTextField(state.getCurrentPartIndex());
		
		if(isOverallStatusFailedMessage(state.getMessage()))
			showGoodTorque(bean, torqueValueTextField);
		else
			showBadTorque(bean, torqueValueTextField);
		
		if(state.getCurrentPartIndex() == 0){
			JTextField pressureField = view.getTorqueValueTextField(1);
			pressureField.setText(Double.toString(bean.getMeasurementAngle()));
			pressureField.setBackground(Color.white);
		}
	}

	private boolean isOverallStatusFailedMessage(Message message) {
		return message != null && "OVERALL_STATUS_FAILED".equals(message.getId());
	}

	
	public void torqueOk(ProcessTorque state) {
		Measurement bean = state.getCurrentTorque();
		JTextField torqueValueTextField = view.getTorqueValueTextField(state.getCurrentPartIndex());
		showGoodTorque(bean, torqueValueTextField);
		
		bean.setMeasurementAngle(0);//clean up
		
		if(state.getCurrentPartIndex() != 1) //Keep stoke error message 
			clearMessageArea(state);
		
	}
	
	private void showBadTorque(Measurement bean, JTextField torqueValueTextField) {
		torqueValueTextField.setBackground(ViewControlUtil.VIEW_COLOR_NG);
		
		torqueValueTextField.setDisabledTextColor(Color.black);
		torqueValueTextField.setText(Double.toString(bean.getMeasurementValue()));
	}

	private void showGoodTorque(Measurement bean, JTextField torqueValueTextField) {
		torqueValueTextField.setText(Double.toString(bean.getMeasurementValue()));
		torqueValueTextField.setBackground(ViewControlUtil.VIEW_COLOR_OK);
		torqueValueTextField.repaint();
		
	}
	
	
	@Override
	protected boolean hasSkippedPart() {
		return false;
	}

	@Override
	protected void setErrorMessage(Message errorMsg) {
		
		if(isOverallStatusFailedMessage(errorMsg)) return;
		
		super.setErrorMessage(errorMsg);
	}


}
