package com.honda.galc.client.datacollection.observer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>LotControlTimeoutManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>Manager that trigger the UI to refresh if the lot control process is timeout </p>
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
 * @author YX
 * 2018.09.21
 *
 */

public class LotControlProductTimeoutManager extends DataCollectionObserverBase implements IProductTimeoutObserver {
	private ClientContext context;
	private LotControlPropertyBean propertyBean;
	private boolean isTimeoutTaskRun = false;
	
	public LotControlProductTimeoutManager(ClientContext context) {
		super();
		this.context = context;
		this.propertyBean = this.context.getProperty();
	}
	
	
	
	@Override
	public void checkNextExpectedProduct(final DataCollectionState state) {
		final DataCollectionController dcc = DataCollectionController.getInstance(context.getAppContext().getApplicationId());

		if(StringUtils.isBlank(state.getExpectedProductId()) && !isTimeoutTaskRun){
			Thread t = new Thread(){
				public void run() {
					isTimeoutTaskRun = true;
					try {
						String product = state.getExpectedProductId();
						while(StringUtils.isBlank(product)){
							Logger.getLogger().info("checking for next expected Product");
							 product = context.getDbManager().getNextExpectedProductId(state.getProductId());
							if (StringUtils.isNotBlank(product )){
								Logger.getLogger().info("Next Expected Product found - "+product);
								state.setExpectedProductId(product);
								saveExpectedProduct(product);
								ProductId request = new ProductId(product);
								dcc.received(request);
								context.getDbManager().getExpectedProductManger().updateProductSequence((ProcessProduct)state);
							}else{
								Logger.getLogger().info("No Next Expected Product found");
								dcc.received(new Request("error", new Object[]{new Message("No Next Product found")}, new Class[]{Message.class}));
								Thread.sleep(propertyBean.getNextExpectedProductTimeOut());
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						isTimeoutTaskRun=false;
					}
				}
			};
			
			
			t.start();
		}else{
			Logger.getLogger().info("LotControlProductTimeoutManager already running");
		}
	}

	void saveExpectedProduct(String productId){
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		ExpectedProduct expectedProduct = expectedProductDao.findByKey(context.getProcessPointId());
		expectedProduct.setProductId(productId);
		expectedProductDao.save(expectedProduct);
	}
}
