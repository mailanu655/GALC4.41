package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.WarGalcDto;
import com.honda.galc.service.msip.property.outbound.WarGalcPropertyBean;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class WarGalcHandler  extends BaseMsipOutboundHandler<WarGalcPropertyBean> {
	public static final String LAST_PROCESS_TIMESTAMP = "LAST_PROCESS_TIMESTAMP";

	private ComponentPropertyDao propertyDao;
	private FrameDao frameDao;
	private EngineDao engineDao;
	private DailyDepartmentScheduleDao scheduleDao;
	private ProcessPointDao processPointDao;
	private ProcessPointDao engProcessPointDao;
	private DailyDepartmentScheduleDao engScheduleDao;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<WarGalcDto> fetchDetails(Date startTimestamp) {
		List<WarGalcDto> dtoList = new ArrayList<WarGalcDto>();
		try{
			Timestamp sTs = new Timestamp(startTimestamp.getTime());
			inititialize();
			return getFrameWarrantyInfo(sTs);
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			WarGalcDto dto = new WarGalcDto();
			e.printStackTrace();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<WarGalcDto> fetchDetails() {
		List<WarGalcDto> dtoList = new ArrayList<WarGalcDto>();
		try{
			Timestamp sTs = new Timestamp(System.currentTimeMillis());
			inititialize();
			return getFrameWarrantyInfo(sTs);
		}catch(Exception e){
			dtoList.clear();
			WarGalcDto dto = new WarGalcDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}		
	}
	
	public List<WarGalcDto> getFrameWarrantyInfo(Timestamp startTimestamp) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

		getLogger().info(String.format("Processing data for %s - %s", startTimestamp, currentTimestamp));
		List<Map<String, Object>> data = createData(startTimestamp);
		List<WarGalcDto> listOfWarGalc = new ArrayList<WarGalcDto>();
		if(!data.isEmpty()){
			getLogger().info("data size :: " + data.size());
			for(Map<String, Object> map : data) {
				WarGalcDto dto = new WarGalcDto();
				dto.setKeyNo(String.valueOf(map.get("KEY_NO")));
				dto.setKeyNo(String.valueOf(map.get("KEY_NO")));
				dto.setAfOnSequenceNumber(String.valueOf(map.get("AF_ON_SEQUENCE_NUMBER")));
				dto.setModelOptionCode(String.valueOf(map.get("MODEL_OPTION_CODE")));
				dto.setModelCode(String.valueOf(map.get("MODEL_CODE")));
				dto.setTime(String.valueOf(map.get("GAL136TBX_TIME")));
				dto.setDate(String.valueOf(map.get("GAL136TBX_DATE")));
				dto.setExtColorCode(String.valueOf(map.get("EXT_COLOR_CODE")));
				dto.setFifCodes(String.valueOf(map.get("FIF_CODES")));
				dto.setProductionLot(String.valueOf(map.get("PRODUCTION_LOT")));
				dto.setProductId(String.valueOf(map.get("PRODUCT_ID")));
				dto.setEngineSerialNo(String.valueOf(map.get("ENGINE_SERIAL_NO")));
				dto.setFramePlantCode(String.valueOf(map.get("FRAME_PLANT_CODE")));
				dto.setLineNumber(String.valueOf(map.get("LINE_NUMBER")));
				dto.setModelYearCode(String.valueOf(map.get("MODEL_YEAR_CODE")));
				dto.setIntColorCode(String.valueOf(map.get("INT_COLOR_CODE")));
				dto.setAfLineNumber(String.valueOf(map.get("AF_LINE_NUMBER")));
				dto.setActualTimestamp(String.valueOf(map.get("GAL136TBX_ACTUAL_TIMESTAMP")));
				dto.setEngKdLotNumber(String.valueOf(map.get("ENG_KD_LOT_NUMBER")));
				dto.setModelTypeCode(String.valueOf(map.get("MODEL_TYPE_CODE")));
				dto.setKdLotNumber(String.valueOf(map.get("KD_LOT_NUMBER")));
				dto.setEnginePlantCode(String.valueOf(map.get("ENGINE_PLANT_CODE")));
				
				listOfWarGalc.add(dto);
			}
		}
		getLogger().info("Exit WarGalc Handler");
		return listOfWarGalc;
	}
	protected List<Map<String, Object>> createData(Timestamp startTimestamp) {
		List<Map<String, Object>> frameData = selectFrameData(startTimestamp);
		if (frameData == null || frameData.isEmpty()) {
			return null;
		}
		if (isCollectEngineData()) {
			List<String> eins = getEins(frameData);
			if (eins != null && !eins.isEmpty()) {
				Map<String, Map<String, Object>> engineData = selectEngineData(eins);
				frameData = mergeData(frameData, engineData);
			}
		}		
		return frameData;
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
	protected Map<String, Map<String, Object>> selectEngineData(List<String> eins) {

		Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();

		String aeOff = getPropertyBean().getAeOffProcessPointId();
		String blockPartName = getPropertyBean().getBlockPartName();
		String headPartName = getPropertyBean().getHeadPartName();
		String transmissionPartName = getPropertyBean().getTransmissionPartName();

		List<Map<String, Object>> engines = getEngineDao().findAllSalesWarrantyFrData(eins, aeOff, blockPartName, headPartName, transmissionPartName);

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
			int blockNumberMachineIx = getPropertyBean().getBlockNumberMachineIx();
			int headNumberMachineIx = getPropertyBean().getHeadNumberMachineIx();

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
	// === dao api === //
		protected List<Map<String, Object>> selectFrameData(Timestamp startTimestamp) {
			getLogger().info("getComponentId() :: " + getComponentId());
			String[] selectingProcessPointIds = getPropertyBean().getSelectingProcessPointIds();
			String[] notSellableTrackingStatus = getPropertyBean().getNotSellableTrackingStatus();
			Map<String, String> historyProcessPointids = getPropertyBean().getProcessPointIds();
			Map<String, String> historyProcessLocations = getPropertyBean().getProcessPointLocations();
			
			String[] plantCodesToExclude = null;
			if (getPropertyBean().getIsExcludeListedPlantCodes()){
				plantCodesToExclude = getPropertyBean().getPlantCodesToExclude();
			}
			
			List<Map<String, Object>> productData = getFrameDao().findAllSalesWarrantyData(startTimestamp, selectingProcessPointIds, 
					notSellableTrackingStatus, historyProcessPointids,historyProcessLocations, plantCodesToExclude);
			return productData;
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
		public void inititialize() {
			this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
			this.frameDao = ServiceFactory.getDao(FrameDao.class);
			this.scheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
			this.scheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
			this.processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
			String engineServiceUrl = getPropertyBean().getEngineServiceUrl();
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

}
