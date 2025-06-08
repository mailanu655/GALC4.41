package com.honda.galc.oif.task.warranty;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.FlfFieldConfig;
import com.honda.galc.common.FlfFormat;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.property.FrameWarrantyPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.ProductSpecUtil;

/**
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>FrameWarrantyTask</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Mar 20, 2015
 */
public class FrameWarrantyTask extends OifTask<Object> implements IEventTaskExecutable {

	public static final String LAST_PROCESS_TIMESTAMP = "LAST_PROCESS_TIMESTAMP";
	public static final String PRODUCT_ID = "PRODUCT_ID";

	private FrameWarrantyPropertyBean property;

	private ComponentPropertyDao propertyDao;
	private FrameDao frameDao;
	private FrameSpecDao frameSpecDao;
	private EngineDao engineDao;
	private DailyDepartmentScheduleDao scheduleDao;
	private ProcessPointDao processPointDao;
	private ProcessPointDao engProcessPointDao;
	private DailyDepartmentScheduleDao engScheduleDao;
	private SalesOrderFifDao salesDao = null;
	private static final String BEV_BUILD_ATTRIBUTE="BEV_MODEL";
	private static final String VIN_INDICATOR_ICE = "ICE";
	private static final String VIN_INDICATOR_BEV = "BEV";

	public FrameWarrantyTask(String name) {
		super(name);
		this.property = PropertyService.getPropertyBean(FrameWarrantyPropertyBean.class, getComponentId());
		this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		this.frameDao = ServiceFactory.getDao(FrameDao.class);
		this.frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		this.scheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		this.scheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		this.processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		this.salesDao = ServiceFactory.getDao(SalesOrderFifDao.class);
		String engineServiceUrl = getProperty().getEngineServiceUrl();
		if (StringUtils.isNotBlank(engineServiceUrl)) {
			if ("LOCAL".equals(engineServiceUrl)) {
				this.engineDao = ServiceFactory.getDao(EngineDao.class);
				this.engProcessPointDao = getProcessPointDao();
				this.engScheduleDao = getScheduleDao();
			} else {
				this.engineDao = HttpServiceProvider.getService(engineServiceUrl, EngineDao.class);
				this.engProcessPointDao = HttpServiceProvider.getService(engineServiceUrl, ProcessPointDao.class);
				this.engScheduleDao = HttpServiceProvider.getService(engineServiceUrl, DailyDepartmentScheduleDao.class);
			}
		}
	}

	public void execute(Object[] args) {

		refreshProperties();

		Timestamp startTimestamp = getLastProcessTimestamp();
		Timestamp currentTimestamp = getCurrentTime(true);

		logger.info(String.format("Processing data for %s - %s", startTimestamp, currentTimestamp));
		
		String[] activeLinesURLsArr = getProperty().getActiveLinesUrls();
		List<String> data = new ArrayList<String>();
		
		if(activeLinesURLsArr != null && activeLinesURLsArr.length > 0) {
			for(String lineUrl : activeLinesURLsArr) {
				data.addAll(createData(startTimestamp, lineUrl));
			}
		} else {
			data.addAll(createData(startTimestamp, null));
		}

		if (data == null || data.isEmpty()) {
			logger.info("No Warranty Records to send");
			return;
		}

		try {
			send(data);
			logger.info(String.format("Send  %s warranty records", data.size()));
			updateLastProcessTimestamp(currentTimestamp);
		} catch(MQUtilityException e) {
			String errorMsg = String.format("Exception occurred when trying to send frame warranty information %s - %s", startTimestamp, currentTimestamp);
			logger.error(e, errorMsg);
			errorsCollector.error(errorMsg);
		}catch (Exception e) {
			String errorMsg = String.format("Exception occured when processing FrameWarrantyTask for %s - %s", startTimestamp, currentTimestamp);
			logger.error(e, errorMsg);
			errorsCollector.error(errorMsg);
		}

	}

	private String getVinInd(String productSpecCode,List<BuildAttribute> attributeList) {
		if(attributeList.size() > 0) {
			BuildAttribute attribute = ProductSpecUtil.getMatched(productSpecCode, attributeList, BuildAttribute.class);
			if(attribute != null) {
				boolean result = StringUtils.equalsIgnoreCase(attribute.getAttributeValue(), "true");
				if(result) {
					return VIN_INDICATOR_BEV;
				}
			}
		}
		return VIN_INDICATOR_ICE;
	}

	protected List<String> createData(Timestamp startTimestamp, String lineUrl) {
		List<Map<String, Object>> frameData = selectFrameData(startTimestamp, lineUrl);
		if (frameData == null || frameData.isEmpty()) {
			return new ArrayList<String>();
		}
		List<BuildAttribute> attributeList = getDao(BuildAttributeDao.class).findAllByAttribute(BEV_BUILD_ATTRIBUTE);
		for(Map<String,Object> thisMap : frameData)  {
			String productId = thisMap.get(PRODUCT_ID).toString();
			FrameDao frmDao = null;
			FrameSpecDao frmSpcDao = null;
			SalesOrderFifDao salesOrderFifDao = null;
			
			if(lineUrl != null) {
				frmDao = HttpServiceProvider.getService(lineUrl + OIFConstants.HTTP_SERVICE_URL_PART, FrameDao.class);
				frmSpcDao = HttpServiceProvider.getService(lineUrl + OIFConstants.HTTP_SERVICE_URL_PART, FrameSpecDao.class);;
				salesOrderFifDao = HttpServiceProvider.getService(lineUrl + OIFConstants.HTTP_SERVICE_URL_PART, SalesOrderFifDao.class);;
			} else {
				frmDao = frameDao;
				frmSpcDao = frameSpecDao;
				salesOrderFifDao = salesDao;
			}
			
			Frame thisFrame = frmDao.findByKey(productId);
			if(thisFrame == null) continue;
			String productSpecCode = thisFrame.getProductSpecCode();
			FrameSpec fSpec = frmSpcDao.findByKey(productSpecCode);
			
			String fifCodes = salesOrderFifDao.getFIFCodeByProductSpec(fSpec);
			
			thisMap.put("FIF_CODES", StringUtils.trimToEmpty(fifCodes));
			thisMap.put("VIN_IND",getVinInd(productSpecCode,attributeList));
			
		}
		if (isCollectEngineData()) {
			List<String> eins = getEins(frameData);
			if (eins != null && !eins.isEmpty()) {
				Map<String, Map<String, Object>> engineData = selectEngineData(eins, lineUrl);
				frameData = mergeData(frameData, engineData);
			}
		}
		
		Map<String, String> formatMap = getProperty().getParseLineDefs();
		int lineLength = getProperty().getMessageLineLength();
		List<FlfFieldConfig> fflConfigs = FlfFieldConfig.parseFieldConfigs(formatMap);
		List<String> list = FlfFormat.format(frameData, fflConfigs, lineLength);
		return list;
	}

	// === dao api === //
	protected List<Map<String, Object>> selectFrameData(Timestamp startTimestamp, String lineUrl) {
		String[] selectingProcessPointIds = getProperty().getSelectingProcessPointIds();
		String[] notSellableTrackingStatus = getProperty().getNotSellableTrackingStatus();
		Map<String, String> historyProcessPointids = getProperty().getProcessPointIds();
		Map<String, String> historyProcessLocations = getProperty().getProcessLocations();
		
		String[] plantCodesToExclude = null;
		if (getProperty().isExcludeListedPlantCodes()){
			plantCodesToExclude = getProperty().getPlantCodesToExclude();
		}
		
		FrameDao frameDao = null;
		
		if(lineUrl != null) {
			frameDao = HttpServiceProvider.getService(lineUrl + OIFConstants.HTTP_SERVICE_URL_PART, FrameDao.class);
		} else {
			frameDao = getFrameDao();
		}
		
		List<Map<String, Object>> productData = frameDao.findAllSalesWarrantyData(startTimestamp, selectingProcessPointIds, 
				notSellableTrackingStatus, historyProcessPointids,historyProcessLocations, plantCodesToExclude);
		return productData;
	}

	protected Map<String, Map<String, Object>> selectEngineData(List<String> eins, String lineUrl) {

		Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();

		String aeOff = getProperty().getAeOffProcessPointId();
		String blockPartName = getProperty().getBlockPartName();
		String headPartName = getProperty().getHeadPartName();
		String transmissionPartName = getProperty().getTransmissionPartName();

		EngineDao engineDao = null;
		
		if(lineUrl != null) {
			engineDao = HttpServiceProvider.getService(lineUrl + OIFConstants.HTTP_SERVICE_URL_PART, EngineDao.class);
		} else {
			engineDao = getEngineDao();
		}
		
		List<Map<String, Object>> engines = engineDao.findAllSalesWarrantyFrData(eins, aeOff, blockPartName, headPartName, transmissionPartName);

		if (engines == null) {
			return data;
		}
		for (Map<String, Object> map : engines) {
			if (map == null) {
				continue;
			}
			String ein = (String) map.get("EIN");
			if (StringUtils.isBlank(ein)) {
				continue;
			}
			setEnginePartNames(map);
			data.put(StringUtils.trim(ein), map);
		}
		return data;
	}

	// === prepare data/utility === //
	protected void setEnginePartNames(Map<String, Object> engineMap) {
		int blockNumberMachineIx = getProperty().getBlockNumberMachineIx();
		int headNumberMachineIx = getProperty().getHeadNumberMachineIx();

		String blockNo = StringUtils.trim((String) engineMap.get("BLOCK_NO"));
		String headNo = StringUtils.trim((String) engineMap.get("HEAD_NO"));
		String blockNoName = "BLOCK_NO_1";
		String headNoName = "HEAD_NO_1";
		if (isFromMachine('2', blockNumberMachineIx, blockNo)) {
			blockNoName = "BLOCK_NO_2";
		}
		if (isFromMachine('2', headNumberMachineIx, headNo)) {
			headNoName = "HEAD_NO_2";
		}
		engineMap.put(blockNoName, blockNo);
		engineMap.put(headNoName, headNo);
	}

	protected boolean isFromMachine(char machineId, int machineIx, String number) {
		if (StringUtils.isBlank(number)) {
			return false;
		}
		if (machineIx < 0 || machineIx >= number.length()) {
			return false;
		}
		return machineId == number.charAt(machineIx);
	}

	protected List<String> getEins(List<Map<String, Object>> frameData) {
		List<String> eins = new ArrayList<String>();
		if (frameData == null) {
			return eins;
		}
		for (Map<String, Object> map : frameData) {
			if (map == null) {
				continue;
			}
			String ein = (String) map.get("ENGINE_SERIAL_NO");
			if (StringUtils.isBlank(ein)) {
				continue;
			}
			eins.add(StringUtils.trim(ein));
		}
		return eins;
	}

	protected List<Map<String, Object>> mergeData(List<Map<String, Object>> frameData, Map<String, Map<String, Object>> engineData) {
		if (engineData == null || engineData.isEmpty()) {
			return frameData;
		}
		for (Map<String, Object> map : frameData) {
			String ein = (String) map.get("ENGINE_SERIAL_NO");
			if (StringUtils.isBlank(ein)) {
				continue;
			}
			ein = StringUtils.trim(ein);
			Map<String, Object> engineMap = engineData.get(ein);
			if (engineMap != null) {
				map.putAll(engineMap);
			}
		}
		return frameData;
	}

	// === get/set === //
	protected boolean isCollectEngineData() {
		return getEngineDao() != null;
	}

	public FrameWarrantyPropertyBean getProperty() {
		return property;
	}

	public void setProperty(FrameWarrantyPropertyBean property) {
		this.property = property;
	}

	public FrameDao getFrameDao() {
		return frameDao;
	}

	public void setFrameDao(FrameDao frameDao) {
		this.frameDao = frameDao;
	}

	public EngineDao getEngineDao() {
		return engineDao;
	}

	public void setEngineDao(EngineDao engineDao) {
		this.engineDao = engineDao;
	}

	public ComponentPropertyDao getPropertyDao() {
		return propertyDao;
	}

	public void setPropertyDao(ComponentPropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	protected DailyDepartmentScheduleDao getScheduleDao() {
		return scheduleDao;
	}

	protected ProcessPointDao getProcessPointDao() {
		return processPointDao;
	}

	protected ProcessPointDao getEngProcessPointDao() {
		return engProcessPointDao;
	}

	protected DailyDepartmentScheduleDao getEngScheduleDao() {
		return engScheduleDao;
	}
}
