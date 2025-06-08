package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CounterByModelGroupDao;
import com.honda.galc.entity.product.CounterByModelGroup;
import com.honda.galc.entity.product.CounterByModelGroupId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>CounterByModelGroupDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CounterByModelGroupDaoImpl description </p>
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
 * Sep 29, 2010
 *
 */
public class CounterByModelGroupDaoImpl extends BaseDaoImpl<CounterByModelGroup,CounterByModelGroupId> implements CounterByModelGroupDao{

	private final String INCREMENT_COUNTER ="update galadm.gal118tbx set passing_counter = coalesce(passing_counter, 0) + 1 " +
	"where process_point_id = ?1 and production_date = ?2 and shift = ?3 and period = ?4 and model_code = ?5 and model_year_code = ?6";

	@Transactional
	public int incrementCounter(CounterByModelGroupId id) {
		Parameters params = Parameters.with("1", id.getProcessPointId());
		params.put("2", id.getProductionDate().toString());
		params.put("3", id.getShift());
		params.put("4", "" + id.getPeriod());
		params.put("5", "" + id.getModelCode());
		params.put("6", "" + id.getModelYearCode());
		
		return executeNativeUpdate(INCREMENT_COUNTER, params);

	}
}
