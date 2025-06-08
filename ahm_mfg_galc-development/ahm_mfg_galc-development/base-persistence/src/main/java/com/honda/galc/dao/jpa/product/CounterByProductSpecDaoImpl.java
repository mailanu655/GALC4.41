package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CounterByProductSpecDao;
import com.honda.galc.entity.product.CounterByProductSpec;
import com.honda.galc.entity.product.CounterByProductSpecId;
import com.honda.galc.service.Parameters;

public class CounterByProductSpecDaoImpl extends BaseDaoImpl<CounterByProductSpec,CounterByProductSpecId> implements CounterByProductSpecDao{


	private final String INCREMENT_COUNTER ="update galadm.gal119tbx set passing_counter = coalesce(passing_counter, 0) + 1 " +
	"where process_point_id = ?1 and production_date = ?2 and shift = ?3 and period = ?4 and product_spec_code = ?5";

	@Transactional
	public int incrementCounter(CounterByProductSpecId id) {
		Parameters params = Parameters.with("1", id.getProcessPointId());
		params.put("2", id.getProductionDate().toString());
		params.put("3", id.getShift());
		params.put("4", "" + id.getPeriod());
		params.put("5", "" + id.getProductSpecCode());
		
		return executeNativeUpdate(INCREMENT_COUNTER, params);
	}

}
