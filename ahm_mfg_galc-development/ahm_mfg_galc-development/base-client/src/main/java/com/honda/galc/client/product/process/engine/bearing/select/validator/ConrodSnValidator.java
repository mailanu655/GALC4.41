package com.honda.galc.client.product.process.engine.bearing.select.validator;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.process.engine.bearing.select.controller.BearingSelectController;
import com.honda.galc.client.product.validator.AbstractValidator;

public class ConrodSnValidator extends AbstractValidator{
	
	public ConrodSnValidator(BearingSelectController controller) {
		super();
		this.controller = controller;
	}

	private BearingSelectController controller;
	
	public boolean execute(String value) {
		Map<Integer, JTextField> conrodSnTextFields = controller.getView().getConrodSnTextFields();
		
		int count = 0;
		for(Entry<Integer, JTextField> e : conrodSnTextFields.entrySet()){
			if(StringUtils.equals(value,e.getValue().getText()))	
				count++;
		}
		
		if(count > 1) {
			setDetailedMessageTemplate(String.format("Conrod %s already installed on this product.", value));
			return false;
		}
		
		return true;
	}

	public BearingSelectController getController() {
		return controller;
	}

	public void setController(BearingSelectController controller) {
		this.controller = controller;
	}

	
}
