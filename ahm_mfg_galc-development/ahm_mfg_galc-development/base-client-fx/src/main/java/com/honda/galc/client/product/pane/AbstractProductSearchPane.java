package com.honda.galc.client.product.pane;

import java.util.List;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductNumberDef;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Screen;

public abstract class AbstractProductSearchPane extends AbstractProductInputPane {
	private double screenWidth;
	private double screenHeight;
	
	private TextField productIdTextField;
	private TextField expectedProductIdField;
	
	protected int parentPanelHeight;

	AbstractProductSearchPane() {
		super();
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		this.setMinHeight(screenHeight * 0.05);
		this.setMinWidth(screenWidth * 0.99);
		this.setPrefWidth(screenWidth * 0.99);
		this.setPrefHeight(screenHeight * 0.07);
	}
	
	AbstractProductSearchPane(ProductController productController) {
		super();
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		this.setProductController(productController);
		this.setMinHeight(screenHeight * 0.05);
		this.setMinWidth(screenWidth * 0.99);
		this.setPrefWidth(screenWidth * 0.99);
		this.setPrefHeight(screenHeight * 0.07);
		
		getProductIdField().textProperty().bindBidirectional(productIdProperty());
	}
	
	public String getLabelStyle() {
		return String.format("-fx-font-family : Dialog; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int) (0.16 * parentPanelHeight));
	}
	
	public String getTextStyle() {
		return String.format("-fx-font-family : Dialog; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int) (0.18 * parentPanelHeight));
	}
	
	public String getTextFieldTextStyle() {
		return String.format("-fx-font-family : Dialog; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int) (0.23 * parentPanelHeight));
	}
	
	public String getButtonStyle() {
		return String.format("-fx-font-family : Dialog; fx-padding: 0 10px 0 0;-fx-font-size: %dpx;", (int) ( 0.25 * parentPanelHeight));
	}

	public String getLabelStyleSmall() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int) (0.14 * parentPanelHeight));
	}

	@Override
	public TextField getProductIdField() {
		if (this.productIdTextField == null) {
			this.productIdTextField = UiFactory.createTextField("productIdTextField", getProductIdFieldLength(),
					getLabelStyle(), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
			
			this.productIdTextField.setMaxWidth(getEntryWidth());
			this.productIdTextField.setFocusTraversable(true);
		}
		return this.productIdTextField;
	}
	
	public void setProductIdField(TextField productIdTextField) {
		this.productIdTextField = productIdTextField;
	}

	@Override
	public TextInputControl getExpectedProductIdField() {
		return expectedProductIdField;
	}
	
	public void setExpectedProductIdField(TextField expectedProductIdField) {
		this.expectedProductIdField = expectedProductIdField;
	}

	@Override
	public void setProductSequence() {
	}

	@Override
	public LoggedTextField getQuantityTextField() {
		return null;
	}
	
	protected int getProductIdFieldLength() {
		List<ProductNumberDef> list = getProductController().getProductTypeData().getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		return length;
	}
	
	public double getDateTimePickerWidth() {
		return getScreenWidth() * 0.12;
	}
	
	public double getLabelWidth() {
		return getScreenWidth() * 0.08;
	}
	
	public double getComboBoxHeight() {
		return parentPanelHeight * 0.34;
	}
	
	public double getComboBoxWidth() {
		return getScreenWidth() * 0.115;
	}
	
	public double getEntryWidth() {
		return getScreenWidth() * 0.20;
	}
	
	public double getLabelHeight() {
		return parentPanelHeight * 0.40;
	}
	
	public double getScreenWidth( ) {
		return this.screenWidth;
	}

	public double getScreenHeight() {
		return this.screenHeight;
	}
	
	public abstract void associateSelected();
}
