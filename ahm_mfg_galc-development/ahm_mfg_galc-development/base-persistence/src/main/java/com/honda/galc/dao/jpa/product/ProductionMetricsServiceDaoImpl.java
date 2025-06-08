package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.JpaEntityManager;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.ProductionMetricsService;
import com.honda.galc.service.ServiceFactory;

public class ProductionMetricsServiceDaoImpl extends JpaEntityManager implements
		ProductionMetricsService {

	private ProcessPoint processPoint;
	@Autowired
	private GpcsDivisionDao gpcsDivisionDao;
	
	private String trackingProcessPoint;
	private Map<String, String[]> map;
	private void defineQuarters() {
		map = new HashMap<String, String[]>();
		map.put("1STQ", new String[] { "PRE1", "PRE2", "1STQ", "BRK1" });
		map.put("2NDQ", new String[] { "2NDQ", "LUNCH" });
		map.put("3RDQ", new String[] { "3RDQ", "BRK2" });
		map.put("4THQ", new String[] { "4THQ", "POST1", "POST2" });
	}
	
	public List<Map<String, String>> getProductionMetrics(
			String productType, String trackingPpt) {
		List<Map<String, String>> metrics = new ArrayList<Map<String, String>>();
		this.trackingProcessPoint = trackingPpt;

		defineQuarters();
		long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

		processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(
				trackingProcessPoint);
		GpcsDivision gpcsDivision = gpcsDivisionDao.findByKey(processPoint
				.getDivisionId());

		DailyDepartmentSchedule currentPeriod = ServiceFactory.getDao(DailyDepartmentScheduleDao.class)
				.findByActualTime(gpcsDivision.getGpcsLineNo(),
						gpcsDivision.getGpcsProcessLocation(),
						gpcsDivision.getGpcsPlantCode(),
						new Timestamp(currentTimeInMillis));
		
		if (null == currentPeriod) return metrics;
		List<DailyDepartmentSchedule> currentPeriodLst = ServiceFactory.getDao(DailyDepartmentScheduleDao.class)
				.findAllByLineAndShift(gpcsDivision.getGpcsLineNo(),
						gpcsDivision.getGpcsProcessLocation(),
						gpcsDivision.getGpcsPlantCode(),
						currentPeriod.getId().getProductionDate(),
						currentPeriod.getId().getShift()
						);
		
		String[] quarter = findQuarter(currentPeriod.getPeriodLabel());

		int quarterActual = 0;
		int quarterPlan = 0;
		int shiftActual = 0;
		int shiftPlan = 0;
		
		Timestamp quarterStartTime = null;
		Timestamp quarterEndTime = null;
		Timestamp shiftStartTime = null;

		long shiftTotalMillis = 0;
		long shiftPartMillis = 0;
		boolean countPartMillis = true;

		for(DailyDepartmentSchedule schedule : currentPeriodLst) {
			if (schedule.getPeriodLabel().equals(quarter[0])) {
				quarterPlan = schedule.getCapacity();
				quarterStartTime = schedule.getStartTimestamp();
				quarterEndTime = schedule.getEndTimestamp();
			}
			if (schedule.getPeriodLabel().equals("1STQ")) {
				shiftStartTime = schedule.getStartTimestamp();
			}
			if (schedule.getPlan().equals("Y")) {
				shiftPlan = shiftPlan + schedule.getCapacity();
				shiftTotalMillis = shiftTotalMillis + (schedule.getEndTimestamp().getTime() - schedule.getStartTimestamp().getTime()) ;
				if (countPartMillis) {
					if (currentTimeInMillis>schedule.getEndTimestamp().getTime())  
						shiftPartMillis = shiftPartMillis+(schedule.getEndTimestamp().getTime() - schedule.getStartTimestamp().getTime());
					else {
						if (currentTimeInMillis>schedule.getStartTimestamp().getTime()) shiftPartMillis = shiftPartMillis+(currentTimeInMillis - schedule.getStartTimestamp().getTime());
						countPartMillis = false;
					}
				}
			}
		}
		
		//Calculate the quarterPlan
		//in time within the period start and end time frame
		long quarterPartMillis = currentTimeInMillis - quarterStartTime.getTime();
		long quarterTotalMillis = quarterEndTime.getTime() - quarterStartTime.getTime();
		
		float percent = 1;
		if (quarterTotalMillis != 0 ) percent = (float)quarterPartMillis / quarterTotalMillis;
		if (currentTimeInMillis>quarterEndTime.getTime()) percent = 1;
		quarterPlan = Math.round(quarterPlan*percent);
		
		//Calculate the shiftPlan
		if (shiftTotalMillis != 0) percent = (float)shiftPartMillis / shiftTotalMillis;
		else percent = 1;
		shiftPlan = Math.round(shiftPlan*percent);

		quarterActual = ServiceFactory.getDao(ProductResultDao.class).findAlreadyProcessedProdAtPP(trackingProcessPoint,quarterStartTime.toString());
		shiftActual = ServiceFactory.getDao(ProductResultDao.class).findAlreadyProcessedProdAtPP(trackingProcessPoint,shiftStartTime.toString());

		Map<String, String> shiftRecord = new java.util.HashMap<String, String>();
		shiftRecord.put("plan", String.valueOf(shiftPlan));
		shiftRecord.put("actual", String.valueOf(shiftActual));

		Map<String, String> quarterRecord = new java.util.HashMap<String, String>();
		quarterRecord.put("plan", String.valueOf(quarterPlan));
		quarterRecord.put("actual", String.valueOf(quarterActual));

		metrics.add(shiftRecord);
		metrics.add(quarterRecord);

		return metrics;
	}

// returns the current "quarter" and its starting period
	private String[] findQuarter(String periodLabel) {
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			for (String period : entry.getValue()) {
				if (period.equals(periodLabel)) {
					return new String[] { entry.getKey(), entry.getValue()[0] };
				}
			}
		}
		return new String[] {"",""};
	}
}
