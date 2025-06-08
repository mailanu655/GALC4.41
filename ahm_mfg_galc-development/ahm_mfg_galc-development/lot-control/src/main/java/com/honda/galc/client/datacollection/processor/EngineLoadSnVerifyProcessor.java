/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Subu Kathiresan
 * @date Dec 06, 2011
 */
public class EngineLoadSnVerifyProcessor extends EngineLoadSnProcessor 
	implements IPartSerialNumberProcessor {

	public EngineLoadSnVerifyProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	public synchronized boolean execute(PartSerialNumber partNumber) {
		Logger.getLogger().info("EngineLoadSnVerifyProcessor: Enter execute()");
		try {
			Logger.getLogger().info("Process part:" + partNumber.getPartSn());
			
			if (!validateScannedEngine(partNumber))
				return false;

			if (confirmPartSerialNumber(partNumber)) {
				String vin = getController().getState().getProduct().getProductId();
				saveAndTrackEngine(vin);
				getController().getFsm().partSnOk(installedPart);
				Logger.getLogger().info("EngineLoadSnVerifyProcessor: Exit execute() ok");
				
				//update the Alt Engine with valid expected spec if property set
				updateEngineSpec();
				
				return true;
			}
		} catch (Exception ex) {
			if (ex.getMessage() != null)
				Logger.getLogger().error(ex.getMessage());
			
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID, ex.getMessage() == null ? "" : ex.getMessage());
		}
		Logger.getLogger().info("EngineLoadSnVerifyProcessor: Exit execute() ng");
		return false;
	}
	
	/**
	 * 
	 * @param vin
	 */
	private boolean saveAndTrackEngine(String vin) {
		//Track engine if PP is given.
		try {
			EngineLoadPropertyBean engineLoadPropertyBean = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, context.getProcessPointId());
			String enginePPID = engineLoadPropertyBean.getEngineTrackingPpid();
			if (enginePPID != null && !enginePPID.equals("")) {
				ProductHistory productHistory = ProductTypeUtil.createProductHistory(engine.getProductId(), enginePPID, ProductType.ENGINE);
				productHistory.setAssociateNo(context.getUserId());
				
				getTrackingService().track(ProductType.ENGINE, productHistory);
			}

			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("PartSerialNumberProcessor:: Could not save/track engine " + engine.getProductId() + " for vin " + vin);
		}
		return false;
	}
}
