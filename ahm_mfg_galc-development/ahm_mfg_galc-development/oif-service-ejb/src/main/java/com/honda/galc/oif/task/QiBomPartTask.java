package com.honda.galc.oif.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.qi.QiBomPartDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.entity.qi.QiBomPart;
import com.honda.galc.oif.dto.QiBomPartDto;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.task.TaskUtils;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;
/**
 * 
 * <h3>QiBomPartTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QiBomPartTask.java description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>March 16, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */

public class QiBomPartTask extends OifTask<Object> implements IEventTaskExecutable {
	
	private HashMap<String, String> productKindMap;
	
	public QiBomPartTask(String name) {
		super(name);
		// get Product Kind Map
		productKindMap = new HashMap<String, String>();
		List<Object[]> plantCodeProductKind = ServiceFactory.getDao(QiPlantDao.class).findPlantCodeProductKind();
		for (Object[] obj : plantCodeProductKind) {
			if (obj[0] == null || obj[0].toString().trim().length() == 0) {
				continue;
			}
			if (obj[1] == null || obj[1].toString().trim().length() == 0) {
				obj[1] = QiConstant.AUTOMOBILE;
			}
			productKindMap.put(obj[0].toString().trim(), obj[1].toString().trim());
		}
	}

	@Override
	public void execute(Object[] args) {
		try {
			if (args == null) {
				processQiBomPart();
			} else {
				processQiBomPart(args);
			}
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
	private void processQiBomPart() throws Exception{
		long startTime = System.currentTimeMillis();
		logger.info("start to process QiBomPart");
		
		// get Product Kind Map
		HashMap<String, String> productKindMap = new HashMap<String, String>();
		List<Object[]> plantCodeProductKind = ServiceFactory.getDao(QiPlantDao.class).findPlantCodeProductKind();
		for (Object[] obj : plantCodeProductKind) {
			if (obj[0] == null || obj[0].toString().trim().length() == 0) {
				continue;
			}
			if (obj[1] == null || obj[1].toString().trim().length() == 0) {
				obj[1] = QiConstant.AUTOMOBILE;
			}
			productKindMap.put(obj[0].toString().trim(), obj[1].toString().trim());
		}
		
		// Refresh properties
		refreshProperties();
		
		// Receive file from MQ
		String[] receivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID), getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
		
		if(receivedFileList == null){
			return;
		}
		
		// Get configured parsing data
		String parseLineDefs = getProperty(OIFConstants.PARSE_LINE_DEFS);
		OIFSimpleParsingHelper<QiBomPartDto> parseHelper = new OIFSimpleParsingHelper<QiBomPartDto>(QiBomPartDto.class, parseLineDefs, logger);
		parseHelper.getParsingInfo();
		

		int totalCount = 0;
		int insertedCount = 0;
		int updatedCount = 0;
		
		try {
			QiBomPartDao qiBomPartDao = ServiceFactory.getDao(QiBomPartDao.class);
			for(int count = 0; count < receivedFileList.length; count++){
				String receivedFile = receivedFileList[count];
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
				
				for (int i = 0; i < readCount; i++) {
					List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger, bytesPerLine, recordsPerRead, i);
					if (receivedRecords.isEmpty()) {
						logger.error("No records in received file: " + receivedFile);
						errorsCollector.error("No records in received file: " + receivedFile);
						continue;
					}
					
					//process files
					List<QiBomPartDto> qiBomPartDTOList = new ArrayList<QiBomPartDto>();
					for (String receivedRecord : receivedRecords) {
						QiBomPartDto qiBomPartDTO = new QiBomPartDto();
						parseHelper.parseData(qiBomPartDTO, receivedRecord);
						if (qiBomPartDTO.getPlantLocCode() == null || qiBomPartDTO.getMtcModel() == null || 
								qiBomPartDTO.getDcPartNo() == null || qiBomPartDTO.getTgtDcPartName() == null) {
							logger.emergency("The primary key is missing for this record: " + receivedRecord);
							errorsCollector.emergency("The primary key is missing for this record: " + receivedRecord);
						} else {
							qiBomPartDTOList.add(qiBomPartDTO);
						}
					}
					
					//update or insert data
					for (QiBomPartDto qiBomPartDTO : qiBomPartDTOList) {
						QiBomPart qiBomPart = qiBomPartDTO.deriveQiBomPart();
						String productKind = productKindMap.get(qiBomPartDTO.getPlantLocCode());
						qiBomPart.getId().setProductKind(productKind == null ? QiConstant.AUTOMOBILE : productKind);
						
						QiBomPart currentQiBomPart = qiBomPartDao.findByKey(qiBomPart.getId()); 
						if (currentQiBomPart == null) {
							qiBomPartDao.save(qiBomPart);
							insertedCount++;
							logger.debug("QI BOM Part record inserted: " + qiBomPart);
						} else if (!currentQiBomPart.equals(qiBomPart)) {
							qiBomPartDao.save(qiBomPart);
							updatedCount++;
							logger.debug("QI BOM Part record updated: " + qiBomPart);
						} else {
							logger.debug("QI BOM Part record unmodified: " + qiBomPart);
						}
						totalCount++;
					}
				}
				logger.info("QI BOM Part record saved; file processed: " + receivedFile);
			}
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			logger.error(String.format("QiBomPartTask failed.  Processed %1$d records before failure; inserted %2$d records and updated %3$d records in %4$d milliseconds.", totalCount, insertedCount, updatedCount, totalTime));
			throw e;
		}
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info(String.format("QiBomPartTask complete.  Received %1$d records total; inserted %2$d records and updated %3$d records in %4$d milliseconds.", totalCount, insertedCount, updatedCount, totalTime));
	}
	
	// This method is called by Rest service QiBomPartService with data in JSON format
	private void processQiBomPart(Object[] args) throws Exception {
		
		Map<String, String> map = TaskUtils.unpackExtraArgs(args);

		String processType = map.get("PROCESS_TYPE");
		String jsonArrayString = "";
		boolean isInsert = false;

		if ("DELETE_ALL".equals(processType)) {
			QiBomPartDao qiBomPartDao = ServiceFactory.getDao(QiBomPartDao.class);
			qiBomPartDao.removeAll();
			return;
		} else {
			jsonArrayString = map.get("DATA");

			if ("INSERT_BATCH".equals(processType)) {
				isInsert = true;
			} // else is MERGER_BATCH
		}

		long startTime = System.currentTimeMillis();
		long endTime;
		long totalTime;
		int totalCount = 0;

		logger.info("start to process QiBomPart");

		// Get configured parsing data
		String parseLineDefs = getProperty(OIFConstants.PARSE_LINE_DEFS);
		OIFSimpleParsingHelper<QiBomPartDto> parseHelper = new OIFSimpleParsingHelper<QiBomPartDto>(QiBomPartDto.class,
				parseLineDefs, logger);
		parseHelper.getParsingInfo();

		// process data
		Gson gson = new Gson();
		String[] stringArray = gson.fromJson(jsonArrayString, String[].class);

		List<QiBomPartDto> qiBomPartDTOList = new ArrayList<QiBomPartDto>();
		for (String receivedRecord : stringArray) {
			QiBomPartDto qiBomPartDTO = new QiBomPartDto();
			try {
				logger.debug("received data:'" + receivedRecord + "', length=" + receivedRecord.length());
				parseHelper.parseData(qiBomPartDTO, receivedRecord);
			} catch (Exception e) {
				logger.error(e);
				logger.error(
						"can't parse the received data: '" + receivedRecord + "', length=" + receivedRecord.length());
				continue;
			}
			if (qiBomPartDTO.getPlantLocCode() == null || qiBomPartDTO.getMtcModel() == null
					|| qiBomPartDTO.getDcPartNo() == null || qiBomPartDTO.getTgtDcPartName() == null) {
				logger.error("The primary key is missing for this record: " + receivedRecord);
			} else {
				qiBomPartDTOList.add(qiBomPartDTO);
			}
		}

		// update or insert data
		String values = "";
		QiBomPartDao qiBomPartDao = ServiceFactory.getDao(QiBomPartDao.class);

		for (QiBomPartDto qiBomPartDTO : qiBomPartDTOList) {
			QiBomPart qiBomPart = qiBomPartDTO.deriveQiBomPart();
			String productKind = productKindMap.get(qiBomPartDTO.getPlantLocCode());
			qiBomPart.getId().setProductKind(productKind == null ? QiConstant.AUTOMOBILE : productKind);
			totalCount++;

			if (qiBomPart.getDcPartName().contains("'")) {
				// double the single quote in order to work with native query
				qiBomPart.setDcPartName(qiBomPart.getDcPartName().replace("'", "''")); 
			}
			values += "('" + qiBomPart.getId().getDcPartNo() + "','" + qiBomPart.getId().getModelCode() + "','"
					+ qiBomPart.getMainPartNo() + "','" + qiBomPart.getDcPartName() + "','"
					+ qiBomPart.getId().getProductKind() + "'),";
		}
		if (values.length() > 0) {
			values = values.substring(0, values.length() - 1);

			if (isInsert) {
				qiBomPartDao.insertBatch(values);
			} else {
				qiBomPartDao.mergeBatch(values);
			}

			endTime = System.currentTimeMillis();
			totalTime = endTime - startTime;
			logger.info(String.format("Processed %1$d records in %2$d milliseconds.", totalCount, totalTime));
		}
	}
}
