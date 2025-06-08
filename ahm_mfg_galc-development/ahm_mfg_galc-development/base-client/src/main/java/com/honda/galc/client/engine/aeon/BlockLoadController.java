package com.honda.galc.client.engine.aeon;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.lotcontrol.ITorqueDeviceListener;
import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CycleComplete;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.MCNumber;
import com.honda.galc.device.dataformat.ResetFlag;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>BlockLoadController Class description</h3>
 * <p> BlockLoadController description </p>
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
 * @author Jeffray Huang<br>
 * Mar 26, 2014
 *
 *
 */
public class BlockLoadController implements ActionListener, DeviceListener, ITorqueDeviceListener, ListSelectionListener{
	private BlockLoadModel model;
	private BlockLoadView view;
	private Block currentBlock;
	
	private TorqueSocketDevice torqueDevice;
	
	
	protected enum State{WaitingForMc, WaitingForTorque, WaitingForCycleComplete};
	private State currentState = State.WaitingForMc;
	
	private final ClientAudioManager audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
	
	public BlockLoadController(BlockLoadModel model,BlockLoadView view) {
		this.model = model;
		this.view = view;
		registerEiDeviceListener();
		registerTorqueSocketDevice();
	}

	
	public void actionPerformed(ActionEvent e) {
		clearMessage();
		try{
			if(e.getSource() == view.resetButton) reset();
			else if(e.getSource() == view.removeButton) removeBlock();
			else if(e.getSource() == view.reloadButton) reloadBlock();
			else if(e.getSource() == view.remakeButton) remakeBlock();
			else if(e.getSource() == view.skipPartButton) skipPart();
			else if(e.getSource() == view.blockMCTextField.getComponent()){
				SwingUtilities.invokeLater(new Runnable() {
					public void run(){
						mcNumberInputed();
					}
				});
			}
		}catch(Exception ex) {
			displayErrorMessage("Exception occurred : " + ex.getMessage());
		}
	}
	
	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			eiDevice.registerDeviceListener(this, getDeviceInputDataList());
			eiDevice.reqisterDeviceData(getDeviceOutputDataList());
		}
	}
	
	private void registerTorqueSocketDevice() {
		for(IDevice device : DeviceManager.getInstance().getDevices().values()){
			if(device instanceof TorqueSocketDevice && device.isEnabled()){
				torqueDevice = (TorqueSocketDevice)device;
				torqueDevice.startDevice();
				//disable torque guns when starting client
				disableTorqueDevice();
				torqueDevice.registerListener(this);
				torqueDevice.requestControl(this);
				return;
			}
		}
	}
	
	private void disableTorqueDevice() {
		if(torqueDevice == null) return;
		try {
			getLogger().info("start disableTorqueDevice.");
			torqueDevice.abortJob();
			getLogger().info("disableTorqueDevice succeeded.");
		} catch (Exception e) {
			getLogger().error(e, "Exception:" + e.getMessage());
		}
	}
	
	private void setJobTorqueDevice(String mcNumber,String instructionCode) {
		try {
			getLogger().info("start activating TorqueDevice.");
			
			torqueDevice.requestVinDownload(mcNumber);
			torqueDevice.setJob(instructionCode);
			torqueDevice.setInstructionCodeSent(true);
			getLogger().info("activating torqueDevice succeeded.");
			
		} catch (Exception e) {
			getLogger().error(e, "Exception:" + e.getMessage());
		}
	}
	
	private IDeviceData resetReceived(){
		getLogger().info("Reset PLC signal received");
		reset();
		return DataCollectionComplete.OK();
	}

	private void reset() {
		model.resetData();
		view.resetMCDataFields();
		view.resetTorqueDataFields();
		view.loadData();
		currentState = State.WaitingForMc;
		disableTorqueDevice();
		view.updateButtonStatus();
		getLogger().info("Reset to accept new block");
	}
	
	private void skipPart() {
		for(int i=0; i< 5; i++) {
			view.torqueTextFields.get(i).setText("");
			view.torqueTextFields.get(i).setBackground(Color.green );
		}
		
		model.getTorqueResults().clear();
		model.setTorqueAttemptCount(0);
		model.setTorqueIndex(0);
		
		sendDataCollectionComplete(true);

		currentState = State.WaitingForCycleComplete;
		view.updateButtonStatus();
		view.getMainWindow().setMessage("waiting for cycle complete");
	}

	private void removeBlock() {
		BlockLoad blockLoad = view.blockListPane.getSelectedItem();
		if(blockLoad == null) {
			displayErrorMessage("Please select a block in the list");
			return;
		}
		model.updateBlockLoadStatus(view.blockListPane.getSelectedItem(),BlockLoadStatus.REMOVE);
		getLogger().info("Removed the block " + blockLoad);
		view.refreshMCListPane();
	}

	private void reloadBlock() {
		BlockLoad blockLoad = view.blockListPane.getSelectedItem();
		if(blockLoad == null) {
			displayErrorMessage("Please select a block in the list");
			return;
		}
		if(blockLoad.getStatus() != BlockLoadStatus.REMOVE) {
			displayErrorMessage("Cannot reload block , block is not in REMOVE status");
			return;
		}
		model.updateBlockLoadStatus(view.blockListPane.getSelectedItem(),BlockLoadStatus.RELOAD);
		getLogger().info("Reloaded the block " + blockLoad);
		view.refreshMCListPane();
	}

	private void remakeBlock() {
		
		audioManager.playNGSound();
		
		BlockLoad blockLoad = view.blockListPane.getSelectedItem();
		if(blockLoad == null) {
			displayErrorMessage("Please select a block in the list");
			return;
		}
		if(blockLoad.getStatus() != BlockLoadStatus.REMOVE) {
			displayErrorMessage("Cannot remake block , block is not in REMOVE status");
			return;
		}
		int option = JOptionPane.showConfirmDialog(view.getMainWindow(), 
				"To REMAKE the block, the current production lot information and \n" +
				"current block information will be lost.  Do you wish to continue?",
				"Remake Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ;
		if (option != 0) return;
		
		model.setRemakeBlockLoad(blockLoad);
		getLogger().info("Start to remake the block " + blockLoad);
		
		view.loadProductionLotData();
		view.resetMCDataFields();
		view.resetTorqueDataFields();
		currentState = State.WaitingForMc;
		disableTorqueDevice();
		
	}
	
	private boolean processMCNumber() {
		String mcNumber = StringUtils.trim(view.blockMCTextField.getComponent().getText());
		if(StringUtils.isEmpty(mcNumber))return false;
		
		try{
			currentBlock = model.validateMCNumber(mcNumber);
			if(currentBlock != null) {
				TextFieldState.READ_ONLY.setState(view.blockMCTextField.getComponent());
				model.createCurrentBlockLoad(currentBlock);
				String boreMeasurement = model.getBlockBoreMeasurement();
				displayBoreMeasurementData(boreMeasurement);
				List<BlockBuildResult> crankJournals = model.getCrankJournal();
				displayCrankJournals(crankJournals);
			}
			
		}catch(Exception ex) {
			setErrorMessage(ex.getMessage());
			TextFieldState.ERROR.setState(view.blockMCTextField.getComponent());
			view.blockMCTextField.getComponent().selectAll();
			return false;
		}
		return true;
	}
	
	public void displayBoreMeasurementData(String boreMeasurement) {
		String data = StringUtils.trim(boreMeasurement);
		for(int i=0;i<view.boreMeasureTextFields.size();i++) {
			JTextField bean = view.boreMeasureTextFields.get(i);
			String text = i < data.length() ? data.substring(i, i+1) : "";
			bean.setText(text);
		}
	}

	public void displayCrankJournals(List<BlockBuildResult> crankJournals) {
		for(int i=0;i<view.crankJournalTextFields.size();i++) {
			JTextField bean = view.crankJournalTextFields.get(i);
			bean.setText(model.getCrankJournal(crankJournals, crankJournals.size() - i -1));
		}
	}
	
	private List<IDeviceData> getDeviceInputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new MCNumber());
		list.add(new CycleComplete());
		list.add(new ResetFlag());
		return list;
	}
	
	private List<IDeviceData> getDeviceOutputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new DataCollectionComplete());
		return list;
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		clearMessage();
		
		try{
			if(deviceData instanceof MCNumber) 
				return mcNumberReceived(((MCNumber)deviceData).getMcNumber());
			else if(deviceData instanceof CycleComplete) 
				return cycleCompleteReceived();
			else if(deviceData instanceof ResetFlag)
				return resetReceived();
		}catch(Exception ex) {
			displayErrorMessage("Exception occurred : " + ex.getMessage());
		}
		return DataCollectionComplete.NG();
	
	}
	
	private IDeviceData cycleCompleteReceived() {
		getLogger().info("Received Cycle Complete");
		if(currentState != State.WaitingForCycleComplete){
			setErrorMessage("Received Cycle Complete when current state is " + currentState);
			return DataCollectionComplete.NG();
		};
		
		try{
			model.saveBlockData();
			model.invokeTracking();
			getLogger().info("Saved block data successfully");
			reset();
			return DataCollectionComplete.OK();
		}catch (Exception ex) {
			setErrorMessage("failed to save block data: " + ex.getMessage());
		}
		return DataCollectionComplete.NG();
	}

	private void mcNumberInputed() {
		getLogger().info("Received MC/DC number : " + view.blockMCTextField.getComponent().getText());
		basicProcessMCNumber();
	}
	
	private IDeviceData mcNumberReceived(String productId) {
		getLogger().info("Received MC/DC number : " + productId);
		if(currentState != State.WaitingForMc) reset();
		view.blockMCTextField.getComponent().setText(StringUtils.trim(productId));
		boolean isOK = basicProcessMCNumber();
		return new DataCollectionComplete(isOK);
	}
	
	private boolean basicProcessMCNumber() {
		boolean isOK = processMCNumber();
		if(!isOK) return false;
		String jobId = getInstructionCode();
		if(torqueDevice != null && !StringUtils.isEmpty(jobId)) {
			setJobTorqueDevice(currentBlock.getMcSerialNumber(),jobId);
			currentState = State.WaitingForTorque;
			model.getTorqueResults().clear();
			model.setTorqueAttemptCount(0);
			model.setTorqueIndex(0);
			view.getMainWindow().setMessage("waiting for torque data");
		}else {
			// send data collection complete to PLC to allow cycle complete
			sendDataCollectionComplete(true);
			currentState = State.WaitingForCycleComplete;
			view.getMainWindow().setMessage("waiting for cycle complete");
		}
		
		if (!model.NOTIFY_STARTER_CHANGE) checkBlockModelTypeChange();
		
		view.updateButtonStatus();
		
		return true;
	}
	
	private void checkBlockModelTypeChange() {
		if(model.getLastBlock() == null || model.getCurrentBlock() ==null) return;
		if(StringUtils.equalsIgnoreCase(model.getLastBlock().getModelCode(),model.getCurrentBlock().getModelCode())) return;
		
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						String msg = "<html><font face='Calibri' size='14' color='red'> " +
								"Block Model Code Changed - \n" 
								+ " Current Model Code : " + model.getCurrentBlock().getModelCode() + "\n" 
								+ " Previous Model Code : " + model.getLastBlock().getModelCode()
						        + "</html>";
						MessageDialog.showInfo(view.getMainWindow(), msg);
				}});
	}
	
	private String getInstructionCode() {
		String ymto = view.ymtoTextField.getComponent().getText();
		LotControlRule rule = model.getRule(ymto); 
		if(rule == null) {
	//		setErrorMessage("No lot control rule defined for ymto: " + ymto);
			return null;
		}
		
		return rule.getInstructionCode();
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	private void displayErrorMessage(String message) {
		view.getMainWindow().setErrorMessage(message);
	}
	
	private void setErrorMessage(String message) {
		view.getMainWindow().setErrorMessage(message);
		getLogger().error(message);
	}
	
	private void clearMessage() {
		view.getMainWindow().clearMessage();
	}
	
	private Logger getLogger() {
		return view.getLogger();
	}

	public String getId() {
		return null;
	}

	public void handleStatusChange(String deviceId,TorqueDeviceStatusInfo statusInfo) {
		getLogger().info("Received " + statusInfo.getClass().getSimpleName() + ":" + statusInfo.getMessage());
		if(statusInfo.getMessageSeverity() == DeviceMessageSeverity.error)
			setErrorMessage("Torque controller : " + statusInfo.getMessage());
		else view.getMainWindow().setMessage("Torque controller : " + statusInfo.getMessage());
	}

	public void processLastTighteningResult(String deviceId,LastTighteningResult result) {
		getLogger().info("Received torque data : " + CommonUtil.toString(result));
		if(currentState != State.WaitingForTorque && currentState != State.WaitingForCycleComplete) {
			sendDataCollectionComplete(false);
			setErrorMessage("Received torque data when current state is " + currentState);
			return;
		}
		
		LotControlRule rule = model.getRule(model.getCurrentBlockLoad().getProductSpecCode());
		
		if(rule == null) {
			setErrorMessage("Cannot process torque result - no lot control rule configured for engine spec " + model.getCurrentBlockLoad().getProductSpecCode());
			return;
		}

		List<MeasurementSpec> measurementSpecs = rule.getParts().get(0).getMeasurementSpecs();
		MeasurementSpec spec = measurementSpecs.get(model.getTorqueIndex());

		String message ="";		
	
		List<LastTighteningResult> torqueResults = model.getTorqueResults();
	
		if(model.getTorqueAttemptCount() < spec.getMaxAttempts()) {
		
			int torqueIndex = torqueResults.size();
			
			if(model.getTorqueResults().size() == model.getTorqueIndex()) torqueResults.add(result);
			else torqueResults.set(torqueIndex - 1,result);
			
			if (result.getTorque() < spec.getMinimumLimit() || result.getTorque() > spec.getMaximumLimit()) {
				result.setTorqueStatus(0);
				message = " - value " + result.getTorque() + " is not within the limits [" + spec.getMinimumLimit() + "," + spec.getMaximumLimit() +"]";
			}
			
			if(!model.isTorqueResultOk(result)) model.setTorqueAttemptCount(model.getTorqueAttemptCount() + 1);
			else {
				model.setTorqueAttemptCount(0); 
				model.setTorqueIndex(model.getTorqueIndex() +1);
			}
		
			for(int i=0; i<torqueResults.size(); i++) {
	
				LastTighteningResult torqueResult = torqueResults.get(i);
				boolean isTorqueOk = model.isTorqueResultOk(torqueResult);
	
				view.torqueTextFields.get(i).setText(Double.toString(torqueResult.getTorque()));
				view.torqueTextFields.get(i).setBackground(isTorqueOk ? Color.green : Color.red);
			}
        }
   
	   if(model.getTorqueAttemptCount() >= spec.getMaxAttempts()) {
		   	disableTorqueDevice();
		   	message = "Reached Maximum Torque Attempt Count: "  + model.getTorqueAttemptCount(); 
	   }
		
		if(torqueResults.size() == measurementSpecs.size() && model.isTorqueResultOk(result)){
		
			sendDataCollectionComplete(true);
			currentState = State.WaitingForCycleComplete;
			view.updateButtonStatus();
			view.getMainWindow().setMessage("waiting for cycle complete");
		
		}else {
			currentState = State.WaitingForTorque;
			if(message.length() == 0) view.getMainWindow().setMessage("waiting for Torque");
			else view.getMainWindow().setErrorMessage("waiting for Torque - " + message);
		}
        
		view.blockMCTextField.getComponent().setEditable(false);
		view.resetButton.setEnabled(true);
		
	}
	
	private void sendDataCollectionComplete(boolean flag) {
		try {
			DeviceManager.getInstance().getEiDevice().syncSend(new DataCollectionComplete(flag));
			getLogger().info("send data collection complete flag " + flag+ " successfully");
		}catch (Exception ex) {
			setErrorMessage("Could not send data collection complete to PLC due to " + ex.getMessage() );
			return;
		}
	}

	public void processMultiSpindleResult(String deviceId,MultiSpindleResultUpload multiSpindleResult) {
	}

	public void controlGranted(String deviceId) {
	}

	public void controlRevoked(String deviceId) {
	}
	
	@Override
	public void handleCommandAccepted(String deviceId, CommandAccepted commandAccepted) {
	}

	public String getApplicationName() {
		return null;
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return null;
	}

	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) return;
		view.updateButtonStatus();
	}
}
