package com.honda.galc.dao.jpa.product;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ModelGroupDao;
import com.honda.galc.entity.product.ModelGroup;
import com.honda.galc.entity.product.ModelGroupId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> ModelGroupDaoImpl Class is used for Finding/Updating  the model groups based on product type and Status
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
 * <TD>1.0</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *         March 31, 2017
 */
public class ModelGroupDaoImpl extends BaseDaoImpl<ModelGroup,ModelGroupId> implements ModelGroupDao{

	private final static String FIND_MODEL_GROUP  ="select e.id.modelGroup from ModelGroup e where e.productType =:productType and e.id.system=:system order by e.id.modelGroup";
	private final static String FIND_SYSTEM = "select distinct e.id.system from ModelGroup e order by e.id.system";
	private static final String UPDATE_MODEL_GROUP_ID = "update ModelGroup e set e.id.modelGroup=:newModelGroupName, e.id.system=:newModelGroupSystem, e.updateUser=:updateUser where e.id.modelGroup=:oldModelGroupName and e.id.system=:oldModelGroupSystem";
	
	private final static String SYSTEM_QICS = "QICS";
	
	/**
	 * This method is used to fetch all the Model Groups based on product type
	 * @param producttype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllQicsModelGroupsByProductType(String productType) {
		Parameters params = Parameters.with("productType", productType).put("system", SYSTEM_QICS);
		return findResultListByQuery(FIND_MODEL_GROUP,params);
	}

	/**
	 * This method is used to fetch all the Systems based on product type
	 * @param productType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllSystems() {
		return findResultListByQuery(FIND_SYSTEM, null);
	}

	/**
	 * This method is used to fetch all the Model Groups based on product type
	 * @param producttype
	 * @return
	 */
	public List<ModelGroup> findAllByProductType(String selectedProductType) {
		Parameters params = Parameters.with("productType", StringUtils.trimToEmpty(selectedProductType));
		return findAll(params, new String[] { "id.modelGroup" }, true);
	}

	/**
	 * This method is used to fetch all the Model Groups based on product type
	 * @param producttype
	 * @return
	 */
	public List<ModelGroup> findAllByProductTypeAndSystem(String selectedProductType, String selectedSystem) {
		Parameters params = Parameters.with("productType", StringUtils.trimToEmpty(selectedProductType))
				.put("id.system", selectedSystem);
		return findAll(params, new String[] { "id.modelGroup" }, true);
	}

	/**
	 * This method is used to fetch all the Model Groups based on status and product type
	 * @param producttype
	 * @param status
	 * @return
	 */
	public List<ModelGroup> findAllByStatusAndProductType(String status, String selectedProductType) {
		return findAll(Parameters.with("active", (status.equalsIgnoreCase("Active")) ? (short) 1 : (status.equalsIgnoreCase("Inactive")) ? (short) 0 : (short) 2).put("productType", StringUtils.trimToEmpty(selectedProductType)));
	}

	/**
	 * This method is used to fetch all the Model Groups based on status and product type
	 * @param producttype
	 * @param status
	 * @return
	 */
	public List<ModelGroup> findAllByStatusProductTypeAndSystem(String status, String selectedProductType, String system){
		return findAll(
				Parameters.with("active", (status.equalsIgnoreCase("Active")) ? (short) 1 : (status.equalsIgnoreCase("Inactive")) ? (short) 0 : (short) 2)
				.put("productType", StringUtils.trimToEmpty(selectedProductType))
				.put("id.system", StringUtils.trimToEmpty(system))
				);
	}

	/**
	 * This method is used to update Model Group Id
	 * @param newModelGroupName
	 * @param newModelGroupSystem
	 * @param userId
	 * @param oldModelGroupName
	 * @param oldModelGroupSystem
	 * @return
	 */
	@Transactional
	public void updateModelGroupId(String newModelGroupName, String newModelGroupSystem, String userId, String oldModelGroupName, String oldModelGroupSystem) {
		Parameters params = Parameters.with("newModelGroupName", newModelGroupName)
				.put("newModelGroupSystem", newModelGroupSystem)
				.put("updateUser", userId)
				.put("oldModelGroupName", oldModelGroupName)
				.put("oldModelGroupSystem", oldModelGroupSystem);
		executeUpdate(UPDATE_MODEL_GROUP_ID, params);
	}

}
