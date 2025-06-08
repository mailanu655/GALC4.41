package com.honda.galc.client.dc.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dc.common.ClientDeviceResolution;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.mvc.ISkipRejectInputView;
import com.honda.galc.client.dc.mvc.ITorqueCollectionView;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.device.DeviceStatusWidgetEvent;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.PartValidity;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.device.dataformat.Torque;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.BomPartUtil;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.util.Callback;

/**
 * @author Subu Kathiresan
 * @date June 02, 2014
 */
public class LotControlOperationView extends OperationView implements ITorqueCollectionView, ISkipRejectInputView {

	protected static LotControlOperationView instance;
	protected static String MEAS_TXT_NAME = "txtMeas";
	protected static String DEFAULT_VIOS = "DEFAULT_VIOS";

	protected Logger logger;
	protected BorderPane borderPane = null;
	protected volatile ArrayList<TextField> inputFields = new ArrayList<TextField>();
	protected volatile ArrayList<Button> skipRejectBtns = new ArrayList<Button>();
	protected volatile DataCollectionViewUtil dcViewUtil;
	protected volatile int currentInputFieldIndex = 0;
	protected LotControlViewEventHandler eventHandler = null;

	public LotControlOperationView(OperationProcessor opProcessor) {
		super(opProcessor);
		this.dcViewUtil = new DataCollectionViewUtil(this, getProcessor().getController().getModel());
		setCmdButtonStyle();
		this.eventHandler = new LotControlViewEventHandler(this);
		register();
		init();
		checkMfgPartValidity();
	}

	public void setCmdButtonStyle() {
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		StyleUtil.setCmdBtnImageWidth(property.getCommandImageWidth());
		StyleUtil.setCmdBtnWidth(property.getCommandBtnWidth());
		StyleUtil.setCmdBtnImageHeight(property.getCommandImageHeight());
		StyleUtil.setCmdBtnHeight(property.getCommandBtnHeight());
		
	}
	
	public void checkMfgPartValidity() {
		DataCollectionController controller = getProcessor().getController();
		ProductModel productModel = controller.getProductModel();
		//Checking validity only if property is set to true
		if(productModel.getProperty().isUseEffectiveDateForParts()) {
			List<String> nonEffectiveMfgParts = new ArrayList<String>();
			for (MCOperationPartRevision mfgPart : getOperation().getManufacturingMFGPartList()) {
				PartValidity partValidity = mfgPart.getPartValidity();
				if(partValidity == null) {
					//Fetching BOM data only if part validity is not set in operation part
					partValidity = BomPartUtil.findPartValidity(productModel.getProductType(), 
							productModel.getProductSpec(), mfgPart);
					mfgPart.setPartValidity(partValidity);
				}
				if(!(partValidity.equals(PartValidity.UNDEFINED) || partValidity.equals(PartValidity.VALID))) {
					//Mfg Part is not Effective
					nonEffectiveMfgParts.add(mfgPart.getPartNo());
				}
			}
			if(!nonEffectiveMfgParts.isEmpty()) {
				controller.getView().getMainWindow().clearMessage();
				controller.getView().getMainWindow().setErrorMessage("Unit '"+getOperation().getUnitNo()
						+ "' has following Non Effective MFG Parts: "
						+ StringUtils.join(nonEffectiveMfgParts, Delimiter.COMMA));
			}
		}
	}

	public void init() {
		borderPane = new BorderPane();
		String width = ClientDeviceResolution.getClientResolutionProperty("DC_VIEW_WIDTH_","width",null);
		if(width!=null){
			borderPane.setMaxWidth(Double.parseDouble(width));
		}
		
		borderPane.setStyle("-fx-background-color: #ECF3FF;");
		VBox vBox = new VBox(10);
		ArrayList<Integer> skippedMeasurements = getProcessor().getController().getModel().getSkippedMeasurementsMap()
				.get(getOperation().getId().getOperationName());
		if (skippedMeasurements == null) {
			skippedMeasurements = new ArrayList<Integer>();
		}

		// Add part scan box
		if (hasScanPart()) {
			addPartScanBox(vBox);
		} else if ((isMeasOnlyOperation()) && (getOperation().getManufacturingMFGPartList().size() > 1)) {
			addPartSelectioBox(vBox);
		}

		if (getOperation().getManufacturingMFGPartList().size() == 1 && skippedMeasurements.size() == 0) {
			addMeasurementBoxes(vBox);
		} else if (skippedMeasurements != null) {
			if (skippedMeasurements.size() > 0) {
				addMeasurementBoxes(vBox);
			}
		}
		borderPane.setTop(vBox);
		this.setCenter(borderPane);
	}

	public void addPartScanBox(VBox vBox) {
		HBox hBox = new HBox(10);
		String partMaskDisp = getPartMaskLabel();
		Label labelPart = UiFactory.createLabel("labelPart", getPartMaskLabel(), Fonts.SS_DIALOG_BOLD(25),
				TextAlignment.RIGHT, 150.0);
		TextField textFieldPart = UiFactory.createTextField("txtPartSn", Fonts.SS_DIALOG_BOLD(20), TextFieldState.EDIT,
				350.0);
		textFieldPart.setPromptText("Enter part SN");

		Label labelPartMask = UiFactory.createLabel("lblPartMask", "  " + partMaskDisp,
				Fonts.SS_DIALOG_BOLD(getFontSize()), TextAlignment.RIGHT, getPartMaskLabelWidth());

		textFieldPart.setPromptText(partMaskDisp);

		setPartScanEventHandler(textFieldPart, vBox);
		inputFields.add(textFieldPart);
		textFieldPart.setEditable(true);
		textFieldPart.setFocusTraversable(true);

		Button skipRejectBtn = dcViewUtil.createSkipRejectBtn();
		setSkipRejectAction(skipRejectBtn);
		skipRejectBtns.add(skipRejectBtn);

		hBox.getChildren().add(UiFactory.createLabel("lblEmpty1", " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 5));
		hBox.getChildren().add(textFieldPart);
		hBox.getChildren().add(skipRejectBtn);
		hBox.getChildren().add(UiFactory.createLabel("lblEmpty2", " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 5));
		vBox.getChildren().add(labelPartMask);
		vBox.getChildren().add(hBox);
		if (getProcessor().getController().getModel().getCompletedOpsMap()
				.containsKey(getOperation().getId().getOperationName())
				|| getProcessor().getController().getModel().getInstalledPartsMap()
						.get(getOperation().getId().getOperationName()) != null) {
			for (MCOperationPartRevision part : getOperation().getManufacturingMFGPartList()) {

				if (getProcessor().getController().getModel().getInstalledPartsMap()
						.get(getOperation().getId().getOperationName()).getPartId()
						.equalsIgnoreCase(part.getId().getPartId())) {

					getOperation().setSelectedPart(part);
				}
			}
			if (getOperation().getManufacturingMFGPartList().size() > 1) {
				addMeasurementBoxes(vBox);
			}
		}
	}

	public void addPartSelectioBox(VBox vBox) {

		Label labelPart = UiFactory.createLabel("labelPart", "Parts", Fonts.SS_DIALOG_BOLD(20), TextAlignment.RIGHT,
				150.0);
		VBox hBox = new VBox(getOperation().getManufacturingMFGPartList().size());
		ArrayList<KeyValue<String, String>> partList = new ArrayList<KeyValue<String, String>>();
		int i = -1, index = 0;
		for (MCOperationPartRevision part : getOperation().getManufacturingMFGPartList()) {
			String value = part.getPartSectionCode() + "-" + part.getPartItemNo() + "-" + part.getId().getPartId();
			InstalledPart installedPart = getProcessor().getController().getModel().getInstalledPartsMap()
					.get(part.getId().getOperationName());
			if (installedPart.getMeasurements().size() != 0) {
				index = i == -1 ? 0 : i;
			}
			i++;
			partList.add(new KeyValue(part.getId().getPartId(), value));
		}

		final ObservableList<KeyValue<String, String>> data = FXCollections.observableArrayList(partList);
		ComboBox<KeyValue<String, String>> cb = new ComboBox<KeyValue<String, String>>(data);
		cb.setStyle("-fx-font: 20px \"Serif\";");
		cb.setId("partsForMeas");

		cb.setCellFactory(new Callback<ListView<KeyValue<String, String>>, ListCell<KeyValue<String, String>>>() {

			public ListCell<KeyValue<String, String>> call(ListView<KeyValue<String, String>> kv) {

				ListCell cell = new ListCell<KeyValue>() {
					@Override
					protected void updateItem(KeyValue item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText("");
						} else {
							setText((String) item.getValue());
						}
					}
				};
				return cell;
			}
		});

		cb.setButtonCell(new ListCell<KeyValue<String, String>>() {
			@Override
			protected void updateItem(KeyValue item, boolean bln) {
				super.updateItem(item, bln);
				if (bln) {
					setText("");
				} else {
					setText((String) item.getValue());
				}

			}

		});

		hBox.getChildren().add(UiFactory.createLabel("lblEmpty1", " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 1));
		hBox.getChildren().add(labelPart);
		hBox.getChildren().add(cb);
		vBox.getChildren().add(hBox);
		cb.getSelectionModel().select(index == 0 ? 0 : index);
		addMeasurementBoxes(vBox);
		setPartMeasEventHandler(cb, vBox);
	}

	protected void setPartMeasEventHandler(ComboBox<KeyValue<String, String>> cb, final VBox vbox) {
		cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String, String>>() {

			public void changed(ObservableValue<? extends KeyValue<String, String>> selected,
					KeyValue<String, String> oldValue, KeyValue<String, String> newValue) {
				InstalledPart installedPart = getProcessor().getController().getModel().getInstalledPartsMap()
						.get(getOperation().getId().getOperationName());
				if (installedPart.getMeasurements().size() > 0) {
					return;
				}
				for (MCOperationPartRevision part : getOperation().getManufacturingMFGPartList()) {
					if (part.getId().getPartId().equalsIgnoreCase((String) newValue.getKey())) {
						getOperation().setSelectedPart(part);
						break;
					}
				}
				if (getOperation().getManufacturingMFGPartList().size() > 1) {
					for (int i = 0; i < vbox.getChildren().size(); i++) {
						if (vbox.getChildren().get(i) instanceof HBox) {
							vbox.getChildren().remove(i);
						}
					}
					int inputchild = inputFields.size();
					for (int i = 0; i < inputchild; i++) {
						inputFields.remove(0);
					}
					addMeasurementBoxes(vbox);

					setFocusToExpectedInputField();
				}

			}

		});

	}

	protected String getPartMaskLabel() {
		StringBuilder partMaskSB = new StringBuilder();
		String partMaskStr = "";
		for (MCOperationPartRevision part : getOperation().getManufacturingMFGPartList()) {
			if (part.getPartMask() != null) {
				partMaskSB.append(part.getPartMask());
				partMaskSB.append(",");
			}

		}
		partMaskStr = partMaskSB.toString().substring(0, partMaskSB.toString().lastIndexOf(","));
		return partMaskStr;
	}

	public void addMeasurementBoxes(VBox vBox) {
		int fontSize =  Integer.parseInt(ClientDeviceResolution.getClientResolutionProperty("DC_VIEW_FONT_SIZE_","font",String.valueOf(getFontSize())));
		// manual entry of measurement when the part type is
		// GALC_SCAN_WITH_MEAS_MANUAL or GALC_MEAS_MANUAL
		TextFieldState measurementState = TextFieldState.READ_ONLY;
		if (DataCollectionModel.hasManualMeasurement(getOperation())) {
			measurementState = TextFieldState.EDIT;
		}

		HBox measRow = new HBox(10);
		
		VBox measColumn = null;
		// Add Measurement boxes
		for (int i = 0; i < getModel().getMeasurementCount(getOperation()); i++) {
			if (i == 0 || i % getTorqueBoxesPerColumn() == 0) { // display n
																// torques per
																// column
				measColumn = new VBox(10);
				measColumn.getChildren()
						.add(UiFactory.createLabel("lblEmpty" + i, " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 5));
				measRow.getChildren().add(measColumn);
			}
			MCOperationMeasurement measurementSpec = getPartSpec().getMeasurement(i);
			String minMax = new Double(measurementSpec.getMinLimit()).toString() + "/"
					+ new Double(measurementSpec.getMaxLimit());

			HBox hBox = new HBox(10);
			TextField textFieldMeasurement = UiFactory.createTextField(MEAS_TXT_NAME + i,
					Fonts.SS_DIALOG_BOLD(fontSize), measurementState, getTorqueBoxWidth(), getInputBoxHeight());
			textFieldMeasurement.setPrefWidth(getTorqueBoxWidth());
			textFieldMeasurement.setMaxWidth(getTorqueBoxWidth());
			textFieldMeasurement.setPromptText(minMax);
			setMeasurementInputEventHandler(textFieldMeasurement);
			inputFields.add(textFieldMeasurement);

			Button skipRejectBtn = dcViewUtil.createSkipRejectBtn();
			setSkipRejectAction(skipRejectBtn);
			skipRejectBtns.add(skipRejectBtn);

			hBox.getChildren().add(UiFactory.createLabel("lblEmpty", " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 5));
			hBox.getChildren().add(textFieldMeasurement);
			hBox.getChildren().add(skipRejectBtn);
			measColumn.getChildren().add(hBox);
		}
		vBox.getChildren().add(measRow);
	}

	protected void setPartScanEventHandler(final TextField textField, final VBox vbox) {
		textField.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String serial = ((TextField) event.getSource()).getText();
				getLogger().check("Scan received:" + serial);
				if (isValidScan(serial)) {
					processPart(serial);
				} else {
					if (!serial.equalsIgnoreCase(getModel().getProductModel().getProductId())) {
						String message = "An unexpected scan of a product has been detected. \n" + "Scanned (" + serial
								+ ") while still completing \n data collection for ("
								+ getModel().getProductModel().getProductId()
								+ "). \n\n Click OK and re-scan the Product currently being built.";
						displayPopUp(message);
						getProcessor().getController().cancel();
					} else {
						processPart(serial);
					}
				}

			}

		});
	}
	
	private void processPart(String serial){
		PartSerialScanData partSerialScanData = new PartSerialScanData(getPartSpec().getPartMask(), serial,
				getModel().getProductModel().getProductType(), getModel().getProductModel().getProductId());
		EventBusUtil.publish(
				new DataCollectionEvent(DataCollectionEventType.PART_SN_RECEIVED, partSerialScanData));
	}

	private void displayPopUp(String msg) {
		final FxDialog dialogStage = new FxDialog("ERROR", ClientMainFx.getInstance().getStage());
		dialogStage.initModality(Modality.APPLICATION_MODAL);

		GridPane pane = new GridPane();
		pane.setStyle("-fx-background-color: RED;");
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);// padding
		Scene scene = new Scene(pane, 800, 300);
		dialogStage.setScene(scene);

		Label label = new Label(msg);
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
		pane.add(label, 0, 2);

		Button button = new Button("OK");
		button.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				dialogStage.close();
			}
		});

		pane.add(button, 0, 4);
		pane.setHalignment(button, HPos.CENTER);
		dialogStage.showDialog();
	}

	protected void setMeasurementInputEventHandler(final TextField textField) {
		textField.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String val = ((TextField) event.getSource()).getText();
				getLogger().check("Measurement received:" + val);
				if (val.length() > 0) {
					if (isValidScan(val)) {
						MeasurementValue measurementVal = new MeasurementValue();
						measurementVal.setMeasurementValue(Double.parseDouble(val));
						int i = Integer.parseInt(((TextField) event.getSource()).getId().substring(7));
						setCurrentInputFieldIndex(i);
						int measSeqNum = hasScanPart() ? getCurrentInputFieldIndex() : getCurrentInputFieldIndex() + 1;
						measurementVal.setMeasurementIndex(measSeqNum);
						getModel().setCurrentMeasurementIndex(measSeqNum);
						int attemptNumber = 1;
						MeasurementId measId = createMeasurementId(measSeqNum);
						if (getModel().getBadMeasurementAttemptsMap().containsKey(measId)) {
							attemptNumber = getModel().getBadMeasurementAttemptsMap().get(measId) + 1;
						}
						measurementVal.setAttemptNumber(attemptNumber);
						//Setting installed measurement sequence numbers to check if any duplicate measurement sequence received
						InstalledPart installedPart = getModel().getInstalledPartsMap().get(getOperation().getId().getOperationName());
						if(installedPart != null && installedPart.getMeasurements() != null) {
							List<Integer> installedMeasurementSequenceNumbers = new ArrayList<Integer>();
							for (Measurement meas: installedPart.getMeasurements()) {
								if (meas.getMeasurementStatus().equals(MeasurementStatus.OK)) {
									installedMeasurementSequenceNumbers.add(meas.getId().getMeasurementSequenceNumber());
								}
							}
							measurementVal.setInstalledMeasurementSequenceNumbers(installedMeasurementSequenceNumbers);
						}
						
						EventBusUtil.publish(
								new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_RECEIVED, measurementVal));
					} else {
						if (!val.equalsIgnoreCase(getModel().getProductModel().getProductId())) {
							String message = "An unexpected scan of a product has been detected. \n" + "Scanned (" + val
									+ ") while still completing \n data collection for ("
									+ getModel().getProductModel().getProductId()
									+ "). \n\n Click OK and re-scan the Product currently being built.";
							displayPopUp(message);
							getProcessor().getController().cancel();
						}
					}
				}
			}
		});
	}

	@Subscribe
	public void handle(Event event) {
		Object val = event.getSource();
		System.out.println(val.toString());
	}

	protected MeasurementId createMeasurementId(int measSeqNum) {
		return new MeasurementId(getModel().getProductModel().getProductId(), getOperation().getId().getOperationName(),
				measSeqNum);
	}

	protected void setSkipRejectAction(final Button skipRejectBtn) {
		skipRejectBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button) event.getSource());
				dcViewUtil.notifySkipReject(btn);
			}
		});
	}

	public void prepareExpectedInputField(int inputFieldIndex) {
		eventHandler.dispatchWaitEvents(inputFieldIndex);
		dcViewUtil.prepareExpectedInputField(getOperation(), inputFields.get(inputFieldIndex), inputFieldIndex);
	}

	public ArrayList<TextField> getInputFields() {
		return inputFields;
	}

	public void setInputFields(ArrayList<TextField> inputFields) {
		this.inputFields = inputFields;
	}

	public List<Button> getSkipRejectButtons() {
		return skipRejectBtns;
	}

	public void setSkipRejectButtons(ArrayList<Button> skipRejectBtns) {
		this.skipRejectBtns = skipRejectBtns;
	}

	public LotControlViewEventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(LotControlViewEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public MCOperationRevision getOperation() {
		return getProcessor().getOperation();
	}

	public DataCollectionModel getModel() {
		return getProcessor().getController().getModel();
	}

	public String getOperationName() {
		return getOperation().getId().getOperationName();
	}

	public int getCurrentInputFieldIndex() {
		return currentInputFieldIndex;
	}

	public void setCurrentInputFieldIndex(int currentInputFieldIndex) {
		this.currentInputFieldIndex = currentInputFieldIndex;
	}

	public MCOperationPartRevision getPartSpec() {
		return getOperation().getSelectedPart();
	}

	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}

	public void setTorque(final Torque torque) {
		setMeasurementSequence(torque);
		int attemptNumber = 1;
		MeasurementId measId = createMeasurementId(torque.getMeasurementIndex());
		if (getModel().getBadMeasurementAttemptsMap().containsKey(measId)) {
			attemptNumber = getModel().getBadMeasurementAttemptsMap().get(measId) + 1;
		}
		torque.setAttemptNumber(attemptNumber);
		dcViewUtil.showTorque(torque, getInputFields().get(getCurrentInputFieldIndex()));
	}

	public PDDAPropertyBean getViosViewProperty() {
		return PropertyService.getPropertyBean(PDDAPropertyBean.class,
				getModel().getProductModel().getProcessPointId());
	}

	private DataCollectionPropertyBean getDataCollectionPropertyBean() {
		return PropertyService.getPropertyBean(DataCollectionPropertyBean.class,
				getOperation().getStructure().getId().getProcessPointId());
	}

	public int getFontSize() {
		return getViosViewProperty().getDcViewFontSize();
	}

	public int getInputBoxHeight() {
		return getViosViewProperty().getInputBoxHeight();
	}

	public int getTorqueBoxWidth() {
		return getViosViewProperty().getTorqueBoxWidth();
	}

	public int getPartMaskLabelWidth() {
		return getViosViewProperty().getPartMaskLabelWidth();
	}

	public int getPartScanBoxWidth() {
		return getViosViewProperty().getPartScanBoxWidth();
	}

	public int getTorqueBoxesPerColumn() {
		return getViosViewProperty().getTorqueBoxesPerColumn();
	}

	
	public void setMeasurementSequence(final Torque torque) {
		int measurementIndex = hasScanPart() ? getCurrentInputFieldIndex() : getCurrentInputFieldIndex() + 1;
		torque.setMeasurementIndex(measurementIndex);
	}

	public void populateCollectedData(InstalledPart installedPart) {
		dcViewUtil.populateCollectedData(installedPart);
		setFocusToExpectedInputField();
	}

	public boolean hasScanPart() {
		return DataCollectionModel.hasScanPart(getOperation());
	}

	public boolean isMeasOnlyOperation() {
		return DataCollectionModel.isMeasOnlyOperation(getOperation());
	}

	public void register() {
		EventBusUtil.unregister(instance);
		EventBusUtil.register(this);
		instance = this;
	}

	@Override
	public void setFocusToExpectedInputField() {
		eventHandler.setFocusToNextExpectedInputField();
	}

	@Subscribe
	public void received(DeviceStatusWidgetEvent<? extends IDevice> event) {
		try {
			TorqueDeviceStatusWidget.getInstance().setOperation(this.getOperation());
			if (getOperation().getSelectedPart().hasMeasurements()) {
				TorqueDeviceStatusWidget.getInstance().setDevice(event.getDevice());
				if (event.isShow() && event.getDevice() != null) {
					TorqueDeviceStatusWidget.getInstance().createStatusIndicators(event.getDevice().getId());
					setTorqueWidget(TorqueDeviceStatusWidget.getInstance());
				} else {
					setTorqueWidget(null);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean isValidScan(String serial) {
		if(getModel().getProductModel().getProperty().isAllowProductForPartScan()){ 
			return true;
		}
		if (getModel().getProductModel().getProperty().isRemoveIEnabled()) {
			if (getModel().getProductModel().getProductType().equals("FRAME")) {
				serial = removeLeadingVinChars(serial);
			}
		}
		BaseProduct product = null;
		
		try {
			product = ProductTypeUtil.getProductDao(getModel().getProductModel().getProductType())
					.findBySn(serial);
		}
		catch(ServiceInvocationException ex) {
			logger.info("Ignoring exception while fetching product for serial ("+serial+"). Exception: "+ex.getMessage());
		}

		if (product == null) {
			return true;
		}

		return false;
	}

	public void setTorqueWidget(TorqueDeviceStatusWidget widget) {
		if (getDataCollectionPropertyBean().isShowDeviceStatusAtBottom()) {
			this.setBottom(widget);
		} else {
			this.setRight(widget);
		}
	}

	@Subscribe
	public void handle(KeypadEvent event) {
		
		switch (event.getEventType()) {
		case KEY_ENTER:
			String text = event.getText().toUpperCase();
			String productId = getModel().getProductModel().getProductId();
			if(!text.equals("")) {
				if (!isValidScan(text)) {
					if (!text.equalsIgnoreCase(productId)) {
	
						String message = "An unexpected scan of a product has been detected. \n" + "Scanned (" + text
								+ ") while still completing \n data collection for (" + productId
								+ "). \n\n Click OK and re-scan the Product currently being built.";
						displayPopUp(message);
						getProcessor().getController().cancel();
					}
				} else {
					logger.info(" received Scan :" + text);
					getProcessor().getController().displayErrorMessage(" Unexpected  Scan received "+text);
					getProcessor().getController().getAudioManager().playNGSound();
				}
			}
			break;
		default:
			break;
		}
	}
	
	public String removeLeadingVinChars(String productId){
		String leadingVinChars =PropertyService.getPropertyBean(SystemPropertyBean.class).getLeadingVinCharsToRemove();
		if(StringUtils.isNotBlank(leadingVinChars)){
		String[] vinChars = leadingVinChars.trim().split(",");
		
			for(String c:vinChars){
				if (productId.toUpperCase().startsWith(c)) {
					return productId.substring(c.length());
				}
			}
		}
		return productId;
	}
	
	@Subscribe
	public void received(DataCollectionResultEvent event) {
		try {
			switch(event.getType()) {
				case DC_ERROR_REPORTED:
					setErrorMessageOnException(event.getMessage());
					break;
				default:
					break;
			}
		} catch(Exception ex) {
			getLogger().error(ex, "Unable to process " + event);
		}
	}
	
	private void setErrorMessageOnException(String message) {
		DataCollectionController controller = getProcessor().getController();
		controller.getView().getMainWindow().clearMessage();
		controller.getView().getMainWindow().setErrorMessage(message);
	}
	
}