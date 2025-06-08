package com.honda.galc.oif.task;

import java.util.List;

import com.honda.galc.dao.product.ShippingTransactionDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ShippingTransaction;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
/**
 * 
 * @author vec15809
 *
 */
public class ShippingTransactionCheckTask extends OifAbstractTask implements IEventTaskExecutable{
    private static final String KEY = "KEY";
	private static final String AFF_OFF_DATE = "AFF_OFF_DATE";
	private static final String PRICE = "PRICE";
	public static final String PRODUCTION_DATE = "PRODUCTION_DATE";
	private ShippingTransactionDao dao;
    /**
     *TIME_FRAME_60A is the time agreed that the 60A should be received after 50A was sent
     *Print card will be triggered if the transaction did not confirmed within the above agreed time.
     *TIME_FRAME_60 in seconds
     */
    private String TIME_FRAME_60A = "TIME_FRAME_60A"; 
    private String PRINT_PROCESS_POINT_ID = "PRINT_PROCESS_POINT_ID";
    private char PRINTED = 'Y';
    private String SEND_LOCATION_FILTER="SEND_LOCATION_FILTER";
	public ShippingTransactionCheckTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		logger.info("start ShippingTransactionCheckTask");
		checkShippingTransaction();
		logger.info("completed ShippingTransactionCheckTask");
	}

	private void checkShippingTransaction() {
		//timeFrame60A in seconds
		Integer timeFrame60A = getPropertyInt(TIME_FRAME_60A);
		String[] sendLocations = getPropertyArray(SEND_LOCATION_FILTER);  
		String printProcessPointId = getProperty(PRINT_PROCESS_POINT_ID);
		List<ShippingTransaction> notConfirmedShippings = getDao().findAllNotConfirmedByCreateTimePassed(timeFrame60A);
		if(notConfirmedShippings == null || notConfirmedShippings.isEmpty()) return;
		
		BroadcastService service = ServiceFactory.getService(BroadcastService.class);
		for(ShippingTransaction shippingTransaction : notConfirmedShippings) {
			if(null != sendLocations && sendLocations.length > 0) {
				for(String location : sendLocations) {
					if(shippingTransaction.getSendLocation().equals(location))
						invokeBroadcast(printProcessPointId, service, shippingTransaction);
				}

			} else {
				invokeBroadcast(printProcessPointId, service, shippingTransaction);
			}
		}
	}

	private void invokeBroadcast(String printProcessPointId, BroadcastService service,
			ShippingTransaction shippingTransaction) {
		try{
			service.broadcast(printProcessPointId, prepareBroadcastData(shippingTransaction));
			logger.info("Broadcast shipping transaction to printer:" + prepareBroadcastData(shippingTransaction));
			shippingTransaction.setPrintedFlag(PRINTED);
			getDao().save(shippingTransaction);
		}catch(Exception e){
			logger.warn(e, "Failed to invoke broadcast service for shipping status:" + prepareBroadcastData(shippingTransaction));
		}
	}

	private DataContainer prepareBroadcastData(ShippingTransaction shippingTransaction) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, shippingTransaction.getVin());
		dc.put(TagNames.SEND_LOCATION.name(), shippingTransaction.getSendLocation());
		dc.put(TagNames.SEND_LOCATION.name()+shippingTransaction.getSendLocation(), true);
		dc.put(DataContainerTag.MODEL, shippingTransaction.getSalesModelCode());
		dc.put(DataContainerTag.TYPE, shippingTransaction.getSalesModelTypeCode());
		dc.put(DataContainerTag.OPTION, shippingTransaction.getSalesModelOptionCode());
		dc.put(DataContainerTag.MODEL, shippingTransaction.getSalesModelCode());
		dc.put(DataContainerTag.COLOR_CODE, shippingTransaction.getSalesModelColorCode());
		dc.put(DataContainerTag.ENGINE_SERIAL_NO, shippingTransaction.getEngineNumber());
		dc.put(DataContainerTag.MODEL, shippingTransaction.getSalesModelCode());
		dc.put(KEY, shippingTransaction.getKeyNumber());
		dc.put(AFF_OFF_DATE, shippingTransaction.getAfOffDate());
		dc.put(PRICE,shippingTransaction.getPriceString());
		dc.put(PRODUCTION_DATE,shippingTransaction.getProductionDate());
		dc.put(DataContainerTag.KD_LOT, shippingTransaction.getKdLotLineNumber()+shippingTransaction.getKdLotDate()+
				shippingTransaction.getKdLotSequenceNumber() + shippingTransaction.getKdLotSuffix());
		dc.put(DataContainerTag.PRODUCTION_LOT, shippingTransaction.getLineNumber()+shippingTransaction.getProductionDate()+
				shippingTransaction.getProductionSequenceNumber() +shippingTransaction.getProductionSuffix());
	
		
		return dc;
	}

	public ShippingTransactionDao getDao() {
		if(dao == null)
			dao = ServiceFactory.getDao(ShippingTransactionDao.class);
		return dao;
	}

	
}
