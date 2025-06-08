package com.honda.galc.client.dc.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.BearingSelectPropertyBean;
import com.honda.galc.client.dc.validator.BearingSelectValidator;
import com.honda.galc.client.dc.view.BearingSelectView;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.bearing.BearingMatrixCellDao;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixCellId;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.bearing.BearingType;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.BearingSelectResultId;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * <h3>BearingSelectProcessor</h3>
 * <h3>The class is the  operation view for the bearing select  </h3>
 * <h4>  </h4>
 * <p> The operation view for BearingPickView must be {@link BearingSelectView}.</p>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>.
 *
 * @see BearingSelectView
 * @author Hale Xie
 * May 30, 2014
 */
public class BearingSelectProcessor extends BearingProcessor {
	
	/** The Constant DATE_TOKEN_PATTERN. */
	private static final String DATE_TOKEN_PATTERN = "[0-9][0-9ABC][0-9][0-9]";
	
	/** The Constant LINE_TOKEN_PATTERN. */
	private static final String LINE_TOKEN_PATTERN = "[0-9]";
	
	/** The block. */
	private Block block;
	
	/** The view. */
	private BearingSelectView view;
	
	/** The validator. */
	private BearingSelectValidator validator;
	
	/** The audio manager. */
	private ClientAudioManager audioManager;
	
	private boolean initialized = false;
	
	private BearingSelectUiState uiState = new BearingSelectUiState();

	/**
	 * Instantiates a new bearing select processor.
	 *
	 * @param controller the controller
	 * @param structure the structure
	 */
	public BearingSelectProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		setAudioManager(new ClientAudioManager(PropertyService.getPropertyBean(
				AudioPropertyBean.class, getApplicationContext()
						.getProcessPointId())));
		setProperty(PropertyService.getPropertyBean(
				BearingSelectPropertyBean.class, getApplicationContext()
						.getProcessPointId()));
	}

	/**
	 * Already processed.
	 */
	public void alreadyProcessed() {
		alreadyProcessedInitState();
		alreadyProcessedExecute();
		postExecute();
	}

	/**
	 * Already processed execute.
	 */
	protected void alreadyProcessedExecute() {
		selectExistingResults();
	}

	/**
	 * Already processed init state.
	 */
	protected void alreadyProcessedInitState() {
		uiState.alreadyProcessed(getView());
	}

	/**
	 * Assemble bearing select result.
	 *
	 * @param result the result
	 * @param blockMeasurements the block measurements
	 * @param crankMainMeasurements the crank main measurements
	 * @param crankConrodMeasurements the crank conrod measurements
	 * @param conrodMeasurements the conrod measurements
	 * @return the bearing select result
	 */
	protected BearingSelectResult assembleBearingSelectResult(
			BearingSelectResult result, String blockMeasurements,
			String crankMainMeasurements, String crankConrodMeasurements,
			String conrodMeasurements) {

		result.setJournalBlockMeasurements(blockMeasurements);
		result.setJournalCrankMeasurements(crankMainMeasurements);
		result.setConrodCrankMeasurements(crankConrodMeasurements);
		result.setConrodConsMeasurements(conrodMeasurements);

		return result;
	}

	/**
	 * Assemble bearing select result.
	 *
	 * @param modelYearCode the model year code
	 * @param modelCode the model code
	 * @param mainBearings the main bearings
	 * @param conrodBearings the conrod bearings
	 * @return the bearing select result
	 */
	protected BearingSelectResult assembleBearingSelectResult(
			String modelYearCode, String modelCode,
			Map<Integer, BearingMatrixCell> mainBearings,
			Map<Integer, BearingMatrixCell> conrodBearings) {

		BearingSelectResult result = new BearingSelectResult();

		BearingSelectResultId id = new BearingSelectResultId();
		id.setProductId(getController().getProductModel().getProductId());
		id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));

		result.setId(id);
		result.setModelYearCode(modelYearCode);
		result.setModelCode(modelCode);

		for (Integer ix : mainBearings.keySet()) {
			BearingMatrixCell bmc = mainBearings.get(ix);
			setMainBearings(result, bmc, ix);
		}

		for (Integer ix : conrodBearings.keySet()) {
			BearingMatrixCell bmc = conrodBearings.get(ix);
			setConrodBearings(result, bmc, ix);
		}

		return result;
	}

	/**
	 * Assemble conrod sn parts.
	 *
	 * @param productId the product id
	 * @param userId the user id
	 * @param indexedPartSerialNumbers the indexed part serial numbers
	 * @return the list
	 */
	protected List<InstalledPart> assembleConrodSnParts(String productId,
			String userId, Map<Integer, String> indexedPartSerialNumbers) {

		List<InstalledPart> parts = new ArrayList<InstalledPart>();
		for (Integer ix : indexedPartSerialNumbers.keySet()) {
			String partName = String.format(
					LineSideContainerTag.CONROD_SERIAL_NUMBER_X, ix);
			String psn = indexedPartSerialNumbers.get(ix);
			InstalledPart ip = assembleInstalledPart(productId, partName, psn,
					userId);
			parts.add(ip);
		}
		return parts;
	}

	/**
	 * Assemble installed part.
	 *
	 * @param productId the product id
	 * @param partName the part name
	 * @param psn the psn
	 * @param userId the user id
	 * @return the installed part
	 */
	protected InstalledPart assembleInstalledPart(String productId,
			String partName, String psn, String userId) {
		InstalledPart part = new InstalledPart(productId, partName);
		part.setPartSerialNumber(psn);
		part.setInstalledPartStatus(InstalledPartStatus.OK);
		part.setAssociateNo(userId);
		return part;
	}

	/**
	 * Finish.
	 */
	public void finish() {
		getController().clearMessages();
		if(isAlreadyProcessed()){
			//Reject is clicked
			if(isRejectionValid()) {
				getView().getDoneButton().setText("Done");
				getView().getDoneButton().setGraphic(getImageView(getDoneImage()));
				DataCollectionEvent dataCollectionEvent = new DataCollectionEvent(DataCollectionEventType.PDDA_REJECT, null);
				dataCollectionEvent.setOperation(getOperation());
				EventBusUtil.publish(dataCollectionEvent);
				//Need to use Platform.runLater for current version of java 1.8,
				//otherwise screen is not refreshing properly
				Platform.runLater(new Runnable() {
				     public void run() {
				    	 getView().resetView(); 
				    	 getView().refreshView();
				     }
				});
			}
		}
		else {
			finishExecute();
			if (getController().isErrorExists()) {
				postExecute();
			} else {
				finishInitValues();
				finishInitState();
				postExecute();
				getView().getDoneButton().setDisable(true);
				getView().getDoneButton().setText("Reject");
				getView().getDoneButton().setGraphic(getImageView(getRejectImage()));
				completeOperation(true);
			}
		}
	}
	
	private boolean isRejectionValid() {
		String bearingPickOpName = StringUtils
				.isNotBlank(getProperty().getBearingPickOperations().get(getOperation().getCommonName()))
						? getProperty().getBearingPickOperations().get(getOperation().getCommonName())
						: getProperty().getBearingPickOperations().get(getOperation().getId().getOperationName());

		if (StringUtils.isNotBlank(bearingPickOpName)) {
		List<String> bearingPickOpNames = ServiceFactory.getDao(MCOperationRevisionDao.class).getAllListPickOperations(getOperation().getStructure().getId().getRevision(), bearingPickOpName);
		
		if(!bearingPickOpNames.isEmpty()) {
			//Check if Bearing Pick Operation is completed
			Engine product = (Engine) getController().getProductModel().getProduct();
			List<String> partNames = new ArrayList<String>();
			for (String bearingPickOperation : bearingPickOpNames) {
				partNames.add(bearingPickOperation);
			}
			if(partNames.size() > 0) {
				List<InstalledPart> installParts = ServiceFactory.getService(InstalledPartDao.class).findAllByProductIdAndPartNames(product.getProductId(), partNames);
					if(installParts != null && installParts.size() > 0 ) {
						for (InstalledPart installedPart : installParts) {
							if(installedPart.isStatusOk()) {
								String unitNo = installedPart.getPartName().split(Delimiter.UNDERSCORE)[0];
									getController().displayErrorMessage("Bearing Pick operation ("+unitNo+") is completed. Please reject '"+unitNo+"' before rejecting this operation.");
									return false;
								}
							} 
						}
					}
		    }
		}
		return true;
	}

	/**
	 * Finish execute. The function validates bearing select input and save bearing select result to database
	 */
	protected void finishExecute() {

		validateTextField(getView().getBlockMeasurementTextFieldsAsList(),
				getView().getBlockMeasurementsEditButton(), getProperty()
						.isBlockMeasurementsEditable());
		validateTextField(getView()
				.getCrankMainMeasurementTextFieldsAsList(), getView()
				.getCrankMainMeasurementsEditButton(),
				isCrankMainMeasurementsEditable());
		validateTextField(getView()
				.getCrankConrodMeasurementTextFieldsAsList(), getView()
				.getCrankConrodMeasurementsEditButton(),
				isCrankConrodMeasurementsEditable());
		validateTextField(getView().getConrodMeasurementTextFieldsAsList(),
				getView().getConrodMeasurementsEditButton(),
				isConrodMeasurementsEditable());

		if (getController().isErrorExists()) {
			return;
		}
		String modelYearCode = ProductSpec.extractModelYearCode(getController()
				.getProductModel().getProduct().getProductSpecCode());
		String modelCode = getController().getProductModel().getProduct()
				.getModelCode();
		String modelTypeCode = ((Product) getController().getProductModel()
				.getProduct()).getModelTypeCode();

		Map<Integer, BearingMatrixCell> mainBearings = selectMainBearings(
				modelYearCode, modelCode, modelTypeCode);
		Map<Integer, BearingMatrixCell> conrodBearings = selectConrodBearings(
				modelYearCode, modelCode, modelTypeCode);

		if (getController().isErrorExists()) {
			return;
		}

		List<Integer> mainIx = getMainBearingIxSequence();
		List<Integer> crankConrodIx = getConrodIxSequence();
		List<Integer> conrodIx = getConrodIxSequence();
		
		String blockMeasurements = UiUtils.toString(mainIx, getView()
				.getBlockMeasurementTextFields());
		String crankMainMeasurements = UiUtils.toString(mainIx, getView()
				.getCrankMainMeasurementTextFields());
		String crankConrodMeasurements = UiUtils.toString(crankConrodIx, getView()
				.getCrankConrodMeasurementTextFields());
		String conrodMeasurements = UiUtils.toString(conrodIx, getView().getConrodMeasurementTextFields());

		BearingSelectResult bearingSelectResult = assembleBearingSelectResult(
				modelYearCode, modelCode, mainBearings, conrodBearings);
		bearingSelectResult = assembleBearingSelectResult(bearingSelectResult,
				blockMeasurements, crankMainMeasurements,
				crankConrodMeasurements, conrodMeasurements);

		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();

		StringBuilder sb = new StringBuilder();
		if (installedParts.size() > 0) {
			ServiceFactory.getDao(BearingSelectResultDao.class).save(
					bearingSelectResult, installedParts);
			if (getLogger().isInfoEnabled()) {
				sb.append("Processed parts:");
				for (InstalledPart ip : installedParts) {
					sb.append(ip.getPartName()).append(":");
					sb.append(ip.getPartSerialNumber()).append(", ");
				}
			}
		} else {
			ServiceFactory.getDao(BearingSelectResultDao.class).save(
					bearingSelectResult);
		}
		getLogger().info(sb.toString());
		getLogger().info(String.format("Processed %s", bearingSelectResult));
	}

	/**
	 * Finish init state.
	 */
	public void finishInitState() {
		uiState.finish(getView());
	}

	/**
	 * Finish init values.
	 */
	protected void finishInitValues() {

	}

	/**
	 * Gets the audio manager.
	 *
	 * @return the audio manager
	 */
	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	/**
	 * Gets the block.
	 *
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * Gets the block input number lengths.
	 *
	 * @return the block input number lengths
	 */
	public List<Integer> getBlockInputNumberLengths() {
		List<ProductTypeData> types = getApplicationContext()
				.getProductTypeDataList();
		List<Integer> defaultList = new ArrayList<Integer>();
		defaultList.add(ProductNumberDef.MCB.getLength());
		ProductTypeData type = null;
		if (types == null) {
			return defaultList;
		}
		for (ProductTypeData pdt : types) {
			if (ProductType.BLOCK.name().equals(pdt.getId())) {
				type = pdt;
			}
		}
		if (type == null) {
			return defaultList;
		}
		List<ProductNumberDef> defs = type.getProductNumberDefs();
		if (defs == null || defs.isEmpty()) {
			return defaultList;
		}

		List<Integer> list = new ArrayList<Integer>();
		for (ProductNumberDef def : defs) {
			list.add(def.getLength());
		}
		if (list.isEmpty()) {
			return defaultList;
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * Gets the block input number max length.
	 *
	 * @return the block input number max length
	 */
	public Integer getBlockInputNumberMaxLength() {
		List<Integer> lengths = getBlockInputNumberLengths();
		if (lengths.size() == 0) {
			return ProductNumberDef.MCB.getLength();
		}
		if (lengths.size() == 1) {
			return lengths.get(0);
		}
		Collections.sort(lengths);
		Collections.reverse(lengths);
		return lengths.get(0);
	}

	/**
	 * Gets the block main ix display sequence.
	 *
	 * @return the block main ix display sequence
	 */
	public List<Integer> getBlockMainIxDisplaySequence() {
		List<Integer> list = getMainBearingIxSequence();
		if (getProperty().isBlockMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * Gets the block measurement pattern.
	 *
	 * @return the block measurement pattern
	 */
	public String getBlockMeasurementPattern() {
		return getProperty().getBlockMeasurementPattern();
	}

	/**
	 * Gets the conrod ix display sequence.
	 *
	 * @return the conrod ix display sequence
	 */
	public List<Integer> getConrodIxDisplaySequence() {
		List<Integer> list = getConrodIxSequence();
		if (getProperty().isConrodMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}
	
	/**
	 * Gets the conrod measurement pattern.
	 *
	 * @return the conrod measurement pattern
	 */
	public String getConrodMeasurementPattern() {
		return getProperty().getConrodMeasurementPattern();
	}

	/**
	 * Gets the crank conrod ix display sequence.
	 *
	 * @return the crank conrod ix display sequence
	 */
	public List<Integer> getCrankConrodIxDisplaySequence() {
		List<Integer> list = getConrodIxSequence();
		if (getProperty().isCrankMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * Gets the crank conrod measurement pattern.
	 *
	 * @return the crank conrod measurement pattern
	 */
	public String getCrankConrodMeasurementPattern() {
		return getProperty().getCrankConrodMeasurementPattern();
	}

	/**
	 * Gets the crank main ix display sequence.
	 *
	 * @return the crank main ix display sequence
	 */
	public List<Integer> getCrankMainIxDisplaySequence() {
		List<Integer> list = getMainBearingIxSequence();
		if (getProperty().isCrankMeasurementsDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * Gets the crank main measurement pattern.
	 *
	 * @return the crank main measurement pattern
	 */
	public String getCrankMainMeasurementPattern() {
		return getProperty().getCrankMainMeasurementPattern();
	}

	/**
	 * Gets the date token pattern.
	 *
	 * @return the date token pattern
	 */
	public String getDateTokenPattern() {
		return DATE_TOKEN_PATTERN;
	}

	/**
	 * Gets the first focusable button.
	 *
	 * @return the first focusable button
	 */
	public Button getFirstFocusableButton() {
		if (!getView().getDoneButton().isDisabled()) {
			return getView().getDoneButton();
		} else {
			return null;
		}
	}

	/**
	 * Gets the focusable text fields.
	 *
	 * @return the focusable text fields
	 */
	protected List<TextField> getFocusableTextFields() {
		List<TextField> list = new ArrayList<TextField>();

		//list.add(getView().getMcbTextField());

		list.addAll(getView().getBlockMeasurementTextFieldsAsList());
		list.addAll(getView().getCrankMainMeasurementTextFieldsAsList());
		list.addAll(getView().getCrankConrodMeasurementTextFieldsAsList());
		list.addAll(getView().getConrodMeasurementTextFieldsAsList());

		return list;
	}

	// === supporting api === //
	/**
	 * Gets the key.
	 *
	 * @param textFields the text fields
	 * @param textField the text field
	 * @return the key
	 */
	public Integer getKey(Map<Integer, TextField> textFields,
			TextField textField) {
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

	/**
	 * Gets the line token pattern.
	 *
	 * @return the line token pattern
	 */
	public String getLineTokenPattern() {
		return LINE_TOKEN_PATTERN;
	}

	/**
	 * Gets the next focusable text field.
	 *
	 * @param currentTextField the current text field
	 * @return the next focusable text field
	 */
	public TextField getNextFocusableTextField(TextField currentTextField) {
		
		List<TextField> list = getFocusableTextFields();
		if(currentTextField==null){
			for (TextField tf : list) {
				if (!tf.isDisabled() && tf.isEditable()) {
					return tf;
				}
			}
		}
		int ix = list.indexOf(currentTextField);
		List<TextField> list2 = list.subList(ix + 1, list.size());
		List<TextField> list3 = list.subList(0, ix + 1);
		List<TextField> list0 = new ArrayList<TextField>();
		list0.addAll(list2);
		list0.addAll(list3);
		for (TextField tf : list0) {
			if (!tf.isDisabled() && tf.isEditable()) {
				return tf;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.processor.BearingProcessor#getProperty()
	 */
	public BearingSelectPropertyBean getProperty() {
		return (BearingSelectPropertyBean) super.getProperty();
	}

	/**
	 * Gets the validator.
	 *
	 * @return the validator
	 */
	public BearingSelectValidator getValidator() {
		return validator;
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public BearingSelectView getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.processor.OperationProcessor#init()
	 */
	@Override
	public void init() {

	}

	/**
	 * Checks if is already processed.
	 *
	 * @return true, if is already processed
	 */
	public boolean isAlreadyProcessed() {
		return getController().getModel().isCurrentOperationComplete();
	}
	
	/**
	 * Checks if Block measurements editable.
	 * 
	 * @return true, if Block measurements editable
	 */
	public boolean isBlockMeasurementsEditable() {
		return true;
	}

	/**
	 * Checks if is conrod measurements editable.
	 *
	 * @return true, if is conrod measurements editable
	 */
	public boolean isConrodMeasurementsEditable() {
		return true;
	}

	/**
	 * Checks if is crank conrod measurements editable.
	 *
	 * @return true, if is crank conrod measurements editable
	 */
	public boolean isCrankConrodMeasurementsEditable() {
		return isCrankMainMeasurementsEditable();
	}

	// === main/conrod derived config api === //
	/**
	 * Checks if is crank main measurements editable.
	 *
	 * @return true, if is crank main measurements editable
	 */
	public boolean isCrankMainMeasurementsEditable() {
		return true;
	}

	/**
	 * Checks if is mcb edit button enabled.
	 *
	 * @return true, if is mcb edit button enabled
	 */
	public boolean isMcbEditButtonEnabled() {
		// REMARK: always false,
		// it is enabled only for recovery, not for simple edit.
		return false;
	}

	/**
	 * Checks if is not processable.
	 *
	 * @param product the product
	 * @return true, if is not processable
	 */
	public boolean isNotProcessable(BaseProduct product) {
		return false;
	}

	/**
	 * Post execute.
	 */
	public void postExecute() {
		getController().processMessages();
		//locate the next text field if possible
		getController().requestFocus();
		
		if(isInputComplete()){
			//No text field or other button is focused. so we should focus on Done Button
			getController().setFocusComponent(getFirstFocusableButton());
			getController().requestFocus();
		}
	}
	
	/**
	 * Checks if is input complete.
	 *
	 * @return true, if is input complete
	 */
	public boolean isInputComplete(){
		List<TextField> textFields = getFocusableTextFields();
		for(TextField tf:textFields){
			if(!tf.isDisable() && tf.isEditable()){
				return false;
			}
		}
		return true;
	}

	/**
	 * Pre execute.
	 */
	public void preExecute() {
		//this.getController().setFocusComponent(null);
		this.getController().clearMessages();
	}

	/**
	 * Prepare existing measurements.
	 */
	protected void prepareExistingMeasurements() {
		if (getProperty().isBlockMeasurementsCollected()) {
			Map<Integer, String> measurements = selectBlockMeasurements();
			UiUtils.setText(getView().getBlockMeasurementTextFields(),
					measurements);
			getLogger()
					.info(String.format("Found block measurements: %s",
							measurements));
		}
		if (getProperty().isConrodMeasurementsCollected()) {
			Map<Integer, String> measurements = selectConrodMeasurements(getController()
					.getProductModel().getProduct());
			UiUtils.setText(getView().getConrodMeasurementTextFields(),
					measurements);
			getLogger()
					.info(String.format("Found conrod measurements: %s",
							measurements));
		}

		if (getProperty().isBlockMeasurementsCollected()) {
			validateTextField(getView().getBlockMeasurementTextFieldsAsList(),
					getView().getBlockMeasurementsEditButton(), getProperty()
							.isBlockMeasurementsEditable());
		}
		if (getProperty().isConrodMeasurementsCollected()) {
			validateTextField(getView().getConrodMeasurementTextFieldsAsList(),
					getView().getConrodMeasurementsEditButton(),
					isConrodMeasurementsEditable());
		}
	}

	/**
	 * Select bearing.
	 *
	 * @param bearingType the bearing type
	 * @param modelYearCode the model year code
	 * @param modelCode the model code
	 * @param modelTypeCode the model type code
	 * @param journalPosition the journal position
	 * @param columnMeasurement the column measurement
	 * @param rowMeasurement the row measurement
	 * @return the bearing matrix cell
	 */
	public BearingMatrixCell selectBearing(BearingType bearingType,
			String modelYearCode, String modelCode, String modelTypeCode,
			String journalPosition, String columnMeasurement,
			String rowMeasurement) {

		BearingMatrixCellDao dao = ServiceFactory
				.getDao(BearingMatrixCellDao.class);
		BearingMatrixCellId id = new BearingMatrixCellId();
		id.setModelYearCode(modelYearCode);
		id.setModelCode(modelCode);
		id.setBearingType(bearingType.name());
		id.setModelTypeCode(modelTypeCode);
		id.setJournalPosition(journalPosition);
		id.setColumnMeasurement(columnMeasurement);
		id.setRowMeasurement(rowMeasurement);

		// Y, M, T, Pos
		BearingMatrixCell matrixCell = dao.findByKey(id);
		if (matrixCell != null) {
			return matrixCell;
		}
		// Y, M, T, *
		id.setModelTypeCode(modelTypeCode);
		id.setJournalPosition("*");
		matrixCell = dao.findByKey(id);
		if (matrixCell != null) {
			return matrixCell;
		}
		// Y, M, *, Pos
		id.setModelTypeCode("*");
		id.setJournalPosition(journalPosition);
		matrixCell = dao.findByKey(id);
		if (matrixCell != null) {
			return matrixCell;
		}
		// Y, M, *, *
		id.setModelTypeCode("*");
		id.setJournalPosition("*");
		matrixCell = dao.findByKey(id);
		return matrixCell;
	}

	/**
	 * Select block measurements.
	 *
	 * @return the map
	 */
	public Map<Integer, String> selectBlockMeasurements() {
		Map<Integer, String> measurements = new LinkedHashMap<Integer, String>();
		Block block = getBlock();
		if (block == null) {
			return measurements;
		}
		BlockBuildResultDao blockBuildResultDao = ServiceFactory
				.getDao(BlockBuildResultDao.class);
		int mainBearingCount = getMainBearingCount();

		for (int i = 1; i <= mainBearingCount; i++) {
			String partName = String.format("%s %s",
					LineSideContainerTag.PART_CRANK_JOURNAL_PREFIX, i);
			BlockBuildResult blockBuildResult = blockBuildResultDao.findById(
					block.getBlockId(), partName);
			if (blockBuildResult != null) {
				measurements.put(i, blockBuildResult.getResultValue());
			}
		}
		return measurements;
	}

	/**
	 * Select conrod bearings.
	 *
	 * @param modelYearCode the model year code
	 * @param modelCode the model code
	 * @param modelTypeCode the model type code
	 * @return the map
	 */
	protected Map<Integer, BearingMatrixCell> selectConrodBearings(
			String modelYearCode, String modelCode, String modelTypeCode) {

		Map<Integer, BearingMatrixCell> bearingMap = new HashMap<Integer, BearingMatrixCell>();
		StringBuilder sb = new StringBuilder();
				
		for (Integer ix : getView().getCrankConrodMeasurementTextFields().keySet()) {
			TextField crankConrodTextField = getView().getCrankConrodMeasurementTextFields().get(ix);
			String crankConrodMeasurement = crankConrodTextField.getText();
			TextField conrodTextField = getView().getConrodMeasurementTextFields().get(ix);
			String conrodMeasurement = conrodTextField.getText();
			String journalPosition = "*";
			BearingMatrixCell matrixCell = selectBearing(BearingType.Conrod,
					modelYearCode, modelCode, modelTypeCode, journalPosition,
					conrodMeasurement, crankConrodMeasurement);
			bearingMap.put(ix, matrixCell);

			if (matrixCell == null) {
				sb.append("[").append(crankConrodMeasurement).append(", ")
						.append(conrodMeasurement).append("]");
				getController().addErrorMessage(crankConrodTextField);
				getController().addErrorMessage(conrodTextField);
			}
		}
		if (sb.length() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Could not match conrod bearings for YM:")
					.append(modelYearCode).append(modelCode);
			msg.append(", measurements:").append(sb);
			getController().addErrorMessage(msg.toString());
		}
		return bearingMap;
	}

	/**
	 * Select conrod measurements.
	 *
	 * @param product the product
	 * @return the map
	 */
	public Map<Integer, String> selectConrodMeasurements(BaseProduct product) {
		Map<Integer, String> measurements = new LinkedHashMap<Integer, String>();
		MeasurementDao measurementDao = ServiceFactory
				.getDao(MeasurementDao.class);
		List<Measurement> list = measurementDao.findAll(product.getProductId(),
				"CON-ROD-CAPS");
		if (list == null || list.isEmpty()) {
			return measurements;
		}
		for (Measurement m : list) {
			if (m == null) {
				continue;
			}
			String value = String.valueOf(Double.valueOf(
					m.getMeasurementValue()).intValue());
			measurements.put(m.getId().getMeasurementSequenceNumber(), value);
		}
		return measurements;
	}
	/**
	 * select the conrods from results
	 */
	protected void prePopulateConCrank(boolean validate) {
		Engine product = (Engine) getController().getProductModel().getProduct();
		// fill in details on Bearing Select Screen
		// Get the unit numbers from Common names (MC_VIOS_MASTER_OP_TBX)
		String operationName = getOperation().getId().getOperationName();
		List<MCViosMasterOperation> listOfConrodUnits = ServiceFactory.getDao(MCViosMasterOperationDao.class).findAllUnitbyCommonName(operationName.substring(operationName.indexOf(Delimiter.UNDERSCORE)+1), ApplicationConstants.CONROD_, getConrodCount());
		// Get indexes for Conrod and CrankShaft from GAL106TBX
		BearingMatrix bearingMatrix = getBearingMatrix();
		List<Integer> firingIx = getFiringIx();
		if(!StringUtil.isNullOrEmpty(bearingMatrix.getConrodRankIndex())) {
			for (Object ix : getView().getConrodMeasurementTextFields().keySet()) {
				TextField field = getView().getConrodMeasurementTextFields().get(ix);
				if (field != null) {
					String[] cons = bearingMatrix.getConrodRankIndex().split(Delimiter.COMMA);
					for (MCViosMasterOperation viosMasterOP : listOfConrodUnits) {
						if(viosMasterOP != null) {
							if (viosMasterOP.getCommonName().equals(ApplicationConstants.CONROD_ + firingIx.get(((Integer) ix) - 1).toString())) {
								InstalledPart part = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(product.getProductId(), viosMasterOP.getId().getOperationName()));
								if( part != null) {
									int j = Integer.parseInt(cons[0])-1;
									if(!StringUtil.isNullOrEmpty(part.getPartSerialNumber())) {
										field.setText(part.getPartSerialNumber().substring(j, j+Integer.parseInt(cons[1])));
										if(validate) {
											getValidator().validate(field);											
										}
									}
									break;
								}								
							}
						}	
					}
				}
			}
		}
		List<MCViosMasterOperation> listOfCrankUnits = ServiceFactory.getDao(MCViosMasterOperationDao.class).findAllUnitbyCommonName(operationName.substring(operationName.indexOf(Delimiter.UNDERSCORE)+1), ApplicationConstants.CRANKSHAFT_, 1);
		if(!StringUtil.isNullOrEmpty(bearingMatrix.getCrankConIndex()) || !StringUtil.isNullOrEmpty(bearingMatrix.getCrankMainIndex())) {
			// Populate CrankShaft
			for(MCViosMasterOperation viosMasterOPForCranksShaft : listOfCrankUnits) {
				if(viosMasterOPForCranksShaft!=null) {
					InstalledPart crankShaftPart = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(product.getProductId(), viosMasterOPForCranksShaft.getId().getOperationName()));
					if(crankShaftPart != null) {
						//retrieval logic for Mains
						String serialNumber = crankShaftPart.getPartSerialNumber();
						if(!StringUtil.isNullOrEmpty(bearingMatrix.getCrankMainIndex())) {
							String[] mains = bearingMatrix.getCrankMainIndex().split(Delimiter.COMMA);
							if(Integer.parseInt(mains[1])%getMainBearingCount() == 0) {
								int indexValue = Integer.parseInt(mains[1])/getMainBearingCount();
								int start = Integer.parseInt(mains[0])-1;
								int end = start+indexValue;
								for (Object ix : getView().getCrankMainMeasurementTextFields().keySet()) {
									TextField field = getView().getCrankMainMeasurementTextFields().get(ix);
									if (field != null) {
										field.setText(serialNumber.substring(start, end));
										if(validate) {
											getValidator().validate(field);											
										}
										start = end;
										end = start+indexValue;
									}
								}
							}
						}
						if(!StringUtil.isNullOrEmpty(bearingMatrix.getCrankConIndex())) {
							//retrieval logic for Cons
							String[] cons = bearingMatrix.getCrankConIndex().split(Delimiter.COMMA);
							if(Integer.parseInt(cons[1])%getConrodCount() == 0) {
								int indexValue = Integer.parseInt(cons[1])/getConrodCount();
								int start = Integer.parseInt(cons[0])-1;
								int end = start+indexValue;
								for (Object ix : getView().getCrankConrodMeasurementTextFields().keySet()) {
									TextField field = getView().getCrankConrodMeasurementTextFields().get(ix);
									if (field != null) {
										field.setText(serialNumber.substring(start, end));
										if(validate) {
											getValidator().validate(field);											
										}
										start = end;
										end = start+indexValue;
									}
								}
							}
						}
						break;
					}
				}	
			}
		}
	}
	/**
	 * Select existing results.
	 */
	protected void selectExistingResults() {
 		Engine product = (Engine) getController().getProductModel().getProduct();
		BearingSelectResult bsResult = ServiceFactory.getDao(BearingSelectResultDao.class).findByProductId(product.getProductId());
		
		if (bsResult == null) {
			return;
		}

		String blockString = StringUtils.trim(bsResult.getJournalBlockMeasurements());

		List<Integer> mainIx = getMainBearingIxSequence();
		
		UiUtils.setText(getView().getBlockMeasurementTextFields(), UiUtils.toIxMap(mainIx, blockString, getProperty().isBlockMeasurementNumeric()));

		boolean validatePrePopulate = false;
		prePopulateConCrank(validatePrePopulate);
	}
	
	public BearingMatrix getBearingMatrix() {
		BearingMatrixDao bearingMatrixDao = ServiceFactory.getDao(BearingMatrixDao.class);
		BearingMatrixId id = new BearingMatrixId();
		
		String modelYearCode = ProductSpec.extractModelYearCode(getController()
				.getProductModel().getProduct().getProductSpecCode());
		String modelCode = getController().getProductModel().getProduct()
				.getModelCode();
		
		id.setModelCode(modelCode);
		id.setModelYearCode(modelYearCode);
		return bearingMatrixDao.findByKey(id);
	}
	
	
	/**
	 * Select main bearings.
	 *
	 * @param modelYearCode the model year code
	 * @param modelCode the model code
	 * @param modelTypeCode the model type code
	 * @return the map
	 */
	protected Map<Integer, BearingMatrixCell> selectMainBearings(
			String modelYearCode, String modelCode, String modelTypeCode) {

		Map<Integer, BearingMatrixCell> bearingMap = new HashMap<Integer, BearingMatrixCell>();
		StringBuilder sb = new StringBuilder();

		for (Integer ix : getMainBearingIxSequence()) {

			TextField blockTextField = getView()
					.getBlockMeasurementTextFields().get(ix);
			String blockMeasurement = blockTextField.getText();
			TextField crankMainTextField = getView()
					.getCrankMainMeasurementTextFields().get(ix);
			String crankMainMeasurement = crankMainTextField.getText();
			String journalPosition = ix.toString();
			BearingMatrixCell matrixCell = selectBearing(BearingType.Main,
					modelYearCode, modelCode, modelTypeCode, journalPosition,
					blockMeasurement, crankMainMeasurement);
			bearingMap.put(ix, matrixCell);
			if (matrixCell == null) {
				sb.append("[").append(blockMeasurement).append(", ")
						.append(crankMainMeasurement).append("]");
				getController().addErrorMessage(blockTextField);
				getController().addErrorMessage(crankMainTextField);
			}
		}
		if (sb.length() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Could not match main bearings for YM : ")
					.append(modelYearCode).append(modelCode);
			msg.append(", measurements : ").append(sb);
			getController().addErrorMessage(msg.toString());
		}
		return bearingMap;
	}

	/**
	 * Sets the audio manager.
	 *
	 * @param audioManager the new audio manager
	 */
	public void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	/**
	 * Sets the block.
	 *
	 * @param block the new block
	 */
	public void setBlock(Block block) {
		this.block = block;
	}

	/**
	 * Sets the conrod bearings.
	 *
	 * @param result the result
	 * @param bmc the bmc
	 * @param bearingIx the bearing ix
	 */
	public void setConrodBearings(BearingSelectResult result,
			BearingMatrixCell bmc, int bearingIx) {
		if (result == null || bmc == null) {
			return;
		}
		switch (bearingIx) {
		case 1:
			result.setConrodUpperBearing01(bmc.getUpperBearing());
			result.setConrodLowerBearing01(bmc.getLowerBearing());
			break;
		case 2:
			result.setConrodUpperBearing02(bmc.getUpperBearing());
			result.setConrodLowerBearing02(bmc.getLowerBearing());
			break;
		case 3:
			result.setConrodUpperBearing03(bmc.getUpperBearing());
			result.setConrodLowerBearing03(bmc.getLowerBearing());
			break;
		case 4:
			result.setConrodUpperBearing04(bmc.getUpperBearing());
			result.setConrodLowerBearing04(bmc.getLowerBearing());
			break;
		case 5:
			result.setConrodUpperBearing05(bmc.getUpperBearing());
			result.setConrodLowerBearing05(bmc.getLowerBearing());
			break;
		case 6:
			result.setConrodUpperBearing06(bmc.getUpperBearing());
			result.setConrodLowerBearing06(bmc.getLowerBearing());
			break;
		}
	}

	/**
	 * Sets the main bearings.
	 *
	 * @param result the result
	 * @param bmc the bmc
	 * @param bearingIx the bearing ix
	 */
	public void setMainBearings(BearingSelectResult result,
			BearingMatrixCell bmc, int bearingIx) {
		if (result == null || bmc == null) {
			return;
		}
		switch (bearingIx) {
		case 1:
			result.setJournalUpperBearing01(bmc.getUpperBearing());
			result.setJournalLowerBearing01(bmc.getLowerBearing());
			break;
		case 2:
			result.setJournalUpperBearing02(bmc.getUpperBearing());
			result.setJournalLowerBearing02(bmc.getLowerBearing());
			break;
		case 3:
			result.setJournalUpperBearing03(bmc.getUpperBearing());
			result.setJournalLowerBearing03(bmc.getLowerBearing());
			break;
		case 4:
			result.setJournalUpperBearing04(bmc.getUpperBearing());
			result.setJournalLowerBearing04(bmc.getLowerBearing());
			break;
		case 5:
			result.setJournalUpperBearing05(bmc.getUpperBearing());
			result.setJournalLowerBearing05(bmc.getLowerBearing());
			break;
		case 6:
			result.setJournalUpperBearing06(bmc.getUpperBearing());
			result.setJournalLowerBearing06(bmc.getLowerBearing());
			break;
		}
	}

	/**
	 * Sets the validator.
	 *
	 * @param validator the new validator
	 */
	public void setValidator(BearingSelectValidator validator) {
		this.validator = validator;
	}

	/**
	 * Sets the view.
	 *
	 * @param widget the new view
	 */
	public void setView(BearingSelectView widget) {
		this.view = widget;
	}

	/**
	 * Start.
	 */
	public void start() {
		getController().clearMessages();
		if (isAlreadyProcessed()) {
			alreadyProcessed();
			return;
		} else {
			boolean validatePrePopulation = true;
			prePopulateConCrank(validatePrePopulation);
		}
		if(!initialized){
			uiState.prepare(getView());
			startInitState();
			if(startExecute()){
				initialized = true;				
			}
		}
		else{
			getController().setFocusComponent(getNextFocusableTextField(null));
		}
		postExecute();
	}
	
	public void reset(){
		block = null;
		initialized=false;
	}
	

	/**
	 * Start execute.
	 */
	public boolean startExecute() {
		if (getProperty().isEnableBlockValidation()) {
			BaseProduct product = getController().getProductModel().getProduct();
			String blockPartName = getProperty().getInstalledBlockPartName();
			NumberType numberType = getProperty().getInstalledBlockPartNameSnType();
	
			InstalledPart installedPartBlock = ServiceFactory.getDao(
					InstalledPartDao.class).findByKey(
					new InstalledPartId(product.getProductId(), blockPartName));
			Block block = null;
	
			if (installedPartBlock == null) {
				String msg = String
						.format("Block (part name: %s, number type: %s) is not assigned to the engine",
								blockPartName, numberType);
				getController().addErrorMessage(msg);
				uiState.disabled(getView());
				return false;
			} else {
				block = ServiceFactory.getDao(BlockDao.class).findBySn(
						installedPartBlock.getPartSerialNumber(), numberType);
				if (block == null) {
					String msg = String
							.format("Block does not exist for %s number that is assigned to this engine",
									numberType);
					getController().addErrorMessage(msg);
					uiState.disabled(getView());
					return false;
				} 
			}
	
			getLogger().info(String.format("Found block: %s", block));
			setBlock(block);
			getView().getDoneButton().setDisable(false);
		}
		prepareExistingMeasurements();
		return true;
	}

	/**
	 * Start init state.
	 */
	public void startInitState() {
		uiState.start(getView());
		getController().setFocusComponent(getNextFocusableTextField(null));
	}

	// === pre/post processing , validation === //
	/**
	 * Validate.
	 *
	 * @param textField the text field
	 */
	public void validate(TextField textField) {
		getValidator().validate(textField);
	}

	/**
	 * Validate text field.
	 *
	 * @param textFields the text fields
	 * @param button the button
	 * @param enableButton the enable button
	 */
	public void validateTextField(List<TextField> textFields, Button button,
			boolean enableButton) {
		if (textFields == null) {
			return;
		}
		for (TextField textField : textFields) {
			validateTextField(textField, button, enableButton);
		}
	}

	/**
	 * Validate text field.
	 *
	 * @param textField the text field
	 * @param button the button
	 * @param enableButton the enable button
	 */
	public void validateTextField(TextField textField, Button button,
			boolean enableButton) {
		if (textField == null) {
			return;
		}
		validate(textField);
		if (enableButton && button != null && button.isDisable()) {
			button.setDisable(false);
		}
	}

	 /**
     * @param commonName
     * @return
     */
    public String getOperationNameFromCommonName(String commonName){ 
    	String opNameFromCommanName = commonName;
    	for(MCOperationRevision operation:this.getController().getModel().getOperations()) {
			if(operation.getCommonName().equals(StringUtils.trimToEmpty(commonName))
					&& this.getOperation().getView().equals(operation.getView())) {
				opNameFromCommanName = operation.getId().getOperationName();
				break;
			}
		}
    	return opNameFromCommanName;
    }
}