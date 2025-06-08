package com.honda.galc.oif.task;

/**
 * 
 * <h3>AHM020SecondaryVINTask</h3>
 * <p> AHM020SecondaryVINTask is for AHM020 </p>
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
 * @author Vivek Bettada<br>
 * January 13, 2015
 *
 */

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.oif.dto.SecondaryVinDTO;
import com.honda.galc.oif.property.OifTaskPropertyBean;
//import com.honda.galc.service.CommonNameService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;

public class AHM020SecondaryVINTask  extends OifTask<Object> implements IEventTaskExecutable {

	private static String serviceId = null;
	private boolean isDebug = false;
	private OifErrorsCollector errorsCollector;
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);
	private String productType = null;
	private String engineUrls = null;

	/**
	 * The timestamp to indicate if its date falls in a working production day.
	 * This is mainly used to determine if the interface file needs to be sent
	 * on Saturday. If the Saturday is a production day, the file will be sent.
	 * Otherwise, it will not be sent to MQ.
	 */
	private java.sql.Timestamp prodStartTs;
	private java.sql.Timestamp prodEndTs;


	public AHM020SecondaryVINTask(String componentId) {
		super(componentId);
		serviceId = componentId;
	}

	public void execute(Object[] args) {
		initialize();
		exportRecord();
	}

	public void initialize() {
		logger.info("initializing AHM020SecondaryVIN.");
		isDebug = getPropertyBoolean("DEBUG", true);
		productType = getProperty("ENG_PRODUCT_TYPE", "ENGINE"); // supposed to be "ENGINE" for default
		engineUrls = getProperty("ENGINE_URLS", "LOCAL"); // "LOCAL" as default
		String strProdDateStart = PropertyService.getProperty(componentId, "CUSTOM_PRODUCTION_START");
		String strProdDateEnd = PropertyService.getProperty(componentId, "CUSTOM_PRODUCTION_END");
		Boolean useCustomDate = PropertyService.getPropertyBoolean(componentId, "USE_CUSTOM_PRODUCTION_DATE", false);
		Boolean useLastRunDate = PropertyService.getPropertyBoolean(componentId, "USE_LAST_RUN_DATE", false);

		if (isDebug) {
			logger.info("RES is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.SEND));
			logger.info("AHM020SecondaryVIN is set to "
					+ getProperty("AHM020_MQ_INTERFACE_ID"));
			logger.info("CUSTOM_PRODUCTION_START is set to " + strProdDateStart);
			logger.info("CUSTOM_PRODUCTION_END is set to " + strProdDateEnd);
			logger.info("USE_CUSTOM_PRODUCTION_DATE is set to " + useCustomDate);
			logger.info("USE_LAST_RUN_DATE is set to " + useLastRunDate);
			logger.info("ACTIVE_LINES is set to " + getProperty("ACTIVE_LINES"));
			logger.info("ACTIVE_LINES_URLS is set to "
					+ getProperty("ACTIVE_LINES_URLS"));

			logger.info("MQ_CONFIG is set to "
					+ PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.MQ_CONFIG));
			logger.info("COLLECT_PROCESS_POINT is set to "
					+ getProperty("COLLECT_PROCESS_POINT"));
			logger.info("ENG_PRODUCT_TYPE is set to " + productType);
			logger.info("ENGINE_URLS is set to " + engineUrls);
		}
		errorsCollector = new OifErrorsCollector(serviceId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		/*custom date range can be specified;
		if custom start date is set, but not custom end-date, end-date will be last day of that month
		if custom dates are not set, the default range will be the 1st to the last day of the previous
		month
		eg. May 1 00:00:00 to June 1 00:00:00
		*/
		if (useCustomDate && strProdDateStart != null && !"".equalsIgnoreCase(strProdDateStart.trim())) {
			try {
				java.util.Date prodDateStart = sdf.parse(strProdDateStart);
				Calendar calStart = GregorianCalendar.getInstance();
				Calendar calEnd = GregorianCalendar.getInstance();
				calStart.setTime(prodDateStart);
				prodStartTs = new java.sql.Timestamp(calStart.getTimeInMillis());
				if (strProdDateEnd != null && !"".equalsIgnoreCase(strProdDateEnd.trim())) {
					java.util.Date prodDateEnd = sdf.parse(strProdDateEnd);
					calEnd.setTime(prodDateEnd);
					prodEndTs = new java.sql.Timestamp(calEnd.getTimeInMillis());
				}
				else  {
					setDefaultEndDate(calStart);
				}
			} catch (ParseException e) {
				logger.error("Cannot parse custom production date value, defaulting to previous month");
				setDefaultRange();
			}
		}else if(useLastRunDate) {
			prodStartTs = getLastProcessTimestamp();
			prodEndTs = new java.sql.Timestamp(System.currentTimeMillis());
		}else {
			setDefaultRange();
		}
	}

	private void setDefaultEndDate(Calendar calStart)  throws ParseException  {
		
		if(calStart == null)  throw new ParseException("start date is null", 0);
		Calendar calEnd = GregorianCalendar.getInstance();
		calEnd.set(Calendar.MONTH, (1+calStart.get(Calendar.MONTH))%12);
		calEnd.set(Calendar.YEAR, calStart.get(Calendar.YEAR) + calStart.get(Calendar.MONTH)/11);
		calEnd.set(Calendar.DATE,1);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		prodEndTs = new java.sql.Timestamp(calEnd.getTimeInMillis());
	}
	
	private void setDefaultStartDate()  {
		
		Calendar calStart = GregorianCalendar.getInstance();
		Calendar now = GregorianCalendar.getInstance();
		calStart.set(Calendar.MONTH, (11+now.get(Calendar.MONTH))%12);
		calStart.set(Calendar.YEAR, now.get(Calendar.YEAR) - (11-now.get(Calendar.MONTH))/11);
		calStart.set(Calendar.DATE, 1);
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		
		prodStartTs = new java.sql.Timestamp(calStart.getTimeInMillis());
	}
	
	private void setDefaultRange()  {
		
		setDefaultStartDate();
		Calendar calStart = GregorianCalendar.getInstance();
		calStart.setTimeInMillis(prodStartTs.getTime());
		try {
			setDefaultEndDate(calStart);
		} catch (ParseException e) {
			logger.error("Unknown error setting end date");
			e.printStackTrace();
		}
	}
	
	private void exportRecord() {

		String mqInterfaceID = getProperty("AHM020_INTERFACE_ID");
		String strTimestampPart = new SimpleDateFormat("yyyyMMddHHmmss").format(prodStartTs);
		String exportFileName = mqInterfaceID + strTimestampPart + ".oif";
		String resultPath = PropertyService.getProperty(
				OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		// call the web services on the active lines and get
		// data from each line
		String[] activeLinesURLsArr = getProperty("ACTIVE_LINES_URLS").split(",");
		String activeLineURL = null;
		String strStartTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prodStartTs);
		String strEndTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prodEndTs);
		try {
			List<SecondaryVinDTO> sVinDTOList = new ArrayList<SecondaryVinDTO>();
			for (String lineURL : activeLinesURLsArr){
				activeLineURL = lineURL;
				sVinDTOList.addAll( getSecondaryVins(strStartTs, strEndTs, serviceId, lineURL) );
			}
			exportRecord(SecondaryVinDTO.class, sVinDTOList);
			updateLastProcessTimestamp(prodEndTs);
		} catch (Exception e) {
			String errorStr = "Exception raised when sending interface file for "
					+ resultPath
					+ exportFileName
					+ " for the MQ interface "
					+ mqInterfaceID
					+ " URL: "
					+ activeLineURL
					+ "  Exception : " + e.getMessage();
			logger.error(errorStr);
			errorsCollector.emergency(errorStr);
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	private <K extends IOutputFormat> void exportRecord(Class<K> outputClass,
			List<K> outputData) {
		String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		String mqInterfaceID = getProperty(OIFConstants.INTERFACE_ID);
		Timestamp productionTimestamp = new java.sql.Timestamp(
				(new java.util.Date()).getTime());
		String strTimestampPart = new SimpleDateFormat("yyyyMMddHHmmss").format(productionTimestamp);
		String exportFileName = mqInterfaceID + strTimestampPart + ".oif";
		String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
		String opFormatDefKey = getProperty(OIFConstants.OUTPUT_FORMAT_DEFS);
		exportDataByOutputFormatHelper(outputClass, outputData, resultPath, exportFileName, mqConfig, opFormatDefKey);
	} 
	
	public List<SecondaryVinDTO> getSecondaryVins(String startTime, String endTime, String serviceId, String lineURL) throws Exception {

		List<SecondaryVinDTO> sVinDTOList = new ArrayList<SecondaryVinDTO>();
		try {
			String pp = PropertyService.getProperty(serviceId, "COLLECT_PROCESS_POINT", null);
			String separator = PropertyService.getProperty(serviceId, "INSTALLED_PART_NAMES_SEPARATOR", ",");
			List<String> partNameList = PropertyService.getPropertyList(serviceId, "INSTALLED_PART_NAMES",separator);
			OifTaskPropertyBean properties = PropertyService.getPropertyBean(OifTaskPropertyBean.class, serviceId);
			Map<String, String> dtoPartNameMapping = properties.getNameMapping();
			InstalledPartDao daoIPart = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, InstalledPartDao.class);
			ProductResultDao daoPResult = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, ProductResultDao.class);
			FrameDao frameDao = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, FrameDao.class);
			Map<String, String> dtoPartNameMappingCommName = new HashMap<String, String>();
			InstalledPartDao daoIPartEngineLine = null;
			EngineDao engineDaoEngineLine = null;
			// NOTE: specify the URLs to Engine environments, use "LOCAL" for the same line
			String[] engineURLsArr = new String[0];
			if (StringUtils.isNotBlank(engineUrls))
				engineURLsArr = engineUrls.split(",");
			
			HashMap<String, String> thisEntry = null;
			List<ProductResult> resultList = daoPResult.getSecondaryVinsByProcessPointAndDateRange(pp, startTime, endTime);
			for(ProductResult thisProductResult : resultList)  {
				String productId = thisProductResult.getProductId();
				if (dtoPartNameMapping != null) {
					Iterator<String> iterator = dtoPartNameMapping.keySet().iterator();
					while (iterator.hasNext()) {
					    String partName = iterator.next();
					    String commonPartName = ServiceFactory.getDao(InstalledPartDao.class).findOneInstalledPartByCommonName(productId, partName);
					    dtoPartNameMappingCommName.put(commonPartName, dtoPartNameMapping.get(partName));
					 }  
				}
				List<InstalledPart> commonNamePartsList = ServiceFactory.getDao(InstalledPartDao.class).findAllInstalledPartByCommonNameList(productId, partNameList, StringUtils.EMPTY);
				if(commonNamePartsList == null || commonNamePartsList.isEmpty()) {
					commonNamePartsList = daoIPart.findAllByProductIdAndPartNames(productId, partNameList);
				}
				List<InstalledPart> partsList = new ArrayList<InstalledPart>(commonNamePartsList);
				Frame thisFrame = frameDao.findByKey(productId);
				String engineSerialNo =  "";
				if (thisFrame!=null)
					engineSerialNo = thisFrame.getEngineSerialNo();
				
				thisEntry = new HashMap<String, String>();
				thisEntry.put("PRODUCT_ID", productId);
				thisEntry.put("ENGINE_SERIAL_NO", engineSerialNo);
				if(productId != null)  {
					String value = ProductNumberDef.justifyJapaneseVIN(productId, JapanVINLeftJustified.booleanValue());
					thisEntry.put("FRAME_NO", value);								
				}
				
				if(StringUtils.isNotBlank(engineSerialNo)){
					String value = String.format("%12s", engineSerialNo.trim());
					thisEntry.put("ENGINE_NO", value);								
					
					// allow multiple Engine Lines
					for (String engineUrl : engineURLsArr) {
						if (StringUtils.isNotBlank(engineUrl)){
							// it determines if it's necessary to use the same line on we are working or a custom url for engine line.
							String auxEngineURL = engineUrl.equalsIgnoreCase("LOCAL") ? lineURL : engineUrl;
							daoIPartEngineLine = HttpServiceProvider.getService(auxEngineURL + OIFConstants.HTTP_SERVICE_URL_PART, InstalledPartDao.class);
							engineDaoEngineLine = HttpServiceProvider.getService(auxEngineURL + OIFConstants.HTTP_SERVICE_URL_PART, EngineDao.class);
						}
						
						String missionSerialNo = "";
						if (engineDaoEngineLine!=null){
							Engine thisEngine = engineDaoEngineLine.findByKey(engineSerialNo);
							if (thisEngine!=null)
								missionSerialNo = thisEngine.getMissionSerialNo();
						}
						thisEntry.put("MISSION_SERIAL_NO", missionSerialNo);
						
						// Engine Parts List. Try to obtain Installed Parts for the Engine associated with the Frame
						if (StringUtils.isNotBlank(productType) && daoIPartEngineLine!=null){
							List<InstalledPart> enginePartsList = daoIPartEngineLine.findAllEngineByEngineProductIdAndPartNames(productType, engineSerialNo, partNameList);
							partsList.addAll(enginePartsList);
						}
						
						daoIPartEngineLine = null;
						engineDaoEngineLine = null;
					}
				
				}
				
				for (InstalledPart thisPart : partsList)  {
					String partName = thisPart.getPartName();
					if (dtoPartNameMappingCommName != null) {
						String dtoPartName = dtoPartNameMappingCommName.get(partName);
						if (StringUtils.isNotBlank(dtoPartName)) {
							partName = StringUtils.trim(dtoPartName);
						}
					}
					thisEntry.put(partName, thisPart.getPartSerialNumber());
				}
				dtoPartNameMappingCommName.clear();
				SecondaryVinDTO sVinDTO = new SecondaryVinDTO();
				sVinDTO.initialize(thisEntry);
				sVinDTOList.add(sVinDTO);
			}
			
		} catch (Exception e) {
			String errMsg = "Exception occured in ProductionResultServiceImpl.getSecondaryVins(...) method. The exception message is:- " + e.getMessage();
			logger.error(e, errMsg);
			throw e;
		}
		return sVinDTOList;
	}

}