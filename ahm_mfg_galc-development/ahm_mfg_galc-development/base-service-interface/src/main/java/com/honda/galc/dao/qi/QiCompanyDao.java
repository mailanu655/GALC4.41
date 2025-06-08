package com.honda.galc.dao.qi;

import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Interface description</h3> 
 * <p>
 * <code>QiCompanyDao</code> is a DAO interface to implement database interaction for Company.
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

public interface QiCompanyDao extends IDaoService<QiCompany, String> {
	
	public void updateCompany(String newCompanyName, String oldCompanyName, String companyDescription, int active, String updateUser);

	public void inactivateCompany(String companyName);

}
