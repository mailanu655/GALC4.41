package com.honda.galc.client.datacollection.strategy;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.client.datacollection.processor.ProductIdProcessor;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductCheckUtil;

/*
 * 
 * @author Gangadhararao Gadde 
 * @ Feb 06, 2014
added additional checks
 */
public class EngineSnConfirmationProcessor extends PartSerialNumberProcessor {

	protected FrameDao frameDao = null;
	protected EngineDao engineDao = null;
	protected ProcessPointDao processPointDao = null;
	
	public EngineSnConfirmationProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partNumber) {
		String vin = getController().getState().getProduct().getProductId();
		checkPartSerialNumber(partNumber);
		String ein = installedPart.getPartSerialNumber();
		return validateEngine(ein, vin,context.getProcessPointId().trim());
	}
	
	private boolean validateEngine(String ein, String vin,String processPoint) {
		boolean result = false;
		try {
			Frame frame = getFrameDao().findByKey(vin); 
			Engine engine = getEngineDao().findByKey(ein);
			ProductCheckUtil productCheckUtil = new ProductCheckUtil();
			productCheckUtil.setProduct(frame);
			productCheckUtil.setProcessPoint(getProcessPointDao().findByKey(processPoint));
			if(engine == null) {
				invalidPart("Engine not found: " + ein);
			} else if(frame.getEngineSerialNo() == null) {
				invalidPart("VIN has no engine assigned.");
			} else if(!ein.equals(frame.getEngineSerialNo())) {
				invalidPart("VIN married with another engine: " + frame.getEngineSerialNo());
			} else if(engine.getVin() == null) {
				invalidPart("No VIN assigned to engine: " + ein);
			} else if(!vin.equals(engine.getVin())) {
				invalidPart("Engine married with another VIN: " + engine.getVin());
			} else if(!productCheckUtil.checkEngineType()) {
				invalidPart("Wrong engine type for VIN:" + engine.getVin());
				return false;
			} else {
				installedPart.setPartSerialNumber(ein);
				installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
				installedPart.setValidPartSerialNumber(true);
				result = true;
			}
		
		} catch(Exception e) {
			invalidPart("Engine could not be verified. " + e.getMessage());
			Logger.getLogger().error("Engine could not be verified. " + e.getMessage());
		}
		return result;
	}

	
	public void invalidPart(String message) {
		throw new TaskException(message, ProductIdProcessor.PROCESS_PRODUCT);
	}
	
	public FrameDao getFrameDao() {
		if (frameDao == null) {
			frameDao = ServiceFactory.getDao(FrameDao.class);
		}
		return frameDao;
	}
	
	public EngineDao getEngineDao() {
		if (engineDao == null) {
			engineDao = ServiceFactory.getDao(EngineDao.class);
		}
		return engineDao;
	}
		
	public ProcessPointDao getProcessPointDao() {
		if (processPointDao == null) {
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
	
	public boolean validate(LotControlRule rule,String productId,ProductBuildResult result){
		return validateEngine(result.getPartSerialNumber(), productId,rule.getId().getProcessPointId().trim());
	}
}
