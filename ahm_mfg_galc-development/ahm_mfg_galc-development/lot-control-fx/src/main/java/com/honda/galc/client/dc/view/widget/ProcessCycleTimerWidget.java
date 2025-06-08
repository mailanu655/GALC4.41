package com.honda.galc.client.dc.view.widget;


/**
 * @author Suriya Sena
 * @date 24 Oct 2013
 */
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.property.PropertyService;

public class ProcessCycleTimerWidget extends AbstractWidget {

	private Timeline timer;
	private int   currentCycleTime = 0;
	private int   countStartSecond = 0;
	private static final int SAMPLE_SIZE = 20;
	private int[] cycleTime;
	private int   cycleIndex = 0;
	private float referenceLineSpeed;
	private  Map<String,Integer>  lineSpeedMap;
	private final String DEFAULT_LINE = "DEFAULT";

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Rectangle currentCycleRect;

	@FXML
	private LoggedLabel currentCycleValue;

	@FXML
	private Rectangle lineSpeedRect;

	@FXML
	private LoggedLabel lineSpeedValue;

	private FloatProperty lineSpeedFloatValue;

	@FXML
	private Rectangle runningAveRect;

	@FXML
	private LoggedLabel runningAvgValue;

	public ProcessCycleTimerWidget(ProductController productController) {
		super(ViewId.PROCESS_CYCLETIMER_WIDGET, productController);
		countStartSecond = getCountStartSecond(productController.getModel().getProcessPointId());
		currentCycleTime = countStartSecond;
		
	}


	@FXML
	void initialize() {
		assert currentCycleRect != null : "fx:id=\"currentCycleRect\" was not injected: check your FXML file 'ProcessCycleTimer.fxml'.";
		assert currentCycleValue != null : "fx:id=\"currentCycleValue\" was not injected: check your FXML file 'ProcessCycleTimer.fxml'.";
		assert lineSpeedRect != null : "fx:id=\"lineSpeedRect\" was not injected: check your FXML file 'ProcessCycleTimer.fxml'.";
		assert lineSpeedValue != null : "fx:id=\"lineSpeedValue\" was not injected: check your FXML file 'ProcessCycleTimer.fxml'.";
		assert runningAveRect != null : "fx:id=\"runningAveRect\" was not injected: check your FXML file 'ProcessCycleTimer.fxml'.";
		assert runningAvgValue != null : "fx:id=\"runningAvgValue\" was not injected: check your FXML file 'ProcessCycleTimer.fxml'.";
	}

	private final static String floatFormat = "%.0f";


	public String getCurrentCycleValue() {
		return currentCycleValue.textProperty().get();
	}

	private void setCurrentCycleValue(int value) {
		currentCycleValue.textProperty().set(String.format("%d", value));
	}
//
//	private FloatProperty currentCycleValueProperty() {
//		return lineSpeedFloatValue;
//	}

	public void startCycle() {

	}


	private void calculateMean() {
		int   n;
		int   sum=0;
		float mean;
		for (n=0; n< SAMPLE_SIZE; n++) {
		   	if (cycleTime[n] == Integer.MIN_VALUE) {
		   		// ignore non measurements 
		   		break;
		   	}
		   	sum+=cycleTime[n];
		}
		mean = (float) (sum/n); 
	//	System.out.printf(" n %d,sum %d, mean %f", n, sum, mean);
		
		if ( mean <= referenceLineSpeed) {
		   runningAveRect.setFill(Color.GREEN);
		} else {
		   runningAveRect.setFill(Color.RED);
		}
		
		runningAvgValue.textProperty().set(String.format(floatFormat,mean));
	}
	
	public void startClock() {
		if (timer!=null) {
			cycleTime[cycleIndex]=currentCycleTime;
			cycleIndex = (cycleIndex + 1) %SAMPLE_SIZE;
            calculateMean();
			currentCycleTime = countStartSecond;
			timer.stop();
		}
		
	    timer =  new Timeline(new KeyFrame( Duration.seconds(1),new EventHandler<javafx.event.ActionEvent>() {
		  public void handle(javafx.event.ActionEvent event) {
				if (currentCycleTime <= referenceLineSpeed) {
					currentCycleRect.setFill(Color.GREEN);
				} else {
					currentCycleRect.setFill(Color.RED);
				}
				setCurrentCycleValue(currentCycleTime++);
		  };
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}
	
	public void stopClock() {
		timer.stop();
		setCurrentCycleValue(countStartSecond);
	}
	
	
	
	@Override
	protected void initComponents() {
		
		//URL url = WidgetHelper.getInstance().getViewURL(this);
		URL url = getClass().getResource("/resource/com/honda/galc/client/dc/view/widget/ProcessCycleTimerWidget.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		EventBusUtil.register(this);
		
		// Initialize values so that average calculated over valid cycles 
		cycleTime = new int[SAMPLE_SIZE];
		for (int i =0; i< SAMPLE_SIZE; i++) {
			cycleTime[i] = Integer.MIN_VALUE;
		}

		lineSpeedFloatValue = new SimpleFloatProperty();
		lineSpeedFloatValue.addListener(new ChangeListener() {
			//@Override
			public void changed(ObservableValue o, Object oldVal, Object newVal) {
				currentCycleValue.textProperty().set(
						String.format(floatFormat, lineSpeedFloatValue.get()));
			}
		});

		try {
			fxmlLoader.load();
			currentCycleValue.setStyle("-fx-text-fill:white;");
			lineSpeedValue.setStyle("-fx-text-fill:white;");
			runningAvgValue.setStyle("-fx-text-fill:white;");

		} catch (IOException exception) {
			exception.printStackTrace();
			throw new RuntimeException(exception);
		}
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
		//setExpectedProductId(productModel.getExpectedProductId());
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		stopClock();
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.referenceLineSpeed = getProcessTime();
		lineSpeedValue.textProperty().set(String.format(floatFormat,this.referenceLineSpeed));
		startClock();
	}
	
//	private int getProcessTime(ProductModel productModel) {
//		int time = 0;
//		UnitDao unitDao = ServiceFactory.getService(UnitDao.class);
//		time = unitDao.getLineSpeed(productModel.getProductId(), productModel.getProcessPointId());
//		return time;
//	}
	
	
	private int getProcessTime() {
		ProcessPoint processPoint =  ClientMainFx.getInstance(). getApplicationContext().getProcessPoint();
		String lineId = processPoint.getLineId();
		String processPointId = processPoint.getProcessPointId();
		loadLineSpeedMap(processPointId);
		if (lineSpeedMap.containsKey(lineId)) {
		  return lineSpeedMap.get(lineId);
		} else if (lineSpeedMap.containsKey(DEFAULT_LINE)){
		  return lineSpeedMap.get(DEFAULT_LINE);
		} else {
		  return 0;
		}
	}
	
	private void loadLineSpeedMap(String processPointId) {
		lineSpeedMap = PropertyService.getPropertyBean(PDDAPropertyBean.class,processPointId).getLineSpeed(Integer.class);
		if (lineSpeedMap == null) {
			lineSpeedMap = new HashMap<String, Integer>();
		}
	}
	
	
	private int getCountStartSecond(String processPointId) {
		  return PropertyService.getPropertyBean(PDDAPropertyBean.class,processPointId).getCountStartSecond();
	}
}