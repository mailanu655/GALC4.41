package com.honda.galc.client.product.process.engine.bearing.pick.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.process.controller.ProcessControllerAdapter;
import com.honda.galc.client.product.process.engine.bearing.pick.model.BearingPickModel;
import com.honda.galc.client.product.process.engine.bearing.pick.model.BearingPickModel.BearingPartType;
import com.honda.galc.client.product.process.engine.bearing.pick.view.BearingPickPanel;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.product.BearingSelectResult;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>BearingPickController</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
public class BearingPickController extends ProcessControllerAdapter<BearingPickPanel> {

	BearingMatrix bearingMatrix;
	String modelYearCode;
	String modelCode;

	public BearingPickController(BearingPickPanel view) {
		super(view);
		setModel(new BearingPickModel(getView().getMainWindow().getApplicationContext()));

		setProcessName("Bearing Pick");
		setMnemonicKey(KeyEvent.VK_P);
		setRequired(true);
		this.modelCode = "";
		this.modelYearCode = "";
	}

	// === process view api === //
	// === prepare === //
	@Override
	protected void prepareInitState() {
		disabledInputState();
	}

	// === start === //
	@Override
	public void startInitState() {

		UiUtils.setState(getView().getBlockMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getCrankMainMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getCrankConrodMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodMeasurementTextFields().values(), TextFieldState.DISABLED);

		UiUtils.setState(getView().getMainUpperBearingTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getMainLowerBearingTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodUpperBearingTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodLowerBearingTextFields().values(), TextFieldState.DISABLED);

		if (getView().getDoneButton() != null)
			getView().getDoneButton().setEnabled(false);

	}

	@Override
	protected void startExecute() {

		BearingSelectResult result = getModel().selectMeasurements(getModel().getProduct());

		getLogger().info(String.format("Found : %s", result));

		String blockString = StringUtils.trim(result.getJournalBlockMeasurements());
		String crankMainString = StringUtils.trim(result.getJournalCrankMeasurements());
		String crankConString = StringUtils.trim(result.getConrodCrankMeasurements());
		String conrodString = StringUtils.trim(result.getConrodConsMeasurements());

		// The Bearing Pick Client needs the ability to dynamically switch
		// between L4 or V6 depending on the engine model.
		if (getModel().getProperty().getUseBearingMatrixForBearingPick()
				&& !(this.modelCode.equalsIgnoreCase(result.getModelCode())
						&& this.modelYearCode.equalsIgnoreCase(result.getModelYearCode()))) {
			this.modelCode = result.getModelCode();
			this.modelYearCode = result.getModelYearCode();
			this.bearingMatrix = getModel().selectBearingMatrix(result);
			getView().removeAll();
			getView().initView();
			getView().mapActions();
			resetInitState();
		}

		List<Integer> mainIx = getModel().getMainBearingIxSequence(this.bearingMatrix);
		List<Integer> conrodIx = getModel().getConrodIxSequence(this.bearingMatrix);
		Map<Integer, String> blockMeasurements = UiUtils.toIxMap(mainIx, blockString);
		Map<Integer, String> crankMainMeasurements = UiUtils.toIxMap(mainIx, crankMainString);
		Map<Integer, String> crankConrodMeasurements = UiUtils.toIxMap(conrodIx, crankConString);
		Map<Integer, String> conrodMeasurements = UiUtils.toIxMap(conrodIx, conrodString);

		UiUtils.setText(getView().getBlockMeasurementTextFields(), blockMeasurements);
		UiUtils.setText(getView().getCrankMainMeasurementTextFields(), crankMainMeasurements);
		UiUtils.setText(getView().getCrankConrodMeasurementTextFields(), crankConrodMeasurements);
		UiUtils.setText(getView().getConrodMeasurementTextFields(), conrodMeasurements);

		Map<Integer, BearingPart> mainUpper = getModel().selectMainUpperBearings(result);
		Map<Integer, BearingPart> mainLower = getModel().selectMainLowerBearings(result);
		Map<Integer, BearingPart> conrodUpper = getModel().selectConrodUpperBearings(result);
		Map<Integer, BearingPart> conrodLower = getModel().selectConrodLowerBearings(result);

		setBearingColors(getView().getMainUpperBearingTextFields(), mainUpper,
				getModel().getBearingPartTypes().contains(BearingPartType.MAIN_UPPER));
		setBearingColors(getView().getMainLowerBearingTextFields(), mainLower,
				getModel().getBearingPartTypes().contains(BearingPartType.MAIN_LOWER));

		setBearingColors(getView().getConrodUpperBearingTextFields(), conrodUpper,
				getModel().getBearingPartTypes().contains(BearingPartType.CONROD_UPPER));
		setBearingColors(getView().getConrodLowerBearingTextFields(), conrodLower,
				getModel().getBearingPartTypes().contains(BearingPartType.CONROD_LOWER));

		getLogger().info(String.format("Main Upper:  %s:", mainUpper));
		getLogger().info(String.format("Main Lower:  %s:", mainLower));
		getLogger().info(String.format("Conrod Upper:%s:", conrodUpper));
		getLogger().info(String.format("Conrod Lower:%s:", conrodLower));

		if (getView().getDoneButton() != null) {
			getView().getDoneButton().setEnabled(true);
			setFocusComponent(getView().getDoneButton());
		}
	}

	// === reset === //
	@Override
	public void resetInitState() {

		UiUtils.setState(getView().getBlockMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getCrankMainMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getCrankConrodMeasurementTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodMeasurementTextFields().values(), TextFieldState.DISABLED);

		UiUtils.setState(getView().getMainUpperBearingTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getMainLowerBearingTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodUpperBearingTextFields().values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodLowerBearingTextFields().values(), TextFieldState.DISABLED);

		if (getView().getDoneButton() != null)
			getView().getDoneButton().setEnabled(false);
	}

	// === not processable === //
	@Override
	protected boolean isNotProcessable() {
		BearingSelectResult result = getModel().selectMeasurements(getModel().getProduct());
		if (result == null) {
			// addMessage("There is no Bearing Result for this product");
			addErrorMessage("There is no Bearing Result for this product");
			return true;
		}
		return false;
	}

	// === common api === //
	@Override
	protected void clearInputValues() {

		setFocusComponent(null);
		clearMessages();

		UiUtils.setText(getView().getBlockMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getCrankMainMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getCrankConrodMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getConrodMeasurementTextFields().values(), "");

		UiUtils.setText(getView().getMainUpperBearingTextFields().values(), "");
		UiUtils.setText(getView().getMainLowerBearingTextFields().values(), "");
		UiUtils.setText(getView().getConrodUpperBearingTextFields().values(), "");
		UiUtils.setText(getView().getConrodLowerBearingTextFields().values(), "");

	}

	protected void disabledInputState() {
		resetInitState();
	}

	protected void setBearingColors(Map<Integer, JTextField> textFields, Map<Integer, BearingPart> bearings,
			boolean highLight) {
		if (textFields == null || bearings == null) {
			return;
		}
		for (Integer ix : textFields.keySet()) {
			JTextField textField = textFields.get(ix);
			BearingPart bearingPart = bearings.get(ix);
			if (textField == null) {
				continue;
			}
			if (bearingPart == null) {
				textField.setText("");
			} else {
				textField.setText(bearingPart.getColor());
				if (highLight) {
					Color color = getModel().getColor(bearingPart.getColor());
					if (color != null) {
						textField.setBackground(color);
						textField.setForeground(Color.BLACK);
						textField.setDisabledTextColor(Color.BLACK);
						if (color.equals(Color.BLACK)) {
							textField.setForeground(Color.WHITE);
							textField.setDisabledTextColor(Color.WHITE);
						}
					}
				}
			}
		}
	}

	// === get/set === //
	@Override
	public BearingPickModel getModel() {
		return (BearingPickModel) super.getModel();
	}

	public BearingMatrix getBearingMatrix() {
		return this.bearingMatrix;
	}

}
