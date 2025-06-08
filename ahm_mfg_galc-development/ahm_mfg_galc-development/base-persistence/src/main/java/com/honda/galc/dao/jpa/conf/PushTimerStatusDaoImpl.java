package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.PushTimerStatusDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.PushTimerStatusDto;
import com.honda.galc.entity.conf.PushTimerStatus;
import com.honda.galc.entity.conf.PushTimerStatusId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Aug 3, 2015
 */
public class PushTimerStatusDaoImpl extends BaseDaoImpl<PushTimerStatus, PushTimerStatusId> implements PushTimerStatusDao {
	
	private static String FIND_STATUS_FOR_ALL_PPS = "SELECT (coalesce (d.sequence_number, 0) + 1) * 1000000"
					+ " + (coalesce (l.line_sequence_number, 0) + 1) * 1000"
					+ " + (coalesce (pp.sequence_number, 0) + 1)"
					+ " seq_number, pt.process_point_id, pt.product_id, pt.status_in_secs, pt.ops_planned, pt.ops_completed, pt.last_updated, pp.division_id"
					+ " FROM push_timer_status_tbx pt"
					+ " JOIN gal214tbx pp ON pt.process_point_id = pp.process_point_id"
					+ " LEFT JOIN galadm.gal195tbx l ON pp.line_id = l.line_id"
					+ " LEFT JOIN galadm.gal128tbx d ON l.division_id = d.division_id"
					+ " LEFT JOIN galadm.gal117tbx s ON d.site_name = s.site_name"
					+ " WHERE d.plant_name = ?1";
	
	private static String FIND_TOP_LAGGERS_BY_DIVISION = "SELECT pt.process_point_id, pt.product_id, pt.status_in_secs, pt.ops_planned, pt.ops_completed, pt.last_updated, pp.division_id"
					+ " FROM push_timer_status_tbx pt, gal214tbx pp"
					+ " WHERE pt.process_point_id = pp.process_point_id"
					+ " AND pt.process_point_id IN"
					+ " (SELECT pp.process_point_id FROM gal214tbx pp, gal128tbx div"
					+ " WHERE pp.division_id = div.division_id"
					+ " AND div.plant_name = ?1"
					+ " AND div.division_id = ?2"
					+ " ORDER BY status_in_secs ASC)";

	public List<PushTimerStatusDto> getPlantProgress(String plantName) {
		Parameters params = Parameters.with("1", plantName);
		return findAllByNativeQuery(FIND_STATUS_FOR_ALL_PPS, params, PushTimerStatusDto.class);
	}

	public List<PushTimerStatusDto> getTopLaggingProcessPoints(String plantName, String divisionId, int howMany) {
		Parameters params = Parameters.with("1", plantName)
				.put("2", divisionId);
		return findAllByNativeQuery(FIND_TOP_LAGGERS_BY_DIVISION, params, PushTimerStatusDto.class, howMany);
	}
}
