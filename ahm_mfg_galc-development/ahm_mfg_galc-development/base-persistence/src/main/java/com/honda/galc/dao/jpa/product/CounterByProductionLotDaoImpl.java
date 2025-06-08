package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CounterByProductionLotDao;
import com.honda.galc.entity.product.CounterByProductionLot;
import com.honda.galc.entity.product.CounterByProductionLotId;
import com.honda.galc.service.Parameters;

public class CounterByProductionLotDaoImpl extends BaseDaoImpl<CounterByProductionLot,CounterByProductionLotId> implements CounterByProductionLotDao{

	private final String INCREMENT_COUNTER ="update galadm.gal120tbx set passing_counter = coalesce(passing_counter, 0) + 1 " +
	"where process_point_id = ?1 and production_date = ?2 and shift = ?3 and period = ?4 and production_lot = ?5";

	@Transactional(isolation=Isolation.SERIALIZABLE)
	public int incrementCounter(CounterByProductionLotId id) {
		Parameters params = Parameters.with("1", id.getProcessPointId());
		params.put("2", id.getProductionDate().toString());
		params.put("3", id.getShift());
		params.put("4", "" + id.getPeriod());
		params.put("5", "" + id.getProductionLot());
		
		return executeNativeUpdate(INCREMENT_COUNTER, params);
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE, propagation=Propagation.REQUIRES_NEW)
	public CounterByProductionLot save(CounterByProductionLot entity) {
		return super.save(entity);
	}
	
}
