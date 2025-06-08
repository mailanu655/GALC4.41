package com.honda.galc.client.product.entry;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.product.pane.GenericSearchByDunnagePane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ManualDunnageEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProcessCompleteEvent;
import com.honda.galc.client.ui.event.ProductInvalidEvent;
import com.honda.galc.client.ui.event.ProductResultEvent;
import com.honda.galc.client.ui.event.ProductValidEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Screen;

public class GenericSearchByDunnageController implements EventHandler<ActionEvent> {
	private GenericSearchByDunnagePane view;
	private SearchByDunnageModel model;
	private ApplicationContext context;
	private boolean isKeyBoardVisiable;
	protected KeyBoardPopup keyboardPopup;
	private ProductPropertyBean productPropertyBean;



	public GenericSearchByDunnageController() {}

	public GenericSearchByDunnageController(GenericSearchByDunnagePane view, ApplicationContext context) {
		this.view = view;
		this.context = context;
		model = new SearchByDunnageModel(context);
		EventBusUtil.register(this);
	}

	public List<String> getProductsByDunnage(String dunnage) {
		List<String> contents = null;
		contents = model.getProductsByDunnage(dunnage);

		if (contents == null || contents.isEmpty()) {
			setErrorMessage("No Products found in Dunnage: " + dunnage, view.getDunnageField());
		}
		return contents;
	}

	public void initEventHandlers() {
		view.getKeyboardButton().setOnAction(this);
		view.getDunnageButton().setOnAction(this);
		view.getDunnageTextField().setOnAction(this);
	}

	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (ClientConstants.KEYBOARD_BUTTON_ID.equalsIgnoreCase(loggedButton.getId())) toggleKeyBoard();
			else if(ClientConstants.DUNNAGE_BUTTON_ID.equalsIgnoreCase(loggedButton.getId())) openManualDunnageEntryDialog(actionEvent);
		} else if(actionEvent.getSource() instanceof LoggedTextField) {
			LoggedTextField loggedTextField = (LoggedTextField) actionEvent.getSource();
			if(ClientConstants.DUNNAGE_TEXT_FIELD_ID.equalsIgnoreCase(loggedTextField.getId())) dunnageTextfieldEvent();
		}
	}

	public void createKeyBoardPopup() {
		Scene primaryScene = view.getMainWindow().getScene();
		primaryScene.getStylesheets().add(this.getClass().getResource(ClientConstants.KEYBOARD_CSS_PATH).toExternalForm());
		String fontUrl = this.getClass().getResource(ClientConstants.KEYBOARD_FONT_URL).toExternalForm();
		Font.loadFont(fontUrl, -1);
		keyboardPopup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.6).initLocale(Locale.ENGLISH).addIRobot(RobotFactory.createFXRobot()).build();
		keyboardPopup.setX(Screen.getPrimary().getVisualBounds().getWidth()/2);
		keyboardPopup.setY(Screen.getPrimary().getVisualBounds().getHeight());
		keyboardPopup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				CommonUtil.setPopupVisible(false, null,keyboardPopup);
			}
		});
		keyboardPopup.setOwner(primaryScene);
	}

	private void dunnageTextfieldEvent() {
		List<String> products = getProductsByDunnage(view.getDunnage());
		if(!products.isEmpty()) {
			EventBusUtil.publish(new ProductEvent(products, ProductEventType.PRODUCT_INPUT_RECIEVED));
		}		
	}

	private void openManualDunnageEntryDialog(ActionEvent actionEvent) {
		setKeyboardPopUpVisible(false);
		ManualDunnageEntryDialog manualDunnageEntryDialog = new ManualDunnageEntryDialog(
				"Manual Dunnage Entry Dialog", getProductTypeData(),view.getMainWindow().getApplicationContext().getApplicationId());
		Logger.getLogger().check("Manual Dunnage Entry Dialog Box populated");
		manualDunnageEntryDialog.showDialog();
		String dunnage = manualDunnageEntryDialog.getResultDunnage();
		Logger.getLogger().check("Dunnage: " + dunnage + " selected");
		if (!(dunnage == null || StringUtils.isEmpty(dunnage))) {
			view.getMainWindow().setWaitCursor();

			view.setDunnage(dunnage);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (getProductPropertyBean().isAutoEnteredManualProductInput()) {
						view.getDunnageField().fireEvent(actionEvent);
						Logger.getLogger().check("Selected Dunnage Auto Entered");
					}
					view.getMainWindow().setDefaultCursor();
				}

			});
		}
	}

	public ProductPropertyBean getProductPropertyBean() {
		if(productPropertyBean == null) {
			productPropertyBean = PropertyService.getPropertyBean(ProductPropertyBean.class, view.getMainWindow().getApplicationContext().getProcessPointId());
		}
		return productPropertyBean;
	}

	@Subscribe
	public void onProductResultEvent(ProductResultEvent event) {
		if(event.getPaneId() == null) return;
		if(event.getPaneId().equals(PaneId.SEARCH_BY_DUNNAGE_PANE)) {
			if(event instanceof ProductValidEvent) {
				view.getDunnageField().setText(null);
				view.setTestFieldState(view.getDunnageTextField(), TextFieldState.EDIT);
			} else if(event instanceof ProductInvalidEvent) {
				TextFieldState.ERROR.setState(view.getDunnageTextField());
				view.getDunnageTextField().selectAll();
			} else if(event instanceof ProcessCompleteEvent) {
				view.getDunnageTextField().setText(null);
				TextFieldState.EDIT.setState(view.getDunnageTextField());
				view.getDunnageTextField().requestFocus();
			}
		} else {
			view.getDunnageTextField().setText(null);
			TextFieldState.EDIT.setState(view.getDunnageTextField());
		}
	}

	@Subscribe
	public void onProductEvent(ProductEvent event) {
		if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
			view.getDunnageField().setText(null);
			view.setTestFieldState(view.getDunnageField(), TextFieldState.EDIT);
		} 
	}

	private void toggleKeyBoard() {
		setKeyboardPopUpVisible(!isKeyboardPopVisible());	
		isKeyBoardVisiable = (!isKeyBoardVisiable);
	}

	public void setErrorMessage(String errorMessage, TextField textField) {
		setErrorMessage(errorMessage);
		TextFieldState.ERROR.setState(textField);
		textField.selectAll();
	}

	public void setTestFieldState(TextField textField, TextFieldState state) {
		state.setState(textField);
	}

	protected void setErrorMessage(String errorMessage) {
		getLogger().warn(errorMessage);
		EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.ERROR));
	}

	public void setKeyboardPopUpVisible(boolean isVisible) {
		CommonUtil.setPopupVisible(isVisible, view.getMainWindow(), keyboardPopup);
	}

	public boolean isManualProductEntryEnabled() {
		return model.isManualProductEntryEnabled();
	}

	private boolean isKeyboardPopVisible() {
		return isKeyBoardVisiable;
	}

	public boolean isKeyboardButtonVisible() {
		return model.isKeyboardButtonVisible();
	}

	public ProductTypeData getProductTypeData() {
		return view.getMainWindow().getApplicationContext().getProductTypeData();
	}

	public Logger getLogger() {
		return view.getMainWindow().getLogger();
	}
}
