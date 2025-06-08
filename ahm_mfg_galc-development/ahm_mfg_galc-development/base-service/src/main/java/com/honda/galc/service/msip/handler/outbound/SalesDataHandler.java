package com.honda.galc.service.msip.handler.outbound;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.service.msip.dto.outbound.SalesReturnsDto;
import com.honda.galc.service.msip.property.outbound.BaseSalesReturnsPropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class SalesDataHandler extends BaseSalesReturnsHandler<BaseSalesReturnsPropertyBean>{
	SalesDataHandler(){		
	}
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesReturnsDto> fetchDetails() {
		List<SalesReturnsDto> dtoList = new ArrayList<SalesReturnsDto>();
		try{
		setDataType(SALES);
		return getSalesReturnsData();		
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			SalesReturnsDto dto = new SalesReturnsDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
}
