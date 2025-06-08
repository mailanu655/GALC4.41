package com.honda.galc.oif.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.common.exception.OifServiceException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.enumtype.UploadStatusType;
import com.honda.galc.oif.dto.BomDTO;
import com.honda.galc.oif.dto.BomExportDTO;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;
import com.honda.galc.util.StringUtil;
/**
 * 
 * <h3>BomTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BomTask.java description </p>
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
 * <TD>Jiamei Li</TD>
 * <TD>Feb 26, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */
public class BomTask extends OifTask<Object> implements IEventTaskExecutable {

	public static final String SCRIPT_FILE_PATH = "SCRIPT_FILE_PATH";
	public static final String SCRIPT_FILE_NAME = "SCRIPT_FILE_NAME";
	public static final String DB_INSTANCE_NAME = "DB_INSTANCE_NAME";
	public static final String LOAD_STATUS = "LOAD_STATUS";
	public static final String OUTPUT_MESSAGE_LINE_LENGTH = "OUTPUT_MESSAGE_LINE_LENGTH";
	private int failedRecords=0;
	
	String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);
	String exportFileName =  getProperty(OIFConstants.EXPORT_FILE_NAME);
	String scriptPath = getProperty(SCRIPT_FILE_PATH);
	String scriptFileName = getProperty(SCRIPT_FILE_NAME);
	String instanceName = getProperty(DB_INSTANCE_NAME);
	
	// File names received from GPCS(MQ).
	protected String[] aReceivedFileList;

	public BomTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try {
			processBom();
		} catch (TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	/**
	 * This method is used for receive files/files from MQ and process it/them for current line.
	 * @throws ParseException
	 */
	private void processBom() throws Exception{
		
		long startTime = System.currentTimeMillis();
		logger.info("start to process Bom");

		// Refresh properties

		refreshProperties();
	
		// Receive file from GPCS(MQ);
		
		aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID), getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if(aReceivedFileList == null){
			return;
		}

		// Get configured parsing data
		String parseLineDefs = getProperty(OIFConstants.PARSE_LINE_DEFS);
		OIFSimpleParsingHelper<BomDTO> parseHelper = new OIFSimpleParsingHelper<BomDTO>(BomDTO.class, parseLineDefs, logger);
		parseHelper.getParsingInfo();

		int totalCount = 0;
		try {
			for(int count = 0; count < aReceivedFileList.length; count++){
				String receivedFile = aReceivedFileList[count];
				if (receivedFile == null) {
					continue;
				}
				String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
				int bytesPerLine = getPropertyInt(OIFConstants.BYTES_PER_LINE, getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH) + System.getProperty("line.separator").length()); 
				//default value = message_line_length + 1 for unix, message_line_length + 2 for windows. 
				int recordsPerRead = getPropertyInt(OIFConstants.RECORDS_PER_READ, 10000); //default to read 10000 records at a time
				long recordCount = OIFFileUtility.getRecordCount(resultPath + receivedFile, logger, bytesPerLine); 
				long remain = recordCount % recordsPerRead;
				long readCount = recordCount / recordsPerRead;
				if (remain != 0) {
					readCount++;
				}
				logger.info("No records received: " + recordCount + ", read Count: "+ readCount);
				for (int i = 0; i < readCount; i++) {
					List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger, bytesPerLine, recordsPerRead, i);
					if (receivedRecords.isEmpty()) {
						logger.error("No records in received file: " + receivedFile);
						errorsCollector.error("No records in received file: " + receivedFile);
						continue;
					}else{
						logger.info("received records: " + receivedRecords.size());
					}

					//process files
					List<BomDTO> bomDTOList = new ArrayList<BomDTO>();
					List<BomExportDTO> bomExportDTOList = new ArrayList<BomExportDTO>();
					
					for(String receivedRecord : receivedRecords){
						BomDTO bomDTO = new BomDTO();
					
						parseHelper.parseData(bomDTO, receivedRecord);
						totalCount++;
						if(bomDTO.getIntColorCode() == null || bomDTO.getMtcColor() == null || 
								bomDTO.getMtcModel() == null || bomDTO.getMtcOption() == null || 
								bomDTO.getMtcType() == null || bomDTO.getPartBlockCode() == null || 
								bomDTO.getPartColorCode() == null || bomDTO.getPartItemNo() == null || 
								bomDTO.getPartNo() == null || bomDTO.getPartSectionCode() == null || 
								bomDTO.getPlantLocCode() == null || bomDTO.getSupplierCatCode() == null || 
								bomDTO.getTgtShipToCode() == null || bomDTO.getEffBegDate() == null){
							logger.emergency("The primary key is missing for this record: " + receivedRecord);
							errorsCollector.emergency("The primary key is missing for this record: " + receivedRecord);
							failedRecords++;
						}else{
							bomDTOList.add(bomDTO);
							
							//generate PDDA_FIF_TYPE for each BOM record.  PDDA_FIF_TYPE = SUBSTR(PART_ITEM_NO,6,2)||SUBSTR(MODEL_TYPE_CODE,1,1)
							String partBlock = bomDTO.getPartBlockCode().trim();
							String partItemNo = bomDTO.getPartItemNo().trim();
							String modelTypeCode = bomDTO.getMtcType().trim();
							if(partBlock != null && partBlock.equals("F") && partItemNo.length() >= 7 && modelTypeCode.length() > 1){
								String pddaFifType = modelTypeCode.substring(0, 1) + partItemNo.substring(5,7);
								bomDTO.setPddaFifType(pddaFifType);
							}
							
							BomExportDTO dto = deriveExportBomDTO(bomDTO);
							bomExportDTOList.add(dto);
						}
					}
					if(errorsCollector.getErrorList().isEmpty()){	
						//export data
						if (bomExportDTOList.size() > 0) {
							logger.info("Exporting bom Data -"+bomExportDTOList.size());
							exportData(BomExportDTO.class, new ArrayList<BomExportDTO>(bomExportDTOList));
						}else{
							logger.info("No records to create exported file");
						}
					}
					
					
				}
				
				
				logger.info("Bom record saved; file processed: " + receivedFile);
			}
			executeShellScript(scriptPath + scriptFileName, instanceName.trim(),exportPath+exportFileName);
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			updateComponentStatus(LOAD_STATUS, UploadStatusType.PASS.name());
			setIncomingJobCount(totalCount, failedRecords,aReceivedFileList );
			
			logger.info("BomTask complete.  Received "+totalCount+" records total; inserted and updated records in "+totalTime+" milliseconds.");
			
			
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			updateComponentStatus(LOAD_STATUS, UploadStatusType.FAIL.name());
			setIncomingJobCount(0,totalCount , aReceivedFileList);
			logger.error("BomTask failed.  Processed "+totalCount+" records before failure; inserted and updated records in "+totalTime+" milliseconds.");
			throw e;
		}finally{
			errorsCollector.sendEmail();
		}
		
	}

	private BomExportDTO deriveExportBomDTO(BomDTO bomDTO) {
		BomExportDTO dto = new BomExportDTO();
		dto.setEffBegDate(new SimpleDateFormat("yyyyMMdd").format(bomDTO.getEffBegDate()));
		dto.setIntColorCode(bomDTO.getIntColorCode());
		dto.setMtcColor(bomDTO.getMtcColor());
		dto.setMtcModel(bomDTO.getMtcModel());
		dto.setMtcOption(bomDTO.getMtcOption());
		dto.setMtcType(bomDTO.getMtcType());
		dto.setPartBlockCode(bomDTO.getPartBlockCode());
		dto.setPartColorCode(bomDTO.getPartColorCode());
		dto.setPartItemNo(bomDTO.getPartItemNo());
		dto.setPartNo(bomDTO.getPartNo());
		dto.setPartSectionCode(bomDTO.getPartSectionCode());
		dto.setPlantLocCode(bomDTO.getPlantLocCode());
		dto.setSupplierNo(bomDTO.getSupplierNo());
		dto.setTgtShipToCode(bomDTO.getTgtShipToCode());
		dto.setDataUpdDescText(bomDTO.getDataUpdDescText());
		dto.setDcFamClassCode(bomDTO.getDcFamClassCode());
		dto.setDcPartNo(bomDTO.getDcPartNo());
		dto.setDcPartName(bomDTO.getDcPartName());
		dto.setEffEndDate(new SimpleDateFormat("yyyyMMdd").format(bomDTO.getEffEndDate()));
		dto.setPartColorIdCode(bomDTO.getPartColorIdCode());
		dto.setPartProdCode(bomDTO.getPartProdCode());
		dto.setPartQty(StringUtil.padLeft(String.valueOf(bomDTO.getPartQty()),6,'0'));
		dto.setPddaFifType(bomDTO.getPddaFifType());
		dto.setProclocGpNo(bomDTO.getProclocGpNo());
		dto.setProclocGpSeqNo(bomDTO.getProclocGpSeqNo());
		dto.setSupplierCatCode(bomDTO.getSupplierCatCode());
		dto.setTargetMfgDestNo(bomDTO.getTargetMfgDestNo());
		dto.setTgtModelDevCode(bomDTO.getTgtModelDevCode());
		dto.setTgtPlantLocCode(bomDTO.getTgtPlantLocCode());
		dto.setTimestampDate(new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS").format(bomDTO.getTimestampDate()));
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
			writer = new BufferedWriter(new FileWriter(target, true));
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
	
	private void executeShellScript(String fileName, String dbInstance, String exportFileName){
		
		 try {
			 	String command =  fileName +" "+dbInstance+" "+exportFileName;
			 	logger.info("attempting to executing comand: "+ command);
	            Process proc = Runtime.getRuntime().exec(command); 
	            BufferedReader read = new BufferedReader(new InputStreamReader(
	                    proc.getInputStream()));
	            try {
	                proc.waitFor();
	              
	            } catch (InterruptedException e) {
	            	logger.error(e, "Error executing shell script");
	                e.printStackTrace();
	            }
	            while (read.ready()) {
	            	logger.info(read.readLine());
	            }
	            if(proc.exitValue() > 0){
	            	errorsCollector.emergency("Script exited with error. please check logs for error");
	            	throw new OifServiceException("Script exited with error. please check logs for error");
	             }
	        } catch (IOException e) {
	        	e.printStackTrace();
	        	logger.error(e, "Error executing shell script");
	        }
		 
	}
	
	
}