package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dao.qi.QiDefectDeviceDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiExternalSystemDataDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectIdMapDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiExternalSystemInfoDao;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiMappingCombinationDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectDevice;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDefectId;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiExternalSystemInfo;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiMappingCombination;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.defect.ScrapService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.qics.QicsDefectInfoManager;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.DailyDepartmentScheduleUtil;

public class HeadlessNaqServiceImpl implements
		HeadlessNaqService {
		
	private static final short DEFAULT_PROD_LINE_NO = 1;
	private boolean validateProductIDLength(String productType,String productId) {
		ProductTypeData productTypeDataFromConfig = getDao(ProductTypeDao.class)
		        .findByKey(productType);
		// Return early if no product types found
		if (productTypeDataFromConfig == null) {
		    return false;
		}
		// Extract valid formats from the first matching ProductTypeData
		String formatString = productTypeDataFromConfig.getProductIdFormatType();
		if (formatString == null || formatString.isEmpty()) {
		    return false;
		}
		Set<String> validProductFormats = Arrays.stream(formatString.split(","))
		        .map(String::trim)
		        .filter(s -> !s.isEmpty())
		        .collect(Collectors.toSet());
		if (validProductFormats.isEmpty()) {
		    return false;
		}
		// Filter ProductNumberDefs based on the valid formats
		List<ProductNumberDef> matchingDefs = ProductNumberDef.getProductNumberDef(ProductType.getType(productType))
		        .stream()
		        .filter(def -> validProductFormats.contains(def.getName()))
		        .collect(Collectors.toList());
		// Check if any definition matches the productId length
		return matchingDefs.stream().anyMatch(def -> productId.length() == def.getLength());

		}

	public int saveDefectData(DefectMapDto defectMapDto) {
		try {	
			String externalSystemName = defectMapDto.getExternalSystemName();
			String externalSystemPartCode =defectMapDto.getExternalPartCode();
			String externalSystemDefectCode = defectMapDto.getExternalDefectCode();
			String productId = defectMapDto.getProductId();
			String productType = defectMapDto.getProductType();
			String mtcModel = "";
			String applicationId = defectMapDto.getProcessPointId();
			
			if(defectMapDto.getExternalSystemKey() == null)  {
				defectMapDto.setExternalSystemKey(0L);
			}
			QiExternalSystemData qiExternalSystemData = setExternalSystemData(defectMapDto);
			if (StringUtils.isBlank(externalSystemName)
					|| StringUtils.isBlank(externalSystemPartCode) || StringUtils.isBlank(externalSystemDefectCode)
					|| StringUtils.isBlank(productId) || StringUtils.isBlank(productType)
					|| StringUtils.isBlank(applicationId))  {
				createQiExternalSystemData(defectMapDto, qiExternalSystemData);
				return QiConstant.SC_NOT_ACCEPTABLE;
			}

			if(!validateProductIDLength(productType,productId)) {
				if(!isExternalSystemDataExists(qiExternalSystemData))
					getDao(QiExternalSystemDataDao.class).insert(qiExternalSystemData);
				return QiConstant.SC_LENGTH_REQUIRED;
			}
			
			if (!defectMapDto.validateDefectDataTypeAndLength() && !defectMapDto.isReprocess()){
				if(!isExternalSystemDataExists(qiExternalSystemData))
					getDao(QiExternalSystemDataDao.class).insert(qiExternalSystemData);
				return QiConstant.SC_LENGTH_REQUIRED;
			}
			
			QiExternalSystemDefectMap qiExternalSystemDefect = null;
			QiEntryModelGrouping entryModelGroup = null;
			QiEntryScreen entryScreen = null;
			mtcModel = getMtcModel(defectMapDto.getProductType().toUpperCase(), defectMapDto.getProductId());
			if(!StringUtils.isBlank(mtcModel))  {
				entryModelGroup = getDao(QiEntryModelGroupingDao.class).findByMtcModel(mtcModel, defectMapDto.getProductType().toUpperCase());
			}
			if(entryModelGroup != null)  {
				qiExternalSystemDefect = getDao(QiExternalSystemDefectMapDao.class)
						.findByPartAndDefectCodeExternalSystemAndEntryModel(
								externalSystemPartCode,
								externalSystemDefectCode,
								externalSystemName,
								entryModelGroup.getId().getEntryModel()
						);
			}
			if(qiExternalSystemDefect!=null){
				QiLocalDefectCombination  localDefectCombination = getDao(QiLocalDefectCombinationDao.class).findByKey(qiExternalSystemDefect.getLocalDefectCombinationId());
				QiEntryScreenId qiEntryScreenId = new QiEntryScreenId(); 
				qiEntryScreenId.setEntryScreen(localDefectCombination.getEntryScreen()); 
				qiEntryScreenId.setEntryModel(localDefectCombination.getEntryModel()); 
				qiEntryScreenId.setIsUsed(localDefectCombination.getIsUsed()); 
				entryScreen = getDao(QiEntryScreenDao.class).findByKey(qiEntryScreenId);
			}
			
			if(qiExternalSystemDefect == null ||  (!defectMapDto.isLotControl() &&  !validateDefectDataWithExistingData(defectMapDto ,entryScreen))) {
				createQiExternalSystemData(defectMapDto, qiExternalSystemData);
				return QiConstant.SC_NOT_FOUND;
			}
			if (!saveDefectDataFromExternalSystem(defectMapDto,qiExternalSystemDefect,externalSystemName ,entryScreen, null)){
				createQiExternalSystemData(defectMapDto, qiExternalSystemData);
				return QiConstant.SC_PARTIAL_CONTENT;
			}
		} catch (Exception e) {
			getLogger().error(e, "Exception when collect data for ",
					this.getClass().getSimpleName());
			return QiConstant.SC_NOT_FOUND;
		}
		return QiConstant.SC_CREATED;
	}

    public String getMtcModel(String productType, String productId )  {
    	String mtcModel = StringUtils.EMPTY;
    	if(StringUtils.isBlank(productId) || StringUtils.isBlank(productType))  return StringUtils.EMPTY;
		BaseProduct	product = ProductTypeUtil.findProduct(productType, productId);
    	if(product == null)  return StringUtils.EMPTY;
		if(ProductType.MBPN.toString().equalsIgnoreCase(productType) && product instanceof MbpnProduct)  {
			mtcModel = product.getModelCode();
		}
		else if(isDieCastType(productType, product))  {
			mtcModel = product.getModelCode();
		}
		else if(!StringUtils.isBlank(product.getProductSpecCode())) {
			String productSpecCode = product.getProductSpecCode().trim();
			if(productSpecCode.length() > 3) {
				mtcModel = productSpecCode.substring(0,4);
			} else {
				mtcModel = productSpecCode;
			}
		}
		return mtcModel;
    }
	

    public boolean isDieCastType(String productType, BaseProduct product)  {
		if(
			   (ProductType.HEAD.toString().equalsIgnoreCase(productType) && product instanceof Head)
			|| (ProductType.BLOCK.toString().equalsIgnoreCase(productType) && product instanceof Block)
			|| (ProductType.CONROD.toString().equalsIgnoreCase(productType) && product instanceof Conrod)
			|| (ProductType.CRANKSHAFT.toString().equalsIgnoreCase(productType) && product instanceof Crankshaft)
		)  {
			return true;
		}
		else  {
			return false;
		}
    }
    
	/**
	 * @param defectMapDto
	 * @param qiExternalSystemData
	 */
	private void createQiExternalSystemData(DefectMapDto defectMapDto,
			QiExternalSystemData qiExternalSystemData) {
		if(!isExternalSystemDataExists(qiExternalSystemData) && !defectMapDto.isReprocess())
			getDao(QiExternalSystemDataDao.class).insert(qiExternalSystemData);
	}
	
	private QiExternalSystemData setExternalSystemData(DefectMapDto defectMapDto) {
		DefectMapDto defectMapDtoId=new DefectMapDto();
		defectMapDtoId.setExternalSystemName((StringUtils.length(defectMapDto.getExternalSystemName()) <= 18)?defectMapDto.getExternalSystemName():defectMapDto.getExternalSystemName().substring(0, 17));
		defectMapDtoId.setExternalPartCode((StringUtils.length(defectMapDto.getExternalPartCode()) <= 64)?defectMapDto.getExternalPartCode():defectMapDto.getExternalPartCode().substring(0, 63));
		defectMapDtoId.setExternalDefectCode((StringUtils.length(defectMapDto.getExternalDefectCode()) <= 64)?defectMapDto.getExternalDefectCode():defectMapDto.getExternalDefectCode().substring(0, 63));
		defectMapDtoId.setProductId(defectMapDto.getProductId());
		defectMapDtoId.setProcessPointId((StringUtils.length(defectMapDto.getProcessPointId()) <= 17)?defectMapDto.getProcessPointId():defectMapDto.getProcessPointId().substring(0, 16));
		
		QiExternalSystemData qiExternalSystemData = new QiExternalSystemData(defectMapDtoId);
		qiExternalSystemData.setId(qiExternalSystemData.getId());
		qiExternalSystemData.setEntrySite((StringUtils.length(defectMapDto.getEntrySite()) <= 16)?defectMapDto.getEntrySite():"");
		qiExternalSystemData.setEntryDept((StringUtils.length(defectMapDto.getEntryDepartment()) <= 32)?defectMapDto.getEntryDepartment():"");
		qiExternalSystemData.setProductType((StringUtils.length(defectMapDto.getProductType()) <= 10)?defectMapDto.getProductType():"");
		qiExternalSystemData.setAssociateId((StringUtils.length(defectMapDto.getAssociateId()) <= 11)?defectMapDto.getAssociateId():"");
		qiExternalSystemData.setWriteUpDept((StringUtils.length(defectMapDto.getWriteupDepartment()) <= 32)?defectMapDto.getWriteupDepartment():"");
		qiExternalSystemData.setImageName((StringUtils.length(defectMapDto.getImageName()) <=20)?defectMapDto.getImageName():"");
		
		qiExternalSystemData.setExternalSystemDefectKey(0);			
		if (defectMapDto.getExternalSystemKey() != null && defectMapDto.getExternalSystemKey() > 0) {
			qiExternalSystemData.setExternalSystemDefectKey(defectMapDto.getExternalSystemKey());
		}
		if(StringUtils.isEmpty(defectMapDto.getOriginalDefectStatus()) || !StringUtils.isNumeric(defectMapDto.getOriginalDefectStatus()) || !(StringUtils.length(defectMapDto.getOriginalDefectStatus()) <= 5))
			qiExternalSystemData.setOriginalDefectStatus((short) 0);
		else 
			qiExternalSystemData.setOriginalDefectStatus(Short.valueOf(defectMapDto.getOriginalDefectStatus()));
		
		if(StringUtils.isEmpty(defectMapDto.getCurrentDefectStatus()) || !StringUtils.isNumeric(defectMapDto.getCurrentDefectStatus()) || !(StringUtils.length(defectMapDto.getCurrentDefectStatus())<=6))
			qiExternalSystemData.setCurrentDefectStatus((short) 0);
		else
			qiExternalSystemData.setCurrentDefectStatus(Short.valueOf(defectMapDto.getCurrentDefectStatus()));
		
		if( !StringUtils.isNumeric(defectMapDto.getxAxis()) || !(StringUtils.length(defectMapDto.getxAxis())<= 10))
			qiExternalSystemData.setPointX(0);
		else
			qiExternalSystemData.setPointX(Integer.valueOf(defectMapDto.getxAxis()));
		
		if(!StringUtils.isNumeric(defectMapDto.getyAxis()) || !(StringUtils.length(defectMapDto.getyAxis()) <= 10))
			qiExternalSystemData.setPointY(0);
		else
			qiExternalSystemData.setPointY(Integer.valueOf(defectMapDto.getyAxis()));
		
		return qiExternalSystemData;
	}

	private boolean isExternalSystemDataExists(QiExternalSystemData qiExternalSystemData) {
		QiExternalSystemData systemData = getDao(QiExternalSystemDataDao.class).findByKey(qiExternalSystemData.getId());		
		return systemData!=null;
	}
	
	

	private boolean validateDefectDataWithExistingData(DefectMapDto defectMapDto , QiEntryScreen entryScreen) {
		if (!StringUtils.isEmpty(defectMapDto.getProductType())
				&& ProductTypeUtil
						.getTypeUtil(defectMapDto.getProductType().toUpperCase()).getProductDao()
						.findByKey(defectMapDto.getProductId()) == null)
			return false;

		if (!StringUtils.isEmpty(defectMapDto.getEntrySite())
				&& getDao(SiteDao.class).findByKey(defectMapDto.getEntrySite()) == null)
			return false;

		if (!StringUtils.isEmpty(defectMapDto.getEntryDepartment())
				&& getDao(DivisionDao.class).findByKey(defectMapDto.getEntryDepartment()) == null)
			return false;

		if (!StringUtils.isEmpty(defectMapDto.getProcessPointId())
				&& getDao(ProcessPointDao.class).findByKey(defectMapDto.getProcessPointId()) == null)
			return false;

		if (!StringUtils.isEmpty(defectMapDto.getOriginalDefectStatus())
				&& DefectStatus.getType(Integer.parseInt(defectMapDto.getOriginalDefectStatus())) == null)
			return false;

		if (!StringUtils.isEmpty(defectMapDto.getCurrentDefectStatus())
				&& DefectStatus.getType(Integer.parseInt(defectMapDto.getCurrentDefectStatus())) == null)
			return false;

		if(entryScreen!=null && entryScreen.getIsImage()==(short)1){
			if (!StringUtils.isEmpty(defectMapDto.getImageName()) && !entryScreen.getImageName().equalsIgnoreCase(defectMapDto.getImageName())
					&& getDao(QiImageDao.class).findImageByImageName(defectMapDto.getImageName()) == null)
				return false;
		}

		return true;
	}

	private boolean saveDefectDataFromExternalSystem(
			DefectMapDto defectMapDto,
			QiExternalSystemDefectMap extDefectMap, String externalSystemName ,QiEntryScreen entryScreen,Long externalSystemKey) {
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findById(defectMapDto.getProcessPointId());
		DailyDepartmentScheduleUtil schedule = new DailyDepartmentScheduleUtil(processPoint);
		BaseProduct	product = (BaseProduct)ProductTypeUtil.getTypeUtil(defectMapDto.getProductType().toUpperCase()).getProductDao().findByKey(defectMapDto.getProductId());
			
		QiDefectResult qiDefectResult = new QiDefectResult();
		DefectMapDto defectMapDtoData = new DefectMapDto();
		List<QiDefectResultDto> getLevelTwoAndLevelThree = new ArrayList<QiDefectResultDto>();
		try {
			defectMapDtoData = findDefectResultByLocalCombinationId(extDefectMap.getLocalDefectCombinationId());

			if (defectMapDtoData.getResponsibleLevelName() != null)
				getLevelTwoAndLevelThree = findAllLevel2AndLevel3ByLevel1(
						defectMapDtoData.getResponsibleSite(),
						defectMapDtoData.getResponsiblePlant(),
						defectMapDtoData.getResponsibleDept(),
						defectMapDtoData.getResponsibleLevelName());
			
			qiDefectResult = setDataToQiDefectResult(
					defectMapDto, qiDefectResult,
					defectMapDtoData, getLevelTwoAndLevelThree, schedule, product, processPoint, entryScreen);
			defectMapDto.setQiDefectResult(qiDefectResult);
			
			if (checkDefectResultExist(qiDefectResult) == true)  {
				AuditLoggerUtil.logAuditInfo(qiDefectResult, null,
						"Received defect data from external system already exist in Defect Result table",
						externalSystemName,
						defectMapDto.getAssociateId());
			}
			
			QiDefectResult insertedQiDefectResult = getDao(QiDefectResultDao.class).save(qiDefectResult);
			// @KM get defectResultId and ActualTimestamp from qiDefectResult and machineId from dto and insert into QI_DEFECT_DEVICE_TBX table
			if (StringUtils.isNotBlank(defectMapDto.getDeviceId())) {
				try {
					QiDefectDevice qiDefectFromDevice = new QiDefectDevice();

					qiDefectFromDevice.setDefectResultId(insertedQiDefectResult.getDefectResultId());
					qiDefectFromDevice.setDeviceId(defectMapDto.getDeviceId());
					qiDefectFromDevice.setActualTimestamp(insertedQiDefectResult.getActualTimestamp());
					getDao(QiDefectDeviceDao.class).save(qiDefectFromDevice);
				} catch (Exception e) {
					getLogger().info("Unable to update Defect device info ("+insertedQiDefectResult.getDefectResultId()+defectMapDto.getDeviceId()+") because of " + e.toString());
				}
			}			
			if (defectMapDto.getExternalSystemKey() != null && defectMapDto.getExternalSystemKey() > 0) {
				try {
					QiExternalSystemDefectId id = new QiExternalSystemDefectId();
					QiExternalSystemInfo qiExtSystem = getDao(QiExternalSystemInfoDao.class).findByExternalSystemName(defectMapDto.getExternalSystemName());
					id.setExternalSystemDefectKey(defectMapDto.getExternalSystemKey());
					id.setExternalSystemId(qiExtSystem.getExternalSystemId());
					id.setDefectResultId(insertedQiDefectResult.getDefectResultId());
					QiExternalSystemDefectIdMap defectKeyMap = new QiExternalSystemDefectIdMap();
					defectKeyMap.setId(id);
					defectKeyMap.setIsQicsRepairReqd(extDefectMap.getIsQicsRepairReqd());
					defectKeyMap.setIsExtSysRepairReqd(extDefectMap.getIsExtSysRepairReqd());
					getDao(QiExternalSystemDefectIdMapDao.class).save(defectKeyMap);
				} catch (Exception e) {
					getLogger().info("Unable to create defect to external key map: " + e.toString());
				}
			}			
			
			DailyDepartmentSchedule dailyDepartmentSchedule = null;
			List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
			qiDefectResultList.add(qiDefectResult);
			//check configuration to save defect in Defect Result
			if(defectMapDto.isLotControl()){
				HeadLessPropertyBean property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPoint.getProcessPointId());
				if(null != property && !property.isUseQicsService() && property.isUpdateDefectResult())  {  // update old GALC Qics only if NAQics is the primary system
					saveDefect(qiDefectResult);
				}
			}else{
				Timestamp groupTimestamp =  checkActualTimestamp(new Timestamp(System.currentTimeMillis()));
				DailyDepartmentScheduleUtil dailyDepartmentScheduleUtil = new DailyDepartmentScheduleUtil(processPoint);
				dailyDepartmentSchedule = dailyDepartmentScheduleUtil.getCurrentSchedule();
				//creates entry in station result table
				saveStationResultHeadless(product, processPoint.getProcessPointId(), DefectStatus.getType(qiDefectResult.getCurrentDefectStatus()), dailyDepartmentSchedule, qiDefectResultList);
				trackProduct(defectMapDto, groupTimestamp);
				
				// defect status is scrap, create an entry in ExceptionalOut table and mark product as scrapped
				if(dailyDepartmentSchedule !=null && qiDefectResult !=null && (DefectStatus.NON_REPAIRABLE.getId() == qiDefectResult.getCurrentDefectStatus() || product.isPreheatScrapStatus())) { 
					createAcutalProblem(insertedQiDefectResult);
					ScrapService scrapService = ServiceFactory.getService(ScrapService.class);
					scrapService.scrapProduct(createRequestDc(qiDefectResult));
				}
			}
			
		} catch (Exception e) {
			getLogger().error(e, "Exception when try to save data for ",
					this.getClass().getSimpleName());

			return false;
		}
		return true;
	}
	
	public void createAcutalProblem(QiDefectResult mainDefect) {
		QiRepairResult qiRepairResult = new QiRepairResult(mainDefect); 
		qiRepairResult = createRepairResult(qiRepairResult,null);
	}
	
	/**
	 * This method is used to create entry for Repair Result
	 * 
	 * @param repairResult
	 * @return
	 */
	public QiRepairResult createRepairResult(QiRepairResult qiRepairResult, QiDefectResult previousQiDefectResult) {
		return getDao(QiRepairResultDao.class).createRepairResult(qiRepairResult, previousQiDefectResult);
	}
	
	private DefaultDataContainer createRequestDc(QiDefectResult defectResult) {
		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.PRODUCT_ID.name(), new ArrayList<String>(Arrays.asList(defectResult.getProductId().toString())));
		requestDc.put(TagNames.REASON.name(), defectResult.getDefectTypeName());
		requestDc.put(TagNames.APPLICATION_ID.name(),defectResult.getApplicationId());
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), defectResult.getApplicationId());
		requestDc.put(TagNames.PRODUCT_TYPE.name(), defectResult.getProductType());
		requestDc.put(TagNames.ASSOCIATE_ID.name(), defectResult.getCreateUser());
		requestDc.put(TagNames.CURRENT_DATE.name(),defectResult.getProductionDate());
		requestDc.put(TagNames.PROCESS_LOCATION.name(), defectResult.getInspectionPartLocationName());
		return requestDc;
	}
	
	public DefectMapDto findDefectResultByLocalCombinationId(int LocalDefectCombinationId) {
		return getDao(QiDefectResultDao.class).findDefectResultByLocalCombinationId(LocalDefectCombinationId);
	}
	
	private List<QiDefectResultDto> findAllLevel2AndLevel3ByLevel1(String getresponsibleSite,
			String getresponsiblePlant, String getresponsibleDept, String getresponsibleLevelName) {
		return getDao(QiResponsibleLevelDao.class).findAllLevel2AndLevel3ByLevel1(getresponsibleSite,getresponsiblePlant,getresponsibleDept,getresponsibleLevelName);
	}
	
	private QiDefectResult setDataToQiDefectResult(
			DefectMapDto defectMapDtoData, //this is actually the defectMapDto
			QiDefectResult qiDefectResult, //DefectMapDto is what is being called defectMapDtoData in the caller
			DefectMapDto DefectMapDto, List<QiDefectResultDto> getLevelTwoAndLevelThree, DailyDepartmentScheduleUtil schedule, BaseProduct baseProduct,
			ProcessPoint processPoint , QiEntryScreen entryScreen) {

		qiDefectResult.setProductId(StringUtils.trimToEmpty(defectMapDtoData.getProductId().toUpperCase()));
		qiDefectResult.setApplicationId(StringUtils.trimToEmpty(defectMapDtoData.getProcessPointId()));
		qiDefectResult.setInspectionPartName(StringUtils.trimToEmpty(DefectMapDto.getInspection_part_name().toUpperCase()));
		qiDefectResult.setInspectionPartLocationName(DefectMapDto.getInspectionPartLocationName().toUpperCase());
		qiDefectResult.setInspectionPartLocation2Name(StringUtils.trimToEmpty(DefectMapDto.getInspectionPartLocation2Name().toUpperCase()));
		qiDefectResult.setInspectionPart2Name(StringUtils.trimToEmpty(DefectMapDto.getInspectionPart2Name().toUpperCase()));
		qiDefectResult.setInspectionPart2LocationName(StringUtils.trimToEmpty(DefectMapDto.getInspectionPart2LocationName().toUpperCase()));
		qiDefectResult.setInspectionPart2Location2Name(StringUtils.trimToEmpty(DefectMapDto.getInspectionPart2Location2Name().toUpperCase()));
		qiDefectResult.setInspectionPart3Name(StringUtils.trimToEmpty(DefectMapDto.getInspectionPart3Name().toUpperCase()));
		qiDefectResult.setDefectTypeName(StringUtils.trimToEmpty(DefectMapDto.getDefectTypeName().toUpperCase()));
		qiDefectResult.setDefectTypeName2(StringUtils.trimToEmpty(DefectMapDto.getDefectTypeName2().toUpperCase()));
		qiDefectResult.setIqsCategoryName(StringUtils.trimToEmpty(DefectMapDto.getIqsCategory().toUpperCase()));
		qiDefectResult.setIqsVersion(StringUtils.trimToEmpty(DefectMapDto.getIqsVersion().toUpperCase()));
		qiDefectResult.setThemeName(StringUtils.trimToEmpty(DefectMapDto.getThemeName().toUpperCase()));
		qiDefectResult.setReportable(DefectMapDto.getReportable());
		qiDefectResult.setResponsibleSite(StringUtils.trimToEmpty(DefectMapDto.getResponsibleSite()));
		qiDefectResult.setResponsiblePlant(StringUtils.trimToEmpty(DefectMapDto.getResponsiblePlant()));
		qiDefectResult.setResponsibleDept(StringUtils.trimToEmpty(DefectMapDto.getResponsibleDept()));
		qiDefectResult.setResponsibleLevel1(StringUtils.trimToEmpty(DefectMapDto.getResponsibleLevelName()));
		
		for(QiDefectResultDto dto : getLevelTwoAndLevelThree) {
			qiDefectResult.setResponsibleLevel2(StringUtils.trimToEmpty(dto.getLevelTwo() ));
			qiDefectResult.setResponsibleLevel3(StringUtils.trimToEmpty(dto.getLevelThree()));
		}

		qiDefectResult.setEntrySiteName(StringUtils.trimToEmpty(DefectMapDto.getEntrySiteName()));
		qiDefectResult.setEntryPlantName(StringUtils.trimToEmpty(DefectMapDto.getEntryPlantName()));
		short prodLineNo = DEFAULT_PROD_LINE_NO;
		try {
			if(processPoint != null)  {
				prodLineNo = getProductionLineNo(processPoint.getProcessPointId());
			}
		} catch (Exception e) {
			prodLineNo = 1;
		}
		qiDefectResult.setEntryProdLineNo(prodLineNo);
		qiDefectResult.setEntryDept(StringUtils.trimToEmpty(defectMapDtoData.getEntryDepartment()));
		qiDefectResult.setProductType(StringUtils.trimToEmpty(defectMapDtoData.getProductType().toUpperCase()));
		qiDefectResult.setWriteUpDept(StringUtils.trimToEmpty(defectMapDtoData.getWriteupDepartment().toUpperCase()));
		if(entryScreen !=null && entryScreen.getIsImage()==(short)1){
			qiDefectResult.setImageName(StringUtils.trimToEmpty(entryScreen.getImageName()));
			qiDefectResult.setPointX(Integer.parseInt(StringUtils.trimToEmpty(defectMapDtoData.getxAxis())));
			qiDefectResult.setPointY(Integer.parseInt(StringUtils.trimToEmpty(defectMapDtoData.getyAxis())));
		}
		if(!StringUtils.isEmpty(defectMapDtoData.getOriginalDefectStatus()) && StringUtils.isNumeric(defectMapDtoData.getOriginalDefectStatus())){
			if(defectMapDtoData.isLotControl())
				qiDefectResult.setOriginalDefectStatus((short)DefectStatus.NOT_REPAIRED.getId());
			else
				qiDefectResult.setOriginalDefectStatus(Short.parseShort(defectMapDtoData.getOriginalDefectStatus()));
		}
			
		if(!StringUtils.isEmpty(defectMapDtoData.getCurrentDefectStatus()) && StringUtils.isNumeric(defectMapDtoData.getCurrentDefectStatus())){
			if(defectMapDtoData.isLotControl())
				qiDefectResult.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
			else
				qiDefectResult.setCurrentDefectStatus(Short.parseShort(defectMapDtoData.getCurrentDefectStatus()));
		}
		qiDefectResult.setDefectCategoryName(StringUtils.trimToEmpty(DefectMapDto.getDefectCategoryName().toUpperCase()));
		qiDefectResult.setRepairArea(StringUtils.trimToEmpty(DefectMapDto.getRepairAreaName()));
		qiDefectResult.setRepairMethodNamePlan(StringUtils.trimToEmpty(DefectMapDto.getRepairMethod()));
		qiDefectResult.setLocalTheme(StringUtils.trimToEmpty(DefectMapDto.getLocalTheme()));
		qiDefectResult.setCreateUser(StringUtils.trimToEmpty(defectMapDtoData.getAssociateId().toUpperCase()));
		qiDefectResult.setIqsQuestionNo(DefectMapDto.getIqsQuestionNo());
		qiDefectResult.setIqsQuestion(DefectMapDto.getIqsQuestion());
		qiDefectResult.setEntryScreen(DefectMapDto.getEntryScreen());
		Timestamp groupTimestamp =  checkActualTimestamp(new Timestamp(System.currentTimeMillis()));
		qiDefectResult.setGroupTimestamp(groupTimestamp);
		qiDefectResult.setActualTimestamp(new Date());
		if(schedule!=null && schedule.getCurrentSchedule()!=null && schedule.getCurrentSchedule().getId()!=null){
			qiDefectResult.setProductionDate(schedule.getCurrentSchedule().getId().getProductionDate());
			qiDefectResult.setShift(schedule.getCurrentSchedule().getId().getShift());
		}
		if(baseProduct!=null){
			qiDefectResult.setProductSpecCode(baseProduct.getProductSpecCode());
			qiDefectResult.setProductionLot(baseProduct.getProductionLot());
			
			if(baseProduct.getProductType().equals(ProductType.FRAME) || 
					baseProduct.getProductType().equals(ProductType.ENGINE)) {
				/**
				 * If Product Type is FRAME or ENGINE
				 */
				
				Product product = (Product)baseProduct;
				qiDefectResult.setKdLotNumber(product.getKdLotNumber());
				qiDefectResult.setAfOnSequenceNumber(product.getAfOnSequenceNumber()==null?0:product.getAfOnSequenceNumber());
			}
		}
		List<String> bomMainPartNoList =  getDao(QiBomQicsPartMappingDao.class).findAllMainPartNoByInspectionPartName(DefectMapDto.getInspectionPartName());
		String bomMainPartNo = !bomMainPartNoList.isEmpty() ? bomMainPartNoList.get(0) : StringUtils.EMPTY;
		qiDefectResult.setBomMainPartNo(bomMainPartNo);
		GpcsDivision gpcsDivision= getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
		TeamRotation teamRotationObj;
		if(gpcsDivision!=null && schedule!=null && schedule.getCurrentSchedule() !=null && schedule.getCurrentSchedule().getId()!=null ){
			teamRotationObj = getDao(TeamRotationDao.class).findTeamDetails(gpcsDivision,schedule.getCurrentSchedule().getId().getShift(),schedule.getCurrentSchedule().getId().getProductionDate());
		if(null!=teamRotationObj)
			qiDefectResult.setTeam(teamRotationObj.getId().getTeam());
		}else{
			qiDefectResult.setTeam(StringUtils.EMPTY);
		}
		qiDefectResult.setProcessName(DefectMapDto.getProcessName());
		qiDefectResult.setProcessNo(DefectMapDto.getProcessNumber());
		qiDefectResult.setUnitDesc(DefectMapDto.getUnitDesc());
		qiDefectResult.setUnitNo(DefectMapDto.getUnitNumber());
		qiDefectResult.setRepairTimePlan(DefectMapDto.getRepairMethodTime());
		qiDefectResult.setEntryModel(DefectMapDto.getEntryModel());
		qiDefectResult.setEngineFiringFlag(DefectMapDto.getEngineFiringFlag());
		qiDefectResult.setGdpDefect((isGdpProcessPoint(StringUtils.trimToEmpty(defectMapDtoData.getProcessPointId())) 
									&& qiDefectResult.getCurrentDefectStatus()==(short)DefectStatus.NOT_FIXED.getId()) ? (short) 1 : (short) 0); 
		qiDefectResult.setTrpuDefect((isTrpuProcessPoint(StringUtils.trimToEmpty(defectMapDtoData.getProcessPointId()))) ? (short) 1 : (short) 0); 


		if( isGdpProcessPoint(StringUtils.trimToEmpty(defectMapDtoData.getProcessPointId()))
			&& (qiDefectResult.getTrpuDefect() == (short) 1) 
			&& isGlobalGdpEnabled() 
			&& isGlobalGdpWriteUpDept(qiDefectResult.getWriteUpDept())) {
			qiDefectResult.setGdpDefect((short) 1);
		}
		
		qiDefectResult.setDefectTransactionGroupId(getDefectTransactionGroupId(qiDefectResult));

		return qiDefectResult;
	}
	
	public Long getDefectTransactionGroupId(QiDefectResult qiDefectResult) {
		return getDao(QiDefectResultDao.class).getNextDefectTransactionGrounpId(qiDefectResult);
		
	}
	
	/**
	 * This method is used for Product Tracking
	 * @param productId 
	 * @param trackerTimeStamp 
	 */
	public void trackProduct(DefectMapDto defectMapDto, Timestamp trackerTimeStamp) {
		String productId=defectMapDto.getProductId().toUpperCase();
		String processPointId=defectMapDto.getProcessPointId();
		String productType=defectMapDto.getProductType().toUpperCase();
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(productId, processPointId,productType);
				
		if (productHistory != null) {
			productHistory.setAssociateNo(defectMapDto.getAssociateId());
			productHistory.setApproverNo("");
			productHistory.setProcessPointId(processPointId);
			productHistory.setProductId(productId);
			productHistory.setActualTimestamp(trackerTimeStamp);
		}

		try {
			if(!StringUtils.isEmpty(productType))	{
				ServiceFactory.getService(TrackingService.class).track(ProductType.getType(productType), productHistory);
				getLogger().info("Tracking Successfully completed for product " + productId);
			} else {
				getLogger().error("Product Type is unknown for product " + productId);
			}
		} catch (Exception ex) {
			throw new ServiceException("Tracking was not Successful for product " + productId, ex);
		}
	}
	
	private Timestamp checkActualTimestamp(Timestamp actualTimestamp) {
		Timestamp dbTimestamp = getProcessPointTimeStamp();
		if (actualTimestamp == null || actualTimestamp.after(dbTimestamp)) {
			return dbTimestamp; 
		}
		return actualTimestamp;
	}
	
	public Timestamp getProcessPointTimeStamp() {
		return getDao(ProcessPointDao.class).getDatabaseTimeStamp();
	}
	
	public boolean checkDefectResultExist(QiDefectResult defectResult) {
		return getDao(QiDefectResultDao.class).checkDefectResultExist(
				defectResult);
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());

	}

	/**
	 * This method is used for processing of Lot Control Defects
	 * @param processPointId 
	 * @param productType 
	 * @param buildResults
	 * @return boolean
	 */
	@Override
	public boolean saveDefectData(String processPointId,ProductType productType,List<? extends ProductBuildResult> buildResults) {
		try{
			HeadLessPropertyBean property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);	
			DefectMapDto defectMapDto= new DefectMapDto();
			defectMapDto.setProductId(buildResults.get(0).getProductId());
			defectMapDto.setProductType(productType.getProductName());
			defectMapDto.setProcessPointId(processPointId);
			defectMapDto.setAssociateId(buildResults.get(0).getAssociateNo());
			ProcessPoint processPoint = getDao(ProcessPointDao.class).findById(processPointId);
			List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
			List<DefectResult> defectdataToBeRepaired = new ArrayList<DefectResult>();
			for (ProductBuildResult buildResult : buildResults){
				if(buildResult.getInstalledPartStatus() != InstalledPartStatus.OK){
					DefectMapDto defectMapDtoData = new DefectMapDto();
					if(buildResult.getMeasurements()==null || buildResult.getMeasurements().isEmpty()) {
						defectMapDtoData.setExternalDefectCode(QicsDefectInfoManager.getInstance().getDefectName(processPointId, buildResult.getPartName(), 
								buildResult.getErrorCode(), InstalledPartStatus.NG.toString()));
					}
					else
					{
						defectMapDtoData.setExternalDefectCode(buildResult.getMeasurements().get(0).getErrorCode());
					}
					defectMapDtoData.setProcessPointId(processPointId);
					defectMapDtoData.setProductId(buildResult.getProductId());
					defectMapDtoData.setxAxis(String.valueOf(buildResult.getPointX()));
					defectMapDtoData.setyAxis(String.valueOf(buildResult.getPointY()));
					defectMapDtoData.setExternalPartCode(buildResult.getPartName());
					defectMapDtoData.setAssociateId(buildResult.getAssociateNo());
					defectMapDtoData.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
					defectMapDtoData.setSiteName(processPoint.getSiteName());
					defectMapDtoData.setEntryDepartment(processPoint.getDivisionId());
					defectMapDtoData.setWriteupDepartment(processPoint.getDivisionName());
					defectMapDtoData.setProductType(productType.getProductName());
					defectMapDtoData.setLotControl(true);
					if(buildResult.getInstalledPartStatusId() != null){
						defectMapDtoData.setCurrentDefectStatus(buildResult.getInstalledPartStatusId().toString());
						defectMapDtoData.setOriginalDefectStatus(buildResult.getInstalledPartStatusId().toString());
					}
					if(property.isInlineRepair() &&  !property.isInlineRepairCreateDefect()){
						if(buildResult.getInstalledPartStatus() == InstalledPartStatus.OK){
							List<QiDefectResult> defects = getDao(QiDefectResultDao.class).findAllByExternalSystemDataAndProductId(buildResult.getPartName(), defectMapDtoData.getExternalSystemName(), buildResult.getProductId());
							Map<String, QiDefectResult> partDefectComb = new HashMap<String, QiDefectResult>();
							for(QiDefectResult qiDefectresult : defects){
								qiDefectresult.setCurrentDefectStatus((short)DefectStatus.REPAIRED.getId());
								qiDefectresult.setUpdateUser(buildResult.getAssociateNo());
								partDefectComb.put(qiDefectresult.getInspectionPartName()+qiDefectresult.getInspectionPartLocationName()+qiDefectresult.getInspectionPart2Name()
										+qiDefectresult.getInspectionPart2LocationName() +qiDefectresult.getInspectionPart2Location2Name() +qiDefectresult.getInspectionPart3Name()
										+qiDefectresult.getDefectTypeName() +qiDefectresult.getDefectTypeName2(), qiDefectresult );
							}
							
							for(Map.Entry<String, QiDefectResult> entry : partDefectComb.entrySet() ){
								if(property.isUpdateDefectResult()){
									List<DefectResult> defectResultList = getDao(DefectResultDao.class).findAllByPartDefectCombAndProductId(entry.getValue(), buildResult.getProductId());
									for(DefectResult result : defectResultList){
										result.setDefectStatus(DefectStatus.FIXED);
										if (property.isOutstandingFlagChangable()) {
											result.setOutstandingFlag(result.isOutstandingStatus());
										}
										result.setRepairTimestamp(new Timestamp(System.currentTimeMillis()));
										if(!buildResult.getAssociateNo().equals(StringUtils.EMPTY))
											result.setRepairAssociateNo(buildResult.getAssociateNo());
										defectdataToBeRepaired.add(result);
										getLogger().info("Headless NAQ Service repaired product:", result.getId().getProductId(), " partName:", 
												buildResult.getPartName(), "DefectResultId:" + result.getId().getDefectResultId()," repaired.");
									}
								}
								getDao(QiDefectResultDao.class).updateAll(defects);
								getDao(DefectResultDao.class).updateAll(defectdataToBeRepaired);
							}
						}
						else
						{
							saveDefectData(defectMapDtoData);
							qiDefectResultList.add(defectMapDtoData.getQiDefectResult());
						}
					
					} else {
						saveDefectData(defectMapDtoData);
						qiDefectResultList.add(defectMapDtoData.getQiDefectResult());
					}
				}
			}
			if(qiDefectResultList != null && qiDefectResultList.size()>0){
				Product	product = (Product)ProductTypeUtil.getTypeUtil(defectMapDto.getProductType().trim().toUpperCase()).getProductDao().findByKey(defectMapDto.getProductId());
				long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
				GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
				DailyDepartmentSchedule dailyDepartmentSchedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
					gpcsDivision.getGpcsLineNo() , gpcsDivision.getGpcsProcessLocation(), 
						gpcsDivision.getGpcsPlantCode(), new Timestamp(currentTimeInMillis));
				saveStationResultHeadless(product, processPoint.getProcessPointId(),isExceptionalOut(qiDefectResultList), dailyDepartmentSchedule ,qiDefectResultList);
				Timestamp groupTimestamp =  checkActualTimestamp(new Timestamp(System.currentTimeMillis()));
				trackProduct(defectMapDto,groupTimestamp);
			}
			return true;
		}catch (Exception e) {
			getLogger().error(e, "Exception when collect data for Lot Control",
					this.getClass().getSimpleName());
			return false;
		}
	}
	
	/**
	 * This method creates defects in defect result table (GAL125TBX)
	 * @param qiDefectResult
	 * @param defectMapDto
	 * @return DefectResult
	 */
	private void  saveDefect(QiDefectResult qiDefectResult) {
		DefectResult defectResult =setDefectData(qiDefectResult);
		if(defectResult != null)
			 getDao(DefectResultDao.class).saveDefectResultForHeadlessService(defectResult);
	}
	
	/**
	 * This method sets defect result data
	 * @param qiDefectResult
	 * @param defectMapDto
	 * @return DefectResult
	 */
	public DefectResult setDefectData(QiDefectResult qiDefectResult) {

		String oldApplicationId = null;
		if (!StringUtils.isBlank(qiDefectResult.getApplicationId())) {
			oldApplicationId = getDao(QiExternalSystemDefectMapDao.class).findOldAppIdByAppId(qiDefectResult.getApplicationId());
			if (StringUtils.isBlank(oldApplicationId)) {
				oldApplicationId = qiDefectResult.getApplicationId();
			}
		}
		
		DefectResult defectResult = new DefectResult();
		DefectResultId defectResultId =new DefectResultId();
		defectResultId.setProductId(qiDefectResult.getProductId());
		defectResultId.setApplicationId(oldApplicationId);
		
		QiMappingCombination combination = getDao(QiMappingCombinationDao.class).findbyPartDefectComb(qiDefectResult);
		if (combination != null && !StringUtils.isBlank(combination.getOldInspectionPartName())) {
			defectResultId.setInspectionPartName(combination.getOldInspectionPartName());
			defectResultId.setInspectionPartLocationName(combination.getOldInspectionPartLocationName());
			defectResultId.setTwoPartPairLocation(combination.getOldInspectionPart2LocationName());
			defectResultId.setTwoPartPairPart(combination.getOldInspectionPart2Name());
			defectResultId.setSecondaryPartName(combination.getOldInspectionPart3Name());
			defectResultId.setDefectTypeName(combination.getOldDefectTypeName());
		} else {
			//set two part pair part/location to blank per HMA's requirement
			defectResultId.setInspectionPartName(qiDefectResult.getInspectionPartName());
			defectResultId.setInspectionPartLocationName(qiDefectResult.getInspectionPartLocationName());
			defectResultId.setSecondaryPartName(qiDefectResult.getInspectionPart2Name());
			defectResultId.setDefectTypeName(qiDefectResult.getDefectTypeName());
			defectResultId.setTwoPartPairLocation("");
			defectResultId.setTwoPartPairPart("");
		}
		
		defectResult = setOldDefectData(qiDefectResult, defectResult);
		defectResult.setAssociateNo(qiDefectResult.getCreateUser());
		defectResult.setDate(qiDefectResult.getProductionDate() !=null ? new java.sql.Date(qiDefectResult.getProductionDate().getTime()) : null);
		defectResult.setDefectStatus(DefectStatus.getOldDefectStatus(qiDefectResult.getCurrentDefectStatus()));
		defectResult.setId(defectResultId);
		defectResult.setPointX(qiDefectResult.getPointX());
		defectResult.setPointY(qiDefectResult.getPointY());
		defectResult.setNewDefect(true);
		defectResult.setShift(qiDefectResult.getShift());
		defectResult.setRepairTimePlan(qiDefectResult.getRepairTimePlan());
		defectResult.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		defectResult.setOutstandingFlag(DefectStatus.getOldOutstandingFlag(qiDefectResult.getOriginalDefectStatus()));
		defectResult.setEngineFiring(qiDefectResult.getEngineFiringFlag() == (short) 1 ? true : false);
		defectResult.setGdpDefect((int)qiDefectResult.getGdpDefect());
		defectResult.setTwoPartDefectFlag(StringUtils.isBlank(defectResultId.getTwoPartPairPart())? false : true);
		defectResult.setNaqDefectResultId(qiDefectResult.getDefectResultId());
		return defectResult;
	}

	private DefectResult setOldDefectData(QiDefectResult qiDefectResult,DefectResult defectResult) {
		//setting image name
		if (!StringUtils.isBlank(qiDefectResult.getImageName())) {
			String oldImageName = getDao(QiExternalSystemDefectMapDao.class).findOldImgByImg(qiDefectResult.getImageName());
			if (StringUtils.isBlank(oldImageName)) {
				oldImageName = qiDefectResult.getImageName();
			}
			defectResult.setImageName(oldImageName);
		}
		//Setting repair method
		if (!StringUtils.isBlank(qiDefectResult.getRepairMethodNamePlan())) {
			String oldRepairMethodNamePlan = getDao(QiExternalSystemDefectMapDao.class).findOldRepairMethodByRepairMethod(qiDefectResult.getRepairMethodNamePlan());
			if (StringUtils.isBlank(oldRepairMethodNamePlan)) {
				oldRepairMethodNamePlan = qiDefectResult.getRepairMethodNamePlan();
			}
			defectResult.setRepairMethodNamePlan(oldRepairMethodNamePlan);
		}
		//Setting regression code
		if (!StringUtils.isBlank(qiDefectResult.getThemeName())) {
			String oldThemeName = getDao(QiExternalSystemDefectMapDao.class).findOldThemeByTheme(qiDefectResult.getThemeName());
			if (StringUtils.isBlank(oldThemeName)) {
				oldThemeName = qiDefectResult.getThemeName();
			}
			defectResult.setRegressionCode(oldThemeName);
		}
		//Setting write up Dept
		String oldWriteUpDepartment = getDao(QiExternalSystemDefectMapDao.class).findOldWriteUpByWriteUpAndResponsibility(qiDefectResult);
		if (StringUtils.isBlank(oldWriteUpDepartment)) {
			oldWriteUpDepartment = qiDefectResult.getWriteUpDept();
		}
		defectResult.setWriteUpDepartment(oldWriteUpDepartment);
		
		//Setting iqs
		DefectMapDto iqs = getDao(QiExternalSystemDefectMapDao.class).findOldIqsByIqs(qiDefectResult);
		if (iqs != null && !StringUtils.isBlank(iqs.getOldIqsCategoryName())) {
			defectResult.setIqsCategoryName(iqs.getOldIqsCategoryName());
			defectResult.setIqsItemName(iqs.getOldIqsItemName());
		} else {
			defectResult.setIqsCategoryName(qiDefectResult.getIqsCategoryName());
			String iqsQuestion = qiDefectResult.getIqsQuestion();
			if (!StringUtils.isBlank(iqsQuestion) && iqsQuestion.length() > 32) {
				iqsQuestion = iqsQuestion.substring(0, 32);
			}
			defectResult.setIqsItemName(iqsQuestion);
		}
		
		//setting entry dept
		DefectMapDto entryDept = getDao(QiExternalSystemDefectMapDao.class).findOldEntryDeptByEntryDept(qiDefectResult);
		if (entryDept!= null && !StringUtils.isBlank(entryDept.getEntryDepartment())) {
			defectResult.setEntryDept(entryDept.getEntryDepartment());
		} else {
			//populate DIVISION_NAME instead of DIVISION_ID into GAL125TBX
			Division entryDivision = getDao(DivisionDao.class).findByDivisionId(qiDefectResult.getEntryDept());
			if (entryDivision != null) {
				defectResult.setEntryDept(entryDivision.getDivisionName());
			}
		}
		
		//setting responsibility
		DefectMapDto responsibility = getDao(QiExternalSystemDefectMapDao.class).findOldResponsiblityByResponsibility(qiDefectResult);
		if(responsibility != null && !StringUtils.isBlank(responsibility.getResponsibleDept())) {
			defectResult.setResponsibleDept(responsibility.getResponsibleDept());
			defectResult.setResponsibleLine(responsibility.getLevelTwo());
			defectResult.setResponsibleZone(responsibility.getLevel());
		} else {
			defectResult.setResponsibleDept(qiDefectResult.getResponsibleDept());
			defectResult.setResponsibleLine(qiDefectResult.getResponsibleLevel2());
			defectResult.setResponsibleZone(qiDefectResult.getResponsibleLevel1());
		}
		return defectResult;
	}

	/**
	 * This method checks if product is Scrap/Pre-Heat Scrap
	 * @return DefectStatus
	 */
	protected DefectStatus isExceptionalOut(List<QiDefectResult>  qiDefectResultList) {
		for(QiDefectResult qiDefectResult : qiDefectResultList){
			if(qiDefectResult.getCurrentDefectStatus() == DefectStatus.SCRAP.getId() ||
					qiDefectResult.getCurrentDefectStatus() == DefectStatus.PREHEAT_SCRAP.getId()){
				return DefectStatus.getType(qiDefectResult.getCurrentDefectStatus());
			}
		}
		return null;
	}
	

	/**
	 * This method insert data in Station Result 
	 * @param product
	 * @param processPointId
	 * @param DefectStatus
	 * @param schedule
	 */
	public void saveStationResultHeadless(BaseProduct product, String processPointId, DefectStatus defectStatus, DailyDepartmentSchedule schedule ,List<QiDefectResult> qidefDefectResultList) {
		if (schedule != null) {
			StationResult stationResult = getStationResult(product, processPointId, schedule);
			DailyDepartmentSchedule shiftFirstPeriod = getDao(DailyDepartmentScheduleDao.class).findShiftFirstPeriod(schedule);
			if (shiftFirstPeriod != null && shiftFirstPeriod.getStartTimestamp() != null) {
				ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(product.getProductType());
				boolean productProcessed = historyDao.isProductProcessedOnOrAfter(product.getProductId(), processPointId, shiftFirstPeriod.getStartTimestamp());
				if (!productProcessed) {
					// only update station result when the product has not been processed at this station after the beginning of this shift
					stationResult.updateResult(deriveDefectStatus(defectStatus , qidefDefectResultList));
					stationResult.setLastProductId(product.getProductId());
					Logger.getLogger().info("product:", product.getProductId(), " defect status:" + product.getDefectStatus());
					getDao(StationResultDao.class).save(stationResult);
				}
			}
		}
	}
	

	/**
	 * This method sets Station Result data
	 * @param product
	 * @param processPointId
	 * @param schedule
	 * @return StationResult
	 */
	 private StationResult getStationResult(BaseProduct product,String processPointId,DailyDepartmentSchedule schedule) {
    	StationResultId stationResultId = new StationResultId();
    	stationResultId.setApplicationId(processPointId);
    	stationResultId.setProductionDate(schedule.getId().getProductionDate());
    	stationResultId.setShift(schedule.getId().getShift());
		StationResult stationResult = getDao(StationResultDao.class).findByKey(stationResultId);
		if(stationResult != null) return stationResult;
		stationResult = new StationResult();
		stationResult.setId(stationResultId);
		stationResult.setFirstProductId(product.getProductId());
		return stationResult;
	  }
	 
	 /**
	 * This method set Defect Status 
	 * @param DefectStatus
	 * @param qiDefectResults
	 * @return DefectStatus
	 */
	 private DefectStatus deriveDefectStatus(DefectStatus defectStatus , List<QiDefectResult> qiDefectResultList) {
		if(defectStatus != null &&(defectStatus.isScrap() || defectStatus.isPreheatScrap() || defectStatus.isDirectPass()))
			return defectStatus;
		if(qiDefectResultList == null || qiDefectResultList.isEmpty()) 
			defectStatus = DefectStatus.DIRECT_PASS;
		else {
			defectStatus = hasOutstandingDefect(qiDefectResultList) ? 
				DefectStatus.OUTSTANDING:DefectStatus.REPAIRED;
		}
		return defectStatus;
	 }
	 
	 
	 /**
	 * This method set Defect Status 
	 * @param qiDefectResults
	 * @return boolean
	 */
	 private boolean hasOutstandingDefect(List<QiDefectResult>  qiDefectResultList) {
		for(QiDefectResult defectResult : qiDefectResultList) {
			if(DefectStatus.isOutstandingStatus(Integer.parseInt(String.valueOf(defectResult.getCurrentDefectStatus()))))
				return true;
		}
		return false;
	}
	 
	 public void updateLegacyDefectResultResponsibility(QiDefectResult qiDefectResult) {
		 DefectMapDto responsibility = getDao(QiExternalSystemDefectMapDao.class).findOldResponsiblityByResponsibility(qiDefectResult);
		 String respDept = "";
		 String respLine = "";
		 String respZone = "";
			
		 if(responsibility != null && !StringUtils.isBlank(responsibility.getResponsibleDept())) {
			 respDept = responsibility.getResponsibleDept();
			 respLine = responsibility.getLevelTwo();
			 respZone = responsibility.getLevel();
		} else {
			respDept = qiDefectResult.getResponsibleDept();
			respLine = qiDefectResult.getResponsibleLevel2();
			respZone = qiDefectResult.getResponsibleLevel1();
		}
		 
		 getDao(DefectResultDao.class).updateResponsibilityByQiDefectResultId(qiDefectResult.getDefectResultId(),
				 respDept, respLine, respZone);
	 }
	 
	public short getProductionLineNo(String processPointId)  {		
		if(StringUtils.isBlank(processPointId))  return DEFAULT_PROD_LINE_NO;
		short prodLineNo = DEFAULT_PROD_LINE_NO;
		try {
			ProcessPoint pp = getDao(ProcessPointDao.class).findById(processPointId);
			if(pp != null)  {
				GpcsDivision gpcs = getDao(GpcsDivisionDao.class).findByKey(pp.getDivisionId());
				if(gpcs != null)  {
					prodLineNo = Short.parseShort(gpcs.getGpcsLineNo());
				}
			}
		} catch (Exception e) {
			prodLineNo =1;	
			getLogger().error(e, "Exception when getting Production Line No",
					this.getClass().getSimpleName());
		}
		return prodLineNo;
	}

	private boolean isGdpProcessPoint(String processPointId) {
		return getBooleanValue(processPointId, QiEntryStationConfigurationSettings.GDP_PROCESS_POINT);
	}

	private boolean isTrpuProcessPoint(String processPointId) {
		return getBooleanValue(processPointId, QiEntryStationConfigurationSettings.TRPU_PROCESS_POINT);
	}

	private boolean getBooleanValue(String processPointId, QiEntryStationConfigurationSettings setting) {
		QiStationConfiguration config = ServiceFactory.getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(processPointId, setting.getSettingsName());
		return QiConstant.YES.equalsIgnoreCase(config == null ? setting.getDefaultPropertyValue() : config.getPropertyValue());
	}
	
	private boolean isGlobalGdpEnabled() {
		return PropertyService.getPropertyBean(QiPropertyBean.class).isGlobalGdp();
	}

	private boolean isGlobalGdpWriteUpDept(String writeUpDepartment) {
		String[] globalGdpDepts = PropertyService.getPropertyBean(QiPropertyBean.class).getGlobalGdpWriteUpDepts();
		for(String dept : globalGdpDepts) {
			if(dept.equalsIgnoreCase(writeUpDepartment)) {
				return true;
			}
		}
		return false;
	}
	
}
