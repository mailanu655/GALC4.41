package com.honda.galc.service.msip.handler.inbound;

import java.util.List;

import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gmm112Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */

public class Gmm112Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm112Dto> {

	private EngineSpecDao engineSpecDao;

	public Gmm112Handler() {
	}

	public void processEngineMasterSpec(List<Gmm112Dto> engSpecDtoList) {
		getLogger().info("start to process EngineMasterSpec");
		initEngineSpecData();
		for (Gmm112Dto es : engSpecDtoList) {
			EngineSpec engSpec = deriveEngineSpec(es);
			engineSpecDao.save(engSpec);
			getLogger().info(" Engine Spec saved" + engSpec);
		}
		getLogger().info("Gmm112Dto is processed: " + engSpecDtoList);
	}

	public void initEngineSpecData() {
		this.engineSpecDao = ServiceFactory.getDao(EngineSpecDao.class);
	}

	public boolean execute(List<Gmm112Dto> dtoList) {
		try {
			processEngineMasterSpec(dtoList);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public EngineSpec deriveEngineSpec(Gmm112Dto gmm112Dto) {
		EngineSpec engineSpec = new EngineSpec(); 
		engineSpec.setProductSpecCode(gmm112Dto.getProductSpecCode());
		engineSpec.setMissionPlantCode(gmm112Dto.getMissionPlantCode());
		engineSpec.setMissionModelCode(gmm112Dto.getMissionModelCode());
		engineSpec.setMissionPrototypeCode(gmm112Dto.getMissionPrototypeCode());
		engineSpec.setMissionModelTypeCode(gmm112Dto.getMissionModelTypeCode());
		engineSpec.setEngineNoPrefix(gmm112Dto.getEngineNoPrefix());
		engineSpec.setTransmission(gmm112Dto.getTransmission());
		engineSpec.setTransmissionDescription(gmm112Dto.getTransmissionDescription());
		engineSpec.setGearShift(gmm112Dto.getGearShift());
		engineSpec.setGearShiftDescription(gmm112Dto.getGearShiftDescription());
		engineSpec.setDisplacement(gmm112Dto.getDisplacement());
		engineSpec.setDisplacementComment(gmm112Dto.getDisplacementComment());
		engineSpec.setEnginePrototypeCode(gmm112Dto.getEnginePrototypeCode());
		engineSpec.setPlantCode(gmm112Dto.getPlantCode());
		engineSpec.setMissionModelYearCode(gmm112Dto.getMissionModelYearCode());
		engineSpec.setModelYearCode(gmm112Dto.getMissionModelYearCode());
		engineSpec.setModelCode(gmm112Dto.getModelCode());
		engineSpec.setModelTypeCode(gmm112Dto.getModelTypeCode());
		engineSpec.setModelOptionCode(gmm112Dto.getModelOptionCode());
	    engineSpec.setModelYearDescription(gmm112Dto.getModelYearDescription());
		return engineSpec;
	}
}
