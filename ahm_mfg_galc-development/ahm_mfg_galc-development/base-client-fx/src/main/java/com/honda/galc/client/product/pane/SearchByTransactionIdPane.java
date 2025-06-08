package com.honda.galc.client.product.pane;

import com.honda.galc.client.product.action.KeyboardAction;
import com.honda.galc.client.product.entry.SearchByTransactionIdController;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.product.mvc.ProductController;
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
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

public class SearchByTransactionIdPane extends AbstractProductSearchPane {
	private static final String TRANSACTION_ID = "Transaction Id";
	private static final String OUTSTANDING_CHECK_LABEL = "Show Only NOT FIXED Transactions";

	private SearchByTransactionIdController searchByTransactionIdController;

	private TextField transactionIdTextField;

	private StringProperty transactionIdProperty;

	private Button transactionIdButton;
	private Button keyboardButton;
	private CheckBox outstandingOnlyCheckBox;

	public SearchByTransactionIdPane(BulkProductController productController) {
		super();
		this.productController = productController;
		parentPanelHeight = productController.getInputPaneHeight();
		initView();
		transactionIdTextField.textProperty().bindBidirectional(transactionIdProperty());
		searchByTransactionIdController = new SearchByTransactionIdController(this, getProductController().getView().getMainWindow().getApplicationContext());
	}

	private void initView() {
		GridPane InputSelectionPane = new GridPane();
		InputSelectionPane.setHgap(10);
		InputSelectionPane.setVgap(0.10 * parentPanelHeight);
			
		InputSelectionPane.add(getOutstandingOnlyCheckBox(),0,0,4,1);
		
		String label = TRANSACTION_ID;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getTransactionIdTextField().requestFocus();
			}
		});
		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {
			transactionIdButton = UiFactory.createButton(label, getButtonStyle(), true);
			InputSelectionPane.add(transactionIdButton,0,1,1,1);
		} else {
			InputSelectionPane.add(UiFactory.createLabel("transactionIdLabel", label,getLabelStyle()),0,1,1,1);
		}
		InputSelectionPane.add(getTransactionIdField(),1,1,1,1);
		getChildren().add(InputSelectionPane);
		
		if (getProductController().getModel().getProperty().isKeyboardButtonVisible()) {
			keyboardButton = UiFactory.createButton("KEYBOARD", getButtonStyle(), true);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					keyboardButton.setOnAction(new KeyboardAction(getProductController()));
				}
			});
			getChildren().add(keyboardButton);
		}
		
	}

	public TextField getTransactionIdField() {
		if (transactionIdTextField == null) {
			transactionIdTextField = UiFactory.createTextField("transactionIdTextField", 20,
					getTextFieldTextStyle(), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
			transactionIdTextField.setMaxWidth(getEntryWidth());
			transactionIdTextField.setPrefHeight(getLabelHeight());
			transactionIdTextField.setFocusTraversable(true);
		}
		return transactionIdTextField;
	}
	
	public CheckBox getOutstandingOnlyCheckBox() {
		if (outstandingOnlyCheckBox == null) {
			outstandingOnlyCheckBox = new CheckBox(OUTSTANDING_CHECK_LABEL);
			outstandingOnlyCheckBox.setSelected(true);
		}
		return outstandingOnlyCheckBox;
	}

	public StringProperty transactionIdProperty() {
		if (this.transactionIdProperty== null) {
			this.transactionIdProperty = new SimpleStringProperty(this, "transactionId");
		}
		return this.transactionIdProperty;
	}

	@Override
	public void associateSelected() {
		if(transactionIdTextField.getText() != null && !transactionIdTextField.getText().isEmpty()) {
			transactionIdTextField.fireEvent(new ActionEvent());
		}
		transactionIdTextField.requestFocus();
	}

	public void setTransactionId(String value) {
		transactionIdProperty().set(value);
	}

	public void setTestFieldState(TextField textField, TextFieldState state) {
		state.setState(textField);
	}

	public SearchByTransactionIdController getSearchByTransactionIdController() {
		return searchByTransactionIdController;
	}

	public TextField getTransactionIdTextField() {
		return transactionIdTextField;
	}

	public StringProperty getTransactionIdProperty() {
		return transactionIdProperty;
	}

	public String getTransactionId() {
		return getTransactionIdProperty().get();
	}

	public Button getTransactionIdButton() {
		return transactionIdButton;
	}

	public Button getKeyboardButton() {
		return keyboardButton;
	}

	@Override
	public ProductController getProductController() {
		return productController;
	}

	public void setProductController(ProductController productController) {
		this.productController = productController;
	}

	public void setErrorMessage(String errorMessage, TextField textField) {
		getProductController().getView().setErrorMessage(errorMessage, (TextField) textField);		
	}

	@Override
	public TextField getProductIdField() {
		return null;
	}

	@Override
	public TextInputControl getExpectedProductIdField() {
		return null;
	}

	@Override
	public void setProductSequence() {	
	}

	@Override
	public LoggedTextField getQuantityTextField() {
		return null;
	}
}
