package com.honda.galc.client.product.entry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.product.pane.GenericSearchByProcessPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.event.SelectionEventType;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;

public class GenericSearchByProcessPaneController implements EventHandler<ActionEvent>{
	private ApplicationContext applicationContext;

	private GenericSearchByProcessPane view;
	private AbstractProductEntryPane parentView;
	private SearchByProcessModel model;

	private ProductPropertyBean productPropertyBean;

	protected KeyBoardPopup keyboardPopup;

	private boolean isKeyBoardVisiable;

	public GenericSearchByProcessPaneController(GenericSearchByProcessPane view, AbstractProductEntryPane parentView) {
		this.view = view;
		this.parentView = parentView;
		this.applicationContext = parentView.getApplicationContext();
		this.model = new SearchByProcessModel(applicationContext);
		EventBusUtil.register(this);
	}

	public void initEventHandlers() {
		view.getKeyboardButton().setOnAction(this);
		view.getSearchButton().setOnAction(this);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (ClientConstants.KEYBOARD_BUTTON_ID.equalsIgnoreCase(loggedButton.getId())) toggleKeyBoard();
			else if(ClientConstants.PRODUCT_SEARCH_BUTTON_ID.equalsIgnoreCase(loggedButton.getId())) searchForProducts();
		}	
	}

	@Subscribe()
	public void onSelectionEvent(SelectionEvent event) {
		if(event == null) return;
		if(event.getSource() instanceof PaneId) {
			if(PaneId.SEARCH_BY_PROCESS_PANE.equals(event.getSource())) {
				if(SelectionEventType.PROCESS_POINT.equals(event.getEventType())) {
					view.getSearchButton().setDisable(false);
				} else if(SelectionEventType.DIVISION.equals(event.getEventType()) ||
						SelectionEventType.LINE.equals(event.getEventType())) {
					view.getSearchButton().setDisable(true);
				}
			}
		}
	}

	private void searchForProducts() {
		List<ProductHistory> productHistory;
		Timestamp startTime = Timestamp.valueOf(view.getStartDatePicker().getDateTimeValue());
		Timestamp endTime = Timestamp.valueOf(view.getEndDatePicker().getDateTimeValue());
		if(validateTimeRange(startTime, endTime)) {
			String processPointId = view.getProcessPointPane().getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem().getProcessPointId();
			if(view.getProcessPointPane().getMachineComboBox().getControl().getSelectionModel().getSelectedIndex() == -1) {
				productHistory = model.getProductHistoyByDateRangeAndProcessPoint(processPointId, startTime, endTime);
			} else {
				String machine = view.getProcessPointPane().getMachineComboBox().getControl().getSelectionModel().getSelectedItem();
				productHistory = model.getProductHistoyByDateRangeAndProcessPoint(processPointId, machine, startTime, endTime);
			}

			if(productHistory.isEmpty()) {
				setErrorMessage("No records found that match the search criteria");
			} else {

				Set<String> products = new HashSet<String>();
				for(ProductHistory currentProductHistory : productHistory) {
					products.add(currentProductHistory.getProductId());
				}
				EventBusUtil.publish(new ProductEvent(new ArrayList<String>(products), ProductEventType.PRODUCT_INPUT_RECIEVED));
			}
		}
	}

	private boolean validateTimeRange(Timestamp startTime, Timestamp endTime) {
		long timeRange = endTime.getTime() - startTime.getTime();
		timeRange = timeRange / (60 * 60 * 1000);
		if(timeRange <= 0L) {
			setErrorMessage("End time must be greated than start time.");
		}
		int timeRangeLimit = model.getProductSearchTimeRangeLimit();
		if(timeRange > timeRangeLimit) {
			setErrorMessage("Time range of " + timeRange + " hours is greater than the allowed " + timeRangeLimit + " hours");
			return false; 
		}

		return true;
	}

	public void setErrorMessage(String errorMessage) {
		getLogger().warn(errorMessage);
		EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.ERROR));
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

	private void toggleKeyBoard() {
		setKeyboardPopUpVisible(!isKeyboardPopVisible());	
		isKeyBoardVisiable = (!isKeyBoardVisiable);
	}

	private void setKeyboardPopUpVisible(boolean isVisible) {
		CommonUtil.setPopupVisible(isVisible, parentView.getMainWindow(), keyboardPopup);
	}	

	private boolean isKeyboardPopVisible() {
		return isKeyBoardVisiable;
	}

	public ProductPropertyBean getProductPropertyBean() {
		if(productPropertyBean == null) {
			productPropertyBean = PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}
		return productPropertyBean;
	}

	public Logger getLogger() {
		return view.getMainWindow().getLogger();
	}
}
