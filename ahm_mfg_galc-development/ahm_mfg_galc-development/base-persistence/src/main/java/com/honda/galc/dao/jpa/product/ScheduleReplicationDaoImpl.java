package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ScheduleReplicationDao;
import com.honda.galc.entity.product.ScheduleReplication;
import com.honda.galc.entity.product.ScheduleReplicationId;
import com.honda.galc.service.Parameters;

public class ScheduleReplicationDaoImpl extends BaseDaoImpl<ScheduleReplication, ScheduleReplicationId> implements ScheduleReplicationDao {

	private static final String GET_DISTINCT_SOURCE_PROC_LOC = "select distinct SOURCE_PROC_LOC from schedule_rep_tbx";

	private static final String GET_DISTINCT_DEST_PROC_LOC = "select distinct DEST_PROC_LOC from schedule_rep_tbx";

	private static final String GET_DISTINCT_DEST_SPEC_CODE = "select distinct DEST_SPEC_CODE from schedule_rep_tbx";

	private static final String FIND_BY_DEST_PROC_LOC = "Select * from schedule_rep_tbx where dest_proc_loc = ?2";

	private static final String FIND_BY_SRC_PROC_LOC = "select * from schedule_rep_tbx where source_proc_loc = ?1";

	@Override
	public List<ScheduleReplication> findBySourceLocation(String sourceProcessLocation) {

		Parameters parameters = Parameters.with("id.sourceProcLoc", sourceProcessLocation);
		return findAll(parameters);
	}

	@Override
	public List<ScheduleReplication> findBySourceLocAndDestLoc(String sourceProcessLocation, String destProcessLocation) {

		Parameters parameters = Parameters.with("id.sourceProcLoc", sourceProcessLocation);
		parameters.put("id.destProcLoc", destProcessLocation);
		return findAll(parameters);
	}

	@Override
	public List<ScheduleReplication> findBySourceLocAndDestLoc(String sourceProcessLocation, String destProcessLocation, String count) {

		Parameters parameters = new Parameters();

		if (!sourceProcessLocation.equalsIgnoreCase("All") && destProcessLocation.equalsIgnoreCase("All")) {
			parameters = Parameters.with("1", sourceProcessLocation);
			return findAllByNativeQuery(FIND_BY_SRC_PROC_LOC, parameters);
		}

		if (sourceProcessLocation.equalsIgnoreCase("All") && !destProcessLocation.equalsIgnoreCase("All")) {
			parameters.put("2", destProcessLocation);
			return findAllByNativeQuery(FIND_BY_DEST_PROC_LOC, parameters);
		}

		if (sourceProcessLocation.equalsIgnoreCase("All") && destProcessLocation.equalsIgnoreCase("All")) {
			return findAllByNativeQuery("Select * from schedule_rep_tbx order by COALESCE(CREATE_TIMESTAMP, UPDATE_TIMESTAMP) desc", parameters, Integer.valueOf(count));
		}

		if (!sourceProcessLocation.equalsIgnoreCase("All") && !destProcessLocation.equalsIgnoreCase("All")) {
			return findBySourceLocAndDestLoc(sourceProcessLocation, destProcessLocation);
		}

		return null;
	}

	@Override
	public boolean isScheduleReplicationExist(String sourceProcessLocation, String destProcessLocation, String destSpecCode) {

		Parameters parameters = Parameters.with("id.sourceProcLoc", sourceProcessLocation);
		parameters.put("id.destProcLoc", destProcessLocation);
		parameters.put("id.destSpecCode", destSpecCode);

		List<ScheduleReplication> results = findAll(parameters);
		if (results.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public List<List<String>> findAllDistinctSrcLocDestSpecCodeDestProcLoc() {

		List<String> sourceProcLoc = findAllByNativeQuery(GET_DISTINCT_SOURCE_PROC_LOC, null, String.class);
		List<String> destProcLoc = findAllByNativeQuery(GET_DISTINCT_DEST_PROC_LOC, null, String.class);
		List<String> destSpecCode = findAllByNativeQuery(GET_DISTINCT_DEST_SPEC_CODE, null, String.class);

		List<List<String>> records = new ArrayList<List<String>>();
		records.add(sourceProcLoc);
		records.add(destProcLoc);
		records.add(destSpecCode);

		return records;
	}

}
