package com.honda.galc.client.dc.view.listener;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractBodyPane.InputFieldType;
import com.honda.galc.client.dc.view.CylinderShimInstallBodyPane;
import com.honda.galc.client.dc.view.CylinderShimInstallView;
import com.honda.galc.client.product.command.TextFieldCommand;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 10, 2014
 */
public class CylinderShimBaseGapListener extends CylinderShimMeasurementListener<CylinderShimInstallView, CylinderShimInstallBodyPane, CylinderShimInstallProcessor> {

	public CylinderShimBaseGapListener(CylinderShimInstallView view, TextFieldCommand validator) {
		super(view, validator);
	}

	public CylinderShimBaseGapListener(CylinderShimInstallView view, TextField field, TextFieldCommand validator) {
		super(view, field, validator);
	}

	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		super.changed(observable, oldValue, newValue);
		int[] idx = getBodyPane().getTextFieldIndex(getField(), InputFieldType.BASE_GAP);
		TextField totalGapField = getBodyPane().getTotalGaps()[idx[0]][idx[1]];
		TextField targetGapField = getBodyPane().getTargetGaps()[idx[0]][idx[1]];
		TextField needShimField = getBodyPane().getNeedShimSzs()[idx[0]][idx[1]];
		TextField needShimIdField = getBodyPane().getNeedShimIds()[idx[0]][idx[1]];
		if (null != oldValue && !oldValue.equalsIgnoreCase(newValue)) {
			totalGapField.setText("");
			targetGapField.setText("");
			needShimField.setText("");
			needShimIdField.setText("");
		}
	}

	protected void processIfCorrect(TextField currentField) {
		getBodyPane().finishedInput(currentField, InputFieldType.BASE_GAP);
		super.processIfCorrect(currentField);
	}

}
