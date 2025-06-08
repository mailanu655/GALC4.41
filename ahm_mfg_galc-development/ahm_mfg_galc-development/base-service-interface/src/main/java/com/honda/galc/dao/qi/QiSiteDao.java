package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Interface description</h3> 
 * <p>
 * <code>QiSiteDao</code> is a DAO interface to implement database interaction for Site.
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
 * <TD>15/11/2016</TD>
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

public interface QiSiteDao extends IDaoService<QiSite, String> {
	public void updateSite(String newSiteName, String oldSiteName,  String siteDescription, int active, String updateUser);
	public void updateSiteByCompanyName(String newCompanyName, String updateUser, String oldCompanyName);
	public List<String> findAllSiteName();
	public void inactivateSite(String site);
	public List<QiSite> findAllByCompany(String companyName);
	public List<String> findAllActiveSites();
	public List<QiSite> findAllActive();
	public List<String> findAllAssignedSites();
}


