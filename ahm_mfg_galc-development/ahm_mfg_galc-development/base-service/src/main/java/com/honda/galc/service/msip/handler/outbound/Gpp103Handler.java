package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.service.msip.dto.outbound.Gpp103Dto;
import com.honda.galc.service.msip.property.outbound.Gpp103PropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gpp103Handler extends BaseMsipOutboundHandler<Gpp103PropertyBean> {

	@SuppressWarnings("unchecked")
	@Override
	public List<Gpp103Dto> fetchDetails()  {
		List<Gpp103Dto> dtoList = new ArrayList<Gpp103Dto>();
		try {
			String ppIdOn=getPropertyBean().getProcessPointOn();
			String ppIdOff=getPropertyBean().getProcessPointOff();
			List<Object[]> details = getDao(ProductionLotDao.class).getListOfIncompleteLots(ppIdOn, ppIdOff);
			getLogger().info("details.size ::" + details.size());
			return convertToDto(details);
		} catch (Exception e) {
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			dtoList.clear();
			Gpp103Dto dto = new Gpp103Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	private List<Gpp103Dto> convertToDto(List<Object[]> results) {
		List<Gpp103Dto> dtoList = new ArrayList<Gpp103Dto>();
		for(Object[] objects : results) {
			Gpp103Dto dto = new Gpp103Dto();
			dto.setProductIdSubString(objects[0].toString());
			dto.setStartProductId(objects[1].toString());
			if(objects[2] != null)
				dto.setPlantCode(objects[2].toString());
			dto.setLineNo(objects[3].toString());
			dto.setProcessLocation(objects[4].toString());
			dto.setOnoff(objects[5].toString());
			dto.setAfOnSequenceNumber(objects[6].toString());
			dto.setProductSpecCode(objects[7].toString());
			dto.setProductId(objects[8].toString());
			dto.setCreateDate(objects[9].toString());
			dto.setCreateTime(objects[10].toString());
			dto.setLotSizeCalc(objects[11].toString());
			dtoList.add(dto);
		}
		return dtoList;		
	}
}
