package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Time;
import java.util.List;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gpp304Dto;
import com.honda.galc.service.msip.property.inbound.BaseMsipCalendarPropertyBean;
/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp304Handler extends ScheduleBasedMsipHandler<BaseMsipCalendarPropertyBean, Gpp304Dto> {
	
	public Gpp304Handler() {}

	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current plan codes 
	 * <p>
	 */
	protected void processDepartmentScheduleByPlanCode(List<Gpp304Dto> dtoList) {
		DailyDepartmentScheduleDao dailyDeptSchedDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		for(Gpp304Dto dto : dtoList) {
		    /*String lineNo,String processLocation, String plantCode, Date productDate,String shift*/
			List<DailyDepartmentSchedule> scheduleList = dailyDeptSchedDao.findAllByLineAndShift(
					dto.getLineNo(), dto.getProcessLocation(), dto.getPlantCode(), 
					dto.getProductionDate(), dto.getShift());
			calculateAndSaveLostCapacity(dto.getLostCapacity(), scheduleList, dto.getStartTime(), dto.getEndTime(), dto.getOnOffFlag());		
		}
	}

	/**
	 * Calculate the lost capacity for each production period and assign it to the production period in which the down time is 
	 * going to take place
	 * @param the capacity number lost due to the downtime
	 * @param resultset that contains the production periods to which the lost capacity will be assgined
	 * @param starting down time
	 * @param ending down time
	 * @param onOffFlag
	 * 
	 * @return array that contains production shedule records that have the lost capacity number decremented from the production
	 * capacity number.
	 */

	private boolean calculateAndSaveLostCapacity(
		int lossCapacity,
		List<DailyDepartmentSchedule> scheduleList,
		Time pStartDownTime,
		Time pEndDownTime,
		int onOffFlag) {
		getLogger().info("**** Inside the getting capacity");
		try {
			int affectedCapacityTotal = lossCapacity;
			int affectedCapacityOffTotal = lossCapacity;
			getLogger().info("**** looping through the Shift Record");
			int averageAffectedCapacity =
				(int) (affectedCapacityTotal / scheduleList.size());
			int averageAffectedCapacityOff =
				(int) (affectedCapacityOffTotal / scheduleList.size());
			for(DailyDepartmentSchedule schedule : scheduleList) {
				//		int totalSubstractionCapacityOff = 0;
				int intCapacity = schedule.getCapacityOn();
				int intCapacityOff = schedule.getCapacity();
				int setCapOn, setCapOff;
				if (intCapacity <= averageAffectedCapacity) {
					affectedCapacityTotal = affectedCapacityTotal - intCapacity;
					setCapOn = 0;
				} else {
					affectedCapacityTotal =
						affectedCapacityTotal - averageAffectedCapacity;
					setCapOn =	new Integer(intCapacity - averageAffectedCapacity);
				}
				if (intCapacityOff <= averageAffectedCapacityOff) {
					affectedCapacityOffTotal =
						affectedCapacityOffTotal - intCapacityOff;
					setCapOff = 0;
				} else {
					affectedCapacityOffTotal =
						affectedCapacityOffTotal - averageAffectedCapacityOff;
					setCapOff = new Integer(
							intCapacityOff - averageAffectedCapacityOff);
				}
				if(onOffFlag == 1 || onOffFlag == 2) {
					schedule.setCapacityOn(setCapOn);
				} else if(onOffFlag == 2){
					schedule.setCapacity(setCapOff);
				}
				if (affectedCapacityTotal < averageAffectedCapacity) {
					averageAffectedCapacity = affectedCapacityTotal;
				}

				if (affectedCapacityOffTotal < averageAffectedCapacityOff) {
					averageAffectedCapacityOff = affectedCapacityOffTotal;
				}
				getDao(DailyDepartmentScheduleDao.class).update(schedule);
			}
			return true;
		} catch (Exception e) {
			getLogger().error(
				"SQLException raised when calculating the downtime. Exception : "
					+ e.getMessage());
			return false;
		}
	}
	
	public boolean execute(List<Gpp304Dto> dtoList) {
		try {
			processDepartmentScheduleByPlanCode(dtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
}
