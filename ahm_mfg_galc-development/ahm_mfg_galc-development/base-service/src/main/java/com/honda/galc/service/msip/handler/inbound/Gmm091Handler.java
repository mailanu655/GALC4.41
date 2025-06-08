package com.honda.galc.service.msip.handler.inbound;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.oif.FifCodeChoicesDao;
import com.honda.galc.entity.fif.FifCodeChoices;
import com.honda.galc.entity.fif.FifCodeChoicesId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gmm091Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gmm091Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm091Dto> {

	public Gmm091Handler() {
	}

	public boolean execute(List<Gmm091Dto> dtoList) {
		try {
			processFifCodeChoices(dtoList);
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void processFifCodeChoices(List<Gmm091Dto> fifCodeChoicesDtoList) throws ParseException {
		getLogger().info("start to process CodeChoices");

		// Process files(s)
		List<Gmm091Dto> fifCodeChoicesDtoListNew = new ArrayList<Gmm091Dto>();
		for (Gmm091Dto receivedRecord : fifCodeChoicesDtoList) {
			if (receivedRecord.getDevSeqCd() == null || receivedRecord.getEfctBegDt() == null
					|| receivedRecord.getFifCode() == null || receivedRecord.getFifType() == null
					|| receivedRecord.getGroupCd() == null || receivedRecord.getModelCd() == null
					|| receivedRecord.getModelYear() == null || receivedRecord.getPlantCd() == null) {
				getLogger().info("The primary key is missing for this record: " + receivedRecord);

			} else {
				fifCodeChoicesDtoListNew.add(receivedRecord);
			}
		}

		// Update or Insert data
		for (Gmm091Dto fifCodeChoicesDto : fifCodeChoicesDtoListNew) {
			FifCodeChoices fifCodeChoices = deriveFifCodeChoices(fifCodeChoicesDto);
			ServiceFactory.getDao(FifCodeChoicesDao.class).save(fifCodeChoices);
		}
		getLogger().info(" FIF Code Choices saved; file processed: " + fifCodeChoicesDtoList);

	}
	

	public FifCodeChoices deriveFifCodeChoices(Gmm091Dto dto) {
		FifCodeChoices fifCodeChoices = new FifCodeChoices();
		fifCodeChoices.setId(deriveID(dto));
		fifCodeChoices.setEfctEndDt(dto.getEfctEndDt());
		fifCodeChoices.setFifDesc(dto.getFifDesc());
		return fifCodeChoices;
	}

	private FifCodeChoicesId deriveID(Gmm091Dto dto) {
		FifCodeChoicesId fifCodeChoicesId = new FifCodeChoicesId();
		fifCodeChoicesId.setDevSeqCd(dto.getDevSeqCd());
		fifCodeChoicesId.setEfctBegDt(dto.getEfctBegDt());
		fifCodeChoicesId.setFifCode(dto.getFifCode());
		fifCodeChoicesId.setFifType(dto.getFifType());
		fifCodeChoicesId.setGroupCd(dto.getGroupCd());
		fifCodeChoicesId.setModelCd(dto.getModelCd());
		fifCodeChoicesId.setModelYear(dto.getModelYear());
		fifCodeChoicesId.setPlantCd(dto.getPlantCd());
		return fifCodeChoicesId;
	}
}
