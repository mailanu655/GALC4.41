package com.honda.galc.dao.jpa.product;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>DailyDepartmentScheduleDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DailyDepartmentScheduleDaoImpl description </p>
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
public class DailyDepartmentScheduleDaoImpl extends BaseDaoImpl<DailyDepartmentSchedule,DailyDepartmentScheduleId> implements DailyDepartmentScheduleDao{

	@Autowired
	private GpcsDivisionDao gpcsDivisionDao;

	final String FIND_BY_ACTUAL_TIMESTAMP = "select * from galadm.gal226tbx where LINE_NO = ?1 AND PROCESS_LOCATION = ?2 AND PLANT_CODE = ?3 AND START_TIMESTAMP <= ?4 ORDER BY START_TIMESTAMP DESC FETCH FIRST ROW ONLY";
	
    final String FIND_PROCESS_INFO = "SELECT DISTINCT PLANT_CODE,LINE_NO,PROCESS_LOCATION FROM GALADM.GAL226TBX";
    final String FIND_SCHEDULE_INFO = "select PROCESS_LOCATION,PERIOD,SHIFT,PERIOD_LABEL,TYPE,PLAN,START_TIME,END_TIME,NEXT_DAY,CAPACITY,CAPACITY_ON from GAL226TBX WHERE PLANT_CODE = ?1 AND LINE_NO = ?2 AND PROCESS_LOCATION = ?3 AND PRODUCTION_DATE = ?4 order by period";
    final String UPDATE_SCHEDULE = "UPDATE GAL226TBX SET PLAN = ?1, START_TIME = ?2, END_TIME = ?3, NEXT_DAY = ?4, CAPACITY = ?5, CAPACITY_ON = ?6 WHERE PLANT_CODE = ?7 AND LINE_NO = ?8 AND PROCESS_LOCATION = ?9 AND PRODUCTION_DATE = ?10 AND PERIOD = ?11";
    final String FIND_NEXT_PROD_DATE="SELECT MIN(PRODUCTION_DATE) FROM GAL226TBX WHERE PRODUCTION_DATE > ?1 AND PROCESS_LOCATION = ?2 AND ISWORK = ?3";
    final String GET_PRODUCTION_DAY="SELECT MIN(PRODUCTION_DATE) FROM GAL226TBX WHERE  START_TIMESTAMP <=?1  AND END_TIMESTAMP>=?2 AND PROCESS_LOCATION = ?3"; 
	
	final String FIND_MIN_START_TIMESTAMP = "SELECT MIN(A.START_TIMESTAMP) FROM GAL226TBX A LEFT JOIN GAL238TBX B ON A.PLANT_CODE=B.GPCS_PLANT_CODE AND A.LINE_NO=B.GPCS_LINE_NO AND A.PROCESS_LOCATION=B.GPCS_PROCESS_LOCATION WHERE B.DIVISION_ID IN (SELECT DIVISION_ID FROM GAL128TBX WHERE DIVISION_NAME = ?2) AND A.PRODUCTION_DATE = ?1 FOR READ ONLY";
	final String FIND_MAX_END_TIMESTAMP = "SELECT MAX(A.END_TIMESTAMP) FROM GAL226TBX A LEFT JOIN GAL238TBX B ON A.PLANT_CODE=B.GPCS_PLANT_CODE AND A.LINE_NO=B.GPCS_LINE_NO AND A.PROCESS_LOCATION=B.GPCS_PROCESS_LOCATION WHERE B.DIVISION_ID IN (SELECT DIVISION_ID FROM GAL128TBX WHERE DIVISION_NAME = ?2) AND A.PRODUCTION_DATE = ?1 FOR READ ONLY";
	final String FIND_START_TIME_NEXT_DAY_INFO = "SELECT A.START_TIME, A.NEXT_DAY FROM GAL226TBX A LEFT JOIN GAL238TBX B ON A.PLANT_CODE=B.GPCS_PLANT_CODE AND A.LINE_NO=B.GPCS_LINE_NO AND A.PROCESS_LOCATION=B.GPCS_PROCESS_LOCATION WHERE B.DIVISION_ID IN (SELECT DIVISION_ID FROM GAL128TBX WHERE DIVISION_NAME = ?1) AND START_TIMESTAMP = (SELECT MIN(START_TIMESTAMP) FROM GAL226TBX WHERE PRODUCTION_DATE = (SELECT MAX(PRODUCTION_DATE) FROM GAL226TBX WHERE B.DIVISION_ID IN (SELECT DIVISION_ID FROM GAL128TBX WHERE DIVISION_NAME = ?1))) FOR READ ONLY";
	final String FIND_END_TIME_NEXT_DAY_INFO = "SELECT A.END_TIME, A.NEXT_DAY FROM GAL226TBX A LEFT JOIN GAL238TBX B ON A.PLANT_CODE=B.GPCS_PLANT_CODE AND A.LINE_NO=B.GPCS_LINE_NO AND A.PROCESS_LOCATION=B.GPCS_PROCESS_LOCATION WHERE B.DIVISION_ID IN (SELECT DIVISION_ID FROM GAL128TBX WHERE DIVISION_NAME = ?1) AND END_TIMESTAMP = (SELECT MAX(END_TIMESTAMP) FROM GAL226TBX WHERE PRODUCTION_DATE = (SELECT MAX(PRODUCTION_DATE) FROM GAL226TBX WHERE B.DIVISION_ID IN (SELECT DIVISION_ID FROM GAL128TBX WHERE DIVISION_NAME = ?1))) FOR READ ONLY";
	
	final String GET_FUTURE_BY_PRODUCTION_DATE = "SELECT a FROM DailyDepartmentSchedule a WHERE a.id.plantCode = :plantCode AND a.id.productionDate >= :productionDate";
	final String GET_FUTURE_BY_PLAN_CODE = "SELECT a FROM DailyDepartmentSchedule a WHERE a.planCode = :planCode AND a.id.productionDate >= :productionDate";

	private static final String FIND_TIME_GAPS = 
		"SELECT t1.LINE_NO, t1.PROCESS_LOCATION, t1.PLANT_CODE, t1.PRODUCTION_DATE, t1.SHIFT, " +
		"t1.PERIOD, t2.PERIOD, t1.END_TIME, t2.START_TIME, t1.NEXT_DAY, " +
		"t1.END_TIMESTAMP, t2.START_TIMESTAMP " +
		"FROM GAL226TBX t1 JOIN GAL226TBX t2 " +
		"ON t1.PROCESS_LOCATION = t2.PROCESS_LOCATION " +
		"AND t1.PRODUCTION_DATE = t2.PRODUCTION_DATE AND t1.PERIOD+1 = t2.PERIOD " +
		"WHERE t1.Production_date > ?1 AND t1.END_TIMESTAMP + 1 MICROSECONDS != t2.START_TIMESTAMP " +
		"ORDER BY t1.PLAN_CODE, t1.LINE_NO, t1.PROCESS_LOCATION, t1.PRODUCTION_DATE, t1.PERIOD";		

	private static final String SELECT_NEXT_PRODUCTION_DAY = 
			"select min(production_date) from galadm.gal226tbx where production_date > ?1 and iswork = 'Y'";
	
	private static final String FIND_BY_PRODDATE_PROCESS_LOC = 
			"select distinct d from DailyDepartmentSchedule d, ProcessPoint p, GpcsDivision g "
			+ " where d.startTimestamp <= :actualTs1 and d.endTimestamp >= :actualTs2 "
			+ 	" and p.processPointId = :pp "
			+	" and g.gpcsProcessLocation = d.id.processLocation and g.divisionId = p.divisionId ";
	
	private static final String FIND_FIRST_PERIOD = "select * from gal226tbx d " +
													"where d.LINE_NO = ?1 and d.PLANT_CODE = ?2 AND d.PROCESS_LOCATION = ?3 AND d.PRODUCTION_DATE >= ?4 " + 
													"order by d.PRODUCTION_DATE, d.PERIOD, d.START_TIME";

	final String FIND_UPTO_CURRENT_TIME = "SELECT d FROM DailyDepartmentSchedule d where d.id.lineNo = :lineNo AND d.id.processLocation = :processLocation AND d.id.plantCode = :plantCode AND d.id.productionDate = :productionDate AND d.startTimestamp <= :startTime";

	private static final String FIND_BY_PROCESS_POINT_AND_PERIOD_LABELS =
			"SELECT d FROM DailyDepartmentSchedule d" + 
			" WHERE d.id.lineNo = :lineNo" +
			" AND d.id.processLocation = :processLocation" +
			" AND d.id.plantCode = :plantCode" +
			" AND d.id.productionDate = :productionDate" +
			" AND d.periodLabel in (:periodLabels)" +
			" ORDER BY d.id.period ASC";
	
   private final static String FIND_ALL_BY_DATE = "SELECT * FROM GALADM.GAL226TBX WHERE PRODUCTION_DATE =?1 order by LINE_NO,PROCESS_LOCATION,PRODUCTION_DATE,PERIOD";

   private final static String FIND_ALL_BY_OFFSET = "Select distinct PRODUCTION_DATE from gal226tbx where PRODUCTION_DATE < ?1 and ISWORK= 'Y' and PROCESS_LOCATION=?3 order by PRODUCTION_DATE desc limit ?2";
   
	private static final String FIND_BY_DIVISION_AND_PLAN =
			"SELECT d FROM DailyDepartmentSchedule d" + 
			" WHERE d.id.lineNo = :lineNo" +
			" AND d.id.processLocation = :processLocation" +
			" AND d.id.plantCode = :plantCode" +
			" AND d.id.productionDate = :productionDate" +
			" AND d.plan = :plan" +
			" ORDER BY d.id.period ASC";
	
	private static final String FIND_ALL_SCHEDULES =
		"select 'HCM' PLANT_CODE, ss.PLAN_CODE PLAN_CODE, ss.line_number LINE_NO, "+
		"				ss.plan_proc_loc PROCESS_LOCATION_GPS, "+
		"				case "+
		"					when ss.plan_code = 'HCM 034DCHO' then 'LP' "+
		"					when ss.plan_code = 'HCM 034DCBO' then 'HP' "+
		"					when ss.plan_code = 'HCM 034MCHO' then 'MH' "+
		"					when ss.plan_code = 'HCM 034MCBO' then 'MB' "+
		"					else ss.plan_proc_loc "+
		"				end PROCESS_LOCATION, "+
		"				ss.DAY_OF_WEEK DAY_OF_WEEK, "+
		"			    dec(ss.PERIOD) PERIOD, ss.SHIFT_CODE SHIFT, ss.LABEL_NAME PERIOD_LABEL, ss.WORK_TYPE TYPE, "+
		"    			ac.WEEK_NUMBER WEEK_NO, "+
		"			    ss.WORK_PLAN_FLAG PLAN, ss.WORK_START_TIME START_TIME, ss.WORK_END_TIME END_TIME, "+
		"			    case "+
		"			    	 when ss.NEXT_DAY = 'Y' then 1 "+
		"			         when ss.NEXT_DAY = 'P' then 0 "+
		"			         else 0 "+
		"			    end NEXT_DAY, "+
		"			    (select cc.WORK_FLAG from TGA3011_TBX cc where cc.on_off_flag = '1' "+
		"			       and cc.PRODUCTION_DATE = date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) "+
		"			       and cc.SHIFT_CODE = ss.SHIFT_CODE "+
		"				   and ( cc.plan_code = case when cc.plan_code = 'HCM 034AE01' then trim(ss.plan_code) || '01' else ss.plan_code end ) "+
		"				   and ss.line_number = cc.line_number) ISWORK, "+
		"			    date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) PRODUCTION_DATE, "+
		"			    timestamp(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2), ss.WORK_START_TIME) START_TIMESTAMP, "+
		"			    timestamp(date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)), ss.WORK_END_TIME) END_TIMESTAMP, "+
		"			    coalesce((select cc.capacity from TGA3011_TBX cc where cc.on_off_flag = '1' "+
		"			         and cc.PRODUCTION_DATE = date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) "+
		"			         and cc.SHIFT_CODE = ss.SHIFT_CODE "+
		"				   	 and ( cc.plan_code = case when cc.plan_code = 'HCM 034AE01' then trim(ss.plan_code) || '01' else ss.plan_code end ) "+
		"					 and ss.line_number = cc.line_number), 0) CAPACITY_ON, "+
		"			    coalesce((select cc.capacity from TGA3011_TBX cc where cc.on_off_flag = '2' "+
		"			         and cc.PRODUCTION_DATE = date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) "+
		"			         and cc.SHIFT_CODE = ss.SHIFT_CODE "+
		"					 and cc.plan_code = ss.plan_code "+
		"					 and ss.line_number = cc.line_number), 0) CAPACITY "+
		"			from TGA3061_TBX ac "+
		"			join TGA3021_TBX ss on ss.day_of_week = ac.day_of_week "+
		"			where "+
		"				( "+
		"				ss.plan_proc_loc = 'AE' "+
		"				or ss.plan_code = 'HCM 034DCHO' "+
		"				or ss.plan_code = 'HCM 034DCBO' "+
		"				or ss.plan_code = 'HCM 034MCHO' "+
		"				or ss.plan_code = 'HCM 034MCBO' "+
		"				) "+
		"				and ss.line_number = '03' "+
		"			    and date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) "+
		"				between ?1 AND ?2 "+
		"			    and ss.valid_date =(select max(valid_date) from galadm.hcm_tga3021 where plan_code =ss.plan_code and DAY_OF_WEEK =ss.day_of_week and valid_date <= date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)))"+
		"			    and not exists (select * from TGA3031_TBX ds where date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) = ds.production_date and ds.plan_code = ss.plan_code) "+
		"		union ( "+
		"		select 'HCM' PLANT_CODE, ds.PLAN_CODE PLAN_CODE, ds.line_number LINE_NO, "+
		"				ds.plan_proc_loc PROCESS_LOCATION_GPS, "+
		"				case "+
		"					when ds.plan_code = 'HCM 034DCHO' then 'LP' "+
		"					when ds.plan_code = 'HCM 034DCBO' then 'HP' "+
		"					when ds.plan_code = 'HCM 034MCHO' then 'MH' "+
		"					when ds.plan_code = 'HCM 034MCBO' then 'MB' "+
		"					else ds.plan_proc_loc  "+
		"				end PROCESS_LOCATION, "+
		"				ucase(substr(dayname(ds.PRODUCTION_DATE),1,3)) DAY_OF_WEEK, "+
		"			    dec(ds.PERIOD) PERIOD, ds.SHIFT_CODE SHIFT, ds.LABEL_NAME PERIOD_LABEL, ds.WORK_TYPE TYPE, "+
		"    			ac.WEEK_NUMBER WEEK_NO, "+
		"			    ds.WORK_PLAN_FLAG PLAN, ds.WORK_START_TIME START_TIME, ds.WORK_END_TIME END_TIME, "+
		"			    case "+
		"			    	 when ds.NEXT_DAY = 'Y' then 1 "+
		"			         when ds.NEXT_DAY = 'P' then 0 "+
		"			         else 0 "+
		"			    end NEXT_DAY, "+
		"			    (select cc.WORK_FLAG from TGA3011_TBX cc where cc.on_off_flag = '1' and cc.PRODUCTION_DATE = ds.PRODUCTION_DATE and cc.SHIFT_CODE = ds.SHIFT_CODE "+
		"				    and ( cc.plan_code = case when cc.plan_code = 'HCM 034AE01' then trim(ds.plan_code) || '01' else ds.plan_code end ) "+
		"					and ds.line_number = cc.line_number) ISWORK, "+
		"			    ds.PRODUCTION_DATE PRODUCTION_DATE, "+
		"			    timestamp(ds.PRODUCTION_DATE, ds.WORK_START_TIME) START_TIMESTAMP, "+
		"			    timestamp(ds.PRODUCTION_DATE, ds.WORK_END_TIME) END_TIMESTAMP, "+
		"			    coalesce((select cc.capacity from TGA3011_TBX cc where cc.on_off_flag = '1' and cc.PRODUCTION_DATE = ds.PRODUCTION_DATE and cc.SHIFT_CODE = ds.SHIFT_CODE "+
		"					and ( cc.plan_code = case when cc.plan_code = 'HCM 034AE01' then trim(ds.plan_code) || '01' else ds.plan_code end ) "+
		"					and ds.line_number = cc.line_number), 0) CAPACITY_ON, "+
		"			    coalesce((select cc.capacity from TGA3011_TBX cc where cc.on_off_flag = '2' and cc.PRODUCTION_DATE = ds.PRODUCTION_DATE and cc.SHIFT_CODE = ds.SHIFT_CODE "+
		"					and cc.plan_code = ds.plan_code "+
		"					and ds.line_number = cc.line_number), 0) CAPACITY "+
		"			from TGA3061_TBX ac "+
		"			join TGA3031_TBX ds on date(substr(ac.calendar_date,1,4)||'-'||substr(ac.calendar_date,5,2)||'-'||substr(ac.calendar_date,7,2)) = ds.PRODUCTION_DATE "+
		"			where "+
		"			( "+
		"				ds.plan_proc_loc = 'AE' "+
		"				or ds.plan_code = 'HCM 034DCHO' "+
		"				or ds.plan_code = 'HCM 034DCBO' "+
		"				or ds.plan_code = 'HCM 034MCHO' "+
		"				or ds.plan_code = 'HCM 034MCBO' "+
		"			) "+
		"			and ds.line_number = '03' "+
		"		    and ds.PRODUCTION_DATE between ?1 AND ?2 "+
		"		) "+
		"		order by PROCESS_LOCATION, PRODUCTION_DATE,PERIOD "+
		"		with ur for read only ";

	private static final String GENERATE_SCHEDULES = 
    	"SELECT ss.LINE_NO,ss.PROCESS_LOCATION,ss.PLANT_CODE,SS.DAY_OF_WEEK,"+
        "DATE(SUBSTR(ac.CALENDAR_DATE,1,4)||'-'||SUBSTR(ac.CALENDAR_DATE,5,2)||'-'||SUBSTR(ac.CALENDAR_DATE,7,2)) PRODUCTION_DATE,"+
        "ss.SHIFT,DEC(ss.PERIOD) PERIOD,ss.PERIOD_LABEL,ss.TYPE TYPE,ss.PLAN,"+
        "ss.START_TIME,ss.END_TIME,ss.NEXT_DAY,"+
        "ss.CAPACITY,ss.CAPACITY_ON,ss.PROCESS_LOCATION PLAN_CODE,ac.WEEK_NUMBER WEEK_NO,'Y' ISWORK,"+
        "TIMESTAMP(SUBSTR(ac.CALENDAR_DATE,1,4)||'-'||SUBSTR(ac.CALENDAR_DATE,5,2)||'-'||SUBSTR(ac.CALENDAR_DATE,7,2), ss.START_TIME) START_TIMESTAMP,"+
        "TIMESTAMP(SUBSTR(ac.CALENDAR_DATE,1,4)||'-'||SUBSTR(ac.CALENDAR_DATE,5,2)||'-'||SUBSTR(ac.CALENDAR_DATE,7,2), ss.END_TIME) END_TIMESTAMP "+
        "FROM GALADM.TGA3061_TBX ac JOIN GALADM.STANDARD_SCHEDULE_TBX ss ON ss.DAY_OF_WEEK = ac.DAY_OF_WEEK "+
        "WHERE DATE(SUBSTR(ac.CALENDAR_DATE,1,4)||'-'||SUBSTR(ac.CALENDAR_DATE,5,2)||'-'||SUBSTR(ac.CALENDAR_DATE,7,2)) "+
	          "BETWEEN ?1 AND ?2 "+
  	   	"ORDER BY PROCESS_LOCATION,PRODUCTION_DATE,PERIOD " +
	   	"WITH UR FOR READ ONLY";

	private static final String DELETE_SCHEDULES= "DELETE FROM GALADM.GAL226TBX WHERE PRODUCTION_DATE >= ?1";

	private static final String FIND_ALL_BY_CURRENT_TIMESTAMP = 
		  "select d.* from galadm.gal226tbx d inner join galadm.gal226tbx s on d.PROCESS_LOCATION = s.PROCESS_LOCATION and d.PRODUCTION_DATE = s.PRODUCTION_DATE " +
		  "where s.START_TIMESTAMP <= ?1 and s.END_TIMESTAMP >= ?1 " +
		  "order by d.LINE_NO,d.PROCESS_LOCATION,d.PRODUCTION_DATE,d.PERIOD";
	
	private final String FIND_ALL_SHIFT = "SELECT DISTINCT SHIFT FROM GALADM.GAL226TBX";
	
	private final String FIND_ALL_SHIFT_BY_DATE_AND_PROCESS_LOCATION = "SELECT DISTINCT SHIFT FROM GALADM.GAL226TBX where PRODUCTION_DATE = ?1 AND PROCESS_LOCATION = ?2";

	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public DailyDepartmentSchedule findByActualTime(String lineNo,
			String processLocation, String plantCode, Timestamp actualTimestamp) {
		
		Parameters parameters = Parameters.with("1", lineNo);
		parameters.put("2", processLocation);
		parameters.put("3", plantCode);
		parameters.put("4", actualTimestamp);
		
		//Only need the latest or max start time stamp sorted by the query
		return findFirstByNativeQuery(FIND_BY_ACTUAL_TIMESTAMP, parameters);
	}
	
	@Transactional
	public DailyDepartmentSchedule find(String divisionId, Timestamp timestamp) {
		GpcsDivision gpcsDivision = getGpcsDivisionDao().findByKey(divisionId);
		if (gpcsDivision == null) {
			return null;
		}
		String lineNo = gpcsDivision.getGpcsLineNo();
		String processLocation = gpcsDivision.getGpcsProcessLocation();
		String plantCode = gpcsDivision.getGpcsPlantCode();
		DailyDepartmentSchedule schedule = findByActualTime(lineNo, processLocation, plantCode, timestamp);
		return schedule;
	}	
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<Object[]> findNextProdDate(String prodDate,
			String processLocation, String isWork) {
		
		Parameters parameters = Parameters.with("1", prodDate);
		parameters.put("2", processLocation);
		parameters.put("3", isWork);
		
		return findAllByNativeQuery(FIND_NEXT_PROD_DATE, parameters, Object[].class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<Object[]> getProductionDay(String prodDate,
			String processLocation) {
		Parameters parameters = Parameters.with("1", prodDate);
		parameters.put("2", prodDate);
		parameters.put("3", processLocation);
		
		return findAllByNativeQuery(GET_PRODUCTION_DAY, parameters, Object[].class);
	}


	public List<Object[]> getSchedule(String plant, String line,
			String processLocation, String productionDate) {
		
		Parameters parameters = Parameters.with("1", plant);
		parameters.put("2", line);
		parameters.put("3", processLocation);
		parameters.put("4", productionDate);
		
		return findAllByNativeQuery(FIND_SCHEDULE_INFO, parameters, Object[].class);
	}


	public List<Object[]> getProcessInfo() {
		return findAllByNativeQuery(FIND_PROCESS_INFO, null, Object[].class);
	}


	@Transactional
	public void saveSchedules(List<Object[]> schedules) {
		for (Object[] schedule : schedules) {
			Parameters params = new Parameters();
			for (int i = 0; i < schedule.length; i++) {
				params.put("" + (i + 1), schedule[i]);
			}
			executeNative(UPDATE_SCHEDULE, params);
		}
		
	}
	
	public List<DailyDepartmentSchedule> findAllById(DailyDepartmentScheduleId id){
		Parameters params = new Parameters(); 
		if(id == null) return new ArrayList<DailyDepartmentSchedule>();
		if(!StringUtils.isEmpty(id.getPlantCode())) params.put("id.plantCode", id.getPlantCode());
		if(!StringUtils.isEmpty(id.getLineNo())) params.put("id.lineNo", id.getLineNo());
		if(!StringUtils.isEmpty(id.getProcessLocation())) params.put("id.processLocation", id.getProcessLocation());
		if(id.getProductionDate() != null && id.getProductionDate().compareTo(new Date(-1)) != 1) params.put("id.productionDate", id.getProductionDate());
		if(id.getPeriod() > 0) params.put("id.period", id.getPeriod());
		if(!StringUtils.isEmpty(id.getShift())) params.put("id.shift", id.getShift());
		String[] orderBy = {"id.plantCode","id.lineNo","id.processLocation","id.productionDate","id.period"};
		return findAll(params, orderBy, true);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> findAllByProcessPoint(
			ProcessPoint processPoint, Date productDate) {
		
		List<DailyDepartmentSchedule> schedules = new ArrayList<DailyDepartmentSchedule>();
		
		GpcsDivision division = gpcsDivisionDao.findByKey(processPoint.getDivisionId());
		if(division == null) return schedules;
		
		Parameters params = Parameters.with("id.lineNo", division.getGpcsLineNo())
		                              .put("id.processLocation",division.getGpcsProcessLocation())
		                              .put("id.plantCode", division.getGpcsPlantCode())
		                              .put("id.productionDate", productDate);
		
		return findAll(params,new String[]{"id.period"},true);
		
	}
	
	public List<DailyDepartmentSchedule> findAllByProcessPointAndTimestamp(ProcessPoint processPoint, Timestamp timestamp) {
		
		List<DailyDepartmentSchedule> schedules = new ArrayList<DailyDepartmentSchedule>();
		
		GpcsDivision division = gpcsDivisionDao.findByKey(processPoint.getDivisionId());
		if(division == null) return schedules;
		
		DailyDepartmentSchedule schedule = findByActualTime(division.getGpcsLineNo(), division.getGpcsProcessLocation(), division.getGpcsPlantCode(), timestamp);
		schedules.add(schedule);
		
		return schedules;
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> findUptoCurrentTime(String lineNo,
			String processLocation, String plantCode, Date productionDate, Timestamp actualTimestamp) {
		
		Parameters parameters = Parameters.with("lineNo", lineNo)
                .put("processLocation", processLocation)
                .put("plantCode", plantCode)
                .put("productionDate", productionDate)
                .put("startTime", actualTimestamp);

		return findAllByQuery(FIND_UPTO_CURRENT_TIME, parameters);
	}

	public List<DailyDepartmentSchedule> findAllByProcessPointAndPeriodLabels(ProcessPoint processPoint,
			List<String> periodLabels, Date productDate) {
		List<DailyDepartmentSchedule> schedules = new ArrayList<DailyDepartmentSchedule>();
		
		GpcsDivision division = gpcsDivisionDao.findByKey(processPoint.getDivisionId());
		if(division == null) return schedules;
		
		Parameters parameters = Parameters.with("lineNo", division.getGpcsLineNo())
                .put("processLocation",division.getGpcsProcessLocation())
                .put("plantCode", division.getGpcsPlantCode())
                .put("productionDate", productDate)
                .put("periodLabels", periodLabels);
		return findAllByQuery(FIND_BY_PROCESS_POINT_AND_PERIOD_LABELS, parameters);
	}
	
	public List<DailyDepartmentSchedule> findAllByDivisionAndPlan(String divisionId,
			String plan, Date productDate) {
		List<DailyDepartmentSchedule> schedules = new ArrayList<DailyDepartmentSchedule>();
		
		GpcsDivision division = gpcsDivisionDao.findByKey(divisionId);
		if(division == null) return schedules;
		
		Parameters parameters = Parameters.with("lineNo", division.getGpcsLineNo())
                .put("processLocation",division.getGpcsProcessLocation())
                .put("plantCode", division.getGpcsPlantCode())
                .put("productionDate", productDate)
                .put("plan", plan);
		return findAllByQuery(FIND_BY_DIVISION_AND_PLAN, parameters);
	}
	
	public List<DailyDepartmentSchedule> findAllByLineAndShift(
			String lineNo,String processLocation, String plantCode, Date productDate,String shift) {
		
		Parameters params = Parameters.with("id.lineNo", lineNo)
		                              .put("id.processLocation",processLocation)
		                              .put("id.plantCode", plantCode)
		                              .put("id.productionDate", productDate)
		                              .put("id.shift", shift);
		
		return findAll(params,new String[]{"id.period"},true);
		
	}
	
	protected GpcsDivisionDao getGpcsDivisionDao() {
		return gpcsDivisionDao;
	}	
		
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public Timestamp findMinStartTimestamp(String prodDate,String processLocation) {		
		Parameters parameters = Parameters.with("1", prodDate);
		parameters.put("2", processLocation);	
		return findFirstByNativeQuery(FIND_MIN_START_TIMESTAMP, parameters,Timestamp.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public Object[] findStartTimeNextDayInfo(String processLocation) {		
		Parameters parameters = Parameters.with("1", processLocation);	
		return findFirstByNativeQuery(FIND_START_TIME_NEXT_DAY_INFO, parameters,Object[].class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public Timestamp findMaxEndTimestamp(String prodDate,String processLocation) {		
		Parameters parameters = Parameters.with("1", prodDate);
		parameters.put("2", processLocation);	
		return findFirstByNativeQuery(FIND_MAX_END_TIMESTAMP, parameters,Timestamp.class);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<Object[]> findEndTimeNextDayInfo(String processLocation) {		
		Parameters parameters = Parameters.with("1", processLocation);	
		return findAllByNativeQuery(FIND_END_TIME_NEXT_DAY_INFO, parameters,Object[].class);
	}

	/**
	 * get all records after current date
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> getAllFutureByProductionDate(Date productionDate) {
		Parameters parameters = Parameters.with("id.productionDate", productionDate);	
		return findAll(parameters);
	}

	/**
	 * get all records after current date
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> getAllByProductionDateAndLocation(Date productionDate, String processLocation) {
		Parameters parameters = Parameters.with("id.productionDate", productionDate);	
		parameters.put("id.processLocation", processLocation);	
		return findAll(parameters);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule>  getAllByProductionDateLocationAndLineNo(Date productionDate, String processLocation, String lineNo) {
		Parameters parameters = Parameters.with("id.productionDate", productionDate);	
		parameters.put("id.processLocation", processLocation);	
		parameters.put("id.lineNo", lineNo);
		return findAll(parameters);
	}

	/**
	 * get all records after current date
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> getScheduleInfo(String plantCode, Date productionDate, String processLocation, String shift) {
		Parameters parameters = Parameters.with("id.plantCode", plantCode);	
		parameters.put("id.productionDate", productionDate);
		parameters.put("id.processLocation", processLocation);	
		parameters.put("id.shift", shift);
		return findAll(parameters,new String[]{"id.period"},true);
	}

	/**
	 * get all records after current date for specified plant
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> getAllFutureByPlantAndProductionDate(String plantCode, Date productionDate) {
		Parameters parameters = Parameters.with("plantCode", plantCode);
		parameters.put("productionDate", productionDate);
		return findAllByQuery(GET_FUTURE_BY_PRODUCTION_DATE, parameters);
	}

	/**
	 * get all records after current date for specified plan codes
	 */
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation = Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> getAllFutureByPlanCodeAndProductionDate(String planCode, Date productionDate) {
		Parameters parameters = Parameters.with("planCode", planCode);	
		parameters.put("productionDate", productionDate);
		return findAllByQuery(GET_FUTURE_BY_PLAN_CODE, parameters);
	}

	public List<String> findTimeGaps(Date productionDate) {
		@SuppressWarnings("unchecked")
		List<Object[]> processingBodyList = findResultListByNativeQuery(FIND_TIME_GAPS, Parameters.with("1", productionDate));
		List<String> resultRecords = new ArrayList<String>();
		StringBuffer vRecord = new StringBuffer();
		for (Object[] objArr : processingBodyList) {
			vRecord.append("LineNo=").append(objArr[0]);
			vRecord.append("; ProcessLocation=").append(objArr[1]);
			vRecord.append("; PlantCode=").append(objArr[2]);
			vRecord.append("; ProductionDate=").append((Date)objArr[3]);
			vRecord.append("; Shift=").append(objArr[4].toString());
			vRecord.append("; Period=").append((Integer) objArr[5]);
			vRecord.append("; Next Period=").append((Integer) objArr[6]);
			vRecord.append("; Period EndTime=").append((Time) objArr[7]);
			vRecord.append("; Next Period StartTime=").append((Time)objArr[8]);
			vRecord.append("; NextDay=").append(objArr[9]);
			vRecord.append("; is out of sequence");
			resultRecords.add(vRecord.toString());
			vRecord.delete(0,vRecord.length());
		}
		return resultRecords;
	}

	public java.util.Date getNextProductionDate(java.util.Date t) {
		// create query
		Parameters params = Parameters.with("1", t);

		// raw query results
		List<Object[]> nextProductionDate = null;
		nextProductionDate = findAllByNativeQuery(SELECT_NEXT_PRODUCTION_DAY, params, Object[].class);
		Date date = null;
		if(nextProductionDate.get(0)!=null){
			if(nextProductionDate.get(0)[0]!=null){
				date = (Date) nextProductionDate.get(0)[0];
				
			}
		}
		return date;
		
	}
	
	
	public DailyDepartmentSchedule findByProductionDateShiftAndPeriodLabel(
			Date productionDate, String shift, String period,
			String processLocation) {

		Parameters params = Parameters
		.with("id.productionDate", productionDate)
		.put("id.shift", shift).put("periodLabel", period)
		.put("id.processLocation", processLocation);

		return findFirst(params);

	}
	

	@Transactional
	public DailyDepartmentSchedule findByProcessLocation(Timestamp actualTs, String pp) {
		Parameters p = Parameters.with("actualTs1", actualTs);	
		p.put("pp", pp);
		p.put("actualTs2", actualTs);
		DailyDepartmentSchedule sched = findFirstByQuery(FIND_BY_PRODDATE_PROCESS_LOC, p);
		return sched;
	}

	/**
	 * get the start time of the first period for a given production date
	 * @param lineNo
	 * @param plantNo
	 * @param processLocation
	 * @param productionDate the given production date
	 */
	public DailyDepartmentSchedule getFirstPeriodTimestamp(String lineNo, String plantNo,
			String processLocation, String productionDate) {
		// create query
		Parameters params = Parameters.with("1", lineNo);
		params.put("2", plantNo);
		params.put("3", processLocation);
		params.put("4", productionDate);
		
		DailyDepartmentSchedule schedule = findFirstByNativeQuery(FIND_FIRST_PERIOD, params);
		
		return schedule;
	}
	
	public List<DailyDepartmentSchedule> findAllByProductionDate(Date productionDate) {
		return findAllByNativeQuery(FIND_ALL_BY_DATE, Parameters.with("1", productionDate));
	}



	public DailyDepartmentSchedule findShiftFirstPeriod(DailyDepartmentSchedule currentSchedule) {
		Parameters params = Parameters.with("id.lineNo", currentSchedule.getId().getLineNo());
		params.put("id.processLocation", currentSchedule.getId().getProcessLocation());
		params.put("id.plantCode", currentSchedule.getId().getPlantCode());
		params.put("id.productionDate", currentSchedule.getId().getProductionDate());
		params.put("id.shift", currentSchedule.getId().getShift());
		DailyDepartmentSchedule firstSchedule = findFirst(params, new String[]{"startTimestamp"}, true);
		return firstSchedule;
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> deriveDailyDepartmentSchedulesFromGPCS(
			Date startDate, Date endDate) {
		Parameters params = Parameters.with("1", startDate).put("2",endDate);
		List results = findResultListByNativeQuery(FIND_ALL_SCHEDULES, params);
		return mapToSchedules(results);
	}
	
	@SuppressWarnings("rawtypes")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<DailyDepartmentSchedule> generateSchedulesFromStandard(
			Date startDate, Date endDate) {
		Parameters params = Parameters.with("1", startDate).put("2",endDate);
		List results = findResultListByNativeQuery(GENERATE_SCHEDULES, params);
		return mapToSchedules(results);
	}
	
	private List<DailyDepartmentSchedule> mapToSchedules(List results) {
		List<DailyDepartmentSchedule> schedules = new ArrayList<DailyDepartmentSchedule>();
		for(Object entry : results) {
			Object[] items = (Object[]) entry;
			DailyDepartmentScheduleId id = new DailyDepartmentScheduleId();
			id.setPlantCode((String)items[0]);
			id.setLineNo((String)items[2]);
			id.setProcessLocation((String)items[4]);
			id.setProductionDate((Date)items[16]);
			id.setShift((String)items[7]);
			id.setPeriod(((BigDecimal)items[6]).intValue());
			DailyDepartmentSchedule schedule = new DailyDepartmentSchedule();
			schedule.setId(id);
			schedule.setDayOfWeek((String)items[5]);
			schedule.setPeriodLabel((String)items[8]);
			schedule.setType((String)items[9]);
			schedule.setPlan((String)items[11]);
			schedule.setStartTime((Time)items[12]);
			schedule.setEndTime((Time)items[13]);
			schedule.setNextDay(((Integer)items[14]).shortValue());
			if(items[20] != null)schedule.setCapacity((Integer)items[20]);
			schedule.setCapacityOn((Integer)items[19]);
			if(schedule.getCapacityOn() <=0) schedule.setCapacityOn(schedule.getCapacity());
			schedule.setPlanCode((String)items[1]);
			schedule.setWeekNo((String)items[10]);
			schedule.setIswork((String)items[15]);
			schedule.setStartTimestamp((Timestamp)items[17]);
			schedule.setEndTimestamp((Timestamp)items[18]);
			schedules.add(schedule);
		}
		return schedules;
	}
	
	@Transactional()
	public int deleteSchedules(String plantCode, String lineNo,
			String processLocation, Date productionDate) {
		Parameters params = Parameters.with("id.plantCode", plantCode);
        params.put("id.lineNo"  , lineNo);
        params.put("id.processLocation", processLocation);
        params.put("id.productiionDate", productionDate);
        return delete(params);
	}


	@Transactional()
	public int deleteSchedules(Date productionDate) {
		return executeNative(DELETE_SCHEDULES, Parameters.with("1", productionDate));
	}

	public List<DailyDepartmentSchedule> findAllByCurrentTimeStamp(Timestamp currentTimestamp) {
	   Parameters params = Parameters.with("1", currentTimestamp); 
	   return findAllByNativeQuery(FIND_ALL_BY_CURRENT_TIMESTAMP, params);
	}

	public List<String> findAllShifts() {
		return findResultListByNativeQuery(FIND_ALL_SHIFT, null);
	}
	
	public List<String> findAllShiftsByDateAndProcessLocation(Date productionDate, String processLocation) {
		Parameters params = Parameters.with("1", productionDate); 
		params.put("2", processLocation); 
		return findResultListByNativeQuery(FIND_ALL_SHIFT_BY_DATE_AND_PROCESS_LOCATION, params);
	}
	
	public List<DailyDepartmentSchedule> findByCurDateProcLocAndQuart(
		String lineNo, String processLocation, List<String> quarter) {
		Parameters params = Parameters.with("id.lineNo", lineNo);
		params.put("id.productionDate", new Date((new java.util.Date()).getTime()));
		params.put("id.processLocation", processLocation);
		params.put("periodLabel", quarter);
		
		
		return findAll(params, new String[]{"id.productionDate"},true);
		
	}

	@Override
	public List<Date> findAllByOffSet(Integer offset, Date parentLotDate, String processLocation) {
		offset = offset*(-1);
		Parameters params = Parameters.with("1", parentLotDate); 
		params.put("2", offset);
		params.put("3", processLocation); 

		return findResultListByNativeQuery(FIND_ALL_BY_OFFSET, params);
 	}

}
