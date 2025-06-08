package com.honda.galc.client.qi.base;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>AbstractQiDialogController</code> is the parent class for QI Dialog.
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
public abstract class AbstractQiDialogController<M extends QiProcessModel, D extends FxDialog> implements EventHandler<ActionEvent>{
	
	private static final String PREVIOUS_LINE_INVALID_MSG = "Tracking status got updated by another process. Please cancel this operation.";

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

	public void close () {}

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
	 * This method is used to get the current logged in user ID.
	 * 
	 * @param userId
	 */
	public String getUserId(){
		return getModel().getApplicationContext().getUserId().toUpperCase();
	}
	
	protected void publishProductPreviousLineInvalidEvent() {
		EventBusUtil.publishAndWait(new StatusMessageEvent(PREVIOUS_LINE_INVALID_MSG, StatusMessageEventType.DIALOG_ERROR));
		EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_PREVIOUS_LINE_INVALID));
	}	
	
	public boolean isFrameQicsEngineSource() {
		return getModel().getProperty().isQicsEngineSource() &&
				getModel().getProductType().equals(ProductType.FRAME.toString());
	}
	public Logger getLogger() {
		return getModel().getLogger();
	}
	
}