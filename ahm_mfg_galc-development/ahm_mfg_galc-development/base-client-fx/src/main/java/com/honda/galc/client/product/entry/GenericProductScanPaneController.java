package com.honda.galc.client.product.entry;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.product.pane.GenericProductScanPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProcessCompleteEvent;
import com.honda.galc.client.ui.event.ProductInvalidEvent;
import com.honda.galc.client.ui.event.ProductResultEvent;
import com.honda.galc.client.ui.event.ProductValidEvent;
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
import javafx.scene.text.Font;
import javafx.stage.Screen;

public class GenericProductScanPaneController implements EventHandler<ActionEvent>{

	private ApplicationContext applicationContext;

	private GenericProductScanPane view;
	private AbstractProductEntryPane parentView;

	protected KeyBoardPopup keyboardPopup;

	private boolean isKeyBoardVisiable;

	private ProductPropertyBean productPropertyBean;

	public GenericProductScanPaneController(GenericProductScanPane view, AbstractProductEntryPane parentView) {
		this.view = view;
		this.parentView = parentView;
		this.applicationContext = parentView.getApplicationContext();
		EventBusUtil.register(this);
	}

	public void initEventHandlers() {
		if (getProductPropertyBean().isKeyboardButtonVisible()) view.getKeyboardButton().setOnAction(this);
		if (getProductPropertyBean().isManualProductEntryEnabled()) view.getProductIdButton().setOnAction(this);
		view.getProductIdField().setOnAction(this);
	}

	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (ClientConstants.KEYBOARD_BUTTON_ID.equalsIgnoreCase(loggedButton.getId())) toggleKeyBoard();
			else if(ClientConstants.PRODUCT_ID_BUTTON_ID.equalsIgnoreCase(loggedButton.getId())) openManualProductEntryDialog(actionEvent);
		} else if(actionEvent.getSource() instanceof LoggedTextField) {
			LoggedTextField loggedTextField = (LoggedTextField) actionEvent.getSource();
			if(ClientConstants.PRODUCT_ID_TEXT_FIELD_ID.equalsIgnoreCase(loggedTextField.getId())) productIdTextFieldEvent(actionEvent);
		}
	}

	private void productIdTextFieldEvent(ActionEvent event) {
		EventBusUtil.publish(new ProductEvent(view.getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
	}

	private void openManualProductEntryDialog(ActionEvent event) {
		setKeyboardPopUpVisible(false);
		ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
				"Manual Product Entry Dialog", getProductTypeData(),applicationContext.getApplicationId(), view.getProductIdField().getText());
		Logger.getLogger().check("Manual Product Entry Dialog Box populated");
		manualProductEntryDialog.showDialog();
		String productId = manualProductEntryDialog.getResultProductId();
		Logger.getLogger().check("Product Id : " + productId + " selected");
		if (!(productId == null || StringUtils.isEmpty(productId))) {
			parentView.getMainWindow().setWaitCursor();
			view.setProductId(productId);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (getProductPropertyBean().isAutoEnteredManualProductInput()) {
						view.getProductIdField().fireEvent(event);
						Logger.getLogger().check("Selected Product Id Auto Entered");
					}
					parentView.getMainWindow().setDefaultCursor();
				}
			});
		}		
	}

	public void createKeyBoardPopup() {
		Scene primaryScene = parentView.getMainWindow().getScene();
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

	@Subscribe
	public void onProductResultEvent(ProductResultEvent event) {
		if(event.getPaneId().equals(PaneId.PRODUCT_SCAN_PANE)) {
			if(event instanceof ProductInvalidEvent) {
				setErrorState();
			} else if(event instanceof ProductValidEvent) {
				clearProductIdTextField();
			} else if(event instanceof ProcessCompleteEvent) {
				clearProductIdTextField();
			}
		} else {
			clearProductIdTextField();
		}
	}

	public ProductPropertyBean getProductPropertyBean() {
		if(productPropertyBean == null) {
			productPropertyBean = PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}
		return productPropertyBean;
	}

	private void toggleKeyBoard() {
		setKeyboardPopUpVisible(!isKeyboardPopVisible());	
		isKeyBoardVisiable = (!isKeyBoardVisiable);
	}

	private void setKeyboardPopUpVisible(boolean isVisible) {
		CommonUtil.setPopupVisible(isVisible, parentView.getMainWindow(), keyboardPopup);
	}	

	public void setErrorState() {
		TextFieldState.ERROR.setState(view.getProductIdField());
		view.getProductIdField().selectAll();
	}

	public void clearProductIdTextField() {
		TextFieldState.EDIT.setState(view.getProductIdField());

		view.getProductIdField().clear();
	}

	private boolean isKeyboardPopVisible() {
		return isKeyBoardVisiable;
	}

	public ProductTypeData getProductTypeData() {
		return parentView.getMainWindow().getApplicationContext().getProductTypeData();
	}

	public AbstractProductEntryPane getParentView() {
		return this.parentView;
	}
}
