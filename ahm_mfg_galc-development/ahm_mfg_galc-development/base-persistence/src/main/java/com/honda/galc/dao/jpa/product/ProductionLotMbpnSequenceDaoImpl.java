package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.entity.product.ProductionLotMbpnSequenceId;
import com.honda.galc.service.Parameters;



public class ProductionLotMbpnSequenceDaoImpl extends BaseDaoImpl<ProductionLotMbpnSequence,ProductionLotMbpnSequenceId> implements ProductionLotMbpnSequenceDao{

	@Override
	public List<ProductionLotMbpnSequence> findAllByProductionLot(String productionLot) {
		Parameters params = Parameters.with("id.productionLot", productionLot);
			
		return findAll(params,new String[] {"id.sequence"});
	}

	@Override
	@Transactional
	public int deleteByProductionLot(String productionLot) {
		return delete(Parameters.with("id.productionLot", productionLot));
	}
	
}
