package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.teamleader.qi.view.DataValidationDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.QiPropertyBean;

import com.honda.galc.service.property.PropertyService;

public abstract class AbstractQiController<M extends AbstractModel, V extends AbstractTabbedView<?,?>> extends AbstractController<M, V> {

	boolean clearTxtField=false;
	boolean specialCharExists=false;
	
	public AbstractQiController(M model, V view) {
		super(model, view);
		getModel().setApplicationContext(getView().getMainWindow().getApplicationContext());
	}

	public Logger getLogger() {
		return getModel().getLogger();
	}
	/**
	 * This method is used for exception handling.
	 */
	public void handleException(String loggerMsg, String errMsg, Exception e) {
		getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.ERROR));
	}
	/**
	 * This mehtod is used to clear the display messgae on the panel.
	 */
	public void clearDisplayMessage() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
	}

	public String getUserId(){
		return getModel().getApplicationContext().getUserId().toUpperCase();
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
	 * This method is used for Text Field Listener
	 */
	public void addFieldListener(final UpperCaseFieldBean TextField){

		TextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(clearTxtField) {
					clearTxtField = false;
					return;
				}
				if(QiCommonUtil.hasSpecialCharacters(TextField)){
					clearTxtField = true;
					specialCharExists = true;
					displayErrorMessage("Special characters are not allowed ");
					TextField.settext(oldValue);
				} else {
					clearDisplayMessage();
					specialCharExists = false;
				}
			}
		});
	}
	
	public abstract void addContextMenuItems();
	
	/**
	 * Displays local site validation and returns true if any site is impacted
	 */
	public String isLocalSiteImpacted(List<?> checkList,Stage parent) {
		Map<String,String> siteMap=PropertyService.getPropertyBean(QiPropertyBean.class).getSitesForValidation();
		if(siteMap==null || siteMap.isEmpty()){
			if(!(MessageDialog.confirm(parent,"Local Site Data will not be validated as local site(s) are not configured. Do you still want to continue?")))
				return QiConstant.NO_LOCAL_SITES_CONFIGURED;
			else
			{
				return "";
			}
		}
		DataValidationDialog dataValidationDialog=new DataValidationDialog("Local Validation",getApplicationId(),getView().getScreenName(),checkList);
		dataValidationDialog.showDialog();
		return dataValidationDialog.isLocalSiteImpact()?QiConstant.LOCAL_SITES_IMPACTED:"";
		
	}
	
	public void publishErrorMessage(String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
	}

}
