package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.view.ViewManagerBase;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.MeasurementValue;

public class MeasurementValueInputAction extends BaseDataCollectionAction {

	private static final long serialVersionUID = -428458253123621308L;

	public MeasurementValueInputAction(ClientContext context, String name) {
		super(context, name);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		confirmMeasurementValue(e);
	}

	private void confirmMeasurementValue(ActionEvent e) {
		
		JTextField measurementValueTextField = (JTextField)e.getSource(); 
		if (measurementValueTextField.isEditable()) {
			String measurementValue = measurementValueTextField.getText();
		
			Logger.getLogger().info("MeasurementValue:" + measurementValue + " is manually entered.");
			MeasurementValue measurement = new MeasurementValue(Double.parseDouble(measurementValue));
			runInSeparateThread(measurement);
		}
		
	}

}
