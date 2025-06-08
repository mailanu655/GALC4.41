package com.honda.galc.client.glassload;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;
import org.springframework.util.StringUtils;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.KeyValue;

public class GlassLoadController implements DataContainerListener,IProductPassedNotification{
	
	private GlassLoadModel model;
	private GlassLoadView view;
	
	private static String AUTO_FRONT_GLASS_TYPE = "AUTO_FRONT_GLASS_TYPE";
	private static String AUTO_REAR_GLASS_TYPE = "AUTO_REAR_GLASS_TYPE";
	private static String AUTO_FRONT_CONFIRMED = "AUTO_FRONT_CONFIRMED";
	private static String AUTO_REAR_CONFIRMED = "AUTO_REAR_CONFIRMED";
	private static String AUTO_FRONT_READY = "AUTO_FRONT_READY";
	private static String AUTO_REAR_READY = "AUTO_REAR_READY";
	private static String AUTO_GLASS_HEARTBEAT = "AUTO_GLASS_HEARTBEAT";
	private static String AUTO_MODE = "AUTO_MODE";
	private static String AUTO_MODE_READ = "AUTO_MODE_READ";
		
	private static String PPA_GLASS_LOADED = "PPA_GLASS_LOADED";
	private static String PPA_GLASS_TYPE_CONFIRMED = "PPA_GLASS_TYPE_CONFIRMED";
	private static String PPA_GLASS_TYPE = "PPA_GLASS_TYPE";
	private static String PPA_GLASS_ALARM = "PPA_GLASS_ALARM";
	private static String PPA_GLASS_HEARTBEAT = "PPA_GLASS_HEARTBEAT";
	private static String PPA_MODE = "PPA_MODE";
	private static String PPA_MODE_READ = "PPA_MODE_READ";
	

	private boolean prevRearTypeConfirmed = false;
	private boolean curRearTypeConfirmed = false;
	private boolean curFrontTypeConfirmed = false;
	private boolean nextFrontTypeConfirmed = false;
	
	private boolean ppaRearTypeConfirmed = false;
	
	private KeyValue<String,Color> currentColorMap = null;
	private Color otherColor = null;
	
	private Timer timer = null;
	private boolean heartbeat;
	
	private volatile static DataContainer returnDC = null;
	
	public PlcMode plcMode = PlcMode.AUTO;
		
	public GlassLoadController(GlassLoadModel model,GlassLoadView view) {
		this.model = model;
		this.view = view;
		registerEiDeviceListener();
				
		AnnotationProcessor.process(this);
		
		setPlcMode();
		
		if(plcMode.equals(PlcMode.AUTO) || plcMode.equals(PlcMode.PPA)) {
			startPLCHeartbeat();
		}

	}

	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			eiDevice.registerDataContainerListener(this);
		}
	}

	public GlassLoadModel getModel() {
		return model;
	}



	public void setModel(GlassLoadModel model) {
		this.model = model;
	}



	public GlassLoadView getView() {
		return view;
	}



	public void setView(GlassLoadView view) {
		this.view = view;
	}
	
	public DataContainer received(DataContainer dc) {
		
		getLogger().info("Received dc - " + dc);
		returnDC = null;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run(){
					basicReceived(dc);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(returnDC != null) {
			getLogger().info("reply dc - " + returnDC);
		}
		return returnDC;
	}

	public void basicReceived(DataContainer dc) {
		
		if(isData(AUTO_MODE,dc)) {
			String value = dc.getString(AUTO_MODE);
			boolean aFlag = value != null && (value.equalsIgnoreCase("TRUE") || value.equals("1"));
            if(aFlag) {
				receiveAutoMode();
				setMessage("Switched to " + plcMode.getModeText());
            }else {
            	switchPlcMode();
            }
            
            return;
		}
		
		if(isPPAMode(dc)) {
			String value = dc.getString(PPA_MODE);
			boolean aFlag = value != null && (value.equalsIgnoreCase("TRUE") || value.equals("1"));
			if(aFlag) receivePPAMode();
			else receiveManualMode();
			
			setMessage("Switched to " + plcMode.getModeText());
			return;
		}
		
		if(plcMode.equals(PlcMode.AUTO)) {
			if(isSignal(AUTO_FRONT_CONFIRMED,dc)) {
				receiveAutoFrontConfirmed();
			}else if(isSignal(AUTO_FRONT_READY,dc)) {				
				returnDC = receiveAutoFrontReady();
			}else if(isSignal(AUTO_REAR_CONFIRMED,dc)) {
				receiveAutoRearConfirmed();
			}else if(isSignal(AUTO_REAR_READY,dc)) {
				returnDC = receiveAutoRearReady();
			}
		}else if(plcMode.equals(PlcMode.PPA)){
			if(isData(PPA_GLASS_TYPE,dc)) {
				returnDC = receivePPAGlassType(dc.getString(PPA_GLASS_TYPE));
			}else if(isPPAGlassLoaded(dc)) {
				receivePPAGlassLoaded(dc.getString(PPA_GLASS_LOADED));
			}
		}
		
	}
	
	public boolean isSignal(String tagName, DataContainer dc) {
		if(!dc.getClientID().equals(tagName)) return false;
		String value = dc.getString(tagName);
		return value != null && (value.equalsIgnoreCase("TRUE") || value.equals("1"));
	}
	
	public boolean isData(String tagName, DataContainer dc) {
		if(!dc.getClientID().equals(tagName)) return false;
		else return true;

	}
	
	public boolean isPPAMode(DataContainer dc) {
		if(!dc.getClientID().equals(PPA_MODE)) return false;
		if(plcMode.equals(PlcMode.AUTO)) return true;
		String value = dc.getString(PPA_MODE);
		boolean aFlag = value != null && (value.equalsIgnoreCase("TRUE") || value.equals("1"));
		if(plcMode.equals(PlcMode.PPA) &&  !aFlag) return true;
		if(plcMode.equals(PlcMode.MANUAL) &&  aFlag) return true;
		return false;
	}
	
	public boolean isPPAGlassLoaded(DataContainer dc) {
		if(!plcMode.equals(PlcMode.PPA)) return false;
		if(!dc.getClientID().equals(PPA_MODE)) return false;
		String value = dc.getString(PPA_MODE);
		boolean aFlag = value != null && (value.equalsIgnoreCase("TRUE") || value.equals("1"));
		return aFlag;
	}
	
	private void receiveAutoMode() {
		if(plcMode.equals(PlcMode.AUTO)) return;
		
		plcMode = PlcMode.AUTO;
		
		view.setProductPanel();
		view.loadProductPanel();
		view.setMode();
		
		startPLCHeartbeat();
	}
	
	private void receivePPAMode() {
		if(plcMode.equals(PlcMode.PPA)) return;
		
		plcMode = PlcMode.PPA;
		
		ppaRearTypeConfirmed = false;
		
		view.setProductPanel();
		view.loadProductPanel();

		view.setMode();
		
		startPLCHeartbeat();
	}
	
	private void receiveManualMode() {
		if(plcMode.equals(PlcMode.MANUAL)) return;
		
		plcMode = PlcMode.MANUAL;
		
		view.setProductPanel();
		
		view.loadProductPanel();
		view.setMode();
	}
		
	private void receiveAutoFrontConfirmed() {
		if(!curFrontTypeConfirmed) {
			view.frontGlassTypeTextFields[1].getComponent().setBackground(Color.green);
			curFrontTypeConfirmed = true;
		}else {
			view.frontGlassTypeTextFields[2].getComponent().setBackground(Color.green);
			nextFrontTypeConfirmed = true;
		}
		
		if(curFrontTypeConfirmed && prevRearTypeConfirmed) {
			
			prevRearTypeConfirmed = false;
			curFrontTypeConfirmed = false;

			completeCurrentVin(true);
		}

	}
	
	private DataContainer receiveAutoFrontReady() {
		
		String glassType = view.frontGlassTypeTextFields[1].getComponent().getText();
		
		if(StringUtils.isEmpty(glassType)) {		
			// current vin's front glassType			
			return processFrontGlassType(1);
		}else {
			// next vin's front glassType
			return processFrontGlassType(2);
		}

	}
	
	private DataContainer processRearGlassType(int index) {
		String vin = view.vinTextFields[index].getComponent().getText();
		String productSpecCode = view.mtocTextFields[index].getComponent().getText();
		String glassType = model.getRearGlassType(productSpecCode);
		view.rearGlassTypeTextFields[index].getComponent().setText(glassType);
		view.rearGlassTypeTextFields[index].getComponent().setBackground(Color.yellow);
		
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(AUTO_REAR_READY + "_REPLY");
		dc.put(AUTO_REAR_GLASS_TYPE, glassType);
		
		String str1 = index == 0 ? "Previous VIN " : "Current VIN ";
		setMessage("Sent Rear Glass Type " + glassType +  " for " + str1 + vin  + " mtoc " + productSpecCode);
		
		return dc;
	}

	private DataContainer processFrontGlassType(int index) {
		String vin = view.vinTextFields[index].getComponent().getText();
		String productSpecCode = view.mtocTextFields[index].getComponent().getText();
		String glassType = model.getFrontGlassType(productSpecCode);
	
		view.frontGlassTypeTextFields[index].getComponent().setText(glassType);
		view.frontGlassTypeTextFields[index].getComponent().setBackground(Color.yellow);
		
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(AUTO_FRONT_READY + "_REPLY");
		dc.put(AUTO_FRONT_GLASS_TYPE, glassType);
		
		String str1 = index == 0 ? "Current VIN " : "Next VIN ";
		setMessage("Sent Front Glass Type " + glassType +  " for " + str1 + vin  + " mtoc " + productSpecCode);

		
		return dc;
	}
	
	private void receiveAutoRearConfirmed() {
		if(!prevRearTypeConfirmed) {
			view.rearGlassTypeTextFields[0].getComponent().setBackground(Color.green);
			prevRearTypeConfirmed = true;
			
			String vin = view.vinTextFields[0].getComponent().getText();
			setMessage("Rear Glass Type of previous VIN " + vin + " is confirmed");

		}else {
			view.rearGlassTypeTextFields[1].getComponent().setBackground(Color.green);
			curRearTypeConfirmed = true;
			
			String vin = view.vinTextFields[1].getComponent().getText();
			setMessage("Rear Glass Type of current VIN " + vin + " is confirmed");
		}
		
		if(curFrontTypeConfirmed && prevRearTypeConfirmed) {
			
			prevRearTypeConfirmed = false;
			curFrontTypeConfirmed = false;
			
			completeCurrentVin(true);
		}
	}
	
	private DataContainer receiveAutoRearReady() {
		String glassType = view.rearGlassTypeTextFields[0].getComponent().getText();
		if(StringUtils.isEmpty(glassType)) {		
			// previous vin's rear glassType
			return processRearGlassType(0);
		}else {
			// current vin's rear glassType
			return processRearGlassType(1);
		}
	}
	
	private DataContainer receivePPAGlassType(String glassType) {
		
		String confirmed = "0";
		
		if(!this.ppaRearTypeConfirmed) {
			//rear glass of previous vehicle
			String productSpecCode = view.mtocTextFields[0].getComponent().getText();
			String glassType1 = model.getRearGlassType(productSpecCode);
			
			view.rearGlassTypeTextFields[0].getComponent().setText(glassType);
			
			if(glassType1.equalsIgnoreCase(glassType)) {
				confirmed = "1";
				view.rearGlassTypeTextFields[0].getComponent().setBackground(Color.green);
			}else {
				sendData(PPA_GLASS_ALARM,"1");
				view.rearGlassTypeTextFields[0].getComponent().setBackground(Color.red);	
				confirmed = "0";
			}
		}else {
			// front glass of current vehicle;
			
			String productSpecCode = view.mtocTextFields[1].getComponent().getText();
			String glassType1 = model.getFrontGlassType(productSpecCode);
			
			view.frontGlassTypeTextFields[1].getComponent().setText(glassType);
			
			if(glassType1.equalsIgnoreCase(glassType)) {
				confirmed = "1";
				view.frontGlassTypeTextFields[1].getComponent().setBackground(Color.green);
			}else {
				sendData(PPA_GLASS_ALARM,"1");
				view.frontGlassTypeTextFields[1].getComponent().setBackground(Color.red);
				confirmed = "0";
			}

		}
		
		return setReply(PPA_GLASS_TYPE,PPA_GLASS_TYPE_CONFIRMED,confirmed);
		
	}
	
	private DataContainer setReply(String clientId, String tagName, String tagValue) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(clientId +  "_REPLY");
		dc.put(tagName, tagValue);
		return dc;

	}
	
	private void receivePPAGlassLoaded(String glassLoaded) {
		boolean isGlassLoaded = glassLoaded != null && (glassLoaded.equalsIgnoreCase("TRUE") || glassLoaded.equals("1"));
		
		if(isGlassLoaded) {
			
			if(!ppaRearTypeConfirmed) {
				ppaRearTypeConfirmed = true;
				view.frontGlassTypeTextFields[1].getComponent().setBackground(Color.blue);
			}else {
				completePPACurrentVin(true);
				ppaRearTypeConfirmed = false;
			}
		}
	}
	
	private DataContainer  sendData(String name,String value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(name);
		dc.put(name,value);
		
		getLogger().info("Sending data - client_id: " + name + " value: " + value );
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		try {
			return eiDevice.syncSend(name,dc);
		}catch(Exception ex) {
			getLogger().error(ex,"erroe sending data to device");
			displayErrorMessage("error sending data to device " + ex.getMessage());			
		}
		
		return null;
	}
	
	private DataContainer readData(String name) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(name);
		DataContainerUtil.setEquipmentRead(dc);
    	
		getLogger().info("Reading data - client_id: " + name);

		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		try {
			return eiDevice.syncSend(name,dc);
		}catch(Exception ex) {
			getLogger().error(ex,"erroe reading data from device " + name);
			displayErrorMessage("error reading data from device " + ex.getMessage());			
		}
		
		return null;
	}
	
	private void completeCurrentVin(boolean isSaveBuildResult) {
		
		String vin = view.vinTextFields[1].getComponent().getText();
		
		assignVinToCarrier(vin);
		
		model.updateExpectedProduct(vin);
		
		if(isSaveBuildResult) saveBuildResults();
		
		String  currentRearGlassType= view.rearGlassTypeTextFields[1].getComponent().getText();
		String nextFrontGlassType = view.frontGlassTypeTextFields[2].getComponent().getText();

		view.loadProductPanel();
		view.completeCurrentVin(curRearTypeConfirmed, nextFrontTypeConfirmed,currentRearGlassType, nextFrontGlassType);

		view.refreshVinListPanel();
		
		setMessage("Completed current VIN " + vin);

		
		this.prevRearTypeConfirmed = curRearTypeConfirmed;
		this.curFrontTypeConfirmed = nextFrontTypeConfirmed;
		this.nextFrontTypeConfirmed = false;
		this.curRearTypeConfirmed = false;

	}
	
	private void saveBuildResults() {
		String previousVin = view.vinTextFields[0].getComponent().getText();
		String rearGlassType = view.rearGlassTypeTextFields[0].getComponent().getText();
		String currentVin = view.vinTextFields[1].getComponent().getText();
		String frontGlassType = view.frontGlassTypeTextFields[1].getComponent().getText();
		
		LotControlRule previousRearRule = model.getLotControlRules(view.mtocTextFields[0].getComponent().getText()).get(1);
		LotControlRule currentFrontRule = model.getLotControlRules(view.mtocTextFields[1].getComponent().getText()).get(0);

		InstalledPart prevInstalledPart = createInstalledPart(previousVin, rearGlassType, previousRearRule);
		InstalledPart currentInstalledPart = createInstalledPart(currentVin, frontGlassType, currentFrontRule);
		
		List<InstalledPart> parts = new ArrayList<InstalledPart>();
		parts.add(prevInstalledPart);
		parts.add(currentInstalledPart);
		
		model.saveBuildResults(parts);
		
	}
	
	private InstalledPart createInstalledPart(String vin, String glassType, LotControlRule rule) {
		InstalledPart installedPart = (InstalledPart)ProductTypeUtil.createBuildResult(ProductType.FRAME.name(), vin, rule.getPartNameString());
		installedPart.setInstalledPartStatusId(1);
		installedPart.setPartSerialNumber(glassType);
		installedPart.setProcessPointId(model.getProcessPointId());
		installedPart.setPartId(rule.getParts().get(0).getId().getPartId());
		installedPart.setAssociateNo(view.getMainWindow().getUserId());
		return installedPart;
	}
	
	protected void skipCurrentVehicle() {
		setMessage("Skip Current VIN");
		if(plcMode.equals(PlcMode.AUTO)) {
			completeCurrentVin(false);
		}else if(plcMode.equals(PlcMode.PPA)) {
			completePPACurrentVin(false);
		}
	}
	
	private void assignVinToCarrier(String vin) {
				
		MultiValueObject<Frame> currentItem= view.getFrame(view.vinTextFields[1].getComponent().getText());
		
		int currentIndex = view.vinListTablePane.getItems().indexOf(currentItem);
		
		String seq = view.vinListTablePane.getItems().get(currentIndex -1).getKeyObject().getQuantity();
		
		GtsCarrier carrier = null; 
		if(StringUtils.isEmpty(seq)) {
			// exception
		}else {
			int intSeq = Integer.valueOf(seq);
			carrier = model.findNextGtsCarrier(intSeq);

		}
		if(carrier == null) return;
		
		String oldVin = carrier.getProductId();
		
		if(!StringUtils.isEmpty(oldVin)) {
			MultiValueObject<Frame> oldItem = view.getFrame(oldVin);
			oldItem.getKeyObject().setQuantity(null);
			oldItem.getValues().set(1, "");
		}
		
		currentItem.getKeyObject().setQuantity("" + carrier.getStatusValue());
		currentItem.getValues().set(1, carrier.getCarrierNumber());
		currentItem.getValues().set(0, true);
		
		int first = view.vinListTablePane.getItems().indexOf(currentItem) - model.getPropertyBean().getProcessedProductNumber();
		
		carrier.setProductId(currentItem.getKeyObject().getProductId());
		model.updateCarrier(carrier);
		
		if(first >= 0) view.vinListTablePane.getItems().remove(0);
		
		getLogger().info("Assigned VIN " + currentItem.getKeyObject().getProductId() + " to Carrier " + currentItem.getValue(1) + " Seq " + seq);
		view.vinListTablePane.refresh();
		
	}
	
	private void completePPACurrentVin(boolean isSaveBuildResult) {
		
		String vin = view.vinTextFields[1].getComponent().getText();
		
		assignVinToCarrier(vin);
		
		model.updateExpectedProduct(vin);
		
		if(isSaveBuildResult) saveBuildResults();


		view.loadProductPanel();

		view.refreshVinListPanel();

//		view.completePPACurrentVin();

		setMessage("Completed current VIN " + vin);
	
	}
	
	protected Color[][] prepareBackgroudColors() {
		List<MultiValueObject<Frame>> frames = view.vinListTablePane.getItems();
		int rowCount = frames.size();
		int columnCount = view.vinListTablePane.getTable().getColumnCount();
		Color[][] colors= new Color[rowCount][columnCount];
		
		Color currentColor = model.getPropertyBean().getBackgroundColor();
		Color alterColor = model.getPropertyBean().getAlternateBackgroundColor();
		
		MultiValueObject<Frame> frameItem = frames.get(0);
		if(currentColorMap == null) {
			currentColor = model.getPropertyBean().getBackgroundColor();
			alterColor = model.getPropertyBean().getAlternateBackgroundColor();
			currentColorMap = new KeyValue<String, Color>(frameItem.getKeyObject().getProductionLot(), currentColor);
			otherColor = alterColor;
		}else {
			if((boolean) frameItem.getValue(10) || frameItem.getKeyObject().getProductionLot().equals(currentColorMap.getKey())) {
				currentColor = currentColorMap.getValue();
				alterColor = otherColor;
			}else {
				currentColor = otherColor;
				alterColor = currentColorMap.getValue();
				currentColorMap = new KeyValue<String, Color>(frameItem.getKeyObject().getProductionLot(), currentColor);
				otherColor = alterColor;
			}
		}
		
		String previousLot = null;
		
		//fill
		for(int i =0; i< rowCount; i++) {
			MultiValueObject<Frame> item = frames.get(i);
			boolean isCurrentProductStraggler = (boolean) item.getValue(10);
			if(isCurrentProductStraggler) {
				for(int j = 0; j <columnCount; j++) {
					colors[i][j] = model.getPropertyBean().getStragglerHighlightBackgroundColor();
				}
			}else {
				Color thisColor = currentColor;

				if(i>=1 && !item.getKeyObject().getProductionLot().equals(previousLot)) {
					// swap colors
					currentColor = alterColor;
					alterColor = thisColor;
				}
				
				previousLot = item.getKeyObject().getProductionLot();

				
				for(int j = 0; j <columnCount; j++) {
					colors[i][j] = currentColor;
				}

			}

		}
			
		for (int i =1 ; i < rowCount ; i++) {		
			
			if( frames.get(i-1).getKeyObject().getProductId().equals(model.getExpectedProduct().getProductId())) {
				for(int j = 0; j <columnCount; j++) {
					colors[i-1][j] = model.getPropertyBean().getPrevHighlightBackgroundColor();
					colors[i][j] = model.getPropertyBean().getCurrentHighlightBackgroundColor();
				}
			}
			
		}
		
		return colors;

	}
	
	protected Color[][] prepareTextColor() {
		List<MultiValueObject<Frame>> frames = view.vinListTablePane.getItems();
		int rowCount = frames.size();
		int columnCount = view.vinListTablePane.getTable().getColumnCount();
		Color[][] colors= new Color[rowCount][columnCount];
		
		for (int i =0 ; i < rowCount ; i++) {		
			for(int j = 0; j <columnCount; j++) {
				colors[i][j] = model.getPropertyBean().getForegroundColor();
			}
		}
		
		for(int i = 0;i < rowCount;i++) {
			
			boolean isStraggler = (boolean)frames.get(i).getValue(10);
	
			if(isStraggler) {
				for(int j = 0; j <columnCount; j++) {
					colors[i][j] = model.getPropertyBean().getStragglerHighlightForegroundColor();
				}
			}
		}
		
		for(int i = 2; i< rowCount; i++) {
			if(i>=2 && frames.get(i-2).getKeyObject().getProductId().equals(model.getExpectedProduct().getProductId())) {
				for(int j = 0; j <columnCount; j++) {
					colors[i-2][j] = model.getPropertyBean().getPrevHighlightForegroundColor();
					colors[i-1][j] = model.getPropertyBean().getCurrentHighlightForegroundColor();
				}
			}
		}
				
		return colors;
	}
	
	/**
	 * notification about last passed product at a process Point
	 * @param event
	 * @param request
	 */
	@EventTopicSubscriber(topic="IProductPassedNotification")
	public void onProductPassedEvent(String event, Request request) {
		try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	
	@Override
public void execute(String processPointId, String productId) {
		
		if(!processPointId.equalsIgnoreCase(model.getAFOnProcessPointId())) return;
		
		getLogger().info("Received AFON VIN " + productId);
		
		MultiValueObject<Frame> item = model.createFrameItem(productId);
		
		if(item == null) return;
		
		List<MultiValueObject<Frame>> items = view.vinListTablePane.getItems();
		
		if(!contains(items, item)) {
			items.add(item);
	
			view.vinListTablePane.reloadData(items);
			view.refreshVinListPanel();
			
		}
		
	}
	
	private boolean contains(List<MultiValueObject<Frame>> items, MultiValueObject<Frame> frameItem) {
		for(MultiValueObject<Frame> item :items) {
			if(item.getKeyObject().equals(frameItem.getKeyObject())) return true;
		}
		return false;
	}
	
	private void startPLCHeartbeat() {

		stopHeartbeat();
		
		TimerTask timerTask = new TimerTask() {
			public void run() {
				sendHeartbeat();
			}
		};

		this.timer = new Timer();
		int time = model.getPropertyBean().getHeartbeatInterval() * 1000;
		if(time < 1000) time = 1000;
		timer.scheduleAtFixedRate(timerTask, time, time);

		getLogger().info("start to heartbeat " + time / 1000 + "seconds");
	}
	
	private void stopHeartbeat() {
		if(this.timer != null) {
			this.timer.cancel();
			this.timer.purge();
			this.timer = null;
		}
	}
	
	private void sendHeartbeat() {
		String heartbeatName = null; 
		if(plcMode.equals(PlcMode.AUTO)) {
			heartbeatName = AUTO_GLASS_HEARTBEAT;
		}else if(plcMode.equals(PlcMode.PPA)) {
			heartbeatName = PPA_GLASS_HEARTBEAT;			
		}
		this.heartbeat = !heartbeat;
		if(heartbeatName != null) {
			DataContainer dc = sendData(heartbeatName, heartbeat? "1" : "0");
			if(dc == null) switchPlcMode();
		} else {
			stopHeartbeat();
		}
	}
	
	private void switchPlcMode() {
		stopHeartbeat();
		DataContainer dc;
		
		if(plcMode.equals(PlcMode.AUTO)) {
			dc = readData(PPA_MODE_READ);
			
			if(dc != null && (dc.getString(PPA_MODE).equals("1") || dc.getString(PPA_MODE).equals("1.0") || dc.getString(PPA_MODE).equalsIgnoreCase("TRUE"))) {
				receivePPAMode();
			}else {
				receiveManualMode();
			}
		}else {
			receiveManualMode();
		}
		
	}
	
	private void setPlcMode() {
		DataContainer dc;
		
		dc = readData(AUTO_MODE_READ);
		if(dc != null && (dc.getString(AUTO_MODE).equals("1.0") || dc.getString(AUTO_MODE).equals("1") || dc.getString(AUTO_MODE).equalsIgnoreCase("TRUE"))) {
			plcMode = PlcMode.AUTO;
			return;
		}
		
		dc = readData(PPA_MODE_READ);
		if(dc != null && (dc.getString(PPA_MODE).equals("1.0") || dc.getString(PPA_MODE).equals("1") || dc.getString(PPA_MODE).equalsIgnoreCase("TRUE"))) {
			plcMode = PlcMode.PPA;
			return;
		}
		
		plcMode = PlcMode.MANUAL;
		
	}
	
	public enum PlcMode {
		
		AUTO(0,"AUTO MODE"),
		PPA(1,"PPA MODE"),
		MANUAL(2,"MANUAL MODE");
		
		private int mode;
		private String modeText;
		PlcMode(int mode, String modeText) {
			
			this.mode = mode;
			this.modeText = modeText;			
		}
		public int getMode() {
			return mode;
		}
		public void setMode(int mode) {
			this.mode = mode;
		}
		public String getModeText() {
			return modeText;
		}
		public void setModeText(String modeText) {
			this.modeText = modeText;
		}
		
		
	}
	
	private void displayErrorMessage(String message) {
		view.getMainWindow().setErrorMessage(message);
	}
	
	private void setErrorMessage(String message) {
		view.getMainWindow().setErrorMessage(message);
		getLogger().error(message);
	}
	
	protected void setMessage(String message) {
		view.getMainWindow().setMessage(message);
		getLogger().info(message);
	}
	
	private void clearMessage() {
		view.getMainWindow().clearMessage();
	}
	
	public Logger getLogger() {
		return view.getLogger();
	}

}
