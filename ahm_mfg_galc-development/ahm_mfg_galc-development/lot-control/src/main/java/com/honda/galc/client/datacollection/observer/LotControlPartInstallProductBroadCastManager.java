package com.honda.galc.client.datacollection.observer;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class LotControlPartInstallProductBroadCastManager extends DataCollectionObserverBase implements IPartInstallObserver {

	protected final ClientContext context;
	private final List<BroadcastDestination> broadcastDestinations;
	private final String processPointId;

	public LotControlPartInstallProductBroadCastManager(ClientContext context) {
		super();
		this.context = context;
		this.processPointId = context.getProcessPointId();
		this.broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId); // device must have auto enabled false to prevent duplicate broadcast on DataCollection complete
	}

	public void partInstall(ProcessPart part) {
		String destList = "";
		String partName = part.getCurrentLotControlRule().getPartName().getPartName();
		try {
			if (broadcastDestinations.isEmpty())
				return;
			for (BroadcastDestination destination : broadcastDestinations) {
				if (destination.getArgument().trim().equalsIgnoreCase(partName.trim())) {
					int seqNo = destination.getSequenceNumber();
					ProductBean productBean = part.getProduct();
					BaseProduct product = productBean.getBaseProduct();
					
					if(product == null){
						ProductDao productDao = ProductTypeUtil.getProductDao(StringUtils.isNotBlank(context.getProperty().getSubAssyProductType())?context.getProperty().getSubAssyProductType():context.getProperty().getProductType());
						product = productDao.findBySn(part.getProductId());
					}
					
					Logger.getLogger(processPointId).info("Broadcast Services started for " + destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")");
					if (product != null)
						invokeBroadcastService(product,seqNo);
					else
						invokeBroadcastService(productBean,seqNo);
					destList = StringUtils.isEmpty(destList) ? destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")" 
							: destList + ", " + destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")";						
				}
			}
			if (!StringUtils.isEmpty(destList)) part.message(new Message("MSG01", "Broadcast sent to destination(s): " + destList, MessageType.INFO)); 

		} catch (Exception e) {
			part.error(new Message("MSG01", "failed to broadcast Product to deviceWise."));
			Logger.getLogger("LotControlProductBroadCastDeviceManager").error(e);
		}	
	}

	private void invokeBroadcastService(final BaseProduct product, final int seqNo) {
		Thread broadcastThread = new Thread(new Runnable() {
			public void run() {
				DataContainer dc = new DefaultDataContainer();
				dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
				dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
				dc.put(DataContainerTag.PRODUCT, product);
				dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
				dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
				getService(BroadcastService.class).broadcast(processPointId, seqNo, dc);
			}
		});
		broadcastThread.start();
	}

	private void invokeBroadcastService(final ProductBean productBean, final int seqNo) {
		Thread broadcastThread = new Thread(new Runnable() {
			public void run() {
				DataContainer dc = new DefaultDataContainer();
				dc.put(DataContainerTag.PRODUCT_ID, productBean.getProductId());
				dc.put(DataContainerTag.PRODUCT_TYPE, context.getProperty().getProductType());
				dc.put(DataContainerTag.PRODUCT, null);
				dc.put(DataContainerTag.PRODUCT_SPEC_CODE, productBean.getProductSpec());
				dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
				getService(BroadcastService.class).broadcast(processPointId, seqNo, dc);
			}
		});
		broadcastThread.start();
	}

	@Override
	public void handleDataCollectionCancel(ProcessPart part) {
		   if (broadcastDestinations.isEmpty())
	           return;
	      for (BroadcastDestination destination : broadcastDestinations) {
	                  if (destination.getArgument().trim().equalsIgnoreCase(Action.CANCEL.name())) {
	                          invokeBroadcastService(part.getProduct().getBaseProduct(),destination.getSequenceNumber());
	                    }
	      }
		
	}

}