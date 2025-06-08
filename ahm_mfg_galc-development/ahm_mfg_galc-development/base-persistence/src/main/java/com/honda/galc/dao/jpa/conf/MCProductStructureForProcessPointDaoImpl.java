package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

/**
 * @author Fredrick Yessaian
 * @date Apr 01, 2016
 * 
 */

public class MCProductStructureForProcessPointDaoImpl extends BaseDaoImpl<MCProductStructureForProcessPoint, MCProductStructureForProcessPointId>
		implements MCProductStructureForProcessPointDao {
	
	private static final String GET_UNMAPPED_PRODUCT_ID_COUNT = "SELECT  count(*) " + 
            "FROM %s " +
            "WHERE PRODUCT_ID NOT IN (SELECT A.PRODUCT_ID FROM MC_PRODUCT_STRU_FOR_PROCESS_POINT_TBX A) ";
	
	private static final String GET_BY_PRODUCT_ID = "select e from MCProductStructureForProcessPoint e where e.id.productId in (:products) and e.divisionId = :divId and e.id.productSpecCode = :specCode and e.id.processPointId = processPoint";
	
	private static final String GET_STRUCTURE_DETAILS = "select s.STRUCTURE_REV, s.PDDA_PLATFORM_ID,s.DIVISION_ID, s.PROCESS_POINT_ID, p.ASM_PROC_NO, s.PROCESS_SEQ_NUM, s.OPERATION_NAME, o.OP_DESC AS OPERATION_DESC, s.OP_REV, "                  
			+ "  o.OP_TYPE AS OPERATION_TYPE, op.OP_SEQ_NUM AS OPERATION_SEQ_NUM, s.PART_ID, s.PART_REV, pr.PART_TYPE, pr.PART_NO, pr.PART_ITEM_NO, pr.PART_SECTION_CODE, " 
			+ " pr.PART_MASK,pr.PART_DESC, m.MIN_LIMIT, m.MAX_LIMIT, m.DEVICE_ID, m.DEVICE_MSG, m.OP_MEAS_SEQ_NUM AS OPERATION_MEAS_SEQ_NUM, pp.PROCESS_POINT_NAME, pp.PROCESS_POINT_DESCRIPTION " +
			"	from galadm.MC_STRUCTURE_TBX s "
            + " join galadm.MC_PDDA_PLATFORM_TBX p on s.PDDA_PLATFORM_ID=p.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_PLATFORM_TBX op on op.OPERATION_NAME=s.OPERATION_NAME and op.OP_REV=s.OP_REV and op.PDDA_PLATFORM_ID=s.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_TBX o on o.OPERATION_NAME=s.OPERATION_NAME and o.OP_REV=s.OP_REV "
            + " left outer join galadm.MC_OP_PART_REV_TBX pr on pr.OPERATION_NAME=s.OPERATION_NAME and pr.PART_REV=s.PART_REV and pr.PART_ID=s.PART_ID "
            + " join galadm.MC_PRODUCT_STRU_FOR_PROCESS_POINT_TBX ps on s.STRUCTURE_REV=ps.STRUCTURE_REV and s.PRODUCT_SPEC_CODE=ps.PRODUCT_SPEC_CODE and s.DIVISION_ID=ps.DIVISION_ID and s.PROCESS_POINT_ID = ps.PROCESS_POINT_ID "
            + " left outer join galadm.MC_OP_MEAS_TBX m on pr.OPERATION_NAME=m.OPERATION_NAME and pr.PART_REV=m.PART_REV and pr.PART_ID=m.PART_ID "
            + " join galadm.gal214tbx pp on pp.PROCESS_POINT_ID = s.PROCESS_POINT_ID "
            + "  where "
            + "  s.DIVISION_ID =?1 AND ps.PRODUCT_ID = ?2 and ps.PROCESS_POINT_ID = ?3 "
            + " order by s.PROCESS_POINT_ID,s.PROCESS_SEQ_NUM,op.OP_SEQ_NUM ";
	
	public int getUnmappedProductCount(ProductType productType) {
		String sqlStmt = String.format(GET_UNMAPPED_PRODUCT_ID_COUNT,getTableForProductType(productType));
		Integer count = findFirstByNativeQuery(sqlStmt, null, Integer.class);
		return count.intValue();
	}
	
	private String getTableForProductType(ProductType productType) {
		switch (productType) {
		case MBPN:
			return CommonUtil.getTableName(ProductTypeUtil.getTypeUtil(ProductType.MBPN).getProductClass());
		case ENGINE:
			return CommonUtil.getTableName(ProductTypeUtil.getTypeUtil(ProductType.ENGINE).getProductClass());
		default:
			return CommonUtil.getTableName(ProductTypeUtil.getTypeUtil(ProductType.FRAME).getProductClass());
		}
	}
	
	public List<MCProductStructureForProcessPoint> findAllBy(String productId, String productSpecCode, String processPoint) {
		Parameters param = Parameters.with("id.productId", productId);
		param.put("id.productSpecCode", productSpecCode);
		param.put("id.processPointId", processPoint);
		return findAll(param);
	}

	public MCProductStructureForProcessPoint findByKey(String productId, String processPointId, String productSpecCode) {
		return findByKey(new MCProductStructureForProcessPointId(productId, processPointId, productSpecCode));
	}
	
	public List<MCProductStructureForProcessPoint> findAllByProductIdDivisionAndSpecCode(List<String> products, String divisionId, String specCode, String processPoint) {
		Parameters params = Parameters.with("products", products).put("divId", divisionId).put("specCode", specCode).put("processpoint", processPoint);
		return findAllByQuery(GET_BY_PRODUCT_ID, params);
	}
	
	public List<StructureUnitDetailsDto> findAllByProductIdDivisionAndProcessPoint(String productId, String divisionId, String processPoint) {
		Parameters params = Parameters.with("1", divisionId).put("2", productId).put("3", processPoint);
		return findAllByNativeQuery(GET_STRUCTURE_DETAILS, params, StructureUnitDetailsDto.class);
	}
}
