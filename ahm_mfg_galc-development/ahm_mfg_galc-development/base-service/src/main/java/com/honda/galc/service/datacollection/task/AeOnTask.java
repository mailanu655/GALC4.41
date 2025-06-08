package com.honda.galc.service.datacollection.task;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.ProductionLotStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.util.ProductCheckType;

public class AeOnTask extends CollectorTask {

	public AeOnTask(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
		this.context = context;
	}

	@Override
	public void execute() {
		//Check to see if AE On was bypassed.  If it was, execute task
		if (context.get(ProductCheckType.AT_CHECK.getName()) == null ||  (Boolean)context.get(ProductCheckType.AT_CHECK.getName()) == false) {
			getLogger().info("Now performing AE On Status updates...");
			updateEngineStatuses();
		}
	}

	private void updateEngineStatuses() {
		String ein = context.get(TagNames.PRODUCT_ID.name()).toString();
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		Engine engineId = engineDao.findByKey(ein);
		
		if (engineId == null) {
			getLogger().error("Invalid engine number: " + ein);
			return;
		}
		
		ProductionLotDao prodLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		ProductionLot prodLot = prodLotDao.findByKey(engineId.getProductionLot());
		if (prodLot != null && prodLot.getLotStatus() == ProductionLotStatus.PROCESS_DEFAULT.getId()) {
			ProductionLotStatus prodLotStatus = ProductionLotStatus.PROCESS_IN;
			prodLot.setLotStatus(prodLotStatus.getId());
		} else {
			getLogger().info("Could not find Production Lot for engine " + ein);
		}
		
		PreProductionLotDao ppLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		PreProductionLot ppLot = ppLotDao.findByKey(engineId.getProductionLot());
		
		if (ppLot != null) {
			ppLot.setStampedCount(ppLot.getStampedCount() + 1);
			PreProductionLotSendStatus ppLotStatus = PreProductionLotSendStatus.INPROGRESS;
			if (ppLot.getStampedCount() >= ppLot.getLotSize()) {
				ppLotStatus = PreProductionLotSendStatus.DONE;
			}

			ppLot.setSendStatus(ppLotStatus);
			ppLotDao.save(ppLot);
			getLogger().info("Successfully updated send status to: " + ppLotStatus.getId() + " for production lot: " + engineId.getProductionLot());
		} else {
			getLogger().info("Could not find PreProduction Lot for engine " + ein);
		}
	}
}
