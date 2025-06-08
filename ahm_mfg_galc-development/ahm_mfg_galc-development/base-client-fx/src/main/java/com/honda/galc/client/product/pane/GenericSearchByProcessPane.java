package com.honda.galc.client.product.pane;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.product.entry.AbstractProductEntryPane;
import com.honda.galc.client.product.entry.GenericSearchByProcessPaneController;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.DateTimePicker;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.UiFactory;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GenericSearchByProcessPane extends AbstractGenericEntryPane {
	private GenericSearchByProcessPaneController controller;

	private MainWindow mainWindow;
	
	private LoggedButton keyboardButton;
	private LoggedButton searchButton;

	private DateTimePicker startDatePicker;
	private DateTimePicker endDatePicker;

	private ProcessPointSelectionPane processPointPane;

	public GenericSearchByProcessPane(AbstractProductEntryPane parentView) {
		super(PaneId.getPaneId(PaneId.SEARCH_BY_PROCESS_PANE.name()));
		this.mainWindow = parentView.getMainWindow();
		controller = new GenericSearchByProcessPaneController(this, parentView);
		initView();
		controller.initEventHandlers();
	}

	private void initView() {
		VBox dateSelectionPane = new VBox(5);
		dateSelectionPane.prefHeightProperty().bind(this.heightProperty().multiply(0.77));
		dateSelectionPane.prefWidthProperty().bind(this.widthProperty().multiply(0.30));

		HBox startDatePane = new HBox();
		HBox endDatePane = new HBox();

		LoggedLabel startDateTimeLabel = createLoggedLabel("startDateTime", "Start Time: ");
		LoggedLabel endDateTimeLabel = createLoggedLabel("endDateTime", "End Time: ");
		startDateTimeLabel.minWidthProperty().bind(widthProperty().multiply(0.08));
		endDateTimeLabel.minWidthProperty().bind(widthProperty().multiply(0.08));
		startDatePicker = createDatePicker();
		startDatePane.getChildren().addAll(startDateTimeLabel, startDatePicker);

		endDatePicker = createDatePicker();
		endDatePane.getChildren().addAll(endDateTimeLabel, endDatePicker);
		startDatePane.setAlignment(Pos.CENTER_RIGHT);
		endDatePane.setAlignment(Pos.CENTER_RIGHT);
		dateSelectionPane.getChildren().addAll(startDatePane, endDatePane);
		
		HBox box = new HBox();
		processPointPane = new ProcessPointSelectionPane(this, getPaneId());
		box.setAlignment(Pos.BASELINE_RIGHT);
		box.prefHeightProperty().bind(this.heightProperty().multiply(0.60));
		box.prefWidthProperty().bind(this.widthProperty().multiply(0.80));
		box.getChildren().addAll(dateSelectionPane, processPointPane);
		getChildren().add(box);

		searchButton = createButton("Search", true, ClientConstants.PRODUCT_SEARCH_BUTTON_ID);
		searchButton.prefWidthProperty().bind(widthProperty().multiply(0.21));
		

		searchButton.setDisable(true);
		setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0");
		createKeyboardButtonAndPopup();
	}	

	public ProcessPointSelectionPane getProcessPointPane() {
		return this.processPointPane;
	}

	private void createKeyboardButtonAndPopup() {
		VBox vBox = new VBox(heightProperty().doubleValue() * 0.004);
		vBox.prefWidthProperty().bind(this.widthProperty().multiply(0.20));
		vBox.prefHeightProperty().bind(this.heightProperty());
		if (controller.getProductPropertyBean().isKeyboardButtonVisible()) {
			keyboardButton = createButton("KEYBOARD", true, ClientConstants.KEYBOARD_BUTTON_ID);
			keyboardButton.prefWidthProperty().bind(widthProperty().multiply(0.21));
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					controller.createKeyBoardPopup();
				}
			});
			vBox.getChildren().add(keyboardButton);
		}
		vBox.getChildren().add(searchButton);

		getChildren().add(vBox);
	}	

	private DateTimePicker createDatePicker() {
		DateTimePicker dateTimePicker = new DateTimePicker();
		dateTimePicker.styleProperty().bind(datePickerStyle);
		dateTimePicker.prefWidthProperty().bind(this.widthProperty().multiply(0.17));
		dateTimePicker.minWidthProperty().bind(this.widthProperty().multiply(0.12));

		return dateTimePicker;
	}

	private LoggedLabel createLoggedLabel(String id, String text) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.fontProperty().bind(hfont);
		label.prefWidthProperty().bind(this.widthProperty().multiply(0.09));
		return label;
	}

	public LoggedTextField getQuantityTextField() {
		return null;
	}

	public DateTimePicker getStartDatePicker() {
		return startDatePicker;
	}

	public void setStartDatePicker(DateTimePicker startDatePicker) {
		this.startDatePicker = startDatePicker;
	}

	public DateTimePicker getEndDatePicker() {
		return endDatePicker;
	}

	public void setEndDatePicker(DateTimePicker endDatePicker) {
		this.endDatePicker = endDatePicker;
	}

	public LoggedButton getKeyboardButton() {
		return keyboardButton;
	}

	public void setKeyboardButton(LoggedButton keyboardButton) {
		this.keyboardButton = keyboardButton;
	}

	public LoggedButton getSearchButton() {
		return searchButton;
	}

	public void setSubmitButton(LoggedButton submitButton) {
		this.searchButton = submitButton;
	}
	
	public MainWindow getMainWindow() {
		return this.mainWindow;
	}
}
