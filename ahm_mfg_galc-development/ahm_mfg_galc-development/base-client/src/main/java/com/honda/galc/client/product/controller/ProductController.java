package com.honda.galc.client.product.controller;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.controller.listener.ProductDeviceListener;
import com.honda.galc.client.product.event.ProcessEvent;
import com.honda.galc.client.product.event.ProductCancelledEvent;
import com.honda.galc.client.product.event.ProductFinishedEvent;
import com.honda.galc.client.product.event.ProductStartedEvent;
import com.honda.galc.client.product.model.ProductModel;
import com.honda.galc.client.product.process.controller.ProcessController;
import com.honda.galc.client.product.process.view.ProcessView;
import com.honda.galc.client.product.validator.AlphaNumericValidator;
import com.honda.galc.client.product.validator.LengthValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.view.ProductPanel;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CycleComplete;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductController</code> is ... .
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
public class ProductController {

	protected enum State {
		IDLE, PROCESSING
	};

	private ProductPanel view;
	private ProductModel model;

	private State state;

	// private ValueValidator inputNumberValidator;
	private ChainCommand inputNumberValidator;
	private ProductDeviceListener deviceListener;

	public ProductController(ProductPanel view) {
		this.view = view;
		this.model = new ProductModel(getView().getMainWindow().getApplicationContext());
		this.state = State.IDLE;
		this.inputNumberValidator = createInputNumberValidator();
		this.deviceListener = new ProductDeviceListener(this);
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null) {
			device.registerDeviceListener(getDeviceListener(), getDeviceData());
		}
	}

	// === external actions api === //
	public void processInputNumberInvoke(String inputNumber) {
		JTextField inputTextField = getView().getInputPanel().getNumberTextField();
		inputTextField.setText(inputNumber);
		ActionEvent ae = new ActionEvent(inputTextField, ActionEvent.ACTION_FIRST, inputNumber);
		getView().getInputNumberListener().actionPerformed(ae);
	}

	public void clickNextButtonInvoke() {
		getView().getInfoPanel().getDoneButton().doClick();
	}

	public void cycleComplete() {
		// REMARK - currenlty we just move to idle state, if required we may
		// add some processing
		toIdle();
	}

	// === ui action api === //
	public void processInputNumber() {
		JTextField inputTextField = getView().getInputPanel().getNumberTextField();
		String inputNumber = inputTextField.getText();

		getLogger().info(String.format("Start processing InputNumber: %s", inputNumber));

		if (inputNumber != null) {
			inputNumber = inputNumber.trim();
			inputTextField.setText(inputNumber);
		}
		List<String> messages = getInputNumberValidator().execute(inputNumber);
		if (!messages.isEmpty()) {
			getView().setErrorMessage(messages.get(0), inputTextField);
			return;
		}

		BaseProduct product = getModel().findProduct(inputNumber);

		if (product == null) {
			ProductTypeData productTypeData = getView().getMainWindow().getApplicationContext().getProductTypeData();
			String productType = productTypeData == null ? "Product" : productTypeData.getProductTypeLabel();
			String msg = String.format("%s does not exist for: %s", productType, inputNumber);
			getView().setErrorMessage(msg, inputTextField);
			return;
		}

		startProduct(product);
	}

	public void startProduct(BaseProduct product) {
		getLogger().info(String.format("Start Processing product: %s ", product));
		getModel().setProduct(product);
		ProductStartedEvent event = new ProductStartedEvent();
		event.setProduct(getModel().getProduct());
		EventBus.publish(event);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("product", product);
		getView().getInfoPanel().setInfo(model);

		((CardLayout) getView().getTogglePanel().getLayout()).show(getView().getTogglePanel(), "info");

		prepareProcessViews(product);

		setFirstEnabledProcessViewSelected();
		setState(State.PROCESSING);
	}

	public void finishProduct() {

		BaseProduct product = getModel().getProduct();
		List<ProcessView> processes = getActiveProcesses();
		if (!processes.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("The following process are not finished and are required.");
			sb.append("\nPlease complete them before finishing product.\n");
			for (int i = 0; i < processes.size(); i++) {
				sb.append(" - ").append(processes.get(i).getController().getProcessName());
				if (i < processes.size() - 1) {
					sb.append("\n");
				}
			}
			JOptionPane.showMessageDialog(getView(), sb, "Warning", JOptionPane.WARNING_MESSAGE);
			if (processes.get(0) instanceof Component) {
				Component component = (Component) processes.get(0);
				getView().getTabbedPanel().setSelectedComponent(component);
				component.requestFocus();
			}
			return;
		}

		getLogger().info(String.format("Finish Processing product: %s ", product));
		getModel().invokeTracking();

		toIdle();
		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionComplete();
		}
		ProductFinishedEvent event = new ProductFinishedEvent();
		event.setProduct(product);
		EventBus.publish(event);
	}

	public void cancel() {
		BaseProduct product = getModel().getProduct();
		getLogger().info(String.format("Cancel Processing product: %s ", product));
		toIdle();
		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionInComplete();
		}
		ProductCancelledEvent event = new ProductCancelledEvent();
		event.setProduct(product);
		EventBus.publish(event);
	}

	// === transition api === //
	public void toIdle() {
		resetProcessViews();
		resetModel();
		resetUi();
		TextFieldState.EDIT.setState(getView().getInputPanel().getNumberTextField());
		((CardLayout) getView().getTogglePanel().getLayout()).show(getView().getTogglePanel(), "input");
		setFirstEnabledViewSelected();
		UiUtils.requestFocus(getView().getInputPanel().getNumberTextField());
		setState(State.IDLE);
	}

	protected void resetModel() {
		getModel().setProduct(null);
	}

	protected void resetUi() {
		getView().getInfoPanel().setInfo(null);
		getView().getInputPanel().getNumberTextField().setText(null);
		getView().clearErrorMessage();
	}

	// === events === //
	@EventSubscriber()
	public void onProcessEvent(ProcessEvent event) {
		if (event == null) {
			return;
		}
		if (ProcessController.State.FINISHED.equals(event.getProcessState())) {
			if (getActiveProcesses().isEmpty()) {
				finishProduct();
			} else {
				getView().getTabbedPanel().setSelectedComponent(getNextEnabledView());
			}
		}
	}

	// === config api === //
	public boolean isInIdleState() {
		return ProductController.State.IDLE.equals(getState());
	}

	public boolean isInProcessingState() {
		return ProductController.State.PROCESSING.equals(getState());
	}

	// === protected api === //
	protected List<ProcessView> getActiveProcesses() {
		List<ProcessView> processes = new ArrayList<ProcessView>();
		for (int i = 0; i < getView().getTabbedPanel().getTabCount(); i++) {
			Component comp = getView().getTabbedPanel().getComponentAt(i);
			if (comp instanceof ProcessView) {
				ProcessView pv = (ProcessView) comp;
				if (pv.getController().isActive() && pv.getController().isRequired()) {
					processes.add(pv);
				}
			}
		}
		return processes;
	}

	protected void prepareProcessViews(BaseProduct product) {
		for (int i = 0; i < getView().getTabbedPanel().getTabCount(); i++) {
			Component comp = getView().getTabbedPanel().getComponentAt(i);
			if (comp instanceof ProcessView) {
				getView().getTabbedPanel().setEnabledAt(i, true);
				ProcessView pv = (ProcessView) comp;
				pv.getController().prepare(product);
			}
		}
	}

	protected void resetProcessViews() {
		for (int i = 0; i < getView().getTabbedPanel().getTabCount(); i++) {
			Component comp = getView().getTabbedPanel().getComponentAt(i);
			if (comp instanceof ProcessView) {
				ProcessView pv = (ProcessView) comp;
				pv.getController().reset();
			}
		}
	}

	protected Component getNextEnabledView() {
		int selectedIx = getView().getTabbedPanel().getSelectedIndex();
		Component view = getFirstEnabledProcessView(selectedIx + 1);
		if (view == null) {
			view = getFirstEnabledProcessView();
		}
		if (view == null) {
			view = getFirstEnabledView(selectedIx + 1);
		}
		if (view == null) {
			view = getFirstEnabledView();
		}
		return view;
	}

	protected void setFirstEnabledProcessViewSelected() {
		Component previousSelection = getView().getTabbedPanel().getSelectedComponent();
		Component newSelection = getFirstEnabledProcessView();
		ProcessView pv = (ProcessView) newSelection;
		if (pv != null) {
			getView().getTabbedPanel().setSelectedComponent(newSelection);
			if (pv.equals(previousSelection)) {
				pv.getController().update();
			}
		}
	}

	protected Component getFirstEnabledProcessView() {
		return (getFirstEnabledProcessView(0));
	}

	protected Component getFirstEnabledProcessView(int startIx) {
		if (startIx < 0) {
			startIx = 0;
		}
		for (int i = startIx; i < getView().getTabbedPanel().getTabCount(); i++) {
			Component comp = getView().getTabbedPanel().getComponentAt(i);
			if (comp.isEnabled() && comp instanceof ProcessView) {
				if (((ProcessView) comp).getController().isActive()) {
					return comp;
				}
			}
		}
		return null;
	}

	protected Component getFirstEnabledView() {
		return getFirstEnabledView(0);
	}

	protected Component getFirstEnabledView(int startIx) {
		if (startIx < 0) {
			startIx = 0;
		}
		for (int i = startIx; i < getView().getTabbedPanel().getTabCount(); i++) {
			Component comp = getView().getTabbedPanel().getComponentAt(i);
			if (comp.isEnabled()) {
				return comp;
			}
		}
		return null;
	}

	protected void setFirstEnabledViewSelected() {
		Component comp = getFirstEnabledView();
		if (comp != null) {
			getView().getTabbedPanel().setSelectedComponent(comp);
		}
	}

	protected ChainCommand createInputNumberValidator() {
		List<Command> commands = new ArrayList<Command>();
		commands.add(new RequiredValidator());
		List<ProductNumberDef> defs = getProductTypeData().getProductNumberDefs();
		if (defs != null && !defs.isEmpty()) {
			Integer[] lengths = new Integer[defs.size()];
			for (int i = 0; i < defs.size(); i++) {
				lengths[i] = defs.get(i).getLength();
			}
			commands.add(new LengthValidator(lengths));
		}

		ChainCommand validator = ChainCommand.create(commands, "Input Number");
		return validator;
	}

	// === get/set === //
	public ProductPanel getView() {
		return view;
	}

	public ProductTypeData getProductTypeData() {
		return getView().getMainWindow().getApplicationContext().getProductTypeData();
	}

	protected ProductDeviceListener getDeviceListener() {
		return deviceListener;
	}

	public State getState() {
		synchronized (state) {
			return state;
		}
	}

	public void setState(State state) {
		synchronized (state) {
			this.state = state;
		}
	}

	protected List<IDeviceData> getDeviceData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new DataCollectionComplete());
		list.add(new CycleComplete());
		return list;
	}

	protected ChainCommand getInputNumberValidator() {
		return inputNumberValidator;
	}

	protected ProductModel getModel() {
		return model;
	}

	protected Logger getLogger() {
		return getView().getLogger();
	}
}
