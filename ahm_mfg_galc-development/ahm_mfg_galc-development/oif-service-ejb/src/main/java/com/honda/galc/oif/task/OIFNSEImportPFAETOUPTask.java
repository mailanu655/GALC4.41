package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductShippingDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.entity.product.ProductShippingId;
import com.honda.galc.oif.dto.NSEDataDTO;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.task.TaskUtils;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>OIFNSEImportPFAETOUPTask</h3>
 * <p> OIFNSEImportPFAETOUPTask is for Import of of NSE interface file PFAETOUP </p>
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
 *
 * </TABLE>
 *   
 * @author Ratul Chakravarty<br>
 * May 01, 2014
 * 
 * 
 * 
 * @author Suriya Sena
 * Added suport to call via rest service.
 *  
 * Sample message format used to the task via the rest service interface. 
 * [
   {
      "java.lang.String":"OIF_NSE_IMPORT_PFAETOUP"
   },
   {
      "java.util.Map": {
            "LINE_NUMBER"    : "01",
            "KD_LOT_NUMBER" : "HMA 012018110030",
            "MODEL"         : "K5MJ",
            "TYPE"          :   "A05",
            "OPTION"        : "00 ",
            "RACKID"        : "00000",
            "EIN"           : "J35Y65232287     ",
            "TRAILERID"     :   "",
            "TIMESTAMP"     : "2018-11-01-09.23.59.465365"
       }
   },
   {
      "java.lang.String":"11"
   },
   {
      "java.lang.String":""
   }
]
 *
 */


public class OIFNSEImportPFAETOUPTask extends OifTask<NSEDataDTO> implements IEventTaskExecutable {
	
	private boolean optionalLoggingEnabled = false;
	private List<String> lineIds = PropertyService.getPropertyList(componentId, "EIN_TRACK_STATUS_EXCLUSIONS");
	private SimpleDateFormat sdf = new SimpleDateFormat(getProperty("TIMESTAMP_FORMAT"));
	private String ppId = getProperty("SHIPPING_RECV_PPID");

	String strProductType;

	public OIFNSEImportPFAETOUPTask(String componentId) {
		super(componentId);
		SystemPropertyBean systemBean = PropertyService.getPropertyBean(SystemPropertyBean.class);
		strProductType = systemBean.getProductType();
	}

	public void execute(Object[] args) {
		
		logger.info("Launching OIFNSEImportPFAETOUPTask. Interface Id = " + componentId + " Date & Time: " + new java.util.Date());
		optionalLoggingEnabled = getPropertyBoolean("OPTIONAL_LOGGING_ENABLED", false);
		
		initialize();
		
	    Map<String,String> map  = TaskUtils.unpackExtraArgs(args);	
	    
	    if (map !=null && !map.isEmpty()) {
	   	   NSEDataDTO dataDTO = new NSEDataDTO(map); 
	   	   try {
    	       processRecord(dataDTO,null);
	   	   } catch (Exception e) {
	   		  logger.error(e, "exception while running process record.");
	   	   }
	    }  else {
	       processNSEFiles();	
	    }
		
		logger.info("OIFNSEImportPFAETOUPTask ends. Interface Id = " + componentId + " Date & Time: " + new java.util.Date());
	}
	
	
	private void processNSEFiles() {
		String fileRecDefComponentID = getProperty("PARSE_LINE_DEFS");
		simpleParseHelper = new OIFSimpleParsingHelper<NSEDataDTO>(NSEDataDTO.class, fileRecDefComponentID, logger);
		simpleParseHelper.getParsingInfo();
		
		int noOfRetrievedMessages = 0;
		int noOfProcessedMessages = 0;
		try {
			MQUtility mqu = new MQUtility(this);
			String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			String[] recvdFiles = mqu.getReceiveFile(getProperty("INTERFACE_ID"), mqConfig, resultPath);
			noOfRetrievedMessages = recvdFiles.length;
			
			for (String recvdFile : recvdFiles) {
				processNSEFileReceived(resultPath + recvdFile);
				noOfProcessedMessages++;
			}
		} catch (MQUtilityException e) {
			logger.error("MQException occured: while processing these files: " + receivedFileList);
			logger.error("MQException occured: " + ExceptionUtils.getFullStackTrace(e));
			errorsCollector.error(e, "MQException occured in OIFNSEImportPFAETOUPTask. Files: " + receivedFileList);
		} catch (ParseException e) {
			logger.error("ParseException occured: while processing these files: " + receivedFileList);
			logger.error("ParseException occured: " + ExceptionUtils.getFullStackTrace(e));
			errorsCollector.error(e, "ParseException occured in OIFNSEImportPFAETOUPTask. Files: " + receivedFileList);
		} catch (IOException e) {
			logger.error("IOException occured: while processing these files: " + receivedFileList);
			logger.error("IOException occured: " + ExceptionUtils.getFullStackTrace(e));
			errorsCollector.error(e, "IOException occured in OIFNSEImportPFAETOUPTask. Files: " + receivedFileList);
		} catch (SQLException e) {
			logger.error("SQLException occured: while processing these files: " + receivedFileList);
			logger.error("SQLException occured: " + ExceptionUtils.getFullStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception occured: while processing these files: " + receivedFileList);
			logger.error("Exception occured: " + ExceptionUtils.getFullStackTrace(e));
			errorsCollector.error(e, "Exception occured in OIFNSEImportPFAETOUPTask. Files: " + receivedFileList);
		} finally {
			errorsCollector.sendEmail();
		}		
		logger.info(noOfRetrievedMessages + " messages retrieved from the queue.");
		logger.info(noOfProcessedMessages + " messages processed.");
	}
	
	private void processNSEFileReceived(String recvdFile) throws IOException, ParseException, SQLException {
		
		List<String> enginesToVerify = new ArrayList<String>();
		BufferedReader br=null;
		
		try {
			br = new BufferedReader(new FileReader(recvdFile));
			while(br.ready()) {
				String singleRecord = br.readLine();
				if(optionalLoggingEnabled)
					logger.info("Single Record :- " + singleRecord);
				
				NSEDataDTO dataDTO = new NSEDataDTO();
				simpleParseHelper.parseData(dataDTO, singleRecord);
	            processRecord(dataDTO,enginesToVerify);
			}
	//		This verification is here because of deadlocks while executing track method in TrackingService
	//		can result in failure of inserting records into GAL131TBX (Engine) 
	//		without any exception thrown
			for (String ein : enginesToVerify) {
				if(ServiceFactory.getDao(EngineDao.class).findByKey(ein) == null) {
					logger.error("The engine is missing: " + ein);
					errorsCollector.error("The engine is missing: " + ein);
				}
			}
		} catch(Exception e) {
			logger.error("Exception occured while processing File: " + recvdFile);
			logger.error("Exception occured while processing File: " + recvdFile + " \n "  + e.getMessage());
			errorsCollector.error(e, "Exception occured while processing File: " + recvdFile + " \n "  + ExceptionUtils.getFullStackTrace(e));
		} finally {
		   if (br!= null) {
			  br.close();
		   }
		}
	}
	
	public void processRecord(NSEDataDTO dataDTO,List<String> enginesToVerify) throws ParseException {
		
		int lineNumber = dataDTO.getLineNumber();
		String kdLotNumber = dataDTO.getKdLotNumber();
		String model = dataDTO.getModel();
		String type = dataDTO.getType();
		String option = dataDTO.getOption();
		String rackID = dataDTO.getRackId();
		String ein = dataDTO.getEin();
		String trailerId = dataDTO.getTrailerId();
		String timestampStr = dataDTO.getTimestamp();
		Timestamp timestamp = new java.sql.Timestamp(sdf.parse(timestampStr).getTime());
		
		if (optionalLoggingEnabled) {
			StringBuilder info = new StringBuilder("lineNumber: ");
			info.append(lineNumber).append(" kdLotNumber: ").append(kdLotNumber).append(" model: ").append(model)
			.append(" type: ").append(type).append(" option: ").append(option).append(" rackID: ")
			.append(rackID).append(" ein: ").append(ein).append(" trailerId: ").append(trailerId)
			.append(" timestamp: ").append(timestamp);
			logger.info(info.toString());
		}

		if ("FRAME".equals(strProductType) && Integer.parseInt(siteLineId) == lineNumber) { // for assembly lines i.e.
																							// Line 1 or Line 2 at HMA
			if (checkStatusBeforeUpdate(ein)) {
				try {
					createEngine(ein, kdLotNumber.trim(), model.trim() + type.trim() + option.trim(), trailerId, ppId);
					createEngineHistory(ProductType.ENGINE, ein, model.trim() + type.trim() + option.trim(), ppId);
				} catch (SQLException sqlExcep) {
					logger.error("SQLException occured in Line " + lineNumber + " while processing Engine: " + ein
							+ "\n" + ExceptionUtils.getFullStackTrace(sqlExcep));
					errorsCollector.error(sqlExcep, "SQLException occured in Line " + lineNumber
							+ " while processing Engine: " + ein + "\n" + sqlExcep.getMessage());
					try {
						logger.error("Retrying tracking Engine: " + ein);
						createEngine(ein, kdLotNumber.trim(), model.trim() + type.trim() + option.trim(), trailerId, ppId);
						createEngineHistory(ProductType.ENGINE, ein, model.trim() + type.trim() + option.trim(), ppId);
					} catch (Exception e) {
						logger.error("Exception occured in Line " + lineNumber + " while retrying processing Engine: "
								+ ein + "\n" + ExceptionUtils.getFullStackTrace(e));
						errorsCollector.error(e, "Exception occured in Line " + lineNumber
								+ " while processing Engine: " + ein + "\n" + e.getMessage());
					}
				} catch (Exception e) {
					logger.error("Exception occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + ExceptionUtils.getFullStackTrace(e));
					errorsCollector.error(e, "Exception occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + e.getMessage());
				}
				if (enginesToVerify != null) {
				  enginesToVerify.add(ein);
				}
			}
		}
		if ("ENGINE".equals(strProductType)) { // for Engine Line
			// track the product
			try {
				TrackingService ts = ServiceFactory.getService(TrackingService.class);
				ts.track(ProductType.ENGINE, ein, getProperty("SHIPPING_RECV_PPID{L" + lineNumber + "}")); // line
																											// number
																											// from the
																											// file
			} catch (Exception e) {
				logger.error("Exception occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + ExceptionUtils.getFullStackTrace(e));
				errorsCollector.error(e, "Exception occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + e.getMessage());
			}

			// update OFF_TIMESTAMP of PRODUCT_CARRIER_TBX TABLE
			ProductCarrierDao pcd = ServiceFactory.getDao(ProductCarrierDao.class);
			List<ProductCarrier> prodCarrList = pcd.findAll(ein, rackID);
			if (prodCarrList != null && !prodCarrList.isEmpty()) {
				ProductCarrier pc = prodCarrList.get(0);
				pc.setOffTimestamp(timestamp);
				pcd.update(pc);
			}
			ProductShipping ps = new ProductShipping();
			ProductShippingId id = new ProductShippingId();
			id.setProductId(ein);
			id.setTrailerNumber(trailerId);
			ps.setId(id);
			ps.setProductTypeString(ProductType.ENGINE.toString());
			ps.setDunnage(rackID);
			ps.setTrailerStatus(1);
			Date date = new Date(timestamp.getTime());
			ps.setShipDate(date);
			getDao(ProductShippingDao.class).save(ps);
		}
	}

	public void createEngineHistory(ProductType productType, String productId, String productSpecCode, String processPointId) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		ProductResult productResult = new ProductResult(productId, processPointId, currentTimestamp);
		productResult.setProductionDate(new java.sql.Date(new java.util.Date().getTime()));
		ProductHistory productHistory = (ProductHistory)productResult;
		productHistory.setAssociateNo("");
		productHistory.setApproverNo("");
		productHistory.setActualTimestamp(currentTimestamp);
		productHistory.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		productHistory.setProcessCount(1);
		ServiceFactory.getDao(ProductResultDao.class).save((ProductResult) productHistory);
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	private void createEngine(String ein, String kdLotNumber, String productSpecCode, String trailerId, String processPointId) throws SQLException {
		Engine engine = new Engine();
		engine.setKdLotNumber(kdLotNumber.trim());
		engine.setProductSpecCode(productSpecCode);
		engine.setPlantCode(PropertyService.getSiteName());
		engine.setProductId(ein);
		engine.setProductionLot("");
		engine.setEngineFiringFlag((short)0);
		engine.setLastPassingProcessPointId(processPointId);
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		engine.setTrackingStatus(processPoint.getLineId());
		getDao(EngineDao.class).saveEngine(engine);
		logger.info("Engine created:" + engine.getProductId());
	}

//	Allow data update only if 
//	1. Engine does not exist or 
//	2. Engine exists but TrackingStatus is not set 
//		or TrackingStatus is in set of parameters from EIN_TRACK_STATUS_EXCLUSIONS
	private boolean checkStatusBeforeUpdate(String ein) {
		boolean result = false;
		Engine eng = getDao(EngineDao.class).findByKey(ein);
		if (eng == null || eng.getTrackingStatus() == null) {
			result = true;
		} else {
			if(lineIds != null && lineIds.size() > 0) {
				if (lineIds.contains(eng.getTrackingStatus())) {
					result = true;
				} else {
					String errorMessage = "The engine is currently online or installed to frame. EngineId: " + ein + ", TrackingStatus: " + eng.getTrackingStatus() + "; Data is not updated.";
					logger.error(errorMessage);
					errorsCollector.error(errorMessage);
				}
			} else {
				String errorMessage = "Expecting EIN_TRACK_STATUS_EXCLUSIONS set for " + this.componentId;
				logger.error(errorMessage);
				errorsCollector.error(errorMessage);
			}
		}
		return result;
	}
	
}
