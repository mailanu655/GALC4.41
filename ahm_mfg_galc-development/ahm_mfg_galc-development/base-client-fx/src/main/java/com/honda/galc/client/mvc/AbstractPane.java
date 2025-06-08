package com.honda.galc.client.mvc;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public abstract class AbstractPane extends BorderPane {
	private TabPane parentView;

	protected ObjectProperty<Font> font = new SimpleObjectProperty<Font>(Font.getDefault());
	protected StringProperty textStyle = new SimpleStringProperty();
	protected StringProperty labelStyle = new SimpleStringProperty();

	protected ObservableValue<? extends Number> height = new SimpleDoubleProperty(1000);
	protected ObservableValue<? extends Number> width = new SimpleIntegerProperty(1000);

	public AbstractPane(TabPane parentView) {
		this.parentView = parentView;
		init();
	}

	private void init() {
		this.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String newTextStyle = String.format("-fx-font-family : Dialog; -fx-font-size: %dpx;", (int) (newValue.doubleValue() * 0.42));
				String newLabelStyle = String.format("-fx-font-weight: bold; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int) (newValue.doubleValue() * 0.50));
				textStyle.setValue(newTextStyle);
				labelStyle.setValue(newLabelStyle);
				font.set(Font.font(newValue.doubleValue() * 0.22));
				height = observable;;
			}
		});

		this.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				width = observable;
			}
		});
	}
}
