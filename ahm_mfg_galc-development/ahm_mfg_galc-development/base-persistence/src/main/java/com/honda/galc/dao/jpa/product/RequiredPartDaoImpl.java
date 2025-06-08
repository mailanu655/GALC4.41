package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.entity.product.RequiredPartId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;
/**
 * 
 * <h3>HcmRequiredPartsDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HcmRequiredPartsDaoImpl description </p>
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
 * @author Paul Chou
 * Jul 15, 2010
 *
 */
public class RequiredPartDaoImpl extends BaseDaoImpl<RequiredPart, RequiredPartId> implements RequiredPartDao{
	 private final String EXCEPT_INSTALLED_PARTS = "EXCEPT (SELECT I.PART_NAME FROM galadm.GAL185TBX I WHERE I.PRODUCT_ID =?3 and I.INSTALLED_PART_STATUS=1)";
	 private final String EXCEPT_INSTALLED_PARTS_FOR_MBPN = " AND R.PART_NAME NOT IN  (SELECT I.PART_NAME FROM galadm.GAL185TBX I WHERE I.PRODUCT_ID =?3 and I.INSTALLED_PART_STATUS=1)";
	 private final String SUB_ID_IS_NULL = "(R.SUB_ID is null or R.SUB_ID = '')";
	 private final String CHECK_REQUIRED_PARTS_FOR_ENGINE = "SELECT TRIM(R.PART_NAME) from galadm.GAL133TBX M, galadm.REQUIRED_PARTS_TBX R " + 
	    "WHERE M.PRODUCT_SPEC_CODE =?1 AND (M.MODEL_YEAR_CODE = R.MODEL_YEAR_CODE OR R.MODEL_YEAR_CODE = '*') AND (M.MODEL_CODE = R.MODEL_CODE OR R.MODEL_CODE = '*') " +
	    "AND R.PROCESS_POINT_ID=?2 AND (M.MODEL_TYPE_CODE = R.MODEL_TYPE_CODE OR R.MODEL_TYPE_CODE = '*') " +
	    "AND (M.MODEL_OPTION_CODE = R.MODEL_OPTION_CODE OR R.MODEL_OPTION_CODE = '*') ";
	 private final String CHECK_REQUIRED_PARTS_FOR_MISSION = "SELECT TRIM(R.PART_NAME) from galadm.MISSION_SPEC_CODE_TBX M, galadm.REQUIRED_PARTS_TBX R " + 
			    "WHERE M.PRODUCT_SPEC_CODE =?1 AND (M.MODEL_YEAR_CODE = R.MODEL_YEAR_CODE OR R.MODEL_YEAR_CODE = '*') AND (M.MODEL_CODE = R.MODEL_CODE OR R.MODEL_CODE = '*') " +
			    "AND R.PROCESS_POINT_ID=?2 AND (M.MODEL_TYPE_CODE = R.MODEL_TYPE_CODE OR R.MODEL_TYPE_CODE = '*') " +
			    "AND (M.MODEL_OPTION_CODE = R.MODEL_OPTION_CODE OR R.MODEL_OPTION_CODE = '*') ";
	 private final String CHECK_REQUIRED_PARTS_FOR_DIECAST="SELECT DISTINCT(TRIM(R.PART_NAME)) from galadm.PRODUCT_SPEC_CODE_TBX M, galadm.REQUIRED_PARTS_TBX R " + 
	    "WHERE M.PRODUCT_SPEC_CODE =?1 AND (M.MODEL_YEAR_CODE = R.MODEL_YEAR_CODE OR R.MODEL_YEAR_CODE = '*') AND (M.MODEL_CODE = R.MODEL_CODE OR R.MODEL_CODE = '*') " +
	    "AND R.PROCESS_POINT_ID=?2 AND (M.MODEL_TYPE_CODE = R.MODEL_TYPE_CODE OR R.MODEL_TYPE_CODE = '*') " +
	    "AND (M.MODEL_OPTION_CODE = R.MODEL_OPTION_CODE OR R.MODEL_OPTION_CODE = '*')";
	 
	 private final String CHECK_REQUIRED_PARTS_FOR_FRAME = "SELECT TRIM(R.PART_NAME) from galadm.GAL144TBX M, galadm.REQUIRED_PARTS_TBX R " + 
	    "WHERE M.PRODUCT_SPEC_CODE =?1 AND (M.MODEL_YEAR_CODE = R.MODEL_YEAR_CODE OR R.MODEL_YEAR_CODE = '*') AND (M.MODEL_CODE = R.MODEL_CODE OR R.MODEL_CODE = '*') " +
	    "AND R.PROCESS_POINT_ID=?2 AND (M.MODEL_TYPE_CODE = R.MODEL_TYPE_CODE OR R.MODEL_TYPE_CODE = '*') " +
	    "AND (M.MODEL_OPTION_CODE = R.MODEL_OPTION_CODE OR R.MODEL_OPTION_CODE = '*') " + 
	    "AND (M.EXT_COLOR_CODE = R.EXT_COLOR_CODE OR R.EXT_COLOR_CODE = '*') " +
	    "AND (M.INT_COLOR_CODE = R.INT_COLOR_CODE OR R.INT_COLOR_CODE = '*') ";
	    
 	private final String CONDITION_DEFAULT_PRODUCT_TYPE = " process_point_id in (select process_point_id" +
 		" from galadm.gal214tbx where division_id = ?1 and process_point_id not in" +
 		" (select component_id from galadm.gal489tbx where property_key = 'PRODUCT_TYPE'" +
 		" and property_value not in (select property_value from galadm.gal489tbx" +
 		" where component_id = 'System_Info' and property_key = 'PRODUCT_TYPE')))";
	 
	private final String CONFITION_PRODUCT_TYPE = " process_point_id in (select a.process_point_id" +
		" from galadm.gal214tbx a, galadm.gal489tbx b where a.division_id = ?1" +
		" and a.process_point_id = b.component_id and b.property_key = 'PRODUCT_TYPE'" +
		" and b.property_value = ?2)";
	
	private final String FIND_ALL_BY_PROCESS_POINT_NAMES_AND_PRODUCTSPEC ="select r " +
			"from RequiredPart r where r.id.processPointId=:processPointId and r.id.modelYearCode = :modelYearCode " +
			"and r.id.modelCode = :modelCode ";
	
	private final String FIND_ALL_BY_PROCESS_POINT_NAMES_AND_MBPN ="SELECT * from galadm.REQUIRED_PARTS_TBX R WHERE R.PROCESS_POINT_ID = ?1 and (TRIM(R.PRODUCT_SPEC_CODE) like '*%' OR locate(TRIM(R.PRODUCT_SPEC_CODE), '%SPEC%') > 0 )";
	
	private static String SELECT_ALL_MATCH_SPEC_CODE = "select r from RequiredPart r where r.id.processPointId = :processPointId and r.id.productSpecCode ";

	private final String CHECK_REQUIRED_PARTS_FOR_MBPN = "SELECT * from galadm.REQUIRED_PARTS_TBX R " + 
	         "WHERE R.PROCESS_POINT_ID = ?1 and (TRIM(R.PRODUCT_SPEC_CODE) like '*%' OR locate(TRIM(R.PRODUCT_SPEC_CODE), '%SPEC%') > 0) ";


	public List<String> findMissingRequiredParts(String specCode,
			String processPointId, ProductType type, String productId, String productSubId) {
				
		if(ProductTypeUtil.isMbpnProduct(type))
			return findMissingRequiredPartsMbpn(specCode, processPointId, type, productId,productSubId);
		else
			return findMissingRequiredPartsMtoc(specCode, processPointId, type, productId,productSubId);
		
	}
	
	public List<String> findMissingRequiredPartsMbpn(String specCode,
			String processPointId, ProductType type, String productId, String productSubId){
		StringBuilder sb = new StringBuilder(CHECK_REQUIRED_PARTS_FOR_MBPN);
		if(!StringUtils.isEmpty(productSubId)){
			sb.append(" AND (").append(SUB_ID_IS_NULL);
			sb.append(" OR ").append(" R.SUB_ID =?2 ").append(") ");
		}
		
		sb.append(EXCEPT_INSTALLED_PARTS_FOR_MBPN);
		
		Parameters params = Parameters.with("1", processPointId);
		params.put("3", productId);
		if(!StringUtils.isEmpty(productSubId))
			params.put("2", productSubId);

		String sql = sb.toString().replace("%SPEC%", StringUtils.trim(specCode));
		
		List<RequiredPart> requiredParts = findAllByNativeQuery(sql, params);
		List<String> parts = new ArrayList<String>();
		for(RequiredPart requiredPart: requiredParts) {
			if(ProductSpecUtil.matchMbpn(specCode, requiredPart.getId().getProductSpecCode())) {
				parts.add(requiredPart.getId().getPartName());
			}
		}
		return parts;
	}
	
	public List<String> findMissingRequiredPartsMtoc(String specCode,
			String processPointId, ProductType type, String productId, String productSubId){
		String sql = generateSql(type, productSubId);

		Parameters params = null;
		if(ProductType.HEAD==type ||ProductType.BLOCK==type||ProductType.CONROD==type||ProductType.CRANKSHAFT==type) {
			if (specCode.length() == 3) {
				params = Parameters.with("1", "~"+specCode);
			} else {
				params = Parameters.with("1", specCode);
			}
		}else {
			params = Parameters.with("1", specCode);		
		}
		params.put("2", processPointId);
		params.put("3", productId);
		if(!StringUtils.isEmpty(productSubId))
			params.put("4", productSubId);
		
		return findAllByNativeQuery(sql, params, String.class);
	}

	private String generateSql(ProductType type, String productSubId) {
		StringBuilder sb = new StringBuilder();
		if(ProductType.ENGINE == type)
			sb.append(CHECK_REQUIRED_PARTS_FOR_ENGINE);		
		else if(ProductType.HEAD==type ||ProductType.BLOCK==type||ProductType.CONROD==type||ProductType.CRANKSHAFT==type)
			sb.append(CHECK_REQUIRED_PARTS_FOR_DIECAST);
		else if(ProductType.MISSION == type)
			sb.append(CHECK_REQUIRED_PARTS_FOR_MISSION);
		else
			sb.append(CHECK_REQUIRED_PARTS_FOR_FRAME);
		if(!StringUtils.isEmpty(productSubId)){
			sb.append(" AND (").append(SUB_ID_IS_NULL);
			sb.append(" OR ").append(" R.SUB_ID =?4 ").append(")");
		}
		
		if (ProductType.HEAD==type) {
			sb.append("EXCEPT (SELECT I.PART_NAME FROM galadm.HEAD_BUILD_RESULTS_TBX I WHERE I.HEAD_ID = ?3 and I.INSTALLED_PART_STATUS=1)");
		}else if (ProductType.BLOCK==type){
			sb.append("EXCEPT (SELECT I.PART_NAME FROM galadm.BLOCK_BUILD_RESULTS_TBX I WHERE I.BLOCK_ID = ?3 and I.INSTALLED_PART_STATUS=1)");
		}else if (ProductType.CRANKSHAFT==type){
			sb.append("EXCEPT (SELECT I.PART_NAME FROM galadm.CRANKSHAFT_BUILD_RESULTS_TBX I WHERE I.CRANKSHAFT_ID = ?3 and I.INSTALLED_PART_STATUS=1)");
		}else if (ProductType.CONROD==type){
			sb.append("EXCEPT (SELECT I.PART_NAME FROM galadm.CONROD_BUILD_RESULTS_TBX I WHERE I.CONROD_ID = ?3 and I.INSTALLED_PART_STATUS=1)");
		}else {
			sb.append(EXCEPT_INSTALLED_PARTS);
		}
		
		return sb.toString();
	}

	public List<RequiredPart> findAllById(RequiredPartId id, String productType) {
		Parameters params = new Parameters(); 
		boolean isMbpnProduct = ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productType));
		String[] orderBy = {"id.modelTypeCode","id.modelOptionCode","id.extColorCode","id.intColorCode"};
		
		if(isMbpnProduct){ 
			StringBuilder sb = new StringBuilder(SELECT_ALL_MATCH_SPEC_CODE);
			sb.append((id.getProductSpecCode().endsWith("%")? "like " : "= "));
			sb.append(":productSpecCode");
			sb.append(" order by r.id.productSpecCode asc");
					
			params.put("processPointId", id.getProcessPointId());
			params.put("productSpecCode", id.getProductSpecCode());
			
			return findAllByQuery(sb.toString(), params);
		} else {//backwards compatible support; needed before all the existing product types use productSepcCode
			if(!StringUtils.isEmpty(id.getProcessPointId())) params.put("id.processPointId", id.getProcessPointId());
			if(!StringUtils.isEmpty(id.getModelYearCode())) params.put("id.modelYearCode", id.getModelYearCode());
			if(!StringUtils.isEmpty(id.getModelCode())) params.put("id.modelCode", id.getModelCode());
			if(!StringUtils.isEmpty(id.getModelTypeCode())) params.put("id.modelTypeCode", id.getModelTypeCode());
			if(!StringUtils.isEmpty(id.getPartName())) params.put("id.partName", id.getPartName());
		}
		if(params.size() == 0) return new ArrayList<RequiredPart>();
		

		return findAll(params, orderBy, true);
	}

	public List<RequiredPart> findAllByProcessPoint(String processPointId) {
		Parameters params = Parameters.with("id.processPointId", processPointId);
		return findAll(params);
	}

	public List<RequiredPart> findAllByIdAndProductType(RequiredPartId id, String division, String productType) {
		if(division == null || productType == null) {
			return new ArrayList<RequiredPart>();
		}
    	StringBuilder builder = new StringBuilder();
		builder.append(buildSqlForId(id));
    	builder.append(CONFITION_PRODUCT_TYPE);
    	
		Parameters params = Parameters.with("1", division);
		params.put("2", productType);
    	
    	return this.findAllByNativeQuery(builder.toString(), params);
	}
	
	public List<RequiredPart> findAllByIdAndDefaultProductType(RequiredPartId id, String division) {
		if(division == null) {
			return new ArrayList<RequiredPart>();
		}
		StringBuilder builder = new StringBuilder();
		builder.append(buildSqlForId(id));
    	builder.append(CONDITION_DEFAULT_PRODUCT_TYPE);

		Parameters params = Parameters.with("1", division);
    	return this.findAllByNativeQuery(builder.toString(), params);
	}

	private String buildSqlForId(RequiredPartId id) {
		StringBuilder builder = new StringBuilder();
		builder.append("select * from galadm.required_parts_tbx where ");
		if(!StringUtils.isEmpty(id.getModelYearCode())) {
			builder.append("model_year_code='");
			builder.append(id.getModelYearCode());
			builder.append("' and ");
		}
		if(!StringUtils.isEmpty(id.getModelCode())) {
			builder.append("model_code='");
			builder.append(id.getModelCode());
			builder.append("' and ");
		}
		if(!StringUtils.isEmpty(id.getModelTypeCode())) {
			builder.append("model_type_code='");
			builder.append(id.getModelTypeCode());
			builder.append("' and ");
		}
		if(!StringUtils.isEmpty(id.getPartName())) {
			builder.append("part_name='");
			builder.append(id.getPartName());
			builder.append("' and ");
		}
		return builder.toString();
	}
	
	public List<RequiredPart> findAllByProcessPointAndProdSpec(String processPoint,BaseProductSpec productSpec){
		return (Mbpn.class.isAssignableFrom(productSpec.getClass())) ?findAllByProcessPointAndProdSpec(processPoint, (Mbpn)productSpec) 
			:findAllByProcessPointAndProdSpec(processPoint,(ProductSpec)productSpec);
	}
	public List<RequiredPart> findAllByProcessPointAndProdSpec(String processPoint,ProductSpec id){
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PROCESS_POINT_NAMES_AND_PRODUCTSPEC);
		Parameters params = new Parameters(); 
	    params.put("processPointId", processPoint);
	    
		if(!StringUtils.isEmpty(id.getModelYearCode())) 
			params.put("modelYearCode", id.getModelYearCode());
		if(!StringUtils.isEmpty(id.getModelCode())) 
			params.put("modelCode", id.getModelCode());
		if(!StringUtils.isEmpty(id.getModelTypeCode()))
			addCodeOrCriteria(sb, params, "modelTypeCode", id.getModelTypeCode());
		if(!StringUtils.isEmpty(id.getModelOptionCode()))
			addCodeOrCriteria(sb, params, "modelOptionCode", id.getModelOptionCode());
		if(!StringUtils.isEmpty(id.getIntColorCode()))
			addCodeOrCriteria(sb, params, "intColorCode", id.getIntColorCode());
		if(!StringUtils.isEmpty(id.getExtColorCode()))
			addCodeOrCriteria(sb, params, "extColorCode", id.getExtColorCode());
			
		return findAllByQuery(sb.toString(), params);
	}
	
	public List<RequiredPart> findAllByProcessPointAndProdSpec(String processPoint,Mbpn specCode){
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PROCESS_POINT_NAMES_AND_MBPN);
		Parameters params = new Parameters(); 
	    params.put("1", processPoint);
	    String sql = sb.toString().replace("%SPEC%", StringUtils.trim(specCode.getProductSpecCode()));
	    // For Wild card support
	    List<RequiredPart> requiredPartsList = new ArrayList<RequiredPart>();
	    for (RequiredPart requiredPart : findAllByNativeQuery(sql, params)) {
	    	 if(ProductSpecUtil.matchMbpn(specCode.getProductSpecCode(), requiredPart.getId().getProductSpecCode())) {
	    		 requiredPartsList.add(requiredPart);
	 	    }
		}
	    return requiredPartsList;
	}
	
	private void addCodeOrCriteria(StringBuilder sb, Parameters params,
			String code, String codeValue) {
			String codeOrWildCard = " and (r.id.CODE = :CODE or r.id.CODE = '*')";
			params.put(code, codeValue);
			sb.append(codeOrWildCard.replace("CODE", code));
		}
	
	public List<RequiredPart> findAllByProductSpecCode(String productSpecCode) {
		Parameters parameters = Parameters.with("id.productSpecCode", productSpecCode);
		return findAll(parameters);
	}
}
