package com.honda.galc.client.dc.view.listener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractBodyPane.InputFieldType;
import com.honda.galc.client.dc.view.CylinderShimInstallBodyPane;
import com.honda.galc.client.dc.view.CylinderShimInstallView;
import com.honda.galc.client.product.command.TextFieldCommand;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * 
 * @author Wade Pei <br>
 * @date Mar 30, 2015
 */
public class CylinderShimActShimIdListener extends
		CylinderShimMeasurementListener<CylinderShimInstallView, CylinderShimInstallBodyPane, CylinderShimInstallProcessor> {

	public CylinderShimActShimIdListener(CylinderShimInstallView view, TextFieldCommand validator) {
		super(view, validator);
	}

	public CylinderShimActShimIdListener(CylinderShimInstallView view, TextField field, TextFieldCommand validator) {
		super(view, field, validator);
	}

	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		super.changed(observable, oldValue, newValue);
		if (StringUtils.trim(newValue).length() == SHIM_ID_LENGTH) {
			process();
		}
	}

	protected void processIfCorrect(TextField currentField) {
		currentField.setText(currentField.getText().toUpperCase());
		getBodyPane().finishedInput(currentField, InputFieldType.ACT_SHIM_ID);
		processIfCorrect(currentField, false);
	}
	
	private void process() {
		if (getBodyPane().isFinished()) {
			return;
		}
		TextField field = getField();
		preExecute(field);
		if (getController().isErrorExists()) {
			processIfErrorExists(field);
		} else {
			processIfCorrect(field);
		}
		postExecute();
	}
}
