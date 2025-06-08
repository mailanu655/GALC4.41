/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.client.datacollection.view.ErrorDialogManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.device.DeviceData;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.EngineLoadSnValidation;
import com.honda.galc.device.dataformat.EngineLoadVin;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Jan 10, 2012
 */
public class EngineLoadVinProcessor extends FrameVinProcessor implements DeviceListener{
	protected InProcessProductDao inProcessProductDao = null;
	protected ProcessPointDao processPointDao = null;
	protected FrameDao frameDao = null;
	protected Frame frame = null;
	protected EngineLoadPropertyBean engineLoadPropertyBean;
	
	public EngineLoadVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	@Override
	public synchronized boolean execute(ProductId productId) {

		Logger.getLogger().info("EngineLoadVinProcessor : Enter execute()");
		try {
			Logger.getLogger().info(PROCESS_PRODUCT + productId);
			confirmProductId(productId);
			
			if (!validateCurrentProduct(productId.getProductId())) {
				return false;
			}
			
			getController().getFsm().productIdOk(product);
			this.registerEiDeviceListener();
			this.sendVinToPlc(productId);
			Logger.getLogger().debug("EngineLoadVinProcessor : Exit execute() ok");
			return true;
		} catch (Throwable t){
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		} 

		Logger.getLogger().info("EngineLoadVinProcessor : Exit execute() ng");
		return false;
	}
	
	/**
	 * validate the current product
	 * 
	 * @return
	 */
	protected boolean validateCurrentProduct(String productId) {	
		String bodyAssemblyLine = getProcessPointDao().findByKey(getFrameLinePropertyBean().getAfOnProcessPointId()).getLineId();
		
		frame = getFrameDao().findByKey(productId);
		
		//check if Frame comes from the correct previous line
		if (frame.getTrackingStatus() == null || !frame.getTrackingStatus().equals(bodyAssemblyLine)) {
			getController().getFsm().productIdNg(product, MESSAGE_ID, "Frame " + productId + " is from an invalid Line " + frame.getTrackingStatus());
			return false;
		}
		
		//check if Frame is the tail node in the in-processed product linked list to prevent processing the last AF ON VIN.
		if (!this.getEngineLoadPropertyBean().isAllowLastAfOnVinProcessing()) {
			InProcessProduct lastAfOnVin = this.getInProcessProductDao().findLastForLine(bodyAssemblyLine);
			if (frame.getId().equals(lastAfOnVin.getId().trim())) {
				ErrorDialogManager mgr = new ErrorDialogManager();
				String message = productId + " is the last AF ON VIN. \nPlease wait for more VINs to be loaded AF ON.";
				mgr.showDialog(context.getFrame(),  message, "", property);
				getController().getFsm().productIdNg(product, MESSAGE_ID, message.replace("\n", ""));
				return false;
			}
		}
				
		//check if Frame already has an engine assigned
		if (frame.getEngineSerialNo() != null && !frame.getEngineSerialNo().trim().equals("")) {
			getController().getFsm().productIdNg(product, MESSAGE_ID, "Frame " + productId + " already assigned to Engine " + frame.getEngineSerialNo().trim());
			return false;
		}
					
		return true;
	}
	
	public FrameDao getFrameDao() {
		if (frameDao == null) {
			frameDao = ServiceFactory.getDao(FrameDao.class);
		}
		return frameDao;
	}

	public ProcessPointDao getProcessPointDao() {
		if (processPointDao == null) {
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
	
	public InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null) {
			inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		}
		return inProcessProductDao;
	}
	
	public EngineLoadPropertyBean getEngineLoadPropertyBean(){
		if (engineLoadPropertyBean == null) {
			engineLoadPropertyBean = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, this.context.getProcessPointId());
		}
		return engineLoadPropertyBean;
	}
	
	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled() && !isOutputDataRegistered()){
			DeviceDataConverter.getInstance().registerOutputDeviceData(getDeviceOutputDataList());
		}
	}
	
	private List<IDeviceData> getDeviceOutputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new EngineLoadVin());
		list.add(new EngineLoadSnValidation());
		return list;
	}
	
	private Boolean isOutputDataRegistered() {
		for(DeviceData deviceData: DeviceDataConverter.getInstance().getDeviceDataList()){
			if(deviceData.isOutput()) return true;
		} 
		return false;
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void sendVinToPlc(ProductId productId) {
	Logger.getLogger().info("Sending VIN to PLC");
		
		EngineLoadVin vin = new EngineLoadVin();
		vin.setVin(productId.toString());
		try {
			DeviceManager.getInstance().getEiDevice().send(vin);
			Logger.getLogger().info("Sent VIN  to PLC");
		} catch (Exception ex) {
			Logger.getLogger().info("Could not send VIN to PLC");
			return;
		}
	}
}