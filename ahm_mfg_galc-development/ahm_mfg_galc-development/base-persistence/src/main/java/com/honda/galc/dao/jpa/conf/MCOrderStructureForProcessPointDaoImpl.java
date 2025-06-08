package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;
import com.honda.galc.service.Parameters;

public class MCOrderStructureForProcessPointDaoImpl extends BaseDaoImpl<MCOrderStructureForProcessPoint, MCOrderStructureForProcessPointId>
		implements MCOrderStructureForProcessPointDao {
	
	private static String MADE_FROM_SERVICE = "SELECT OrdStr.ORDER_NO AS ORDER_NO , OrdStr.PRODUCT_SPEC_CODE AS PRODUCT_SPEC_CODE, OrdStr.STRUCTURE_REV AS STRUCTURE_REV, Struct.PROCESS_POINT_ID AS PROCESS_POINT_ID, "   
			+ "  Struct.OPERATION_NAME AS OPERATION_NAME, Struct.OP_REV AS OP_REV, Op.OP_TYPE AS OP_TYPE, OpPlat.OP_SEQ_NUM AS OP_SEQ_NUM, Struct.PART_ID AS PART_ID, Struct.PART_REV AS PART_REV, OpPart.PART_NO AS PART_NO, "   
			+ "  OpPart.PART_SECTION_CODE AS PART_SECTION_CODE, OpPart.PART_ITEM_NO AS PART_ITEM_NO,OpPart.PART_MASK AS PART_MASK, OpPart.PART_MARK AS PART_MARK FROM GALADM.MC_ORDER_STRU_FOR_PROCESS_POINT_TBX OrdStr   " 
			+ "  LEFT JOIN GALADM.MC_STRUCTURE_TBX Struct ON (OrdStr.PRODUCT_SPEC_CODE = Struct.PRODUCT_SPEC_CODE AND OrdStr.STRUCTURE_REV = Struct.STRUCTURE_REV)   "
			+ "  LEFT JOIN GALADM.MC_OP_REV_TBX Op ON (Struct.OPERATION_NAME = Op.OPERATION_NAME AND Struct.OP_REV = Op.OP_REV)   "
			+ "  LEFT JOIN GALADM.MC_OP_REV_PLATFORM_TBX OpPlat ON (OpPlat.OPERATION_NAME = Op.OPERATION_NAME AND OpPlat.OP_REV = Op.OP_REV AND OpPlat.PDDA_PLATFORM_ID = Struct.PDDA_PLATFORM_ID) " 
			+ "  LEFT JOIN GALADM.MC_OP_PART_REV_TBX OpPart ON (Op.OPERATION_NAME = OpPart.OPERATION_NAME AND Struct.PART_ID = OpPart.PART_ID AND Struct.PART_REV = OpPart.PART_REV)   "
			+ "  WHERE OrdStr.ORDER_NO = CAST(?1 AS CHARACTER(17)) AND Struct.PROCESS_POINT_ID = CAST (?2 AS CHARACTER(20)) AND Op.OP_TYPE = 'GALC_MADE_FROM'    "
			+ "  ORDER BY OpPlat.OP_SEQ_NUM";
	
	private static String MADE_FROM_BY_ORDER_NO_AND_PROD_SPEC = "SELECT OrdStr.ORDER_NO AS ORDER_NO, OrdStr.PRODUCT_SPEC_CODE AS PRODUCT_SPEC_CODE, OrdStr.STRUCTURE_REV AS STRUCTURE_REV, Struct.PROCESS_POINT_ID AS PROCESS_POINT_ID,  "   
			+ "  Struct.OPERATION_NAME AS OPERATION_NAME, Struct.OP_REV AS OP_REV,Op.OP_TYPE AS OP_TYPE, OpPlat.OP_SEQ_NUM AS OP_SEQ_NUM, Struct.PART_ID AS PART_ID, Struct.PART_REV AS PART_REV, OpPart.PART_NO AS PART_NO,     "
			+ "  OpPart.PART_SECTION_CODE AS PART_SECTION_CODE, OpPart.PART_ITEM_NO AS PART_ITEM_NO,OpPart.PART_MASK AS PART_MASK, OpPart.PART_MARK AS PART_MARK FROM GALADM.MC_ORDER_STRU_FOR_PROCESS_POINT_TBX OrdStr     " 
			+ "  LEFT JOIN GALADM.MC_STRUCTURE_TBX Struct ON (OrdStr.PRODUCT_SPEC_CODE = Struct.PRODUCT_SPEC_CODE AND OrdStr.STRUCTURE_REV = Struct.STRUCTURE_REV)    "
			+ "  LEFT JOIN GALADM.MC_OP_REV_TBX Op ON (Struct.OPERATION_NAME = Op.OPERATION_NAME AND Struct.OP_REV = Op.OP_REV)    "
			+ "  LEFT JOIN GALADM.MC_OP_REV_PLATFORM_TBX OpPlat ON (OpPlat.OPERATION_NAME = Op.OPERATION_NAME AND OpPlat.OP_REV = Op.OP_REV AND OpPlat.PDDA_PLATFORM_ID = Struct.PDDA_PLATFORM_ID)  " 
			+ "  LEFT JOIN GALADM.MC_OP_PART_REV_TBX OpPart ON (Op.OPERATION_NAME = OpPart.OPERATION_NAME AND Struct.PART_ID = OpPart.PART_ID AND Struct.PART_REV = OpPart.PART_REV)    "
			+ "  WHERE OrdStr.ORDER_NO = ?1 AND OrdStr.PRODUCT_SPEC_CODE = ?2     "
			+ "  ORDER BY OpPlat.OP_SEQ_NUM";
	
	private static final String GET_BY_ORDER_NO__DIVISION_PROCESS_POINT = "SELECT NULL AS PRODUCT_ID, OST.PRODUCT_SPEC_CODE AS PRODUCT_SPEC_CODE, OST.ORDER_NO AS PRODUCTION_LOT, OST.STRUCTURE_REV AS STRUCTURE_REVISION, OST.DIVISION_ID AS DIVISION, OST.STRUCTURE_REV AS STRUCTURE_REV, OST.CREATE_TIMESTAMP AS CREATE_TIMESTAMP, OST.PROCESS_POINT_ID  "
			 + " FROM MC_ORDER_STRU_FOR_PROCESS_POINT_TBX OST "
			 + " WHERE OST.ORDER_NO LIKE ?1 AND "
			 + " OST.DIVISION_ID = ?2 AND OST.PROCESS_POINT_ID = ?3";
	
	private static final String GET_STRUCTURE_DETAILS = "select s.STRUCTURE_REV, s.PDDA_PLATFORM_ID,s.DIVISION_ID, s.PROCESS_POINT_ID, p.ASM_PROC_NO, s.PROCESS_SEQ_NUM, s.OPERATION_NAME, o.OP_DESC AS OPERATION_DESC, s.OP_REV, "                  
			+ "  o.OP_TYPE AS OPERATION_TYPE, op.OP_SEQ_NUM AS OPERATION_SEQ_NUM, s.PART_ID, s.PART_REV, pr.PART_TYPE, pr.PART_NO, pr.PART_ITEM_NO, pr.PART_SECTION_CODE, " 
			+ " pr.PART_MASK,pr.PART_DESC, m.MIN_LIMIT, m.MAX_LIMIT, m.DEVICE_ID, m.DEVICE_MSG, m.OP_MEAS_SEQ_NUM AS OPERATION_MEAS_SEQ_NUM, pp.PROCESS_POINT_NAME, pp.PROCESS_POINT_DESCRIPTION " +
			"	from galadm.MC_STRUCTURE_TBX s "
            + " join galadm.MC_PDDA_PLATFORM_TBX p on s.PDDA_PLATFORM_ID=p.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_PLATFORM_TBX op on op.OPERATION_NAME=s.OPERATION_NAME and op.OP_REV=s.OP_REV and op.PDDA_PLATFORM_ID=s.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_TBX o on o.OPERATION_NAME=s.OPERATION_NAME and o.OP_REV=s.OP_REV "
            + " left outer join galadm.MC_OP_PART_REV_TBX pr on pr.OPERATION_NAME=s.OPERATION_NAME and pr.PART_REV=s.PART_REV and pr.PART_ID=s.PART_ID "
            + " join galadm.MC_ORDER_STRU_FOR_PROCESS_POINT_TBX ps on s.STRUCTURE_REV=ps.STRUCTURE_REV and s.PRODUCT_SPEC_CODE = ps.PRODUCT_SPEC_CODE and s.DIVISION_ID = ps.DIVISION_ID and s.PROCESS_POINT_ID = ps.PROCESS_POINT_ID "
            + " left outer join galadm.MC_OP_MEAS_TBX m on pr.OPERATION_NAME=m.OPERATION_NAME and pr.PART_REV=m.PART_REV and pr.PART_ID=m.PART_ID "
            + " join galadm.gal214tbx pp on pp.PROCESS_POINT_ID = s.PROCESS_POINT_ID "
            + " where "
            + "  s.DIVISION_ID =?1 AND ps.ORDER_NO = ?2  and ps.PROCESS_POINT_ID = ?3 "
            + " order by s.PROCESS_POINT_ID,s.PROCESS_SEQ_NUM,op.OP_SEQ_NUM ";	
	
	public List<MfgCtrlMadeFrom> getMadeFrom(String orderNo, String processPointId) {
		Parameters params = Parameters.with("1", orderNo).put("2", processPointId);
		return findAllByNativeQuery(MADE_FROM_SERVICE, params, MfgCtrlMadeFrom.class);
	}
	
	public List<MfgCtrlMadeFrom> getMadeFromByOrderNoAndProdSpecCode(String orderNo, String prodSpecCode) {
		Parameters params = Parameters.with("1", orderNo).put("2", prodSpecCode);
		return findAllByNativeQuery(MADE_FROM_BY_ORDER_NO_AND_PROD_SPEC, params, MfgCtrlMadeFrom.class);
	}

	public MCOrderStructureForProcessPoint findByKey(String orderId, String modeId) {
		return findByKey(new MCOrderStructureForProcessPointId(orderId, modeId));
	}
	
	public void removeByKey(String orderId, String modeId) {
		removeByKey(new MCOrderStructureForProcessPointId(orderId, modeId));
	}

	public List<StructureDetailsDto> findByOrderNumberDivisionAndProcessPoint(String orderNo, String divisionId, String processPointId) {
		String likeOrderNo = "%" + orderNo + "%";
		Parameters params = Parameters.with("1", likeOrderNo).put("2", divisionId).put("3", processPointId);
		return findAllByNativeQuery(GET_BY_ORDER_NO__DIVISION_PROCESS_POINT, params, StructureDetailsDto.class);
	}
	
	public List<StructureUnitDetailsDto> findAllByOrderNoDivisionAndProcessPoint(String orderNo, String divisionId, String processPoint) {
		Parameters params = Parameters.with("1", divisionId).put("2", orderNo).put("3", processPoint);
		return findAllByNativeQuery(GET_STRUCTURE_DETAILS, params, StructureUnitDetailsDto.class);
	}
}
