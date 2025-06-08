package com.honda.galc.client.dc.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.event.PsetCommandAcceptedEvent;
import com.honda.galc.client.dc.event.PsetErrorEvent;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.device.IDevice;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.property.PropertyService;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * @author Subu Kathiresan
 * @date May 19, 2015
 */
public class TorqueDeviceStatusWidget extends BorderPane {

	public static String CONNECTED = "Connected";
	public static String TOOL_ENABLED = "Tool Enabled";
	public static String LAST_PING = "Last Ping";
	public static String PSET = "PSet";
	public static long INITIAL_DELAY_MILLISECS = 0;
	public static long INTERVAL_MILLISECS = 500;
	public static TorqueDeviceStatusWidget instance = null;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private Runnable updateStatus = null;
	private volatile MCOperationRevision operation = null;
	private volatile IDevice device = null;
	private volatile Label lblDeviceName = null;
	
	private volatile ConcurrentHashMap<String, Circle> statusCircles = new ConcurrentHashMap<String, Circle>();
	private volatile ConcurrentHashMap<String, Label> statusLabels = new ConcurrentHashMap<String, Label>();
	
	private boolean psetError = false;
	
	private TorqueDeviceStatusWidget() {
		EventBusUtil.register(this);
		updateStatus = new Runnable() {
		       public void run() {
		    	   scheduleStatusUpdate();
		       }
	   };
	   scheduler.scheduleWithFixedDelay(updateStatus, INITIAL_DELAY_MILLISECS, INTERVAL_MILLISECS, TimeUnit.MILLISECONDS);
	}
	
	private void scheduleStatusUpdate() {
		Platform.runLater(new Runnable() {
			public void run() {
				updateStatusIndicators();
			}
		});
	}
    
	private void updateStatusIndicators() {
		try {
			if (device == null || !(device instanceof TorqueSocketDevice)) {
				return;
			}
			
			TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
			refreshToolConnectedStatus(torqueDevice);
			refreshToolEnabledStatus(torqueDevice);
			refreshToolPsetValue(torqueDevice);
			refreshLastPingStatus(torqueDevice);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void refreshToolPsetValue(TorqueSocketDevice torqueDevice) {
		statusLabels.get(device.getId() + PSET).setText(PSET + ": " + torqueDevice.getCurrentInstructionCode());
	}

	public void refreshToolConnectedStatus(TorqueSocketDevice torqueDevice) {
		if (torqueDevice.isConnected()) {
			statusCircles.get(device.getId() + CONNECTED).setFill(Color.GREEN);
			if (!psetError) {
				statusCircles.get(device.getId() + PSET).setFill(Color.GREEN);
			} else {
				statusCircles.get(device.getId() + PSET).setFill(Color.RED);
			}
		} else {
			statusCircles.get(device.getId() + CONNECTED).setFill(Color.RED);
			statusCircles.get(device.getId() + PSET).setFill(Color.RED);
		}
	}

	public void refreshToolEnabledStatus(TorqueSocketDevice torqueDevice) {
		if (torqueDevice.isToolEnabled() && torqueDevice.isConnected()) {
			statusCircles.get(device.getId() + TOOL_ENABLED).setFill(Color.GREEN);
		} else {
			statusCircles.get(device.getId() + TOOL_ENABLED).setFill(Color.RED);
		}
	}

	public void refreshLastPingStatus(TorqueSocketDevice torqueDevice) {
		Circle circle = statusCircles.get(device.getId() + LAST_PING);
		statusLabels.get(device.getId() + LAST_PING).setText(LAST_PING + ": " + getLastPingTime(torqueDevice));

		long currentTime = new Date().getTime();
		if (currentTime > torqueDevice.getLastPingReply() && currentTime < (torqueDevice.getLastPingReply() + 1000)) {
			animateGreenStatusIndicator(circle);
		} 
		
		if (currentTime >(torqueDevice.getLastPingReply() + 10000)) {
			circle.setOpacity(1.0);
			circle.setFill(Color.RED);
		}
	}

	public void animateGreenStatusIndicator(Circle circle) {
		circle.setFill(Color.GREEN);
		FadeTransition ft = new FadeTransition(Duration.millis(10000), circle);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.play();
	}
	
	public String getLastPingTime(TorqueSocketDevice torqueDevice) {
		if (torqueDevice.getLastPingReply() == -1L) {
			return "Unknown";
		}
		return getDateFormat().format(torqueDevice.getLastPingReply());	
	}
	
	public void createStatusIndicators(String deviceId) {
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		VBox statusVBox = new VBox(5);
		statusVBox.setPrefWidth(property.getTorquePanelWidth());
		statusVBox.setStyle(StyleUtil.getStatusIndicatorStyle());
		lblDeviceName = UiFactory.createLabel("lblDeviceName", deviceId, Fonts.SS_DIALOG_BOLD(property.getTorquePanelHeadingFont()), 100);
		statusVBox.getChildren().add(lblDeviceName);
		statusVBox.getChildren().add(createHostDetails());
		statusVBox.getChildren().add(createStatusIndicator(deviceId, CONNECTED));
		statusVBox.getChildren().add(createStatusIndicator(deviceId, TOOL_ENABLED));
		statusVBox.getChildren().add(createStatusIndicator(deviceId, PSET));
		statusVBox.getChildren().add(createStatusIndicator(deviceId, LAST_PING));
		
		this.setBottom(statusVBox);
	}

	
	private HBox createHostDetails(){
		HBox hbox = new HBox(10);
		
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		Label lblHostName = UiFactory.createLabel("lblHostName", "      ", Fonts.SS_DIALOG_PLAIN(property.getTorquePanelFont()), 200.0);
		TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
		lblHostName.setText(torqueDevice.getHostName()+":"+torqueDevice.getPort());
		hbox.getChildren().add(lblHostName);
		
		return hbox;
	}


	public HBox createStatusIndicator(String deviceId, String id) {
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		HBox hBox = new HBox(10);
		
		Circle circle = new Circle(10);
		circle.setId(deviceId + id);
		circle.setFill(Color.ORANGE);
		statusCircles.put(deviceId + id, circle);
		
		Label lblName = UiFactory.createLabel("lblName", "      ", Fonts.SS_DIALOG_PLAIN(property.getTorquePanelFont()), 200.0);
		lblName.setId(deviceId + id);
		lblName.setText(id);
		statusLabels.put(deviceId + id, lblName);
		
		hBox.getChildren().add(circle);
		hBox.getChildren().add(lblName);
		
		return hBox;
	}
	
	public SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("h:mm:ss");
	}
	
	public MCOperationRevision getOperation() {
		return operation;
	}

	public void setOperation(MCOperationRevision operation) {
		this.operation = operation;
	}
	
	public static TorqueDeviceStatusWidget getInstance() {
		if (instance == null) {
			instance = new TorqueDeviceStatusWidget();
		}
		return instance;
	}
	
	public IDevice getDevice() {
		return device;
	}

	public void setDevice(IDevice device) {
		this.device = device;
	}
	
	@Subscribe
	public void received(PsetErrorEvent psetErrorEvent) {
		psetError = true;	
	}
	
	@Subscribe
	public void received(PsetCommandAcceptedEvent psetCommandAcceptedEvent) {
		psetError = false;
	}
	
}
