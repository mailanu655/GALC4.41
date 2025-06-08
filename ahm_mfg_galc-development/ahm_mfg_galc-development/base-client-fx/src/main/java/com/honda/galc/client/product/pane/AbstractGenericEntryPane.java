package com.honda.galc.client.product.pane;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.entry.AbstractProductEntryPane;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.UiFactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class AbstractGenericEntryPane extends MigPane {
	protected AbstractProductEntryPane parentView;
	private PaneId paneId;

	protected ObjectProperty<Font> hfont = new SimpleObjectProperty<Font>(Font.getDefault());
	protected ObjectProperty<Font> vComboBoxHLabelFont = new SimpleObjectProperty<Font>(Font.getDefault());
	protected ObjectProperty<Font> hComboBoxVLabelFont = new SimpleObjectProperty<Font>(Font.getDefault());

	protected ObjectProperty<Font> labelFont = new SimpleObjectProperty<Font>(Font.getDefault());

	protected StringProperty textStyle = new SimpleStringProperty();
	protected StringProperty datePickerStyle = new SimpleStringProperty();
	protected StringProperty hComboBoxSyle = new SimpleStringProperty();
	protected StringProperty vCbHLableComboBoxStyle = new SimpleStringProperty();
	protected StringProperty hCbVLableComboBoxStyle = new SimpleStringProperty();

	protected ObservableValue<? extends Number> height = new SimpleDoubleProperty(1000);
	protected ObservableValue<? extends Number> width = new SimpleIntegerProperty(1000);

	public AbstractGenericEntryPane(PaneId panelId) {
		super("", "[][grow,fill]");
		this.paneId = panelId;
		setId(panelId.name());
		init();
	}

	private void init() {
		this.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String newTextStyle = String.format("-fx-font-family : Dialog; -fx-font-weight: bold; -fx-font-size: %dpx;", (int) (newValue.doubleValue() * 0.24));
				String newDatePickerStyle = String.format("-fx-background-color: #ffc0cb; -fx-font-family : Dialog; fx-padding: 0 2px 0 0;  -fx-font-size: %dpx;", (int) (newValue.doubleValue() * 0.18));
				String newHComboBoxStyle = String.format("-fx-font-family : Dialog; -fx-font-weight: bold; -fx-padding: 0 0px 0 0;-fx-font-size: %dpx;", (int) Math.ceil(newValue.doubleValue() * 0.19));
				String newVCbHLableComboBoxStyle = String.format("-fx-font-family : Dialog; -fx-font-weight: bold; -fx-padding: 0 0px 0 0;-fx-font-size: %dpx;", (int) Math.ceil(newValue.doubleValue() * 0.13));
				String newHCbVLableComboBoxStyle = String.format("-fx-font-family : Dialog; -fx-font-weight: bold; -fx-padding: 0 0px 0 0;-fx-font-size: %dpx;", (int) Math.ceil(newValue.doubleValue() * 0.17));

				textStyle.setValue(newTextStyle);
				hComboBoxSyle.setValue(newHComboBoxStyle);
				vCbHLableComboBoxStyle.setValue(newVCbHLableComboBoxStyle);
				hCbVLableComboBoxStyle.setValue(newHCbVLableComboBoxStyle);

				datePickerStyle.setValue(newDatePickerStyle);
				hfont.set(Font.font("Dialog", FontWeight.BOLD, newValue.doubleValue() * 0.20));
				vComboBoxHLabelFont.set(Font.font("Dialog", FontWeight.BOLD, newValue.doubleValue() * 0.12));
				hComboBoxVLabelFont.set(Font.font("Dialog", FontWeight.BOLD, newValue.doubleValue() * 0.22));
				height = observable;
			}
		});
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				width = observable;
			}
		});
	}

	protected LoggedButton createButton(String text, boolean isEnabled, String id) {
		LoggedButton button =  UiFactory.createButton(text, isEnabled);
		button.setId(id);
		button.styleProperty().bind(textStyle);
		button.prefHeightProperty().bind(this.heightProperty().multiply(0.35));
		return button;
	}

	public String getPaneLabel() {
		return this.paneId.getPaneLabel();
	}

	public PaneId getPaneId() {
		return this.paneId;
	}
}
