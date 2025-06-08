package com.honda.galc.service.msip.handler.inbound;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.oif.FifCodeGroupsDao;
import com.honda.galc.entity.fif.FifCodeGroups;
import com.honda.galc.entity.fif.FifCodeGroupsId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gmm090Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gmm090Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm090Dto> {

	public Gmm090Handler() {
	}

	public boolean execute(List<Gmm090Dto> dtoList) {
		try {
			processFifCodeGroups(dtoList);
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void processFifCodeGroups(List<Gmm090Dto> fifCodeGroupsDtoList) throws ParseException {
		getLogger().info("start to process CodeGroups");
		List<Gmm090Dto> fifCodeGroupsDTOListNew = new ArrayList<Gmm090Dto>();

		for (Gmm090Dto receivedRecord : fifCodeGroupsDtoList) {
			if (receivedRecord.getBaseType() == null || receivedRecord.getDevSeqCd() == null
					|| receivedRecord.getEfctBegDt() == null || receivedRecord.getFifType() == null
					|| receivedRecord.getGroupCode() == null || receivedRecord.getModelCd() == null
					|| receivedRecord.getModelYear() == null || receivedRecord.getPlantCd() == null) {
				getLogger().info("The primary key is missing for this record: " + receivedRecord);

			} else {
				fifCodeGroupsDTOListNew.add(receivedRecord);
			}
		}
		// Update or Insert data
		for (Gmm090Dto fifCodeGroupsDto : fifCodeGroupsDTOListNew) {
			FifCodeGroups fifCodeGroups = deriveFifCodeGroups(fifCodeGroupsDto);
			ServiceFactory.getDao(FifCodeGroupsDao.class).save(fifCodeGroups);
		}
		getLogger().info(" FIF Code Groups saved; file processed: " + fifCodeGroupsDtoList);

	}
	
	public FifCodeGroups deriveFifCodeGroups(Gmm090Dto dto) {
		FifCodeGroups fifCodeGroups = new FifCodeGroups();
		fifCodeGroups.setId(deriveID(dto));
		fifCodeGroups.setEfctEndDt(dto.getEfctEndDt());
		fifCodeGroups.setFifLength(dto.getFifLength());
		fifCodeGroups.setFifOffset(dto.getFifOffset());
		fifCodeGroups.setGroupDesc(dto.getGroupDesc());
		fifCodeGroups.setRequired(dto.getRequired());
		return fifCodeGroups;
	}

	private FifCodeGroupsId deriveID(Gmm090Dto dto) {
		FifCodeGroupsId fifCodeGroupsId = new FifCodeGroupsId();
		fifCodeGroupsId.setBaseType(dto.getBaseType());
		fifCodeGroupsId.setDevSeqCd(dto.getDevSeqCd());
		fifCodeGroupsId.setEfctBegDt(dto.getEfctBegDt());
		fifCodeGroupsId.setFifType(dto.getFifType());
		fifCodeGroupsId.setGroupCd(dto.getGroupCode());
		fifCodeGroupsId.setModelCd(dto.getModelCd());
		fifCodeGroupsId.setModelYear(dto.getModelYear());
		fifCodeGroupsId.setPlantCd(dto.getPlantCd());
		return fifCodeGroupsId;
	}
}
