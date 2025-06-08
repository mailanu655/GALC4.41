package com.honda.galc.client.product.pane;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.LoggedTextField;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>HomeScreenForUpcInputPane</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Shweta Kadav
 */
public abstract class AbstractProductInputPane extends MigPane {
	protected ProductController productController;

	private StringProperty productIdProperty;
	protected TabPane productInputTabPane;
	
	public AbstractProductInputPane() {
		super("", "[][grow,fill]");

	}

	public AbstractProductInputPane(ProductController productController) {
		super("", "[][grow,fill]");
		this.productController = productController;

		getProductIdField().textProperty().bindBidirectional(productIdProperty());
	}

	protected ProductController getProductController() {
		return productController;
	}

	protected void setProductController(ProductController productController) {
		this.productController = productController;
	}

	public void setProductId(String value) { productIdProperty().set(value); }
	public String getProductId() { return productIdProperty().get(); }
	public StringProperty productIdProperty() {
		if (this.productIdProperty == null) {
			this.productIdProperty = new SimpleStringProperty(this, "checkInTime");
		}
		return this.productIdProperty;
	}
	
	public TabPane getProductInputTabPane() {
		return this.productInputTabPane;
	}
	
	/**
	 * Get the product id text field.<br>
	 * Implementations of this method must follow the lazy initialization pattern.
	 */
	public abstract TextField getProductIdField();

	public abstract TextInputControl getExpectedProductIdField();

	public abstract void setProductSequence();

	public abstract LoggedTextField getQuantityTextField();
}
