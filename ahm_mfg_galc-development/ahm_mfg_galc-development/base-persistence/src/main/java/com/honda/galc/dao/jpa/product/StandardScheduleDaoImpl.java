package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.StandardScheduleDao;
import com.honda.galc.entity.product.StandardSchedule;
import com.honda.galc.entity.product.StandardScheduleId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>StandardScheduleDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> StandardScheduleDaoImpl description </p>
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
 * Mar 7, 2011
 *
 */
public class StandardScheduleDaoImpl extends BaseDaoImpl<StandardSchedule,StandardScheduleId> implements StandardScheduleDao {
	final static String FIND_PROCESS_INFO = "SELECT DISTINCT PLANT_CODE,LINE_NO,PROCESS_LOCATION,DAY_OF_WEEK FROM GALADM.STANDARD_SCHEDULE_TBX";
	final static String FIND_SCHEDULE_INFO = "SELECT PROCESS_LOCATION,PERIOD,SHIFT,PERIOD_LABEL,TYPE,PLAN,START_TIME,END_TIME,NEXT_DAY,CAPACITY,CAPACITY_ON FROM STANDARD_SCHEDULE_TBX WHERE PLANT_CODE = ?1 AND LINE_NO = ?2 AND PROCESS_LOCATION = ?3 AND DAY_OF_WEEK = ?4 ORDER BY PERIOD";
	final static String UPDATE_SCHEDULE = "UPDATE STANDARD_SCHEDULE_TBX SET PLAN = ?1, START_TIME = ?2, END_TIME = ?3, NEXT_DAY = ?4, CAPACITY = ?5, CAPACITY_ON = ?6 WHERE PLANT_CODE = ?7 AND LINE_NO = ?8 AND PROCESS_LOCATION = ?9 AND DAY_OF_WEEK = ?10 AND PERIOD = ?11";
	private static final String CHECK_WORK_DAY = "SELECT ISWORK FROM GAL226TBX WHERE PROCESS_LOCATION = ? AND ? BETWEEN START_TIMESTAMP AND END_TIMESTAMP FOR READ ONLY";

	
	public List<StandardSchedule> findAllById(StandardScheduleId id){
		Parameters params = new Parameters(); 
		if(id == null) return new ArrayList<StandardSchedule>();
		if(!StringUtils.isEmpty(id.getPlantCode())) params.put("id.plantCode", id.getPlantCode());
		if(!StringUtils.isEmpty(id.getLineNo())) params.put("id.lineNo", id.getLineNo());
		if(!StringUtils.isEmpty(id.getProcessLocation())) params.put("id.processLocation", id.getProcessLocation());
		if(!StringUtils.isEmpty(id.getDayOfWeek())) params.put("id.dayOfWeek", id.getDayOfWeek());
		if(id.getPeriod() > 0) params.put("id.period", id.getPeriod());
		if(!StringUtils.isEmpty(id.getShift())) params.put("id.shift", id.getShift());
		String[] orderBy = {"id.plantCode","id.lineNo","id.processLocation","id.dayOfWeek","id.period"};
		return findAll(params, orderBy, true);
	}
	
	public List<Object[]> getProcessInfo() {
		return findAllByNativeQuery(FIND_PROCESS_INFO, null, Object[].class);
	}

	public List<Object[]> getSchedule(String plant, String line,
			String processLocation, String productionDate) {
		Parameters parameters = Parameters.with("1", plant);
		parameters.put("2", line);
		parameters.put("3", processLocation);
		parameters.put("4", productionDate);
		
		return findAllByNativeQuery(FIND_SCHEDULE_INFO, parameters, Object[].class);
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
	
	public boolean isProductionDay(String processLocation, String productionTimestampStr) {
		boolean retVal = false;
		Parameters params = Parameters.with("1", processLocation);
		params.put("2", new java.sql.Timestamp(Long.parseLong(productionTimestampStr)));
		@SuppressWarnings("unchecked")
		List<Object> resultList = findResultListByNativeQuery(CHECK_WORK_DAY, params);
		if(resultList != null && resultList.size() != 0) {
			String val = resultList.get(0) == null ? "N" : resultList.get(0).toString();
			if(val.equalsIgnoreCase("Y"))
				retVal = true;
			else 
				retVal = false;
		} 
		return retVal;
	}
	
	public List getQueryResults(String query, Parameters params) {
		return findResultListByNativeQuery(query, params);
	}
}
