package com.honda.galc.service.mqmessaging;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductSpecDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.NGTMQMessagingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.mq.MqDevice;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * 
 * <h3>NGTMessagingServiceImpl Class description</h3>
 * <p>
 * NGTMessagingServiceImpl description
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author Haohua Xie<br>
 *         Feb 25, 2014
 * 
 * 
 */
public class NGTMessagingServiceImpl implements NGTMQMessagingService {
	private Logger logger = Logger.getLogger(this.getClass().getName());;

	public void send(DataContainer dc) {
		String ppid = (String) dc.get(DataContainerTag.PROCESS_POINT_ID);
		ppid = ppid == null ? "" : ppid;
		String cid = (String) dc.get(DataContainerTag.CLIENT_ID);
		prepareData(ppid, dc);
		sendToMqQueue(dc, cid);
	}

	protected void sendToMqQueue(DataContainer dc, String cid) {
		DeviceDao deviceDao = ServiceFactory.getDao(DeviceDao.class);
		Device device = deviceDao.findByKey(cid);
		MqDevice mqdevice = createDevice();
		DataContainer outDc = calculateAttributes(dc);
		mqdevice.sendToMqQueue(device, outDc);
	}

	protected String findProductType(String processPointId) {
		ApplicationPropertyBean propertyBean = PropertyService.getPropertyBean(
				ApplicationPropertyBean.class, processPointId);
		return propertyBean.getProductType();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void prepareData(String processPointId, DataContainer dc) {
		if (!dc.containsKey(DataContainerTag.PRODUCT_ID)) {
			return;
		}
		if (!dc.containsKey(DataContainerTag.PRODUCT_TYPE)) {
			dc.put(DataContainerTag.PRODUCT_TYPE,
					findProductType(processPointId));
		}
		if (!dc.containsKey(DataContainerTag.PRODUCT)) {
			ProductDao productDao = ProductTypeUtil.getProductDao((String) dc
					.get(DataContainerTag.PRODUCT_TYPE));
			Product product = (Product) productDao.findByKey(dc
					.getString(DataContainerTag.PRODUCT_ID));
			dc.put(DataContainerTag.PRODUCT, product);
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE,
					product.getProductSpecCode());
			ProductSpecDao productSpecDao = (ProductSpecDao)ProductTypeUtil
					.getProductSpecDao((String) dc
							.get(DataContainerTag.PRODUCT_TYPE));
			ProductSpec productSpec = (ProductSpec)productSpecDao.findByKey(product
					.getProductSpecCode());
			dc.put(DataContainerTag.PRODUCT_SPEC, productSpec);
		}
		if (!dc.containsKey(DataContainerTag.PROCESS_POINT_ID)) {
			dc.put(DataContainerTag.PROCESS_POINT_ID, processPointId);
		}
	}

	protected DataContainer calculateAttributes(DataContainer dc) {
		return new AttributeConvertor(logger).convertFromDeviceDataFormat(dc
				.get(DataContainerTag.CLIENT_ID).toString(), dc);
	}

	protected MqDevice createDevice() {
		return new MqDevice(logger);
	}

}
