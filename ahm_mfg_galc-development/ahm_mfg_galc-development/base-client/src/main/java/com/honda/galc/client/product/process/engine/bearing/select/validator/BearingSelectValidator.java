package com.honda.galc.client.product.process.engine.bearing.select.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.command.TextFieldCommand;
import com.honda.galc.client.product.process.engine.bearing.select.controller.BearingSelectController;
import com.honda.galc.client.product.process.engine.bearing.select.model.BearingSelectModel;
import com.honda.galc.client.product.process.engine.bearing.select.view.BearingSelectPanel;
import com.honda.galc.client.product.validator.AlphaNumericValidator;
import com.honda.galc.client.product.validator.LengthValidator;
import com.honda.galc.client.product.validator.LotControlRuleValidator;
import com.honda.galc.client.product.validator.NumericValidator;
import com.honda.galc.client.product.validator.RegExpValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.validator.SameTypeValidator;
import com.honda.galc.client.product.validator.StringTokenValidator;
import com.honda.galc.client.product.validator.StringValidator;
import com.honda.galc.client.product.validator.UniqueInstalledPartValidator;
import com.honda.galc.data.LineSideContainerTag;

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

	private BearingSelectController controller;
	private Map<JTextField, TextFieldCommand> validators;

	public BearingSelectValidator(BearingSelectController controller) {
		this.controller = controller;
		this.validators = new HashMap<JTextField, TextFieldCommand>();
		mapValidators();
	}

	public void validate(JTextField textField) {
		TextFieldCommand validator = getValidators().get(textField);
		if (validator == null) {
			return;
		}
		validator.execute();
	}

	protected void mapValidators() {
		mapBlockValidator();
		mapCrankSnValidator();
		mapConrodSnValidator();
		mapBlockMeasurementValidators();
		mapCrankMainMeasurementsValidators();
		mapCrankConrodMeasurementsValidators();
		mapConrodMeasurementsValidators();
	}

	// === validators definitions === //
	protected void mapBlockValidator() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaNumericValidator());
		validators.add(new LengthValidator(getModel().getBlockInputNumberLengths().toArray(new Integer[] {})));
		validators.add(new BlockExistValidatorCommand(getController()));
		validators.add(new UniqueInstalledPartValidator(getModel().getProperty().getInstalledBlockPartName(), getModel()));

		TextFieldCommand validator = TextFieldCommand.create(getController(), getView().getMcbTextField(), validators, "SN");
		getValidators().put(validator.getTextField(), validator);
	}

	protected void mapCrankSnValidator() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaNumericValidator());
		validators.add(new LengthValidator(getModel().getProperty().getCrankSnLength()));
		validators.add(new StringTokenValidator("Date Token", 6, 9, new RegExpValidator(getModel().getDateTokenPattern())));
		validators.add(new StringTokenValidator("Line Token", 10, 10, new NumericValidator()));
		validators.add(new StringTokenValidator("Sequence Token", 11, 15, new NumericValidator()));
		validators.add(new LotControlRuleValidator(LineSideContainerTag.CRANK_SERIAL_NUMBER, getModel().getRules(), getModel()));

		TextFieldCommand validator = TextFieldCommand.create(getController(), getView().getCrankSnTextField(), validators, "Crank SN");
		getValidators().put(validator.getTextField(), validator);
	}

	protected void mapConrodSnValidator() {

		for (Integer ix : getView().getConrodSnTextFields().keySet()) {
			JTextField tf = getView().getConrodSnTextFields().get(ix);
			String partName = String.format(LineSideContainerTag.CONROD_SERIAL_NUMBER_X, ix);
			List<Command> validators = new ArrayList<Command>();
			validators.add(new RequiredValidator());
			validators.add(new AlphaNumericValidator());
			validators.add(new LengthValidator(getModel().getProperty().getConrodSnLength()));
			validators.add(new ConrodSnValidator(getController()));
			validators.add(new StringTokenValidator("Revision Token", 3, 3, new NumericValidator()));
			validators.add(new StringTokenValidator("Date Token", 6, 9, new RegExpValidator(getModel().getDateTokenPattern())));
			validators.add(new StringTokenValidator("Line Token", 10, 10, new NumericValidator()));
			validators.add(new StringTokenValidator("Sequence Token", 11, 15, new NumericValidator()));
			validators.add(new LotControlRuleValidator(partName, getModel().getRules(), getModel()));

			String propertyName = String.format("Conrod SN.%s", ix);
			TextFieldCommand validator = TextFieldCommand.create(getController(), tf, validators, propertyName);
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapBlockMeasurementValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new StringValidator(getModel().getProperty().getMainBearingColumnValues()));
		validators.add(new SameTypeValidator(getView().getBlockMeasurementTextFields().values()));
		for (JTextField tf : getView().getBlockMeasurementTextFields().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getController(), tf, validators, "Block Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapCrankMainMeasurementsValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new StringValidator(getModel().getProperty().getMainBearingRowValues()));
		for (JTextField tf : getView().getCrankMainMeasurementTextFields().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getController(), tf, validators, "Crank Journal Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapCrankConrodMeasurementsValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new StringValidator(getModel().getProperty().getConrodBearingRowValues()));
		for (JTextField tf : getView().getCrankConrodMeasurementTextFields().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getController(), tf, validators, "Crank Conrod Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	protected void mapConrodMeasurementsValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new StringValidator(getModel().getProperty().getConrodBearingColumnValues()));
		for (JTextField tf : getView().getConrodMeasurementTextFields().values()) {
			TextFieldCommand validator = TextFieldCommand.create(getController(), tf, validators, "Conrod Ranking");
			getValidators().put(validator.getTextField(), validator);
		}
	}

	// === get/set === //
	protected BearingSelectController getController() {
		return controller;
	}

	protected BearingSelectPanel getView() {
		return getController().getView();
	}

	protected Map<JTextField, TextFieldCommand> getValidators() {
		return validators;
	}

	protected BearingSelectModel getModel() {
		return getController().getModel();
	}
}
