package com.honda.galc.service.msip.handler.inbound;

import java.util.List;

import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpecId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gmm113Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gmm113Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm113Dto> {
	private FrameMTOCPriceMasterSpecDao frameMTOCPriceMasterSpecDao;

	public Gmm113Handler() {
	}

	public boolean execute(List<Gmm113Dto> dtoList) {

		try {
			processMTOCPriceSpecs(dtoList);
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	// Need method to get data off the queue and do something with it.
	public void processMTOCPriceSpecs(List<Gmm113Dto> dtoList) {
		getLogger().info("start to process MTOC Price Master");
		initMTOCPriceSpecData();
		// Update or insert data
		for (Gmm113Dto fpms : dtoList) {
			FrameMTOCPriceMasterSpec frameMTOCPriceMasterSpec = deriveFrameMTOCPriceMasterSpec(fpms);
			frameMTOCPriceMasterSpecDao.save(frameMTOCPriceMasterSpec);
			getLogger().info(" Frame Price Spec saved" + frameMTOCPriceMasterSpec);
		}
		getLogger().info(" Frame MTOC Price Master Specification saved; file processed: " + dtoList);
	}

	public void initMTOCPriceSpecData() {
		frameMTOCPriceMasterSpecDao = ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class);
	}
	
	public FrameMTOCPriceMasterSpec deriveFrameMTOCPriceMasterSpec(Gmm113Dto gmm113Dto) {
		FrameMTOCPriceMasterSpec frameMTOCPriceMasterSpec = new FrameMTOCPriceMasterSpec(); 
		frameMTOCPriceMasterSpec.setId(deriveID(gmm113Dto));
		frameMTOCPriceMasterSpec.setCurrency(gmm113Dto.getCurrency());
		frameMTOCPriceMasterSpec.setPrice(gmm113Dto.getPrice());
		frameMTOCPriceMasterSpec.setPriceType(gmm113Dto.getPriceType());
		frameMTOCPriceMasterSpec.setQuotationNo(gmm113Dto.getQuotationNo());
		return frameMTOCPriceMasterSpec;
	}

	public FrameMTOCPriceMasterSpecId deriveID(Gmm113Dto gmm113Dto){
		FrameMTOCPriceMasterSpecId frameMTOCPriceMasterSpecId = new FrameMTOCPriceMasterSpecId();
		frameMTOCPriceMasterSpecId.setPlantCodeFrame(gmm113Dto.getPlantCodeFrame());
		frameMTOCPriceMasterSpecId.setModelYearCode(gmm113Dto.getModelYearCode());
		frameMTOCPriceMasterSpecId.setModelCode(gmm113Dto.getModelCode());
		frameMTOCPriceMasterSpecId.setModelTypeCode(gmm113Dto.getModelTypeCode());
		frameMTOCPriceMasterSpecId.setModelOptionCode(gmm113Dto.getModelOptionCode());
		frameMTOCPriceMasterSpecId.setExtColorCode(gmm113Dto.getExtColorCode());
		frameMTOCPriceMasterSpecId.setIntColorCode(gmm113Dto.getIntColorCode());
		frameMTOCPriceMasterSpecId.setEffectiveDate(gmm113Dto.getEffectiveDate());
		return frameMTOCPriceMasterSpecId;
	}

}
