package com.honda.galc.dao.jpa.oif;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.fif.BomId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;
/**
 * 
 * <h3>BomDaoImpl.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BomDaoImpl.java description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Jiamei Li</TD>
 * <TD>Feb 26, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */
/** * * 
 * @Oif PDDA Part Mark Interface  
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012
 */
public class BomDaoImpl extends BaseDaoImpl<Bom, BomId> implements BomDao {

	private static String LIST_OF_BOMTBX_PAPID_DATA = "SELECT A.MTC_MODEL,A.MTC_COLOR,A.MTC_OPTION,A.MTC_TYPE,A.INT_COLOR_CODE,B.PART_CODE,B.ATTRIBUTE " +
			"FROM GALADM.BOM_TBX A JOIN VIOS.PAPID1 B ON B.PART_NO=A.PART_NO";

	private static String FIND_EFFECTIVE_DATE = "select distinct BOM.EFF_BEG_DATE,BOM.EFF_END_DATE, MTR.SPEC_CODE_MASK from GALADM.BOM_TBX BOM JOIN "
			+ "GALADM.MC_OP_PART_REV_TBX PARTREV on "
			+ "(BOM.PART_NO=PARTREV.PART_NO and PARTREV.PART_SECTION_CODE=BOM.PART_SECTION_CODE and "
			+ "PARTREV.PART_ITEM_NO=BOM.PART_ITEM_NO) JOIN GALADM.MC_OP_PART_MATRIX_TBX MTR ON "
			+ "(PARTREV.OPERATION_NAME = MTR.OPERATION_NAME and PARTREV.PART_REV = MTR.PART_REV and "
			+ "PARTREV.PART_ID=MTR.PART_ID)  "
			+ "WHERE BOM.MTC_MODEL=SUBSTR (?1 ,1, 4) AND BOM.MTC_TYPE = SUBSTR (?1, 5, 3) AND MTR.SPEC_CODE_MASK = ?1 AND MTR.OPERATION_NAME = ?2 and "
			+ "BOM.PART_NO = ?3 AND CURRENT_DATE between BOM.EFF_BEG_DATE and "
			+ "BOM.EFF_END_DATE order by BOM.EFF_END_DATE FETCH FIRST ROW ONLY";


	private static String LIST_OF_UPDATED_PAPID_DATA = "select distinct pid.part_no, pid.plant_loc_code, pid.model_year_date, pid.vehicle_model_code, pid.part_code AS PART_MARK, pid.part_desc, pid.station_address,"
			+ "pid.part_code_locator, pik.mask_position, pik.mask_length, pia.mask_code AS PART_MASK, pid.display_type, pid.structure_length, pid.part_identification_id "
			+ " from vios.papid1 pid"
			+ " left outer join vios.papik1 pik on pid.part_identification_id = pik.part_identification_id "
			+ " left outer join vios.papia1 pia on pia.pid_part_mask_id = pik.pid_part_mask_id"
			+ " where (pid.UPDATE_TIMESTAMP > ?1) or (pid.UPDATE_TIMESTAMP is null and pid.CREATE_TIMESTAMP > ?1)"
			+ " and (pid.plant_loc_code = ?2)";

	private static String PRODUCT_SPEC_TO_REPLICATE = "select * from bom_tbx a " + 
			"    inner join gal144tbx b on a.MTC_MODEL = b.MODEL_YEAR_CODE||b.MODEL_CODE " + 
			"        and a.MTC_TYPE = b.MODEL_TYPE_CODE " + 
			"        and (a.MTC_OPTION = b.MODEL_OPTION_CODE or a.MTC_OPTION = '') " + 
			"        and (a.MTC_COLOR = b.EXT_COLOR_CODE or a.MTC_COLOR = '') " + 
			"        and (a.INT_COLOR_CODE = b.INT_COLOR_CODE or a.INT_COLOR_CODE = '') " + 
			"where b.product_spec_code = (select product_spec_code from gal212tbx where production_lot = ?1) " + 
			"    and current_date between a.EFF_BEG_DATE and a.EFF_END_DATE " + 
            "    and @PART_NOT_LIKE@ "+
            "    and @PART_LIKE@ order by a.EFF_BEG_DATE  desc FETCH FIRST ROW ONLY";
	
	private static String LIST_OF_PAPID_IMAGE_DATA = "select image from vios.papll1 pll where pll.part_identification_id = ?1";

	private static String LIST_OF_VALID_MODELS = "SELECT * FROM GALADM.BOM_TBX WHERE (CURRENT_DATE BETWEEN EFF_BEG_DATE AND EFF_END_DATE) " ;

	private static String LIST_OF_BOM_MODELS = "SELECT * FROM GALADM.BOM_TBX "     
			+" WHERE PLANT_LOC_CODE  = ?2"
			+" AND SUBSTR(PART_NO, 1,5)  = ?1"
			+" AND EFF_END_DATE >= CURRENT_DATE"
			+" AND SUBSTR(MTC_MODEL, 1, 1) IN (SELECT DISTINCT MODEL_YEAR_CODE FROM GALADM.GAL144TBX WHERE MODEL_YEAR_DESCRIPTION >= (YEAR(CURRENT_DATE)-1))"
			+" ORDER BY MTC_MODEL, PART_NO, PART_COLOR_CODE";

	private static String FIND_ALL_BY_PART_NO_AND_MTC = "SELECT * FROM GALADM.BOM_TBX"     
			+" WHERE PART_NO like '@PART_NO_SUBSTR@%'"
			+" AND MTC_MODEL = ?2"
			+" AND MTC_TYPE = ?3"
			+" AND PLANT_LOC_CODE  = ?1";

	private static String FIND_MODEL_AND_TYPE_BY_MODEL_YEAR_CODE_AND_PART_NO_PREFIX = "SELECT DISTINCT BOM.MTC_MODEL, MTC_TYPE FROM GALADM.BOM_TBX BOM"
			+" WHERE BOM.PART_NO LIKE '@PART_NO_PREFIX@%'"     
			+" AND BOM.MTC_MODEL LIKE '@MODEL_YEAR_CODE@%'"
			+" AND BOM.EFF_END_DATE >= CURRENT_DATE"
			+" AND EXISTS (SELECT 1 FROM GALADM.GAL144TBX G144 WHERE BOM.MTC_MODEL = CONCAT(G144.MODEL_YEAR_CODE, G144.MODEL_CODE) AND BOM.MTC_TYPE = G144.MODEL_TYPE_CODE FETCH FIRST ROW ONLY)";

	private static String FIND_MODEL_AND_TYPE_BY_SYSTEM_MODEL_GROUP_AND_PART_NO_PREFIX = "SELECT DISTINCT BOM.MTC_MODEL, BOM.MTC_TYPE FROM GALADM.BOM_TBX BOM"
			+" WHERE BOM.PART_NO LIKE '@PART_NO_PREFIX@%'"
			+" AND BOM.MTC_MODEL IN (SELECT MGRP.MTC_MODEL FROM GALADM.MODEL_GROUPING_TBX MGRP WHERE MGRP.SYSTEM = ?1 AND MGRP.MODEL_GROUP = ?2)"
			+" AND BOM.EFF_END_DATE > CURRENT_DATE"
			+" AND EXISTS (SELECT 1 FROM GALADM.GAL144TBX G144 WHERE BOM.MTC_MODEL = CONCAT(G144.MODEL_YEAR_CODE, G144.MODEL_CODE) AND BOM.MTC_TYPE = G144.MODEL_TYPE_CODE FETCH FIRST ROW ONLY)";

	private static String FIND_ALL_BY_MODEL_YEAR_CODE_PART_NO_PREFIX_AND_PART_COLOR_CODE = "SELECT BOM.* FROM GALADM.BOM_TBX BOM"
			+" WHERE BOM.PART_NO LIKE '@PART_NO_PREFIX@%'"     
			+" AND BOM.MTC_MODEL LIKE '@MODEL_YEAR_CODE@%'"
			+" AND BOM.PART_COLOR_CODE = ?1"
			+" AND BOM.EFF_END_DATE >= CURRENT_DATE"
			+" AND EXISTS (SELECT 1 FROM GALADM.GAL144TBX G144 WHERE BOM.MTC_MODEL = CONCAT(G144.MODEL_YEAR_CODE, G144.MODEL_CODE) AND BOM.MTC_TYPE = G144.MODEL_TYPE_CODE FETCH FIRST ROW ONLY)";

	private static String FIND_ALL_BY_SYSTEM_MODEL_GROUP_PART_NO_PREFIX_AND_PART_COLOR_CODE = "SELECT BOM.* FROM GALADM.BOM_TBX BOM"
			+" WHERE BOM.PART_NO LIKE '@PART_NO_PREFIX@%'"
			+" AND BOM.MTC_MODEL IN (SELECT MGRP.MTC_MODEL FROM GALADM.MODEL_GROUPING_TBX MGRP WHERE MGRP.SYSTEM = ?1 AND MGRP.MODEL_GROUP = ?2)"
			+" AND BOM.PART_COLOR_CODE = ?3"
			+" AND BOM.EFF_END_DATE > CURRENT_DATE"
			+" AND EXISTS (SELECT 1 FROM GALADM.GAL144TBX G144 WHERE BOM.MTC_MODEL = CONCAT(G144.MODEL_YEAR_CODE, G144.MODEL_CODE) AND BOM.MTC_TYPE = G144.MODEL_TYPE_CODE FETCH FIRST ROW ONLY)";
	
	private static String FIND_BOM_PART_NO = "SELECT DISTINCT b.PART_NO FROM BOM_TBX b" + 
			"    INNER JOIN GAL144TBX fs ON CONCAT(fs.MODEL_YEAR_CODE,fs.MODEL_CODE) = b.MTC_MODEL" + 
			"        AND fs.MODEL_TYPE_CODE = b.MTC_TYPE" + 
			"        AND (fs.MODEL_OPTION_CODE = b.MTC_OPTION OR b.MTC_OPTION = '')" + 
			"        AND (fs.EXT_COLOR_CODE = b.MTC_COLOR OR b.MTC_COLOR = '')" + 
			"    WHERE fs.PRODUCT_SPEC_CODE = ?1" + 
			"        AND b.PART_NO = ?2" + 
			"        AND CURRENT_DATE BETWEEN b.EFF_BEG_DATE and b.EFF_END_DATE";
			
   private static String FIND_VALID_BOM_PART_NO = "select * from BOM_TBX where PART_NO like '%@partNo@%' and EFF_BEG_DATE <= CURRENT_DATE and EFF_END_DATE >= CURRENT_DATE";

	public List<Object[]> getAllBomtbxPapidData(){		
		return findAllByNativeQuery(LIST_OF_BOMTBX_PAPID_DATA, null,Object[].class);
	}

	public List<Bom> findAllBy(String partNo, String mtcModel, String mtcType) {
		Parameters parameters = Parameters.with("id.partNo", partNo);
		parameters.put("id.mtcModel", mtcModel);
		parameters.put("id.mtcType", mtcType);
		return findAll(parameters);
	}

	public List<Object[]> findEffectiveDate(String specCodeMask , String operationName , String partNo){
		Parameters param = Parameters.with("1", specCodeMask);
		param.put("2", operationName);
		param.put("3", partNo);
		List<Object[]> dates = executeNative(param, FIND_EFFECTIVE_DATE);
		return dates;

	}

	public List<Bom> getPartList(String partNo, String partSectionCode, String partItemNo, String mtcModel,
			String mtcType, String mtcOption, String mtcColor, String intColorCode) {
		//Setting up all the parameters
		List<Bom> bomList = null;
		Parameters params = Parameters.with("id.partNo", partNo);
		params.put("id.partSectionCode", partSectionCode);
		params.put("id.partItemNo", partItemNo);
		params.put("id.mtcModel", mtcModel);
		params.put("id.mtcType", mtcType);
		if(StringUtils.isNotBlank(mtcOption)) {
			params.put("id.mtcOption", mtcOption);
		}
		if(StringUtils.isNotBlank(mtcColor)) {
			params.put("id.mtcColor", mtcColor);
		}
		if(StringUtils.isNotBlank(intColorCode)) {
			params.put("id.intColorCode", intColorCode);
		}
		bomList = findAll(params);
		if(bomList!=null && !bomList.isEmpty()) {
			return bomList;
		}
		//BOM List is empty with entire spec code; reducing filter criteria from right to left
		if(params.getParameters().containsKey("id.intColorCode")) {
			params.getParameters().remove("id.intColorCode");
			bomList = findAll(params);
			if(bomList!=null && !bomList.isEmpty()) {
				return bomList;
			}
		}
		if(params.getParameters().containsKey("id.mtcColor")) {
			params.getParameters().remove("id.mtcColor");
			bomList = findAll(params);
			if(bomList!=null && !bomList.isEmpty()) {
				return bomList;
			}
		}
		if(params.getParameters().containsKey("id.mtcOption")) {
			params.getParameters().remove("id.mtcOption");
			bomList = findAll(params);
			if(bomList!=null && !bomList.isEmpty()) {
				return bomList;
			}
		}
		//REMARK - Not reducing filter further, mtc_model and mtc_type are concrete values
		return new ArrayList<Bom>();
	}

	public List<Object[]> getUpdatedPapidData(String plantCode, String lastRunTimestamp) {
		Parameters param = Parameters.with("1", lastRunTimestamp);
		param.put("2", plantCode);

		return executeNative(param, LIST_OF_UPDATED_PAPID_DATA);
	}

	public List<Object[]> findImageByPartId(Integer partIdentificationId) {
		Parameters param = Parameters.with("1", partIdentificationId);

		return executeNative(param, LIST_OF_PAPID_IMAGE_DATA);
	}
	public List<Bom> findAllValidModels(String plantCode, String partNumber){
		String query = LIST_OF_VALID_MODELS+" AND PLANT_LOC_CODE ='"+plantCode+"' AND PART_NO like '"+partNumber+"%'";
		return findAllByNativeQuery(query, null);
	}

	public List<Bom> findAllByPartNo(String partNo, String plantCode) {
		Parameters parameters = Parameters.with("1", partNo);
		parameters.put("2", plantCode);

		return findAllByNativeQuery(LIST_OF_BOM_MODELS, parameters);
	}

	public List<Bom> findAllByPartNoAndMtc(String partNo, String plantCode, String mtcModel, String mtcType) {
		String partNoSubstr = partNo.substring(0,11);
		Parameters parameters = Parameters.with("1", plantCode);
		parameters.put("2", mtcModel);
		parameters.put("3", mtcType);
		return findAllByNativeQuery(FIND_ALL_BY_PART_NO_AND_MTC.replace("@PART_NO_SUBSTR@", partNoSubstr), parameters);
	}
	
	public Bom findProductSpecToBeReplicate(String productionLot, List<String> notLikeParts, List<String> likeParts) {
		Parameters parameters = Parameters.with("1", productionLot);
		
        String notLikeCondition = StringUtil.toSqlNotLikeString(notLikeParts, "a.part_no"); 
        String likeCondition = StringUtil.toSqlLikeString(likeParts, "a.part_no");
        String sql = PRODUCT_SPEC_TO_REPLICATE.replace("@PART_NOT_LIKE@", notLikeCondition).replace("@PART_LIKE@", likeCondition);
		return findFirstByNativeQuery(sql, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findModelAndTypeByModelYearCodeAndPartNoPrefix(String modelYearCode, String partNoPrefix) {
		return findResultListByNativeQuery(FIND_MODEL_AND_TYPE_BY_MODEL_YEAR_CODE_AND_PART_NO_PREFIX.replace("@MODEL_YEAR_CODE@", modelYearCode).replace("@PART_NO_PREFIX@", partNoPrefix), null);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findModelAndTypeBySystemModelGroupAndPartNoPrefix(String system, String modelGroup, String partNoPrefix) {
		Parameters parameters = Parameters.with("1", system).put("2", modelGroup);
		return findResultListByNativeQuery(FIND_MODEL_AND_TYPE_BY_SYSTEM_MODEL_GROUP_AND_PART_NO_PREFIX.replace("@PART_NO_PREFIX@", partNoPrefix), parameters);
	}

	public List<Bom> findAllByModelYearCodePartNoPrefixAndPartColorCode(String modelYearCode, String partNoPrefix, String partColorCode) {
		Parameters parameters = Parameters.with("1", partColorCode);
		return findAllByNativeQuery(FIND_ALL_BY_MODEL_YEAR_CODE_PART_NO_PREFIX_AND_PART_COLOR_CODE.replace("@MODEL_YEAR_CODE@", modelYearCode).replace("@PART_NO_PREFIX@", partNoPrefix), parameters);
	}

	public List<Bom> findAllBySystmMdlGrpPrtPrfxNPrtClrCd(String system, String modelGroup, String partNoPrefix, String partColorCode) {
		Parameters parameters = Parameters.with("1", system)
				.put("2", modelGroup)
				.put("3", partColorCode);
		return findAllByNativeQuery(FIND_ALL_BY_SYSTEM_MODEL_GROUP_PART_NO_PREFIX_AND_PART_COLOR_CODE.replace("@PART_NO_PREFIX@", partNoPrefix), parameters);
	}

	@Override
	public String getBomPartNo(String parentProductSpec, String mbpnProductSpec) {
		return findFirstByNativeQuery(FIND_BOM_PART_NO, Parameters.with("1", parentProductSpec).put("2", mbpnProductSpec), String.class);
	}

	@Override
	public boolean isBomPartNoValid(String partNo) {
		List<Bom> bomRecords = 	findAllByNativeQuery(FIND_VALID_BOM_PART_NO.replace("@partNo@", partNo), null);
		if(bomRecords.isEmpty())
			return false;
		
		return true;
	}

}