/**
 * 
 */
package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.BuildSheetPartGroupDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.BuildSheetPartGroup;
import com.honda.galc.entity.conf.BuildSheetPartGroupId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.Parameters;

public class BuildSheetPartGroupDaoImpl extends BaseDaoImpl<BuildSheetPartGroup, BuildSheetPartGroupId> implements BuildSheetPartGroupDao {
	
	private final String PART_MATRIX_GROUP = "SELECT BSG.FORM_ID, BAD.ATTRIBUTE_LABEL, BSG.ROW, BSG.COL, G259.ATTRIBUTE_VALUE, BSG.MODEL_GROUP "+
			"FROM GALADM.BUILD_SHEET_PART_ATTR_BY_MODEL_GRP_TBX  BSG JOIN GALADM.GAL259TBX G259  ON G259.ATTRIBUTE = BSG.ATTRIBUTE "+
			"JOIN GALADM.BUILD_ATTRIBUTE_DEF_TBX BAD ON BAD.ATTRIBUTE = G259.ATTRIBUTE "+
			"AND G259.PRODUCT_SPEC_CODE in (?2, SUBSTR(?2, 1, 7), ?4, TRIM(SUBSTR(?4, 1, 20))) "+
			"WHERE BSG.FORM_ID = ?1 AND BSG.MODEL_GROUP in (select MODEL_GROUP from MODEL_GROUPING_TBX where MTC_MODEL = ?3) ORDER BY ROW, COL ";
	
	@Override
	public List<BuildSheetPartGroup> findAllByFormId(String formId, String modelGroup) {
		Parameters params = Parameters.with("id.formId", formId);
			if(StringUtils.isNotBlank(modelGroup))
				params.put("id.modelGroup", modelGroup);
		return findAll(params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> generatePartMatrix(ProductSpec productSpec, String formId) {
		Parameters parameters = Parameters.with("1", formId);
		if(productSpec != null){
			parameters.put("2", productSpec.getProductSpecCode());
			parameters.put("3", productSpec.getModelYearCode() + productSpec.getModelCode());
			if(StringUtils.equals(productSpec.getModelOptionCode(), "00")){
				parameters.put("4", productSpec.getProductSpecCode().replace(productSpec.getProductSpecCode().substring(7, 10), "   "));
			} else {
				parameters.put("4", productSpec.getProductSpecCode());
			}
		}
		return findResultListByNativeQuery(PART_MATRIX_GROUP, parameters);
	}
	
}
