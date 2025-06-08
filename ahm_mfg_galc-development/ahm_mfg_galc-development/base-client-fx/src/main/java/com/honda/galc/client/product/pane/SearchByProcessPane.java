package com.honda.galc.client.product.pane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.product.action.KeyboardAction;
import com.honda.galc.client.product.entry.SearchByProcessController;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.DateTimePicker;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.event.ProductProcessEvent;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class SearchByProcessPane extends AbstractProductSearchPane {
	private SearchByProcessController controller;

	private LoggedButton keyboardButton;
	private LoggedButton searchButton;

	private DateTimePicker startDatePicker;
	private DateTimePicker endDatePicker;

	private LabeledComboBox<ProcessPoint> processPointComboBox;
	private LabeledComboBox<String> machineComboBox;
	private LabeledComboBox<Division> departmentComboBox;


	public SearchByProcessPane(BulkProductController productController) {
		super();
		this.productController = productController;
		parentPanelHeight = productController.getInputPaneHeight();
		initView();
		createController();
		EventBusUtil.register(this);
	}

	private void createController() {
		if(controller == null) {
			controller = new SearchByProcessController(this, getProductController().getView().getMainWindow().getApplicationContext());
		}
	}

	private void initView() {
		TilePane dateSelectionPane = new TilePane();
		dateSelectionPane.setPrefColumns(2);
		dateSelectionPane.setVgap(0.10 * parentPanelHeight);
		LoggedLabel startDateTimeLabel = createLoggedLabel("startDateTime", "Start Time: " , getTextStyle());
		LoggedLabel endDateTimeLabel = createLoggedLabel("endDateTime", "End Time: " , getTextStyle());

		startDatePicker = createDatePicker();
		dateSelectionPane.getChildren().addAll(startDateTimeLabel, startDatePicker);

		endDatePicker = createDatePicker();
		dateSelectionPane.getChildren().addAll(endDateTimeLabel, endDatePicker);
		dateSelectionPane.setPadding(new Insets(0, parentPanelHeight * 0.08, 0 ,0));
		dateSelectionPane.setAlignment(Pos.CENTER_LEFT);

		HBox processSelectionHBox = new HBox(15);

		departmentComboBox = new LabeledComboBox<Division>("Department", false, new Insets(0), true, false);
		departmentComboBox.getLabel().setStyle(getLabelStyleSmall());
		departmentComboBox.getControl().setStyle(getLabelStyleSmall());
		departmentComboBox.getControl().setMinHeight(getComboBoxHeight());
		departmentComboBox.getControl().setMaxWidth(getComboBoxWidth());
		processSelectionHBox.getChildren().add(departmentComboBox);

		processPointComboBox = new LabeledComboBox<ProcessPoint>("Process Point", false, new Insets(0), true, false);
		processPointComboBox.getLabel().setStyle(getLabelStyleSmall());
		processPointComboBox.getControl().setStyle(getLabelStyleSmall());
		processPointComboBox.getControl().setMinHeight(getComboBoxHeight());
		processPointComboBox.getControl().setMaxWidth(getComboBoxWidth());
		processSelectionHBox.getChildren().add(processPointComboBox);

		machineComboBox = new LabeledComboBox<String>("Machine", false, new Insets(0), true, false);
		machineComboBox.getLabel().setStyle(getLabelStyleSmall());
		machineComboBox.getControl().setStyle(getLabelStyleSmall());
		machineComboBox.getControl().setMinHeight(getComboBoxHeight());
		machineComboBox.getControl().setMaxWidth(getComboBoxWidth() / 2);
		processSelectionHBox.getChildren().add(machineComboBox);

		getChildren().add(dateSelectionPane);
		getChildren().add(processSelectionHBox);

		searchButton = UiFactory.createButton("Search", getButtonStyle(), true);
		searchButton.setMinWidth(getScreenWidth() * 0.386);
		searchButton.setDisable(true);

		VBox vBox = new VBox(parentPanelHeight * 0.02);
		if (getProductController().getModel().getProperty().isKeyboardButtonVisible()) {
			keyboardButton = UiFactory.createButton("KEYBOARD", getButtonStyle(), true);
			keyboardButton.setMinWidth(getScreenWidth() * 0.276);
			vBox.getChildren().add(keyboardButton);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					keyboardButton.setOnAction(new KeyboardAction(getProductController()));
				}
			});
		}
		vBox.getChildren().add(searchButton);

		getChildren().add(vBox);
	}	

	private DateTimePicker createDatePicker() {
		DateTimePicker dateTimePicker = new DateTimePicker();
		dateTimePicker.setMinWidth(getDateTimePickerWidth());
		dateTimePicker.setMaxWidth(getDateTimePickerWidth());
		dateTimePicker.setStyle(getDateTimePickerStyle());

		return dateTimePicker;
	}

	private LoggedLabel createLoggedLabel(String id, String text,String cssClass) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.setStyle(cssClass);
		label.setPrefWidth(getLabelWidth());
		return label;
	}
	
	@Override
	public void associateSelected() {
		if(!searchButton.isDisabled()) {
			searchButton.fireEvent(new ActionEvent());
		}
		searchButton.requestFocus();
	}
	
	private String getDateTimePickerStyle() {
		return String.format("-fx-background-color: #ffc0cb; -fx-font-family : Dialog; fx-padding: 0 2px 0 0; -fx-font-size: %dpx;", (int) (0.16 * parentPanelHeight));
	}

	public LoggedTextField getQuantityTextField() {
		return null;
	}

	@Override
	public BulkProductController getProductController() {
		return (BulkProductController) productController;
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

	public LabeledComboBox<ProcessPoint> getProcessPointComboBox() {
		return processPointComboBox;
	}

	public void setProcessPointComboBox(LabeledComboBox<ProcessPoint> processPointComboBox) {
		this.processPointComboBox = processPointComboBox;
	}

	public LabeledComboBox<String> getMachineComboBox() {
		return machineComboBox;
	}

	public void setMachineComboBox(LabeledComboBox<String> machineComboBox) {
		this.machineComboBox = machineComboBox;
	}

	public LoggedButton getKeyboardButton() {
		return keyboardButton;
	}

	public void setKeyboardButton(LoggedButton keyboardButton) {
		this.keyboardButton = keyboardButton;
	}

	public void setProductController(BulkProductController productController) {
		this.productController = productController;
	}

	public LoggedButton getSearchButton() {
		return searchButton;
	}

	public void setSubmitButton(LoggedButton submitButton) {
		this.searchButton = submitButton;
	}

	public LabeledComboBox<Division> getDepartmentComboBox() {
		return departmentComboBox;
	}

	public void setDepartmentComboBox(LabeledComboBox<Division> departmentComboBox) {
		this.departmentComboBox = departmentComboBox;
	}

	@Override
	public TextField getProductIdField() {
		return null;
	}

	@Override
	public TextInputControl getExpectedProductIdField() {
		return null;
	}

	@Override
	public void setProductSequence() {		
	}
	
	@Subscribe
	public void onProductProcessEvent(ProductProcessEvent event) {
		if (event == null) {
			return;
		} else {
			getDepartmentComboBox().setSelectedIndex(-1);
		} 
	
	}
}
