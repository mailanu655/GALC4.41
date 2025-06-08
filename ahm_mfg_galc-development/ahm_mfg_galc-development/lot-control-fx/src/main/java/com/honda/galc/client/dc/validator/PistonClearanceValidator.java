package com.honda.galc.client.dc.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextField;

import com.honda.galc.client.dc.processor.PistonClearanceProcessor;
import com.honda.galc.client.dc.view.PistonClearanceView;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.product.validator.FloatNumberValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
/*   
* @author Jiamei Li<br>
* Jul 10, 2014
*
*/
public class PistonClearanceValidator {

	private PistonClearanceProcessor processor;
	private PistonClearanceView view;
	private Map<TextField, TextFieldCommand> validators;
	public PistonClearanceValidator(PistonClearanceView view){
		this.view = view;
		this.processor = view.getProcessor();
		this.validators = new HashMap<TextField, TextFieldCommand>();
		mapValidators();
	}
	
	public void validate(TextField textField) {
		TextFieldCommand validator = getValidators().get(textField);
		if (validator == null) {
			return;
		}
		validator.execute();
	}
	
	protected void mapValidators(){
		mapPistonMeasurementsValidator();
		mapClearanceMeasurementsValidator();
	}
	
	protected void mapPistonMeasurementsValidator(){
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new FloatNumberValidator());
		for (TextField tf : getView().getPistonMeasurements().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getProcessor().getController(), tf, validators, "Piston measurement");
			getValidators().put(validator.getTextField(), validator);
		}
	}
	
	protected void mapClearanceMeasurementsValidator(){
		List<Command> validators = new ArrayList<Command>();
		validators.add(new FloatNumberValidator());
		validators.add(new PistonClearanceRangeValidator(this.getProcessor()));
		for (TextField tf : getView().getClearanceMeasurements().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getProcessor().getController(), tf, validators, "Clearance measurement");
			getValidators().put(validator.getTextField(), validator);
		}
	}
	
	public PistonClearanceProcessor getProcessor() {
		return processor;
	}
	public PistonClearanceView getView() {
		return view;
	}
	public Map<TextField, TextFieldCommand> getValidators() {
		return validators;
	}
}
