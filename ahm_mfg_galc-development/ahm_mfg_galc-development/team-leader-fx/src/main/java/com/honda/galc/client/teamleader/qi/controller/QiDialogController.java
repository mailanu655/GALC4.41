package com.honda.galc.client.teamleader.qi.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.client.teamleader.qi.view.DataValidationDialog;
import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationDialogController</code> is the parent class for QI Dialog.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public abstract class QiDialogController<M extends QiModel, D extends QiFxDialog> implements EventHandler<ActionEvent>{

	private M model;
	private D dialog;
	boolean clearTxtField=false;
	boolean specialCharExists=false;

	public D getDialog() {
		return dialog;
	}

	public void setDialog(D dialog) {
		this.dialog = dialog;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		getModel().setApplicationContext(applicationContext);
	}

	public Logger getLogger() {
		return getModel().getLogger();
	}
	
	public void close() {
	}

	public String getUserId(){
		return getModel().getUserId().toUpperCase();
	}
	/**
	 * This method is used to handle exceptions.
	 * @param loggerMsg
	 * @param errMsg
	 * @param parentScreen
	 * @param e
	 */
	public void handleException(String loggerMsg, String errMsg, Exception e) {
		getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
	}
	/**
	 * This method is used to clear the exception display message.
	 */
	public void clearDisplayMessage() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
	}
	/**
	 * This method is used to publish error message.
	 */
	public void publishErrorMessage(String errorMsg) {
		EventBusUtil.publish(new StatusMessageEvent(errorMsg, StatusMessageEventType.DIALOG_ERROR));
	}
	public abstract void handle(ActionEvent arg0);
	public abstract void initListeners();

	public void displayErrorMessage(String loggerMsg, String errMsg)
	{
		getLogger().error(loggerMsg);
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
	}
	
	protected boolean isUpdated(AuditEntry entity) {
		if (getModel().isUpdated(entity)) {
			String logMsg = entity.getClass().getSimpleName() + "(" + entity.getId() + ")-" + QiConstant.CONCURRENT_UPDATE_MSG_TMPL;
			String userMsg = QiConstant.CONCURRENT_UPDATE_MSG;
			displayErrorMessage(logMsg, userMsg);
			return true;
		}
		return false;
	}
	
	protected boolean isUpdated(List<? extends AuditEntry> entities) {
		if (getModel().isUpdated(entities)) {
			displayErrorMessage(QiConstant.CONCURRENT_UPDATE_MSG, QiConstant.CONCURRENT_UPDATE_MSG);
			return true;
		}
		return false;
	}

	/**
	 * set textfield listeners
	 */
	public void setTextFieldListener(UpperCaseFieldBean textField) {
		textField.focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				clearDisplayMessage();
			}
		});
	}
	/**
	 * set radiobutton listeners
	 */
	public void setRadioButtonListener(LoggedRadioButton radioButton){
		radioButton.focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				clearDisplayMessage();
			}
		});
	}

	/**
	 * This method is used for Text Field Listener
	 */
	public void addFieldListener(final UpperCaseFieldBean TextField, final boolean isTextValueString, final boolean isSpecialChaReq){
		TextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if(clearTxtField) {
					clearTxtField = false;
					return;
				}
					if(isTextValueString){
						if(isSpecialChaReq && hasSpecialCharacters(TextField)){
							clearTxtField = true;
							specialCharExists = true;
							TextField.settext(oldValue);
						} else {
							clearDisplayMessage();
							specialCharExists = false;
						}
					}else{
						if(QiCommonUtil.isNumberTextMaximum(0,TextField)){
							displayErrorMessage("Value cannot be greater than 32767","Value cannot be greater than 32767");
							TextField.clear();
							TextField.setText(String.valueOf(0));
						}
					}
				enableUpdateButton();
			}
		});
	}
	
	public void addFieldListener(final UpperCaseFieldBean textField, final boolean isTextValueString) {
		addFieldListener(textField,isTextValueString,true);
	}
	
	/**
	 * Adds the numeric check listener.
	 *
	 * @param textField the text field
	 */
	public void addNumericCheckListener(final UpperCaseFieldBean textField){
		textField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(QiCommonUtil.isNumberTextMaximum(0L,textField)){
					displayErrorMessage("Value cannot be greater than 2,147,483,647 ","Value cannot be greater than 2,147,483,647 ");
					textField.clear();
					textField.setText(String.valueOf(0));
				}
			}
		});
	}

	/**
	 * This method is used to check the special character in  TextBox
	 * @param textField
	 * @return
	 */
	public boolean hasSpecialCharacters(final Object textField){
		if(QiCommonUtil.hasSpecialCharacters(textField)){
			displayErrorMessage("Special character not allowed","Special character not allowed");
			return true;
		}
		return false;
	}

	/**
	 * This method is used for Text Field Listener
	 */
	public void addTextFieldCommonListener(final UpperCaseFieldBean textField, final boolean isTextValueString){
		textField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(isTextValueString){
					if(QiCommonUtil.hasSpecialCharacters(textField)){
						textField.clear();
						textField.settext(oldValue);
					}
				}else{
					if (!newValue.matches("\\d+")) {
						textField.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
				
				getDialog().getUpdateButton().setDisable(true);
			}
		});
	}

	/**
	 * This method is used for Text Area Listener
	 */
	public void addTextAreaListener(final LoggedTextArea textArea){
		textArea.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				textArea.setText(textArea.getText().toUpperCase());
				if(hasSpecialCharacters(textArea)){
					textArea.clear();
					textArea.setText(oldValue);
				}
				
				enableUpdateButton();
				
			}
		});
	}

	/**
	 * set combobox listeners
	 */
	public void setComboBoxListener(LoggedComboBox<?> loggedComboBox){
		loggedComboBox.focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				clearDisplayMessage();
			}
		});
	}
	
	/**
	 * Displays local site validation and returns true if any site is impacted
	 */
	public String isLocalSiteImpacted(List validateIdList,String screenName,Stage parent) {
		Map<String,String> siteMap=PropertyService.getPropertyBean(QiPropertyBean.class).getSitesForValidation();
		if(siteMap==null || siteMap.isEmpty()){
			if(!(MessageDialog.confirm(parent,"Local Site Data will not be validated as local site(s) are not configured. Do you still want to continue?")))
				return QiConstant.NO_LOCAL_SITES_CONFIGURED;
			else
				return "";
		}
		DataValidationDialog dataValidationDialog=new DataValidationDialog("Local Validation",getDialog(),screenName,validateIdList);
		dataValidationDialog.showDialog();
		return dataValidationDialog.isLocalSiteImpact()?QiConstant.LOCAL_SITES_IMPACTED:"";
		
	}

	protected ChangeListener<String> updateEnablerForStringValueChange = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			enableUpdateButton();
		}
	};

	protected ChangeListener<KeyValue<Integer, String>> updateEnablerForKeyValueChange = new ChangeListener<KeyValue<Integer, String>>() {
		public void changed(ObservableValue<? extends KeyValue<Integer, String>> observable, KeyValue<Integer, String> oldValue, KeyValue<Integer, String> newValue) {
			enableUpdateButton();
		}
	};
	
	protected ChangeListener<Toggle> updateEnablerForToggle = new ChangeListener<Toggle>() {
		public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
			enableUpdateButton();
		}
	};
	
	protected ChangeListener<Boolean> updateEnablerForBoolean= new ChangeListener<Boolean>() {
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
	    	enableUpdateButton();
	    }
	};
	
	protected ChangeListener<BigDecimal> updateEnablerForBigDecimalValueChange = new ChangeListener<BigDecimal>() {
		public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue, BigDecimal newValue) {
			if(newValue!=null)
			    Logger.getLogger().check(newValue.toString()+" is selected");
			enableUpdateButton();
		}
	};
	
	
	/**
	 * This method is used for Text Field Listener
	 */
	protected void addTextChangeListener(final UpperCaseFieldBean TextField){
		TextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				enableUpdateButton();
				
			}
		});
	}

	/**
	 * This method is used for Text Field Listener
	 * 
	 */
	protected void addTextAreaChangeListener(final LoggedTextArea textArea){
		textArea.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				enableUpdateButton();
			}
		});
	}

	/** This method will add listener to enable update button on change event on Table.
	 * 
	 * @param objectTablePane
	 */
	protected <T> void addUpdateEnablerForTableChangeListener(ObjectTablePane<T> table) {
		table.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
			public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
				enableUpdateButton();
			}
		});
	}

	/** This method will enable update button in update dialog.
	 * 
	 */
	protected void enableUpdateButton() {
		if(getDialog().getTitle().contains(QiConstant.UPDATE)){
			getDialog().getUpdateButton().setDisable(false);
		}
		else if(getDialog().getTitle().equals("Defect Tagging Updation")){
			getDialog().getUpdateButton().setDisable(false);
		}
	}
	
	/**
	 * This method is used for Text Field Listener
	 */
	public void addLimitFieldListener(final UpperCaseFieldBean TextField, final int fieldLimit){
		TextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				if(!TextField.getText().equals("")){
					if(clearTxtField) {
						clearTxtField = false;
						return;
					}
					if (!newValue.matches("\\d+")) { // if the value is not empty and contains non-digit characters
						newValue= newValue.replaceAll("[^0-9]",""); // remove non-numeric characters
						TextField.setText(newValue); // recursively call this method with the replacement value
						return;
					}
					int newIntValue = StringUtils.isEmpty(newValue) ? 0 : Integer.parseInt(newValue);
					if (newIntValue > fieldLimit) {
						displayErrorMessage("Value cannot be greater than "+fieldLimit,"Value cannot be greater than "+fieldLimit);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								TextField.clear();
							}
						});
					}
					else
					{
						clearDisplayMessage();
					}
				
				}
			}
		});
	}
	
}
