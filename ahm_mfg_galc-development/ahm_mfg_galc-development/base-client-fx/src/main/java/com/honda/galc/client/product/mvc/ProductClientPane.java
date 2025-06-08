package com.honda.galc.client.product.mvc;

import java.awt.event.ActionListener;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.pane.AbstractProductInputPane;
import com.honda.galc.client.product.pane.ProductInfoPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.property.ProductPropertyBean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * @author Karol Wozniak
 */
public class ProductClientPane extends AbstractProductClientPane {
		
	private ActionListener inputNumberListener;

	// === model === //
	public ProductClientPane(MainWindow window,ProductPropertyBean productPropertyBean) {
		super(window);
		this.controller = new ProductController(this,productPropertyBean);
		initView();
		mapActions();
	}

	// === life cycle ===//
	protected void initView() {
		this.inputPane = createProductInputPane();
		this.infoPane = createProductInfoPane();
		this.togglePane = createTogglePane();
		this.productProcessPane = createProductProcessPane();
		this.productWidgetPane = createProductWidgetPane();
		this.productIdlePaneContainer = createProductIdlePaneContainer();
		
		this.processPane = createProcessPane();
		this.toggleProcessPane = createToggleProcessPane();
		
		setId("ProductPanel");
		setTop(getTogglePane());
		setCenter(toggleProcessPane);
		showInputPane();
		showIdlePane();
	}
	
	protected AbstractProductInputPane createProductInputPane() {
		return (AbstractProductInputPane) createPanel(
				controller.getModel().getProperty().getProductInputPane());
	}
	
	protected ProductInfoPane createProductInfoPane() {
		return (ProductInfoPane) createPanel(
				controller.getModel().getProperty().getProductInfoPane());
	}
	
	// === factory === //
	protected StackPane createTogglePane() {
		StackPane pane = new StackPane();
		pane.getChildren().add(getInputPane());
		pane.getChildren().add(getInfoPane());
		return pane;
	}
	
	protected TabPane createTabbedPane() {
		TabPane pane = new TabPane();
		pane.setMinSize(200, 200);
		pane.requestFocus();
		return pane;
	}
	
	protected void mapActions() {
		getInputPane().getProductIdField().setOnAction(new EventHandler<ActionEvent>(){
		   @Override public void handle(ActionEvent e) {
			   EventBusUtil.publish(new ProductEvent(getInputPane().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
			}
		});
	}
	
	public ActionListener getInputNumberListener() {
		return inputNumberListener;
	}
}
