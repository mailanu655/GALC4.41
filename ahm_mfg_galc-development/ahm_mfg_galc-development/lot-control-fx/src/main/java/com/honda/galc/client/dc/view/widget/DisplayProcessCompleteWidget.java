package com.honda.galc.client.dc.view.widget;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.UiFactory;

public class DisplayProcessCompleteWidget extends AbstractWidget {

	private int numberOfCompleted = 0;

	public DisplayProcessCompleteWidget(ProductController productController) {
		super(ViewId.DISPLAY_PROCESS_COMPLETE_WIDGET, productController);
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		numberOfCompleted++;
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setTop(getProductionMetrics());

	}

	@Override
	protected void initComponents() {
	}

	private Node getProductionMetrics() {

		VBox vbox = new VBox();
		vbox.setId("black-box");
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setPrefWidth(400);

		Label title = UiFactory.createLabel("title", "Product Completed");
		Label number = UiFactory.createLabel("number", numberOfCompleted + "",Fonts.SS_DIALOG_BOLD(85));
		
		vbox.getChildren().addAll(title,number);

		return vbox;
	}


}
