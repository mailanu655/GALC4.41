package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CarrierId;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ProductIdRefresh;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CarrierUtil;

/**
 * 
 * @author vec15809
 *
 */
public class CarrierIdProcessor extends FrameVinProcessor {

	public CarrierIdProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
		// TODO Auto-generated constructor stub
	}

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new ProductIdRefresh());
		list.add(new CarrierId());
		return list;
	}
	
	public synchronized boolean execute(CarrierId carrierId) {

		Logger.getLogger().debug("CarrierIdProcessor : Enter");
		try {
			Logger.getLogger().info(PROCESS_PRODUCT + carrierId.getCarrierId());
			ProductId productId = CarrierUtil.findProductIdByCarrier(context.getProperty().getTrackingArea(), carrierId);
			
			if(StringUtils.isEmpty(productId.getProductId())) {
				Logger.getLogger().warn("No product found on carrier: ", carrierId.getCarrierId());
				Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(carrierId.getCarrierId());
				if(frame != null)
					productId.setProductId(frame.getProductId());
			}
			
			return execute(productId);
			
		} catch (Throwable t){
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(new Message("MSG01", t.getCause().toString()));
		} 

		return false;

	}

}
