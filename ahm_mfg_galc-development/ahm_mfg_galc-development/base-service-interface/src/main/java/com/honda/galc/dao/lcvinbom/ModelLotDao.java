package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.entity.lcvinbom.ModelLot;
import com.honda.galc.entity.lcvinbom.ModelLotId;
import com.honda.galc.service.IDaoService;

public interface ModelLotDao extends IDaoService<ModelLot, ModelLotId> {
	public List<ModelLot> getAssignedLots(long modelPartId);
	public List<ModelLot> getByModelPartIdAndProdLot(long modelPartId, String startingProductionLot);
	public int removeByModelPartId(long modelPartId);
	public ModelLot saveModelLot(ModelLot modelLot);
	
 
}
