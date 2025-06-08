package com.honda.galc.dao.jpa.qi;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiSiteDaoImpl Class description</h3>
 * <p> QiSiteDaoImpl description </p>
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
 *
 * </TABLE>
 *   
 * @author L&T Infotech<br>
 *
 */

public class QiSiteDaoImpl extends BaseDaoImpl<QiSite, String> implements QiSiteDao {

	private final static String UPDATE_COMPANY_FOR_SITE = "update galadm.QI_SITE_TBX set COMPANY=?1, UPDATE_USER=?2 where COMPANY=?3";
	
	private final static String UPDATE_SITE = "Update galadm.QI_SITE_TBX set SITE_DESCRIPTION=?1, ACTIVE=?2, UPDATE_USER=?3, SITE = ?4 where SITE=?5 ";
	
    private final static String FIND_ALL_SITE_NAME = "select e.site from QiSite e order by e.site";
    
    private final static String FIND_ALL_BY_COMPANY = "select e from QiSite e where e.company = :company order by e.site";
    
    private final static String FIND_ALL_ACTIVE_SITE_NAME = "select e.site from QiSite e where e.active = 1 order by e.site";
    
    private final static String FIND_ALL_ASSIGNED_SITE_NAME = "SELECT DISTINCT * FROM ((select distinct TRIM(a.SITE) from GALADM.QI_SITE_TBX a "
    		+ "join GALADM.QI_RESPONSIBLE_LEVEL_TBX b on a.SITE = b.SITE "
    		+ "join GALADM.QI_STATION_RESPONSIBILITY_TBX c on b.RESPONSIBLE_LEVEL_ID = c.RESPONSIBLE_LEVEL_ID) "
    		+ "UNION ALL (SELECT distinct TRIM(a.SITE) FROM galadm.QI_RESPONSIBLE_LEVEL_TBX a "
    		+ "JOIN galadm.QI_LOCAL_DEFECT_COMBINATION_TBX b on a.RESPONSIBLE_LEVEL_ID = b.RESPONSIBLE_LEVEL_ID))";
    
	@Transactional  
	public void updateSiteByCompanyName(String newCompanyName, String updateUser, String oldCompanyName) {
		Parameters params = Parameters.with("1", newCompanyName).put("2", updateUser).put("3", oldCompanyName);
		executeNativeUpdate(UPDATE_COMPANY_FOR_SITE, params);
	}
	
	@Transactional  
	public void updateSite(String newSite, String oldSite, String siteDescription, int active, String updateUser) {
		Parameters params = Parameters.with("1", siteDescription).put("2", active).put("3", updateUser).put("4", newSite).put("5", oldSite);
		executeNativeUpdate(UPDATE_SITE, params);
		
	}
	
	/**
	 * This method is used to find list of all Site
	 */
	public List<String> findAllSiteName() {
		return findByQuery(FIND_ALL_SITE_NAME, String.class);
	}

	/** This method is used to make site inactive 
	 * 
	 * @param site
	 */
	@Transactional
	public void inactivateSite(String site) {
		update(Parameters.with("active", 0), Parameters.with("site", site));
	}

	public List<QiSite> findAllByCompany(String companyName) {
		return findAllByQuery(FIND_ALL_BY_COMPANY, Parameters.with("company", companyName));
	}

	
	/**
	 * This method is used to find list of all active Sites
	 */
	public List<String> findAllActiveSites() {
		return findByQuery(FIND_ALL_ACTIVE_SITE_NAME, String.class);
	}
	
	/**
	 * This method is used to find all List of active QiSite
	 */
	public List<QiSite> findAllActive() {
		return findAll(Parameters.with("active", 1),new String[]{"site"},true);
	}

	public List<String> findAllAssignedSites() {
		return findAllByNativeQuery(FIND_ALL_ASSIGNED_SITE_NAME, null,String.class);
	}
}
