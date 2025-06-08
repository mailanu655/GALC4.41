package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ModelTypeHoldDao;
import com.honda.galc.entity.product.ModelTypeHold;
import com.honda.galc.entity.product.ModelTypeHoldId;
import com.honda.galc.service.Parameters;


public class ModelTypeHoldDaoImpl extends BaseDaoImpl<ModelTypeHold,ModelTypeHoldId> implements ModelTypeHoldDao {

	private static final String  DELETE_BY_PRODUCTION_LOT_RANGE = "delete from MODEL_TYPE_HOLD_TBX h where h.PRODUCTION_LOT between ?1 and ?2";
	
	@Override
	@Transactional
	public int deleteAllByProductionLotRange(String start, String end) {
		Parameters params = Parameters.with("1", start);
		params.put("2", end);

		
		return executeNativeUpdate(DELETE_BY_PRODUCTION_LOT_RANGE, params);
	}
}
