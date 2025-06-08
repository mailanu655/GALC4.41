package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.CounterId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>CounterDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CounterDaoImpl description </p>
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
 * Sep 28, 2010
 *
 */
public class CounterDaoImpl extends BaseDaoImpl<Counter,CounterId> implements CounterDao{
	private final String INCREMENT_COUNTER ="update galadm.gal240tbx set passing_counter = coalesce(passing_counter, 0) + 1 " +
			"where process_point_id = ?1 and production_date = ?2 and shift = ?3 and period = ?4";

	private final String FIND_COUNT = 
		"SELECT PROCESS_POINT_ID,SHIFT,SUM(PASSING_COUNTER) AS PASSING_COUNTER " +
		"FROM GALADM.GAL240TBX WHERE PRODUCTION_DATE = ?1" + 
		" GROUP BY PROCESS_POINT_ID,SHIFT ORDER BY PROCESS_POINT_ID,SHIFT";
	
	private final String FIND_COUNT_FOR_DIVISION = 
		"select c.PROCESS_POINT_ID,c.SHIFT,SUM(c.PASSING_COUNTER) AS PASSING_COUNTER " +
		"from galadm.gal240tbx c inner join galadm.gal214tbx p on c.PROCESS_POINT_ID=p.PROCESS_POINT_ID " +
		"where c.production_date= ?1 and p.DIVISION_ID= ?2 " +
		" GROUP BY c.PROCESS_POINT_ID,c.SHIFT ORDER BY c.PROCESS_POINT_ID,c.SHIFT";
	
	private final String FIND_COUNT_PROCESS_POINT = 
			"SELECT SUM(PASSING_COUNTER) AS PASSING_COUNTER " +
			"FROM GALADM.GAL240TBX WHERE PRODUCTION_DATE = ?1 AND PROCESS_POINT_ID = ?2";
		
	private final String GET_PBC_BY_DATE_PROCESS_AND_SHIFT =
			"SELECT SUM(c.PASSING_COUNTER) AS PASSING_COUNTER from galadm.GAL240TBX c where c.PRODUCTION_DATE=?1 AND c.PROCESS_POINT_ID=?2 AND c.shift=?3 GROUP BY c.PROCESS_POINT_ID, c.SHIFT";

	@Autowired
	DailyDepartmentScheduleDao dailyDepartmentScheduleDao;
	
	@Autowired
	ProcessPointDao processPointDao;
	
	private final String GET_PBC_FOR_PROCESS =
			"select coalesce(sum(passing_counter),0) from galadm.GAL240TBX where PROCESS_POINT_ID=?1 and PRODUCTION_DATE=CURRENT DATE";

	@Transactional
	public int incrementCounter(CounterId id) {
		Parameters params = Parameters.with("1", id.getProcessPointId());
		params.put("2", "" + id.getProductionDate());
		params.put("3", id.getShift());
		params.put("4", "" + id.getPeriod());
		
		return executeNativeUpdate(INCREMENT_COUNTER, params);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Counter> findCounts(Date productionDate) {
		
		List results = findResultListByNativeQuery(FIND_COUNT, Parameters.with("1", productionDate));
		return convertToCounters(results);
			
	}

	@SuppressWarnings("unchecked")
	public int getPassingBodyCountForCurrentDate(String processPointId) {
		Parameters params = Parameters.with("1", processPointId);
		int count = findFirstByNativeQuery(GET_PBC_FOR_PROCESS, params, Integer.class);
		return count;
			
	}

	@SuppressWarnings("unchecked")
	private List<Counter> convertToCounters(List results) {
		List<Counter> counters = new ArrayList<Counter>();
		for(Object entry : results) {
			Object[] items = (Object[]) entry;
			Counter counter = new Counter();
			CounterId id = new CounterId();
			id.setProcessPointId((String)items[0]);
			id.setShift((String)items[1]);
			counter.setId(id);
			counter.setPassingCounter((Integer)items[2]);
			counters.add(counter);
		}
		return counters;
	}

	@SuppressWarnings("unchecked")
	public List<Counter> findAllByDivisionIdAndProductionDate(Date productionDate, String divisionId) {
		List results = findResultListByNativeQuery(FIND_COUNT_FOR_DIVISION, Parameters.with("1", productionDate).put("2", divisionId));
		return convertToCounters(results);
	}

	@Override
	public Long findCount(Date productionDate, String processPointId) {
		return findFirstByNativeQuery(FIND_COUNT_PROCESS_POINT, Parameters.with("1", productionDate).put("2", processPointId), Long.class);
	}

	public Integer getCountByDateShiftAndProcess(Date productionDate, String shift, String processPointId) {
		Integer count = findFirstByNativeQuery(GET_PBC_BY_DATE_PROCESS_AND_SHIFT, Parameters.with("1", productionDate).put("2", processPointId).put("3", shift), Integer.class);
		return count;
	}
}
