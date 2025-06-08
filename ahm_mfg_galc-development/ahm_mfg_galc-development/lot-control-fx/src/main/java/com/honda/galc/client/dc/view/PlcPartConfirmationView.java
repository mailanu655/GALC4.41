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
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.processor.PartConfirmationProcessor;
import com.honda.galc.client.dc.processor.PlcPartConfirmationProcessor;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.product.validator.LengthValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.ProductTypeData;

public class PlcPartConfirmationView extends OperationView {
	
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
	
	/** The Reject button. */
	protected Button rejectButton;

	/**
	 * Instantiates a new part confirmation view.
	 *
	 * @param processor the  operation processor. It must be an instance of {@link PartConfirmationProcessor}
	 */
	public PlcPartConfirmationView(OperationProcessor processor) {
		this((PlcPartConfirmationProcessor) processor);
	}

	/**
	 * Instantiates a new part confirmation view.
	 *
	 * @param processor the processor
	 */
	public PlcPartConfirmationView(PlcPartConfirmationProcessor processor) {
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
		contentPane.add(UiFactory.createLabel("descriptionLabel", "Scan PID label to confirm data received in GALC", Fonts.SS_DIALOG_BOLD(20)), new CC().spanX(2).alignX("left"));
		Label productIdLabel = UiFactory.createLabel(ptd.getProductIdLabel(),"Product Id",UiFactory.getIdle().getLabelFont());

		List<ProductNumberDef> list = ptd.getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		actualProductIdTextField = UiFactory.createTextField("actualProductIdTextField", length, UiFactory.getIdle().getInputFont(), TextFieldState.EDIT);
		contentPane.add(productIdLabel, new CC());
		contentPane.add(actualProductIdTextField, new CC().grow());
		
		rejectButton = UiFactory.createButton("Reject", UiFactory.getInfo().getButtonFont(), false);
		rejectButton.defaultButtonProperty().bind(rejectButton.focusedProperty());
		contentPane.add(rejectButton, new CC().spanX(2).alignX("right"));

		initValidators();
		mapActions();
		initialize();
		
		Platform.runLater(new Runnable() {
			public void run() {
				refreshView();
			}
		});
	}
	
	private void initialize(){
		if(!getProcessor().hasInstalledPart()){
			actualProductIdTextField.setText("");
			actualProductIdTextField.requestFocus();
			rejectButton.setDisable(true);
		}else{
			actualProductIdTextField.setText(getProcessor().getProductId());
			actualProductIdTextField.setEditable(false);
			rejectButton.setDisable(false);
			rejectButton.requestFocus();
		}
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
				actualProductIdTextField.requestFocus();
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
		TextFieldState.EDIT.setState(getActualProductIdTextField());
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
									String message = getProcessor().getMeasurementOverallStatus();
									if(StringUtils.isEmpty(message))
										getProcessor().finish();
									else getProcessor().getController().displayErrorMessage(message);									
								}
							}
						}

				});
		rejectButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				rejectButton.setDisable(true);
				getActualProductIdTextField().setText("");
				getActualProductIdTextField().setEditable(true);
				getActualProductIdTextField().requestFocus();
				EventBusUtil.publish(createDataCollectionEvent(DataCollectionEventType.PDDA_REJECT));
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
	public PlcPartConfirmationProcessor getProcessor() {
		return (PlcPartConfirmationProcessor) super.getProcessor();
	}
}

