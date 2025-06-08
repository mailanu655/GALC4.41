package com.honda.galc.client.dc.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.view.BBScaleOperationView;
import com.honda.galc.client.dc.view.OperationView;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.product.mvc.AbstractProductClientPane;
import com.honda.galc.client.product.mvc.ProductClientPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.bbscale.BBScaleSocketDevice;
import com.honda.galc.device.bbscale.Corner;
import com.honda.galc.device.bbscale.ScaleWeight;
import com.honda.galc.device.bbscale.VehicleWeight;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Vivek Bettada
 * @date Aug 04, 2014
 */
public class BBScaleProcessor extends OperationProcessor implements IOperationProcessor, DeviceListener  {
	
	private VehicleWeight curVehWeights = null;
	BaseProduct product = null;
	static volatile boolean viewInitialized = false;
	private static Timeline timer;
	private BBScaleOperationView view;
	private ConcurrentHashMap<Integer, MCOperationMeasurement> measSeqMap;
	private int count;
	private String userId;
	
	public BBScaleProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		EventBusUtil.register(this);
		getDevice().clear();
		getDevice().registerListener(this);
		DataCollectionModel model = getController().getModel();
		product = model.getProductModel().getProduct();
        ConcurrentHashMap<MCOperationRevision, OperationView> vMap = getController().getModel().getOperationViewMap();
        if(vMap != null)  {
        	 OperationView operationView = vMap.get(operation);
        	 if(operationView != null && operationView instanceof BBScaleOperationView)  {
             	setView((BBScaleOperationView)(vMap.get(operation)));
        	 }
        }
        measSeqMap = getMeasurementSequenceMap();
		updateVin(product.getProductId());
		//User id
		userId = model.getProductModel().getApplicationContext().getUserId();
	}
	
	private ConcurrentHashMap<Integer, MCOperationMeasurement> getMeasurementSequenceMap() {
		ConcurrentHashMap<Integer, MCOperationMeasurement> measSeqMap = new ConcurrentHashMap<Integer, MCOperationMeasurement>();
		//Fetch parts
		ConcurrentHashMap<String, List<MCOperationPartRevision>> partsMap = getController().getModel().getPartsMap();
		if(partsMap!=null && !partsMap.isEmpty()) {
			//Fetching parts for current operation
			String operationName = getOperation().getId().getOperationName();
			List<MCOperationPartRevision> parts = partsMap.get(operationName);
			if(parts!=null && !parts.isEmpty()) {
				if(parts.size() > 1) {
					Logger.getLogger().debug("More than 1 parts are associated for operation: " + operationName);
				}
				//Getting first part for BB Scale
				MCOperationPartRevision part = parts.iterator().next();
				//Getting Measurements
				List<MCOperationMeasurement> measurements = part.getMeasurements();
				
				if(measurements!=null) {
					Iterator<MCOperationMeasurement> measItr = measurements.iterator();
					while(measItr.hasNext()) {
						MCOperationMeasurement meas = measItr.next();
						measSeqMap.put(meas.getId().getMeasurementSeqNum(), meas);
					}
				}
			}
			else {
				Logger.getLogger().debug("No parts are associated for operation: " + operationName);
			}
		}
		else {
			Logger.getLogger().debug("No parts are associated for this product");
		}
		return measSeqMap;
	}

	public void setView(BBScaleOperationView view) {
		this.view = view;
	}
	
	public IDeviceData received(String clientId, final IDeviceData deviceData) {
		Platform.runLater(new Runnable() {
            public void run() { 
            	processReceivedData(deviceData);
            };
        });
		return null;
	}
	
	private void processReceivedData( IDeviceData deviceData) {
		boolean retVal = false;
		if (deviceData instanceof VehicleWeight) {
			curVehWeights = (VehicleWeight) deviceData;
			if(isViewInitialized()){
			    displayLimits();
				displayWeights(curVehWeights);
				setColors();
			}
			retVal = saveBuildResults(curVehWeights);
			setCarImage(retVal);
			if(retVal)  {
				startClock();
			}
			if(getDevice().isAutoCloseSocket())  {
				getDevice().closeConnection();
			}
		}
	}
	
	public void displayWeights(VehicleWeight w) {
		double lfrt = w.getScaleWeights().get(0).getWeight();
		double lrr = w.getScaleWeights().get(1).getWeight();
		double rfrt = w.getScaleWeights().get(2).getWeight();
		double rrr = w.getScaleWeights().get(3).getWeight();
		
		double lcr = w.getScaleWeights().get(4).getWeight();
		double rcr = w.getScaleWeights().get(5).getWeight();
		double total = w.getScaleWeights().get(6).getWeight();
		double crDiff = rcr - lcr;
		
		setWeightText("LFrt_val", lfrt);
		setWeightText("LRr_val", lrr);
		setWeightText("RFrt_val", rfrt);
		setWeightText("RRr_val", rrr);
		setWeightText("LCr_val", lcr);
		setWeightText("RCr_val", rcr);
		setWeightText("Tot_val", total);
		setWeightText("Cr_diff", crDiff);
	}
	
	private void setCarImage(boolean isGood)  {
		String carImagePath = null;
		if(isGood)  {
			carImagePath = "resource/images/bbscales/green_nsx.png";
		}
		else  {
			carImagePath = "resource/images/bbscales/red_nsx.png";
		}
    	Image nextCarImage = new Image(carImagePath);
    	ImageView iView = view.getImageViewField("cc_car_img");
    	iView.setImage(nextCarImage);
	}
	
	private void setWeightText(String textId, double dWt)  {
		String strWt = String.format("%.1f", dWt);
		view.getTextField(textId).setText(strWt);
	}
	
	private void setColors() {
		setValueTextFieldColor("LFrt_val","LFrt_min","LFrt_max");
		setValueTextFieldColor("LRr_val","LRr_min","LRr_max");
		setValueTextFieldColor("RFrt_val","RFrt_min","RFrt_max");
		setValueTextFieldColor("RRr_val","RRr_min","RRr_max");
		setLabelColor("LCr_val","lbl_lcr_min","lbl_lcr_max");
		setLabelColor("RCr_val","lbl_rcr_min","lbl_rcr_max");
		setLabelColor("Tot_val","lbl_tot_min","lbl_tot_max");
		setLabelColor("Cr_diff","lbl_crdiff_min","lbl_crdiff_max");
	}
	
	public void resetValues() {
		resetTextField("LFrt_val","LFrt_min","LFrt_max");
		resetTextField("LRr_val","LRr_min","LRr_max");
		resetTextField("RFrt_val","RFrt_min","RFrt_max");
		resetTextField("RRr_val","RRr_min","RRr_max");
		resetAggregateField("LCr_val","lbl_lcr_min","lbl_lcr_max");
		resetAggregateField("RCr_val","lbl_rcr_min","lbl_rcr_max");
		resetAggregateField("Cr_diff","lbl_crdiff_min","lbl_crdiff_max");
		resetAggregateField("Tot_val","lbl_tot_min","lbl_tot_max");
        getController().displayMessage("Last_VIN: " + product.getId());
        Parent root = view.getScene().getRoot();
        ProductClientPane pPane = (ProductClientPane)(root.lookup("#ProductPanel"));
    	pPane.setStatusMessage("Last_VIN: " + product.getId());
    	view.getTextField("vin").clear();
	}
	
	private void setValueTextFieldColor(String valueId, String minId, String maxId) {
		TextField textNode = view.getTextField(valueId);
		TextField minNode = view.getTextField(minId);
		TextField maxNode = view.getTextField(maxId);
		
		double value = Double.parseDouble(textNode.getText());
		boolean isMaxOk = (value <= Double.parseDouble(maxNode.getText()));
		boolean isMinOk = (value >= Double.parseDouble(minNode.getText()));
		boolean isOk = isMinOk && isMaxOk;
		String bgcolor = isOk ? "white" : "red";
		String txcolor = isOk ? "green" : "black";
		String minColor = isMinOk ? "black" : "red";
		String maxColor = isMaxOk ? "black" : "red";
		textNode.setStyle("-fx-background-color: \"" + bgcolor + "\"; -fx-text-fill: \"" + txcolor + "\"; -fx-font-weight: bold; -fx-font-size: 25px;");
		minNode.setStyle("-fx-text-fill: \"" + minColor + "\"");
		maxNode.setStyle("-fx-text-fill: \"" + maxColor + "\"");
		
	}

	private void setLabelColor(String valueId, String minId, String maxId) {
		TextField textNode = view.getTextField(valueId);
		Label minNode = view.getLabelField(minId);
		Label maxNode = view.getLabelField(maxId);
		
		double value = Double.parseDouble(textNode.getText());
		boolean isMaxOk = (value <= Double.parseDouble(maxNode.getText()));
		boolean isMinOk = (value >= Double.parseDouble(minNode.getText()));
		boolean isOk = isMinOk && isMaxOk;
		String bgcolor = isOk ? "white" : "red";
		String txcolor = isOk ? "green" : "black";
		String minColor = isMinOk ? "black" : "red";
		String maxColor = isMaxOk ? "black" : "red";
		
		textNode.setStyle("-fx-background-color: \"" + bgcolor + "\"; -fx-text-fill: \"" + txcolor + "\"; -fx-font-weight: bold; -fx-font-size: 25px;");

		minNode.setStyle("-fx-text-fill: \"" + minColor + "\"");
		maxNode.setStyle("-fx-text-fill: \"" + maxColor + "\"");
		
	}

	public void clearListeners() {
		getDevice().clear();
	}
	
	private void resetTextField(String valueId, String minId, String maxId) {
		TextField textNode = view.getTextField(valueId);
		view.getTextField(minId).clear();
		view.getTextField(maxId).clear();
		textNode.clear();
		textNode.setStyle("-fx-background-color: \"white\"");		
	}

	private void resetAggregateField(String valueId, String minId, String maxId) {
		TextField textNode = view.getTextField(valueId);
		view.getLabelField(minId).setText("min");
		view.getLabelField(maxId).setText("max");
		textNode.clear();
		textNode.setStyle("-fx-background-color: \"white\"");		
	}

	public void displayLimits() {
		view.getTextField("LFrt_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_LFRT)));
		view.getTextField("LFrt_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_LFRT)));
		view.getTextField("LRr_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_LRR)));
		view.getTextField("LRr_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_LRR)));
		view.getTextField("RFrt_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_RFRT)));
		view.getTextField("RFrt_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_RFRT)));
		view.getTextField("RRr_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_RRR)));
		view.getTextField("RRr_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_RRR)));
		view.getLabelField("lbl_lcr_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_LCR)));
		view.getLabelField("lbl_lcr_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_LCR)));
		view.getLabelField("lbl_rcr_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_RCR)));
		view.getLabelField("lbl_rcr_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_RCR)));
		view.getLabelField("lbl_tot_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_TOTAL)));
		view.getLabelField("lbl_tot_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_TOTAL)));
		view.getLabelField("lbl_crdiff_max").setText(Double.toString(getMaxLimit(Corner.BBSCALES_CRDIFF)));
		view.getLabelField("lbl_crdiff_min").setText(Double.toString(getMinLimit(Corner.BBSCALES_CRDIFF)));
	}
	
	private Double getMaxLimit(Corner corner) {
		//Get measurement from corner
		MCOperationMeasurement meas = getOpMeasurementForCorner(corner);
		if(meas!=null) { return meas.getMaxLimit(); }
		return 0.0;		
	}
	
	private Double getMinLimit(Corner corner) {
		//Get measurement from corner
		MCOperationMeasurement meas = getOpMeasurementForCorner(corner);
		if(meas!=null) { return meas.getMinLimit(); }
		return 0.0;		
	}
	
	private MCOperationMeasurement getOpMeasurementForCorner(Corner corner) {
		//Get measurement sequence number from scale weight
		int measSeq = corner.getMeasurementSequence();
		//Get measurement from map
		MCOperationMeasurement meas = measSeqMap.get(measSeq);
		return meas;
	}

	public void updateVin(String vin) {
		if(isViewInitialized() && view != null)  {
			view.getTextField("vin").setText(vin);
		}
	}
	
	public void updateVin() {
		if(isViewInitialized() && product != null)  {
			view.getTextField("vin").setText(product.getProductId());			
		}
	}
	
	private boolean checkWeight(ScaleWeight weight) {
		double min = getMinLimit(weight.getCorner());
		double max = getMaxLimit(weight.getCorner());
		return weight.getWeight() >= min && weight.getWeight() <= max;
	}
	public void stopClock() {
		if(timer != null){
			timer.stop();
			
		}
	}
	
	public void startClock() {
		stopClock();
		count = 60;
	    timer =  new Timeline(new KeyFrame( Duration.seconds(5),new EventHandler<ActionEvent>() {
		  public void handle(ActionEvent event) {
				count -=5;
				getController().displayMessage("screen will be cleared in " + count + " seconds");
		  };
		}));
	    
	    timer.setOnFinished(new EventHandler<ActionEvent>(){
	    	public void handle(ActionEvent event) {
	        	resetValues();
	            completeOperation();
	            clearListeners();
	        	getController().cancel();
	    	}	
	    });
		timer.setCycleCount(12);
		timer.play();
	}
	
	/**
	 * Check scale weights within the defined range.
	 * The preservation of the actual results is done.
	 * @param torque
	 * @return
	 */
	public void checkWeight(Measurement thisMeas, MCOperationMeasurement mSpec, Corner whichCorner) {
		
		String msgString = null;
		boolean status = true;

		//check Status
		if (thisMeas.getMeasurementValueStatus() != MeasurementStatus.OK) {
			msgString = (whichCorner.toString() + ":" + thisMeas.getMeasurementValueStatus());
			status = false;
		} else if ((thisMeas.getMeasurementValue()  < mSpec.getMinLimit()) || 
				(mSpec.getMaxLimit() < thisMeas.getMeasurementValue())) 
		{
			msgString = (whichCorner.toString() + ":" + thisMeas.getMeasurementValue() + 
					" is not within range (" + mSpec.getMinLimit() + "," + mSpec.getMaxLimit() + ")");
			status = false;
		} 


		if(!status) handleException(msgString);
	}
	
	public int getMeasurementIndex() {
		return getController().getModel().getCurrentMeasurementIndex();
	}
	
	protected void handleException(String info) {
		throw new TaskException(info, this.getClass().getSimpleName());
	}
	
	public BBScaleSocketDevice getDevice() {
		BBScaleSocketDevice bbscalesDevice = (BBScaleSocketDevice) DeviceManager
				.getInstance().getDevice("YV1D002");
		return bbscalesDevice;
	}

	public static boolean isViewInitialized() {
		return viewInitialized;
	}

	public static void setViewInitialized(boolean isInit) {
		viewInitialized = isInit;
	}
	
	private boolean saveBuildResults(VehicleWeight vehicleWeight) {
		boolean status = true;
		MCOperationRevision mcOp = getOperation();
		InstalledPart installedPart = new InstalledPart(product.getProductId(), getOperation().getId().getOperationName());
		installedPart.setAssociateNo(userId);
		int partRev = 0;
		String partId = "";
		if(mcOp.getManufacturingPart() != null)  {
			partRev = mcOp.getManufacturingPart().getId().getPartRevision();
			partId = mcOp.getManufacturingPart().getId().getPartId();
		}
		installedPart.setPartRevision(partRev);
		installedPart.setPartId(partId);
		installedPart.setProcessPointId(getController().getProcessPointId());
		installedPart.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setOperationRevision(mcOp.getId().getOperationRevision());
		//Creating list of measurements
		for(ScaleWeight weight : vehicleWeight.getScaleWeights()) {
			boolean isOk = checkWeight(weight);
			//Creating measurement record
			Measurement measurement = new Measurement(installedPart.getProductId(),
					installedPart.getPartName(), weight.getCorner().getMeasurementSequence());
			measurement.setPartRevision(partRev);
			measurement.setMeasurementValue(weight.getWeight());
			measurement.setMeasurementStatus(isOk ? MeasurementStatus.OK : MeasurementStatus.NG);
			measurement.setActualTimestamp(installedPart.getActualTimestamp());
			measurement.setPartId(partId);
			//Adding measurement in installed part
			installedPart.getMeasurements().add(measurement);
			if(!isOk) status = false;
		}
		if(status)  {
			installedPart.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
			getController().getModel().getCurrentProductBuildList().add(installedPart);
		}
		getController().getPersistenceManager().saveInstalledPart(installedPart);
		
		return status;
	}
}
