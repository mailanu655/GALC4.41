/**
 * 
 */
package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetPartCheckSpecDao;
import com.honda.galc.dto.RequiredLetPartSpecDetailsDto;
import com.honda.galc.entity.product.LetPartCheckSpec;
import com.honda.galc.entity.product.LetPartCheckSpecId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.ProductSpecUtil;

/**
 * @author vf031824
 *
 */
public class LetPartCheckSpecDaoImpl extends BaseDaoImpl<LetPartCheckSpec, LetPartCheckSpecId> implements LetPartCheckSpecDao {
	
	private static String FIND_ALL_ACTIVE_REQUIRED_SPECS = "select * "
			+ "from LET_PART_CHECK_SPEC_TBX l "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = l.PART_NAME) "
			+ "and (p.PART_ID = l.PART_ID)"
			+ "join GAL261TBX n on (l.PART_NAME = n.PART_NAME "
			+ "and n.LET_CHECK_REQUIRED = 1) "
			+ "where n.PRODUCT_TYPE = ?1 ";
	
	private static String FIND_PART_NAMES_BY_PRODUCT_TYPE = "select distinct l.part_name "
			+ "from LET_PART_CHECK_SPEC_TBX l "
			+ "join PART_SPEC_TBX p on (p.PART_NAME = l.PART_NAME) "
			+ "and (p.PART_ID = l.PART_ID)"
			+ "join GAL261TBX n on (l.PART_NAME = n.PART_NAME "
			+ "and n.LET_CHECK_REQUIRED = 1) "
			+ "where n.PRODUCT_TYPE = ?1 ";
	
	private static String FIND_ALL_ACTIVE_BY_PRODUCT_SPEC_CODE = "select "
			+ "l.PART_NAME, p.PART_ID, p.PRODUCT_SPEC_CODE, l.INSPECTION_PARAM_ID, l.INSPECTION_PROGRAM_ID, l.SEQUENCE_NUMBER, n.product_type "
			+ "from LET_PART_CHECK_SPEC_TBX l "
			+ "join GAL245TBX p on l.PART_NAME = p.PART_NAME and p.PART_ID = l.PART_ID "
			+ "join GAL261TBX n on n.LET_CHECK_REQUIRED = 1 "
			+ "and n.PART_NAME = l.PART_NAME "
			+ "where l.PARAM_TYPE = ?1 "
			+ "order by l.part_name, l.SEQUENCE_NUMBER asc";
			
	private static String FIND_ALL_ACTIVE_BY_PART_NAME_SPEC_AND_PAREM_TYPE = 
			"select * from LET_PART_CHECK_SPEC_TBX l "
			+ "join GAL261TBX p on (p.PART_NAME = l.PART_NAME "
			+ "and p.LET_CHECK_REQUIRED= 1) "
			+ "where l.part_name = ?1 and l.part_id = ?2 and l.param_type = ?3 "
			+ "order by l.sequence_number asc";
	
	
	public List<LetPartCheckSpec> findAllActiveSpecsByProductType(String productType) {
		
		Parameters params = Parameters.with("1", productType);
		
		return findAllByNativeQuery(FIND_ALL_ACTIVE_REQUIRED_SPECS, params);
	}
	
	public List<String> findAllActivePartNamesByProductType(String productType) {
		
		Parameters params = Parameters.with("1", productType);
		
		return findAllByNativeQuery(FIND_PART_NAMES_BY_PRODUCT_TYPE, params, String.class);
	}
	
	public List<LetPartCheckSpec> findAllActiveByPartNameSpecAndParamType(String partName, String partSpec, int paramType) {
		
		Parameters params = new Parameters();
		params.put("1", partName);
		params.put("2", partSpec);
		params.put("3", paramType);
		params.put("4", "sequence_number");
		
		return findAllByNativeQuery(FIND_ALL_ACTIVE_BY_PART_NAME_SPEC_AND_PAREM_TYPE, params);
	}
	
	public List<RequiredLetPartSpecDetailsDto> findAllActiveByProductSpecCode(String productSpecCode, String ProductId, String productType, int paramType) {
		Parameters params = new Parameters();
		params.put("1", paramType);
		
		List<RequiredLetPartSpecDetailsDto> requiredLetSpecDetails = findAllByNativeQuery(FIND_ALL_ACTIVE_BY_PRODUCT_SPEC_CODE, params, RequiredLetPartSpecDetailsDto.class);
		
		return ProductSpecUtil.getMatchedRuleList(productSpecCode, productType, requiredLetSpecDetails, RequiredLetPartSpecDetailsDto.class);
	}
}
