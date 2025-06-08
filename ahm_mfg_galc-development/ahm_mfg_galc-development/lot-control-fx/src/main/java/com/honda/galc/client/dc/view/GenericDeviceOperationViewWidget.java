package com.honda.galc.client.dc.view;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


import com.honda.galc.client.dc.common.ClientDeviceResolution;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.mvc.ISkipRejectInputView;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.Torque;
import com.honda.galc.device.dataformat.VoltageMeterResult;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.property.PropertyService;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Min Sun
 * @date Aug 1, 2016
 */
public class GenericDeviceOperationViewWidget extends OperationView implements DeviceListener,ISkipRejectInputView {

	protected static String MEAS_TXT_NAME = "txtMeas";
	protected static final String ZERO_CHECK_UNIT = "MEAS:COND?";
	protected static final String VALUE_CHECK_UNIT = "MEAS:VOLT:DC?";
	protected static final String SUCCESS_FLG = "1";
	protected static final String REJECT   = "REJECT";

	protected BorderPane borderPane = null;
	protected volatile ArrayList<TextField> inputFields = new ArrayList<TextField>();
	protected volatile ArrayList<Button> skipRejectBtns = new ArrayList<Button>();
	protected volatile DataCollectionViewUtil dcViewUtil;
	protected LotControlViewEventHandler eventHandler = null;
	private volatile Button RejectBtn = null;

	public GenericDeviceOperationViewWidget(OperationProcessor opProcessor) {
		super(opProcessor);
		this.dcViewUtil = new DataCollectionViewUtil(this, getModel());
		setCmdButtonStyle();
		this.eventHandler = new LotControlViewEventHandler(this);
		registerDeviceListener();
		init();
	}
	
	private void registerDeviceListener() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		if (device != null)	device.registerDeviceListener(this, getDeviceData());
	}
	
	private List<IDeviceData> getDeviceData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new VoltageMeterResult());
		return list;
	}
	public IDeviceData received(String clientId, final IDeviceData deviceData) {
		
		String deviceMsg = getDeviceMsg();
		if (getDeviceMsg() != null 
				&&deviceData instanceof VoltageMeterResult) {
			if (deviceMsg.equals(VALUE_CHECK_UNIT)) 
				setVoltMeterValue(((VoltageMeterResult) deviceData).getVoltageMeterResult());
			if (deviceMsg.equals(ZERO_CHECK_UNIT))
				voltageMeterResultReceived((VoltageMeterResult)deviceData);
			return DataCollectionComplete.OK();
		}
		return null;
	}
	
	private String getDeviceMsg() {
		return getModel().getCurrentOperation().getSelectedPart().getMeasurements().get(0).getDeviceMsg();
	}

	private void voltageMeterResultReceived(VoltageMeterResult voltageMeterResult){
		if (voltageMeterResult.getVoltageMeterResult().equals(SUCCESS_FLG)) {
			TextField textFieldMeasurement = UiFactory.createTextField(MEAS_TXT_NAME);
			inputFields.add(textFieldMeasurement);
			Button skipRejectBtn = dcViewUtil.createSkipRejectBtn();
			skipRejectBtns.add(skipRejectBtn);
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_RECEIVED, getTorque("1")));
		}
		else getProcessor().getController().displayErrorMessage("Voltage Meter Result Check:fail!");
	}

	public void setCmdButtonStyle() {
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		StyleUtil.setCmdBtnImageWidth(property.getCommandImageWidth());
		StyleUtil.setCmdBtnWidth(property.getCommandBtnWidth());
		StyleUtil.setCmdBtnImageHeight(property.getCommandImageHeight());
		StyleUtil.setCmdBtnHeight(property.getCommandBtnHeight());
	}
	
	private void init() {
		borderPane = new BorderPane();
		String width = ClientDeviceResolution.getClientResolutionProperty("DC_VIEW_WIDTH_","width",null);
		if(width!=null){
			borderPane.setMaxWidth(Double.parseDouble(width));
		}
		
		borderPane.setStyle("-fx-background-color: #ECF3FF;");
		VBox vBox = new VBox(10);
		ArrayList<Integer> skippedMeasurements = getModel().getSkippedMeasurementsMap().get(getOperation().getId().getOperationName());
		if (skippedMeasurements == null) {
			skippedMeasurements = new ArrayList<Integer>();
		}
		String deviceMsg =  getDeviceMsg();
		if (deviceMsg != null && deviceMsg.equals(VALUE_CHECK_UNIT)) {
			addMeasurementBoxes(vBox);
			borderPane.setTop(vBox);
		}
		if (deviceMsg != null && deviceMsg.equals(ZERO_CHECK_UNIT)) {
			if(getModel().isCurrentOperationComplete()) {
				addRejectButton(vBox);
				borderPane.setBottom(vBox);
			}
		}
		this.setCenter(borderPane);
		broadcastDataContainer();
	}

	public void addMeasurementBoxes(VBox vBox) {
		int fontSize =  Integer.parseInt(ClientDeviceResolution.getClientResolutionProperty("DC_VIEW_FONT_SIZE_","font",String.valueOf(getFontSize())));
		TextFieldState measurementState = TextFieldState.READ_ONLY;
		HBox measRow = new HBox(10);
		VBox measColumn = null;
		getModel().setCurrentMeasurementIndex(0);
		measColumn = new VBox(10);
		measColumn.getChildren().add(UiFactory.createLabel("lblEmpty", " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 5));
		measRow.getChildren().add(measColumn);
		MCOperationMeasurement measurementSpec = getProcessor().getOperation().getSelectedPart().getMeasurement(0);
		String minMax = new Double(measurementSpec.getMinLimit()).toString() + "/" + new Double(measurementSpec.getMaxLimit());
		HBox hBox = new HBox(10);
		TextField textFieldMeasurement = UiFactory.createTextField(MEAS_TXT_NAME,Fonts.SS_DIALOG_BOLD(fontSize), measurementState, getTorqueBoxWidth(), getInputBoxHeight());
		textFieldMeasurement.setPrefWidth(getTorqueBoxWidth());
		textFieldMeasurement.setMaxWidth(getTorqueBoxWidth());
		textFieldMeasurement.setPromptText(minMax);
		inputFields.add(textFieldMeasurement);
		textFieldMeasurement.setFocusTraversable(true);
		Button skipRejectBtn = dcViewUtil.createSkipRejectBtn();
		setSkipRejectAction(skipRejectBtn);
		skipRejectBtns.add(skipRejectBtn);

		hBox.getChildren().add(UiFactory.createLabel("lblEmpty", " ", Fonts.SS_DIALOG_BOLD(getFontSize()), 5));
		hBox.getChildren().add(textFieldMeasurement);
		hBox.getChildren().add(skipRejectBtn);
		measColumn.getChildren().add(hBox);
		vBox.getChildren().add(measRow);
	}

	protected void setSkipRejectAction(final Button skipRejectBtn) {
		skipRejectBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button) event.getSource());
				dcViewUtil.notifySkipReject(btn);
			}
		});
	}

	public ArrayList<TextField> getInputFields() {
		return inputFields;
	}

	public void setInputFields(ArrayList<TextField> inputFields) {
		this.inputFields = inputFields;
	}

	public DataCollectionModel getModel() {
		return getProcessor().getController().getModel();
	}

	public List<Button> getSkipRejectButtons() {
		return skipRejectBtns;
	}

	public void setSkipRejectButtons(ArrayList<Button> skipRejectBtns) {
		this.skipRejectBtns = skipRejectBtns;
	}

	private void setVoltMeterValue(String voltMeterResult) {
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_RECEIVED, getTorque(voltMeterResult)));
		getInputFields().get(getCurrentInputFieldIndex()).setText(voltMeterResult);
	}
	
	//broadcast info to device
	private void broadcastDataContainer() {
		if (!getViosViewProperty().isBroadcastToDevice()) {
			getLogger().info("no broadcast to device.");
			return;
		}


		String deviceSeq = getModel().getCurrentOperation().getStructure().getOperationRevisionPlatform().getDeviceId();
		if (StringUtils.isEmpty(deviceSeq))
			deviceSeq = "1";
		try {
			getService(BroadcastService.class).broadcast(
							getProcessor().getController().getProductModel().getProcessPoint().getProcessPointId(),
							Integer.parseInt(deviceSeq), getProcessor().getController().getProductModel().getProductId());
		} catch (Exception e) {
			getProcessor().getController().displayErrorMessage("failed to broadcast Unit Information to deviceWise.");
			getLogger().error(e);
		}
		getProcessor().getController().displayMessage("Unit Information has been sent to deviceWise successfully.");
	}
	
	private Torque getTorque(String voltMeterResult) {
		Torque torque = new Torque();
		torque.setAngle((double) 0);
		torque.setAngleStatus(1);
		torque.setTorqueValue(Double.parseDouble(voltMeterResult));
		torque.setTorqueStatus(1);
		torque.setTighteningStatus(1);
		torque.setSequence(1);
		torque.setMeasurementIndex(1);
		return torque;
	}

	private PDDAPropertyBean getViosViewProperty() {
		return PropertyService.getPropertyBean(PDDAPropertyBean.class,
				getModel().getProductModel().getProcessPointId());
	}

	private int getFontSize() {
		return getViosViewProperty().getDcViewFontSize();
	}

	private int getInputBoxHeight() {
		return getViosViewProperty().getInputBoxHeight();
	}

	private int getTorqueBoxWidth() {
		return getViosViewProperty().getTorqueBoxWidth();
	}

	public void populateCollectedData(InstalledPart installedPart) {
		if (getDeviceMsg() != null ) {
			if (getDeviceMsg().equals(VALUE_CHECK_UNIT))  {
				dcViewUtil.populateCollectedData(installedPart);
				setFocusToExpectedInputField();
			}
			if (getDeviceMsg().equals(ZERO_CHECK_UNIT))  {
				borderPane = new BorderPane();
				VBox vBox = new VBox(10);
				borderPane.setBottom(vBox);
				addRejectButton(vBox);
				this.setRight(borderPane);
			}
		}
	}
	
	private Button getRejectBtn() {
		if (RejectBtn == null) {
			RejectBtn = UiFactory.createButton("", StyleUtil.getCompleteBtnStyle(20), true);
			RejectBtn.setPrefWidth(200);
			getRejectBtn().setText(REJECT);
		}
		return RejectBtn;
	}
	
	private void addRejectButton(VBox vBox) {
		getRejectBtn().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				 if (getRejectBtn().getText().equals(REJECT)){
					 borderPane.setBottom(new VBox());
					EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PDDA_REJECT, null));
					getModel().setCurrentMeasurementIndex(0);
					getLogger().check("Reject button is clicked");
				} 
			}
		});
		vBox.getChildren().add(getRejectBtn());
	}

}