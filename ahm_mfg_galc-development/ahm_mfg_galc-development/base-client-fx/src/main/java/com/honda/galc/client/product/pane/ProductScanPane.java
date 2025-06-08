package com.honda.galc.client.product.pane;

import com.honda.galc.client.product.action.KeyboardAction;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.utils.UiFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ProductScanPane extends AbstractProductSearchPane {

	private Button productIdButton;

	private Button keyboardButton;


	public ProductScanPane(ProductController productController) {
		super();
		this.productController = productController;
		parentPanelHeight = productController.getInputPaneHeight();
		initView();
		getProductIdField().textProperty().bindBidirectional(productIdProperty());

	}	

	private void initView() {
		String label = getProductController().getProductTypeData().getProductIdLabel();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getProductIdField().requestFocus();
			}
		});		

		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {
			productIdButton = UiFactory.createButton(label,getButtonStyle(), true);
			getChildren().add(productIdButton);
		} else {
			add(UiFactory.createLabel("productIdLabel", label, getLabelStyle()));
		}

		getChildren().add(getProductIdField());

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
	

	@Override
	public void associateSelected() {
		if(getProductIdField().getText() != null && !getProductIdField().getText().isEmpty()) {
			getProductIdField().fireEvent(new ActionEvent());
		}
		getProductIdField().requestFocus();		
	}
	
	public void setProductIdTextFieldStyle(String style) {
		getProductIdField().setStyle(style);
	}
	
	public void setStyle(int parentPaneHeight) {
		this.parentPanelHeight = parentPaneHeight;
		getProductIdField().setStyle(getTextFieldTextStyle());
		getProductIdButton().setStyle(getButtonStyle());
		getKeyboardButton().setStyle(getButtonStyle());
		getProductIdField().setPrefHeight(getLabelHeight());
	}

	public Button getProductIdButton() {
		return productIdButton;
	}

	public void setProductIdButton(Button productIdButton) {
		this.productIdButton = productIdButton;
	}

	public Button getKeyboardButton() {
		return keyboardButton;
	}

	public void setKeyboardButton(Button keyboardButton) {
		this.keyboardButton = keyboardButton;
	}

	public void setVisible() {
		this.setVisible(true);
	}
}
