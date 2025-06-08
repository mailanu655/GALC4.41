package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.entity.product.ModelGrouping;
import com.honda.galc.entity.product.ModelGroupingId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> ModelGroupingDaoImpl Class.
 * <h4>Description ModelGroupingDaoImpl class used for Finding/updating the model grouping based on model Group</h4>
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
 * <TD>LnT Infotech</TD>
 * <TD>March. 31, 2017</TD>
 * <TD>1.0</TD>
 * <TD></TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class ModelGroupingDaoImpl extends BaseDaoImpl<ModelGrouping,ModelGroupingId> implements ModelGroupingDao {

	private static final String FIND_ALL_BY_MTC_MODEL_SYSTEM_AND_PRODUCT_TYPE = "SELECT A.* FROM GALADM.MODEL_GROUPING_TBX A JOIN GALADM.MODEL_GROUP_TBX B ON A.MODEL_GROUP = B.MODEL_GROUP WHERE A.MTC_MODEL = ?1 AND A.SYSTEM = ?2 AND B.PRODUCT_TYPE = ?3 ORDER BY A.MODEL_GROUP ASC";

	private static final String UPDATE_MODEL_GROUP_ID = "update ModelGrouping e set e.id.modelGroup=:newModelGroupName, e.id.system=:newModelGroupSystem, e.updateUser=:userId where e.id.modelGroup=:oldModelGroupName and e.id.system=:oldModelGroupSystem";

	private static final String FIND_RECENT_MODEL_GROUPS_BY_SYSTEM = "SELECT DISTINCT MODEL_GROUP FROM GALADM.GAL144TBX G144"
			+ " JOIN GALADM.MODEL_GROUPING_TBX MGT ON MGT.MTC_MODEL = G144.MODEL_YEAR_CODE || G144.MODEL_CODE"
			+ " WHERE MGT.SYSTEM = ?1 AND G144.MODEL_YEAR_DESCRIPTION >= (YEAR(CURRENT_DATE)-1) ORDER BY MODEL_GROUP";


	/**
	 * This method is used to find all the assigned Mtc model to a particular model Group
	 * @param model Group
	 * @return
	 */
	public List<ModelGrouping> findAllByModelGroup(String modelGroup) {
		return findAll(Parameters.with("id.modelGroup",StringUtils.trimToEmpty(modelGroup)));
	}

	/**
	 * This method is used to find all the assigned Mtc model to a particular model Group
	 * @param modelGroup
	 * @param system
	 * @return
	 */
	public List<ModelGrouping> findAllByModelGroupAndSystem(String modelGroup, String system) {
		return findAll(Parameters.with("id.modelGroup",StringUtils.trimToEmpty(modelGroup)).put("id.system", system));
	}

	public List<ModelGrouping> findAllByMtcModelSystemAndProductType(String mtcModel, String system, String productType) {
		Parameters params = Parameters.with("1", mtcModel).put("2", system).put("3", productType);
		List<ModelGrouping> modelGroupings = this.findAllByNativeQuery(FIND_ALL_BY_MTC_MODEL_SYSTEM_AND_PRODUCT_TYPE, params);
		if (modelGroupings == null || modelGroupings.isEmpty()) {
			return null;
		}
		return modelGroupings;
	}

	/**
	 * This method is used to find a QiModelGrouping data based on Mtc Model and System.
	 */
	public ModelGrouping findModelGroupingByMtcModelAndSystem(String mtcModel, String system) {
		return findFirst(Parameters.with("id.mtcModel", StringUtils.trimToEmpty(mtcModel)).put("id.system", system));
	}

	@Transactional
	public void updateModelGroupingId(String newModelGroupName, String newModelGroupSystem, String userId, String oldModelGroupName, String oldModelGroupSystem) {
		Parameters params = Parameters.with("newModelGroupName", newModelGroupName)
				.put("newModelGroupSystem", newModelGroupSystem)
				.put("userId", userId)
				.put("oldModelGroupName", oldModelGroupName)
				.put("oldModelGroupSystem", oldModelGroupSystem);
		executeUpdate(UPDATE_MODEL_GROUP_ID, params);
	}

	public List<String> findRecentModelGroupsBySystem(String system) {
		return findAllByNativeQuery(FIND_RECENT_MODEL_GROUPS_BY_SYSTEM, Parameters.with("1", system), String.class);
	}

}
