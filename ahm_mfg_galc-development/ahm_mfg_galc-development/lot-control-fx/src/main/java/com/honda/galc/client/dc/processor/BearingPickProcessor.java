package com.honda.galc.client.dc.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.BearingPickPropertyBean;
import com.honda.galc.client.dc.view.BearingPickView;
import com.honda.galc.client.dc.view.BearingSelectView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


/**
 * 
 * <h3>BearingPickProcessor</h3>
 * <h3>The class is a operation processor for the bearing pick view </h3>
 * <h4>  </h4>
 * <p> The operation view for BearingPickProcessor must be {@link BearingPickView}. </p>
 * <p> The bearing pick view shows the information only when bearing select is done. see {@link BearingSelectView} and {@link BearingSelectProcessor}</p>
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
 *
 * </TABLE>
 * @see BearingPickView 
 * @see BearingSelectView
 * @see BearingSelectProcessor
 * @author Hale Xie
 * May 30, 2014
 *
 */
public class BearingPickProcessor extends BearingProcessor {
	
	/**
	 * The Enum of  Bearing Part Types.
	 */
	public enum BearingPartType {
		
		/** The main upper bearing. */
		MAIN_UPPER, 
		
		/** The main lower bearing. */
		MAIN_LOWER, 
		
		/** The conrod upper bearring. */
		CONROD_UPPER, 
		
		/** The conrod lower bearing. */
		CONROD_LOWER
	};

	private List<BearingPartType> bearingPartTypes;
	private Map<String, Color> colors;
	private BearingPickView view;

	public BearingPickView getView() {
		return view;
	}

	public void setView(BearingPickView view) {
		this.view = view;
	}

	/**
	 * Instantiates a new bearing pick processor.
	 *
	 * @param controller the controller
	 * @param structure the structure
	 */
	public BearingPickProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		this.colors = new TreeMap<String, Color>();
		this.bearingPartTypes = new ArrayList<BearingPartType>();
		setProperty(PropertyService.getPropertyBean(
				BearingPickPropertyBean.class, getApplicationContext()
						.getProcessPointId()));
		initProcessor();
	}

	public List<Integer> getMainBearingIxDisplaySequence() {
		List<Integer> list = getMainBearingIxSequence();
		if (getProperty().isBearingDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}
	
	public List<Integer> getConrodIxDisplaySequence() {
		List<Integer> list = getConrodIxSequence();
		if (getProperty().isBearingDisplayReversed()) {
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * initialize the processor.
	 */
	protected void initProcessor() {
		initColors();
		initBearingPartTypes();
	}

	/**
	 * initialize the colors.
	 */
	protected void initColors() {
		getColors().put("BLACK", Color.BLACK);
		getColors().put("RED", Color.RED);
		getColors().put("GREEN", Color.GREEN);
		getColors().put("BLUE", Color.BLUE);
		getColors().put("WHITE", Color.WHITE);

		getColors().put("YELLOW", Color.YELLOW);
		getColors().put("PINK", Color.PINK);
		getColors().put("BROWN", Color.rgb(204, 102, 0));

		getColors().put("PURPLE", Color.rgb(128, 0, 128));
		getColors().put("CYAN", Color.CYAN);
		getColors().put("ORANGE", Color.ORANGE);
		getColors().put("GRAY", Color.GRAY);

		getColors().put("NO_DATA", Color.LIGHTGRAY);

	}

	/**
	 * initialize the bearing part types.
	 */
	protected void initBearingPartTypes() {
		String propertyString = getProperty().getBearingPartTypes();
		if (!StringUtils.isBlank(propertyString)) {
			String[] ar = propertyString.split(",");
			for (String str : ar) {
				if (StringUtils.isBlank(str)) {
					continue;
				}
				str = str.trim();
				try {
					BearingPartType type = BearingPartType.valueOf(str);
					getBearingPartTypes().add(type);
				} catch (Exception e) {
					String msg = String
							.format("Could not parse BearingPartType for %s", str);
					Logger.getLogger(getClass().getName()).warn(msg);
				}
			}
		}
		if (getBearingPartTypes().isEmpty()) {
			getBearingPartTypes().addAll(
					Arrays.asList(BearingPartType.values()));
		}
	}

	public List<BearingPartType> getBearingPartTypes() {
		return bearingPartTypes;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

	public BearingPickPropertyBean getProperty() {
		return (BearingPickPropertyBean) super.getProperty();
	}

	/**
	 * read bearing select result from GALC database.
	 *
	 * @param product the product
	 * @return the bearing select result. The return value could be null if there is no bearing select result in database.
	 */
	public BearingSelectResult selectMeasurements(BaseProduct product) {
		BearingSelectResult bsResult = ServiceFactory.getDao(
				BearingSelectResultDao.class).findByProductId(
				product.getProductId());
		return bsResult;
	}

	public Map<Integer, BearingPart> selectMainUpperBearings(
			BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart main01 = result.getJournalUpperBearing01() == null ? null
				: dao.findByKey(result.getJournalUpperBearing01());
		BearingPart main02 = result.getJournalUpperBearing02() == null ? null
				: dao.findByKey(result.getJournalUpperBearing02());
		BearingPart main03 = result.getJournalUpperBearing03() == null ? null
				: dao.findByKey(result.getJournalUpperBearing03());
		BearingPart main04 = result.getJournalUpperBearing04() == null ? null
				: dao.findByKey(result.getJournalUpperBearing04());
		map.put(1, main01);
		map.put(2, main02);
		map.put(3, main03);
		map.put(4, main04);

		int mainBearingCount = getMainBearingCount();

		if (mainBearingCount > 4) {
			BearingPart main05 = result.getJournalUpperBearing05() == null ? null
					: dao.findByKey(result.getJournalUpperBearing05());
			map.put(5, main05);
		}
		if (mainBearingCount > 5) {
			BearingPart main06 = result.getJournalUpperBearing06() == null ? null
					: dao.findByKey(result.getJournalUpperBearing06());
			map.put(6, main06);
		}
		return map;
	}

	public Map<Integer, BearingPart> selectMainLowerBearings(
			BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart main01 = result.getJournalLowerBearing01() == null ? null
				: dao.findByKey(result.getJournalLowerBearing01());
		BearingPart main02 = result.getJournalLowerBearing02() == null ? null
				: dao.findByKey(result.getJournalLowerBearing02());
		BearingPart main03 = result.getJournalLowerBearing03() == null ? null
				: dao.findByKey(result.getJournalLowerBearing03());
		BearingPart main04 = result.getJournalLowerBearing04() == null ? null
				: dao.findByKey(result.getJournalLowerBearing04());

		map.put(1, main01);
		map.put(2, main02);
		map.put(3, main03);
		map.put(4, main04);
		int mainBearingCount = getMainBearingCount();
		if (mainBearingCount > 4) {
			BearingPart main05 = result.getJournalLowerBearing05() == null ? null
					: dao.findByKey(result.getJournalLowerBearing05());
			map.put(5, main05);
		}
		if (mainBearingCount > 5) {
			BearingPart main06 = result.getJournalLowerBearing06() == null ? null
					: dao.findByKey(result.getJournalLowerBearing06());
			map.put(6, main06);
		}
		return map;
	}

	public Map<Integer, BearingPart> selectConrodUpperBearings(
			BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart conrod01 = result.getConrodUpperBearing01() == null ? null
				: dao.findByKey(result.getConrodUpperBearing01());
		BearingPart conrod02 = result.getConrodUpperBearing02() == null ? null
				: dao.findByKey(result.getConrodUpperBearing02());
		BearingPart conrod03 = result.getConrodUpperBearing03() == null ? null
				: dao.findByKey(result.getConrodUpperBearing03());
		BearingPart conrod04 = result.getConrodUpperBearing04() == null ? null
				: dao.findByKey(result.getConrodUpperBearing04());

		map.put(1, conrod01);
		map.put(2, conrod02);
		map.put(3, conrod03);
		map.put(4, conrod04);

		int conrodBearingCount = getConrodCount();
		if (conrodBearingCount > 4) {
			BearingPart conrod05 = result.getConrodUpperBearing05() == null ? null
					: dao.findByKey(result.getConrodUpperBearing05());
			map.put(5, conrod05);
		}
		if (conrodBearingCount > 5) {
			BearingPart conrod06 = result.getConrodUpperBearing06() == null ? null
					: dao.findByKey(result.getConrodUpperBearing06());
			map.put(6, conrod06);
		}
		return map;
	}

	public Map<Integer, BearingPart> selectConrodLowerBearings(
			BearingSelectResult result) {
		Map<Integer, BearingPart> map = new HashMap<Integer, BearingPart>();
		if (result == null) {
			return map;
		}
		BearingPartDao dao = ServiceFactory.getDao(BearingPartDao.class);

		BearingPart conrod01 = result.getConrodLowerBearing01() == null ? null
				: dao.findByKey(result.getConrodLowerBearing01());
		BearingPart conrod02 = result.getConrodLowerBearing02() == null ? null
				: dao.findByKey(result.getConrodLowerBearing02());
		BearingPart conrod03 = result.getConrodLowerBearing03() == null ? null
				: dao.findByKey(result.getConrodLowerBearing03());
		BearingPart conrod04 = result.getConrodLowerBearing04() == null ? null
				: dao.findByKey(result.getConrodLowerBearing04());

		map.put(1, conrod01);
		map.put(2, conrod02);
		map.put(3, conrod03);
		map.put(4, conrod04);

		int conrodBearingCount = getConrodCount();
		if (conrodBearingCount > 4) {
			BearingPart conrod05 = result.getConrodLowerBearing05() == null ? null
					: dao.findByKey(result.getConrodLowerBearing05());
			map.put(5, conrod05);
		}
		if (conrodBearingCount > 5) {
			BearingPart conrod06 = result.getConrodLowerBearing06() == null ? null
					: dao.findByKey(result.getConrodLowerBearing06());
			map.put(6, conrod06);
		}
		return map;
	}

	/**
	 * Sets the bearing colors according to bearing select result.
	 *
	 * @param textFields the text fields
	 * @param bearings the bearings
	 * @param highLight the high light
	 */
	protected void setBearingColors(Map<Integer, TextField> textFields,
			Map<Integer, BearingPart> bearings, boolean highLight) {
		if (textFields == null || bearings == null) {
			return;
		}
		for (Integer ix : textFields.keySet()) {
			TextField textField = textFields.get(ix);
			BearingPart bearingPart = bearings.get(ix);
			if (textField == null) {
				continue;
			}
			if (bearingPart == null) {
				textField.setText("");
			} else {
				textField.setText(bearingPart.getColor());
				if (highLight) {
					Color color = getColor(bearingPart.getColor());
					if (color != null) {
						setTextFieldBackgroundColor(textField, color);
					}
				}
			}
		}
	}

	/**
	 * Sets the text field background color.
	 *
	 * @param textField the text field
	 * @param color the color
	 */
	protected void setTextFieldBackgroundColor(TextField textField, Color color) {
		String style = String.format(
				"-fx-background-color: rgb(%1$d,%2$d,%3$d);",
				(int)(color.getRed()*255), (int)(color.getGreen()*255),
				(int)(color.getBlue()*255));
		if (color.equals(Color.BLACK)) {
			style += "-fx-text-fill: white;";
		} else {
			style += "-fx-text-fill: black;";
		}
		textField.setStyle(style);
	}
	
	/**
	 * Sets the text fields background color.
	 *
	 * @param textFields the text fields
	 * @param color the color
	 */
	protected void setTextFieldsBackgroundColor(Collection<TextField> textFields, Color color){
		for(TextField tf:textFields){
			setTextFieldBackgroundColor(tf, color);
		}
	}

	/**
	 * Gets the color.
	 *
	 * @param colorName the color name
	 * @return the color. If there is no color for the color name, light gray will be return.
	 */
	protected Color getColor(String colorName) {
		Color color = getColors().get(colorName);
		if (color == null) {
			return Color.LIGHTGRAY;
		}
		return color;
	}

	/**
	 * start to process the bearing pick information for the VIN.
	 */
	public void start() {
		getController().clearMessages();
		resetInitState();
		if (isNotProcessable()) {
			return;
		}
		startExecute();
		getController().setFocusComponent(getFirstFocusableButton());
		getController().requestFocus();
	}

	
	/**
	 * Gets the first focusable button.
	 *
	 * @return the first focusable button. The function only returns the enabled buttons. If there is no button enable, null will be returned.
	 */
	public Button getFirstFocusableButton() {
		if (!getView().getDoneButton().isDisabled()) {
			return getView().getDoneButton();
		} else {
			return null;
		}
	}
	
	/**
	 * Start to get the bearing pick information and show them on the screen. 
	 */
	protected void startExecute() {

		BearingSelectResult result = selectMeasurements(getController()
				.getProductModel().getProduct());

		getLogger().info(String.format("Found : %s", result));

		String blockString = StringUtils.trim(result
				.getJournalBlockMeasurements());
		String crankMainString = StringUtils.trim(result
				.getJournalCrankMeasurements());
		String crankConString = StringUtils.trim(result
				.getConrodCrankMeasurements());
		String conrodString = StringUtils.trim(result
				.getConrodConsMeasurements());

		List<Integer> mainIx = getMainBearingIxSequence();
		List<Integer> conrodIx = getConrodIxSequence();

		Map<Integer, String> blockMeasurements = UiUtils.toIxMap(mainIx,
				blockString, getProperty().isBlockMeasurementNumeric());
		Map<Integer, String> crankMainMeasurements = UiUtils.toIxMap(mainIx,
				crankMainString, getProperty().isCrankMainMeasurementNumeric());
		Map<Integer, String> crankConrodMeasurements = UiUtils.toIxMap(
				conrodIx, crankConString, getProperty().isCrankConrodMeasurementNumeric());
		Map<Integer, String> conrodMeasurements = UiUtils.toIxMap(conrodIx,
				conrodString, getProperty().isConrodMeasurementNumeric());

		UiUtils.setText(getView().getBlockMeasurementTextFields(),
				blockMeasurements);
		UiUtils.setText(getView().getCrankMainMeasurementTextFields(),
				crankMainMeasurements);
		UiUtils.setText(getView().getCrankConrodMeasurementTextFields(),
				crankConrodMeasurements);
		UiUtils.setText(getView().getConrodMeasurementTextFields(),
				conrodMeasurements);

		Map<Integer, BearingPart> mainUpper = selectMainUpperBearings(result);
		Map<Integer, BearingPart> mainLower = selectMainLowerBearings(result);
		Map<Integer, BearingPart> conrodUpper = selectConrodUpperBearings(result);
		Map<Integer, BearingPart> conrodLower = selectConrodLowerBearings(result);

		setBearingColors(getView().getMainUpperBearingTextFields(), mainUpper,
				getBearingPartTypes().contains(BearingPartType.MAIN_UPPER));
		setBearingColors(getView().getMainLowerBearingTextFields(), mainLower,
				getBearingPartTypes().contains(BearingPartType.MAIN_LOWER));

		setBearingColors(getView().getConrodUpperBearingTextFields(),
				conrodUpper,
				getBearingPartTypes().contains(BearingPartType.CONROD_UPPER));
		setBearingColors(getView().getConrodLowerBearingTextFields(),
				conrodLower,
				getBearingPartTypes().contains(BearingPartType.CONROD_LOWER));

		getLogger().info(String.format("Main Upper:  %s:", mainUpper));
		getLogger().info(String.format("Main Lower:  %s:", mainLower));
		getLogger().info(String.format("Conrod Upper:%s:", conrodUpper));
		getLogger().info(String.format("Conrod Lower:%s:", conrodLower));

		getView().getDoneButton().setDisable(false);
		getController().setFocusComponent(getView().getDoneButton());
	}

	/**
	 * Checks if bearing select result does exist in GALC DB. 
	 *
	 * @return true, if no bearing select result exists in GALC DB
	 */
	protected boolean isNotProcessable() {
		BearingSelectResult result = selectMeasurements(getController()
				.getProductModel().getProduct());
		if (result == null || !isBearingSelectCompleted()) {
			// addMessage("There is no Bearing Result for this product");
			getController().addErrorMessage(
					"There is no Bearing Result for this product");
			return true;
		}
		return false;
	}
	
	private boolean isBearingSelectCompleted() {
		//Get Bearing Select Operation
		String bearingSelectOpName = getBearingSelectOperationName();
		if(StringUtils.isNotBlank(bearingSelectOpName)) {
			boolean isComplete = getController().getModel().isOperationComplete(bearingSelectOpName);
			
			if(!isComplete) {
				List<String> partNames = new ArrayList<String>();
				partNames.add(bearingSelectOpName);
				List<InstalledPart> installParts = ServiceFactory.getService(InstalledPartDao.class).findAllValidParts(getController().getModel().getProductModel().getProductId(), partNames);
				if(installParts !=null && installParts.size() > 0)
					return true;
			}
				
			return isComplete;
		}
		return false;
	}

	/**
	 * Reset initial state of the UI components
	 */
	public void resetInitState() {
		UiUtils.setText(getView().getBlockMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getCrankMainMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getCrankConrodMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getConrodMeasurementTextFields().values(), "");
		UiUtils.setText(getView().getMainUpperBearingTextFields().values(), "");
		UiUtils.setText(getView().getMainLowerBearingTextFields().values(), "");
		UiUtils.setText(getView().getConrodUpperBearingTextFields().values(), "");
		UiUtils.setText(getView().getConrodLowerBearingTextFields().values(), "");
		
		UiUtils.setState(getView().getBlockMeasurementTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(
				getView().getCrankMainMeasurementTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(getView().getCrankConrodMeasurementTextFields()
				.values(), TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodMeasurementTextFields().values(),
				TextFieldState.DISABLED);

		UiUtils.setState(getView().getMainUpperBearingTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(getView().getMainLowerBearingTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodUpperBearingTextFields().values(),
				TextFieldState.DISABLED);
		UiUtils.setState(getView().getConrodLowerBearingTextFields().values(),
				TextFieldState.DISABLED);

		setTextFieldsBackgroundColor(getView().getBlockMeasurementTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getCrankMainMeasurementTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getCrankConrodMeasurementTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getConrodMeasurementTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getMainUpperBearingTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getMainLowerBearingTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getConrodUpperBearingTextFields().values(), Color.LIGHTGRAY);
		setTextFieldsBackgroundColor(getView().getConrodLowerBearingTextFields().values(), Color.LIGHTGRAY);
		
		getView().getDoneButton().setDisable(true);
	}
	
	/**
	 * Finish the process procedure for the current work unit and switch to the next work unit.
	 */
	public void finish(){
		getView().getDoneButton().setDisable(true);
		if(getController().getModel().isCurrentOperationComplete()) {
			getView().getDoneButton().setText("Done");
			getView().getDoneButton().setGraphic(getImageView(getDoneImage()));
			DataCollectionEvent dataCollectionEvent = new DataCollectionEvent(DataCollectionEventType.PDDA_REJECT, null);
			dataCollectionEvent.setOperation(getOperation());
			EventBusUtil.publish(dataCollectionEvent);
			getView().refreshView();
		} else {
			getView().getDoneButton().setText("Reject");
			getView().getDoneButton().setGraphic(getImageView(getRejectImage()));
			completeOperation(true);
		}
	}
	
	/**
	 * return Operation name on the basis of mapping configuration
	 */
	public String getBearingSelectOperationName() {
		if (getProperty().getBearingPickOperations() != null) {
			for (String bearingSelectPropertyValue : getProperty().getBearingPickOperations().keySet()) {
				String bearingPickPropertyValue = StringUtils
						.trimToEmpty(getProperty().getBearingPickOperations().get(bearingSelectPropertyValue));
				if (StringUtils.isNotBlank(bearingPickPropertyValue)) {
					// Bearing Pick configured value can either be common name or operation name
					String bearingPickOpName = null;
					// Checking for common name
					if(StringUtils.equals(getOperation().getCommonName(), bearingPickPropertyValue)) {
						//Property value is the common name
						bearingPickOpName = getOperation().getId().getOperationName();
					} else if(StringUtils.equals(getOperation().getId().getOperationName(), bearingPickPropertyValue)) {
						//Property value is the operation name
						bearingPickOpName = getOperation().getId().getOperationName();
					}
					
					if(StringUtils.isNotBlank(bearingPickOpName)) {
						//Found Bearing Pick Operation name
						//Bearing Select value can either be common name or operation name
						bearingSelectPropertyValue = ServiceFactory.getDao(MCOperationRevisionDao.class).getSelectOperationName(getOperation().getStructure().getId().getRevision(), bearingSelectPropertyValue);
						return bearingSelectPropertyValue;
						
					}


				}

			}
		}
		return null;
	}
}