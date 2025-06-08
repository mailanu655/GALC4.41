package com.honda.galc.dao.jpa.oif;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.MaterialServiceDao;
import com.honda.galc.entity.oif.MaterialService;
import com.honda.galc.entity.oif.MaterialServiceId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MaterialServiceDaoImpl</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Mar 11, 2015
 */
public class MaterialServiceDaoImpl extends BaseDaoImpl<MaterialService, MaterialServiceId> implements MaterialServiceDao {
	
	public static final String DELETE_PROCESSED = "delete from ms_pmx_tbx where (kd_lot_number, process_point_id) in " 
	     + "(select kd_lot_number, process_point_id from ms_pmx_tbx "
	     + " group by kd_lot_number, process_point_id, lot_size "
	     + " having lot_size = sum(case when 'Y' = sent_flag then 1 else 0 end) "
	     + " and max(actual_timestamp) < ?1 )";
	
	
	public static final String INSERT_TO_PROCESS = "insert into ms_pmx_tbx (product_id, plan_code, line_no, process_point_id, production_date, actual_timestamp, product_spec_code, lot_size, on_seq_no, production_lot, kd_lot_number, plan_off_date, sent_flag) "
		 + "select ph.product_id ,pl.plan_code, pl.line_no, ph.process_point_id, ph.production_date, ph.actual_timestamp, pl.product_spec_code ,kdl.kd_lot_size as lot_size, kds.kd_seq_no as on_seq_no, pl.lot_number as production_lot, pl.kd_lot_number, pl.plan_off_date, 'N' as sent_flag "
		 + "from (select product_id, process_point_id, production_lot, production_date, min(actual_timestamp) as actual_timestamp from gal215tbx where process_point_id in (?1) and actual_timestamp >= ?2 group by product_id, process_point_id, production_lot, production_date) ph "
		 + "left join gal217tbx pl on pl.production_lot = ph.production_lot "
		 + "left join (select kd_lot_number, sum(lot_size) as kd_lot_size from gal217tbx "
		 + "@PROCESS_LOCATIONS@ "
		 + "group by kd_lot_number) kdl on kdl.kd_lot_number = pl.kd_lot_number "
		 + "join (select ph2.product_id ,ph2.process_point_id, ph2.actual_timestamp, pl2.kd_lot_number, pl2.production_lot "
		 + ",row_number() over(partition by pl2.kd_lot_number, ph2.process_point_id  order by pl2.kd_lot_number asc, ph2.process_point_id asc,  ph2.ACTUAL_TIMESTAMP asc) as kd_seq_no "
		 + "from (select product_id, process_point_id, production_lot, min(actual_timestamp) as actual_timestamp from gal215tbx where process_point_id in (?1) and actual_timestamp >= ?2 group by product_id, process_point_id, production_lot) ph2 "
		 + "left join gal217tbx pl2 on pl2.production_lot = ph2.production_lot "
		 + ") kds on kds.product_id = ph.product_id and kds.process_point_id = ph.process_point_id and kds.actual_timestamp = ph.actual_timestamp "
		 + "where not exists (select product_id, process_point_id from ms_pmx_tbx m where m.product_id = ph.product_id and m.process_point_id = ph.process_point_id)";		 
		 
	public static final String PROCESS_LOCATION_FILTER = "WHERE PROCESS_LOCATION IN (@P_LOCATIONS@)";
	
	public static final String SELECT_TO_PROCESS = "select e from MaterialService e where (e.sentFlag is null or e.sentFlag <> 'Y') and e.actualTimestamp >= :actualTimestamp order by e.actualTimestamp asc";
	
	@Transactional
	public int insertToProcess(String processPointId, Timestamp startTimestamp, String[] processLocations) {
		String processLocationsFilter = (processLocations!=null && processLocations.length>0) ? PROCESS_LOCATION_FILTER.replaceAll("@P_LOCATIONS@",StringUtil.toSqlInString(processLocations))  : "";
		return super.executeNativeUpdate(INSERT_TO_PROCESS.replaceAll("@PROCESS_LOCATIONS@", processLocationsFilter), Parameters.with("1", processPointId).put("2", startTimestamp));
	}

	@Transactional
	public int deleteProcessed(Timestamp beforeTimestamp) {
		return super.executeNativeUpdate(DELETE_PROCESSED, Parameters.with("1", beforeTimestamp));
	}

	public List<MaterialService> findAllToProcess(Timestamp fromTimestamp) {
		return findAllByQuery(SELECT_TO_PROCESS, Parameters.with("actualTimestamp", fromTimestamp));
	}
}
