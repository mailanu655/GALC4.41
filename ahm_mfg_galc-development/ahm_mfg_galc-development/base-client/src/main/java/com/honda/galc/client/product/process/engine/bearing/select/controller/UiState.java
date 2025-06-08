package com.honda.galc.client.product.process.engine.bearing.select.controller;

import com.honda.galc.client.product.process.engine.bearing.select.view.BearingSelectPanel;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.component.TextFieldState;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UiState</code> is ... .
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
public class UiState {

	public static void prepare(BearingSelectPanel view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	public static void start(BearingSelectPanel view) {
		if (view == null) {
			return;
		}

		TextFieldState.READ_ONLY.setState(view.getMcbTextField());
		view.getMcbEditButton().setText(BearingSelectPanel.EDIT_ACTION_LABEL);
		view.getMcbEditButton().setActionCommand(BearingSelectPanel.EDIT_ACTION_COMMAND);
		view.getMcbEditButton().setEnabled(false);

		view.getCrankSnCollectCheckBox().setEnabled(view.getModel().getProperty().isCrankSnCollectToogable());
		view.getCrankSnCollectCheckBox().setSelected(view.getModel().getProperty().isCrankSnCollect());
		TextFieldState crankSnState = view.getCrankSnCollectCheckBox().isSelected() ? TextFieldState.EDIT : TextFieldState.DISABLED;

		view.getConrodSnCollectCheckBox().setEnabled(view.getModel().getProperty().isConrodSnCollectToogable());
		view.getConrodSnCollectCheckBox().setSelected(view.getModel().getProperty().isConrodSnCollect());
		TextFieldState conrodSnState = view.getConrodSnCollectCheckBox().isSelected() ? TextFieldState.EDIT : TextFieldState.DISABLED;

		view.getCrankSnSelectButton().setEnabled(false);
		crankSnState.setState(view.getCrankSnTextField());
		view.getCrankSnEditButton().setEnabled(false);

		UiUtils.setEnabled(view.getConrodSnSelectButtons().values(), false);
		UiUtils.setState(view.getConrodSnTextFields().values(), conrodSnState);
		view.getConrodSnEditButton().setEnabled(false);

		UiUtils.setEditable(view.getCrankMainMeasurementTextFields().values(), !view.getCrankSnCollectCheckBox().isSelected());
		UiUtils.setEditable(view.getCrankConrodMeasurementTextFields().values(), !view.getCrankSnCollectCheckBox().isSelected());
		UiUtils.setEditable(view.getConrodMeasurementTextFields().values(), !view.getConrodSnCollectCheckBox().isSelected());

		UiUtils.setState(view.getBlockMeasurementTextFields().values(), TextFieldState.EDIT);
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(), TextFieldState.EDIT);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(), TextFieldState.EDIT);
		UiUtils.setState(view.getConrodMeasurementTextFields().values(), TextFieldState.EDIT);

		view.getBlockMeasurementsEditButton().setEnabled(false);
		view.getCrankMainMeasurementsEditButton().setEnabled(false);
		view.getCrankConrodMeasurementsEditButton().setEnabled(false);
		view.getConrodMeasurementsEditButton().setEnabled(false);

		view.getDoneButton().setEnabled(false);
	}

	public static void reset(BearingSelectPanel view) {
		if (view == null) {
			return;
		}

		TextFieldState.DISABLED.setState(view.getMcbTextField());
		view.getMcbEditButton().setText(BearingSelectPanel.EDIT_ACTION_LABEL);
		view.getMcbEditButton().setActionCommand(BearingSelectPanel.EDIT_ACTION_COMMAND);
		view.getMcbEditButton().setEnabled(false);

		view.getCrankSnCollectCheckBox().setEnabled(false);
		view.getCrankSnCollectCheckBox().setSelected(false);

		view.getConrodSnCollectCheckBox().setEnabled(false);
		view.getConrodSnCollectCheckBox().setSelected(false);

		view.getCrankSnSelectButton().setEnabled(false);
		TextFieldState.DISABLED.setState(view.getCrankSnTextField());
		view.getCrankSnEditButton().setEnabled(false);

		UiUtils.setEnabled(view.getConrodSnSelectButtons().values(), false);
		UiUtils.setState(view.getConrodSnTextFields().values(), TextFieldState.DISABLED);
		view.getConrodSnEditButton().setEnabled(false);

		UiUtils.setState(view.getBlockMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(view.getConrodMeasurementTextFields().values(), TextFieldState.DISABLED);

		view.getBlockMeasurementsEditButton().setEnabled(false);
		view.getCrankMainMeasurementsEditButton().setEnabled(false);
		view.getCrankConrodMeasurementsEditButton().setEnabled(false);
		view.getConrodMeasurementsEditButton().setEnabled(false);

		view.getDoneButton().setEnabled(false);
	}

	public static void alreadyProcessed(BearingSelectPanel view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	public static void notProcessable(BearingSelectPanel view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	public static void finish(BearingSelectPanel view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	protected static void readOnly(BearingSelectPanel view) {
		if (view == null) {
			return;
		}

		TextFieldState.READ_ONLY.setState(view.getMcbTextField());
		view.getMcbEditButton().setText(BearingSelectPanel.EDIT_ACTION_LABEL);
		view.getMcbEditButton().setActionCommand(BearingSelectPanel.EDIT_ACTION_COMMAND);
		view.getMcbEditButton().setEnabled(false);

		view.getCrankSnCollectCheckBox().setEnabled(false);
		view.getCrankSnCollectCheckBox().setSelected(view.getModel().getProperty().isCrankSnCollect());

		view.getConrodSnCollectCheckBox().setEnabled(false);
		view.getConrodSnCollectCheckBox().setSelected(view.getModel().getProperty().isConrodSnCollect());

		view.getCrankSnSelectButton().setEnabled(false);
		if (view.getCrankSnCollectCheckBox().isSelected()) {
			TextFieldState.READ_ONLY.setState(view.getCrankSnTextField());
		} else {
			TextFieldState.DISABLED.setState(view.getCrankSnTextField());
		}
		view.getCrankSnEditButton().setEnabled(false);

		UiUtils.setEnabled(view.getConrodSnSelectButtons().values(), false);
		if (view.getConrodSnCollectCheckBox().isSelected()) {
			UiUtils.setState(view.getConrodSnTextFields().values(), TextFieldState.READ_ONLY);
		} else {
			UiUtils.setState(view.getConrodSnTextFields().values(), TextFieldState.DISABLED);
		}
		view.getConrodSnEditButton().setEnabled(false);

		UiUtils.setState(view.getBlockMeasurementTextFields().values(), TextFieldState.READ_ONLY);
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(), TextFieldState.READ_ONLY);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(), TextFieldState.READ_ONLY);
		UiUtils.setState(view.getConrodMeasurementTextFields().values(), TextFieldState.READ_ONLY);

		view.getBlockMeasurementsEditButton().setEnabled(false);
		view.getCrankMainMeasurementsEditButton().setEnabled(false);
		view.getCrankConrodMeasurementsEditButton().setEnabled(false);
		view.getConrodMeasurementsEditButton().setEnabled(false);

		view.getDoneButton().setEnabled(false);
	}

	protected static void disabled(BearingSelectPanel view) {
		if (view == null) {
			return;
		}
		reset(view);
		view.getCrankSnCollectCheckBox().setSelected(view.getModel().getProperty().isCrankSnCollect());
		view.getConrodSnCollectCheckBox().setSelected(view.getModel().getProperty().isConrodSnCollect());
	}

	public static void clear(BearingSelectPanel view) {
		if (view == null) {
			return;
		}

		view.getController().setFocusComponent(null);
		view.getController().clearMessages();

		view.getMcbTextField().setText("");

		view.getCrankSnCollectCheckBox().setSelected(false);
		view.getConrodSnCollectCheckBox().setSelected(false);

		view.getCrankSnTextField().setText("");

		UiUtils.setText(view.getConrodSnTextFields().values(), "");
		UiUtils.setText(view.getBlockMeasurementTextFields().values(), "");
		UiUtils.setText(view.getCrankMainMeasurementTextFields().values(), "");
		UiUtils.setText(view.getCrankConrodMeasurementTextFields().values(), "");
		UiUtils.setText(view.getConrodMeasurementTextFields().values(), "");
	}

	public static void initCrankMeasurementTextFieldsForCrankSnEdit(BearingSelectPanel view) {
		UiUtils.setText(view.getCrankMainMeasurementTextFields().values(), "");
		UiUtils.setText(view.getCrankConrodMeasurementTextFields().values(), "");
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(), TextFieldState.EDIT);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(), TextFieldState.EDIT);
	}
}
