package com.honda.galc.client.schedule;


import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.CarrierAttributeDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CarrierId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.CarrierAttribute;
import com.honda.galc.entity.product.CarrierAttributeId;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.on.ProductOnService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.StringUtil;

public class CarrierProductOnController extends ScheduleClientControllerSeq implements DeviceListener {

	private MbpnProductDao mbpnProductDao;
	
	private ProductionLotMbpnSequenceDao productionLotMbpnSequenceDao;
	private CarrierAttributeDao carrierAttributeDao;
	private BroadcastService _broadcastService;
	private ProductOnService _productOnService;
	private TrackingService trackingService;
	private List<ProductionLotMbpnSequence> productionLotMbpnSequences=new ArrayList<ProductionLotMbpnSequence>();
	private ProcessPoint processPoint;
	private List<String> scannedMbpnsForLot = new ArrayList<String>();
	private List<String> expectedMbpnsForLot = new ArrayList<String>();
	private List<MbpnProduct> scannedProducts = new ArrayList<MbpnProduct>();
	private KeyListener l;
	private ClientAudioManager audioManager;
	
	
	
	public CarrierProductOnController(ScheduleMainPanel panel) {
		super(panel);
		registerDeviceData();	
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class,scheduleMainPanel.getApplicationId()));
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if (deviceData instanceof CarrierId) {
			String  carrierId = ((CarrierId)deviceData).getCarrierId();
			if(isCarrierNumberValid(carrierId)) {
				setCarrierNumberOK();
				scheduleMainPanel.getCurrentLotPanel().getCarrierField().getComponent()
				.setText(removeLeadingZeros(carrierId));
			}
		}
		return null;
	}
	
	public void registerDeviceData() {
		IDevice eiDevice = DeviceManager.getInstance().getDevice(EiDevice.NAME);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(this, getProcessData());
		}
	}
	
	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new CarrierId());
		
		return list;
	}

	public void onEvent(SchedulingEvent event) {

		switch (event.getEventType()) {
		case PROCESS_PRODUCT:
			scheduleMainPanel.getMainWindow().setMessage("");
			processProductOn(event);
			break;
		case CURRENT_ORDER_CHANGED:
			reset();
			addCarrierKeyListener();
			break;
		default:
		}

		super.onEvent(event);
	}

	private void processProductOn(SchedulingEvent event) {

		String productId = StringUtils.trimToEmpty((String) event.getTargetObject());
		processProductOn(StringUtils.upperCase(productId));

	}

	private void processProductOn(String productId) {
		
		//if empty broadcast and populate carrier data
		if(StringUtils.startsWith(productId, getEmptyCarrierString())) {
			handleEmptyCarrier();
		}else {
			validateProduct(productId);
				
			if (scannedMbpnsForLot.size() >= getExpectedMbpns().size()) {
				DataContainer dc = new DefaultDataContainer();
				dc.put(TagNames.PRODUCT_ID.name(), productId);
								
				getLogger().info("updating  Mbpn Products Data ");
				saveAndTrackProducts();
				
				getLogger().info("invoking Product On Service ");
				getProductOnService().execute(prepareData(dc));
				
				getLogger().info("updating  CarrierAttribute Data ");
				updateCarrierAttributeData();
				
				getLogger().info("invoking Broadcast Service ");
				invokeBroadcast();
				getLogger().info("refresh Screen ");		
				refreshLots();
				scheduleMainPanel.getCurrentLotPanel().carrierSnTextSetFocus();
				
			}else {
				if(StringUtils.isEmpty(getCarrierId().trim()) ) {
					scheduleMainPanel.getCurrentLotPanel().carrierSnTextSetFocus();
				}else {
					scheduleMainPanel.getCurrentLotPanel().partSnTextSetFocus(scheduleMainPanel.getCurrentLotPanel().getPartSn().get(scannedMbpnsForLot.size()).getProductIdTextField());
				}
			}
		}
	}

	
	private void handleEmptyCarrier() {
		try {
			if(StringUtils.isEmpty(getCarrierId())) {
				throw new TaskException("Please enter Carrier Id before scanning Parts", "PRODUCT");
			}
			
			getLogger().info("updating  CarrierAttribute Data for Empty Carrier");
			String trackingArea = getProcessPoint(scheduleMainPanel.getProcessPointId()).getLineId();
			String carrierNumber = getCarrierId();
			
			removeCarrierData(trackingArea,carrierNumber);
			
			saveCarrierAttribute(trackingArea,carrierNumber, DataContainerTag.MODEL_CODE, getEmptyCarrierModelCode());
			saveCarrierAttribute(trackingArea,carrierNumber, DataContainerTag.COLOR_CODE, getEmptyCarrierColorCode());
			saveCarrierAttribute(trackingArea,carrierNumber,DataContainerTag.COMBINATION_CODE, getEmptyCarrierCombinationCode());
			saveCarrierAttribute(trackingArea,carrierNumber,"PART1", getEmptyCarrierString());
			
			getLogger().info("invoking Broadcast Service for Empty Carrier ");
			DataContainer dataContainer = new DefaultDataContainer();
			dataContainer.put(DataContainerTag.UNIT_RELEASE, 1);
			dataContainer.put(DataContainerTag.PART_INFO_LIST, getEmptyCarrierString());
			dataContainer.put(DataContainerTag.CARRIER_ID, getCarrierId());
			dataContainer.put(DataContainerTag.MODEL_CODE, getEmptyCarrierModelCode());
			dataContainer.put(DataContainerTag.COLOR_CODE, getEmptyCarrierColorCode());
			dataContainer.put(DataContainerTag.COMBINATION_CODE, getEmptyCarrierCombinationCode());
			getLogger().info(dataContainer.toString());
			getBroadcastService().broadcast(scheduleMainPanel.getProcessPointId(), dataContainer);
			
			getLogger().info("refresh Screen ");
			refreshLots();
			scheduleMainPanel.getCurrentLotPanel().carrierSnTextSetFocus();
		
			
		}catch(Exception e) {
			e.printStackTrace();
			renderFieldBeanNg(scheduleMainPanel.getCurrentLotPanel().getPartSn().get(this.scannedMbpnsForLot.size()).getProductIdTextField(), "Empty");
			setError(e.getMessage());
			audioManager.playNoGoodSound();
		}
		
		
	}

	
	private void invokeBroadcast() {
		String carrierId = getCarrierId();
		String productId = "";
		for (int i = 0; i < scannedMbpnsForLot.size(); i++) {

			productId = productId + ","+scheduleMainPanel.getCurrentLotPanel().getPartSn().get(i).getProductIdTextField()
					.getText(); 
		}
		
		DataContainer dataContainer = new DefaultDataContainer();
		dataContainer.put(DataContainerTag.UNIT_RELEASE, 1);
		dataContainer.put(DataContainerTag.PART_INFO_LIST, productId);
		dataContainer.put(DataContainerTag.CARRIER_ID, carrierId);
		dataContainer.put(DataContainerTag.MODEL_CODE, getAttributeValue(DataContainerTag.MODEL_CODE));
		dataContainer.put(DataContainerTag.COLOR_CODE, getAttributeValue(DataContainerTag.COLOR_CODE));
		dataContainer.put(DataContainerTag.COMBINATION_CODE, getCombinationCode());
		getLogger().info(dataContainer.toString());
		getBroadcastService().broadcast(scheduleMainPanel.getProcessPointId(), dataContainer);
	}
	
	private void updateCarrierAttributeData() {
		try {
		String trackingArea = getProcessPoint(scheduleMainPanel.getProcessPointId()).getLineId();
		String carrierNumber = getCarrierId();
		
		removeCarrierData(trackingArea,carrierNumber);
		
		saveCarrierAttribute(trackingArea,carrierNumber, DataContainerTag.MODEL_CODE, getAttributeValue(DataContainerTag.MODEL_CODE));
		saveCarrierAttribute(trackingArea,carrierNumber, DataContainerTag.COLOR_CODE, getAttributeValue(DataContainerTag.COLOR_CODE));
		saveCarrierAttribute(trackingArea,carrierNumber,DataContainerTag.COMBINATION_CODE, getCombinationCode());
		
		
		for (int i = 0; i < scannedMbpnsForLot.size(); i++) {
			String partId = scheduleMainPanel.getCurrentLotPanel().getPartSn().get(i).getProductIdTextField()
					.getText();
			saveCarrierAttribute(trackingArea,carrierNumber,"PART"+(i+1), partId);
		}
		}catch(Exception e) {
			e.printStackTrace();
			setError(e.getMessage());
		}
	}

	private void removeCarrierData(String trackingArea, String carrierNumber) {
		
		getLogger().info("deleting CarrierAttribute Data for Carrier -"+ carrierNumber);
		List<CarrierAttribute> carrierAttributes = getCarrierAttributeDao().findByTrackingAreaAndCarrierNumber(trackingArea, carrierNumber);
		
		if(!carrierAttributes.isEmpty()) {
			getCarrierAttributeDao().removeAll(carrierAttributes);
		}
		
	}

	private void saveCarrierAttribute(String trackingArea,String carrierNumber, String attributeName,String attributeValue) {
		CarrierAttribute carrierAttribute = new CarrierAttribute();
		CarrierAttributeId carrierAttributeId = new CarrierAttributeId();
		carrierAttributeId.setTrackingArea(trackingArea);
		carrierAttributeId.setCarrierNumber(carrierNumber);
		carrierAttributeId.setAttribute(attributeName);
		
		carrierAttribute.setId(carrierAttributeId);
		carrierAttribute.setAttributeValue(attributeValue);
		
		getLogger().info("saving CarrierAttribute Data "+ carrierAttribute.toString());
		getCarrierAttributeDao().save(carrierAttribute);
		
	}
	private String getCombinationCode() {
		if(!this.productionLotMbpnSequences.isEmpty()) return removeLeadingZeros(this.productionLotMbpnSequences.get(0).getCombinationCode());
		return null;
	}

	private void validateProduct(String productId) {
		try {
			if(StringUtils.isEmpty(getCarrierId())) {
				scheduleMainPanel.getCurrentLotPanel().getCarrierField().requestFocus();
				throw new TaskException("Please enter Carrier Id before scanning Parts", "CARRIER");
			}
			if(StringUtils.isEmpty(productId)) {
				throw new TaskException("invalid Product", "PRODUCT");
			}
			MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
			String scannedSpec = "";
		if (mbpnProduct != null) {
			scannedSpec = mbpnProduct.getCurrentProductSpecCode();
			// if validate spec is true
			if (getProperties().isValidateMbpnSpec() ) {
				String expectedSpec = (String) this.getExpectedMbpns().get(this.scannedMbpnsForLot.size());
				
				if(expectedSpec.trim().equalsIgnoreCase(scannedSpec.trim())) {
					getLogger().info(" Product Spec for  Scanned Product - "+productId + " matches expected productSpec - "+expectedSpec);
				}else {
					String msg = " Product Spec - %s for  Scanned Product - %s does not match expected productSpec - %s";
					getLogger().error(String.format(msg,scannedSpec,productId,expectedSpec));
					setError(String.format(msg,getDesc(scannedSpec)+"("+scannedSpec+")",productId,getDesc(expectedSpec) + "("+expectedSpec+")"));
					throw new TaskException(String.format(msg,getDesc(scannedSpec)+"("+scannedSpec+")",productId,getDesc(expectedSpec)+ "("+expectedSpec+")"), "PRODUCT");
				}
				
			}
				
			if (isPreviousLineCheckEnabled()) {
				performPreviousLineCheck(mbpnProduct);			
			}
			if(getProperties().isUpdateSpecColor()) {
				mbpnProduct.setCurrentProductSpecCode(getMbpnSpecWithColorCode(mbpnProduct.getCurrentProductSpecCode(), getExtColor()) );//use mbpn spec to append color based on config prop (default = false)
			}
			mbpnProduct.setCurrentOrderNo( getProdLot());
		} else {
			
			// allow user to select mbpn and save to mbpn product tbx
			scheduleMainPanel.getCurrentLotPanel().getPartSn().get(this.scannedMbpnsForLot.size()).getProductIdTextField().setText(productId);
			scannedSpec = selectMbpn();
			if(StringUtils.isNotEmpty(scannedSpec)) {
				mbpnProduct = createProduct(productId, getProdLot(), scannedSpec);
			}else {
				throw new TaskException("invalid Product", "PRODUCT");
			}
		}

		if(isProductAlreadyScanned(mbpnProduct)) {
				String msg = " Product already scanned - %s ";
				getLogger().error(String.format(msg,productId));
				setError(String.format(msg,productId));
				throw new TaskException(String.format(msg,productId), "PRODUCT");
		}
			
		renderFieldBeanOk(scheduleMainPanel.getCurrentLotPanel().getPartSn().get(this.scannedMbpnsForLot.size()).getProductIdTextField(), productId);
		this.scannedProducts.add(mbpnProduct);
		this.scannedMbpnsForLot.add(scannedSpec);
		setError("");	
		audioManager.playOKSound();
		}catch(Exception e) {
			e.printStackTrace();
			renderFieldBeanNg(scheduleMainPanel.getCurrentLotPanel().getPartSn().get(this.scannedMbpnsForLot.size()).getProductIdTextField(), productId);
			setError(e.getMessage());
			audioManager.playNoGoodSound();
		}
	}

	private String getCarrierId() {
		String carrierId = scheduleMainPanel.getCurrentLotPanel().getCarrierField().getComponent().getText();
		return carrierId;
	}

	private List<String> getExpectedMbpns() {
		if (this.expectedMbpnsForLot == null || this.expectedMbpnsForLot.isEmpty()) {

			this.productionLotMbpnSequences = getProductionLotMbpnSequenceDao().findAllByProductionLot(getProdLot());

			for (ProductionLotMbpnSequence productionLotMbpnSequence : productionLotMbpnSequences) {
				
				expectedMbpnsForLot.add(productionLotMbpnSequence.getMbpn());
			}

		}
		return this.expectedMbpnsForLot;
	}

	private MbpnProduct createProduct(String productId, String productionLot, String productSpecCode) {
		
		MbpnProduct mbpnProduct = new MbpnProduct();
		mbpnProduct.setProductId(productId);

		mbpnProduct.setCurrentOrderNo(productionLot);
		if(getProperties().isUpdateSpecColor()) {
			mbpnProduct.setCurrentProductSpecCode(getMbpnSpecWithColorCode(productSpecCode, getExtColor()));
		}else {
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
		}
		mbpnProduct.setLastPassingProcessPointId(scheduleMainPanel.getProcessPointId());
		return mbpnProduct;
	}

	private MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null) {
			mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}
	
	private ProductionLotMbpnSequenceDao getProductionLotMbpnSequenceDao() {
		if (productionLotMbpnSequenceDao == null) {
			productionLotMbpnSequenceDao = ServiceFactory.getDao(ProductionLotMbpnSequenceDao.class);
		}
		return productionLotMbpnSequenceDao;
	}

	private BroadcastService getBroadcastService() {
		if (_broadcastService == null) {
			_broadcastService = ServiceFactory.getService(BroadcastService.class);
		}
		return _broadcastService;
	}

	private ProductOnService getProductOnService() {
		if (_productOnService == null) {
			_productOnService = ServiceFactory.getService(ProductOnService.class);
		}
		return _productOnService;
	}
	private String getProdLot() {
		return this.scheduleMainPanel.getCurrentLotPanel().getCurrentLot().getKeyObject().getProductionLot();
	}

	private String getExtColor() {
		return ProductSpecUtil.extractExtColorCode(
				this.scheduleMainPanel.getCurrentLotPanel().getCurrentLot().getKeyObject().getProductSpecCode());
	}
	
	private String getAttributeValue(String tag) {
		String productSpecCode = this.scheduleMainPanel.getCurrentLotPanel().getCurrentLot().getKeyObject().getProductSpecCode();
		return removeLeadingZeros(getBuildAttributeCache().findAttributeValue(productSpecCode, tag));
		
	}
	
	private DataContainer prepareData(DataContainer dc) {
		dc.put(TagNames.PROCESS_POINT_ID.name(), scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.APPLICATION_ID.name(), scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.CLIENT_ID.name(), scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.PRODUCTION_LOT.name(), getCurrentOrNextLot());
		getLogger().info(dc.toString());
		return dc;
	}
	
	protected String getCurrentOrNextLot() {
		String prodLot = null;
		List<PreProductionLot> lots = scheduleMainPanel.getCurrentLotPanel().getCurrentLots();
		int i = 0;
		while(prodLot == null && i < lots.size()) {
			PreProductionLot tempLot = lots.get(i++);
			if(tempLot.getStampedCount() < getLotSize(tempLot)) {
				prodLot = tempLot.getProductionLot();
			}
		}
		
		if(prodLot == null) {
			if(scheduleMainPanel.getUpcomingLotPanel().getItems().size() > 0) {
				prodLot = scheduleMainPanel.getUpcomingLotPanel().getItems().get(0).getKeyObject().getProductionLot();
			} else {
				getLogger().error("There is no upcoming lot.");
			}
		}
		return prodLot;
	}
	
	private int getLotSize(PreProductionLot preProductionLot) {
		return preProductionLot.getLotSize();
	}
	
	private Logger getLogger() {
		return scheduleMainPanel.getLogger();
	}
	
	private String getMbpnSpecWithColorCode(String mbpn, String color) {
		String mbpnSpec = StringUtil.padRight(mbpn,MbpnDef.MBPN.getLength(),' ',true);
		String hesColor = StringUtil.padRight(color,MbpnDef.HES_COLOR.getLength(),' ',true);
		
		return mbpnSpec+hesColor;
	}
	
	private void reset() {
		 this.expectedMbpnsForLot.clear();
		 this.scannedProducts.clear();
		 this.scannedMbpnsForLot.clear();
		 this.productionLotMbpnSequences.clear();
		 setError("");
		 scheduleMainPanel.getCurrentLotPanel().getCarrierField().getComponent().removeKeyListener(l);
	}
	
	protected void renderFieldBeanOk(LengthFieldBean bean, String serialNumber) {
		bean.setText(serialNumber);
		bean.setBackground(Color.green);
		bean.setDisabledTextColor(Color.black);
		bean.setForeground(Color.black);
		bean.setEditable(false);
		bean.setEnabled(false);
	}
	
	protected void renderFieldBeanNg(LengthFieldBean bean, String text) {
		bean.setText(text);
		bean.setBackground(Color.red);
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		bean.setEnabled(true);
	}
	
	private void setError(String msg) {
		scheduleMainPanel.getMainWindow().setErrorMessage(msg);
	}
	
	
	private String  selectMbpn() {
		Object[] descArray = getMbpnDescriptions(getExpectedMbpns());
		int x = JOptionPane.showOptionDialog(scheduleMainPanel.getMainWindow(), "Please Select Mbpn", "Please Select Mbpn", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE, null, descArray, descArray[0]);
		if(x > 0) return getExpectedMbpns().get(x-1);
        return null;
    }
	
	private Object[] getMbpnDescriptions(List<String> expectedMbpns){
		List<String> mbpnDescList = new ArrayList<String>();
		mbpnDescList.add("CANCEL");
		for(String mbpnSpec:expectedMbpns) {
			String desc = getDesc(mbpnSpec);
			if(!mbpnDescList.contains(desc))mbpnDescList.add(desc);
		}
		
		return mbpnDescList.toArray();
	}

	private String getDesc(String mbpnSpec) {
		String desc = mbpnSpec;
		Mbpn mbpn = getMbpnDao().findByKey(mbpnSpec);
		if(mbpn != null)desc = StringUtils.isEmpty(mbpn.getDescription())?mbpn.getMbpn():mbpn.getDescription();
		
		return desc;
	}
	protected void trackProduct(MbpnProduct product) {
		if(product == null)
			getLogger().warn(this.getClass().getSimpleName(), " product is null and skipped tracking.");
		else 
			getTrackingService().track(product, scheduleMainPanel.getProcessPointId());
		
	}

	protected TrackingService getTrackingService() {
		if(trackingService == null)
			trackingService = getService(TrackingService.class);

		return trackingService;
	}
	
	private CarrierAttributeDao getCarrierAttributeDao() {
		if (carrierAttributeDao == null) {
			carrierAttributeDao = ServiceFactory.getDao(CarrierAttributeDao.class);
		}
		return carrierAttributeDao;
	}
	
	private ProcessPoint getProcessPoint(String processPointId) { 
		
		if(this.processPoint == null) {
			this.processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		}
	
		return this.processPoint;
	}
	
	private String getEmptyCarrierString() {
		return PropertyService.getProperty(scheduleMainPanel.getApplicationId(), "EMPTY_CARRIER_NAME","EMPTY");
	}
	
	private String getEmptyCarrierCombinationCode() {
		return PropertyService.getProperty(scheduleMainPanel.getApplicationId(), "EMPTY_CARRIER_COMBINATION_CODE","EMPTY");
	}

	private String getEmptyCarrierColorCode() {
		String emptyCarrierColorCode = PropertyService.getProperty(scheduleMainPanel.getApplicationId(), "EMPTY_CARRIER_COLOR_CODE","");
		if (StringUtils.isBlank(emptyCarrierColorCode)) {
			emptyCarrierColorCode = getAttributeValue(DataContainerTag.COLOR_CODE);
		}
		return emptyCarrierColorCode;
	}

	private String getEmptyCarrierModelCode() {
		return PropertyService.getProperty(scheduleMainPanel.getApplicationId(), "EMPTY_CARRIER_MODEL_CODE","EMPTY");
	}

	protected void performPreviousLineCheck(MbpnProduct product) {
		ProductCheckUtil prodCheckUtil = new ProductCheckUtil(product, getProcessPoint(scheduleMainPanel.getProcessPointId()));
		if (prodCheckUtil.invalidPreviousLineCheck()) {
			String trackingStatus = product.getTrackingStatus();
			String lineName=trackingStatus==null?null:ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus).getLineName();
			String msg="Product "+product.getProductId() +" came from an unexpected line : "+lineName;
			throw new TaskException(msg, "PRODUCT");
		}		
	}
	
	private boolean isPreviousLineCheckEnabled() {
		return PropertyService.getPropertyBoolean(scheduleMainPanel.getApplicationId(), "PREVIOUS_LINE_CHECK_ENABLED",false);
	}
	
	private int getCarrierNumberLength() {
		return PropertyService.getPropertyInt(scheduleMainPanel.getApplicationId(), "CARRIER_NUMBER_LENGTH",4);
	}
	
	private void addCarrierKeyListener() {
		l = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					if(isCarrierNumberValid(getCarrierId())) {
						setCarrierNumberOK();
					}
					
				}
				
			}
		
		};
		scheduleMainPanel.getCurrentLotPanel().getCarrierField().getComponent().addKeyListener(l);	
		
	}
	
	private boolean isCarrierReadError(String carrierId) {
		return !StringUtils.isNumeric(carrierId.trim());
	}
	
	private void setCarrierNumberError(String msg) {
		setError(msg);
		audioManager.playNoGoodSound();
		scheduleMainPanel.getCurrentLotPanel().getCarrierField().getComponent().setBackground(Color.RED);
		scheduleMainPanel.getCurrentLotPanel().carrierSnTextSetFocus();
	}
	
	private void setCarrierNumberOK() {
		setError("");
		scheduleMainPanel.getCurrentLotPanel().getCarrierField().getComponent().setBackground(Color.GREEN);
		scheduleMainPanel.getCurrentLotPanel().partSnTextSetFocus(scheduleMainPanel.getCurrentLotPanel().getPartSn().get(0).getProductIdTextField());
	}
	
	private String removeLeadingZeros(String s) {
		return !StringUtils.isEmpty(s)?s.replaceFirst("^0+(?!$)", ""):null;
	}
	
	private boolean isCarrierNumberValid(String carrierId) {
		if(isCarrierReadError(carrierId)) {
			setCarrierNumberError("Invalid Carrier Number "+carrierId+" Received");
			return false;
		}
		if(carrierId.length()>getCarrierNumberLength()) {
			setCarrierNumberError("Carrier Number "+carrierId+" length greater than "+getCarrierNumberLength());
			return false;
		}
		
		return true;
	}
	
	private void saveAndTrackProducts() {
		
		for(MbpnProduct mbpnProduct:scannedProducts) {
			getMbpnProductDao().save(mbpnProduct);
			//track 
			trackProduct(mbpnProduct);
		}
	}
	
	private boolean isProductAlreadyScanned(MbpnProduct mbpnProduct) {
		if(this.scannedProducts.contains(mbpnProduct)) {
			return true;
		}
		return false;
	}
}
