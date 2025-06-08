package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.openjpa.util.OpenJPAException;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.Giv706Dto;
import com.honda.galc.service.msip.property.outbound.Giv706PropertyBean;
import com.honda.galc.service.productionlot.ProductionLotService;
import com.honda.galc.util.OIFConstants;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Giv706Handler extends BaseMsipOutboundHandler<Giv706PropertyBean> {

	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";	
	String errorMsg = null;
	Boolean isError = false;
	
	Boolean JapanVINLeftJustified = false;
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Giv706Dto> fetchDetails() {
		List<Giv706Dto> dtoList = new ArrayList<Giv706Dto>();
		try{
			Boolean isTransmissionPlant	= getPropertyBean().getIsTransmissionPlant();
			JapanVINLeftJustified = getPropertyBean().getJapanVinLeftJustified();
			if (!isTransmissionPlant )
			{
				return exportRecords();
			}
			else
			{
				return executeProcessingBody();
			}
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			Giv706Dto dto = new Giv706Dto();
			dto.setErrorMsg("Error whe executing the Processing Body Task: " + e.getMessage());
			e.printStackTrace();
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	private List<Giv706Dto> exportRecords() {
		List<Giv706Dto> result			=	new ArrayList<Giv706Dto>();
		// Track the number of records being written to the interface file
		// get processing body data from each line other than current
		String[] activeLineURLs = getPropertyBean().getActiveLineUrls();
		List<String> outputRecords = new ArrayList<String>();
		for (String activeLineURL : activeLineURLs) {
			ProductionLotService productionLotService = HttpServiceProvider
					.getService(activeLineURL
							+ HTTP_SERVICE_URL_PART, ProductionLotService.class);
			List<Object[]> processingBodyList = productionLotService.getProcessingBody(getComponentId());
			result.addAll(convertToNonTransDto(processingBodyList));
		}
		// get data from the current line
		if (getPropertyBean().getIncludeActiveLine()) {
			ProductionLotService service = ServiceFactory
					.getService(ProductionLotService.class);
			outputRecords.clear();
			List<Object[]> processingBodyList = service.getProcessingBody(getComponentId());
			result.addAll(convertToNonTransDto(processingBodyList));
		}
		return result;
	}
	
	/**
	 * Method to get the processing body result
	 */
	private List<Giv706Dto> executeProcessingBody()
	{
		List<Giv706Dto> result			=	new ArrayList<Giv706Dto>();
		try {
			getLogger().info( "Starting the Processing Body Task" );
			final String[]	activeLineUrl 		=	getPropertyBean().getActiveLineUrls();
			final String 	processPoint		=	getPropertyBean().getProcessPoint();
			final String	trackingStatus		=	getPropertyBean().getTrackingStatus();	
			for (String activeLine : activeLineUrl)
			{
				getLogger().info( "Process the line " + activeLine );
				//get the production result service 
				ProductionLotService productionLotService = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ProductionLotService.class);
				
				getLogger().info( "Get the Processing Body data... " );
				List<Object []> processingBodyResult	=	productionLotService.getProcessingBody( processPoint, trackingStatus );
				result.addAll(convertToDTO(processingBodyResult));
			}
			getLogger().info( "Finish Processing Body Task" );
		} catch ( OpenJPAException px )
		{
			getLogger().info("Error whe executing the Processing Body Task: " + px.getMessage());
			result.clear();
			Giv706Dto dto = new Giv706Dto();
			dto.setErrorMsg("Error whe executing the Processing Body Task: " + px.getMessage());
			dto.setIsError(true);
			result.add(dto);
			return result;
		}
		return result;
	}
	
	private List<Giv706Dto> convertToDTO(List<Object[]> processingBodyResult) {
		int count = 1;
		List<Giv706Dto> dtoList = new ArrayList<Giv706Dto>();
		for (Object[] body : processingBodyResult)
		{
			Giv706Dto processingBody	=	new Giv706Dto();
			processingBody.setPlanCode					(	body[0].toString()	);
			processingBody.setLineNumber				(	body[1].toString()	);
			processingBody.setInHouseProcessLocation	(	body[2].toString()	);
			processingBody.setVinNumber					(	ProductNumberDef.justifyJapaneseVIN(body[3].toString(), JapanVINLeftJustified.booleanValue()));
			processingBody.setProductionSequenceNumber	(	body[4].toString()	);
			processingBody.setAlcActualTimestamp		(	body[5].toString()	);
			processingBody.setProductSpecCode			(	body[6].toString()	);
			processingBody.setKdLotNumber				(	body[7].toString()	);
			processingBody.setPartNumber				(	body[8] == null ? "" : body[8].toString());
			processingBody.setPartColorCode				(	body[9] == null ? "" : body[9].toString());
			processingBody.setOnSequenceNumber			(	String.format( "%05d", count)	);
			count++;
			dtoList.add( processingBody );
		}
		return dtoList;
	}
	
	private List<Giv706Dto> convertToNonTransDto(List<Object[]> processingBodyList) {
		// Result set returns the following fields :
		// 0 - Plan_Code
		// 1 - Line_No
		// 2 - Process_Location
		// 3 - Product_ID
		// 4 - Production_Lot
		// 5 - Actual_Timestamp
		// 6 - Product_Spec_Code
		// 7 - KD_Lot_Number
		List<Giv706Dto> result = new ArrayList<Giv706Dto>(); 
		if(processingBodyList != null && !processingBodyList.isEmpty()) {
			int recordCount = 0;
			SimpleDateFormat stsf1 = new SimpleDateFormat("yyyyMMddHHmmss");
			for (Object[] objArr : processingBodyList) {
				Giv706Dto processingBody	=	new Giv706Dto();
				recordCount++;
				processingBody.setPlanCode(objArr[0].toString());
				processingBody.setLineNumber(objArr[1].toString());
				processingBody.setInHouseProcessLocation(objArr[2].toString());
				processingBody.setVinNumber(ProductNumberDef.justifyJapaneseVIN(objArr[3].toString(), JapanVINLeftJustified.booleanValue()));
				processingBody.setProductionSequenceNumber(objArr[4].toString());
				Timestamp ts = (Timestamp) objArr[5];
				processingBody.setAlcActualTimestamp(stsf1.format(ts));
				processingBody.setProductSpecCode(objArr[6].toString());
				processingBody.setKdLotNumber(objArr[7].toString());	
				processingBody.setOnSequenceNumber(String.format("%05d", recordCount));	//On-Seq-No. (5)
				result.add(processingBody);
			}
		}
		return result;
	}

}
