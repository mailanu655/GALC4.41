package com.honda.galc.oif.task.gds;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ProductCountTask Class description</h3>
 * <p> ProductCountTask description </p>
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
 * @author Jeffray Huang<br>
 * Nov 6, 2012
 *
 *
 */
public class ProductCountTask extends AbstractDataCalculationTask{

	public ProductCountTask(String name) {
		super(name);
	}

	public void processCalculation() {
		fetchCurentDepartmentSchedules();
		
		processPlanProduction();

		List<Counter> allCounts = findAllCounts();

		processActualProduction(allCounts);
		
		processBanlance(allCounts);
		
		deriveProductionMode();
	}
	
	private void processPlanProduction() {
		
		for(Entry<String,List<DailyDepartmentSchedule>> entry : currentDepartmentSchedules.entrySet()) {
			String deptName = entry.getKey();
			int totalCapacity = getTotalCapacity(entry.getValue());
			int totalCapacityOn = getTotalCapacityOn(entry.getValue());
			int dailyProgress = 0;
			int dailyProgressOn = 0;
			
			Map<String,List<DailyDepartmentSchedule>> shiftSchedules = getShiftSchedules(entry.getValue());
			
			for(Entry<String,List<DailyDepartmentSchedule>> pair : shiftSchedules.entrySet()){
				List<DailyDepartmentSchedule> schedules = pair.getValue();
				String shift = pair.getKey();
				int shiftCapacity = getTotalCapacity(schedules);
				int shiftCapacityOn = getTotalCapacityOn(schedules);
				
				
				List<Integer> progress = getShiftCapacityProgress(schedules);
				updateValue(deptName + "\\Shift " + shift + "\\Capacity",shiftCapacity);
				updateValue(deptName + "\\Shift " + shift + "\\Plan Production",progress.get(0));
				updateValue(deptName + "\\Shift " + shift + "\\Capacity On",shiftCapacityOn);
				updateValue(deptName + "\\Shift " + shift + "\\Plan Production On",progress.get(1));
				if(isCurrentShift(schedules)) {
					updateValue(deptName + "\\Current Shift\\Capacity",shiftCapacity);
					updateValue(deptName + "\\Current Shift\\Plan Production",progress.get(0));
					updateValue(deptName + "\\Current Shift\\Capacity On",shiftCapacityOn);
					updateValue(deptName + "\\Current Shift\\Plan Production On",progress.get(1));
				}
				dailyProgress +=progress.get(0);
				dailyProgressOn +=progress.get(1);
			}
			
			updateValue(deptName + "\\Capacity",totalCapacity);
			updateValue(deptName + "\\Plan Production",dailyProgress);
			updateValue(deptName + "\\Capacity On",totalCapacityOn);
			updateValue(deptName + "\\Plan Production On",dailyProgressOn);
		}
	}

	private List<Integer> getShiftCapacityProgress(List<DailyDepartmentSchedule> schedules) {
		Integer shiftCapacityProgress = 0;
		Integer shiftCapacityOnProgress = 0;
		List<Integer> result = new ArrayList<Integer>();
		for(DailyDepartmentSchedule dds : schedules){
			if(dds.isPeriodPast(getTestTimestamp())){
				shiftCapacityProgress += dds.getCapacity();
				shiftCapacityOnProgress += dds.getCapacityOn();
			}
			else if(dds.isPeriodCurrent(getTestTimestamp())){
				long duration = Math.abs(dds.getEndTimestamp().getTime() - dds.getStartTimestamp().getTime());
				long timeSpan = dds.getDuration(getTestTimestamp());
				
				if(duration > 0 && timeSpan > 0){
					shiftCapacityProgress += (int)(dds.getCapacity() * timeSpan /duration);
					shiftCapacityOnProgress += (int)(dds.getCapacityOn() * timeSpan /duration);
				}
				break;
			}
		}
		
		result.add(shiftCapacityProgress);
		result.add(shiftCapacityOnProgress);
		
		return result;
	}

	private void processActualProduction(List<Counter> allCounts){
		List<List<List<String>>> allProcessPoints = getAllProcessPoints("PROD_COUNT_");
		
		for(List<List<String>> processPointLists :allProcessPoints){
			for(List<String> processPointIds :processPointLists){
				
				String combinedProcessPointName = getCombinedProcessPointName(processPointIds);
				String deptName = getDepartmentName(processPointIds);
				
				Map<String,List<DailyDepartmentSchedule>> shiftSchedules = getShiftSchedules(currentDepartmentSchedules.get(deptName));
				int dailyCount = 0;
				for(Entry<String,List<DailyDepartmentSchedule>> pair : shiftSchedules.entrySet()){
					List<DailyDepartmentSchedule> schedules = pair.getValue();
					String shift = pair.getKey();
					int count = 0;
					for(String ppId : processPointIds) {
						count += getCount(allCounts, ppId,shift);
					}
					updateValue(deptName + "\\Shift " + shift + "\\" + combinedProcessPointName + "\\Actual Production", count);
	    			if(isCurrentShift(schedules)) 
	    				updateValue(deptName + "\\Current Shift\\" + combinedProcessPointName + "\\Actual Production", count);
	    			
	    			dailyCount += count;
				}
				
				updateValue(deptName + "\\" + combinedProcessPointName + "\\Actual Production", dailyCount);
			}
		}
	}
	
	private void processBanlance(List<Counter> allCounts) {
		List<List<List<String>>> allProcessPoints = getAllProcessPoints("BALANCE_");
		
		for(List<List<String>> processPointLists : allProcessPoints) {
			if(processPointLists.size() <= 0){
				// log error 
				continue;
			}
			List<String> inProcessPointIds = processPointLists.get(0);
			List<String> outProcessPointIds = processPointLists.get(1);
			String deptName = getDepartmentName(inProcessPointIds);

			int countIn = getCount(allCounts,inProcessPointIds);
			int countOut = getCount(allCounts,outProcessPointIds);
			updateValue(deptName + "\\BALANCE",countIn - countOut);
		}
		
	}
	
	//Send production schedule to ALC Data Server
	//0 = Non Production -- break, lunch time
	//1 = Production -- production time, before shift starting 60 seconds and non production day
	//2 = Power Saver -- between shifts
	private void deriveProductionMode() {
		String minutesProperty = PropertyService.getProperty(componentId,"PRE-SHIFT_DISPLAY_LEAD_TIME");
		List<String> minutes = PropertyService.getPropertyList(componentId,"PRE-SHIFT_DISPLAY_LEAD_TIME",Delimiter.SEMI_COLON);
		updateValue("General\\Schedule Modes\\Available Pre-Shift Lead Time Minutes", minutesProperty);
		
		for(String dept : currentDepartmentSchedules.keySet()) {
			for(String minute : minutes) {
				
				updateValue(dept + "\\Schedule Modes\\Lead Time " + 
	        		      minute + " Minutes", getDeptScheduleMode(dept, Integer.parseInt(minute)));
			}
		}	
	}
	
	/*0 = Non Production -- break, lunch time and between shifts(scheduled non production time)
	  1 = Production -- production time, before shift starting 60 seconds and non production day
	  2 = Power Saver -- between production dates, no schedule existing*/
  public int getDeptScheduleMode(String dept, int preShiftLeadMinute)
  {
  	List<DailyDepartmentSchedule> deptSchedules = currentDepartmentSchedules.get(dept);
      Timestamp deptStartTime = getTestTimestamp();
      Timestamp deptEndTime = getTestTimestamp();

      for (DailyDepartmentSchedule tmpSched : deptSchedules) {     	
      	if (tmpSched.isPlan() && (deptStartTime == null || deptStartTime.after(tmpSched.getStartTimestamp()))) 
      		deptStartTime = tmpSched.getStartTimestamp();
   
      	if (tmpSched.isPlan() && (deptEndTime == null || deptEndTime.before(tmpSched.getEndTimestamp()))) 
      		deptEndTime = tmpSched.getEndTimestamp();
  
          if(tmpSched.isInBetween(getTestTimestamp())) {
              if (tmpSched.isPlan()) return 1;
              else return 0;
          }
      } 
      
      if (getTestTimestamp().before(deptStartTime) && (deptStartTime.getTime() - getTestTimestamp().getTime() <= preShiftLeadMinute * 60000))
  			return 1; 			
      if (getTestTimestamp().after(deptEndTime) && (getTestTimestamp().getTime() - deptEndTime.getTime() <= preShiftLeadMinute * 60000))
  			return 1;
      return 2;
}
	private int getCount(List<Counter> allCounts, String processPointId, String shift) {
		for(Counter counter :allCounts) {
			if(counter.getId().getProcessPointId().equals(processPointId) && counter.getId().getShift().equals(shift)) return counter.getPassingCounter();
		}
		return 0;
	}
	
	private int getCount(List<Counter> allCounts, List<String> processPointIds) {
		int count = 0;
		for(Counter counter :allCounts) {
			if(processPointIds.contains(counter.getId().getProcessPointId())) count += counter.getPassingCounter();
		}
		return count;
	}	
	
	private int getTotalCapacity(List<DailyDepartmentSchedule> schedules) {
		int capacity = 0;
		for(DailyDepartmentSchedule schedule: schedules) {
			capacity += schedule.getCapacity();
		}
		return capacity;
	}
	
	private int getTotalCapacityOn(List<DailyDepartmentSchedule> schedules) {
		int capacity = 0;
		for(DailyDepartmentSchedule schedule: schedules) {
			capacity += schedule.getCapacityOn();
		}
		return capacity;
	}
	
	private boolean isCurrentShift(List<DailyDepartmentSchedule> schedules){
		for(DailyDepartmentSchedule schedule: schedules) {
			if(schedule.isInBetween(getTestTimestamp())) return true;
		}
		return false;
	}
	
	private Map<String,List<DailyDepartmentSchedule>> getShiftSchedules(List<DailyDepartmentSchedule> departmentSchedules){
		Map<String,List<DailyDepartmentSchedule>> shiftSchedules = new LinkedHashMap<String, List<DailyDepartmentSchedule>>();
		List<DailyDepartmentSchedule> schedules = null;
		for(DailyDepartmentSchedule schedule: departmentSchedules) {
			if(schedules != null && !schedule.getId().getShift().equals(schedules.get(0).getId().getShift())){
				shiftSchedules.put(schedules.get(0).getId().getShift(), schedules);
			};
			if(schedules == null || !schedule.getId().getShift().equals(schedules.get(0).getId().getShift())){
				schedules = new ArrayList<DailyDepartmentSchedule>();
			}				
			schedules.add(schedule);
		}
		if(schedules != null && !schedules.isEmpty())
			shiftSchedules.put(schedules.get(0).getId().getShift(), schedules);
		return shiftSchedules;
	}
	
	private List<Counter> findAllCounts() {
		List<Counter> allCounters = new ArrayList<Counter>();
		for(DailyDepartmentSchedule dds: currentSchedules.values()){
			List<Counter> divisionAll = getDao(CounterDao.class).findAllByDivisionIdAndProductionDate(dds.getId().getProductionDate(), dds.getId().getProcessLocation());
			if(divisionAll != null)
				allCounters.addAll(divisionAll);
		}
		
		
		return allCounters;
	}

}
