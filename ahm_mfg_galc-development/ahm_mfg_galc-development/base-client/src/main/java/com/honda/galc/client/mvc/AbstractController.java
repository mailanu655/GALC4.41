package com.honda.galc.client.mvc;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import com.honda.galc.common.logging.Logger;


/**
 * 
 * <h3>AbstractController Class description</h3>
 * <p> AbstractController description </p>
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
 * Feb 24, 2014
 *
 *
 */
public abstract class AbstractController<M extends IModel,V extends AbstractView<?,?>> implements IController<M,V> {

	protected M model;
	
	/** The view component. */
	protected V view;
	
    
	public AbstractController (M model,V view) {
		this.model = model;
		this.view = view;
	}
	
	public V getView() {
		return view;
	}
	
	public M getModel() {
		return model;
	}
	
	public void showAndLogErrorMessage(String errorMessage) {
		getView().getMainWindow().setErrorMessage(errorMessage);
		getLogger().error(errorMessage);
	}
	
	public void clearMessage() {
		getView().getMainWindow().clearMessage();
	}
	
	public void showAndLogInfo(String infoMessage) {
		getView().getMainWindow().setMessage(infoMessage);
		getLogger().info(infoMessage);
	}
	
	public void showInfo(String infoMessage) {
		getView().getMainWindow().setMessage(infoMessage);
	}
	
	public void showErrorMessage(String errorMessage) {
		getView().getMainWindow().setErrorMessage(errorMessage);
	}
	
	public Logger getLogger() {
		return getView().getMainWindow().getLogger();
	}
	
	public String getProcessPointId() {
		return getView().getMainWindow().getApplicationContext().getProcessPointId();
	}
	
	public boolean isEvent(ListSelectionEvent e,ListSelectionModel model) {
		return e.getSource().equals(model);
	}
	
	public boolean isEvent(ActionEvent e,JButton button) {
		return e.getSource().equals(button);
	}
	
}
