package com.honda.galc.client.qi.defectentry;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.CacheEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.dao.qi.QiDefectResultImageDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dao.qi.QiImageSectionPointDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dao.qi.QiStationResponsibilityDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.dao.qics.DefectRepairResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.dto.qi.QiMostFrequentDefectsDto;
import com.honda.galc.dto.qi.QiRecentDefectDto;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.KickoutService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

/**
 * <h3>DefectEntryModel description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectEntryModel</code> is model for Defect Entry Screen
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
 * <TD>L&T Infotech</TD>
 * <TD>15/11/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */

public class DefectEntryModel extends AbstractQiDefectProcessModel  {

	Map<String, List<QiDefectResult>> imageDefectResult = new HashMap<String, List<QiDefectResult>>();

	public DefectEntryModel() {
		super();
		EventBusUtil.register(this);
		ProductPropertyBean props = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId());
		getDefectEntryCacheUtil().initializeCache(props.getCacheSizeMem(), props.getCacheSizeDisk(), props.getCacheTimeToLive(), props.getCacheTimeToIdle());

		try {
			// schedule the daily cache refresh, formatted as HH:mm in the station settings
			final QiStationConfiguration dailyRefreshCacheTime = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(), QiEntryStationConfigurationSettings.DAILY_REFRESH_CACHE_TIME.getSettingsName());
			if (dailyRefreshCacheTime != null && !dailyRefreshCacheTime.equals(QiConstant.NONE)) {
				Logger.getLogger().info("Scheduling daily PDC cache refresh time for " + dailyRefreshCacheTime.getPropertyValue());
				String[] dailyRefreshCacheTimeArray = dailyRefreshCacheTime.getPropertyValue().split(Delimiter.COLON);
				ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
				ZonedDateTime nextRun = now.withHour(Integer.valueOf(dailyRefreshCacheTimeArray[0])).withMinute(Integer.valueOf(dailyRefreshCacheTimeArray[1])).withSecond(0);
				if(now.compareTo(nextRun) > 0)
					nextRun = nextRun.plusDays(1); // schedule the first run for the next day if the current time is after the daily refresh

				Duration duration = Duration.between(now, nextRun);
				long initalDelay = duration.getSeconds();

				ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
				scheduler.scheduleAtFixedRate(
						new Runnable() {
							@Override
							public void run() {
								Logger.getLogger().info("Running scheduled daily PDC cache refresh for " + dailyRefreshCacheTime.getPropertyValue());
								refreshPdcCache(Delimiter.ASTERISK);
							}
						},
						initalDelay,
						TimeUnit.DAYS.toSeconds(1),
						TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Unable to schedule daily cache refresh");
		}
	}

	@Subscribe
	public void processEvent(CacheEvent event) {
		try {
			String applicationId = event.getApplicationId();
			if (applicationId == null || getApplicationId().equals(applicationId)) {
				switch(event.getEventType()) {
				case REFRESH_PDC_CACHE:
					refreshPdcCache(event.getCacheKey());
					break;
				default:
				}
			}
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private void refreshPdcCache(String entryModel) {
		try {
			if (Delimiter.ASTERISK.equals(entryModel)) {
				Logger.getLogger().info("Refreshing all PDC caches");
				getDefectEntryCacheUtil().getPartDefectCombOriginalCache().clearCache();
			} else {
				Logger.getLogger().info("Refreshing PDC caches for entry model " + entryModel);
				List<String> entryModelMtcModels = getDao(QiEntryModelGroupingDao.class).findAllMtcModelByEntryModel(entryModel, getApplicationContext().getProductTypeData().getProductTypeName());
				for(Object cacheKey : getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getKeys()) {
					List<String> cacheKeySegments = Arrays.asList(cacheKey.toString().split(Delimiter.BACKSLASH + Delimiter.DOT));
					if (!Collections.disjoint(cacheKeySegments, entryModelMtcModels)) // remove the key if one of its segments matches one of the MTC models for the entry model
						getDefectEntryCacheUtil().getPartDefectCombOriginalCache().remove(cacheKey);
				}
			}
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	/**
	 * This method is used to find all Part1 by given paramters
	 * @return
	 */
	public List<String> findAllPart1ByEntryScreen(String entryScreen, String textEntryMenu, String part1Filter) {
		return findAllPart1ByEntryScreen(entryScreen, textEntryMenu, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, part1Filter);
	}

	/**
	 * This method is used to find all Part1 by filter
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part2
	 * @param defect
	 * @return
	 */
	public List<String> findAllPart1ByEntryScreen(String entryScreen, String textEntryMenu, String loc, String part2, String defect, String defect2, String filter, String part1Filter) {
		List<String> part1List = new ArrayList<String>();
		if (getProductModel()!=null) {
			part1List = getDefectEntryCacheUtil().getMatchingPart1List(entryScreen, textEntryMenu, loc, part2, defect, defect2, part1Filter);
		}
		if(!StringUtils.isEmpty(filter)) {
			return filterStringListByFirstChar(part1List, filter);
		}
		return part1List;
	}

	/**
	 * This method is used to find all Part1 by filter
	 * @param entryScreen
	 * @param partFilter
	 * @return
	 */
	public List<String> findAllPart1ByEntryScreenAndPartFilter(String entryScreen, String entryMenu, String partFilter) {
		List<String> part1List = new ArrayList<String>();
		if (getProductModel()!=null) {
			part1List = getDefectEntryCacheUtil().getMatchingPart1ListByPartFilter(entryScreen, entryMenu, partFilter);
		}
		return part1List;
	}

	/**
	 * This method is used to find All Part1 or Part2 based on Current Working Process Point, Product Kind property configured, current Mtc Model and Given Entry Department
	 * @param entryDept
	 * @return 
	 */
	public List<String> findAllPart1AndPart2(String entryDept){
		String part1Part2Key = generateCacheKey(getCurrentWorkingProcessPointId(), getProductKind(), getMtcModel(), entryDept);
		List<String> part1Part2List = new ArrayList<String>();
		if (getDefectEntryCacheUtil().getPart1Part2Cache().containsKey(part1Part2Key)) {
			part1Part2List = getDefectEntryCacheUtil().getPart1Part2Cache().getList(part1Part2Key, String.class);
		} else {
			part1Part2List = getDao(QiPartLocationCombinationDao.class).findAllPart1AndPart2(getCurrentWorkingProcessPointId(), getProductKind(), getMtcModel(), entryDept);
			getDefectEntryCacheUtil().getPart1Part2Cache().put(part1Part2Key, part1Part2List);
		}
		return	part1Part2List;
	}

	public List<String> findAllByAllPartLocation(String entryDept){
		return  getDao(QiPartLocationCombinationDao.class).findAllByAllPartLocation(getCurrentWorkingProcessPointId(), getProductKind(), getMtcModel(), entryDept);
	}



	/**
	 * This method is used to find all Loc by filter
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part2
	 * @param defect
	 * @return
	 */
	public List<String> findAllLocByEntryScreen(String entryScreen, String textEntryMenu, String part1, String part2, String defect, String defect2, String filter, String part1Filter) {
		List<String> locList = new ArrayList<String>();
		if (getProductModel()!=null) {
			locList = getDefectEntryCacheUtil().getMatchingLocList(entryScreen, textEntryMenu, part1, part2, defect, defect2, part1Filter);
		}
		if(!StringUtils.isEmpty(filter)) {
			return filterStringListByFirstChar(locList, filter);
		}
		return locList;
	}

	/**
	 * This method is used to find all Part2 by given parameters
	 * @return
	 */
	public List<String> findAllPart2ByEntryScreen(String entryScreen, String textEntryMenu, String partFilter) {
		return findAllPart2ByEntryScreen(entryScreen, textEntryMenu, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, partFilter);
	}


	/**
	 * This method is used to find all Part2 by filter
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part1
	 * @param defect
	 * @return
	 */
	public List<String> findAllPart2ByEntryScreen(String entryScreen, String textEntryMenu, String part1, String loc, String defect, String defect2, String filter, String part2Name) {
		List<String> part2List = new ArrayList<String>();
		if(getProductModel()!=null) {
			part2List = getDefectEntryCacheUtil().getMatchingPart2List(entryScreen, textEntryMenu, part1, loc, defect, defect2, part2Name);
		}
		if(!StringUtils.isEmpty(filter)) {
			return filterStringListByFirstChar(part2List, filter);
		}
		return part2List;
	}
	/**
	 * This method is used to find all Defect by filter
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part1
	 * @param part2
	 * @return
	 */
	public List<String> findAllDefectByEntryScreen(String entryScreen, String textEntryMenu, String part1, String loc, String part2, String filter, String part1Filter) {
		List<String> defectList = new ArrayList<String>();
		if(getProductModel()!=null) {
			defectList = getDefectEntryCacheUtil().getMatchingDefectList(entryScreen, textEntryMenu, part1, loc, part2, part1Filter);
		}
		if(!StringUtils.isEmpty(filter)) {
			return filterStringListByFirstChar(defectList, filter);
		}
		return defectList;
	}
	/**
	 * This method is used to find all TextEntryMenu by EntryScreen
	 * @param entryScreen
	 * @return
	 */
	public List<String> findAllTextEntryMenuByEntryScreen(String entryScreen, String part1Filter) {
		List<QiDefectResultDto> dtoList = getDefectEntryCacheUtil().getPartDefectCombCache().getList(entryScreen,QiDefectResultDto.class);
		if(dtoList != null)
			return getDefectEntryCacheUtil().getMenuListFromDtoList(dtoList, part1Filter);
		return new ArrayList<String>();
	}
	/**
	 * This method is used to find all Part Defect Combination by filter
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param inspectionPartName
	 * @param location
	 * @param inspectionPart2Name
	 * @param defectTypeName
	 * @return
	 */
	public List<QiDefectResultDto> findAllPartDefectCombByFilter(String entryScreen, String textEntryMenu, 
			String inspectionPartName, String location, String inspectionPart2Name, String defectTypeName, String defectTypeName2, String part1Filter) {
		return getDefectEntryCacheUtil().getPdcListByTextFilter(entryScreen, textEntryMenu, inspectionPartName, location, inspectionPart2Name, defectTypeName, defectTypeName2, part1Filter);
	}

	/** 
	 * Find All Site
	 */
	public List<String> findAllSite() {
		String key = "QiSiteDao.findAllSiteName";
		if (getClientCache().containsKey(key)) {
			return getClientCache().getList(key, String.class);
		}
		List<String> data = StringUtil.trimStringList(getDao(QiSiteDao.class).findAllSiteName());
		getClientCache().put(key, data);
		return data;
	}

	/** 
	 * Find All Plant for selected site
	 * @param site
	 */
	public List<String> findAllPlantBySite(String site) {
		return StringUtil.trimStringList(getDao(QiPlantDao.class).findAllPlantBySite(site));
	}
	/** 
	 * Find All Active Department for selected Site and Plant
	 * @param site
	 * @param plant
	 */
	public List<String> findAllDepartmentBySiteAndPlant(String site, String plant) {
		return StringUtil.trimStringList(getDao(QiDepartmentDao.class).findAllActiveDepartmentsBySiteAndPlant(site, plant));
	}

	/** 
	 * Find All Active Level1 for selected Site Plant and Department
	 * @param site
	 * @param plant
	 * @param Dept
	 */
	public List<String> findAllLevel1BySitePlantAndDept(String site, String plant, String dept) {
		return StringUtil.trimStringList(getDao(QiResponsibleLevelDao.class).findAllActiveLevel1BySitePlantAndDept(site, plant, dept));
	}

	/**
	 * This method is used to find all WriteUpDept for specific Process Point
	 * @return
	 */
	public List<String> findAllWriteUpDept(){
		String key = generateCacheKey("QiStationWriteUpDepartmentDao.findAllWriteUpDeptByProcessPoint", getCurrentWorkingProcessPointId());		
		if (getClientCache().containsKey(key)) {
			return getClientCache().getList(key, String.class);
		}
		List<String> data = getDao(QiStationWriteUpDepartmentDao.class).findAllWriteUpDeptByProcessPoint(getCurrentWorkingProcessPointId());
		getClientCache().put(key, data);
		return data;
	}

	/**
	 * This method is used to create Scrap Entry for that Product
	 * @param exceptionalOut
	 * @return
	 */
	public ExceptionalOut createProductScrapEntry(ExceptionalOut exceptionalOut) {
		return getDao(ExceptionalOutDao.class).createExceptionalOut(exceptionalOut);
	}

	/**
	 * This method is used to create entry for Defect Result
	 * @param defectResult
	 * @return
	 */
	public QiDefectResult createDefectResult(QiDefectResult qiDefectResult, QiDefectResult previousQiDefectResult) {
		Logger.getLogger().info("Create DefectResult: " + qiDefectResult.toString());
		List<QiDefectResultImage> defectResultImages = qiDefectResult.getDefectResultImages();
		QiDefectResult defectResult = getDao(QiDefectResultDao.class).createQiDefectResult(qiDefectResult, previousQiDefectResult);
		defectResult.setDefectResultImages(saveDefectResultImages(defectResult, defectResultImages));
		return defectResult;
	}
	
	public List<QiDefectResultImage> saveDefectResultImages(QiDefectResult defectResult, List<QiDefectResultImage> defectResultImages) {
		List<QiDefectResultImage> result = new ArrayList<QiDefectResultImage>();
		if(!defectResultImages.isEmpty()) {
			for(QiDefectResultImage item : defectResultImages) {
				item.getId().setDefectResultId(defectResult.getDefectResultId());
				if(StringUtils.isEmpty(item.getId().getImageUrl())) {
					if(item.getFile() != null) {
						String url = MicroserviceUtils.getInstance().postFile(item.getFile());
						if(url != null) {
							item.getId().setImageUrl(url);
							item.setFile(null);
							result.add(item);
						}
					}
				}
			}
			result = getDao(QiDefectResultImageDao.class).saveAll(result);
		}
		return result;
	}

	/**
	 * This method is used to get Level 2 and Level 3 by Level 1
	 * @param site
	 * @param plant
	 * @param dept
	 * @param level1
	 * @return
	 */
	public List<QiDefectResultDto> findLevel2andLevel3ByLevel1(String site, String plant, String dept, String level1) {
		return getDao(QiResponsibleLevelDao.class).findAllLevel2AndLevel3ByLevel1(site, plant, dept, level1);
	}

	/**
	 * This method is used to find all Text Entry Menu by filter
	 * @param entryScreen
	 * @param menu
	 * @return
	 */
	public List<String> findAllTextEntryMenuByFilter(String entryScreen, String menu, String part1Filter) {
		List<String> allMenuList = findAllTextEntryMenuByEntryScreen(entryScreen, part1Filter);
		List<String> filteredMenuList = new ArrayList<String>();
		for(String entryMenu : allMenuList) {
			if(entryMenu.toUpperCase().contains(menu.toUpperCase()))
				filteredMenuList.add(entryMenu);
		}
		return filteredMenuList;
	}

	/** 
	 * Find All Defect Result
	 */
	public List<QiDefectResult> findAllByProductId(List<Short> statusList) {
		return getDao(QiDefectResultDao.class).findAllByProductId(getProductId(), statusList);
	}

	/**
	 * This method is used to check if DefectResult already exist or not
	 * @param defectResult
	 * @return
	 */
	public boolean checkDefectResultExist(QiDefectResult defectResult) {
		return getDao(QiDefectResultDao.class).checkDefectResultExist(defectResult);
	}

	/**
	 * This method is used to get MainPartNo for given Part1
	 * @param inspectionPartName
	 * @return
	 */
	public List<String> findMainPartNoByInspectionPartName(String inspectionPartName) {
		return getDao(QiBomQicsPartMappingDao.class).findAllMainPartNoByInspectionPartName(inspectionPartName);
	}

	/**
	 * This method is used to get selected Product.
	 */
	public BaseProduct getProduct() {
		return getProductModel().getProduct();
	}

	/**
	 * This method is used to find Responsible Level by id
	 * @param id
	 * @return
	 */
	public QiResponsibleLevel findResponsibleLevelById(int id) {
		return getDao(QiResponsibleLevelDao.class).findByKey(id);
	}

	/**
	 * This method is used to find All Defect by Mtc Model
	 * @param mtcModel
	 * @param entryDept
	 * @param recentDefectRange
	 * @return
	 */
	
	public List<QiRecentDefectDto> findAllByMtcModelAndEntryDept(String mtcModel, String entryDept, Integer recentDefectRange,String processPointId) {
		return getDao(QiDefectResultDao.class).findAllByMtcModelAndEntryDept(mtcModel, entryDept, getProductId(),recentDefectRange,processPointId, getProductType());
	}
	/**
	 * This method is used to find Entry Station by processPointId
	 * @return
	 */
	public QiStationConfiguration findEntryStationById(){
		return getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(),"Range");
	}
	/**
	 * This method is used to show all image sections on an image
	 * @param imageName
	 * @return
	 */
	public List<QiImageSectionPoint> showAllImageSectionsByFilter(String entryScreen, String imageName, String defect1, String defect2, String part1Filter) {
		List<QiImageSectionPoint> imageSectionList = new ArrayList<QiImageSectionPoint>();
		if (getProductModel()!=null) {
			imageSectionList = getDefectEntryCacheUtil().getMatchingImageSectionList(entryScreen, defect1, defect2, part1Filter);
		}
		return imageSectionList;
	}
	/**
	 * This method is used to show all image sections on an image
	 * @param imageName
	 * @return
	 */
	public List<QiImageSectionPoint> showAllImageSectionsByPartLocFilter(String entryScreen, String partFilter) {
		List<QiImageSectionPoint> imageSectionList = new ArrayList<QiImageSectionPoint>();
		if (getProductModel()!=null) {
			imageSectionList = getDefectEntryCacheUtil().getMatchingImageSectionListByPartFilter(entryScreen, partFilter);
		}
		return imageSectionList;
	}
	/**
	 * This method is used to show all image sections on an image
	 * @param imageName
	 * @return
	 */
	public List<QiImageSectionPoint> showImageSectionByFilter(String entryScreen, String partFilter) {
		List<QiImageSectionPoint> imageSectionList = new ArrayList<QiImageSectionPoint>();
		if (getProductModel()!=null) {
			imageSectionList = getDefectEntryCacheUtil().getMatchingImageSectionListByPartFilter(entryScreen, partFilter);
		}
		return imageSectionList;
	}
	/**
	 * This method is used to fetch all Defect 1 for selected image section for particular image entry screen
	 * @param entryScreen
	 * @param imageSectionId
	 * @param defect2
	 * @return
	 */
	public List<String> findAllDefect1ByImageEntryScreen(String entryScreen, String imageName, int imageSectionId, String defect2, int partLocationId, String filter, boolean realProblemDefectOnly, String part1Filter) {
		List<String> defect1List = new ArrayList<String>();
		if(getProductModel()!=null){
			defect1List = getDefectEntryCacheUtil().getMatchingDefect1List(entryScreen, imageSectionId, defect2, partLocationId, part1Filter);
		}
		if(!StringUtils.isEmpty(filter)) {
			return filterStringListByFirstChar(defect1List, filter);
		}
		return defect1List;
	}
	/**
	 * This method is used to fetch all Defect 2 for selected image section for particular image entry screen
	 * @param entryScreen
	 * @param imageSectionId
	 * @param defect1
	 * @return
	 */
	public List<String> findAllDefect2ByImageEntryScreen(String entryScreen, String imageName, int imageSectionId, String defect1, int partLocationId, String filter, String part1Filter) {
		List<String> defect2List = new ArrayList<String>();
		if(getProductModel()!=null){
			defect2List = getDefectEntryCacheUtil().getMatchingDefect2List(entryScreen, imageSectionId, defect1, partLocationId, part1Filter);
		}
		defect2List.removeAll(Arrays.asList(null,StringUtils.EMPTY));
		if(!StringUtils.isEmpty(filter)) {
			return filterStringListByFirstChar(defect2List, filter);
		}
		return defect2List;
	}


	/**
	 * This method finds color code for given process point id and writeup dept
	 * @param dept
	 */
	public String findColorCodeByWriteupDeptAndProcessPointId(String dept){
		return getDao(QiStationWriteUpDepartmentDao.class).findColorCodeByWriteupDeptAndProcessPointId(dept, getCurrentWorkingProcessPointId());
	}

	/**
	 * This method finds property value against property key based on process point.
	 * @return QiEntryStationConfigManagement
	 */
	public QiStationConfiguration findPropertyKeyValueByProcessPoint(String propertyKey) {
		String key = generateCacheKey("QiStationConfigurationDao.findValueByProcessPointAndPropKey", getCurrentWorkingProcessPointId(), propertyKey);
		if (getClientCache().containsKey(key)) {
			return getClientCache().get(key, QiStationConfiguration.class);
		}
		QiStationConfiguration data = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(), propertyKey);
		getClientCache().put(key, data);
		return data;		
	}

	public QiStationConfiguration findStationConfiguration(String propertyKey) {
		QiStationConfiguration data = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getCurrentWorkingProcessPointId(), propertyKey);
		return data;		
	}

	/**
	 * This method is used to get Line Color for Image Section
	 * @return
	 */
	public String getLineColor(){
		return getProperty().getLineColor();
	}

	/**
	 * This method is used to get Line Width for Image Section
	 * @return
	 */
	public int getLineWidth(){
		return getProperty().getLineWidth();
	}

	/**
	 * This method is used to font size for thumbnail entryscreenname hover popup text 
	 * @return
	 */
	public int getThumbnailHoverFontSize(){
		return getProperty().getThumbnailHoverFontSize();
	}

	/**
	 * This method is used to get Full Part Defect Description for given Image Section Id
	 * @param imageSectionId
	 * @param defectTypeName
	 * @param defectTypeName2
	 * @return
	 */
	public List<QiDefectResultDto> findAllFullPartDefectCombByFilter(String entryScreen, int imageSectionId, String defectTypeName, String defectTypeName2, int partLocationId) {
		return getDefectEntryCacheUtil().findAllPartDefectCombByImageFilter(entryScreen, imageSectionId, defectTypeName, defectTypeName2, partLocationId);
	}

	/**
	 * This method is used to fetch all part location combinations for a given image section id
	 * @param entryScreen
	 * @param imageSectionId
	 * @return
	 */
	public QiImageSectionDto findPartLocationComb(String entryScreen, Integer imageSectionId){
		return getDefectEntryCacheUtil().getMatchingPartLocCombList(entryScreen, imageSectionId);
	}

	/**
	 * This method is used to find default writeUp department as configured from Station Configuration Screen based on process point.
	 * @return QiStationWriteUpDepartment
	 */
	public QiStationWriteUpDepartment findDefaultWriteUpDeptByProcessPoint() {
		String key = generateCacheKey("QiStationWriteUpDepartmentDao.findDefaultWriteUpDeptByProcessPoint", getCurrentWorkingProcessPointId());
		if (getClientCache().containsKey(key)) {
			return getClientCache().get(key, QiStationWriteUpDepartment.class);
		}
		QiStationWriteUpDepartment data = getDao(QiStationWriteUpDepartmentDao.class).findDefaultWriteUpDeptByProcessPoint(getCurrentWorkingProcessPointId());
		getClientCache().put(key, data);
		return data;
	}

	/**
	 * This method finds property value against property key based on process point.
	 * @return QiEntryStationConfigManagement
	 */
	public QiDefectResult findDefectResultById(long id){
		return getDao(QiDefectResultDao.class).findByKey(id);
	}

	/**
	 * Find the most recent defect that has PDC matches an existing defectResult and entered at process point
	 * @return QiDefectResult
	 */
	public QiDefectResult findDefect(QiDefectResult defectResult, String applicationId) {
		return getDao(QiDefectResultDao.class).findFirstMatch(defectResult, applicationId);
	}
	
	/**
	 * This method is used to update entry for Defect Result.
	 * @param defectResult
	 * @return 
	 */
	public QiDefectResult updateDefectResult(QiDefectResult defectResult) {
		return getDao(QiDefectResultDao.class).update(defectResult);
	}

	/**
	 * This method is used to find All Image data by ImageName
	 * @param imageName
	 */
	public QiImage getImageByImageName(String imageName){
		return getDao(QiImageDao.class).findImageByImageName(imageName);
	}

	/**
	 * This method is used to execute action for Product Buttons: DONE & DIRECT PASS
	 */
	public void refreshCache() {
		getDefectEntryCacheUtil().clearProductCache();
	}

	/**
	 * This method is used to refresh defectByimagename
	 */
	public void refreshDefect() {
		imageDefectResult.clear();
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
	/**
	 * This method is used to fetch all part location combinations for a given image section ids
	 * @param imageSectionIdList
	 * @return
	 */
	public List<QiImageSectionDto> findAllPartLocationCombByImageSections(String entryScreen, List<Integer> imageSectionIdList, String defect1, String defect2, String part1Filter) {
		List<QiImageSectionDto> allPartLocationCombByImageSections = getDefectEntryCacheUtil().getMatchingPartLocCombList(entryScreen, imageSectionIdList, defect1, defect2, part1Filter);

		Collections.sort(allPartLocationCombByImageSections,new Comparator<QiImageSectionDto>() {
			@Override
			public int compare(QiImageSectionDto dto1, QiImageSectionDto dto2) {
				return dto1.getFullPartDesc().compareTo(dto2.getFullPartDesc());
			}
		}); 
		return allPartLocationCombByImageSections;
	}

	@SuppressWarnings("unchecked")
	public KickoutDto createKickout(QiDefectResult defectResult) {
		List<KickoutDto> kickoutDtoList = new ArrayList<KickoutDto>();
		KickoutDto kickoutDto = new KickoutDto();

		kickoutDto.setProductId(defectResult.getProductId());
		kickoutDto.setProductType(defectResult.getProductType());
		kickoutDtoList.add(kickoutDto);

		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		data.put(DataContainerTag.APPLICATION_ID, getApplicationId());
		data.put(DataContainerTag.ASSOCIATE_NO, defectResult.getCreateUser());
		data.put(DataContainerTag.DESCRIPTION, defectResult.getInspectionPartName() + " " + defectResult.getDefectTypeName());
		data.put(DataContainerTag.KICKOUT_COMMENT,defectResult.getComment());
		data.put(DataContainerTag.DIVISION_ID, defectResult.getKickoutDivisionId());
		data.put(DataContainerTag.LINE_ID, defectResult.getKickoutLineId());
		data.put(DataContainerTag.PROCESS_POINT_ID, defectResult.getKickoutProcessPointId());

		KickoutService kickoutService = ServiceFactory.getService(KickoutService.class);
		DataContainer resultDc = kickoutService.kickoutProducts(data);
		KickoutDto resultKickoutDto = new KickoutDto();
		if(resultDc != null && resultDc.get(DataContainerTag.KICKOUT_PRODUCTS) != null) {
			resultKickoutDto = ((List<KickoutDto>) resultDc.get(DataContainerTag.KICKOUT_PRODUCTS)).get(0);
		}
		return resultKickoutDto;
	}

	/**
	 * This method is used to generate Part Defect Comb Cache
	 * @param entryScreen
	 * @param isImageEntryScreen
	 */
	public void generatePartDefectCombCache(QiDefectEntryDto entryScreen, boolean isImageEntryScreen, boolean realProblemPdcOnly, boolean mbpnDefectEntryLimitToProductIdPart, String entryModel) {
		List<QiDefectResultDto> partDefectCombList = new ArrayList<QiDefectResultDto>();
		String entryScreenName = entryScreen.getEntryScreen();

		if (getProductModel()!=null) {
			String mainNo = mbpnDefectEntryLimitToProductIdPart ? getMainNo() : null;
			partDefectCombList = getPartDefectCombinations(entryScreen, entryModel, getInspectionPartName(), mainNo);
			if (isImageEntryScreen) {
				getImageSections(entryScreen, getProductKind(), getMtcModel(), getCurrentWorkingProcessPointId(), getProductType(), mainNo);
			}
			// filtering based on defect category
			if (realProblemPdcOnly) {
				List<QiDefectResultDto> realProblemPartDefectCombList = new ArrayList<QiDefectResultDto>();
				for (QiDefectResultDto defectResultDto : partDefectCombList) {
					if (QiConstant.REAL_PROBLEM.equalsIgnoreCase(defectResultDto.getDefectCategoryName())) {
						realProblemPartDefectCombList.add(defectResultDto);
					}
				}
				getDefectEntryCacheUtil().getPartDefectCombCache().put(entryScreenName, realProblemPartDefectCombList);
			} else {
				getDefectEntryCacheUtil().getPartDefectCombCache().remove(entryScreenName);
				getDefectEntryCacheUtil().getPartDefectCombCache().put(entryScreenName, partDefectCombList); //refresh with original part defect comb list
			}
		}
	}

	protected List<QiDefectResultDto> getPartDefectCombinations(QiDefectEntryDto entryScreen, String entryModel, String inspectionPartName, String mainNo) {
		String entryScreenName = entryScreen.getEntryScreen(); 
		String cachePdcKey = getCachePdcKey(entryScreenName, mainNo);
		if (getDefectEntryCacheUtil().getPartDefectCombOriginalCache().containsKey(cachePdcKey)) {
			return getDefectEntryCacheUtil().getPartDefectCombOriginalCache().getList(cachePdcKey, QiDefectResultDto.class);
		}
		List<QiDefectResultDto> partDefectCombList = null;
		if (entryScreen.isImage()) {
			String imageName = entryScreen.getImageName();
			//REMARK: if a number of variable parameters changes for this dao method, please update getCachePdcKey() method to reflect parameters
			partDefectCombList = getDao(QiLocalDefectCombinationDao.class).findAllPartDefectCombByDefectEntryImageFilter(imageName, entryScreenName, entryModel, inspectionPartName, mainNo);
		} else {
			//REMARK: if a number of variable parameters changes for this dao method, please update getPdcCacheKey() method to reflect parameters 
			partDefectCombList = getDao(QiLocalDefectCombinationDao.class).findAllPartDefectCombByDefectEntryTextFilter(entryScreenName, entryModel, inspectionPartName, mainNo);
		}
		getDefectEntryCacheUtil().getPartDefectCombOriginalCache().put(cachePdcKey, partDefectCombList);
		return partDefectCombList;
	}

	protected List<QiImageSectionPoint> getImageSections(QiDefectEntryDto entryScreen, String productKind, String mtcModel, String processPointId, String productType, String mainNo) {
		String entryScreenName = entryScreen.getEntryScreen();
		String imageName = entryScreen.getImageName();
		String cacheImageSectionKey = getCacheImageSectionKey(entryScreenName, imageName, mainNo);
		List<QiImageSectionPoint> imageSectionList = new ArrayList<QiImageSectionPoint>();
		if (getDefectEntryCacheUtil().getImageSectionCache().containsKey(entryScreenName)) {
			return getDefectEntryCacheUtil().getImageSectionCache().getList(entryScreenName, QiImageSectionPoint.class);
		}
		if (getDefectEntryCacheUtil().getImageSectionClientCache().containsKey(cacheImageSectionKey)) {
			imageSectionList = getDefectEntryCacheUtil().getImageSectionClientCache().getList(cacheImageSectionKey, QiImageSectionPoint.class);
		} else {
			//REMARK: if a number of variable parameters changes for this dao method, please update getCacheImageSecionKey() method to reflect parameters 
			imageSectionList = getDao(QiImageSectionPointDao.class).findAllByDefectFilter(productKind, entryScreenName, imageName, mtcModel, processPointId, getProductType(), mainNo);
			getDefectEntryCacheUtil().getImageSectionClientCache().put(cacheImageSectionKey, imageSectionList);
		}
		getDefectEntryCacheUtil().getImageSectionCache().put(entryScreenName, imageSectionList);
		return imageSectionList;
	}

	public String getCachePdcKey(String entryScreenName, String mainNo) {
		String key = generateCacheKey(entryScreenName, mainNo, getMtcModel(), getCurrentWorkingProcessPointId(), getProductKind(), getInspectionPartName(), getProductType());
		return key;
	}

	public String getCacheImageSectionKey(String entryScreenName, String imageName, String mainNo) {
		String key = generateCacheKey(getProductKind(), entryScreenName, imageName,  mainNo, getMtcModel(), getCurrentWorkingProcessPointId(), getProductType());
		return key;
	}

	public String getEntryModel()  {
		String entryModel = "";
		QiEntryModelGrouping entryModelGrouping = findByMtcModel();
		if (entryModelGrouping != null && entryModelGrouping.getId() != null) {
			entryModel = entryModelGrouping.getId().getEntryModel();
		}
		return entryModel;
	}

	/**
	 * This method is used to find entry model based on mtc model
	 * @param mtcModel
	 * @return
	 */
	public QiEntryModelGrouping findByMtcModel() {
		String key = generateCacheKey("QiEntryModelGroupingDao.findByMtcModel", getMtcModel(), getProductType());		
		if (getClientCache().containsKey(key)) {
			return getClientCache().get(key, QiEntryModelGrouping.class);
		}
		QiEntryModelGrouping data = getDao(QiEntryModelGroupingDao.class).findByMtcModel(getMtcModel(), getProductType());
		getClientCache().put(key, data);
		return data;		
	}
	/**
	 * This method is used to find All mtc Model based on entry model
	 * @param entryModel
	 * @return
	 */
	public List<String> findAllMtcModelByEntryModel(String entryModel) {
		return getDao(QiEntryModelGroupingDao.class).findAllMtcModelByEntryModel(entryModel, getProductType());
	}

	public Timestamp getProcessPointTimeStamp() {
		return getDao(ProcessPointDao.class).getDatabaseTimeStamp();
	}

	/**
	 * This method is used to filter String ArrayList
	 */
	private List<String> filterStringListByFirstChar(List<String> list, String filter) {
		List<String> filteredList = new ArrayList<String>();
		for(String string : list) {
			if(string.startsWith(filter))
				filteredList.add(string);
		}
		return filteredList;
	}

	/**
	 * save the repair result data
	 * 
	 * @param qiRepairResultList
	 */
	public List<QiRepairResult> saveAllRepairResults(List<QiRepairResult> qiRepairResultList) {
		return getDao(QiRepairResultDao.class).createRepairResults(qiRepairResultList);
	}

	/**
	 * inserts the repair method data when the Add Repair Method button is
	 * clicked
	 * 
	 * @param qiAppliedRepairMethod
	 */
	public List<QiAppliedRepairMethod> saveAllRepairMethods(List<QiAppliedRepairMethod> qiAppliedRepairMethodList) {
		return getDao(QiAppliedRepairMethodDao.class).saveAllRepairMethods(qiAppliedRepairMethodList, getCurrentWorkingProcessPointId());
	}
	/**
	 * gets the max of the sequence column(REPAIR_METHOD_SEQ)
	 * 
	 * @return
	 */
	public Integer findMaxSequenceValue() {
		return getDao(QiAppliedRepairMethodDao.class).findCurrentSequence();
	}
	/**
	 * This method is used to update entry for Defect Result List.
	 * @param defectResultList
	 * @return 
	 */
	public void updateAllDefectResults(List<QiDefectResult> defectResultList) {
		getDao(QiDefectResultDao.class).updateAll(defectResultList);
	}
	/**
	 * This method is used to find All Part1 on the basis of given params
	 * @return 
	 */
	public List<String> findAllPart1ByProcessPoint(String entryDept){
		String key = generateCacheKey("QiPartLocationCombinationDao.findAllPart1ByProcessPoint", getCurrentWorkingProcessPointId(), getProductKind(), getMtcModel(),entryDept);
		if (getClientCache().containsKey(key)) {
			return getClientCache().getList(key, String.class);
		}
		List<String> data = getDao(QiPartLocationCombinationDao.class).findAllPart1ByProcessPoint(getCurrentWorkingProcessPointId(), getProductKind(), getMtcModel(),entryDept);
		getClientCache().put(key, data);
		return data;		
	}

	/**
	 * This method is used to check if PDC exist in current session or not.
	 * @param defectResult
	 * @return
	 */
	public boolean isPdcExistInCurrentSession(QiDefectResult defectResult) {
		return getDefectEntryCacheUtil().isPdcExistInCurrentSession(defectResult);
	}
	/**
	 * This method finds all Entry Screen by Process Point.
	 * @return List<QiDefectResultDto>
	 */
	public List<QiDefectEntryDto> findAllEntryScreenByProcessPoint(String filter, String entryDept, boolean onlyRealProblemScreen) {
		if (getProductModel() == null) {
			return new ArrayList<QiDefectEntryDto>();
		}
		String key = generateCacheKey("QiStationEntryScreenDao.findAllEntryScreenByProcessPoint", getProductKind(), filter, getCurrentWorkingProcessPointId(), getMtcModel(), entryDept, onlyRealProblemScreen, getProductType());
		if (getClientCache().containsKey(key)) {
			return getClientCache().getList(key, QiDefectEntryDto.class);
		}
		List<QiDefectEntryDto> data = getDao(QiStationEntryScreenDao.class).findAllEntryScreenByProcessPointAndPartLocation(filter, getCurrentWorkingProcessPointId(), getMtcModel(), entryDept, onlyRealProblemScreen);
		getClientCache().put(key, data);
		return data;
	}
	/**
	 * This method is used to find Station Previous Defect by ProcessPoint
	 * @return
	 */
	public QiStationPreviousDefect findStationPreviousDefectByProcessPointAndEntryDept() {
		QiStationPreviousDefectId qiStationPreviousDefectId = new QiStationPreviousDefectId();
		qiStationPreviousDefectId.setProcessPointId(getCurrentWorkingProcessPointId());
		qiStationPreviousDefectId.setEntryDivisionId(getProperty().isUpcStation() ? getEntryDept() : getCurrentWorkingEntryDept());
		return getDao(QiStationPreviousDefectDao.class).findByKey(qiStationPreviousDefectId);
	}
	/**
	 * This method finds max repair id.
	 */
	public long findMaxRepairId(){
		return getDao(QiRepairResultDao.class).findMaxRepairId();
	}

	/**
	 * This method finds max defect_result_id
	 * @return
	 */
	public long findMaxDefectResultId() {
		return getDao(QiDefectResultDao.class).findMaxDefectResultId();
	}

	public QiResponsibleLevel findResponsibleLevelByNameAndDeptId(String respLevel1, QiDepartmentId qiDepartmentId) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentAndLevelName( 
				qiDepartmentId.getSite(), qiDepartmentId.getPlant(), qiDepartmentId.getDepartment(),respLevel1);
	}

	/**
	 * This method is used to find all defect result by image name and entry department
	 * @param entryScreen
	 * @param imageName
	 * @param entryDept
	 * @return
	 */
	public List<QiDefectResult> findAllByImageNameAndEntryDept(String imageName, String entryDept){

		if (imageDefectResult.get(imageName) == null) {
			imageDefectResult.put(imageName, getDao(QiDefectResultDao.class).findAllByImageNameAndEntryDept(getProductId(),imageName ,entryDept));
		}
		return imageDefectResult.get(imageName); 
	}



	/**
	 * This method is used to find all defect result by product id and entry department
	 * @param entryScreen
	 * @param imageName
	 * @param entryDept
	 * @return
	 */
	public List<QiDefectResult> findAllByProductIdAndEntryDept(String entryDept) {
		return getDao(QiDefectResultDao.class).findAllByProductIdAndEntryDept(getProductId(),entryDept);
	}
	/**
	 * This method is used to update entry for Engine Firing Flag in GAL131TBX.
	 * @param engineFiringFlag
	 * @return 
	 */
	public void updateFiringFlag(boolean engineFiringFlag){
		getDao(EngineDao.class).updateFiringFlag(getProductId(), engineFiringFlag);
	}

	public SubProduct findSubIdByProductId(){
		return getDao(SubProductDao.class).findByKey(getProductId());
	}

	public List<QiStationResponsibilityDto> findAllAssignedStationResponsibilitiesByProcessPoint() {
		String key = generateCacheKey("QiStationResponsibilityDao.findAllAssignedRespByProcessPoint", getCurrentWorkingProcessPointId());
		if (getClientCache().containsKey(key)) {
			return getClientCache().getList(key, QiStationResponsibilityDto.class);
		}
		List<QiStationResponsibilityDto> data = getDao(QiStationResponsibilityDao.class).findAllAssignedRespByProcessPoint(getCurrentWorkingProcessPointId());
		getClientCache().put(key, data);
		return data;		
	}
	public List<QiStationPreviousDefect> findAllByProcessPoint() {
		return getDao(QiStationPreviousDefectDao.class).findAllByProcessPoint(getCurrentWorkingProcessPointId());
	}

	public List<QiStationEntryScreen> findAllByEntryScreenModelAndDept(String entryScreenName, String entryModel, String entryDept, String processPointId) {
		String key = generateCacheKey("QiStationEntryScreenDao.findAllByEntryScreenModelAndDept", entryScreenName, entryModel, entryDept, processPointId);
		if (getClientCache().containsKey(key)) {
			return getClientCache().getList(key, QiStationEntryScreen.class);
		}
		List<QiStationEntryScreen> data = getDao(QiStationEntryScreenDao.class).findAllByEntryScreenModelAndDept(entryScreenName, entryModel, entryDept, processPointId);
		getClientCache().put(key, data);
		return data;
	}

	/**This method is used to get Repair Result by defectResultId
	 * @return List<QiRepairResult>
	 */
	public List<QiRepairResult> findAllRepairResultByDefectResultId(long defectResultId) {
		return getDao(QiRepairResultDao.class).findAllByDefectResultId(defectResultId);
	}

	/**
	 * gets the max of the sequence column(REPAIR_METHOD_SEQ)
	 * @return
	 */
	public Integer findMaxSequenceValueByRepairId(Long repairId) {
		return getDao(QiAppliedRepairMethodDao.class).findCurrentSequence(repairId);
	}

	public List<QiMostFrequentDefectsDto> findMostFrequentDefectsByProcessPointEntryScreenDuration(String entryScreen, Date beginTs)
	{
		List<QiMostFrequentDefectsDto> recentDefectList = new ArrayList<QiMostFrequentDefectsDto>();
		if(beginTs != null)  {
			recentDefectList = getDao(QiDefectResultDao.class).findMostFrequentDefectsByProcessPointEntryScreenDuration(
					getCurrentWorkingProcessPointId(), entryScreen, getEntryModel(), getCurrentWorkingEntryDept(),
					beginTs);
		}
		return recentDefectList;
	}

	public int getCachedDefectResultCount() {
		return cachedDefectResultList != null && !cachedDefectResultList.isEmpty() ? cachedDefectResultList.size() : 0;
	}

	public boolean hasExistingDefect() {
		List<QiStationPreviousDefect> previousDefectList = findAllByProcessPoint();
		List<QiDefectResult> configuredDefectList = new ArrayList<QiDefectResult>();
		StringBuilder entryDept = new StringBuilder(StringUtils.EMPTY);
		Map<String, String> configuredPreviousDefect = new HashMap<String, String>();

		for(QiStationPreviousDefect qiStationPreviousDefect : previousDefectList ){
			configuredPreviousDefect.put(qiStationPreviousDefect.getId().getEntryDivisionId(), qiStationPreviousDefect.getOriginalDefectStatus() + "," + qiStationPreviousDefect.getCurrentDefectStatus());
			if(!StringUtils.isEmpty(entryDept.toString()))
				entryDept.append(",").append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
			else
				entryDept.append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
		}

		List<QiDefectResult> defectResults = new  ArrayList<QiDefectResult>();
		if(!StringUtils.isEmpty(entryDept.toString()))
			defectResults = findAllByProductIdAndEntryDept(entryDept.toString());
		for(QiDefectResult qiDefectResult : defectResults ){
			for(Map.Entry<String, String>  map:configuredPreviousDefect.entrySet()){
				if(map.getKey().toString().equalsIgnoreCase(qiDefectResult.getEntryDept())){
					String statusString = map.getValue();
					int index = statusString.indexOf(",");
					short originalStatus = new Short(statusString.substring(0, index)).shortValue();
					short currentStatus = new Short(statusString.substring(index + 1)).shortValue();
					if (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == (short)DefectStatus.ALL.getId()
							|| (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == qiDefectResult.getCurrentDefectStatus()) 
							|| (currentStatus == (short)DefectStatus.ALL.getId() && originalStatus == qiDefectResult.getOriginalDefectStatus())
							|| (originalStatus == qiDefectResult.getOriginalDefectStatus() && currentStatus == qiDefectResult.getCurrentDefectStatus())) {
						configuredDefectList.add(qiDefectResult);
					}
				}
			}
		}

		if (configuredDefectList.size() > 0) {
			return true;
		}

		if (getCachedDefectResultList().size() > 0) {
			return true;
		}

		return false;
	}


	public void deleteOldRepairResult(long qiRepairId) {
		getDao(DefectRepairResultDao.class).deleteByQiRepairId(qiRepairId);
	}

	public void updateOldDefectStatus(long qiDefectResultId, int defectStatus, java.util.Date repairTimestamp, String repairAssociateNo) {
		getDao(DefectResultDao.class).updateByQiDefectResultId(qiDefectResultId, defectStatus, repairTimestamp, repairAssociateNo);
	}

	public void replicateRepairResult(QiAppliedRepairMethodDto qiAppliedRepairMethodDto, String partDefectDesc, String repairProcessPointId) {
		getDao(QiAppliedRepairMethodDao.class).replicateRepairResult(qiAppliedRepairMethodDto, partDefectDesc, repairProcessPointId);
	}

	protected String generateCacheKey(Object...params) {
		return StringUtils.join(params, Delimiter.DOT);
	}

	protected PersistentCache getClientCache() {
		return getProductModel().getClientCache();	
	}

	//Begin Responsibility Interface
	public List<QiResponsibleLevel> findAllBySitePlantDepartmentLevel(String site, String plant, String department,short level){
		return getDao(QiResponsibleLevelDao.class).findAllBySitePlantDepartmentLevel(site, plant, department,level);
	}

	public QiResponsibleLevel findByResponsibleLevelId(Integer responsibleLevelId){
		return getDao(QiResponsibleLevelDao.class).findByResponsibleLevelId(responsibleLevelId);
	}

	public List<QiResponsibleLevel> findAllLevel2HavingSameLevel1(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel2HavingSameLevel1(site, plant, department, lvl1Name);
	}

	public List<QiResponsibleLevel> findAllAssignedLevel2HavingSameLevel1(String site, String plant, String department,String lvl1Name, String processPointId){
		return getDao(QiResponsibleLevelDao.class).findAllAssignedLevel2HavingSameLevel1(site, plant, department, lvl1Name, processPointId);
	}

	public List<QiResponsibleLevel> findAllLevel2HavingAssignedLevel1(String site, String plant, String department,String processPointId){
		return getDao(QiResponsibleLevelDao.class).findAllLevel2HavingAssignedLevel1(site, plant, department, processPointId);
	}

	public List<QiResponsibleLevel> findAllLevel3HavingSameLevel1(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel3HavingSameLevel1(site, plant, department, lvl1Name);
	}

	public List<QiResponsibleLevel> findAllLevel3HavingAssignedLevel1(String site, String plant, String department,String processPointId){
		return getDao(QiResponsibleLevelDao.class).findAllLevel3HavingAssignedLevel1(site, plant, department, processPointId);
	}

	public List<QiResponsibleLevel> findAllLevel1HavingSameLevel2(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel1HavingSameLevel2(site, plant, department, lvl1Name);
	}

	public List<QiResponsibleLevel> findAllAssignedLevel1HavingSameLevel2(String site, String plant, String department,String lvl2Name, String processPoint){
		return getDao(QiResponsibleLevelDao.class).findAllAssignedLevel1HavingSameLevel2(site, plant, department, lvl2Name, processPoint);
	}

	/**
	 * This method gets all level 2 s having a level 1 with the given responsibility level 1 name
	 * @param site
	 * @param plant
	 * @param department
	 * @param level
	 * @return List<QiResponsibleLevel>
	 */
	public List<QiResponsibleLevel> findAllLevel3HavingSameLevel2(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel3HavingSameLevel2(site, plant, department, lvl1Name);
	}

	public List<QiResponsibleLevel> findAllLevel1HavingSameLevel3(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel1HavingSameLevel3(site, plant, department, lvl1Name);
	}

	public List<QiResponsibleLevel> findAllAssignedLevel1HavingSameLevel3(String site, String plant, String department,String lvl3Name, String processPoint){
		return getDao(QiResponsibleLevelDao.class).findAllAssignedLevel1HavingSameLevel3(site, plant, department, lvl3Name, processPoint);
	}

	public List<QiResponsibleLevel> findAllLevel2HavingSameLevel3(String site, String plant, String department,String lvl1Name){
		return getDao(QiResponsibleLevelDao.class).findAllLevel2HavingSameLevel3(site, plant, department, lvl1Name);
	}

	public QiResponsibleLevel findLevel1BySitePlantDepartmentLevelNameAndLevel(String site,String plant, String dept, String level1) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentLevelNameAndLevel(site,plant, dept,level1,(short) 1);
	}

	public QiResponsibleLevel findBySitePlantDeptLvlNameUpperLvlNameAndLvl(String site,String plant, String dept, String lvlName, String upperLvlName, int lvl, int upperLvl) {
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDeptLvlAndNameAndUpperLvlAndName(site, plant, dept, lvlName, upperLvlName, (short)lvl, (short) upperLvl);
	}
	public QiResponsibleLevel findResponsibleLevel1BySitePlantDeptL1L2L3Names(String site,String plant, String dept, String l1Name, String l2Name, String l3Name) {
		return getDao(QiResponsibleLevelDao.class).findResponsibleLevel1BySitePlantDeptL1L2L3Names(site, plant, dept,  l1Name, l2Name, l3Name);
	}

	public QiResponsibleLevel findBySitePlantDepartmentLevelName(String site, String plant, String department, String levelName){
		return getDao(QiResponsibleLevelDao.class).findBySitePlantDepartmentAndLevelName(site, plant, department, levelName);
	}

	public QiResponsibleLevel findResponsibleLevelById(Integer responsibleLevelId){
		return getDao(QiResponsibleLevelDao.class).findByKey(responsibleLevelId);
	}

	public List<QiResponsibleLevel> findAllBySitePlantAndDepartment(String site, String plant, String department, short level, Integer upperResponsibleLevelId) {
		return getDao(QiResponsibleLevelDao.class).findAllBySitePlantAndDepartment(site, plant, department, level, upperResponsibleLevelId);
	}
	//End Responsibility Interface

	//check if current tracking status is invalid previous line ID for this process point
	public boolean isPreviousLineInvalid() {
		boolean isLineIdCheckEnabled = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getCurrentWorkingProcessPoint().getProcessPointId()).isLineIdCheckEnabled();
		if (isLineIdCheckEnabled) {
			ProductCheckUtil productCheckUtil = new ProductCheckUtil();
			//get current tracking status
			BaseProduct product = ProductTypeUtil.getTypeUtil(getProduct().getProductType()).findProduct(getProduct().getProductId());
			productCheckUtil.setProduct(product); 
			productCheckUtil.setProcessPoint(getCurrentWorkingProcessPoint());
			return productCheckUtil.invalidPreviousLineCheck();
		} else {
			return false;
		}
	}

	public List<QiResponsibilityMapping> findAll() {
		return getDao(QiResponsibilityMappingDao.class).findAll();
	}

	public QiResponsibleLevel findDefaultResponsibleLevel1(long defectResultId) {
		QiResponsibleLevel defaultResponsibleLevel1 = null;
		QiDefectResultHist defectResultHist = getDao(QiDefectResultHistDao.class).findFirstDefectResultHistory(defectResultId);
		if (defectResultHist != null) { // has defect result history record, responsibility may be overwritten
			defaultResponsibleLevel1 = getDao(QiResponsibleLevelDao.class).findDefaultResponsibleLevel1(defectResultHist);
		} 
		return defaultResponsibleLevel1;
	}

	public Timestamp getInitialVqGdpTimestamp(String productId, List<String> processPointIdList) {
		return getDao(ProductResultDao.class).getInitialProcessTimestamp(productId, processPointIdList);
	}

	public List<QiDefectResult> findScrappedDefectsForProductId(String productId) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByProductIdAndCurrentDefectStatus(productId, (short) DefectStatus.NON_REPAIRABLE.getId());
	}
	
	public void updateNotCompletelyFixed(String repairIdsString) {
		getDao(QiAppliedRepairMethodDao.class).updateNotCompletelyFixed(repairIdsString, getUserId());
	}

}