package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.util.SortedArrayList;

/**
 * <h3>Class description</h3>
 * Schedule client controller. 
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
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class MultiLineScheduleClientController extends ScheduleClientController {

	public MultiLineScheduleClientController(ScheduleMainPanel panel) {
		super(panel);
	}

	@Override
	public void retrievePreProductionLots() {

		String processLocation = Arrays.asList(properties.getProcessLocation()).get(0);
		String planCode = Arrays.asList(properties.getPlanCode()).get(0);
		
		boolean retrieveByPlanCode = properties.isRetrieveByPlanCode();
		List<PreProductionLot> preProductionLots;
		
		if(!retrieveByPlanCode) {
			preProductionLots = getDao(PreProductionLotDao.class).findAllForProcessLocation(processLocation);
		}else {
			preProductionLots = getDao(PreProductionLotDao.class).findAllForProcessLocationAndPlanCode(processLocation,planCode);
		}
		List<PreProductionLot> processedLots = new ArrayList<PreProductionLot>();
		List<PreProductionLot> upcomingLots = new ArrayList<PreProductionLot>();
		List<PreProductionLot> onHoldLots = new SortedArrayList<PreProductionLot>("getProductionLot");

		// add lots to the processed lots until an unsent lot is found
		boolean unsentFound = false;
		for(PreProductionLot lot : preProductionLots) {
			if(lot.getProcessLocation().equals(processLocation)) {
				if(lot.getHoldStatus() == 0) {
					onHoldLots.add(lot);
				} else {
					if (!unsentFound) {
						if(lot.getSendStatus() == PreProductionLotSendStatus.WAITING 
								|| lot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS)
							unsentFound = true;
						else
							processedLots.add(lot);
					}
					if (unsentFound) {
						upcomingLots.add(lot);
					}
				}
			}
		}

		// put the processed and upcoming lots into one list
		preProductionLots.clear();
		{
			int processedCount = properties.getProcessedRowCount();
			if (processedCount < processedLots.size()) {
				preProductionLots.addAll(processedLots.subList(processedLots.size() - processedCount, processedLots.size()));
			} else {
				preProductionLots.addAll(processedLots);
			}
		}
		preProductionLots.addAll(upcomingLots);

		// publish the changes to the list of lots (all lots stored in the upcoming lots panel)
		publishDataChanged(prepareData(preProductionLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
		publishDataChanged(prepareData(onHoldLots,onHoldPanelProperties),SchedulingEventType.ON_HOLD_ORDER_CHANGED);

		lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestampByProcessLocation(processLocation);

	}


}
