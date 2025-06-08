package com.honda.galc.client.product.pane;

import com.honda.galc.client.product.action.KeyboardAction;
import com.honda.galc.client.product.entry.SearchByRepairAreaController;
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

public class SearchByRepairAreaPane extends AbstractProductSearchPane {
	private static final String REPAIR_AREA = "Repair Area";

	private SearchByRepairAreaController searchByRepairAreaController;

	private TextField repairAreaTextField;

	private StringProperty repairAreaProperty;

	private Button repairAreaButton;
	private Button keyboardButton;


	public SearchByRepairAreaPane(BulkProductController productController) {
		super();
		this.productController = productController;
		parentPanelHeight = productController.getInputPaneHeight();
		initView();
		repairAreaTextField.textProperty().bindBidirectional(repairAreaProperty());
		searchByRepairAreaController = new SearchByRepairAreaController(this, getProductController().getView().getMainWindow().getApplicationContext());
	}

	private void initView() {
		String label = REPAIR_AREA;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getRepairAreaField().requestFocus();
			}
		});
		if(getProductController().getModel().getProperty().isManualProductEntryEnabled()) {
			repairAreaButton = UiFactory.createButton(label, getButtonStyle(), true);
			getChildren().add(repairAreaButton);
		} else {
			add(UiFactory.createLabel("repairAreaLabel", label, getLabelStyle()));
		}
		getChildren().add(getRepairAreaField());

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
		if(repairAreaTextField.getText() != null && !repairAreaTextField.getText().isEmpty()) {
			repairAreaTextField.fireEvent(new ActionEvent());
		}
		repairAreaTextField.requestFocus();
	}

	public TextField getRepairAreaField() {
		if(repairAreaTextField == null) {
			repairAreaTextField = UiFactory.createTextField("repairAreaTextField", 20,
					getTextFieldTextStyle(), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
			repairAreaTextField.setMaxWidth(getEntryWidth());
			repairAreaTextField.setPrefHeight(getLabelHeight());
			repairAreaTextField.setFocusTraversable(true);
		}
		return repairAreaTextField;
	}

	public void setErrorMessage(String errorMessage, Object textField) {

		getProductController().getView().setErrorMessage(errorMessage, (TextField) textField);
	}

	public void setRepairArea(String value) {
		repairAreaProperty().set(value);
	}

	public String getRepairArea() { 
		return repairAreaProperty().get();
	}

	public StringProperty repairAreaProperty() {
		if (this.repairAreaProperty == null) {
			this.repairAreaProperty = new SimpleStringProperty(this, "repairArea");
		}
		return this.repairAreaProperty;
	}

	public void setTestFieldState(TextField textField, TextFieldState state) {
		state.setState(textField);
	}
	
	public TextField getRepairAreaTextField() {
		return repairAreaTextField;
	}

	public void setRepairAreaTextField(TextField repairAreaTextField) {
		this.repairAreaTextField = repairAreaTextField;
	}

	public StringProperty getRepairAreaProperty() {
		return repairAreaProperty;
	}

	public void setRepairAreaProperty(StringProperty repairAreaProperty) {
		this.repairAreaProperty = repairAreaProperty;
	}

	public Button getRepairAreaButton() {
		return repairAreaButton;
	}

	public void setRepairAreaButton(Button repairAreaButton) {
		this.repairAreaButton = repairAreaButton;
	}

	public Button getKeyboardButton() {
		return keyboardButton;
	}

	public void setKeyboardButton(Button keyboardButton) {
		this.keyboardButton = keyboardButton;
	}

	@Override
	public ProductController getProductController() {
		return this.productController;
	}	

	public SearchByRepairAreaController getSearchByRepairAreaController() {
		return searchByRepairAreaController;
	}

	@Override
	public TextField getProductIdField() {
		return null;
	}
}
