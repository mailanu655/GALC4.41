package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCProductStructureDaoImpl
	extends BaseDaoImpl<MCProductStructure, MCProductStructureId> 
	implements MCProductStructureDao {
	
	private static final String GET_UNMAPPED_PRODUCT_IDS = "SELECT PRODUCT_ID " + 
            "FROM %s " +
            "WHERE PRODUCT_ID NOT IN (SELECT A.PRODUCT_ID FROM MC_PRODUCT_STRUCTURE_TBX A) ";

    private static final String GET_UNMAPPED_PRODUCT_ID_COUNT = "SELECT  count(*) " + 
            "FROM %s " +
            "WHERE PRODUCT_ID NOT IN (SELECT A.PRODUCT_ID FROM MC_PRODUCT_STRUCTURE_TBX A) ";


	private static final String GET_UNMAPPED_MBPN_PRODUCT_SPEC_CODES = "SELECT MBPN.CURRENT_PRODUCT_SPEC_CODE FROM    GALADM.MBPN_PRODUCT_TBX MBPN  " +
																	" LEFT JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX MC_PROD_STRU ON (MBPN.PRODUCT_ID = MC_PROD_STRU.PRODUCT_ID)  " +
																	" WHERE MC_PROD_STRU.PRODUCT_ID IS NULL  AND MBPN.CURRENT_PRODUCT_SPEC_CODE IS NOT NULL " +
																	" GROUP BY MBPN.CURRENT_PRODUCT_SPEC_CODE";

	private static final String GET_UNMAPPED_ENGINE_PRODUCT_SPEC_CODES = "SELECT ENGINE.PRODUCT_SPEC_CODE FROM    GALADM.GAL131TBX ENGINE " +
																		" LEFT JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX MC_PROD_STRU ON (ENGINE.PRODUCT_ID = MC_PROD_STRU.PRODUCT_ID) " +
																		" WHERE MC_PROD_STRU.PRODUCT_ID IS NULL AND ENGINE.PRODUCT_SPEC_CODE IS NOT NULL" +
																		" GROUP BY ENGINE.PRODUCT_SPEC_CODE";
	

	private static final String GET_UNMAPPED_FRAME_PRODUCT_SPEC_CODES = "SELECT FRAME.PRODUCT_SPEC_CODE FROM    GALADM.GAL143TBX FRAME  " +
														" LEFT JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX MC_PROD_STRU ON (FRAME.PRODUCT_ID = MC_PROD_STRU.PRODUCT_ID)  " +
														" WHERE MC_PROD_STRU.PRODUCT_ID IS NULL  AND FRAME.PRODUCT_SPEC_CODE IS NOT NULL " +
														" GROUP BY FRAME.PRODUCT_SPEC_CODE";

	private static final String GET_BY_PRODUCT_ID = "select e from MCProductStructure e where e.id.productId in (:products) and e.id.divisionId = :divId and e.id.productSpecCode = :specCode"; 
	
	private static final String GET_STRUCTURE_DETAILS = "select s.STRUCTURE_REV, s.PDDA_PLATFORM_ID,s.DIVISION_ID, s.PROCESS_POINT_ID, p.ASM_PROC_NO, s.PROCESS_SEQ_NUM, s.OPERATION_NAME, o.OP_DESC AS OPERATION_DESC, s.OP_REV, "                  
			+ "  o.OP_TYPE AS OPERATION_TYPE, op.OP_SEQ_NUM AS OPERATION_SEQ_NUM, s.PART_ID, s.PART_REV, pr.PART_TYPE, pr.PART_NO, pr.PART_ITEM_NO, pr.PART_SECTION_CODE, " 
			+ " pr.PART_MASK,pr.PART_DESC, m.MIN_LIMIT, m.MAX_LIMIT, m.DEVICE_ID, m.DEVICE_MSG, m.OP_MEAS_SEQ_NUM AS OPERATION_MEAS_SEQ_NUM, pp.PROCESS_POINT_NAME, pp.PROCESS_POINT_DESCRIPTION " +
			"	from galadm.MC_STRUCTURE_TBX s "
            + " join galadm.MC_PDDA_PLATFORM_TBX p on s.PDDA_PLATFORM_ID=p.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_PLATFORM_TBX op on op.OPERATION_NAME=s.OPERATION_NAME and op.OP_REV=s.OP_REV and op.PDDA_PLATFORM_ID=s.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_TBX o on o.OPERATION_NAME=s.OPERATION_NAME and o.OP_REV=s.OP_REV "
            + " left outer join galadm.MC_OP_PART_REV_TBX pr on pr.OPERATION_NAME=s.OPERATION_NAME and pr.PART_REV=s.PART_REV and pr.PART_ID=s.PART_ID "
            + " join galadm.MC_PRODUCT_STRUCTURE_TBX ps on s.STRUCTURE_REV=ps.STRUCTURE_REV and s.PRODUCT_SPEC_CODE=ps.PRODUCT_SPEC_CODE and s.DIVISION_ID=ps.DIVISION_ID "
            + " left outer join galadm.MC_OP_MEAS_TBX m on pr.OPERATION_NAME=m.OPERATION_NAME and pr.PART_REV=m.PART_REV and pr.PART_ID=m.PART_ID "
            + " join galadm.gal214tbx pp on pp.PROCESS_POINT_ID = s.PROCESS_POINT_ID "
            + "  where "
            + "  s.DIVISION_ID =?1 AND ps.PRODUCT_ID = ?2 AND ps.Structure_rev = ?3 "
            + " order by s.PROCESS_POINT_ID,s.PROCESS_SEQ_NUM,op.OP_SEQ_NUM ";
	
	public int getUnmappedProductCount(ProductType productType) {
		String sqlStmt = String.format(GET_UNMAPPED_PRODUCT_ID_COUNT,getTableForProductType(productType));
		Integer count = findFirstByNativeQuery(sqlStmt, null, Integer.class);
		return count.intValue();
	}

	public List<String> getAllUnmappedProducts(ProductType productType) {
		ArrayList<String> productIdList = new ArrayList<String>();

		String sqlStmt = String.format(GET_UNMAPPED_PRODUCT_IDS,getTableForProductType(productType));
		List<String> resultSet = findResultListByNativeQuery(sqlStmt, null);

		for (String result : resultSet) {
			productIdList.add(result);
		}
		return productIdList;
	}

	private String getTableForProductType(ProductType productType) {
		switch (productType) {
		case MBPN:
			return "MBPN_PRODUCT_TBX";
		case ENGINE:
			return "GAL131TBX";
		default:
			return "GAL143TBX";
		}
	}
	
	public List<String> getUnmappedProductSpecCodes(String sqlStmt, Date lastUpdated) {
		ArrayList<String> productSpecCodeList = new ArrayList<String>();

		Parameters parameters = Parameters.with("1", lastUpdated);
		List<String> resultSet = findResultListByNativeQuery(sqlStmt, parameters);

		for (String result : resultSet) {
			productSpecCodeList.add(result);
		}
		return productSpecCodeList;
	}

	@SuppressWarnings("unchecked")
	public List<String> getUnmappedMBPNProductSpecCodes() {
		return findResultListByNativeQuery(GET_UNMAPPED_MBPN_PRODUCT_SPEC_CODES, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUnmappedEngineProductSpecCodes() {
		return findResultListByNativeQuery(GET_UNMAPPED_ENGINE_PRODUCT_SPEC_CODES, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUnmappedFrameProductSpecCodes() {
		return findResultListByNativeQuery(GET_UNMAPPED_FRAME_PRODUCT_SPEC_CODES, null);
	}
	
	public List<MCProductStructure> findAllProductForProdSpecCodeAndRevision(
			String prodSpecCode, long structureRevision) {
		Parameters param = Parameters.with("productSpecCode", prodSpecCode);
		param.put("structureRevision", structureRevision);
		return findAll(param);
	}
	
	public List<MCProductStructure> findAllBy(String productId, String productSpecCode) {
		Parameters param = Parameters.with("id.productId", productId);
		param.put("id.productSpecCode", productSpecCode);
		return findAll(param);
	}

	public MCProductStructure findByKey(String productId, String divisionId, String productSpecCode) {
		return findByKey(new MCProductStructureId(productId, divisionId, productSpecCode));
	}

	public List<StructureUnitDetailsDto> findAllByProductAndDivision(String productId, String divisionId,long structureRev){
		Parameters params = Parameters.with("1", divisionId).put("2", productId).put("3", structureRev);
		return findAllByNativeQuery(GET_STRUCTURE_DETAILS, params, StructureUnitDetailsDto.class);
	}
	
	public List<MCProductStructure> findProductByStructureRevAndDivId(long structureRev, String divisionId) {
		return findAll(Parameters.with("structureRevision", structureRev).put("id.divisionId", divisionId));
	}
	
	public List<MCProductStructure> findAllByProductIdDivisionAndSpecCode(List<String> products, String divisionId, String specCode){
		Parameters params = Parameters.with("products", products).put("divId", divisionId).put("specCode", specCode);
		return findAllByQuery(GET_BY_PRODUCT_ID, params);
	}

	@Override
	public List<MCProductStructure> findAllByProductIdAndDivisionId(String productId, String divisionId) {
		Parameters param = Parameters.with("id.productId", productId);
		param.put("id.divisionId", divisionId);
		return findAll(param);
	}
}
