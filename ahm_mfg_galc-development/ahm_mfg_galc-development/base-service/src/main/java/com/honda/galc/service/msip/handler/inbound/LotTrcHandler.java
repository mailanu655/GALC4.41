package com.honda.galc.service.msip.handler.inbound;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.LotTraceabilityDao;
import com.honda.galc.entity.product.LotTraceability;
import com.honda.galc.entity.product.LotTraceabilityId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.LotTrcDto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class LotTrcHandler extends BaseMsipInboundHandler<BaseMsipPropertyBean, LotTrcDto> {
	
	public LotTrcHandler() {}

	public boolean execute(List<LotTrcDto> dtoList) {
		try {
			//update or insert data
			LotTraceabilityDao ltDao = ServiceFactory.getDao(LotTraceabilityDao.class);
			for(LotTrcDto dto : dtoList) {
				String lsn = dto.getLsn();
				String kdLot = dto.getKdLotNumber();
				if(StringUtils.isEmpty(lsn) || StringUtils.isEmpty(kdLot))  {
					getLogger().emergency("The primary key is missing for this record: " 
							+ dto.toString());
				} else {
					LotTraceability lotTrace = ltDao.findById(lsn, kdLot);
					if(lotTrace == null)  {
						lotTrace = createLotTrace(dto);
					}
					updateLotTrace(lotTrace, dto);
					ltDao.save(lotTrace);
				}
			}
			getLogger().info(" Lot Traceability record saved; data processed");
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public LotTraceability createLotTrace(LotTrcDto ltDtlDTO)  {
		
		if(ltDtlDTO == null)  return null;
		
		LotTraceability lotTrace = new LotTraceability();
		String lsn = ltDtlDTO.getLsn();
		String kdLot = ltDtlDTO.getKdLotNumber();
		LotTraceabilityId lotTraceId = new LotTraceabilityId(lsn, kdLot);
		lotTrace.setPartNum(ltDtlDTO.getPartNum());
		lotTrace.setPartColor(ltDtlDTO.getPartQty());
		lotTrace.setId(lotTraceId);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuffer sBuf = new StringBuffer();
		sBuf.append(ltDtlDTO.getCaptureDate().trim())
								.append(ltDtlDTO.getCaptureTime().trim());
		try {
			Date captureDate = dateFormatter.parse(sBuf.toString());
			lotTrace.setCaptureDate(captureDate);
		} catch (ParseException e) {
			getLogger().error(e, "Cannot parse capture date/time:" + sBuf); 
		}
		return lotTrace;
	}

	
	public void updateLotTrace(LotTraceability lotTrace, LotTrcDto ltDtlDTO)  {
		
		if(ltDtlDTO == null || lotTrace == null)  return;
		lotTrace.setPartNum(ltDtlDTO.getPartNum());
		lotTrace.setPartColor(ltDtlDTO.getPartQty());
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuffer sBuf = new StringBuffer();
		sBuf.append(ltDtlDTO.getCaptureDate().trim())
								.append(ltDtlDTO.getCaptureTime().trim());
		try {
			Date captureDate = dateFormatter.parse(sBuf.toString());
			lotTrace.setCaptureDate(captureDate);
		} catch (ParseException e) {
			getLogger().error(e, "Cannot parse capture date/time:" + sBuf); 
		}
	}
}
