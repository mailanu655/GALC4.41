package com.honda.galc.client.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.enumtype.ScreenAccessLevel;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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

	private M model;
	
	/** The view component. */
	private V view;
	
    private Control focusComponent;
	private List<String> errorMessages;
	private Map<TextField, List<String>> fieldErrorMessages;
	private List<String> messages;
    
	public AbstractController (M model,V view) {
		this.model = model;
		this.view = view;
		this.fieldErrorMessages = new LinkedHashMap<TextField, List<String>>();
		this.errorMessages = new ArrayList<String>();
		this.messages = new ArrayList<String>();

	}
	
	public V getView() {
		return view;
	}
	
	public M getModel() {
		return model;
	}
	
	@Override
	public void activate(){
		initEventHandlers();
	}
	
	public void displayMessage(String msg) {
		getView().getMainWindow().setMessage(msg);
		getView().getLogger().info(msg);
	}

	/*
	 * Change the status bar to green as well for a more visual indication 
	 * that the result was good
	 */
	public void displaySuccessfulMessage(String msg) {
		getView().getMainWindow().setMessage(msg, Color.LIGHTGREEN);
		getView().getLogger().info(msg);
	}
	
	public void displayErrorMessage(String msg) {
		getView().getMainWindow().setErrorMessage(msg);
		getView().getLogger().warn(msg);
	}
	
	public void displayErrorWithId(String msg, String id) {
		getView().getMainWindow().setErrorMessage(msg, id);
		getView().getLogger().warn(msg);
	}
	
	public void displayErrorMessage(String loggerMsg, String userMsg) {
		getView().getMainWindow().setErrorMessage(userMsg);
		getView().getLogger().warn(loggerMsg);
	}
	
	public Control getFocusComponent() {
		return focusComponent;
	}

	public void setFocusComponent(Control focusComponent) {
		this.focusComponent = focusComponent;
	}

	protected List<String> getErrorMessages() {
		return errorMessages;
	}

	public Map<TextField, List<String>> getFieldErrorMessages() {
		return fieldErrorMessages;
	}

	protected List<String> getMessages() {
		return messages;
	}
	
	protected void setFocusComponentIfNull() {
		Control component = getFocusComponent();
		if (component != null) {
			return;
		}
		setFocusComponent(getFirstFocusableComponent());
	}
	
	// === input components api === //
	public Control getFirstFocusableComponent() {
		Control component = getFirstFocusableErrorTextField();
		if (component != null) {
			return component;
		}
		component = getFirstFocusableTextField();
		if (component == null) {
			component = getFirstFocusableButton();
		}
		return component;
	}

	public TextField getFirstFocusableErrorTextField() {
		if (getFieldErrorMessages().isEmpty()) {
			return null;
		}
		for (TextField textField : getFieldErrorMessages().keySet()) {
			if (textField.isDisabled() && textField.isEditable()) {
				return textField;
			}
		}
		return null;
	}

	public TextField getFirstFocusableTextField() {
		TextField component = null;
		List<TextField> fields = getFocusableTextFields();
		if ((component = getFirstFocusableTextFieldInState(fields, TextFieldState.ERROR)) != null) {
			return component;
		}
		if ((component = getFirstFocusableTextFieldInState(fields, TextFieldState.EDIT)) != null) {
			return component;
		}
		return null;
	}

	public Control getFirstFocusableButton() {
		return null;
	}
	
	protected List<TextField> getFocusableTextFields() {
		return null;
	}
	
	protected TextField getFirstTextFieldInState(List<TextField> components, TextFieldState state) {
		if (components == null || components.isEmpty() || state == null) {
			return null;
		}
		for (TextField comp : components) {
			if (state.isInState(comp)) {
				return comp;
			}
		}
		return null;
	}

	protected TextField getFirstFocusableTextFieldInState(List<TextField> components, TextFieldState state) {
		if (components == null || components.isEmpty() || state == null) {
			return null;
		}
		for (TextField comp : components) {
			if (state.isInState(comp) && !comp.isDisabled() && comp.isEditable()) {
				return comp;
			}
		}
		return null;
	}
	
	public boolean isErrorExists(TextField textField) {
		if (textField == null) {
			return false;
		}
		if (getFieldErrorMessages().get(textField) != null) {
			return true;
		}
		return false;
	}

	public void addErrorMessage(String errorMsg) {
		getErrorMessages().add(errorMsg);
	}

	public void addErrorMessage(TextField textField, String errorMsg) {
		if (textField == null) {
			return;
		}
		List<String> list = getFieldErrorMessages().get(textField);
		if (list == null) {
			list = new ArrayList<String>();
			getFieldErrorMessages().put(textField, list);
		}
		if (!StringUtils.isBlank(errorMsg)) {
			list.add(errorMsg);
		}
	}

	public void addErrorMessage(TextField textField) {
		addErrorMessage(textField, null);
	}

	public void addErrorMessage(Collection<TextField> textFields, String errorMsg) {
		if (textFields == null) {
			return;
		}
		for (TextField textField : textFields) {
			addErrorMessage(textField, errorMsg);
		}
	}

	public void addMessage(String msg) {
		getMessages().add(msg);
	}

	public void addMessage(List<String> msgs) {
		if (msgs != null) {
			getMessages().addAll(msgs);
		}
	}

	public void clearMessages() {
		getErrorMessages().clear();
		getFieldErrorMessages().clear();
		getMessages().clear();
		clearMessage();
	}

	public void clearMessage() {
		if(getView().getMainWindow() != null)
			getView().getMainWindow().clearMessage();
	}

	public void clearStatusOnly() {
		if(getView().getMainWindow() != null)
			getView().getMainWindow().clearStatusOnly();
	}

	public void clearById(String newId) {
		if(getView().getMainWindow() != null)
			getView().getMainWindow().clearById(newId);
	}

	// === messages / errorMessages === //
	public void processMessages() {

		for (TextField textField : getFieldErrorMessages().keySet()) {
			TextFieldState.ERROR.setState(textField);
		}

		String errMsg = getErrorMessage();
		if (errMsg != null) {
			displayErrorMessage(errMsg);
			return;
		}

		String msg = getMessage();
		if (msg != null) {
			displayMessage(msg);
			return;
		}
	}

	protected String getErrorMessage() {
		for (String str : getErrorMessages()) {
			if (!StringUtils.isEmpty(str)) {
				return str;
			}
		}
		for (List<String> msgs : getFieldErrorMessages().values()) {
			if (msgs != null && !msgs.isEmpty()) {
				for (String str : msgs) {
					if (!StringUtils.isEmpty(str)) {
						return str;
					}
				}
			}
		}
		return null;
	}

	protected String getMessage() {
		for (String str : getMessages()) {
			if (!StringUtils.isEmpty(str)) {
				return str;
			}
		}
		return null;
	}

	public boolean isErrorExists() {
		return !getErrorMessages().isEmpty() || !getFieldErrorMessages().isEmpty();
	}


	public void requestFocus() {
		setFocusComponentIfNull();
		UiUtils.requestFocus(getFocusComponent());
	}

	protected Logger getLogger() {
		return getView().getLogger();
	}
	
	public ProductActionId[] getProductActionIds() {
		return new ProductActionId[0];
	}
	
	public ProductActionId[] getProductActionIdsOnAccept() {
		return new ProductActionId[0];
	}
	
	public abstract void initEventHandlers();
	
	/**
	 * This method is used to get application id of the main window
	 * @return  applicationId
	 */
	
	public String getApplicationId(){
		return getView().getMainWindow().getApplicationContext().getApplicationId();
	}

	
	/**
 	* This method is used to check user authorization
 	*
 	*/
	public boolean isFullAccess() {
		return getView().getMainWindow().getApplicationContext().getHighestAccessLevel() == ScreenAccessLevel.FULL_ACCESS;
	}
	
	/**
	 * This method will be used to pass any extra details while publishing
	 * product event.<br>
	 * Respective controller need to override it if using this feature.
	 * 
	 * @param productDetails
	 */
	public void setProductEventDetails(Map<String, Object> productDetails) {
		//TODO Each controller can override this if they are subscribing for any product event
		//and except data from not-dependent module. 
	}

	
}
