package com.honda.galc.client.product;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.linesidemonitor.property.LineSideMonitorPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class ExpectedEmptyProductManager extends ExpectedProductManager {
	
	protected LineSideMonitorPropertyBean lsmProperty;
	private static final String zeroPadding = "0";
	
	public ExpectedEmptyProductManager(ClientContext context) {
		super(context);
	}
	
	protected LineSideMonitorPropertyBean getLsmProperty() {
		if (lsmProperty == null) {
			String ppid = context.getProcessPointId();
			lsmProperty = PropertyService.getPropertyBean(LineSideMonitorPropertyBean.class, ppid);
		}
		return lsmProperty;
	}
	
	@Override
	public String getNextExpectedProductId(String productId) {
		String nextProductId ="";
		if (StringUtils.isBlank(productId) && !context.getProperty().isCheckExpectedProductFromPreviousLine()) {
			productId = expectedProdId.getLastProcessedProduct();
		}		
		do {
			List<Frame> productCarrierFrame = getDao(FrameDao.class).findAllByProductCarrier(getLsmProperty().getProductSequenceProcessPoint(), productId, 0, 2);
			for (int i = 1; i < 3; i++) {
				nextProductId = productCarrierFrame.get(i).getProductId();
				if (!isEmptyCarrier(nextProductId)) {
					break;
				} else {
					productId = nextProductId;
					if (context.isOnLine() && isBroadcastConfigured())
						invokeBroadcastService(nextProductId);
				}
			}

		} while (isEmptyCarrier(nextProductId));	
		return nextProductId;	
	}
	
	@Override
	public String findPreviousProductId(String productId) {

		String previousProductId = "";
		List<Frame> productCarrierFrame = getDao(FrameDao.class).findAllByProductCarrier(
				getLsmProperty().getProductSequenceProcessPoint(), productId,1,0);
		//check property from property table
		String maxLength = PropertyService.getPropertyBean(SystemPropertyBean.class, getLsmProperty().getProductSequenceProcessPoint()).getAFNoSequenceNumber();
		
		if (productCarrierFrame.isEmpty()) {
			return null;
		} else {
			Frame product = getDao(FrameDao.class).findByKey(productId);
			int index = productCarrierFrame.indexOf(product);
			if (productCarrierFrame.get(index - 1) != null) {
				String afOnSequence = String.valueOf(productCarrierFrame.get(index - 1).getAfOnSequenceNumber());
				if(!StringUtil.isNullOrEmpty(afOnSequence)) {
					//default padding
					int afONSeqNolenth = afOnSequence.length()+1;
					if(!StringUtil.isNullOrEmpty(maxLength))
						afONSeqNolenth = Integer.parseInt(maxLength);
					//Left padding with zero
					afOnSequence = StringUtils.leftPad(afOnSequence, afONSeqNolenth, zeroPadding);
					//search PRODUCT_CARRIER_TBX with padded carrier id
					ProductCarrier productCarrier = getDao(ProductCarrierDao.class).findByCarrierId(afOnSequence);
					if(productCarrier !=null)
						previousProductId = productCarrier.getId().getProductId();
				}
				
				return previousProductId;
			} else {
				return null;
			}
		}
	}
	
	protected boolean isBroadcastConfigured() {
		ProcessPoint processPoint = context.getAppContext().getProcessPoint();
		if(processPoint == null) return false;
		List<BroadcastDestination> destinations = 
			ServiceFactory.getDao(BroadcastDestinationDao.class)
			.findAllByProcessPointId(processPoint.getProcessPointId());
		return !destinations.isEmpty();
	}

	protected void invokeBroadcastService(String nextProductId) {
		ProcessPoint processPoint = context.getAppContext().getProcessPoint();
		if (processPoint == null) {
			Logger.getLogger().warn("WARN:", " invalid process point - skipped broadcast service.");
			return;
		}		
		if (nextProductId != null) {
			try {
				List<BroadcastDestination > broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(context.getProcessPointId());
				
				for (BroadcastDestination broadcastDestination : broadcastDestinations) {
					if (!broadcastDestination.getDestinationType().equals(DestinationType.DEVICE_WISE)) continue;
					if(broadcastDestination.getArgument().equalsIgnoreCase("EMPTY")){
						DataContainer dc = new DefaultDataContainer();
						dc.put(DataContainerTag.PRODUCT_ID, nextProductId);
						dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
						getService(BroadcastService.class).broadcast(broadcastDestination, context.getProcessPointId(), dc);
					}
				}				
			} catch (Exception e) {
				Logger.getLogger().warn(e, "Failed to invoke broadcast service");
			}
		}
	}

	protected boolean isEmptyCarrier(String productId) {
		if(productId.startsWith(getEmptyCarrier())) return true;
		return false;
	}
	
	private String getEmptyCarrier() {
		return context.getProperty().getEmptyCarrierName();
	}

}
