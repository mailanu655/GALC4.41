package com.honda.galc.service.msip.handler.inbound;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.entity.product.MissionSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gmm114Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gmm114Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm114Dto> {

	private MissionSpecDao missionSpecDao;

	public Gmm114Handler() {
	}

	public boolean execute(List<Gmm114Dto> dtoList) {
		try {

			processMissionMasterSpec(dtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}

	String makeProductSpecCode(Gmm114Dto missionSpec) {
		StringBuffer productSpecCode = new StringBuffer();
		String modelYearCode = missionSpec.getModelYearCode();
		String modelCode = missionSpec.getModelCode();
		String modelTypeCode = missionSpec.getModelTypeCode();
		boolean isValid = true;
		if (modelYearCode == null || modelYearCode.trim().length() == 0) {
			isValid = false;
		}
		if (modelCode == null || modelCode.trim().length() == 0) {
			isValid = false;
		}
		if (modelTypeCode == null || modelTypeCode.trim().length() == 0) {
			isValid = false;
		}
		if (isValid) {
			productSpecCode.append(modelYearCode).append(modelCode).append(modelTypeCode);
		}
		return productSpecCode.toString();
	}

	public void processMissionMasterSpec(List<Gmm114Dto> dtoList) {
		getLogger().info("start to process MissionMasterSpec");
		initMissionSpecData();
		List<Gmm114Dto> dtoListNew = new ArrayList<Gmm114Dto>();

		for (Gmm114Dto gmmDto : dtoList) {
			Gmm114Dto missionSpecDTO = new Gmm114Dto();
			String productSpecCode = makeProductSpecCode(gmmDto);
			if (productSpecCode.length() > 0) {
				missionSpecDTO.setProductSpecCode(productSpecCode);
				dtoListNew.add(missionSpecDTO);
				getLogger().info("Mission Spec added: " + missionSpecDTO);
			}
		}
		if (dtoListNew.size() > 0) {
			List<MissionSpec> missionSpecList = new ArrayList<MissionSpec>();
			for (Gmm114Dto missionSpecDto : dtoListNew) {
				MissionSpec missionSpec = deriveMissionSpec(missionSpecDto);
				missionSpecList.add(missionSpec);
			}
			missionSpecDao.saveAll(missionSpecList);
			getLogger().info(" Mission Spec List saved: " + missionSpecList.size() + " objects.");
		}
		getLogger().info("file processed: " + dtoList);
	}

	public void initMissionSpecData() {
		this.missionSpecDao = ServiceFactory.getDao(MissionSpecDao.class);
	}
	
	public MissionSpec deriveMissionSpec(Gmm114Dto missionSpecDto) {
		MissionSpec missionSpec = new MissionSpec(); 
		missionSpec.setProductSpecCode(missionSpecDto.getModelYearCode() + missionSpecDto.getModelCode() + missionSpecDto.getModelTypeCode());
		missionSpec.setPlantCode(missionSpecDto.getPlantCode());
		missionSpec.setModelYearCode(missionSpecDto.getModelYearCode());
		missionSpec.setModelCode(missionSpecDto.getModelCode());
		missionSpec.setModelTypeCode(missionSpecDto.getModelTypeCode());
		missionSpec.setModelYearDescription(missionSpecDto.getModelYearDescription());
		missionSpec.setModelYearDescription(missionSpecDto.getModelYearDescription());
		missionSpec.setMissionNoPrefix(missionSpecDto.getMissionNoPrefix());
		return missionSpec;
	}

}
