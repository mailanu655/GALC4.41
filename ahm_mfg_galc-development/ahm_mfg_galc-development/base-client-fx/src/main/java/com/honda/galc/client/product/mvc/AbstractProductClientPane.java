package com.honda.galc.client.product.mvc;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.pane.AbstractProcessInfoDialog;
import com.honda.galc.client.product.pane.AbstractProductInputPane;
import com.honda.galc.client.product.pane.ProductIdlePaneContainer;
import com.honda.galc.client.product.pane.ProductInfoPane;
import com.honda.galc.client.product.pane.ProductProcessPane;
import com.honda.galc.client.product.pane.ProductWidgetPane;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.ui.menu.MainMenuDialog;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.util.ReflectionUtils;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public abstract class AbstractProductClientPane extends ApplicationMainPane{
	protected ProductController controller;
	
	protected StackPane togglePane;
	protected AbstractProductInputPane inputPane;
	protected ProductInfoPane infoPane;
	protected StackPane toggleProcessPane;
	protected SplitPane processPane;
	protected ProductIdlePaneContainer productIdlePaneContainer;
	protected ProductProcessPane productProcessPane;
	protected ProductWidgetPane productWidgetPane;

	public AbstractProductClientPane(MainWindow window) {
		super(window);
	}

	protected void showProcessPane() {
		toggleProcessPane.getChildren().remove(productIdlePaneContainer);
		if(!toggleProcessPane.getChildren().contains(processPane))
			toggleProcessPane.getChildren().add(processPane);
	}

	protected void showInfoPane() {
		getInputPane().setVisible(false);
		getInfoPane().setVisible(true);
	}

	public AbstractProductInputPane getInputPane() {
		return inputPane;
	}

	public ProductInfoPane getInfoPane() {
		return infoPane;
	}

	public StackPane getTogglePane() {
		return togglePane;
	}

	public ProductController getController() {
		return controller;
	}
	
	@Override
	public void clearErrorMessage() {
		super.clearErrorMessage();
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}

	public void setErrorMessage(String errorMessage, TextField textField) {
		setErrorMessage(errorMessage);
		TextFieldState.ERROR.setState(textField);
		textField.selectAll();
	}
	
	public void setTestFieldState(TextField textField, TextFieldState state) {
		state.setState(textField);
	}

	public ProductProcessPane getProductProcessPane() {
		return productProcessPane;
	}

	public void setProductButtons(ProductActionId[] productActionIds) {
		setProductButtons(productActionIds, true);
	}

	public void setProductButtons(ProductActionId[] productActionIds, boolean canTakeFocus) {
		getInfoPane().setProductButtons(productActionIds, canTakeFocus);
	}

	public ProductIdlePaneContainer getProductIdlePaneContainer() {
		return productIdlePaneContainer;
	}

	public void setProductIdlePaneContainer(ProductIdlePaneContainer productIdlePaneContainer) {
		this.productIdlePaneContainer = productIdlePaneContainer;
	}

	public void showIdlePane() {
		toggleProcessPane.getChildren().remove(processPane);
		if(!toggleProcessPane.getChildren().contains(productIdlePaneContainer))
			toggleProcessPane.getChildren().add(productIdlePaneContainer);
	}

	public void showInputPane() {
		getInputPane().setVisible(true);
		getInfoPane().setVisible(false);
	}

	@Override
	public void setMessage(String message) {
		super.setMessage(message);
	}
	
	@Override
	public void setMessage(String message,Color color) {
		super.setMessage(message, color);
	}
	
	@Override
	public void setStatusMessage(String message) {
		super.setMessage(message);
	}

	public void openMainMenuDialog() {
		String mainMenuDialog = getController().getModel().getProperty().getMainMenuDialog();
		if(StringUtils.isEmpty(mainMenuDialog)) return;
		MainMenuDialog dialog = (MainMenuDialog) createProcessInfoDialog(mainMenuDialog);
		EventBusUtil.publish(new ProgressEvent(0,"Hide"));
		if(dialog != null && dialog.showMainMenu()) {
			dialog.showDialog();
		}
	}

	protected AbstractProcessInfoDialog createProcessInfoDialog(String dialogName) {
		Class<?> clazz;
		try {
			clazz = Class.forName(dialogName);
		} catch (ClassNotFoundException e) {
			//overwritten dialog name "false" is not a valid class but meaning don't show the dialog
			//in this case, don't show/log error message
			if (!StringUtils.isBlank(dialogName) && !dialogName.trim().equalsIgnoreCase("false")) { 
				setErrorMessage("class does not exist : " + dialogName);
			}
			return null;
		}
		if(AbstractProcessInfoDialog.class.isAssignableFrom(clazz))
			return (AbstractProcessInfoDialog)ReflectionUtils.createInstance(clazz,getController());
		else {
			setErrorMessage("class is not a process info dialog");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected Object createPanel(String className) {
		Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			getLogger().error("Class " + className + " not found");
			return null;
		}
		return ReflectionUtils.createInstance(clazz, new Object[] {controller});
	}

	protected ProductProcessPane createProductProcessPane() {
		return new ProductProcessPane(controller);
	}

	protected ProductWidgetPane createProductWidgetPane() {
		return new ProductWidgetPane(controller);
	}

	protected ProductIdlePaneContainer createProductIdlePaneContainer() {
		return new ProductIdlePaneContainer(controller);
	}

	protected SplitPane createProcessPane() {
		SplitPane processPane = new SplitPane();
		processPane.setOrientation(Orientation.VERTICAL);
		processPane.getItems().add(productProcessPane);
		if(productWidgetPane.hasWidgets() && controller.getModel().getProperty().isProductWidgetDisplayed()){
			processPane.getItems().add(productWidgetPane);
			processPane.setDividerPositions(0.76,0.24);
		}
	
		return processPane;
	}

	public void openProcessInfoDialog() {
		String processInfoDialog = getController().getModel().getProperty().getProcessInfoDialog();
		if(StringUtils.isEmpty(processInfoDialog)) return;
		AbstractProcessInfoDialog dialog = createProcessInfoDialog(processInfoDialog);
		EventBusUtil.publish(new ProgressEvent(0,"Hide"));
		if(dialog != null ) {
			dialog.showDialog();
		}
	}

	public ProductTypeData getProductTypeData() {
		return getMainWindow().getApplicationContext().getProductTypeData();
	}
	
	protected StackPane createToggleProcessPane() {
		return new StackPane();
	}
}
