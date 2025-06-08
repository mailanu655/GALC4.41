package com.honda.galc.client.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CarrierId;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ResetCommand;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.property.CarrierAssociationPropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * <h3>CarrierAssociationController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CarrierAssociationController description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Oct 9, 2018</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Oct 9, 2018
 */
public class CarrierAssociationController implements DeviceListener, ActionListener, KeyListener, FocusListener{
	private CarrierAssociationView view;
	private CarrierAssociationPropertyBean propertyBean;
	private BaseProduct product;
	private ProductCheckPropertyBean checkerPropertyBean;
	private PaintOnState state;
	private GtsCarrierDao carrierDao;
	private ClientAudioManager audioManager;
	private Boolean broadcast = null;
	private Boolean tracking = null;
	
	
	public CarrierAssociationController(CarrierAssociationView view) {
		super();
		this.view = view;
		
		init();
	}

	private void init() {
		getCarrierAssociationPropertyBean();
		registerDeviceData();
		state = new PaintOnState(getFields());
		
		
		final String confirmKey = "confirmKey";
		this.view.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), confirmKey);
		this.view.getActionMap().put(confirmKey, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				view.getDoneButton().doClick(0);
			}
		});
		
	}

	public CarrierAssociationPropertyBean getCarrierAssociationPropertyBean() {
		if(propertyBean == null)
			propertyBean = PropertyService.getPropertyBean(CarrierAssociationPropertyBean.class, getProcessPointId());
		return propertyBean;
	}

	private List<String> getFields() {
		List<String> list = new ArrayList<String>();
		if(getCarrierAssociationPropertyBean().isCarrierAssociationRequired()){
			list.add(TagNames.PRODUCT_ID.name());
			list.add(TagNames.CARRIER_ID.name());
		} else {
			list.add(TagNames.PRODUCT_ID.name());
		}
		return list;
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		Logger.getLogger().info("Device data received: ", deviceData.getClass().getSimpleName(), " : ", deviceData.toString());
		boolean checkResult = false;
		if(deviceData instanceof ProductId)
			checkResult = validateProductId(((ProductId)deviceData).getProductId());
		else if(deviceData instanceof CarrierId)
			checkResult = validateCarrierId(((CarrierId)deviceData).getCarrierId());
		else if(deviceData instanceof ResetCommand)
			checkResult = doAssociation();
		else 
			Logger.getLogger().warn("Invalid data received: " + deviceData);
		
		return checkResult ? DataCollectionComplete.OK() : DataCollectionComplete.NG();
	}

	private boolean validateProductId(String productId) {
		try {
			product = ProductTypeUtil.getTypeUtil(propertyBean.getProductType()).findProduct(productId);
		} catch (Exception e) {
			renderResult(TagNames.PRODUCT_ID.name(),productId, "Exception to find product.");
		}
		
		if(product == null) {
			renderResult(TagNames.PRODUCT_ID.name(), productId, "Product does not exist!");
		} else {
			String checkResult = doCheck();
			renderResult(TagNames.PRODUCT_ID.name(), productId, checkResult);
		}
		
		return state.getStatus(TagNames.PRODUCT_ID.name());
		
	}

	private void renderResult(String fieldId, String key, String checkResult) {
		
		if(!StringUtils.isEmpty(checkResult)) {
			Logger.getLogger().error(checkResult);
		}
		
		KeyValue<String, String> kv = new KeyValue<String, String>(key, checkResult);
		
		// The requirement is when not confirm button click and 
		// next VIN come, then reset the client to process the next 
		// VIN/Carrier 
		
		if(getCarrierAssociationPropertyBean().isResetByNextCar() && state.isComplete()) {
			state.reset();
			view.reset();
			getAudioManager().playAlarmSound();
		}
		
		// continue after reset state
		boolean isLastError = state.change(fieldId, kv);
		
		if(TagNames.PRODUCT_ID.name().equals(fieldId) && product != null)
			view.setProductSpec(product.getProductSpecCode());
		view.setValue(fieldId, kv, isLastError);
	
		if(propertyBean.isEnableAudioAlarm()) {
			if(StringUtils.EMPTY.equals(checkResult))
				getAudioManager().playGoodSound();
			else if(!StringUtils.isEmpty(checkResult))
				getAudioManager().playNGSound();
		}
		
		if(state.isComplete()) {
			view.getDoneButton().setEnabled(true);
			view.getDoneButton().requestFocus();
		}
	}
	

	private String doCheck() {
		String[] checkTypes = getCheckerPropertyBean().getProductCheckTypes();
		if (checkTypes == null || checkTypes.length == 0) {
			return StringUtils.EMPTY;
		}
		
		Map<String, Object> checkResults = ProductCheckUtil.check(product, view.getMainWindow().getApplicationContext().getProcessPoint(), getCheckerPropertyBean().getProductCheckTypes());
		if (checkResults == null || checkResults.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String message =  "Failed Product Checks:" + ProductCheckUtil.formatTxt(checkResults);
		Logger.getLogger().error(message);
		return message;

	}

	public void registerDeviceData() {
		IDevice eiDevice = DeviceManager.getInstance().getDevice(EiDevice.NAME);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(this, getProcessData());
		}
	}
	
	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new CarrierId());
		list.add(new ResetCommand());
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.clearMessage();
		try{
			if(e.getSource() == view.getProductIdField())
				validateProductId(view.getProductIdField().getText());
			else if(e.getSource() == view.getVirtualRfidField()) 
				validateCarrierId(view.getVirtualRfidField().getText());
			else if(e.getSource() == view.getDoneButton()) {
				doAssociation();
			}
		
		}catch(Exception ex) {
			Logger.getLogger().error("Exception occurred : " + ex.getMessage());
			displayErrorMessage("Exception occurred : " + ex.getMessage());
		}
		
	}

	private boolean doAssociation() {
		if(!state.isComplete()) {
			Logger.getLogger().warn("ERROR: unable to associcate product and carrier:" + state.toString());
			return false;
		}
		
		try {
			DataCollectionComplete dataCollectionComplete = DataCollectionComplete.OK();
			try {
				associateProductAndCarrier();
				view.reset();
				if (isTracking())
					doTracking();
			} catch (Exception e) {
				Logger.getLogger().error("ERROR:" + e.getMessage());
				displayErrorMessage("Failed to associate product with carrier.");
				dataCollectionComplete = DataCollectionComplete.NG();
			}
			
			if (!StringUtils.isEmpty(propertyBean.getReplyClientId()))
				writeReplyToDevice(dataCollectionComplete);
			if (isBroadcast())
				doBroadcast();
			if (propertyBean.isNotifyLsm())
				notifyLSM();
			if (propertyBean.isSaveLastProduct())
				saveLastProduct();

			state.reset();
			product = null;
		} catch (Exception e) {
			Logger.getLogger().error("ERROR:" + e.getMessage());
			displayErrorMessage("ERROR:" + "Error in product and carrier association.");
			return false;
		}
		return true;
	}

	private void writeReplyToDevice(DataCollectionComplete dataCollectionComplete) {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(propertyBean.getReplyClientId());
		device.getDeviceFormat(TagNames.DATA_COLLECTION_COMPLETE.name()).setValue(dataCollectionComplete);
		eiDevice.syncSend(propertyBean.getReplyClientId(), device.toRequestDataContainer());
		
	}

	private void saveLastProduct() {
		ExpectedProduct expectedProduct = new ExpectedProduct(product.getProductId(),getProcessPointId());
		ServiceFactory.getDao(ExpectedProductDao.class).save(expectedProduct);
		Logger.getLogger().info("Updated ExpectedProduct:", expectedProduct.toString());
	}

	private boolean isTracking() {
		if(tracking == null)
			tracking = view.getMainWindow().getApplicationContext().getProcessPoint().isPassingCount() || view.getMainWindow().getApplicationContext().getProcessPoint().isTrackingPoint();
		return tracking;
	}

	private void notifyLSM() {
		ServiceFactory.getNotificationService(IProductPassedNotification.class, getProcessPointId()).execute(
				getProcessPointId(), product.getProductId());

		Logger.getLogger().info("Notified product passed event:" + getProcessPointId() + ":" + product.getProductId());
	}

	private String getProcessPointId() {
		return view.getMainWindow().getApplicationContext().getProcessPointId();
	} 
	

	private void doTracking() {
		TrackingService service = ServiceFactory.getService(TrackingService.class);
		service.track(ProductType.valueOf(getCarrierAssociationPropertyBean().getProductType()), product.getProductId(), getProcessPointId());
	}


	private void doBroadcast() {
		if (product != null) {
			try{
				BroadcastService broadcastService = ServiceFactory.getService(BroadcastService.class);
				DataContainer dc = new DefaultDataContainer();
				dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
				dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
				broadcastService.broadcast(getProcessPointId(), dc);
			}catch(Exception e){
				Logger.getLogger().warn(e, "Failed to invoke broadcast service. " + e.getMessage());
			}
		}
	}

	private void associateProductAndCarrier() {
		if(!state.isAssociation()) return;
		GtsCarrier entity = findCarrier(state.getValue(TagNames.CARRIER_ID.name()));
		entity.setProductId(state.getValue(TagNames.PRODUCT_ID.name()));
		getCarrierDao().update(entity);
	}

	private GtsCarrier findCarrier(String carrierId) {
		GtsCarrierId id = new GtsCarrierId(propertyBean.getTrackingArea(), carrierId);
		GtsCarrier entity = getCarrierDao().findByKey(id);
		return entity;
	}

	private void displayErrorMessage(String string) {
		view.getMainWindow().getStatusMessagePanel().setErrorMessageArea(string);
		
	}

	private boolean validateCarrierId(String carrierId) {
		String checkResult = StringUtils.EMPTY;
		
		if(!getCarrierAssociationPropertyBean().isCarrierAssociationRequired()) {
			//this should never happen
			Logger.getLogger().warn("Carrier Id received while carrier association is not required.");
			return false;
		}
		
		//check Carrier exist and not used
		GtsCarrier carrier = null;
		carrier = findCarrier(carrierId);

		if(carrier == null)
			checkResult = "Error: Carrier " + carrierId + " does not exist!";
		else if(!StringUtils.isEmpty(carrier.getProductId())) {
			displayErrorMessage("Error: carrier " + carrierId + " already associated with " + carrier.getProductId());
			if (!MessageDialog.confirm(view, "Re-associate Carrier: "+ carrierId + " with Product ID: "+ view.getProductIdField().getText() + "?"))
				checkResult = "Error: carrier " + carrierId + " already associated with " + carrier.getProductId();
		}
		renderResult(TagNames.CARRIER_ID.name(), carrierId, checkResult);
		
		return state.getStatus(TagNames.CARRIER_ID.name());
	}


	public ProductCheckPropertyBean getCheckerPropertyBean() {
		if(checkerPropertyBean == null)
			checkerPropertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProcessPointId());
		return checkerPropertyBean;
	}

	public GtsCarrierDao getCarrierDao() {
		if(carrierDao == null)
			carrierDao = ServiceFactory.getDao(GtsCarrierDao.class);
		
		return carrierDao;
	}

	public ClientAudioManager getAudioManager() {
		if(audioManager == null)
			audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class,
					ApplicationContext.getInstance().getProcessPointId()));
		
		return audioManager;
	}

	public Boolean isBroadcast() {
		if(broadcast == null) {
			List<BroadcastDestination> destinations = 
					ServiceFactory.getDao(BroadcastDestinationDao.class)
					.findAllByProcessPointId(getProcessPointId());
			broadcast = !destinations.isEmpty();
		}
		return broadcast;
	}
	
	public PaintOnState getState() {
		return state;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			doAssociation();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		
	}

    /**
     * Invoked when a component loses the keyboard focus.
     */
	@Override
    public void focusLost(FocusEvent e) {
		if (e.getSource() == view.getVirtualCarierTextField().getComponent() 
		&& !view.getVirtualCarierTextField().getComponent().getText().equals(state.getValue(TagNames.CARRIER_ID.name()))) {
			view.getDoneButton().setEnabled(false);
			displayErrorMessage("Press Enter to validate new Carrier ID.");
			TextFieldState.ERROR.setState(view.getVirtualCarierTextField().getComponent());
			view.getVirtualCarierTextField().getComponent().requestFocus();
		}
		else if (e.getSource() == view.getProductIdField() && !view.getProductIdField().getText().equals(state.getValue(TagNames.PRODUCT_ID.name()))) {
			view.getDoneButton().setEnabled(false);
			displayErrorMessage("Press Enter to validate new VIN.");
			TextFieldState.ERROR.setState(view.getProductIdField());
			view.getProductIdField().requestFocus();
		}
    }

	
}



