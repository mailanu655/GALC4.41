
package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.ServiceFactory;


/**
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 */
public class EngineLoadMissionSnProcessor extends ProcessorBase implements IProductIdProcessor {

	public static final String MESSAGE_ID = "PART_SN"; 
	protected InstalledPart installedPart = null;
	protected EngineDao engineDao = null;

	public EngineLoadMissionSnProcessor(ClientContext context) {
		super(context);
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
	}


	@Override
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().info("EngineLoadMissionSnProcessor: Enter execute()");
		try {
			LotControlRule rule = getController().getState().getCurrentLotControlRule();
			Logger.getLogger().info("Process part:" + rule.getPartNameString());
			String scannedEngineSerialNo = getController().getState().getProduct().getPartList().get(0).getPartSerialNumber();
			Engine engine = getEngineDao().findByKey(scannedEngineSerialNo);
			getInstalledPart(productId.getProductId());
			installedPart.setPartSerialNumber(engine.getMissionSerialNo());
			getController().getFsm().partSnOk(installedPart);
			Logger.getLogger().info("EngineLoadMissionSnProcessor: Exit execute() ok");
			return true;			
		} catch (Exception ex) {
			if (ex.getMessage() != null)
				Logger.getLogger().error(ex.getMessage());		
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID, ex.getMessage() == null ? "" : ex.getMessage());
		} 
		Logger.getLogger().info("EngineLoadMissionSnProcessor: Exit execute() ng");
		return false;
	}

	public EngineDao getEngineDao() {
		if (engineDao == null) {
			engineDao = ServiceFactory.getDao(EngineDao.class);
		}
		return engineDao;
	}

	private void getInstalledPart(String productId){
		installedPart.setProductId(productId);
		installedPart.setPartName(getController().getCurrentLotControlRule().getPartNameString());
		installedPart.setPartId(getController().getCurrentLotControlRule().getParts().get(0).getId().getPartId());
		installedPart.setValidPartSerialNumber(true);
	}

	public void registerDeviceListener(DeviceListener listener) {
	}

}