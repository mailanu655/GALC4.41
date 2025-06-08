package com.honda.galc.oif.task;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.LotTraceabilityDao;
import com.honda.galc.entity.product.LotTraceability;
import com.honda.galc.entity.product.LotTraceabilityId;
import com.honda.galc.oif.dto.LotTraceabilityDTO;
import com.honda.galc.oif.dto.PurchaseContractDTO;
import com.honda.galc.oif.dto.PurchaseContractHeaderDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>LotTraceabilityTask Class description</h3>
 * <p> LotTraceabilityTask description </p>
 * 
 *  Message Source: GCCS/MEI 
 *  Message Destination: 2SD-F-GALC 
 *  Interface ID: OSLAS4#PMCGAL#LOTTRC 
 *  Run Frequency---------------------------------------------------- 
 *  Real-time via native MQ. 
 *  Business Purpose------------------------------------------------ 
 *  Allow GALC to store supplier part information in the event of part quality issues.
 *  MEI sends a message to GALC to indicate supplier information related to specific parts and the KD (VIN) that those
 *   parts are associated to.
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>VB</TH>
 * <TH>03/04/2015</TH>
 * <TH>0.1</TH>
 * <TH>None</TH>
 * <TH>Create the interface task</TH>
 * </TR>
 *
 * </TABLE>
 *    
 * @author Vivek Bettada 
 * @since March 04, 2015
*/


public class LotTraceabilityTask extends OifTask<PurchaseContractDTO> 
	implements IEventTaskExecutable {
	
	protected String[] aReceivedFileList;
	
	protected OIFSimpleParsingHelper<LotTraceabilityDTO> parseHelper; 
	
	public LotTraceabilityTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			processRecords();
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
		
	protected void initParseHelper() { 
//	Get configured parsing data 
		String parseDefs = getProperty("PARSE_LINE_DEFS");
		parseHelper = new OIFSimpleParsingHelper<LotTraceabilityDTO>(LotTraceabilityDTO.class, parseDefs, logger);
		parseHelper.getParsingInfo();
	}
	
	
	//Need method to get data off the queue and do something with it.
	protected void processRecords(){
		logger.info("Start to process LotTraceability");
		refreshProperties();
		
		aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
		
		if(aReceivedFileList == null){
			return;
		}
		//Get configured parsing data
		initParseHelper();

		
		//		Process file(s) and update Lot traceability
		for (int count=0; count<aReceivedFileList.length; count++) {
			String receivedFile = aReceivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
			if (receivedRecords.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: " + receivedFile);
				continue;
			}

			if (receivedRecords.size() < 1) {
				logger.error("No detail records?: " + receivedFile);
				errorsCollector.error("No detail records in received file? " + receivedFile);
				continue;
			}

			List<String> detailRecs = receivedRecords.subList(1, receivedRecords.size());
			List<LotTraceabilityDTO> lotTraceDtoList = new ArrayList<LotTraceabilityDTO>();
			LotTraceabilityDao ltDao = ServiceFactory.getDao(LotTraceabilityDao.class);
			for(String receivedRecord : detailRecs) {
				LotTraceabilityDTO ltDtlDTO = new LotTraceabilityDTO();
				parseHelper.parseData(ltDtlDTO, receivedRecord);
				String lsn = ltDtlDTO.getLsn();
				String kdLot = ltDtlDTO.getKdLotNumber();
				if(StringUtils.isEmpty(lsn) || StringUtils.isEmpty(kdLot))  {
					logger.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
					errorsCollector.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
				} else {
					LotTraceability lotTrace = ltDao.findById(lsn, kdLot);
					if(lotTrace == null)  {
						lotTrace = createLotTrace(ltDtlDTO);
					}
					updateLotTrace(lotTrace, ltDtlDTO);
					ltDao.save(lotTrace);

					lotTraceDtoList.add(ltDtlDTO);
				}
			}
			logger.info(" Lot Traceability record saved; file processed: " + receivedFile);
		}
			
	}
	
	
	public LotTraceability createLotTrace(LotTraceabilityDTO ltDtlDTO)  {
		
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
			logger.error(e, "Cannot parse capture date/time:" + sBuf); 
		}
		return lotTrace;
	}

	
	public void updateLotTrace(LotTraceability lotTrace, LotTraceabilityDTO ltDtlDTO)  {
		
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
			logger.error(e, "Cannot parse capture date/time:" + sBuf); 
		}
	}

}
