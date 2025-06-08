package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.service.msip.dto.outbound.RearDifferentialDto;
import com.honda.galc.service.msip.property.outbound.RearDifferentialPropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class RearDifferentialHandler extends BaseMsipOutboundHandler<RearDifferentialPropertyBean>{
	String sendFileName;
	String errorMsg = null;
	Boolean isError = false;

	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RearDifferentialDto> fetchDetails(Date startTimestamp, int duration) {
		Timestamp sTs = new Timestamp(startTimestamp.getTime());
		Timestamp endTs = getTimestamp(sTs, duration);
		List<RearDifferentialDto> result = new ArrayList<RearDifferentialDto>();
		try{		
			List<Object[]> details = getDao(InstalledPartDao.class).findByPartName(getPropertyBean().getPartName(), sTs, endTs);		
			return convertToDto(details);
		}catch(Exception e){
			getLogger().error("Error when try to convert date for RearDifferential Interface " + e.getMessage());
			e.printStackTrace();
			result.clear();
			RearDifferentialDto dto = new RearDifferentialDto();
			dto.setErrorMsg("Error when try to convert date for RearDifferential Interface " + e.getMessage());
			dto.setIsError(true);
			result.add(dto);
			return result;
		}
	}
	
	private List<RearDifferentialDto> convertToDto(List<Object[]> results) {
		List<RearDifferentialDto> dtoList = new ArrayList<RearDifferentialDto>();
		for(Object[] objects : results) {
			RearDifferentialDto dto = new RearDifferentialDto();
			dto.setInstallTs(objects[7].toString());
			dto.setModelCode(objects[1].toString());
			dto.setModelType(objects[2].toString());
			dto.setModelYear(objects[0].toString());
			dto.setProdDate(objects[4].toString());
			dto.setSerialNo(objects[6].toString());
			dto.setShipDate(objects[4].toString());
			dto.setVin(objects[3].toString());
			dtoList.add(dto);
		}
		return dtoList;		
	}
}
	
