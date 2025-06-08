package com.honda.galc.client.ui.component;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

import com.honda.galc.client.utils.UiFactory;
import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;


public class DateTimePickerSkin extends DatePickerSkin {

	private DateTimePicker dateTimePicker;
	private DatePickerContent content;
	private Spinner<Integer> hourSpinner;
	private Spinner<Integer> minuteSpinner;
	private Spinner<Integer> secondSpinner;
	private double screenWidth;

	public DateTimePickerSkin(DateTimePicker dateTimePicker) {
		super(dateTimePicker);
		this.dateTimePicker = dateTimePicker;
	}

	@Override
	public Node getPopupContent() {
		if(content == null) {
			content = (DatePickerContent) super.getPopupContent();
			screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
			HBox container = new HBox(createHourSpinner(), createMinuteSpinner(), createAmPmSpinner());

			container.setSpacing(20);
			container.setAlignment(Pos.CENTER);
			content.getChildren().add(container);

			hourSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
				dateTimePicker.setTimeValue(dateTimePicker.getTimeValue().withHour(((Integer) newValue).intValue()));
			});

			minuteSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
				dateTimePicker.setTimeValue(dateTimePicker.getTimeValue().withMinute(((Integer) newValue).intValue()));
			});

			secondSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
				dateTimePicker.setTimeValue(dateTimePicker.getTimeValue().withSecond(((Integer) newValue).intValue()));
			});
			
			dateTimePicker.timeValueProperty().addListener(e -> {
				int hour = dateTimePicker.getTimeValue().getHour();
				int minute = dateTimePicker.getTimeValue().getMinute();
				int second = dateTimePicker.getTimeValue().getSecond();
				
				hourSpinner.getValueFactory().setValue(hour);
				minuteSpinner.getValueFactory().setValue(minute);
				secondSpinner.getValueFactory().setValue(second);
			});
		}
		return content;
	}

	private Node createHourSpinner() {
		VBox container = new VBox();
		Label hour = UiFactory.createLabel("Hour", "Hour", getLabelStyleSmaller());
		hour.setFont(Font.font("Roboto"));
		hourSpinner = new Spinner<Integer>(0, 23, this.dateTimePicker.getDateTimeValue().get(ChronoField.HOUR_OF_DAY));
		hourSpinner.setEditable(true);
		hourSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		hourSpinner.getValueFactory().setWrapAround(true);
		hourSpinner.setPrefWidth(40);

		container.getChildren().addAll(hour, hourSpinner);
		return container;
	}

	private Node createMinuteSpinner() {
		VBox container = new VBox();
		Label minute = UiFactory.createLabel("Minute", "Minute", getLabelStyleSmaller());
		minuteSpinner = new Spinner<Integer>(0, 59, this.dateTimePicker.getDateTimeValue().getMinute(), 1);
		minuteSpinner.setEditable(true);
		minuteSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		minuteSpinner.getValueFactory().setWrapAround(true);
		minuteSpinner.setPrefWidth(40);
		container.getChildren().addAll(minute, minuteSpinner);
		return container;
	}

	private Node createAmPmSpinner() {
		VBox container = new VBox();
		Label second = UiFactory.createLabel("Second", "Second", getLabelStyleSmaller());
		secondSpinner = new Spinner<Integer>(0, 59, this.dateTimePicker.getDateTimeValue().getSecond(), 1);
		secondSpinner.setEditable(true);
		secondSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		secondSpinner.getValueFactory().setWrapAround(true);
		secondSpinner.setPrefWidth(40);
		container.getChildren().addAll(second, secondSpinner);
		return container;
	}

	public  String  getLabelStyleSmaller() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}
}
