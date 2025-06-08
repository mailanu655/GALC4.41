package com.honda.galc.client.product.pane;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.product.entry.AbstractProductEntryPane;
import com.honda.galc.client.product.entry.GenericSearchByDunnageController;
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
import javafx.scene.control.TextField;

public class GenericSearchByDunnagePane extends AbstractGenericEntryPane {
	private static final String DUNNAGE = "DUNNAGE";
	
	private ApplicationContext applicationContext;
	private GenericSearchByDunnageController controller;
	
	private MainWindow mainWindow;

	private LoggedTextField dunnageTextField;

	private StringProperty dunnageProperty;

	private Button dunnageButton;
	private Button keyboardButton;

	public GenericSearchByDunnagePane(AbstractProductEntryPane parentView) {
		super(PaneId.SEARCH_BY_DUNNAGE_PANE);
		this.parentView = parentView;
		this.mainWindow = parentView.getMainWindow();
		this.applicationContext = parentView.getMainWindow().getApplicationContext();
		initView();
		createController();
		controller.initEventHandlers();
		dunnageTextField.textProperty().bindBidirectional(dunnageProperty());
		EventBusUtil.register(this);
	}

	private void createController() {
		if(controller == null) {
			controller = new GenericSearchByDunnageController(this, applicationContext);
		}
	}

	private void initView() {
		String label = DUNNAGE;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getDunnageField().requestFocus();
			}
		});
		if(getController().isManualProductEntryEnabled()) {
			dunnageButton = createButton(label, true, ClientConstants.DUNNAGE_BUTTON_ID);
			getChildren().add(dunnageButton);
		} else {
			this.getChildren().add(UiFactory.createLabel("dunnageLabel", label));
		}
		getChildren().add(getDunnageField());

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
		if(dunnageTextField.getText() != null && !dunnageTextField.getText().isEmpty()) {
			dunnageTextField.fireEvent(new ActionEvent());
		}
		dunnageTextField.requestFocus();
	}
	
	public GenericSearchByDunnageController getController() {
		if(controller == null) {
			controller = new GenericSearchByDunnageController(this, applicationContext);
		}
		return controller;
	}

	public TextField getDunnageField() {
		if(dunnageTextField == null) {
			dunnageTextField = UiFactory.createTextField(ClientConstants.DUNNAGE_TEXT_FIELD_ID, 20,
					null, TextFieldState.EDIT, Pos.BASELINE_LEFT, true);

			dunnageTextField.prefHeightProperty().bind(this.heightProperty().multiply(0.40));
			dunnageTextField.prefWidthProperty().bind(this.widthProperty().multiply(0.35));
			dunnageTextField.styleProperty().bind(textStyle);
			dunnageTextField.maxWidthProperty().bind(this.widthProperty().multiply(0.30));
			dunnageTextField.setFocusTraversable(true);
		}
		return dunnageTextField;
	}

	public void setErrorMessage(String errorMessage, Object textField) {

		controller.setErrorMessage("error", (TextField) textField);
	}

	public void setDunnage(String value) {
		dunnageProperty().set(value);
	}

	public String getDunnage() { 
		return dunnageProperty().get();
	}

	public StringProperty dunnageProperty() {
		if (this.dunnageProperty == null) {
			this.dunnageProperty = new SimpleStringProperty(this, "dunnage");
		}
		return this.dunnageProperty;
	}

	public void setTestFieldState(TextField textField, TextFieldState state) {
		state.setState(textField);
	}

	public TextField getDunnageTextField() {
		return dunnageTextField;
	}

	public StringProperty getDunnageProperty() {
		return dunnageProperty;
	}

	public Button getDunnageButton() {
		return dunnageButton;
	}

	public void setDunnageButton(Button dunnageButton) {
		this.dunnageButton = dunnageButton;
	}

	public Button getKeyboardButton() {
		return keyboardButton;
	}

	public GenericSearchByDunnageController getSearchByDunnageController() {
		return controller;
	}
	
	public MainWindow getMainWindow() {
		return this.mainWindow;
	}
	 
	@Override
	public String getPaneLabel() {
		return "Search By Dunnage";
	}
}
