package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ProductionResultService;
import com.honda.galc.service.msip.dto.outbound.Giv730Dto;
import com.honda.galc.service.msip.property.outbound.Giv730PropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Giv730Handler extends BaseMsipOutboundHandler<Giv730PropertyBean> {
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	String errorMsg = null;
	Boolean isError = false;

	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Giv730Dto> fetchDetails(Date startTimestamp, int duration) {
		Timestamp sTs = new Timestamp(startTimestamp.getTime());
		Timestamp endTs = getTimestamp(sTs, duration);
		List<Giv730Dto> dtoList = new ArrayList<Giv730Dto>();
		try{
			return exportRecord(sTs, endTs);
		}catch(Exception e){
			dtoList.clear();
			Giv730Dto dto = new Giv730Dto();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}		
	}
	
	boolean isEmptyResult(Map<String, List<Object[]>> asynchProductionReports)  {
		
		boolean isEmpty = true;;
		if (asynchProductionReports == null || asynchProductionReports.isEmpty()) {
			return true;
		}
		else {
			Set<String> mapSet = asynchProductionReports.keySet();
			for(String planCode : mapSet)  {
				List<Object[]> data = asynchProductionReports.get(planCode);
				//data is a list of arrays per plan code; each list element is an array
				//each array has product_spec_code and associated count
				for(Object[] specCode : data)  {
					if(specCode != null && specCode.length != 0)  {
						isEmpty = false;
						break;
					}
				}
				if(!isEmpty)  break;
			}
		}
		return isEmpty;
	}
	
	public List<Giv730Dto> exportRecord(Timestamp startTime, Timestamp endTime) {
		// get the current date and time
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat timeFormat = new SimpleDateFormat("HHmmss");
		Timestamp now = getCurrentTime(); 
		String dateStr = dateFormat.format(now);
		String timeStr = timeFormat.format(now);
		List<Giv730Dto> resultList = new ArrayList<Giv730Dto>();

		// support multiple lines
		String[] activeLines = getPropertyBean().getActiveLines();
		if(activeLines == null || activeLines.length == 0) {
			getLogger().error("Needed configuration is missing [active lines: " + activeLines.toString() +"]");
			Giv730Dto dto = new Giv730Dto();
			dto.setErrorMsg("Exception formatting date in Bearing Usage Interface");
			dto.setIsError(true);
			resultList.add(dto);
			return resultList;
		}

		try {
			for(String line : activeLines) {
				
				ProductionResultService service = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART, ProductionResultService.class);
				if(service == null) {
					getLogger().error("Can not access service for Line " + line);
					continue;
				}
				
				Map<String, List<Object[]>> asynchProductionReports = service.getAsynchProductionReport(getComponentId(), startTime, endTime);
				if(isEmptyResult(asynchProductionReports))  {
					updateLastProcessTimestamp(now);
					return exceptionLogging("There is no data for Asynchronous Production Report GIV730.", 
							"There is no data for Asynchronous Production Report GIV730.");
				}
				Giv730Dto dto = null;
				
				Iterator<String> it = asynchProductionReports.keySet().iterator();
				String planCode = "";
				List<Object[]> data = null;
				

				//loop through PLAN_CODE
				while (it.hasNext()) {
					planCode = it.next().toString();
					data = asynchProductionReports.get(planCode);
					for (Object[] obj : data){
						dto = new Giv730Dto();
						if(!generateDto(obj, dto, planCode, dateStr, timeStr))
							continue;
						resultList.add(dto);
					}
				}
			}			
			updateLastProcessTimestamp(now);
			getLogger().info("Finish Asynchronous Production Report GIV730.");		
			return resultList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return exceptionLogging("Error processing Asynchronous Production Report GIV730.", 
					"Error processing Asynchronous Production Report GIV730.");
		}
	}
	
	private boolean generateDto(Object[] obj, Giv730Dto dto, String planCode, String dateStr, String timeStr){
		String lotNo = (String)obj[1];
		if(StringUtils.isBlank(lotNo))  {
			String errMsg = "Lot No. not found for this record, skipping: " + dto.toString();
			getLogger().error(errMsg);
			return false;
		}
		dto.setPlanCode(planCode);
		dto.setProductSpecCode(obj[0].toString().substring(0, 29)); //Product_Spec_Code length=30, MBPN+HES_COLOR=29
		int qty = (Integer)obj[2];
		String sQty = String.format("%05d", qty);
		dto.setProductionQuantity(sQty);
		dto.setProductionDate(dateStr);
		dto.setProductionTime(timeStr);
		dto.setLotNumber((String)obj[1]);
		return true;
	}
	
	private List<Giv730Dto> exceptionLogging(String logMsg, String errorMsg){
		List<Giv730Dto> resultList = new ArrayList<Giv730Dto>();
		getLogger().error("Error processing Asynchronous Production Report GIV730.");
		resultList.clear();
		Giv730Dto dto = new Giv730Dto();
		dto.setErrorMsg("Error processing Asynchronous Production Report GIV730.");
		dto.setIsError(true);
		resultList.add(dto);
		return resultList;
	}
}
