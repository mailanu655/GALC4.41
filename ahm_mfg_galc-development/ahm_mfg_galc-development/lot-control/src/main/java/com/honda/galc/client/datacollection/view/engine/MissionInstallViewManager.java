package com.honda.galc.client.datacollection.view.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.strategy.MissionInstall;
import com.honda.galc.client.datacollection.view.DataCollectionPanel;
import com.honda.galc.client.datacollection.view.ViewControlUtil;
import com.honda.galc.client.datacollection.view.ViewManager;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartNameSerialNumber;

/**
 * 
 * <h3>MissionInstallView</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MissionInstallView description </p>
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
 * Mar 9, 2011
 *
 */

public class MissionInstallViewManager extends ViewManager implements ActionListener {
	
	private int missingRequiredPartCount = 0;
		
	public MissionInstallViewManager(ClientContext clientContext) {
		super(clientContext);
	}
	
	

	@Override
	protected DataCollectionPanel createDataCollectionPanel(
			DefaultViewProperty property) {
		if(view == null){
			view =  new MissionInstallPanel(property, viewManagerProperty.getMainWindowWidth(),
				viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}

	
	@Override
	public void productIdOk(ProcessProduct state) {
		super.productIdOk(state);
		showMissingRequiredPart(state);
	}

	private void showMissingRequiredPart(ProcessProduct state) {
		if(state.getProduct().isMissingRequiredPart()){
			int i = 0;
			for(String part: state.getProduct().getMissingRequiredPartList()){
				getView().getMissingReqPartLabel(i).setText(part);
				getView().getMissingReqPartLabel(i).setVisible(true);
				
				getView().getMissingPartField(i).setVisible(true);
				getView().getMissingPartField(i).setEnabled(true);
				getView().getMissingPartField(i).requestFocus();
				
			}
			
			renderFieldBeanInit(getView().getMissingPartField(0), true);
		}
		
	}

	@Override
	public void initPartSn(ProcessPart state) {
		
		if(state.getProduct().getMissingRequiredPartList() != null && 
				state.getProduct().getMissingRequiredPartList().size() > 0)
			return;
		
		super.initPartSn(state);
	}

	
	@Override
	public void partVisibleControl(boolean eneble, boolean visible) {
		super.partVisibleControl(eneble, visible);
		
		for(int i = 0; i < MissionInstallPanel.maxMissingPart; i++){
			
			getView().getMissingReqPartLabel(i).setVisible(visible);
			getView().getMissingPartField(i).setColor(ViewControlUtil.VIEW_COLOR_INPUT);
			ViewControlUtil.refreshObject(getView().getMissingPartField(i), "",
					ViewControlUtil.VIEW_COLOR_INPUT, eneble);
			getView().getMissingPartField(i).setVisible(visible);
			getView().getMissingPartField(i).setEnabled(eneble);
			
		}	
		
	}

	@Override
	protected void initConnections() throws Exception {
		super.initConnections();
		
		for(int i = 0; i < MissionInstallPanel.maxMissingPart; i++){
			getView().getMissingPartField(i).addActionListener(this);
			getView().getMissingPartField(i).addKeyListener(this);
		}
		
		view.getButton(1).setAction(new SkipMissionButtonAction(context, getButtonLabel(1)));
	}
	
	@Override
	public void message(DataCollectionState state) {
		super.message(state);
		
		if(MissionInstall.MISSING_REQUIRED_PART_ERROR.equals(state.getMessage().getId())){
			
			renderFieldBeanNg(getView().getMissingPartField(0), getView().getMissingPartField(0).getText());
			getView().getMissingPartField(0).requestFocus();
			
		} else if(MissionInstall.MISSING_REQUIRED_PART_OK.equals(state.getMessage().getId())){
			
			renderFieldBeanOk(getView().getMissingPartField(0), getView().getMissingPartField(0).getText());
			
			moveToFirstLotControlPart(state);
			
		} else if (MissionInstall.SKIP_MISSING_REQUIRED_PART.equals(state.getMessage().getId())){
			
			getView().getMissingPartField(0).setColor(ViewControlUtil.VIEW_COLOR_NG);
			getView().getMissingPartField(0).setBackground(ViewControlUtil.VIEW_COLOR_NG);
			getView().getMissingPartField(0).setEnabled(false);
		
			state.getProduct().getMissingRequiredPartList().clear();//remove(0); only one required part
		
			moveToFirstLotControlPart(state);
		}
	}



	private void moveToFirstLotControlPart(DataCollectionState state) {
		if(state instanceof ProcessPart)
			initPartSn((ProcessPart)state);
		else
			Logger.getLogger().error("Unexpected state:", state.getClass().getSimpleName());
	}
	

	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < MissionInstallPanel.maxMissingPart; i++){
			if(e.getSource() == getView().getMissingPartField(i))
				handleMissingRequiredPartSn(i);
		}
		
	}
	
	
	private void handleMissingRequiredPartSn(int index) {
		final PartNameSerialNumber partNameSn = new PartNameSerialNumber(
				DataCollectionController.getInstance().getState().getProduct().getMissingRequiredPartList().get(index),
				getView().getMissingPartField(index).getText());

		invokeInThread(partNameSn);

	}

	private void invokeInThread(final PartNameSerialNumber partNameSn) {
		Thread t = new Thread(){
			public void run() {
				DataCollectionController.getInstance().received(partNameSn);
			}
		};

		t.start();
	}

	//==================================Getters & Setters
	public int getMissingRequiredPartCount() {
		return missingRequiredPartCount;
	}

	public void setMissingRequiredPartCount(int missingRequiredPartCount) {
		this.missingRequiredPartCount = missingRequiredPartCount;
	}
	
	public MissionInstallPanel getView(){
		return (MissionInstallPanel) view;
	}


}
