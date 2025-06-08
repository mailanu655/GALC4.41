package com.honda.galc.client.product.process.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.product.event.ProcessEvent;
import com.honda.galc.client.product.process.model.ProcessModel;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessControllerAdapter</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProcessControllerAdapter<T extends ApplicationMainPanel> implements ProcessController<T> {

	private int mnemonicKey;
	private String processName;
	private T view;

	private ProcessModel model;

	// === controlling components === //
	private State state;
	private boolean required;
	private JComponent focusComponent;
	private List<String> errorMessages;
	private Map<JTextField, List<String>> fieldErrorMessages;
	private List<String> messages;

	private Map<State, Set<State>> transitions;

	public ProcessControllerAdapter(T view) {
		this.view = view;
		this.state = State.IDLE;
		this.fieldErrorMessages = new LinkedHashMap<JTextField, List<String>>();
		this.errorMessages = new ArrayList<String>();
		this.messages = new ArrayList<String>();
		this.transitions = defineTransitions();
	}

	// === transitions see defineTransitions() === //
	public void prepare(BaseProduct product) {
		// TODO review validate trans, maybe use TRANSISIONING state and synch
		// on it
		clearMessages();
		prepareInitValues();
		prepareInitState();
		prepareExecute(product);
		postExecute();
		setStateNotify(State.READY);
	}

	public void update() {
		clearMessage();
		if (State.READY.equals(getState())) {
			start();
			return;
		}
		if (State.IDLE.equals(getState())) {
			return;
		}
		updateInitValues();
		updateInitState();
		updateExecute();
		postExecute();
	}

	public void start() {
		clearMessages();
		if (isAlreadyProcessed()) {
			alreadyProcessed();
			return;
		}
		if (isNotProcessable()) {
			notProcessable();
			return;
		}
		getLogger().info(String.format("%s start processing product: %s", getProcessName(), getModel().getProduct()));
		startInitValues();
		startInitState();
		startExecute();
		postExecute();
		setStateNotify(State.PROCESSING);
	}

	public void finish() {
		clearMessages();
		finishExecute();
		if (isErrorExists()) {
			postExecute();
		} else {
			getLogger().info(String.format("%s finished processing product: %s", getProcessName(), getModel().getProduct()));
			finishInitValues();
			finishInitState();
			postExecute();
			setStateNotify(State.FINISHED);
		}
	}

	public void reset() {
		clearMessages();
		resetInitValues();
		resetInitState();
		resetExecute();
		postExecute();
		setStateNotify(State.IDLE);
	}

	public void alreadyProcessed() {
		getLogger().info(String.format("%s already processed: %s", getProcessName(), getModel().getProduct()));
		alreadyProcessedInitValues();
		alreadyProcessedInitState();
		alreadyProcessedExecute();
		postExecute();
		setStateNotify(State.ALREADY_PROCESSED);
	}

	public void notProcessable() {
		getLogger().info(String.format("%s product not processable: %s", getProcessName(), getModel().getProduct()));
		notProcessableInitValues();
		notProcessableInitState();
		notProcessableExecute();
		postExecute();
		setStateNotify(State.NOT_PROCESSABLE);
	}

	// === implementation prepare === //
	protected void prepareInitValues() {
		clearInputValues();
	}

	protected void prepareInitState() {

	}

	protected void prepareExecute(BaseProduct product) {
		getModel().setProduct(product);
	}

	// === implementation start === //
	protected void startInitValues() {

	}

	public void startInitState() {

	}

	protected void startExecute() {

	}

	// === implementation update === //
	protected void updateInitValues() {

	}

	protected void updateInitState() {

	}

	protected void updateExecute() {

	}

	// === implementation finish === //
	protected void finishExecute() {

	}

	protected void finishInitValues() {

	}

	protected void finishInitState() {

	}

	// === implementation reset === //
	protected void resetInitValues() {
		clearInputValues();
	}

	protected void resetInitState() {

	}

	protected void resetExecute() {
		clearModel();
	}

	// === implementation alreadyProcessed === //
	protected void alreadyProcessedInitValues() {

	}

	protected void alreadyProcessedInitState() {

	}

	protected void alreadyProcessedExecute() {
		String msg = String.format("Product is already processed by %s", getProcessName());
		addErrorMessage(msg);
	}

	// === implementation notProcessable === //
	protected void notProcessableInitValues() {

	}

	protected void notProcessableInitState() {

	}

	protected void notProcessableExecute() {
		String msg = String.format("Product is not processable by %s", getProcessName());
		addMessage(msg);
	}

	// === implementation common === //
	protected void postExecute() {
		processMessages();
		requestFocus();
	}

	protected void clearInputValues() {

	}

	protected void clearModel() {
		getModel().setProduct(null);
	}

	// === input components api === //
	public JComponent getFirstFocusableComponent() {
		JComponent component = getFirstFocusableErrorTextField();
		if (component != null) {
			return component;
		}
		component = getFirstFocusableTextField();
		if (component == null) {
			component = getFirstFocusableButton();
		}
		return component;
	}

	public JTextField getFirstFocusableErrorTextField() {
		if (getFieldErrorMessages().isEmpty()) {
			return null;
		}
		for (JTextField textField : getFieldErrorMessages().keySet()) {
			if (textField.isEnabled() && textField.isEditable()) {
				return textField;
			}
		}
		return null;
	}

	public JTextField getFirstFocusableTextField() {
		JTextField component = null;
		List<JTextField> fields = getFocusableTextFields();
		if ((component = getFirstFocusableTextFieldInState(fields, TextFieldState.ERROR)) != null) {
			return component;
		}
		if ((component = getFirstFocusableTextFieldInState(fields, TextFieldState.EDIT)) != null) {
			return component;
		}
		return null;
	}

	public JButton getFirstFocusableButton() {
		return null;
	}

	protected List<JTextField> getFocusableTextFields() {
		return null;
	}

	protected JTextField getFirstTextFieldInState(List<JTextField> components, TextFieldState state) {
		if (components == null || components.isEmpty() || state == null) {
			return null;
		}
		for (JTextField comp : components) {
			if (state.isInState(comp)) {
				return comp;
			}
		}
		return null;
	}

	protected JTextField getFirstFocusableTextFieldInState(List<JTextField> components, TextFieldState state) {
		if (components == null || components.isEmpty() || state == null) {
			return null;
		}
		for (JTextField comp : components) {
			if (state.isInState(comp) && comp.isEnabled() && comp.isEditable()) {
				return comp;
			}
		}
		return null;
	}

	// === messages / errorMessages === //
	public void processMessages() {

		for (JTextField textField : getFieldErrorMessages().keySet()) {
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

	public boolean isErrorExists(JTextField textField) {
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

	public void addErrorMessage(JTextField textField, String errorMsg) {
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

	public void addErrorMessage(JTextField textField) {
		addErrorMessage(textField, null);
	}

	public void addErrorMessage(Collection<JTextField> textFields, String errorMsg) {
		if (textFields == null) {
			return;
		}
		for (JTextField textField : textFields) {
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
		if (getView() instanceof ApplicationMainPanel) {
			((ApplicationMainPanel) getView()).getMainWindow().clearMessage();
		}
	}

	public void requestFocus() {
		setFocusComponentIfNull();
		UiUtils.requestFocus(getFocusComponent());
	}

	protected void setFocusComponentIfNull() {
		JComponent component = getFocusComponent();
		if (component != null) {
			return;
		}
		setFocusComponent(getFirstFocusableComponent());
	}

	public void displayMessage(String msg) {
		getView().getMainWindow().setMessage(msg);
		getView().getLogger().info(msg);
	}

	public void displayErrorMessage(String msg) {
		getView().getMainWindow().setErrorMessage(msg);
		getView().getLogger().warn(msg);
	}

	// === controlling state/transition api === //
	protected boolean isAlreadyProcessed() {
		return false;
	}

	protected boolean isNotProcessable() {
		return false;
	}

	public boolean isInState(State state) {
		if (getState() != null) {
			return getState().equals(state);
		}
		return getState() == state;
	}

	protected boolean isValidTransition(State start, State end) {
		if (start == null) {
			return false;
		}

		Set<State> resultStates = getTransitions().get(start);
		if (resultStates == null) {
			return false;
		}
		return resultStates.contains(end);
	}

	protected Map<State, Set<State>> defineTransitions() {
		Map<State, Set<State>> transitions = new HashMap<State, Set<State>>();

		Set<State> fromIdle = new HashSet<State>();
		fromIdle.add(State.READY);

		Set<State> fromReady = new HashSet<State>();
		fromReady.add(State.PROCESSING);
		fromReady.add(State.ALREADY_PROCESSED);
		fromReady.add(State.NOT_PROCESSABLE);
		fromReady.add(State.IDLE);

		Set<State> fromProcessing = new HashSet<State>();
		fromProcessing.add(State.FINISHED);
		fromProcessing.add(State.IDLE);

		Set<State> fromFinished = new HashSet<State>();
		fromFinished.add(State.IDLE);

		Set<State> fromAlreadyProcessed = new HashSet<State>();
		fromAlreadyProcessed.add(State.IDLE);

		Set<State> fromNotProcessable = new HashSet<State>();
		fromNotProcessable.add(State.IDLE);

		transitions.put(State.IDLE, fromIdle);
		transitions.put(State.READY, fromReady);
		transitions.put(State.PROCESSING, fromProcessing);
		transitions.put(State.FINISHED, fromFinished);
		transitions.put(State.ALREADY_PROCESSED, fromAlreadyProcessed);
		transitions.put(State.NOT_PROCESSABLE, fromNotProcessable);

		return transitions;
	}

	// === config === //
	public boolean isActive() {
		return State.READY.equals(getState()) || State.PROCESSING.equals(getState());
	}

	public boolean isRequired() {
		return required;
	}

	protected void setRequired(boolean required) {
		this.required = required;
	}

	// === get/set === //
	public T getView() {
		return view;
	}

	public String getProcessName() {
		if (StringUtils.isEmpty(this.processName)) {
			return getView().getClass().getSimpleName();
		}
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public ProductDao<? extends BaseProduct> getProductDao(ProductType productType) {
		return ProductTypeUtil.getProductDao(productType);
	}

	public State getState() {
		synchronized (state) {
			return state;
		}
	}

	protected void setState(State state) {
		synchronized (state) {
			this.state = state;
		}
	}

	protected void setStateNotify(State state) {
		if (state.equals(getState())) {
			return;
		}
		setState(state);
		stateChangedNotify();
	}

	protected void stateChangedNotify() {
		ProcessEvent event = new ProcessEvent();
		if (getModel().getProduct() != null) {
			event.setProductId(getModel().getProduct().getProductId());
		}
		event.setProcessName(getProcessName());
		event.setProcessState(getState());
		EventBus.publish(event);
	}

	public JComponent getFocusComponent() {
		return focusComponent;
	}

	public void setFocusComponent(JComponent focusComponent) {
		this.focusComponent = focusComponent;
	}

	protected List<String> getErrorMessages() {
		return errorMessages;
	}

	public Map<JTextField, List<String>> getFieldErrorMessages() {
		return fieldErrorMessages;
	}

	protected List<String> getMessages() {
		return messages;
	}

	public Map<State, Set<State>> getTransitions() {
		return transitions;
	}

	public int getMnemonicKey() {
		return mnemonicKey;
	}

	public void setMnemonicKey(int mnemonicKey) {
		this.mnemonicKey = mnemonicKey;
	}

	public ProcessModel getModel() {
		return model;
	}

	public void setModel(ProcessModel model) {
		this.model = model;
	}

	protected Logger getLogger() {
		return getView().getLogger();
	}
}
