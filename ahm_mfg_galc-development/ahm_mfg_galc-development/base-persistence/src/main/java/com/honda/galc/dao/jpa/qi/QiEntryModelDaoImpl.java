package com.honda.galc.dao.jpa.qi;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> QiEntryModelDaoImpl Class is used for Finding the entry models based on product type and Status
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
 */
public class QiEntryModelDaoImpl extends BaseDaoImpl<QiEntryModel,QiEntryModelId> implements QiEntryModelDao{
	
	private final static String FIND_ENTRY_MODEL  ="select DISTINCT e.id.entryModel from QiEntryModel e where e.productType = :productType order by e.id.entryModel";
	private static final String UPDATE_ENTRY_MODEL_NAME = "update GALADM.QI_ENTRY_MODEL_TBX em set em.ENTRY_MODEL = ?1 , em.UPDATE_USER =?2 where em.ENTRY_MODEL = ?3 AND em.IS_USED = ?4";
	private static final String UPDATE_ENTRY_MODEL_VERSION = "update GALADM.QI_ENTRY_MODEL_TBX e set e.IS_USED = ?1 , e.UPDATE_USER =?2 where e.ENTRY_MODEL = ?3 AND e.IS_USED = ?4";
	
	private final  String FIND_ALL_BY_PLANT_AND_PRODUCT_TYPE = "SELECT DISTINCT EMT.ENTRY_MODEL FROM galadm.QI_ENTRY_MODEL_TBX EMT "
			+ " JOIN galadm.QI_ENTRY_SCREEN_TBX ESM ON EMT.ENTRY_MODEL = ESM.ENTRY_MODEL "
			+ " JOIN galadm.QI_ENTRY_SCREEN_DEPT_TBX ESD ON ESM.ENTRY_SCREEN = ESD.ENTRY_SCREEN "
			+ " JOIN galadm.GAL128TBX P ON ESD.DIVISION_ID = P.DIVISION_ID "
			+ " WHERE P.PLANT_NAME = ?1 AND  "
			+ " EMT.PRODUCT_TYPE = ?2 " 
			+ " order by EMT.ENTRY_MODEL";
	
	private static final String FIND_MTC_ASSIGNED_ENTRY_MODEL_BY_PRODUCT_TYPE = "select DISTINCT em.* from galadm.QI_ENTRY_MODEL_TBX em, galadm.QI_ENTRY_MODEL_GROUPING_TBX emg "
			+ " where em.ENTRY_MODEL = emg.ENTRY_MODEL and em.PRODUCT_TYPE = ?1  ORDER by em.ENTRY_MODEL";
	
	/**
	 * This method is used to fetch all the Entry Models based on product type
	 * @param producttype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findEntryModelsByProductType(String productType){
		Parameters params = Parameters.with("productType", productType);
		return findResultListByQuery(FIND_ENTRY_MODEL,params);
	}
	
	/**
	 * This method is used to fetch all the Entry Models based on product type
	 * @param producttype
	 * @return
	 */
	public List<QiEntryModel> findAllByProductType(String selectedProductType) {
		Parameters params = Parameters.with("productType",
				StringUtils.trimToEmpty(selectedProductType));
		return findAll(params, new String[] { "id.entryModel" }, true);
	}

	/**
	 * This method is used to fetch all the Entry Models based on status and product type
	 * @param producttype
	 * @param status
	 * @return
	 */
	public List<QiEntryModel> findAllEntryModelByStatus(String status,String selectedProductType ){
		return findAll(Parameters.with("active", (status.equalsIgnoreCase("Active")) ? (short) 1 : (status.equalsIgnoreCase("Inactive")) ? (short) 0 : (short) 2).put("productType", StringUtils.trimToEmpty(selectedProductType)));
	}

	@Transactional
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName, short isUsed) {
		Parameters params = Parameters.with("1", newEntryModelName)
				.put("2", userId)
				.put("3", oldEntryModelName)
				.put("4", isUsed);
		executeNativeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllByPlantAndProductType(String plant, String selectedProductType) {
		Parameters params = Parameters.with("1", plant).put("2", selectedProductType);
		return findResultListByNativeQuery(FIND_ALL_BY_PLANT_AND_PRODUCT_TYPE, params);
	}
	
	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion, String userId) {
		Parameters params = Parameters.with("1", newVersion)
				.put("2", userId)
				.put("3", entryModel)
				.put("4", oldVersion);
		executeNativeUpdate(UPDATE_ENTRY_MODEL_VERSION, params);
	}
	
	public List<QiEntryModel> findAllByName(String modelName) {
		return findAll(Parameters.with("id.entryModel", modelName));
	}
	
	public int findEntryModelCount(String entryModel) {
		return (int) count(Parameters.with("id.entryModel", entryModel));
	}
	
	/**
	 * This method is used to fetch all the MTC assigned Entry Models based on product type
	 * @param producttype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QiEntryModel> findAllMtcAssignedModelByProductType(String selectedProductType) {
		Parameters params = Parameters.with("1", StringUtils.trimToEmpty(selectedProductType));
		return findAllByNativeQuery(FIND_MTC_ASSIGNED_ENTRY_MODEL_BY_PRODUCT_TYPE, params);
	}
}
