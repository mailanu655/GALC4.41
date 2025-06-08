package com.honda.galc.client.dc.view;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.miginfocom.layout.CC;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.processor.PartConfirmationProcessor;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.product.validator.LengthValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * 
 * <h3>PartConfirmationView</h3>
 * <h3>The class is a operation view to show measurement overall status. </h3>
 * <h4>  </h4>
 * <p> The operation processor for PartConfirmationView must be {@link PartConfirmationProcessor}. </p>
 * <p> the overall status is OK only when <br/>
 * 	<li> The current part is already installed(a record in GAL185TBX).
 * 	<li> All of measurements(records in GAL195TBX) of the installed part is OK.
 * 	<li> The number of the measurements(record count in GAL195TBX) equals to MC_OP_PART_REV_TBX.MEASUREMENT_COUNT.
 * </p>
 * <br/>
 * <p>
 * If any of the above criteria fails, the overall status will be NG(Not Good).</p>
 * 
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
 * @see PartConfirmationProcessor
 * @author Hale Xie
 * June 30, 2014
 *
 */

public class PartConfirmationView extends OperationView {
	
	/**
	 * The Class ProductIdValidator is responsible to check if the scanned product ID equals to the current process product ID.
	 */
	protected class ProductIdValidator implements Command {

		/* (non-Javadoc)
		 * @see com.honda.galc.client.product.command.Command#getMessage(java.lang.String)
		 */
		public String getMessage(String propertyName) {
			return "The scanned product id doesn't match the current product id.";
		}

		/* (non-Javadoc)
		 * @see com.honda.galc.client.product.command.Command#getDetailedMessage(java.lang.String)
		 */
		public String getDetailedMessage(String propertyName) {
			return "The scanned product id doesn't match the current product id.";
		}

		/* (non-Javadoc)
		 * @see com.honda.galc.client.product.command.Command#execute(java.lang.String)
		 */
		public boolean execute(String value) {
			String expectedProductId = getProcessor().getProductId();
			return StringUtils.equalsIgnoreCase(expectedProductId,
					getActualProductIdTextField().getText());
		}

	}

	/** The content pane. */
	protected MigPane contentPane;
	
	/** The actual product id text field. */
	protected TextField actualProductIdTextField;
	
	/** The actual product id text field validator. */
	protected TextFieldCommand actualProductIdTextFieldValidator;
	
	/** The measurement overall status text field. */
	protected TextField measurementOverallStatusTextField;

	/**
	 * Gets the measurement overall status text field.
	 *
	 * @return the measurement overall status text field
	 */
	public TextField getMeasurementOverallStatusTextField() {
		return measurementOverallStatusTextField;
	}

	/** The done button. */
	protected Button doneButton;

	/**
	 * Instantiates a new part confirmation view.
	 *
	 * @param processor the  operation processor. It must be an instance of {@link PartConfirmationProcessor}
	 */
	public PartConfirmationView(OperationProcessor processor) {
		this((PartConfirmationProcessor) processor);
	}

	/**
	 * Instantiates a new part confirmation view.
	 *
	 * @param processor the processor
	 */
	public PartConfirmationView(PartConfirmationProcessor processor) {
		super(processor);
		EventBusUtil.register(this);
	}

	
	/**
	 * Handle product started event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleProductStartedEvent(ProductStartedEvent event){
		resetView();
	}
	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.OperationView#initComponents()
	 */
	@Override
	public void initComponents() {
		contentPane = new MigPane("insets 10, wrap 2",
				"[250!,left][300!, left]");
		setCenter(contentPane);

		// Product ID row
		ProductTypeData ptd = getProcessor().getController().getProductModel()
				.getApplicationContext().getProductTypeData();
		Label productIdLabel = UiFactory.createLabel(ptd.getProductIdLabel(),"Product Id",UiFactory.getIdle().getLabelFont());

		List<ProductNumberDef> list = ptd.getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		actualProductIdTextField = UiFactory.createTextField("actualProductIdTextField", length, UiFactory.getIdle().getInputFont(), TextFieldState.EDIT);
		contentPane.add(productIdLabel, new CC());
		contentPane.add(actualProductIdTextField, new CC().grow());

		Label measurementOverallStatusLabel = UiFactory.createLabel("measurementOverallStatusLabel", "Measurement Status", UiFactory.getIdle().getLabelFont());
		measurementOverallStatusTextField = UiFactory.createTextField("measurementOverallStatusTextField", UiFactory.getIdle().getLabelFont(), TextFieldState.DISABLED);
		contentPane.add(measurementOverallStatusLabel, new CC());
		contentPane.add(measurementOverallStatusTextField, new CC().grow());

		doneButton = UiFactory.createButton("Done", UiFactory.getInfo().getButtonFont(), false);
		doneButton.defaultButtonProperty().bind(doneButton.focusedProperty());
		contentPane.add(doneButton, new CC().spanX(2).alignX("right"));
		initValidators();
		mapActions();
		resetFocus();
		
		Platform.runLater(new Runnable() {
			public void run() {
				refreshView();
			}
		});
	}
	
	/**
	 * Reset focus of user input. If the product id isn't scanned yet, the focus will be on the text field of product ID or the focus will be on the Done Button. 
	 */
	public void resetFocus(){
		if(getActualProductIdTextField().isEditable()){
			getProcessor().getController().setFocusComponent(
					getActualProductIdTextField());
		}
		else{
			getProcessor().getController().setFocusComponent(
					getDoneButton());
		}
		
		getProcessor().getController().requestFocus();
	}

	/**
	 * Gets the done button.
	 *
	 * @return the done button
	 */
	public Button getDoneButton() {
		return doneButton;
	}

	/**
	 * Initialize the validators.
	 */
	protected void initValidators() {

		ProductTypeData ptd = getProcessor().getController().getProductModel()
				.getApplicationContext().getProductTypeData();
		ProductType productType = ptd.getProductType();
		actualProductIdTextFieldValidator = new TextFieldCommand();
		actualProductIdTextFieldValidator.setController(getProcessor()
				.getController());
		actualProductIdTextFieldValidator
				.setTextField(getActualProductIdTextField());
		List<Command> commands = new ArrayList<Command>();
		commands.add(new RequiredValidator());
		commands.add(new LengthValidator(productType.getProductIdLength()));
		commands.add(new ProductIdValidator());
		actualProductIdTextFieldValidator.setCommands(commands);
		actualProductIdTextFieldValidator.setPropertyName(ptd
				.getProductIdLabel());

	}

	/**
	 * Refresh the UI components according to the current product build result.
	 */
	protected void refreshProductBuildResult() {
		MeasurementStatus measurementOvervallStatus = getProcessor()
				.getMeasurementOverallStatus();
		getMeasurementOverallStatusTextField().setText(
				measurementOvervallStatus.name());
		if (measurementOvervallStatus == MeasurementStatus.OK) {
			TextFieldState.READ_ONLY
					.setState(getMeasurementOverallStatusTextField());
		} else {
			TextFieldState.ERROR_READ_ONLY
					.setState(getMeasurementOverallStatusTextField());
		}
	}

	/**
	 * Handle unit navigator event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleUnitNavigatorEvent(UnitNavigatorEvent event) {
		getLogger().debug("PartConfirmationView.handleUnitNavigatorEvent : " + event.toString());
		List<MCOperationRevision> structures = this.getProcessor().getController().getModel().getOperations();
		int index = structures.indexOf(this.getProcessor().getOperation());
		if (event.getType().equals(UnitNavigatorEventType.SELECTED) && index == event.getIndex()) {
			//refresh the view when the work unit is selected.
			refreshView();
		}
	}
	@Override
	public void refreshView() {
		super.refreshView();
		Platform.runLater(new Runnable() {
			public void run() {
				contentPane.layout();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.AbstractDataCollectionWidget#refreshView()
	 */
	@Override
	public void resetView() {
		getActualProductIdTextField().setText("");
		getMeasurementOverallStatusTextField().setText("");
		TextFieldState.EDIT.setState(getActualProductIdTextField());
		TextFieldState.DISABLED.setState(getMeasurementOverallStatusTextField());
	}

	/**
	 * Map UI actions to their event handlers.
	 */
	protected void mapActions() {

		getActualProductIdTextField().setOnAction(
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						getProcessor().getController().clearMessages();
						actualProductIdTextFieldValidator.execute();
						getProcessor().getController().processMessages();
						if (!getProcessor().getController().isErrorExists()) {
							getProcessor().processProductId();
							getProcessor().getController().processMessages();
							if(!getProcessor().getController().isErrorExists()){
								refreshProductBuildResult();
								getDoneButton().setDisable(false);
								getProcessor().getController().setFocusComponent(
										getDoneButton());
								getProcessor().getController().requestFocus();
							}
							else{
								getDoneButton().setDisable(true);
							}
						}

					}
				});
		getDoneButton().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				getProcessor().finish();
			}
		});
	}


	/**
	 * Gets the actual product id text field.
	 *
	 * @return the actual product id text field
	 */
	public TextField getActualProductIdTextField() {
		return actualProductIdTextField;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.AbstractDataCollectionWidget#getProcessor()
	 */
	@Override
	public PartConfirmationProcessor getProcessor() {
		return (PartConfirmationProcessor) super.getProcessor();
	}
}
