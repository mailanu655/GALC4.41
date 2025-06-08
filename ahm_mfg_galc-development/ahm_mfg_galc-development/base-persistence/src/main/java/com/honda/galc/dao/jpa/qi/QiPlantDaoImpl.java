package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiPlantDaoImpl</code> is an implementation class for QiPlantDao interface.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>01/11/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */

public class QiPlantDaoImpl extends BaseDaoImpl<QiPlant, QiPlantId> implements QiPlantDao {

	private final static String UPDATE_SITE_FOR_PLANT = "update galadm.QI_PLANT_TBX set UPDATE_USER=?1, SITE=?2 where  PLANT=?3 AND SITE=?4";
	private final static String FIND_PLANT_NAME  ="select distinct PLANT_NAME from GALADM.GAL128TBX ";
	private final static String FIND_PLANT_BY_SITE ="SELECT TRIM(E.PLANT) FROM GALADM.QI_PLANT_TBX E WHERE E.SITE = ?1 ORDER BY E.PLANT";
	
	private final static String UPDATE_PLANT = "Update QiPlant p set p.plantDesc=:plantDesc, p.active=:active, p.pddaPlantCode=:pddaPlantCode, "
			+ " p.productKind=:productKind, p.updateUser=:updateUser, p.entrySite=:entrySite, p.entryPlant=:entryPlant,"
			+ " p.productLineNo=:productLineNo, p.id.plant=:newPlant, p.pddaLine=:pddaLine WHERE p.id.site = :site AND p.id.plant=:oldPlant";
	
	private final static String FIND_PLANTCODE_PRODUCTKIND = "select distinct HAM_PLANT_CODE, PRODUCT_KIND from GALADM.QI_PLANT_TBX";
	private static final String FIND_ACTIVE_PLANT_BY_SITE = "SELECT TRIM(E.PLANT) FROM GALADM.QI_PLANT_TBX E WHERE E.SITE = ?1 AND E.ACTIVE = 1 ORDER BY E.PLANT";
	private static final String FIND_ALL_ACTIVE_BY_SITE_LIST = "select qiPlant from QiPlant qiPlant where qiPlant.active = 1 and qiPlant.id.site in (:site) order by qiPlant.id.site, qiPlant.id.plant";
	private final static String FIND_ALL_ASSIGNED_PLANT_NAME = "SELECT DISTINCT * FROM ((select distinct TRIM(a.PLANT) from GALADM.QI_PLANT_TBX a "
			+ "join GALADM.QI_RESPONSIBLE_LEVEL_TBX b on a.PLANT = b.PLANT "
			+ "join GALADM.QI_STATION_RESPONSIBILITY_TBX c on b.RESPONSIBLE_LEVEL_ID = c.RESPONSIBLE_LEVEL_ID) "
			+ "UNION ALL (SELECT distinct TRIM(a.PLANT) FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a "
    		+ "JOIN galadm.QI_LOCAL_DEFECT_COMBINATION_TBX b on a.RESPONSIBLE_LEVEL_ID = b.RESPONSIBLE_LEVEL_ID))";
	
	public List<String> findAllPlantList() {
		return findByQuery(FIND_PLANT_NAME,null);
	}
	
	public List<String> findAllPlantBySite(String Site) {
		Parameters params = Parameters.with("1", Site );
		return findAllByNativeQuery(FIND_PLANT_BY_SITE, params, String.class);
	}
	
	@Transactional  
	public void updatePlant(QiPlant newPlant, String oldPlantName) {
		Parameters params = Parameters.with("plantDesc", newPlant.getPlantDesc())
				.put("active", newPlant.getActive())
				.put("pddaPlantCode",newPlant.getPddaPlantCode())
				.put("productKind",newPlant.getProductKind())
				.put("updateUser",newPlant.getUpdateUser())
				.put("entrySite", newPlant.getEntrySite())
				.put("entryPlant", newPlant.getEntryPlant())
				.put("productLineNo", newPlant.getProductLineNo())
				.put("newPlant", newPlant.getId().getPlant())
				.put("site", newPlant.getId().getSite())
				.put("oldPlant", oldPlantName)
				.put("pddaLine", newPlant.getPddaLine());;
		executeUpdate(UPDATE_PLANT, params);
	}
	
	/** This method is used to make plant inactive 
	 * 
	 * @param site
	 */
	@Transactional
	public void inactivatePlant(QiPlantId id) {
		update(Parameters.with("active", 0), Parameters.with("id.site", id.getSite()).put("id.plant",id.getPlant()));
	}

	/** This method is used to update Site name for department 
	 * 
	 * @param updateUser
	 * @param siteName
	 * @param oldPlantId
	 */
	@Transactional
	public void updateSiteById(String updateUser, String siteName, QiPlantId id) {
		Parameters params =Parameters.with("1", updateUser).put("2",siteName ).put("3", id.getPlant()).put("4", id.getSite());
		executeNativeUpdate(UPDATE_SITE_FOR_PLANT, params);
	}

	/** This method is used to return a list of plants in site
	 * 
	 * @param updateUser
	 * @return
	 */
	public List<QiPlant> findAllBySite(String siteName) {
		return findAll(Parameters.with("id.site", siteName),new String[]{"id.plant"});
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findPlantCodeProductKind() {
		return findResultListByNativeQuery(FIND_PLANTCODE_PRODUCTKIND, null);
	}

	
	/** This method is used to return a list of active plants in site
	 * 
	 * @param siteName
	 * @return active plantNames
	 */
	public List<String> findAllActivePlantsBySite(String Site) {
		Parameters params = Parameters.with("1", Site );
		return findAllByNativeQuery(FIND_ACTIVE_PLANT_BY_SITE, params, String.class);
	}
	
	/**
	 * This method is used to find all List of QiPlant by List of Site Names
	 */
	public List<QiPlant> findAllActiveBySiteList(List<String> siteList) {
		return findAllByQuery(FIND_ALL_ACTIVE_BY_SITE_LIST, Parameters.with("site", siteList));
	}
	/** This method is used to return plant for selected pddaPlantCode
	 * 
	 * @param pddaPlantCode
	 * @return Qiplant
	 */
	public QiPlant findByPddaPlantCode(String pddaPlantCode) {
		return findFirst(Parameters.with("pddaPlantCode", pddaPlantCode));
	}

	/** This method is used to return plant for selected pddaPlantCode and production_line_no
	 * 
	 * @param pddaPlantCode
	 * @param production_line_no
	 * @return Qiplant
	 */
	public QiPlant findByPddaPlantCodeAndProdLineNo(String pddaPlantCode, String sLineNo) {
		short  lineNo;
		QiPlant myPlant = null;
		try {
			lineNo = Short.valueOf(sLineNo);
			myPlant = findFirst(Parameters.with("pddaPlantCode", pddaPlantCode).put("productLineNo", lineNo));
		} catch (Exception e) {
			myPlant = null;
		}
		return myPlant;
	}

	public List<String> findAllAssignedPlants() {
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_PLANT_NAME, null,String.class);
	}

}
