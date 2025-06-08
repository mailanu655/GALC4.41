package com.honda.galc.client.datacollection.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.ui.ErrorMessageDialog;
import com.honda.galc.client.ui.ErrorMessageDialog.ButtonOptions;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.service.ServiceFactory;

public class ErrorDialogManager implements KeyListener, ActionListener {

	private ErrorMessageDialog errorDialog;
	private String requestName = null;
	private Map<String, String> options;
	private String errorType;
	private TerminalPropertyBean terminalProperty;
	private String processPointId;
	
	public ErrorDialogManager() {	
	}
	
	public ErrorDialogManager(String processPointId) {
		this.processPointId = processPointId;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		handleEvent(button.getText());
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Boolean handleScan = false;
			String text = ((JTextField) e.getSource()).getText();
			Logger.getLogger().info("Error Dialog received scan - "+text );
			if(errorDialog.getButtonOptions().equals(ButtonOptions.REFRESH_ONLY)){
				if(errorDialog.getRefreshLabel().equalsIgnoreCase(text)){
					handleScan = true;
				}
			}else if(errorDialog.getButtonOptions().equals(ButtonOptions.REFRESH_SKIP)){
				if((errorDialog.getRefreshLabel().equalsIgnoreCase(text))||(errorDialog.getSkipLabel().equalsIgnoreCase(text))){
					handleScan = true;
				}
			}else if(errorDialog.getButtonOptions().equals(ButtonOptions.YES_NO)){
				if((errorDialog.getYesLabel().equalsIgnoreCase(text))||(errorDialog.getNoLabel().equalsIgnoreCase(text))){
					handleScan = true;
				}
			}else if(errorDialog.getButtonOptions().equals(ButtonOptions.YES_NO_REFRESH)){
				if((errorDialog.getYesLabel().equalsIgnoreCase(text))||(errorDialog.getNoLabel().equalsIgnoreCase(text)) || (errorDialog.getRefreshLabel().equalsIgnoreCase(text))){
					handleScan = true;
				}
			}else if(errorDialog.getButtonOptions().equals(ButtonOptions.CANCEL_SKIP_REFRESH)){
				if((errorDialog.getRefreshLabel().equalsIgnoreCase(text))||(errorDialog.getSkipLabel().equalsIgnoreCase(text))||(errorDialog.getCancelLabel().equalsIgnoreCase(text))){
					handleScan = true;
				}
			}
		
			if(handleScan){
				handleEvent(text);
			}else{
				Logger.getLogger().info("Error Dialog received unexpected scan - "+text );
				errorDialog.getScanTextBox().setText("");
				errorDialog.getScanTextBox().requestFocus();
				playNGSound();
			}
		}
	}

	public String showDialog(JFrame frame, String message, String errorType,
			TerminalPropertyBean property) {
		setRequestName("");
		setProperty(property);
		String backGroundColor, foreGroundColor, errorCode = "DPS";
		boolean allowScan = property.isShowScanOnlyErrorDialog();
		ButtonOptions buttonList = ButtonOptions.REFRESH_ONLY;
		this.errorType = errorType;
		String title = "Error";
		HashMap<String, String> defaultOptions = new HashMap<String, String>();
		defaultOptions.put(ErrorMessageDialog.defaultRefreshLabel, ErrorMessageDialog.defaultRefreshLabel);
		defaultOptions.put(ErrorMessageDialog.defaultSkipLabel, ErrorMessageDialog.defaultSkipLabel);
		defaultOptions.put(ErrorMessageDialog.defaultCancelLabel, ErrorMessageDialog.defaultCancelLabel);
		defaultOptions.put(ErrorMessageDialog.defaultYesLabel, ErrorMessageDialog.defaultYesLabel);
		defaultOptions.put(ErrorMessageDialog.defaultNoLabel, ErrorMessageDialog.defaultNoLabel);
		
		if (errorType.equals(LotControlConstants.UNEXPECTED_PRODUCT_SCAN)) {
			backGroundColor = property.getDifferentProductBackColor();
			foreGroundColor = property.getDifferentProductForeColor();
			options = property.getDifferentProductButton()!=null?property.getDifferentProductButton():defaultOptions;
			title = "Different Product";
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultRefreshLabel))+" and confirm the "+property.getProductIdLabel();
		} else if (errorType.equals(LotControlConstants.MSG_MAX_MEASUREMENT_ATTEMPTS_EXCEEDED)) {
			message = "Max Number of Attempts Exceeded. \n ";
			options = property.getMaxAttemptsButton()!=null?property.getMaxAttemptsButton():defaultOptions;

			if(property.isScanlessEnabled() && isInAuto() && property.isPopupRemoveRepairScanlessEnabled()) {
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultSkipLabel))+" to skip the current part/measurement";
				buttonList = ButtonOptions.SKIP_ONLY;
			} else {
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultRefreshLabel))+" to skip the current "+property.getProductIdLabel();
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultSkipLabel))+" to skip the current part/measurement";
				buttonList = ButtonOptions.REFRESH_SKIP;
			}
			backGroundColor = property.getMaxAttemptsBackColor();
			foreGroundColor = property.getMaxAttemptsForeColor();
			errorCode = "MA";
			title = "Max Attempts";
		} else if (errorType.equals(LotControlConstants.OUT_OF_SEQUENCE)) {
			errorCode = "OSS";
			HashMap<String, String> defaultOSSOptions = new HashMap<String, String>();
			defaultOSSOptions.put(ErrorMessageDialog.defaultYesLabel, ErrorMessageDialog.defaultYesLabel);
			defaultOSSOptions.put(ErrorMessageDialog.defaultNoLabel, ErrorMessageDialog.defaultNoLabel);
			options = property.getOutOfSeqButton()!=null?property.getOutOfSeqButton():defaultOSSOptions;
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultYesLabel))+" to complete data collection out of order.";
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultNoLabel))+" to scan another "+property.getProductIdLabel();
			backGroundColor = property.getOutOfSequenceBackColor();
			foreGroundColor = property.getOutOfSequenceForeColor();
			buttonList = ButtonOptions.YES_NO;
			title = "Out of Sequence Scan";
		} else if (errorType.equals(LotControlConstants.FAILED_PRODUCT_CHECKS)) {
			options = property.getProductCheckButton()!=null?property.getProductCheckButton():defaultOptions;
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultCancelLabel))+" to assign another part ";
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultSkipLabel))+" to skip assigning a part";
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultRefreshLabel))+" to skip the current VIN ";
			
			backGroundColor = property.getProductCheckBackColor();
			foreGroundColor = property.getProductCheckForeColor();
			errorCode = "PC";
			buttonList = ButtonOptions.CANCEL_SKIP_REFRESH;
			title = "Failed Product Checks";

		}else if(errorType.equals(LotControlConstants.TEST_TORQUE)){
			backGroundColor = property.getTestTorqueBackColor();
			foreGroundColor = property.getTestTorqueForeColor();
			errorCode = "TT";
			buttonList = ButtonOptions.REFRESH_ONLY;
			title = "Pre Shift Tool Test";
			options = property.getTestTorqueButton()!=null?property.getTestTorqueButton():defaultOptions;
			message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultRefreshLabel))+" to close ";
		} else if (errorType.equals(LotControlConstants.FAILED_PRODUCT_CHECKS_ACK)) {
			options = property.getProductCheckButton()!=null?property.getProductCheckButton():defaultOptions;
			message = message + getMsg(allowScan, options.get(ErrorMessageDialog.defaultRefreshLabel)) + " to skip the current VIN ";
			
			backGroundColor = property.getProductCheckBackColor();
			foreGroundColor = property.getProductCheckForeColor();
			errorCode = "PCF";
			title = "Product Check Failed";
		} else if (errorType.equals(LotControlConstants.MBPN_ALREADY_ASSIGNED) || errorType.equals(LotControlConstants.DIFFERENT_MBPN_ASSIGNED)) {

			if (errorType.equals(LotControlConstants.MBPN_ALREADY_ASSIGNED)){
				backGroundColor = property.getAlreadyAssignedBackColor();
				foreGroundColor = property.getAlreadyAssignedForeColor();
				errorCode = "AA";
				title = "Already Assigned";
				options = property.getAlreadyAssignedButton()!=null?property.getAlreadyAssignedButton():defaultOptions;
			}else{
				backGroundColor = property.getDifferentMbpnAssignedBackColor();
				foreGroundColor = property.getDifferentMbpnAssignedForeColor();
				errorCode = "DPA";
				title = "Different MBPN Assigned";
				options = property.getDifferentMbpnAssignedButton()!=null?property.getDifferentMbpnAssignedButton():defaultOptions;
			}
			
			if(property.isAlreadyAssignedAllowReassign()){
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultYesLabel))+" to remove from current Vin and reassign ";
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultNoLabel))+" to assigning another part";
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultRefreshLabel))+" to skip the current VIN ";
				buttonList = ButtonOptions.YES_NO_REFRESH;
			}else{
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultCancelLabel))+" to assign another part ";
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultSkipLabel))+" to skip assigning a part";
				message = message+getMsg(allowScan,options.get(ErrorMessageDialog.defaultRefreshLabel))+" to skip the current VIN ";
				buttonList = ButtonOptions.CANCEL_SKIP_REFRESH;
			}
			
			
		}else {
			backGroundColor = property.getDefaultBackColor();
			foreGroundColor = property.getDefaultForeColor();
			options = new HashMap<String, String>();
			options.put(ErrorMessageDialog.defaultRefreshLabel, "OK");
		}
		errorDialog = new ErrorMessageDialog(frame, title, message, backGroundColor, foreGroundColor, errorCode,
				allowScan, buttonList, options);
		
		Logger.getLogger().info("Showing Error Dialog -"+ title +" with buttons- "+buttonList.toString() +" and allowScan set to- "+allowScan) ;
		
		errorDialog.setActionListener(this);
		errorDialog.setKeyListener(this);
		errorDialog.setVisible(true);
		
		Logger.getLogger().info("Error Dialog received request to-"+ getRequestName());
		
		return getRequestName();
	}

	private void setProperty(TerminalPropertyBean property) {
		this.terminalProperty = property;
	}
	
	private TerminalPropertyBean getProperty() {
		return this.terminalProperty;
	}
	
	private void handleEvent(String text) {
		String refreshLabel = "";String skipLabel = "";String yesLabel="";String noLabel="";
		if(options != null && options.size()>0){
			refreshLabel = options.containsKey(ErrorMessageDialog.defaultRefreshLabel)?options.get(ErrorMessageDialog.defaultRefreshLabel):ErrorMessageDialog.defaultRefreshLabel;
			skipLabel = options.containsKey(ErrorMessageDialog.defaultSkipLabel)?options.get(ErrorMessageDialog.defaultSkipLabel):ErrorMessageDialog.defaultSkipLabel;
			yesLabel = options.containsKey(ErrorMessageDialog.defaultYesLabel)?options.get(ErrorMessageDialog.defaultYesLabel):ErrorMessageDialog.defaultYesLabel;
			noLabel = options.containsKey(ErrorMessageDialog.defaultNoLabel)?options.get(ErrorMessageDialog.defaultNoLabel):ErrorMessageDialog.defaultNoLabel;
		}
		if(errorType.equalsIgnoreCase(LotControlConstants.OUT_OF_SEQUENCE)){
			if(text.equalsIgnoreCase(yesLabel)) setRequestName(Boolean.TRUE.toString());
			if(text.equalsIgnoreCase(noLabel)) setRequestName(Boolean.FALSE.toString());
		}else if (text.equalsIgnoreCase(refreshLabel)) {
			if(errorType.equals(LotControlConstants.TEST_TORQUE)) {
				setRequestName("cancel");
			}
			else if(!getProperty().isAllowSkipProduct() ) {
				Logger.getLogger().info("Skipping Product not allowed for Processpoint executing Refresh/Repair ");
				setRequestName("cancel");
			}
			else {	
				setRequestName("skipProduct");
					getState().skipProduct();
				}
		} else if (text.equalsIgnoreCase(skipLabel)) {
			if (getState() instanceof ProcessProduct) {
				setRequestName("skipProduct");
			} else if (getState() instanceof ProcessPart || getState() instanceof ProcessTorque) {
				setRequestName("skipPart");
			}

		}else if(errorType.equals(LotControlConstants.MBPN_ALREADY_ASSIGNED) || errorType.equals(LotControlConstants.DIFFERENT_MBPN_ASSIGNED)){
			if(text.equalsIgnoreCase(yesLabel)) setRequestName(yesLabel);
			if(text.equalsIgnoreCase(noLabel)) setRequestName(noLabel);
		}
		
		errorDialog.dispose();
	}

	private DataCollectionState getState() {
		return DataCollectionController.getInstance().getState();
	}

	private String getRequestName() {
		return requestName;
	}

	private void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	protected void playNGSound() {
		if(LotControlAudioManager.isExist())
			LotControlAudioManager.getInstance().playNGSound();
	}
	private String getMsg(boolean allowScan, String buttonLabel){
		String msg = "\n ** Click ";
		if(allowScan)msg = msg+"or scan ";
		msg = msg+buttonLabel;
		
		return msg;
	}

	private ComponentStatusDao getComponentStatusDao() {
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}

	private boolean isInAuto() {
		ComponentStatus componentStatus = getComponentStatusDao().findByKey(processPointId, "SCANLESS_OPERATION_MODE");
		String operationMode = componentStatus == null ? "N/A" : componentStatus.getStatusValue();
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(operationMode);
	}
	
}
