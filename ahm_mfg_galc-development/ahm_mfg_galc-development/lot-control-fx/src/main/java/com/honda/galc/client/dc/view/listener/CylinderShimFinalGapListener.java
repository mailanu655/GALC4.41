package com.honda.galc.client.dc.view.listener;

import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.dc.view.CylinderShimInstallBodyPane;
import com.honda.galc.client.dc.view.CylinderShimInstallView;
import com.honda.galc.client.product.command.TextFieldCommand;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * 
 * @author Wade Pei <br>
 * @date Apr 10, 2015
 */
public class CylinderShimFinalGapListener extends
		CylinderShimMeasurementListener<CylinderShimInstallView, CylinderShimInstallBodyPane, CylinderShimInstallProcessor> {

	public CylinderShimFinalGapListener(CylinderShimInstallView view, TextFieldCommand validator) {
		super(view, validator);
	}

	public CylinderShimFinalGapListener(CylinderShimInstallView view, TextField field, TextFieldCommand validator) {
		super(view, field, validator);
	}

	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		super.changed(observable, oldValue, newValue);		
	}

}
