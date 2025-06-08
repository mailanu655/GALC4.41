
package com.honda.galc.dao.jpa.product;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.StragglerCodeAssignmentDto;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.entity.product.StragglerId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 05, 2015
 */
public class StragglerDaoImpl extends BaseDaoImpl<Straggler,StragglerId> implements StragglerDao {	
	
	private static String FIND_CURRENT_BY_PP_DELAYED_AT = "SELECT A.* FROM GALADM.STRAGGLER_TBX A WHERE A.PP_DELAYED_AT = ?1 AND NOT EXISTS"
			+" (SELECT 1 FROM GALADM.GAL215TBX B WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID = ?1 FETCH FIRST ROW ONLY) ORDER BY A.IDENTIFIED_TIMESTAMP DESC, PRODUCT_ID";
	
	private static String FIND_ALL_STRAGGLER_CODE_ASSIGNMENT_DATA = "SELECT STRAGGLER.IDENTIFIED_TIMESTAMP, STRAGGLER.PRODUCT_ID, STRAGGLER.PP_DELAYED_AT,STRAGGLER.STRAGGLER_TYPE, PRODUCT.KD_LOT_NUMBER, PRODUCT.PRODUCT_SPEC_CODE, STRAGGLER.CODE, STRAGGLER.COMMENTS"
			+" FROM GALADM.STRAGGLER_TBX STRAGGLER JOIN GALADM.@PRODUCT_TABLE@ PRODUCT ON STRAGGLER.PRODUCT_ID = PRODUCT.@PRODUCT_ID_COLUMN@"
			+" WHERE STRAGGLER.PP_DELAYED_AT = '@PP_DELAYED_AT@'"
			+" ORDER BY STRAGGLER.IDENTIFIED_TIMESTAMP DESC, STRAGGLER.PRODUCT_ID";
	
	private static String FIND_CURRENT_STRAGGLER_CODE_ASSIGNMENT_DATA = "SELECT STRAGGLER.IDENTIFIED_TIMESTAMP, STRAGGLER.PRODUCT_ID, STRAGGLER.PP_DELAYED_AT,STRAGGLER.STRAGGLER_TYPE, PRODUCT.KD_LOT_NUMBER, PRODUCT.PRODUCT_SPEC_CODE, STRAGGLER.CODE, STRAGGLER.COMMENTS"
			+" FROM GALADM.STRAGGLER_TBX STRAGGLER JOIN GALADM.@PRODUCT_TABLE@ PRODUCT ON STRAGGLER.PRODUCT_ID = PRODUCT.@PRODUCT_ID_COLUMN@"
			+" WHERE STRAGGLER.PP_DELAYED_AT = '@PP_DELAYED_AT@'"
			+" AND NOT EXISTS (SELECT 1 FROM GALADM.GAL215TBX B WHERE STRAGGLER.PRODUCT_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID = '@PP_DELAYED_AT@' FETCH FIRST ROW ONLY)"
			+" AND NOT EXISTS (SELECT 1 FROM GALADM.GAL136TBX C WHERE STRAGGLER.PRODUCT_ID = C.PRODUCT_ID)"
			+" ORDER BY STRAGGLER.IDENTIFIED_TIMESTAMP DESC, STRAGGLER.PRODUCT_ID";
	
	private static String FIND_NON_CURRENT_STRAGGLER_CODE_ASSIGNMENT_DATA = "SELECT STRAGGLER.IDENTIFIED_TIMESTAMP, STRAGGLER.PRODUCT_ID, STRAGGLER.PP_DELAYED_AT,STRAGGLER.STRAGGLER_TYPE, PRODUCT.KD_LOT_NUMBER, PRODUCT.PRODUCT_SPEC_CODE, STRAGGLER.CODE, STRAGGLER.COMMENTS"
			+" FROM GALADM.STRAGGLER_TBX STRAGGLER JOIN GALADM.@PRODUCT_TABLE@ PRODUCT ON STRAGGLER.PRODUCT_ID = PRODUCT.@PRODUCT_ID_COLUMN@"
			+" WHERE STRAGGLER.PP_DELAYED_AT = '@PP_DELAYED_AT@'"
			+" AND ("
			+" EXISTS (SELECT 1 FROM GALADM.GAL215TBX B WHERE STRAGGLER.PRODUCT_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID = '@PP_DELAYED_AT@' FETCH FIRST ROW ONLY)"
			+" OR EXISTS (SELECT 1 FROM GALADM.GAL136TBX C WHERE STRAGGLER.PRODUCT_ID = C.PRODUCT_ID)"
			+" ) ORDER BY STRAGGLER.IDENTIFIED_TIMESTAMP DESC, STRAGGLER.PRODUCT_ID";
	
	private static String FIND_PREVIOUS_UNPROCESSSED_LOTS = " WITH X (PRODUCT_ID, LAST_PASSING_PROCESS_POINT_ID, NEXT_PRODUCT_ID, LINE_ID, LINE_PRODUCT_SEQ, PRODUCTION_LOT)"
			+" AS (SELECT ROOT.PRODUCT_ID,ROOT.LAST_PASSING_PROCESS_POINT_ID,ROOT.NEXT_PRODUCT_ID,ROOT.LINE_ID,1 AS LINE_PRODUCT_SEQ,ROOT.PRODUCTION_LOT"
			+" FROM GALADM.GAL176TBX ROOT WHERE     ROOT.NEXT_PRODUCT_ID IS NULL AND ROOT.LINE_ID IN"
			+" (SELECT L.LINE_ID FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l ON p.LINE_ID = l.LINE_ID LEFT JOIN GALADM.GAL128TBX D"
			+" ON L.DIVISION_ID = D.DIVISION_ID WHERE     D.PLANT_NAME = @PLANT@ AND     (coalesce (d.SEQUENCE_NUMBER, 0) + 1)* 1000000"
			+" +   (  coalesce (l.LINE_SEQUENCE_NUMBER,0) + 1)* 1000 + (coalesce (p.SEQUENCE_NUMBER, 0) + 1) <"
			+" (SELECT     (  coalesce (d.SEQUENCE_NUMBER,0)+ 1) * 1000000 +   (  coalesce (l.LINE_SEQUENCE_NUMBER, 0)+ 1)* 1000 + (  coalesce ( p.SEQUENCE_NUMBER, 0) + 1)"
			+" ALL_SEQ_NUM FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l ON p.LINE_ID = l.LINE_ID"
			+" LEFT JOIN GALADM.GAL128TBX D ON L.DIVISION_ID = D.DIVISION_ID  WHERE     D.PLANT_NAME = @PLANT@ AND P.PROCESS_POINT_ID =@CURRENT_PP@) GROUP BY L.LINE_ID)"
			+" UNION ALL "
			+" SELECT SUB.PRODUCT_ID,SUB.LAST_PASSING_PROCESS_POINT_ID,SUB.NEXT_PRODUCT_ID, SUB.LINE_ID, SUPER.LINE_PRODUCT_SEQ + 1 AS LINE_PRODUCT_SEQ,SUB.PRODUCTION_LOT"
			+" FROM GALADM.GAL176TBX SUB, X SUPER WHERE SUB.NEXT_PRODUCT_ID = SUPER.PRODUCT_ID)"
			+" SELECT LOT.PRODUCT_ID, CURRENT_TIMESTAMP AS IDENTIFIED_TIMESTAMP,STRAGGLERS_BEHIND.LAST_PASSING_PROCESS_POINT_ID,STRAGGLERS_BEHIND.UNITS_BEHIND"
			+" FROM X LOT LEFT JOIN GALADM.GAL215TBX HIST ON     HIST.PROCESS_POINT_ID = @CURRENT_PP@ AND LOT.PRODUCT_ID = HIST.PRODUCT_ID"
			+" LEFT JOIN (SELECT DIV.SEQUENCE_NUMBER,DIV.DIVISION_NAME,LINE.LINE_SEQUENCE_NUMBER,LINE.LINE_NAME, X.LINE_ID, X.LINE_PRODUCT_SEQ,X.PRODUCT_ID,X.LAST_PASSING_PROCESS_POINT_ID,"
			+" row_number () OVER () AS UNITS_BEHIND FROM X LEFT JOIN GAL195TBX LINE ON X.LINE_ID = LINE.LINE_ID"
			+" LEFT JOIN GALADM.GAL128TBX DIV ON LINE.DIVISION_ID = DIV.DIVISION_ID"
			+" ORDER BY DIV.SEQUENCE_NUMBER DESC, LINE.LINE_SEQUENCE_NUMBER DESC, X.LINE_PRODUCT_SEQ DESC) AS STRAGGLERS_BEHIND"
			+" ON LOT.PRODUCT_ID = STRAGGLERS_BEHIND.PRODUCT_ID WHERE LOT.PRODUCTION_LOT = @CURRENT_LOT@ AND HIST.ACTUAL_TIMESTAMP IS NULL" 
			+" AND NOT EXISTS (SELECT 1 FROM GALADM.STRAGGLER_TBX STRAGGLERS_EXISTING WHERE STRAGGLERS_EXISTING.PRODUCT_ID = LOT.PRODUCT_ID AND STRAGGLERS_EXISTING.PP_DELAYED_AT = @CURRENT_PP@ AND STRAGGLERS_EXISTING.ACTUAL_PROCESS_TIMESTAMP IS NULL AND STRAGGLERS_EXISTING.STRAGGLER_TYPE=@STRG_TYPE@ FETCH FIRST ROW ONLY)"
			+" ORDER BY STRAGGLERS_BEHIND.UNITS_BEHIND  WITH CS FOR READ ONLY";
	
	private static String FIND_PREVIOUS_UNPROCESSSED_LOTS_BY_KDLOT = " WITH X (PRODUCT_ID, LAST_PASSING_PROCESS_POINT_ID, NEXT_PRODUCT_ID, LINE_ID, LINE_PRODUCT_SEQ, PRODUCTION_LOT)"
			+" AS (SELECT ROOT.PRODUCT_ID,ROOT.LAST_PASSING_PROCESS_POINT_ID,ROOT.NEXT_PRODUCT_ID,ROOT.LINE_ID,1 AS LINE_PRODUCT_SEQ,ROOT.PRODUCTION_LOT"
			+" FROM GALADM.GAL176TBX ROOT WHERE     ROOT.NEXT_PRODUCT_ID IS NULL AND ROOT.LINE_ID IN"
			+" (SELECT L.LINE_ID FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l ON p.LINE_ID = l.LINE_ID LEFT JOIN GALADM.GAL128TBX D"
			+" ON L.DIVISION_ID = D.DIVISION_ID WHERE     D.PLANT_NAME = @PLANT@ AND     (coalesce (d.SEQUENCE_NUMBER, 0) + 1)* 1000000"
			+" +   (  coalesce (l.LINE_SEQUENCE_NUMBER,0) + 1)* 1000 + (coalesce (p.SEQUENCE_NUMBER, 0) + 1) <"
			+" (SELECT     (  coalesce (d.SEQUENCE_NUMBER,0)+ 1) * 1000000 +   (  coalesce (l.LINE_SEQUENCE_NUMBER, 0)+ 1)* 1000 + (  coalesce ( p.SEQUENCE_NUMBER, 0) + 1)"
			+" ALL_SEQ_NUM FROM galadm.gal214tbx p LEFT JOIN galadm.gal195tbx l ON p.LINE_ID = l.LINE_ID"
			+" LEFT JOIN GALADM.GAL128TBX D ON L.DIVISION_ID = D.DIVISION_ID  WHERE     D.PLANT_NAME = @PLANT@ AND P.PROCESS_POINT_ID =@CURRENT_PP@) GROUP BY L.LINE_ID)"
			+" UNION ALL "
			+" SELECT SUB.PRODUCT_ID,SUB.LAST_PASSING_PROCESS_POINT_ID,SUB.NEXT_PRODUCT_ID, SUB.LINE_ID, SUPER.LINE_PRODUCT_SEQ + 1 AS LINE_PRODUCT_SEQ,SUB.PRODUCTION_LOT"
			+" FROM GALADM.GAL176TBX SUB, X SUPER WHERE SUB.NEXT_PRODUCT_ID = SUPER.PRODUCT_ID)"
			+" SELECT LOT.PRODUCT_ID, CURRENT_TIMESTAMP AS IDENTIFIED_TIMESTAMP,STRAGGLERS_BEHIND.LAST_PASSING_PROCESS_POINT_ID,STRAGGLERS_BEHIND.UNITS_BEHIND"
			+" FROM X LOT LEFT JOIN GALADM.GAL215TBX HIST ON     HIST.PROCESS_POINT_ID = @CURRENT_PP@ AND LOT.PRODUCT_ID = HIST.PRODUCT_ID"
			+" LEFT JOIN (SELECT DIV.SEQUENCE_NUMBER,DIV.DIVISION_NAME,LINE.LINE_SEQUENCE_NUMBER,LINE.LINE_NAME, X.LINE_ID, X.LINE_PRODUCT_SEQ,X.PRODUCT_ID,X.LAST_PASSING_PROCESS_POINT_ID,"
			+" row_number () OVER () AS UNITS_BEHIND FROM X LEFT JOIN GAL195TBX LINE ON X.LINE_ID = LINE.LINE_ID"
			+" LEFT JOIN GALADM.GAL128TBX DIV ON LINE.DIVISION_ID = DIV.DIVISION_ID"
			+" ORDER BY DIV.SEQUENCE_NUMBER DESC, LINE.LINE_SEQUENCE_NUMBER DESC, X.LINE_PRODUCT_SEQ DESC) AS STRAGGLERS_BEHIND"
			+" ON LOT.PRODUCT_ID = STRAGGLERS_BEHIND.PRODUCT_ID WHERE LOT.PRODUCTION_LOT in (SELECT PRODUCTION_LOT FROM GALADM.GAL212TBX WHERE KD_LOT_NUMBER = @CURRENT_LOT@) AND HIST.ACTUAL_TIMESTAMP IS NULL" 
			+" AND NOT EXISTS (SELECT 1 FROM GALADM.STRAGGLER_TBX STRAGGLERS_EXISTING WHERE STRAGGLERS_EXISTING.PRODUCT_ID = LOT.PRODUCT_ID AND STRAGGLERS_EXISTING.PP_DELAYED_AT = @CURRENT_PP@ AND STRAGGLERS_EXISTING.ACTUAL_PROCESS_TIMESTAMP IS NULL AND STRAGGLERS_EXISTING.STRAGGLER_TYPE=@STRG_TYPE@  FETCH FIRST ROW ONLY)"
			+" ORDER BY STRAGGLERS_BEHIND.UNITS_BEHIND  WITH CS FOR READ ONLY";
	
	
	public List<Straggler> findCurrentByPpDelayedAt(String ppDelayedAt) {
		Parameters params = Parameters.with("1", ppDelayedAt);
		return findAllByNativeQuery(FIND_CURRENT_BY_PP_DELAYED_AT, params, Straggler.class);
	}
	
	public List<StragglerCodeAssignmentDto> findStragglerCodeAssignmentData(String ppDelayedAt, ProductType productType, boolean findCurrent, boolean findNonCurrent) {
		if (!findCurrent && !findNonCurrent) {
			return null;
		}
		ProductTypeUtil productTypeUtil = ProductTypeUtil.getTypeUtil(productType);
		Class<? extends BaseProduct> productClass = productTypeUtil.getProductClass();
		if (!Product.class.isAssignableFrom(productClass)) {
			throw new IllegalArgumentException("Product type " + productType.name() + " is not valid for Straggler Code Assignment");
		}
		String productTable = CommonUtil.getTableName(productClass);
		String productIdColumn = CommonUtil.getIdColumnName(productClass);
		String sql = null;
		if (findCurrent) {
			if (findNonCurrent) {
				sql = FIND_ALL_STRAGGLER_CODE_ASSIGNMENT_DATA.replace("@PP_DELAYED_AT@", ppDelayedAt).replace("@PRODUCT_TABLE@", productTable).replace("@PRODUCT_ID_COLUMN@", productIdColumn);
			} else {
				sql = FIND_CURRENT_STRAGGLER_CODE_ASSIGNMENT_DATA.replace("@PP_DELAYED_AT@", ppDelayedAt).replace("@PRODUCT_TABLE@", productTable).replace("@PRODUCT_ID_COLUMN@", productIdColumn);
			}
		} else {
			sql = FIND_NON_CURRENT_STRAGGLER_CODE_ASSIGNMENT_DATA.replace("@PP_DELAYED_AT@", ppDelayedAt).replace("@PRODUCT_TABLE@", productTable).replace("@PRODUCT_ID_COLUMN@", productIdColumn);
		}
		List<Object[]> results = executeNative(sql);
		if (results == null || results.isEmpty()) {
			return null;
		}
		List<StragglerCodeAssignmentDto> stragglerCodeAssignmentData = new ArrayList<StragglerCodeAssignmentDto>();
		for (Object[] result : results) {
			StragglerCodeAssignmentDto dto = new StragglerCodeAssignmentDto();
			dto.setIdentifiedTimestamp((Timestamp) result[0]);
			dto.setProductId((String) result[1]);
			dto.setPpDelayedAt((String) result[2]);
			dto.setStragglerType((String) result[3]);
			dto.setKdLotNumber((String) result[4]);
			dto.setModel(ProductSpecUtil.extractModelYearCode((String) result[5]).concat(ProductSpecUtil.extractModelCode((String) result[5])));
			dto.setType(ProductSpecUtil.extractModelTypeCode((String) result[5]));
			dto.setOption(ProductSpecUtil.extractModelOptionCode((String) result[5]));
			dto.setColor(ProductSpecUtil.extractExtColorCode((String) result[5]));
			dto.setIntColor(ProductSpecUtil.extractIntColorCode((String) result[5]));
			dto.setCode((String) result[6]);
			dto.setComments((String) result[7]);
			stragglerCodeAssignmentData.add(dto);
		}
		return stragglerCodeAssignmentData;
	}
	
	public List<Straggler> findStragglerProductList(String vin, String processPointId)
	{
		return findAll(Parameters.with("id.productId", vin).put("id.ppDelayedAt", processPointId));
	}
	
	
	public List<Object[]> findPrevUnProcessedLots(String plantName,String processPointId,String currentProdLot, String stragglerType) {
		String qry = FIND_PREVIOUS_UNPROCESSSED_LOTS.replace("@PLANT@","'" + plantName.trim() + "'")
				.replace("@CURRENT_PP@","'" + processPointId.trim() + "'")
				.replace("@CURRENT_LOT@","'" + currentProdLot.trim() + "'")
				.replace("@STRG_TYPE@", "'" + stragglerType.trim() + "'");
		
		return executeNative(qry);
	}
	
	public List<Object[]> findPrevUnProcessedLotsByKDLot(String plantName,String processPointId,String currentProdLot, String stragglerType) {
		String qry = FIND_PREVIOUS_UNPROCESSSED_LOTS_BY_KDLOT.replace("@PLANT@","'" + plantName.trim() + "'")
				.replace("@CURRENT_PP@","'" + processPointId.trim() + "'")
				.replace("@CURRENT_LOT@","'" + currentProdLot.trim() + "'")
				.replace("@STRG_TYPE@", "'" + stragglerType.trim() + "'");
	
		return executeNative(qry);
	}
	
}

