package com.honda.galc.client.ui.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.honda.galc.client.utils.QiConstant;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

public class DateTimePicker extends DatePicker {

	private ObjectProperty<LocalTime> timeValue = new SimpleObjectProperty<LocalTime>();
	private ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<LocalDateTime>();

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(QiConstant.DATE_TIME_FORMAT);

	public DateTimePicker() {
		super();
		setValue(LocalDate.now());
        setTimeValue(LocalTime.now());
        setDateTimeValue(LocalDateTime.now());
		createTimePicker();
		
        timeValue.addListener(t -> {
            dateTimeValue.set(LocalDateTime.of(this.getValue(), timeValue.get()));

            getEditor().setText(LocalDateTime.of(this.getValue(), timeValue.get()).format(dateTimeFormatter));
        });

        this.valueProperty().addListener(t -> {
        	 dateTimeValue.set(LocalDateTime.of(this.getValue(), timeValue.get()));
        });
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new DateTimePickerSkin(this);
	}

	private void createTimePicker() {

		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					LocalDateTime value=null;
					if (dateTimeValue == null) {
						value = LocalDateTime.of(date, LocalTime.now());
					} else {
						value =LocalDateTime.of(date, timeValue.get());
					}
					return (value != null) ? dateTimeFormatter.format(value) : "";

				} else {
					return "";
				}
			}
			
			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					timeValue.set(LocalTime.parse(string, dateTimeFormatter));
					return LocalDate.parse(string, dateTimeFormatter);
				} else {
					dateTimeValue.set(null);
					return null;
				}
			}
		};
		this.setConverter(converter);
		this.setPromptText(QiConstant.DATE_TIME_FORMAT.toLowerCase());
		this.setStyle("-fx-background-color: #ffc0cb;");		
	}

    public ObjectProperty<LocalDateTime> dateTimeValueProperty(){
        if (dateTimeValue == null){
            dateTimeValue = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.of(this.getValue(), timeValue.get()));
        }
        return dateTimeValue;
    }
    
    public LocalTime getTimeValue(){
        return timeValue.get();
    }

    void setTimeValue(LocalTime timeValue){
        this.timeValue.set(timeValue);
    }

    public ObjectProperty<LocalTime> timeValueProperty(){
        return timeValue;
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValueProperty().get();
    }

    public void setDateTimeValue (LocalDateTime dateTimeValue) {
        dateTimeValueProperty().set(dateTimeValue);
    }

	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}
}
