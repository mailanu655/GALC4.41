package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiEntryModelGroupingId;
import com.honda.galc.service.Parameters;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>Class description</h3> QiEntryModelGroupingDaoImpl Class.
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
public class QiEntryModelGroupingDaoImpl extends BaseDaoImpl<QiEntryModelGrouping,QiEntryModelGroupingId> implements QiEntryModelGroupingDao {

	private static final String UPDATE_ENTRY_MODEL_NAME = "update GALADM.QI_ENTRY_MODEL_GROUPING_TBX e set e.ENTRY_MODEL = ?1 , e.UPDATE_USER =?2 where e.ENTRY_MODEL = ?3 and e.IS_USED=?4";
	private static final String FIND_ALL_MTC_MODEL_BY_ENTRY_MODEL = "SELECT TRIM(emg.id.mtcModel) FROM QiEntryModelGrouping emg, QiEntryModel em "
			+ "WHERE em.id.entryModel = emg.id.entryModel AND em.id.entryModel= :entryModel AND em.productType= :productType";
	private static final String UPDATE_VERSION_VALUE = "update GALADM.QI_ENTRY_MODEL_GROUPING_TBX e set e.IS_USED = ?1 where e.ENTRY_MODEL = ?2 and e.IS_USED=?3";
	private static final String FIND_ASSIGNED_MTC_MODEL_BY_PRODUCT_TYPE = "select emg.* from galadm.QI_ENTRY_MODEL_GROUPING_TBX emg, galadm.QI_ENTRY_MODEL_TBX em "
			+ " where em.ENTRY_MODEL = emg.ENTRY_MODEL and em.PRODUCT_TYPE = ?1";
	private static final String FIND_BY_MTC_PRODUCT_TYPE = "SELECT emg FROM QiEntryModelGrouping emg, QiEntryModel em "
			+ "WHERE em.id.entryModel = emg.id.entryModel AND emg.id.mtcModel= :mtcModel AND em.productType= :productType";
	
	/**
	 * This method is used to find all the assigned Mtc model to a particular Entry Model
	 * @param entryModel
	 * @return
	 */

	public List<QiEntryModelGrouping> findAllByEntryModel(String entryModel,short isUsed) {
		return findAll(Parameters.with("id.entryModel",StringUtils.trimToEmpty(entryModel)).put("id.isUsed", isUsed));

	}

	
	/**
	 * This method is used to find all QiEntryModelGrouping data based on Mtc Model
	 * @return
	 */
	public QiEntryModelGrouping findByMtcModel(String mtcModel, String productType){
    	Parameters params = Parameters.with("mtcModel", StringUtils.trimToEmpty(mtcModel)).put("productType", productType);
    	return findFirstByQuery(FIND_BY_MTC_PRODUCT_TYPE, params);
	}
    
	@Transactional
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName, short isUsed) {
		Parameters params = Parameters.with("1", newEntryModelName)
				.put("2", userId)
				.put("3", oldEntryModelName)
		        .put("4", isUsed);
		executeNativeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	/**
	 * This method is used to find all MtcModel data based on entry model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllMtcModelByEntryModel(String entryModel, String productType) {
		Parameters params = Parameters.with("entryModel", entryModel).put("productType", productType);
		return findResultListByQuery(FIND_ALL_MTC_MODEL_BY_ENTRY_MODEL, params);
	}

	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion) {
		executeNativeUpdate(UPDATE_VERSION_VALUE,Parameters.with("1", newVersion).put("2", entryModel).put("3", oldVersion));
	}
	
	@Transactional
	public void removeByEntryModelAndVersion(String entryModel, short version) {
		delete(Parameters.with("id.entryModel",entryModel).put("id.isUsed", version));
	}
	
	/**
	 * This method is used to find all QiEntryModelGrouping data based on Mtc Model
	 * @return
	 */
	public QiEntryModelGrouping findByMtcModelAndVersion(String mtcModel, short version) {
		return findFirst(Parameters.with("id.mtcModel",StringUtils.trimToEmpty(mtcModel)).put("id.isUsed", version));
	}
	
	/**
	 * This method is used to fetch all the MTC assigned based on product type
	 * @param producttype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QiEntryModelGrouping> findAllAssignedMtcModelByProductType(String productType) {
		Parameters params = Parameters.with("1", StringUtils.trimToEmpty(productType));
		return findAllByNativeQuery(FIND_ASSIGNED_MTC_MODEL_BY_PRODUCT_TYPE, params);
	}
}
