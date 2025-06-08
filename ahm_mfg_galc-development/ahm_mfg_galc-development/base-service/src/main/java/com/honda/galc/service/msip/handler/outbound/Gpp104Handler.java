package com.honda.galc.service.msip.handler.outbound;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.Gpp104Dto;
import com.honda.galc.service.msip.property.outbound.Gpp104PropertyBean;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gpp104Handler extends BaseMsipOutboundHandler<Gpp104PropertyBean> {
	private final static String DATE_FORMAT = "yyyyMMdd";
	private final static String TIME_FORMAT = "HHmmss";
	private final static Integer OFF_FLAG = 2;
	private final static String MINUS_FLAG = "0";
	
	@SuppressWarnings("unchecked")
	public List<Gpp104Dto> fetchDetails()  {
		getLogger().info("Inside List<Gpp104Dto> fetchDetails ");
		getLogger().info("Step in the Transmission Production Progress Interface: " + getComponentId());
		List<Gpp104Dto> result = new ArrayList<Gpp104Dto>();
		// General properties	
		final String processLocation = getPropertyBean().getProcessLocation();
		final String propertyDate = getPropertyBean().getDate();
		final String processPointAmOn = getPropertyBean().getProcessPointAmOn();
		final String processPointAmOff = getPropertyBean().getProcessPointAmOff();
		final String plantCode = getPropertyBean().getPlantCode();
		
		ProductionLotDao productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		Date createDate = null;
		try {
			createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(propertyDate);

			List<Object> listTransmission = productionLotDao.getProductionProgress(processLocation,
							plantCode, createDate, processPointAmOn, processPointAmOff);

			// copy properties from entity to a DTO
			Date actualDate = new Date();

			for (Object object : listTransmission) {
				Gpp104Dto productionProgressDTO = generateProductionProgressDto(object, productionLotDao, actualDate);
				result.add(productionProgressDTO);
			}
			return result;
		}
		catch(Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			result.clear();
			Gpp104Dto dto = new Gpp104Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + ex.getMessage());
			dto.setIsError(true);
			result.add(dto);
			return result;
		}
		
	}
	
	private Gpp104Dto generateProductionProgressDto(Object object, ProductionLotDao productionLotDao,
			Date actualDate){
		final Integer productionCountOff = (Integer) ((Object[]) object)[12];
		ProductionLot productionLot = productionLotDao.findByKey((String) ((Object[]) object)[16]);

		final Gpp104Dto productionProgressDTO = new Gpp104Dto();
		productionProgressDTO.setPlantCode(productionLot.getPlanCode());
		
		productionProgressDTO.setLineNo(productionLot.getLineNo());
		productionProgressDTO.setProcessLocation(productionLot.getProcessLocation());
		productionProgressDTO.setOnOffFlag(OFF_FLAG + "");
		productionProgressDTO.setKdLotNo(productionLot.getKdLotNumber());
		productionProgressDTO.setProdSeqNo(productionLot.getLotNumber());
		productionProgressDTO.setMbpn((String) ((Object[]) object)[5]);
		productionProgressDTO.setHesColor((String) ((Object[]) object)[6]);
		productionProgressDTO.setMtoc(productionLot.getProductSpecCode());
		productionProgressDTO.setResultQty(String.format("%8s", productionCountOff.toString()).replace(' ', '0'));
		productionProgressDTO.setCreatedDate(new SimpleDateFormat(DATE_FORMAT).format(actualDate));
		productionProgressDTO.setCreatedTime(new SimpleDateFormat(TIME_FORMAT).format(actualDate));
		productionProgressDTO.setMinusFlag(MINUS_FLAG);
		productionProgressDTO.setFiller("");
		
		return productionProgressDTO;
	}
}
