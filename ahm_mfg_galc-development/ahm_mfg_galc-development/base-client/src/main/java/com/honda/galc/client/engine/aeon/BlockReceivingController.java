package com.honda.galc.client.engine.aeon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.MCNumber;
import com.honda.galc.device.dataformat.ResetCommand;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>BlockReceivingController Class description</h3>
 * <p> BlockReceivingController description </p>
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
 * Jan 13, 2016
 *
 *
 */
public class BlockReceivingController implements ActionListener, KeyListener, FocusListener, DeviceListener {
	
	private BlockReceivingModel model;
	private BlockReceivingView view;
	private boolean mcNumberEntered = false;
	
	private final ClientAudioManager audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));

	public BlockReceivingController(BlockReceivingModel model, BlockReceivingView view) {
		this.model = model;
		this.view = view;
		registerEiDeviceListener();
	}
	
	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			eiDevice.registerDeviceListener(this, getDeviceInputDataList());
			eiDevice.reqisterDeviceData(getDeviceOutputDataList());
		}
	}
	
	private List<IDeviceData> getDeviceInputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new MCNumber());
		return list;
	}
	
	private List<IDeviceData> getDeviceOutputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new DataCollectionComplete());
		list.add(new ResetCommand());
		return list;
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(view.blockMCTextField.getComponent())) mcNumberInputed();
		else if(e.getSource().equals(view.okButton)) okClicked();
		else if(e.getSource().equals(view.resetButton)) resetClicked();
	}
	
	private IDeviceData mcNumberReceived(String productId) {
		getLogger().info("Received AEP MC number from Device: " + productId);
		view.blockMCTextField.getComponent().setText(StringUtils.trim(productId));
		boolean isOK = basicProcessMCNumber();
		return new DataCollectionComplete(isOK);
	}
	
	private void mcNumberInputed() {
		getLogger().info("Received AEP MC number from Scan : " + view.blockMCTextField.getComponent().getText());
		mcNumberEntered = true;
		basicProcessMCNumber();
	}
	
	
	private boolean basicProcessMCNumber() {
		clearMessage();
		String mcNumber = StringUtils.trim(view.blockMCTextField.getComponent().getText());
		try{
			model.validateBlock(mcNumber);
		}catch(Exception ex) {
			
			view.getMainWindow().setErrorMessage("invalid AEP MC Number: " + mcNumber);
			TextFieldState.ERROR.setState(view.blockMCTextField.getComponent());
			view.blockMCTextField.getComponent().selectAll();
			
			audioManager.playNGSound();
			
			return false;
		}
		
		audioManager.playOKSound();
		
		TextFieldState.READ_ONLY.setState(view.blockMCTextField.getComponent());
		
		String boreMeasurement = model.getBlockBoreMeasurement();
		displayBoreMeasurementData(boreMeasurement);
		List<BlockBuildResult> crankJournals = model.getCrankJournal();
		displayCrankJournals(crankJournals);
		view.boreTextFields.get(0).requestFocusInWindow();
		
		return true;
	}
	
	private void okClicked() {
		if(checkBoreMeasurementValues() && checkCrankJournalValues()) {
			try{
				String boreMeasurementValue = getBoreMeasurementValue();
				List<String> crankJournalValues = new ArrayList<String>(); 
				String crankStr= getCrankJournals(crankJournalValues);
				model.saveData(getBoreMeasurementValue(),crankJournalValues);
				String message = "block data saved successfully - MC number: " + 
					view.blockMCTextField.getComponent().getText() + " Bore: " + boreMeasurementValue + " Crank Journal: " + crankStr;
				getLogger().info(message);
				view.getMainWindow().setMessage(message);
				resetData();
			}catch (Exception ex) {
				setErrorMessage("failed to save block data: " + ex.getMessage());
				
				audioManager.playNGSound();
				
				return;
			}
		}
		
		boolean isOk = sendDataCollectionComplete(true);
		
		if(isOk) audioManager.playOKSound();
		else audioManager.playNGSound();
		
		
		
	}
	
	private void resetClicked() {
		resetData();	
		sendResetCommand();
		
	}
	
	private void resetData() {
		for(JTextField bean : view.boreTextFields) {
			bean.setText("");
			TextFieldState.EDIT.setState(bean);
		}
		for(JTextField bean : view.crankTextFields) {
			bean.setText("");
			TextFieldState.EDIT.setState(bean);
		}
	    model.resetData();
		view.blockMCTextField.getComponent().setText("");
		TextFieldState.EDIT.setState(view.blockMCTextField.getComponent());
		view.blockMCTextField.getComponent().requestFocusInWindow();
	}
	
	private void boreMeasurementValueReceived(JTextField bean) {
		if(model.checkBoreMeasureMent(bean.getText())) {
			TextFieldState.EDIT.setState(bean);
			view.setFocusForNextValue(bean);
		}else {
			TextFieldState.ERROR.setState(bean);
			bean.selectAll();
			view.getMainWindow().setErrorMessage("invalid value: " + bean.getText());
		}
	}

	private void crankJournalValueReceived(JTextField bean) {
		if(model.checkCrankJournal(bean.getText())){
			TextFieldState.EDIT.setState(bean);
			view.setFocusForNextValue(bean);
		}else {
			TextFieldState.ERROR.setState(bean);
			bean.selectAll();
			view.getMainWindow().setErrorMessage("invalid value: " + bean.getText());
		}
	}
	
	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		view.getMainWindow().clearMessage();
		if(view.boreTextFields.contains(e.getSource())){
			if(mcNumberEntered) {
				mcNumberEntered = false;
			}else boreMeasurementValueReceived((JTextField)e.getSource());
		}else if(view.crankTextFields.contains(e.getSource()))
			crankJournalValueReceived((JTextField)e.getSource());
		else if(e.getKeyChar() == KeyEvent.VK_ENTER && view.okButton.equals(e.getSource())){
			view.okButton.doClick();
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void focusGained(FocusEvent e) {
		if(view.boreTextFields.contains(e.getSource()) || view.crankTextFields.contains(e.getSource())) {
			JTextField bean = ((JTextField)e.getSource());
			TextFieldState.EDIT.setState(bean);
			bean.selectAll(); 
		}
	}

	public void focusLost(FocusEvent e) {
		if(view.boreTextFields.contains(e.getSource())){
			JTextField bean = ((JTextField)e.getSource());
			setState(bean,model.checkBoreMeasureMent(bean.getText()));
		}else if(view.crankTextFields.contains(e.getSource())){
			JTextField bean = ((JTextField)e.getSource());
			setState(bean,model.checkCrankJournal(bean.getText()));
		}
	}
	
	private boolean checkBoreMeasurementValues() {
		for(JTextField bean : view.boreTextFields){
			if(!model.checkBoreMeasureMent(bean.getText())){
				setErrorStatus(bean);
				return false;
			}
		}
		
		return true;
	}
	
	public void displayBoreMeasurementData(String boreMeasurement) {
		String data = StringUtils.trim(boreMeasurement);
		for(int i=0;i<view.boreTextFields.size();i++) {
			JTextField bean = view.boreTextFields.get(i);
			String text = i < data.length() ? data.substring(i, i+1) : "";
			bean.setText(text);
			setState(bean,model.checkBoreMeasureMent(bean.getText()));
		}
	}

	public void displayCrankJournals(List<BlockBuildResult> crankJournals) {
		for(int i=0;i<view.crankTextFields.size();i++) {
			JTextField bean = view.crankTextFields.get(i);
			bean.setText(model.getCrankJournal(crankJournals, crankJournals.size() - i -1));
			setState(bean,model.checkCrankJournal(bean.getText()));
		}
	}
	
	private void setState(JTextField bean,boolean isValid) {
		if(!StringUtils.isEmpty(bean.getText()) && !isValid) {
			TextFieldState.ERROR.setState(bean);
		}else if(StringUtils.isEmpty(bean.getText())) {
			TextFieldState.EDIT.setState(bean);
		}else {
			TextFieldState.READ_ONLY.setState(bean);
			bean.setEnabled(true);
			bean.setRequestFocusEnabled(true);
		}
	}
	
	private boolean checkCrankJournalValues() {
		for(JTextField bean : view.crankTextFields){
			if(!model.checkCrankJournal(bean.getText())){
				setErrorStatus(bean);
				return false;
			}
		}
		
		return true;
	}
	
	private void setErrorStatus(JTextField bean) {
		TextFieldState.ERROR.setState(bean);
		bean.selectAll();
		bean.requestFocusInWindow();
		view.getMainWindow().setErrorMessage("invalid value: " + bean.getText());
		
	}
	
	private String getBoreMeasurementValue() {
		String result = "";
		for(JTextField bean : view.boreTextFields){
			result +=StringUtils.trim(bean.getText());
		}
		return result;
	}
	
	private String getCrankJournals(List<String> results){
		String crankJournal = "";
		for(int i= view.crankTextFields.size() - 1; i>=0;i--){
			JTextField bean = view.crankTextFields.get(i);
			results.add(StringUtils.trim(bean.getText()));
			crankJournal +=StringUtils.trim(bean.getText());
		}
		return crankJournal;
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
	
	private boolean sendDataCollectionComplete(boolean flag) {
		try {
			EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
			if(eiDevice != null && eiDevice.isEnabled()){
				eiDevice.syncSend(new DataCollectionComplete(flag));
				getLogger().info("send data collection complete flag " + flag+ " successfully");
			}
		}catch (Exception ex) {
			setErrorMessage("Could not send data collection complete to PLC due to " + ex.getMessage() );
			return false;
		}
		return true;
	}
	
	private void sendResetCommand() {
		ResetCommand resetFlag = new ResetCommand();
		resetFlag.setResetFlag("1");
		try {
			EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
			if(eiDevice != null && eiDevice.isEnabled()){
				eiDevice.syncSend(resetFlag);
				getLogger().info("send reset commnad successfully");
			}
		}catch (Exception ex) {
			setErrorMessage("Could not reset commnad to PLC due to " + ex.getMessage() );
			return;
		}
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		clearMessage();
		
		try{
			if(deviceData instanceof MCNumber) 
				return mcNumberReceived(((MCNumber)deviceData).getMcNumber());
			
		}catch(Exception ex) {
			setErrorMessage("Exception occurred : " + ex.getMessage());
		}
		return DataCollectionComplete.NG();
	}
}
