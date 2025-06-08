package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.view.BearingSelectView;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiUtils;

/**
 * The Class UiState is a utility class to manage UI component status.
 */
public class BearingSelectUiState {

	/**
	 * Change the UI components when a EIN is already processed.
	 * 
	 * @param view
	 *            the view
	 */
	public void alreadyProcessed(BearingSelectView view) {
		if (view == null) {
			return;
		}
		disabled(view);
		view.getDoneButton().setDisable(false);
	}

	/**
	 * Clear the input of the UI components.
	 * 
	 * @param view
	 *            the view
	 */
	public void clear(BearingSelectView view) {
		if (view == null) {
			return;
		}

		view.getProcessor().getController().setFocusComponent(null);
		view.getProcessor().getController().clearMessages();

		UiUtils.setText(view.getBlockMeasurementTextFields().values(), "");
		UiUtils.setText(view.getCrankMainMeasurementTextFields().values(), "");
		UiUtils.setText(view.getCrankConrodMeasurementTextFields().values(), "");
		UiUtils.setText(view.getConrodMeasurementTextFields().values(), "");
	}

	/**
	 * disable the UI comonents.
	 * 
	 * @param view
	 *            the view
	 */
	protected void disabled(BearingSelectView view) {
		if (view == null) {
			return;
		}
		reset(view);
	}

	/**
	 * Set the UI component's status when bearing select is complete.
	 * 
	 * @param view
	 *            the view
	 */
	public void finish(BearingSelectView view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	/**
	 * Inits the crank measurement text fields for crank sn edit.
	 * 
	 * @param view
	 *            the view
	 */
	public void initCrankMeasurementTextFieldsForCrankSnEdit(
			BearingSelectView view) {
		UiUtils.setText(view.getCrankMainMeasurementTextFields().values(), "");
		UiUtils.setText(view.getCrankConrodMeasurementTextFields().values(), "");
		if (view.getProcessor().isCrankMainMeasurementsEditable()) {
			UiUtils.setState(view.getCrankMainMeasurementTextFields().values(),
					TextFieldState.EDIT);
			UiUtils.setState(view.getCrankConrodMeasurementTextFields()
					.values(), TextFieldState.EDIT);
		} else {
			UiUtils.setState(view.getCrankMainMeasurementTextFields().values(),
					TextFieldState.DISABLED);
			UiUtils.setState(view.getCrankConrodMeasurementTextFields()
					.values(), TextFieldState.DISABLED);
		}
	}

	/**
	 * Change the UI components when a EIN cannot be processed.
	 * 
	 * @param view
	 *            the view
	 */
	public void notProcessable(BearingSelectView view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	/**
	 * Prepare the UI components .
	 * 
	 * @param view
	 *            the view
	 */
	public void prepare(BearingSelectView view) {
		if (view == null) {
			return;
		}
		disabled(view);
	}

	/**
	 * Change the UI components to Read only.
	 * 
	 * @param view
	 *            the view
	 */
	protected void readOnly(BearingSelectView view) {
		if (view == null) {
			return;
		}

		UiUtils.setState(view.getBlockMeasurementTextFields().values(),
				TextFieldState.READ_ONLY);
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(),
				TextFieldState.READ_ONLY);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(),
				TextFieldState.READ_ONLY);
		UiUtils.setState(view.getConrodMeasurementTextFields().values(),
				TextFieldState.READ_ONLY);

		view.getBlockMeasurementsEditButton().setDisable(true);
		view.getCrankMainMeasurementsEditButton().setDisable(true);
		view.getCrankConrodMeasurementsEditButton().setDisable(true);
		view.getConrodMeasurementsEditButton().setDisable(true);

		view.getDoneButton().setDisable(true);
	}

	/**
	 * Reset the UI components.
	 * 
	 * @param view
	 *            the view
	 */
	public void reset(BearingSelectView view) {
		if (view == null) {
			return;
		}

		UiUtils.setState(view.getBlockMeasurementTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(view.getConrodMeasurementTextFields().values(),
				TextFieldState.DISABLED);

		view.getBlockMeasurementsEditButton().setDisable(true);
		view.getCrankMainMeasurementsEditButton().setDisable(true);
		view.getCrankConrodMeasurementsEditButton().setDisable(true);
		view.getConrodMeasurementsEditButton().setDisable(true);

		view.getDoneButton().setDisable(true);
	}

	/**
	 * Make the UI ready to process bearing select ranks
	 * 
	 * @param view
	 *            the view
	 */
	public void start(BearingSelectView view) {
		if (view == null) {
			return;
		}

		UiUtils.setEditable(view.getCrankMainMeasurementTextFields().values(),
				true);
		UiUtils.setEditable(
				view.getCrankConrodMeasurementTextFields().values(), true);
		UiUtils.setEditable(view.getConrodMeasurementTextFields().values(),
				true);

		UiUtils.setState(view.getBlockMeasurementTextFields().values(),
				TextFieldState.EDIT);
		UiUtils.setState(view.getCrankMainMeasurementTextFields().values(),
				TextFieldState.EDIT);
		UiUtils.setState(view.getCrankConrodMeasurementTextFields().values(),
				TextFieldState.EDIT);
		UiUtils.setState(view.getConrodMeasurementTextFields().values(),
				TextFieldState.EDIT);

		view.getBlockMeasurementsEditButton().setDisable(false);
		view.getCrankMainMeasurementsEditButton().setDisable(false);
		view.getCrankConrodMeasurementsEditButton().setDisable(false);
		view.getConrodMeasurementsEditButton().setDisable(false);

		view.getDoneButton().setDisable(false);
	}
}
