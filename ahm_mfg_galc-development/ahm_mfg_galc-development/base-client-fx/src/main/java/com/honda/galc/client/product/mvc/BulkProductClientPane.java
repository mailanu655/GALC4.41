package com.honda.galc.client.product.mvc;

import java.awt.event.ActionListener;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.pane.BulkProductInfoPane;
import com.honda.galc.client.product.pane.BulkProductInputPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.property.ProductPropertyBean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

public class BulkProductClientPane extends AbstractProductClientPane{

	ActionListener inputNumberListener;
	private double screenHeight;

	public BulkProductClientPane(MainWindow window,ProductPropertyBean productPropertyBean) {
		super(window);
		this.controller = new BulkProductController(this,productPropertyBean);
		initView();
		mapActions();
	}

	protected void initView() {
		screenHeight = Screen.getPrimary().getBounds().getHeight();
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

	protected BulkProductInputPane createProductInputPane() {
		return (BulkProductInputPane) createPanel(
				controller.getModel().getProperty().getProductInputPane());
	}

	protected BulkProductInfoPane createProductInfoPane() {
		return (BulkProductInfoPane) createPanel(
				controller.getModel().getProperty().getProductInfoPane());
	}

	protected StackPane createTogglePane() {
		StackPane pane = new StackPane();
		pane.getChildren().add(getInputPane());
		pane.getChildren().add(getInfoPane());
		return pane;
	}
	
	@Override
	public void showInputPane() {
		getInfoPane().setPrefHeight(screenHeight * 0.156);
		getInputPane().setPrefHeight(screenHeight * 0.156);
		getInfoPane().setMinHeight(screenHeight * 0.156);
		getInfoPane().setMinHeight(screenHeight * 0.156);
		getInfoPane().setVisible(false);
		getInputPane().setVisible(true);

	}
	
	@Override
	public void showInfoPane() {
		getInfoPane().setPrefHeight(screenHeight * 0.12);
		getInputPane().setPrefHeight(screenHeight * 0.12);
		getInfoPane().setMinHeight(screenHeight * 0.12);
		getInfoPane().setMinHeight(screenHeight * 0.12);
		getInputPane().setVisible(false);
		getInfoPane().setVisible(true);
	}
	
	private void mapActions() {
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
