package com.honda.galc.client.dc.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextField;

import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.processor.BearingSelectProcessor;
import com.honda.galc.client.dc.view.BearingSelectView;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.product.validator.AlphaValidator;
import com.honda.galc.client.product.validator.NumericValidator;
import com.honda.galc.client.product.validator.RegExpValidator;
import com.honda.galc.client.product.validator.RequiredValidator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingSelectValidator</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class BearingSelectValidator {

	protected BearingSelectProcessor processor;
	private Map<TextField, TextFieldCommand> validators;
	protected BearingSelectView widget;

	public BearingSelectValidator(BearingSelectView widget) {
		this.widget = widget;
		this.processor = widget.getProcessor();
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

	protected void mapValidators() {
		mapBlockMeasurementValidators();
		mapCrankMainMeasurementsValidators();
		mapCrankConrodMeasurementsValidators();
		mapConrodMeasurementsValidators();
	}

	protected void mapBlockMeasurementValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaValidator());
		validators.add(new RegExpValidator(getProcessor()
				.getBlockMeasurementPattern()));

		for (TextField tf : getWidget().getBlockMeasurementTextFields().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getProcessor()
					.getController(), tf, validators, "Block Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapCrankMainMeasurementsValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new NumericValidator());
		validators.add(new RegExpValidator(getProcessor()
				.getCrankMainMeasurementPattern()));
		for (TextField tf : getWidget().getCrankMainMeasurementTextFields()
				.values()) {
			TextFieldCommand validator = TextFieldCommand.create(getProcessor()
					.getController(), tf, validators, "Crank Journal Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapCrankConrodMeasurementsValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaValidator());
		validators.add(new RegExpValidator(getProcessor()
				.getCrankConrodMeasurementPattern()));
		for (TextField tf : getWidget().getCrankConrodMeasurementTextFields()
				.values()) {
			TextFieldCommand validator = TextFieldCommand.create(getProcessor()
					.getController(), tf, validators, "Crank Conrod Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapConrodMeasurementsValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new RegExpValidator(getProcessor()
				.getConrodMeasurementPattern()));
		for (TextField tf : getWidget().getConrodMeasurementTextFields().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getProcessor()
					.getController(), tf, validators, "Conrod Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	// === get/set === //
	protected BearingSelectProcessor getProcessor() {
		return processor;
	}

	protected BearingSelectView getWidget() {
		return widget;
	}

	protected Map<TextField, TextFieldCommand> getValidators() {
		return validators;
	}

	protected DataCollectionModel getModel() {
		return getProcessor().getController().getModel();
	}
}
