package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.entity.product.ProductionLotMbpnSequenceId;
import com.honda.galc.service.IDaoService;



public interface ProductionLotMbpnSequenceDao extends IDaoService<ProductionLotMbpnSequence, ProductionLotMbpnSequenceId> {
	
	//findAll by ProductionLot
	public List<ProductionLotMbpnSequence> findAllByProductionLot(String productionLot);
	
	public int deleteByProductionLot(String productionLot);
	
}
