package com.honda.galc.client.product.pane;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.product.entry.AbstractProductEntryPane;
import com.honda.galc.client.product.entry.GenericSearchByTransactionIdController;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class GenericSearchByTransactionIdPane extends AbstractGenericEntryPane {
	private static final String TRANSACTIONID = "TRANSACTION ID";
	private static final String OUTSTANDING_KICKOUT_CHECK_LABEL = "Show ONLY kickouts with transactions that have fixed defects ";
	
	private ApplicationContext applicationContext;
	private GenericSearchByTransactionIdController controller;
	
	private MainWindow mainWindow;

	private LoggedTextField transactionIdTextField;

	private StringProperty transactionIdProperty;

	private Button transactionIdBtn;
	private Button keyboardButton;
	private CheckBox outstandingKickoutOnlyCheckBox;

	public GenericSearchByTransactionIdPane(AbstractProductEntryPane parentView) {
		super(PaneId.SEARCH_BY_TRANSACTIONID_PANE);
		this.parentView = parentView;
		this.mainWindow = parentView.getMainWindow();
		this.applicationContext = parentView.getMainWindow().getApplicationContext();
		initView();
		createController();
		controller.initEventHandlers();
		transactionIdTextField.textProperty().bindBidirectional(dunnageProperty());
		EventBusUtil.register(this);
	}

	private void createController() {
		if(controller == null) {
			controller = new GenericSearchByTransactionIdController(this, applicationContext);
		}
	}

	private void initView() {
		String label = TRANSACTIONID;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getTransactionIdField().requestFocus();
			}
		});
		if(getController().isManualProductEntryEnabled()) {
			transactionIdBtn = createButton(label, true, ClientConstants.TRANSACTIONID_BUTTON_ID);
			getChildren().add(transactionIdBtn);

		} else {
			this.getChildren().add(UiFactory.createLabel("dunnageLabel", label));
		}
		getChildren().add(getTransactionIdField());
		getChildren().add(getOutstandingKickoutOnlyCheckBox());

		if (getController().isKeyboardButtonVisible()) {
			keyboardButton =createButton("KEYBOARD", true, ClientConstants.KEYBOARD_BUTTON_ID);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					controller.createKeyBoardPopup();
				}
			});
			getChildren().add(keyboardButton);
		}
		this.setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0");

	}
	
	public void associateSelected() {
		if(transactionIdTextField.getText() != null && !transactionIdTextField.getText().isEmpty()) {
			transactionIdTextField.fireEvent(new ActionEvent());
		}
		transactionIdTextField.requestFocus();
	}
	
	public GenericSearchByTransactionIdController getController() {
		if(controller == null) {
			controller = new GenericSearchByTransactionIdController(this, applicationContext);
		}
		return controller;
	}

	public CheckBox getOutstandingKickoutOnlyCheckBox() {
		if (outstandingKickoutOnlyCheckBox == null) {
			outstandingKickoutOnlyCheckBox = new CheckBox(OUTSTANDING_KICKOUT_CHECK_LABEL);
		}
		return outstandingKickoutOnlyCheckBox;
	}
	
	
	public TextField getTransactionIdField() {
		if(transactionIdTextField == null) {
			transactionIdTextField = UiFactory.createTextField(ClientConstants.TRANSACTIONID_TEXT_FIELD_ID, 20,
			null, TextFieldState.EDIT, Pos.BASELINE_LEFT, true);

			transactionIdTextField.prefHeightProperty().bind(this.heightProperty().multiply(0.40));
			transactionIdTextField.prefWidthProperty().bind(this.widthProperty().multiply(0.35));
			transactionIdTextField.styleProperty().bind(textStyle);
			transactionIdTextField.maxWidthProperty().bind(this.widthProperty().multiply(0.30));
			transactionIdTextField.setFocusTraversable(true);
		}
		return transactionIdTextField;
	}

	public void setErrorMessage(String errorMessage, Object textField) {

		controller.setErrorMessage("error", (TextField) textField);
	}

	public void setTransactionId(String value) {
		dunnageProperty().set(value);
	}

	public String getTransactionId() { 
		return dunnageProperty().get();
	}

	public StringProperty dunnageProperty() {
		if (this.transactionIdProperty == null) {
			this.transactionIdProperty = new SimpleStringProperty(this, "dunnage");
		}
		return this.transactionIdProperty;
	}

	public void setTestFieldState(TextField textField, TextFieldState state) {
		state.setState(textField);
	}

	public TextField getTransactionIdTextField() {
		return transactionIdTextField;
	}

	public StringProperty getTransactionIdProperty() {
		return transactionIdProperty;
	}

	public Button getTransactionIdButton() {
		return transactionIdBtn;
	}

	public void setTransactionIdButton(Button dunnageButton) {
		this.transactionIdBtn = dunnageButton;
	}

	public Button getKeyboardButton() {
		return keyboardButton;
	}

	public GenericSearchByTransactionIdController getSearchByTransactionIdController() {
		return controller;
	}
	
	public MainWindow getMainWindow() {
		return this.mainWindow;
	}
	 
	@Override
	public String getPaneLabel() {
		return "Search By Transaction Id";
	}
}
