package com.honda.galc.client.teamleader.qi.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Toggle;
import javafx.scene.paint.Color;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.client.teamleader.qi.view.QiAbstractPanel;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.enumtype.ScreenAccessLevel;

public abstract class QiAbstractPanelController<M extends QiModel, P extends QiAbstractPanel, V extends QiAbstractTabbedView<?,?>> implements EventHandler<ActionEvent> {

	private AuditEntry entity;
	private M model;
	private P panel;
	private V view;

	public Logger getLogger() {
		return panel.getLogger();
	}

	public String getUserId() {
		return getModel().getUserId().toUpperCase();
	}

	/** This implementation of this method will handle the events occur on panel
	 * 
	 */
	public abstract void handle(ActionEvent arg0);
	
	/** This implementation of this method will initialize the listeners on panel
	 * 
	 */
	public abstract void initListeners();
	
	/**
	 * set text field listeners
	 */
	public void setTextFieldListener(LoggedTextField textField) {
		textField.focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				clearDisplayMessage();
			}
		});
	}

	/**
	 * set radiobutton listeners
	 */
	public void setRadioButtonListener(LoggedRadioButton radioButton) {
		radioButton.focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				clearDisplayMessage();
			}
		});
	}

	/**
	 * This method is used for Text Field Listener
	 */
	public void addFieldListener(final LoggedTextField textField, final boolean isTextValueString, final boolean isUpdateEnablerOn, final boolean isSpecialCharCheckOn) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (isTextValueString) {
					if (isSpecialCharCheckOn && hasSpecialCharacters(textField)) {
						textField.clear();
						textField.setText(oldValue);
					}
				} else {
					if (QiCommonUtil.isNumberTextMaximum(0,textField)) {
						try{
						displayErrorMessage("Value cannot be greater than "+Short.MAX_VALUE+"", "Value cannot be greater than "+Short.MAX_VALUE+"");
						}
						catch(Exception e){
							EventBusUtil.publish(new StatusMessageEvent("Value cannot be greater than "+Short.MAX_VALUE+"", StatusMessageEventType.DIALOG_ERROR));
						}
						
						textField.clear();
						textField.setText(String.valueOf(0));
					}
				}
				
				getPanel().getPanelButton().setDisable(!isUpdateEnablerOn);
			}
		});
	}

	/**
	 * This method is used to check the special character in TextBox
	 * 
	 * @param textField
	 * @return
	 */
	public boolean hasSpecialCharacters(LoggedTextField textField) {
		Pattern regex = Pattern.compile("^[A-Za-z0-9\\s]*$");
		Matcher matcher = null;
			matcher = regex.matcher(StringUtils.trim(textField.getText()));

		if (!matcher.find()) {
			try {
				displayErrorMessage("Special characters are not allowed ", "Special characters are not allowed ");
			} catch (Exception e) {
				EventBusUtil.publish(new StatusMessageEvent("Special characters are not allowed ", StatusMessageEventType.DIALOG_ERROR));
			}
			return true;
		}
		return false;
	}

	/**
	 * This method is used for Text Field Listener
	 */
	public void addTextFieldCommonListener(final UpperCaseFieldBean textField, final boolean isTextValueString) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (isTextValueString) {
					if (QiCommonUtil.hasSpecialCharacters(textField)) {
						textField.clear();
						textField.setText(oldValue);
					}
				} else {
					if (!newValue.matches("\\d+")) {
						textField.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
				
				getPanel().getPanelButton().setDisable(false);
			}
		});
	}

	/**
	 * This method is used for Text Area Listener
	 */
	public void addTextAreaListener(final LoggedTextArea textArea) {
		textArea.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				textArea.setText(textArea.getText().toUpperCase());
				if (QiCommonUtil.hasSpecialCharacters(textArea)) {
					textArea.clear();
					textArea.setText(oldValue);
				}
				
				getPanel().getPanelButton().setDisable(false);
			}
		});
	}
	
	/**  This method is used to get Active/Inactive button's status.
	 *
	 * @return
	 */
	protected int getStatus(){
		if(getPanel().getActiveRadioBtn().isSelected())
			return 1;
		else if(getPanel().getInactiveRadioBtn().isSelected())
			return 0;
		return 0;
	}
	
	/**  This method is used for displaying error message and log the error
	 *
	 * @param loggerMsg
	 * @param errMsg
	 */
	public void displayErrorMessage(String loggerMsg, String errMsg) {
		panel.getLogger().warn(loggerMsg);
		setErrorMessage(errMsg);
	}
	/**  This method is used for displaying error message
	 *
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		panel.setErrorMessage(errorMessage);
	}

	public void setMessage(String message) {
		panel.setMessage(message);
	}
	
	public void setMessage(String message,Color color) {
		panel.setMessage(message, color);
	}
	
	public void setStatusMessage(String message) {
		panel.setMessage(message);
	}
	
	/**
	 * This method is used to clear the displayed error message on main window.
	 */
	public void clearDisplayMessage() {
		panel.clearErrorMessage();
	}
	
	/**  This method will be used to ensure all the mandatory fields have valid values
	 * 
	 */
	protected abstract boolean checkMandatoryFields(boolean isPanel);
	
	/**  This method will be used to populate the fields on panel based on users tree node selection on main view
	 * 
	 */
	protected abstract void populateData();
	
	/** Check whether user has full access or not
	 * @return
	 */
	public boolean isFullAccess() {
		return getModel().getApplicationContext().getHighestAccessLevel() == ScreenAccessLevel.FULL_ACCESS;
	}
	
	/** This method is used to display message when user try to
	 *  perform operation which he/she is not authorized to do. 
	 * 
	 */
	protected void displayUnauthorizedOperationMessage(){
			displayErrorMessage("You are not authorized to perform this operation.", "You are not authorized to perform this operation.");
	}

	/**  This method is used to display message if there is validation failure on dialog for the panel.
	 * 
	 * @param errorMessage
	 */
	protected void displayErrorMessageForDialog(String errorMessage) {
		EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.DIALOG_ERROR));
	}

	/**  This method is used to display and log message if there is validation failure on dialog for the panel.
	 * 
	 * @param logMessage
	 * @param errorMessage
	 */
	protected void displayErrorMessageForDialog(String logMessage, String errorMessage){
	getLogger().warn(logMessage);
	EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.DIALOG_ERROR));
	}
	
	/**
	 * This method is used to get application id of the main window
	 * @return  applicationId
	 */
	
	public String getApplicationId(){
		return getView().getMainWindow().getApplicationContext().getApplicationId();
	}
	
	public P getPanel() {
		return panel;
	}

	public void setPanel(P panel) {
		this.panel = panel;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}
	
	public AuditEntry getEntity() {
		return entity;
	}

	public void setEntity(AuditEntry entity) {
		this.entity = entity;
	}
	
	protected <T> void addCommonComboboxChangeListener(final ComboBox<T> comboBox) {
		comboBox.valueProperty().addListener(new ChangeListener<T>() {
					@Override
					public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
						getPanel().getPanelButton().setDisable(false);
					}
				});
	}


	protected void addStatusChangeListener() {
		getPanel().getActiveRadioBtn().getToggleGroup().selectedToggleProperty()
				.addListener(new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
						getPanel().getPanelButton().setDisable(false);
					}
				});
	}

	protected void addDescAreaChangeListener() { 
		getPanel().getDescriptionTextArea().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getPanel().getPanelButton().setDisable(false);
			}

		});
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
}
