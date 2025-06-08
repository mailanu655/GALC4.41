package com.honda.galc.device.simulator.torque;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.property.TorqueDevicePropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.client.DeviceSimulatorMainFx;
import com.honda.galc.device.simulator.client.IDeviceSimulator;
import com.honda.galc.device.simulator.client.TorqueSender;
import com.honda.galc.device.simulator.client.ui.StyleUtil;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.property.DevicePropertyBean;

/**
 * @author Subu Kathiresan
 * @date Jan 6, 2015
 */
public class TorqueSimulator implements IDeviceSimulator {

	public static String CONNECTED = "Connected";
	public static String TOOL_ENABLED = "Tool Enabled";
	public static String LAST_PING = "Last Ping";
	public static String PSET = "PSet";
	
	public static String TORQUE_INPUT = "Torque";
	public static String ANGLE_INPUT = "Angle";
	public static String TIGHTENING_ID_INPUT = "Tid";
	
	public static String REVERSE_TORQUE_INPUT = "Reverse Torque";
	public static String TIGHTENING_STATUS_INPUT = "Tightening Status NG";
	public static String TORQUE_STATUS_INPUT = "Torque Status NG";
	public static String ANGLE_STATUS_INPUT = "Angle Status NG";
	
	private volatile ArrayList<TorqueDevicePropertyBean> torqueDevicePropertyBeans = new ArrayList<TorqueDevicePropertyBean>();
	private volatile ConcurrentHashMap<String, VirtualTorqueDevice> torqueDevices = new ConcurrentHashMap<String, VirtualTorqueDevice>();
	private volatile ConcurrentHashMap<String, HBox> hBoxes = new ConcurrentHashMap<String, HBox>();
	
	private volatile ArrayList<TextField> textInputs = new ArrayList<TextField>();
	private volatile ArrayList<CheckBox> checkBoxInputs = new ArrayList<CheckBox>();
	
	private volatile ConcurrentHashMap<String, Circle> statusCircles = new ConcurrentHashMap<String, Circle>();
	private volatile ConcurrentHashMap<String, Label> statusLabels = new ConcurrentHashMap<String, Label>();
	
	private TorqueSender torqueSender;
	private Tab torqueSimTab;
	
	public TorqueSimulator() {
		EventBusUtil.register(this);

		this.torqueSimTab = new Tab("Torque Devices");
		VBox vBox = addHeaderVBox(torqueSimTab);
		startDevices(torqueSimTab, vBox);
	}

	public VBox addHeaderVBox(Tab torqueSimTab) {
		VBox vBox = new VBox(10);
		vBox.getChildren().add(UiUtils.createNewEmptyLabel("HeaderLabel"));
		vBox.getChildren().add(UiFactory.createLabel("terminalLabel", "Terminal: " + getTerminalName(), Fonts.SS_DIALOG_BOLD(18)));
		torqueSimTab.setContent(vBox);
		return vBox;
	}

	private void startDevices(Tab torqueSimTab, VBox vBox) {
		
		List<ComponentProperty> properties = getProperties(getTerminalName(), "device.*deviceId\\d*");
		System.out.println("Loading properties for " + getTerminalName());
		for(ComponentProperty property : properties) {
			System.out.println(property.getPropertyKey() + ": " + property.getPropertyValue());
			DevicePropertyBean devicePropertyBean = DeviceManager.getInstance().getDevicePropertyBean(getTerminalName(), property);
			if(StringUtils.isEmpty(devicePropertyBean.getDeviceId())) {
				continue;
			}
			
			if (devicePropertyBean instanceof TorqueDevicePropertyBean) {
				System.out.println("Creating Torque simulator view for " + devicePropertyBean.getDeviceId());
				createTorqueSimulatorView(torqueSimTab, vBox, devicePropertyBean);
			}
		}
	}

	private void createTorqueSimulatorView(Tab torqueSimTab, VBox vBox, DevicePropertyBean devicePropertyBean) {
		TorqueDevicePropertyBean torqueDevicePropertyBean = (TorqueDevicePropertyBean) devicePropertyBean;
		String deviceId = torqueDevicePropertyBean.getDeviceId();
		torqueDevicePropertyBeans.add(torqueDevicePropertyBean);
		
		startVirtualTorqueDevice(torqueDevicePropertyBean, deviceId);
		
		HBox hBox = new HBox(10);
		hBox.setId(deviceId);

		Label labelDevice = UiFactory.createLabel("labelDevice", deviceId + ":" + torqueDevicePropertyBean.getPort(), Fonts.SS_DIALOG_PLAIN(20), 150.0);
		labelDevice.setPrefHeight(60);

		hBox.getChildren().add(labelDevice);
		hBox.getChildren().add(createInputBoxes(deviceId));
		hBox.getChildren().add(createSeparator());
		hBox.getChildren().add(createStatusIndicators(deviceId));
		hBox.setStyle(StyleUtil.getDeviceBoxStyle());
		
		hBoxes.put(deviceId, hBox);
		vBox.getChildren().add(hBox);
	}

	public void startVirtualTorqueDevice(TorqueDevicePropertyBean torqueDevicePropertyBean, String deviceId) {
		VirtualTorqueDevice vTorqueDevice = new VirtualTorqueDevice(deviceId, torqueDevicePropertyBean.getPort());
		vTorqueDevice.start();
		torqueDevices.put(deviceId, vTorqueDevice);
	}

	public VBox createInputBoxes(String deviceId) {
		VBox inputVBox = new VBox();
		inputVBox.setSpacing(5);
		
		addTextInput(deviceId, inputVBox, TORQUE_INPUT);
		addTextInput(deviceId, inputVBox, ANGLE_INPUT);
		addTextInput(deviceId, inputVBox, TIGHTENING_ID_INPUT);
		
		GridPane cbGridPane = new GridPane();
		cbGridPane.setHgap(10);
		cbGridPane.setVgap(10);
		
		addCheckBoxInput(deviceId, cbGridPane, REVERSE_TORQUE_INPUT, 0, 0);
		addCheckBoxInput(deviceId, cbGridPane, TIGHTENING_STATUS_INPUT, 0, 1);
		addCheckBoxInput(deviceId, cbGridPane, TORQUE_STATUS_INPUT, 1, 0);
		addCheckBoxInput(deviceId, cbGridPane, ANGLE_STATUS_INPUT, 1, 1);
		inputVBox.getChildren().add(cbGridPane);
		
		Button torqueSendBtn = UiFactory.createButton("Send Torque", StyleUtil.getBtnStyle(), true);
		torqueSendBtn.setId(deviceId);
		setTorqueSendBtnAction(torqueSendBtn);
		inputVBox.getChildren().add(torqueSendBtn);
		
		return inputVBox;
	}

	public void addTextInput(String deviceId, VBox inputVBox, String inputName) {
		TextField txtInput = UiFactory.createTextField("txtInput", Fonts.SS_DIALOG_BOLD(35), TextFieldState.EDIT, 200.0);
		txtInput.setId(deviceId + inputName);
		txtInput.setPromptText(inputName);
		setTextInputAction(txtInput);
		inputVBox.getChildren().add(txtInput);
		textInputs.add(txtInput);
	}
	
	public void addCheckBoxInput(String deviceId, GridPane gridPane, String inputName, int row, int col) {
		CheckBox chkBoxInput = new CheckBox(inputName);
		chkBoxInput.setId(deviceId + inputName);
		gridPane.add(chkBoxInput, row, col);
		checkBoxInputs.add(chkBoxInput);
	}
	
	public Separator createSeparator() {
		Separator vSeparator = new Separator();
		vSeparator.setPrefWidth(100);
		vSeparator.setOrientation(Orientation.VERTICAL);
		return vSeparator;
	}

	public VBox createStatusIndicators(String deviceId) {
		VBox statusVBox = new VBox();
		statusVBox.setStyle(StyleUtil.getStatusIndicatorStyle());
		statusVBox.setSpacing(5.0);
		statusVBox.getChildren().add(createStatusIndicator(deviceId, CONNECTED));
		statusVBox.getChildren().add(createStatusIndicator(deviceId, TOOL_ENABLED));
		statusVBox.getChildren().add(createStatusIndicator(deviceId, PSET));
		statusVBox.getChildren().add(createStatusIndicator(deviceId, LAST_PING));
		
		return statusVBox;
	}

	public HBox createStatusIndicator(String deviceId, String id) {
		
		HBox hBox = new HBox();
		hBox.setSpacing(10.0); 
		
		Circle circle = new Circle(10);
		circle.setId(deviceId + id);
		circle.setFill(Color.ORANGE);
		statusCircles.put(deviceId + id, circle);
		
		Label lblName = UiFactory.createLabel("lblName", "      ", Fonts.SS_DIALOG_PLAIN(18), 200.0);
		lblName.setId(deviceId + id);
		lblName.setText(id);
		statusLabels.put(deviceId + id, lblName);
		
		hBox.getChildren().add(circle);
		hBox.getChildren().add(lblName);
		
		return hBox;
	}

	private void setTorqueSendBtnAction(final Button torqueSendBtn) {
		torqueSendBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button)event.getSource());
				System.out.println("Send Torque btn clicked for device: " + torqueSendBtn.getId());
				DataContainer dc = collectTighteningResultData(btn);
				getTorqueSender().send(torqueDevices.get(btn.getId()), dc);
			}
		});
	}
	
	public DataContainer collectTighteningResultData(Button btn) {
		DataContainer dc = new DefaultDataContainer();
		addTxtInputTag(btn.getId(), "TORQUE", TORQUE_INPUT, dc);
		addTxtInputTag(btn.getId(), "ANGLE", ANGLE_INPUT, dc);
		addTxtInputTag(btn.getId(), "TIGHTENING_ID", TIGHTENING_ID_INPUT, dc);
		
		addChkBoxInputTag(btn.getId(), "TIGHTENING_STATUS", REVERSE_TORQUE_INPUT, dc);
		addChkBoxInputTag(btn.getId(), "TIGHTENING_STATUS", TIGHTENING_ID_INPUT, dc);
		addChkBoxInputTag(btn.getId(), "TORQUE_STATUS", TORQUE_STATUS_INPUT, dc);
		addChkBoxInputTag(btn.getId(), "ANGLE_STATUS", ANGLE_STATUS_INPUT, dc);
		return dc;
	}
	
	private void addTxtInputTag(String id, String tagName, String tagInputName, DataContainer dc) {
		String input = StringUtils.trimToEmpty(getTextInputValue(id, tagInputName));
		if (!input.equals("")) {
			dc.put(tagName, input);
		}
	}

	private void addChkBoxInputTag(String id, String tagName, String tagInputName, DataContainer dc) {
		Boolean checked = getCheckBoxInputValue(id, tagInputName);

		if (checked) {
			if (tagInputName.equals(REVERSE_TORQUE_INPUT)) {
				dc.put(tagName, "2");
			} else {
				dc.put(tagName, "0");
			}
		}
	}

	
	private String getTextInputValue(String deviceId, String inputName) {
		for(TextField txtField: textInputs) {
			if (txtField.getId().equals(deviceId + inputName)) {
				String torqueValue = txtField.getText();
				txtField.clear();
				return torqueValue;
			} else {
				continue;
			}
		}
		return "";
	}
	
	private boolean getCheckBoxInputValue(String deviceId, String inputName) {
		for(CheckBox chkBox: checkBoxInputs) {
			if (chkBox.getId().equals(deviceId + inputName)) {
				return chkBox.isSelected();
			} else {
				continue;
			}
		}
		return false;
	}
	
	private void setTextInputAction(final TextField txt) {
		txt.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String val = ((TextField) event.getSource()).getText();
				System.out.println(((TextField) event.getSource()).getId() + " input entered:" + val);
			}
		});
	}

	@Subscribe
	public void received(OPMessageEvent opMessageEvent) {
		
		switch (opMessageEvent.getMsgType()) {
		case communicationStart:
			statusCircles.get(opMessageEvent.getDeviceId() + CONNECTED).setFill(Color.GREEN);
			break;
		case communicationStop:
			statusCircles.get(opMessageEvent.getDeviceId() + CONNECTED).setFill(Color.RED);
			break;
		case keepAlive:
			Circle circle = statusCircles.get(opMessageEvent.getDeviceId() + LAST_PING);
			circle.setFill(Color.GREEN);
			
			statusLabels.get(opMessageEvent.getDeviceId() + LAST_PING).setText(LAST_PING + ": " + getDateFormat().format(new Date().getTime()));
			animateStatusIndicator(circle);
			break;
		case toolEnable:
			statusCircles.get(opMessageEvent.getDeviceId() + TOOL_ENABLED).setFill(Color.GREEN);
			break;
		case toolDisable:
			statusCircles.get(opMessageEvent.getDeviceId() + TOOL_ENABLED).setFill(Color.RED);
			break;
		case selectParamSet:
			statusCircles.get(opMessageEvent.getDeviceId() + PSET).setFill(Color.GREEN);
			statusLabels.get(opMessageEvent.getDeviceId() + PSET).setText(PSET + ": " + opMessageEvent.getMessage().substring(20));
		default:
			break;
		}
	}

	public void animateStatusIndicator(Circle circle) {
		FadeTransition ft = new FadeTransition(Duration.millis(10000), circle);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.play();
	}
	
	public TorqueSender getTorqueSender() {
		if (torqueSender == null) {
			torqueSender = new TorqueSender();
		}
		return torqueSender;
	}
	
	public void setTorqueSender(TorqueSender torqueSender) {
		this.torqueSender = torqueSender;
	}
	
	public String getTerminalName() {
		return DeviceSimulatorMainFx.getTerminalName();
	}
	
	public List<ComponentProperty> getProperties(String componentId, String regex) {
		return DeviceSimulatorMainFx.getProperties(componentId, regex);
	}
	
	public SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("h:mm:ss");
	}

	public Tab getDeviceSimTab() {
		return torqueSimTab;
	}

	public void SetDeviceSimTab(Tab deviceSimTab) {
		torqueSimTab = deviceSimTab;
	}
}
