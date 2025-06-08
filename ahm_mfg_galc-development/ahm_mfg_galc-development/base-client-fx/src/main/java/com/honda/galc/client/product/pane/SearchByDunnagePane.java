package com.honda.galc.client.product.pane;

import com.honda.galc.client.product.action.KeyboardAction;
import com.honda.galc.client.product.entry.SearchByDunnageController;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SearchByDunnagePane extends AbstractProductSearchPane {
	private static final String DUNNAGE = "DUNNAGE";

	private SearchByDunnageController searchByDunnageController;

	private TextField dunnageTextField;

	private StringProperty dunnageProperty;

	private Button dunnageButton;
	private Button keyboardButton;


	public SearchByDunnagePane(BulkProductController productController) {
		super();
		this.productController = productController;
		parentPanelHeight = productController.getInputPaneHeight();
		initView();
		dunnageTextField.textProperty().bindBidirectional(dunnageProperty());
		searchByDunnageController = new SearchByDunnageController(this, getProductController().getView().getMainWindow().getApplicationContext());
	}

	private void initView() {
		String label = DUNNAGE;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getDunnageField().requestFocus();
			}
		});
		if(getProductController().getModel().getProperty().isManualProductEntryEnabled()) {
			dunnageButton = UiFactory.createButton(label, getButtonStyle(), true);
			getChildren().add(dunnageButton);
		} else {
			add(UiFactory.createLabel("dunnageLabel", label, getLabelStyle()));
		}
		getChildren().add(getDunnageField());

		if (getProductController().getModel().getProperty().isKeyboardButtonVisible()) {
			keyboardButton = UiFactory.createButton("KEYBOARD",getButtonStyle(), true);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					keyboardButton.setOnAction(new KeyboardAction(getProductController()));
				}
			});
			getChildren().add(keyboardButton);
		}
	}
	
	@Override
	public void associateSelected() {
		if(dunnageTextField.getText() != null && !dunnageTextField.getText().isEmpty()) {
			dunnageTextField.fireEvent(new ActionEvent());
		}
		dunnageTextField.requestFocus();
	}

	public TextField getDunnageField() {
		if(dunnageTextField == null) {
			dunnageTextField = UiFactory.createTextField("dunnageTextField", 20,
					getTextFieldTextStyle(), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
			dunnageTextField.setMaxWidth(getEntryWidth());
			dunnageTextField.setPrefHeight(getLabelHeight());
			dunnageTextField.setFocusTraversable(true);
		}
		return dunnageTextField;
	}

	public void setErrorMessage(String errorMessage, Object textField) {

		getProductController().getView().setErrorMessage(errorMessage, (TextField) textField);
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
	@Override
	public ProductController getProductController() {
		return this.productController;
	}

	public SearchByDunnageController getSearchByDunnageController() {
		return searchByDunnageController;
	}

	@Override
	public TextField getProductIdField() {
		return null;
	}
}
