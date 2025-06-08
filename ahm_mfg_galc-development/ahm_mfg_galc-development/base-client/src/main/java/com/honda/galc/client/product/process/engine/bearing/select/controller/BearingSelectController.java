package com.honda.galc.client.product.process.engine.bearing.select.controller;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.product.process.controller.ProcessControllerAdapter;
import com.honda.galc.client.product.process.engine.bearing.select.model.BearingSelectModel;
import com.honda.galc.client.product.process.engine.bearing.select.validator.BearingSelectValidator;
import com.honda.galc.client.product.process.engine.bearing.select.view.BearingSelectPanel;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingType;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.BearingSelectHelper;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingSelectController</code> is ... .
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
public class BearingSelectController extends ProcessControllerAdapter<BearingSelectPanel> {

	private BearingSelectValidator validator;
	private ClientAudioManager audioManager;

	public BearingSelectController(BearingSelectPanel view) {
		super(view);
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		setModel(new BearingSelectModel(getView().getMainWindow().getApplicationContext()));

		setProcessName("Bearing Select");
		setMnemonicKey(KeyEvent.VK_S);
		setRequired(true);
	}

	// === product client actions start/finish === //
	// === prepare === //
	protected void prepareInitState() {
		UiState.prepare(getView());
	}

	// === start === //
	@Override
	public void startInitState() {
		UiState.start(getView());
	}

	@Override
	protected void startExecute() {

		Engine product = getModel().getProduct();
		String blockPartName = getModel().getProperty().getInstalledBlockPartName();
		NumberType numberType = getModel().getProperty().getInstalledBlockPartNameSnType();

		InstalledPart installedPartBlock = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(product.getProductId(), blockPartName));
		Block block = null;

		if (installedPartBlock == null) {
			String msg = String.format("Block (part name: %s, number type: %s) is not assigned to the engine", blockPartName, numberType);
			addErrorMessage(getView().getMcbTextField(), msg);
		} else {
			block = ServiceFactory.getDao(BlockDao.class).findBySn(installedPartBlock.getPartSerialNumber(), numberType);
			if (block == null) {
				String msg = String.format("Block does not exist for %s number that is assigned to this engine", numberType);
				addErrorMessage(getView().getMcbTextField(), msg);
			} 
			getView().getMcbTextField().setText(installedPartBlock.getPartSerialNumber());
		}

		if (block == null) {
			getView().getMcbEditButton().setText(BearingSelectPanel.RECOVER_ACTION_LABEL);
			getView().getMcbEditButton().setActionCommand(BearingSelectPanel.RECOVER_ACTION_COMMAND);
			getView().getMcbEditButton().setEnabled(true);
			return;
		} else {
			getLogger().info(String.format("Found block: %s", block));
			getModel().setBlock(block);
			getView().getMcbEditButton().setEnabled(getModel().isMcbEditButtonEnabled());
			getView().getDoneButton().setEnabled(true);
		}
		prepareExistingMeasurements();
	}

	// === finish === //
	@Override
	protected void finishExecute() {

		if (getView().getCrankSnCollectCheckBox().isSelected()) {
			processCrankSn();
		}
		if (getView().getConrodSnCollectCheckBox().isSelected()) {
			for (JTextField textField : getView().getConrodSnTextFieldsAsList()) {
				processConrodSn(textField);
			}
		}
		validateTextField(getView().getBlockMeasurementTextFieldsAsList(), getView().getBlockMeasurementsEditButton(), getModel().getProperty().isBlockMeasurementsEditable());
		if (!getView().getCrankSnCollectCheckBox().isSelected()) {
			validateTextField(getView().getCrankMainMeasurementTextFieldsAsList(), getView().getCrankMainMeasurementsEditButton(), isCrankMainMeasurementsEditable());
			validateTextField(getView().getCrankConrodMeasurementTextFieldsAsList(), getView().getCrankConrodMeasurementsEditButton(), isCrankConrodMeasurementsEditable());
		}
		if (!getView().getConrodSnCollectCheckBox().isSelected()) {
			validateTextField(getView().getConrodMeasurementTextFieldsAsList(), getView().getConrodMeasurementsEditButton(), isConrodMeasurementsEditable());
		}

		if (isErrorExists()) {
			return;
		}
		String productId = getModel().getProduct().getProductId();
		String userId = getView().getMainWindow().getUserId();
		String modelYearCode = ProductSpec.extractModelYearCode(getModel().getProduct().getProductSpecCode());
		String modelCode = getModel().getProduct().getModelCode();
		String modelTypeCode = getModel().getProduct().getModelTypeCode();

		Map<Integer, BearingMatrixCell> mainBearings = selectMainBearings(modelYearCode, modelCode, modelTypeCode);
		Map<Integer, BearingMatrixCell> conrodBearings = selectConrodBearings(modelYearCode, modelCode, modelTypeCode);

		if (isErrorExists()) {
			return;
		}

		List<Integer> mainIx = getModel().getMainBearingIxSequence();
		List<Integer> conrodIx = getModel().getConrodIxSequence();
		String blockMeasurements = UiUtils.toString(mainIx, getView().getBlockMeasurementTextFields());
		String crankMainMeasurements = UiUtils.toString(mainIx, getView().getCrankMainMeasurementTextFields());
		String crankConrodMeasurements = UiUtils.toString(conrodIx, getView().getCrankConrodMeasurementTextFields());
		String conrodMeasurements = UiUtils.toString(conrodIx, getView().getConrodMeasurementTextFields());

		BearingSelectResult bearingSelectResult = getModel().assembleBearingSelectResult(modelYearCode, modelCode, mainBearings, conrodBearings);
		bearingSelectResult = getModel().assembleBearingSelectResult(bearingSelectResult, blockMeasurements, crankMainMeasurements, crankConrodMeasurements, conrodMeasurements);

		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		if (getView().getCrankSnCollectCheckBox().isSelected()) {
			InstalledPart crankIp = getModel().assembleInstalledPart(productId, LineSideContainerTag.CRANK_SERIAL_NUMBER, getView().getCrankSnTextField().getText(), userId);
			installedParts.add(crankIp);
		}

		if (getView().getConrodSnCollectCheckBox().isSelected()) {
			Map<Integer, String> conrodSns = UiUtils.getText(getView().getConrodSnTextFields());
			installedParts.addAll(getModel().assembleConrodSnParts(productId, userId, conrodSns));
		}

		StringBuilder sb = new StringBuilder();
		if (installedParts.size() > 0) {
			ServiceFactory.getDao(BearingSelectResultDao.class).save(bearingSelectResult, installedParts);
			if (getLogger().isInfoEnabled()) {
				sb.append("Processed parts:");
				for (InstalledPart ip : installedParts) {
					sb.append(ip.getPartName()).append(":");
					sb.append(ip.getPartSerialNumber()).append(", ");
				}
			}
		} else {
			ServiceFactory.getDao(BearingSelectResultDao.class).save(bearingSelectResult);
		}
		getLogger().info(sb.toString());
		getLogger().info(String.format("Processed %s", bearingSelectResult));
	}

	@Override
	public void finishInitState() {
		UiState.finish(getView());
	}

	// === reset === //
	@Override
	public void resetInitState() {
		UiState.reset(getView());
	}

	// === alreadyProceseed === //
	@Override
	protected void alreadyProcessedInitValues() {
		super.alreadyProcessedInitValues();
	}

	@Override
	protected void alreadyProcessedInitState() {
		UiState.alreadyProcessed(getView());
	}

	@Override
	protected void alreadyProcessedExecute() {
		selectExistingResults();
		super.alreadyProcessedExecute();
	}

	// === notProcessable === //
	@Override
	protected void notProcessableInitValues() {
		super.notProcessableInitValues();
	}

	@Override
	protected void notProcessableInitState() {
		UiState.notProcessable(getView());
	}

	@Override
	protected void notProcessableExecute() {
		selectExistingResults();
		super.notProcessableExecute();
	}

	// === BearingSelect function actions === //
	/**
	 * <pre>
	 * This method will execute the following steps to recover Block:
	 * find block by serial number (id, mcb, dcb) 
	 * check if block is not associated with ein in 185
	 * check if block is in block table
	 * associate block sn to ein in 185 table
	 * migrate attributes in 185 from mcb to ein
	 * migrate attributes in 198 from mcb to ein
	 * in block table update ein to current ein
	 * select block main measurements (from block build results)
	 * select conrod caps measurements (measurements by ein)
	 * 
	 * </pre>
	 */
	public void recoverBlock() {
		TextFieldState.EDIT.setState(getView().getMcbTextField());
		UiUtils.setEditable(getView().getBlockMeasurementTextFields().values(), getModel().getProperty().isBlockMeasurementsEditable());
		UiUtils.setState(getView().getBlockMeasurementTextFields().values(), TextFieldState.EDIT);

		UiUtils.setEditable(getView().getConrodMeasurementTextFields().values(), isConrodMeasurementsEditable());
		UiUtils.setState(getView().getConrodMeasurementTextFields().values(), TextFieldState.EDIT);

		String sn = getView().getMcbTextField().getText();
		getLogger().info(String.format("Start processing SN: %s", sn));
		if (sn != null) {
			sn = sn.trim();
			getView().getMcbTextField().setText(sn);
		}

		validate(getView().getMcbTextField());
		if (isErrorExists()) {
			return;
		}

		Block block = getModel().getBlock();

		NumberType numberType = getModel().getProperty().getInstalledBlockPartNameSnType();
		if (StringUtils.isBlank(block.getSerialNumber(numberType))) {
			String msg = String.format("Can not assign block %s to engine by %s as %s serial number is empty", block, numberType, numberType);
			addErrorMessage(getView().getMcbTextField(), msg);
			return;
		}
		getView().getMcbTextField().setText(block.getSerialNumber(numberType));
		
		List<String> messages = getModel().installBlock(block, getView().getMainWindow().getUserId());
		addMessage(messages);

		BlockDao blockDao = ServiceFactory.getDao(BlockDao.class);
		block = blockDao.findByKey(block.getId());
		getModel().setBlock(block);

		prepareExistingMeasurements();

		TextFieldState.READ_ONLY.setState(getView().getMcbTextField());
		getView().getMcbEditButton().setText(BearingSelectPanel.EDIT_ACTION_LABEL);
		getView().getMcbEditButton().setActionCommand(BearingSelectPanel.EDIT_ACTION_COMMAND);
		getView().getMcbEditButton().setEnabled(getModel().isMcbEditButtonEnabled());

		getView().getDoneButton().setEnabled(true);
		addMessage("Block recovered successfully");
	}

	public void processCrankSn() {

		UiState.initCrankMeasurementTextFieldsForCrankSnEdit(getView());
		String inputNumber = getView().getCrankSnTextField().getText();

		getLogger().info(String.format("Start processing CrankSN: %s", inputNumber));

		if (inputNumber != null) {
			inputNumber = inputNumber.trim();
			getView().getCrankSnTextField().setText(inputNumber);
		}

		validate(getView().getCrankSnTextField());
		if (isErrorExists()) {
			alarmInvalidCrankSn();
			return;
		}

		deriveCrankMeasurementsFromCrankSN(inputNumber);

		validateTextField(getView().getCrankMainMeasurementTextFieldsAsList(), getView().getCrankMainMeasurementsEditButton(), isCrankMainMeasurementsEditable());
		validateTextField(getView().getCrankConrodMeasurementTextFieldsAsList(), getView().getCrankConrodMeasurementsEditButton(), isCrankConrodMeasurementsEditable());
		if (isErrorExists()) {
			addErrorMessage(getView().getCrankSnTextField());
			alarmInvalidCrankSn();
			return;
		}
		TextFieldState.READ_ONLY.setState(getView().getCrankSnTextField());
		getView().getCrankSnEditButton().setEnabled(true);
		alarmValidCrankSn();
	}

	public void processConrodSn(JTextField textField) {

		Integer key = getKey(getView().getConrodSnTextFields(), textField);
		JTextField measurementTextField = getView().getConrodMeasurementTextFields().get(key);

		if (measurementTextField != null) {
			measurementTextField.setText("");
			TextFieldState.EDIT.setState(measurementTextField);
		}

		String inputNumber = textField.getText();
		getLogger().info(String.format("Start processing ConrodSN %s", inputNumber));
		if (inputNumber != null) {
			inputNumber = inputNumber.trim();
			textField.setText(inputNumber);
		}

		validate(textField);
		if (isErrorExists(textField)) {
			return;
		}

		if (getModel().getProperty().getConrodMeasurementStartIx() < 0 || (getModel().getProperty().getConrodMeasurementStartIx() + 1) > inputNumber.length()) {
			String msg = "Could not calculate Conrod Ranking from Conrod SN, please check configuration for CON_LENGTH, CONROD_POSITION";
			addErrorMessage(textField, msg);
			return;
		} else {
			String m = inputNumber.substring(getModel().getProperty().getConrodMeasurementStartIx(), getModel().getProperty().getConrodMeasurementStartIx() + 1);
			if (measurementTextField != null) {
				measurementTextField.setText(m);
				validateTextField(measurementTextField, getView().getConrodMeasurementsEditButton(), isConrodMeasurementsEditable());
			}
		}

		if (isErrorExists(measurementTextField)) {
			addErrorMessage(textField);
			return;
		}

		TextFieldState.READ_ONLY.setState(textField);
		getView().getConrodSnEditButton().setEnabled(true);
	}

	// === pre/post processing , validation === //
	public void validate(JTextField textField) {
		getValidator().validate(textField);
	}

	public void preExecute() {
		setFocusComponent(null);
		clearMessages();
	}

	public void postExecute() {
		processMessages();
		requestFocus();
	}

	// === common api === //
	@Override
	public void clearInputValues() {
		UiState.clear(getView());
	}

	@Override
	protected void clearModel() {
		getModel().setBlock(null);
		super.clearModel();
	}

	// === supporting api === //
	protected void prepareExistingMeasurements() {
		if (getModel().getProperty().isBlockMeasurementsCollected()) {
			Map<Integer, String> measurements = getModel().selectBlockMeasurements();
			UiUtils.setText(getView().getBlockMeasurementTextFields(), measurements);
			getLogger().info(String.format("Found block measurements: %s", measurements));
		}
		if (!getView().getConrodSnCollectCheckBox().isSelected() && getModel().getProperty().isConrodMeasurementsCollected()) {
			Map<Integer, String> measurements = getModel().selectConrodMeasurements(getModel().getProduct());
			UiUtils.setText(getView().getConrodMeasurementTextFields(), measurements);
			getLogger().info(String.format("Found conrod measurements: %s", measurements));
		}

		if (getModel().getProperty().isBlockMeasurementsCollected()) {
			validateTextField(getView().getBlockMeasurementTextFieldsAsList(), getView().getBlockMeasurementsEditButton(), getModel().getProperty().isBlockMeasurementsEditable());
		}
		
		if (!getView().getConrodSnCollectCheckBox().isSelected() && getModel().getProperty().isConrodMeasurementsCollected()) {
			validateTextField(getView().getConrodMeasurementTextFieldsAsList(), getView().getConrodMeasurementsEditButton(), isConrodMeasurementsEditable());
		}
		
		if (getModel().getProperty().isCrankSnCollected()){
			InstalledPart crankSnInstalledPart = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(getModel().getProduct().getProductId(), LineSideContainerTag.CRANK_SERIAL_NUMBER));
			if(crankSnInstalledPart != null && !StringUtils.isEmpty(crankSnInstalledPart.getPartSerialNumber())){
				getView().getCrankSnTextField().setText(crankSnInstalledPart.getPartSerialNumber());
				processCrankSn();
			}
		}

	}

	protected void selectExistingResults() {
		Engine product = getModel().getProduct();
		String blockPartName = getModel().getProperty().getInstalledBlockPartName();

		InstalledPartDao ipDao = ServiceFactory.getDao(InstalledPartDao.class);
		InstalledPart installedPartBlock = ipDao.findByKey(new InstalledPartId(product.getProductId(), blockPartName));
		if (installedPartBlock != null) {
			getView().getMcbTextField().setText(installedPartBlock.getPartSerialNumber());
		}

		InstalledPart crankSn = ipDao.findByKey(new InstalledPartId(product.getProductId(), LineSideContainerTag.CRANK_SERIAL_NUMBER));
		if (crankSn != null) {
			getView().getCrankSnTextField().setText(crankSn.getPartSerialNumber());
		}

		for (Integer ix : getView().getConrodSnTextFields().keySet()) {
			String partName = String.format(LineSideContainerTag.CONROD_SERIAL_NUMBER_X, ix);
			InstalledPart ip = ipDao.findByKey(new InstalledPartId(product.getProductId(), partName));
			if (ip != null) {
				JTextField tf = getView().getConrodSnTextFields().get(ix);
				if (tf != null) {
					tf.setText(ip.getPartSerialNumber());
				}
			}
		}

		BearingSelectResult bsResult = ServiceFactory.getDao(BearingSelectResultDao.class).findByProductId(product.getProductId());
		if (bsResult == null) {
			return;
		}

		String blockString = StringUtils.trim(bsResult.getJournalBlockMeasurements());
		String crankMainString = StringUtils.trim(bsResult.getJournalCrankMeasurements());
		String crankConString = StringUtils.trim(bsResult.getConrodCrankMeasurements());
		String conrodString = StringUtils.trim(bsResult.getConrodConsMeasurements());

		List<Integer> mainIx = getModel().getMainBearingIxSequence();
		List<Integer> conrodIx = getModel().getConrodIxSequence();

		UiUtils.setText(getView().getBlockMeasurementTextFields(), UiUtils.toIxMap(mainIx, blockString));
		UiUtils.setText(getView().getCrankMainMeasurementTextFields(), UiUtils.toIxMap(mainIx, crankMainString));
		UiUtils.setText(getView().getCrankConrodMeasurementTextFields(), UiUtils.toIxMap(conrodIx, crankConString));
		UiUtils.setText(getView().getConrodMeasurementTextFields(), UiUtils.toIxMap(conrodIx, conrodString));
	}

	protected void deriveCrankMeasurementsFromCrankSN(String inputNumber) {

		String crankSnTypeKey = parseCrankSnTypeKey(inputNumber);
		int crankMainStartIx = getCrankMeasurementsStartIx(crankSnTypeKey, getModel().getProperty().getCrankMainMeasurementsStartIx(Integer.class), BearingSelectHelper.CRANK_MAIN_MEASUREMENTS_START_IX);
		int crankConrodStartIx = getCrankMeasurementsStartIx(crankSnTypeKey, getModel().getProperty().getCrankConrodMeasurementsStartIx(Integer.class), BearingSelectHelper.CRANK_CONROD_MEASUREMENTS_START_IX);
		
		int crankMainEndIx = crankMainStartIx + getModel().getMainBearingCount();
		int crankConrodEndIx = crankConrodStartIx + getModel().getConrodCount();
		
		if (crankMainStartIx < 0 || crankMainEndIx > inputNumber.length() || crankConrodStartIx < 0 || crankConrodEndIx > inputNumber.length()) {
			String msg = "Could not calculate Crank Ranking from Crank SN, please check configuration for CRANK_LENGTH, CRANKSHAFT_CONROD_POSITION, CRANKSHAFT_JOURNAL_POSITION";
			addErrorMessage(getView().getCrankSnTextField(), msg);
		} else {
			String crankMainString = inputNumber.substring(crankMainStartIx, crankMainEndIx);
			List<Integer> mainIx = getModel().getMainBearingIxSequence();
			Map<Integer, String> crankMainMeasurements = UiUtils.toIxMap(mainIx, crankMainString);
			UiUtils.setText(getView().getCrankMainMeasurementTextFields(), crankMainMeasurements);

			String crankConString = inputNumber.substring(crankConrodStartIx, crankConrodEndIx);
			List<Integer> conrodIx = getModel().getConrodIxSequence();
			Map<Integer, String> crankConrodMeasurements = UiUtils.toIxMap(conrodIx, crankConString);
			UiUtils.setText(getView().getCrankConrodMeasurementTextFields(), crankConrodMeasurements);
		}
	}
	
	protected String parseCrankSnTypeKey(String crankSn) {
		if (StringUtils.isBlank(crankSn)) {
			return null;
		}
		int[] keyIx = StringUtil.toIntArray(getModel().getProperty().getCrankSnTypeIx());
		if (keyIx == null || keyIx.length < 2) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; (i + 1) < keyIx.length; i = i + 2) {
			int startIx = keyIx[i];
			int length = keyIx[i + 1];
			if (startIx < 0 || length < 0 || (startIx + length) > crankSn.length()) {
				continue;
			}
			String token = crankSn.substring(startIx, startIx + length);
			sb.append(token);
		}
		if (sb.length() > 0) {
			return sb.toString();
		}
		return null;
	}

	protected int getCrankMeasurementsStartIx(String crankSnTypeKey, Map<String, Integer> crankMeasurementsStartIxMap, int defaultIx) {
		if (crankMeasurementsStartIxMap == null) {
			return defaultIx;
		}
		if (StringUtils.isNotBlank(crankSnTypeKey)) {
			Integer value = crankMeasurementsStartIxMap.get(crankSnTypeKey);
			if (value != null) {
				return value;
			}
		}
		Integer value = crankMeasurementsStartIxMap.get("*");
		if (value != null) {
			return value;
		}
		return defaultIx;
	}

	protected Map<Integer, BearingMatrixCell> selectMainBearings(String modelYearCode, String modelCode, String modelTypeCode) {

		Map<Integer, BearingMatrixCell> bearingMap = new HashMap<Integer, BearingMatrixCell>();
		StringBuilder sb = new StringBuilder();

		for (Integer ix : getModel().getMainBearingIxSequence()) {

			JTextField blockTextField = getView().getBlockMeasurementTextFields().get(ix);
			String blockMeasurement = blockTextField.getText();
			JTextField crankMainTextField = getView().getCrankMainMeasurementTextFields().get(ix);
			String crankMainMeasurement = crankMainTextField.getText();
			String journalPosition = ix.toString();
			BearingMatrixCell matrixCell = BearingSelectHelper.selectBearing(BearingType.Main, modelYearCode, modelCode, modelTypeCode, journalPosition, blockMeasurement, crankMainMeasurement);
			bearingMap.put(ix, matrixCell);
			if (matrixCell == null) {
				if (getModel().getProperty().isCrankSnCollect()) {
					addErrorMessage(getView().getCrankSnTextField());
				}
				sb.append("[").append(blockMeasurement).append(", ").append(crankMainMeasurement).append("]");
				addErrorMessage(blockTextField);
				addErrorMessage(crankMainTextField);
			}
		}
		if (sb.length() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Could not match main bearings for YM : ").append(modelYearCode).append(modelCode);
			msg.append(", measurements : ").append(sb);
			addErrorMessage(msg.toString());
		}
		return bearingMap;
	}

	protected Map<Integer, BearingMatrixCell> selectConrodBearings(String modelYearCode, String modelCode, String modelTypeCode) {

		Map<Integer, BearingMatrixCell> bearingMap = new HashMap<Integer, BearingMatrixCell>();
		StringBuilder sb = new StringBuilder();

		for (Integer ix : getView().getCrankConrodMeasurementTextFields().keySet()) {
			JTextField crankConrodTextField = getView().getCrankConrodMeasurementTextFields().get(ix);
			String crankConrodMeasurement = crankConrodTextField.getText();
			JTextField conrodTextField = getView().getConrodMeasurementTextFields().get(ix);
			String conrodMeasurement = conrodTextField.getText();
			String journalPosition = "*";
			BearingMatrixCell matrixCell = BearingSelectHelper.selectBearing(BearingType.Conrod, modelYearCode, modelCode, modelTypeCode, journalPosition, conrodMeasurement, crankConrodMeasurement);
			bearingMap.put(ix, matrixCell);

			if (matrixCell == null) {
				if (getModel().getProperty().isCrankSnCollect()) {
					addErrorMessage(getView().getCrankSnTextField());
				}
				if (getModel().getProperty().isConrodSnCollect()) {
					addErrorMessage(getView().getConrodSnTextFields().get(ix));
				}
				sb.append("[").append(crankConrodMeasurement).append(", ").append(conrodMeasurement).append("]");
				addErrorMessage(crankConrodTextField);
				addErrorMessage(conrodTextField);
			}
		}
		if (sb.length() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Could not match conrod bearings for YM:").append(modelYearCode).append(modelCode);
			msg.append(", measurements:").append(sb);
			addErrorMessage(msg.toString());
		}
		return bearingMap;
	}

	protected void alarmInvalidCrankSn() {
		if (getModel().getProperty().isAlarmInvalidCrankSn()) {
			try {
				getAudioManager().playNGSound();
			} catch (Exception e) {
				getLogger().warn(e);
			}
		}
	}
	
	protected void alarmValidCrankSn() {
		if (getModel().getProperty().isAlarmValidCrankSn()) {
			try {
				getAudioManager().playOKSound();
			} catch (Exception e) {
				getLogger().warn(e);
			}
		}
	}	

	// === controling api overides === //
	@Override
	protected boolean isAlreadyProcessed() {
		return getModel().isBearingSlectResultExists(getModel().getProduct());
	}

	@Override
	protected boolean isNotProcessable() {
		return getModel().isNotProcessable(getModel().getProduct());
	}

	// === supporting api === //
	public Integer getKey(Map<Integer, JTextField> textFields, JTextField textField) {
		if (textFields == null || textField == null) {
			return null;
		}
		for (Integer i : textFields.keySet()) {
			if (textField.equals(textFields.get(i))) {
				return i;
			}
		}
		return null;
	}

	@Override
	public JButton getFirstFocusableButton() {
		if (getView().getDoneButton().isEnabled()) {
			return getView().getDoneButton();
		} else {
			return null;
		}
	}

	@Override
	protected List<JTextField> getFocusableTextFields() {
		List<JTextField> list = new ArrayList<JTextField>();

		list.add(getView().getMcbTextField());
		list.add(getView().getCrankSnTextField());
		list.addAll(getView().getConrodSnTextFieldsAsList());

		list.addAll(getView().getBlockMeasurementTextFieldsAsList());
		list.addAll(getView().getCrankMainMeasurementTextFieldsAsList());
		list.addAll(getView().getCrankConrodMeasurementTextFieldsAsList());
		list.addAll(getView().getConrodMeasurementTextFieldsAsList());

		return list;
	}

	public JTextField getNextFocusableTextField(JTextField currentTextField) {
		List<JTextField> list = getFocusableTextFields();
		int ix = list.indexOf(currentTextField);
		List<JTextField> list2 = list.subList(ix + 1, list.size());
		List<JTextField> list3 = list.subList(0, ix + 1);
		List<JTextField> list0 = new ArrayList<JTextField>();
		list0.addAll(list2);
		list0.addAll(list3);
		for (JTextField tf : list0) {
			if (tf.isEnabled() && tf.isEditable()) {
				return tf;
			}
		}
		return null;
	}

	public void validateTextField(List<JTextField> textFields, JButton button, boolean enableButton) {
		if (textFields == null) {
			return;
		}
		for (JTextField textField : textFields) {
			validateTextField(textField, button, enableButton);
		}
	}

	public void validateTextField(JTextField textField, JButton button, boolean enableButton) {
		if (textField == null) {
			return;
		}
		validate(textField);
		if (enableButton && button != null && !button.isEnabled()) {
			button.setEnabled(true);
		}
	}

	// === main/conrod derived config api === //
	public boolean isCrankMainMeasurementsEditable() {
		boolean editable = !getView().getCrankSnCollectCheckBox().isSelected();
		return editable;
	}

	public boolean isCrankConrodMeasurementsEditable() {
		return isCrankMainMeasurementsEditable();
	}

	public boolean isConrodMeasurementsEditable() {
		boolean editable = !getView().getConrodSnCollectCheckBox().isSelected();
		return editable;
	}

	// === get/set === //
	public BearingSelectValidator getValidator() {
		return validator;
	}

	public void setValidator(BearingSelectValidator validator) {
		this.validator = validator;
	}

	@Override
	public BearingSelectModel getModel() {
		return (BearingSelectModel) super.getModel();
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}
}
