package com.honda.galc.service.broadcast.servertask;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductHoldService;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Paul Chou
 * @created Sept 23, 2019
 */
/**
 * @author Subu Kathiresan
 * @date May 12, 2022
 */
public class SpecCheckHoldTask implements IServerTask {
	private ExpectedProductDao expectedProductDao;
	private FrameDao frameDao;
	private ProductHoldService holdService;
	private ProductCheckPropertyBean productCheckPropertyBean;

	@Override
	public DataContainer execute(DataContainer dc) {
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		
		if (StringUtils.isBlank(productId)) {
			throw new TaskException("ProductId can not be blank.");
		}
		
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		Frame product = (Frame) dc.get(DataContainerTag.PRODUCT);
	
		ExpectedProduct previousVin = getExpectedProductDao().findByKey(processPointId);
		if(previousVin == null) return dc;
		
		Frame previousVehicle = getFrameDao().findByKey(previousVin.getProductId());
		if(previousVehicle == null) {
			Logger.getLogger().warn("Failed to find previous vehicle:", previousVin.getProductId(), "  from database.");
			return dc;
		}
		
		if(product == null) {
			Logger.getLogger().warn("Current product is null!");
			return dc;
		}

		if(!product.getProductSpecCode().equals(previousVehicle.getProductSpecCode())) {
			dc = putProductOnHold(product, previousVehicle, processPointId);
		}
		
		return dc;
	}
	
	private DataContainer putProductOnHold(Frame product, Frame previousVehicle, String processPointId) {
		Logger.getLogger().info("Spec Changed from current:", product.getProductId(), ":", product.getProductSpecCode(), " previous:", previousVehicle.getProductId(), ":",previousVehicle.getProductSpecCode());
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), product.getProductId());
		dc.put(TagNames.PROCESS_POINT_ID.name(), processPointId);
		dc.put(TagNames.PRODUCT.name(), product);
		dc.put(TagNames.HOLD_REASON.name(), getProductCheckPropertyBean(processPointId).getSpecCheckHoldReason());
		dc.put(TagNames.ASSOCIATE_ID.name(), processPointId);
		dc.put(TagNames.HOLD_SOURCE.name(), 0);
		dc.put(TagNames.HOLD_RESULT_TYPE.name(), getProductCheckPropertyBean(processPointId).getSpecCheckHoldType());
		dc.put(TagNames.QSR_HOLD.name(), true);
				
		getHoldService().execute(dc);
		Logger.getLogger().info("productId: " + product.getProductId() + " put on spec change hold at processPoint: " + processPointId);
		return dc;
	}

	public ProductHoldService getHoldService() {
		if(holdService == null)
			holdService = ServiceFactory.getService(ProductHoldService.class);
		return holdService;
	}
	
	public ProductCheckPropertyBean getProductCheckPropertyBean(String processPointId) {
		if(productCheckPropertyBean == null)
			productCheckPropertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointId);
		return productCheckPropertyBean;
	}

	public ExpectedProductDao getExpectedProductDao() {
		if(expectedProductDao == null)
			expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		return expectedProductDao;
	}

	public FrameDao getFrameDao() {
		if(frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);
		return frameDao;
	}
}
