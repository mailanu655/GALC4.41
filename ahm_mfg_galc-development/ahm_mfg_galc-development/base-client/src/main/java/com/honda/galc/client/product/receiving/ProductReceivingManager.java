package com.honda.galc.client.product.receiving;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SnConverter;

/**
 * <h3>Class description</h3>
 * This class is responsible for handling received products.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class ProductReceivingManager {

	private ApplicationContext applicationContext;
	private ReceivedProductData data;
	private BroadcastService broadcastService;

	public String processReceivedProductId(String originalId, String format) throws ProductReceivingException {
		Logger.getLogger().info("Start processing received ID: " + originalId);
		String newId = null;
		try {
			Logger.getLogger().info("Converting ID using format: " + format);
			newId = convertProductId(originalId, format);
			Logger.getLogger().info("Finished conversion. New ID: " + newId);
		} catch (Exception e) {
			Logger.getLogger().error("Exception during ID conversion: " + e.getMessage());
			throw new ProductReceivingException("Error converting product ID. Please call help desk.");
		}

		try {
			saveResults(newId, originalId, format);
		} catch (ProductReceivingException exception) {
			Logger.getLogger().error("Unable to save conversion result: " + exception.getMessage());
			throw exception;
		} catch (Exception e) {
			Logger.getLogger().error("Exception occured when saving results: " + e.getMessage());
			throw new ProductReceivingException("Unable to save new product. Please call help desk.");
		}
		Logger.getLogger().info("Successfully converted: " + originalId);
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, newId);
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());

		getBroadcastService().broadcast(getApplicationContext().getProcessPointId(), dc);
		return newId;
	}

	private void saveResults(String newId, String originalId, String format) throws ProductReceivingException {
		try {
			Class<?> processorClass = Class.forName(processorClassName());
			ProductReceivingProcessor processor = (ProductReceivingProcessor) processorClass.newInstance();
			processor.setApplicationContext(getApplicationContext());
			processor.setData(getData());
			processor.saveProduct(newId);
			processor.saveBuildResult(newId, format, originalId);
			processor.track(newId);
		} catch (ProductReceivingException pre) {
			throw pre;
		} catch (Exception e) {
			throw new ProductReceivingException("Unable to save product/result. Please call help desk.");
		}
		Logger.getLogger().info("Finished saving block data: " + newId);
	}
	
	private String processorClassName() {
		StringBuilder builder = new StringBuilder();
		builder.append("com.honda.galc.client.product.receiving.");
		builder.append(getData().getProductType().getProductName());
		builder.append("ReceivingProcessor");
		return builder.toString();
	}
	
	public String convertProductId(String productId, String format) {
		return SnConverter.convert(productId, format);
	}

	public BroadcastService getBroadcastService() {
		if(broadcastService == null) {
			broadcastService = ServiceFactory.getService(BroadcastService.class);
		}
		return broadcastService;
	}
		
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ReceivedProductData getData() {
		return data;
	}

	public void setData(ReceivedProductData data) {
		this.data = data;
	}
}
