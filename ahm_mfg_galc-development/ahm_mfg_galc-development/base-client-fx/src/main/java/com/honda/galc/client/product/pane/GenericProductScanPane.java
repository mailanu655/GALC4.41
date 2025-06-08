package com.honda.galc.client.product.pane;

import java.util.List;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.product.entry.AbstractProductEntryPane;
import com.honda.galc.client.product.entry.GenericProductScanPaneController;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductNumberDef;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class GenericProductScanPane extends AbstractGenericEntryPane {
	private static final String SCAN_PRODUCTS = "Scan Products";

	private LoggedButton productIdButton;
	private LoggedButton keyboardButton;

	private LoggedTextField productIdTextField;

	private GenericProductScanPaneController controller;
	private StringProperty productIdProperty;

	public static final boolean ENABLED = true;

	public GenericProductScanPane(AbstractProductEntryPane parentView) {
		super(PaneId.getPaneId(PaneId.PRODUCT_SCAN_PANE.name()));	
		controller = new GenericProductScanPaneController(this, parentView);
		initView();
		controller.initEventHandlers();
		getProductIdField().textProperty().bindBidirectional(productIdProperty());
	}

	private void initView() {
		createProductTypeField();
		createKeyboardButtonAndPopup();
		this.setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0");
	}

	private void createKeyboardButtonAndPopup() {
		if (controller.getProductPropertyBean().isKeyboardButtonVisible()) {
			keyboardButton = createButton("KEYBOARD", true, ClientConstants.KEYBOARD_BUTTON_ID);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					controller.createKeyBoardPopup();
				}
			});
			this.getChildren().add(keyboardButton);
		}
	}

	private void createProductTypeField() {
		final String labelText = controller.getProductTypeData().getProductIdLabel();

		if (controller.getProductPropertyBean().isManualProductEntryEnabled()) {
			productIdButton = createButton(labelText, true, ClientConstants.PRODUCT_ID_BUTTON_ID);
			getChildren().add(productIdButton);
		} else {
			LoggedLabel label = UiFactory.createLabel("productIdLabel", labelText);
			label.styleProperty().bind(textStyle);
			label.prefHeightProperty().bind(this.heightProperty().multiply(0.40));

			this.getChildren().add(label);
		}
		this.getChildren().add(getProductIdField());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getProductIdField().requestFocus();
			}
		});		
	}

	public StringProperty productIdProperty() {
		if (this.productIdProperty == null) {
			this.productIdProperty = new SimpleStringProperty(this, ClientConstants.PRODUCT_ID_TEXT_FIELD_ID);
		}
		return this.productIdProperty;
	}

	public TextField getProductIdField() {
		if (this.productIdTextField == null) {
			this.productIdTextField = UiFactory.createTextField(ClientConstants.PRODUCT_ID_TEXT_FIELD_ID, getProductIdFieldLength(),
					null, TextFieldState.EDIT, Pos.BASELINE_LEFT, true);

			productIdTextField.prefHeightProperty().bind(this.heightProperty().multiply(0.40));
			productIdTextField.prefWidthProperty().bind(this.widthProperty().multiply(0.35));
			productIdTextField.styleProperty().bind(textStyle);
			productIdTextField.maxWidthProperty().bind(this.widthProperty().multiply(0.30));
			this.productIdTextField.setFocusTraversable(true);
		}
		return this.productIdTextField;
	}

	protected int getProductIdFieldLength() {
		List<ProductNumberDef> list = controller.getProductTypeData().getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		return length;
	}

	public LoggedButton getProductIdButton() {
		return this.productIdButton;
	}

	public LoggedButton getKeyboardButton() {
		return this.keyboardButton;
	}

	public LoggedTextField getQuantityTextField() {
		return null;
	}

	public void setProductId(String productId) { 
		productIdProperty().set(productId); 
	}
	public String getProductId() {
		return productIdProperty().get(); 
	}

	@Override
	public String getPaneLabel() {
		return SCAN_PRODUCTS;
	}
}
