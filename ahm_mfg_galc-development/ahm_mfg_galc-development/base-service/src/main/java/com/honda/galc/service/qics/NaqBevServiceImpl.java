package com.honda.galc.service.qics;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiIqsDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.dao.qi.QiPddaResponsibilityDao;
import com.honda.galc.dao.qi.QiRepairMethodDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiAppliedRepairMethodId;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.NaqBevService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.ScrapService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.DailyDepartmentScheduleUtil;


public class NaqBevServiceImpl implements NaqBevService {
	private static final String OK = "OK";
	private static final String NG = "NG";
	private static final String DEFAULT_REPAIR_METHOD = "GOT REPAIR";
	private static final String DEFAULT_USER_ID = "GOT USER";
	private static final String REPAIR_COMMENT_PREFIX = "Changed to Fixed from ";
	private static final String DEFAULT_SCRAP_COMMENT = "Scrapped from External System";
	private static final short DEFAULT_PROD_LINE_NO = 1;
	private static final int[] REPAIRABLE_DEFECT_STATUS = {DefectStatus.NOT_FIXED.getId(), DefectStatus.NOT_REPAIRED.getId()};
	
	@Override
	public List<QiDefectResult> getDefectByProcessPointIdAndProductId(String processPointId, String productId) {
		try {
			return getDao(QiDefectResultDao.class).findAllByProductIdAndApplicationId(productId, processPointId);
		} catch (Exception e) {
			logStackTrace(e);
			return new ArrayList<QiDefectResult>();
		}
	}
	
	@Override
	public List<QiDefectResult> getDefects(String processPointId, String productId, String defectStatus) {
		try {
			List<QiDefectResult> defectResults = getDao(QiDefectResultDao.class).findAllByProductIdAndApplicationId(productId, processPointId);
			return defectResults.stream().filter(n -> n.getCurrentDefectStatus() == Short.parseShort(defectStatus)).collect(Collectors.toList());
		} catch (Exception e) {
			logStackTrace(e);
			return new ArrayList<QiDefectResult>();
		}
	}

	@Override
	public List<QiDefectResult> getDefectByProductId(String productId) {
		try {
			return getDao(QiDefectResultDao.class).findAllByProductId(productId);
		} catch (Exception e) {
			logStackTrace(e);
			return new ArrayList<QiDefectResult>();
		}
	}

	public List<QiDefectResult> getDefectByProductIdAndDefectStatus(String productId, String defectStatus) {
		try {
			return getDao(QiDefectResultDao.class).findAllByProductIdAndCurrentDefectStatus(productId, Short.parseShort(defectStatus));
		} catch (Exception e) {
			logStackTrace(e);
			return new ArrayList<QiDefectResult>();
		}
	}

	@Override
	public List<QiDefectResultDto> getPdcByPPAndProductId(String processPointId, String productId) {
		try {
			return getDefectCombination(processPointId, productId);
		} catch (Exception e) {
			logStackTrace(e);
			return new ArrayList<QiDefectResultDto>();
		}
	}

	@Override
	public List<QiDefectResultDto> getPdcByPPAndYearModel(String processPointId, String yearModel) {
		List<QiDefectResultDto> result = new ArrayList<QiDefectResultDto>();
		
		try {
			QiEntryModelGrouping entryModelGrouping = getDao(QiEntryModelGroupingDao.class).findByMtcModelAndVersion(yearModel, (short) 1);

			if(entryModelGrouping == null) {
				Logger.getLogger().error("Unable to find QiEntryModelGrouping for " + yearModel);
				return result;
			}
			String entryModel = entryModelGrouping.getId().getEntryModel();
			
			Logger.getLogger().info("Entry model for the request: " + entryModel);
			List<QiStationEntryScreen> stationEntryScreenList = getDao(QiStationEntryScreenDao.class).findAllByProcessPointAndEntryModel(processPointId, entryModel);
			for(QiStationEntryScreen ses : stationEntryScreenList) {
				QiEntryScreenId key = new QiEntryScreenId(ses.getEntryScreen(), entryModel, (short) 1);
				QiEntryScreen entryScreen = getDao(QiEntryScreenDao.class).findByKey(key);
				if(entryScreen != null) {
					String entryScreenName = entryScreen.getId().getEntryScreen(); 
					if (entryScreen.isImage()) {
						result.addAll(getDao(QiLocalDefectCombinationDao.class).findAllPartDefectCombByDefectEntryImageFilter(entryScreen.getImageName(), entryScreenName, entryModel, "", ""));
					} else {
						result.addAll(getDao(QiLocalDefectCombinationDao.class).findAllPartDefectCombByDefectEntryTextFilter(entryScreenName, entryModel, "", ""));
					}
				}
				Logger.getLogger().info("Entry screen found: " + ses.getEntryScreen());
			}
		} catch (Exception e) {
			logStackTrace(e);
		}
		return result;
	}
	
	@Override
	public String createDefect(String productId, String defectCombinationId, String defectStatus, String processPointId, String associateId, String comment) {
		QiDefectResult defectResult = null;
		ProcessPoint processPoint = null;
		BaseProduct product;
		String productType = null;

		try {
			int status = Integer.parseInt(defectStatus);

			productType = getProductType(processPointId);
			if(StringUtils.isEmpty(productType)) {
				return NG + ": Could not find product type";
			}
			Logger.getLogger().info("Found product type: " + productType);
			
			if(StringUtils.isEmpty(productId) || (product = getProduct(productType, productId)) == null) {
				return NG + ": Unable to find product: " + productId;
			}

			if(associateId == null || StringUtils.isEmpty(associateId)) {
				return NG + ": Associate ID can not be NULL : " + associateId;
			}
			if(StringUtils.isEmpty(processPointId) || (processPoint = getProcessPoint(processPointId)) == null) {
				return NG + ": Invalid process point ID";
			}

			int id = Integer.parseInt(defectCombinationId);
			if(!isDefectCombinationIdValid(id, processPointId, productId)) {
				Logger.getLogger().error("Invalid defect combination ID: " + defectCombinationId);
				return NG + ": Invalid defect combination ID " + defectCombinationId;
			}
			QiLocalDefectCombination localDefectCombination = getDao(QiLocalDefectCombinationDao.class).findByKey(id);
			
			List<QiDefectResult> allDefects = findAllDefectsByProductId(productId);
			if(DefectStatus.NON_REPAIRABLE.getId() == status) {
				if(allDefects.stream().anyMatch(n -> DefectStatus.NON_REPAIRABLE.getId() == n.getCurrentDefectStatus())) {
					return NG + ": Product already scrapped";
				} else {
					defectResult = createDefectResult(product, localDefectCombination, status, processPoint, associateId.toUpperCase());
					if(defectResult != null) {
						defectResult.setComment(StringUtils.isEmpty(comment) ? DEFAULT_SCRAP_COMMENT : comment);
						Logger.getLogger().info("Defect created: " + defectResult);
						Logger.getLogger().info("Calling ScrapService");
						DataContainer result = ServiceFactory.getService(ScrapService.class).scrapProduct(createScrapDataContainer(defectResult));
						if(LineSideContainerValue.COMPLETE != result.get(TagNames.REQUEST_RESULT.name())) {
							return NG + ": Result from ScrapService: " + result.get(TagNames.MESSAGE.name());
						}
						Logger.getLogger().info("Finished calling ScrapService. Result: " + result);
						return OK;
					} else {
						return NG + ": Unable to scrap product " + product.getProductId();
					}
				}
			} else if(DefectStatus.NOT_FIXED.getId() != status && DefectStatus.FIXED.getId() != status) {
				return NG + ": Invalid defect status";
			} 
			
			if(allDefects.stream().anyMatch(n -> DefectStatus.NON_REPAIRABLE.getId() == n.getCurrentDefectStatus())) {
				if(DefectStatus.NOT_FIXED.getId() == status) {
					defectResult = createDefectResult(product, localDefectCombination, DefectStatus.NOT_FIXED_SCRAPPED.getId(), processPoint, associateId.toUpperCase());
					return checkResult(defectResult, product.getProductId());
				}
			}

			defectResult = createDefectResult(product, localDefectCombination, status, processPoint, associateId.toUpperCase());
			return checkResult(defectResult, product.getProductId());
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@Override
	public String repairDefect(String defectResultId, String productId, String repairProcessPointId, String defectStatus, String repairMethod,String associateId, String comment) {
		List<QiDefectResult> allDefects;
		QiDefectResult defectResult = null;
		
		if(StringUtils.isEmpty(defectResultId)) {
			return NG + ": Invalid defectresultId";
		} else if(StringUtils.isEmpty(repairProcessPointId)) {
			return NG + ": Invalid repair process point ID";
		} else if(StringUtils.isEmpty(productId)) {
			return NG + ": Invalid product ID";
		} 
		
		try {
			if(StringUtils.isEmpty(defectStatus) || DefectStatus.FIXED.getId() != Integer.parseInt(defectStatus)) {
				return NG + ": Invalid defect status";
			} 

			allDefects = findAllDefectsByProductId(productId);
			if(allDefects.stream().anyMatch(n -> DefectStatus.NON_REPAIRABLE.getId() == n.getCurrentDefectStatus())) {
				Logger.getLogger().warn("Product scrapped. Product ID: " + productId);
				return NG + ": Product is scrapped.";
			} 

			for(QiDefectResult defect : allDefects) {
				if(defect.getDefectResultId() == Long.parseLong(defectResultId)) {
					defectResult = defect;
					break;
				}
			}
			if(defectResult == null) {
				Logger.getLogger().warn("Defect does not exist: " + defectResultId + ", Product " + productId);
				return NG + ": Defect does not exist " + defectResultId + ", Product " + productId;
			} 

			Logger.getLogger().info("Defect found: " + defectResult.getDefectResultId() + " - defect status: "+ defectResult.getCurrentDefectStatus());		
			if(ArrayUtils.contains(REPAIRABLE_DEFECT_STATUS, defectResult.getCurrentDefectStatus())) {
				List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
				qiDefectResultList.add(defectResult);
				return repairDefects(qiDefectResultList, repairProcessPointId, Integer.parseInt(defectStatus), repairMethod, comment,associateId);
			} else {
				return NG + ": Cannot repair defect that has defect status other than 5 or 6";
			}
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@Override
	public String repairDefectByDefectType(String productId, String repairProcessPointId, String defectTypeName, String repairMethod,String associateId, String comment) {

		if(StringUtils.isEmpty(repairProcessPointId)) {
			return NG + ": Invalid repair process point ID";
		} else if(StringUtils.isEmpty(defectTypeName)) {
			return NG + ": Invalid defect type name";
		} else if(StringUtils.isEmpty(productId)) {
			return NG + ": Invalid product ID";
		}
		
		List<QiDefectResult> allDefects;
		try {
			allDefects = findAllDefectsByProductId(productId);
			if(allDefects.stream().anyMatch(n -> DefectStatus.NON_REPAIRABLE.getId() == n.getCurrentDefectStatus())) {
				Logger.getLogger().warn("Product is scrapped: " + productId);
				return NG + ": Product is scrapped.";
			} 
			
			List<QiDefectResult> toBeRepairedList = allDefects.stream().filter(n -> isDefectValid(n, defectTypeName)).collect(Collectors.toList());
			Logger.getLogger().info("Number of defects to be repaired: " + toBeRepairedList.size());
			
			if(toBeRepairedList.isEmpty()) {
				Logger.getLogger().info("No defect needs to be repaired");
				return NG + ": No outstanding defect found for " + defectTypeName;
			} 

			return repairDefects(toBeRepairedList, repairProcessPointId, DefectStatus.FIXED.getId(), repairMethod, comment,associateId);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@Override
	public List<String> getRepairMethodByProcessPoint(String processPointId) {
		try {
			List<QiRepairMethod> repairMethods = getDao(QiRepairMethodDao.class).findAllRepairMethodsByQicsStation(processPointId);
			return repairMethods.stream().filter(n -> QiActiveStatus.ACTIVE.getId() == n.getActive()).map(n -> n.getId().toString()).sorted().collect(Collectors.toList());
		} catch (Exception e) {
			logStackTrace(e);
			return new ArrayList<String>();
		}
	}

	@Override
	public String repairDefects(String productId, String processPointId, String repairProcessPointId, String repairMethod,String associateId, String comment) {

		if(StringUtils.isEmpty(repairProcessPointId)) {
			return NG + ": Invalid repair process point ID";
		} else if(StringUtils.isEmpty(processPointId)) {
			return NG + ": Invalid process point ID";
		} else if(StringUtils.isEmpty(productId)) {
			return NG + ": Invalid product ID";
		}
		
		List<QiDefectResult> allDefects;
		try {
			allDefects = findAllDefectsByProductId(productId);
			if(allDefects.stream().anyMatch(n -> DefectStatus.NON_REPAIRABLE.getId() == n.getCurrentDefectStatus())) {
				Logger.getLogger().warn("Product is scrapped: " + productId);
				return NG + ": Product is scrapped.";
			} 
			
			List<QiDefectResult> toBeRepairedList = allDefects.stream().filter(n -> ArrayUtils.contains(REPAIRABLE_DEFECT_STATUS, n.getCurrentDefectStatus()) 
					&& processPointId.equalsIgnoreCase(n.getApplicationId())).collect(Collectors.toList());
			Logger.getLogger().info("Number of defects to be repaired: " + toBeRepairedList.size());
			
			if(toBeRepairedList.isEmpty()) {
				Logger.getLogger().info("No defect needs to be repaired");
				return NG + ": Cannot repair defect that has defect status other than 5 or 6. Number of defect found: " + toBeRepairedList.size();
			} 

			return repairDefects(toBeRepairedList, repairProcessPointId, DefectStatus.FIXED.getId(), repairMethod, comment,associateId);
			
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	
	
/*
 *	========== Private methods ===========
 */
		
	private List<QiDefectResult> findAllDefectsByProductId(String productId) {
		List<QiDefectResult> qiDefectResultList = getDao(QiDefectResultDao.class).findAllByProductId(productId);
		Logger.getLogger().info(qiDefectResultList.size() + " defects found for " + productId);
		return qiDefectResultList; 
	}
		

	private boolean isDefectCombinationIdValid(int localDefectCombinationId, String processPointId, String productId) {
		List<QiDefectResultDto> dtoList = getDefectCombination(processPointId, productId);
		return dtoList.stream().filter(n -> n.getLocalDefectCombinationId() == localDefectCombinationId).collect(Collectors.toList()).size() > 0;
	}
	
	private List<QiDefectResultDto> getDefectCombination(String processPointId, String productId) {

		String productType;
		ProductTypeUtil util;
		if((productType = getProductType(processPointId)) == null || (util = getProductTypeUtil(productType)) == null) {
			return new ArrayList<QiDefectResultDto>();
		}

		BaseProduct product = util.getProductDao().findByKey(productId);
		if(product == null || StringUtils.isEmpty(processPointId)) {
			return new ArrayList<QiDefectResultDto>();
		}
		
		BaseProductSpec spec = util.getProductSpecDao().findByProductSpecCode(product.getProductSpecCode(), productType);
				
		Logger.getLogger().info("ProductType: " + productType + " === ProductSpecCode: " + product.getProductSpecCode());
		Logger.getLogger().info("ProductSpecCode: " + spec);
		
		return getPdcByPPAndYearModel(processPointId, getMtcModelCode(product, productType, spec));
	}

	private String getMtcModelCode(BaseProduct product, String productTypeName, BaseProductSpec spec) {
		ProductType productType = ProductType.getType(productTypeName);
		if(ProductTypeUtil.isMbpnProduct(productType)) {
			Mbpn mbpn = (Mbpn) spec;
			return mbpn.getClassNo();
		} else {
			ProductSpec productSpec = (ProductSpec) spec;
			return ProductTypeUtil.isDieCast(productType) ? product.getProductSpecCode() : productSpec.getModelYearCode() + productSpec.getModelCode(); 
		}
	}
	
	private QiDefectResult createDefectResult(BaseProduct product, QiLocalDefectCombination localDefectCombination, int defectStatus, ProcessPoint processPoint, String userId) {

		QiDefectResult defectResult = new QiDefectResult();
		
		QiPartDefectCombination partDefectCombination = getDao(QiPartDefectCombinationDao.class).findByKey(localDefectCombination.getRegionalDefectCombinationId());
		if(partDefectCombination != null) {
			defectResult.setDefectTypeName(partDefectCombination.getDefectTypeName());
			defectResult.setDefectTypeName2(partDefectCombination.getDefectTypeName2());
			defectResult.setReportable(partDefectCombination.getReportable());
			defectResult.setThemeName(partDefectCombination.getThemeName());

			QiPartLocationCombination partLocationCombination = getDao(QiPartLocationCombinationDao.class).findByKey(partDefectCombination.getPartLocationId());
			if(partLocationCombination != null) {
				defectResult.setInspectionPartName(partLocationCombination.getInspectionPartName());
				defectResult.setInspectionPartLocationName(partLocationCombination.getInspectionPartLocationName());
				defectResult.setInspectionPartLocation2Name(partLocationCombination.getInspectionPartLocation2Name());
				defectResult.setInspectionPart2Name(partLocationCombination.getInspectionPart2Name());
				defectResult.setInspectionPart2LocationName(partLocationCombination.getInspectionPart2LocationName());
				defectResult.setInspectionPart2Location2Name(partLocationCombination.getInspectionPart2Location2Name());
				defectResult.setInspectionPart3Name(partLocationCombination.getInspectionPart3Name());
			}
			
			QiIqs iqs = getDao(QiIqsDao.class).findByKey(partDefectCombination.getIqsId());
			if(iqs != null) {
				defectResult.setIqsVersion(iqs.getIqsVersion());
				defectResult.setIqsCategoryName(iqs.getIqsCategory());
				defectResult.setIqsQuestionNo(iqs.getIqsQuestionNo());
				defectResult.setIqsQuestion(iqs.getIqsQuestion());
			}
		} else {
			Logger.getLogger().error("Unable to find partDefectCombination: " + localDefectCombination.getRegionalDefectCombinationId());
			return null;
		}

		defectResult.setApplicationId(processPoint.getProcessPointId());
		defectResult.setProductId(product.getProductId());

		defectResult.setCreateUser(getUserId(userId));
		defectResult.setEntrySiteName(localDefectCombination.getEntrySiteName());
		defectResult.setEntryPlantName(localDefectCombination.getEntryPlantName());
		defectResult.setEntryProdLineNo(getEntryProdLineNo(processPoint));   
		defectResult.setEntryDept(processPoint.getDivisionId());
		defectResult.setEntryScreen(localDefectCombination.getEntryScreen());
		defectResult.setEntryModel(localDefectCombination.getEntryModel());

		defectResult.setProductSpecCode(product.getProductSpecCode());		
		defectResult.setOriginalDefectStatus((short) DefectStatus.NOT_REPAIRED.getId());
		defectResult.setCurrentDefectStatus((short) defectStatus);
		defectResult.setProductType(product.getProductType().name());

		QiResponsibleLevel responsibleLevel = getDao(QiResponsibleLevelDao.class).findByKey(localDefectCombination.getResponsibleLevelId());
		if(responsibleLevel != null) {
			responsibleLevel.getLevel();
			defectResult.setResponsibleSite(responsibleLevel.getSite());
			defectResult.setResponsiblePlant(responsibleLevel.getPlant());
			defectResult.setResponsibleDept(responsibleLevel.getDepartment());
			defectResult.setResponsibleLevel1(responsibleLevel.getResponsibleLevelName());
		}

		QiPddaResponsibility pddaResponsibility = getDao(QiPddaResponsibilityDao.class).findByKey(localDefectCombination.getPddaResponsibilityId());
		if(pddaResponsibility != null) {
			defectResult.setProcessNo(pddaResponsibility.getProcessName());
			defectResult.setProcessName(pddaResponsibility.getProcessName());
			defectResult.setUnitNo(pddaResponsibility.getUnitNumber());
			defectResult.setUnitDesc(pddaResponsibility.getUnitDescription());
		}
		
		QiStationWriteUpDepartment writeUpDept = getDao(QiStationWriteUpDepartmentDao.class).findDefaultWriteUpDeptByProcessPoint(processPoint.getProcessPointId());
		if(writeUpDept != null) {
			defectResult.setWriteUpDept(writeUpDept.getId().getDivisionId());
		}
		
		defectResult.setReportable(localDefectCombination.getReportable());
		defectResult.setRepairArea(localDefectCombination.getRepairAreaName());
		defectResult.setRepairMethodNamePlan(localDefectCombination.getRepairMethod());
		defectResult.setRepairTimePlan(localDefectCombination.getRepairMethodTime());
		
		defectResult.setLocalTheme(localDefectCombination.getLocalTheme());
		defectResult.setDefectCategoryName(localDefectCombination.getDefectCategoryName());
		defectResult.setTerminalName(getTerminalName(processPoint.getProcessPointId()));
		defectResult.setProductionDate(getProductionDate(processPoint));

		defectResult.setCreateUser(getUserId(userId));
		Logger.getLogger().info("New defect: " + defectResult.toString());		
		defectResult.setDefectTransactionGroupId(getDao(QiDefectResultDao.class).getNextDefectTransactionGrounpId(defectResult));
			
		return getDao(QiDefectResultDao.class).createQiDefectResult(defectResult, null);
	}

	private String repairDefects(List<QiDefectResult> qiDefectResultList, String repairProcessPointId, int defectStatus, String repairMethod, String comment, String associateId) {
		ProcessPoint repairProcessPoint = getDao(ProcessPointDao.class).findByKey(repairProcessPointId);
		if(repairProcessPoint == null) {
			Logger.getLogger().error("Invalid repair process point ID: " + repairProcessPointId);
			return NG + ": Invalid repair process point ID " + repairProcessPointId;
		}
		
		if(!StringUtils.isEmpty(repairMethod) 
				&& !repairMethod.trim().equals(DEFAULT_REPAIR_METHOD)) {
			List<QiRepairMethod> qiRepairMethod = getDao(QiRepairMethodDao.class).findFilteredRepairMethods(repairMethod);
			if(qiRepairMethod.isEmpty()) {
				return NG+": Invalid Repair Method.";
			}
		}
		
		List<QiDefectResult> finalResultList = new ArrayList<QiDefectResult>();
		List<QiRepairResult> finalRepairList = new ArrayList<QiRepairResult>();
		
		for(QiDefectResult defectResult : qiDefectResultList){
			Logger.getLogger().info("Processing defect: " + defectResult);
		
			defectResult.setCurrentDefectStatus((short) defectStatus);
			defectResult.setUpdateUser(DEFAULT_USER_ID);
			
			List<QiRepairResult> repairResultList = getDao(QiRepairResultDao.class).findAllByDefectResultId(defectResult.getDefectResultId());				
			if(repairResultList.isEmpty()) {
				QiRepairResult repairResult = new QiRepairResult(defectResult);
				String site = repairProcessPoint.getSiteName();
				String plant = repairProcessPoint.getPlantName();
				short lineNo = getEntryProdLineNo(repairProcessPoint);
				repairResult.setEntrySiteName(site);
				repairResult.setEntryPlantName(plant);
				repairResult.setEntryProdLineNo(lineNo);
				repairResult.setEntryDept(repairProcessPoint.getDivisionId());
				repairResult.setApplicationId(repairProcessPointId);
				repairResult.setTerminalName(getTerminalName(repairProcessPointId));
				repairResult.setActualProblemSeq((short) 1);
				repairResult.setProductionDate(getProductionDate(repairProcessPoint));
				repairResult.setComment(StringUtils.isEmpty(comment) ? getDefaultRepairComment(repairProcessPointId) : comment);
				repairResult.setCreateUser((associateId==null ||associateId.trim().isEmpty())?DEFAULT_USER_ID:associateId.trim().toUpperCase());
				finalRepairList.add(repairResult);
			} else {
				for(QiRepairResult repairResult : repairResultList) {
					repairResult.setCurrentDefectStatus((short) defectStatus);
					repairResult.setUpdateUser(DEFAULT_USER_ID);
				}
				finalRepairList.addAll(repairResultList);
			}
			finalResultList.add(defectResult);
		}

		Logger.getLogger().info("Saving repair results: " + finalRepairList.toString());
		List<QiRepairResult> savedRepairResultList = getDao(QiRepairResultDao.class).createRepairResults(finalRepairList);
		
		Logger.getLogger().info("Creating repair methods" + savedRepairResultList.toString());
		createRepairMethod(savedRepairResultList, repairProcessPointId, repairMethod, comment);
		
		Logger.getLogger().info("Updating all defect results: " + finalResultList.toString());
		getDao(QiDefectResultDao.class).updateAll(finalResultList);

		return OK;
	}

	private List<QiAppliedRepairMethod> createRepairMethod(List<QiRepairResult> savedResultList, String processPointId, String repairMethod, String comment) {
		List<QiAppliedRepairMethod> finalRepairMethodList = new ArrayList<QiAppliedRepairMethod>();
		for(QiRepairResult savedResult : savedResultList){
			Integer count = getDao(QiAppliedRepairMethodDao.class).findCurrentSequence(savedResult.getRepairId());
			count = count == null ? 0 : count;
			count++;
			QiAppliedRepairMethod qiAppliedRepairMethod = new QiAppliedRepairMethod();
			QiAppliedRepairMethodId id = new QiAppliedRepairMethodId();
			qiAppliedRepairMethod.setId(id);
			qiAppliedRepairMethod.getId().setRepairMethodSeq(count);
			qiAppliedRepairMethod.getId().setRepairId(savedResult.getRepairId());
			qiAppliedRepairMethod.setRepairMethod(StringUtils.isEmpty(repairMethod) ? DEFAULT_REPAIR_METHOD : repairMethod);
			qiAppliedRepairMethod.setApplicationId(processPointId);
			qiAppliedRepairMethod.setCreateUser(DEFAULT_USER_ID);
			qiAppliedRepairMethod.setRepairTime(1);
			qiAppliedRepairMethod.setComment(StringUtils.isEmpty(comment) ? getDefaultRepairComment(processPointId) : comment);
			qiAppliedRepairMethod.setIsCompletelyFixed((savedResult.getCurrentDefectStatus() == (short) DefectStatus.FIXED.getId()) ? 1 : 0);
			finalRepairMethodList.add(qiAppliedRepairMethod);
		}
		Logger.getLogger().info("Saving all repair methods: " + finalRepairMethodList.toString());
		return getDao(QiAppliedRepairMethodDao.class).saveAllRepairMethods(finalRepairMethodList, processPointId);
	}
	
	private short getEntryProdLineNo(ProcessPoint processPoint) {
        try {
            GpcsDivision gpcs = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
	        return gpcs != null ? Short.parseShort(gpcs.getGpcsLineNo()) : DEFAULT_PROD_LINE_NO;
        } catch (Exception e) {
        	Logger.getLogger().error("Unable to get production Line No: " + e.getMessage());
        	return DEFAULT_PROD_LINE_NO;
        }
	}

	private Date getProductionDate(ProcessPoint processPoint) {
		DailyDepartmentScheduleUtil util = new DailyDepartmentScheduleUtil(processPoint);
		DailyDepartmentSchedule schedule = util.getCurrentSchedule();
		return schedule == null ? null : schedule.getId().getProductionDate();
	}
	
	private String getTerminalName(String processPointId) {
		Terminal terminal = getDao(TerminalDao.class).findFirstByProcessPointId(processPointId);
		return terminal == null ? processPointId : terminal.getHostName();
	}

	private String getProductType(String processPointId) {
		ApplicationPropertyBean propertyBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, processPointId);
		return (propertyBean == null || propertyBean.getProductType() == null) ? null : propertyBean.getProductType();
	}

	private boolean isDefectValid(QiDefectResult defectResult, String defectTypeName) {
		return defectTypeName.equalsIgnoreCase(defectResult.getDefectTypeName()) && ArrayUtils.contains(REPAIRABLE_DEFECT_STATUS, defectResult.getCurrentDefectStatus());
	}
	
	private String getUserId(String userId) {
		return StringUtils.isEmpty(userId) ? DEFAULT_USER_ID : userId;
	}

	private ProductTypeUtil getProductTypeUtil(String productType) {
		ProductTypeUtil util = ProductTypeUtil.getTypeUtil(productType);
		if(util == null) {
			Logger.getLogger().error("Invalid product type: " + productType);
		}
		return util;
	}
	
	private BaseProduct getProduct(String productType, String productId) {
		BaseProduct product = null;
		ProductTypeUtil util = getProductTypeUtil(productType);
		if(util == null || (product = (BaseProduct) util.findProduct(productId)) == null) {
			Logger.getLogger().error("Unable to find product: " + productId);
			return null;
		} else {
			Logger.getLogger().info("Product found: " + product.toString());
			return product;
		}
	}
	
	private ProcessPoint getProcessPoint(String processPointId) {
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(processPointId);
		if(processPoint == null) {
			Logger.getLogger().error("Unable to find process point: " + processPointId);
			return null;
		} else {
			return processPoint;
		}
	}
	
	private String getDefaultRepairComment(String processPointId) {
		return REPAIR_COMMENT_PREFIX + processPointId;
	}
	
	private String handleException(Exception e) {
		logStackTrace(e);
		String message = NG + " - Exception: " + e.getMessage();
		return message.length() > 256 ? message.substring(0, 255) : message;
	}
	
	private void logStackTrace(Exception e) {
		StringBuilder builder = new StringBuilder();
		builder.append("Exception: ");
		for(StackTraceElement element : e.getStackTrace()) {
			builder.append(element.toString());
			builder.append('\n');
		}
		Logger.getLogger().error(builder.toString());
	}
	
	private String checkResult(QiDefectResult defectResult, String productId) {
		if(defectResult == null) {
			Logger.getLogger().error("Unable to create defect for " + productId);
			return NG + ": Unable to create defect for " + productId;
		} else {
			Logger.getLogger().info("Defect was saved: " + defectResult.toString());
			return OK;
		}
	}
	
	private DefaultDataContainer createScrapDataContainer(QiDefectResult defectResult) {
		List<String> productIdList = new ArrayList<String> ();
		productIdList.add(defectResult.getProductId());
		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.PRODUCT_ID.name(), productIdList);
		requestDc.put(TagNames.APPLICATION_ID.name(),defectResult.getApplicationId());
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), defectResult.getApplicationId());
		requestDc.put(TagNames.PRODUCT_TYPE.name(), defectResult.getProductType());
		requestDc.put(TagNames.PROCESS_LOCATION.name(), defectResult.getInspectionPartLocationName());
		requestDc.put(TagNames.REASON.name(), defectResult.getDefectTypeName());
		requestDc.put(TagNames.CURRENT_DATE.name(), defectResult.getProductionDate() == null ? null : new java.sql.Date(defectResult.getProductionDate().getTime()));
		requestDc.put(TagNames.COMMENT.name(), defectResult.getComment());
		requestDc.put(TagNames.ASSOCIATE_ID.name(), defectResult.getCreateUser());
		Logger.getLogger().info("Scrap request DC: " + requestDc.toString());
		return requestDc;
	}
}
