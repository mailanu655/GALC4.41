package com.honda.galc.client.datacollection.processor;


import java.util.ArrayList;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.ServiceFactory;

public class KeyCylinderVinProcessor extends ProcessorBase implements IProductIdProcessor{
	protected InstalledPart installedPart = null;
	protected ProductBean product = null;
	private static final String PART_SN_MESSAGE_ID = "PART_SN";
	private String afSeqNo = null;
	public KeyCylinderVinProcessor(ClientContext context) {
		super(context);
		init();
		
	}

	public synchronized boolean execute(PartSerialNumber partnumber) {
		Logger.getLogger().debug("KeyCylinderProcessor : Enter KeyCylinderPartSerialNumber");
		try {
			DataCollectionState currentState = (DataCollectionState) getController().getState();
			if (currentState instanceof ProcessTorque) {
				getController().getFsm().error(new Message("Unexpected Part Serial number scan received, waiting for Torque"));
				return false;
			} else if (currentState instanceof ProcessProduct) {
				getController().getFsm().error(new Message("Unexpected Part Serial number scan received, waiting for Product scan"));
				return false;
			}
			
			Logger.getLogger().info("Process part:" + partnumber.getPartSn());
			
			installedPart.setPartSerialNumber(afSeqNo);
			installedPart.setPartId(getController().getCurrentLotControlRule().getParts().get(0).getId().getPartId());
			installedPart.setValidPartSerialNumber(true);	
			getController().getFsm().partSnOk(installedPart);
			
				
			Logger.getLogger().debug("KeyCylinderProcessor:: Exit KeyCylinderPartSerialNumber ok");
			return true;

		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("KeyCylinderProcessor:: Exit confirmPartSerialNumber ng");
		return false;
		}
		
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().debug("KeyCylinderProcessor : Enter KeyCylinderProductId");
		try {
			afSeqNo = getAfSeqNoByVin(productId.getProductId());
			getInstalledPart(productId);
			getController().getFsm().partSnOk(installedPart);
			Logger.getLogger().debug("KeyCylinderProcessor : Enter KeyCylinderProductId OK");
			return true;
		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			product.setValidProductId(false);
			getController().getFsm().productIdNg(product, te.getTaskName(), te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			getController().getFsm().error(new Message("PRODUCT", se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(new Message("MSG01", e.getCause().toString()));
		} catch (Throwable t){
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(new Message("MSG01", t.getCause().toString()));
		} 

		Logger.getLogger().debug("Finished executing: " + this.getClass().getName());
		return false;
	}
	
	private void getInstalledPart(ProductId productId){
		installedPart.setProductId(productId.getProductId());
		installedPart.setPartName(getController().getCurrentLotControlRule()
					.getPartNameString());
		installedPart.setPartSerialNumber(afSeqNo);
		installedPart.setPartId(getController().getCurrentLotControlRule().getParts().get(0).getId().getPartId());
		installedPart.setValidPartSerialNumber(true);
	}
	
	private String getAfSeqNoByVin(String prodId){
		String afseqno = null;
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		Frame frame = frameDao.findByKey(prodId);
		if(frame== null)
			getController().getFsm().error(
					new Message(
							prodId,
							"product id does not exist",
							MessageType.INFO));
		
			else{
				if(frame.getAfOnSequenceNumber()== null)
					afseqno= "UNKNOWN";
				else
				afseqno = Integer.toString(frame.getAfOnSequenceNumber());
			}
		return afseqno; 
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
	}

	public void registerDeviceListener(DeviceListener listener) {
		
	}

}


