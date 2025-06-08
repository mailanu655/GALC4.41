package com.honda.galc.dao.jpa.product;

import static com.honda.galc.service.ServiceFactory.getNotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.enumtype.LotcontrolNotificationType;
import com.honda.galc.notification.service.ILotControlRuleNotification;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class LotControlRuleDaoImpl extends BaseDaoImpl<LotControlRule, LotControlRuleId> implements LotControlRuleDao{

//	@Autowired
//	private PartNameDao partNameDao;
	
	private final String FIND_ALL_RULES_AND_PROCESS_POINT_NAMES ="select r, p " +
	"from LotControlRule r , Line l, ProcessPoint p, PartName n where  n.productType=:productType and n.partName = r.id.partName";
	private final String FIND_ALL_RULES_AND_PROCESS_POINT_NAMES_FOR_MBPN ="select r, p " +
			"from LotControlRule r , Line l, ProcessPoint p, PartName n where (TRIM(r.id.productSpecCode) LIKE '*%' OR locate(TRIM(r.id.productSpecCode), '%SPEC%') > 0) " +
			"and n.productType=:productType and n.partName = r.id.partName"; 
	private static final String FIND_PROCESS_POINT_ID_BY_MTOC_AND_PART_NAME = "SELECT DISTINCT a.PROCESS_POINT_ID FROM GAL246TBX a WHERE (a.MODEL_YEAR_CODE=?1 or a.MODEL_YEAR_CODE='*') and (a.MODEL_CODE=?2 or a.MODEL_CODE='*') and (a.MODEL_TYPE_CODE=?3 or a.MODEL_TYPE_CODE='*') and (a.MODEL_OPTION_CODE=?4 or a.MODEL_OPTION_CODE='*') and a.PART_NAME=?5";
	private static final String FIND_RULE_BY_MTOC_AND_PART_NAME = "SELECT DISTINCT a.* FROM GAL246TBX a WHERE a.MODEL_YEAR_CODE=?1 and a.MODEL_CODE=?2 and a.MODEL_TYPE_CODE=?3 and a.MODEL_OPTION_CODE=?4 and a.PART_NAME=?5";
	private static final String FIND_RULE_BY_YMTOCI_AND_PART_NAME ="SELECT DISTINCT a.* FROM GAL246TBX a WHERE a.MODEL_YEAR_CODE=?1 and a.MODEL_CODE=?2 and a.MODEL_TYPE_CODE=?3 and a.MODEL_OPTION_CODE=?4 and a.INT_COLOR_CODE=?5 and a.EXT_COLOR_CODE=?6 and a.PART_NAME=?7";
	private static final String FIND_RULE_BY_MBPN_AND_PART_NAME ="SELECT DISTINCT a.* FROM GAL246TBX a WHERE a.PRODUCT_SPEC_CODE=?1 and a.PART_NAME=?2";
	
	private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_SPEC ="select r from LotControlRule r"+
	" where r.id.processPointId = :processPointId ";	
	
	private final static String FIND_ALL_MATCH_RULES = "SELECT r FROM LotControlRule r WHERE r.id.productSpecCode ";
	
	public static final String FIND_PART_NAMES_BY_PROCESS_POINTS_YMTOC = "select distinct r.id.partName from LotControlRule r where r.id.processPointId in( :processPointId) ";
	public static final String FIND_PART_NAMES_BY_PROCESS_POINTS_YMTOC_PCC = "select distinct r.id.partName from LotControlRule r, PartName p where r.id.processPointId in( :processPointId) and r.id.partName = p.partName and r.partConfirmFlag = 1 ";
	private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_PARTNAME ="select r from LotControlRule r"+
			" where r.id.processPointId = :processPointId and r.id.partName = :partName";	
	
	public static final String FIND_ALL_PART_SPECS_BY_RULE = "select ps from PartSpec ps left join fetch ps.measurementSpecs, PartByProductSpecCode r, PartName pn where r.id.partName = ps.id.partName and r.id.partId = ps.id.partId and r.id.partName = pn.partName and pn.productType = :productType ";
	public static final String FIND_ALL_RULE_AND_PART_NAME_DATA = "select r.id " 
			+ ",r.sequenceNumber, r.expectedInstallTime, r.verificationFlag, r.serialNumberScanFlag, r.subId, r.instructionCode, r.serialNumberUniqueFlag, r.strategy, r.deviceId,r.partConfirmFlag, r.qiDefectFlag, r.groupId "
			+ ",r.partConfirmFlag, pn.windowLabel, pn.subProductType ,pn.partVisible ,pn.repairCheck "
			+ "from LotControlRule r, PartName pn, ProcessPoint pp, Line l "
			+ "where r.id.partName = pn.partName "
			+ "and pn.productType = :productType "
			+ "and r.id.processPointId = pp.processPointId and pp.lineId = l.lineId ";	
	public static final String FIND_ALL_PBPSC_DATA = "select r.id, pbpsc.id "
			+ "from PartByProductSpecCode pbpsc, PartName pn, LotControlRule r " 
			+ "where pbpsc.id.partName = pn.partName and pn.productType = :productType "
			+ "and r.id.modelYearCode = pbpsc.id.modelYearCode and r.id.modelCode = pbpsc.id.modelCode and r.id.modelTypeCode = pbpsc.id.modelTypeCode "
			+ "and r.id.modelOptionCode = pbpsc.id.modelOptionCode and r.id.extColorCode = pbpsc.id.extColorCode  and r.id.intColorCode = pbpsc.id.intColorCode "
			+ "and r.id.partName = pbpsc.id.partName and r.id.productSpecCode = pbpsc.id.productSpecCode ";
	
	public static final String FIND_GROUP_ID_BY_PRODUCT_TYPE = "select distinct r.groupId from LotControlRule r where r.partName.productType = :productType order by r.groupId asc";
	private static final String FIND_ALL_BY_GROUP_ID ="select r from LotControlRule r"+
		" where r.partName.productType = :productType and r.id.productSpecCode like :productSpecCode";	
	
	public static final String FIND_RULES_BY_PRODUCT_SPEC_AND_PROCESS_POINT_ID 
		= "select r from LotControlRule r where r.id.processPointId = :processPointId and locate(TRIM(r.id.productSpecCode), '%SPEC%') > 0 and r.sequenceNumber = :sequenceNumber";
	
	public static final String FIND_RULES_BY_PART_SPEC_ID = "select r from PartByProductSpecCode p, LotControlRule r " +
			"where p.id.partId = :partId and p.id.partName = :partName and p.id.modelYearCode = r.id.modelYearCode and p.id.modelCode = r.id.modelCode " +
			"and p.id.modelTypeCode = r.id.modelTypeCode and p.id.modelOptionCode = r.id.modelOptionCode and p.id.extColorCode = r.id.extColorCode " +
			"and p.id.intColorCode = r.id.intColorCode and p.id.partName = r.id.partName";
	

	@Autowired
	private ProcessPointDao processPointDao;
	
	/**
	 * @RGALCDEV-1628
	 * Added NOT_SUPPORTED transactional scope to suspend current transaction
	 * and use an isolated datasource to make this query and then return 
	 * datasource to connection pool.
	 *
	 * @param productSpecCode
	 * @param productType
	 * @return
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)  
	public List<LotControlRule> findAllByProcessPoint(String ppId) {
		
		String[] orderBy = {"sequenceNumber","id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode","id.productSpecCode"};
		List<LotControlRule> allRules = findAll(Parameters.with("id.processPointId", ppId), orderBy, true);
		
		return allRules;
		
	}
	
	@Transactional
    public LotControlRule save(LotControlRule entity) { 
		if(entity.getPartName()!=null) {
			entity.getId().setPartName(entity.getId().getPartName());
			entity.getPartName().setPartName(entity.getPartName().getPartName());
		}
		
		LotControlRule updated= super.save(entity);
		ILotControlRuleNotification service  = getNotificationService(ILotControlRuleNotification.class);
		//make sure to use entity object since it is a unmanaged object
		// another option is to detach the "updated" object
		// detach has to be called to avoid serialization exception
		//since notification is invoked asyncronous
		if(service != null)service.execute(entity, LotcontrolNotificationType.UPDATE);
      return updated;
    } 
	
	@Transactional
    public void remove(LotControlRule entity) {

		super.remove(entity);
        getNotificationService(ILotControlRuleNotification.class).execute(entity, LotcontrolNotificationType.REMOVE);
    }
	
	/**
	 * 
	 */
	public List<LotControlRule> findAllById(LotControlRuleId id) {
		Parameters params = new Parameters(); 
		
		if(id.getProcessPointId() == null && id.getModelYearCode() == null) return new ArrayList<LotControlRule>();
		if(id.getProcessPointId()!= null && id.getProcessPointId().length() > 0) params.put("id.processPointId", id.getProcessPointId());
		if(id.getPartName() != null && id.getPartName().length() > 0) params.put("id.partName", id.getPartName());
		if(id.getModelYearCode() != null && id.getModelYearCode().length() > 0) params.put("id.modelYearCode", id.getModelYearCode());
		if(id.getModelCode() != null && id.getModelCode().length() > 0) params.put("id.modelCode", id.getModelCode());
		if(id.getModelTypeCode() != null && id.getModelTypeCode().length() > 0) params.put("id.modelTypeCode", id.getModelTypeCode());
		if(id.getModelOptionCode() != null && id.getModelOptionCode().length() > 0) params.put("id.modelOptionCode", id.getModelOptionCode());
		if(id.getExtColorCode() != null && id.getExtColorCode().length() > 0) params.put("id.extColorCode", id.getExtColorCode());
		if(id.getIntColorCode() != null && id.getIntColorCode().length() > 0 ) params.put("id.intColorCode", id.getIntColorCode());
		String[] orderBy = {"id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode","sequenceNumber"};
		return findAll(params, orderBy, true);

	}
	
	public List<LotControlRule> findAllById(LotControlRuleId id, String productType) {
		Parameters params = new Parameters(); 
		boolean isMbpnProduct = ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productType));
		if(isMbpnProduct){ 
			StringBuilder sb = new StringBuilder(FIND_ALL_MATCH_RULES);
			sb.append((id.getProductSpecCode().endsWith("%")? "like " : "= "));
			sb.append(":productSpecCode");
			sb.append(((id.getPartName() != null && id.getPartName().length() > 0)? " AND r.id.partName= :partName " : ""));
			sb.append(((id.getProcessPointId()!= null && id.getProcessPointId().length() > 0)? " AND r.id.processPointId = :processPointId " : ""));
			sb.append(" order by r.id.productSpecCode asc");
			
			params.put("productSpecCode", id.getProductSpecCode());
			if(id.getPartName() != null && id.getPartName().length() > 0) params.put("partName", id.getPartName());
			if(id.getProcessPointId()!= null && id.getProcessPointId().length() > 0) params.put("processPointId", id.getProcessPointId());		
			
			return findAllByQuery(sb.toString(), params);
		}else{//backwards compatible support; needed before all the existing product types use productSepcCode
			if(id.getProcessPointId() == null && id.getModelYearCode() == null) return new ArrayList<LotControlRule>();
			if(id.getProcessPointId()!= null && id.getProcessPointId().length() > 0) params.put("id.processPointId", id.getProcessPointId());
			if(id.getPartName() != null && id.getPartName().length() > 0) params.put("id.partName", id.getPartName());
			if(id.getModelYearCode() != null && id.getModelYearCode().length() > 0) params.put("id.modelYearCode", id.getModelYearCode());
			if(id.getModelCode() != null && id.getModelCode().length() > 0) params.put("id.modelCode", id.getModelCode());
			if(id.getModelTypeCode() != null && id.getModelTypeCode().length() > 0) params.put("id.modelTypeCode", id.getModelTypeCode());
			if(id.getExtColorCode() != null && id.getExtColorCode().length() > 0) params.put("id.extColorCode", id.getExtColorCode());
			if(id.getIntColorCode() != null && id.getIntColorCode().length() > 0 ) params.put("id.intColorCode", id.getIntColorCode());
		}
		String[] orderBy = {"id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode","sequenceNumber"};
		return findAll(params, orderBy, true);

	}		
	
	@SuppressWarnings("unchecked")
	public List findAllRulesAndProcessPoints(LotControlRuleId ruleId, String productType) {
		String orderBy = " order by l.lineSequenceNumber asc, p.sequenceNumber asc, r.id.processPointId, r.sequenceNumber asc, r.id ";
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_RULES_AND_PROCESS_POINT_NAMES);
		Parameters params = new Parameters();
		params.put("productType", productType);
		if(!StringUtils.isEmpty(ruleId.getModelYearCode()))		
			addCodeOrCriteria(sb, params, "modelYearCode", ruleId.getModelYearCode());
		if(!StringUtils.isEmpty(ruleId.getModelCode()))
			addCodeOrCriteria(sb, params, "modelCode", ruleId.getModelCode());
		if(!StringUtils.isEmpty(ruleId.getModelTypeCode()))
			addCodeOrCriteria(sb, params, "modelTypeCode", ruleId.getModelTypeCode());
		if(!StringUtils.isEmpty(ruleId.getModelOptionCode()))
			addCodeOrCriteria(sb, params, "modelOptionCode", ruleId.getModelOptionCode());
		if(!StringUtils.isEmpty(ruleId.getIntColorCode()))
			addCodeOrCriteria(sb, params, "intColorCode", ruleId.getIntColorCode());
		if(!StringUtils.isEmpty(ruleId.getExtColorCode()))
			addCodeOrCriteria(sb, params, "extColorCode", ruleId.getExtColorCode());
		
		sb.append(" and r.id.processPointId = p.processPointId and p.lineId = l.lineId ");
		sb.append(orderBy);
		List allRules = findAllByQuery(sb.toString(), params);
		
		return allRules;
	}

	@SuppressWarnings("unchecked")
	public List findAllRulesAndProcessPointsForMbpn(LotControlRuleId ruleId, String productType){
		String orderBy = " order by l.lineSequenceNumber asc, p.sequenceNumber asc, r.sequenceNumber asc";
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_RULES_AND_PROCESS_POINT_NAMES_FOR_MBPN);
		Parameters params = Parameters.with("productType", productType);
		
		sb.append(" and r.id.processPointId = p.processPointId and p.lineId = l.lineId ");
		sb.append(orderBy);
		String sql = sb.toString().replace("%SPEC%", StringUtils.trim(ruleId.getProductSpecCode()));
		List allRules = findAllByQuery(sql, params);
		
		return allRules;
	}
	
	@SuppressWarnings("unchecked")
	public List findAllRulesAndProcessPoints(LotControlRuleId ruleId) {
		String orderBy = " order by l.lineSequenceNumber asc, p.sequenceNumber asc, r.sequenceNumber asc";
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_RULES_AND_PROCESS_POINT_NAMES);
		Parameters params = new Parameters();
	
		if(!StringUtils.isEmpty(ruleId.getModelYearCode()))		
			addCodeOrCriteria(sb, params, "modelYearCode", ruleId.getModelYearCode());
		if(!StringUtils.isEmpty(ruleId.getModelCode()))
			addCodeOrCriteria(sb, params, "modelCode", ruleId.getModelCode());
		if(!StringUtils.isEmpty(ruleId.getModelTypeCode()))
			addCodeOrCriteria(sb, params, "modelTypeCode", ruleId.getModelTypeCode());
		if(!StringUtils.isEmpty(ruleId.getModelOptionCode()))
			addCodeOrCriteria(sb, params, "modelOptionCode", ruleId.getModelOptionCode());
		if(!StringUtils.isEmpty(ruleId.getIntColorCode()))
			addCodeOrCriteria(sb, params, "intColorCode", ruleId.getIntColorCode());
		if(!StringUtils.isEmpty(ruleId.getExtColorCode()))
			addCodeOrCriteria(sb, params, "extColorCode", ruleId.getExtColorCode());
		
		sb.append("and r.id.processPointId = p.processPointId and p.lineId = l.lineId ");
		sb.append(orderBy);
		List allRules = findAllByQuery(sb.toString(), params);
		
		return allRules;
	}
	
	private void addCodeOrCriteria(StringBuilder sb, Parameters params,
		String code, String codeValue) {
		String codeOrWildCard = " and (r.id.CODE = :CODE or r.id.CODE = '*')";
		params.put(code, codeValue);
		sb.append(codeOrWildCard.replace("CODE", code));
	}

	private void addCodeOrCriteriaIfDefined(StringBuilder sb, Parameters params, String code, String codeValue) {
		if (StringUtils.isBlank(codeValue)) {
			return;
		}
		addCodeOrCriteria(sb, params, code, codeValue);
	}
	
	/**
	 * @see LotControlRuleDao#findAllRulesAndProcessPointsAssemble
	 */
	public List<?> findAllRulesAndProcessPointsAssemble(LotControlRuleId ruleId, String productType) {

		List<PartSpec> partSpecs = findAllPartSpecs(ruleId, productType);
		List<?> partByProductSpecData = findAllPartByProducSpecData(ruleId, productType);
		List<?> ruleAndPartNameData = findAllRuleAndPartNameData(ruleId, productType);
		
		List<LotControlRule> rules = assembleRule(ruleAndPartNameData, partByProductSpecData, partSpecs, productType);

		List<String> processPointIds = new ArrayList<String>();
		for (LotControlRule r : rules) {
			if (processPointIds.contains(r.getId().getProcessPointId())) {
				continue;
			}
			processPointIds.add(r.getId().getProcessPointId());
		}
		List<ProcessPoint> processPoints = getProcessPointDao().findAllByIds(processPointIds);
		
		Map<String, ProcessPoint> processPointIx = new HashMap<String, ProcessPoint>();
		for (ProcessPoint pp : processPoints) {
			processPointIx.put(pp.getProcessPointId(), pp);
		}

		List<Object[]> data = new ArrayList<Object[]>();
		for (LotControlRule r : rules) {
			String ppId = r.getId().getProcessPointId();
			ProcessPoint pp = processPointIx.get(ppId);
			Object[] row = {r, pp};
			data.add(row);
		}
		return data;
	}
	
	public List<LotControlRule> findAllForRule(String processPointId){
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		String[] orderBy = {"id.modelYearCode","id.modelCode","id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode"};
		rules =  findAll(Parameters.with("id.processPointId", processPointId), orderBy, true);
		return rules;
	}

	public List<LotControlRule> findAllByModelYear(String year){
		String[] orderBy = {"id.modelYearCode","id.modelCode","id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode"};
		return  findAll(Parameters.with("id.modelYearCode", year), orderBy, true);
	}
	
	public List<LotControlRule> findAllByPartName(PartName partName) {
		Parameters params = new Parameters();
		params.put("partName",partName);
		String[] orderBy = {"partName"};
		return findAll(params, orderBy, true);		
	}
	
	public List<LotControlRule> findAllByPartName(String partName) {
		Parameters params = new Parameters();
		params.put("id.partName",partName);
		String[] orderBy = {"partName"};
		return findAll(params, orderBy, true);	
	}    
    
    public String findProcessPointIdByMtocAndPartName(String mtoc, String partName) {
    	Parameters params = Parameters
    	.with("1", ProductSpec.extractModelYearCode(mtoc))
    	.put("2", ProductSpec.extractModelCode(mtoc))
    	.put("3", ProductSpec.extractModelTypeCode(mtoc))
    	.put("4", ProductSpec.extractModelOptionCode(mtoc))
    	.put("5", partName);
    	return findFirstByNativeQuery(FIND_PROCESS_POINT_ID_BY_MTOC_AND_PART_NAME, params, String.class);
    }
    
    public List<LotControlRule> findRuleByMtocAndPartName(PartByProductSpecCode part, String partName) {
    	Parameters params = Parameters
    	.with("1", part.getId().getModelYearCode())
    	.put("2", part.getId().getModelCode())
    	.put("3", part.getId().getModelTypeCode())
    	.put("4", part.getId().getModelOptionCode())
    	.put("5", partName);
    	return findAllByNativeQuery(FIND_RULE_BY_MTOC_AND_PART_NAME, params);
    }
    
    public List<LotControlRule> findRuleByYmtociAndPartName(PartByProductSpecCode part, String partName) {
    	Parameters params = Parameters
    	.with("1", part.getId().getModelYearCode())
    	.put("2", part.getId().getModelCode())
    	.put("3", part.getId().getModelTypeCode())
    	.put("4", part.getId().getModelOptionCode())
    	.put("5", part.getId().getIntColorCode())
    	.put("6", part.getId().getExtColorCode())
    	.put("7", partName);
    	return findAllByNativeQuery(FIND_RULE_BY_YMTOCI_AND_PART_NAME, params);
    }

	@Override
	public List<LotControlRule> findRuleByMbpnAndPartName(PartByProductSpecCode part, String partName) {
		Parameters params = Parameters
		  .with("1", part.getId().getProductSpecCode())
		  .put("2", partName);
		return findAllByNativeQuery(FIND_RULE_BY_MBPN_AND_PART_NAME, params);
	}

    //finds all lot control rules by process point id and product spec
    public List<LotControlRule> findAllByProcessPointAndProductSpec(String processPointId, ProductSpec spec) {
    	String orderBy = " order by r.sequenceNumber asc";
    	StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PROCESS_POINT_ID_AND_SPEC);
		Parameters params = new Parameters();
		params.put("processPointId", processPointId);
		sb.append(orderBy);
		List<LotControlRule> lotControlRules = findAllByQuery(sb.toString(), params);
		if(lotControlRules != null && !lotControlRules.isEmpty())
		return ProductSpecUtil.getMatchedList(spec.getProductSpecCode(), lotControlRules, LotControlRule.class);
		
		return findAllByQuery(sb.toString(), params);	
    }
    
    public List<LotControlRule> findAllByProductSpecCode(String productSpecCode) {
    	if(StringUtils.isBlank(productSpecCode)) return new ArrayList<LotControlRule>();
    	
    	Parameters params = new Parameters();
    	String year, model, type, option, extColor, intColor;
		
		if(!StringUtils.isEmpty(year = ProductSpec.extractModelYearCode(productSpecCode))) params.put("id.modelYearCode", year);
		if(!StringUtils.isEmpty(model = ProductSpec.extractModelCode(productSpecCode))) params.put("id.modelCode", model);
		if(!StringUtils.isEmpty(type = ProductSpec.extractModelTypeCode(productSpecCode))) params.put("id.modelTypeCode", type);
		if(!StringUtils.isEmpty(option = ProductSpec.extractModelOptionCode(productSpecCode))) params.put("id.modelOptionCode", option);
		if(!StringUtils.isEmpty(extColor = ProductSpec.extractExtColorCode(productSpecCode))) params.put("id.extColorCode", extColor);
		if(!StringUtils.isEmpty(intColor = ProductSpec.extractIntColorCode(productSpecCode))) params.put("id.intColorCode", intColor);
		String[] orderBy = {"id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode","sequenceNumber"};
		return findAll(params, orderBy, true);
    }

    public List<LotControlRule> findAllForProductSpecCode(String productSpecCode) {
    	Parameters parameters = Parameters.with("id.productSpecCode", productSpecCode);
    	return findAll(parameters);
    }

    public List<LotControlRule> findAllByProcessPointAndProductSpec(String processPointId, String productType, ProductSpec spec) {
    	String orderBy = " order by r.sequenceNumber asc";
    	StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PROCESS_POINT_ID_AND_SPEC);
		Parameters params = new Parameters();
		params.put("processPointId", processPointId);
		sb.append(orderBy);
		List<LotControlRule> lotControlRules = findAllByQuery(sb.toString(), params);
		if(lotControlRules != null && !lotControlRules.isEmpty())
		return ProductSpecUtil.getMatchedRuleList(spec.getProductSpecCode(), productType, lotControlRules, LotControlRule.class);
		
		return lotControlRules;	
    }
    
    @SuppressWarnings("unchecked")
	public List<String> findAllPartNames(List<String> processPointId, ProductSpec spec, boolean partConfirmCheck) {
		String sql = FIND_PART_NAMES_BY_PROCESS_POINTS_YMTOC;
		if (partConfirmCheck) {
			sql = FIND_PART_NAMES_BY_PROCESS_POINTS_YMTOC_PCC;
		}
		StringBuilder sb = new StringBuilder(sql);
		Parameters params = Parameters.with("processPointId", processPointId);
		addCodeOrCriteriaIfDefined(sb, params, "modelYearCode", spec.getModelYearCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelCode", spec.getModelCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelTypeCode", spec.getModelTypeCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelOptionCode", spec.getModelOptionCode());
		addCodeOrCriteriaIfDefined(sb, params, "intColorCode", spec.getIntColorCode());
		addCodeOrCriteriaIfDefined(sb, params, "extColorCode", spec.getExtColorCode());
		sb.append(" order by r.id.partName asc");
		return findResultListByQuery(sb.toString(), params);
	}
    
    public List<LotControlRule> findAllByProcessPointIdAndPartName(String processPoint, String partName) {
    	Parameters params = Parameters
    	.with("processPointId", processPoint)
      	.put("partName", partName);
    	return findAllByQuery(FIND_ALL_BY_PROCESS_POINT_ID_AND_PARTNAME, params);
    }
    
	public long count(LotControlRuleId id, List<String> processPointIds) {

		Parameters params = new Parameters();
		params.putIfNotEmpty("id.modelYearCode", id.getModelYearCode());
		params.putIfNotEmpty("id.modelCode", id.getModelCode());
		params.putIfNotEmpty( "id.modelTypeCode", id.getModelTypeCode());
		params.putIfNotEmpty("id.modelOptionCode", id.getModelOptionCode());
		params.putIfNotEmpty("id.extColorCode", id.getExtColorCode());
		params.putIfNotEmpty("id.intColorCode", id.getIntColorCode());
		params.putIfNotEmpty("id.partName", id.getPartName());
		if (processPointIds != null && !processPointIds.isEmpty()) {
			params.put("id.processPointId", processPointIds);
		} else {
			params.putIfNotEmpty("id.processPointId", id.getProcessPointId());
		}
		return count(params);
	}
    
    // === select assembly data === //
	@SuppressWarnings("unchecked")
	protected List<PartSpec> findAllPartSpecs(LotControlRuleId ruleId, String productType) {
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_PART_SPECS_BY_RULE);
		Parameters params = new Parameters();
		params.put("productType", productType);
		addCodeOrCriteriaIfDefined(sb, params, "modelYearCode", ruleId.getModelYearCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelCode", ruleId.getModelCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelTypeCode", ruleId.getModelTypeCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelOptionCode", ruleId.getModelOptionCode());
		addCodeOrCriteriaIfDefined(sb, params, "intColorCode", ruleId.getIntColorCode());
		addCodeOrCriteriaIfDefined(sb, params, "extColorCode", ruleId.getExtColorCode());
		List<?> result = findAllByQuery(sb.toString(), params);
		List<?> list = deduplicate(result);
		for (Object o : list) {
			PartSpec ps = (PartSpec) o;
			ps.setMeasurementSpecs(deduplicate(ps.getMeasurementSpecs()));
		}
		return (List<PartSpec>) list;
	}
	
	protected List<?> findAllRuleAndPartNameData(LotControlRuleId ruleId, String productType) {
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_RULE_AND_PART_NAME_DATA);		
		Parameters params = new Parameters();
		params.put("productType", productType);
		addCodeOrCriteriaIfDefined(sb, params, "modelYearCode", ruleId.getModelYearCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelCode", ruleId.getModelCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelTypeCode", ruleId.getModelTypeCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelOptionCode", ruleId.getModelOptionCode());
		addCodeOrCriteriaIfDefined(sb, params, "intColorCode", ruleId.getIntColorCode());
		addCodeOrCriteriaIfDefined(sb, params, "extColorCode", ruleId.getExtColorCode());
		sb.append(" order by l.lineSequenceNumber asc, pp.sequenceNumber asc, r.id.processPointId, r.sequenceNumber asc, r.id, r.id.partName");
		List<?> list = findAllByQuery(sb.toString(), params);
		return list;
	}
	
	protected List<?> findAllPartByProducSpecData(LotControlRuleId ruleId, String productType) {
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_PBPSC_DATA);		
		Parameters params = new Parameters();
		params.put("productType", productType);
		addCodeOrCriteriaIfDefined(sb, params, "modelYearCode", ruleId.getModelYearCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelCode", ruleId.getModelCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelTypeCode", ruleId.getModelTypeCode());
		addCodeOrCriteriaIfDefined(sb, params, "modelOptionCode", ruleId.getModelOptionCode());
		addCodeOrCriteriaIfDefined(sb, params, "intColorCode", ruleId.getIntColorCode());
		addCodeOrCriteriaIfDefined(sb, params, "extColorCode", ruleId.getExtColorCode());
		sb.append(" order by r.id.processPointId, r.sequenceNumber asc, r.id, pbpsc.id.partName, pbpsc.id.partId ");
		List<?> list = findAllByQuery(sb.toString(), params);
		return list;
	}
	
	public List<LotControlRule> findAllByPartId(String partId, String partName){
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_RULES_BY_PART_SPEC_ID);
		Parameters params = new Parameters();
		params.put("partId", partId);
		params.put("partName", partName);
		return findAllByQuery(sb.toString(), params);
	}
	
    // === assembly api === //
	private List<LotControlRule> assembleRule(List<?> ruleAndPartNameData, List<?> partByProductSpecData, List<PartSpec> partSpecs, String productType) {	
		
		Map<PartSpecId, PartSpec> partSpecIx = new HashMap<PartSpecId, PartSpec>();
		Map<String, List<PartSpec>> partSpecByPartNameIx = new HashMap<String, List<PartSpec>>();		
		
		for (PartSpec ps : partSpecs) {
			partSpecIx.put(ps.getId(), ps);
		}
		for (PartSpec partSpec : partSpecIx.values()) {
			String partName = partSpec.getId().getPartName();
			List<PartSpec> specs = partSpecByPartNameIx.get(partName);
			if (specs == null) {
				specs = new ArrayList<PartSpec>();
				partSpecByPartNameIx.put(partName, specs);
			}
			specs.add(partSpec);
		}
		Map<LotControlRuleId, Set<PartByProductSpecCode>> partByProductSpecIx = createPartByProducSpecIx(partByProductSpecData, partSpecIx);
		Map<String, PartName> partNameIx = createPartNameIx(ruleAndPartNameData, productType, partSpecByPartNameIx);
		List<LotControlRule> rules = assembleRule(ruleAndPartNameData);
		
		for (LotControlRule rule : rules) {
			Set<PartByProductSpecCode> pbpsc = partByProductSpecIx.get(rule.getId());
			if (pbpsc != null && !pbpsc.isEmpty()) {
				rule.getPartByProductSpecs().addAll(pbpsc);
			}
			PartName partName = partNameIx.get(rule.getId().getPartName());
			rule.setPartName(partName);
		}
		return rules;
	}
	
	private List<LotControlRule> assembleRule(List<?> ruleData)  {
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		if (ruleData == null || ruleData.isEmpty()) {
			return rules;
		}
		for (Object item : ruleData) {
			Object[] ar = (Object[]) item;
			LotControlRule rule = assembleRule(ar);
			rules.add(rule);
		}
		return rules;
	}
    
	private LotControlRule assembleRule(Object[] ar) {
		int ix = 0;
		LotControlRule rule = new LotControlRule();
		rule.setId((LotControlRuleId) ar[ix++]);
		rule.setSequenceNumber((Integer)ar[ix++]);
		rule.setExpectedInstallTime((Integer)ar[ix++]);
		rule.setVerificationFlag((Integer)ar[ix++]);
		rule.setSerialNumberScanFlag((Integer)ar[ix++]);
		rule.setSubId((String)ar[ix++]);
		rule.setInstructionCode((String)ar[ix++]);
		rule.setSerialNumberUniqueFlag((Integer)ar[ix++]);
		rule.setStrategy((String)ar[ix++]);
		rule.setDeviceId((String)ar[ix++]);
		rule.setPartConfirmFlag((Integer)ar[ix++]);
		if (ar[ix] != null) {
			rule.setQiDefectFlag((Integer)ar[ix]);
		}
		return rule;
	}

	private Map<String, PartName> createPartNameIx(List<?> partData, String productType, Map<String, List<PartSpec>> partSpecByPartNameIx) {
		Map<String, PartName> partNames = new HashMap<String, PartName>();
		if (partData == null || partData.isEmpty()) {
			return partNames;
		}
		for (Object item : partData) {
			Object[] ar = (Object[]) item;
			LotControlRuleId id = (LotControlRuleId) ar[0];
			String name = id.getPartName();
			if (partNames.containsKey(name)) {
				continue;
			}
			PartName pn = assemblePartName(ar, name, productType);
			if (partSpecByPartNameIx.get(pn.getPartName()) != null) {
				pn.getAllPartSpecs().addAll(partSpecByPartNameIx.get(pn.getPartName()));
			}			
			partNames.put(pn.getPartName(), pn);
		}
		return partNames;
	}
	
	private PartByProductSpecCode assemblePartByProductSpecCode(PartByProductSpecCodeId pbpscId, Map<PartSpecId, PartSpec> partSpecIx) {
		PartByProductSpecCode pbpsc = new PartByProductSpecCode();
		pbpsc.setId(pbpscId);
		PartSpecId psId = new PartSpecId();
		psId.setPartId(pbpscId.getPartId());
		psId.setPartName(pbpscId.getPartName());
		PartSpec partSpec = partSpecIx.get(psId);
		pbpsc.setPartSpec(partSpec);
		return pbpsc;
	}
	
	private Map<LotControlRuleId, Set<PartByProductSpecCode>> createPartByProducSpecIx(List<?> data,  Map<PartSpecId, PartSpec> partSpecIx) {
		Map<LotControlRuleId, Set<PartByProductSpecCode>> pbpscMap = new HashMap<LotControlRuleId, Set<PartByProductSpecCode>>();
		for (Object o : data) {
			Object[] row = (Object[]) o;
			LotControlRuleId rId = (LotControlRuleId) row[0];
			PartByProductSpecCodeId pbpscId = (PartByProductSpecCodeId) row[1];
			Set<PartByProductSpecCode> pbpscs = pbpscMap.get(rId);
			if (pbpscs == null) {
				pbpscs = new LinkedHashSet<PartByProductSpecCode>();
				pbpscMap.put(rId, pbpscs);
			}
			PartByProductSpecCode pbpsc = assemblePartByProductSpecCode(pbpscId, partSpecIx);
			pbpscs.add(pbpsc);
		}
		return pbpscMap;
	}

	private PartName assemblePartName(Object[] ar, String name, String productType) {
		int ix = 13;
		PartName partName = new PartName();
		partName.setPartName(name);
		partName.setProductTypeName(productType);
		partName.setPartConfirmCheck((Integer)ar[ix++]);
		partName.setWindowLabel((String)ar[ix++]);
		partName.setSubProductType((String)ar[ix++]);
		partName.setPartVisible(ar[ix]==null?0:(Integer)ar[ix]);
		ix++;
		partName.setRepairCheck(ar[ix]==null?0:(Integer)ar[ix]);
		return partName;
	}    

    // === utility api === //
	protected <T> List<T> deduplicate(List<T> list) {
		if (list == null || list.size() == 0) {
			return list;
		}
		Set<T> set = new LinkedHashSet<T>();
		set.addAll(list);
		return new ArrayList<T>(set);
	}
    // === get/set === //
	public ProcessPointDao getProcessPointDao() {
		return processPointDao;
	}

	public void setProcessPointDao(ProcessPointDao processPointDao) {
		this.processPointDao = processPointDao;
	}
   
	@SuppressWarnings("unchecked")
	public List<String> findAllGroupIdsByProductType(String productType) {
		return findResultListByQuery(FIND_GROUP_ID_BY_PRODUCT_TYPE, Parameters.with("productType", productType));
	}

	public List<LotControlRule> findAllByProducttyepAndSpecCode(String productType, String specCode) {
		Parameters params = Parameters.with("productType", productType).put("productSpecCode", specCode);
    	return findAllByQuery(FIND_ALL_BY_GROUP_ID, params);
		
	}

	public List<LotControlRule> getLotControlRuleByProductSpecCodeProcessId(String processPointId, String productSpecCode, Integer sequenceNumber) {
		
		Parameters params = new Parameters();
		params.put("id.processPointId", processPointId);
		params.put("sequenceNumber", sequenceNumber);
		
		String sql = FIND_RULES_BY_PRODUCT_SPEC_AND_PROCESS_POINT_ID.replace("%SPEC%", StringUtils.trim(productSpecCode));
		
		return findAllByQuery(sql, params);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)  
	public List<LotControlRule> findRulesByPartNameAndUniqueFlag(String partName, int uniqueFlag){
		Parameters params = new Parameters();
		params.put("id.partName", partName);
		params.put("serialNumberUniqueFlag", uniqueFlag);
		
		List<LotControlRule> allRules = findAll(params);
		return allRules;
	}
}

