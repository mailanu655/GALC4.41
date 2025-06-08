package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.dto.ExtRequiredPartDto;
import com.honda.galc.dto.LetRequiredPartSpecsDto;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.service.Parameters;

public class PartSpecDaoImpl extends BaseDaoImpl<PartSpec, PartSpecId> implements PartSpecDao {

	private static final long serialVersionUID = 1L;

	private static String LIST_OF_PART_SPEC_CODE_DATA = "select parts.* from galadm.SUB_PRODUCT_TBX sp"
			+ " join galadm.gal246tbx rules "
			+ "on (rules.MODEL_YEAR_CODE=substr(sp.PRODUCT_SPEC_CODE,1,1) or rules.MODEL_YEAR_CODE='*')"
			+ "and (rules.MODEL_CODE=substr(sp.PRODUCT_SPEC_CODE,2,3) or rules.MODEL_CODE='*')"
			+ "and (rules.MODEL_TYPE_CODE=substr(sp.PRODUCT_SPEC_CODE,5,3) or rules.MODEL_TYPE_CODE='*')"
			+ "and (rules.MODEL_OPTION_CODE=substr(sp.PRODUCT_SPEC_CODE,8,3) or rules.MODEL_OPTION_CODE='*')"
			+ "and (rules.EXT_COLOR_CODE=substr(sp.PRODUCT_SPEC_CODE,11,10) or rules.EXT_COLOR_CODE='*')"
			+ "and (rules.INT_COLOR_CODE=substr(sp.PRODUCT_SPEC_CODE,21,2) or rules.INT_COLOR_CODE='*')"
			+ " join galadm.gal245tbx pbmtoc on (pbmtoc.MODEL_YEAR_CODE=rules.MODEL_YEAR_CODE)"
			+ "and (pbmtoc.MODEL_CODE = rules.MODEL_CODE)" + "and (pbmtoc.MODEL_TYPE_CODE = rules.MODEL_TYPE_CODE)"
			+ "and (pbmtoc.PART_NAME = rules.PART_NAME)"
			+ " join galadm.gal261tbx partname on partname.PART_NAME = rules.PART_NAME"
			+ " join galadm.PART_SPEC_TBX parts on (parts.PART_ID = pbmtoc.PART_ID)"
			+ "and (parts.PART_NAME = pbmtoc.PART_NAME)"
			+ " join galadm.gal214tbx pp on pp.PROCESS_POINT_ID = rules.PROCESS_POINT_ID"
			+ " join galadm.gal195tbx line on line.LINE_ID = pp.LINE_ID "
			+ " join galadm.gal128tbx div on line.DIVISION_ID = div.DIVISION_ID"
			+ " join galadm.gal214tbx cpp on cpp.PROCESS_POINT_ID=?1 and cpp.PLANT_NAME = line.PLANT_NAME "
			+ " join galadm.gal195tbx cline on cpp.LINE_ID = cline.LINE_ID "
			+ " join galadm.gal128tbx cdiv on cline.DIVISION_ID = cdiv.DIVISION_ID " + "WHERE sp.PRODUCT_ID=?2 "
			+ "and ((coalesce(div.SEQUENCE_NUMBER,0)+1)*1000000+(coalesce(line.LINE_SEQUENCE_NUMBER,0)+1)*1000+"
			+ "(coalesce(pp.SEQUENCE_NUMBER,0)+1)) "
			+ " < ((coalesce(cdiv.SEQUENCE_NUMBER,0)+1)*1000000+(coalesce(cline.LINE_SEQUENCE_NUMBER,0)+1)*1000+"
			+ "(coalesce(cpp.SEQUENCE_NUMBER,0)+1))";

	
	private static String LIST_OF_PARTS_WITH_PARTMARK_DATA = "SELECT p from PartSpec p WHERE (p.partNumber IS NOT NULL AND p.partNumber <> '')"
															+" AND (p.partMark IS NOT NULL AND p.partMark <> '')"
															+ " ORDER BY p.id.partName, p.id.partId";
	
	private static String LIST_OF_PARTS_WITH_PARTNUMBER_AND_ASSIGNED_TO_PROCESSPOINT = "SELECT distinct partspec.part_id,partspec.part_name,partspec.part_number FROM GALADM.PART_SPEC_TBX partspec "
																						+"inner join GALADM.GAL246TBX rules on partspec.part_name = rules.part_name "
																						+"WHERE (partspec.PART_NUMBER is not null OR partspec.PART_NUMBER <> '')";
	
	private static String FIND_ALL_BY_PROCESS_POINT = "select p from PartSpec p where p.id.partName IN " +
			"(select distinct a.id.partName from LotControlRule a where a.id.processPointId = :processPointId)";
	
	private static String FIND_ALL_WITH_EXT_REQUIRED =  "select distinct(p.part_name), "
			+ "case "
			+ "when l.part_name is null then 'false' " 
			+ "else 'true' "
			+ "end as LET_CHECK_BOX, "
			+ "case "
			+ "when e.part_name is null then 'false' " 
			+ "else 'true' "
			+ "end as EXT_CHECK_BOX "
			+ "from PART_SPEC_TBX p " 
			+ "left join LET_PART_CHECK_SPEC_TBX l on p.part_name=l.part_name "
			+ "left join EXT_REQUIRED_PART_SPEC_TBX e on p.part_name=e.part_name "
			+ "order by LET_CHECK_BOX desc ";
	
	private static String FIND_ALL_WITH_LET_REQUIRED = "select l.PART_ID, l.PART_NAME, p.PART_DESCRIPTION, p.PART_SERIAL_NUMBER_MASK, prog.INSPECTION_PGM_NAME, param.INSPECTION_PARAM_NAME, l.SEQUENCE_NUMBER, l.PARAM_TYPE  " 
			+ "from gal714tbx prog, gal715tbx param, LET_PART_CHECK_SPEC_TBX l, part_spec_tbx p "
			+ "where l.INSPECTION_PROGRAM_ID=prog.INSPECTION_PGM_ID "
			+ "and l.INSPECTION_PARAM_ID=param.INSPECTION_PARAM_ID "
			+ "and l.PART_ID=p.PART_ID "
			+ "and l.PART_NAME=p.PART_NAME ";
	
	private static String FIND_BY_PART_NAME_WITH_LET_REQUIRED = "select l.PART_ID, l.PART_NAME, p.PART_DESCRIPTION, p.PART_SERIAL_NUMBER_MASK, prog.INSPECTION_PGM_NAME, param.INSPECTION_PARAM_NAME, l.SEQUENCE_NUMBER, l.PARAM_TYPE " 
			+ "from gal714tbx prog, gal715tbx param, LET_PART_CHECK_SPEC_TBX l, part_spec_tbx p "
			+ "where l.INSPECTION_PROGRAM_ID=prog.INSPECTION_PGM_ID "
			+ "and l.INSPECTION_PARAM_ID=param.INSPECTION_PARAM_ID "
			+ "and l.PART_ID=p.PART_ID "
			+ "and l.PART_NAME=p.PART_NAME "
			+ "and l.PART_NAME = ?1 " 
			+ "order by l.SEQUENCE_NUMBER ASC";
	
	private static String FIND_BY_PART_NAME_WITH_LET_REQUIRED_BY_SPEC = "select l.PART_ID, l.PART_NAME, p.PART_DESCRIPTION, p.PART_SERIAL_NUMBER_MASK, prog.INSPECTION_PGM_NAME, param.INSPECTION_PARAM_NAME, l.SEQUENCE_NUMBER, l.PARAM_TYPE " 
			+ "from gal714tbx prog, gal715tbx param, LET_PART_CHECK_SPEC_TBX l, part_spec_tbx p "
			+ "where l.INSPECTION_PROGRAM_ID=prog.INSPECTION_PGM_ID "
			+ "and l.INSPECTION_PARAM_ID=param.INSPECTION_PARAM_ID "
			+ "and l.PART_ID=p.PART_ID "
			+ "and l.PART_NAME=p.PART_NAME "
			+ "and l.PART_NAME = ?1 " 
			+ "and l.PART_ID = ?2 "
			+ "order by l.SEQUENCE_NUMBER ASC";
	private static final String FIND_ALL_SAME_PART_NAMES = "SELECT DISTINCT PART_NAME FROM PART_SPEC_TBX WHERE PART_NUMBER like ?1 WITH UR FOR READ ONLY";

	public List<PartSpec> findPartByLotCtrRule(LotControlRule rule) {
		Parameters params = prepareLotCtrRuleParameters(rule);
		return findAllByNamedQuery("PartSpec.getPartByLotCtrRule", params);

	}

	public List<PartSpec> findAllByPartName(String partName) {
		return findAll(Parameters.with("id.partName", partName));
	}

	private Parameters prepareLotCtrRuleParameters(LotControlRule rule) {
		Parameters params = Parameters.with("partName", rule.getId().getPartName());
		params.put("modelYear", rule.getId().getModelYearCode());
		params.put("modelCode", rule.getId().getModelCode());
		params.put("modelType", rule.getId().getModelTypeCode());
		params.put("opTionCode", rule.getId().getModelOptionCode());
		params.put("extColor", rule.getId().getExtColorCode());
		params.put("intColor", rule.getId().getIntColorCode());

		return params;
	}

	public List<PartSpec> findProdSpecs(String prodId, String processPointId) {
		Parameters params = Parameters.with("1", processPointId);
		params.put("2", prodId);
		return findAllByNativeQuery(LIST_OF_PART_SPEC_CODE_DATA, params);
	}

	public List<PartSpec> findAllPartsWithPartNumberAndPartMark() {
		// TODO Auto-generated method stub
		return findAllByQuery(LIST_OF_PARTS_WITH_PARTMARK_DATA);
	}
	
	public List<PartSpec> findAllWithPartNumberAndAssignedToProcessPoint(){
		
		return findAllByNativeQuery(LIST_OF_PARTS_WITH_PARTNUMBER_AND_ASSIGNED_TO_PROCESSPOINT, null);
	}
	
	public PartSpec findValueWithPartNameAndPartID(String partName, String partID) {
		
		Parameters params = new Parameters();
		params.put("id.partName", partName);
		params.put("id.partId", partID);
		
		return (PartSpec) findFirst(params);
	}

	public long findNumberOfPartsByImageId(int imageId) {
		Parameters params = Parameters.with("imageId", imageId);
		return count(params);
	}
	
	public List<PartSpec> findAllByProcessPoint(String processPointId) {
		
		Parameters params = new Parameters();
		params.put("processPointId", processPointId);
		
		return findAllByQuery(FIND_ALL_BY_PROCESS_POINT, params);
	}
	
	public List<ExtRequiredPartDto> findAllWithExtRequired(){
		
		return findAllByNativeQuery(FIND_ALL_WITH_EXT_REQUIRED, null, ExtRequiredPartDto.class);
	}
	
	public List<LetRequiredPartSpecsDto> findAllWithLETRequired(){
		
		return findAllByNativeQuery(FIND_ALL_WITH_LET_REQUIRED, null, LetRequiredPartSpecsDto.class);
	}
	
	public List<LetRequiredPartSpecsDto> findByPartNameWithLETRequired(String partName) {
		Parameters params = Parameters.with("1", partName);
		return  findAllByNativeQuery(FIND_BY_PART_NAME_WITH_LET_REQUIRED, params, LetRequiredPartSpecsDto.class);
	}
	
	public List<LetRequiredPartSpecsDto> findByPartNameWithLETRequiredByPartSpec(String partName, String partSpec) {
		Parameters params = new Parameters();
		params.put("1", partName);
		params.put("2", partSpec);
		return  findAllByNativeQuery(FIND_BY_PART_NAME_WITH_LET_REQUIRED_BY_SPEC, params, LetRequiredPartSpecsDto.class);
	}

	@Override
	public List<String> findAllPartNamesByPartNumberBase5(String base5) {
		Parameters params = Parameters.with("1", (base5 + "%"));
		return findAllByNativeQuery(FIND_ALL_SAME_PART_NAMES, params, String.class);
	}
	
}
