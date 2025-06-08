package com.honda.galc.client.dc.view.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.device.IDevice;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.ServiceFactory;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ToolDetailsWidget extends AbstractWidget{

	private volatile ConcurrentHashMap<String, Circle> statusCircles = new ConcurrentHashMap<String, Circle>();
	private volatile ConcurrentHashMap<String, Label> statusLabels = new ConcurrentHashMap<String, Label>();
	private List<IDevice> deviceSet = new ArrayList<IDevice>();
	public static long INITIAL_DELAY_MILLISECS = 0;
	public static long INTERVAL_MILLISECS = 500;
	private int WIDTH = 175;
	private int HEIGHT = 150;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private Runnable updateStatus = null;
	
	public ToolDetailsWidget(ProductController productController) {
		super(ViewId.TOOL_DETAILS_WIDGET, productController);
		updateStatus = new Runnable() {
		       public void run() {
		    	   Platform.runLater(new Runnable() {
		   			public void run() {
		   				for (IDevice iDevice : deviceSet) {
							if(iDevice.isConnected())
								statusCircles.get(iDevice.getId()).setFill(Color.GREEN);
							else
								statusCircles.get(iDevice.getId()).setFill(Color.RED);
						}
		   			}
		   		});
		       }
	   };
	   scheduler.scheduleWithFixedDelay(updateStatus, INITIAL_DELAY_MILLISECS, INTERVAL_MILLISECS, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setCenter(createTitiledPane(createDetailsPane(productModel)));
		
	}

	private Node createDetailsPane(ProductModel productModel) {
		deviceSet = new ArrayList<IDevice>();
		HBox detailBox = new HBox();
		List<MCOperationRevision> opRevlist = getProductController().getModel().getProduct().getOperations();
		Set<String> devices = new HashSet<String>();
		//Get device ids from measurement units
		for (MCOperationRevision mcOperationRevision : opRevlist) {
			if(mcOperationRevision.getType().equals(OperationType.GALC_MEAS_MANUAL) || mcOperationRevision.getType().equals(OperationType.GALC_MEAS) ||
					mcOperationRevision.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL) || mcOperationRevision.getType().equals(OperationType.GALC_SCAN_WITH_MEAS))
			{
				List<MCOperationMeasurement> toolOperation = ServiceFactory.getDao(MCOperationMeasurementDao.class).findAllByOperationNamePartIdAndRevision(mcOperationRevision.getId().getOperationName(),
						mcOperationRevision.getSelectedPart().getId().getPartId(), mcOperationRevision.getSelectedPart().getId().getPartRevision());
				if(toolOperation != null && toolOperation.size() > 0 ){
					devices.add(toolOperation.get(0).getDeviceId());
				}
			}
		}
		//Verify which devices needs to enable and disable
		for(IDevice enabledDevices : DeviceManager.getInstance().getDevices().values()){
				String deviceId = enabledDevices.getId();
				IDevice device = DeviceManager.getInstance().getDevice(deviceId);
				if(devices.contains(deviceId) )  {
					deviceSet.add(device);
				}
		}

		float i = deviceSet.size()/3f;
		int numberOfColumns  = (int) Math.ceil(i);
		int widthDevice = WIDTH;
		
		if(numberOfColumns == 1)
			widthDevice = WIDTH-50;
		else if(numberOfColumns>1)
			widthDevice = Math.round(WIDTH/numberOfColumns);
		
			
		
		VBox vBox = null;
		int  k = 1;
		
		for (int j = 0; j < deviceSet.size() ;j++) {
			IDevice device = deviceSet.get(j);
			if(k==1) {
				vBox = new VBox();	
			} 
			vBox.getChildren().add(createStatusIndicator(device.getId(), device.isConnected(), widthDevice));
			if(k == 3 || j == (deviceSet.size()-1)) {
				detailBox.getChildren().add(vBox);
				k =1;
			} else {
				k++;
			}
		}
		
		if(deviceSet.size() == 0) {
			Label label = new Label("NA");
			label.setFont(Font.font("", FontWeight.BOLD, 14));
			detailBox.getChildren().add(label);
		}
			
		return detailBox;
	}

	
	public Node createStatusIndicator(String deviceId, boolean flag, int widthDevice) {
		TilePane pane = new TilePane();
		pane.setPrefSize(widthDevice, 20);
		pane.setPadding(new Insets(3));
		pane.setStyle("-fx-border-color: #808080; -fx-border-width: 1, 1; -fx-border-insets: 1, 1 1 1 1");
		HBox hBox = new HBox();
		hBox.setSpacing(6);
		Circle circle = new Circle(10);
		circle.setId(deviceId);
		if(flag)
			circle.setFill(Color.GRAY);
		else
			circle.setFill(Color.RED);
		
		statusCircles.put(deviceId , circle);
		Label lblName = UiFactory.createLabel(deviceId, deviceId, "20");
		statusLabels.put(deviceId, lblName);
		hBox.getChildren().addAll(circle, lblName);
		pane.getChildren().addAll(hBox);
		return pane;
	}
	private TitledPane createTitiledPane(Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText("Tools Required");
		titledPane.setFont(Font.font("", FontWeight.BOLD, 14));
		titledPane.setContent(content);
		titledPane.setPrefSize(WIDTH, HEIGHT);
		titledPane.setCollapsible(false);
		return titledPane;
	}
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
		this.setCenter(null);
		
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		this.setCenter(null);
	}

}
