package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.util.SortedArrayList;

public class ScheduleDisplayController extends ScheduleClientController {

	public ScheduleDisplayController(ScheduleMainPanel panel) {
		super(panel);
	}

	@Override
	public void retrievePreProductionLots() {
		clearCurrentProductId();
		String planCode = properties.getPlanCode();

		upcomingLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllUpcomingLotsByPlanCode(planCode));
		processedLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllPreviousLotsByPlanCode(planCode, properties.getProcessedRowCount()));

		Collections.reverse(processedLots);

		List<PreProductionLot> onHoldLots = getDao(PreProductionLotDao.class).findAllOnHoldLotsByPlanCode(planCode);
		List<PreProductionLot> currentLots = new SortedArrayList<PreProductionLot>("getSequence");

		Iterator<PreProductionLot> iterator = upcomingLots.iterator();
		PreProductionLot currentLot = null;
		
		int offset = properties.getScheduleDisplayOffset();
		PreProductionLot previousLot = upcomingLots.size() == 0 ? null : upcomingLots.get(0);
		while (previousLot != null && offset > 0) {
			final int remaining = previousLot.getLotSize() - previousLot.getStampedCount();
			if (offset < remaining) {
				previousLot.setStampedCount(previousLot.getStampedCount() + offset);
				offset = 0;
			} else {
				upcomingLots.remove(previousLot);
				previousLot.setStampedCount(previousLot.getLotSize());
				previousLot.setSendStatus(PreProductionLotSendStatus.SENT);
				processedLots.remove(0);
				processedLots.add(previousLot);
				previousLot = upcomingLots.size() == 0 ? null : upcomingLots.get(0);
				offset -= remaining;
			}
		}

		if(getProperties().isMoveByKdLot()) {

			while(iterator.hasNext() && previousLot != null){
				currentLot = iterator.next();
				if(currentLot.isSameKdLot(previousLot)){
					currentLots.add(currentLot);
					iterator.remove();
					previousLot = currentLot;

				} else 
					previousLot = null;
			}
		} else if(previousLot != null){
			currentLots.add(previousLot);
			upcomingLots.remove(0);
		}

		if(!isProcessedProductOrLot()){
			publishDataChanged(prepareData(processedLots,processedPanelProperties),SchedulingEventType.PROCESSED_ORDER_CHANGED);
		} else {
			retrieveProcessedProducts();
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct(currentLots, upcomingLots);
			}
		}

		publishDataChanged(prepareData(currentLots,currentPanelProperties),SchedulingEventType.CURRENT_ORDER_CHANGED);
		publishDataChanged(prepareData(upcomingLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
		publishDataChanged(prepareData(onHoldLots,onHoldPanelProperties),SchedulingEventType.ON_HOLD_ORDER_CHANGED);

		lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestampByPlanCode(properties.getPlanCode());
	}
	
	protected void completeLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		// not required
	}
}
