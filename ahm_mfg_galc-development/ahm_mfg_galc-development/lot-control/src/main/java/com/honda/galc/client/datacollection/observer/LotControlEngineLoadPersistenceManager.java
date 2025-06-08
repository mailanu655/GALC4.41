package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 13, 2012
 */
public class LotControlEngineLoadPersistenceManager extends LotControlFramePersistenceManager{
	public static final String ENGINE = "ENGINE";
	public static final int STATUS_OK = 1;
	
	protected FrameDao frameDao = null;
	protected EngineDao engineDao = null;
	protected InProcessProductDao inProcessProductDao = null;
	
	public LotControlEngineLoadPersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	protected void saveCollectedData(ProcessProduct product) {
		try {
			super.saveCollectedData(product);
			String vin = DataCollectionController.getInstance().getState().getProduct().getProductId();
			Engine engine = getEngine();
			saveFrame(vin, engine);
			saveAndTrackEngine(vin, engine);
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("LotControlEngineLoadPersistenceManager:: Could not save collected data " + ex.getMessage());
		}
	}

	/**
	 * updates the frame with engine data
	 * 
	 * @param vin
	 * @param engine
	 * @return
	 */
	protected boolean saveFrame(String vin, Engine engine) {
		try {
			//assign engine to frame and track frame
			Frame frame = getFrameDao().findByKey(vin); 
			frame.setEngineSerialNo(engine.getProductId());
			frame.setMissionSerialNo(engine.getMissionSerialNo());
			frame.setEngineStatus(STATUS_OK);
			getFrameDao().update(frame);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("LotControlEngineLoadPersistenceManager:: Could not save frame " + vin + " with engine " + engine.getProductId());
		}
		return false;
	}
	
	/**
	 * updates the engine and tracks engine and frame
	 * 
	 * @param vin
	 * @param engine
	 * @return
	 */
	protected boolean saveAndTrackEngine(String vin, Engine engine) {
		try {
			//assign frame to engine and track engine (frame is also tracked as part of engine tracking!)
			engine.setVin(vin);
			engine.setMissionStatus(STATUS_OK);
			getEngineDao().update(engine);
			
			ProductHistory productHistory = ProductTypeUtil.createProductHistory(engine.getProductId(), context.getProcessPointId(), ProductType.ENGINE);
			productHistory.setAssociateNo(context.getUserId());
			
			getTrackingService().track(ProductType.ENGINE, productHistory);
			
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("LotControlEngineLoadPersistenceManager:: Could not save/track engine " + engine.getProductId() + " for vin " + vin);
		}
		return false;
	}

	/**
	 * retrieves the engine from the installed part list
	 * @return
	 */
	private InstalledPart findEngineFromInstalledPartList() {
		for(InstalledPart part : DataCollectionController.getInstance().getState().getProduct().getPartList()){
			if(part.getId().getPartName().equals(ENGINE))
				Logger.getLogger().debug("Engine retrieved from installed parts list: " + part.getPartSerialNumber());
				return part;
		}
		return null;
	}
	
	/**
	 * finds and returns engine based on the scanned engine serial number
	 * @return
	 */
	private Engine getEngine() {
		return getEngineDao().findByKey(findEngineFromInstalledPartList().getPartSerialNumber());
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
	
	public InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null) {
			inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		}
		return inProcessProductDao;
	}
	
	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
}
