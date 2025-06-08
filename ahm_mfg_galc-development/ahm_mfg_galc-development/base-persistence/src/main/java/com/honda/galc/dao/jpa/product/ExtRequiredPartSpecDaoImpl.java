/**
 * 
 */
package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dto.ExtRequiredPartSpecDto;
import com.honda.galc.entity.product.ExtRequiredPartSpec;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

/**
 * @author VF031824
 *
 */
public class ExtRequiredPartSpecDaoImpl extends BaseDaoImpl<ExtRequiredPartSpec, PartId> implements ExtRequiredPartSpecDao{
	private static String FIND_ALL_REQUIRED_SPECS = "select "
			+ "e.PART_NAME, "
			+ "e.PART_ID, "
			+ "p.PART_DESCRIPTION, "
			+ "p.PART_SERIAL_NUMBER_MASK, "
			+ "p.PART_MAX_ATTEMPTS, "
			+ "p.MEASUREMENT_COUNT, "
			+ "p.COMMENT, "
			+ "p.PART_MARK, "
			+ "p.PART_NUMBER, "
			+ "e.PART_GROUP, "
			+ "e.PARSE_STRATEGY, "
			+ "e.PARSE_INFORMATION "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)";
	
	private static String FIND_ALL_ACTIVE_FULL_REQUIRED_SPECS = "select "
			+ "e.PART_NAME, "
			+ "e.PART_ID, "
			+ "p.PART_DESCRIPTION, "
			+ "p.PART_SERIAL_NUMBER_MASK, "
			+ "p.PART_MAX_ATTEMPTS, "
			+ "p.MEASUREMENT_COUNT, "
			+ "p.COMMENT, "
			+ "p.PART_MARK, "
			+ "p.PART_NUMBER, "
			+ "e.PART_GROUP, "
			+ "e.PARSE_STRATEGY, "
			+ "e.PARSE_INFORMATION, "
			+ "r.PRODUCT_SPEC_CODE "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID) "
			+ "join GAL245TBX r on (e.part_name = r.part_name) and (e.part_id = r.part_id) "
			+ "join GAL261TBX n on (e.PART_NAME = N.PART_NAME "
			+ "and n.EXTERNAL_REQUIRED = 1)" 
			+ "where e.PART_GROUP IN (@partGroups@)";
	
	private static String FIND_ALL_ACTIVE_REQUIRED_SPECS = "select * "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)"
			+ "join GAL261TBX n on (e.PART_NAME = n.PART_NAME "
			+ "and n.EXTERNAL_REQUIRED = 1) "
			+ "where n.PRODUCT_TYPE = ?1 ";
	
	private static String FIND_ALL_ACTIVE_REQUIRED_SPEC_BY_PART_NAME = "select * "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)"
			+ "join GAL261TBX n on (e.PART_NAME = n.PART_NAME "
			+ "and n.EXTERNAL_REQUIRED = 1) "
			+ "where n.PART_NAME = ?1 ";

	private static String FIND_ALL_REQUIRED_SPECS_BY_PRODUCT_TYPE = "select "
			+ "e.PART_NAME, "
			+ "e.PART_ID, "
			+ "p.PART_DESCRIPTION, "
			+ "p.PART_SERIAL_NUMBER_MASK, "
			+ "p.PART_MAX_ATTEMPTS, "
			+ "p.MEASUREMENT_COUNT, "
			+ "p.COMMENT, "
			+ "p.PART_MARK, "
			+ "p.PART_NUMBER, "
			+ "e.PART_GROUP, "
			+ "e.PARSE_STRATEGY, "
			+ "e.PARSE_INFORMATION "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)"
			+ "join GAL261TBX n on (e.PART_NAME = n.PART_NAME) "
			+ "where n.PRODUCT_TYPE = ?1 ";
	
	private static String FIND_ALL_REQUIRED_SPECS_BY_PART_NAMES = "select "
			+ "e.PART_NAME, "
			+ "e.PART_ID, "
			+ "p.PART_DESCRIPTION, "
			+ "p.PART_SERIAL_NUMBER_MASK, "
			+ "p.PART_MAX_ATTEMPTS, "
			+ "p.MEASUREMENT_COUNT, "
			+ "p.COMMENT, "
			+ "p.PART_MARK, "
			+ "p.PART_NUMBER, "
			+ "e.PART_GROUP, "
			+ "e.PARSE_STRATEGY, "
			+ "e.PARSE_INFORMATION "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)"
			+ "join GAL261TBX n on (e.PART_NAME = n.PART_NAME) "
			+ "where n.PART_NAME IN (@partNames@)";
	
	private static String FIND_ALL_REQUIRED_SPECS_BY_PRODUCT_TYPE_AND_PART_NAME = "select "
			+ "e.PART_NAME, "
			+ "e.PART_ID, "
			+ "p.PART_DESCRIPTION, "
			+ "p.PART_SERIAL_NUMBER_MASK, "
			+ "p.PART_MAX_ATTEMPTS, "
			+ "p.MEASUREMENT_COUNT, "
			+ "p.COMMENT, "
			+ "p.PART_MARK, "
			+ "p.PART_NUMBER, "
			+ "e.PART_GROUP, "
			+ "e.PARSE_STRATEGY, "
			+ "e.PARSE_INFORMATION "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)"
			+ "join GAL261TBX n on (e.PART_NAME = n.PART_NAME) "
			+ "where n.PRODUCT_TYPE = ?1 AND n.PART_NAME = ?2";
	
	
	private static String FIND_DISTINCT_REQUIRED_SPECS_BY_PRODUCT_TYPE = "select "
			+ "distinct e.PART_NAME "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = e.PART_NAME) "
			+ "and (p.PART_ID = e.PART_ID)"
			+ "join GAL261TBX n on (e.PART_NAME = n.PART_NAME) "
			+ "where n.PRODUCT_TYPE = ?1 "
			+ "and n.EXTERNAL_REQUIRED =  1";

	private static String FIND_ALL_DISTINCT_ACTIVE_BY_PART_GROUP = "select "
			+ "distinct e.PART_NAME, p.PRODUCT_TYPE "
			+ "from EXT_REQUIRED_PART_SPEC_TBX e "
			+ "join gal261tbx p on e.PART_NAME = p.PART_NAME "
			+ "where e.part_group IN (@partGroups@) "
			+ "and p.EXTERNAL_REQUIRED = 1";

	public List<ExtRequiredPartSpecDto> findAllRequiredSpecs() {

		return findAllByNativeQuery(FIND_ALL_REQUIRED_SPECS, null, ExtRequiredPartSpecDto.class);
	}
	
	public List<ExtRequiredPartSpecDto> findAllActiveRequiredSpecs(String partGroups) {
		return findAllByNativeQuery(FIND_ALL_ACTIVE_FULL_REQUIRED_SPECS.replace("@partGroups@", StringUtil.toSqlInString(partGroups)),
				null, ExtRequiredPartSpecDto.class);
	}
	
	public List<ExtRequiredPartSpec> findAllActiveRequiredSpecsByProductType(String productType) {
		Parameters params = Parameters.with("1", productType);
		
		return findAllByNativeQuery(FIND_ALL_ACTIVE_REQUIRED_SPECS, params);
	}
	
	public 	List<ExtRequiredPartSpecDto> findAllRequiredSpecsByProductType(String productType) {
		Parameters params = Parameters.with("1", productType);
		
		return findAllByNativeQuery(FIND_ALL_REQUIRED_SPECS_BY_PRODUCT_TYPE, params, ExtRequiredPartSpecDto.class);
	}
	
	public 	List<ExtRequiredPartSpecDto> findAllRequiredSpecsByProductTypeAndPartName(String productType, String partName) {
		Parameters params = new Parameters();
		params.put("1", productType);
		params.put("2", partName);
		
		return findAllByNativeQuery(FIND_ALL_REQUIRED_SPECS_BY_PRODUCT_TYPE_AND_PART_NAME, params, ExtRequiredPartSpecDto.class);
	}
	
	public List<String> findDistinctActiveRequiredSpecsByProductType(String productType) {
		Parameters params = Parameters.with("1", productType);
	
		return findAllByNativeQuery(FIND_DISTINCT_REQUIRED_SPECS_BY_PRODUCT_TYPE, params, String.class);
	}
	
	public List<ExtRequiredPartSpec> findAllActiveRequiredSpecsByPartName(String partName) {
		Parameters params = Parameters.with("1", partName);
		
		return findAllByNativeQuery(FIND_ALL_ACTIVE_REQUIRED_SPEC_BY_PART_NAME, params);
	}
	
	public List<ExtRequiredPartSpec> findAllActiveRequiredSpecsByPartNames(List<String> partNames) {
		return findAllByNativeQuery(FIND_ALL_REQUIRED_SPECS_BY_PART_NAMES.replace("@partNames@", StringUtil.toSqlInString(partNames)), null);
	}
	
	public List<Object[]> findAllActiveByPartGroup(String partGroup) {
		
		return findAllByNativeQuery(FIND_ALL_DISTINCT_ACTIVE_BY_PART_GROUP.replace("@partGroups@", StringUtil.toSqlInString(partGroup)), null, Object[].class);
	}
}
