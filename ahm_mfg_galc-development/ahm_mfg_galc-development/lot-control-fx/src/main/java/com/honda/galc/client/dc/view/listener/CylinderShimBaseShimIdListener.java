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
 * @date Jul 10, 2014
 */
public class CylinderShimBaseShimIdListener extends CylinderShimMeasurementListener<CylinderShimInstallView, CylinderShimInstallBodyPane, CylinderShimInstallProcessor> {

	public CylinderShimBaseShimIdListener(CylinderShimInstallView view, TextFieldCommand validator) {
		super(view, validator);
	}

	public CylinderShimBaseShimIdListener(CylinderShimInstallView view, TextField field, TextFieldCommand validator) {
		super(view, field, validator);
	}

	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		super.changed(observable, oldValue, newValue);
		int[] idx = getBodyPane().getTextFieldIndex(getField(), InputFieldType.BASE_SHIM_ID);
		TextField baseShimField = getBodyPane().getBaseShims()[idx[0]][idx[1]];
		if (null != oldValue && !oldValue.equalsIgnoreCase(newValue)) {
			baseShimField.setText("");
		}
		
		if (StringUtils.trim(newValue).length() == SHIM_ID_LENGTH) {
			process();
		}
	}

	protected void processIfCorrect(TextField currentField) {
		currentField.setText(currentField.getText().toUpperCase());
		getBodyPane().finishedInput(currentField, InputFieldType.BASE_SHIM_ID);
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
