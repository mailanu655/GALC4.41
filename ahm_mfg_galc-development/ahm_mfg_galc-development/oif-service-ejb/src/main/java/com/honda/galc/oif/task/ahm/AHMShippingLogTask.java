package com.honda.galc.oif.task.ahm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.enumtype.UploadStatusType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.oif.dto.BomDTO;
import com.honda.galc.oif.dto.BomExportDTO;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.oif.dto.ShippingLogExportDTO;
import com.honda.galc.oif.dto.ShippingLogDTO;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>AHMShippingLogTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> This class is to receive the shipping log from AHM. This file will be 
 *                sent from to HMA every night along with the Inventory file and the  
 *                daily shipping transaction file. Note that the interface file id is
 *                D--GMG#HMAGAL#AHM030. </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>LK</TD>
 * <TD>Jan 06, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Larry Karpov
 * @created Jan 06, 2015
 */
public class AHMShippingLogTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
	public static final String LOAD_STATUS = "LOAD_STATUS";
	public static final String OUTPUT_MESSAGE_LINE_LENGTH = "OUTPUT_MESSAGE_LINE_LENGTH";
	public static final String HEADER = "HEADER";
	public static final String TRAILER = "TRAILER";
	public static final String END_OF_DAY= "ED";
	private static final String FORMAT_DATE = "yyyy-MM-dd";
	private static final String DISTRIBUTION_LIST = "DISTRIBUTION_EMAIL_LIST";
	private static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	
	String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);
	String exportFileName =  getProperty(OIFConstants.EXPORT_FILE_NAME);
	Map<String,String> trackingStatusMap = null;
	
	int receivedRecordsCount = 0;
	int errorFileRecordCount = 0;
	int vinsProcessedCount = 0;
	String resultPath="";
	
	public AHMShippingLogTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			processAHMShippingLog();
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
	
	/**
	 * Receive file(s) from MQ.<br>
	 * @throws ParseException 
	 */
	private void processAHMShippingLog() throws ParseException {
		long startTime = System.currentTimeMillis();
				
		logger.info("start to process AHMShippingLog");
		ShippingLogPropertyBean propBean = PropertyService.getPropertyBean(ShippingLogPropertyBean.class, componentId);
		String interfaceId = propBean.getInterfaceId();
		Integer lineLength = propBean.getMessageLineLength();
		trackingStatusMap = propBean.getTrackingStatusMap();
//		Receive file from AHM(MQ);
		
		String[] aReceivedFileList = getFilesFromMQ(interfaceId, lineLength);
		if (aReceivedFileList != null && aReceivedFileList.length > 0) {
        	logger.info("File received = " + aReceivedFileList[0].toString()); 
      
		// Get configured parsing data
				String parseLineDefs = getProperty(OIFConstants.PARSE_LINE_DEFS);
				OIFSimpleParsingHelper<ShippingLogDTO> parseHelper = new OIFSimpleParsingHelper<ShippingLogDTO>(ShippingLogDTO.class, parseLineDefs, logger);
				parseHelper.getParsingInfo();

				int totalCount = 0;
				try {
					for(int count = 0; count < aReceivedFileList.length; count++){
						String receivedFile = aReceivedFileList[count];
						if (receivedFile == null) {
							continue;
						}
						//String receivedFile = "D--GMG#ELPGAL#AHM030316296948513975067_20210513095949_1";
						resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
						int bytesPerLine = getPropertyInt(OIFConstants.BYTES_PER_LINE, getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH) + System.getProperty("line.separator").length()); 
						//default value = message_line_length + 1 for unix, message_line_length + 2 for windows. 
						int recordsPerRead = getPropertyInt(OIFConstants.RECORDS_PER_READ, 10000); //default to read 10000 records at a time
						long recordCount = OIFFileUtility.getRecordCount(resultPath + receivedFile, logger, bytesPerLine); 
						long remain = recordCount % recordsPerRead;
						long readCount = recordCount / recordsPerRead;
						if (remain != 0) {
							readCount++;
						}
						logger.info("records received: " + recordCount + ", read Count: "+ readCount);
						for (int i = 0; i < readCount; i++) {
							List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger, bytesPerLine, recordsPerRead, i);
							if (receivedRecords.isEmpty()) {
								logger.error("No records in received file: " + receivedFile);
								errorsCollector.error("No records in received file: " + receivedFile);
								continue;
							}else{
								logger.info("received records: " + receivedRecords.size());
								receivedRecordsCount = receivedRecords.size();
							}
							if(receivedRecords.get(0).contains( HEADER)) receivedRecords.remove(0);//remove header row
							if(receivedRecords.get(receivedRecords.size()-1).contains( TRAILER ))receivedRecords.remove(receivedRecords.size()-1);//remove last row
							//ignore ED
							List<String> previousErrorRecords =  OIFFileUtility.loadRecordsFromFile(exportPath + exportFileName, logger, bytesPerLine, recordsPerRead, i);

							if (previousErrorRecords.isEmpty()) {
								logger.error("No prior error records in exported file: " + exportFileName);
																
							}else{
								logger.info("exported records: " + previousErrorRecords.size());
								errorFileRecordCount = previousErrorRecords.size();
								if(previousErrorRecords.size() > 0 && !StringUtils.isEmpty(previousErrorRecords.get(0))) 
									receivedRecords.addAll(previousErrorRecords);
							}
							//process files
							Map<String,List<ShippingLogDTO>> shippingLogDTOList = new HashMap<String,List<ShippingLogDTO>>();
							List<ShippingLogExportDTO> shippingExportLogList = new ArrayList<ShippingLogExportDTO>();
							//sort it by vin, dateTime desc, ignore PC( parking address) if its IR then back in plant,
							for(String receivedRecord : receivedRecords){
								
								if(receivedRecord.contains(END_OF_DAY)) continue;
								ShippingLogDTO shippingLogDTO = new ShippingLogDTO();
								parseHelper.parseData(shippingLogDTO, receivedRecord);
								totalCount++;
								if(shippingLogDTO.getTransType().equalsIgnoreCase(END_OF_DAY)) continue;
								if(shippingLogDTO.getVinPrefix()== null || shippingLogDTO.getVinSerial() == null){
									logger.emergency("The vin is missing for this record: " + receivedRecord);
									errorsCollector.emergency("The vin is missing for this record: " + receivedRecord);
									
								}else{
									String productId = shippingLogDTO.getVinPrefix()+shippingLogDTO.getVinSerial();
									logger.info("processed - " +productId);
									if(shippingLogDTOList.containsKey(productId)) {
										logger.info("key already exists - " +productId);
										List<ShippingLogDTO> shippingList = shippingLogDTOList.get(productId);
										shippingList.add(shippingLogDTO);
										logger.info("adding - " +shippingLogDTO.toString());
										shippingLogDTOList.put(productId,shippingList);
									}else {
										logger.info("new Key - " +productId);
										List<ShippingLogDTO> shippingList = new ArrayList<ShippingLogDTO>();
										shippingList.add(shippingLogDTO);
										logger.info("adding - " +shippingLogDTO.toString());
										shippingLogDTOList.put(productId,shippingList);
									}
								}
							}
							
							for(String productId:shippingLogDTOList.keySet()) {
								ShippingLogDTO shippingLogDTO = getLatestStatus(shippingLogDTOList.get(productId));
								logger.info(" Latest Status for -"+shippingLogDTO.getVinPrefix()+shippingLogDTO.getVinSerial() +"("+shippingLogDTO.toString()+")");
								if(!trackingStatusMatches(shippingLogDTO)) {
									logger.info("tracking status does not match for - " +shippingLogDTO.getVinPrefix()+shippingLogDTO.getVinSerial());
									ShippingLogExportDTO dto = deriveExportShippingLogDTO(shippingLogDTO);
									shippingExportLogList.add(dto);
								}
							}
							//send email
							int matchedCount = shippingLogDTOList.keySet().size() - shippingExportLogList.size();
							int umatchedCount = (shippingExportLogList.size() > 0 && StringUtils.isEmpty(shippingExportLogList.get(0).getProductId().trim())) ?0:shippingExportLogList.size();
							emailReport(shippingExportLogList,receivedRecordsCount,errorFileRecordCount,shippingLogDTOList.keySet().size(),matchedCount,umatchedCount );
							
								//export data
							if (shippingExportLogList.size() > 0) {
									logger.info("Exporting AHMShippingLog Data -"+shippingExportLogList.size());
									//create empty file to clean up old data to avoid appending/duplicating data
									exportData(ShippingLogExportDTO.class, new ArrayList<ShippingLogExportDTO>());
									//update file with mismatch vin data
									exportData(ShippingLogExportDTO.class, new ArrayList<ShippingLogExportDTO>(shippingExportLogList));
							}else{
									logger.info("No records to create exported file");
									//create empty file
									exportData(ShippingLogExportDTO.class, new ArrayList<ShippingLogExportDTO>());
									
							}
							
							
							
						}
						
						
						logger.info(" file processed: " + receivedFile);
					}
					
					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					updateComponentStatus(LOAD_STATUS, UploadStatusType.PASS.name());
				
					
					logger.info("AHMShippingLogTask complete.  Received "+totalCount+" records total; processed records in "+totalTime+" milliseconds.");
					
					
				} catch (Exception e) {
					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					updateComponentStatus(LOAD_STATUS, UploadStatusType.FAIL.name());
					
					logger.error("AHMShippingLogTask failed.  Processed "+totalCount+" records before failure;  in "+totalTime+" milliseconds.");
					e.printStackTrace();
				}finally{
					errorsCollector.sendEmail();
				}
		 }	
			
	}
	
	private void emailReport(List<ShippingLogExportDTO> shippingExportLogList, int receivedRecordCount, int priorErrorFileCount, int vinCount,int matchedCount, int unmatchedCount) {
		try {
			
		List<String> outputMessages = new ArrayList<String>();
				
		//Create and form the report
		outputMessages.add("Matching GALC Vin Ship Status with AH Transaction Log - " + System.currentTimeMillis());
		outputMessages.add("==========================================================================================");
		outputMessages.add("Vin               GALC Status         AHM Status         Match_Result");
		outputMessages.add("------------------------------------------------------------------------------------------");
		//get all information about vins that not exist into database
		for (ShippingLogExportDTO shippingLogExportDTO : shippingExportLogList) {
			outputMessages.add( shippingLogExportDTO.getProductId() + " " + getGALCTrackingStatus(shippingLogExportDTO.getProductId())
					+ "    " +shippingLogExportDTO.getTransType() + "    " + " *** NOT MATCHING ***");
		}
		outputMessages.add("------------------------------------------------------------------------------------------");
			
	
		
		outputMessages.add("\nTotal number of records from AHM = "+ receivedRecordCount);
		outputMessages.add("\nTotal number of records from prior error list = "+ priorErrorFileCount);
		outputMessages.add("\nTotal number of vins processed = "+ vinCount);
		outputMessages.add("Total number of vins matched with GALC = "+ matchedCount);
		outputMessages.add("Total number of vins not matched with GALC = "+ unmatchedCount);
		
	
		String distributionList = getProperty(DISTRIBUTION_LIST);
		String emailSubject = getProperty(EMAIL_SUBJECT);
		String reportName =  resultPath +"AHMTransactionLogReport" +".txt";
		//Create a report file
	
			OIFFileUtility.writeToFile(outputMessages,  reportName);
		
		//Send the report file in an email
		sendEmail(reportName , distributionList, emailSubject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private String getGALCTrackingStatus(String productId) {
		Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(productId);
		
		return frame != null? frame.getTrackingStatus() :"";
	}

	private ShippingLogDTO getLatestStatus(List<ShippingLogDTO> list) {
		if(list.size()> 1) {
			int index= 0;
			ShippingLogDTO tempShippingLogDTO = list.get(0);
			Map<String,List<ShippingLogDTO>> statusListMap = new HashMap<String,List<ShippingLogDTO>>();
			for(ShippingLogDTO shippingLogDto:list) {
				logger.info("record index "+index +" - " + shippingLogDto.toString());
				if(index > 0) {
					if(shippingLogDto.getDateTime() != null && tempShippingLogDTO.getDateTime() != null) {
						if(shippingLogDto.getDateTime().after(tempShippingLogDTO.getDateTime())) {
						
							logger.info(shippingLogDto.toString()+" is newer than " +tempShippingLogDTO.toString());
							tempShippingLogDTO = shippingLogDto;
							
						}else if(shippingLogDto.getDateTime().equals(tempShippingLogDTO.getDateTime())){
							if(!shippingLogDto.getTransType().equalsIgnoreCase(tempShippingLogDTO.getTransType())) {
								ShippingStatusEnum status = ShippingStatusEnum.getShippingStatusByTransactionType(shippingLogDto.getTransType());
								ShippingStatusEnum tempStatus = ShippingStatusEnum.getShippingStatusByTransactionType(tempShippingLogDTO.getTransType());
								
								if(status.getStatus() > tempStatus.getStatus()) {
									logger.info(shippingLogDto.toString()+" is higher status than " +tempShippingLogDTO.toString());
									tempShippingLogDTO = shippingLogDto;
								}
							}
						}
					}
				}
				index++;
			}
			return tempShippingLogDTO;
		}else {
			return list.get(0);
		}
	}
	
	

	private boolean trackingStatusMatches(ShippingLogDTO shippingLogDTO) {
		String productId = shippingLogDTO.getVinPrefix()+shippingLogDTO.getVinSerial();
		String status = shippingLogDTO.getTransType();
		if(status.equalsIgnoreCase(ShippingStatusEnum.S65A.getTransactionType())) { 
			logger.info("ProductId - "+productId + " is at Tracking status - " +status); 
			return true;
		}
		
		else {
			Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(productId);
			if(frame != null) {
				
				List<String> validStatus = getTrackingStatusByShippingStatus(status.trim());
				
				if(frame.getTrackingStatus() != null && validStatus.contains(frame.getTrackingStatus().trim())) {
					logger.info("ProductId - "+productId + " is at Tracking status - " +frame.getTrackingStatus() +" and is expected to be at - "+ validStatus.toString());
					return true;
				}else {
					logger.info("ProductId - "+productId + " is at Tracking status - " +frame.getTrackingStatus() +" but is expected to be at - "+ validStatus.toString());
				}
			}
		}
		
		return false;
	}

	private List<String> getTrackingStatusByShippingStatus(String status){
		String trackingStatus = trackingStatusMap.get(status.trim()) != null? trackingStatusMap.get(status.trim()):ShippingStatusEnum.getShippingStatusByTransactionType(status.trim()).getName();
		
		if(StringUtils.isNotEmpty(trackingStatus)) {
			
			String[] statusList = trackingStatus.split(",");
			
			return Arrays.asList(statusList);
		}else {
			return new ArrayList<String>();
		}
	}
	

	private ShippingLogExportDTO deriveExportShippingLogDTO(ShippingLogDTO shippingLogDTO) {
		ShippingLogExportDTO dto = new ShippingLogExportDTO();
		dto.setTransCode(shippingLogDTO.getTransCode());
		dto.setTransType(shippingLogDTO.getTransType());
		dto.setDate(shippingLogDTO.getDate());
		dto.setTime(shippingLogDTO.getTime());
		dto.setData(StringUtils.isEmpty(shippingLogDTO.getData())?"":shippingLogDTO.getData());
		dto.setLoc(shippingLogDTO.getLoc());
		dto.setVinPrefix(shippingLogDTO.getVinPrefix());
		dto.setVinSerial(shippingLogDTO.getVinSerial());
		
		return dto;
	}


	private <K extends IOutputFormat> void exportData(Class<K> outputClass,
			List<K> outputData) {
		
		
		String opFormatDefKey = this.componentId;
		
		File target = new File(exportPath + exportFileName);
		int length = getPropertyInt(OUTPUT_MESSAGE_LINE_LENGTH);
		
		if(StringUtils.isBlank(exportPath) || StringUtils.isBlank(exportFileName)) {
			logger.error("Export file path or name is missing!");
			return;
		}
		
		// format data
		List<String> recordStrList = formatData(outputData, outputClass, length, opFormatDefKey);
		logger.info("writing to File -"+  recordStrList.size());
		// write the records to the file
		writeRecordsToFile(target, recordStrList, null, null);
	
	}
	
	private <K extends IOutputFormat> List<String> formatData(List<K> outputData, Class<K> outputClass, int length, String opFormatDefKey){
		List<String> recordStrList = new ArrayList<String>();
		if (!outputData.isEmpty()) {
			OutputFormatHelper<K> ofHelper = new OutputFormatHelper<K>(opFormatDefKey, logger, this.errorsCollector);
			ofHelper.initialize(outputClass);
			char[] charArray = new char[length];
			Arrays.fill(charArray, ' ');

			for (K dto : outputData) {
				String strResult = ofHelper.formatOutput(dto, charArray);
				recordStrList.add(strResult);
			}
		}
		return recordStrList;
	}
	
	private void writeRecordsToFile(File target, List<String> recordStrList, String header, String footer){
		if (target.getParentFile() == null || !target.getParentFile().exists()
				|| !target.getParentFile().isDirectory()) {
			target.getParentFile().mkdir();
			
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(target, false));
			if(header != null){
			writer.write(header);
			writer.newLine();}
			for (String record : recordStrList) {
				writer.write(record);
				writer.newLine();
			}
			if(footer != null){
			writer.write(footer);
			writer.newLine();}
			writer.flush();
		} catch (IOException e) {
			logger.error(e, "Error to create exported file");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e, "Error to create exported file");
				}
			}
		}
	}
	
	/**
	 * 	email report file
	 * @param reportFile	-	this is the path of the report
	 * @param emailList		-	this is the email distribution list
	 * @param emailSubject	-	this is the subject for the email
	 */
	private void sendEmail(String reportFile, String emailList, String emailSubject) {
		OifServiceEMailHandler eMailHandler = new OifServiceEMailHandler(OIFConstants.OIF_NOTIFICATION_PROPERTIES);		
		eMailHandler.setEmailAddressList(emailList);
		StringBuffer msg = new StringBuffer();			
		msg.append("This e-mail has an attached with information about current inventory. \n");
		msg.append("Please review the attached inventory comparison report for details. \n");		
		eMailHandler.delivery(emailSubject, msg.toString(), reportFile);
	}
}