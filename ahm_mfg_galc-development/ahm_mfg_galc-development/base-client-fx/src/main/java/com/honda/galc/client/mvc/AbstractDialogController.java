package com.honda.galc.client.mvc;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>AbstractDialogController</code> is the parent class for Dialog Controller.
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
 * <TD>10/01/2017</TD>
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
public abstract class AbstractDialogController<M extends AbstractModel, D extends FxDialog> implements EventHandler<ActionEvent>{

	private M model;
	private D dialog;

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

	public String getUserId(){
		return getModel().getUserId().toUpperCase();
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

	/**
	 * This method is used to handle exceptions.
	 * @param loggerMsg
	 * @param errMsg
	 * @param parentScreen
	 * @param e
	 */
	public void handleException(String loggerMsg, String errMsg, Exception e) {
		Logger.getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
	}
	
	/**
	 * This method is used to display error message.
	 * @param loggerMsg
	 * @param errMsg
	 */
	public void displayErrorMessage(String loggerMsg, String errMsg)
	{
		Logger.getLogger().error(loggerMsg);
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
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

	@SuppressWarnings("rawtypes")
	public void setUpperCaseListener(final Object text){
		final LoggedTextArea textArea;
		final LoggedComboBox comboBox;
		if(text instanceof LoggedTextArea){
			textArea = (LoggedTextArea) text;
			textArea.textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable,
						String oldValue, String newValue) {
					textArea.setText(textArea.getText().toUpperCase());
				}
			});
		}else if(text instanceof LoggedComboBox){
			comboBox = (LoggedComboBox) text;
			comboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> arg0,
						String oldValue, String newValue) {
					comboBox.getEditor().setText(newValue.toUpperCase());
				}
			});
		}
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
	public void addFieldListener(final UpperCaseFieldBean TextField, final boolean isTextValueString){
		TextField.textProperty().addListener(new ChangeListener<String>() {
			boolean clearErrorMessage = false;
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(isTextValueString){
					if(hasSpecialCharacters(TextField)){
						clearErrorMessage = false;
						TextField.clear();
						TextField.settext(oldValue);
						clearErrorMessage = true;
					} else {
						if(clearErrorMessage) {
							clearErrorMessage = false;
							clearDisplayMessage();
						}
					}
				}else{
					if(QiCommonUtil.isNumberTextMaximum(0,TextField)){
						displayErrorMessage("Value cannot be greater than 32767","Value cannot be greater than 32767");
						TextField.clear();
						TextField.setText(String.valueOf(0));
					}
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
			displayErrorMessage("Special characters are not allowed ","Special characters are not allowed ");
			return true;
		}
		return false;
	}

}
