package com.honda.galc.dao.jpa.lcvinbom;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.ModelLotDao;
import com.honda.galc.entity.lcvinbom.ModelLot;
import com.honda.galc.entity.lcvinbom.ModelLotId;
import com.honda.galc.service.Parameters;

public class ModelLotDaoImpl extends BaseDaoImpl<ModelLot, ModelLotId> 
	implements ModelLotDao {

	@Override
	public List<ModelLot> getAssignedLots(long modelPartId) {
		Parameters params = Parameters.with("id.modelPartId", modelPartId);
		return findAll(params);
	}
	
	@Override
	public List<ModelLot> getByModelPartIdAndProdLot(long modelPartId, String startingProductionLot) {
		Parameters params = Parameters.with("id.modelPartId", modelPartId)
				.put("startingProductionLot", startingProductionLot);
		return findAll(params);
	}

	@Override
	@Transactional
	public int removeByModelPartId(long modelPartId) {
		Parameters params = Parameters.with("id.modelPartId", modelPartId);
		return delete(params);
	}

	@Override
	@Transactional
	public ModelLot saveModelLot(ModelLot modelLot) {
		if(modelLot!=null && modelLot.getId()!=null) {
			ModelLot modelLotEntity = findByKey(modelLot.getId());
			if(modelLotEntity!=null) {
				modelLotEntity.setStartingProductionLot(modelLot.getStartingProductionLot());
				return save(modelLotEntity);
			} else {
				return save(modelLot);
			}
		}
		return modelLot;
	}
}
