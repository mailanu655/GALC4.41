package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.ProductId;

public class NoActionReqdProcessor extends ProcessorBase
implements IProductIdProcessor {
	
	protected ProductBean product;
	public static final String MESSAGE_ID = "PRODUCT"; 
	
	public NoActionReqdProcessor(ClientContext context) {
		super(context);
		init();
	}

	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().debug("NoActionReqdProcessor : Enter NoActionReqdProductId");
		
		try {
			getController().getFsm().partNoAction();
			Logger.getLogger().debug("NoActionReqdProcessor : Exit NoActionReqdProductId ok");
			return true;
		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			product.setValidProductId(false);
			getController().getFsm().productIdNg(product, te.getTaskName(), te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			getController().getFsm().error(new Message(MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(new Message("MSG01", e.getCause().toString()));
		} catch (Throwable t){
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(new Message("MSG01", t.getCause().toString()));
		} 

		Logger.getLogger().debug("NoActionReqdProcessor : Exit NoActionReqdProductId ng");
		return false;
	}

	public void init() {
		product = new ProductBean();
	}

	public void registerDeviceListener(DeviceListener listener) {
	}

}
