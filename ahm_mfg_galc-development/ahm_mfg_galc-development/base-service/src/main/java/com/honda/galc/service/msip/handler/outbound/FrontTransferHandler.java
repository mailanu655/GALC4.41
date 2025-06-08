package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.service.msip.dto.outbound.FrontTransferDto;
import com.honda.galc.service.msip.property.outbound.FrontTransferPropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class FrontTransferHandler extends BaseMsipOutboundHandler<FrontTransferPropertyBean>{
	String sendFileName;
	String errorMsg = null;
	Boolean isError = false;

	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FrontTransferDto> fetchDetails(Date startTimestamp, int duration) {
		Timestamp sTs = new Timestamp(startTimestamp.getTime());
		Timestamp endTs = getTimestamp(sTs, duration);
		List<FrontTransferDto> result = new ArrayList<FrontTransferDto>();
		try{		
			List<Object[]> details = getDao(InstalledPartDao.class).findByPartName(getPropertyBean().getPartName(), sTs, endTs);		
			return convertToDto(details);
		}catch(Exception e){
			getLogger().error("Error when try to convert date for FrontTransfer Interface " + e.getMessage());
			e.printStackTrace();
			result.clear();
			FrontTransferDto dto = new FrontTransferDto();
			dto.setErrorMsg("Error when try to convert date for FrontTransfer Interface " + e.getMessage());
			dto.setIsError(true);
			result.add(dto);
			return result;
		}
	}
	
	private List<FrontTransferDto> convertToDto(List<Object[]> results) {
		List<FrontTransferDto> dtoList = new ArrayList<FrontTransferDto>();
		for(Object[] objects : results) {
			FrontTransferDto dto = new FrontTransferDto();
			dto.setInstallTs(convertIfNull(objects[7], ""));
			dto.setModelCode(convertIfNull(objects[1], ""));
			dto.setModelType(convertIfNull(objects[2], ""));
			dto.setModelYear(convertIfNull(objects[0], ""));
			dto.setProdDate(convertIfNull(objects[4], ""));
			dto.setSerialNo(convertIfNull(objects[6], ""));
			dto.setShipDate(convertIfNull(objects[4], ""));
			dto.setVin(convertIfNull(objects[3], ""));
			dtoList.add(dto);
		}
		return dtoList;		
	}
}
	
