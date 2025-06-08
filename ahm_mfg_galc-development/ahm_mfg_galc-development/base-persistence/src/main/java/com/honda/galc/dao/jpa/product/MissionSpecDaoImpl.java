package com.honda.galc.dao.jpa.product;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.MissionSpec;
import com.honda.galc.service.Parameters;


/**
 * <h3>Class description</h3>
 * MissionSpec DAO Impl Class.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 2, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140902</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class MissionSpecDaoImpl extends ProductSpecDaoImpl<MissionSpec, String> implements MissionSpecDao {

	private final static String FIND_ALL_BY_PROCESS_POINT_ID = "select s from MissionSpec s, LotControlRule r " +
		"where r.id.processPointId = :processPointId and s.modelYearCode = r.id.modelYearCode and s.modelCode = r.id.modelCode";
	private final static String FIND_ALL_BY_PREFIX = "select s from MissionSpec s where s.productSpecCode like :specCodePrefix";
	private static final String FIND_BY_FILTER_MODEL_YEAR_CODE = "select MODEL_YEAR_CODE, MODEL_CODE,MODEL_YEAR_DESCRIPTION from GALADM.MISSION_SPEC_CODE_TBX  where concat(MODEL_YEAR_CODE, MODEL_CODE) like ?1 or MODEL_YEAR_DESCRIPTION like ?1 group by MODEL_YEAR_CODE, MODEL_CODE,MODEL_YEAR_DESCRIPTION";
	private final String FIND_BY_PRODUCT_ID = "select s from MissionSpec s, Mission p where p.productId = :productId and p.productSpecCode = s.productSpecCode";
	public List<MissionSpec> findAllByProcessPointId(String processPointId) {
		return findAllByQuery(FIND_ALL_BY_PROCESS_POINT_ID, Parameters.with("processPointId", processPointId));
	}

	public List<MissionSpec> findAllProductSpecCodesOnly(String productType) {
    	return findAll();
	}

	public MissionSpec findByProductSpecCode(String productSpecCode, String productType) {
		return findByKey(productSpecCode);
	}

	public List<MissionSpec> findAllByPrefix(String prefix) {
		return findAllByQuery(FIND_ALL_BY_PREFIX, Parameters.with("specCodePrefix", prefix + "%"));
	}
	/**
	 * This Method is used to populate available Mtc model based on filter
	 * 
	 * @return
	 */
	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesByFilter(String filter, String productType) {
		Parameters param = Parameters.with("1", "%" + StringUtils.trimToEmpty(filter) + "%");
		List<QiMtcToEntryModelDto> resultList = findAllByNativeQuery(FIND_BY_FILTER_MODEL_YEAR_CODE, param,QiMtcToEntryModelDto.class);
		return resultList;
	}

	@Override
	public MissionSpec findByProductId(String productId) {
		return  findFirstByQuery(FIND_BY_PRODUCT_ID, Parameters.with("productId", productId));
	}
}
