package com.honda.galc.oif.task;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.oif.dto.PurchaseContractDTO;
import com.honda.galc.oif.dto.PurchaseContractHeaderDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>GAL102HPurchaseContractTask Class description</h3>
 * <p> GAL102HPurchaseContractTask description </p>
 * 
 *  Message Source: AHM-MF 
 *  Message Destination: 2SD-F-GALC 
 *  Interface ID: D--GMG#PMCGAL#GAL102H 
 *  Run Frequency---------------------------------------------------- 
 *  Received when AH issues purchase order to order sales by the model types. Sent day after PMC PC transmits PIN file. 
 *  Business Purpose------------------------------------------------ 
 *  PCs are necessary for two fundamental reasons:  First, they are the legal commitment to facilitate a purchase between
 *  AHM and PMC.  Second, AHM tracks the receipt of vehicles by purchase contract number.
 *   At the end of the month, AHM knows how many vehicles have or haven't been received against that PC number.
 *   PMC's shipments/invoice to AHM contains the PC numbers. 
 
 *  
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>VB</TH>
 * <TH>2/17/2015</TH>
 * <TH>0.1</TH>
 * <TH>None</TH>
 * <TH>Create the interface task</TH>
 * </TR>
 *
 * </TABLE>
 *    
 * @author Vivek Bettada 
 * @since February 17, 2015
*/


public class GAL102HPurchaseContractTask extends OifTask<PurchaseContractDTO> 
	implements IEventTaskExecutable {
	
//	The list of file names that are received from AHM-MF(MQ).
	protected String[] aReceivedFileList;
	
	protected OIFSimpleParsingHelper<PurchaseContractDTO> parseHelper; 
	protected OIFSimpleParsingHelper<PurchaseContractHeaderDTO> parseHelperHdr; 
	
	public static final int REC_ID_HDR = 1;
	public static final int REC_ID_DTL = 2;
	private int totalRecords=0;
	
	public GAL102HPurchaseContractTask(String name) {
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
			setIncomingJobCount(totalRecords, 0, aReceivedFileList);
			errorsCollector.sendEmail();
		}
	}
		
	protected void initParseHelper() { 
//	Get configured parsing data 
		String parseDefs = getProperty("PARSE_LINE_DEFS");
		parseHelper = new OIFSimpleParsingHelper<PurchaseContractDTO>(PurchaseContractDTO.class, parseDefs, logger);
		parseHelper.getParsingInfo();
		parseHelperHdr = new OIFSimpleParsingHelper<PurchaseContractHeaderDTO>(PurchaseContractHeaderDTO.class, parseDefs, logger);
		parseHelperHdr.getParsingInfo();
	}
	
	
	//Need method to get data off the queue and do something with it.
	protected void processRecords(){
		logger.info("Start to process GAL102HPurchaseContract");
		refreshProperties();
		
		aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
		
		if(aReceivedFileList == null){
			return;
		}
		String recordIdIndex = getProperty("RECORD_ID");
		if(recordIdIndex == null || "".equals(recordIdIndex.trim()))  {
			logger.error("RECORD_ID not set in configuration...\nexiting");
			setIncomingJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			return;
		}
		String[] recordId_cfg = recordIdIndex.split(",");
		if(recordId_cfg == null || recordId_cfg.length < 2)  {
			logger.error("RECORD_ID not set or not in correct format\nUsage: RECORD_ID\t14,3...\nexiting");
			setIncomingJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			return;
		}
		int recordId_start = Integer.parseInt(recordId_cfg[0].trim());
		int recordId_len = Integer.parseInt(recordId_cfg[1].trim());
		//Get configured parsing data
		initParseHelper();

		
//		Process file(s) and update Purchase Contract
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
				setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
				continue;
			}

			if (receivedRecords.size() < 2) {
				logger.error("No detail records?: " + receivedFile);
				errorsCollector.error("No detail records in received file? " + receivedFile);
				setIncomingJobStatus(OifRunStatus.NO_DETAIL_RECORDS_IN_RECEIVED_FILE);
				continue;
			}
			
			PurchaseContractHeaderDTO pcHeader = null;
			PurchaseContractDao pcDao = ServiceFactory.getDao(PurchaseContractDao.class);
			for(String recvRec : receivedRecords) {
				String sRecordId = recvRec.substring(recordId_start, recordId_start + recordId_len);
				int recId = 0;
				if(sRecordId != null && !"".equals(sRecordId.trim()))  {
					recId = Integer.parseInt(sRecordId.trim());
				}
				if(recId == REC_ID_HDR)  {
					String headerRec = recvRec;
					//process header record
					pcHeader = new PurchaseContractHeaderDTO();
					parseHelperHdr.parseData(pcHeader, headerRec);
				}
				else if(recId == REC_ID_DTL)  {
					totalRecords++;
					PurchaseContractDTO pcDtlDTO = new PurchaseContractDTO();
					parseHelper.parseData(pcDtlDTO, recvRec);
					String pcNo = pcDtlDTO.getPurchaseContractNumber();
					String pcSalesModelCode = pcDtlDTO.getSalesModelId();
					String pcModelType = pcDtlDTO.getSalesModelTypeCode();
					String pcOptionCode = pcDtlDTO.getSalesModelOptionCode();
					String pcExtColor = pcDtlDTO.getFactoryColorCode();
					String pcIntColor = pcDtlDTO.getSalesInteriorColorCode();
	
					PurchaseContract pContract = pcDao.findByContractNoAndSpecCode(pcNo, pcSalesModelCode, pcModelType,
							pcOptionCode, pcExtColor, pcIntColor);
					if(pContract == null)  {
						pContract = createPurchaseContract(pcDtlDTO);
					}
					updatePurchaseContract(pContract, pcDtlDTO, pcHeader);
					pcDao.save(pContract);
				}
			}
			logger.info(" Purchase Contract saved; file processed: " + receivedFile);
		}
				
	}
	
	
	public PurchaseContract createPurchaseContract(PurchaseContractDTO pcDtlDto)  {
		
		if(pcDtlDto == null)  return null;
		
		PurchaseContract pc = new PurchaseContract();
		pc.setPurchaseContractNumber(pcDtlDto.getPurchaseContractNumber());
		pc.setSalesModelCode(pcDtlDto.getSalesModelId());
		pc.setSalesModelTypeCode(pcDtlDto.getSalesModelTypeCode());
		pc.setSalesModelOptionCode(pcDtlDto.getSalesModelOptionCode());
		pc.setSalesExtColorCode(pcDtlDto.getFactoryColorCode());
		pc.setSalesIntColorCode(pcDtlDto.getSalesInteriorColorCode());
		return pc;
	}

	public void updatePurchaseContract(PurchaseContract pc, PurchaseContractDTO pcDtlDto, PurchaseContractHeaderDTO headerDto)  {
		
		if(pcDtlDto == null || pc == null)  return;
		Calendar now = GregorianCalendar.getInstance();
		pc.setCurrency(pcDtlDto.getCurrencyCode());
		pc.setCurrencySettlement(pcDtlDto.getCurrencySettlementCode());
		pc.setDeliveryFormCode(pcDtlDto.getDeliveryFormCode());
		pc.setOrderUnit(pcDtlDto.getOrderedUnit());
		pc.setShipper(headerDto.getShipper());
		pc.setSubsCode(pcDtlDto.getSubCode());
		pc.setProductGroupCode(headerDto.getProductGroup());
		pc.setReceiveDate(now.getTime());
		pc.setOrderDueDate(CommonUtil.convertDate(headerDto.getOrderDueDate()));
	
	}

}