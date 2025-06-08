package com.honda.galc.client.product.widget;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UpcomingProductWidget extends AbstractWidget{
	
	private TextField expectedProduct;
	
	public UpcomingProductWidget(ProductController productController) {
		super(ViewId.UPCOMING_PRODUCT_WIDGET, productController);
	}
	
	@Override
	protected void initComponents() {
		HBox box = new HBox(10);
		box.setAlignment(Pos.CENTER);
		Label label = UiFactory.createLabel("ProductID", "Product ID", Fonts.SS_DIALOG_BOLD(35));
		box.getChildren().add(label);
		expectedProduct = UiFactory.createTextField("expectedProduct", 20, Fonts.SS_DIALOG_PLAIN(35),TextFieldState.READ_ONLY);
		expectedProduct.setText(getProductController().getModel().getExpectedProductId());
		box.getChildren().add(expectedProduct);
		setCenter(box);
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
		setExpectedProductId(productModel.getExpectedProductId());
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		setExpectedProductId(productModel.getExpectedProductId());
	}
	
	private void setExpectedProductId(String productId) {
		expectedProduct.setText(productId);
	}
}
