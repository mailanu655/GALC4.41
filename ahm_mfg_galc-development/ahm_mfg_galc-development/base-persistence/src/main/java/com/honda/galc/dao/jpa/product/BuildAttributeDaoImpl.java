package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.ProductSpecUtil;

public class BuildAttributeDaoImpl extends BaseDaoImpl<BuildAttribute, BuildAttributeId> implements BuildAttributeDao{

	private static String SELECT_DISTINCT_ATTRIBUTE = "select distinct ATTRIBUTE, ATTRIBUTE_VALUE from GALADM.GAL259TBX";

	private static String SELECT_DISTINCT_ATTRIBUTE_FOR_TYPE = "select distinct ATTRIBUTE, ATTRIBUTE_VALUE from GALADM.GAL259TBX where (PRODUCT_TYPE = ?1 OR PRODUCT_TYPE is null)";

	private static String SELECT_DISTINCT_ATTRIBUTE_NAME_FOR_TYPE = "select distinct ATTRIBUTE from GALADM.GAL259TBX where (PRODUCT_TYPE = ?1 OR PRODUCT_TYPE is null) order by ATTRIBUTE";

	private static String SELECT_ALL_MATCHING_PRINT_FORM = "select b from BuildAttribute b, PrintForm p where Upper(b.id.attribute) = Upper(p.id.formId)";

	private static String SELECT_ALL_MATCH_SPEC_CODE = "select a from BuildAttribute a where a.id.productSpecCode ";

	private static String SELECT_ALL_MATCH_ATTRIBUTE = "select a from BuildAttribute a where a.id.attribute like ";

	private static String SELECT_ALL_MATCH_ATTRIBUTE_VALUE = "select a.attributeValue from BuildAttribute a where a.attributeValue =  ";

	private static String ORDER_BY_SPEC_CODE = " order by a.id.productSpecCode asc";

	private static String UPDATE_DESCRIPTION = "UPDATE GALADM.GAL259TBX  set  ATTRIBUTE_DESCRIPTION = ?1 where ATTRIBUTE = ?2 and ATTRIBUTE_VALUE = ?3";

	private static final String UPDATE_BUILD_ATTRIBUTE ="update BuildAttribute e set e.id.subId = :newSubId, e.attributeValue = :attributeValue, " + 
											"e.attributeDescription = :attributeDescription, e.updateUser = :updateUser " +
											"where e.id.attribute = :attribute and e.id.productSpecCode = :productSpecCode and e.id.subId = :oldSubId";
	
	private static final String FIND_ATTRIBUTE_VALUE = "select e.ATTRIBUTE_VALUE, e.PRODUCT_SPEC_CODE from  galadm.GAL259TBX  as e where  e.ATTRIBUTE like ?1 "
            + " and e.PRODUCT_SPEC_CODE like ?2" ;

	private static String GET_PARENT_CHILD_DETAILS = "select gal259tbx.PRODUCT_SPEC_CODE as PARENT, gal259tbx.ATTRIBUTE_VALUE from gal259tbx where gal259tbx.ATTRIBUTE = 'COMPONENT_PARTS' and gal259tbx.PRODUCT_SPEC_CODE = ?1"
			+ " and gal259tbx.ATTRIBUTE_VALUE = ?2" + " UNION"
			+ " select gal259tbx.PRODUCT_SPEC_CODE as PARENT, gal259tbx.ATTRIBUTE_VALUE from gal259tbx where ATTRIBUTE = 'COMPONENT_PARTS' and gal259tbx.PRODUCT_SPEC_CODE = substring(?1 ,1,7) || '*'"
			+ " and gal259tbx.ATTRIBUTE_VALUE = ?2";
	
	private static String SELECT_DISTINCT_ATTRIBUTE_NAME = "select distinct ATTRIBUTE from GALADM.GAL259TBX";
	
	public List<BuildAttribute> findAllByAttribute(String attribute) {
		return findAll( Parameters.with("id.attribute",attribute));
	}

	public BuildAttribute findfirstByAttributeAndValue(String attribute, String value) {
		return findFirst( Parameters.with("id.attribute",attribute).put("attributeValue", value));
	}

	@Transactional
	public void updateDescription(String attribute, String value, String attributeDescription){
		executeNativeUpdate(UPDATE_DESCRIPTION,Parameters.with("1", attributeDescription)
				.put("2", attribute).put("3", value));
	}

	public BuildAttribute findById(String attribute, String productSpecCode) {
		return getbestMatchingAttribute(attribute,productSpecCode);
	}

	@SuppressWarnings("unchecked")
	public List<KeyValue<String, String>> findAllDistinctAttributes() {
		List<KeyValue<String, String>> attributes = new ArrayList<KeyValue<String, String>>();
		Query q = entityManager.createNativeQuery(SELECT_DISTINCT_ATTRIBUTE);
		List list =  q.getResultList(); 

		for(int i=0; i< list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			String attribute = StringUtils.trim((String)objects[0]);
			String attributeValue = StringUtils.trim((String)objects[1]);
			attributes.add(new KeyValue<String,String>(attribute, attributeValue));
		}

		return attributes;
	}

	public List<KeyValue<String, String>> findAllDistinctAttributes(String productType) {
		List<KeyValue<String, String>> attributes = new ArrayList<KeyValue<String, String>>();
		List<Object[]> list = executeNative(Parameters.with("1", productType),SELECT_DISTINCT_ATTRIBUTE_FOR_TYPE);

		for(int i=0; i< list.size(); i++) {
			Object[] objects = (Object[]) list.get(i);
			String attribute = StringUtils.trim((String)objects[0]);
			String attributeValue = StringUtils.trim((String)objects[1]);
			attributes.add(new KeyValue<String,String>(attribute, attributeValue));
		}

		return attributes;
	}

	public List<String> findAllDistinctAttributeNames(String productType) {
		List<String> attributes = findAllByNativeQuery(SELECT_DISTINCT_ATTRIBUTE_NAME_FOR_TYPE, Parameters.with("1", productType), String.class);
		return attributes;
	}

	public List<BuildAttribute> findAllMatchId(String productSpecCode, String attribute) {

		if(StringUtils.isEmpty(productSpecCode)) 
			return findAll(Parameters.with("id.attribute",attribute));
		else {
			if(StringUtils.isEmpty(attribute)) {
				return findAllByQuery(getMatchedIdQueryString(productSpecCode) + "'" + productSpecCode + "'" + ORDER_BY_SPEC_CODE);
			}else 
				return findAllByQuery(getMatchedIdQueryString(productSpecCode) + "'" + productSpecCode + "' and a.id.attribute = '" + attribute + "'"
						+ ORDER_BY_SPEC_CODE);
		}
	}

	public List<BuildAttribute> findAllByProductSpecCode(String productSpecCode) {
		Parameters parameters = Parameters.with("id.productSpecCode", productSpecCode);
		return findAll(parameters);
	}

	public List<BuildAttribute> findAllMatchBuildAttributes(String attributePrefix) {

		if(!StringUtils.isEmpty(attributePrefix)) {
			StringBuilder builder = new StringBuilder();
			builder.append("'");
			builder.append(attributePrefix);
			builder.append("%'");

			return findAllByQuery(SELECT_ALL_MATCH_ATTRIBUTE  +  builder.toString());
		} else {
			return findAll();
		}
	}

	public List<BuildAttribute> findAllMatchPrintAttributes() {
		return findAllByQuery(SELECT_ALL_MATCHING_PRINT_FORM);
	}

	public List<String>  findByBuildAttributeValue(String attributeValue) {
		return findByQuery(SELECT_ALL_MATCH_ATTRIBUTE_VALUE + "'" + attributeValue + "'" , String.class);
	}

	public List<BuildAttribute> findAllMatchId(String specCode, String attribute, String productType) {
		Parameters params = new Parameters();
		StringBuilder sb = new StringBuilder();
		sb.append(prepareSelectClause()).append(" where ");
		sb.append(prepareFilterAndParams(specCode, attribute, productType, params));
		sb.append(" order by e.id.productSpecCode asc");
		return findAllByQuery(sb.toString(), params);
	}

	public List<BuildAttribute> findAllMatchIdAndGroup(String specCode, String attribute, String productType, String attributeGroup) {
		Parameters params = new Parameters();
		StringBuilder sb = new StringBuilder();
		sb.append(prepareSelectClause());
		if (attributeGroup != null) {
			sb.append(", BuildAttributeDefinition as f");
		}
		sb.append(" where ");
		sb.append(prepareFilterAndParams(specCode, attribute, productType, params));
		if (attributeGroup != null) {
			sb.append(" and e.id.attribute = f.attribute and f.attributeGroup = :attributeGroup");
			params.put("attributeGroup", attributeGroup);
		}
		sb.append(" order by e.id.productSpecCode asc");
		return findAllByQuery(sb.toString(), params);
	}

	public long count(String specCode, String attribute, String productType) {
		StringBuilder sb = new StringBuilder();
		Parameters params = new Parameters();
		sb.append(prepareCountClause()).append(" where ");
		sb.append(prepareFilterAndParams(specCode, attribute, productType, params));
		long count =  count(sb.toString(), params);
		return count;
	}

	public long count(String specCode, String attribute, String productType, String attributeGroup) {
		StringBuilder sb = new StringBuilder();
		Parameters params = new Parameters();
		sb.append(prepareCountClause());
		if (attributeGroup != null) {
			sb.append(", BuildAttributeDefinition as f");
		}
		sb.append(" where ");
		sb.append(prepareFilterAndParams(specCode, attribute, productType, params));
		if (attributeGroup != null) {
			sb.append(" and e.id.attribute = f.attribute and f.attributeGroup = :attributeGroup");
			params.put("attributeGroup", attributeGroup);
		}
		long count =  count(sb.toString(), params);
		return count;
	}

	// ----------- Util functions -----------------
	private String getMatchedIdQueryString(String productSpecCode) {
		return SELECT_ALL_MATCH_SPEC_CODE  + (productSpecCode.endsWith("%")? "like " : "= ");
	}

	@Transactional
	public void updateBuildAttribute(BuildAttribute buildAttribute, String value, String subId, String description, String userName) {
		executeUpdate(UPDATE_BUILD_ATTRIBUTE,
				Parameters.with("attribute", buildAttribute.getId().getAttribute())
				.put("productSpecCode", buildAttribute.getId().getProductSpecCode())
				.put("oldSubId", buildAttribute.getId().getSubId())
				.put("newSubId", subId)
				.put("attributeValue", value)
				.put("attributeDescription", description)
				.put("updateUser", userName));
	}

	private BuildAttribute getbestMatchingAttribute(String attribute,String productSpecCode){

		List<BuildAttribute> attributes = new ArrayList<BuildAttribute>();

		attributes = findAll(Parameters.with("id.attribute", attribute), new String[]{"id.productSpecCode"});

		return ProductSpecUtil.getMatched(productSpecCode, attributes, BuildAttribute.class);

	}

	protected String prepareFilterAndParams(String specCodeMask, String attribute, String productType, Parameters params) {
		StringBuilder sb = new StringBuilder();
		if (productType != null) {
			sb.append(" (e.productType=:productType or e.productType is null)" );
			params.put("productType", productType);
		} else {
			sb.append(" e.productType is null" );
		}
		if (attribute != null) {
			sb.append(" and e.id.attribute = :attribute");
			params.put("attribute", attribute);
		}
		if (specCodeMask != null) {
			if(!StringUtils.isEmpty(productType) && ProductTypeUtil.isMbpnProduct(productType)){
				sb.append(" and e.id.productSpecCode like :productSpecCode");
				String mask = specCodeMask.endsWith("%")?specCodeMask: specCodeMask+"%";
				params.put("productSpecCode",mask);
			}else{
				String sqlMask = ProductSpecUtil.convertSpecMaskToSqlMask(specCodeMask);
				sb.append(" and e.id.productSpecCode like :productSpecCode");
				params.put("productSpecCode", sqlMask);
			}
		}
		return sb.toString();
	}
	
	public List<String> findAtrributeValueAndSpecForProductSpecAndServiceLot(String serviceLotCombination,String  productSpecCode){
		String serviceLotCombinations = serviceLotCombination+"%";
		Parameters params = Parameters.with("1", serviceLotCombinations );
		params.put("2", productSpecCode);
		
			return findAllByNativeQuery(FIND_ATTRIBUTE_VALUE,params,String.class);
		}

	@Override
	public List<Object[]> findAllParentChildDetails(String productSpecCode, String attributeValue) {
		Parameters params = Parameters.with("1", productSpecCode);
		params.put("2", attributeValue);
		
		return findAllByNativeQuery(GET_PARENT_CHILD_DETAILS, params, Object[].class);
	}

	@Override
	public List<String> findAllDistinctAttributeNames() {
		List<String> attributes = findAllByNativeQuery(SELECT_DISTINCT_ATTRIBUTE_NAME, null, String.class);
		return attributes;
	} 
}
