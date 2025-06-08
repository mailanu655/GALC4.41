package com.honda.galc.service.msip.handler.outbound;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.dto.outbound.Gal104Dto;
import com.honda.galc.service.msip.property.outbound.Gal104PropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gal104Handler extends BaseMsipOutboundHandler<Gal104PropertyBean> {

	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Gal104Dto> fetchDetails() {
		List<Gal104Dto> dtoList = new ArrayList<Gal104Dto>();
		try{
			//retrieve and merge data
			String[] activeLines = getPropertyBean().getActiveLineUrls();
			HashMap<String, Gal104Dto> resultMap = new LinkedHashMap<String, Gal104Dto>();					
			
			//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
			if(activeLines == null || activeLines.length == 0) {
				getLogger().error("Needed configuration is missing [" + "Counting  active lines: " + activeLines.toString() +"]");
				return null;
			}
			
			for(String line : activeLines) {
				FrameDao frameDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,FrameDao.class);
				DailyDepartmentScheduleDao dailyDepartmentScheduleDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,DailyDepartmentScheduleDao.class);

				if(frameDao==null) {
					getLogger().error("Can not access Service DAO for Line " + line + ", move to next Line");
					continue;
				}
				resultMap = exportMap(frameDao, dailyDepartmentScheduleDao, resultMap);	
			}
			getLogger().info("Finished Vin in VQ Task");
			
			return new ArrayList<Gal104Dto>(resultMap.values());
			
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			Gal104Dto dto = new Gal104Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}		
	}
	
	
	private HashMap<String, Gal104Dto> exportMap(FrameDao frameDao, DailyDepartmentScheduleDao dailyDepartmentScheduleDao,
			HashMap<String, Gal104Dto> resultMap){
		List<Object[]> resultSetList = frameDao.getVinInVQ(getPropertyBean().getTrackingStatus());
		Gal104Dto dto = null;
		if (resultSetList!=null) {
			for(int i=0; i<resultSetList.size(); i++) {
				Object[] lineObj = resultSetList.get(i);
				String key = convertIfNull(lineObj[0], "");
				
				Date afOffDate = (Date) lineObj[13];
				Date nextWorkday = dailyDepartmentScheduleDao.getNextProductionDate(afOffDate);	
				
				if(!resultMap.containsKey(key)) {
					dto = new Gal104Dto();
					dto.setShipperId(getPropertyBean().getShipperId());
					dto.setVersionNumber(getPropertyBean().getVersionNumber());
					dto.setVin(ProductNumberDef.justifyJapaneseVIN(convertIfNull(lineObj[0], ""), getPropertyBean().getJapanVinLeftJustified().booleanValue()));
					dto.setCondensedKdLot(convertIfNull(lineObj[1], ""));
					dto.setCondensedProductionLot(convertIfNull(lineObj[2], ""));
					dto.setSalesModelCode(convertIfNull(lineObj[3], ""));
					dto.setSalesModelTypeCode(convertIfNull(lineObj[4], ""));
					dto.setSalesModelOption(convertIfNull(lineObj[5], ""));
					dto.setSalesExtColorCode(convertIfNull(lineObj[6], ""));
					dto.setSalesIntColorCode(convertIfNull(lineObj[7], ""));
					dto.setManufacturerExtColorCode(convertIfNull(lineObj[8], ""));
					dto.setManufacturerModelTypeCode(convertIfNull(lineObj[9], ""));
					dto.setManufacturerModelOptionCode(convertIfNull(lineObj[10], ""));
					dto.setManufacturerExtColorCode(convertIfNull(lineObj[11], ""));
					dto.setManufacturerIntColorCode(convertIfNull(lineObj[12], ""));
					dto.setExpectedShipDate(nextWorkday.toString().replace("-", ""));
					dto.setLastWorkingDate(convertIfNull(lineObj[14], "").substring(0, 10).replace("-", ""));
					dto.setLastWorkingTime(convertIfNull(lineObj[14], "").substring(11, 19).replace(":", ""));
					dto.setFiller(" ");
					resultMap.put(key, dto);
				}
			}
		}
		return resultMap;
	}
	

}
