/**
 * 
 */
package com.honda.galc.client.headless;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.client.device.plc.omron.FinsSocketPlcDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductPriorityPlanDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Nov 19, 2012
 */
public abstract class PlcDataReadyEventProcessorBase {
	
	private static final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE";
	
	protected Logger _logger;
	protected String _plcDeviceId;
	protected String _applicationId;
	protected IPlcSocketDevice _plcDevice;
	
	private FrameDao _frameDao = null;
	private BuildAttributeDao _buildAttributeDao = null;
	private ProductionLotDao _productionLotDao = null;
	private PreProductionLotDao _preProductionLotDao = null;
	private ProductStampingSequenceDao _stampingSeqDao = null;
	private DeviceFormatDao _deviceFormatDao = null;
	private ExpectedProductDao _expectedProductDao = null;
	private ProductPriorityPlanDao _productPriorityPlanDao = null;

	protected boolean trackProduct(String productId) {
		try {
			String productType = PropertyService.getProperty(getApplicationId(), PRODUCT_TYPE_KEY);	
			if(productType != null)	{
				getTrackingService().track(ProductType.getType(productType), productId, getApplicationId());
				getLogger().info("Tracking Successfully completed for product " + productId);
			} else {
				getLogger().error("Product Type is unknown for product " + productId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().info("Tracking was not Successful for product " + productId);
			return false;
		}
		return true;
	}
	
  	protected Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(getBean().getTerminalId() + "_" + getApplicationId());
			_logger.getLogContext().setApplicationInfoNeeded(true);
			_logger.getLogContext().setMultipleLine(false);
			_logger.getLogContext().setCenterLog(false);
		}
		_logger.getLogContext().setThreadName(getApplicationId() + "-" + Thread.currentThread().getName());
		return _logger;
	}

	protected IPlcSocketDevice getPlcDevice() {
		if (_plcDevice == null) {
			_plcDevice = (FinsSocketPlcDevice) DeviceManager.getInstance().getDevice(getPlcDeviceId());
			if (!_plcDevice.isInitialized()) {
				_plcDevice.activate();
			}
			getLogger().info("Found device " + _plcDevice.getId() + ": " +  _plcDevice.getHostName() + ":" + _plcDevice.getPort());
		}
		return _plcDevice;
	}
	
	public FrameDao getFrameDao() {
		if (_frameDao == null)
			_frameDao = ServiceFactory.getDao(FrameDao.class);
		
		return _frameDao;
	}
	
	public BuildAttributeDao getBuildAttributeDao() {
		if (_buildAttributeDao == null)
			_buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		
		return _buildAttributeDao;
	}
	
	public ProductionLotDao getProductionLotDao() {
		if (_productionLotDao == null)
			_productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		
		return _productionLotDao;
	}
	
	public PreProductionLotDao getPreProductionLotDao() {
		if (_preProductionLotDao == null)
			_preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		
		return _preProductionLotDao;
	}
	
	public ProductStampingSequenceDao getStampingSeqDao() {
		if (_stampingSeqDao == null)
			_stampingSeqDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		
		return _stampingSeqDao;
	}
	
	public DeviceFormatDao getDeviceFormatDao() {
		if (_deviceFormatDao == null)
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		
		return _deviceFormatDao;
	}

	public ExpectedProductDao getExpectedProductDao() {
		if (_expectedProductDao == null)
			_expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		
		return _expectedProductDao;
	}

	public ProductPriorityPlanDao getProductPriorityPlanDao() {
		if (_productPriorityPlanDao == null)
			_productPriorityPlanDao = ServiceFactory.getDao(ProductPriorityPlanDao.class);
		
		return _productPriorityPlanDao;
	}
	
	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
	
	public String getPlcDeviceId() {
		return _plcDeviceId;
	}

	public void setPlcDeviceId(String plcDeviceId) {
		_plcDeviceId = plcDeviceId;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}
	
	public PlcDataCollectionBean getBean() {
		return PlcDataCollectionController.getInstance(getApplicationId()).getBean();
	}
}
