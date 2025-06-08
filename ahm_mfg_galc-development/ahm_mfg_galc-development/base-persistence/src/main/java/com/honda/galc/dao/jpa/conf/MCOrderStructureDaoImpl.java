/**
 * 
 */
package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.service.Parameters;


/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */

public class MCOrderStructureDaoImpl extends BaseDaoImpl<MCOrderStructure, MCOrderStructureId> implements MCOrderStructureDao {

	private static String MADE_FROM_SERVICE = "SELECT OrdStr.ORDER_NO , OrdStr.PRODUCT_SPEC_CODE, OrdStr.STRUCTURE_REV, Struct.PROCESS_POINT_ID,   "
			+ "  Struct.OPERATION_NAME, Struct.OP_REV,Op.OP_TYPE, OpPlat.OP_SEQ_NUM, Struct.PART_ID, Struct.PART_REV, OpPart.PART_NO,   "
			+ "  OpPart.PART_SECTION_CODE, OpPart.PART_ITEM_NO,OpPart.PART_MASK, OpPart.PART_MARK FROM GALADM.MC_ORDER_STRUCTURE_TBX OrdStr   "
			+ "  LEFT JOIN GALADM.MC_STRUCTURE_TBX Struct ON (OrdStr.PRODUCT_SPEC_CODE = Struct.PRODUCT_SPEC_CODE AND OrdStr.STRUCTURE_REV = Struct.STRUCTURE_REV)  "
			+ "  LEFT JOIN GALADM.MC_OP_REV_TBX Op ON (Struct.OPERATION_NAME = Op.OPERATION_NAME AND Struct.OP_REV = Op.OP_REV)  "
			+ "  LEFT JOIN GALADM.MC_OP_REV_PLATFORM_TBX OpPlat ON (OpPlat.OPERATION_NAME = Op.OPERATION_NAME AND OpPlat.OP_REV = Op.OP_REV AND OpPlat.PDDA_PLATFORM_ID = Struct.PDDA_PLATFORM_ID) "
			+ "  LEFT JOIN GALADM.MC_OP_PART_REV_TBX OpPart ON (Op.OPERATION_NAME = OpPart.OPERATION_NAME AND Struct.PART_ID = OpPart.PART_ID AND Struct.PART_REV = OpPart.PART_REV)  "
			+ "  WHERE OrdStr.ORDER_NO = CAST(?1 AS CHARACTER(17)) AND Struct.PROCESS_POINT_ID = CAST (?2 AS CHARACTER(20)) AND Op.OP_TYPE = 'MADE_FROM'   "
			+ "  ORDER BY OpPlat.OP_SEQ_NUM";

	private static String MADE_FROM_BY_ORDER_NO_AND_PROD_SPEC = "SELECT OrdStr.ORDER_NO , OrdStr.PRODUCT_SPEC_CODE, OrdStr.STRUCTURE_REV, Struct.PROCESS_POINT_ID,   "
			+ "  Struct.OPERATION_NAME, Struct.OP_REV,Op.OP_TYPE, OpPlat.OP_SEQ_NUM, Struct.PART_ID, Struct.PART_REV, OpPart.PART_NO,   "
			+ "  OpPart.PART_SECTION_CODE, OpPart.PART_ITEM_NO,OpPart.PART_MASK, OpPart.PART_MARK FROM GALADM.MC_ORDER_STRUCTURE_TBX OrdStr   "
			+ "  LEFT JOIN GALADM.MC_STRUCTURE_TBX Struct ON (OrdStr.PRODUCT_SPEC_CODE = Struct.PRODUCT_SPEC_CODE AND OrdStr.STRUCTURE_REV = Struct.STRUCTURE_REV)  "
			+ "  LEFT JOIN GALADM.MC_OP_REV_TBX Op ON (Struct.OPERATION_NAME = Op.OPERATION_NAME AND Struct.OP_REV = Op.OP_REV)  "
			+ "  LEFT JOIN GALADM.MC_OP_REV_PLATFORM_TBX OpPlat ON (OpPlat.OPERATION_NAME = Op.OPERATION_NAME AND OpPlat.OP_REV = Op.OP_REV AND OpPlat.PDDA_PLATFORM_ID = Struct.PDDA_PLATFORM_ID) "
			+ "  LEFT JOIN GALADM.MC_OP_PART_REV_TBX OpPart ON (Op.OPERATION_NAME = OpPart.OPERATION_NAME AND Struct.PART_ID = OpPart.PART_ID AND Struct.PART_REV = OpPart.PART_REV)  "
			+ "  WHERE OrdStr.ORDER_NO = ?1 AND OrdStr.PRODUCT_SPEC_CODE = ?2   "
			+ "  ORDER BY OpPlat.OP_SEQ_NUM";

	private static String FIND_NOT_MAPPED_ORDER_IDS = "SELECT PROD_LOT.PRODUCTION_LOT, PROD_LOT.LOT_SIZE, PROD_LOT.START_PRODUCT_ID, PROD_LOT.SEND_STATUS, PROD_LOT.STAMPED_COUNT, PROD_LOT.HOLD_STATUS, " +
														"PROD_LOT.SENT_TIMESTAMP, PROD_LOT.NEXT_PRODUCTION_LOT, PROD_LOT.LOT_NUMBER, PROD_LOT.PLANT_CODE, PROD_LOT.PROCESS_LOCATION, PROD_LOT.LINE_NO, " +
														"PROD_LOT.PRODUCT_SPEC_CODE, PROD_LOT.CREATE_TIMESTAMP, PROD_LOT.UPDATE_TIMESTAMP, PROD_LOT.BUILD_SEQ_NOT_FIXED_FLAG, PROD_LOT.BUILD_SEQUENCE_NUMBER, " +
														"PROD_LOT.PLAN_CODE, PROD_LOT.KD_LOT_NUMBER, PROD_LOT.SEQUENCE, PROD_LOT.REMAKE_FLAG, PROD_LOT.STAMPING_FLAG, PROD_LOT.CARRY_IN_OUT_FLAG, " +
														"PROD_LOT.CARRY_IN_OUT_UNITS, PROD_LOT.MBPN, PROD_LOT.HES_COLOR FROM GALADM.GAL212TBX PROD_LOT " +
													    " LEFT JOIN GALADM.MC_ORDER_STRUCTURE_TBX ORD_STRU ON (PROD_LOT.PRODUCTION_LOT = ORD_STRU.ORDER_NO) " +
													    " WHERE ORD_STRU.ORDER_NO IS NULL";
	
	private static final String GET_BY_ORDER_NO_AND_DIVISION = "SELECT NULL AS PRODUCT_ID, OST.PRODUCT_SPEC_CODE AS PRODUCT_SPEC_CODE, OST.ORDER_NO AS PRODUCTION_LOT, OST.STRUCTURE_REV AS STRUCTURE_REVISION, OST.DIVISION_ID AS DIVISION, OST.STRUCTURE_REV AS STRUCTURE_REV, OST.CREATE_TIMESTAMP AS CREATE_TIMESTAMP  "
			 + " FROM MC_ORDER_STRUCTURE_TBX OST "
			 + " WHERE OST.ORDER_NO LIKE ?1 AND "
			 + " OST.DIVISION_ID = ?2 ";
	
	private static final String GET_STRUCTURE_DETAILS = "select s.STRUCTURE_REV, s.PDDA_PLATFORM_ID,s.DIVISION_ID, s.PROCESS_POINT_ID, p.ASM_PROC_NO, s.PROCESS_SEQ_NUM, s.OPERATION_NAME, o.OP_DESC AS OPERATION_DESC, s.OP_REV, "                  
			+ "  o.OP_TYPE AS OPERATION_TYPE, op.OP_SEQ_NUM AS OPERATION_SEQ_NUM, s.PART_ID, s.PART_REV, pr.PART_TYPE, pr.PART_NO, pr.PART_ITEM_NO, pr.PART_SECTION_CODE, " 
			+ " pr.PART_MASK,pr.PART_DESC, m.MIN_LIMIT, m.MAX_LIMIT, m.DEVICE_ID, m.DEVICE_MSG, m.OP_MEAS_SEQ_NUM AS OPERATION_MEAS_SEQ_NUM, pp.PROCESS_POINT_NAME, pp.PROCESS_POINT_DESCRIPTION " +
			"	from galadm.MC_STRUCTURE_TBX s "
            + " join galadm.MC_PDDA_PLATFORM_TBX p on s.PDDA_PLATFORM_ID=p.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_PLATFORM_TBX op on op.OPERATION_NAME=s.OPERATION_NAME and op.OP_REV=s.OP_REV and op.PDDA_PLATFORM_ID=s.PDDA_PLATFORM_ID "
            + " join galadm.MC_OP_REV_TBX o on o.OPERATION_NAME=s.OPERATION_NAME and o.OP_REV=s.OP_REV "
            + " left outer join galadm.MC_OP_PART_REV_TBX pr on pr.OPERATION_NAME=s.OPERATION_NAME and pr.PART_REV=s.PART_REV and pr.PART_ID=s.PART_ID "
            + " join galadm.MC_ORDER_STRUCTURE_TBX ps on s.STRUCTURE_REV=ps.STRUCTURE_REV and s.PRODUCT_SPEC_CODE=ps.PRODUCT_SPEC_CODE and s.DIVISION_ID=ps.DIVISION_ID "
            + " left outer join galadm.MC_OP_MEAS_TBX m on pr.OPERATION_NAME=m.OPERATION_NAME and pr.PART_REV=m.PART_REV and pr.PART_ID=m.PART_ID "
            + " join galadm.gal214tbx pp on pp.PROCESS_POINT_ID = s.PROCESS_POINT_ID "
            + "  where "
            + "  s.DIVISION_ID =?1 AND ps.ORDER_NO = ?2 "
            + " order by s.PROCESS_POINT_ID,s.PROCESS_SEQ_NUM,op.OP_SEQ_NUM ";
	
	public List<MCOrderStructure> findAllByProductSpecCode(String productSpecCode) {
		Parameters params = Parameters.with("productSpecCode", productSpecCode);
		return findAll(params);
	}

	public List<MCOrderStructure> findAllByStructureRevision(long structureRevision) {
		Parameters params = Parameters.with("structureRevision", structureRevision);
		return findAll(params);
	}

	public List<MfgCtrlMadeFrom> getMadeFrom(String orderNo, String processPointId) {
		Parameters params = Parameters.with("1", orderNo).put("2", processPointId);
		List<Object[]> mfgCtrlMadeFromResponseLst = findAllByNativeQuery(MADE_FROM_SERVICE, params, Object[].class);
		return populateMadeFromList(mfgCtrlMadeFromResponseLst);
	}

	public List<MfgCtrlMadeFrom> getMadeFromByOrderNoAndProdSpecCode(String orderNo, String prodSpecCode) {
		Parameters params = Parameters.with("1", orderNo).put("2", prodSpecCode);
		List<Object[]> mfgCtrlMadeFromResponseLst = findAllByNativeQuery(MADE_FROM_BY_ORDER_NO_AND_PROD_SPEC, params, Object[].class);
		return populateMadeFromList(mfgCtrlMadeFromResponseLst);
	}

	private List<MfgCtrlMadeFrom> populateMadeFromList(List<Object[]> mfgCtrlMadeFromResponseLst) {
		if (null == mfgCtrlMadeFromResponseLst || mfgCtrlMadeFromResponseLst.isEmpty()) {
			return null;
		}
		ArrayList<MfgCtrlMadeFrom> mfgCtrlMadeFromLst = new ArrayList<MfgCtrlMadeFrom>();
		for (Object[] madeFromObj : mfgCtrlMadeFromResponseLst) {

			MfgCtrlMadeFrom mfgCtrlMadeFrom = new MfgCtrlMadeFrom();

			mfgCtrlMadeFrom.setOrderNo(madeFromObj[0].toString());
			if (madeFromObj[1] != null)
				mfgCtrlMadeFrom.setProductSpecCode(madeFromObj[1].toString());
			if (madeFromObj[2] != null)
				mfgCtrlMadeFrom.setStructureRevision((long) Integer.parseInt(madeFromObj[2].toString()));
			if (madeFromObj[3] != null)
				mfgCtrlMadeFrom.setProcessPointId(madeFromObj[3].toString());
			if (madeFromObj[4] != null)
				mfgCtrlMadeFrom.setOperationName(madeFromObj[4].toString());
			if (madeFromObj[5] != null)
				mfgCtrlMadeFrom.setOperationRevision(Integer.parseInt(madeFromObj[5].toString()));
			if (madeFromObj[6] != null)
				mfgCtrlMadeFrom.setOperationType(madeFromObj[6].toString());
			if (madeFromObj[7] != null)
				mfgCtrlMadeFrom.setOperationSequenceNum(Integer.parseInt(madeFromObj[7].toString()));
			if (madeFromObj[8] != null)
				mfgCtrlMadeFrom.setPartId(madeFromObj[8].toString());
			if (madeFromObj[9] != null)
				mfgCtrlMadeFrom.setPartRevision(Integer.parseInt(madeFromObj[9].toString()));
			if (madeFromObj[10] != null)
				mfgCtrlMadeFrom.setPartNo(madeFromObj[10].toString());
			if (madeFromObj[11] != null)
				mfgCtrlMadeFrom.setPartSectionCode(madeFromObj[11].toString());
			if (madeFromObj[12] != null)
				mfgCtrlMadeFrom.setPartItemNo(madeFromObj[12].toString());
			if (madeFromObj[13] != null)
				mfgCtrlMadeFrom.setPartMask(madeFromObj[13].toString());
			if (madeFromObj[14] != null)
				mfgCtrlMadeFrom.setPartMark(madeFromObj[14].toString());

			mfgCtrlMadeFromLst.add(mfgCtrlMadeFrom);
		}
		return mfgCtrlMadeFromLst;
	}

	public List<MCOrderStructure> getUnmappedOrderIds() {
		return findAllByNativeQuery(FIND_NOT_MAPPED_ORDER_IDS,null,MCOrderStructure.class);
	}
	
	public MCOrderStructure findByKey(String orderId, String modeId) {
		return findByKey(new MCOrderStructureId(orderId, modeId));
	}
	
	public void removeByKey(String orderId, String modeId) {
		removeByKey(new MCOrderStructureId(orderId, modeId));
	}

	public List<StructureDetailsDto> findByOrderNumberAndDivision(String orderNo, String divisionId) {
		String likeOrderNo = "%" + orderNo + "%";
		Parameters params = Parameters.with("1", likeOrderNo).put("2", divisionId);
		return findAllByNativeQuery(GET_BY_ORDER_NO_AND_DIVISION, params, StructureDetailsDto.class);
	}
	
	public List<MCOrderStructure> findOrderByStructureRevAndDivId(long structureRev, String divisionId) {
		return findAll(Parameters.with("structureRevision", structureRev).put("id.divisionId", divisionId));
	}
	
	public List<StructureUnitDetailsDto> findAllUnitDetailsByOrderNoAndDivision(String orderNo, String divisionId){
		Parameters params = Parameters.with("1", divisionId).put("2", orderNo);
		return findAllByNativeQuery(GET_STRUCTURE_DETAILS, params, StructureUnitDetailsDto.class);
	}
}
