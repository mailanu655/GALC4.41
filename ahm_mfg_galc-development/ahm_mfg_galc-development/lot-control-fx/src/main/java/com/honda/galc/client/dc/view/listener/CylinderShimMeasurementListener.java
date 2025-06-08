package com.honda.galc.client.dc.view.listener;

import com.honda.galc.client.dc.processor.CylinderShimInstallAbstractProcessor;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractBodyPane;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractView;
import com.honda.galc.client.product.command.TextFieldCommand;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 9, 2014
 */
public class CylinderShimMeasurementListener<V extends CylinderShimInstallAbstractView<B, P>, B extends CylinderShimInstallAbstractBodyPane<V>, P extends CylinderShimInstallAbstractProcessor<V,B>> extends MeasurementBaseListener<CylinderShimInstallAbstractView<B, P>> implements EventHandler<ActionEvent>,
		ChangeListener<String> {
	
	protected int SHIM_ID_LENGTH = 3;

	public CylinderShimMeasurementListener(V view, TextFieldCommand validator) {
		super(view, validator);
	}

	public CylinderShimMeasurementListener(V view, TextField field, TextFieldCommand validator) {
		super(view, field, validator);
	}

	public void handle(ActionEvent event) {
		if (getBodyPane().isFinished()) {
			return;
		}
		TextField field = (TextField) event.getSource();
		preExecute(field);
		if (getController().isErrorExists()) {
			processIfErrorExists(field);
		} else {
			processIfCorrect(field);
		}
		postExecute();
	}

	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (getBodyPane().isFinished()) {
			return;
		}
		if (null != oldValue && !oldValue.equalsIgnoreCase(newValue)) {
			getBodyPane().setFieldCheckResult(getField(), false);
		}
	}

	protected void processIfCorrect(TextField currentField) {
		processIfCorrect(currentField, true);
	}
	
	protected void processIfCorrect(TextField currentField, boolean playSound) {
		Control nextFocused = getBodyPane().getNextFocusableTextField(currentField);
		getController().setFocusComponent(nextFocused);
		getBodyPane().setFocusedComponent(nextFocused);
		getBodyPane().setFieldCheckResult(currentField, true);
		if(playSound) {
			getProcessor().getAudioManager().playOKSound();
		}
		if (getBodyPane().getAllFieldsOk().get()) {
			getController().setFocusComponent(getBodyPane().getDoneBtn());
		}
	}

	protected void processIfErrorExists(TextField currentField) {
		getController().setFocusComponent(currentField);
		getBodyPane().setFieldCheckResult(currentField, false);
		getProcessor().getAudioManager().playNGSound();
	}

	public CylinderShimInstallAbstractProcessor getProcessor() {
		return getView().getProcessor();
	}

	protected B getBodyPane() {
		return getView().getBodyPane();
	}

}
