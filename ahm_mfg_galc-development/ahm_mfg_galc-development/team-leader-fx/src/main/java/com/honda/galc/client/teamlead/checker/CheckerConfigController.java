package com.honda.galc.client.teamlead.checker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckerType;
import com.honda.galc.client.mvc.AbstractController;

public class CheckerConfigController extends AbstractController<CheckerConfigModel, CheckerConfigView> implements EventHandler<ActionEvent>{

	public CheckerConfigController(CheckerConfigModel model, CheckerConfigView view) {
		super(model, view);
	}

	@Override
	public void handle(ActionEvent actionEvent) {

	}

	@Override
	public void initEventHandlers() {
		addCheckerComboBoxListener();
		addDivisionIdComboBoxListener();
	}

	@SuppressWarnings("rawtypes")
	private void addCheckerComboBoxListener() {
		getView().getCheckerTypeComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldVal, String newVal) { 
				CheckerView checkerView = createCheckerView(CheckerType.getCheckerType(newVal));
				getView().setCenter(checkerView);
			} 
		});
	}

	private void addDivisionIdComboBoxListener() {
		getView().getDivisionIdComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldVal, String newVal) { 
				if(newVal.equals(CheckerConstants.ALL)) {
					newVal = StringUtils.EMPTY;
				}
				getModel().setDivisionId(newVal);
				getView().setCenter(null);
				getView().getCheckerTypeComboBox().getSelectionModel().clearSelection();
			} 
		});
	}

	@SuppressWarnings("rawtypes")
	private CheckerView createCheckerView(CheckerType checkerType) {
		switch (checkerType) {
		case Operation:
			return new OperationCheckerView(getModel(),getView().getMainWindow());
		case Part:
			return new OperationPartCheckerView(getModel(), getView().getMainWindow());
		case Measurement:
			return new MeasurementCheckerView(getModel(),getView().getMainWindow());
		case Application:
			return new ApplicationCheckerView(getModel(), getView().getMainWindow());	
		default:
			return null;
		}
	}
}
