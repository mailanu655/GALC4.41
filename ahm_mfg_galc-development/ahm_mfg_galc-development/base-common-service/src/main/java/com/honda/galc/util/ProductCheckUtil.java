package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.util.ProductCheckType.INSTALLED_PARTS_BY_PROCESS_POINTS_STATUS_CHECK;
import static com.honda.galc.util.ProductCheckType.OUTSTANDING_PARTS_BY_PROCESS_POINTS_CHECK;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.kernel.DelegatingResultList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameEngineModelMapDao;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.HeadHistoryDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.dao.product.KickoutLocationDao;
import com.honda.galc.dao.product.LetPartCheckSpecDao;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.RuleExclusionDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.InstalledPartDetail;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.data.MCInstalledPartDetailDto;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.YMTOCI;
import com.honda.galc.dto.ExtProductCheckDto;
import com.honda.galc.dto.ExtProductStatusDto;
import com.honda.galc.dto.InstalledPartDetailsDto;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.dto.RequiredLetPartSpecDetailsDto;
import com.honda.galc.dto.SubAssemblyPartListDto;
import com.honda.galc.dto.lcvinbom.LetCategoryCodeDto;
import com.honda.galc.dto.lcvinbom.PmqaDto;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.KickoutStatus;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.ExtRequiredPartSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadHistory;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Kickout;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.entity.product.RuleExclusion;
import com.honda.galc.entity.product.RuleExclusionId;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.net.HttpClient;
import com.honda.galc.property.BaseLinePropertyBean;
import com.honda.galc.property.EngineLinePropertyBean;
import com.honda.galc.property.ExpectedProductPropertyBean;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.property.MissionLinePropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.InstalledPartService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductHoldService;
import com.honda.galc.service.lcvinbom.VinBomService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.check.PartResultData;

/**
 * 
 * <h3>ProductCheckUtil Class description</h3>
 * <p> ProductCheckUtil description </p>
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
 * @author Jeffray Huang<br>
 * Apr 6, 2011
 *
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 */
public class ProductCheckUtil {
	
	private BaseProduct product;
	private ProcessPoint processPoint;
	private ProductTypeData productTypeData;															 
	private List<String> installedPartNames;
	private Device device;
	private Map<Object, Object> context = null;
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORIZATION_METHOD = "Basic";
	private static final String NONE_FOUND = "None Found";
	private static final String MISSING_PART = "Missing";
	private static final String BLANK = "";
	private static final String SEQUENCE_NAME = "SEQUENCE_NAME";
	private boolean overallPartsStatus = true;
	private static final String LET_CATEGORY_NAMES = "LET_CATEGORY_NAMES";
	private static Map<String, List<PreviousLine>> previousLineMap;
	private Map<String, List<String>> installedProductNameMap;
	private static final String BEV_BUILD_ATTRIBUTE="BEV_MODEL";
	
	enum SpecCheckProdSequenceType{InProcessProduct, ProductSequence, ExpectedProduct};
	
	public ProductCheckUtil() {
		
	}
	
	public ProductCheckUtil(BaseProduct product, ProcessPoint processPoint) {
		this.product = product;
		this.processPoint = processPoint;
	}
	
	public ProductCheckUtil(BaseProduct product, ProcessPoint processPoint, List<String> partNames) {
		this(product,processPoint);
		this.setInstalledPartNames(partNames);
	}
	
	public ProductCheckUtil(Map<Object, Object> context) {
		this.context = context;
	}

	public BaseProduct getProduct() {
		return product;
	}
	public void setProduct(BaseProduct product) {
		this.product = product;
	}
	public ProcessPoint getProcessPoint() {
		return processPoint;
	}
	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}
	
	public ProductTypeData getProductTypeData() {
		if(productTypeData == null) {
			productTypeData = getDao(ProductTypeDao.class).findByKey(product.getProductType().toString());
		}
		return productTypeData;
	}

	public void setProductTypeData(ProductTypeData productTypeData) {
		this.productTypeData = productTypeData;
	}

	public List<String> getInstalledPartNames() {
		if(installedPartNames == null) installedPartNames = new ArrayList<String>();
		return installedPartNames;
	}
	
	public void setInstalledPartNames(List<String> installedPartNames) {
		this.installedPartNames = installedPartNames;
	}
		
	public List<String> outstandingDefectsCheck() {
		
		List<String> defects = new ArrayList<String>();
		
		try {
			
			List<DefectResult> defectResults = getDao(DefectResultDao.class).findAllByProductId(product.getProductId());

			for (DefectResult defect : defectResults) {
				if(defect.isOutstandingStatus() || defect.isScrapStatus())
					defects.add(defect.toShortString());
			}

		} catch (Exception e) {
		}
		return defects;
	}
	
	public boolean outstandingDefectsNotExistCheck() {
		return outstandingDefectsCheck().isEmpty();
	}
	
	public boolean outstandingDefectStatusCheck() {
		return product == null ? true: product.isOutstandingStatus();
	}
	
	public boolean kickoutExistCheck() {
		boolean isRecursiveKickout = PropertyService.getPropertyBean(SystemPropertyBean.class, getProcessPoint().getProcessPointId()).isRecursiveKickout();
		List<Kickout> kickouts = new ArrayList<Kickout>();
		if(isRecursiveKickout) {
			kickouts =  ServiceFactory.getDao(KickoutDao.class).findAllActiveByProductId(product.getProductId());
		} else {
			kickouts = ServiceFactory.getDao(KickoutDao.class).findAllActiveByProductIdAndProcessPoint(product.getProductId(), processPoint.getProcessPointId(), processPoint.getSequenceNumber());
		}
		return !kickouts.isEmpty();
	}
	
	public boolean qiDefectExistCheck() {
		List<String> qiDefectResults = new ArrayList<String>();
			qiDefectResults = qiDefectCheck();
		return !qiDefectResults.isEmpty();
	}
	
	public List<String> qiDefectCheck() {
		List<QiDefectResult> qiDefectResults = new ArrayList<QiDefectResult>();
		boolean isKickoutDefect = getPropertyBean().isKickoutDefect();
		List<String> defectResultData=new ArrayList<String>();
		if(isKickoutDefect) {
			qiDefectResults = getDao(QiDefectResultDao.class).findAllNotRepairedDefects(product.getProductId());
		} else {
			qiDefectResults = ServiceFactory.getDao(QiDefectResultDao.class).findAllNotRepairedNotKickoutDefects(product.getProductId());
		}
		if(qiDefectResults.size()>0){
			for(QiDefectResult defectResult:qiDefectResults){
				String dataToDisplay=defectResult.getInspectionPartName()+" "+defectResult.getInspectionPartLocationName()+" "+
						defectResult.getInspectionPartLocation2Name()+" "+defectResult.getInspectionPart2Name()+" "+
						defectResult.getInspectionPart2LocationName()+" "+defectResult.getInspectionPartLocation2Name()+" "+
						defectResult.getInspectionPart3Name()+" "+defectResult.getDefectTypeName()+" "+defectResult.getDefectTypeName2().replaceAll("null", "").replaceAll("\\s+"," ");
				if(defectResult.getKickoutId() > 0) {
					String processPointName = ServiceFactory.getDao(KickoutLocationDao.class).findProcessPointNameForKickout(defectResult.getKickoutId());
					if(processPointName != null) {
						dataToDisplay = dataToDisplay.concat(" Kickout Process Point: " + processPointName);
					}
				}
				defectResultData.add(dataToDisplay);
			}
		}
		return defectResultData;
	}
	
	public List<String> qiKickoutCheck() {
		List<QiDefectResult> qiDefectResults = new ArrayList<QiDefectResult>();
		qiDefectResults = ServiceFactory.getDao(QiDefectResultDao.class).findAllNotRepairedKickoutDefects(product.getProductId());
		List<String> defectResultData=new ArrayList<String>();
		
		if(qiDefectResults.size() > 0){
			for(QiDefectResult defectResult:qiDefectResults)  {
				StringBuilder sb = new StringBuilder();
				sb.append(defectResult.getInspectionPartName()).append(" ").append(defectResult.getInspectionPartLocationName()).append(" ")
				.append(defectResult.getInspectionPartLocation2Name()).append(" ").append(defectResult.getInspectionPart2Name()).append(" ")
				.append(defectResult.getInspectionPart2LocationName()).append(" ").append(defectResult.getInspectionPartLocation2Name()).append(" ")
				.append(defectResult.getInspectionPart3Name()).append(" ").append(defectResult.getDefectTypeName()).append(" ")
				.append(defectResult.getInspectionPart3Name()).append(" ").append(defectResult.getDefectTypeName2().replaceAll("null", "").replaceAll("\\s+"," "))				
				;

				if(defectResult.getKickoutId() > 0) {
					String processPointName = ServiceFactory.getDao(KickoutLocationDao.class).findProcessPointNameForKickout(defectResult.getKickoutId());
					if(processPointName != null) {
						sb.append(" Kickout Process Point: " ).append(processPointName);
					}
					Kickout kick = ServiceFactory.getDao(KickoutDao.class).findByKey(defectResult.getKickoutId());
					if(kick != null && kick.getKickoutUser() != null)  {
						sb.append(" - ").append(kick.getKickoutUser());
					}
				}
				defectResultData.add(sb.toString());
			}
		}
		return defectResultData;
	}
	
	public List<String> kickoutCheck() {
		List<String> kickoutResultData = new ArrayList<String>();
		List<KickoutDto> kickoutDtoList = ServiceFactory.getDao(KickoutDao.class).findActiveKickoutInfoByProductId(product.getProductId());
		if(kickoutDtoList != null && !kickoutDtoList.isEmpty()) {
			for(KickoutDto kickoutDto : kickoutDtoList) {
				Division division = getDivision(kickoutDto.getDivisionId());
				Line line = getLine(kickoutDto.getLineId());
				ProcessPoint processPoint = getProcessPoint(kickoutDto.getProcessPointId());
				
				kickoutDto.setDivisionName(division == null ? "No Department" : division.getDivisionName());
				kickoutDto.setLineName(line == null ? "No Line" : line.getLineName());
				kickoutDto.setProcessPointName(processPoint == null ? "No Process Point" : processPoint.getProcessPointName());
				
				StringBuilder sb = new StringBuilder();
				sb.append(kickoutDto.getDescription()).append(" ")
				.append(kickoutDto.getComment()).append(" ")
				.append(KickoutStatus.getType(kickoutDto.getKickoutStatus()).getName()).append(" ")
				.append(kickoutDto.getDivisionName()).append(" ")
				.append(kickoutDto.getLineName()).append(" ")
				.append(kickoutDto.getProcessPointName()).append(" ")
				.append(" - ").append(kickoutDto.getKickoutUser());

				kickoutResultData.add(sb.toString());
			}
		}
		return kickoutResultData;
	}
	
	public Map<String, Object> kickoutLocationCheck() {
		Map<String, Object> results = new HashMap<String, Object>();
		List<KickoutDto> kickoutDtoList = ServiceFactory.getDao(KickoutDao.class).findActiveKickoutInfoByProductId(product.getProductId());

		if(kickoutDtoList != null && !kickoutDtoList.isEmpty()) {
			KickoutDto kickoutDto = getFirstSequenceKickout(kickoutDtoList);
			if(kickoutDto !=null) {
				results.put(TagNames.KICKOUT_FLAG.name(), true);
				results.put(TagNames.KICKOUT_DIVISION_SEQUENCE.name(), kickoutDto.getDivisionSequence());
				results.put(TagNames.KICKOUT_LINE_SEQUENCE.name(), kickoutDto.getLineSequence());
				results.put(TagNames.KICKOUT_PROCESS_POINT_SEQUENCE.name(), kickoutDto.getProcessPointSequence());
			}
			else {
				results.put(TagNames.KICKOUT_FLAG.name(), false);
			}
		}
		return results;
	}

	public boolean qiNonRepairableDefectCheck() {
		List<QiDefectResult> nonRepairableDefects = ServiceFactory.getDao(QiDefectResultDao.class)
				.findAllByProductIdAndCurrentDefectStatus(product.getProductId(), (short) DefectStatus.NON_REPAIRABLE.getId());
		return !nonRepairableDefects.isEmpty();
	}

	private KickoutDto getFirstSequenceKickout(List<KickoutDto> kickoutDtoList) {
		KickoutDto kickoutDto = null;
		for(KickoutDto currentKickoutDto : kickoutDtoList) {
			Division division = getDivision(currentKickoutDto.getDivisionId());
			Line line = getLine(currentKickoutDto.getLineId());
			ProcessPoint processPoint = getProcessPoint(currentKickoutDto.getProcessPointId());

			currentKickoutDto.setDivisionSequence(division == null ? 0 : division.getSequenceNumber());
			currentKickoutDto.setLineSequence(line == null ? 0 : line.getLineSequenceNumber());
			currentKickoutDto.setProcessPointSequence(processPoint == null ? 0 : processPoint.getSequenceNumber());
			
			if(kickoutDto == null) {
				kickoutDto = currentKickoutDto;
			}else if(currentKickoutDto.getDivisionSequence() < kickoutDto.getDivisionSequence()) {
				kickoutDto = currentKickoutDto;
			}else if(currentKickoutDto.getLineSequence() < kickoutDto.getLineSequence()) {
				kickoutDto = currentKickoutDto;
			}else if(currentKickoutDto.getProcessPointSequence() < kickoutDto.getProcessPointSequence()) {
				kickoutDto = currentKickoutDto;
			}
		}
		return kickoutDto;
	}

	public List<String> defectsExistCheck() {
		List<String> defects = new ArrayList<String>();
		Map<String, String[]> defectTypeMap = getPropertyBean().getDefectsExistCheckDefectTypes(String[].class);
		String[] propArray = getMappedProperty(defectTypeMap);
		List<String> defectTypes = null;
		if (propArray != null) {
			defectTypes = Arrays.asList(propArray);
		}
		List<DefectResult> defectResults = getDao(DefectResultDao.class).findAllByProductId(product.getProductId());
		for (DefectResult defect : defectResults) {
			if (DefectStatus.DIRECT_PASS.getId() == defect.getDefectStatusValue()) {
				continue;
			}
			if (defectTypes == null) {
				defects.add(defect.toShortString());
			} else if (defectTypes.contains(defect.getDefectTypeName())) {
				defects.add(defect.toShortString());
			}
		}
		return defects;
	}
	
	public boolean engineRepairedDefectStatusCheck() {
		
		return !outstandingDefectStatusCheck();
		
	}
	
	public List<String> headMarriedCheck() {
		
		Head head = (Head) product;
		List<String> messages = new ArrayList<String>();
		String ein = head.getEngineSerialNumber();
		if (!StringUtils.isEmpty(ein)) {
			messages.add("Married to engine: " + ein);
		}
		
		String partName = PropertyService.getProperty("prop_EngineHeadMarriage", "HEAD_PART_NAME");
		
		List<InstalledPart> installedParts = 
			getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(partName, head.getHeadId());
		
		for(InstalledPart item : installedParts) {
			messages.add("Installed to engine: " + item.getProductId() + " , part name: " + item.getPartName());
		}
		
		return messages;
	}
	
	public List<String> engineHeadNotMarriedCheck() {
		
		List<Head> heads = findAllHeads(product.getProductId());
		return engineHeadNotMarriedCheck(heads);
		
	}
	
	private List<String> engineHeadNotMarriedCheck(List<Head> heads) {
		List<String> messages = new ArrayList<String>();
		if (heads.isEmpty()) {
			messages.add("Missing Head marriage");
		} else if (heads.size() > 1) {
			StringBuilder builder = new StringBuilder();
			builder.append("Multiple Heads : ");
			for (Head head : heads) {
				builder.append("[").append(head.getId()).append("]");
			}
			messages.add(builder.toString());
		}
		
		return messages;
	}
	
	public List<String> engineBlockNotMarriedCheck() {
		List<Block> blocks = findAllBlocks(product.getProductId());
		return engineBlockNotMarriedCheck(blocks);
	}

	private List<String> engineBlockNotMarriedCheck(List<Block> blocks) {
		List<String> messages = new ArrayList<String>();
		if (blocks.isEmpty()) {
			messages.add("Missing Block marriage");
		} else if (blocks.size() > 1) {
			StringBuilder builder = new StringBuilder();
			builder.append("Multiple Blocks : ");
			for (Block block : blocks) {
				builder.append("[").append(block.getId()).append("]");
			}
			messages.add(builder.toString());
		}
		return messages;
	}
	
	public List<String> engineHeadOutstandingDefectsCheck(){
		
		List<Head> heads = findAllHeads(product.getProductId());
		List<String> messages =  engineHeadNotMarriedCheck(heads);
		
		if (messages.isEmpty()) {
			setProduct( heads.get(0));
			List<String> defects = outstandingDefectsCheck();
			List<String> defectsResults = new ArrayList<String>();
			for (String defect : defects) {
				StringBuilder builder = new StringBuilder();
				builder.append(defect).append(" [").append(product.getProductId()).append("]");
				defectsResults.add(builder.toString());
			}
			return defectsResults;
		} else {
			messages.set(0, "Failed to perform check - " + messages.get(0));
			return messages;
		}
	}
	
	public List<String> engineBlockOutstandingDefectsCheck(){
			
			List<Block> blocks = findAllBlocks(product.getProductId());
			List<String> messages =  engineBlockNotMarriedCheck(blocks);
			
			if (messages.isEmpty()) {
				setProduct( blocks.get(0));
				List<String> defects = outstandingDefectsCheck();
				List<String> defectsResults = new ArrayList<String>();
				for (String defect : defects) {
					StringBuilder builder = new StringBuilder();
					builder.append(defect).append(" [").append(product.getProductId()).append("]");
					defectsResults.add(builder.toString());
				}
				return defectsResults;
			} else {
				messages.set(0, "Failed to perform check - " + messages.get(0));
				return messages;
			}
		}
	
	public boolean engineBhOutstandingDefectStatusCheck() {
		
		boolean engineOutstandingDefectStatus = outstandingDefectStatusCheck();
		boolean blockOutstandingDefectStatus = true;
		boolean headOutstandingDefectStatus = true;

		Block block = findBlock(product.getProductId());
		Head head = findHead(product.getProductId());
		if(block!=null){
			setProduct(block);
			blockOutstandingDefectStatus = outstandingDefectStatusCheck();
		};
		if(head!=null){
			setProduct(head);
			headOutstandingDefectStatus = outstandingDefectStatusCheck();
		};
		
		return engineOutstandingDefectStatus || blockOutstandingDefectStatus || headOutstandingDefectStatus;
		
	}
	
	public boolean blockMcNumberDoesNotExistCheck() {
		
		Block block = (Block)product;
		return block == null || StringUtils.isEmpty(block.getMcSerialNumber());
		
	}
	
	public boolean headMcNumberDoesNotExistCheck() {
		
		Head head = (Head)product;
		return head == null || StringUtils.isEmpty(head.getMcSerialNumber());
		
	}
	
	public boolean mcNumberDoesNotExistCheck() {
		if (getProduct() instanceof DieCast) {
			DieCast dieCast = (DieCast) product;
			return StringUtils.isBlank(dieCast.getMcSerialNumber());
		}
		return true;
	}
	
	public Map<String, Boolean> installedPartsStatusCheck() {
		
		List<String> parts = fetchPartNames();
		
		Map<String, Boolean> results = new HashMap<String, Boolean>();

		for (String partName : parts) {
			boolean installedStatus = isPartInstalled(partName);
			results.put(partName, installedStatus);
		}

		return results;
		
	}
	
	//@KM
	public boolean isInstalledProductVerifyCheck() {
		return installedProductVerifyCheck().isEmpty(); 
	}

	public List<String> installedProductVerifyCheck() {
		List<String> overallCheckResults = new ArrayList<String>();
		BaseProduct installedProduct = null;
		String installedPartName = "";
		String partSN="";
		List<String> installedProductTypeList = PropertyService.getPropertyList(processPoint.getProcessPointId(), "INSTALLED_PRODUCT_TYPES");
		for (String installedProductType : installedProductTypeList) {
			try{
				if (installedProductType.equals(ProductType.ENGINE.name())) { //Engine is the child product.
					installedPartName = installedProductType;
					Frame frame = (Frame)ProductTypeUtil.getProductDao(getProduct().getProductType()).findBySn(product.getProductId());
		
					partSN = frame.getEngineSerialNo();				
					installedProduct = (Engine) ProductTypeUtil.getTypeUtil(installedProductType).getProductDao().findBySn(partSN);
					overallCheckResults.addAll(getInstalledProductCheckResults(installedProduct, installedProductType, installedPartName));
				} else { //Diecast part is the child product.
					List<String> installedPartNames = PropertyService.getPropertyList(processPoint.getProcessPointId(), TagNames.getDiecastMarriedPartName(ProductType.getType(installedProductType)).toString());
					for (String installedPartNameEntry  : installedPartNames) {
						installedPartName = installedPartNameEntry;
						ProductBuildResult buildResult = ProductTypeUtil.getProductBuildResultDao(getProduct().getProductType()).findById(getProduct().getProductId(), installedPartName);
						partSN = buildResult.getPartSerialNumber();
						
						DiecastDao installedProductDao = (DiecastDao) ProductTypeUtil.getProductDao(ProductType.getType(installedProductType));
						installedProduct = (DieCast) installedProductDao.findByMCDCNumber(partSN);
						overallCheckResults.addAll(getInstalledProductCheckResults(installedProduct, installedProductType, installedPartName));						
					}
				}
			}catch(Exception e){
				Logger.getLogger().warn("Could not perform check - installedProductVerifyCheck due to : " + e);
			}						
		}

		return overallCheckResults;
	}
	
	public List<String> getInstalledProductCheckResults(BaseProduct installedProduct, String installedProductType, String installedPartName){
		List<String> partCheckResults = new ArrayList<String>();
		if (installedProduct != null) {
			//Process point may be specific to installed part or installed product.  If neither is found use actual process point ID.
			String validInstalledPartName=installedPartName.trim().replaceAll(" ", "_");
			String installedProductLastCheckPoint = PropertyService.getProperty(processPoint.getProcessPointId(), "INSTALLED_" + validInstalledPartName + "_LAST_CHECK_POINT",
					PropertyService.getProperty(processPoint.getProcessPointId(), "INSTALLED_" + installedProductType + "_LAST_CHECK_POINT", processPoint.getProcessPointId()));
						
			String[] installedProductCheckTypes = PropertyService.getPropertyList(processPoint.getProcessPointId(), "INSTALLED_" + installedProductType + "_CHECK_TYPES").toArray(new String[0]);

			ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(installedProductLastCheckPoint);
				
			if (processPoint != null) {
				Map<String,Object> checkResults = check(installedProduct,processPoint,installedProductCheckTypes);

				for(Entry<String, Object> entry: checkResults.entrySet()){
					int idx = 0;
					StringBuilder sb = new StringBuilder();
					Object val=entry.getValue();
					if(val instanceof DelegatingResultList){
					for(String check : (List<String>) entry.getValue()){
						idx++;
						sb.append(idx);
						sb.append(") ");
						sb.append(check);
					    sb.append("\n");
					}
					}else{
						sb.append(val.toString());
					}
					
					partCheckResults.add(entry.getKey() + ": " + installedPartName + " - " + sb.toString());
				}
			} else {
				Logger.getLogger().warn("Could not perform check(s) " + installedProductCheckTypes.toString() + " for " + installedPartName);
			}
		}
		return partCheckResults;
	}
	

	
	public List<String> outstandingPartsCheck() {
		Map<String,Boolean> results = installedPartsStatusCheck();
		List<String> parts = new ArrayList<String>();
		for(Entry<String,Boolean> item : results.entrySet()) {
			if(!item.getValue()) parts.add(item.getKey());
		}
		return parts;
	}
	
	public List<String> outstandingPartsByProcessPointsCheck() {
		Map<String, Boolean> results = installedPartsByProcessPointsStatusCheck();
		List<String> parts = new ArrayList<String>();
		for (Entry<String, Boolean> item : results.entrySet()) {
			if (!item.getValue()) {
				parts.add(item.getKey());
			}
		}
		return parts;
	}

	public List<String> partsExistCheck() {
		List<String> results = new ArrayList<String>();
		Map<String, String[]> partNameMap = getPropertyBean().getPartsExistCheckPartNames(String[].class);
		String[] propArray = getMappedProperty(partNameMap);
		if (propArray == null || propArray.length == 0) {
			return results;
		}
		List<String> partNames = Arrays.asList(propArray);
		ProductBuildResultDao<? extends ProductBuildResult, ?> pbrDao = ProductTypeUtil.getProductBuildResultDao(product.getProductType());
		List<? extends ProductBuildResult> installedParts = pbrDao.findAllByProductIdAndPartNames(product.getProductId(), partNames);
		if (installedParts != null) {
			for (ProductBuildResult ip : installedParts) {
				if (ip == null) {
					continue;
				}
				results.add(StringUtils.trim(ip.getPartName()) + "(SN: " + StringUtils.trim(ip.getPartSerialNumber()) + ")");
			}
		}
		return results;
	}
	
	public Map<String, Boolean> installedPartsByProcessPointsStatusCheck() {

		Map<String, Boolean> results = new LinkedHashMap<String, Boolean>();
		String[] processPointIds = getPropertyBean().getInstalledPartsCheckProcessPoints();
		if (processPointIds == null || processPointIds.length == 0) {
			processPointIds = new String[] { getProcessPoint().getProcessPointId() };
		}

		BaseProductSpecDao<?, ?> productSpecDao = ProductTypeUtil.getProductSpecDao(getProduct().getProductType());
		ProductSpec productSpec = (ProductSpec) productSpecDao.findByProductSpecCode(getProduct().getProductSpecCode(), getProduct().getProductType().name());
		if (productSpec == null) {
			String msg = "installedPartsByProcessPointsStatusCheck() - ProductSpec does not exist for - product:%s, productSpecCode: %s.";
			msg = String.format(msg, getProduct(), getProduct().getProductSpecCode());
			Logger.getLogger().warn(msg);
			results.put(msg, false);
			return results;
		}

		List<String> ppIds = Arrays.asList(processPointIds);
		boolean partConfirmCheck = PropertyService.getPropertyBoolean(processPoint.getProcessPointId(), "PART_CONFIRM_CHECK", false);
		
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		List<String> partNames = lotControlRuleDao.findAllPartNames(ppIds, productSpec, partConfirmCheck);
		if (partNames == null || partNames.isEmpty()) {
			String msg = "%s or %s is defined for process points that have no rules/parts defined, processPointIds: %s.";
			msg = String.format(msg, INSTALLED_PARTS_BY_PROCESS_POINTS_STATUS_CHECK, OUTSTANDING_PARTS_BY_PROCESS_POINTS_CHECK, ppIds);
			Logger.getLogger().warn(msg);
			return results;
		}

		for (String partName : partNames) {
			results.put(StringUtils.trim(partName), Boolean.FALSE);
		}

		ProductBuildResultDao<? extends ProductBuildResult, ?> pbrDao = ProductTypeUtil.getProductBuildResultDao(product.getProductType());
		List<? extends ProductBuildResult> installedParts = pbrDao.findAllByProductIdAndPartNames(product.getProductId(), new ArrayList<String>(results.keySet()));
		
		if (installedParts != null) {
			for (ProductBuildResult ip : installedParts) {
				if (ip == null) {
					continue;
				}
				results.put(StringUtils.trim(ip.getPartName()), ip.isStatusOk());
			}
		}
		return results;
	}
	
	public List<String> outstandingRequiredPartsCheckForMultipleProductTypes() {
		
		Map<String, String> requiredPartProcessPointIdMap = getPropertyBean().getRequiredPartProcessPointIdMap();
		
		String targetPpid = null;
		if(requiredPartProcessPointIdMap != null && requiredPartProcessPointIdMap.size() > 0)
			targetPpid = requiredPartProcessPointIdMap.get(getProduct().getProductType().name());

		if(StringUtils.isEmpty(targetPpid)) return new ArrayList<String>();
		
		return getDao(RequiredPartDao.class).findMissingRequiredParts
				(getProductSpecCode(), targetPpid, product.getProductType(), product.getProductId(), product.getSubId());

	}
	
	public List<String> outstandingRequiredPartsCheck() {
		
		return getDao(RequiredPartDao.class).findMissingRequiredParts
			(getProductSpecCode(), getProcessPoint().getProcessPointId(), product.getProductType(), product.getProductId(), product.getSubId());
		
	}
	
	private String getProductSpecCode() {
		if(product.getProductType() == ProductType.MBPN)
			return ((MbpnProduct)product).getCurrentProductSpecCode();
		else 
			return product.getProductSpecCode();
	}

	public boolean outstandingRequiredPartsNotExistCheck() {
		
		List<String> parts = outstandingRequiredPartsCheck();
		return parts.isEmpty();
		
	}
	
	public boolean missionNotInstalledCheck() {
		
		Engine engine = (Engine)product;
		return engine.getMissionStatus() <=0;
		
	}
	
	public Map<String,Object> installedSealantsStatusCheck() {
		Map<String, String> sealants = getSealantsFromProperty();
		Map<String, Object> results = new HashMap<String, Object>();
		if (sealants == null || sealants.isEmpty()) return results;

		for (Map.Entry<String, String> entry : sealants.entrySet()) {
			String sealantName = entry.getKey();
			String strMminTime = entry.getValue();
			int minimumPassedMinutes = Integer.valueOf(strMminTime);

			Map<String, Object> sealantCheck = checkSealant( sealantName, minimumPassedMinutes);
			results.putAll(sealantCheck);
		}

		return results;
	}
	
	private Map<String,Object> checkSealant(String sealantName, int minimumPassedMinutes) {
		Map<String, Object> entry = new HashMap<String, Object>();
		boolean sealantStatus = false;
		String time ="Unknown";
		InstalledPart installedPart = getDao(InstalledPartDao.class).findById(product.getProductId(), sealantName);
		if(installedPart != null && installedPart.isStatusOk() && installedPart.getActualTimestamp() != null) {
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			long timeDiffMiliSeconds = currentTime.getTime() - installedPart.getActualTimestamp().getTime();
			double passedMinutes = (timeDiffMiliSeconds / 1000) / 60;
			double remainingTime = Math.ceil(minimumPassedMinutes - passedMinutes);
			if(remainingTime <= 0) remainingTime = 0;
			sealantStatus = remainingTime <= 0;
			time = String.valueOf(Double.valueOf(remainingTime).intValue());
		}
		entry.put(sealantName, sealantStatus);
		entry.put(sealantName + " TIME", time);
		return entry;
	}
	
	public boolean engineFiringTestCheck() {
		
		Engine engine = (Engine)product;
		return engine.getEngineFiringFlag() >= 1;
		
	}
	
	public String engineEcuValueCheck() {
		BuildAttribute attribute = getDao(BuildAttributeDao.class).findById("ECU", product.getProductSpecCode());
		return attribute == null ? "" : attribute.getAttributeValue();
	}
	
	public boolean productOnHoldStatusCheck() {
		return product.getHoldStatus() >= 1;
	}

	public boolean engineBlockOnHoldStatusCheck() {
		
		Block block = findBlock(product.getProductId());
		return block == null || block.getHoldStatus() >= 1;
		
	}
	
	public boolean engineHeadOnHoldStatusCheck() {
		
		Head head = findHead(product.getProductId());
		return head == null || head.getHoldStatus() >= 1;
		
	}
	
	public boolean mhOffNotCompleteCheck() {
		List<String> mhOffPPIDs = PropertyService.getPropertyList(getProcessPoint().getProcessPointId(), "MH_OFF_PROCESS_POINT_ID");
		for(String ppid : mhOffPPIDs) {
			List<HeadHistory> results = getDao(HeadHistoryDao.class).findAllByProductAndProcessPoint(product.getProductId(), ppid);
			if(!results.isEmpty()) return false;
		}
		return true;
	}
	
	public boolean hcmHeadMhOffNotCompleteCheck() {
		Head head = (Head) product;
		String plant = ProductNumberDef.DCH.getPlant(head.getDcSerialNumber());
		return "HC".equals(plant) ? mhOffNotCompleteCheck() : false;
	}
	
	public boolean invalidPreviousLineCheck() {
		List<PreviousLine> previousLines = getPreviousLineMap().get(getProcessPoint().getLineId());
		if (previousLines == null || previousLines.size() == 0) {
			previousLines = getDao(PreviousLineDao.class).findAllByLineId(getProcessPoint().getLineId());
			getPreviousLineMap().put(getProcessPoint().getLineId(), previousLines);
		}
		for(PreviousLine line : previousLines) {
			if(line.getId().getPreviousLineId().equals(product.getTrackingStatus())) return false;
		}
		return true;
	}
	
	/**
	 * Check existence of specific hold types for a product id
	 * 
	 * @param holdType
	 * @return 
	 */
	public List<String> productOnSpecificHoldCheck(HoldResultType holdType) {
		
		List<String> holds = new ArrayList<String>();
		if(product == null) return null;
		List<HoldResult> holdResults = 
			getDao(HoldResultDao.class).findAllByProductAndReleaseFlag(product.getProductId(), false,holdType);
		
		if(holdResults != null && !holdResults.isEmpty()){ 
			for(HoldResult item : holdResults) {
				holds.add(format(product.getProductId(),item));
			}
		}							
		
		// production lot hold status check
		String lotResult = productionLotOnHoldCheck();
		if(lotResult!= null)
			holds.add(lotResult);
		return holds;
	}
	
	public List<String> productOnHoldCheck() {
		return productOnSpecificHoldCheck(HoldResultType.GENERIC_HOLD);
	}
	
	public List<String> productOnAtShippingHoldCheck() {
		return productOnSpecificHoldCheck(HoldResultType.HOLD_AT_SHIPPING);
	}
	
	public List<String> productOnNowHoldCheck() {
		return productOnSpecificHoldCheck(HoldResultType.HOLD_NOW);
	}
	
	public String productionLotOnHoldCheck() {
		
		if(StringUtils.isEmpty(product.getProductionLot())) return null;
		PreProductionLot preProductionLotResult = 
			getDao(PreProductionLotDao.class).findByKey(product.getProductionLot());
		
		if(preProductionLotResult== null) return null;
		else
			return (preProductionLotResult.getHoldStatus()== 0) ? product.getProductionLot()+ "  status is on hold"+ product.getProductId(): null;
	}
	
	public List<String> engineBlockOnHoldCheck() {
		
		Block block = findBlock(product.getProductId());
		setProduct(block);
		return productOnHoldCheck();
		
	}
	
	public List<String> engineHeadOnHoldCheck() {
		
		Head head = findHead(product.getProductId());
		setProduct(head);
		return productOnHoldCheck();
		
	}
	
	public boolean engineBhOnHoldStatusCheck() {
		
		boolean engineOnHoldStatus = productOnHoldStatusCheck();
		boolean blockOnHoldStatus = true;
		boolean headOnHoldStatus = true;
		Block block = findBlock(product.getProductId());
		Head head = findHead(product.getProductId());
		if(block != null) {
			setProduct(block);
			blockOnHoldStatus = productOnHoldStatusCheck();
		}
		if(head != null) {
			setProduct(head);
			headOnHoldStatus = productOnHoldStatusCheck();
		}
		
		return engineOnHoldStatus || blockOnHoldStatus || headOnHoldStatus;
		
	}
	
	private String format(String productId, HoldResult holdResult) {
		
		StringBuilder builder = new StringBuilder();
		String respDpt = null;

		try {
			int qsr_id = holdResult.getQsrId();
			Qsr qsr = qsr_id > 0 ? getDao(QsrDao.class).findByKey(qsr_id) : null;
			if (qsr != null) {
				respDpt = qsr.getResponsibleDepartment();
				builder.append("QSR ID: " + qsr_id + " - ");
			}
		} catch(Exception e){}
		
		builder.append(holdResult.getHoldReason() + " ");
		builder.append("[" + productId + "], ");
		if (StringUtils.isNotBlank(respDpt)) 
			builder.append("[Resp Dpt: " + respDpt + "], ");
		builder.append("[Assoc # : " + holdResult.getHoldAssociateNo() + "], ");
		builder.append("[Name : " + holdResult.getHoldAssociateName() + "], ");
		builder.append("[Phone : " + holdResult.getHoldAssociatePhone() + "]");
		return builder.toString();
	}
	
	private List<Head> findAllHeads(String engineId) {
		return getDao(HeadDao.class).findAllByEngineSerialNumber(engineId);
	}
	
	private List<Block> findAllBlocks(String engineId) {
		return getDao(BlockDao.class).findAllByEngineSerialNumber(engineId);
	}
	
	private Head findHead(String engineId) {
		List<Head> heads = findAllHeads(engineId);
		return heads.isEmpty() ? null : heads.get(0);
	}
	
	private Block findBlock(String engineId) {
		List<Block> blocks = findAllBlocks(engineId);
		return blocks.isEmpty() ? null : blocks.get(0);
	}
	
	private Map<String,String> getInstalledPartsFromProperty() {
		
		return getPropertyBean().getInstalledParts();
		
	}
	
	private List<String>fetchPartNames() {
		return getInstalledPartNames().isEmpty() ? fetchPartNamesFromProperty() : getInstalledPartNames(); 
	}
	
	private List<String> fetchPartNamesFromProperty() {
		Map<String,String> installedPartsMap = getInstalledPartsFromProperty();
		if(installedPartsMap == null) return new ArrayList<String>();
		String partNames = installedPartsMap.containsKey(product.getModelCode())?
			installedPartsMap.get(product.getModelCode()) : installedPartsMap.get("*");
		partNames = StringUtils.trim(partNames);
		String[] names = StringUtils.split(partNames, Delimiter.COMMA);
		return Arrays.asList(names);
	}
	
	private Map<String,String> getSealantsFromProperty() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProcessPoint().getProcessPointId()).getInstalledSealants();
	}
	
	private boolean isPartInstalled(String partNumber) {
		return ProductTypeUtil.getProductBuildResultDao(product.getProductType()).isPartInstalled(product.getProductId(),partNumber);
	}
	
	public boolean isInstalledPartStatusCheck(String productId, String partName) {		
		InstalledPart part = getInstalledPartDao().findByKey(new InstalledPartId(productId, partName));
		return (part == null) ? false : ( 
				part.getInstalledPartStatus() == InstalledPartStatus.getType(InstalledPartStatus.OK.getId()) ||
				part.getInstalledPartStatus() == InstalledPartStatus.getType(InstalledPartStatus.REPAIRED.getId()) ||
				part.getInstalledPartStatus() == InstalledPartStatus.getType(InstalledPartStatus.ACCEPT.getId()));
	}
	
	private Method getMethod(String methodName) {
		Method method = null;
		try {
			method = ClassUtils.getPublicMethod(ProductCheckUtil.class, methodName, null);
		} catch (Exception e) {
		    throw new SystemException("Failed to get method " + methodName, e);
		}	
		return method;
	}
	
	private String getMethodName(String productCheckType) {
		
		String[] items = productCheckType.split("_");
		boolean isFirst = true;
		String methodName = "";
		for(String item : items) {
			String name = item.toLowerCase();
			if(isFirst) {
				isFirst = false;
			}else {
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			}
			methodName += name;
		}
		
		return methodName;
		
	}
	
	public Object check(BaseProduct product, ProcessPoint processPoint,String productCheckType) {
		
		setProduct(product);
		setProcessPoint(processPoint);
		
		
		return check(productCheckType);
		
	}

	public Object check(BaseProduct product, ProcessPoint processPoint,String productCheckType, Device device) {

		setProduct(product);
		setProcessPoint(processPoint);
		setDevice(device);


		return check(productCheckType);

	}
	
	private void setDevice(Device device) {
		this.device = device;
		
	}

	public Object check(BaseProduct product, ProcessPoint processPoint,List<String> installedPartNames,String productCheckType) {
		
		setProduct(product);
		setProcessPoint(processPoint);
		setInstalledPartNames(installedPartNames);
		
		return check(productCheckType);
		
	}
	
	public Object check(String productCheckType) {
		String methodName = getMethodName(productCheckType);
		Method method = getMethod(methodName);
		Object returnValue = null;
		try {
			returnValue = method.invoke(this, new Object[]{});
		} catch (Exception e) {
			throw new SystemException("Failed to invoke method " + methodName, e );
		}
		
		return returnValue;
	}
	
	public static Map<String,Object> check(BaseProduct product, ProcessPoint processPoint,
			String[] productCheckTypeIds){
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		return doCheck(product, processPoint, productCheckTypeIds,
				productCheckUtil);
	}
	
	private static Map<String, Object> doCheck(BaseProduct product, ProcessPoint processPoint, 
			String[] productCheckTypeIds, ProductCheckUtil productCheckUtil) {
		Map<String,Object> checkResults = new LinkedHashMap<String,Object>();
		for(String productCheckType : productCheckTypeIds) {
			Object returnValue = productCheckUtil.check(product,processPoint,productCheckType);
			if(isValid(returnValue))
				checkResults.put(productCheckType, returnValue);
		}
		return checkResults;
	}
	
	private static boolean isPass(Object returnValue) {
		return returnValue == null || Boolean.TRUE.equals(returnValue);
	}
	

	public static Map<String,Object> check(BaseProduct product, ProcessPoint processPoint,
			String[] productCheckTypeIds, Map<Object, Object> context){
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(context);
		return doCheck(product, processPoint, productCheckTypeIds,
				productCheckUtil);
	}
	
	
	public static Map<String,Object> check(BaseProduct product, ProcessPoint processPoint,
			String[] productCheckTypeIds, Device device, Map<Object, Object> context){

		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(context);
		Map<String,Object> checkResults = new LinkedHashMap<String,Object>();
		for(String productCheckType : productCheckTypeIds) {
			Object returnValue = productCheckUtil.check(product,processPoint,productCheckType, device);
			checkResults.put(productCheckType, returnValue);
		}
		return checkResults;
	}
	
	
	public static Map<String,Object> check(BaseProduct product, ProcessPoint processPoint,String[] productCheckTypeIds,
			List<String> partNames){

		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setInstalledPartNames(partNames);
		return doCheck(product, processPoint, productCheckTypeIds,
				productCheckUtil);
	}
	
	public static Map<String,Object> check(BaseProduct product, ProcessPoint processPoint,List<String> installedPartNames,String[] productCheckTypeIds){

		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		Map<String,Object> checkResults = new LinkedHashMap<String,Object>();
		for(String productCheckType : productCheckTypeIds) {
			Object returnValue = productCheckUtil.check(product,processPoint,installedPartNames,productCheckType);
			if(isValid(returnValue))
				checkResults.put(productCheckType, returnValue);
		}
		return checkResults;
	}
	
	public static String toErrorString(Map<String,Object> checkResults){
		if(checkResults == null || checkResults.isEmpty()) return "";
		String message ="Product Check Result - ";
		for(Map.Entry<String, Object> entrySet : checkResults.entrySet()) {
			if(!isPass(entrySet.getValue()))
				message +="Check Type: " + entrySet.getKey() + " Result: " + getResultString(entrySet.getValue()) + ";\n"; 
		}
		return message;
	}
	
	public static String formatTxt(Map<String, Object> checkResults) {
		if (checkResults == null || checkResults.isEmpty()) {
			return "";
		}
		int rowCount = 0;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : checkResults.entrySet()) {
			rowCount++;
			String name = entry.getKey();
			Object result = entry.getValue();
			if (name == null) {
				continue;
			}
			ProductCheckType checkType = ProductCheckType.get(name);
			if (checkType != null) {
				name = checkType.getName();
			}
			sb.append(rowCount).append(".").append(name);
			String formattedResult = formatTxt(result, ", ");
			if (StringUtils.isNotBlank(formattedResult)) {
				sb.append(" : ").append(formattedResult);
			}
			sb.append("   ").append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	public static String formatMissingPart(Map<String, Object> checkResults) {
		if (checkResults == null || checkResults.isEmpty()) {
			return "";
		}
		int rowCount = 0;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : checkResults.entrySet()) {
			rowCount++;
			String name = entry.getKey();
			Object result = entry.getValue();
			if (name == null) {
				continue;
			}
			ProductCheckType checkType = ProductCheckType.get(name);
			if (checkType != null) {
				name = checkType.getName();
			}
			sb.append(rowCount).append(".").append(name + " : " + "\n");
			String formattedResult = formatMissingPart(result, ", ");
			if (StringUtils.isNotBlank(formattedResult)) {
				sb.append(formattedResult);
			}
			sb.append("   ").append(System.getProperty("line.separator"));
			sb.append("   ").append("\n");
		}
		return sb.toString();
	}

	public static String formatMissingPart(Object result, String itemSeparator) {
		if (result == null) {
			return "";
		} else if (result instanceof String) {
			return (String) result;
		} else if (result instanceof Boolean) {
			return String.valueOf((Boolean) result);
		}
		Collection<?> collection = null;
		if (result instanceof Collection) {
			collection = (Collection<?>) result;
		} else if (result.getClass().isArray()) {
			collection = Arrays.asList((Object[]) result);
		} else if (result instanceof Map) {
			collection = ((Map<?, ?>) result).entrySet();
		}
		if (collection == null || collection.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		List<String> noGoodPartReasonList = new ArrayList<String> ();
		noGoodPartReasonList.add("Missing Part");
		noGoodPartReasonList.add("Missing PartId");
		noGoodPartReasonList.add("Missing Measurement");
		noGoodPartReasonList.add("Incorrect Measurement");
		noGoodPartReasonList.add("Measurement Count Mismatch");
		noGoodPartReasonList.add("Incorrect Part");
		
		for (Object item : collection) {
			for(String noGoodPartReason : noGoodPartReasonList) {
				if(item.toString().contains(noGoodPartReason)) {
					String partNameAndSerialNoWithSpace = item.toString().substring(0, item.toString().indexOf(noGoodPartReason));
					String partNameAndSerialNo = "";
					for(String str : partNameAndSerialNoWithSpace.split("\t\t\t")) {
						partNameAndSerialNo = partNameAndSerialNo + str.trim() + "     ";
					}
					sb.append(noGoodPartReason + ":- " + partNameAndSerialNo + "\n");
					break;
				}
			}
		}
		
		return sb.toString();
	}
	
	
	public static String formatTxt(Object result, String itemSeparator) {
		if (result == null) {
			return "";
		} else if (result instanceof String) {
			return (String) result;
		} else if (result instanceof Boolean) {
			return String.valueOf((Boolean) result);
		}
		Collection<?> collection = null;
		if (result instanceof Collection) {
			collection = (Collection<?>) result;
		} else if (result.getClass().isArray()) {
			collection = Arrays.asList((Object[]) result);
		} else if (result instanceof Map) {
			collection = ((Map<?, ?>) result).entrySet();
		}
		if (collection == null || collection.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object item : collection) {
			if (sb.length() > 0) {
				sb.append(itemSeparator);
			}
			sb.append(item);
		}
		return sb.toString();
	}
	
	private static String getResultString(Object result) {
		if(result == null) return "PASS";
		else if(Boolean.TRUE.equals(result)) return "YES";
		else if(Boolean.FALSE.equals(result)) return "NO";
		else if(result instanceof Collection<?>){
			return Arrays.toString(((Collection<?>)result).toArray());
		}
		return result.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Object object) {
		if(object == null) return false;
		if(object instanceof List) return !((List)object).isEmpty();
		if(object instanceof Map) return !((Map)object).isEmpty();
		if(object instanceof Boolean) return (Boolean)object;
		return true;
	}
	
	public List<PartResultData> installedPartsInspectionCheck() throws SystemException {
		boolean partConfirmCheck = PropertyService.getPropertyBoolean(processPoint.getProcessPointId(), "PART_CONFIRM_CHECK", true);
		return installedPartCheck(processPoint.getProcessPointId(), partConfirmCheck, false);
	}

	public List<PartResultData> installedPartsInspectionAllProcessesCheck() throws SystemException {
		boolean partConfirmCheck = PropertyService.getPropertyBoolean(processPoint.getProcessPointId(), "PART_CONFIRM_CHECK", true);
		return installedPartCheck(null, partConfirmCheck, false);
	}

	public List<String> letCheck() {
		Logger.getLogger().info("Executing LET Check");
		String letProgramCategory = PropertyService.getPropertyBean(LetPropertyBean.class, processPoint.getProcessPointId()).getLetCheckType();
		List<String> retVal = new ArrayList<String>();
		List<Map<String, Object>> list = getDao(LetProgramResultDao.class).findAllOutstandingPrograms(product.getProductId(), letProgramCategory);
		if (list == null || list.isEmpty()) {
			return retVal;
		}
		for (Map<String, Object> item : list) {
			Object programName = item.get(LETDataTag.CRITERIA_PGM_NAME);
			Object result = item.get(LETDataTag.RESULT);
			String resultTxt = "";
			if (result == null) {
				resultTxt = "Not Tested";
			} else {
				Integer statusInt = StringUtil.toInteger(result.toString().trim());
				if (statusInt == null || LetInspectionStatus.getType(statusInt) == null) {
					resultTxt = "Unknown Result";
				} else {
					LetInspectionStatus status = LetInspectionStatus.getType(statusInt);
					resultTxt = status.getLabel();
				}
			}
			String txt = "Test " + resultTxt + " : " + programName;
			retVal.add(txt);
		}
		return retVal;
	}
	
	public List<String> letCheckMicroservice() {
		Logger.getLogger().info("Executing LET Check Microservice");
		
		List<String> results = new ArrayList<String>();
		String url = PropertyService.getPropertyBean(LetPropertyBean.class).getLetMicroserviceUrl();
		if(StringUtils.isEmpty(url)) {
			Logger.getLogger().error("LET microservice URL property is not set.");
			return results;
		}
		String category = PropertyService.getPropertyBean(LetPropertyBean.class, processPoint.getProcessPointId()).getLetCheckType();
		String newUrl = String.format(url, product.getProductId(), product.getProductSpecCode().substring(0, 9), category.replaceAll(" ", "%20"));
		
		try {
			String response = HttpClient.get(newUrl,  HttpURLConnection.HTTP_OK);
			
			ObjectMapper mapper = new ObjectMapper();
			ArrayList<LinkedHashMap<String, Object>> responseItems = mapper.readValue(response, ArrayList.class);
			if(responseItems == null || responseItems.isEmpty()) {
				return results;
			}
			
			String status;
			for(LinkedHashMap<String, Object> map : responseItems) {
				status = (String) map.get("status");
				results.add("Test " + (StringUtils.isEmpty(status) ? "Not Tested" : status) + " : " + map.get("testName"));
			}
		} catch (Exception ex) {
	 		ex.printStackTrace();
	 		Logger.getLogger().error("LET microservice exception: " + ex.getMessage());
		}		

		return results;
	}
	
	public List<String> nextExpectedProductCheck() throws SystemException {
		List<String> checkResults = new ArrayList<String>();
		ExpectedProductDao expectedProductDao = getDao(ExpectedProductDao.class);
		List<ExpectedProduct> expectedProdList= expectedProductDao.findAllForProcessPoint(processPoint.getProcessPointId());
		boolean isExpectedProduct = false;

		for (ExpectedProduct expectedProduct : expectedProdList){
			if((expectedProduct.getProductId()!=null)&& expectedProduct.getProductId().equals(product.getProductId())){
				isExpectedProduct = true;				
			}
		}

		if(!isExpectedProduct){
			checkResults.add("Expected Product Check failed ");
		}
		return checkResults;
	}
	
	public List<PartResultData> processPointInstalledPartCheck() throws SystemException {
		String processPointList = PropertyService.getProperty(processPoint.getProcessPointId(), "PRODUCT_CHECK_PROCESS_POINTS");
		boolean partConfirmCheck = PropertyService.getPropertyBoolean(processPoint.getProcessPointId(), "PART_CONFIRM_CHECK", true);
		return installedPartCheck(processPointList, partConfirmCheck, true);
	}	
	
	public List<PartResultData> installedPartCheck(String processPointIdStr, boolean partConfirmCheck, boolean useProcessPoint ) throws 
	SystemException
	{
		Vector<PartResultData> partDetails = new Vector<PartResultData>();
		boolean enableRepairCheck=PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false);
		List<InstalledPartDetail> installedParts = new ArrayList<InstalledPartDetail>();
			installedParts = getDao(InstalledPartDao.class).getAllInstalledPartDetails(product.getProductId(), product.getProductType().name(),  processPointIdStr, partConfirmCheck, useProcessPoint,
					PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false),enableRepairCheck);
		Set<InstalledPartDetail> uniqueInstalledParts = new HashSet<InstalledPartDetail>();
		uniqueInstalledParts.addAll(installedParts);
		List<InstalledPartDetail> installedPartsByPartName = null;
		for (InstalledPartDetail installPart : uniqueInstalledParts) {
			// This is a required LotControlRule
			boolean needShowInPopup = false;
			String strPartName = installPart.getPartName();
			String strSerial = null;
			String strTorques = "";
			String strReason = null;
			PartResultData partData;
			InstalledPartDetail installedPart = null;
			installedPartsByPartName = new ArrayList<InstalledPartDetail>();
			if (StringUtils.isNotEmpty(strPartName)) {
				for (InstalledPartDetail installPartDetail : installedParts) {
					if (installPartDetail.getPartName() != null) {
						if (installPart.getPartName().trim().equals(
								installPartDetail.getPartName().trim())) {
							installedPartsByPartName.add(installPartDetail);
						}
					}
				}
			} else {
				strPartName = installPart.getRulePartName();
			}
			if (!installedPartsByPartName.isEmpty()) {
				installedPart = installedPartsByPartName.get(0);
			}

			if (installedPart != null) {
				strSerial = installedPart.getPartSerialNumber();
				strReason = installedPart.getInstalledPartReason();
				if (installedPart.getInstalledPartStatus() != InstalledPartStatus.OK.getId()
					&& installedPart.getInstalledPartStatus() != InstalledPartStatus.ACCEPT.getId()
					&& installedPart.getInstalledPartStatus() != InstalledPartStatus.REPAIRED.getId()) {
					// This installed part was not installed correctly
					needShowInPopup = true;
					strReason = "Incorrect Part";

				}
				if(installedPart.getPartId() != null){
				if (installedPart.getMeasurementCount() > 0) {
					if (installedPartsByPartName.size() != installedPart
							.getMeasurementCount()) {
						// lastAttempt missing
						needShowInPopup = true;
						strReason = "Measurement Count Mismatch";

					}
					for (InstalledPartDetail details : installedPartsByPartName) {

						if (details.getMeasurementValue() != null) {
							strTorques = strTorques
							+ details.getMeasurementValue() + ", ";
						} else {
							if(!enableRepairCheck || (details.getPartConfirmCheck()==1 || (details.getPartConfirmCheck()==0 && details.getRepairCheck()!=1 )))
							{
								// lastAttempt missing
								needShowInPopup = true;
								strReason = "Missing Measurement";
								
							}
							break;
						}							
						if(MeasurementStatus.OK.getId() != details.getMeasurementStatus()){								
							needShowInPopup = true;

							strReason = "Incorrect Measurement";
						}
					}
				}
				}else{
					needShowInPopup = true;
					strReason = "Missing PartId";

				}
			} else {
				if(!enableRepairCheck || (installPart.getPartConfirmCheck()==1 || (installPart.getPartConfirmCheck()==0 && installPart.getRepairCheck()!=1 )))
				{
					// Installed Part not found
					needShowInPopup = true;
					strReason = "Missing Part";
				}

			}
			if (needShowInPopup) {
				partData = new PartResultData();
				partData.part_Name = strPartName;
				partData.part_Serial = strSerial;
				partData.torques = strTorques;
				partData.part_Reason = strReason;
				partDetails.addElement(partData);
			}
		}
		
		return partDetails;
	}
	public boolean engineNotInstalledCheck() {
		if (getProduct() instanceof Frame && doEngineCheck()) {
			Frame product = (Frame) getProduct();
			return product.getEngineStatus() == null || product.getEngineStatus() < 1;
		}
		return false;
	}

	public boolean engineNumberEmptyCheck() {
		if (getProduct() instanceof Frame && doEngineCheck()) {
			Frame product = (Frame) getProduct();
			return StringUtils.isBlank(product.getEngineSerialNo());
		} else if (getProduct() instanceof DieCast) {
			DieCast product = (DieCast) getProduct();
			return StringUtils.isBlank(product.getEngineSerialNumber());
		}
		return false;
	}
	
	public boolean incompleteEngineDataCheck() {
		Engine engine = null;
		if (getProduct() instanceof Frame) {
			Frame product = (Frame) getProduct();
			engine = getDao(EngineDao.class).findByKey(product.getEngineSerialNo());
		} else if (getProduct() instanceof Engine) {
			engine = (Engine) getProduct();
		}
		return engine == null || StringUtils.isBlank(engine.getPlantCode());
	}

	public boolean missionNumberEmptyCheck() {
		if (getProduct() instanceof Frame) {
			Frame product = (Frame) getProduct();
			return StringUtils.isBlank(product.getMissionSerialNo());
		} else if (getProduct() instanceof Engine) {
			Engine product = (Engine) getProduct();
			return StringUtils.isBlank(product.getMissionSerialNo());
		}
		return false;
	}

	public List<String> engineOnHoldCheck() {
		if (getProduct() instanceof Frame && doEngineCheck()) {
			Frame product = (Frame) getProduct();
			String ein = product.getEngineSerialNo();
			Engine engine = null;
			if (StringUtils.isBlank(ein)) {
				List<String> reason = new ArrayList<String>();
				reason.add("No Engine Found");
				return reason;
			}
			
			engine = getDao(EngineDao.class).findByKey(ein);
			setProduct(engine);
		}
		return productOnHoldCheck();
	}
	
	public boolean engineNumberNotEmptyCheck() {
		return !engineNumberEmptyCheck();
	}

	public boolean engineNotAssignedCheck() {
		return !duplicateEngineAssignmentCheck();
	}
	
	public boolean duplicateEngineAssignmentCheck() {
		if (getProduct() instanceof Frame && doEngineCheck()) {
			Frame product = (Frame) getProduct();
			String ein = product.getEngineSerialNo();
			if (StringUtils.isBlank(ein) ) {
				return false;
			}
			List<Frame> list = getDao(FrameDao.class).findByEin(product.getEngineSerialNo());
			if (list != null && list.size() > 1) {
				return true;
			}
		}
		return false;
	}

	public List<String> productResultExistCheck() {
		String[] ppIds = getPropertyBean().getProductResultExistProcessPointIds();
		return productHistoryCheck(true, ppIds);
	}
	
	public List<String> productResultMissingCheck() {
		String[] ppIds = getPropertyBean().getProductResultMissingProcessPointIds();
		return productHistoryCheck(false, ppIds);
	}

	public List<String> prevProdResultMissingCheck() {
		String[] ppIds = getPropertyBean().getPrevProdResultMissingPpids();
		return prevProductHistoryCheck(false, ppIds);
	}
	
	public List<String> productHistoryNotExistCheck() {
		String[] processPointIdList = getPropertyBean().getHistoryProcessPointIdList();
		return productHistoryCheck(false, processPointIdList);
	}

	public List<String> productHistoryExistCheck() {
		String[] processPointIdList = getPropertyBean().getHistoryProcessPointIdList();
		return productHistoryCheck(true, processPointIdList);
	}

	public List<String> productHistoryCheck(final boolean checkExist, String[] processPointIdList) {
		final boolean checkNotExist = !checkExist;
		if (processPointIdList == null || processPointIdList.length == 0) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		ProcessPointDao processPointDao = getDao(ProcessPointDao.class);
		ProductHistoryDao<?, ?> productHistoryDao = ProductTypeUtil.getProductHistoryDao(getProduct().getProductType());
		String msgTpl = "%s (%s)";
		for (String ppId : processPointIdList) {
			ProcessPoint pp = processPointDao.findByKey(StringUtils.trim(ppId));
			if (pp == null) {
				continue;
			}
			List<?> historyList = productHistoryDao.findAllByProductAndProcessPoint(getProduct().getProductId(), ppId);
			if (checkExist && (historyList != null && !historyList.isEmpty())) {
				result.add(String.format(msgTpl, pp.getProcessPointName(), pp.getProcessPointId()));
			} else if (checkNotExist && (historyList == null || historyList.isEmpty())) {
				result.add(String.format(msgTpl, pp.getProcessPointName(), pp.getProcessPointId()));
			}
		}
		return result;
	}
	
	/**
	 * Get the latest product from GAL215BX table for the current process point id and 
	 * check if that product id has any records in GAL215TBX table against the process point(s) configured in properties PREV_PROD_RESULT_MISSING_PPIDS
	 *  
	 * @param checkExist
	 * @param processPointIdList
	 * @return
	 */
	public List<String> prevProductHistoryCheck(final boolean checkExist, String[] processPointIdList) {
		final boolean checkNotExist = !checkExist;
		if (processPointIdList == null || processPointIdList.length == 0) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		ProcessPointDao processPointDao = getDao(ProcessPointDao.class);
		ProductHistoryDao<?, ?> productHistoryDao = ProductTypeUtil.getProductHistoryDao(getProduct().getProductType());
		String msgTpl = "%s (%s)";
		for (String ppId : processPointIdList) {
			ProcessPoint pp = processPointDao.findByKey(StringUtils.trim(ppId));
			if (pp == null) {
				continue;
			}
			List<?> historyList = productHistoryDao.findAllByProductAndProcessPoint(productHistoryDao.getLatestProductId(this.processPoint.getProcessPointId()), ppId);
			if (checkExist && (historyList != null && !historyList.isEmpty())) {
				result.add(String.format(msgTpl, pp.getProcessPointName(), pp.getProcessPointId()));
			} else if (checkNotExist && (historyList == null || historyList.isEmpty())) {
				result.add(String.format(msgTpl, pp.getProcessPointName(), pp.getProcessPointId()));
			}
		}
		return result;
	}

	public boolean productShippedCheck() {
		String ppId = getProduct().getLastPassingProcessPointId();
		if (StringUtils.isBlank(ppId)) {
			return false;
		}
		String[] ppIds = getPropertyBean().getProductShippedProcessPointIds();
		if (ppIds == null || ppIds.length == 0) {
			return false;
		}
		List<String> shippingProcessPointIds = Arrays.asList(ppIds);
		return shippingProcessPointIds.contains(ppId);
	}
	
	public boolean engineDockingCheck() {
		if (getProduct() instanceof Frame && doEngineCheck()) {
			Frame product = (Frame) getProduct();
			if(product.getEngineStatus() == null || product.getEngineStatus() == 0)
			{
				return false;
			}else
				return true;
		}
		return false;
	}

	public boolean missionDockingCheck() {
		if (getProduct() instanceof Frame && doEngineCheck()) {
			Frame product = (Frame) getProduct();
			String ein = product.getEngineSerialNo();
			Engine engine = null;
			if (ein != null) {
				engine = getDao(EngineDao.class).findByKey(ein);
				if(engine!=null)
				{
					if(engine.getMissionStatus()==0)
						return false;
				}					
			}
		}
		return true;
	}

	public boolean onOffProductHistoryCountCheck()
	{
		int offCount = 0;
		int onCount = 0;
		List<ProductResult> productResultList=getDao(ProductResultDao.class).findAllByProductId(product.getProductId());
		for(ProductResult result:productResultList)
		{
			String processPointId=result.getProcessPointId();
			ProcessPointType processPointType=getDao(ProcessPointDao.class).findByKey(processPointId).getProcessPointType();
			if (processPointType.getId() == ProcessPointType.Off.getId()
					|| processPointType.getId() == ProcessPointType.OffQics.getId()
					|| processPointType.getId() == ProcessPointType.ProductOff.getId())
			{
				if (!processPointId.equals(getFrameLinePropertyBean().getVqShipProcessPointId()))
				{
					offCount = offCount + 1;
				}
			}
			if (processPointType.getId() == ProcessPointType.On.getId())
			{
				onCount = onCount + 1;
			}
		}
		if (offCount == 3 && onCount == 4)
		{
			return true;
		}else			
			return false;
	}

	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class);
	}

	protected EngineLinePropertyBean getEngineLinePropertyBean() {
		return PropertyService.getPropertyBean(EngineLinePropertyBean.class);
	}

	protected MissionLinePropertyBean getMissionLinePropertyBean() {
		return PropertyService.getPropertyBean(MissionLinePropertyBean.class);
	}

	public List<String> engineCureCompleteCheck(){
		List<String> messages = new ArrayList<String>();
		
		String previousLines = getDao(LineDao.class).findByKey(getProcessPoint().getLineId()).getPreviousLinesIdsAsString();
		if(previousLines == null  || previousLines.split(",").length ==0){
			messages.add("Engine has no previous line.");
			return messages;
		}
		if(product instanceof Engine){
			Engine engine = (Engine)product;
			String trackingStatus = engine.getTrackingStatus().replaceAll(" ", "");
			if(trackingStatus == null || trackingStatus.length() == 0){
				messages.add("Engine " + product.getProductId() + " has no status information.");
				return messages;
			}
			
			for(String previousLine : previousLines.split(",")){
				if(previousLine.replaceAll(" ", "").equalsIgnoreCase(trackingStatus)){
					messages.add("Ready");
					return messages;
				}
			}
			
			messages.add("Engine " + product.getProductId() + " is not in Cure Complete status.");
		}
		
		return messages;
	}
	
	
	public boolean diecastProductNotMcOnCheck(){
		return StringUtils.isEmpty(((DieCast)product).getMcSerialNumber()) && StringUtils.isEmpty(((DieCast)product).getEngineSerialNumber());
	}
	
	public boolean productAlreadyProcessedCheck(String processPoints) {
		List<String> processPointList = Arrays.asList(processPoints.split(Delimiter.COMMA));
		return passedProcessPointCheck(processPointList);
	}
	
	public boolean atCheck() {
		String offProcessPoints = PropertyService.getProperty(processPoint.getProcessPointId(), TagNames.AT_PROCESS_POINTS.name());
		if(StringUtils.isEmpty(offProcessPoints)) {
			return true;
		}
		List<String> processPointList = Arrays.asList(offProcessPoints.split(Delimiter.COMMA));
		return passedProcessPointCheck(processPointList);
	}
	
	public List<String> atCheckList() {
		List<String> processPointList = PropertyService.getPropertyList(processPoint.getProcessPointId(), TagNames.AT_PROCESS_POINTS.name());
		List<String> result = new ArrayList<String>();
		StringBuilder resultBuilder = new StringBuilder();
		for (String processPoint : processPointList) {
			if (!passedProcessPointCheck(processPoint)) {
				resultBuilder.append(resultBuilder.length() == 0 ? "Product has not passed : " : ", ");
				String[] ppids = processPoint.split("\\|");
				for (int i = 0; i < ppids.length; i++) {
					if (i > 0) resultBuilder.append(" | ");
					ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class).findByKey(ppids[i]);
					resultBuilder.append(pp != null ? pp.getProcessPointName() : ppids[i] + " (not found)");
				}
			}
		}
		if (resultBuilder.length() != 0) result.add(resultBuilder.toString());
		return result;
	}
	
	public boolean notAtCheck() {
		String offProcessPoints = PropertyService.getProperty(processPoint.getProcessPointId(), TagNames.NOT_AT_PROCESS_POINTS.name());
		List<String> processPointList = Arrays.asList(offProcessPoints.split(Delimiter.COMMA));
		return !passedProcessPointCheck(processPointList);
	}
	
	public List<String> notAtCheckList() {
		List<String> processPointList = PropertyService.getPropertyList(processPoint.getProcessPointId(), TagNames.NOT_AT_PROCESS_POINTS.name());
		List<String> result = new ArrayList<String>();
		StringBuilder resultBuilder = new StringBuilder();
		for (String processPoint : processPointList) {
			if (passedProcessPointCheck(processPoint)) {
				resultBuilder.append(resultBuilder.length() == 0 ? "Product has passed : " : ", ");
				String[] ppids = processPoint.split("\\|");
				for (int i = 0; i < ppids.length; i++) {
					if (i > 0) resultBuilder.append(" | ");
					ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class).findByKey(ppids[i]);
					resultBuilder.append(pp != null ? pp.getProcessPointName() : ppids[i] + " (not found)");
				}
			}
		}
		if (resultBuilder.length() != 0) result.add(resultBuilder.toString());
		return result;
	}
	
	public boolean passedProcessPointCheck(List<String> ProcessPointIdList) {
		for(String processPointId : ProcessPointIdList) {
			if (!passedProcessPointCheck(processPointId)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Takes a process point id (or a list of process point ids separated by the | character).<br>
	 * Returns true iff the process point id (or any of the process point ids) have been passed by the product.
	 */
	public boolean passedProcessPointCheck(String processPointId) {
		ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(product.getProductType());

		String[] ppids = processPointId.split("\\|");
		for (String ppid : ppids) {
			List<? extends ProductHistory> results = historyDao.findAllByProductAndProcessPoint(product.getProductId(), ppid);
			if (!results.isEmpty()) return true;
		}
		return false;
	}

	public boolean productExistCheck(){
		return ProductTypeUtil.getProductDao(product.getProductType()).findBySn(product.getProductId()) != null;
	}
	
	public boolean headBlockNotMachiningOnCheck(){
		if(product == null) return false;
		return ((DieCast)product).getEngineSerialNumber() == null && ((DieCast)product).getMcSerialNumber() == null;
	}
	
	public Map<String, Boolean> productStateCheck(){
		Map<String,Boolean> results = installedPartsStatusCheck();
	
		BaseProduct checkingProduct = ProductTypeUtil.getProductDao(product.getProductType()).findBySn(product.getProductId());
		if(checkingProduct != null){
			results.put(TagNames.SCRAP_STATUS.name(), !checkingProduct.isScrapStatus() && !checkingProduct.isPreheatScrapStatus());
			results.put(TagNames.DEFECT_STATUS.name(), DefectStatus.OUTSTANDING != checkingProduct.getDefectStatus());
			results.put(TagNames.HOLD_STATUS.name(), checkingProduct.getHoldStatus() == HoldStatus.NOT_ON_HOLD.getId());
			results.put(TagNames.REQUIRED_PART.name(), outstandingRequiredPartsNotExistCheck()); 
		}
		return results;
	}
	
	public String notAssociatedWithCarrierCheck(){
		
		List<GtsCarrier> list = ServiceFactory.getDao(GtsCarrierDao.class).findAllByProductId(PropertyService.getPropertyBean(
				SystemPropertyBean.class,processPoint.getProcessPointId()).getTrackingArea(), product.getProductId());
		
		if(list != null && list.size() > 0 ) 
			return list.get(0).getId().getCarrierNumber();
		
		return null;
	}
	
	public boolean validHeadIdNumberCheck(){
		return validateDiecastIdNumber(ProductType.HEAD.name());
	}
	
	public boolean validBlockIdNumberCheck(){
		return validateDiecastIdNumber(ProductType.BLOCK.name());
	}
	
	public boolean validCrankshaftIdNumberCheck(){
		return validateDiecastIdNumber(ProductType.CRANKSHAFT.name());
	}

	public boolean validConrodIdNumberCheck(){
		return validateDiecastIdNumber(ProductType.CONROD.name());
	}
	
	
	public boolean validateDiecastIdNumber(String productType){
		if(StringUtils.isEmpty(product.getProductId())){
			return false;
		}
		
		List<ProductNumberDef> productNumberDefs = getDao(ProductTypeDao.class).findByKey(productType).getProductNumberDefs();
		
		if(!ProductNumberDef.isNumberValid(product.getProductId(), productNumberDefs)){
			return false;
		}

		return true;
	}
	
	public Map<String, Boolean> hcmHeadBlockMcReadyCheck(){
		
		List<ProductNumberDef> numberDefs = new ArrayList<ProductNumberDef>();
		numberDefs.add(ProductNumberDef.DCH);
		boolean numberValid = ProductNumberDef.isNumberValid(product.getProductId(), numberDefs); 
		boolean productExist = productExistCheck();
		
		Map<String, Boolean> checkResults = productStateCheck();
		checkResults.put(TagNames.VALID_PRODUCT_NUMBER_FORMAT.name(), numberValid);
		checkResults.put(TagNames.PRODUCT_EXISTS.name(), productExist);
		
		boolean headBlockNotMachiningOnCheck = headBlockNotMachiningOnCheck();
		checkResults.put(TagNames.NOT_MC_ON.name(), headBlockNotMachiningOnCheck);
		boolean dcOff = atCheck();
		checkResults.put(TagNames.AT_CHECK.name(), dcOff);
		
		Boolean mcReady = true;
		if("HC".equals(ProductNumberDef.DCB.getPlant(product.getProductId()))){
			for(Entry<String, Boolean> e: checkResults.entrySet()){
				mcReady &= e.getValue();
				if(!mcReady) break;
			}
					
			mcReady &= headBlockNotMachiningOnCheck;
			mcReady &= numberValid;
			mcReady &= dcOff;
			mcReady &= productExist;
			
			checkResults.put(TagNames.MC_READY.name(), mcReady);
			checkResults.put(TagNames.NOT_HCM_PRODUCT.name(), false);
			
		} else {
			checkResults.put(TagNames.NOT_HCM_PRODUCT.name(), true);
			mcReady &= numberValid;
			checkResults.put(TagNames.MC_READY.name(), mcReady);
		}
		return checkResults;
	}

	public boolean checkDestinationChange()      
	{
		if (getProduct() instanceof Frame) 
		{
			Frame frame = (Frame) getProduct(); 
			Integer seqNumber = frame.getAfOnSequenceNumber();         
			int prevSeqNumber = seqNumber.intValue() - 1;
			if (prevSeqNumber <= 0)
			{
				prevSeqNumber = 9999;
			}
			List<Frame> frameList= ServiceFactory.getDao(FrameDao.class).findByAfOnSequenceNumberOrderByPlanOffDate(prevSeqNumber, false);
			Frame previousFrame=frameList.get(0);
			String prevVin = previousFrame.getProductId();
			String prevProdSpecCode = previousFrame.getProductSpecCode();
			Logger.getLogger().info( "Previous product ID:" + prevVin + " Previous product spec code:" + prevProdSpecCode);
			Logger.getLogger().info( "Current product ID:" + frame.getProductId() + " Current product spec code:" + frame.getProductSpecCode());
			String dest = frame.getProductSpecCode().substring(4, 5);
			String prevDest = prevProdSpecCode.substring(4, 5);
			String model = frame.getProductSpecCode().substring(1,4);
			String prevModel = prevProdSpecCode.substring(1,4);
			String year = frame.getProductSpecCode().substring(0,1);
			String prevYear = prevProdSpecCode.substring(0,1);
			boolean isChanged = false;
			if (!dest.equals(prevDest) || !model.equals(prevModel) || !year.equals(prevYear)) {
				isChanged = true;
			} 
			return isChanged;
		}else 
			return false;		 
	}
	
	public boolean checkEngineType()
	{
		if (getProduct() instanceof Frame && doEngineCheck()) 
		{
			Frame frame = (Frame) getProduct(); 
			String ein = frame.getEngineSerialNo();
			Engine engine = null;
			if (ein != null) {
				engine = getDao(EngineDao.class).findByKey(ein);
			}
			return checkEngineTypeForEngineAssignment(frame, engine,getPropertyBean().isUseAltEngineMto());	
		}
		else
			return false;
	}
	

	//HMIN did not like atCheck name. Since atCheck is used at other plants , HMIN created a new check productAlreadyProcessedCheck
	public boolean  productAlreadyProcessedCheck()
	{
		return atCheck();
	}
	
	public boolean buildResultExistCheck(){
		String partName = PropertyService.getProperty(getProcessPoint().getProcessPointId(), TagNames.RESULT_EXISTS_CHECK_PART_NAME.name());
		ProductBuildResult buildResult = ProductTypeUtil.getProductBuildResultDao(getProduct().getProductType()).findById(getProduct().getProductId(), partName);
		
		return buildResult != null;
	}
	
	public boolean buildResultNotExistCheck(){
		return !buildResultExistCheck();
	}

	
	public boolean deviceDataStatusCheck(){
		String tagName = PropertyService.getProperty(getProcessPoint().getProcessPointId(), TagNames.DEVICE_DATA_STATUS_TAG.name());
		//The status from plc input for this check must be Boolean
		return (Boolean)device.getInputObjectValue(tagName);
	}
	
	
	public List<String> invalidProductCheck() {
		List<String> results = new ArrayList<String>();
		results.addAll(invalidProductIdCheck());
		results.addAll(processedProductCheck());
		return results;
	}
	
	public List<String> invalidProductIdCheck() {
		List<ProductNumberDef> productNumberDefs = getProductTypeData().getProductNumberDefs();
		Boolean isValid = ProductNumberDef.isNumberValid(product.getProductId(), productNumberDefs);
		return addMessage(isValid ? null : "Invalid product id:" + product.getProductId());
	}
	
	public List<String> expectedProductCheck() {
		ProductPropertyBean properties = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPoint().getProcessPointId());
		if(!properties.isCheckExpectedProductId()) return addMessage(null);
		ExpectedProduct expectedProduct = getDao(ExpectedProductDao.class).findByKey(getProcessPoint().getProcessPointId());
		if(expectedProduct == null) return addMessage("No Expected Product Id");
		if (!expectedProduct.getProductId().equalsIgnoreCase(product.getProductId())) 
			return addMessage("Current Product Id does not match the expected product Id : " +  expectedProduct.getProductId());
		else return addMessage(null);
	}
	
	public List<String> processedProductCheck() {
		ProductPropertyBean properties = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPoint().getProcessPointId());
		if(!properties.isCheckProcessedProduct()) return addMessage(null);
		ProductHistoryDao<?, ?> productHistoryDao = ProductTypeUtil.getProductHistoryDao(getProduct().getProductType());
		List<? extends ProductHistory> productHistories = productHistoryDao.findAllByProductAndProcessPoint(product.getProductId(), getProcessPoint().getProcessPointId());
		return addMessage(productHistories.isEmpty()? null : "Product + " + product.getProductId() + " has been processed");
	}
	
	private List<String> addMessage(String message) {
		List<String> messages = new ArrayList<String>();
		if(!StringUtils.isEmpty(message)) messages.add(message);
		return messages;
	}
	
		
	public List<String> diecastMarriedCheck() {
		DieCast head = (DieCast) product;
		List<String> messages = new ArrayList<String>();
		String ein = head.getEngineSerialNumber();
		if (!StringUtils.isEmpty(ein)) {
			messages.add(product.getProductType().name() + " Married to engine: " + ein);
		}
		
		List<String> partNames = PropertyService.getPropertyList(processPoint.getProcessPointId(), TagNames.getDiecastMarriedPartName(product.getProductType()).name());
		for(String partName : partNames) {
			List<InstalledPart> installedParts = 
				getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(partName, head.getProductId());
			
			for(InstalledPart item : installedParts) {
				messages.add("Installed to engine: " + item.getProductId() + " , part name: " + item.getPartName());
			}
		}
		
		return messages;
	}
	
	public boolean isDiecastMarriedCheck(){
		return diecastMarriedCheck().size() == 0;
	}
	
	//Product ID must pass validation of ProductNumberDefs from other plants
	//If fail, must treat as home product with invalid number def.
	public boolean validOtherPlantProductCheck() {
		String notHomeProductNumberDefs = PropertyService.getProperty(processPoint.getProcessPointId(), "OTHER_PLANT_PRODUCT_NUMBER_DEFS");
		List<ProductNumberDef> productNumberDefs = ProductNumberDef.getProductNumberDefs(notHomeProductNumberDefs);
		return ProductNumberDef.isNumberValid(product.getProductId(), productNumberDefs);
	}
	
	public boolean installedPartCureTimeOkCheck(){
		Map<String, Object> resultMap = installedPartCureTimeCheck();
		return resultMap.isEmpty();
	}
	
	public Map<String, Object> installedPartCureTimeCheck(){
		ProductCheckPropertyBean bean = getPropertyBean();
		String[] parts = bean.getCureTimeCheckParts();
		Map<String, String> cureTimeMax = bean.getCureTimeMax();
		Map<String, String> cureTimeMin = bean.getCureTimeMin();
		
		Map<String, Object> checkResultMap = new LinkedHashMap<String, Object>();
		
		if(parts != null && parts.length != 0) {
			for(String part : parts){
				String msg = checkCureTime(part, CommonUtil.stringToInteger(cureTimeMin == null ? null : cureTimeMin.get(part)), 
						CommonUtil.stringToInteger(cureTimeMax == null ? null : cureTimeMax.get(part)));
				
				if(!StringUtils.isEmpty(msg)) checkResultMap.put(part+":MSG", msg);
			}
		} else {
			putContext(TagNames.ERROR_MESSAGE.name(), "Cure Time Check Parts is not defined.");
		}
		
		return checkResultMap;
	}

	private String checkCureTime(String partName, Integer cureTimeMin, Integer cureTimeMax) {
		String msg = null;
		boolean result = true;
		
		InstalledPart installedPart = getDao(InstalledPartDao.class).findById(product.getProductId(), partName);
		if(installedPart != null && installedPart.isStatusOk() && installedPart.getActualTimestamp() != null) {
			Long elapsedMs = System.currentTimeMillis() - installedPart.getActualTimestamp().getTime();
						
			if(cureTimeMin != null){
				result = elapsedMs > cureTimeMin * 60 *1000;
				Integer remainingMin = (int)Math.ceil(cureTimeMin - elapsedMs/1000/60);
				
				putContext(partName+Delimiter.DOT +TagNames.REMAINING_CURE_TIME_MIN.name(), remainingMin <= 0 ? 0 : remainingMin);
				putContext(partName+Delimiter.DOT +TagNames.EXCEEDED_CURE_TIME_MIN.name(), remainingMin >= 0 ? 0 : Math.abs(remainingMin));
				
				if(!result)
					msg = partName + " is less than minum cure time:" + cureTimeMin;
			}
			
			if(cureTimeMax != null) {
				boolean resultMax = elapsedMs < cureTimeMax * 60 *1000;
				Integer remainingMax = (int)Math.ceil(cureTimeMax - elapsedMs/1000/60);
				
				putContext(partName+Delimiter.DOT +TagNames.EXCEEDED_CURE_TIME_MAX.name(), remainingMax >= 0 ? 0 : Math.abs(remainingMax));
				putContext(partName+Delimiter.DOT +TagNames.REMAINING_CURE_TIME_MAX.name(), remainingMax <= 0 ? 0 : remainingMax);
				
				if(!resultMax)
					msg = partName + " is greater than maxmium cure time: " + cureTimeMax;
				
				result &= resultMax;
			}
			
			putContext(partName+Delimiter.DOT +TagNames.PASSED_TIME.name(), Math.ceil(elapsedMs/1000/60));
						
		} else {
			result = false;
			putContext(partName+Delimiter.DOT +TagNames.REMAINING_CURE_TIME_MIN.name(),"Unkown"); //compatible to legacy Galc firing bench testing
			putContext(partName+Delimiter.DOT +TagNames.REMAINING_CURE_TIME_MAX.name(),"Unknow");
			msg = partName + " is not installed.";
		} 
		
		putContext(partName+Delimiter.DOT +TagNames.CURE_TIME_RESULT.name(), result);
				
		return msg;
	}

	private void putContext(String key, Object value) {
		if(context != null)
			context.put(key, value);
	}

	public Object getContext(String key) {
		return (context != null ? context.get(key) : null);
	}

	private ProductCheckPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProcessPoint().getProcessPointId());
	}
	
	public List<PartResultData> outstandingMCOperationCheck(String ppList, String filterType, String divisionList) {
		Vector<PartResultData> partResults = new Vector<PartResultData>();
		List<MCInstalledPartDetailDto> installedParts = ServiceFactory.getService(InstalledPartService.class).getOutstandingMCOperations(product.getProductId(), ppList, filterType, divisionList);
				
		for (MCInstalledPartDetailDto installPart : installedParts) {
			PartResultData partResult = new PartResultData();
			partResult.part_Name = installPart.getOperationName();
			partResult.part_Desc = installPart.getOperationDescription();
			partResult.procecssPointId = installPart.getProcessPointId();
						
			if (isInstructionOperation(installPart)) {
				//This is an instruction only operation there are no parts associated
				if (installPart.getInstalledPartStatus() == null || installPart.getInstalledPartStatus() == 0) {	
					if (StringUtils.isBlank(partResult.part_Desc)) partResult.part_Desc = "INSTRUCTION";						
					partResults.add(partResult);								
					continue;
				}
			}
			
			if (isPartScanOperation(installPart)) {
				//Includes a Part Scan 				
					if(!partAlreadyFlagged(partResults, installPart)){
						if (StringUtils.isBlank(partResult.part_Desc)) partResult.part_Desc = "PART SCAN";
						partResults.add(partResult);
					}				
					continue;
			}
			
			if (isMeasurementOperation(installPart)) {
				//Measurements only 
					if (StringUtils.isBlank(partResult.part_Desc)) partResult.part_Desc = "MEASUREMENT";
					partResults.add(partResult);
					continue;
			}
			
			partResults.add(partResult);					
		}
		
		return partResults;
	}
	
	private boolean partAlreadyFlagged(Vector<PartResultData> partResults, MCInstalledPartDetailDto newPart  ){
		for(PartResultData part : partResults){
			if(StringUtils.equalsIgnoreCase(newPart.getOperationName(),part.part_Name)){
				return true;
			}
		}
		
		return false;
	}	
	
	private boolean isPartScanOperation(MCInstalledPartDetailDto installPart){
		if (installPart.getOpType().equals(OperationType.GALC_SCAN.toString()) 
				|| installPart.getOpType().equals(OperationType.GALC_SCAN_WITH_MEAS.toString()) 
				|| installPart.getOpType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL.toString())) {
			return true;
		}
		
		return false;
	}
	
	private boolean isInstructionOperation(MCInstalledPartDetailDto installPart){
		if (installPart.getOpType().equals(OperationType.INSTRUCTION.toString()) 
				|| installPart.getOpType().equals(OperationType.GALC_INSTRUCTION.toString()) 
				|| installPart.getOpType().equals(OperationType.GALC_AUTO_COMPLETE.toString())) {
			return true;
		}
		
		return false;
	}
	
	private boolean isMeasurementOperation(MCInstalledPartDetailDto installPart){
		if (installPart.getOpType().equals(OperationType.GALC_MEAS.toString()) 
				|| installPart.getOpType().equals(OperationType.GALC_MEAS_MANUAL.toString())
				|| installPart.getOpType().equals(OperationType.GALC_SCAN_WITH_MEAS.toString()) 
				|| installPart.getOpType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL.toString())) {
			return true;
		}
		
		return false;
	}
	
	public List<String> legacyShipCheck(){
		LegacyUtil legacyUtil= new LegacyUtil();
		DataContainer dc = new DefaultDataContainer();
		dc.put("productID", getProduct().getProductId());
		List<String> checkResults = legacyUtil.executeCheck(dc, processPoint.getProcessPointId(), ProductCheckType.LEGACY_SHIP_CHECK.toString());
		
		return checkResults;
	}
	
	protected  <T> T getMappedProperty(Map<String, T> propertyMap) {
		T value = null;
		if (propertyMap == null) {
			return value;
		}
		value = propertyMap.get(getProduct().getModelCode());
		if (value == null) {
			value = propertyMap.get("*");
		}
		return value;
	}
	
	public boolean checkEngineTypeForEngineAssignment(Frame frame, Engine engine,boolean useAltEngineMto) {
		if(frame==null || engine==null)
			return false;
		FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
		String vinEngineYmto = StringUtils.trimToEmpty(frameSpec.getEngineMto());
		String altEngineYmto = StringUtils.trimToEmpty(frameSpec.getAltEngineMto());
		List<FrameEngineModelMap> assocEngineYmtoList;
		String engineProductSpecCode=StringUtils.trimToEmpty(engine.getProductSpecCode());
		if(useAltEngineMto && !((vinEngineYmto == null && altEngineYmto==null) || (engineProductSpecCode == null))) {
			if ((vinEngineYmto!=null && vinEngineYmto.equals(engineProductSpecCode) == true) || (altEngineYmto != null && altEngineYmto.equals(engineProductSpecCode)))	return true;			 
		} 
		if (vinEngineYmto != null && engineProductSpecCode != null) {		
			if (vinEngineYmto.equals(engineProductSpecCode)) return true;
		} 	
		if ((assocEngineYmtoList = ServiceFactory.getDao(FrameEngineModelMapDao.class).findAllByFrameYmto(frame.getProductSpecCode())) != null) {
			for (FrameEngineModelMap engineYmto : assocEngineYmtoList ) {
				if (engineYmto.getEngineYmto().equals(engineProductSpecCode)) return true;
			}
		}
		return false;
	}	
	
	/**
	 * uses lot control rules to check for missing/NG parts
	 * @return
	 */
	public List<String> recursiveInstalledPartCheck() {

		List<String> returnList = new ArrayList<String>();
		HashMap<String, List<InstalledPartDetailsDto>> installedPartsMap = null;
		try {
			installedPartsMap = new HashMap<String, List<InstalledPartDetailsDto>>();
		
			getInstalledPartDetails(
				getProduct().getProductId(),
				getProduct().getProductType(),
				getProduct().getProductSpecCode(),
				installedPartsMap);
			
			Logger.getLogger().info("recursiveInstalledPartcheck products: " + installedPartsMap.keySet());
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("Error performing recursiveInstalledPartcheck: " + StringUtils.trimToEmpty(ex.getMessage()));
		}
		
		for (String productDetail: installedPartsMap.keySet()) {
			for(InstalledPartDetailsDto ipDto: installedPartsMap.get(productDetail)) {
				
				// ignore if its not a required part when "Only show required parts" is enabled
				if ((getPropertyBean().isUsePartConfirmFlag() && ipDto.getPartConfirmFlag() == 0) ||
						// installed and good
						(ipDto.isInstalled() && StringUtils.trimToEmpty(ipDto.getStatus()).equals(InstalledPartDetailsDto.STATUS_GOOD))) {
						continue;
				}
				
				returnList.add(productDetail + ": " 
					+ ipDto.getPartName() 
					+ " " + (ipDto.isInstalled()? ipDto.getStatus() : MISSING_PART));
			}

			if(getPropertyBean().isUseExtProductHoldCheck()) {
				// Added to verify external product on hold check
				List<String> externalChecks = externalProductOnHoldCheck();
				for(String externalCheck: externalChecks) {
					if(!StringUtils.contains(externalCheck, "Unable to invoke external product check for"))
						returnList.add(productDetail + ": " + externalCheck);
				}
			}
		}
		return returnList;
	}

	/**
	 * recursive method to find installedParts based on lot control rules
	 * 
	 * @param productId
	 * @param productType
	 * @param productSpecCode
	 * @param installedPartsMap
	 * @return
	 */
	private List<InstalledPartDetailsDto> getInstalledPartDetails(String productId, ProductType productType, String productSpecCode, 
			HashMap<String, List<InstalledPartDetailsDto>> installedPartsMap) {
		
		//Get all parts from lot control rules and get all parts for current product id from the process points in the above step.
		List<InstalledPartDetailsDto> installedPartDetails = ServiceFactory.getDao(InstalledPartDao.class).getInstalledPartDetails(
				getProcessPoint().getProcessPointId(),
				productId,
				productType, 
				productSpecCode);
		installedPartsMap.put(productType + " " + productId, installedPartDetails);
		
		for(InstalledPartDetailsDto partDetail: installedPartDetails) {
			Logger.getLogger().info("getInstalledPartDetails: " + partDetail.getPartName() 
					+ " " + partDetail.getProductType() 
					+ " - " + partDetail.getSubProductType());
			
			// Recursively search parts that have sub product types defined.
			if ((partDetail.getProductType() == null || StringUtils.trim(partDetail.getProductType()).equalsIgnoreCase(productType.toString())) 
					&& StringUtils.trimToNull(partDetail.getSubProductType()) != null
					&& StringUtils.trimToNull(partDetail.getPartSerialNumber()) != null) {
				
				List<InstalledPartDetailsDto> subProductParts = getInstalledPartDetails(
						partDetail.getPartSerialNumber(),
						ProductType.getType(partDetail.getSubProductType()),
						getProductSpecCode(partDetail.getPartSerialNumber(), partDetail.getSubProductType()), 
						installedPartsMap);
				installedPartsMap.put(partDetail.getSubProductType() + " " + partDetail.getPartSerialNumber(), subProductParts);
			}
		}
		
		return installedPartDetails;
	}
	
	private String getProductSpecCode(String productId, String productType) {
		return ProductTypeUtil.getProductDao(productType).findBySn(productId, ProductType.getType(productType)).getProductSpecCode();
	}
	
	public List<String> partOnHoldCheck(String productIdPrefix) throws Exception {
		//get all the parts and sub parts first
		String productId = getInstalledPartNames().get(0).trim();
		List<Object[]> subParts = getDao(InstalledPartDao.class).findSubParts(productId, productIdPrefix);
		List<String> holds = new ArrayList<String>();
		
		//for each scanned part and its sub parts, check the hold status
		for(Object[] subPartArray : subParts){
			for(Object subPart : subPartArray){
				if(subPart != null){
					String subPartStr = ((String) subPart).trim();
					//If some subpart is found or subpart is not equals to Scanned serial number, check the HoldResult table.
					if(!NONE_FOUND.equalsIgnoreCase(subPartStr) && !subPartStr.equalsIgnoreCase(productId) ){
						productExistsInHoldList(holds, subPartStr);
					}
				}
			}
			
		}
		
		//check if the scanned serial number is on hold , regardless it has sub parts
		productExistsInHoldList(holds, productId);
		
		return holds;
	}	

	private void productExistsInHoldList(List<String> holds, String subPart) throws Exception{
		List<HoldResult> tmpHoldResults = getDao(HoldResultDao.class).findAllByProductId(subPart);
		if(tmpHoldResults != null && !tmpHoldResults.isEmpty()) {
			for(HoldResult item : tmpHoldResults) {
				if(item.getReleaseFlag() ==0)
					holds.add(format(item.getId().getProductId(),item));
			}
		}
	}
	
	/**
	 * Checker Method which will check if product has KICKOUT_FLAG set or not
	 * Also return at what point it KICKOUT_AT,KICKOUT_LINEID,KICKOUT_PROCESS_POINT. 
	 * Returns the first process/lineid that has kickout at the line
	 * 
	 * @return
	 * Map<String,String>
	 */
	public Map<String, Object> productKickoutCheck(){
		Map<String,Object> results =  new HashMap<String, Object>();
		results.put(TagNames.KICKOUT_FLAG.name(),false);
		
		BaseProduct checkingProduct = ProductTypeUtil.getProductDao(product.getProductType()).findBySn(product.getProductId());
		
		if(checkingProduct != null){
			ProcessPoint kickoutPPID = getProcessPointDao().findFirstKickoutProcessPointForProduct(product.getProductId(), processPoint.getProcessPointId(), processPoint.getSequenceNumber());
			if(null!=kickoutPPID){
				Line tempLine = getLineDao().findByKey(kickoutPPID.getLineId());
				results.put(TagNames.KICKOUT_FLAG.name(),true);
				results.put(TagNames.KICKOUT_AT.name(), getPropertyBean().isKickoutByProcessLevel()?kickoutPPID.getSequenceNumber():tempLine.getLineSequenceNumber());
				results.put(TagNames.KICKOUT_LINEID.name(),tempLine.getLineId());
				results.put(TagNames.KICKOUT_PROCESS_POINT.name(), kickoutPPID.getProcessPointId());
			}
		}
		return results;
	}
	
	public boolean checkSpecChange() {
		try {
			Logger.getLogger().info("Started spec change check: " + product.getProductId());
			SpecCheckProdSequenceType seqType = SpecCheckProdSequenceType.valueOf(getPropertyBean().getSpecCheckProductSequenceType());
			String preProductId = findPreviousProductId(seqType);			
			if(preProductId == null) {
				return true;
			}
			
			// check for model change
			BaseProduct lastProduct = ProductTypeUtil.findProduct(product.getProductType().toString(), preProductId);
			try {
				// Y-x, M-xxx, T-xxx, O-xxx
				String ymto = product.getProductSpecCode().trim().substring(0, 10);
				String ymtoPrevious = lastProduct.getProductSpecCode().trim().substring(0, 10);
				Logger.getLogger().warn("Comparing product spec codes for " + product.getProductId() + " and " + lastProduct.getProductId());
				if(!ymto.equals(ymtoPrevious)) {
					Logger.getLogger().warn("YMTO changed between " + lastProduct.getProductId() + "(" + ymtoPrevious +") and " + product.getProductId() + "(" + ymto + ")");
					return true;
				}
			} catch (Exception ex) {
				Logger.getLogger().warn("Unable to parse product spec codes for " + product.getProductId() + " and " + lastProduct.getProductId());
				return true;
			}

			// check for stragglers
			if (getPropertyBean().isIncludeStragglersForSpecCheck() && stragglerCheck()) {
				return true;
			}

			// check for remake
			if (getPropertyBean().isIncludeRemakesForSpecCheck() && remakeCheck()) {
				return true;
			}
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Could not perform Spec change check due to : " + ex);
			return true;
		}
		return false;
	}

	public boolean stragglerCheck() {
		if (product.getProductType().equals(ProductType.FRAME)) {
			Frame frame = (Frame) product;
			Logger.getLogger().info("Checking if " + product.getProductId() + " is a straggler");
			List<Object[]> productsList = getDao(FrameDao.class).findVinsInKdLotSortByAfOnTimestamp(frame.getKdLotNumber(), getFrameLinePropertyBean().getAfOnProcessPointId(), frame.getProductId());
			if (productsList == null) {
				return false;
			}
			int[] seqArr = new int[productsList.size()];
			int i = 0;
			for (Object[] productData : productsList) {
				if (productData[1] != null) {
					seqArr[i++] = (Integer) productData[1];
				}
			}

			if (!isConsecutive(seqArr, getAfOnMaxSequenceVal())) {
				Logger.getLogger().warn("Vin " + product.getProductId() + " is an AF straggler");
				return true;
			}
		}
		return false;
	}
	
    private boolean isConsecutive(int[] arr, int seqMaxValue)  {
        // Check if all elements present in the set are consecutive
    	long mod = seqMaxValue + 1;
        long prev = -1; 
        for (int curr: arr)  {
        	if (prev == seqMaxValue) {
        		// roll over, set prev to mod value
        		prev = mod;
        	}
            if ((prev != -1) && (curr != (prev + 1) % mod)) {
            	Logger.getLogger().warn("Straggler sequence for VIN " + product.getProductId() + " is: " + Arrays.toString(arr));
                return false;
            } 
            prev = curr;
        } 
        return true;
    }

	private Integer getAfOnMaxSequenceVal() {
		String sequenceName = PropertyService.getProperty(getFrameLinePropertyBean().getAfOnProcessPointId(), SEQUENCE_NAME);		
		SequenceDao sequenceDao = ServiceFactory.getDao(SequenceDao.class);
		Sequence seq = sequenceDao.findByKey(sequenceName);
		Integer seqMaxValue = Integer.MAX_VALUE;
		if (seq != null) {
			seqMaxValue = seq.getEndSeq();
		} else {
			Logger.getLogger().warn("Unable to find max AFON seq value for sequence name " + sequenceName + ", using default: " + seqMaxValue);
		}
		return seqMaxValue;
	}

	public boolean remakeCheck() {
		Logger.getLogger().info("Checking if " + product.getProductId() + " is a remade unit");
		if (getDao(PreProductionLotDao.class).findByKey(product.getProductionLot()).getRemakeFlag().equals("Y")) {
			Logger.getLogger().warn("Vin " + product.getProductId() + " is a remake");
			return true;
		}
		return false;
	}
	
	public List<String> checkSpecChangeList(){
		checkSpecChange();
		return null;
	}
	
	public boolean checkSpecChangeAndHold(){
		boolean changed = checkSpecChange();
		if(changed) holdProduct();
		
		return changed;
		
	}
	
	private String findPreviousProductId(SpecCheckProdSequenceType seqType) {
		switch(seqType){
		case InProcessProduct:
			return findPreviousInProcessproduct(product.getProductId());  
		case ProductSequence:
			return findPreviousInProductSequence(product.getProductId());
		case ExpectedProduct: 
			return findPreviousByExpectedProduct(product.getProductId());
		}
		return null;
	}

	private String findPreviousByExpectedProduct(String productId) {
		 ExpectedProduct expectedProduct = ServiceFactory.getDao(ExpectedProductDao.class).findForProcessPoint(processPoint.getProcessPointId());
		 return expectedProduct == null ? null: expectedProduct.getLastProcessedProduct();
	}

	private String findPreviousInProcessproduct(String nextProductId) {
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		List<InProcessProduct> inProcessProducts = inProcessProductDao.findByNextProductId(nextProductId);
		
		return (inProcessProducts != null && !inProcessProducts.isEmpty()) ?
			inProcessProducts.get(0).getProductId() : null;
	}

	private String findPreviousInProductSequence(String productId) {
		ProductSequenceDao productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
		ExpectedProductPropertyBean bean = PropertyService.getPropertyBean(ExpectedProductPropertyBean.class, processPoint.getProcessPointId());
		String productSeqInPpId = bean.getInProductSequenceId();
		
		ProductSequence productSequence = productSequenceDao.findByKey(new ProductSequenceId(productId, productSeqInPpId));
		ProductSequence previousSeq = productSequenceDao.findPrevProductId(productSequence);
		return (previousSeq != null) ? previousSeq.getId().getProductId() : null;
	}

	private void holdProduct() {
		int holdSource = (context == null) ? 0 : 1;
		if(!getContext().containsKey(TagNames.PRODUCT.name()) && product != null)
			getContext().put(TagNames.PRODUCT.name(),product);
		if(!getContext().containsKey(TagNames.PROCESS_POINT.name()) && processPoint != null)
			getContext().put(TagNames.PROCESS_POINT.name(),processPoint);
		if(!getContext().containsKey(TagNames.ASSOCIATE_ID.name()))
			getContext().put(TagNames.ASSOCIATE_ID.name(), processPoint.getProcessPointId());
		getContext().put(TagNames.HOLD_SOURCE.name(), "" + holdSource);
		getContext().put(TagNames.HOLD_REASON.name(), getPropertyBean().getSpecCheckHoldReason());
		
		DataContainer dc = new DefaultDataContainer();
		dc.putAll(context);
		DataContainer results = ServiceFactory.getService(ProductHoldService.class).execute(dc);
		getContext().putAll(results);
		
	}

	private  Map<Object, Object> getContext() {
		if(context == null)
			context = new HashMap<Object, Object>();
		return context;
	}

	public List<String> checkSpecChangeAndHoldList(){
		checkSpecChangeAndHold();
		return null;
	}
	
	public List<String> externalProductOnHoldCheck() {
		Logger.getLogger().info("Executing: eternal product on hold check " + product.getProductId());		
		ArrayList<String> retList = new ArrayList<String>();
		
		try {
			String response = invokeExtRestService();
			if (StringUtils.isBlank(response)) {
				Logger.getLogger().warn("No data returned for External product check " + product.getProductId());
				retList.add(product.getProductId() + " is not found");
			} else {
				ExtProductCheckDto dto = ToStringUtil.getGson().fromJson(response, ExtProductCheckDto.class);
				for(ExtProductStatusDto status: dto.getProductStatusList()) {
					if (status.isUnitOnHold()) {
						Logger.getLogger().warn("External product check: " + status.getId().getOriginSiteInstance() + "/" + product.getProductId() + " is on hold");
						retList.add(product.getProductId() + " is on hold at site: " + status.getId().getOriginSiteInstance());
					}
				}
			}
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Unable to invoke external product check for " + product.getProductId());
			retList.add("Unable to invoke external product check for " + product.getProductId());
		}
		Logger.getLogger().info("Completed: eternal product on hold check " + product.getProductId());	
		return retList;
	}

	private String invokeExtRestService() {
		String url = getPropertyBean().getExtCheckerRestUrl() + "/products/productId/" + getProduct().getProductId();
		Map<String, String> requestHeaderProps = new HashMap<String, String>();
		requestHeaderProps.put(AUTHORIZATION_HEADER, AUTHORIZATION_METHOD + " " + getPropertyBean().getExtCheckerRestAuthCode());
		
		String response = HttpClient.get(url, HttpURLConnection.HTTP_OK, requestHeaderProps);
		Logger.getLogger().info("External product check response for product " + product.getProductId() +" is " + response);
		return response;
	}
	
	public List<String> notFixedDefects() {	
		List<String> defectResultData=new ArrayList<String>();
		List<QiDefectResult> qiDefectResult=getDao(QiDefectResultDao.class).findAllNotRepairedDefects(product.getProductId());
		if(qiDefectResult.size()>0){
			for(QiDefectResult defectResult:qiDefectResult){
				String dataToDisplay=defectResult.getInspectionPartName()+" "+defectResult.getInspectionPartLocationName()+" "+
						defectResult.getInspectionPartLocation2Name()+" "+defectResult.getInspectionPart2Name()+" "+
						defectResult.getInspectionPart2LocationName()+" "+defectResult.getInspectionPartLocation2Name()+" "+
						defectResult.getInspectionPart3Name()+" "+defectResult.getDefectTypeName()+" "+defectResult.getDefectTypeName2().replaceAll("null", "").replaceAll("\\s+"," ");
				if(defectResult.getKickoutId() > 0) {
					String processPointName = ServiceFactory.getDao(KickoutLocationDao.class).findProcessPointNameForKickout(defectResult.getKickoutId());
					if(processPointName != null) {
						dataToDisplay = dataToDisplay.concat(" Kickout Process Point: " + processPointName);
					}
				}
				defectResultData.add(dataToDisplay);
			}
		}
		return defectResultData;
	}
	
	public List<String> notFixedDefectsByEntryDepartment() {

		List<String> defectResultData = new ArrayList<String>();
		List<QiDefectResult> qiDefectResult = getDao(QiDefectResultDao.class)
				.findAllNotRepairedDefects(product.getProductId());
		List<QiStationPreviousDefect> qiStationPreviousDefects = getDao(QiStationPreviousDefectDao.class)
				.findAllByProcessPoint(processPoint.getProcessPointId());
		List<String> assignedDivs = new ArrayList<String>();
		for (QiStationPreviousDefect qiStationPreviousDefect : qiStationPreviousDefects) {
			assignedDivs.add(qiStationPreviousDefect.getId().getEntryDivisionId());
		}
		for (QiDefectResult defectResult : qiDefectResult) {
			if (assignedDivs.contains(defectResult.getEntryDept())) {
				String dataToDisplay = defectResult.getInspectionPartName() + " "
						+ defectResult.getInspectionPartLocationName() + " "
						+ defectResult.getInspectionPartLocation2Name() + " " + defectResult.getInspectionPart2Name()
						+ " " + defectResult.getInspectionPart2LocationName() + " "
						+ defectResult.getInspectionPartLocation2Name() + " " + defectResult.getInspectionPart3Name()
						+ " " + defectResult.getDefectTypeName() + " "
						+ defectResult.getDefectTypeName2().replaceAll("null", "").replaceAll("\\s+", " ");
				defectResultData.add(dataToDisplay);

			}
		}
		return defectResultData;
	}
	
	public boolean allDefectsFixedCheck() {
		List<QiDefectResult> qiDefectResult = getDao(QiDefectResultDao.class).findAllNotRepairedDefects(product.getProductId());
		return qiDefectResult.isEmpty();
	}
	
	/**
	 * This Checker method will check for the last passing process point 
	 * for the latest record matches the intended process point
	 * 
	 * @return
	 * boolean
	 */
	public boolean lastProcessPointCheck(){
		String processPoints = getPropertyBean().getLastPassingProcessPoint();
		String lastProcessPoint = product.getLastPassingProcessPointId();
		
		if(StringUtils.isNotBlank(processPoints) && StringUtils.isNotBlank(lastProcessPoint)){ 
			StringTokenizer st = new StringTokenizer(processPoints, ","); 
			while(st.hasMoreTokens()) { 
				String processPoint = st.nextToken();
				if(StringUtils.equalsIgnoreCase(StringUtils.trim(lastProcessPoint),StringUtils.trim(processPoint))){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This Checker method will check that all of the external required
	 * parts are installed to the VIN and subproducts.
	 * 
	 * @param product
	 * @return
	 * boolean
	 */
	public boolean externalRequiredPartCheck(){
		boolean partCheck = true;
		Logger.getLogger().info("Executing External Part Check");

		partCheck = checkExternalRequiredParts(product, product.getProductType().toString());

		List<SubAssemblyPartListDto> subParts = getAllSubparts(product);
		for(SubAssemblyPartListDto subPart : subParts) {
			PartName partName = ServiceFactory.getDao(PartNameDao.class).findByKey(subPart.getPartName()); 

			BaseProduct baseProduct = ProductTypeUtil.findProduct(partName.getSubProductType().toString(), subPart.getPartSerialNumber());

			String mainNo = MbpnSpecCodeUtil.getMainNo(baseProduct.getProductSpecCode());
			installedPartNames = ServiceFactory.getDao(PartNameDao.class).findPartNamesForMbpnByMainNo(mainNo);

			partCheck = checkExternalRequiredParts(baseProduct, partName.getSubProductType().toString());
		}
		Logger.getLogger().info("External Part Check complete.");
		return  partCheck;
	}

	private boolean checkExternalRequiredParts(BaseProduct product, String productType) {

		boolean isGoodPart = false;
		boolean matchFound = false;
		String lastPart = "";
		List<ExtRequiredPartSpec> extRequiredParts = null;

		if(ProductTypeUtil.isMbpnProduct(product.getProductType())){
			extRequiredParts = ServiceFactory.getDao(ExtRequiredPartSpecDao.class)
					.findAllActiveRequiredSpecsByPartNames(installedPartNames);
		}else {
			extRequiredParts = ServiceFactory.getDao(ExtRequiredPartSpecDao.class)
					.findAllActiveRequiredSpecsByProductType(productType);
		}

		List<String> partNames = getPartNamesFromDto(extRequiredParts);

		List<InstalledPart> parts = ServiceFactory.getDao(InstalledPartDao.class).
				findAllByProductIdAndPartNames(product.getProductId(), partNames);

		if(extRequiredParts.size() > 0) lastPart = extRequiredParts.get(0).getId().getPartName();

		if(extRequiredParts.size() == 0) matchFound = true;

		for(int x = 0; x < extRequiredParts.size(); x++) {
			if((matchFound) || (extRequiredParts.get(x).getId().getPartName().equals(lastPart))){

				isGoodPart = false;
				matchFound = false;

				for(int y = 0; y < parts.size(); y++) {
					if(extRequiredParts.get(x).getId().getPartName().equals(parts.get(y).getPartName())&&
							extRequiredParts.get(x).getId().getPartId().equals(parts.get(y).getPartId())) {
						matchFound = true;
						isGoodPart = validateExternalRequiredPart(parts.get(y), extRequiredParts.get(x), product);
						if(!isGoodPart) {
							Logger.getLogger().info("Match was found for " + extRequiredParts.get(x).getId().getPartName() + " but failed part validation check.");
							overallPartsStatus = false;
						}
						lastPart = extRequiredParts.get(x).getId().getPartName();
						break;
					}
				}
				if(!matchFound) Logger.getLogger().info("Match was not found for " + extRequiredParts.get(x).getId().getPartName());
				lastPart = extRequiredParts.get(x).getId().getPartName();

			} else {
				Logger.getLogger().info("overallPartStatus set to false.");
				overallPartsStatus = false;
			}
		}
		return !(matchFound && overallPartsStatus);
	}

	private List<SubAssemblyPartListDto> getAllSubparts(BaseProduct product) {
		List<SubAssemblyPartListDto> subParts = findSubassemblyParts(product.getProductId());	

		return subParts;
	}

	private List<String> getPartNamesFromDto( List<ExtRequiredPartSpec> extRequiredParts) {
		List<String> partNames = new ArrayList<String>();

		for(ExtRequiredPartSpec partSpec : extRequiredParts) {
			partNames.add(partSpec.getId().getPartName());
		}
		return partNames;
	}

	private boolean validateExternalRequiredPart(InstalledPart part, ExtRequiredPartSpec requiredPart, BaseProduct product) {
		RuleExclusionId ruleExclusionId = new RuleExclusionId();
		ruleExclusionId.setPartName(part.getPartName());
		ruleExclusionId.setProductSpecCode(product.getProductSpecCode());
		List<RuleExclusion> excludedRules = ServiceFactory.getDao(RuleExclusionDao.class).findAllByPartName(part.getPartName());

		List<RuleExclusion> rule=	ProductSpecUtil.getMatchedRuleList(product.getProductSpecCode(),
				product.getProductType().toString(), excludedRules, RuleExclusion.class);	
		if(rule.size() > 0) return true;

		if(	part.getInstalledPartStatus() == InstalledPartStatus.getType(InstalledPartStatus.OK.getId()) ||
			part.getInstalledPartStatus() == InstalledPartStatus.getType(InstalledPartStatus.REPAIRED.getId()) ||
			part.getInstalledPartStatus() == InstalledPartStatus.getType(InstalledPartStatus.ACCEPT.getId())) {
			return true;
	}else {
		Logger.getLogger().info(part.getId().getPartName() + " failed external required part validation check!");
		return false;	
	}
}
	
	public boolean letDataCheck() {
		Logger.getLogger().info("Begin LET Data Check.");
		return checkLetData(product);
	}

	private boolean checkLetData(BaseProduct product) {
		String letResultSN = BLANK;
		boolean letDataCheckResult = true;
		RequiredLetPartSpecDetailsDto prevRequiredPartSpecDetail = new RequiredLetPartSpecDetailsDto();

		List<RequiredLetPartSpecDetailsDto> requiredLetPartSpecDetails = getLetPartCheckSpecDao().
				findAllActiveByProductSpecCode(product.getProductSpecCode(), product.getProductId(), product.getProductType().toString(), 1);

		for(RequiredLetPartSpecDetailsDto requiredLetPartSpecDetail : requiredLetPartSpecDetails){

			if(requiredLetPartSpecDetail.getSequenceNumber() == 1){
				
				if(!letResultSN.equals(BLANK)) {
					letDataCheckResult = (letDataCheckResult && installedPartMatchLetResultCheck(prevRequiredPartSpecDetail, letResultSN));
				}
				
				letResultSN = getLetResult(requiredLetPartSpecDetail);
				
			} else {
				letResultSN = letResultSN + getLetResult(requiredLetPartSpecDetail);	
			}
			prevRequiredPartSpecDetail = requiredLetPartSpecDetail;
		}
		RequiredLetPartSpecDetailsDto requiredLetPartSpecDetail = requiredLetPartSpecDetails.get(requiredLetPartSpecDetails.size() - 1);

		letDataCheckResult = (letDataCheckResult && installedPartMatchLetResultCheck(requiredLetPartSpecDetail, letResultSN));

		Logger.getLogger().info("LET Data Check complete.");
		return letDataCheckResult;
	}
	
	private boolean installedPartMatchLetResultCheck(RequiredLetPartSpecDetailsDto requiredLetPartSpecDetail, String letResultSN) {
		boolean letDataCheckResult = false;
		InstalledPartId installedPartId = new InstalledPartId();
		installedPartId.setPartName(requiredLetPartSpecDetail.getPartName());
		installedPartId.setProductId(product.getProductId());

		InstalledPart installedPart = getInstalledPartDao().findByKey(installedPartId);
		boolean isTrimSpecialCharacters  = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId()).isTrimSpecialCharacters();
		
		if(installedPart != null){
			if(installedPart.getPartSerialNumber().trim().contains((isTrimSpecialCharacters)?StringUtil.trimSpecialCharacterFrontAndEnd(letResultSN.trim()):letResultSN.trim())) {
				Logger.getLogger().info("Part " + installedPart.getPartName().trim() + " matches LET result.");
			} else {
				Logger.getLogger().info("Part SN did not match for part: " + installedPart.getPartName() +
						", LET SN: " + letResultSN + " Installed part table SN: " + installedPart.getPartSerialNumber());
				letDataCheckResult = true;
				installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
				getInstalledPartDao().update(installedPart);
				Logger.getLogger().info("Installed part status set to NG for part: " + installedPart.getPartName());
			}
		}else {
			List<RuleExclusion> excludedRules = getRuleExclusionDao()
					.findAllByPartName(requiredLetPartSpecDetail.getPartName());

			List<RuleExclusion> rule = ProductSpecUtil.getMatchedRuleList(product.getProductSpecCode(),
					requiredLetPartSpecDetail.getProductType(), excludedRules, RuleExclusion.class);
			if(rule.size() > 0) {
				Logger.getLogger().info("Part: " + requiredLetPartSpecDetail.getPartName() + " with product spec code of " +
						product.getProductSpecCode() + " is excluded from check.");
				return letDataCheckResult;
			}
			else{
				Logger.getLogger().info("Part: " + requiredLetPartSpecDetail.getPartName().trim() + " is not installed on Product.");
				letDataCheckResult = true;
			}
		}
		return letDataCheckResult;
	}

	private String getLetResult(RequiredLetPartSpecDetailsDto requiredLetPartSpecDetail) {
		return getLetResultDao().getInspectionResult(product.getProductId(),
				requiredLetPartSpecDetail.getInspectionProgramId(),+ requiredLetPartSpecDetail.getInspectionParamId(), 1);
	}

	private List<SubAssemblyPartListDto> findSubassemblyParts(String productId) {
		return ServiceFactory.getDao(InstalledPartDao.class).findSubPartsByProductId(productId);
	}
	
	public ProcessPointDao getProcessPointDao(){
		return ServiceFactory.getDao(ProcessPointDao.class);
	}

	public LineDao getLineDao(){
		return ServiceFactory.getDao(LineDao.class);
	}
	
	public InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);
	}
	
	public LetPartCheckSpecDao getLetPartCheckSpecDao() {
		return ServiceFactory.getDao(LetPartCheckSpecDao.class);
	}
	
	public LetResultDao getLetResultDao() {
		return ServiceFactory.getDao(LetResultDao.class);
	}
	
	public RuleExclusionDao getRuleExclusionDao() {
		return ServiceFactory.getDao(RuleExclusionDao.class);
	}
	
	public List<ExceptionalOut> checkScrappedExceptionalOut(){
		return getDao(ExceptionalOutDao.class).findAllByProductId(product.getProductId());	
	}
		
	public boolean productScrappedCheck(){
		if(getProduct().getDefectStatus() == DefectStatus.SCRAP || getProduct().getDefectStatus() == DefectStatus.PREHEAT_SCRAP) return true;
		return false;
	}	
	
	public boolean productNonRepairableCheck() {
		List<QiDefectResult> qiDefectResult = ServiceFactory.getDao(QiDefectResultDao.class).findAllByProductIdAndCurrentDefectStatus(product.getProductId(), (short) DefectStatus.NON_REPAIRABLE.getId());
			return qiDefectResult != null && !qiDefectResult.isEmpty();
	}
	
	public boolean isModelChanged(){
		String lastProcessedProductId = ProductTypeUtil.getProductHistoryDao(getProduct().getProductType()).getLatestProductId(getProcessPoint().getId());
		if(StringUtils.isBlank(lastProcessedProductId)){
			return false;
		}
		BaseProduct lastProcessedProduct = ProductTypeUtil.getProductDao(getProduct().getProductType()).findBySn(lastProcessedProductId);
		boolean matchModelCode = ProductSpecUtil.matchModelCode(getProduct().getProductSpecCode(), lastProcessedProduct.getProductSpecCode());
		boolean matchModelYearCode = ProductSpecUtil.matchModelYearCode(getProduct().getProductSpecCode(), lastProcessedProduct.getProductSpecCode());
		return !matchModelCode || !matchModelYearCode;
	}
	
	public List<String> modelChangeCheck(){
		List<String> results = new ArrayList<String>();
		if(isModelChanged()){
			results.add(ProductSpecUtil.extractModelYearCode(getProduct().getProductSpecCode()) + ProductSpecUtil.extractModelCode(getProduct().getProductSpecCode()));
		}	
		return results;
	}
	
	public boolean isMcModelChanged(){
		String lastProcessedProductId = ProductTypeUtil.getProductHistoryDao(getProduct().getProductType()).getLatestProductId(getProcessPoint().getId());
		if(StringUtils.isBlank(lastProcessedProductId)){
			return false;
		}
		BaseProduct lastProcessedProduct = ProductTypeUtil.getProductDao(getProduct().getProductType()).findBySn(lastProcessedProductId);
		
		String lastProcessedMcModel = ProductNumberDef.MCTCCASE.getModel(((DieCast)lastProcessedProduct).getMcSerialNumber());
		String currentMcModel = ProductNumberDef.MCTCCASE.getModel(((DieCast)getProduct()).getMcSerialNumber());
		return !currentMcModel.equalsIgnoreCase(lastProcessedMcModel) ;
	}
	
	public List<String> mcModelChangeCheck(){
		List<String> results = new ArrayList<String>();
		if(!(getProduct() instanceof DieCast)){
			results.add("Invalid product type for MC Model Change Check");
			return results;
		}	
		
		if(isMcModelChanged()){
			results.add(ProductNumberDef.MCTCCASE.getModel(((DieCast)getProduct()).getMcSerialNumber()));
		}	
		return results;
	}
	
	public List<String> checkIppTags() {	
		List<String> results = new ArrayList<String>();		
		List<IPPTag> ippTags=getDao(IPPTagDao.class).findAllByProductId(product.getProductId());
		if(ippTags.size()>0){
			StringBuffer ippResultData = new StringBuffer();
			ippResultData.append("There are "+ippTags.size()+" IppTags attached to "+product.getProductId()+" (");
			for(IPPTag ippTag:ippTags){
				ippResultData.append(ippTag.getIppTagNo()+" ");
			}
			ippResultData.append(")");
			results.add(ippResultData.toString());
		}
		return results;
	}
	
    /** @author Madhuri Devi
	 *  @date Nov 14,2018
	 *  Checks if a product has a price.
	 * 
	 *  @return		true when the product has any price
	 * 				false when the product's price is null
	 */				
	
	 public boolean framePriceNotEmptyCheck() {
			Integer effectiveDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));			
				Product product = (Product) getProduct();			
				String mtoc= product.getProductSpecCode();
				if(StringUtils.isEmpty(mtoc)) return false;		
	            String price  =  ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class).getMTOCPrice(mtoc.trim(),effectiveDate);
	            if(price==null) return false;
	            return true;			
		}
	
	public boolean framePriceEmptyCheck() {
		return !framePriceNotEmptyCheck();		
	}
	
	/**
	 * checker to check if the product has been shipped or not.
	 * 
	 * @param productId
	 * @return	boolean 
	 */
	public static boolean isProductShipped(String productId){
		ShippingStatus shippingStatus = ServiceFactory.getDao(ShippingStatusDao.class).findByKey(productId);
		if(shippingStatus==null || ShippingStatusEnum.S90A.equals(ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus()))) return false;
		else return true;
	}
	
	public boolean lotChangeCheck(){
		if (getProduct() instanceof Frame) {
			Frame frame = (Frame) getProduct();	
			String lotChgInd = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId()).getLotChangeIndicator();
			if(StringUtils.isNotBlank(lotChgInd)) {
				ComponentStatus componentStatus = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(processPoint.getProcessPointId(),lotChgInd);
				if (componentStatus != null) {
					if(StringUtils.equalsIgnoreCase(componentStatus.getStatusValue(),frame.getProductionLot()) ||
							StringUtils.equalsIgnoreCase(componentStatus.getStatusValue(),frame.getKdLotNumber())) {
						return false;
					}
				}
				ComponentStatusId cpId =  new ComponentStatusId();
				cpId.setComponentId(processPoint.getProcessPointId());
				cpId.setStatusKey(lotChgInd);
						
				ComponentStatus cpStatus = new ComponentStatus();
				cpStatus.setId(cpId);
				if(StringUtils.equalsIgnoreCase(lotChgInd, "PRODUCTION_LOT")) {
					cpStatus.setStatusValue(frame.getProductionLot());
				} else {
					cpStatus.setStatusValue(frame.getKdLotNumber());				
				}
				ServiceFactory.getDao(ComponentStatusDao.class).save(cpStatus);
				return true;
			}
		}		
		
		return false;
	}
	
	public List<String> destinationCheck() {
		List<String> results = new ArrayList<String>();	
		if (getProduct() instanceof Frame) {
			Frame frame = (Frame) getProduct();
			FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
			results.add(frameSpec.getSalesModelTypeCode());	
		}
		return results;
	}
	
	public List<String> trackingStatusCheck() {
		List<String> results = new ArrayList<String>();	
		if (getProduct() instanceof Frame) {
			Frame frame = (Frame) getProduct();			
			results.add(frame.getTrackingStatus()+" - "+getDao(LineDao.class).findByKey(frame.getTrackingStatus()).getLineDescription());	
		} else if (getProduct() instanceof Engine) {
			Engine engine = (Engine) getProduct();
			results.add(engine.getTrackingStatus()+" - "+getDao(LineDao.class).findByKey(engine.getTrackingStatus()).getLineDescription());
		} else if (getProduct() instanceof MbpnProduct) {
			MbpnProduct product = (MbpnProduct) getProduct();
			results.add(product.getTrackingStatus()+" - "+getDao(LineDao.class).findByKey(product.getTrackingStatus()).getLineDescription());
		}
		return results;
	}
	
	public List<String> uniquePartsCheck() {
		List<String> results = new ArrayList<String>();	
		ProductCheckPropertyBean property = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId());
		String stationCodes = property.getStationDisplayCodes();
		if(StringUtils.isNotBlank(stationCodes)){
			StringTokenizer st = new StringTokenizer(stationCodes,Delimiter.COMMA);
			while(st.hasMoreElements()){
				BuildAttribute attribute = getDao(BuildAttributeDao.class).findById(st.nextToken().trim(), product.getProductSpecCode());
				if(attribute != null)
					results.add(attribute.getId().getAttribute()+" - "+attribute.getAttributeValue());
			}
		}		
		return results;
	}
	
	public List<String> checkEngineMismatchAndHold() {
		List<String> results = new ArrayList<String>();	
		if(product instanceof Frame && doEngineCheck()) {	
			InstalledPartDao ipDao = ServiceFactory.getDao(InstalledPartDao.class);
			String engineNumber = ipDao.getPartSerialNumber(product.getProductId(), PropertyService.getProperty(processPoint.getProcessPointId(), "PART_NAME_FOR_ENGINE", null));
			if(checkEngineMatch(engineNumber)) {
				DataContainer dc = new DefaultDataContainer();
				dc.put("prodUnitIdNo", product.getProductId());
				dc.put("mtcModel", ProductSpecUtil.extractModelYearCode(product.getProductSpecCode()) + ProductSpecUtil.extractModelCode(product.getProductSpecCode()));
				dc.put("mtcType", ProductSpecUtil.extractModelTypeCode(product.getProductSpecCode()));
				dc.put("stopOrdDescText", engineNumber);
				dc.put("procAsmLineNo", product.getProductionLot().substring(5, 6));
				LegacyUtil legacyUtil= new LegacyUtil();
				results.add("Lot Control Engine number: "+engineNumber+" doesn't match with Frame Engine number : "+ ((Frame) product).getEngineSerialNo());
				if(PropertyService.getPropertyBoolean(processPoint.getProcessPointId(), "ENGINE_MISMATCH_HOLD", true)) {
	                List<String> checkResults = legacyUtil.executeCheck(dc, processPoint.getProcessPointId(), ProductCheckType.CHECK_ENGINE_MISMATCH_AND_HOLD.toString());
	                for (String temp : checkResults) {
	                    if(StringUtils.isNotEmpty(temp)) {
	                        results.add(temp);
	                    }
	                }
	            }
			} 		
		}		
		return results;
	}
	
	private boolean checkEngineMatch(String engineNumber) {
		if(StringUtils.isEmpty(engineNumber) || StringUtils.isEmpty(((Frame) product).getEngineSerialNo()) || 
				!StringUtils.equalsIgnoreCase(((Frame) product).getEngineSerialNo(), engineNumber)) {
			return true;
		}			
		return false;
	}
	
	public List<String> productionLotCheck() {
		List<String> results = new ArrayList<String>();
		Frame frame = (Frame) getProduct();
		results.add(frame.getProductionLot());
		return results;
	}

	public List<String> kdLotCheck() {
		List<String> results = new ArrayList<String>();
		Frame frame = (Frame) getProduct();
		results.add(frame.getKdLotNumber());
		String planCode = PropertyService.getProperty(processPoint.getProcessPointId(), TagNames.PLAN_CODE.name(), null);
		int scheduleQty = ServiceFactory.getDao(FrameDao.class).getScheduleQuantity(product.getProductId(), planCode);
		int passQty = ServiceFactory.getDao(FrameDao.class).getPassQuantity(product.getProductId(), processPoint.getProcessPointId());
		results.add("Remaining / Schedule: "+ String.valueOf(scheduleQty - passQty) +" / "+String.valueOf(scheduleQty));
		return results;
	}
	
	/**
	 * Check Purchase Contract for Shipping - Legach style checker
	 * @return List of String
	 */
	public List<String> purchaseContractCheck(){
	    List<String> result = new ArrayList<String>();
		List<PurchaseContract> list = ServiceFactory.getDao(PurchaseContractDao.class).findByProductSpecCode(product.getProductSpecCode());
		if(list == null || list.size() == 0)
			result.add("Failed to find purchase contract for " + product.getProductSpecCode());
		
		return result;
	}
	
    public boolean purchaseContractExistCheck() {
    	return purchaseContractCheck().isEmpty();
    }
    
    /**
     * Check Frame Price for Shipping - Legacy style checker 
     * @return List of String
     */
    public List<String> priceCheck(){
    	List<String> result = new ArrayList<String>();
    	if(!priceAvailableCheck())
    		result.add("Failed price check.");
    	
    	return result;
    }
    
    /**
     * Check price available by production date 
     * @return
     */
    public boolean priceAvailableCheck() { 
    	DailyDepartmentScheduleUtil util = new DailyDepartmentScheduleUtil(processPoint);
    	Date productionDate = util.getProductionDate();
    	String dateStr = new SimpleDateFormat("yyyyMMdd").format(productionDate);
    	
        String price  =  ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class).
        		getPriceForProductionDate(getProduct().getProductSpecCode(), dateStr);
        
        return price != null;
    }
    
    public ProcessPoint getProcessPoint(String processPointId) {
    	return ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
    }
	
    public Line getLine(String lineId) {
    	return ServiceFactory.getDao(LineDao.class).findByKey(lineId);
    }
    
    public Division getDivision(String divisionId) {
    	return ServiceFactory.getDao(DivisionDao.class).findByDivisionId(divisionId);
    }
    
    /**
     * check Engine installed on Frame
     * @return
     */
	public List<String> installedEngineCheck() {
        List<String> result = new ArrayList<String>();
        		
		Frame product = (Frame) getProduct();
		if (StringUtils.isBlank(product.getEngineSerialNo())) {
			 result.add("Missing Engine Serial No.");
			 return result;
		}
		
		Engine engine = getDao(EngineDao.class).findByKey(product.getEngineSerialNo());
		if(engine == null) {
			 result.add("Invalid Engine Serial No assigned.");
			 return result;
		}
		
		if(StringUtils.isBlank(engine.getVin()) || !engine.getVin().equals(product.getProductId())) {
			result.add("Invalid Vin associated with Engine.");
			return result;
		}
						
		String installedEnginePartName = getPropertyBean().getInstalledEnginePartName();
		String installedEngine = getDao(InstalledPartDao.class).getPartSerialNumber(product.getProductId(), installedEnginePartName);
		if(!engine.getProductId().equals(installedEngine))
			result.add("Invalid Engine build result.");
			
		return result;
	}
	
	/**
	 * Verify LET inspection status from the PMQA server
	 * // DataCollection service expects true when NG and false when OK
	 */
	public boolean checkPmqaResults() {
		boolean checkFail=true;
		String processPointId = this.processPoint == null?this.getContext().get("PROCESS_POINT_ID").toString() :this.processPoint.getProcessPointId();
		//Call PMQA REST Service
		List<String> categoryNames = PropertyService.getPropertyList(processPointId, LET_CATEGORY_NAMES);
		
		PmqaDto pmqaDto = getVinBomService().retrievePmqaData(this.product.getProductId());
		if(pmqaDto!=null) {
			//Return true there are no items in the defect_items list
			if(pmqaDto.getDefect_items()==null || pmqaDto.getDefect_items().isEmpty()) {
				checkFail= false;
				Logger.getLogger().info(this.product.getProductId()+"- pmqa Check ("+" No defects)");
				return checkFail;
			} else {
				List<String> inspectionNames = new ArrayList<String>();
				if(categoryNames!=null && !categoryNames.isEmpty()) {//defects exist and  categories configured
					for(String categoryName: categoryNames) {
						inspectionNames = checkDefectsWithCategoryName(pmqaDto.getDefect_items(), categoryName);
						if(inspectionNames.size() > 0) {
							Logger.getLogger().info(this.product.getProductId()+"- pmqa Check (defects found -"+StringUtils.join(inspectionNames, ",")+")");
							return checkFail;//defects may be part of inclusion list or not part of exclusionlist
						}
					}
					
					
				}else {//defects exist and no categories configured
					Logger.getLogger().info(this.product.getProductId()+"- pmqa Check (defects exist, no categories configured)");
					return checkFail;
				}
				//defects exist
				if(inspectionNames.size() == 0) {
					//defects are not part of inclusion list or part of exclusion list
					Logger.getLogger().info(this.product.getProductId()+"- pmqa Check (No matching defects found)");
					checkFail= false;
				}
				return checkFail;
			}
		}else {//no valid response received
			Logger.getLogger().info(this.product.getProductId()+"- pmqa Check (no valid response received from pmqa)");
			return checkFail;
		}
		
	}
	// DataCollection service expects reason List when NG and empty list when OK
	public List<String> pmqaDefectsByCategoryCheck() {
		List<String> result = new ArrayList<String>();
		String processPointId = this.processPoint == null?this.getContext().get("PROCESS_POINT_ID").toString() :this.processPoint.getProcessPointId();
		List<String> categoryNames = PropertyService.getPropertyList(processPointId , LET_CATEGORY_NAMES);
		
		PmqaDto pmqaDto = getVinBomService().retrievePmqaData(this.product.getProductId());
		if(pmqaDto!=null) {
			//Return true there are no items in the defect_items list
			if(pmqaDto.getDefect_items()==null || pmqaDto.getDefect_items().isEmpty()) {
				return result;
			} else  {
				if(categoryNames!=null && !categoryNames.isEmpty()) {
					for(String categoryName: categoryNames) {
						List<String> inspectionNames = checkDefectsWithCategoryName(pmqaDto.getDefect_items(), categoryName);
						if(inspectionNames.size() > 0) {
							result.add(categoryName +"("+StringUtils.join(inspectionNames, ",")+")");
						}
					}
					
					return result;
				}else {
					result.addAll(pmqaDto.getDefect_items());
					return result;
				}
			}
		}else {
			result.add("no data received from pmqa");
			return result;
		}
		
	}
	private List<String> checkDefectsWithCategoryName(List<String> defectItems, String categoryName) {
		List<String> inspectionNames = new ArrayList<String>();

		List<LetCategoryCodeDto> letCategoryCodeDtoList = getVinBomService().getInspectionsByCategoryName(categoryName);
		//Check if a category code is provided
		if(letCategoryCodeDtoList!=null && !letCategoryCodeDtoList.isEmpty()) {
			if(defectItems==null ||defectItems.isEmpty()) {
				return inspectionNames;
			}
			Set<String> inclusionList = new HashSet<String>();
			Set<String> exclusionList = new HashSet<String>();
			for(LetCategoryCodeDto letCategoryCodeDto: letCategoryCodeDtoList) {
				if(letCategoryCodeDto.getInclusive()) {
					inclusionList.addAll(letCategoryCodeDto.getInspectionNames());
				} else {
					exclusionList.addAll(letCategoryCodeDto.getInspectionNames());
				}
			}
			for(String defect_item: defectItems) {
				if(inclusionList.contains(StringUtils.trim(defect_item))) {
					inspectionNames.add(defect_item);
				}
			}
			
			if(!exclusionList.isEmpty()) {
				
				for(String defect_item:defectItems) {
					if(!exclusionList.contains(StringUtils.trim(defect_item))){
						inspectionNames.add(defect_item);
					}
				}
			}
			
		}else {
			inspectionNames.add(categoryName+"- category does not exist");
			
		}
		return inspectionNames;
	}
	
	protected VinBomService getVinBomService() {
		return ServiceFactory.getService(VinBomService.class);
	}
	
	public List<String> vinBomShipStatusCheck() {
		List<String> result = new ArrayList<String>();
		List<String> systemNames = getDao(VinBomPartDao.class).getShipStatusByProductId(product.getProductId());
		//message and concatenated list of system names
		if(systemNames != null && systemNames.size() > 0) {
			String namesString = StringUtils.join(systemNames, ",");
			result.add("Ship Status not OK - Vin has systems that are not complete - "+namesString);
		}
		return result;
	}
	
	public static Map<String, List<PreviousLine>> getPreviousLineMap() {
		if(previousLineMap == null) {
			previousLineMap = new HashMap<String, List<PreviousLine>>();
		}
		return previousLineMap;
	}	
	
	
	/**
	 * Check if product spec of the current product is changed compare to previous product
	 * The check is configurable from property CHECK_SPEC
	 * Y: model year
	 * M: model
	 * T: type
	 * O: option
	 * C: color
	 * I: internal color
	 * @return
	 */
	public static Boolean isSpecChanged(BaseProduct product, String ppid, Logger pLogger) {
		Logger logger = (pLogger == null) ? Logger.getLogger() : pLogger;
		try {
			return doSpechChangeCheck(product, ppid, logger);
		} catch (Exception e) {
			logger.error(e, "Exception on Spec Change Check", e.getMessage());
		}
		return false;
	}
	
	public static Boolean isSpecChanged(BaseProduct product,String ppid) {
		return isSpecChanged(product, ppid, null);
	}

	private static Boolean doSpechChangeCheck(BaseProduct currentProduct, String ppid, Logger logger) {
		if(currentProduct == null) {
			logger.warn("Current product is null!");
			return false;
		}
		
		ExpectedProduct previousVin = ServiceFactory.getDao(ExpectedProductDao.class).findByKey(ppid);
        if(previousVin == null) {
        	logger.warn("previous product from Expected Product is null!");
        	return false;
        }
		
        ProductTypeUtil typeUtil = ProductTypeUtil.getTypeUtil(currentProduct.getProductType());
		BaseProduct previousProduct = typeUtil.getProductDao().findByKey(previousVin.getProductId());
		if(previousProduct == null) {
			logger.warn("Failed to find previous vehicle:", previousVin.getProductId(), "  from database.");
			return false;
		}
		
	
		ProductCheckPropertyBean propertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, ppid);
		logger.info("Check Spec:", propertyBean.getCheckSpec());
		if(StringUtils.isEmpty(propertyBean.getCheckSpec())) {
			if(!currentProduct.getProductSpecCode().equals(previousProduct.getProductSpecCode())){
				logger.info("Spec Changed from current:", currentProduct.getProductId(), "-", currentProduct.getProductSpecCode(), 
						":", " previous:", previousProduct.getProductId(), "-",previousProduct.getProductSpecCode());
				return true;
			}else 
				return false;
		}else if(propertyBean.getCheckSpec().equalsIgnoreCase("DESTINATION")){			
			//Code to do the destination check.. checkDestinationChange
			String dest = currentProduct.getProductSpecCode().substring(4, 5);
			String prevDest = previousProduct.getProductSpecCode().substring(4, 5);
			String model = currentProduct.getProductSpecCode().substring(1,4);
			String prevModel = previousProduct.getProductSpecCode().substring(1,4);
			String year = currentProduct.getProductSpecCode().substring(0,1);
			String prevYear = previousProduct.getProductSpecCode().substring(0,1);

			if (!dest.equals(prevDest) || !model.equals(prevModel) || !year.equals(prevYear)) {
				logger.info("destination Changed from current:", currentProduct.getProductId(), "-", currentProduct.getProductSpecCode(), 
				 ":", " previous:", previousProduct.getProductId(), "-",previousProduct.getProductSpecCode());
				return true;
			} else 
				return false;
			
		}else {
			 if(!(YMTOCI.getString(currentProduct.getProductSpecCode(), propertyBean.getCheckSpec()))
					.equals(YMTOCI.getString(previousProduct.getProductSpecCode(), propertyBean.getCheckSpec()))) {
				 logger.info("Spec Changed from current:", currentProduct.getProductId(), "-", currentProduct.getProductSpecCode(), 
						 ":", " previous:", previousProduct.getProductId(), "-",previousProduct.getProductSpecCode());
				 return true;
			 }else 
				 return false;
		}
	}

	/**
	 * The installedProductCheck was implemented for NAQICS-1356 Parts QICS Check Improvement
	 * The ticket requires the ability to execute checkers for the products that were installed to the main products. An example is 
	 * that to execute checkers for Head, Block, Conrods and etc. that were already installed to an engine. There are some similar 
	 * checkers available but they cannot meet all the requirements from this ticket. 
	 * In order to do installedProductCheck, following items need to be configured
	 * 
	 * 1. INSTALLED_PRODUCT_NAME_MAP
	 *    This property tells the names used to install corresponding products. (the names could be different in different plants)
	 *    Example for Engine line
	 *    Component ID						Property Key						Property Value
	 *  Default_EngineLineProperties	INSTALLED_PRODUCT_NAME_MAP{BLOCK}	BLOCK MC NUMBER
	 *  Default_EngineLineProperties	INSTALLED_PRODUCT_NAME_MAP{HEAD}	HEAD LC S550
	 *  Default_EngineLineProperties	INSTALLED_PRODUCT_NAME_MAP{CONROD}	CONROD LC 1,CONROD LC 2,CONROD LC 3,CONROD LC 4
	 *  
	 *  2. INSTALLED_PRODUCT_CHECK_MAP
	 *     This map defines the checks that will be applied to the corresponding products, see following example
	 *    Component ID					Property Key						Property Value
	 * 	Process Point ID 	INSTALLED_PRODUCT_CHECK_MAP{BLOCK}		VALID_BLOCK_ID_NUMBER_CHECK,PRODUCT_ON_HOLD_CHECK
	 * 	Process Point ID 	INSTALLED_PRODUCT_CHECK_MAP{HEAD}		VALID_HEAD_ID_NUMBER_CHECK,NOT_FIXED_DEFECTS
	 * 	Process Point ID 	INSTALLED_PRODUCT_CHECK_MAP{CONROD}		VALID_CONROD_ID_NUMBER_CHECK,KICKOUT_CHECK
	 * 
	 *  
	 * Some of the checkers require a process point to be set in order to perform the checks for a given product. If you do 
	 * want to configure such a checker, you can use the existing property SUB_PRODUCT_CHECK_PROCESS_POINT to define process
	 * points to be used. See ProductCheckPropertyBean for more details about this property. 
	 * 
	 * Based on the requirement, most of the checkers (on hold, kickout, defect) used by installedProductCheck do not need to 
	 * set up a process point.  
	 *  
	 * @return
	 */

	public Map<String, Object> installedProductCheck() {
		Map<String, Object> results = new HashMap<String, Object>();
		List<BaseProduct> installedProducts = getInstalledProducts();

		for (BaseProduct baseProduct : installedProducts) {
			results.putAll(checkInstalledProduct(baseProduct));
		}
		return results;
	}

	private Map<String, Object> checkInstalledProduct(BaseProduct baseProduct) {
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, String> installedProductChecks = PropertyService.getPropertyMap(getProcessPoint().getProcessPointId(), "INSTALLED_PRODUCT_CHECK_MAP");
		String checks = installedProductChecks.get(baseProduct.getProductType().name());
		Logger.getLogger().info("Start to check installed product " + baseProduct.toString() + "  check types: " + checks);
		
		if (StringUtils.isNotEmpty(checks)) {
			Map<String, String> productCheckProcessPoints = PropertyService.getPropertyMap(getProcessPoint().getProcessPointId(), "SUB_PRODUCT_CHECK_PROCESS_POINT");
			String processPointId = productCheckProcessPoints.get(baseProduct.getProductType().name());
			ProcessPoint processPoint = StringUtils.isEmpty(processPointId) ? null : ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
			Object checkResult = null;
			for (String productCheck : checks.split(",")) {
				ProductCheckUtil util = new ProductCheckUtil(baseProduct, processPoint);
				try {
					checkResult = util.check(productCheck);

					if (!((checkResult instanceof Boolean) && isPass(checkResult))) {
						String resultString = ProductCheckUtil.formatTxt(checkResult, System.getProperty("line.separator"));
						if (StringUtils.isNotEmpty(resultString)) {
							results.put(baseProduct.toString() + " : " + productCheck, resultString);
						}
					}

				} catch (Exception e) {
					Logger.getLogger().warn("Unable to check: " + productCheck + " for " + baseProduct);
					results.put(baseProduct.toString(), "Unable to check: " + productCheck);
				}
			}
		}
		return results;
	}

	private List<BaseProduct> getInstalledProducts() {
		List<BaseProduct> installedProducts = new ArrayList<BaseProduct>();

		for (String typeName : getInstalledProductNameMap().keySet()) {
			ProductType productType = ProductType.getType(typeName);
			List<String> partNames = getInstalledProductNameMap().get(typeName);
			if (productType != null && !partNames.isEmpty()) {
				List<InstalledPart> installedParts = ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(product.getProductId(), partNames);
				for (InstalledPart part : installedParts) {
					BaseProduct baseProduct = ProductTypeUtil.getProductDao(productType).findBySn(part.getPartSerialNumber());
					if (baseProduct != null) {
						installedProducts.add(baseProduct);
					}
				}
			}
		}
		return installedProducts;
	}

	private Map<String, List<String>> getInstalledProductNameMap() {
		if (installedProductNameMap == null) {
			BaseLinePropertyBean propertyBean = null;
			installedProductNameMap = new HashMap<String, List<String>>();
			switch (product.getProductType()) {
			case FRAME:
				propertyBean = getFrameLinePropertyBean();
				break;
			case ENGINE:
				propertyBean = getEngineLinePropertyBean();
				break;
			case MISSION:
				propertyBean = getMissionLinePropertyBean();
				break;
			default:
			}

			if (propertyBean != null) {
				Map<String, String> tempMap = propertyBean.getInstalledProductNameMap();
				if (tempMap != null) {
					for (String key : tempMap.keySet()) {
						installedProductNameMap.put(key, Arrays.asList(tempMap.get(key).split(",")));
					}
				}
			}
		}

		return installedProductNameMap;
	}
	
	private boolean doEngineCheck() {

		Logger.getLogger().info("Starting engine check for product spec code: {} " + product.getProductSpecCode());
		BuildAttribute attribute = getDao(BuildAttributeDao.class).findById(BEV_BUILD_ATTRIBUTE, product.getProductSpecCode());
		boolean shouldPerformEngineCheck = true;
		if (attribute != null) {
			boolean isBevModel = StringUtils.equalsIgnoreCase(attribute.getAttributeValue(), "true");
			shouldPerformEngineCheck = !isBevModel;
		} else {
			Logger.getLogger().info("No build attribute found for product spec code in doEngineCheck: {} " + product.getProductSpecCode());
		}
		Logger.getLogger().info("Ending engine check for product spec code: " + product.getProductSpecCode() + " Returing shouldPerformEngineCheck: " + shouldPerformEngineCheck);
		return shouldPerformEngineCheck;
	}
}