package com.honda.galc.client.dc.view;

import java.util.ArrayList;
import java.util.List;



import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.common.ClientDeviceResolution;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.device.DeviceStatusWidgetEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.FxTransitionsUtil;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
public class RearSealTiltDCView extends LotControlOperationView  {

	private static String COMPLETE = "COMPLETE";
	private static String REJECT   = "REJECT";
	private Button completeRejectBtn;
	private static double tiltrange = 0.0;
	
	public RearSealTiltDCView(OperationProcessor opProcessor) {
		super(opProcessor);
	}
	
	@Override
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

		if(getModel().getMeasurementCount(getOperation()) == 4)  {
			if (getOperation().getManufacturingMFGPartList().size() == 1 && skippedMeasurements.size() == 0) {
				addMeasurementBoxes(vBox);
			} else if (skippedMeasurements != null) {
				if (skippedMeasurements.size() > 0) {
					addMeasurementBoxes(vBox);
				}
			}
			borderPane.setTop(vBox);
			
			VBox vBox1 = new VBox(10);
			addCompleteButton(vBox1);
			
			EventHandler<ActionEvent> doneAction = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					finish();
				}
			};
			getCompleteRejectBtn().setOnAction(doneAction);
			
			borderPane.setBottom(vBox1);
			this.setCenter(borderPane);
			
			ProductCheckPropertyBean property = PropertyService.getPropertyBean(ProductCheckPropertyBean.class);
			tiltrange = property.getTiltRange();
				
			
		} else {
			String message = "Incorrect measurement configuration, Please contact IS";
			showErrorMessages(message);
		}
		
	}
	private void handleValidMeasurement(DataCollectionResultEvent event) {
		MeasurementInputData mInputData = (MeasurementInputData) event.getInputData();
		int inputIndex = -1;
		if (hasScanPart()) {
			inputIndex = mInputData.getMeasurementIndex();
		} else {
			inputIndex = mInputData.getMeasurementIndex() - 1;
		}
		handleValidInput(inputIndex);
	}
	

	@Subscribe
	public void received(DataCollectionResultEvent event) {
		try {
			if (getOperation().equals(event.getOperation())) {
				getLogger().info(event.getType() + " received");
				switch(event.getType()) {
				
				case VALID_MEASUREMENT_RECEIVED:
					handleValidMeasurement(event);
					break;

				default:
				}
			}
		} catch(Exception ex) {
			getLogger().error(event.getType() + " Exception Occured");
		}
	}
	
	public Button getCompleteRejectBtn() {
		if (completeRejectBtn == null) {
			completeRejectBtn = UiFactory.createButton(COMPLETE, "", true);
			setBtnImageAndText(COMPLETE);
		}
		return completeRejectBtn;
	}
	
	
	private void setBtnImageAndText(String btnText) {
		Image imageComplete = new Image("resource/images/common/confirm.png");
		Image imageReject = new Image("resource/images/common/reject.png");
		getCompleteRejectBtn().setText(btnText);
		if (btnText.equals(COMPLETE)) {
			getCompleteRejectBtn().setGraphic(StyleUtil.normalizeImage(new ImageView(imageComplete)
					, getViosViewProperty().getCompleteRejectNormalizedImageSize()));
		} else {
			getCompleteRejectBtn().setGraphic(StyleUtil.normalizeImage(new ImageView(imageReject)
					, getViosViewProperty().getCompleteRejectNormalizedImageSize()));
		}
	}
	
	public void addCompleteButton(VBox vBox) {
		EventHandler<KeyEvent> doneAction = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
					finish();
				}
			}
		};
		getCompleteRejectBtn().setOnKeyPressed(doneAction);
		vBox.getChildren().add(getCompleteRejectBtn());
	}
	
	
	protected void finish() {

		InstalledPart installedPart = getModel().getInstalledPartsMap().get(getOperation().getId().getOperationName()); 
		List<Measurement> mesurements = null;
		if(installedPart != null) {
			mesurements = ServiceFactory.getDao(MeasurementDao.class).findAll(installedPart.getId().getProductId(), installedPart.getId().getPartName());
		}
		if(mesurements != null && mesurements.size() == 4) {
			Measurement meas0 = mesurements.get(0);
			Measurement meas1 = mesurements.get(1);
			Measurement meas2 = mesurements.get(2);
			Measurement meas3 = mesurements.get(3);
			List<String> partNames = new ArrayList<String>();
			partNames.add(installedPart.getId().getPartName());
		if (getCompleteRejectBtn().getText().equals(COMPLETE)) {
				getCompleteRejectBtn().setDisable(true);
				Logger.getLogger().check("Complete button is clicked");
					if(mesurements.get(0).getMeasurementValue() != 0.0 && mesurements.get(1).getMeasurementValue() != 0.0 
							&& mesurements.get(2).getMeasurementValue() != 0.0 && mesurements.get(3).getMeasurementValue() != 0.0) {

						if(Math.abs(meas0.getMeasurementValue()-meas1.getMeasurementValue()) <= tiltrange) {
							setTextBoxEffect(0, Color.DEEPSKYBLUE);
							setTextBoxEffect(1, Color.DEEPSKYBLUE);
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_OK, meas0);
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_OK, meas1);
						} else {		
							setTextBoxEffect(0, Color.RED);
							setTextBoxEffect(1, Color.RED);
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_NG, meas0);
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_NG, meas1);
						}
						
						if(Math.abs(meas2.getMeasurementValue()-meas3.getMeasurementValue()) <= tiltrange) {
							setTextBoxEffect(2, Color.DEEPSKYBLUE);
							setTextBoxEffect(3, Color.DEEPSKYBLUE);		
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_OK, meas2);
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_OK, meas3);
						} else {
							setTextBoxEffect(2, Color.RED);
							setTextBoxEffect(3, Color.RED);	
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_NG, meas2);
							publishEvent(DataCollectionEventType.MANUAL_MEASUREMENT_NG, meas3);
						}
						
						if(Math.abs(meas0.getMeasurementValue()-meas1.getMeasurementValue()) <= tiltrange && Math.abs(meas2.getMeasurementValue()-meas3.getMeasurementValue()) <= tiltrange) {
							publishEvent(DataCollectionEventType.OP_COMPLETE, meas0);
						} else {
							String message = "Tilt measurement must be within "+tiltrange+"mm.";
							showErrorMessages(message);
							publishEvent(DataCollectionEventType.OP_INCOMPLETE, meas0);
						}
						getCompleteRejectBtn().setDisable(false);
					} 
				} else {
						setBtnImageAndText(COMPLETE);
						publishEvent(DataCollectionEventType.PDDA_REJECT, meas0);
						
						
						for (int i = 0; i < getInputFields().size(); i++) {
							resetRejectInputField(i);
						}
						TextField txtField0 = getInputFields().get(0);
						UiUtils.requestFocus(txtField0);
						txtField0.setEffect(FxTransitionsUtil.outerGlow(Color.DEEPSKYBLUE));
						Logger.getLogger().check("Reject button is clicked");
			   } 
		} else {
			getCompleteRejectBtn().setDisable(false);
			showErrorMessages("Enter All Measurements to continue..");
		}
	
		
	}

	private void publishEvent(DataCollectionEventType eventType, Measurement meas) {
		EventBusUtil.publish(new DataCollectionEvent(eventType, getMeasurementValue(meas)));
	}

	private void showErrorMessages(String message) {
		getProcessor().getController().clearMessages();
		getProcessor().getController().addErrorMessage(message);
		getProcessor().getController().processMessages();
		getProcessor().getController().getAudioManager().playNGSound();
	}
	
	private InputData getMeasurementValue(Measurement measurement) {
		MeasurementValue measurementValue = new MeasurementValue(measurement.getMeasurementValue());
		measurementValue.setMeasurementIndex(measurement.getId().getMeasurementSequenceNumber());
		return measurementValue;
	}
	
	private void setTextBoxEffect(int index, Color color) {
		TextField txtField1 = getInputFields().get(index);
		txtField1.setEffect(FxTransitionsUtil.innerGlow(color));
	}
	
	public void resetRejectInputField(int index) {
		Button button = getSkipRejectButtons().get(index);
		TextField textInput = getInputFields().get(index);
		String SKIP_IMAGE = "resource/com/honda/galc/client/dc/view/skip_next.png";
		button.setGraphic(setBtnImageView(button, SKIP_IMAGE));
		FxTransitionsUtil.rotateTransition(1, 3000, button.getGraphic());
		
		textInput.setEffect(null);
		textInput.setText("");
		textInput.setEditable(true);
	}
	
	public static ImageView setBtnImageView(Button btn, String imgResource) {
		btn.setId(imgResource);
		Image image = new Image(imgResource);
        ImageView btnImgView = new ImageView(image);
        btnImgView.setImage(image);
        btnImgView.setFitWidth(StyleUtil.getCmdBtnImageWidth());
        btnImgView.setFitHeight(StyleUtil.getCmdBtnImageHeight());
        btnImgView.setPreserveRatio(true);
        btnImgView.setSmooth(true);
        btnImgView.setCache(true);
		return btnImgView;
	}
	
	@Override
	public void populateCollectedData(InstalledPart installedPart) {
		dcViewUtil.populateCollectedData(installedPart);
		boolean measurementStatus = false;
		List<Measurement> mesurements = null;
		if(installedPart != null) {
			mesurements = ServiceFactory.getDao(MeasurementDao.class).findAll(installedPart.getId().getProductId(), installedPart.getId().getPartName());
			for (Measurement measurement : mesurements) {
				TextField txtField = getInputFields().get(measurement.getId().getMeasurementSequenceNumber()-1);
				if(measurement.getMeasurementStatus() == MeasurementStatus.NG){
					txtField.setEffect(FxTransitionsUtil.innerGlow(Color.RED));
					measurementStatus = true;
				} else {
					txtField.setEffect(FxTransitionsUtil.innerGlow(Color.DEEPSKYBLUE));
				} 
			}
		}

		if(installedPart != null && mesurements.size() != 4){
			setBtnImageAndText(COMPLETE);
		} else if ((getOperation() == null
				|| getOperation().getType().equals(OperationType.GALC_MEAS_MANUAL)) && !measurementStatus) {
			setBtnImageAndText(REJECT);
		} else {
			setBtnImageAndText(COMPLETE);
		}
		
		if(isInputComplete()) {
			getCompleteRejectBtn().setFocusTraversable(true);
			UiUtils.requestFocus(getCompleteRejectBtn());
		}

	}
	@Override
	public void setFocusToExpectedInputField() {
		TextField expectedInputField = getNextFocusableTextField(getInputFields(), 0);
		if (expectedInputField != null) {
			setCurrentInputFieldIndex(getInputFields().indexOf(expectedInputField));
			prepareExpectedInputField(getInputFields().indexOf(expectedInputField));
		} else {
			if(isInputComplete()) {
				getCompleteRejectBtn().setFocusTraversable(true);
				UiUtils.requestFocus(getCompleteRejectBtn());
			}
		}
	}
	
	@Override
	public void handleValidInput(int inputIndex){
		eventHandler.handleValidInput(inputIndex);
		setFocusToExpectedInputField();
	}


	public TextField getNextFocusableTextField(List<TextField> list, int startIdx) {
		if (null == list || list.isEmpty()) {
			return null;
		}
		if (startIdx < 0) {
			startIdx = 0;
		}
		int len = list.size();
		TextField tf = null;
		for (int i = startIdx; i < len; i++) {
			tf = list.get(i);
			if (tf.getText().isEmpty()) {
				if (wasInputSkipped(i)) {
					continue;
				}
				return tf;
			}
		}
		return null;
	}

	public boolean wasInputSkipped(int index) {
		Button btn = getSkipRejectButton(index);
		return (btn != null && (btn.getId().equals(DataCollectionViewUtil.COLLECT_SCAN_IMAGE) ||
				btn.getId().equals(DataCollectionViewUtil.COLLECT_TORQUE_IMAGE)));
	}

	public Button getSkipRejectButton(int index) {
			return getSkipRejectButtons().get(index);

	}

	@Override
	protected void setSkipRejectAction(final Button skipRejectBtn) {
		skipRejectBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button) event.getSource());
				dcViewUtil.notifySkipReject(btn);
				InstalledPart installedPart = getModel().getInstalledPartsMap().get(getOperation().getId().getOperationName()); 
				installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
				setBtnImageAndText(COMPLETE);
				
				if(isInputComplete()) {
					getCompleteRejectBtn().setFocusTraversable(true);
					UiUtils.requestFocus(getCompleteRejectBtn());
				}
			}
		});
	}

	public boolean isInputComplete(){
		List<TextField> textFields = getInputFields();
		for(TextField tf:textFields){
			if(tf.isEditable() || StringUtil.isNullOrEmpty(tf.getText())){
				return false;
			}
		}
		return true;
	}
	
	@Subscribe
	public void received(DeviceStatusWidgetEvent<? extends IDevice> event) {
		
	}
}
