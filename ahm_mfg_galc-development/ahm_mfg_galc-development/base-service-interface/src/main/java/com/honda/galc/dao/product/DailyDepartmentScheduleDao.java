package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>DailyDepartmentScheduleDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DailyDepartmentScheduleDao description </p>
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
 * @author Paul Chou
 * Aug 25, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
//see com.honda.galc.dao.jpa.product.DailyDepartmentScheduleDaoImpl
public interface DailyDepartmentScheduleDao 
	extends IDaoService<DailyDepartmentSchedule, DailyDepartmentScheduleId>, ScheduleDao {
	
	/**
	 * Find schedule by current time not support transaction
	 * @param lineNo
	 * @param processLocation
	 * @param plantCode
	 * @param currentTime
	 * @return
	 */
	public List<DailyDepartmentSchedule> findAllById(DailyDepartmentScheduleId id);
	public DailyDepartmentSchedule findByActualTime(String lineNo, String processLocation, String plantCode, Timestamp actualTimestamp);
	public DailyDepartmentSchedule find(String divisionId, Timestamp timestamp);
	
	public List<DailyDepartmentSchedule> findAllByProcessPoint(ProcessPoint processPoint, Date productDate);
	public List<DailyDepartmentSchedule> findAllByProcessPointAndTimestamp(ProcessPoint processPoint, Timestamp timestamp);
	public List<DailyDepartmentSchedule> findAllByProcessPointAndPeriodLabels(ProcessPoint processPoint, List<String> periodLabels, Date productDate);
	public List<DailyDepartmentSchedule> findAllByDivisionAndPlan(String divisionId, String plan, Date productDate);
	public List<DailyDepartmentSchedule> findAllByLineAndShift(String lineNo,String processLocation, String plantCode, Date productDate,String shift);
	public List<Object[]> findNextProdDate(String prodDate, String processLocation, String isWork);
	public List<Object[]> getProductionDay(String prodDate, String processLocation);
	public Timestamp findMinStartTimestamp(String prodDate, String processLocation);
	public Object[] findStartTimeNextDayInfo(String processLocation);
	public Timestamp findMaxEndTimestamp(String prodDate, String processLocation);
	public List<Object[]> findEndTimeNextDayInfo(String processLocation);
    public List<DailyDepartmentSchedule> getAllFutureByProductionDate(Date productionDate);
    public List<DailyDepartmentSchedule> getAllByProductionDateAndLocation(Date productionDate, String processLocation);
    public List<DailyDepartmentSchedule> getAllByProductionDateLocationAndLineNo(Date productionDate, String processLocation, String LineNo);
    public List<DailyDepartmentSchedule> getScheduleInfo(String plantCode, Date productionDate, String processLocation, String shift);
	public List<DailyDepartmentSchedule> getAllFutureByPlantAndProductionDate(String plantCode, Date productionDate);
	public List<DailyDepartmentSchedule> getAllFutureByPlanCodeAndProductionDate(String planCode, Date productionDate);
	public List<String> findTimeGaps(Date startDate);
	public java.util.Date getNextProductionDate(java.util.Date t);
	public DailyDepartmentSchedule findByProductionDateShiftAndPeriodLabel(Date productionDate, String shift, String period,String processLocation);
	public DailyDepartmentSchedule findByProcessLocation(Timestamp actualTs, String processLoc);
	public DailyDepartmentSchedule getFirstPeriodTimestamp(String lineNo, String plantNo, String processLocation, String productionDate);
	public List<DailyDepartmentSchedule> findAllByProductionDate(Date productionDate);
	public DailyDepartmentSchedule findShiftFirstPeriod(DailyDepartmentSchedule currentSchedule);
	public List<DailyDepartmentSchedule> findUptoCurrentTime(String lineNo, String processLocation, String plantCode, Date prodDate, Timestamp actualTimestamp);
	
	/**
	 * derive daily department schedules from TGA3011,TGA3021,TGA3031 and TGA3061
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DailyDepartmentSchedule> deriveDailyDepartmentSchedulesFromGPCS(Date startDate,Date endDate);

	public List<DailyDepartmentSchedule> generateSchedulesFromStandard(Date startDate,Date endDate);

	public int deleteSchedules(String plantCode,String lineNo,String processLocation,Date productionDate);
	
	/**
	 * delete all schedules on and after production dates
	 * @param productionDate
	 * @return
	 */
	public int deleteSchedules(Date productionDate);
	public List<DailyDepartmentSchedule> findAllByCurrentTimeStamp(Timestamp currentTimestamp);
	public List<String> findAllShifts();
	public List<String> findAllShiftsByDateAndProcessLocation(Date productionDate, String processLocation);
	public List<DailyDepartmentSchedule> findByCurDateProcLocAndQuart(String lineNo, String processLocation, List<String> quarter);
	public List<Date> findAllByOffSet(Integer offset, Date parentLotDate, String processLocation);
}
